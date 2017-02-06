package com.xmx.androidkotlinbase.Tools.OperationLog

import com.xmx.androidkotlinbase.Tools.Data.SQL.BaseSQLEntityManager
import org.greenrobot.eventbus.EventBus

import java.util.Date

/**
 * Created by The_onE on 2016/9/4.
 * 操作日志实体管理器
 */
class OperationLogEntityManager private constructor() : BaseSQLEntityManager<OperationLog>() {
    // 单例模式
    companion object {
        private var instance: OperationLogEntityManager? = null
        @Synchronized fun instance(): OperationLogEntityManager {
            if (null == instance) {
                instance = OperationLogEntityManager()
            }
            return instance!!
        }
    }

    init {
        tableName = "OperationLog" // 表名
        entityTemplate = OperationLog() // 实体模版
        // 打开数据库
        openDatabase()
    }

    // 添加日志
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
