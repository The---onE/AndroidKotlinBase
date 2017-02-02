package com.xmx.androidkotlinbase.Data.Cloud

import com.avos.avoscloud.AVObject
import com.xmx.androidkotlinbase.Tools.Data.Cloud.ICloudEntity

import java.util.Date

/**
 * Created by The_onE on 2016/3/27.
 * 测试LeanCloud数据库实体
 */
class Cloud : ICloudEntity {
    // ID，手动生成实体默认为空
    var cloudId: String? = null
    // 数据内容
    var data: String? = null
    // 时间
    var time: Date? = null

    // 将实体转化为可插入或更新的数据
    override fun getContent(tableName: String): AVObject {
        val obj = AVObject(tableName)
        // 有ID用于更新数据，无ID用于插入数据
        if (cloudId != null) {
            obj.objectId = cloudId
        }
        obj.put("Data", data)
        obj.put("Time", time)
        return obj
    }

    // 将查询的数据转化为实体
    override fun convertToEntity(obj: AVObject): Cloud {
        val cloud = Cloud()
        cloud.cloudId = obj.objectId
        cloud.data = obj.getString("Data")
        cloud.time = obj.getDate("Time")
        return cloud
    }
}
