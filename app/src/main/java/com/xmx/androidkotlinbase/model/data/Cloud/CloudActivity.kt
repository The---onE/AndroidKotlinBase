package com.xmx.androidkotlinbase.model.data.cloud

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.widget.AdapterView

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import kotlinx.android.synthetic.main.activity_cloud.*

import java.util.ArrayList
import java.util.Date
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class CloudActivity : BaseTempActivity() {
    var selfFlag = false // 是否只显示自己相关的数据

    // 数据列表适配器
    private var cloudAdapter: CloudAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_cloud)

        cloudAdapter = CloudAdapter(this, ArrayList<Cloud>())
        listCloud.adapter = cloudAdapter
    }

    override fun setListener() {
        // 长按提示更新或删除数据
        listCloud.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapterView, view, i, l ->
            val cloud = cloudAdapter?.getItem(i) as Cloud?

            val builder = AlertDialog.Builder(this@CloudActivity)
            builder.setMessage("要更新该记录吗？")
            builder.setTitle("提示")
            builder.setNegativeButton("删除") { dialogInterface, i ->
                // 删除数据
                CloudEntityManager.instance().deleteFromCloud(cloud!!.cloudId!!,
                        success = {
                            user ->
                            showToast(R.string.delete_success)
                            // 刷新列表
                            updateList()
                        },
                        error = CloudEntityManager.instance().defaultError(this),
                        cloudError = CloudEntityManager.instance().defaultCloudError(this)
                )
            }
            builder.setPositiveButton("更新") { dialogInterface, i ->
                val update = HashMap<String, Any>()
                update.put("Time", Date())
                // 更新数据
                CloudEntityManager.instance().updateToCloud(cloud!!.cloudId!!, update,
                        success = {
                            user ->
                            showToast(R.string.update_success)
                            // 刷新列表
                            updateList()
                        },
                        error = CloudEntityManager.instance().defaultError(this),
                        cloudError = CloudEntityManager.instance().defaultCloudError(this)
                )
            }
            builder.setNeutralButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
            builder.show()
            false
        }

        // 点击确定添加数据
        btnCloudAdd.setOnClickListener {
            val data = editCloud.text.toString()
            val entity = Cloud()
            entity.data = data
            entity.time = Date()
            // 添加数据
            CloudEntityManager.instance().insertToCloud(entity,
                    success = {
                        user, objectId ->
                        showToast(R.string.add_success)
                        // 刷新列表
                        updateList()
                    },
                    error = CloudEntityManager.instance().defaultError(this),
                    cloudError = CloudEntityManager.instance().defaultCloudError(this)
            )
            editCloud.setText("")
        }
        // 只查询自己的数据
        btnCloudSelf.setOnClickListener {
            selfFlag = true
            updateList()
        }
        // 查询全部数据
        btnCloudAll.setOnClickListener {
            selfFlag = false
            updateList()
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 读取数据并显示在列表中
        updateList()
    }

    // 查询数据并更新至列表中
    private fun updateList() {
        // 查询数据
        if (selfFlag) {
            // 查询自己的数据
            CloudEntityManager.instance().selectByCondition(null,
                    "Time", false,
                    success = {
                        // 添加用户参数，查询用户数据
                        user, clouds ->
                        cloudAdapter?.updateList(clouds)
                    },
                    error = CloudEntityManager.instance().defaultError(this),
                    cloudError = CloudEntityManager.instance().defaultCloudError(this)
            )
        } else {
            // 查询全部数据
            CloudEntityManager.instance().selectByCondition(null,
                    "Time", false,
                    success = {
                        // 不添加用户参数，查询全部数据
                        clouds ->
                        cloudAdapter?.updateList(clouds)
                    },
                    error = CloudEntityManager.instance().defaultError(this),
                    cloudError = CloudEntityManager.instance().defaultCloudError(this)
            )
        }
    }
}
