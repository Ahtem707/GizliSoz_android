package com.iw.gizlysoz.LevelsActivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.animation.*
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.iw.gizlysoz.Extension.Loop
import com.iw.gizlysoz.Extension.makeFrameLayout
import com.iw.gizlysoz.Extension.makeLayout
import com.iw.gizlysoz.Extension.size
import com.iw.gizlysoz.R


interface LevelCellInterface {

    data class Input(
        val context: Context,
        val layout: Layout,
        val appearance: Appearance,
        var number: Int = 0,
        var state: State = State.normal,
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

    enum class State {
        lock,
        normal,
        selected
    }
}

@SuppressLint("ViewConstructor")
class LevelCell (
    val input: LevelCellInterface.Input
    ): FrameLayout(input.context) {

    init {
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
        when(input.state) {
            LevelCellInterface.State.lock -> {
                button.text = null
                button.background = input.appearance.cellLockIcon
            }
            LevelCellInterface.State.normal -> {
                button.background = null
                button.text = input.number.toString()
            }
            LevelCellInterface.State.selected -> {

                val a = ContextCompat.getColor(input.context, R.color.yellow)
                button.setBackgroundColor(a)
                button.text = input.number.toString()

                var phase = false
                Loop { me ->
                    val r = if (phase) 0f else 360f
                    val animate = animate()
                    phase = !phase
                    animate.startDelay = 3000
                    animate.rotationX(r)
                    animate.rotationY(r)
                    animate.duration = 500
                    animate.withEndAction {
                        me.next()
                    }
                    animate.start()
                }
            }
        }
    }
}