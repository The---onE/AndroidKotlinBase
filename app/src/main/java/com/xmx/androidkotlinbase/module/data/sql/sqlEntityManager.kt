package com.xmx.androidkotlinbase.module.data.sql

import com.xmx.androidkotlinbase.common.data.sql.BaseSQLEntityManager

/**
 * Created by The_onE on 2016/3/27.
 * 测试SQLite实体管理器，单例对象
 */
object sqlEntityManager : BaseSQLEntityManager<SQL>() {

    init {
        tableName = "SQLTest" // 表名
        entityTemplate = SQL() // 实体模版
        // 打开数据库
        openDatabase()
    }
}
