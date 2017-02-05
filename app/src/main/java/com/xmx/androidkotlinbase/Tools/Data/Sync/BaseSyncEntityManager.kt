package com.xmx.androidkotlinbase.Tools.Data.Sync

import android.content.Context
import android.widget.Toast

import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.Tools.Data.Cloud.BaseCloudEntityManager
import com.xmx.androidkotlinbase.Tools.Data.DataConstants
import com.xmx.androidkotlinbase.Tools.Data.SQL.BaseSQLEntityManager
import com.xmx.androidkotlinbase.Tools.User.UserData

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
    protected var userId: String? = null

    // 特化SQLite数据库管理
    inner class SQLManager : BaseSQLEntityManager<Entity>() {
        fun syncInit(tableName: String, entityTemplate: Entity) {
            this.tableName = tableName
            this.entityTemplate = entityTemplate
        }
    }

    inner class CloudManager : BaseCloudEntityManager<Entity>() {
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
    protected var tableName: String? = null
    protected var entityTemplate: Entity? = null //空模版，不需要数据
    protected var userField: String? = null //用户字段，保存当前登录用户的ObjectId，为空时不保存用户字段

    // 初始化变量
    // tableName 数据库和云端表名相同
    // entityTemplate 模版，不需要数据
    // userField 用户字段，保存当前登录用户的ObjectId，不设置则不保存用户字段
    protected fun syncInit(tableName: String, entityTemplate: Entity, userField: String?) {
        sqlManager.syncInit(tableName, entityTemplate)
        cloudManager.syncInit(tableName, entityTemplate, userField)
        this.tableName = tableName
        this.entityTemplate = entityTemplate
        this.userField = userField
    }

    // 检查管理器是否已初始化
    protected fun checkDatabase(): Boolean {
        return tableName != null && entityTemplate != null
    }

    // 默认的错误处理
    fun defaultError(context: Context): (Int) -> Unit {
        return {
            e ->
            when (e) {
                DataConstants.NOT_INIT -> Toast.makeText(context, R.string.failure, Toast.LENGTH_SHORT).show()
                DataConstants.NOT_LOGGED_IN -> Toast.makeText(context, R.string.not_loggedin, Toast.LENGTH_SHORT).show()
                DataConstants.CHECK_LOGIN_ERROR -> Toast.makeText(context, R.string.cannot_check_login, Toast.LENGTH_SHORT).show()
                DataConstants.NOT_RELATED_USER -> Toast.makeText(context, R.string.not_related_user, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 切换账户，打开对应用户的数据库文件
    protected fun switchAccount(id: String): Boolean {
        if (id != userId) {
            userId = id
            sqlManager.openDatabase(userId)
            return true
        }
        return false
    }

    // 将云端数据同步至数据库
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

    // 向云端添加数据，成功后添加至数据库
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

    // 从云端删除一条记录，成功后删除数据库中对应记录
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

    //在云端更新一条记录，成功后更新数据库中对应记录
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
                    val string: String
                    when (v) {
                        is Date ->string = "$k = ${v.time}"
                        is String -> string = "$k = '$v'"
                        else -> string = "$k = ${v.toString()}"
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
