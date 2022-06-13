package com.iw.gizlysoz.Level

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

class CharCell: FrameLayout {

    interface Layout {
        val cellSize: Float
    }

    interface Appearance {
        val charCellBgHidden: Int
        val charCellBgEmpty: Int
        val charCellBgFill: Int
        val charCellBgSelected: Int
        val charCellTextSize: Float
        val charCellTextColor: Int
        val charCellCornerRadius: Float
    }

    enum class CellState {
        hidden,
        empty,
        fill,
        select
    }

    private val layout: Layout
    private val appearance: Appearance

    private val label = TextView(context)

    private val char: String

    constructor(
        context: Context,
        char: String,
        layout: Layout,
        appearance: Appearance
    ): super(context) {
        this.layout = layout
        this.appearance = appearance
        this.char = char
        setupSubview()
        setupLayout()
        setupAppearance()
        setupContent()
    }

    // MARK - Private
    private fun setupSubview() {
        this.addView(label)
    }

    private fun setupLayout() {
        this.layoutParams = ViewGroup.LayoutParams(
            layout.cellSize.toInt(),
            layout.cellSize.toInt()
        )

        label.gravity = Gravity.CENTER;
    }

    private fun setupAppearance() {
        this.setCellBackground(appearance.charCellBgEmpty)
        label.textSize = appearance.charCellTextSize
        label.setTextColor(appearance.charCellTextColor)
    }

    private fun setupContent() {
        label.text = char
    }

    private fun View.setCellBackground(bgColor: Int) {

        val borderWidth = 2
        val cornerRadius = this.layoutParams.width.toFloat()

        val gradient = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setStroke(borderWidth, bgColor)
            setColor(bgColor)
            if(appearance.charCellCornerRadius == Float.MAX_VALUE) {
                this.cornerRadius = cornerRadius
            } else {
                this.cornerRadius = appearance.charCellCornerRadius
            }
        }
        this.background = gradient
    }

    // MARK - Public
    fun setState(state: CellState) {
        val color = when(state) {
            CellState.hidden -> appearance.charCellBgHidden
            CellState.empty -> appearance.charCellBgEmpty
            CellState.fill -> appearance.charCellBgFill
            CellState.select -> appearance.charCellBgSelected
        }
        this.setCellBackground(color)
        if(state == CellState.empty) label.text = ""
    }

    fun setText(text: String) {
        label.text = text
    }
}