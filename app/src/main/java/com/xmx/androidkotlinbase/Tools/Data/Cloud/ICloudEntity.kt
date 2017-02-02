package com.xmx.androidkotlinbase.Tools.Data.Cloud

import com.avos.avoscloud.AVObject

/**
 * Created by The_onE on 2017/2/1.
 * LeanCloud实体接口，实现该接口后即可通过管理器管理
 */
interface ICloudEntity {
    // 将实体转化为可插入或更新的数据接口
    fun getContent(tableName: String): AVObject

    // 将查询的数据转化为实体接口
    fun convertToEntity(obj: AVObject): ICloudEntity
}