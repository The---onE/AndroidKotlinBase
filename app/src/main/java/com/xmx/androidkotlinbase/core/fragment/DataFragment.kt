package com.xmx.androidkotlinbase.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xmx.androidkotlinbase.model.data.cloud.CloudActivity
import com.xmx.androidkotlinbase.model.data.sql.SQLActivity
import com.xmx.androidkotlinbase.model.data.sync.SyncActivity
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_data.*

/**
 * Created by The_onE on 2017/1/18.
 */
class DataFragment : BaseFragment() {
    override fun getContentView(inflater: LayoutInflater?, container: ViewGroup?): View {
        return inflater!!.inflate(R.layout.fragment_data, container, false);
    }

    override fun initView(view: View) {

    }

    override fun setListener(view: View) {
        btnSQL.setOnClickListener {
            startActivity(SQLActivity::class.java)
        }
        btnCloud.setOnClickListener {
            startActivity(CloudActivity::class.java)
        }
        btnSync.setOnClickListener {
            startActivity(SyncActivity::class.java)
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}