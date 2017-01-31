package com.xmx.androidkotlinbase.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xmx.androidkotlinbase.Activities.TempActivity
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.Tools.FragmentBase.BaseFragment
import com.xmx.androidkotlinbase.Tools.Utils.VibratorUtil
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by The_onE on 2017/1/18.
 */
class HomeFragment : BaseFragment() {
    override fun getContentView(inflater: LayoutInflater?, container: ViewGroup?): View {
        return inflater!!.inflate(R.layout.fragment_home, container, false);
    }

    override fun initView(view: View) {

    }

    override fun setListener(view: View) {
        btnTempActivity.setOnClickListener {
            startActivity(TempActivity::class.java)
        }

        // 震动一秒
        btnVibrateOnce.setOnClickListener {
            VibratorUtil.instance().vibrate(context, 1000)
        }

        // 持续震动
        btnVibrateForever.setOnClickListener {
            VibratorUtil.instance().vibrate(context)
        }

        // 节奏震动
        btnVibrateRhythm.setOnClickListener {
            VibratorUtil.instance().vibrate(context, 500, 250, 3)
        }

        // 取消震动
        btnCancelVibrate.setOnClickListener {
            VibratorUtil.instance().cancel(context)
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}