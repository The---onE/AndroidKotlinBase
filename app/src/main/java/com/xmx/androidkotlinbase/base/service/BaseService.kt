package com.xmx.androidkotlinbase.base.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.annotation.IdRes
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.xmx.androidkotlinbase.core.MyApplication

import com.xmx.androidkotlinbase.R

/**
 * Created by The_onE on 2016/7/1.
 * Service基类，声明业务接口，提供常用功能
 */
abstract class BaseService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // 在Application中集中管理
        MyApplication.instance().addService(this)

        // 处理业务逻辑
        processLogic(intent)
        // 设置服务运行于前台
        // 使用showForeground方法设置为前台服务
        setForeground(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    // 处理业务逻辑接口
    protected abstract fun processLogic(intent: Intent)

    // 设置前台服务接口
    protected abstract fun setForeground(intent: Intent)

    // 设置为前台服务，提高优先级，持续显示通知
    // iActivity 点击通知时打开的Activity
    fun showForeground(iActivity: Class<*>, content: String) {
        showForeground(iActivity, R.mipmap.ic_launcher, getString(R.string.app_name), content)
    }

    // 设置为前台服务，提高优先级，持续显示通知
    // iActivity 点击通知时打开的Activity
    fun showForeground(iActivity: Class<*>, title: String, content: String) {
        showForeground(iActivity, R.mipmap.ic_launcher, title, content)
    }

    // 设置为前台服务，提高优先级，持续显示通知
    // iActivity 点击通知时打开的Activity
    fun showForeground(iActivity: Class<*>, @IdRes sIcon: Int,
                       title: String, content: String) {
        // 设置通知中的内容
        val notificationId = -1
        val notificationIntent = Intent(this, iActivity)
        val contentIntent = PendingIntent.getActivity(this, notificationId,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(sIcon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent)
                .setWhen(0)
        val notification = mBuilder.build()
        // 持续显示通知
        startForeground(notificationId, notification)
    }

    // 显示提示信息
    protected fun showToast(str: String?) {
        val s = str ?: ""
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    // 显示提示信息
    protected fun showToast(@IdRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    // 打印日志
    protected fun showLog(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    // 打印日志
    protected fun showLog(tag: String, i: Int) {
        Log.e(tag, "$i")
    }

    // 启动新Activity
    // 格式为startActivity(Activity名::class.java)
    protected fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    // 带参数启动新Activity
    protected fun startActivity(cls: Class<*>, vararg objs: String) {
        val intent = Intent(this, cls)
        var i = 0
        while (i < objs.size) {
            intent.putExtra(objs[i], objs[++i])
            i++
        }
        startActivity(intent)
    }
}
