package com.xmx.androidkotlinbase.common.data.sql

import android.content.ContentValues
import android.database.Cursor

/**
 * Created by The_onE on 2016/3/22.
 * SQLite数据库实体接口，实现该接口后即可通过管理器管理
 */
interface ISQLEntity {
    /**
     * 获取数据库中的表结构的接口
     * @return 表结构
     */
    fun tableFields(): String

    /**
     * 将实体转化为可插入或更新的数据的接口
     * @return 创建完成并填充数据的数据库对象
     */
    fun getContent(): ContentValues

    /**
     * 将查询的数据转化为实体的接口
     * @param[c] 查询结果集
     * @return 转化为对应类的实体
     */
    fun convertToEntity(c: Cursor): ISQLEntity
}