package com.xmx.androidkotlinbase.utils

import android.content.Context
import android.widget.Toast

import com.xmx.androidkotlinbase.common.log.operationLogEntityManager
import com.xmx.androidkotlinbase.core.CoreConstants

/**
 * Created by The_onE on 2016/11/7.
 * 异常处理类
 */
object ExceptionUtil {
    /**
     * 轻微异常处理，记录日志，不影响程序
     * @param[e] 异常信息
     * @param[context] 当前上下文
     */
    fun normalException(e: Exception, context: Context? = null) {
        // 记录错误日志
        operationLogEntityManager.addLog("$e")
        // 在调试状态显示错误信息
        if (CoreConstants.EXCEPTION_DEBUG) {
            // 打印异常堆栈跟踪
            e.printStackTrace()
            // 显示错误信息
            Toast.makeText(context, "出现异常:${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // 致命异常
    /**
     * 致命异常处理，记录日志，传递异常交由上层异常处理器处理
     * @param[e] 异常信息
     * @param[context] 当前上下文
     */
    fun fatalError(e: Exception, context: Context? = null) {
        // 记录错误日志
        operationLogEntityManager.addLog("$e")
        // 在调试状态显示错误信息
        if (CoreConstants.EXCEPTION_DEBUG) {
            // 打印异常堆栈跟踪
            e.printStackTrace()
            // 显示错误信息
            Toast.makeText(context, "致命异常:${e.message}", Toast.LENGTH_SHORT).show()
        }
        // 传递异常，交由异常处理器处理
        throw e
    }
}
