package com.example.customview1

import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class RoundRectActivity : AppCompatActivity() {

    private val paint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private var rect = RectF()
    var corners = floatArrayOf(80f, 80f, 80f, 80f, 80f, 80f, 80f, 80f)

    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_rect)

        val imageView =
            findViewById<View>(R.id.image) as ImageView
        val width = resources.getDimensionPixelSize(R.dimen.clip_rect_width)
        val height = resources.getDimensionPixelSize(R.dimen.clip_rect_height)

        bitmap = createClipRect(width, height)
        imageView.setImageBitmap(bitmap)
    }

    // Assumes width > height
    private fun createClipRect(width: Int, height: Int): Bitmap {
        paint.color = Color.GREEN
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val radius = height / 2.toFloat()

        // Top left radius in px
        // Top right radius in px
        // Bottom right radius in px
        // Bottom left radius in px
        rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.save()
        val path = Path()
        path.addRoundRect(rect, corners, Path.Direction.CW)
        canvas.drawPath(path, paint)
        canvas.restore()

        /* canvas.save();
    canvas.clipRect(0, 0, width, height);
    rect.set(0, 0, width + height, height);
    canvas.drawRoundRect(rect, radius, radius, paint);*/
        // canvas.restore();
        return bitmap
    }

}