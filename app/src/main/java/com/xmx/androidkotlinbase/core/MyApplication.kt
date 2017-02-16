package com.xmx.androidkotlinbase.core

import android.app.Activity
import android.app.Application
import android.app.Service
import com.avos.avoscloud.AVOSCloud

import com.xmx.androidkotlinbase.common.user.userManager
import java.util.*

/**
 * Created by The_onE on 2017/1/16.
 * 自定义Application，应用唯一实例
 * 单例类，初始化各插件，管理各组件
 */
class MyApplication : Application() {
    // 单例模式
    companion object {
        private var ins: MyApplication? = null
        fun getInstance(): MyApplication = ins!!
    }

    // 运行中的Activity容器
    private val activityList = LinkedList<Activity>()
    // 运行中的Service容器
    private val serviceList = LinkedList<Service>()

    /**
     * 添加Activity到容器中
     * @param[activity] 要添加的Activity
     */
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    /**
     * 添加Service到容器中
     * @param[service] 要添加的Service
     */
    fun addService(service: Service) {
        serviceList.add(service)
    }

    /**
     * 退出程序，关闭所有相关组件
     */
    fun exit() {
        activityList.forEach { it.finish() }
        serviceList.forEach { it.stopSelf() }
        System.exit(0)
    }

    override fun onCreate() {
        super.onCreate()
        // 设置当前实例
        ins = this

        // 注册异常处理器
        crashHandler.init(this)

        // 初始化LeanCloud
        AVOSCloud.initialize(this, CoreConstants.APP_ID, CoreConstants.APP_KEY)
        // 初始化用户管理器
        userManager.init(this)
    }
}