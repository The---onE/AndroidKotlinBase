package com.xmx.androidkotlinbase.common.data.cloud

import com.avos.avoscloud.*
import com.xmx.androidkotlinbase.common.data.DataConstants
import com.xmx.androidkotlinbase.common.user.UserConstants
import com.xmx.androidkotlinbase.common.user.UserData
import com.xmx.androidkotlinbase.common.user.userManager
import java.util.*

/**
 * Created by The_onE on 2017/2/1.
 * LeanCloud数据库管理基类，提供常用的增删改查功能
 */
abstract class BaseCloudEntityManager<Entity : ICloudEntity> {

    // 子类构造函数中初始化下列变量！
    protected var tableName: String? = null // 表(Class)名
    protected var entityTemplate: Entity? = null // 空模版，不需要数据
    protected var userField: String? = null // 用户字段，保存当前登录用户的ObjectId，为空时不保存用户字段

    /**
     * 检查管理器是否已初始化
     * @return 管理器是否已初始化
     */
    protected fun checkDatabase(): Boolean {
        return tableName != null && entityTemplate != null
    }

    /**
     * 查询全部数据
     * @param[success] 查询成功的回调，获取数据
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[cloudError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun selectAll(success: (List<Entity>) -> Unit,
                  error: (Int) -> Unit,
                  cloudError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        // 查询表中全部数据
        val query = AVQuery<AVObject>(tableName)
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: List<AVObject>?, e: AVException?) {
                if (e == null) {
                    // 将查询的数据转化为实体
                    val entities = avObjects!!.mapTo(ArrayList<Entity>()) {
                        val entity: ICloudEntity = entityTemplate!!.convertToEntity(it)
                        entity as Entity
                    }
                    success(entities)
                } else {
                    cloudError(e)
                }
            }
        })
    }

    /**
     * 查询全部数据并排序
     * @param[order] 排序字段
     * @param[ascFlag] true为升序，false为降序
     * @param[success] 查询成功的回调，获取数据
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[cloudError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun selectAll(order: String?, ascFlag: Boolean,
                  success: (List<Entity>) -> Unit,
                  error: (Int) -> Unit,
                  cloudError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        val query = AVQuery<AVObject>(tableName)
        // 添加排序
        if (order != null) {
            if (ascFlag) {
                query.orderByAscending(order)
            } else {
                query.orderByDescending(order)
            }
        }
        // 查询表中全部数据并排序
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: List<AVObject>?, e: AVException?) {
                if (e == null) {
                    // 将查询的数据转化为实体
                    val entities = avObjects!!.mapTo(ArrayList<Entity>()) {
                        val entity: ICloudEntity = entityTemplate!!.convertToEntity(it)
                        entity as Entity
                    }
                    success(entities)
                } else {
                    cloudError(e)
                }
            }
        })
    }

    /**
     * 按条件查询数据并排序
     * @param[conditions] 查询条件，等值查询
     * @param[order] 排序字段
     * @param[ascFlag] true为升序，false为降序
     * @param[success] 查询成功的回调，获取数据
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[cloudError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun selectByCondition(conditions: Map<String, Any>?,
                          order: String?, ascFlag: Boolean,
                          success: (List<Entity>) -> Unit,
                          error: (Int) -> Unit,
                          cloudError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        val query = AVQuery<AVObject>(tableName)
        // 添加查询条件
        if (conditions != null) {
            for ((k, v) in conditions) {
                query.whereEqualTo(k, v)
            }
        }
        // 添加排序
        if (order != null) {
            if (ascFlag) {
                query.orderByAscending(order)
            } else {
                query.orderByDescending(order)
            }
        }
        // 查询表中符合条件的数据并排序
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: List<AVObject>?, e: AVException?) {
                if (e == null) {
                    // 将查询的数据转化为实体
                    val entities = avObjects!!.mapTo(ArrayList<Entity>()) {
                        val entity: ICloudEntity = entityTemplate!!.convertToEntity(it)
                        entity as Entity
                    }
                    success(entities)
                } else {
                    cloudError(e)
                }
            }
        })
    }

    /**
     * 在【登录状态下】按条件查询当前用户相关数据并排序
     * @param[conditions] 查询条件，等值查询
     * @param[order] 排序字段
     * @param[ascFlag] true为升序，false为降序
     * @param[success] 查询成功的回调，获取数据和【当前用户数据】
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[cloudError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun selectByCondition(conditions: Map<String, Any>?,
                          order: String?, ascFlag: Boolean,
                          success: (UserData, List<Entity>) -> Unit,
                          error: (Int) -> Unit,
                          cloudError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        // 校验登录，获取用户数据
        userManager.checkLogin(
                success = {
                    user ->
                    val query = AVQuery<AVObject>(tableName)
                    // 只查询当前用户相关数据
                    if (userField != null) {
                        query.whereEqualTo(userField, user.objectId)
                    }
                    // 添加查询条件
                    if (conditions != null) {
                        for ((k, v) in conditions) {
                            query.whereEqualTo(k, v)
                        }
                    }
                    // 添加排序
                    if (order != null) {
                        if (ascFlag) {
                            query.orderByAscending(order)
                        } else {
                            query.orderByDescending(order)
                        }
                    }
                    // 查询表中符合条件的数据并排序
                    query.findInBackground(object : FindCallback<AVObject>() {
                        override fun done(avObjects: List<AVObject>?, e: AVException?) {
                            if (e == null) {
                                // 将查询的数据转化为实体
                                val entities = avObjects!!.mapTo(ArrayList<Entity>()) {
                                    val entity: ICloudEntity = entityTemplate!!.convertToEntity(it)
                                    entity as Entity
                                }
                                success(user, entities)
                            } else {
                                cloudError(e)
                            }
                        }
                    })
                },
                error = {
                    e ->
                    when (e) {
                    // 若未登录则提示尚未登录
                        UserConstants.NOT_LOGGED_IN -> error(DataConstants.NOT_LOGGED_IN)
                        UserConstants.CANNOT_CHECK_LOGIN -> error(DataConstants.NOT_LOGGED_IN)
                    // 若校验登录失败则提示重新登录
                        UserConstants.USERNAME_ERROR -> error(DataConstants.CHECK_LOGIN_ERROR)
                        UserConstants.CHECKSUM_ERROR -> error(DataConstants.CHECK_LOGIN_ERROR)
                    }
                },
                cloudError = {
                    e ->
                    cloudError(e)
                })
    }

    /**
     * 插入数据
     * @param[entity] 要插入的数据实体
     * @param[success] 插入成功的回调，获取插入数据ID
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[cloudError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun insertToCloud(entity: Entity,
                      success: (String) -> Unit, // 返回插入数据ID
                      error: (Int) -> Unit,
                      cloudError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        val obj = entity.getContent(tableName!!)
        // 插入数据
        obj.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                if (e == null) {
                    // 返回该数据的ID
                    success(obj.objectId)
                } else {
                    cloudError(e)
                }
            }
        })
    }

    /**
     * 在【登录状态下】插入数据，该数据与当前用户关联
     * @param[entity] 要插入的数据实体
     * @param[success] 插入成功的回调，获取插入数据ID和【当前用户数据】
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[cloudError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun insertToCloud(entity: Entity,
                      success: (UserData, String) -> Unit, // 返回当前用户和插入数据ID
                      error: (Int) -> Unit,
                      cloudError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        // 校验登录，获取用户数据
        userManager.checkLogin(
                success = {
                    user ->
                    val obj = entity.getContent(tableName!!)
                    // 将数据与当前用户关联
                    if (userField != null) {
                        obj.put(userField, user.objectId)
                    }
                    // 插入数据
                    obj.saveInBackground(object : SaveCallback() {
                        override fun done(e: AVException?) {
                            if (e == null) {
                                // 返回该数据的ID
                                success(user, obj.objectId)
                            } else {
                                cloudError(e)
                            }
                        }
                    })
                },
                error = {
                    e ->
                    when (e) {
                    // 若未登录则提示尚未登录
                        UserConstants.NOT_LOGGED_IN -> error(DataConstants.NOT_LOGGED_IN)
                        UserConstants.CANNOT_CHECK_LOGIN -> error(DataConstants.NOT_LOGGED_IN)
                    // 若校验登录失败则提示重新登录
                        UserConstants.USERNAME_ERROR -> error(DataConstants.CHECK_LOGIN_ERROR)
                        UserConstants.CHECKSUM_ERROR -> error(DataConstants.CHECK_LOGIN_ERROR)
                    }
                },
                cloudError = {
                    e ->
                    cloudError(e)
                }
        )
    }

    /**
     * 在【登录状态下】删除数据，只能删除当前用户相关数据
     * @param[objectId] 要删除的数据ID
     * @param[success] 删除成功的回调，获取【当前用户数据】
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[cloudError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun deleteFromCloud(objectId: String,
                        success: (UserData) -> Unit,
                        error: (Int) -> Unit,
                        cloudError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        // 校验登录，获取用户数据
        userManager.checkLogin(
                success = {
                    user ->
                    val query = AVQuery<AVObject>(tableName)
                    query.getInBackground(objectId, object : GetCallback<AVObject>() {
                        override fun done(obj: AVObject?, e: AVException?) {
                            if (e == null) {
                                // 如果该数据不是与当前用户关联数据则不能删除
                                if (userField != null) {
                                    if (obj!!.getString(userField) != user.objectId) {
                                        error(DataConstants.NOT_RELATED_USER)
                                        return
                                    }
                                }
                                // 删除数据
                                obj!!.deleteInBackground(object : DeleteCallback() {
                                    override fun done(e: AVException?) {
                                        if (e == null) {
                                            success(user)
                                        } else {
                                            cloudError(e)
                                        }
                                    }
                                })
                            } else {
                                cloudError(e)
                            }
                        }
                    })
                },
                error = {
                    e ->
                    when (e) {
                    // 若未登录则提示尚未登录
                        UserConstants.NOT_LOGGED_IN -> error(DataConstants.NOT_LOGGED_IN)
                        UserConstants.CANNOT_CHECK_LOGIN -> error(DataConstants.NOT_LOGGED_IN)
                    // 若校验登录失败则提示重新登录
                        UserConstants.USERNAME_ERROR -> error(DataConstants.CHECK_LOGIN_ERROR)
                        UserConstants.CHECKSUM_ERROR -> error(DataConstants.CHECK_LOGIN_ERROR)
                    }
                },
                cloudError = {
                    e ->
                    cloudError(e)
                })
    }

    /**
     * 在【登录状态下】更改数据，只能修改当前用户相关数据
     * @param[objectId] 要更改的数据ID
     * @param[update] 要更改的字段及对应值
     * @param[success] 更改成功的回调，获取【当前用户数据】
     * @param[error] 出现错误的回调，一般是用户相关的错误
     * @param[cloudError] 出现网络错误的回调，一般是未连接到网络或初始化错误
     */
    fun updateToCloud(objectId: String, update: Map<String, Any>?,
                      success: (UserData) -> Unit,
                      error: (Int) -> Unit,
                      cloudError: (Exception) -> Unit) {
        if (!checkDatabase()) {
            error(DataConstants.NOT_INIT)
            return
        }
        // 校验登录，获取用户数据
        userManager.checkLogin(
                success = {
                    user ->
                    val query = AVQuery<AVObject>(tableName)
                    query.getInBackground(objectId, object : GetCallback<AVObject>() {
                        override fun done(obj: AVObject?, e: AVException?) {
                            if (e == null) {
                                // 如果该数据不是与当前用户关联数据则不能删除
                                if (userField != null) {
                                    if (obj!!.getString(userField) != user.objectId) {
                                        error(DataConstants.NOT_RELATED_USER)
                                        return
                                    }
                                }
                                // 更新数据
                                if (update != null) {
                                    for ((k, v) in update) {
                                        obj!!.put(k, v)
                                    }
                                    obj!!.saveInBackground(object : SaveCallback() {
                                        override fun done(e: AVException?) {
                                            if (e == null) {
                                                success(user)
                                            } else {
                                                cloudError(e)
                                            }
                                        }
                                    })
                                }
                            } else {
                                cloudError(e)
                            }
                        }
                    })
                },
                error = {
                    e ->
                    when (e) {
                    // 若未登录则提示尚未登录
                        UserConstants.NOT_LOGGED_IN -> error(DataConstants.NOT_LOGGED_IN)
                        UserConstants.CANNOT_CHECK_LOGIN -> error(DataConstants.NOT_LOGGED_IN)
                    // 若校验登录失败则提示重新登录
                        UserConstants.USERNAME_ERROR -> error(DataConstants.CHECK_LOGIN_ERROR)
                        UserConstants.CHECKSUM_ERROR -> error(DataConstants.CHECK_LOGIN_ERROR)
                    }
                },
                cloudError = {
                    e ->
                    cloudError(e)
                }
        )
    }
}