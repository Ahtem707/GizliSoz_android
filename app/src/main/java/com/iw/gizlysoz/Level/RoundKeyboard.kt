package com.iw.gizlysoz.Level

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.iw.gizlysoz.*
import com.iw.gizlysoz.Extension.DrawView
import com.iw.gizlysoz.Extension.LinePosition
import com.iw.gizlysoz.ProjectManagers.MainManager
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


fun LevelActivity.setupRoundKeyboard() {

    // Обновить размер блока клавиатуры
    val layout = RoundKeyboardConstants.Layout(this)
    customKeyboard.updateLayoutParams<ConstraintLayout.LayoutParams> {
        width = layout.keyboardSize.width
        height = layout.keyboardSize.height
    }

    // Получить данные
    val chars = MainManager.share.getLevel().chars

    // Добавить клавиатуру
    val input = RoundKeyboard.Input(chars)
    val roundKeyboard = RoundKeyboard(this, input)
    customKeyboard.addView(roundKeyboard)
    roundKeyboard.sendWord = { word ->
        val func = openWordCompletion
        if(func != null) func(word)
    }
}

class Point {
    val x: Float
    val y: Float

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }
    constructor(x: Int, y: Int) {
        this.x = x.toFloat()
        this.y = y.toFloat()
    }
}
data class CharCellData(val id: Int, val cell: CharCell?, val char: String, var point: Point)

class RoundKeyboardConstants {
    class Layout(context: Context): CharCell.Layout {
        val wrap = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val parent = ConstraintLayout.LayoutParams.MATCH_PARENT
        val keyboardSize = FrameLayout.LayoutParams(1000,1000)
        val keyboardCenter = Point(keyboardSize.width/2, keyboardSize.height/2)
        override val cellSize = 200f
        val keyboardRound = keyboardSize.width/2 - cellSize/2
        val shuffleSize = FrameLayout.LayoutParams(200,200)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    class Appearance(context: Context): CharCell.Appearance {
        override val charCellBgHidden: Int = ContextCompat.getColor(context, R.color.hidden)
        override val charCellBgEmpty = ContextCompat.getColor(context, R.color.charCellEmpty)
        override val charCellBgFill = ContextCompat.getColor(context, R.color.charCellFill)
        override val charCellBgSelected = ContextCompat.getColor(context, R.color.charCellSelected)
        override val charCellTextSize = 26f
        override val charCellTextColor = ContextCompat.getColor(context, R.color.black)
        override val charCellCornerRadius = Float.MAX_VALUE
        val lineColor = ContextCompat.getColor(context, R.color.lineColor)
        val shuffleIcon = context.getDrawable(R.drawable.shuffle_icon)
        val shuffleBg = ContextCompat.getColor(context, R.color.teal_200)
    }
}

class RoundKeyboard(context: Context, private val input: Input): RelativeLayout(context) {

    // Mark - Wrapper objects
    data class Input(
        var chars: Array<String>
    )

    private val layout = RoundKeyboardConstants.Layout(context)
    private val appearance = RoundKeyboardConstants.Appearance(context)
    private val lineLayer = FrameLayout(context)
    private val charsLayer = FrameLayout(context)
    private val touchView = View(context)
    private var charBtnPoints: ArrayList<CharCellData> = ArrayList()
    private var charBtnPointsSelected: ArrayList<CharCellData> = ArrayList()
    private var shuffleIcon: ImageView? = null
    private var shuffleButton: Button? = null
    private var isShuffle: Boolean = false


    var sendWord: ((word: String) -> Unit)? = null

    init {
        setupSubview()
        setupLayout()
        setupKeyboardTouch()
    }

    private fun setupSubview() {
        setupShuffleButton()
        setupChars()
    }

    private fun setupLayout() {

        this.layoutParams = ViewGroup.LayoutParams(
            layout.parent,
            layout.parent
        )

        this.lineLayer.layoutParams = ViewGroup.LayoutParams(
            layout.parent,
            layout.parent
        )

        this.charsLayer.layoutParams = ViewGroup.LayoutParams(
            layout.parent,
            layout.parent
        )

        this.touchView.layoutParams = ViewGroup.LayoutParams(
            layout.parent,
            layout.parent
        )

        this.addView(lineLayer)
        this.addView(charsLayer)
        this.addView(touchView)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    private fun setupShuffleButton() {
        val layoutParams = layout.shuffleSize
        val leftOffset = (layout.keyboardSize.width - layout.shuffleSize.width) / 2
        val topOffset = (layout.keyboardSize.height - layout.shuffleSize.height) / 2
        layoutParams.leftMargin = leftOffset
        layoutParams.topMargin = topOffset

        shuffleIcon = ImageView(context)
        shuffleIcon?.setImageDrawable(appearance.shuffleIcon)
        shuffleIcon?.layoutParams = layoutParams
        val back = GradientDrawable().apply {
            this.cornerRadius = layout.shuffleSize.width.toFloat()
            this.setColor(appearance.shuffleBg)
        }
        shuffleIcon?.background = back

        shuffleButton = Button(context)
        shuffleButton?.layoutParams = layoutParams
        shuffleButton?.background = null

        shuffleButton?.setOnClickListener {
            this.shuffle()
        }

        this.addView(shuffleIcon)
        this.addView(shuffleButton)
    }

    private fun setupChars() {
        val pointsCircle = getPointsCircle(input.chars.count(), layout.keyboardRound, 0f)

        pointsCircle.forEachIndexed { index, item ->
            val point = Point(
                layout.keyboardCenter.x + item.x,
                layout.keyboardCenter.y + item.y
            )
            val char = input.chars[index]
            val cell = CharCell(context, char, layout, appearance)
            charsLayer.addView(cell)
            cell.x = point.x - cell.layoutParams.width/2
            cell.y = point.y - cell.layoutParams.height/2
            val data = CharCellData(index, cell, char, point)
            charBtnPoints.add(data)
        }
    }

    private fun shuffle() {
        if(isShuffle) return
        isShuffle = true
        shuffleIcon?.animate()
            ?.alpha(0.5f)
            ?.duration = 300
        val points = charBtnPoints.map { it.point } as? ArrayList<Point> ?: return
        points.shuffle()
        for(i in 0 until charBtnPoints.count()) {
            charBtnPoints[i].point = points[i]
        }
        charBtnPoints.forEach {
            it.cell?.animate()
                ?.x(it.point.x - it.cell.layoutParams.width/2)
                ?.y(it.point.y - it.cell.layoutParams.height/2)
                ?.setDuration(1000)
                ?.withEndAction {
                    shuffleIcon?.animate()
                        ?.alpha(1f)
                        ?.duration = 300
                    isShuffle = false
                }
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
        // Перемешать позиции
        pointList.shuffle()
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