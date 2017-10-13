package com.xmx.androidkotlinbase.module.log

import android.os.Bundle
import android.view.View
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import kotlinx.android.synthetic.main.activity_exception_test.*

/**
 * Created by The_onE on 2016/10/3.
 * 异常模拟页，测试对常见异常的处理
 */
class ExceptionTestActivity : BaseTempActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_exception_test)
    }

    override fun setListener() {
        // 测试除0异常
        btnDivideByZero.setOnClickListener { val e = 1 / 0 }
        // 测试空指针异常
        btnNullPointer.setOnClickListener {
            val e: String? = null
            e!!.contains("e")
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }
}
