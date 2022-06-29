package com.iw.gizlysoz.MainActivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.iw.gizlysoz.*
import com.iw.gizlysoz.ProjectManagers.MainManager

class MainActivity : AppCompatActivity() {

    private var maxLevel: Int = 2;
    private var view: View? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupSubview()

        val levelBtn = findViewById<Button>(R.id.levelBtn)
        levelBtn.setOnClickListener {
            openLevelSelect();
        }

        val startBtn = findViewById<Button>(R.id.startBtn)
        startBtn.setOnClickListener {
            openActivity();
        }

        startBtn.setBackgroundColor(R.color.green.toColor(this))

        // Создаем менеджер
        MainManager.share = MainManager(this)
        MainManager.share.loadLevels()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        view?.let { view ->
            hideSystemUI(window, view)
        }
    }

    private fun setupSubview() {
        view = findViewById(R.id.MainView)
    }

    private fun openLevelSelect() {

        val intent = Intent(
            this,
            LevelsScreen::class.java
        )
        startActivity(intent)
    }

    private fun openActivity() {
        val intent = Intent(
            this,
            LevelActivity::class.java
        )
        startActivity(intent)
    }
}
