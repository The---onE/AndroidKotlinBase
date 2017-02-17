package com.xmx.androidkotlinbase.model.web

import android.content.Context
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.xmx.androidkotlinbase.utils.StringUtil

/**
 * Created by The_onE on 2017/2/18.
 * 自定义页面事件处理(alert,prompt等页面事件)
 */
class MyWebChromeClient(val mContext: Context) : WebChromeClient() {
    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        StringUtil.showToast(mContext, message)
        // 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
        result?.confirm();
        return true
    }
}