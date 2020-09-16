package com.example.customview1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.core.content.res.ResourcesCompat

class MyCanvasView(context: Context) : View(context) {

    //이전에 그린 것을 캐싱하기 위한 객체
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    //캔버스 배경색
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

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