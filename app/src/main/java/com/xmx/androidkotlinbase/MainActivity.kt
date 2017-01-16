package com.xmx.androidkotlinbase

import android.os.Bundle
import android.widget.Toast
import com.xmx.androidkotlinbase.Tools.ActivityBase.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    // 初始化View
    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
    }

    // 声明事件监听
    override fun setListener() {
        btnTest.setOnClickListener {
            Toast.makeText(this, "Hello Kotlin", Toast.LENGTH_SHORT).show()
        }
    }

    // 处理业务逻辑
    override fun processLogic(savedInstanceState: Bundle?) {
    }

}
