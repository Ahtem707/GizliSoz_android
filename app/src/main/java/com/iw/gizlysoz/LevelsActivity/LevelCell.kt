package com.iw.gizlysoz.LevelsActivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.iw.gizlysoz.Extension.makeFrameLayout
import com.iw.gizlysoz.Extension.makeLayout
import com.iw.gizlysoz.Extension.size
import com.iw.gizlysoz.R
import com.iw.gizlysoz.roundedCornersDrawable

interface LevelCellInterface {

    data class Input(
        val context: Context,
        val layout: Layout,
        val appearance: Appearance,
        var number: Int = 0,
        var isLock: Boolean = false,
        var delegate: LevelCellDelegate
    )

    interface Layout {
        val cellSize: Int
        val cellButtonSize: Int
    }

    interface Appearance {
        val cellBackground: Int
        val cellTextFont: Typeface
        val cellTextSize: Float
        val cellTextColor: Int
        val cellLockIcon: Drawable?
    }

    interface LevelCellDelegate {
        fun onClick(index: Int)
    }
}

@SuppressLint("ViewConstructor")
class LevelCell (
    val input: LevelCellInterface.Input
    ): FrameLayout(input.context) {

    init {
        println("myLogA: " + input)
        setupSubview()
        setupLayout()
        setupAppearance()
        setupContent()
    }

    private lateinit var button: Button

    private fun setupSubview() {
        button = Button(input.context)
        button.setOnClickListener {
            val index = it.tag as Int
            input.delegate.onClick(index)
        }
    }

    private fun setupLayout() {
        layoutParams = makeFrameLayout().size(input.layout.cellSize)
        button.layoutParams = makeLayout().size(input.layout.cellButtonSize)

        addView(button)
    }

    private fun setupAppearance() {
        this.setBackgroundColor(input.appearance.cellBackground)
        button.typeface = input.appearance.cellTextFont
        button.textSize = input.appearance.cellTextSize
        button.setTextColor(input.appearance.cellTextColor)
    }

    private fun setupContent() {
        button.tag = input.number
        if (input.isLock) {
            button.text = null
            button.background = input.appearance.cellLockIcon
        } else {
            button.background = null
            button.text = input.number.toString()
        }
    }
}