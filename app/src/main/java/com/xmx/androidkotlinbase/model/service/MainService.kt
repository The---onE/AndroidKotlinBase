package com.xmx.androidkotlinbase.model.service

import android.content.Intent
import android.os.IBinder

import com.xmx.androidkotlinbase.core.activity.MainActivity
import com.xmx.androidkotlinbase.base.service.BaseService
import com.xmx.androidkotlinbase.common.log.operationLogEntityManager
import com.xmx.androidkotlinbase.utils.Timer

class MainService : BaseService() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private var time = System.currentTimeMillis()
    private var timer = Timer {
        val now = System.currentTimeMillis()
        showToast("服务已运行${now - time}毫秒")
        operationLogEntityManager.addLog("服务已运行${now - time}毫秒")
    }

    override fun processLogic(intent: Intent) {
        timer.start(5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        operationLogEntityManager.addLog("服务停止")

        timer.stop()
    }

    override fun setForeground(intent: Intent) {
        showForeground(MainActivity::class.java, "正在运行")
    }
}
