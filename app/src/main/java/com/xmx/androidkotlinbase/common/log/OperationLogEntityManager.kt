package com.xmx.androidkotlinbase.common.log

import com.xmx.androidkotlinbase.common.data.sql.BaseSQLEntityManager
import org.greenrobot.eventbus.EventBus

import java.util.Date

/**
 * Created by The_onE on 2016/9/4.
 * 操作日志实体管理器，单例对象
 */
object OperationLogEntityManager : BaseSQLEntityManager<OperationLog>() {

    init {
        tableName = "OperationLog" // 表名
        entityTemplate = OperationLog() // 实体模版
        // 打开数据库
        openDatabase()
    }

    /**
     * 添加日志
     * @param[operation] 记录的操作内容
     */
    fun addLog(operation: String) {
        // 创建操作日志实体
        val entity = OperationLog()
        entity.mOperation = operation
        entity.mTime = Date()
        // 将操作日志添加到数据库
        insertData(entity)
        // 发布日志变动事件
        EventBus.getDefault().post(LogChangeEvent())
    }
}
