package com.xmx.androidkotlinbase.model.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import com.xmx.androidkotlinbase.common.user.UserConstants
import com.xmx.androidkotlinbase.common.user.userManager
import com.xmx.androidkotlinbase.utils.ExceptionUtil
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseTempActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
    }

    override fun setListener() {
        // 处理登录逻辑
        btnLogin.setOnClickListener {
            val username = tvUsername.text.toString()
            val password = tvPassword.text.toString()
            // 输入校验
            if (username.isBlank()) {
                showToast(R.string.username_empty)
            } else if (password.isBlank()) {
                showToast(R.string.password_empty)
            } else {
                // 处理登录
                btnLogin.isEnabled = false
                userManager.login(username, password,
                        success = {
                            // 登录成功
                            showToast(R.string.login_success)
                            setResult(RESULT_OK, Intent())
                            finish()
                        },
                        error = {
                            e ->
                            // 登录失败
                            when (e) {
                                UserConstants.USERNAME_ERROR -> showToast(R.string.username_error)
                                UserConstants.PASSWORD_ERROR -> showToast(R.string.password_error)
                            }
                            btnLogin.isEnabled = true
                        },
                        cloudError = {
                            e ->
                            // 网络错误，登录失败
                            showToast(R.string.network_error)
                            ExceptionUtil.normalException(e, this)
                            btnLogin.isEnabled = true
                        }
                )
            }
        }

        // 打开注册页
        btnRegister.setOnClickListener {
            startActivityForResult(Intent(this, RegisterActivity::class.java),
                    UserConstants.REGISTER_REQUEST_CODE)
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UserConstants.REGISTER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(RESULT_OK, Intent())
                finish()
            }
        }
    }
}
