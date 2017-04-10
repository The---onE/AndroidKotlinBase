package com.xmx.androidkotlinbase.common.notification

import android.app.Activity
import android.os.Bundle

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.utils.NotificationUtils

/**
 * Created by The_onE on 2017/4/10.
 * 点击消息通知后的空Intent，移除消息通知后关闭
 */
class NotificationTempActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_temp)
        // 移除消息通知
        val id = intent.getIntExtra("notificationId", 0)
        NotificationUtils.removeNotification(this, id)
        // 关闭Activity
        finish()
    }
}
