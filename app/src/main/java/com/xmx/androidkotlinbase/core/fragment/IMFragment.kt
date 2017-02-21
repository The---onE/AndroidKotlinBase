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
import com.xmx.androidkotlinbase.common.im.Callback.CreateConversationCallback
import com.xmx.androidkotlinbase.common.im.Callback.GetTextChatLogCallback
import com.xmx.androidkotlinbase.common.im.Callback.JoinConversationCallback
import com.xmx.androidkotlinbase.common.im.Callback.SendMessageCallback
import com.xmx.androidkotlinbase.common.im.IMMessageHandlerManager
import com.xmx.androidkotlinbase.common.im.imClientManager
import com.xmx.androidkotlinbase.common.user.UserConstants
import com.xmx.androidkotlinbase.common.user.userManager
import com.xmx.androidkotlinbase.model.IM.IMAdapter
import com.xmx.androidkotlinbase.model.IM.IMTextMessageHandler
import com.xmx.androidkotlinbase.model.IM.OnReceiveCallback
import com.xmx.androidkotlinbase.utils.ExceptionUtil
import kotlinx.android.synthetic.main.fragment_im.*

import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class IMFragment : BaseFragment() {
    private var imAdapter: IMAdapter? = null

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_im, container, false)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    // 登录失败的处理
    val loginError = {
        e: Int ->
        when (e) {
//            UserConstants.NOT_LOGGED_IN -> showToast(R.string.not_loggedin)
            UserConstants.USERNAME_ERROR -> showToast(R.string.username_error)
            UserConstants.CHECKSUM_ERROR -> showToast(R.string.not_loggedin)
            UserConstants.CANNOT_CHECK_LOGIN -> showToast(R.string.cannot_check_login)
        }
    }

    override fun setListener(view: View) {
        btnOpenClient.setOnClickListener {
            userManager.checkLogin(
                    success = {
                        user ->
                        showToast("IM客户端打开中……")
                        imClientManager.openClient(user.username!!,
                                object : AVIMClientCallback() {
                                    override fun done(avimClient: AVIMClient?, e: AVIMException?) {
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

        btnJoinConversation.setOnClickListener {
            imClientManager.createConversation("test", object : CreateConversationCallback() {
                override fun success(conversation: AVIMConversation) {
                    showToast("创建对话成功")
                    imClientManager.joinConversation(conversation, object : JoinConversationCallback() {
                        override fun success(conversation: AVIMConversation) {
                            showToast("加入对话成功")
                            updateList()
                        }

                        override fun failure(e: Exception) {
                            showToast("加入对话失败")
                            ExceptionUtil.normalException(e, context)
                        }

                        override fun clientError() {
                            showToast("IM客户端未打开")
                        }
                    })
                }

                override fun failure(e: Exception) {
                    showToast("创建对话失败")
                    ExceptionUtil.normalException(e, context)
                }

                override fun exist(conversation: AVIMConversation) {
                    showToast("对话已存在")
                    imClientManager.joinConversation(conversation, object : JoinConversationCallback() {
                        override fun success(conversation: AVIMConversation) {
                            showToast("加入对话成功")
                            updateList()
                        }

                        override fun failure(e: Exception) {
                            showToast("加入对话失败")
                            ExceptionUtil.normalException(e, context)
                        }

                        override fun clientError() {
                            showToast("IM客户端未打开")
                        }
                    })
                }

                override fun clientError() {
                    showToast("IM客户端未打开")
                }
            })
        }

        btnSendMessage.setOnClickListener {
            val data = editIM.text.toString()
            editIM.setText("")
            imClientManager.sendText(data, object : SendMessageCallback() {
                override fun success() {
                    showToast("发送成功")
                    updateList()
                }

                override fun failure(e: Exception) {
                    showToast("发送失败")
                    ExceptionUtil.normalException(e, context)
                }

                override fun conversationError() {
                    showToast("对话未建立")
                }
            })
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {
        imAdapter = IMAdapter(context, ArrayList<AVIMTextMessage>())
        listIM.adapter = imAdapter

        val handler = IMTextMessageHandler(context, object : OnReceiveCallback() {
            override fun receive(text: String, from: String, time: Long, conversation: AVIMConversation, client: AVIMClient) {
                updateList()
            }
        })
        IMMessageHandlerManager.getInstance().addTextMessageHandler(handler)
    }

    fun updateList() {
        imClientManager.getTextChatLog(object : GetTextChatLogCallback() {
            override fun success(messages: List<AVIMTextMessage>) {
                imAdapter?.updateList(messages)
            }

            override fun failure(e: Exception) {
                showToast("获取聊天记录失败")
                ExceptionUtil.normalException(e, context)
            }

            override fun conversationError() {
                showToast("对话未建立")
            }
        })
    }

}
