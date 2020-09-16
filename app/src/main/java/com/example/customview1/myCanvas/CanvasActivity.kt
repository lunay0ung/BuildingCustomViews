package com.example.customview1.myCanvas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import com.example.customview1.R

class CanvasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 커스텀뷰를 코드 상으로 생성시키므 xml 파일은 필요 없다.
        val myCanvasView = MyCanvasView(this)
        myCanvasView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        myCanvasView.contentDescription = getString(R.string.canvasContentDescription)
        setContentView(myCanvasView)
        //드로잉을 위해서는 뷰의 사이즈를 알아야 하지만 onCreate()에서는 알 수 없다. 이 시점에서 아직 사이즈는 결정되지 않았기 때문이다.


    }
}