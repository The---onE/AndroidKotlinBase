package com.xmx.androidkotlinbase.model.data.sync

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.AdapterView

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseTempActivity
import kotlinx.android.synthetic.main.activity_sync.*
import java.util.*

/**
 * Created by xmx on 2016/6/1.
 */
class SyncActivity : BaseTempActivity() {
    // 数据列表适配器
    private var syncAdapter: SyncAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_sync)

        SyncManager.instance().updateData()
        syncAdapter = SyncAdapter(this, ArrayList<Sync>())
        listSync.adapter = syncAdapter
    }

    override fun setListener() {
        // 长按提示更新或删除数据
        listSync.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapterView, view, i, l ->
            val sync = syncAdapter?.getItem(i) as Sync

            val builder = AlertDialog.Builder(this@SyncActivity)
            builder.setMessage("要更新该记录吗？")
            builder.setTitle("提示")
            builder.setNegativeButton("删除") { dialogInterface, i ->
                // 删除数据
                SyncEntityManager.instance().deleteData(sync.cloudId,
                        success = {
                            user ->
                            showToast(R.string.delete_success)
                            // 刷新列表
                            SyncManager.instance().updateData()
                            syncAdapter?.updateList(SyncManager.instance().data!!)
                        },
                        error = SyncEntityManager.instance().defaultError(this),
                        syncError = SyncEntityManager.instance().defaultSyncError(this)
                )
            }
            builder.setPositiveButton("更新") { dialogInterface, i ->
                val update = HashMap<String, Any>()
                update.put("Time", Date())
                // 更新数据
                SyncEntityManager.instance().updateData(sync.cloudId, update,
                        success = {
                            user ->
                            showToast(R.string.update_success)
                            // 刷新列表
                            SyncManager.instance().updateData()
                            syncAdapter?.updateList(SyncManager.instance().data!!)
                        },
                        error = SyncEntityManager.instance().defaultError(this),
                        syncError = SyncEntityManager.instance().defaultSyncError(this)
                )
            }
            builder.setNeutralButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
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
            SyncEntityManager.instance().insertData(entity,
                    success = {
                        user, objectId ->
                        showToast(R.string.add_success)
                        // 刷新列表
                        SyncManager.instance().updateData()
                        syncAdapter?.updateList(SyncManager.instance().data!!)
                    },
                    error = SyncEntityManager.instance().defaultError(this),
                    syncError = SyncEntityManager.instance().defaultSyncError(this)
            )
            editSync.setText("")
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 加载数据
        SyncEntityManager.instance().syncFromCloud(null,
                success = {
                    user, entities ->
                    SyncManager.instance().updateData()
                    syncAdapter?.updateList(SyncManager.instance().data!!)
                    showToast(R.string.sync_success)
                },
                error = SyncEntityManager.instance().defaultError(this),
                syncError = SyncEntityManager.instance().defaultSyncError(this)
        )
    }
}
