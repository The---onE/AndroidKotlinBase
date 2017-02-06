package com.xmx.androidkotlinbase.Log

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ListView
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.Tools.ActivityBase.BaseTempActivity
import com.xmx.androidkotlinbase.Tools.OperationLog.LogChangeEvent
import com.xmx.androidkotlinbase.Tools.OperationLog.OperationLog
import com.xmx.androidkotlinbase.Tools.OperationLog.OperationLogEntityManager
import com.xmx.androidkotlinbase.Tools.OperationLog.OperationLogManager
import kotlinx.android.synthetic.main.activity_operation_log.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class OperationLogActivity : BaseTempActivity() {

    // 操作日志列表适配器
    private var operationLogAdapter: OperationLogAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_operation_log)
        operationLogAdapter = OperationLogAdapter(this, ArrayList<OperationLog>())
        listOperationLog.adapter = operationLogAdapter
    }

    override fun setListener() {
        btnClearLog.setOnClickListener(View.OnClickListener {
            OperationLogEntityManager.instance().clearDatabase()
            OperationLogManager.instance().updateData()
            operationLogAdapter?.updateList(OperationLogManager.instance().data!!)
        })
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        OperationLogManager.instance().updateData()
        operationLogAdapter?.updateList(OperationLogManager.instance().data!!)
        // 订阅事件
        EventBus.getDefault().register(this)
    }

    // 订阅操作日志变动事件
    @Subscribe
    fun onEvent(event: LogChangeEvent) {
        OperationLogManager.instance().updateData()
        operationLogAdapter?.updateList(OperationLogManager.instance().data!!)
    }
}
