package com.iw.gizlysoz

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class LevelActivity : AppCompatActivity() {

    private val cellSize = 150
    private val cellMargin = 20
    private var matrix: Array<Array<Int>>? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_activity)

        val actionBar = supportActionBar
        actionBar!!.title = "Level 1"

        createMatrix(4)

        // Input value
        val tmp = getIntent().getStringExtra("levelInput")
        println("myLog: $tmp")
    }

    @SuppressLint("ResourceType")
    private fun createMatrix(size: Int) {
        val verticalLayout = findViewById<LinearLayout>(R.id.verticalLayout)

        this.matrix = Array(size) { Array(size) {0} }
        for (i in 0 until size) {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL
            for(j in 0 until size) {
                val cell = makeCell("h")
                val id = View.generateViewId()
                cell.id = id
                this.matrix!![i][j] = id
                horizontalLayout.addView(cell)
            }
            verticalLayout.addView(horizontalLayout)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun makeCell(text: String): FrameLayout {
        val container = FrameLayout(this)
        val content = FrameLayout(this)
        val label = TextView(this)

        val containerLayout = LinearLayout.LayoutParams(cellSize, cellSize)
        val contentLayout = LinearLayout.LayoutParams(cellSize - cellMargin, cellSize - cellMargin)
        contentLayout.gravity = Gravity.CENTER

        container.layoutParams = containerLayout
        content.layoutParams = contentLayout
        label.gravity = Gravity.CENTER

        content.background = roundedCornersDrawable(
            2.dpToPixels(applicationContext), // border width in pixels
            R.color.red.toColor(this), // border color
            10.dpToPixels(applicationContext).toFloat(), // corners radius
            R.color.yellow.toColor(this)
        )

        label.text = text
        content.addView(label)
        container.addView(content)
        return container
    }

    private fun roundedCornersDrawable(
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

}