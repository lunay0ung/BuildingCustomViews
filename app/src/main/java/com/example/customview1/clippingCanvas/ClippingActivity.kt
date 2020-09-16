package com.example.customview1.clippingCanvas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customview1.R

class ClippingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_clipping)
        setContentView(ClippedView(this))


    }
}