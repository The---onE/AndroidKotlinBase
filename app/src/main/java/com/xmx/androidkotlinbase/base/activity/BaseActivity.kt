package com.xmx.androidkotlinbase.base.activity

import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.xmx.androidkotlinbase.core.MyApplication

/**
 * Created by The_onE on 2017/1/16.
 * Activity基类，声明业务接口，提供常用功能
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 在Application中集中管理
        MyApplication.getInstance().addActivity(this)

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

    /**
     * 初始化View接口
     * @param[savedInstanceState] [onCreate]方法中的实例状态
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 声明事件监听接口
     */
    protected abstract fun setListener()

    /**
     * 处理业务逻辑接口
     * @param[savedInstanceState] [onCreate]方法中的实例状态
     */
    protected abstract fun processLogic(savedInstanceState: Bundle?)

    /**
     * 设置layout文件后的默认处理
     */
    protected fun onViewCreated() {
    }

    /**
     * 设置Activity对应layout
     * @param[layoutResID] Activity对应的layout文件ID
     */
    override fun setContentView(@IdRes layoutResID: Int) {
        super.setContentView(layoutResID)
        onViewCreated()
    }

    /**
     * 设置Activity对应layout
     * @param[view] Activity对应的View
     */
    override fun setContentView(view: View) {
        super.setContentView(view)
        onViewCreated()
    }

    /**
     * 设置Activity对应layout
     * @param[view] Activity对应的View
     * @param[params] view的属性
     */
    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)
        onViewCreated()
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