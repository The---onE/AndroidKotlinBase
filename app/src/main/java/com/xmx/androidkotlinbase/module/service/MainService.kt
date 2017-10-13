package com.xmx.androidkotlinbase.module.service

import android.content.Intent
import android.os.IBinder
import com.xmx.androidkotlinbase.R

import com.xmx.androidkotlinbase.core.activity.MainActivity
import com.xmx.androidkotlinbase.base.service.BaseService
import com.xmx.androidkotlinbase.common.log.OperationLogEntityManager
import com.xmx.androidkotlinbase.utils.Timer

/**
 * Created by The_onE on 2016/7/1.
 * 测试服务，显示通知并定时记录日志
 */
class MainService : BaseService() {
    override fun onBind(p0: Intent?): IBinder? = null

    private var time = System.currentTimeMillis()
    // 定时任务
    private var timer = Timer {
        val now = System.currentTimeMillis()
        showToast("服务已运行${now - time}毫秒")
        OperationLogEntityManager.addLog("服务已运行${now - time}毫秒")
    }

    override fun processLogic(intent: Intent) {
        // 每隔5秒执行一次任务
        timer.start(5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止服务
        OperationLogEntityManager.addLog("服务停止")
        timer.stop()
    }

    override fun setForeground(intent: Intent) {
        val title = intent.getStringExtra("title") ?: getString(R.string.app_name)
        val content = intent.getStringExtra("content") ?: "正在运行"
        // 设置前台服务
        showForeground(MainActivity::class.java, content, title)
    }
}
