package com.xmx.androidkotlinbase.Data.SQL

import android.content.ContentValues
import android.database.Cursor

import com.xmx.androidkotlinbase.Tools.Data.SQL.ISQLEntity

import java.util.Date

/**
 * Created by The_onE on 2016/3/27.
 * 测试SQLite数据库实体
 */
class SQL : ISQLEntity {
    // ID，手动生成实体默认为-1
    public var id: Long = -1
    // 数据内容
    public var data: String? = null
    // 时间
    public var time: Date? = null

    // 数据库中的表结构
    override fun tableFields(): String {
        return "ID integer not null primary key autoincrement, " +
                "Data text, " +
                "Time integer not null default(0)"

    }

    // 将实体转化为可插入或更新的数据
    override fun getContent(): ContentValues {
        val content = ContentValues()
        // 有ID用于更新数据，无ID用于插入数据
        if (id > 0) {
            content.put("ID", id)
        }
        content.put("Data", data)
        content.put("Time", time?.time)
        return content
    }

    // 将查询的数据转化为实体
    override fun convertToEntity(c: Cursor): SQL {
        val entity = SQL()
        entity.id = c.getLong(0)
        entity.data = c.getString(1)
        entity.time = Date(c.getLong(2))

        return entity
    }
}
