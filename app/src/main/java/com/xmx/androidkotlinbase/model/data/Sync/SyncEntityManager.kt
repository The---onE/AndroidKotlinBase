package com.xmx.androidkotlinbase.model.data.sync

import android.content.Context
import android.widget.Toast
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.common.data.DataConstants
import com.xmx.androidkotlinbase.common.data.sync.BaseSyncEntityManager
import com.xmx.androidkotlinbase.utils.ExceptionUtil

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
     * @return 用于管理器中syncError参数的网络错误处理函数
     */
    fun defaultSyncError(context: Context): (Exception) -> Unit {
        return {
            e ->
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
            ExceptionUtil.normalException(e, context)
        }
    }
}
