package com.iw.gizlysoz.LevelsScreen

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.iw.gizlysoz.Extension.*
import com.iw.gizlysoz.ProjectManagers.MainManager
import com.iw.gizlysoz.R

interface LevelsScreenInterface {

    data class Input(
        val viewModel: Context
    )
}

class LevelsScreenLayout(context: Context): Layout(context), LevelCellInterface.Layout {
    override val cellSize: Int = 160
    val cellBetweenSpacing: Int = 25
    val gridTop: Int = 50
    val gridHorizontal: Int = 30
}

class LevelsScreenAppearance(context: Context): Appearance(context), LevelCellInterface.Appearance {
    override val cellBackground: Int = R.color.teal_200
    override val cellTextFont: Typeface = Typeface.DEFAULT
    override val cellTextSize: Float = 30f
    override val cellTextColor: Int = R.color.textPrimary
    override val cellLockIcon: Drawable? = context.getDrawable(R.drawable.lock_icon)
    override val cellLockIconScale: Float = 0.7f
    val backImage: Drawable? = ContextCompat.getDrawable(context, R.drawable.back1)
}

class LevelsActivity: BaseActivity(), LevelCellInterface.LevelCellDelegate {

    private lateinit var layout: LevelsScreenLayout
    private lateinit var appearance: LevelsScreenAppearance

    private var backImageView: ImageView? = null
    private var gridView: GridView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layout = LevelsScreenLayout(this)
        appearance = LevelsScreenAppearance(this)

        setupSubview()
        setupLayout()
        setupAppearance()
        setupContent()
    }

    private fun setupSubview() {

        // Init subview
        backImageView = ImageView(this)
        gridView = GridView(this)

        // Init id
        backImageView?.id = backImageView.hashCode()
        gridView?.id = gridView.hashCode()

        // addView
        view?.addView(backImageView)
        view?.addView(gridView)

        // Configure
        backImageView?.scaleType = ImageView.ScaleType.CENTER_CROP
        backImageView?.setImageDrawable(appearance.backImage)
        gridView?.numColumns = 5
    }

    private fun setupLayout() {

        val set = ConstraintSet()

        backImageView?.let {
            set.connect(it.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            set.connect(it.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            set.connect(it.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            set.connect(it.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        }

        gridView?.let {
//            set.constrainWidth(it.id, layout.parent)
            set.constrainHeight(it.id, layout.parent)
            set.connect(it.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, layout.gridTop)
            set.connect(it.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, layout.gridHorizontal)
            set.connect(it.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, layout.gridHorizontal)
//            set.centerHorizontally(it.id, ConstraintSet.PARENT_ID)
        }

        set.applyTo(view)

//        val p = layout.gridViewPadding
//        gridView?.setPadding(p,p,p,p)
//        gridView?.gravity = Gravity.CENTER_HORIZONTAL
        gridView?.verticalSpacing = layout.cellBetweenSpacing
        gridView?.horizontalSpacing = layout.cellBetweenSpacing
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
        val currectLevel = MainManager.share.currentLevel
        val lastOpenLevel = MainManager.share.lastOpenLevel
        val adapter = GridAdapter(levelCellInput, levelsCount, currectLevel, lastOpenLevel)
        gridView?.adapter = adapter
    }

    override fun onClick(index: Int) {
        if(MainManager.share.lastOpenLevel >= index) {
            val message = "Siz sectiniz $index sevieni"
            android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
            com.iw.gizlysoz.ProjectManagers.MainManager.share.currentLevel = index
            this.finish()
        }
    }
}