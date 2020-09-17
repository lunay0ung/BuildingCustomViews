package com.example.customview1.clippingCanvas

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.example.customview1.R

//The @JvmOverloads annotation instructs the Kotlin compiler to generate overloads for this function that substitute default parameter values.
class ClippedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
    }

    private val path = Path()

    //variables for dimensions for a clipping rectangle around the whole set of shapes.
    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight) //90dp
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom) //90dp
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)  //0dp
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft) //0dp

    //variables for the inset of a rectangle and the offset of a small rectangle
    private val rectInset = resources.getDimension(R.dimen.rectInset) //8dp
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset) //40dp

    //사각형 안에 그릴 작은 원의 반지름
    private val circleRadius = resources.getDimension(R.dimen.circleRadius)

    //사각형 내부에 위치할 텍스트의 offset과 사이즈
    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)


    /**
     * 직사각형 변수
     * RectF는 부동 소수점 좌표 상 직사각형을 hold하는 클래스이다
     * */
    private var rectF = RectF(
        rectInset, //8dp
        rectInset, //8dp
        clipRectRight - rectInset, //90dp - 8dp
        clipRectBottom - rectInset //90dp - 8dp
    )


    /**
    * 화면에는 사각형이 2열 4행으로 그려지는데 2열에 대한 정보
    * */
    private val columnOne = rectInset
    private val columnTwo = columnOne + rectInset + clipRectRight

    /**
     * 각 행의 좌표
     * */
    private val rowOne = rectInset //8dp
    private val rowTwo = rowOne + rectInset + clipRectBottom
    private val rowThree = rowTwo + rectInset + clipRectBottom
    private val rowFour = rowThree + rectInset + clipRectBottom
    private val textRow = rowFour + (1.5f * clipRectBottom)

    /**
     * In onDraw(), you call methods to draw seven different clipped rectangles as shown in the app screenshot below.
     * The rectangles are all drawn in the same way; the only difference is their defined clipping regions and location on the screen.
     * onDraw()에서 7개의 다른 사각형을 그릴 메소드를 호출한다
     * 이 사각형들은 모두 같은 방식으로 그려지는데, 유일한 차이점은 clipping 지점과 화면 상 위치이다.
     *
     *  When you use View classes provided by the Android system, the system clips views for you to minimize overdraw.
     *  When you use custom View classes and override the onDraw() method, clipping what you draw becomes your responsibility.
     *  안드로이드 시스템에서 제공하는 View 클래스를 이용하면, 시스템 상에서 clip view의 overdraw를 최소화해준다.
     *  그런데 custome view 클래스와 onDraw 메소드를 오버라이드 해서 사용하면 당신이 그리는 것을 clipping하는 것은 당신의 책임이다. 
     * */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackAndUnclippedRectangle(canvas)
        drawDifferenceClippingExample(canvas)
        drawCircularClippingExample(canvas)
        drawIntersectionClippingExample(canvas)
        drawCombinedClippingExample(canvas)
        drawRoundedRectangleClippingExample(canvas)
        drawOutsideClippingExample(canvas)
        drawSkewedTextExample(canvas)
        drawTranslatedTextExample(canvas)
        // drawQuickRejectExample(canvas)
    }
    private fun drawClippedRectangle(canvas: Canvas) {
        canvas.clipRect(
            clipRectLeft,clipRectTop,
            clipRectRight,clipRectBottom
        )
        canvas.drawColor(Color.WHITE)

        paint.color = Color.RED
        canvas.drawLine( //사각형 내부에 빨간 대각선을 그린
            clipRectLeft,clipRectTop,
            clipRectRight,clipRectBottom,paint
        )

        paint.color = Color.GREEN
        canvas.drawCircle(
            circleRadius,clipRectBottom - circleRadius,
            circleRadius,paint
        )

        //사각형 내부 오른쪽 위 모서리에 텍스트를 배치한다
        paint.color = Color.BLUE
        // Align the RIGHT side of the text with the origin.
        paint.textSize = textSize
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            context.getString(R.string.clipping),
            clipRectRight,textOffset,paint
        )
    }

    private fun drawBackAndUnclippedRectangle(canvas: Canvas){
        canvas.drawColor(Color.GRAY)
        canvas.save()
        canvas.translate(columnOne,rowOne)
        drawClippedRectangle(canvas)
        canvas.restore()
    }
    private fun drawDifferenceClippingExample(canvas: Canvas){
        canvas.save() //캔버스를 저장해둔다
        canvas.translate(columnTwo,rowOne) //다음 사각형의 origin을 오른쪽으로 옮긴다
        canvas.clipRect( //frame 생성을 위해 두개의 clipping rectangle의 차이를 이용한다
            2 * rectInset,2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            canvas.clipRect(
                4 * rectInset,4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset,
                Region.Op.DIFFERENCE
            )
        } else {
            canvas.clipOutRect(
                4 * rectInset,4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset
            )
        }
        drawClippedRectangle(canvas)
        canvas.restore()
    }
    private fun drawCircularClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnOne, rowTwo)
        // Clears any lines and curves from the path but unlike reset(),
        // keeps the internal data structure for faster reuse.
        path.rewind() //path가 가지고 있을지도 모를 line과 curve를 비워준다. reset()과 달리 faster use를 위해 내부 데이터는 저장둔다.
        path.addCircle( //
            circleRadius,clipRectBottom - circleRadius,
            circleRadius,Path.Direction.CCW
        )
        // The method clipPath(path, Region.Op.DIFFERENCE) was deprecated in
        // API level 26. The recommended alternative method is
        // clipOutPath(Path), which is currently available in
        // API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas.clipPath(path, Region.Op.DIFFERENCE)
        } else {
            canvas.clipOutPath(path)
        }
        drawClippedRectangle(canvas)
        canvas.restore()
    }
    private fun drawIntersectionClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnTwo, rowTwo)
        canvas.clipRect(
            clipRectLeft, clipRectTop,
            clipRectRight - smallRectOffset, //90dp - 40dp
            clipRectBottom - smallRectOffset //90dp - 40dp
        )
        // The method clipRect(float, float, float, float, Region.Op
        // .INTERSECT) was deprecated in API level 26. The recommended
        // alternative method is clipRect(float, float, float, float), which
        // is currently available in API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight,clipRectBottom,
                Region.Op.INTERSECT
            )
        } else {
            canvas.clipRect(
                clipRectLeft + smallRectOffset, //0dp + 40dp = 40dp
                clipRectTop + smallRectOffset, //0dp + 40dp = 40dp
                clipRectRight, clipRectBottom  //90dp, 90dp
            )
        }
        drawClippedRectangle(canvas)
        canvas.restore()

    }
    private fun drawCombinedClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnOne, rowThree)
        path.rewind()
        path.addCircle(
            clipRectLeft + rectInset + circleRadius,
            clipRectTop + circleRadius + rectInset,
            circleRadius, Path.Direction.CCW ) //CCW: counter-clockwise
        path.addRect(
            clipRectRight / 2 - circleRadius,
            clipRectTop + circleRadius + rectInset,
            clipRectRight / 2 + circleRadius,
            clipRectBottom - rectInset,Path.Direction.CCW
        )
        canvas.clipPath(path)
        drawClippedRectangle(canvas)
        canvas.restore()
    }
    private fun drawRoundedRectangleClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnTwo,rowThree)
        path.rewind()
        /**
         * The addRoundRect() function takes a rectangle, values for the x and y values of the corner radius,
         * and the direction to wind the round-rectangle's contour.
         * Path.Direction specifies how closed shapes (e.g. rects, ovals) are oriented when they are added to a path
         * */
        path.addRoundRect(  //addRoundRect는 직사각형 객체와 모서리 반에 대한 x, y값, 둥근 사각형의 윤곽을 감쌀 direction값을 받는다.
            rectF,clipRectRight / 4,
            clipRectRight / 4, Path.Direction.CCW //Path.Direction은 rects, ovals와 같은 shape이 path에 추가되었을 때 어떤 방향으로 놓일지 특정하는 값이다
        )
        canvas.clipPath(path)
        drawClippedRectangle(canvas)
        canvas.restore()
    }
    private fun drawOutsideClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnOne,rowFour)
        canvas.clipRect(2 * rectInset,2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset)
        drawClippedRectangle(canvas)
        canvas.restore()
    }
    private fun drawTranslatedTextExample(canvas: Canvas){
        canvas.save()
        paint.color = Color.GREEN
        // Align the RIGHT side of the text with the origin.
        paint.textAlign = Paint.Align.LEFT
        // Apply transformation to canvas.
        canvas.translate(columnTwo,textRow)
        // Draw text.
        canvas.drawText(context.getString(R.string.translated),
            clipRectLeft,clipRectTop,paint)
        canvas.restore()
    }
    private fun drawSkewedTextExample(canvas: Canvas){
        canvas.save()
        paint.color = Color.YELLOW
        paint.textAlign = Paint.Align.RIGHT
        // Position text.
        canvas.translate(columnTwo, textRow)
        // Apply skew transformation.
        canvas.skew(0.2f, 0.3f)
        canvas.drawText(context.getString(R.string.skewed),
            clipRectLeft, clipRectTop, paint)
        canvas.restore()
    }
    private fun drawQuickRejectExample(canvas: Canvas){
    }
}