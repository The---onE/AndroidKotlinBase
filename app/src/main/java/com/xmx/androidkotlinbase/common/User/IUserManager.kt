package com.xmx.androidkotlinbase.common.user

import com.avos.avoscloud.AVException

/**
 * Created by The_onE on 2017/1/29.
 */
interface IUserManager {
    // 注销
    fun logout(proc: ((UserData) -> Unit)?): Boolean

    // 注册
    fun register(username: String, password: String, nickname: String,
                 success: () -> Unit,
                 error: (Int) -> Unit,
                 cloudError: (Exception) -> Unit)

    // 登录
    fun login(username: String, password: String,
              success: (UserData) -> Unit,
              error: (Int) -> Unit,
              cloudError: (Exception) -> Unit)

    // 用设备保存的数据自动登录
    fun autoLogin(success: (UserData) -> Unit,
                  error: (Int) -> Unit,
                  cloudError: (Exception) -> Unit)

    // 判断是否已登录，若已登录获取用户数据
    fun checkLogin(success: (UserData) -> Unit,
                   error: (Int) -> Unit,
                   cloudError: (Exception) -> Unit)
}