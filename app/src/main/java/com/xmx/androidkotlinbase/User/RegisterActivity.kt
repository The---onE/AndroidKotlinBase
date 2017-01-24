package com.xmx.androidkotlinbase.User

import android.os.Bundle

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.Tools.ActivityBase.BaseTempActivity
import com.xmx.androidkotlinbase.Tools.User.UserConstants
import com.xmx.androidkotlinbase.Tools.User.UserManager
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseTempActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_register)
    }

    override fun setListener() {
        btnRegister.setOnClickListener {
            val nickname = editNickname.text.toString()
            val username = editUsername.text.toString()
            val password = editPassword.text.toString()
            val password2 = editPassword2.text.toString()

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

            btnRegister.isEnabled = false
            UserManager.instance().register(username, password, nickname,
                    success = {
                        showToast(R.string.register_success)
                        finish()
                    },
                    error = {
                        e ->
                        when (e) {
                            UserConstants.USERNAME_ERROR -> {
                                showToast(R.string.username_exist)
                                btnRegister.isEnabled = true
                            }
                            UserConstants.NICKNAME_EXIST -> {
                                showToast(R.string.nickname_exist)
                                btnRegister.isEnabled = true
                            }
                        }
                    },
                    cloudError = {
                        e ->
                        showToast(R.string.network_error)
                        filterException(e)
                        btnRegister.isEnabled = true
                    })
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }
}
