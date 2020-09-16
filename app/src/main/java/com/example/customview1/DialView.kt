package com.example.customview1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

//https://codelabs.developers.google.com/codelabs/advanced-andoid-kotlin-training-custom-views/#4

//this enum is of type Int because the values are string resources rather than actual strings.
private enum class FanSpeed(val label: Int) {
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high);
}

private const val RADIUS_OFFSET_LABEL = 30
private const val RADIUS_OFFSET_INDICATOR = -35

//The @JvmOverloads annotation instructs the Kotlin compiler to generate overloads for this function that substitute default parameter values.
class DialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /*
    * 아래의 값들은 뷰의 드로잉 과정을 보다 최대한 빠르게 하기 위
    * 뷰가 실제로 화면에 그려지기 전에 초기화된다.
    * */
    private var radius = 0.0f //원의 반지름
    private var fanSpeed = FanSpeed.OFF
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }


    /*
    * The onSizeChanged() method is called any time the view's size changes, including the first time it is drawn when the layout is inflated.
    * Override onSizeChanged() to calculate
    * positions, dimensions, and any other values related to your custom view's size, instead of recalculating them every time you draw.
    *
    * onSizeChanged() 메소드는 레이아웃이 인플레이트되면서 뷰가 처음으로 화면에 그려질 때를 포함해서 뷰의 사이즈가 바뀔 때마다 호출된다.
    * 이 메소드를 오버라이드 하는 이유는
    * 커스텀뷰와 관련한 positions, dimensions, and any other values들을 계산하기 위해서이다(매번 화면에 그릴 때마다 다시 계산하는 대신).
    * */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(width, height) / 2.0 * 0.8).toFloat()
    }

    /*
    * This extension function on the PointF class calculates the X, Y coordinates on the screen for the text label and current indicator (0, 1, 2, or 3),
    * given the current FanSpeed position and radius of the dial. You'll use this in onDraw().
    *
    * 이 PointF 확장함수는 텍스트 레이블과 현재의 indicator를 위해 현재의 팬스피드 포지션과 다이얼의 반지름을 고려하여 스크린의 X, Y 좌표을 계산한다.
    * onDraw()에서 사용될 것이다.
    * */
    private fun PointF.computeXYForSpeed(pos: FanSpeed, radius: Float) {
        // Angles are in radians.
        val startAngle = Math.PI * (9/8.0)
        val angle = startAngle + pos.ordinal * (Math.PI / 4)
        x = (radius * cos(angle)).toFloat() + width / 2
        y = (radius * sin(angle)).toFloat() + height / 2
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Set dial background color to green if selection not off.
        paint.color = if (fanSpeed == FanSpeed.OFF) Color.GRAY else Color.GREEN

        // Draw the dial.
        /*
        * This method uses the current view width and height to find the center of the circle, the radius of the circle, and the current paint color.
        * The width and height properties are members of the View superclass and indicate the current dimensions of the view.
        *
        * 아래의 메소드는 원의 중심, 반지름, 현재 페인트 컬러를 파악하기 위해 현재 뷰의 넓이와 높이를 이용한다.
        * 넓이와 높이 속성은 뷰 슈퍼클래스의 멤버이며 현재 뷰의 면적을 가리킨다.
        * */
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)

        // Draw the indicator circle.
        /*
        *  code to draw a smaller circle for the fan speed indicator mark, also with the drawCircle() method
        * This part uses the PointF.computeXYforSpeed() extension method to calculate the X,Y coordinates for the indicator center based on the current fan speed.
        *
        * 팬 스피드를 표시하기 위해 더 작은 원을 그리는 코드이다.
        *  이 부분에서는 현재의 팬스피드를 기반으로 indicator의 center X, Y 좌표값을 계산하기 위해  PointF.computeXYforSpeed() 확장함수를 사용한다.
        * */
        val markerRadius = radius + RADIUS_OFFSET_INDICATOR
        pointPosition.computeXYForSpeed(fanSpeed, markerRadius)
        paint.color = Color.BLACK
        canvas.drawCircle(pointPosition.x, pointPosition.y, radius/12, paint)

        // Draw the text labels.
        /*
        * Finally, draw the fan speed labels (0, 1, 2, 3) at the appropriate positions around the dial.
        * This part of the method calls PointF.computeXYForSpeed() again to get the position for each label,
        * and reuses the pointPosition object each time to avoid allocations. Use drawText() to draw the labels.
        *
        * 마지막으로 팬의 스피드 레이블(0, 1, 2, 3)을 다이얼 주변의 적절한 위치에 그린다.
        * 이 부분에서 각 레이블의 위치를 구하기 위해 PointF.computeXYForSpeed() 메소드를 다시 한번 사용하며
        * 매번 메모리가 allocation되는 것을 피하기 위해 pointPosition 객체값을 사용한다.
        * 레이블을 그리기 위해 drawText() 메소드를 사용한다.
        * */
        val labelRadius = radius + RADIUS_OFFSET_LABEL
        for (i in FanSpeed.values()) {
            pointPosition.computeXYForSpeed(i, labelRadius)
            val label = resources.getString(i.label)
            canvas.drawText(label, pointPosition.x, pointPosition.y, paint)
        }
    }



}