package com.xmx.androidkotlinbase.model.web

import android.os.Bundle

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import kotlinx.android.synthetic.main.activity_local_vertical_web.*


/**
 * Created by The_onE on 2017/2/17.
 * 测试竖向本地网页，打开assets/vertical.html页
 */
class LocalVerticalWebActivity : BaseTempActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_local_vertical_web)
    }

    override fun setListener() {
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 允许JS执行
        webBrowser.settings.javaScriptEnabled = true

        // 设置自定义浏览器属性(对不同协议的URL分别处理)
        webBrowser.setWebViewClient(MyWebViewClient())
        // 设置自定义页面事件处理(alert,prompt等页面事件)
        webBrowser.setWebChromeClient(MyWebChromeClient(this))

        // 设置可以支持缩放
        webBrowser.settings.setSupportZoom(true);
        // 设置出现缩放工具
        webBrowser.settings.builtInZoomControls = true
        //不显示缩放按钮
        webBrowser.settings.displayZoomControls = false
        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webBrowser.settings.useWideViewPort = true
        // 设置默认加载的可视范围是大视野范围
        webBrowser.settings.loadWithOverviewMode = true

        // 打开本地网页
        webBrowser.loadUrl("file:///android_asset/vertical.html")
    }
}
