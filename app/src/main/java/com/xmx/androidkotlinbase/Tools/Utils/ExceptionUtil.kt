package com.xmx.androidkotlinbase.Tools.Utils

import android.content.Context
import android.widget.Toast
import com.xmx.androidkotlinbase.Constants

/**
 * Created by The_onE on 2016/11/7.
 * 异常处理类
 */

object ExceptionUtil {
    // 异常处理
    fun filterException(e: Exception, context: Context? = null): Boolean {
        if (Constants.EXCEPTION_DEBUG) {
            // Debug模式
            // 打印异常堆栈跟踪
            e.printStackTrace()
            // 若可显示则显示错误信息
            context?.let { Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
            // 记录错误日志
//            OperationLogEntityManager.getInstance().addLog(e.message)
            return false
        } else {
            // 非Debug模式
            return true
        }
    }
}
