package com.iw.gizlysoz.Extension

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

abstract class Layout(val context: Context) {
    val wrap = ViewGroup.LayoutParams.WRAP_CONTENT
    val parent = ViewGroup.LayoutParams.MATCH_PARENT
}

fun makeLayout(): ViewGroup.LayoutParams {
    val wrap = ViewGroup.LayoutParams.WRAP_CONTENT
    return ViewGroup.LayoutParams(wrap, wrap)
}

fun makeFrameLayout(): FrameLayout.LayoutParams {
    val wrap = FrameLayout.LayoutParams.WRAP_CONTENT
    return FrameLayout.LayoutParams(wrap, wrap)
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

fun <T>T.gravity(value: Int): T where T: FrameLayout.LayoutParams {
    this.gravity = value
    return this
}

fun ConstraintLayout.makeConstrains(): ConstraintSet {
    return ConstraintSet()
}

fun ConstraintSet.pinSize(view: View, value: Int): ConstraintSet {
    this.constrainWidth(view.id, value)
    this.constrainHeight(view.id, value)
    return this
}

fun ConstraintSet.pinWidth(view: View, value: Int): ConstraintSet {
    this.constrainWidth(view.id, value)
    return this
}

fun ConstraintSet.pinHeight(view: View, value: Int): ConstraintSet {
    this.constrainHeight(view.id, value)
    return this
}

fun ConstraintSet.pinTopToSuperview(view: View, value: Int): ConstraintSet {
    this.connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, value)
    return this
}

fun ConstraintSet.pinBottomToSuperview(view: View, value: Int): ConstraintSet {
    this.connect(view.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, value)
    return this
}

fun ConstraintSet.pinHCenterToSuperview(view: View, value: Int): ConstraintSet {
    this.centerHorizontally(view.id, ConstraintSet.PARENT_ID)
    return this
}

fun ConstraintSet.pinVCenterToSuperview(view: View, value: Int): ConstraintSet {
    this.centerVertically(view.id, ConstraintSet.PARENT_ID)
    return this
}

fun ConstraintSet.pinCenterToSuperview(view: View, value: Int): ConstraintSet {
    this.centerHorizontally(view.id, ConstraintSet.PARENT_ID)
    this.centerVertically(view.id, ConstraintSet.PARENT_ID)
    return this
}

fun ConstraintSet.apply(constraintLayout: ConstraintLayout) {
    this.applyTo(constraintLayout)
}

