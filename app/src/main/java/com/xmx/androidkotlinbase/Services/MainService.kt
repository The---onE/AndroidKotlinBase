package com.xmx.androidkotlinbase.Services

import android.content.Intent
import com.xmx.androidkotlinbase.MainActivity

import com.xmx.androidkotlinbase.Tools.OperationLog.OperationLogEntityManager
import com.xmx.androidkotlinbase.Tools.ServiceBase.BaseService
import com.xmx.androidkotlinbase.Tools.Timer

class MainService : BaseService() {

    private var time = System.currentTimeMillis()
    private var timer = Timer {
        val now = System.currentTimeMillis()
        showToast("服务已运行${now - time}毫秒")
        OperationLogEntityManager.instance().addLog("服务已运行${now - time}毫秒")
    }

    override fun processLogic(intent: Intent) {
        timer.start(5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        OperationLogEntityManager.instance().addLog("服务停止")

        timer.stop()
    }

    override fun setForeground(intent: Intent) {
        showForeground(MainActivity::class.java, "正在运行")
    }
}
