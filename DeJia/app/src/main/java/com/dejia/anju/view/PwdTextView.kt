package com.dejia.anju.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class PwdTextView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context!!, attrs, defStyleAttr) {
    private var radius = 0f
    private var hasPwd = false
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (hasPwd) {
            // 画一个黑色的圆
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = Color.BLACK
            paint.style = Paint.Style.FILL
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
        }
    }

    fun clearPwd() {
        hasPwd = false
        invalidate()
    }

    fun drawPwd(radius: Float) {
        hasPwd = true
        if (radius == 0f) {
            this.radius = (width / 4).toFloat()
        } else {
            this.radius = radius
        }
        invalidate()
    }
}