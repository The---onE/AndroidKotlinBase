package com.xmx.androidkotlinbase.utils

import android.content.Context
import android.support.annotation.StringRes
import android.util.Log
import android.widget.Toast

/**
 * Created by The_onE on 2017/2/18.
 * 字符串处理类
 */
object StringUtil {

    /**
     * 连接字符串
     *
     * @param items     待连接的字符串列表
     * @param separator 分隔字符串
     * @return 格式化后的文本
     */
    fun join(items: List<String>, separator: String): String {
        val sb = StringBuffer()
        sb.append(items[0])
        for (i in 1 until items.size) {
            sb.append(separator)
            sb.append(items[i])
        }
        return String(sb)
    }

    /**
     * 显示提示信息
     * @param[context] 当前上下文
     * @param[str] 要显示的字符串信息
     */
    fun showToast(context: Context, str: String?) {
        val s = str ?: ""
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

            /**
     * 显示提示信息
     * @param[context] 当前上下文
     * @param[resId] 要显示的字符串在strings文件中的ID
     */
    fun showToast(context: Context, @StringRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    /**
     * 打印日志
     * @param[tag] 日志标签
     * @param[msg] 日志信息
     */
    fun showLog(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    /**
     * 打印日志
     * @param[tag] 日志标签
     * @param[i] 数字作为日志信息
     */
    fun showLog(tag: String, i: Int) {
        Log.e(tag, "$i")
    }
}