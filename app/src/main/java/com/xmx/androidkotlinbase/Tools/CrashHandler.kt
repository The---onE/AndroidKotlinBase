package com.xmx.androidkotlinbase.Tools

import android.content.Context
import android.os.Looper
import android.widget.Toast
import com.xmx.androidkotlinbase.MyApplication
import com.xmx.androidkotlinbase.Tools.OperationLog.OperationLogEntityManager

/**
 * Created by The_onE on 2017/1/16.
 * 自定义异常处理器
 */

class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    // 单例模式
    companion object {
        private var instance: CrashHandler? = null
        @Synchronized fun instance(): CrashHandler {
            if (null == instance) {
                instance = CrashHandler()
            }
            return instance!!
        }
    }

    private var mContext: Context? = null
    // 系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    // 初始化
    fun init(context: Context) {
        mContext = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置本对象为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    // 处理用户未处理的异常
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义处理未完成则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            try {
                // 等待处理完成
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                // 处理出现错误
                Toast.makeText(mContext, "出现错误:" + e, Toast.LENGTH_SHORT).show()
                MyApplication.instance().exit()
            }

            // 退出程序
            MyApplication.instance().exit()
        }
    }

    // 自定义异常处理
    private fun handleException(ex: Throwable): Boolean {
        // 打印异常堆栈跟踪
        ex.printStackTrace()

        // 记录错误日志
        OperationLogEntityManager.instance().addLog("" + ex)

        // 在新线程中显示错误信息
        object : Thread() {
            override fun run() {
                Looper.prepare()
                Toast.makeText(mContext, "出现错误" + ex, Toast.LENGTH_LONG).show()
                Looper.loop()
            }
        }.start()

        return true
    }
}