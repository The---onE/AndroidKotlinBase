package com.xmx.androidkotlinbase.common.web

import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Created by The_onE on 2017/2/18.
 * 自定义浏览器属性(对不同协议的URL分别处理)
 * 处理各种通知与请求事件
 */
class BaseWebViewClient : WebViewClient() {
    /**
     * 设置不打开系统浏览器，直接在WebView中显示
     * @param view WebView控件
     * @param url 要打开的URL
     * @return 是否覆盖加载事件
     */
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return super.shouldOverrideUrlLoading(view, url)
    }
}