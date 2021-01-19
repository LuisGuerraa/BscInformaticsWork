package com.example.drag

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.drag.game.data.Drawing

class DragGameView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val filledBlack = ink(Color.BLACK, Paint.Style.FILL_AND_STROKE)
    private val borderBlack = ink(Color.BLACK, Paint.Style.STROKE)


    var drawing: Drawing? = null

    fun triggerView(drawing: Drawing?) {
        this.drawing = drawing
        invalidate()
    }


    private fun ink(color: Int, style: Paint.Style): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = style
        paint.strokeWidth = 5.0F
        return paint
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)

        drawing?.forEach{
            canvas.drawCircle(it.x * width, it.y * height, 10F ,filledBlack)
        }

    }

    private fun drawBorder(canvas: Canvas) =
            canvas.drawRect(0.0F, 0.0F, width.toFloat() - 1, height.toFloat() - 1, borderBlack)
    private fun Float.percent(pc: Int) = this / 100 * pc
}

