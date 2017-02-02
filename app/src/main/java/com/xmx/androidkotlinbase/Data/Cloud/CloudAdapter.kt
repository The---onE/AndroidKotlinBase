package com.xmx.androidkotlinbase.Data.Cloud

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xmx.androidkotlinbase.R

import com.xmx.androidkotlinbase.Tools.Data.BaseEntityAdapter

import java.text.SimpleDateFormat

/**
 * Created by The_onE on 2016/3/27.
 * 测试Cloud实体适配器
 */
class CloudAdapter(context: Context, data: List<Cloud>) : BaseEntityAdapter<Cloud>(context, data) {

    // 复用列表中的项
    private class ViewHolder {
        internal var data: TextView? = null
        internal var time: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder?
        // 是否有可复用的项
        if (view == null) {
            // 若没有则创建新的ViewHolder
            view = LayoutInflater.from(mContext).inflate(R.layout.item_cloud, null)
            if (view != null) {
                holder = ViewHolder()
                holder.data = view.findViewById(R.id.itemData) as TextView
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
                val cloud = mData[position]
                holder.data!!.text = cloud.data
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val timeString = df.format(cloud.time)
                holder.time!!.text = timeString
            }
        } else {
            holder?.data!!.text = "加载失败"
        }

        return view!!
    }
}