package com.xmx.androidkotlinbase.base.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // 在Application中集中管理
        MyApplication.getInstance().addService(this)

        // 处理业务逻辑
        processLogic(intent)
        // 设置服务运行于前台
        // 使用showForeground方法设置为前台服务
        setForeground(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 处理业务逻辑接口
     * @param[intent] 启动Service传递的Intent
     */
    protected abstract fun processLogic(intent: Intent)

    /**
     * 设置前台服务接口
     * 若需要设置Service为前台服务则调用[showForeground]方法
     * @param[intent] 启动Service传递的Intent
     */
    protected abstract fun setForeground(intent: Intent)

    /**
     * 设置为前台服务，提高优先级，持续显示通知
     * @param[iActivity] 点击通知时打开的Activity
     * @param[content] 通知显示的内容
     * @param[title] 通知显示的标题
     * @param[sIcon] 通知显示的图标ID
     */
    fun showForeground(iActivity: Class<*>,
                       content: String,
                       title: String = getString(R.string.app_name),
                       @IdRes sIcon: Int = R.mipmap.ic_launcher) {
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

    /**
     * 显示提示信息
     * @param[str] 要显示的字符串信息
     */
    protected fun showToast(str: String?) {
        val s = str ?: ""
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    /**
     * 显示提示信息
     * @param[resId] 要显示的字符串在strings文件中的ID
     */
    protected fun showToast(@IdRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    /**
     * 打印日志
     * @param[tag] 日志标签
     * @param[msg] 日志信息
     */
    protected fun showLog(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    /**
     * 打印日志
     * @param[tag] 日志标签
     * @param[i] 数字作为日志信息
     */
    protected fun showLog(tag: String, i: Int) {
        Log.e(tag, "$i")
    }

    /**
     * 启动新Activity
     * @param[cls] 要启动的Activity类，格式为 Activity名::class.java
     */
    protected fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    /**
     * 带参数启动新Activity
     * @param[cls] 要启动的Activity类，格式为 Activity名::class.java
     * @param[objects] 向Activity传递的参数，奇数项为键，偶数项为值
     */
    protected fun startActivity(cls: Class<*>, vararg objects: String) {
        val intent = Intent(this, cls)
        var i = 0
        while (i < objects.size) {
            intent.putExtra(objects[i], objects[++i])
            i++
        }
        startActivity(intent)
    }
}
