package com.example.welltrackapplicationassignment2.SupplementalActivities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.welltrackapplicationassignment2.R

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0f // Progress percentage (0-100)
    private var max = 100f // Max progress
    private var strokeWidth = 20f // Thickness of the circle

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.progress_background)
        style = Paint.Style.STROKE
        strokeWidth = this@CircularProgressBar.strokeWidth
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.progress_foreground)
        style = Paint.Style.STROKE
        strokeWidth = this@CircularProgressBar.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width - strokeWidth) / 2f

        // Draw background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        // Draw progress circle
        val sweepAngle = (progress / max) * 360f
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f,
            sweepAngle,
            false,
            progressPaint
        )
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, max)
        invalidate() // Redraw the view
    }
}
