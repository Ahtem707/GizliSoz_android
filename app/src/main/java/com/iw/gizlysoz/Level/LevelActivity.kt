package com.iw.gizlysoz

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.iw.gizlysoz.Level.*

class LevelActivity : AppCompatActivity(), CrossViewOutput {

    private lateinit var view: View
    lateinit var crossView: FrameLayout
    lateinit var customKeyboard: FrameLayout

    val viewModel = LevelViewModel()
    var openWordCompletion: ((text: String?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_activity)
        setupSubview()
        hideSystemUI(window, view)

        // Получить текущий уровень
        val level = getIntent().getIntExtra("level",0)

        // Установить название экрана
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.level) + " $level"
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.show()

        // Получить данные по текущему уровню
        viewModel.jsonFetch(this, level)
        setupCrossView()
        setupRoundKeyboard()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI(window, view)
    }

    private fun setupSubview() {
        view = findViewById(R.id.LevelView)
        crossView = findViewById(R.id.crossView)
        customKeyboard = findViewById(R.id.customKeyboard)
    }

    override fun complete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.wellDone)
        builder.setMessage(R.string.youWin)

        builder.setPositiveButton(R.string.well) { dialog, which ->
            this.finish()
        }

        builder.show()
    }
}