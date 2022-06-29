package com.iw.gizlysoz.Extension

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.LinearLayout

open class Layouts(context: Context) {
    val wrap = ViewGroup.LayoutParams.WRAP_CONTENT
    val parent = ViewGroup.LayoutParams.MATCH_PARENT
}

fun makeFrameLayout(): FrameLayout.LayoutParams {
    val wrap = FrameLayout.LayoutParams.WRAP_CONTENT
    return FrameLayout.LayoutParams(wrap, wrap)
}

fun makeLayout(): ViewGroup.LayoutParams {
    val wrap = ViewGroup.LayoutParams.WRAP_CONTENT
    return ViewGroup.LayoutParams(wrap, wrap)
}

fun makeMarginLayout(): ViewGroup.MarginLayoutParams {
    val wrap = ViewGroup.MarginLayoutParams.WRAP_CONTENT
    return ViewGroup.MarginLayoutParams(wrap, wrap)
}

fun makeGridLayout(): GridLayout.LayoutParams {
    val wrap = GridLayout.LayoutParams.WRAP_CONTENT
    return GridLayout.LayoutParams().size(wrap)
}

fun makeLinearLayout(): LinearLayout.LayoutParams {
    val wrap = LinearLayout.LayoutParams.WRAP_CONTENT
    return LinearLayout.LayoutParams(wrap,wrap)
}

fun <T>T.size(value: Int): T where T: ViewGroup.LayoutParams {
    this.width = value
    this.height = value
    return this as T
}

fun <T>T.width(value: Int): T where T: ViewGroup.LayoutParams {
    this.width = value
    return this
}

fun <T>T.height(value: Int): T where T: ViewGroup.LayoutParams {
    this.height = value
    return this
}

fun <T>T.top(value: Int): T where T: ViewGroup.MarginLayoutParams {
    this.topMargin = value
    return this
}

fun <T>T.bottom(value: Int): T where T: ViewGroup.MarginLayoutParams {
    this.bottomMargin = value
    return this
}

fun <T>T.left(value: Int): T where T: ViewGroup.MarginLayoutParams {
    this.leftMargin = value
    return this
}

fun <T>T.right(value: Int): T where T: ViewGroup.MarginLayoutParams {
    this.rightMargin = value
    return this
}

fun <T>T.vertical(value: Int): T where T: ViewGroup.MarginLayoutParams {
    this.topMargin = value
    this.bottomMargin = value
    return this
}

fun <T>T.horizontal(value: Int): T where T: ViewGroup.MarginLayoutParams {
    this.leftMargin = value
    this.rightMargin = value
    return this
}

fun <T>T.all(value: Int): T where T: ViewGroup.MarginLayoutParams {
    this.topMargin = value
    this.bottomMargin = value
    this.leftMargin = value
    this.rightMargin = value
    return this
}

fun <T>T.gravity(value: Int): T where T: LinearLayout.LayoutParams {
    this.gravity = value
    return this
}

fun <T>T.gravity(value: Int): T where T: GridLayout.LayoutParams {
    this.setGravity(value)
    return this
}