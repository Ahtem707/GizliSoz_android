package com.iw.gizlysoz.Extension

import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

open class BaseActivity: AppCompatActivity() {

    var view: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (view == null) {
            view = ConstraintLayout(this)
            setContentView(view)
        }
        view?.let { hideSystemUI(window, it) }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
//        view?.let { hideSystemUI(window, it) }
    }
//

    fun setContentView(view: ConstraintLayout?) {
        super.setContentView(view)
        this.view = view
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        this.view = view as? ConstraintLayout
    }

    private fun hideSystemUI(window: Window, view: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun Resource(color: Int): Int {
        return ContextCompat.getColor(this, color)
    }
}