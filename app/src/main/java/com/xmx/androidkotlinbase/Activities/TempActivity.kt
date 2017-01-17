package com.xmx.androidkotlinbase.Activities

import android.os.Bundle
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.Tools.ActivityBase.BaseTempActivity

class TempActivity : BaseTempActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_temp)
    }

    override fun setListener() {

    }

    override fun processLogic(savedInstanceState: Bundle?) {

    }
}
