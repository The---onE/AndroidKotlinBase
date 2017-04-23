package com.xmx.androidkotlinbase.common.user

import com.avos.avoscloud.AVException

/**
 * Created by The_onE on 2017/1/29.
 * 用户管理器接口，通过该接口提供的服务进行注册登录等操作
 * 登录后在设备保留校验信息，之后可通过校验信息自动登录
 * 自动登录时比对校验信息，确认无误则成功登录并更新校验信息
 * 用户在其他设备登录会导致本设备校验信息失效，需重新登录
 * 在登录或自动登录成功后才可校验登录获取数据，用于确认是否在其他设备登录或登录过期
 */
interface IUserManager {

    /**
     * 判断是否已登录
     * @return 是否已登录
     */
    fun isLoggedIn(): Boolean

    /**
     * 注销接口
     * @param[proc] 注销成功的处理，返回注销用户数据
     * @return 是否注销成功
     */
    fun logout(proc: ((UserData) -> Unit)?): Boolean

    /**
     * 注册接口
     * @param[username] 用户名
     * @param[password] 密码，未经加密的密码
     * @param[nickname] 昵称
     * @param[success] 注册成功的处理
     * @param[error] 注册失败的处理，一般由于和已注册用户冲突
     * @param[cloudError] 网络错误注册失败的处理
     */
    fun register(username: String, password: String, nickname: String,
                 success: () -> Unit,
                 error: (Int) -> Unit,
                 cloudError: (Exception) -> Unit)

    /**
     * 登录接口
     * @param[username] 用户名
     * @param[password] 密码，未经加密的密码
     * @param[success] 登录成功的处理，获取该用户数据
     * @param[error] 登录失败的处理，一般由于用户名密码不匹配
     * @param[cloudError] 网络错误登录失败的处理
     */
    fun login(username: String, password: String,
              success: (UserData) -> Unit,
              error: (Int) -> Unit,
              cloudError: (Exception) -> Unit)

    /**
     * 用设备保存的数据自动登录
     * @param[success] 登录成功的处理，获取该用户数据
     * @param[error] 登录失败的处理，一般由于登录过期或在其他设备登录
     * @param[cloudError] 网络错误登录失败的处理
     */
    fun autoLogin(success: (UserData) -> Unit,
                  error: (Int) -> Unit,
                  cloudError: (Exception) -> Unit)

    /**
     * 判断是否已登录，若已登录获取用户数据
     * @param[success] 登录成功的处理，获取该用户数据
     * @param[error] 登录失败的处理，一般由于未登录或在其他设备登录
     * @param[cloudError] 网络错误登录失败的处理
     */
    fun checkLogin(success: (UserData) -> Unit,
                   error: (Int) -> Unit,
                   cloudError: (Exception) -> Unit)
}