package com.xmx.androidkotlinbase.Tools.Data.SQL

import android.content.ContentValues
import android.database.Cursor

/**
 * Created by The_onE on 2016/3/22.
 * SQLite数据库实体接口，实现该接口后即可通过管理器管理
 */
interface ISQLEntity {
    fun tableFields(): String

    val content: ContentValues

    fun convertToEntity(c: Cursor): ISQLEntity
}