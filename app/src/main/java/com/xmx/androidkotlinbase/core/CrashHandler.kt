package com.xmx.androidkotlinbase.core

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import kotlin.system.exitProcess

import com.xmx.androidkotlinbase.common.log.operationLogEntityManager

/**
 * Created by The_onE on 2017/1/16.
 * 自定义异常处理器，单例对象
 */
object crashHandler : Thread.UncaughtExceptionHandler {

    private var mContext: Context? = null
    // 系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * 初始化，在Application中调用
     * 调用后会替代系统默认异常处理器
     * @param[context] 当前上下文
     */
    fun init(context: Context) {
        mContext = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置本对象为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 处理用户未处理的异常
     * 调试模式记录异常信息并关闭程序
     * 非调试模式记录异常信息并重启程序
     * @param[thread] 产生异常的线程
     * @param[ex] 异常信息
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        // 处理异常
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义处理未完成则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            try {
                if (CoreConstants.EXCEPTION_DEBUG) {
                    // 在新线程中显示错误信息
                    object : Thread() {
                        override fun run() {
                            Looper.prepare()
                            mContext?.apply {
                                Toast.makeText(mContext, "出现错误:${ex.message},程序即将结束",
                                        Toast.LENGTH_LONG).show()
                            }
                            Looper.loop()
                        }
                    }.start()
                    // 等待处理完成
                    Thread.sleep(3000)
                    // 结束程序
                    MyApplication.getInstance().exit()
                } else {
                    // 在新线程中显示错误信息
                    object : Thread() {
                        override fun run() {
                            Looper.prepare()
                            Toast.makeText(mContext, "出现意料之外的错误,程序即将重启",
                                    Toast.LENGTH_LONG).show()
                            Looper.loop()
                        }
                    }.start()
                    // 等待处理完成
                    Thread.sleep(3000)
                    // 重启程序
                    exitProcess(-1)
                }
            } catch (e: InterruptedException) {
                // 处理再次出现错误
                Toast.makeText(mContext, "处理出现错误:${e.message}", Toast.LENGTH_SHORT).show()
                MyApplication.getInstance().exit()
            }

        }
    }

    /**
     * 自定义异常处理
     * @param[ex] 异常信息
     */
    private fun handleException(ex: Throwable): Boolean {
        if (CoreConstants.EXCEPTION_DEBUG) {
            // 打印异常堆栈跟踪
            Log.e("Error:", Log.getStackTraceString(ex))
            // ex.printStackTrace()
        }
        // 记录错误日志
        operationLogEntityManager.addLog("$ex")
        // 已进行处理
        return true
    }
}