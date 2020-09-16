package com.example.customview1.myCanvas

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.example.customview1.R


//In order to draw, you need a Paint object that specifies how things are styled when drawn, and a Path that specifies what is being drawn.
//화면에 뭔가 그리기 위해서는 어떻게 그려질지 표현하기 위한 페인트 오브젝트와 뭐가 그려지고 있는지 특정하는 path가 필요하
private const val STROKE_WIDTH = 12f // has to be float

class MyCanvasView(context: Context) : View(context) {
    private var path = Path()

    private val drawColor = ResourcesCompat.getColor(resources,
        R.color.colorPaint, null)
    //이전에 그린 것을 캐싱하기 위한 객체
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    //캔버스 배경색
    private val backgroundColor = ResourcesCompat.getColor(resources,
        R.color.colorBackground, null)

    private lateinit var frame: Rect //캔버스 테두리 그리기


    /**
     * Using a path, there is no need to draw every pixel and each time request a refresh of the display.
     * Instead, you can (and will) interpolate a path between points for much better performance.
     *
     * path를 이용하면 모든 픽셀을 그릴 필요가 없다.
     * 대신 훨씬 나은 퍼포먼스를 위 포인트 간 거리를 채우면 된다.해
     *
     * Don't draw every single pixel.
     * If the finger has has moved less than this distance, don't draw. scaledTouchSlop, returns
     * the distance in pixels a touch can wander before we think the user is scrolling.
     *
     * 모든 픽셀을 다 그리지 않는다.
     * 만약 손가락이 아래 값보다 적게 움직였다면 그리지 않는다.
     * scaledTouchSlop은 유저가 뭔가 스크롤링 하고 있다고 생각하기 전에 터칭으로 움직일 수 있는 거리를 픽셀 값으로 반한다.
     */
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private var currentX = 0f
    private var currentY = 0f

    //현재 터치 중인 x, y 좌표를 캐싱하기 위한 변수
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f


    private val paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        isDither = true //when true, affects how colors with higher-precision than the device are down-sampled.
                    // For example, dithering is the most common means of reducing the color range of images down to the 256 (or fewer) colors.
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER, strokeJoin of Paint.Join specifies how lines and curve segments join on a stroked path.
        strokeCap = Paint.Cap.ROUND // default: BUTT, sets the shape of the end of the line to be a cap.
        strokeWidth =
            STROKE_WIDTH // default: Hairline-width (really thin)
    }

    //스크린 면적이 바뀔 때 안드로이드 시스템이 호출는 콜백
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (::extraBitmap.isInitialized) extraBitmap.recycle() //화면 사이즈가 바뀌었기 때문에 새로운 bitmap 객체를 생성해야 함 -> 그 전에 이미 사용하던 것을 recycle시킴.  memory leak 방지

        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) //각 색상을 4byte로 저장(권장되는 설정)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        // Calculate a rectangular frame around the picture.
        val inset = 40
        frame = Rect(inset, inset, width - inset, height - inset)
    }

    /**
    * Override onDraw() and draw the contents of the cached extraBitmap on the canvas associated with the view. The drawBitmap() Canvas method comes in several versions.
    * In this code, you provide the bitmap, the x and y coordinates (in pixels) of the top left corner, and null for the Paint, as you'll set that later.
    * onDraw() 메소드를 오버라이드 한 후, 캔버스 상에 extraBitmap에 캐시된 내용을 그린다.
    * 캔버스의 drawBitmap() 메소드는 여러 버전으로 제공된다.
    * 이 코드에서는 bitmap과 왼쪽 상단에 x, y 좌표를 제공하고 페인트 값으로는 Null을 설정한 뒤 나중에 세팅해준다.
    * */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        // Draw a frame around the canvas.
        canvas.drawRect(frame, paint)
    }

    /**
     *  MyCanvasView#performClick을 호출하고 구현할 필요가 없다. MyCanvasView 커스텀뷰는 클릭 액션을 다루지 않기 때문이다.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    /**
     * The following methods factor out what happens for different touch events,
     * as determined by the onTouchEvent() when statement.
     * This keeps the when conditional block
     * concise and makes it easier to change what happens for each event.
     * No need to call invalidate because we are not drawing anything.
     * 아래의 메소드들은 각기 다른 터치 이밴트에 무슨 일이 발생하고 있는지 가려낸다.
     * 아무 것도 그려내고 있지 않기 때문에 invalidate()를 호출할 필요는 없다.
     *
     */
    private fun touchStart() {
        path.reset() //path를 리셋한다.
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    /**
     *  Calculate the traveled distance (dx, dy), create a curve between the two points and store it in path,
     *  update the running currentX and currentY tally, and draw the path.
     *  Then call invalidate() to force redrawing of the screen with the updated path.
     *  dx, dy 좌표를 통해 이동한 거리를 측정한  두 지점 간 curve를 그리고 path에 저장한다.
     *  또한 현재 이동 중인 currentX, currentY를 업데이트하고 path를 그린다.
     *  그 다음 업뎃된 path에 따라 스크린에 리드로잉하기 위해 invalidate()를 호출한다.
    * */
    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            //Using quadTo() instead of lineTo() create a smoothly drawn line without corners.
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to save it.
            extraCanvas.drawPath(path, paint)
        }
        // Invalidate() is inside the touchMove() under ACTION_MOVE because there are many other
        // types of motion events passed into this listener, and we don't want to invalidate the
        // view for those.
        invalidate()
    }

    private fun touchUp() {
        // Reset the path so it doesn't get drawn again.
        path.reset()
    }

}