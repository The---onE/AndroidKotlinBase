package com.xmx.androidkotlinbase.Tools.OperationLog

import android.content.ContentValues
import android.database.Cursor

import com.xmx.androidkotlinbase.Tools.Data.SQL.ISQLEntity

import java.util.Date

/**
 * Created by The_onE on 2016/9/4.
 * 操作日志实体，保存在SQLite数据库中
 */
class OperationLog : ISQLEntity {
    var mId: Long = -1
    // 操作记录
    var mOperation: String? = null
    // 操作时间
    var mTime: Date? = null

    // 数据库中的表结构
    override fun tableFields(): String {
        return "ID integer not null primary key autoincrement, " +
                "Operation text, " +
                "Time integer not null default(0)"
    }

    // 将实体转化为可插入的数据
    override fun getContent(): ContentValues {
        val content = ContentValues()
        if (mId > 0) {
            content.put("ID", mId)
        }
        content.put("Operation", mOperation)
        content.put("Time", mTime?.time)
        return content
    }

    // 将查询的数据转化为实体
    override fun convertToEntity(c: Cursor): OperationLog {
        val entity = OperationLog()
        entity.mId = c.getLong(0)
        entity.mOperation = c.getString(1)
        entity.mTime = Date(c.getLong(2))

        return entity
    }
}
