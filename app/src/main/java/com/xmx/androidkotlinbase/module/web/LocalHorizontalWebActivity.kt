package com.xmx.androidkotlinbase.module.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import com.xmx.androidkotlinbase.common.web.BaseWebChromeClient
import com.xmx.androidkotlinbase.common.web.BaseWebViewClient
import kotlinx.android.synthetic.main.activity_local_horizontal_web.*

/**
 * Created by The_onE on 2017/2/17.
 * 测试横向本地网页，打开assets/horizontal.html页
 */
class LocalHorizontalWebActivity : BaseTempActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_local_horizontal_web)
    }

    override fun setListener() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun processLogic(savedInstanceState: Bundle?) {
        // 允许JS执行
        webBrowser.settings.javaScriptEnabled = true

        // 设置自定义浏览器属性(对不同协议的URL分别处理)
        webBrowser.setWebViewClient(BaseWebViewClient())
        // 设置自定义页面事件处理(alert,prompt等页面事件)
        webBrowser.setWebChromeClient(object : BaseWebChromeClient() {
            override fun onAlert(message: String) {
                val builder = AlertDialog.Builder(this@LocalHorizontalWebActivity)
                builder.setMessage(message)
                        .setTitle("提示")
                        .setPositiveButton("确定", { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        })
                        .show()
            }
        })

        // 设置可以支持缩放
        webBrowser.settings.setSupportZoom(true)
        // 设置出现缩放工具
        webBrowser.settings.builtInZoomControls = true
        //不显示缩放按钮
        webBrowser.settings.displayZoomControls = false
        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webBrowser.settings.useWideViewPort = true
        // 设置默认加载的可视范围是大视野范围
        webBrowser.settings.loadWithOverviewMode = true

        // 打开本地网页
        webBrowser.loadUrl("file:///android_asset/horizontal.html")
    }
}
