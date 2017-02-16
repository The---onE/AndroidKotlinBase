package com.xmx.androidkotlinbase.common.user

/**
 * Created by The_onE on 2016/7/3.
 * 登录相关常量及错误码
 */
object UserConstants {
    val USER_INFO_TABLE = "UserInfo" // 云端用户信息表名
    val USER_DATA_TABLE = "UserData" // 云端用户数据表名
    val LOGIN_LOG_TABLE = "LoginLog" // 云端用户日志表名

    val USER_SHARED_PREFERENCE = "USER" // SharedPreference用户表名

    val USERNAME_EXIST = -1 // 用户名已存在
    val NICKNAME_EXIST = -2 // 昵称已存在
    val PASSWORD_ERROR = -3 // 密码错误
    val USERNAME_ERROR = -4 // 用户名不存在
    val NOT_LOGGED_IN = -5 // 尚未登录
    val CHECKSUM_ERROR = -6 // 校验码错误
    val CANNOT_CHECK_LOGIN = -10 // 尚未通过注册、登录或自动登录完成登录，不能校验登录

    val LOGIN_SUCCESS = 1 // 登录成功

    val LOGIN_REQUEST_CODE = 1000 // 登录请求代码
    val REGISTER_REQUEST_CODE = 1001 // 登录请求代码
}
