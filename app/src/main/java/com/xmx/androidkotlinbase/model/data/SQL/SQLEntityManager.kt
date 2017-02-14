package com.xmx.androidkotlinbase.model.data.sql

import com.xmx.androidkotlinbase.common.data.sql.BaseSQLEntityManager

/**
 * Created by The_onE on 2016/3/27.
 * 测试SQLite实体管理器
 */
class SQLEntityManager private constructor() : BaseSQLEntityManager<SQL>() {

    init {
        tableName = "SQLTest" // 表名
        entityTemplate = SQL() // 实体模版
        // 打开数据库
        openDatabase()
    }

    // 单例模式
    companion object {
        private var instance: SQLEntityManager? = null
        @Synchronized fun instance(): SQLEntityManager {
            if (null == instance) {
                instance = SQLEntityManager()
            }
            return instance!!
        }
    }
}
