package com.iw.gizlysoz.CurrentLevelScreen

import android.content.Context
import android.graphics.Matrix
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.iw.gizlysoz.Extension.*
import com.iw.gizlysoz.ProjectManagers.MainManager
import com.iw.gizlysoz.R

class CurrentLevelActivityLayout(context: Context): Layout(context) {
    val crossViewSize: Int = 1000
    val crossViewTop: Int = 30

    val customKeyboardSize: Int = 1000
    val customKeyboardBottom: Int = 30
}

class CurrentLevelActivityAppearance(context: Context): Appearance(context) {
    val backImage = ContextCompat.getDrawable(context, R.drawable.back1)
}

class CurrentLevelActivity: BaseActivity(), CrossViewOutput {

    private lateinit var layout: CurrentLevelActivityLayout
    private lateinit var appearance: CurrentLevelActivityAppearance

    // Create components
    var backImageView: ImageView? = null
    var crossView: FrameLayout? = null
    var customKeyboard: FrameLayout? = null
    
    var openWordCompletion: ((text: String?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInit()
        setupSubview()
        setupLayout()
        setupBackImage()
        setupCrossView()
        setupRoundKeyboard()

        // Получить текущий уровень

        val level = MainManager.share.currentLevel

        // Установить название экрана
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.level) + " $level"
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.show()
    }

    private fun setupInit() {
        layout = CurrentLevelActivityLayout(this)
        appearance = CurrentLevelActivityAppearance(this)
    }

    private fun setupSubview() {
        backImageView = ImageView(this)
        crossView = FrameLayout(this)
        customKeyboard = FrameLayout(this)

        backImageView?.id = backImageView.hashCode()
        crossView?.id = crossView.hashCode()
        customKeyboard?.id = customKeyboard.hashCode()

        view?.addView(backImageView)
        view?.addView(crossView)
        view?.addView(customKeyboard)
    }

    private fun setupLayout() {
        val set = ConstraintSet()

        backImageView?.let {
            set.connect(it.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            set.connect(it.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            set.connect(it.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            set.connect(it.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        }

        crossView?.let {
            set.constrainHeight(it.id, layout.crossViewSize)
            set.constrainWidth(it.id, layout.crossViewSize)
            set.centerHorizontally(it.id, ConstraintSet.PARENT_ID)
            set.connect(it.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, layout.crossViewTop)
        }

        customKeyboard?.let {
            set.constrainHeight(it.id, layout.customKeyboardSize)
            set.constrainWidth(it.id, layout.customKeyboardSize)
            set.centerHorizontally(it.id, ConstraintSet.PARENT_ID)
            set.connect(it.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, layout.customKeyboardBottom)
        }

        set.applyTo(view)
    }

    private fun setupBackImage() {
        backImageView?.scaleType = ImageView.ScaleType.CENTER_CROP
        backImageView?.setImageDrawable(appearance.backImage)
    }

    override fun complete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.wellDone)
        builder.setMessage(R.string.youWin)

        builder.setPositiveButton(R.string.well) { dialog, which ->
            this.finish()
        }

        builder.show()

        val manager = MainManager.share
        if(manager.currentLevel < manager.levelsAll.count()) {
            manager.currentLevel += 1
            manager.lastOpenLevel = manager.currentLevel
        }
    }
}