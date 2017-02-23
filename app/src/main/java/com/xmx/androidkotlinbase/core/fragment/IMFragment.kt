package com.xmx.androidkotlinbase.core.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.fragment.BaseFragment
import com.xmx.androidkotlinbase.common.im.IMMessageHandlerManager
import com.xmx.androidkotlinbase.common.im.imClientManager
import com.xmx.androidkotlinbase.common.user.UserConstants
import com.xmx.androidkotlinbase.common.user.userManager
import com.xmx.androidkotlinbase.module.im.IMAdapter
import com.xmx.androidkotlinbase.module.im.IMTextMessageHandler
import com.xmx.androidkotlinbase.utils.ExceptionUtil
import kotlinx.android.synthetic.main.fragment_im.*

import java.util.ArrayList

/**
 * Created by The_onE on 2017/2/20.
 * 测试数据相关组件是否运行正常，演示其使用方法
 */
class IMFragment : BaseFragment() {
    // 消息记录适配器
    private val imAdapter: IMAdapter by lazy {
        IMAdapter(context, ArrayList<AVIMTextMessage>())
    }

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_im, container, false)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    // 登录失败的处理
    val loginError = {
        e: Int ->
        when (e) {
            UserConstants.NOT_LOGGED_IN -> showToast(R.string.not_loggedin)
            UserConstants.USERNAME_ERROR -> showToast(R.string.username_error)
            UserConstants.CHECKSUM_ERROR -> showToast(R.string.not_loggedin)
            UserConstants.CANNOT_CHECK_LOGIN -> showToast(R.string.cannot_check_login)
        }
    }

    override fun setListener(view: View) {
        // 打开IM客户端
        btnOpenClient.setOnClickListener {
            // 校验登录，需先登录才能使用
            userManager.checkLogin(
                    success = {
                        user ->
                        showToast("IM客户端打开中……")
                        // 打开IM客户端
                        imClientManager.openClient(user.username!!,
                                object : AVIMClientCallback() {
                                    override fun done(imClient: AVIMClient?, e: AVIMException?) {
                                        // 客户端打开成功
                                        showToast("IM客户端打开成功")
                                    }
                                })
                    },
                    error = loginError,
                    cloudError = {
                        e ->
                        showToast(R.string.network_error)
                        ExceptionUtil.normalException(e, context)
                    }
            )
        }
        // 加入测试对话
        btnJoinConversation.setOnClickListener {
            // 创建名为"test"的对话
            imClientManager.createConversation("test",
                    success = {
                        conversation ->
                        // 创建成功
                        showToast("创建对话成功")
                        // 加入创建的对话
                        imClientManager.joinConversation(conversation,
                                success = {
                                    // 加入成功
                                    showToast("加入对话成功")
                                    // 读取并显示消息记录
                                    updateList()
                                },
                                failure = {
                                    e ->
                                    // 加入失败
                                    showToast("加入对话失败")
                                    ExceptionUtil.normalException(e, context)
                                },
                                clientError = {
                                    // 客户端尚未打开
                                    showToast("IM客户端未打开")
                                }
                        )
                    },
                    exist = {
                        conversation ->
                        // 对话已存在
                        showToast("对话已存在")
                        // 加入已存在的对话
                        imClientManager.joinConversation(conversation,
                                success = {
                                    // 加入成功
                                    showToast("加入对话成功")
                                    // 读取并显示消息记录
                                    updateList()
                                },
                                failure = {
                                    e ->
                                    // 加入失败
                                    showToast("加入对话失败")
                                    ExceptionUtil.normalException(e, context)
                                },
                                clientError = {
                                    // 客户端尚未打开
                                    showToast("IM客户端未打开")
                                }
                        )
                    },
                    failure = {
                        e ->
                        // 创建对话失败
                        showToast("创建对话失败")
                        ExceptionUtil.normalException(e, context)
                    },
                    clientError = {
                        // 客户端尚未打开
                        showToast("IM客户端未打开")
                    }
            )
        }
        // 发送消息
        btnSendMessage.setOnClickListener {
            // 读取要发送的消息
            val data = editIM.text.toString()
            // 发送消息
            imClientManager.sendText(data,
                    success = {
                        // 发送成功
                        showToast("发送成功")
                        // 清空文本框
                        editIM.setText("")
                        // 更新消息记录列表
                        updateList()
                    },
                    failure = {
                        e ->
                        // 发送失败
                        showToast("发送失败")
                        ExceptionUtil.normalException(e, context)
                    },
                    conversationError = {
                        // 尚未加入对话
                        showToast("对话未建立")
                    }
            )
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {
        // 设置消息记录列表适配器
        listIM.adapter = imAdapter
        // 创建自定义文本消息处理器
        val handler = IMTextMessageHandler(context,
                onReceive = {
                    text, from, time, conversation, client ->
                    updateList()
                })
        // 添加自定义文本消息处理器
        IMMessageHandlerManager.getInstance().addTextMessageHandler(handler)
    }

    /**
     * 读取并更新消息记录列表
     */
    fun updateList() {
        // 读取消息记录
        imClientManager.getTextChatLog(
                success = {
                    messages ->
                    // 读取成功
                    // 更新消息记录列表
                    imAdapter.updateList(messages)
                },
                failure = {
                    e ->
                    // 获取失败
                    showToast("获取聊天记录失败")
                    ExceptionUtil.normalException(e, context)
                },
                conversationError = {
                    // 尚未加入对话
                    showToast("对话未建立")
                }
        )
    }

}
