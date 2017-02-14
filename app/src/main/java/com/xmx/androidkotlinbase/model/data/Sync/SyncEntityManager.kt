package com.xmx.androidkotlinbase.model.data.sync

import com.xmx.androidkotlinbase.common.data.sync.BaseSyncEntityManager

/**
 * Created by The_onE on 2016/3/27.
 * 测试SQLite数据库与LeanCloud数据库同步实体管理器
 */
class SyncEntityManager private constructor() : BaseSyncEntityManager<Sync>() {

    init {
        // 初始化表名、模版、用户字段
        syncInit("SyncTest", Sync(), "User")
    }

    // 单例模式
    companion object {
        private var instance: SyncEntityManager? = null
        @Synchronized fun instance(): SyncEntityManager {
            if (null == instance) {
                instance = SyncEntityManager()
            }
            return instance!!
        }
    }
}
