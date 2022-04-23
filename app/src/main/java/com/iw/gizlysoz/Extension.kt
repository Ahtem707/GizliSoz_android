package com.iw.gizlysoz;

import androidx.core.content.ContextCompat
import android.content.Context
import android.util.TypedValue

fun Int.toColor(self: Context): Int {
    return ContextCompat.getColor(self, this)
}

fun Int.dpToPixels(context: Context): Int {
    val value = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics)
    return value.toInt()
}