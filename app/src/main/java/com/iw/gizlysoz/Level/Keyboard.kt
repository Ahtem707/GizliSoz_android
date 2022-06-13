package com.iw.gizlysoz.Level

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.iw.gizlysoz.*
import com.iw.gizlysoz.Extension.DrawView
import com.iw.gizlysoz.Extension.LinePosition
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

fun LevelActivity.setupRoundKeyboard() {

    // Обновить размер блока клавиатуры
    val layout = RoundKeyboardConstants.Layout(this)
    customKeyboard.updateLayoutParams<ConstraintLayout.LayoutParams> {
        width = layout.keyboardSize.toInt()
        height = layout.keyboardSize.toInt()
    }

    // Получить данные
    val chars = this.viewModel.levelData?.chars ?: return

    // Перемешать массив символов
    chars.shuffle()

    // Добавить клавиатуру
    val roundKeyboard = RoundKeyboard(this, chars)
    customKeyboard.addView(roundKeyboard)
    roundKeyboard.sendWord = { word ->
        val func = openWordCompletion
        if(func != null) func(word)
    }
}

data class Point(val x: Float, val y: Float)
data class CharCellData(val id: Int, val cell: CharCell?, val char: String, val point: Point)

class RoundKeyboardConstants {
    class Layout(context: Context): CharCell.Layout {
        val keyboardSize = 1000f
        val keyboardCenter = Point(keyboardSize/2, keyboardSize/2)
        override val cellSize = 200f
        val keyboardRound = keyboardSize/2 - cellSize/2
    }

    class Appearance(context: Context): CharCell.Appearance {
        override val charCellBgHidden: Int = ContextCompat.getColor(context, R.color.hidden)
        override val charCellBgEmpty = ContextCompat.getColor(context, R.color.charCellEmpty)
        override val charCellBgFill = ContextCompat.getColor(context, R.color.charCellFill)
        override val charCellBgSelected = ContextCompat.getColor(context, R.color.charCellSelected)
        override val charCellTextSize = 26f
        override val charCellTextColor = ContextCompat.getColor(context, R.color.black)
        override val charCellCornerRadius = Float.MAX_VALUE
        val lineColor = ContextCompat.getColor(context, R.color.lineColor)
    }
}

@SuppressLint("ViewConstructor")
class RoundKeyboard(context: Context, private val chars: Array<String>): RelativeLayout(context) {

    private val layout = RoundKeyboardConstants.Layout(context)
    private val appearance = RoundKeyboardConstants.Appearance(context)
    private val lineLayer = FrameLayout(context)
    private val charsLayer = FrameLayout(context)
    private val touchView = View(context)
    private var charBtnPoints = ArrayList<CharCellData>()
    private var charBtnPointsSelected = ArrayList<CharCellData>()

    var sendWord: ((word: String) -> Unit)? = null

    init {
        setupLayout()
        setupChars()
        setupKeyboardTouch()
    }

    private fun setupLayout() {

        this.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        this.lineLayer.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        this.charsLayer.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        this.touchView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        this.addView(lineLayer)
        this.addView(charsLayer)
        this.addView(touchView)
    }

    private fun setupChars() {
        val pointsCircle = getPointsCircle(chars.count(), layout.keyboardRound, 0f)

        pointsCircle.forEachIndexed { index, item ->
            val point = Point(
                layout.keyboardCenter.x + item.x,
                layout.keyboardCenter.y + item.y
            )
            val char = chars[index]
            val cell = CharCell(context, char, layout, appearance)
            charsLayer.addView(cell)
            cell.x = point.x - cell.layoutParams.width/2
            cell.y = point.y - cell.layoutParams.height/2
            val data = CharCellData(index, cell, char, point)
            charBtnPoints.add(data)
        }
    }

    private fun getPointsCircle(n: Int, radius: Float, offsetAngel: Float = 0f): ArrayList<Point> {
        val pointList: ArrayList<Point> = ArrayList()
        for(i in 0 until n) {
            val index = i.toFloat()
            val angel = 360.0 / n * index + offsetAngel
            val angelRadian = angel * Math.PI / 180.0
            val x = (radius * cos(angelRadian)).toFloat()
            val y = (radius * sin(angelRadian)).toFloat()
            pointList.add(Point(x,y))
        }
        return pointList
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyboardTouch() {
        val cellRadius = (layout.cellSize/2)
        touchView.setOnTouchListener { _, event ->
            val nowPoint = Point(event.x, event.y)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    charBtnPoints.forEach { data ->
                        if((nowPoint.x - data.point.x).pow(2) + (nowPoint.y - data.point.y).pow(2) <= cellRadius.pow(2)) {
                            data.cell?.setState(CharCell.CellState.select)
                            charBtnPointsSelected.add(data)
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    charBtnPoints.forEach { data ->
                        if((nowPoint.x - data.point.x).pow(2) + (nowPoint.y - data.point.y).pow(2) <= cellRadius.pow(2)) {
                            val checkCollision = charBtnPointsSelected.filter { it.id == data.id }
                            if (checkCollision.isEmpty()) {
                                data.cell?.setState(CharCell.CellState.select)
                                charBtnPointsSelected.add(data)
                            }
                        }
                        charBtnPointsSelected.removeAll { it.id == -1 }
                        val tmpLine = CharCellData(-1,null, "", nowPoint)
                        charBtnPointsSelected.add(tmpLine)
                        updateLines()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    charBtnPointsSelected.forEach { data ->
                        data.cell?.setState(CharCell.CellState.fill)
                    }
                    charBtnPointsSelected.removeAll { it.id == -1 }
                    if(charBtnPointsSelected.size > 1) {
                        val word = charBtnPointsSelected.map { it.char }.reduce { x, y -> x + y }
                        if(!word.isEmpty()){
                            sendWord?.let { it(word) }
                        }
                        charBtnPointsSelected.clear()
                    }
                    updateLines()
                }
            }

            return@setOnTouchListener true
        }
    }

    private fun updateLines() {
        lineLayer.removeAllViews()
        val positions = ArrayList<LinePosition>()
        val leng = charBtnPointsSelected.count()
        for (i in 0..leng-2) {
            val arr = charBtnPointsSelected
            val position = LinePosition(arr[i].point.x, arr[i].point.y, arr[i+1].point.x, arr[i+1].point.y)
            positions.add(position)
        }
        lineLayer.addView(DrawView(context, positions, appearance.lineColor))
    }
}