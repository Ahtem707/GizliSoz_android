package com.iw.gizlysoz.Extension

import android.content.Context
import androidx.core.content.ContextCompat
import com.iw.gizlysoz.R

open class Appearances(context: Context) {
    val red = ContextCompat.getColor(context, R.color.red)
    val yellow = ContextCompat.getColor(context, R.color.yellow)
    val green = ContextCompat.getColor(context, R.color.green)
    val blue = ContextCompat.getColor(context, R.color.lineColor)
}