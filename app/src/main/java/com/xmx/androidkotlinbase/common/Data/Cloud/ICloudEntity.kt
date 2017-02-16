package com.xmx.androidkotlinbase.common.data.cloud

import com.avos.avoscloud.AVObject

/**
 * Created by The_onE on 2017/2/1.
 * LeanCloud实体接口，实现该接口后即可通过管理器管理
 */
interface ICloudEntity {
    /**
     * 将实体转化为可插入或更新的数据的接口
     * @param[tableName] 根据表名创建实体
     * @return 创建完成并填充数据的云端对象
     */
    fun getContent(tableName: String): AVObject

    /**
     * 将查询的数据转化为实体的接口
     * @param[obj] 查询到的数据项
     * @return 转化为对应类的实体
     */
    fun convertToEntity(obj: AVObject): ICloudEntity
}