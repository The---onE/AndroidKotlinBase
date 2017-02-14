package com.xmx.androidkotlinbase.model.data.cloud

import com.xmx.androidkotlinbase.common.data.cloud.BaseCloudEntityManager

/**
 * Created by The_onE on 2016/3/27.
 * 测试LeanCloud实体管理器
 */
class CloudEntityManager private constructor() : BaseCloudEntityManager<Cloud>() {

    init {
        tableName = "CloudTest" // 表名
        entityTemplate = Cloud() // 实体模版
        userField = "User" // 用户字段
    }

    // 单例模式
    companion object {
        private var instance: CloudEntityManager? = null
        @Synchronized fun instance(): CloudEntityManager {
            if (null == instance) {
                instance = CloudEntityManager()
            }
            return instance!!
        }
    }
}
