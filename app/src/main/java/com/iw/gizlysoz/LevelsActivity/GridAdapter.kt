package com.iw.gizlysoz.LevelsActivity

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class GridAdapter(
    private val cellInput: LevelCellInterface.Input,
    private val levelsCount: Int,
    private val lastOpenLevel: Int
): BaseAdapter() {

    override fun getCount(): Int {
        return levelsCount
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val cellInput = cellInput
        cellInput.number = p0 + 1
        cellInput.isLock = p0 + 1 > lastOpenLevel
        return LevelCell(cellInput)
    }

    override fun getItem(p0: Int): Any {
        return p0 + 1
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
}