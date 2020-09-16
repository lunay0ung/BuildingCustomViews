package com.example.customview1.clippingCanvas

import android.content.Context
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

}