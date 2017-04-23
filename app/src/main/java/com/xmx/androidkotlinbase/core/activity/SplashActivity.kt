package com.xmx.androidkotlinbase.core.activity

import android.os.Bundle
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseSplashActivity
import com.xmx.androidkotlinbase.common.user.*
import com.xmx.androidkotlinbase.core.CoreConstants
import com.xmx.androidkotlinbase.utils.ExceptionUtil
import com.xmx.androidkotlinbase.utils.Timer
import kotlinx.android.synthetic.main.activity_splash.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by The_onE on 2017/2/15.
 * 应用启动页，一定时间后自动或点击按钮跳转至主Activity
 */
class SplashActivity : BaseSplashActivity() {

    private var um: IUserManager = userManager // 用户管理器

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
        // 自动登录
        um.autoLogin(
                success = {
                    data: UserData ->
                    EventBus.getDefault().post(LoginEvent())
                },
                error = {
                    e: Int ->
                    when (e) {
                        UserConstants.NOT_LOGGED_IN -> showToast(R.string.not_loggedin)
                        UserConstants.USERNAME_ERROR -> showToast(R.string.username_error)
                        UserConstants.CHECKSUM_ERROR -> showToast(R.string.not_loggedin)
                        UserConstants.CANNOT_CHECK_LOGIN -> showToast(R.string.cannot_check_login)
                    }
                },
                cloudError = {
                    e ->
                    showToast(R.string.network_error)
                    ExceptionUtil.normalException(e, this)
                }
        )
    }
}
