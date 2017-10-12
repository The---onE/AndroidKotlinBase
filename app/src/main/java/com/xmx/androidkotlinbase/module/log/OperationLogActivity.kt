package com.xmx.androidkotlinbase.module.log

import android.os.Bundle
import android.view.View
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import com.xmx.androidkotlinbase.common.log.LogChangeEvent
import com.xmx.androidkotlinbase.common.log.OperationLog
import com.xmx.androidkotlinbase.common.log.OperationLogEntityManager
import com.xmx.androidkotlinbase.common.log.OperationLogManager
import kotlinx.android.synthetic.main.activity_operation_log.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import java.util.ArrayList

/**
 * Created by The_onE on 2016/10/3.
 * 查看日志页，显示所有本应用的操作日志
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
        // 清空日志
        btnClearLog.setOnClickListener(View.OnClickListener {
            OperationLogEntityManager.clearDatabase()
            OperationLogManager.updateData()
            operationLogAdapter?.updateList(OperationLogManager.data!!)
        })
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        OperationLogManager.updateData()
        operationLogAdapter?.updateList(OperationLogManager.data!!)
        // 订阅事件
        EventBus.getDefault().register(this)
    }

    /**
     * 订阅操作日志变动事件
     * @param[event] 操作日志变动事件
     */
    @Subscribe
    fun onEvent(event: LogChangeEvent) {
        OperationLogManager.updateData()
        operationLogAdapter?.updateList(OperationLogManager.data!!)
    }
}
