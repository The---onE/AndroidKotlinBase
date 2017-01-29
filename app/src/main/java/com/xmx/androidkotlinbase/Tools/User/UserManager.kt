package com.xmx.androidkotlinbase.Tools.User

import android.content.Context
import android.content.SharedPreferences
import com.avos.avoscloud.*

import com.xmx.androidkotlinbase.Constants
import com.xmx.androidkotlinbase.Tools.Utils.ExceptionUtil

import java.security.MessageDigest
import java.util.Random
import com.avos.avoscloud.AVException
import com.avos.avoscloud.SignUpCallback
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.LogInCallback


/**
 * Created by The_onE on 2016/1/10.
 * 用户管理器
 */
class UserManager private constructor() : IUserManager {
    companion object {
        // 单例模式
        private var instance: UserManager? = null

        @Synchronized fun instance(): UserManager {
            if (null == instance) {
                instance = UserManager()
            }
            return instance!!
        }

        // 默认的登录事件
        fun defaultLogin(user: UserData): Unit {
            // 关注消息推送中用户订阅的频道
//        val subscribing = user.getList("subscribing")
//        if (subscribing != null) {
//            for (sub in subscribing) {
//                PushService.subscribe(mContext, UserManager.getSHA(sub), ReceiveMessageActivity::class.java)
//            }
//            AVInstallation.getCurrentInstallation().saveInBackground()
//        }
        }

        // 默认的注销事件
        fun defaultLogout(user: UserData): Unit {
            //SyncEntityManager.getInstance().getSQLManager().clearDatabase();

            // 取消关注消息推送中用户订阅的频道
//        val subscribing = user.getList("subscribing")
//        if (subscribing != null) {
//            for (sub in subscribing) {
//                PushService.unsubscribe(mContext, UserManager.getSHA(sub))
//            }
//            AVInstallation.getCurrentInstallation().saveInBackground()
//        }
        }

        // 生成数字ID
        fun getId(user: AVObject): Long {
            val id = user.objectId
            return Math.abs(id.hashCode()).toLong()
        }

        // 字符串SHA加密
        fun getSHA(s: String): String {
            try {
                val md = MessageDigest.getInstance("SHA-256")
                md.update(s.toByteArray(charset("UTF-8")))
                val digest = md.digest()
                return String.format("%064x", java.math.BigInteger(1, digest))
            } catch (e: Exception) {
                ExceptionUtil.filterException(e)
            }
            return ""
        }

        // 生成随机校验码
        fun makeChecksum(): String {
            val checksum = Random().nextInt()
            return "$checksum"
        }

        // 生成用于AVUser的密码
        fun makeAVPassword(username: String, seed: String): String {
            return getSHA("$username$seed")
        }
    }

    private var mContext: Context? = null
    // 在设备中存储登录信息
    private var mSP: SharedPreferences? = null

    // 是否已登录(注册、登录或自动登录)
    private var loginFlag = false

    fun setContext(context: Context) {
        mContext = context
        mSP = context.getSharedPreferences(Constants.USER_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }

    // 获取设备存储的校验码
    fun getChecksum(): String {
        return mSP?.getString("checksum", "") ?: ""
    }

    // 获取设备存储的用户名
    fun getUsername(): String {
        return mSP?.getString("username", "") ?: ""
    }

    // 判断是否已登录
    fun isLoggedIn(): Boolean {
        return mSP?.getBoolean("loggedin", false) ?: false
    }

    // 登录成功后的处理
    private fun loginProc(user: UserData, un: String, cs: String, nn: String,
                          proc: ((UserData) -> Unit)? = null) {
        loginFlag = true
        // 将用户信息保存到设备
        val editor = mSP?.edit()
        if (editor != null) {
            editor.putBoolean("loggedin", true)
            editor.putString("username", un)
            editor.putString("checksum", cs)
            editor.putString("nickname", nn)
            editor.apply()
        }

        // 将登录信息保存到云端日志
        saveLog(un)

        // 自定义处理
        if (proc != null) {
            proc(user)
        } else {
            defaultLogin(user)
        }
    }

    // 在云端保存登录日志
    private fun saveLog(username: String) {
        val post = AVObject(Constants.LOGIN_LOG_TABLE)
        post.put("username", username)
        post.put("status", 0)
        post.put("timestamp", System.currentTimeMillis() / 1000)
        // 保存登录日志
        post.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                e?.apply {
                    ExceptionUtil.filterException(this)
                }
            }
        })
    }

    // 注销后的处理
    private fun logoutProc(user: UserData,
                           proc: ((UserData) -> Unit)? = null) {
        loginFlag = false
        // 清空在设备存储的用户信息
        val editor = mSP?.edit()
        if (editor != null) {
            editor.putBoolean("loggedin", false)
            editor.putString("username", "")
            editor.putString("checksum", "")
            editor.putString("nickname", "")
            editor.apply()
        }

        // 自定义处理
        if (proc != null) {
            proc(user)
        } else {
            defaultLogout(user)
        }
    }

    // 注册AVUser
    private fun registerAVUser(username: String, seed: String,
                               success: () -> Unit,
                               error: (Exception) -> Unit) {
        val user = AVUser() // 新建 AVUser 对象实例
        user.username = username // 设置用户名
        user.setPassword(makeAVPassword(username, seed)) // 设置密码
        user.signUpInBackground(object : SignUpCallback() {
            override fun done(e: AVException?) {
                if (e == null) {
                    // 注册成功
                    success()
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    error(e)
                }
            }
        })
    }

    // 登录AVUser
    private fun loginAVUser(username: String, seed: String,
                            success: () -> Unit,
                            error: (Exception) -> Unit) {
        AVUser.logInInBackground(username, makeAVPassword(username, seed),
                object : LogInCallback<AVUser>() {
                    override fun done(avUser: AVUser, e: AVException?) {
                        if (e == null) {
                            success()
                        } else {
                            error(e)
                        }
                    }
                })
    }

    // 注销
    override fun logout(proc: ((UserData) -> Unit)?): Boolean {
        // 若为登录状态则执行注销
        if (isLoggedIn()) {
            // 根据用户名查找当前登录的用户数据
            val username = getUsername()
            val query = AVQuery<AVObject>(Constants.USER_DATA_TABLE)
            query.whereEqualTo("username", username)
            query.findInBackground(object : FindCallback<AVObject>() {
                override fun done(list: List<AVObject>, e: AVException?) {
                    if (e == null) {
                        if (list.isNotEmpty()) {
                            // 获取当前用户数据
                            val user = list[0]
                            val data = UserData.convert(user)
                            // 处理用户注销
                            logoutProc(data, proc)
                            // AVUser登出
                            AVUser.logOut();
                            // 取消关注用户订阅频道
                            /*List<String> subscribing = user.getList("subscribing");
                            if (subscribing != null) {
                                for (String sub : subscribing) {
                                    PushService.unsubscribe(mContext, UserManager.getSHA(sub));
                                }
                                AVInstallation.getCurrentInstallation().saveInBackground();
                            }*/
                        }
                    } else {
                        e.printStackTrace()
                    }
                }
            })
            return true
        }
        return false
    }

    // 注册
    override fun register(username: String, password: String, nickname: String,
                          success: () -> Unit,
                          error: (Int) -> Unit,
                          cloudError: (Exception) -> Unit) {
        val query = AVQuery<AVObject>(Constants.USER_INFO_TABLE)
        // 获取是否有该用户名的用户
        query.whereEqualTo("username", username)
        query.countInBackground(object : CountCallback() {
            override fun done(count: Int, e: AVException?) {
                if (e == null) {
                    if (count > 0) {
                        // 该用户名已存在
                        error(UserConstants.USERNAME_EXIST)
                    } else {
                        // 该用户名可用
                        val query2 = AVQuery<AVObject>(Constants.USER_DATA_TABLE)
                        // 获取是否有该昵称的用户
                        query2.whereEqualTo("nickname", nickname)
                        query2.countInBackground(object : CountCallback() {
                            override fun done(i: Int, e: AVException?) {
                                if (e == null) {
                                    if (i > 0) {
                                        // 昵称已存在
                                        error(UserConstants.NICKNAME_EXIST)
                                    } else {
                                        // 可以注册
                                        // 添加用户信息
                                        val post = AVObject(Constants.USER_INFO_TABLE)
                                        post.put("username", username)
                                        post.put("password", UserManager.getSHA(password))
                                        post.put("status", 0)
                                        post.put("timestamp", System.currentTimeMillis() / 1000)
                                        // 添加用户数据
                                        val data = AVObject(Constants.USER_DATA_TABLE)
                                        data.put("username", username)
                                        data.put("nickname", nickname)
                                        val checksum = UserManager.makeChecksum()
                                        data.put("checksumA", UserManager.getSHA(checksum))
                                        // 用于登录AVUser的字符串
                                        val seed = UserManager.makeChecksum()
                                        data.put("checksumAV", seed)
                                        // 用户信息与数据关联
                                        post.put("data", data)
                                        // 设置用户信息权限只读
                                        val acl = AVACL()
                                        acl.publicReadAccess = true
                                        acl.publicWriteAccess = false
                                        post.acl = acl
                                        // 保存数据
                                        post.saveInBackground(object : SaveCallback() {
                                            override fun done(e: AVException?) {
                                                if (e == null) {
                                                    // 保存成功
                                                    // 注册AVUser
                                                    registerAVUser(username, seed,
                                                            success = {
                                                                // 以注册用户进行登录
                                                                val d = UserData.convert(data)
                                                                loginProc(d, username, checksum, nickname)
                                                                success()
                                                            },
                                                            error = {
                                                                e ->
                                                                cloudError(e)
                                                            })
                                                } else {
                                                    cloudError(e)
                                                }
                                            }
                                        })
                                    }
                                } else {
                                    cloudError(e)
                                }
                            }
                        })
                    }
                } else {
                    cloudError(e)
                }
            }
        })
    }

    // 登录
    override fun login(username: String, password: String,
                       success: (UserData) -> Unit,
                       error: (Int) -> Unit,
                       cloudError: (Exception) -> Unit) {
        val query = AVQuery<AVObject>(Constants.USER_INFO_TABLE)
        // 获取是否有该用户名的用户
        query.whereEqualTo("username", username)
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: List<AVObject>, e: AVException?) {
                if (e == null) {
                    if (avObjects.isNotEmpty()) {
                        // 获取该用户名的用户信息
                        val user = avObjects[0]
                        val rightPassword = user.getString("password")
                        // 比对加密后的密码
                        if (rightPassword == getSHA(password)) {
                            // 密码匹配成功，登录成功
                            // 获取用户数据
                            user.getAVObject<AVObject>("data")
                                    .fetchIfNeededInBackground(object : GetCallback<AVObject>() {
                                        override fun done(data: AVObject, e: AVException?) {
                                            if (e == null) {
                                                // 获取基本数据
                                                val nickname = data.getString("nickname")
                                                // 获取用于登录AVUser的字符串
                                                val seed = data.getString("checksumAV")
                                                // 生成新的校验码，存于设备和云端用于自动登录校验
                                                val newChecksum = makeChecksum()
                                                data.put("checksumA", getSHA(newChecksum))
                                                data.saveInBackground(object : SaveCallback() {
                                                    override fun done(e: AVException?) {
                                                        if (e == null) {
                                                            // 登录AVUser
                                                            loginAVUser(username, seed,
                                                                    success = {
                                                                        // 处理用户登录
                                                                        val d = UserData.convert(data)
                                                                        loginProc(d, username,
                                                                                newChecksum, nickname)
                                                                        success(d)
                                                                    },
                                                                    error = {
                                                                        e ->
                                                                        cloudError(e)
                                                                    })
                                                        } else {
                                                            cloudError(e)
                                                        }
                                                    }
                                                })
                                            } else {
                                                cloudError(e)
                                            }
                                        }
                                    })
                        } else {
                            // 密码匹配失败
                            error(UserConstants.PASSWORD_ERROR)
                        }
                    } else {
                        // 用户名不存在
                        error(UserConstants.USERNAME_ERROR)
                    }
                } else {
                    cloudError(e)
                }
            }
        })
    }

    // 用设备保存的数据自动登录，更新校验码
    override fun autoLogin(success: (UserData) -> Unit,
                           error: (Int) -> Unit,
                           cloudError: (Exception) -> Unit) {
        val username = getUsername()
        // 用户未登陆过
        if (!isLoggedIn() || username.isEmpty()) {
            // 用户未登录
            error(UserConstants.NOT_LOGGED_IN)
            return
        }
        val query = AVQuery<AVObject>(Constants.USER_DATA_TABLE)
        // 获取是否有该用户名的用户
        query.whereEqualTo("username", username)
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(list: List<AVObject>, e: AVException?) {
                if (e == null) {
                    if (list.isNotEmpty()) {
                        // 获取该用户名的用户数据
                        val user = list[0]
                        val data = UserData.convert(user)
                        val checksum = user.getString("checksumA")
                        // 比对设备存储的校验码是否与云端一致
                        if (checksum == getSHA(getChecksum())) {
                            // 校验码一致，自动登录成功
                            val nickname = user.getString("nickname")
                            // 获取用于登录AVUser的字符串
                            val seed = user.getString("checksumAV")
                            // 生成新的校验码用于下次自动登录
                            val newChecksum = makeChecksum()
                            user.put("checksumA", getSHA(newChecksum))
                            user.saveInBackground(object : SaveCallback() {
                                override fun done(e: AVException?) {
                                    if (e == null) {
                                        // 登录AVUser
                                        loginAVUser(username, seed,
                                                success = {
                                                    // 处理用户登录
                                                    loginProc(data, username, newChecksum, nickname)
                                                    success(data)
                                                },
                                                error = {
                                                    e ->
                                                    cloudError(e)
                                                })
                                    } else {
                                        cloudError(e)
                                    }
                                }
                            })
                        } else {
                            // 校验不一致，用户在其他设备登录，自动注销，需重新登录
                            logoutProc(data)
                            error(UserConstants.CHECKSUM_ERROR)
                        }
                    } else {
                        // 用户不存在，自动注销，需重新登录
                        error(UserConstants.USERNAME_ERROR)
                    }
                } else {
                    cloudError(e)
                }
            }
        })
    }

    // 判断是否已登录，若已登录获取用户数据
    override fun checkLogin(success: (UserData) -> Unit,
                            error: (Int) -> Unit,
                            cloudError: (Exception) -> Unit) {
        // 若尚未通过注册、登录或自动登录完成登录，则不能校验登录
        if (!loginFlag) {
            error(UserConstants.CANNOT_CHECK_LOGIN)
        }
        val query = AVQuery<AVObject>(Constants.USER_DATA_TABLE)
        // 获取是否有该用户名的用户
        query.whereEqualTo("username", getUsername())
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(list: List<AVObject>, e: AVException?) {
                if (e == null) {
                    if (list.isNotEmpty()) {
                        // 获取该用户名的用户数据
                        val user = list[0]
                        val data = UserData.convert(user)
                        // 比对设备存储的校验码是否与云端一致
                        val checksum = user.getString("checksumA")
                        if (checksum == getSHA(getChecksum())) {
                            // 校验登录成功，获取用户数据
                            success(data)
                        } else {
                            // 校验不一致，用户在其他设备登录，自动注销，需重新登录
                            logoutProc(data)
                            error(UserConstants.CHECKSUM_ERROR)
                        }
                    } else {
                        // 用户不存在，自动注销，需重新登录
                        error(UserConstants.USERNAME_ERROR)
                    }
                } else {
                    cloudError(e)
                }
            }
        })
    }
}
