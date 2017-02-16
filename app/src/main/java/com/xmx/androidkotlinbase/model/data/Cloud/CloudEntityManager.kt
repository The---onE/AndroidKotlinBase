package com.xmx.androidkotlinbase.model.data.cloud

import android.content.Context
import android.widget.Toast
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.common.data.DataConstants
import com.xmx.androidkotlinbase.common.data.cloud.BaseCloudEntityManager
import com.xmx.androidkotlinbase.utils.ExceptionUtil

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


    /**
     * 默认的错误处理
     * @param[context] 当前上下文
     * @return 用于管理器中error参数的错误处理函数
     */
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

    /**
     * 默认的网络错误处理
     * @param[context] 当前上下文
     * @return 用于管理器中cloudError参数的网络错误处理函数
     */
    fun defaultCloudError(context: Context): (Exception) -> Unit {
        return {
            e ->
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
            ExceptionUtil.normalException(e, context)
        }
    }
}
