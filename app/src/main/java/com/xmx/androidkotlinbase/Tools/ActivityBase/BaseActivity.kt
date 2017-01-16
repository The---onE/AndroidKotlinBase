package com.xmx.androidkotlinbase.Tools.ActivityBase

import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xmx.androidkotlinbase.MyApplication
import com.xmx.androidkotlinbase.Constants

/**
 * Created by The_onE on 2017/1/16.
 * Activity基类，初始化插件，声明业务接口，提供常用功能
 */

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 在Application中集中管理
        MyApplication.instance().addActivity(this)

        // 初始化View
        initView(savedInstanceState)
        // 声明事件监听
        setListener()
        // 处理业务逻辑
        processLogic(savedInstanceState)
    }

    // 在Kotlin中无需获取控件对象，直接使用ID即可
    // import kotlinx.android.synthetic.布局文件.*
    // 控件ID.setText(文本)
    // 控件ID.setOnClickListener { 点击处理 }
//    protected fun <VT : View> getViewById(@IdRes id: Int): VT {
//        return findViewById(id) as VT
//    }

    // 初始化View接口
    protected abstract fun initView(savedInstanceState: Bundle?)

    // 声明事件监听接口
    protected abstract fun setListener()

    // 处理业务逻辑接口
    protected abstract fun processLogic(savedInstanceState: Bundle?)

    // 统一处理View
    protected fun onViewCreated() {}

    // 设置Activity对应layout
    override fun setContentView(@IdRes layoutResID: Int) {
        super.setContentView(layoutResID)
        onViewCreated()
    }

    // 设置Activity对应layout
    override fun setContentView(view: View) {
        super.setContentView(view)
        onViewCreated()
    }

    // 设置Activity对应layout
    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)
        onViewCreated()
    }

    // 异常处理
    protected fun filterException(e: Exception): Boolean {
        if (e != null && Constants.EXCEPTION_DEBUG) {
            // Debug模式
            // 打印异常堆栈跟踪
            e.printStackTrace()
            // 显示错误信息
            showToast(e.message)
            // 记录错误日志
//            OperationLogEntityManager.getInstance().addLog(e.message)
            return false
        } else {
            // 非Debug模式
            return true
        }
    }

    // 显示提示信息
    protected fun showToast(str: String?) {
        if (str == null) {
            str == ""
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
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