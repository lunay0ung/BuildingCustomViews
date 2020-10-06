package com.example.customview1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customview1.clippingCanvas.ClippingActivity
import com.example.customview1.myCanvas.CanvasActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonCanvas.setOnClickListener {
            startActivity(Intent(this, CanvasActivity::class.java))
        }
        buttonClipping.setOnClickListener {
            startActivity(Intent(this, ClippingActivity::class.java))
        }

        buttonRoundRect.setOnClickListener {
            startActivity(Intent(this, RoundRectActivity::class.java))
        }
    }
}