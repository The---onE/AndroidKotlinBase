package com.xmx.androidkotlinbase.core.fragment


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.fragment.BaseFragment
import com.xmx.androidkotlinbase.common.notification.NotificationTempActivity
import com.xmx.androidkotlinbase.utils.NotificationUtils
import kotlinx.android.synthetic.main.fragment_notification.*

/**
 * Created by The_onE on 2017/4/10.
 * 测试消息通知组件是否运行正常，演示其使用方法
 */
class NotificationFragment : BaseFragment() {

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

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
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {
    }
}
