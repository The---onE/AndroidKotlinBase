package com.xmx.androidkotlinbase.Tools.OperationLog

/**
 * Created by The_onE on 2016/2/24.
 * 操作日志数据管理器
 */
class OperationLogManager {
    // 实体管理器版本，不一致时需更新
    private var sqlVersion: Long = 0
    // 自身版本，提示调用者是否有更新
    var version = System.currentTimeMillis()
        private set
    // 实体列表
    private var sqlList: List<OperationLog>? = null

    // 单例模式
    companion object {
        private var instance: OperationLogManager? = null
        @Synchronized fun instance(): OperationLogManager {
            if (null == instance) {
                instance = OperationLogManager()
            }
            return instance!!
        }
    }

    // 获取数据
    val data: List<OperationLog>?
        get() = sqlList

    // 更新数据，若实体有更新则同步更新
    fun updateData(): Long {
        val logManager = OperationLogEntityManager.instance()
        // 判断实体是否有更新
        if (logManager.version != sqlVersion) {
            sqlVersion = logManager.version
            // 将所有日志按时间降序排列
            sqlList = logManager.selectAll("Time", false)
            // 数据更新
            version++
        }
        return version
    }
}
