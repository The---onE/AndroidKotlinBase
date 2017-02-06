package com.xmx.androidkotlinbase.Log

import android.os.Bundle
import android.view.View
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.Tools.ActivityBase.BaseTempActivity
import kotlinx.android.synthetic.main.activity_exception_test.*

/**
 * Created by The_onE on 2016/10/3.
 */
class ExceptionTestActivity : BaseTempActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_exception_test)
    }

    override fun setListener() {
        btnDivideByZero.setOnClickListener(View.OnClickListener { val e = 1 / 0 })

        btnNullPointer.setOnClickListener(View.OnClickListener {
            val e: String? = null
            e!!.contains("e")
        })
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }
}
