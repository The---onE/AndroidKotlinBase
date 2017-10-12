package com.xmx.androidkotlinbase.core.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.fragment.BaseFragment
import com.xmx.androidkotlinbase.common.user.IUserManager
import com.xmx.androidkotlinbase.common.user.UserConstants
import com.xmx.androidkotlinbase.common.user.UserData
import com.xmx.androidkotlinbase.common.user.UserManager
import com.xmx.androidkotlinbase.utils.ExceptionUtil
import com.xmx.androidkotlinbase.module.user.LoginActivity
import com.xmx.androidkotlinbase.module.user.RegisterActivity
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * Created by The_onE on 2017/1/18.
 * 测试用户管理组件是否运行正常，演示其使用方法
 */
class UserFragment : BaseFragment() {

    private var um: IUserManager = UserManager // 用户管理器

    // 是否已成功登录
    var loginFlag = false

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun setListener(view: View) {
        // 打开登录页
        btnLogin.setOnClickListener {
            startActivityForResult(Intent(context, LoginActivity::class.java),
                    UserConstants.LOGIN_REQUEST_CODE)
        }
        // 打开注册页
        btnRegister.setOnClickListener {
            startActivityForResult(Intent(context, RegisterActivity::class.java),
                    UserConstants.REGISTER_REQUEST_CODE)
        }
        // 注销
        btnLogout.setOnClickListener {
            um.logout {
                showToast(R.string.logout_success)
                loginFlag = false
                tvStatus.text = "当前状态：未登陆"
                tvUserData.text = "帐号： \n昵称： "

                btnLogin.visibility = VISIBLE
                btnRegister.visibility = VISIBLE
                btnLogout.visibility = GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 在登录页登录成功或注册页注册成功
        if (requestCode == UserConstants.LOGIN_REQUEST_CODE
                || requestCode == UserConstants.REGISTER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                loginFlag = true
            }
        }
    }

    // 登录成功的处理
    val loginSuccess = {
        data: UserData ->
        loginFlag = true
        tvStatus.text = "当前状态：已登陆"
        tvUserData.text = "帐号：${data.username}\n昵称：${data.nickname}"

        btnLogin.visibility = GONE
        btnRegister.visibility = GONE
        btnLogout.visibility = VISIBLE
    }

    // 登录失败的处理
    val loginError = {
        e: Int ->
        when (e) {
            UserConstants.NOT_LOGGED_IN -> showToast(R.string.not_loggedin)
            UserConstants.USERNAME_ERROR -> showToast(R.string.username_error)
            UserConstants.CHECKSUM_ERROR -> showToast(R.string.not_loggedin)
            UserConstants.CANNOT_CHECK_LOGIN -> showToast(R.string.cannot_check_login)
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {
        // 在SplashActivity中自动登录，在此校验登录
        um.checkLogin(
                success = loginSuccess,
                error = loginError,
                cloudError = {
                    e ->
                    showToast(R.string.network_error)
                    ExceptionUtil.normalException(e, context)
                }
        )
    }

    override fun onResume() {
        super.onResume()
        // 登录成功后，校验登录获取用户数据
        if (loginFlag) {
            um.checkLogin(
                    success = loginSuccess,
                    error = loginError,
                    cloudError = {
                        e ->
                        showToast(R.string.network_error)
                        ExceptionUtil.normalException(e, context)
                    }
            )
        }
    }
}