package com.iw.gizlysoz

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.iw.gizlysoz.Level.LevelViewModel
import com.iw.gizlysoz.Level.Words


data class LevelActivityInput(val level: Int)

class LevelActivity : AppCompatActivity() {

    private lateinit var view: View
    private lateinit var verticalLayout: LinearLayout
    private lateinit var wordInput: TextInputEditText
    private lateinit var wordBtn: Button

    private val cellSize = 150
    private val cellMargin = 20
    private lateinit var matrix: Array<Array<FrameLayout?>>

    private val viewModel = LevelViewModel()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_activity)
        setupSubview()
        hideSystemUI(window, view)

        // Получить текущий уровень
        val level = getIntent().getIntExtra("level",0)

        // Установить название экрана
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.level) + " $level"
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.show()

        // Создать пустую матрицу
        createMatrix(0)

        // Получить данные по текущему уровню
        viewModel.jsonFetch(this, level)

        // Обновить матрицу
        updateMatrix()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI(window, view)
    }

    private fun setupSubview() {
        view = findViewById(R.id.LevelView)
        verticalLayout = findViewById(R.id.verticalLayout)
        wordInput = findViewById(R.id.wordInput)
        wordBtn = findViewById(R.id.wordBtn)
        wordBtn.setOnClickListener {
            val text = wordInput.text.toString()
            if(text != null) {
                print("myLog: text $text")
                openWord(text)
            }
        }
    }

    private fun updateMatrix() {
        if(viewModel.levelData == null) return
        val level = viewModel.levelData!!
        // Проверка размера ячейки с длиной слова
        if(level.words.values.map { it.chars.count() }.maxOrNull() != level.size) return
        // Проверка длины слова с количеством координат
        if(!checkWordLength(level.words)) return

        // Cоздать новую матрицу, если нужно
        if(level.size != matrix.count()) {
            createMatrix(level.size)
        }

        // Скрыть все ячейки
        for(row in matrix) {
            for(item in row) {
                item?.visibility = View.INVISIBLE
            }
        }

        // Заполнить матрицу данными
        level.words.forEach {
            for(i in it.value.chars.indices) {
                val cell = matrix[it.value.x[i]][it.value.y[i]]
                val content = (cell?.children?.first() as FrameLayout)
                cell.isVisible = true
            }
        }
    }

    private fun openWord(text: String?) {
        if(viewModel.levelData == null) return
        val level = viewModel.levelData!!

        level.words.forEach {
            if(it.key == text) {
                for(i in it.value.chars.indices) {
                    val cell = matrix[it.value.x[i]][it.value.y[i]]
                    val content = (cell?.children?.first() as FrameLayout)
                    val label = (content.children.first() as TextView)
                    label.text = it.value.chars[i]
                }
            }
        }
    }

    private fun checkWordLength(words: Words): Boolean {
        for(key in words.keys) {
            val word = viewModel.levelData?.words?.get(key)
            val length = word?.chars?.count()
            if(length == word?.x?.count() && length == word?.y?.count()) {
                return true
            } else {
                Log.e("Fatal error","Parameter length mismatch")
                return false
            }
        }
        return false
    }

    @SuppressLint("ResourceType")
    private fun createMatrix(size: Int) {

        this.matrix = Array(size) { Array(size) {null} }
        for (x in 0 until size) {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL
            for(y in 0 until size) {
                val cell = makeCell("")
                this.matrix[y][x] = cell
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
            R.color.cellBackground.toColor(this), // border color
            10.dpToPixels(applicationContext).toFloat(), // corners radius
            R.color.cellBackground.toColor(this)
        )

        label.text = text
        label.textSize = 26F
        label.setTextColor(R.color.black.toColor(this))
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