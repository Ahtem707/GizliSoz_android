package com.iw.gizlysoz

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView;

class MainActivity : AppCompatActivity() {

    private var levelSelected: Int = 1;
    private var view: View? = null
    var navigationView: NavigationView? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupSubview()

        val levelBtn = findViewById<Button>(R.id.levelBtn)
        levelBtn.text = "$levelSelected\n" + getString(R.string.level)
        levelBtn.setOnClickListener {
            openLevelSelect();
        }

        val startBtn = findViewById<Button>(R.id.startBtn)
        startBtn.setOnClickListener {
            openActivity();
        }

        startBtn.setBackgroundColor(R.color.green.toColor(this))
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
        levelSelected++;
        val levelBtn = findViewById<Button>(R.id.levelBtn)
        levelBtn.text = "$levelSelected\n" + getString(R.string.level)
    }

    private fun openActivity() {
        val intent = Intent(
            this,
            LevelActivity::class.java
        );
        intent.putExtra("level", levelSelected)
        startActivity(intent);
    }
}
