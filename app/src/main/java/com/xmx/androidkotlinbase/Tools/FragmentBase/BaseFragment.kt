package com.xmx.androidkotlinbase.Tools.FragmentBase

import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xmx.androidkotlinbase.Constants

/**
 * Created by The_onE on 2017/1/18.
 * Fragment基类，声明业务接口，提供常用功能
 */
abstract class BaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 设置Fragment对应布局
        val view = getContentView(inflater, container)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.let {
            // 初始化View
            initView(view)
            // 声明事件监听
            setListener(view)
            // 处理业务逻辑
            processLogic(view, savedInstanceState)
        }
    }

    // 设置Fragment对应布局接口
    // 实现中return inflater!!.inflate(R.layout.Fragment布局, container, false);
    protected abstract fun getContentView(inflater: LayoutInflater?, container: ViewGroup?): View

    // 初始化View接口
    protected abstract fun initView(view: View)

    // 声明事件监听接口
    protected abstract fun setListener(view: View)

    // 处理业务逻辑接口
    protected abstract fun processLogic(view: View, savedInstanceState: Bundle?)

    // 异常处理
    protected fun filterException(e: Exception): Boolean {
        if (Constants.EXCEPTION_DEBUG) {
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
        val s = str ?: ""
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    // 显示提示信息
    protected fun showToast(@IdRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
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
        val intent = Intent(context, cls)
        startActivity(intent)
    }

    // 带参数启动新Activity
    protected fun startActivity(cls: Class<*>, vararg objs: String) {
        val intent = Intent(context, cls)
        var i = 0
        while (i < objs.size) {
            intent.putExtra(objs[i], objs[++i])
            i++
        }
        startActivity(intent)
    }
}