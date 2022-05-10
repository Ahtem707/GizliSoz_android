package com.iw.gizlysoz.Extension

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

data class LinePosition(val sX: Float, val sY: Float,
                        val eX: Float, val eY: Float) {}

class DrawView : View {

    var paint: Paint = Paint();
    var points: ArrayList<LinePosition> = ArrayList();

    constructor(context: Context?, points: ArrayList<LinePosition>, color: Int) : super(context) {
        this.points = points;
        paint.setColor(color)
        paint.strokeWidth = 30f;
    }

    override fun onDraw(canvas: Canvas) {
        points.forEach {
            canvas.drawLine(it.sX, it.sY, it.eX, it.eY, paint);
        }
    }
}