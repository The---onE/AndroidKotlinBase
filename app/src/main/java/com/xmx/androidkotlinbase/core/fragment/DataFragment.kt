package com.xmx.androidkotlinbase.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xmx.androidkotlinbase.module.data.cloud.CloudActivity
import com.xmx.androidkotlinbase.module.data.sql.SQLActivity
import com.xmx.androidkotlinbase.module.data.sync.SyncActivity
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_data.*

/**
 * Created by The_onE on 2017/1/18.
 * 测试数据相关组件是否运行正常，演示其使用方法
 */
class DataFragment : BaseFragment() {
    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View =
            inflater.inflate(R.layout.fragment_data, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun setListener(view: View) {
        // 打开SQLite数据管理页
        btnSQL.setOnClickListener {
            startActivity(SQLActivity::class.java)
        }
        // 打开LeanCloud数据管理页
        btnCloud.setOnClickListener {
            startActivity(CloudActivity::class.java)
        }
        // 打开SQLite与LeanCloud同步数据管理页
        btnSync.setOnClickListener {
            startActivity(SyncActivity::class.java)
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}