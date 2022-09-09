package com.iw.gizlysoz.MainScreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import com.iw.gizlysoz.CurrentLevelScreen.CurrentLevelActivity
import com.iw.gizlysoz.Extension.BaseActivity
import com.iw.gizlysoz.LevelsScreen.LevelsActivity
import com.iw.gizlysoz.ProjectManagers.MainManager
import com.iw.gizlysoz.R

class MainActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupSubview()

        val levelBtn = findViewById<Button>(R.id.levelBtn)
        levelBtn.setOnClickListener {
            openLevelSelect()
        }

        val startBtn = findViewById<Button>(R.id.startBtn)
        startBtn.setOnClickListener {
            openActivity()
        }

        startBtn.setBackgroundColor(Resource(R.color.green))

        // Создаем менеджер
        MainManager.share = MainManager(this)
        MainManager.share.loadLevels()
    }

    private fun setupSubview() {

    }

    private fun openLevelSelect() {

        val intent = Intent(
            this,
            LevelsActivity::class.java
        )
        startActivity(intent)
    }

    private fun openActivity() {
        val intent = Intent(
            this,
            CurrentLevelActivity::class.java
        )
        startActivity(intent)
    }
}
