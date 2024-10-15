package com.sgalera.gaztelubira.ui.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.sgalera.gaztelubira.R

class LowerDiagonalView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.red_vs) // Fondo azul
        isAntiAlias = true
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        path.reset()

        path.moveTo(width, 600f)
        path.lineTo(width, height)
        path.lineTo(-600f, height)
        path.close()

        canvas.drawPath(path, paint)
    }
}