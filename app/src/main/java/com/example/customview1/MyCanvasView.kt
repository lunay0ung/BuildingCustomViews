package com.example.customview1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import androidx.core.content.res.ResourcesCompat


//In order to draw, you need a Paint object that specifies how things are styled when drawn, and a Path that specifies what is being drawn.
//화면에 뭔가 그리기 위해서는 어떻게 그려질지 표현하기 위한 페인트 오브젝트와 뭐가 그려지고 있는지 특정하는 path가 필요하
private const val STROKE_WIDTH = 12f // has to be float

class MyCanvasView(context: Context) : View(context) {
    private var path = Path()
    
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)
    //이전에 그린 것을 캐싱하기 위한 객체
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    //캔버스 배경색
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

    private val paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        isDither = true //when true, affects how colors with higher-precision than the device are down-sampled.
                    // For example, dithering is the most common means of reducing the color range of images down to the 256 (or fewer) colors.
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER, strokeJoin of Paint.Join specifies how lines and curve segments join on a stroked path.
        strokeCap = Paint.Cap.ROUND // default: BUTT, sets the shape of the end of the line to be a cap.
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    //스크린 면적이 바뀔 때 안드로이드 시스템이 호출는 콜백
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (::extraBitmap.isInitialized) extraBitmap.recycle() //화면 사이즈가 바뀌었기 때문에 새로운 bitmap 객체를 생성해야 함 -> 그 전에 이미 사용하던 것을 recycle시킴.  memory leak 방지

        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) //각 색상을 4byte로 저장(권장되는 설정)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    /*
    * Override onDraw() and draw the contents of the cached extraBitmap on the canvas associated with the view. The drawBitmap() Canvas method comes in several versions.
    * In this code, you provide the bitmap, the x and y coordinates (in pixels) of the top left corner, and null for the Paint, as you'll set that later.
    * onDraw() 메소드를 오버라이드 한 후, 캔버스 상에 extraBitmap에 캐시된 내용을 그린다.
    * 캔버스의 drawBitmap() 메소드는 여러 버전으로 제공된다.
    * 이 코드에서는 bitmap과 왼쪽 상단에 x, y 좌표를 제공하고 페인트 값으로는 Null을 설정한 뒤 나중에 세팅해준다.
    * */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }


}