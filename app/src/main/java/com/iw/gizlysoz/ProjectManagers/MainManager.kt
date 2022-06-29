package com.iw.gizlysoz.ProjectManagers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

typealias Words = Map<String, MainManager.Word>

class MainManager(val context: Context) {

    companion object {
        lateinit var share: MainManager
    }

    var levelsAll: List<Level> = listOf()
    var currectLevel: Int = 1
    var lastOpenLevel: Int = 1

    data class Word(
        val chars: Array<String>,
        val x: Array<Int>,
        val y: Array<Int>,
        val description: String
    )

    data class Level(
        val level: Int,
        val name: String,
        val size: Int,
        val chars: Array<String>,
        val words: Words)

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun loadLevels() {
        val fileName = "data.json"
        val jsonFileString = getJsonDataFromAsset(context, fileName) ?: return
        val gson = Gson()
        val listPersonType = object : TypeToken<List<Level>>() {}.type
        levelsAll = gson.fromJson(jsonFileString, listPersonType)
    }

    fun getLevel(): Level {
        return levelsAll.filter { it.level == currectLevel }.first()
    }
}