package com.xmx.androidkotlinbase.Tools.Data

import android.content.Context
import android.widget.BaseAdapter

/**
 * Created by The_onE on 2016/7/11.
 * 实体适配器基类，用于生成将实体显示在列表中的适配器
 */
abstract class BaseEntityAdapter<Entity>(protected var mContext: Context,
                                         protected var mData: List<Entity>) : BaseAdapter() {

    // 更新数据，重新显示列表
    fun updateList(data: List<Entity>) {
        mData = data
        notifyDataSetChanged()
    }

    // 数据条数
    override fun getCount(): Int {
        return mData.size
    }

    // 获取数据项
    override fun getItem(i: Int): Any? {
        if (i < mData.size) {
            return mData[i]
        } else {
            return null
        }
    }

    // 获取数据索引
    override fun getItemId(i: Int): Long {
        return i.toLong()
    }
}