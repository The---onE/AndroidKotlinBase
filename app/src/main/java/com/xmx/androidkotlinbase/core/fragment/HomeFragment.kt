package com.xmx.androidkotlinbase.core.fragment

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.module.log.ExceptionTestActivity
import com.xmx.androidkotlinbase.module.log.OperationLogActivity
import com.xmx.androidkotlinbase.module.service.MainService
import com.xmx.androidkotlinbase.base.fragment.BaseFragment
import com.xmx.androidkotlinbase.common.log.operationLogEntityManager
import com.xmx.androidkotlinbase.utils.VibratorUtil
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by The_onE on 2017/1/18.
 * 测试各零散组件是否运行正常，演示其使用方法
 */
class HomeFragment : BaseFragment() {
    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun setListener(view: View) {
        // 运行服务
        btnStartService.setOnClickListener {
            val service = Intent(context, MainService::class.java)
            context.startService(service)
            operationLogEntityManager.addLog("开启服务")
            showToast("已开启服务")
        }

        // 停止服务
        btnStopService.setOnClickListener {
            // 获取系统服务管理器
            val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            // 设置查询上限
            val defaultNum = 1000
            // 查询运行的服务
            val runServiceList = manager
                    .getRunningServices(defaultNum)
            var flag = false
            runServiceList.forEach {
                // 查询前台服务
                if (it.foreground) {
                    // 根据包名查询服务，当前应用创建的进程没有应用包名
                    if (it.service.shortClassName == ".model.service.MainService") {
                        // 关闭服务
                        val intent = Intent()
                        intent.component = it.service
                        context.stopService(intent)
                        showToast("已关闭服务")
                        flag = true
                    }
                }
            }
            if (!flag) {
                showToast("服务未开启")
            }
        }

        // 震动一秒
        btnVibrateOnce.setOnClickListener {
            VibratorUtil.vibrate(context, 1000)
        }

        // 持续震动
        btnVibrateForever.setOnClickListener {
            VibratorUtil.vibrate(context)
        }

        // 节奏震动
        btnVibrateRhythm.setOnClickListener {
            VibratorUtil.vibrate(context, 500, 250, 3)
        }

        // 取消震动
        btnCancelVibrate.setOnClickListener {
            VibratorUtil.cancel(context)
        }

        // 查看日志
        btnShowOperationLog.setOnClickListener {
            startActivity(OperationLogActivity::class.java)
        }

        // 异常模拟
        btnExceptionTest.setOnClickListener {
            startActivity(ExceptionTestActivity::class.java)
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}