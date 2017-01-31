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

    // 交替震动
    // repeatTimes 表示震动次数，0则不断重复
    fun vibrate(context: Context, vibrateTime: Long, pauseTime: Long, repeatTimes: Int) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (repeatTimes <= 0) {
            // 不断重复
            vibrator.vibrate(longArrayOf(pauseTime, vibrateTime), 0)
        } else {
            // 0, V, P, V, P, V, ...
            val array = Array<Long>(repeatTimes * 2, {
                i ->
                when (i) {
                    0 -> 0
                    else -> when (i % 2) {
                        1 -> vibrateTime
                        0 -> pauseTime
                        else -> 0
                    }
                }
            })
            vibrator.vibrate(array.toLongArray(), -1)
        }
    }

    // 官方震动
    // pattern 表示震动序列(P, V, P, V, ...)
    // repeat 表示从指定的下标开始重复震动，-1则不重复
    fun vibrate(context: Context, pattern: LongArray, repeat: Int) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(pattern, repeat)
    }

    // 取消震动
    fun cancel(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
    }
}
