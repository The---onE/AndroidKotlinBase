package com.xmx.androidkotlinbase.Tools.Utils

import android.content.Context
import android.os.Vibrator

/**
 * Created by The_onE on 2017/1/31.
 * 震动控制器
 */

class VibratorUtil {
    // 单例模式
    companion object {
        private var instance: VibratorUtil? = null
        @Synchronized fun instance(): VibratorUtil {
            if (null == instance) {
                instance = VibratorUtil()
            }
            return instance!!
        }
    }

    // 震动一定时间
    fun vibrate(context: Context, time: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(time)
    }

    // 持续震动
    fun vibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(longArrayOf(0, 10000), 0)
    }

    // 取消震动
    fun cancel(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
    }
}
