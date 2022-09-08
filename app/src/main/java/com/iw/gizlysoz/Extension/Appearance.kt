package com.iw.gizlysoz.Extension

import android.content.Context
import androidx.core.content.ContextCompat
import com.iw.gizlysoz.R

abstract class Appearance(val context: Context) {

    val red = getColor(R.color.red)
    val yellow = getColor(R.color.yellow)
    val green = getColor(R.color.green)
    val blue = getColor(R.color.lineColor)

    private fun getColor(color: Int): Int {
        return ContextCompat.getColor(context, color)
    }
}