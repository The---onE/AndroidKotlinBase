package com.xmx.androidkotlinbase.core.activity

import android.os.Bundle
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseSplashActivity
import com.xmx.androidkotlinbase.core.CoreConstants
import com.xmx.androidkotlinbase.utils.Timer
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by The_onE on 2017/2/15.
 * 应用启动页，一定时间后自动或点击按钮跳转至主Activity
 */
class SplashActivity : BaseSplashActivity() {
    // 定时器，一定时间后跳转主Activity
    val timer: Timer by lazy {
        Timer {
            jumpToMainActivity()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_splash)
    }

    override fun setListener() {
        // 跳过等待，直接跳转
        btnSkip.setOnClickListener {
            timer.stop()
            timer.execute()
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 设置定时器在一定时间后跳转
        timer.start(CoreConstants.SPLASH_TIME, once = true)
    }
}
