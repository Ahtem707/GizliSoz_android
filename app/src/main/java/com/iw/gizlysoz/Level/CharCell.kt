package com.iw.gizlysoz.Level

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

class CharCell: FrameLayout {

    private val layout: RoundKeyboardConstants.Layout;
    private val appearance: RoundKeyboardConstants.Appearance;

    private val label = TextView(context)

    private val char: String;

    constructor(
        context: Context,
        char: String,
        layout: RoundKeyboardConstants.Layout,
        appearance: RoundKeyboardConstants.Appearance
    ): super(context) {
        this.layout = layout;
        this.appearance = appearance;
        this.char = char;
        setupLayout();
        setupAppearance();
        setupContent();
    }

    private fun setupLayout() {
        this.layoutParams = ViewGroup.LayoutParams(
            layout.cellSize.toInt(),
            layout.cellSize.toInt()
        )

        label.gravity = Gravity.CENTER;
    }

    private fun setupAppearance() {
        this.setCellBackground(appearance.charCellBgEmpty);
    }

    private fun setupContent() {
        label.text = char;
        label.textSize = appearance.textSize;
        label.setTextColor(appearance.textColor);
        this.addView(label);
    }

    private fun View.setCellBackground(bgColor: Int) {

        val borderWidth = 2;
        val cornerRadius = this.layoutParams.width.toFloat();

        val gradient = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setStroke(borderWidth, bgColor)
            setColor(bgColor)
            this.cornerRadius = cornerRadius
        }
        this.background = gradient
    }
}