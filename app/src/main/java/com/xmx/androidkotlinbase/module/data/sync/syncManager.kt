package com.xmx.androidkotlinbase.module.data.sync

/**
 * Created by The_onE on 2016/2/24.
 * 测试同步数据管理器，单例对象
 */
object SyncManager {

    // 实体管理器版本，不一致时需更新
    private var syncVersion: Long = 0
    // 自身版本，提示调用者是否有更新
    var version = System.currentTimeMillis()
        private set
    // 实体列表
    private var syncList: List<Sync>? = null

    // 获取数据
    val data: List<Sync>?
        get() = syncList

    /**
     * 更新数据，若实体有更新则同步更新
     * @return 管理器自身当前版本
     */
    fun updateData(): Long {
        val entityManager = SyncEntityManager
        // //判断实体是否有更新
        // 多应用同时操作数据库不判断，直接进行覆盖
        //if (entityManager.sqlManager.version !== syncVersion) {
        syncVersion = entityManager.sqlManager.version
        // 将所有数据按时间降序排列
        syncList = entityManager.sqlManager.selectAll("Time", false)
        // 数据更新
        version++
        //}
        return version
    }
}
