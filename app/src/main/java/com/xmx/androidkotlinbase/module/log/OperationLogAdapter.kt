package com.xmx.androidkotlinbase.module.log

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xmx.androidkotlinbase.R

import com.xmx.androidkotlinbase.common.data.BaseEntityAdapter
import com.xmx.androidkotlinbase.common.log.OperationLog

import java.text.SimpleDateFormat

/**
 * Created by The_onE on 2016/3/27.
 * 操作日志实体适配器
 */
class OperationLogAdapter(context: Context, data: List<OperationLog>) : BaseEntityAdapter<OperationLog>(context, data) {
    // 复用列表中的项
    private class ViewHolder {
        internal var operation: TextView? = null
        internal var time: TextView? = null
    }

    // 将数据填充到列表项中
    @SuppressLint("SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder?
        // 是否有可复用的项
        if (view == null) {
            // 若没有则创建新的ViewHolder
            view = LayoutInflater.from(mContext).inflate(R.layout.item_operation_log, parent, false)
            if (view != null) {
                holder = ViewHolder()
                holder.operation = view.findViewById(R.id.itemOperation) as TextView
                holder.time = view.findViewById(R.id.itemTime) as TextView
                view.tag = holder
            } else {
                holder = null
            }
        } else {
            holder = view.tag as ViewHolder
        }

        // 将数据显示在列表项中
        if (position < mData.size) {
            if (holder != null) {
                val log = mData[position]
                holder.operation!!.text = log.mOperation
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val timeString = df.format(log.mTime)
                holder.time!!.text = timeString
            }
        } else {
            holder?.operation!!.text = "加载失败"
        }

        return view!!
    }
}