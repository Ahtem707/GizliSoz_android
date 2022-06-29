package com.iw.gizlysoz.Level

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import com.iw.gizlysoz.LevelActivity
import com.iw.gizlysoz.ProjectManagers.MainManager
import com.iw.gizlysoz.ProjectManagers.Words
import com.iw.gizlysoz.R

interface CrossViewOutput {
    fun complete()
}

fun LevelActivity.setupCrossView() {
    val layout = CrossView.Layout(this)
    crossView.updateLayoutParams<ConstraintLayout.LayoutParams> {
        width = layout.crossView.toInt()
        height = layout.crossView.toInt()
    }
    val input = CrossView.Input(this)
    val crossView = CrossView(this, input)
    this.crossView.addView(crossView)
    this.openWordCompletion = { word ->
        crossView.openWord(word)
    }
}

class CrossView(context: Context, private val input: Input): RelativeLayout(context) {

    // Mark - Wrapper objects
    data class Input(
        val delegate: CrossViewOutput
    )

    class Layout(context: Context): CharCell.Layout {
        val wrap = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val parent = ConstraintLayout.LayoutParams.MATCH_PARENT
        val crossView = 1000f
        override val cellSize = 140f
        val spaces = 30
    }

    class Appearance(context: Context): CharCell.Appearance {
        override val charCellBgHidden = ContextCompat.getColor(context, R.color.hidden)
        override val charCellBgEmpty = ContextCompat.getColor(context, R.color.charCellEmpty)
        override val charCellBgFill = ContextCompat.getColor(context, R.color.charCellFill)
        override val charCellBgSelected = ContextCompat.getColor(context, R.color.charCellSelected)
        override val charCellTextSize = 26f
        override val charCellCornerRadius = 10f
        override val charCellTextColor = ContextCompat.getColor(context, R.color.black)
    }

    // Mark - Variables
    private val layout = Layout(context)
    private val appearance = Appearance(context)
    private lateinit var matrix: Array<Array<CharCell?>>
    private val level: MainManager.Level = MainManager.share.getLevel()
    private var levelOpenWords: ArrayList<String> = ArrayList()

    // Mark: - Init
    init {
        setupSubview()
        setupLayout()
        setupAppearance()
    }

    private fun setupSubview() {
        this.gravity = Gravity.CENTER_HORIZONTAL

        val size = level.size ?: return

        // Создать пустую матрицу
        createMatrix(size)

        // Обновить матрицу
        updateMatrix()
    }

    private fun setupLayout() {}

    private fun setupAppearance() {}

    private fun createMatrix(size: Int) {
        var verticalLayout = LinearLayout(context)
        verticalLayout.orientation = LinearLayout.VERTICAL
        this.addView(verticalLayout)

        this.matrix = Array(size) { Array(size) {null} }
        for (x in 0 until size) {
            val horizontalLayout = LinearLayout(context)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL
            for(y in 0 until size) {
                val cell = CharCell(context, "", layout, appearance)
                this.matrix[y][x] = cell
                horizontalLayout.addView(cell)
            }
            val leng = horizontalLayout.children.count()
            for(i in 0..leng-2) {
                val space = Space(context)
                space.layoutParams = ConstraintLayout.LayoutParams(layout.spaces,layout.parent)
                horizontalLayout.addView(space, i*2+1)
            }
            verticalLayout.addView(horizontalLayout)
        }
        val leng = verticalLayout.children.count()
        for(i in 0..leng-2) {
            val space = Space(context)
            space.layoutParams = ConstraintLayout.LayoutParams(layout.parent,layout.spaces)
            verticalLayout.addView(space, i*2+1)
        }
    }

    private fun updateMatrix() {
        // Проверка размера ячейки с длиной слова
        if(level.words.values.maxOf { it.chars.count() } > level.size) return
        // Проверка длины слова с количеством координат
        if(!checkWordLength(level.words)) return

        // Скрыть все ячейки
        for(row in matrix) {
            for(item in row) {
                val cell = item ?: return
                cell.setState(CharCell.CellState.hidden)
            }
        }

        // Заполнить матрицу данными
        level.words.forEach {
            for(i in it.value.chars.indices) {
                val cell = matrix[it.value.x[i]][it.value.y[i]] ?: return
                cell.setText(it.value.chars[i])
                cell.setState(CharCell.CellState.empty)
            }
        }
    }

    fun openWord(text: String?) {
        val text = text ?: return

        // Проверка нет ли слова среди открытых
        if(levelOpenWords.contains(text)) return

        // Открыть слово если оно есть
        level.words.forEach {
            if(it.key == text) {
                for(i in it.value.chars.indices) {
                    val cell = matrix[it.value.x[i]][it.value.y[i]] ?: return
                    cell.setText(it.value.chars[i])
                    cell.setState(CharCell.CellState.fill)
                }
                levelOpenWords.add(text)
                if(levelOpenWords.size == level.words.size) {
                    input.delegate.complete()
                }
            }
        }
    }

    private fun checkWordLength(words: Words): Boolean {
        for(key in words.keys) {
            val word = level.words[key]
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
}
