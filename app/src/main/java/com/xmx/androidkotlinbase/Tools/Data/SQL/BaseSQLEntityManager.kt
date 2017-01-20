package com.xmx.androidkotlinbase.Tools.Data.SQL

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import com.xmx.androidkotlinbase.Constants
import com.xmx.androidkotlinbase.Tools.Utils.ExceptionUtil

import java.io.File
import java.util.ArrayList

/**
 * Created by The_onE on 2017/1/20.
 * SQLite数据库管理基类，提供常用的增删改查功能
 */
abstract class BaseSQLEntityManager<Entity : ISQLEntity> {
    // 管理数据库连接
    protected var database: SQLiteDatabase? = null
    // 数据变更时会更新版本
    protected var version = System.currentTimeMillis()
    // 是否已打开数据库
    protected var openFlag = false

    // 子类构造函数中初始化下列变量！初始化后再调用openDatabase方法
    protected var tableName: String? = null // 表名
    protected var entityTemplate: Entity? = null // 空模版，不需要数据

    // 打开数据库文件
    // filename: 若为空则使用默认数据库文件，否则使用默认目录中的给定文件
    private fun openSQLFile(filename: String? = null) {
        // 打开或创建数据库目录
        val d = android.os.Environment.getExternalStorageDirectory().toString() +
                Constants.DATABASE_DIR
        val dir = File(d) // 数据库目录路径
        val flag = dir.exists() || dir.mkdirs() // 若目录不存在则创建目录

        // 打开数据库文件
        if (flag) {
            // 若无给定文件则使用默认数据库文件，否则使用默认目录中的给定文件
            var sqlFile = android.os.Environment.getExternalStorageDirectory().toString()
            if (filename == null) {
                sqlFile += Constants.DATABASE_FILE
            } else {
                sqlFile += Constants.DATABASE_DIR + "/" + filename + ".db"
            }

            val file = File(sqlFile) // 数据库文件路径
            database = SQLiteDatabase.openOrCreateDatabase(file, null) // 打开数据库文件
            if (database == null) {
                Log.e("DatabaseError", "创建文件失败")
            }
            version++
        } else {
            Log.e("DatabaseError", "创建目录失败")
        }
    }

    // 打开数据库
    fun openDatabase(filename: String? = null): Boolean {
        // 打开数据库文件
        openSQLFile(filename)
        if (database != null && tableName != null && entityTemplate != null) {
            // 若不存在表则创建表
            val createSQL = "create table if not exists " +
                    "$tableName(${entityTemplate!!.tableFields()})"
            database!!.execSQL(createSQL)
            openFlag = true
        } else {
            openFlag = false
        }
        return openFlag
    }

    // 关闭数据库
    fun closeDatabase() {
        openFlag = false
    }

    // 检查数据库是否打开，若未打开则打开
    protected fun checkDatabase(): Boolean {
        return openFlag || openDatabase()
    }

    // 清空数据表
    fun clearDatabase() {
        if (!checkDatabase()) {
            return
        }
        // 清空表中数据
        val clear = "delete from $tableName"
        database!!.execSQL(clear)
        // 自增主键归零
        val zero = "delete from sqlite_sequence where NAME = '$tableName'"
        database!!.execSQL(zero)
        // 数据变更
        version++
    }

    // 插入实体数据
    fun insertData(entity: Entity): Long {
        if (!checkDatabase()) {
            return -1
        }
        // 插入数据
        val flag = database!!.insert(tableName, null, entity.content)
        if (flag > 0) {
            // 数据变更
            version++
        }
        return flag
    }

    // 事务操作
    fun operationInTransaction(operation: () -> Int, // 事务要进行的操作
                               success: (total: Int) -> Unit, // 事务执行成功的操作
                               error: (e: Exception) -> Unit) // 事务执行失败的操作
            : Boolean {
        if (!checkDatabase()) {
            return false
        }
        var flag = false // 事务是否执行完成
        // 开启事务
        database!!.beginTransaction()
        try {
            // 执行事务
            val total = operation()
            // 标记事务执行成功
            database!!.setTransactionSuccessful()
            // 事务执行成功
            success(total)
            flag = true
            // 数据变更
            version++
        } catch (e: Exception) {
            // 事务执行失败
            error(e)
        } finally {
            // 事务执行结束，若失败则回滚
            database!!.endTransaction()
        }
        return flag
    }

    // 插入多条实体数据，在同一事务中
    fun insertData(entities: List<Entity>,
                   success: (total: Int) -> Unit): Boolean {
        return operationInTransaction(
                // 在事务中插入数据
                operation = {
                    for (entity in entities) {
                        database!!.insert(tableName, null, entity.content)
                    }
                    return@operationInTransaction entities.size
                },
                // 插入成功
                success = {
                    total ->
                    success(total)
                },
                // 插入失败，处理异常
                error = {
                    e ->
                    ExceptionUtil.filterException(e)
                }
        )
    }

    // 插入多条实体数据，在同一事务中
    fun insertData(entities: List<Entity>,
                   proceeding: (index: Int) -> Unit,
                   success: (total: Int) -> Unit): Boolean {
        return operationInTransaction(
                // 在事务中插入数据，每插入一条进行回调
                operation = {
                    var index = 0
                    for (entity in entities) {
                        database!!.insert(tableName, null, entity.content)
                        index++
                        proceeding(index)
                    }
                    return@operationInTransaction index
                },
                // 插入成功
                success = {
                    total ->
                    success(total)
                },
                // 插入失败，处理异常
                error = {
                    e ->
                    ExceptionUtil.filterException(e)
                }
        )
    }

    //删除数据
    fun deleteById(id: Long): Int {
        if (!checkDatabase()) {
            return 0
        }
        // 删除数据
        val res = database!!.delete(tableName, "ID = ?", arrayOf(id.toString()))
        // 数据变更
        version++
        return res
    }

    // 删除多条数据，在同一事务中
    fun deleteByIds(ids: List<Long>,
                    success: (total: Int) -> Unit): Boolean {
        return operationInTransaction(
                // 在事务中删除数据
                operation = {
                    ids.forEach {
                        id ->
                        database!!.delete(tableName, "ID = ?", arrayOf(id.toString()))
                    }
                    return@operationInTransaction ids.size
                },
                // 删除成功
                success = {
                    total ->
                    success(total)
                },
                // 删除失败，处理异常
                error = {
                    e ->
                    ExceptionUtil.filterException(e)
                }
        )
    }

    // 删除多条数据，在同一事务中
    fun deleteByIds(ids: List<Long>,
                    proceeding: (index: Int) -> Unit,
                    success: (total: Int) -> Unit): Boolean {
        return operationInTransaction(
                // 在事务中删除数据，每删除一条进行回调
                operation = {
                    var index = 0
                    ids.forEach {
                        id ->
                        database!!.delete(tableName, "ID = ?", arrayOf(id.toString()))
                        index++
                        proceeding(index)
                    }
                    return@operationInTransaction index
                },
                // 删除成功
                success = {
                    total ->
                    success(total)
                },
                // 删除失败，处理异常
                error = {
                    e ->
                    ExceptionUtil.filterException(e)
                }
        )
    }

    // 删除数据
    fun deleteByCloudId(cloudId: String) {
        if (!checkDatabase()) {
            return
        }
        // 删除数据
        database!!.delete(tableName, "CLOUD_ID = ?", arrayOf(cloudId))
        // 数据变更
        version++
    }

    // 更新数据 updateData(id, "KEY1=Value1", "KEY2=Value2")
    fun updateData(id: Long, vararg strings: String) {
        if (!checkDatabase()) {
            return
        }
        // 更新内容
        var content: String = ""
        if (strings.isNotEmpty()) {
            content = strings.joinToString(prefix = "set ", separator = ", ")
        }
        // 更新数据
        val update = "update $tableName $content where ID = $id"
        database!!.execSQL(update)
        // 数据变更
        version++
    }

    // 更新数据 updateData(cloudId, "KEY1=Value1", "KEY2=Value2")
    fun updateData(cloudId: String, vararg strings: String) {
        if (!checkDatabase()) {
            return
        }
        // 更新内容
        var content: String = ""
        if (strings.isNotEmpty()) {
            content = strings.joinToString(prefix = "set ", separator = ", ")
        }
        // 更新数据
        val update = "update $tableName $content where CLOUD_ID = '$cloudId'"
        database!!.execSQL(update)
        // 数据变更
        version++
    }

    // 将查询数据转化为实体列表
    protected fun convertToEntities(cursor: Cursor): List<Entity>? {
        if (!checkDatabase()) {
            return null
        }
        val entities = ArrayList<Entity>()
        if (cursor.moveToFirst()) {
            // 将每一条数据转化为实体，并加入列表
            do {
                val entity: ISQLEntity = entityTemplate!!.convertToEntity(cursor)
                entities.add(entity as Entity)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return entities
    }

    // 将查询数据转化为实体
    private fun convertToEntity(cursor: Cursor): Entity? {
        if (!checkDatabase()) {
            return null
        }
        // 将查询数据转化为实体并返回
        if (cursor.moveToFirst()) {
            val entity: ISQLEntity = entityTemplate!!.convertToEntity(cursor)
            return entity as Entity
        } else {
            return null
        }
    }

    // 查询全部数据
    fun selectAll(): List<Entity>? {
        if (!checkDatabase()) {
            return null
        }
        // 查询全部数据并转化为实体列表
        val cursor = database!!.rawQuery("select * from $tableName!!", null)
        val entities = convertToEntities(cursor)
        cursor.close()
        return entities
    }

    // 查询全部数据并排序, ascFlag为true升序，为false降序
    fun selectAll(order: String, ascFlag: Boolean): List<Entity>? {
        if (!checkDatabase()) {
            return null
        }
        // 设置排序方式
        val asc: String
        if (ascFlag) {
            asc = "asc"
        } else {
            asc = "desc"
        }
        // 查询全部数据并转化为实体列表
        val cursor = database!!.rawQuery("select * from $tableName order by $order $asc", null)
        val entities = convertToEntities(cursor)
        cursor.close()
        return entities
    }

    // 通过ID查询数据
    fun selectById(id: Long): Entity? {
        if (!checkDatabase()) {
            return null
        }
        // 查询数据并转化为实体
        val cursor = database!!.rawQuery("select * from $tableName where ID=$id", null)
        val entity = convertToEntity(cursor)
        cursor.close()
        return entity
    }

    //通过云ID查询数据
    fun selectByCloudId(cloudId: String): Entity? {
        if (!checkDatabase()) {
            return null
        }
        // 查询数据并转化为实体
        val cursor = database!!.rawQuery("select * from $tableName where CLOUD_ID='$cloudId'"
                , null)
        val entity = convertToEntity(cursor)
        cursor.close()
        return entity
    }

    // 查询值在范围内的数据
    fun selectAmount(data: String, min: String, max: String): List<Entity>? {
        if (!checkDatabase()) {
            return null
        }
        // 查询符合条件的数据并转化为实体列表
        val cursor = database!!.rawQuery("select * from $tableName" +
                " where $data between $min and $max", null)
        val entities = convertToEntities(cursor)
        cursor.close()
        return entities
    }

    // 查询符合条件的第一条数据 selectFirst(排序字段, true为升序/false为降序, "KEY1=Value1", "KEY2!=Value2")
    fun selectFirst(order: String, ascFlag: Boolean, vararg condition: String): Entity? {
        if (!checkDatabase()) {
            return null
        }
        // 查询条件
        var content: String = ""
        if (condition.isNotEmpty()) {
            content = condition.joinToString(prefix = "where ", separator = "and ")
        }
        // 设置排序方式
        val asc: String
        if (ascFlag) {
            asc = "asc"
        } else {
            asc = "desc"
        }
        // 查询第一条符合条件的数据并转化为实体
        val cursor = database!!.rawQuery("select * from $tableName $content order by $order $asc limit 1", null)
        val entity = convertToEntity(cursor)
        cursor.close()
        return entity
    }

    //根据条件查询数据 selectByCondition("TIME", true为升序/false为降序, "KEY1=Value1", "KEY2!=Value2")
    fun selectByCondition(order: String, ascFlag: Boolean, vararg condition: String): List<Entity>? {
        if (!checkDatabase()) {
            return null
        }
        // 查询条件
        var content: String = ""
        if (condition.isNotEmpty()) {
            content = condition.joinToString(prefix = "where ", separator = "and ")
        }
        // 设置排序方式
        val asc: String
        if (ascFlag) {
            asc = "asc"
        } else {
            asc = "desc"
        }
        val cursor = database!!.rawQuery("select * from $tableName $content order by $order $asc", null)
        val entities = convertToEntities(cursor)
        cursor.close()
        return entities
    }

    // 获取数据总条数
    val count: Int
        get() {
            // 获取数据总条数并返回
            val cursor = database!!.rawQuery("select COUNT(*) from $tableName", null)
            var count = 0
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
            cursor.close()
            return count
        }
}