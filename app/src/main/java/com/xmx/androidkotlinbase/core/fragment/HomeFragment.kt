package com.xmx.androidkotlinbase.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.module.log.ExceptionTestActivity
import com.xmx.androidkotlinbase.module.log.OperationLogActivity
import com.xmx.androidkotlinbase.base.fragment.BaseFragment
import com.xmx.androidkotlinbase.module.dialog.TestDialog
import com.xmx.androidkotlinbase.utils.VibratorUtil
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by The_onE on 2017/1/18.
 * 测试各零散组件是否运行正常，演示其使用方法
 */
class HomeFragment : BaseFragment() {
    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View =
            inflater.inflate(R.layout.fragment_home, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun setListener(view: View) {
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

        // 弹出对话框
        btnDialogTest.setOnClickListener {
            val dialog = TestDialog();
            dialog.initDialog(context)
            dialog.show(activity.fragmentManager, "TEST")
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}