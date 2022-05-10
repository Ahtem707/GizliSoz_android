package com.iw.gizlysoz.Level

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.iw.gizlysoz.*
import com.iw.gizlysoz.Extension.DrawView
import com.iw.gizlysoz.Extension.LinePosition
import kotlin.math.pow

fun LevelActivity.setupRoundKeyboard() {

    // Обновить размер блока клавиатуры
    // ?? Вариативно можно будет убрать
    val layout = RoundKeyboardConstants.Layout(this);
    customKeyboard.updateLayoutParams<ConstraintLayout.LayoutParams> {
        width = layout.keyboardSize.toInt();
        height = layout.keyboardSize.toInt();
    }

    // Получить данные
    val chars = this.viewModel.levelData?.chars ?: return;
    // val chars = arrayOf("a","b","c","d","e","f","g")

    // Перемешать массив символов
    chars.shuffle();

    // Добавит клавиатуру
    val roundKeyboard = RoundKeyboard(this, chars);
    customKeyboard.addView(roundKeyboard);
    roundKeyboard.sendWord = { word ->
        openWord(word)
    }
}

data class Point(val x: Float, val y: Float) {}
data class CharCellData(val id: Int, var char: String, val point: Point)

class RoundKeyboardConstants {
    class Layout(context: Context) {
        val keyboardSize = 1000f;
        val keyboardCenter = Point(keyboardSize/2, keyboardSize/2);
        val cellSize = 200f;
        val keyboardRound = keyboardSize/2 - cellSize/2;
    }

    class Appearance(context: Context) {
        val charCellBgEmpty = ContextCompat.getColor(context, R.color.cellEmptyColor);
        val charCellBgFill = ContextCompat.getColor(context, R.color.cellFillColor);
        val textSize = 26f;
        val textColor = ContextCompat.getColor(context, R.color.black);
        val lineColor = ContextCompat.getColor(context, R.color.lineColor);
    }
}

class RoundKeyboard: RelativeLayout {

    private val layout = RoundKeyboardConstants.Layout(context);
    private val appearance = RoundKeyboardConstants.Appearance(context);
    private val lineLayer = FrameLayout(context);
    private val charsLayer = FrameLayout(context);
    private val touchView = View(context);
    private var charBtnPoints = ArrayList<CharCellData>();
    private var charBtnPointsSelected = ArrayList<CharCellData>();

    var sendWord: ((word: String) -> Unit)? = null

    private val chars: Array<String>;

    constructor(context: Context, chars: Array<String>) : super(context) {
        this.chars = chars;
        setupLayout();
        setupChars();
        setupKeyboardTouch();
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

        this.addView(lineLayer);
        this.addView(charsLayer);
        this.addView(touchView);
    }

    private fun setupChars() {
        val pointsCircle = getPointsCircle(chars.count(), layout.keyboardRound, 0f);

        pointsCircle.forEachIndexed { index, item ->
            val point = Point(
                layout.keyboardCenter.x + item.x,
                layout.keyboardCenter.y + item.y
            )
            val data = CharCellData(index, "", point)
            charBtnPoints.add(data)
        }

        chars.forEachIndexed { i, char ->
            charBtnPoints[i].char = char;
            val cell = CharCell(context, char, layout, appearance);
            val data = charBtnPoints[i];
            charsLayer.addView(cell);
            cell.x = data.point.x - cell.layoutParams.width/2;
            cell.y = data.point.y - cell.layoutParams.height/2;
        }
    }

    private fun getPointsCircle(n: Int, radius: Float, offsetAngel: Float): ArrayList<Point> {
        var pointList = ArrayList<Point>();
        for(i in 0..n-1) {
            val i = i.toFloat();
            val angel = 360.0 / n * i + offsetAngel;
            val angelRadian = angel * Math.PI / 180.0;
            val x = (radius * Math.cos(angelRadian)).toFloat();
            val y = (radius * Math.sin(angelRadian)).toFloat();
            pointList.add(Point(x,y));
        }
        return pointList
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyboardTouch() {
        val cellRadius = (layout.cellSize/2);
        touchView.setOnTouchListener { _, event ->
            val nowPoint = Point(event.x, event.y);

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    charBtnPoints.forEach { data ->
                        if((nowPoint.x - data.point.x).pow(2) + (nowPoint.y - data.point.y).pow(2) <= cellRadius.pow(2)) {
                            charBtnPointsSelected.add(data);
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    charBtnPoints.forEach { data ->
                        if((nowPoint.x - data.point.x).pow(2) + (nowPoint.y - data.point.y).pow(2) <= cellRadius.pow(2)) {
                            val checkCollision = charBtnPointsSelected.filter { it.id == data.id }
                            if (checkCollision.isEmpty()) {
                                charBtnPointsSelected.add(data);
                            }
                        }
                        charBtnPointsSelected.removeAll { it.id == -1 }
                        val tmpLine = CharCellData(-1,"",nowPoint)
                        charBtnPointsSelected.add(tmpLine);
                        updateLines();
                    }
                }
                MotionEvent.ACTION_UP -> {
                    charBtnPointsSelected.removeAll { it.id == -1 }
                    val word = charBtnPointsSelected.map { it.char }.reduce { x, y -> x + y }
                    sendWord!!(word);
                    charBtnPointsSelected.clear();
                    updateLines();
                }
            }

            return@setOnTouchListener true;
        }
    }

    private fun updateLines() {
        lineLayer.removeAllViews();
        var positions = ArrayList<LinePosition>();
        val leng = charBtnPointsSelected.count();
        for (i in 0..leng-2) {
            val arr = charBtnPointsSelected;
            val position = LinePosition(arr[i].point.x, arr[i].point.y, arr[i+1].point.x, arr[i+1].point.y);
            positions.add(position);
        }
        lineLayer.addView(DrawView(context, positions, appearance.lineColor));
    }
}