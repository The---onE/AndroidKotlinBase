package com.xmx.androidkotlinbase

import android.app.Activity
import com.xmx.androidkotlinbase.Tools.CrashHandler
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by The_onE on 2017/1/16.
 * 自定义Application，单例类，初始化各插件
 */

class MyApplication : android.app.Application() {
    // 单例模式
    companion object {
        private var instance: MyApplication? = null
        fun instance(): MyApplication = instance!!
    }

    // 运行中的Activity容器
    private val activityList = LinkedList<Activity>()
    // 添加Activity到容器中
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }
    // 退出程序，遍历所有Activity并finish
    fun exit() {
        for (activity in activityList) {
            activity.finish()
        }
        System.exit(0)
    }

    // 初始化过程
    override fun onCreate() {
        super.onCreate()
        instance = this

        // 注册异常处理器
        val crashHandler = CrashHandler.instance()
        crashHandler.init(this)
    }
}