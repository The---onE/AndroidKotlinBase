package com.xmx.androidkotlinbase.common.data.sql

import android.content.ContentValues
import android.database.Cursor

/**
 * Created by The_onE on 2016/3/22.
 * SQLite数据库实体接口，实现该接口后即可通过管理器管理
 */
interface ISQLEntity {
    // 数据库中的表结构接口
    fun tableFields(): String

    // 将实体转化为可插入或更新的数据接口
    fun getContent(): ContentValues

    // 将查询的数据转化为实体接口
    fun convertToEntity(c: Cursor): ISQLEntity
}