package com.xmx.androidkotlinbase.module.data.sync

import android.content.ContentValues
import android.database.Cursor

import com.avos.avoscloud.AVObject
import com.xmx.androidkotlinbase.common.data.sync.ISyncEntity

import java.util.Date

/**
 * Created by xmx on 2016/6/1.
 * 测试SQLite数据库与LeanCloud数据库同步实体
 */
class Sync : ISyncEntity {
    // 同步ID，手动生成实体默认为空串
    override var cloudId: String = ""
    // 本地数据库ID，手动生成实体默认为-1
    var id: Long = -1
    var data: String? = null
    var time: Date? = null

    // 将实体转化为可插入或更新至云端数据库的数据
    override fun getContent(tableName: String): AVObject {
        val obj = AVObject(tableName)
        // 有ID用于更新数据，无ID用于插入数据
        if (cloudId.isNotBlank()) {
            obj.objectId = cloudId
        }
        obj.put("Data", data)
        obj.put("Time", time)
        return obj
    }

    // 将从云端查询的数据转化为实体
    override fun convertToEntity(obj: AVObject): Sync {
        val entity = Sync()
        entity.cloudId = obj.objectId
        entity.data = obj.getString("Data")
        entity.time = obj.getDate("Time")
        return entity
    }

    // 本地数据库中的表结构
    override fun tableFields(): String {
        return "ID integer not null primary key autoincrement, " +
                "CLOUD_ID text not null, " +
                "Data text, " +
                "Time integer not null default(0)"
    }

    // 将实体转化为可插入或更新至本地数据库的数据
    override fun getContent(): ContentValues {
        val content = ContentValues()
        // 有ID用于更新数据，无ID用于插入数据
        if (id > 0) {
            content.put("ID", id)
        }
        // 保存同步ID用于与云端同步
        if (cloudId.isNotBlank()) {
            content.put("CLOUD_ID", cloudId)
        }
        content.put("Data", data)
        content.put("Time", time?.time)
        return content
    }

    // 将从本地数据库查询的数据转化为实体
    override fun convertToEntity(c: Cursor): Sync {
        val entity = Sync()
        entity.id = c.getLong(0)
        entity.cloudId = c.getString(1)
        entity.data = c.getString(2)
        entity.time = Date(c.getLong(3))

        return entity
    }
}
