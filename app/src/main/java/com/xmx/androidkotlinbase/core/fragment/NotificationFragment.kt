package com.xmx.androidkotlinbase.core.fragment


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.fragment.BaseFragment
import com.xmx.androidkotlinbase.common.log.OperationLogEntityManager
import com.xmx.androidkotlinbase.common.notification.NotificationTempActivity
import com.xmx.androidkotlinbase.module.service.MainService
import com.xmx.androidkotlinbase.utils.NotificationUtils
import kotlinx.android.synthetic.main.fragment_notification.*

/**
 * Created by The_onE on 2017/4/10.
 * 测试消息通知组件是否运行正常，演示其使用方法
 */
class NotificationFragment : BaseFragment() {

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View =
            inflater.inflate(R.layout.fragment_notification, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun setListener(view: View) {
        // 发送通知
        btnRemind.setOnClickListener {
            // 获取数据
            val id = editId.text.toString()
            val i = id.hashCode()
            val title = editTitle.text.toString()
            val content = editContent.text.toString()
            // 生成临时Intent
            val intent = Intent(context, NotificationTempActivity::class.java)
            intent.putExtra("notificationId", i)
            // 发送通知
            NotificationUtils.showNotification(context, i, intent, title, content)
            showToast("已发送通知")
        }
        // 发送持续通知
        btnNotification.setOnClickListener {
            // 获取数据
            val id = editId.text.toString()
            val i = id.hashCode()
            val title = editTitle.text.toString()
            val content = editContent.text.toString()
            // 生成临时Intent
            val intent = Intent(context, NotificationTempActivity::class.java)
            intent.putExtra("notificationId", i)
            // 发送持续通知
            NotificationUtils.showNotification(context, i, intent, title, content, true, true)
            showToast("已发送持续通知")
        }
        // 根据ID移除通知
        btnRemoveNotification.setOnClickListener {
            // 获取ID
            val id = editId.text.toString()
            val i = id.hashCode()
            // 移除通知
            NotificationUtils.removeNotification(context, i)
            showToast("已移除通知")
        }

        // 运行服务
        btnStartService.setOnClickListener {
            val service = Intent(context, MainService::class.java)
            val title = editTitle.text.toString()
            if (title.isNotBlank()) {
                service.putExtra("title", title)
            }
            val content = editContent.text.toString()
            if (content.isNotBlank()) {
                service.putExtra("content", content)
            }
            context.startService(service)
            OperationLogEntityManager.addLog("开启服务")
            showToast("已开启服务")
        }

        // 停止服务
        btnStopService.setOnClickListener {
            // 获取系统服务管理器
            val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            // 设置查询上限
            val defaultNum = 1000
            // 查询运行的服务
            val runServiceList = manager
                    .getRunningServices(defaultNum)
            var flag = false
            runServiceList.forEach {
                // 查询前台服务
                if (it.foreground) {
                    // 根据包名查询服务，当前应用创建的进程没有应用包名
                    if (it.service.shortClassName == ".module.service.MainService") {
                        // 关闭服务
                        val intent = Intent()
                        intent.component = it.service
                        context.stopService(intent)
                        showToast("已关闭服务")
                        flag = true
                    }
                }
            }
            if (!flag) {
                showToast("服务未开启")
            }
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {
    }
}
