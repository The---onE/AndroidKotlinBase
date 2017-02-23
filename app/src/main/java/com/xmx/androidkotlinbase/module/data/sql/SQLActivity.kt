package com.xmx.androidkotlinbase.module.data.sql

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.widget.AdapterView

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import kotlinx.android.synthetic.main.activity_sql.*

import java.util.ArrayList
import java.util.Date

/**
 * Created by The_onE on 2016/3/27.
 * 测试SQLite数据管理页
 */
class SQLActivity : BaseTempActivity() {

    // 数据列表适配器
    private var sqlAdapter: SQLAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_sql)

        // 列表初始化前不显示数据
        sqlAdapter = SQLAdapter(this, ArrayList<SQL>())
        listSQL.adapter = sqlAdapter
    }

    override fun setListener() {
        // 长按提示更新或删除数据
        listSQL.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapterView, view, i, l ->
            val sql = sqlAdapter?.getItem(i) as SQL

            val builder = AlertDialog.Builder(this@SQLActivity)
            builder.setMessage("要更新该记录吗？")
            builder.setTitle("提示")
            builder.setNegativeButton("删除") { dialogInterface, i ->
                // 删除数据
                sqlEntityManager.deleteById(sql.id)
                // 刷新列表
                sqlManager.updateData()
                val data = sqlManager.data
                data?.let {
                    sqlAdapter?.updateList(data)
                }
            }
            builder.setPositiveButton("更新") { dialogInterface, i ->
                // 更新数据
                sqlEntityManager.updateData(sql.id,
                        "Time = " + Date().time)
                // 刷新列表
                sqlManager.updateData()
                val data = sqlManager.data
                data?.let {
                    sqlAdapter?.updateList(data)
                }
            }
            builder.setNeutralButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
            builder.show()
            false
        }

        // 点击确定添加数据
        btnSQL.setOnClickListener {
            val data = editSQL.text.toString()
            val entity = SQL()
            entity.data = data
            entity.time = Date()
            // 添加数据
            sqlEntityManager.insertData(entity)
            editSQL.setText("")
            showToast(R.string.add_success)
            // 刷新列表
            sqlManager.updateData()
            val d = sqlManager.data
            d?.let {
                sqlAdapter?.updateList(d)
            }
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 读取数据并显示在列表中
        sqlManager.updateData()
        val data = sqlManager.data
        data?.let {
            sqlAdapter?.updateList(data)
        }
    }

    override fun onResume() {
        super.onResume()
        // Activity重新显示时刷新列表
        sqlManager.updateData()
        val data = sqlManager.data
        data?.let {
            sqlAdapter?.updateList(data)
        }
    }
}
