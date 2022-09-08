package com.iw.gizlysoz;

import androidx.core.content.ContextCompat
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Int.dpToPixels(context: Context): Int {
    val value = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics)
    return value.toInt()
}

fun showSystemUI(window: Window, view: View) {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.systemBars())
}

fun roundedCornersDrawable(
    borderWidth: Int = 10, // border width in pixels
    borderColor: Int = Color.BLACK, // border color
    cornerRadius: Float = 25F, // corner radius in pixels
    bgColor: Int = Color.TRANSPARENT // view background color
): Drawable {
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setStroke(borderWidth, borderColor)
        setColor(bgColor)
        // make it rounded corners
        this.cornerRadius = cornerRadius
    }
}

