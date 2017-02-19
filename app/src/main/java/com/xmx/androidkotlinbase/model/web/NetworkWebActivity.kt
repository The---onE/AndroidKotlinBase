package com.xmx.androidkotlinbase.model.web

import android.os.Bundle
import android.support.v7.app.AlertDialog

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import com.xmx.androidkotlinbase.common.web.BaseWebChromeClient
import com.xmx.androidkotlinbase.common.web.BaseWebViewClient
import kotlinx.android.synthetic.main.activity_network_web.*

/**
 * Created by The_onE on 2017/2/17.
 * 测试网络网页，打开http://www.bing.com(必应主页)
 */
class NetworkWebActivity : BaseTempActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_network_web)
    }

    override fun setListener() {
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 允许JS执行
        webBrowser.settings.javaScriptEnabled = true

        // 设置自定义浏览器属性(对不同协议的URL分别处理)
        webBrowser.setWebViewClient(BaseWebViewClient())
        // 设置自定义页面事件处理(alert,prompt等页面事件)
        webBrowser.setWebChromeClient(object : BaseWebChromeClient() {
            override fun onAlert(message: String) {
                val builder = AlertDialog.Builder(this@NetworkWebActivity)
                builder.setMessage(message)
                        .setTitle("提示")
                        .setPositiveButton("确定", {
                            dialogInterface, i ->
                            dialogInterface.dismiss()
                        })
                        .show()
            }
        })

        // 设置可以支持缩放
        webBrowser.settings.setSupportZoom(true)
        // 设置出现缩放工具
        webBrowser.settings.builtInZoomControls = true
        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webBrowser.settings.useWideViewPort = true
        //设置默认加载的可视范围是大视野范围
        webBrowser.settings.loadWithOverviewMode = true

        // 打开网络网页
        webBrowser.loadUrl("http://www.bing.com")
    }
}
