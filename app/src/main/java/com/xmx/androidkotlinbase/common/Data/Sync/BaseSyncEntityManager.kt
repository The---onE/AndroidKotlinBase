package com.xmx.androidkotlinbase.common.data.sync

import com.xmx.androidkotlinbase.common.data.cloud.BaseCloudEntityManager
import com.xmx.androidkotlinbase.common.data.DataConstants
import com.xmx.androidkotlinbase.common.data.sql.BaseSQLEntityManager
import com.xmx.androidkotlinbase.common.user.UserData

import java.util.ArrayList
import java.util.Date

/**
 * Created by The_onE on 2016/5/29.
 * SQLite数据库与LeanCloud数据库同步管理基类
 * 增删改数据时先上传至云端，保存成功后对本地数据库同步操作，查询时直接使用本地数据库中的数据
 * 每个用户对应一个由用户ID命名的本地数据库文件
 */
abstract class BaseSyncEntityManager<Entity : ISyncEntity> {
    // 当前用户，打开相应的本地数据库文件
    private var userId: String? = null

    /**
     * 特化SQLite数据库管理
     */
    inner class SQLManager : BaseSQLEntityManager<Entity>() {
        /**
         * 初始化管理器
         */
        fun syncInit(tableName: String, entityTemplate: Entity) {
            this.tableName = tableName
            this.entityTemplate = entityTemplate
        }
    }

    /**
     * 特化LeanCloud数据库管理
     */
    inner class CloudManager : BaseCloudEntityManager<Entity>() {
        /**
         * 初始化管理器
         */
        fun syncInit(tableName: String, entityTemplate: Entity, userField: String?) {
            this.tableName = tableName
            this.entityTemplate = entityTemplate
            this.userField = userField
        }
    }

    // 使用数据库管理器查询数据
    var sqlManager = SQLManager()
    var cloudManager = CloudManager()

    // 子类构造函数中调用syncInit方法初始化下列变量！
    private var tableName: String? = null
    private var entityTemplate: Entity? = null //空模版，不需要数据
    private var userField: String? = null //用户字段，保存当前登录用户的ObjectId，为空时不保存用户字段

    /**
     * 初始化变量
     * @param[tableName] 数据库和云端表名相同
     * @param[entityTemplate] 模版，不需要数据
     * @param[userField] 用户字段，保存当前登录用户的ObjectId，不设置则不保存用户字段
     */
    protected fun syncInit(tableName: String, entityTemplate: Entity, userField: String?) {
        sqlManager.syncInit(tableName, entityTemplate)
        cloudManager.syncInit(tableName, entityTemplate, userField)
        this.tableName = tableName
        this.entityTemplate = entityTemplate
        this.userField = userField
    }

    /**
     * 检查管理器是否已初始化
     * @return 管理器是否已初始化
     */
    protected fun checkDatabase(): Boolean = tableName != null && entityTemplate != null

    /**
     * 切换账户，打开对应用户的数据库文件
     * @param[id] 新用户的ID
     * @return 是否成功切换
     */
    protected fun switchAccount(id: String): Boolean {
        if (id != userId) {
            userId = id
            sqlManager.openDatabase(userId)
            return true
        }
        return false
    }

    /**
     * 将云端数据同步至本地数据库
     * @param[conditions] 需要同步的数据查询条件
     * @param[success] 同步成功的回调，获取数据和用户数据
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[syncError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun syncFromCloud(conditions: Map<String, Any>?,
                      success: (UserData, List<Entity>) -> Unit,
                      error: (Int) -> Unit,
                      syncError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        // 从云端获取数据
        cloudManager.selectByCondition(conditions, null, false,
                success = {
                    user, entities ->
                    // 若切换用户或尚未设置用户则打开对应用户的数据库文件
                    switchAccount(user.objectId!!)
                    // 将获取的新数据写入数据库，原数据不变
                    entities.filter { sqlManager.selectByCloudId(it.cloudId) == null }
                            .forEach { sqlManager.insertData(it) }
                    // 获取成功
                    success(user, entities)
                },
                error = {
                    e ->
                    error(e)
                },
                cloudError = {
                    e ->
                    syncError(e)
                })
    }

    /**
     * 向云端添加数据，成功后添加至数据库
     * @param[entity] 要插入的数据实体
     * @param[success] 插入成功的回调，获取插入数据ID和用户数据
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[syncError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun insertData(entity: Entity,
                   success: (UserData, String) -> Unit, // 返回插入数据ID
                   error: (Int) -> Unit,
                   syncError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        // 向云端插入数据
        cloudManager.insertToCloud(entity,
                success = {
                    user, objectId ->
                    // 若切换用户或尚未设置用户则打开对应用户的数据库文件
                    switchAccount(user.objectId!!)
                    // 向本地数据库写入文件
                    entity.cloudId = objectId
                    sqlManager.insertData(entity)
                    // 添加成功
                    success(user, objectId)
                },
                error = {
                    e ->
                    error(e)
                },
                cloudError = {
                    e ->
                    syncError(e)
                })
    }

    /**
     * 从云端删除一条记录，成功后删除数据库中对应记录
     * @param[objectId] 要删除的数据ID
     * @param[success] 删除成功的回调，获取当前用户数据
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[syncError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun deleteData(objectId: String,
                   success: (UserData) -> Unit,
                   error: (Int) -> Unit,
                   syncError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        // 从云端删除数据
        cloudManager.deleteFromCloud(objectId,
                success = {
                    user ->
                    // 若切换用户或尚未设置用户则打开对应用户的数据库文件
                    switchAccount(user.objectId!!)
                    // 在本地数据库删除该条数据
                    sqlManager.deleteByCloudId(objectId)
                    // 删除成功
                    success(user)
                },
                error = {
                    e ->
                    error(e)
                },
                cloudError = {
                    e ->
                    syncError(e)
                })
    }

    /**
     * 在云端更新一条记录，成功后更新数据库中对应记录
     * @param[objectId] 要更改的数据ID
     * @param[update] 要更改的字段及对应值
     * @param[success] 更改成功的回调，获取当前用户数据
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[syncError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun updateData(objectId: String, update: Map<String, Any>,
                   success: (UserData) -> Unit,
                   error: (Int) -> Unit,
                   syncError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        cloudManager.updateToCloud(objectId, update,
            success = {
                user ->
                // 若切换用户或尚未设置用户则打开对应用户的数据库文件
                switchAccount(user.objectId!!)
                val strings = ArrayList<String>()
                // 生成本地数据库更新内容
                for ((k, v) in update) {
                    val string: String = when (v) {
                        is Date -> "$k = ${v.time}"
                        is String -> "$k = '$v'"
                        else -> "$k = $v"
                    }
                    strings.add(string)
                }
                // 在本地数据库更新该条数据
                sqlManager.updateData(objectId, *strings.toTypedArray())
                success(user)
            },
            error = {
                e ->
                error(e)
            },
            cloudError = {
                e ->
                syncError(e)
            })
    }
}
