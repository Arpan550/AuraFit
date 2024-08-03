package com.example.aurafit.ui.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ProgressCircle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint()
    private var progress = 0
    private var borderColor = Color.GREEN

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        paint.isAntiAlias = true
    }

    fun setProgress(progress: Int, borderColor: Int) {
        this.progress = progress
        this.borderColor = borderColor
        invalidate()
    }

    fun setBorderColor(color: Int) {
        this.borderColor = color
        invalidate()
    }

    fun getCurrentBorderColor(): Int {
        return borderColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = (width / 2).toFloat()
        paint.color = borderColor
        canvas.drawCircle(radius, radius, radius - paint.strokeWidth / 2, paint)
        paint.color = Color.RED
        val sweepAngle = (progress / 100f) * 360
        canvas.drawArc(
            paint.strokeWidth / 2, paint.strokeWidth / 2,
            width - paint.strokeWidth / 2, height - paint.strokeWidth / 2,
            -90f, sweepAngle, false, paint
        )
    }
}
