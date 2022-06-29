package com.iw.gizlysoz

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.iw.gizlysoz.Extension.*
import com.iw.gizlysoz.LevelsActivity.GridAdapter
import com.iw.gizlysoz.LevelsActivity.LevelCellInterface
import com.iw.gizlysoz.ProjectManagers.MainManager

interface LevelsScreenInterface {

    data class Input(
        val viewModel: Context
    )

    class Layout(context: Context): Layouts(context), LevelCellInterface.Layout {
        override val cellSize: Int = 160
        override val cellButtonSize: Int = super.parent
        val gridViewPadding: Int = 50
        val cellBetweenSpacing: Int = 25
    }

    class Appearance(context: Context): Appearances(context), LevelCellInterface.Appearance {
        override val cellBackground: Int = ContextCompat.getColor(context, R.color.teal_200)
        override val cellTextFont: Typeface = Typeface.DEFAULT
        override val cellTextSize: Float = 26F
        override val cellTextColor: Int = ContextCompat.getColor(context, R.color.textPrimary)
        override val cellLockIcon: Drawable? = context.getDrawable(R.drawable.lock_icon)
    }
}

class LevelsScreen: AppCompatActivity(), LevelCellInterface.LevelCellDelegate {

    private lateinit var layout: LevelsScreenInterface.Layout
    private lateinit var appearance: LevelsScreenInterface.Appearance

    private lateinit var view: FrameLayout
    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layout = LevelsScreenInterface.Layout(this)
        appearance = LevelsScreenInterface.Appearance(this)

        setupSubview()
        setupLayout()
        setupAppearance()
        setupContent()
    }

    private fun setupSubview() {
        // Init
        view = FrameLayout(this)
        gridView = GridView(this)

        // Configure
        gridView.numColumns = 5
    }

    private fun setupLayout() {
        view.layoutParams = makeFrameLayout()
            .size(layout.parent)
            .top(220)

        gridView.layoutParams = makeLinearLayout()
            .size(layout.parent)
        val p = layout.gridViewPadding
        gridView.setPadding(p,p,p,p)
        gridView.gravity = Gravity.CENTER_HORIZONTAL
        gridView.verticalSpacing = layout.cellBetweenSpacing
        gridView.horizontalSpacing = layout.cellBetweenSpacing

        // Add parent view
        setContentView(view)
        view.addView(gridView)
    }

    private fun setupAppearance() {

    }

    private fun setupContent() {
        val levelCellInput = LevelCellInterface.Input(
            context = this,
            layout = layout,
            appearance = appearance,
            delegate = this
        )
        val levelsCount = MainManager.share.levelsAll.count()
        val lastOpenLevel = MainManager.share.lastOpenLevel
        val adapter = GridAdapter(levelCellInput, levelsCount, lastOpenLevel)
        gridView.adapter = adapter
    }

    override fun onClick(index: Int) {
        val message = "Siz sectiniz $index sevieni"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        MainManager.share.currectLevel = index
        this.finish()
    }
}