package com.xmx.androidkotlinbase.module.net

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import com.xmx.androidkotlinbase.utils.ExceptionUtil
import com.xmx.androidkotlinbase.utils.JSONUtil
import kotlinx.android.synthetic.main.activity_json.*

/**
 * A simple [Fragment] subclass.
 */
class JsonActivity : BaseTempActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_json)
    }

    override fun setListener() {
        btnParse.setOnClickListener(View.OnClickListener {
            val json = editJson.text.toString().trim { it <= ' ' }
            try {
                if (json.startsWith("{")) {
                    val map = JSONUtil.parseObject(json)
                    textResult.text = JSONUtil.formatJSONObject(map, " : ", "|----")
                } else if (json.startsWith("[")) {
                    val list = JSONUtil.parseArray(json)
                    textResult.text = JSONUtil.formatJSONArray(list, " : ", "|----")
                } else {
                    showToast("格式不正确")
                }
            } catch (e: Exception) {
                ExceptionUtil.normalException(e, this@JsonActivity)
            }
        })
    }

    override fun processLogic(savedInstanceState: Bundle?) {

    }
}
