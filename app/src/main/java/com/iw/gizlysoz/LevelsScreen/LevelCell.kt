package com.iw.gizlysoz.LevelsScreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.iw.gizlysoz.Extension.*
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
    }

    interface Appearance {
        val cellBackground: Int
        val cellTextFont: Typeface
        val cellTextSize: Float
        val cellTextColor: Int
        val cellLockIcon: Drawable?
        val cellLockIconScale: Float
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

    private var button: Button? = null

    private fun setupSubview() {
        // Init subview
        button = Button(input.context)

        // Init id
        button?.id = button.hashCode()

        // Configure
        button?.setOnClickListener {
            val index = it.tag as Int
            input.delegate.onClick(index)
        }
    }

    private fun setupLayout() {

        layoutParams = makeFrameLayout().size(input.layout.cellSize)
        button?.layoutParams = makeLayout().size(input.layout.cellSize)

        // addView
        addView(button)
    }

    private fun setupAppearance() {
        this.setBackgroundColor(input.appearance.cellBackground)
        button?.typeface = input.appearance.cellTextFont
        button?.textSize = input.appearance.cellTextSize
        button?.setTextColor(input.appearance.cellTextColor)
    }

    private fun setupContent() {

        button?.tag = input.number
        when(input.state) {
            LevelCellInterface.State.lock -> {
                button?.text = null
                button?.background = input.appearance.cellLockIcon
                button?.scaleX = input.appearance.cellLockIconScale
                button?.scaleY = input.appearance.cellLockIconScale
            }
            LevelCellInterface.State.normal -> {
                button?.background = null
                button?.text = input.number.toString()
            }
            LevelCellInterface.State.selected -> {

                val a = ContextCompat.getColor(input.context, R.color.yellow)
                button?.setBackgroundColor(a)
                button?.text = input.number.toString()

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