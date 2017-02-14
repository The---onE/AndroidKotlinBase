package com.xmx.androidkotlinbase.base.activity

import android.content.Intent
import android.os.Bundle
import com.xmx.androidkotlinbase.core.activity.MainActivity

/**
 * Created by The_onE on 2017/1/18.
 * 启动Activity基类，APP启动页，预处理部分数据后跳转至内容页
 */
abstract class BaseSplashActivity : BaseActivity() {
    // 打开主Activity，结束自身
    fun jumpToMainActivity() {
        startActivity(MainActivity::class.java)
        finish()
    }
}
