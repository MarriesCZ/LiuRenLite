package com.marries.liurenlite

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.toArgb
import com.marries.liurenlite.liuren.LiuRenLite
import com.marries.liurenlite.liuren.currentGUA
import com.marries.liurenlite.ui.theme.ColorTheme
import com.marries.liurenlite.ui.theme.ThemeColor
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val liuRenLite = LiuRenLite()
    private lateinit var mGuaText: TextView
    private lateinit var mTimeYin: TextView
    private lateinit var mDuanText: TextView
    private lateinit var mDayAndHourText: TextView
    private lateinit var mGuaMHDText: TextView
    private var mScreenHeight by Delegates.notNull<Float>()
    private var mScreenWidth by Delegates.notNull<Float>()
    private val mGuaTextX by lazy { mGuaText.x }
    private val mGuaTextY by lazy { mGuaText.y }
    private var viewState = ViewState.DOWN

    enum class ViewState {
        UP, ANIMATE, MHD, DOWN
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        currentGUA = liuRenLite.getGua()
        val themeColor = ColorTheme.getColorTheme(currentGUA).getThemeColor()
        updateBackground(themeColor)
        mGuaText = findViewById<TextView>(R.id.gua_text).apply { text = currentGUA.title }
        mTimeYin = findViewById<TextView>(R.id.time_yin_text).apply { text = liuRenLite.getTimeYin() }
        mDuanText = findViewById<TextView>(R.id.duan_text).apply { text = currentGUA.duan }
        mDayAndHourText = findViewById<TextView>(R.id.day_and_hour_text).apply { text = liuRenLite.getGuaWithDayAndHour() }
        mGuaMHDText = findViewById<TextView>(R.id.month_day_hour_gua_text).apply { text = liuRenLite.getMHD() }
        initState()
        findViewById<RelativeLayout>(R.id.content).setOnClickListener {
            // 是否需要跳过MHD阶段
            val isDayAndHourEmpty = mDayAndHourText.text.isEmpty()
            when (viewState) {
                ViewState.UP -> downSlidAnimator(isDayAndHourEmpty)
                ViewState.ANIMATE -> Unit
                ViewState.DOWN -> if (isDayAndHourEmpty) upSlidAnimator(true) else updateAnimator()
                ViewState.MHD -> upSlidAnimator(false)
            }
        }
    }

    private fun initState() {
        val dm = DisplayMetrics()
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(dm)
        mScreenHeight = dm.heightPixels.toFloat()
        mScreenWidth = dm.widthPixels.toFloat()
    }

    private fun updateBackground(themeColor: ThemeColor) {
        window.statusBarColor = themeColor.background.toArgb()
        window.navigationBarColor = themeColor.background.toArgb()
        window.setBackgroundDrawable(ColorDrawable(themeColor.background.toArgb()))
    }

    private fun upSlidAnimator(isDayAndHourEmpty: Boolean) {
        val lengthX = mScreenWidth - mGuaTextX - mGuaText.width - 2 * mTimeYin.width
        ValueAnimator.ofFloat(0f, 1.0f).apply {
            duration = 600
            repeatCount = 0
            addUpdateListener {
                val value = it.animatedValue as Float
                mGuaText.x = mGuaTextX + value * lengthX
                mGuaText.y = (1 - value) * mGuaTextY
                mTimeYin.x = mGuaText.x + mGuaText.width
                mTimeYin.y = mGuaText.y + mGuaText.height / 2 - mTimeYin.height / 2
                mTimeYin.alpha = value
                if (!isDayAndHourEmpty) {
                    mGuaMHDText.alpha = 1.0f - value
                    mDayAndHourText.alpha = 1.0f - value
                }
                mDuanText.alpha = value
                mDuanText.y = mScreenHeight - mDuanText.height * (1 + 1.5f * value)
            }
            addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    viewState = ViewState.ANIMATE
                }

                override fun onAnimationEnd(animation: Animator) {
                    viewState = ViewState.UP
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            start()
        }
    }

    private fun updateAnimator() {
        ValueAnimator.ofFloat(0f, 1.0f).apply {
            duration = 600
            repeatCount = 0
            addUpdateListener {
                val value = it.animatedValue as Float
                mGuaMHDText.alpha = value
                mDayAndHourText.alpha = value
                mDayAndHourText.y = mScreenHeight - mDayAndHourText.height * (1 + 1.5f * value)
            }
            addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    viewState = ViewState.ANIMATE
                }

                override fun onAnimationEnd(animation: Animator) {
                    viewState = ViewState.MHD
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            start()
        }
    }

    private fun downSlidAnimator(isDayAndHourEmpty: Boolean) {
        val lengthX = mScreenWidth - mGuaTextX - mGuaText.width - 2 * mTimeYin.width
        ValueAnimator.ofFloat(1.0f, 0f).apply {
            duration = 600
            repeatCount = 0
            addUpdateListener {
                val value = it.animatedValue as Float
                mGuaText.x = mGuaTextX + value * lengthX
                mGuaText.y = (1 - value) * mGuaTextY
                mTimeYin.x = mGuaText.x + mGuaText.width
                mTimeYin.y = mGuaText.y + mGuaText.height / 2 - mTimeYin.height / 2
                mTimeYin.alpha = value
                mDuanText.alpha = value
                mDuanText.y = mScreenHeight - mDuanText.height * (1 + 1.5f * value)
                if (!isDayAndHourEmpty) {
                    mDayAndHourText.y = mScreenHeight - mDayAndHourText.height * (1 + 1.5f * value)
                }
            }
            addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    viewState = ViewState.ANIMATE
                }

                override fun onAnimationEnd(animation: Animator) {
                    viewState = ViewState.DOWN
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            start()
        }
    }
}
