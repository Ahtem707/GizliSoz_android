package com.iw.gizlysoz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            openActivity()
        }
    }

    private fun openActivity() {
        val intent = Intent(
            this,
            LevelActivity::class.java
        );
        intent.putExtra("level", 1)
        startActivity(intent);
    }
}
