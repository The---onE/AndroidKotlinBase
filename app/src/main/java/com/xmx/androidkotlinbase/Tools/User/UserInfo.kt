package com.xmx.androidkotlinbase.Tools.User

import com.avos.avoscloud.AVObject

/**
 * Created by The_onE on 2017/1/22.
 * 用户信息实体
 */
class UserInfo {
    var objectId: String? = null // ID
    var username: String? = null // 用户名
    var status: Int? = null // 状态
    var dataPointer : AVObject? = null // 数据指针，通过fetchIfNeededInBackground进行获取

    // 将LeanCloud对象转化为实体
    companion object {
        fun convert(o: AVObject): UserInfo {
            val info = UserInfo()
            info.objectId = o.objectId
            info.username = o.getString("username")
            info.status = o.getInt("status")
            info.dataPointer = o.getAVObject<AVObject>("data")

            return info
        }
    }
}