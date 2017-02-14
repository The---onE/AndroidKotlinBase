package com.xmx.androidkotlinbase.utils

import android.content.Context
import android.widget.Toast
import com.xmx.androidkotlinbase.core.CoreConstants
import com.xmx.androidkotlinbase.core.MyApplication
import com.xmx.androidkotlinbase.common.log.OperationLogEntityManager

/**
 * Created by The_onE on 2016/11/7.
 * 异常处理类
 */

object ExceptionUtil {
    // 轻微异常
    fun normalException(e: Exception, context: Context? = null) {
        // 记录错误日志
        OperationLogEntityManager.instance().addLog("$e")
        // 在调试状态显示错误信息
        if (CoreConstants.EXCEPTION_DEBUG) {
            // 打印异常堆栈跟踪
            e.printStackTrace()
            // 显示错误信息
            Toast.makeText(context, "出现异常:${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // 致命异常
    fun fatalError(e: Exception, context: Context? = null) {
        // 记录错误日志
        OperationLogEntityManager.instance().addLog("$e")
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
