package com.xmx.androidkotlinbase.model.service

import android.content.Intent
import android.os.IBinder

import com.xmx.androidkotlinbase.core.activity.MainActivity
import com.xmx.androidkotlinbase.base.service.BaseService
import com.xmx.androidkotlinbase.common.log.operationLogEntityManager
import com.xmx.androidkotlinbase.utils.Timer

/**
 * Created by The_onE on 2016/7/1.
 * 测试服务，显示通知并定时记录日志
 */
class MainService : BaseService() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private var time = System.currentTimeMillis()
    // 定时任务
    private var timer = Timer {
        val now = System.currentTimeMillis()
        showToast("服务已运行${now - time}毫秒")
        operationLogEntityManager.addLog("服务已运行${now - time}毫秒")
    }

    override fun processLogic(intent: Intent) {
        // 每隔5秒执行一次任务
        timer.start(5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止服务
        operationLogEntityManager.addLog("服务停止")
        timer.stop()
    }

    override fun setForeground(intent: Intent) {
        // 设置前台服务
        showForeground(MainActivity::class.java, "正在运行")
    }
}