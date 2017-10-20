package com.xmx.androidkotlinbase.module.data.sql

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.common.data.BaseEntityAdapter

import java.text.SimpleDateFormat

/**
 * Created by The_onE on 2016/3/27.
 * 测试SQLite实体适配器
 */
class SQLAdapter(context: Context, data: List<SQL>) : BaseEntityAdapter<SQL>(context, data) {

    // 复用列表中的项
    private class ViewHolder {
        internal var data: TextView? = null
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_sql, parent, false)
            if (view != null) {
                holder = ViewHolder()
                holder.data = view.findViewById(R.id.itemData)
                holder.time = view.findViewById(R.id.itemTime)
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
                val sql = mData[position]
                holder.data!!.text = sql.data
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val timeString = df.format(sql.time)
                holder.time!!.text = timeString
            }
        } else {
            holder?.data!!.text = "加载失败"
        }

        return view!!
    }
}