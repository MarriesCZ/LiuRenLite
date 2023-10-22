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
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.toArgb
import com.marries.liurenlite.databinding.MainLayoutBinding
import com.marries.liurenlite.liuren.LiuRenLite
import com.marries.liurenlite.liuren.currentGUA
import com.marries.liurenlite.ui.theme.ColorTheme
import com.marries.liurenlite.ui.theme.ThemeColor
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val liuRenLite = LiuRenLite()
    private lateinit var mViewBinding: MainLayoutBinding
    private var mScreenHeight by Delegates.notNull<Float>()
    private var mScreenWidth by Delegates.notNull<Float>()
    private val guaTextX by lazy { mViewBinding.guaText.x }
    private val guaTextY by lazy { mViewBinding.guaText.y }
    private var viewState = ViewState.DOWN

    enum class ViewState {
        UP, ANIMATE, MHD, DOWN
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        currentGUA = liuRenLite.getGua()
        val themeColor = ColorTheme.getColorTheme(currentGUA).getThemeColor()
        updateBackground(themeColor)
        with(mViewBinding) {
            guaText.text = currentGUA.title
            timeYinText.text = liuRenLite.getTimeYin()
            duanText.text = currentGUA.duan
            dayAndHourText.text = liuRenLite.getGuaWithDayAndHour()
            monthDayHourGuaText.text = liuRenLite.getMHD()
            initState()
            findViewById<RelativeLayout>(R.id.content).setOnClickListener {
                // 是否需要跳过MHD阶段
                val isDayAndHourEmpty = dayAndHourText.text.isEmpty()
                when (viewState) {
                    ViewState.UP -> downSlidAnimator(isDayAndHourEmpty)
                    ViewState.ANIMATE -> Unit
                    ViewState.DOWN -> if (isDayAndHourEmpty) upSlidAnimator(true) else updateAnimator()
                    ViewState.MHD -> upSlidAnimator(false)
                }
            }
        }
    }

    private fun initState() {
        DisplayMetrics().apply {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(this)
        }.also {
            mScreenHeight = it.heightPixels.toFloat()
            mScreenWidth = it.widthPixels.toFloat()
        }
    }

    private fun updateBackground(themeColor: ThemeColor) {
        window.apply {
            statusBarColor = themeColor.background.toArgb()
            navigationBarColor = themeColor.background.toArgb()
            setBackgroundDrawable(ColorDrawable(themeColor.background.toArgb()))
        }
    }

    private fun upSlidAnimator(isDayAndHourEmpty: Boolean) {
        with(mViewBinding) {
            val lengthX = mScreenWidth - guaTextX - guaText.width - 2 * timeYinText.width
            ValueAnimator.ofFloat(0f, 1.0f).apply {
                duration = 600
                repeatCount = 0
                addUpdateListener {
                    val value = it.animatedValue as Float
                    guaText.x = guaTextX + value * lengthX
                    guaText.y = (1 - value) * guaTextY
                    mViewBinding.timeYinText.x = guaText.x + guaText.width
                    mViewBinding.timeYinText.y = guaText.y + guaText.height / 2 - timeYinText.height / 2
                    mViewBinding.timeYinText.alpha = value
                    if (!isDayAndHourEmpty) {
                        monthDayHourGuaText.alpha = 1.0f - value
                        dayAndHourText.alpha = 1.0f - value
                    }
                    duanText.alpha = value
                    duanText.y = mScreenHeight - duanText.height * (1 + 1.5f * value)
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
    }

    private fun updateAnimator() {
        ValueAnimator.ofFloat(0f, 1.0f).apply {
            duration = 600
            repeatCount = 0
            addUpdateListener {
                val value = it.animatedValue as Float
                mViewBinding.monthDayHourGuaText.alpha = value
                mViewBinding.dayAndHourText.alpha = value
                mViewBinding.dayAndHourText.y = mScreenHeight - mViewBinding.dayAndHourText.height * (1 + 1.5f * value)
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
        val lengthX = mScreenWidth - guaTextX - mViewBinding.guaText.width - 2 * mViewBinding.timeYinText.width
        ValueAnimator.ofFloat(1.0f, 0f).apply {
            duration = 600
            repeatCount = 0
            addUpdateListener {
                with(mViewBinding) {
                    val value = it.animatedValue as Float
                    guaText.x = guaTextX + value * lengthX
                    guaText.y = (1 - value) * guaTextY
                    timeYinText.x = guaText.x + guaText.width
                    timeYinText.y = guaText.y + guaText.height / 2 - timeYinText.height / 2
                    timeYinText.alpha = value
                    duanText.alpha = value
                    duanText.y = mScreenHeight - duanText.height * (1 + 1.5f * value)
                    if (!isDayAndHourEmpty) {
                        dayAndHourText.y = mScreenHeight - dayAndHourText.height * (1 + 1.5f * value)
                    }
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
