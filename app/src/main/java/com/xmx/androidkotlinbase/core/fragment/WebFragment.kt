package com.xmx.androidkotlinbase.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.fragment.BaseFragment
import com.xmx.androidkotlinbase.model.web.LocalHorizontalWebActivity
import com.xmx.androidkotlinbase.model.web.LocalVerticalWebActivity
import com.xmx.androidkotlinbase.model.web.NetworkWebActivity
import kotlinx.android.synthetic.main.fragment_web.*

/**
 * Created by The_onE on 2017/1/18.
 * 测试网页组件是否运行正常，演示其使用方法
 */
class WebFragment : BaseFragment() {
    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun setListener(view: View) {
        // 打开网络网页
        btnNetwork.setOnClickListener {
            startActivity(NetworkWebActivity::class.java)
        }
        // 打开本地网页(竖)
        btnLocalVertical.setOnClickListener {
            startActivity(LocalVerticalWebActivity::class.java)
        }
        // 打开本地网页(横)
        btnLocalHorizontal.setOnClickListener {
            startActivity(LocalHorizontalWebActivity::class.java)
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}