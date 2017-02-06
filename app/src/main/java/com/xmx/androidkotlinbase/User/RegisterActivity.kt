package com.xmx.androidkotlinbase.User

import android.content.Intent
import android.os.Bundle

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.Tools.ActivityBase.BaseTempActivity
import com.xmx.androidkotlinbase.Tools.User.UserConstants
import com.xmx.androidkotlinbase.Tools.User.UserManager
import com.xmx.androidkotlinbase.Tools.Utils.ExceptionUtil
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseTempActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_register)
    }

    override fun setListener() {
        // 处理注册逻辑
        btnRegister.setOnClickListener {
            val nickname = editNickname.text.toString()
            val username = editUsername.text.toString()
            val password = editPassword.text.toString()
            val password2 = editPassword2.text.toString()

            // 输入校验
            if (nickname.isBlank()) {
                showToast(R.string.nickname_hint)
                return@setOnClickListener
            }
            if (username.isBlank()) {
                showToast(R.string.username_empty)
                return@setOnClickListener
            }
            if (password == "") {
                showToast(R.string.password_empty)
                return@setOnClickListener
            }
            if (password != password2) {
                showToast(R.string.password_different)
                return@setOnClickListener
            }

            // 处理注册
            btnRegister.isEnabled = false
            UserManager.instance().register(username, password, nickname,
                    success = {
                        // 注册成功
                        showToast(R.string.register_success)
                        setResult(RESULT_OK, Intent())
                        finish()
                    },
                    error = {
                        e ->
                        // 注册失败
                        when (e) {
                            UserConstants.USERNAME_EXIST -> showToast(R.string.username_exist)
                            UserConstants.NICKNAME_EXIST -> showToast(R.string.nickname_exist)
                        }
                        btnRegister.isEnabled = true
                    },
                    cloudError = {
                        e ->
                        // 网络错误，注册失败
                        showToast(R.string.network_error)
                        ExceptionUtil.normalException(e, this)
                        btnRegister.isEnabled = true
                    })
        }

        // 取消注册
        btnCancel.setOnClickListener {
            finish()
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }
}
