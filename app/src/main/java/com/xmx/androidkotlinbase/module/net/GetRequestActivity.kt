package com.xmx.androidkotlinbase.module.net

import android.os.Bundle
import android.support.v4.app.Fragment

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import com.xmx.androidkotlinbase.common.net.HttpManager
import com.xmx.androidkotlinbase.utils.ExceptionUtil
import kotlinx.android.synthetic.main.activity_get_request.*

/**
 * A simple [Fragment] subclass.
 */
class GetRequestActivity : BaseTempActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_get_request)

        textResponse.setTextIsSelectable(true)
    }

    override fun setListener() {
        btnRequest.setOnClickListener {
            var address = editAddress.text.toString()
            if (address != "") {
                if (!address.startsWith("http://")) {
                    address = "http://" + address
                }
                HttpManager.get(address, null,
                        success = { result ->
                            textResponse.text = result
                        },
                        fail = { e ->
                            ExceptionUtil.normalException(e, this@GetRequestActivity)
                        }
                )
            } else {
                showToast("地址不能为空")
            }
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {

    }
}
