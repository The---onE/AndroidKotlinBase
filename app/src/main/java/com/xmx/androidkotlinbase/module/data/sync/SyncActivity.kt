package com.xmx.androidkotlinbase.module.data.sync

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.AdapterView

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import kotlinx.android.synthetic.main.activity_sync.*
import java.util.*

/**
 * Created by xmx on 2016/6/1.
 * 测试SQLite与LeanCloud数据同步管理页面
 */
class SyncActivity : BaseTempActivity() {
    // 数据列表适配器
    private var syncAdapter: SyncAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_sync)

        SyncManager.updateData()
        syncAdapter = SyncAdapter(this, ArrayList())
        listSync.adapter = syncAdapter
    }

    override fun setListener() {
        // 长按提示更新或删除数据
        listSync.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, i, _ ->
            val sync = syncAdapter?.getItem(i) as Sync

            val builder = AlertDialog.Builder(this@SyncActivity)
            builder.setMessage("要更新该记录吗？")
            builder.setTitle("提示")
            builder.setNegativeButton("删除") { _, _ ->
                // 删除数据
                SyncEntityManager.deleteData(sync.cloudId,
                        success = {
                            showToast(R.string.delete_success)
                            // 刷新列表
                            SyncManager.updateData()
                            syncAdapter?.updateList(SyncManager.data!!)
                        },
                        error = SyncEntityManager.defaultError(this),
                        syncError = SyncEntityManager.defaultSyncError(this)
                )
            }
            builder.setPositiveButton("更新") { _, _ ->
                val update = HashMap<String, Any>()
                update.put("Time", Date())
                // 更新数据
                SyncEntityManager.updateData(sync.cloudId, update,
                        success = {
                            showToast(R.string.update_success)
                            // 刷新列表
                            SyncManager.updateData()
                            syncAdapter?.updateList(SyncManager.data!!)
                        },
                        error = SyncEntityManager.defaultError(this),
                        syncError = SyncEntityManager.defaultSyncError(this)
                )
            }
            builder.setNeutralButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
            builder.show()
            false
        }

        // 点击确定添加数据
        btnSync.setOnClickListener {
            val data = editSync.text.toString()
            val entity = Sync()
            entity.data = data
            entity.time = Date()
            // 添加数据
            SyncEntityManager.insertData(entity,
                    success = { _, _ ->
                        showToast(R.string.add_success)
                        // 刷新列表
                        SyncManager.updateData()
                        syncAdapter?.updateList(SyncManager.data!!)
                    },
                    error = SyncEntityManager.defaultError(this),
                    syncError = SyncEntityManager.defaultSyncError(this)
            )
            editSync.setText("")
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 加载数据
        SyncEntityManager.syncFromCloud(null,
                success = { _, _ ->
                    SyncManager.updateData()
                    syncAdapter?.updateList(SyncManager.data!!)
                    showToast(R.string.sync_success)
                },
                error = SyncEntityManager.defaultError(this),
                syncError = SyncEntityManager.defaultSyncError(this)
        )
    }
}
