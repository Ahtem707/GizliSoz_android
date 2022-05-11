package com.iw.gizlysoz.Level

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

data class Word(
    val chars: Array<String>,
    val x: Array<Int>,
    val y: Array<Int>,
    val description: String
) {}

typealias Words = Map<String, Word>

data class Level(
    val level: Int,
    val name: String,
    val size: Int,
    val chars: Array<String>,
    val words: Words) {}

class LevelViewModel {

    var levelData: Level? = null;
    var levelOpenWords: ArrayList<String> = ArrayList();

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

    fun jsonFetch(applicationContext: Context, level: Int) {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "data.json")
        if (jsonFileString == null) return
        val gson = Gson()
        val listPersonType = object : TypeToken<List<Level>>() {}.type
        var levels: List<Level> = gson.fromJson(jsonFileString, listPersonType)
        levelData = levels.filter { it.level == level }.first()
    }
}