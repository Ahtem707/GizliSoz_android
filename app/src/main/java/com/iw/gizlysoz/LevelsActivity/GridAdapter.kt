package com.iw.gizlysoz.LevelsActivity

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class GridAdapter(
    private val cellInput: LevelCellInterface.Input,
    private val levelsCount: Int,
    private val currectLevel: Int,
    private val lastOpenLevel: Int
): BaseAdapter() {

    override fun getCount(): Int {
        return levelsCount
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val cellInput = cellInput
        cellInput.number = p0 + 1
        cellInput.state = when {
            p0 + 1 == currectLevel -> LevelCellInterface.State.selected
            p0 + 1 <= lastOpenLevel -> LevelCellInterface.State.normal
            else -> LevelCellInterface.State.lock
        }
        return LevelCell(cellInput)
    }

    override fun getItem(p0: Int): Any {
        return p0 + 1
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
}