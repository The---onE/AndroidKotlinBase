package com.xmx.androidkotlinbase.module.data.sql

/**
 * Created by The_onE on 2016/2/24.
 * 测试SQLite数据管理器，单例对象
 */
object sqlManager {

    // 实体管理器版本，不一致时需更新
    private var sqlVersion: Long = 0
    // 自身版本，提示调用者是否有更新
    var version = System.currentTimeMillis()
        private set
    // 实体列表
    private var sqlList: List<SQL>? = null

    // 获取数据
    val data: List<SQL>?
        get() = sqlList

    /**
     * 更新数据，若实体有更新则同步更新
     * @return 管理器自身当前版本
     */
    fun updateData(): Long {
        val sqlManager = SqlEntityManager
        // //判断实体是否有更新
        // 多应用同时操作数据库不判断，直接进行覆盖
        if (sqlManager.version != sqlVersion) {
            sqlVersion = sqlManager.version
            // 将所有数据按时间降序排列
            sqlList = sqlManager.selectAll("Time", false)
            // 数据更新
            version++
        }
        return version
    }
}
