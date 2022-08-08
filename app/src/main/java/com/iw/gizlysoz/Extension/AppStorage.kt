package com.iw.gizlysoz.Extension

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlin.reflect.typeOf

class AppStorage(context: Context) {

    enum class Keys {
        currentLevel,
        lastOpenLevel
    }

    private var shared: SharedPreferences
    private var editor: SharedPreferences.Editor
    private val fileStorage: String = "myPref"

    init {
        shared = context.getSharedPreferences(fileStorage, MODE_PRIVATE)
        editor = shared.edit()
    }

    fun <T>set(key: Keys, value: T) {
        editor.apply {
            val key = key.toString()
            when(value) {
                is Int -> putInt(key, value as Int)
                is String -> putString(key, value as String)
            }
            apply()
        }
    }

    fun getInt(key: Keys, default: Int = 0): Int {
        return shared.getInt(key.toString(), default)
    }

    fun getString(key: Keys, default: String? = null): String? {
        return shared.getString(key.toString(), default)
    }
}