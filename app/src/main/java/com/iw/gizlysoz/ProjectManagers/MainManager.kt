package com.iw.gizlysoz.ProjectManagers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iw.gizlysoz.Extension.AppStorage
import java.io.IOException

typealias Words = Map<String, MainManager.Word>

class MainManager(val context: Context) {

    companion object {
        lateinit var share: MainManager
    }

    private val appStorage: AppStorage

    var levelsAll: List<Level> = listOf()

    var currentLevel: Int
        get() {
            return appStorage.getInt(AppStorage.Keys.currentLevel, default = 1)
        }
        set(newValue) {
            appStorage.set(AppStorage.Keys.currentLevel, newValue)
        }

    var lastOpenLevel: Int
        get() {
            return appStorage.getInt(AppStorage.Keys.lastOpenLevel, default = 1)
        }
        set(newValue) {
            appStorage.set(AppStorage.Keys.lastOpenLevel, newValue)
        }

    init {
        appStorage = AppStorage(context)
    }

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
        val chars: String,
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
        return levelsAll.filter { it.level == currentLevel }.first()
    }
}