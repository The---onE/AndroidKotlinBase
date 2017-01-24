package com.xmx.androidkotlinbase.User

import android.content.Intent
import android.os.Bundle

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.Tools.ActivityBase.BaseTempActivity
import com.xmx.androidkotlinbase.Tools.User.UserConstants
import com.xmx.androidkotlinbase.Tools.User.UserManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseTempActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
    }

    override fun setListener() {
        btnLogin.setOnClickListener {
            val username = tvUsername.text.toString()
            val password = tvPassword.text.toString()
            if (username.isBlank()) {
                showToast(R.string.username_empty)
            } else if (password.isBlank()) {
                showToast(R.string.password_empty)
            } else {
                btnLogin.isEnabled = false
                UserManager.instance().login(username, password,
                        success = {
                            showToast(R.string.login_success)
                            val i = Intent()
                            setResult(RESULT_OK, i)
                            finish()
                        },
                        error = {
                            e ->
                            when (e) {
                                UserConstants.USERNAME_ERROR -> {
                                    showToast(R.string.username_error)
                                    btnLogin.isEnabled = true
                                }
                                UserConstants.PASSWORD_ERROR -> {
                                    showToast(R.string.password_error)
                                    btnLogin.isEnabled = true
                                }
                            }
                        },
                        cloudError = {
                            e ->
                            showToast(R.string.network_error)
                            filterException(e)
                            btnLogin.isEnabled = true
                        }
                )
            }
        }

        btnRegister.setOnClickListener { startActivity(RegisterActivity::class.java) }
    }

    override fun processLogic(savedInstanceState: Bundle?) {

    }
}
