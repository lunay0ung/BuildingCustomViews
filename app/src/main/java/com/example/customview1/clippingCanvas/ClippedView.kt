package com.example.customview1.clippingCanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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
    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)

    //variables for the inset of a rectangle and the offset of a small rectangle
    private val rectInset = resources.getDimension(R.dimen.rectInset)
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset)

    //사각형 안에 그릴 작은 원의 반지름
    private val circleRadius = resources.getDimension(R.dimen.circleRadius)

    //사각형 내부에 위치할 텍스트의 offset과 사이즈
    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)

    /**
    * 화면에는 사각형이 2열 4행으로 그려지는데 2열에 대한 정보
    * */
    private val columnOne = rectInset
    private val columnTwo = columnOne + rectInset + clipRectRight

    /**
     * 각 행의 좌표
     * */
    private val rowOne = rectInset
    private val rowTwo = rowOne + rectInset + clipRectBottom
    private val rowThree = rowTwo + rectInset + clipRectBottom
    private val rowFour = rowThree + rectInset + clipRectBottom
    private val textRow = rowFour + (1.5f * clipRectBottom)

    /**
     * In onDraw(), you call methods to draw seven different clipped rectangles as shown in the app screenshot below.
     * The rectangles are all drawn in the same way; the only difference is their defined clipping regions and location on the screen.
     * onDraw()에서 7개의 다른 사각형을 그릴 메소드를 호출한다
     * 이 사각형들은 모두 같은 방식으로 그려지는데, 유일한 차이점은 clipping 지점과 화면 상 위치이다.
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
    }
    private fun drawCircularClippingExample(canvas: Canvas){
    }
    private fun drawIntersectionClippingExample(canvas: Canvas){
    }
    private fun drawCombinedClippingExample(canvas: Canvas){
    }
    private fun drawRoundedRectangleClippingExample(canvas: Canvas){
    }
    private fun drawOutsideClippingExample(canvas: Canvas){
    }
    private fun drawTranslatedTextExample(canvas: Canvas){
    }
    private fun drawSkewedTextExample(canvas: Canvas){
    }
    private fun drawQuickRejectExample(canvas: Canvas){
    }
}