package com.xmx.androidkotlinbase.common.im

import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.AVIMMessage
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage

import java.util.ArrayList

/**
 * Created by wli on 15/8/13.
 */
object imClientManager {

    private var client: AVIMClient? = null
    //获取用户名，IM客户端以用户名为唯一标识
    var username: String? = null
        private set
    private var openFlag = false
    internal var currentConversation: AVIMConversation? = null

    internal fun checkClient(): Boolean {
        return openFlag && client != null && !username.isNullOrEmpty()
    }

    //打开客户端，所有操作必须在打开客户端之后进行
    fun openClient(username: String,
                   callback: AVIMClientCallback) {
        this.username = username
        client = AVIMClient.getInstance(username)
        client!!.open(callback)
        openFlag = true
    }

    //关闭客户端
    fun closeClient(callback: AVIMClientCallback) {
        if (checkClient()) {
            client!!.close(callback)
            openFlag = false
            client = null
            username = ""
        }
    }

    //创建对话，若同名对话已存在不创建，回调函数中返回对应对话
    fun createConversation(name: String,
                           success: (AVIMConversation) -> Unit,
                           exist: (AVIMConversation) -> Unit,
                           failure: (Exception) -> Unit,
                           clientError: () -> Unit) {
        if (checkClient()) {
            val query = client!!.query
            query.whereEqualTo("name", name)
            query.findInBackground(object : AVIMConversationQueryCallback() {
                override fun done(convs: List<AVIMConversation>?, e: AVIMException?) {
                    if (e == null) {
                        if (convs != null && !convs.isEmpty()) {
                            val conversation = convs[0]
                            exist(conversation)
                        } else {
                            client!!.createConversation(ArrayList<String>(), name, null,
                                    object : AVIMConversationCreatedCallback() {
                                        override fun done(avimConversation: AVIMConversation, e: AVIMException?) {
                                            if (e == null) {
                                                success(avimConversation)
                                            } else {
                                                failure(e)
                                            }
                                        }
                                    })
                        }
                    } else {
                        failure(e)
                    }
                }
            })
        } else {
            clientError()
        }
    }

    //查找对话，回调函数中返回找到的对话
    fun findConversation(name: String,
                         found: (AVIMConversation) -> Unit,
                         notFound: () -> Unit,
                         failure: (Exception) -> Unit,
                         clientError: () -> Unit) {
        if (checkClient()) {
            val query = client!!.query
            query.whereEqualTo("name", name)
            query.findInBackground(object : AVIMConversationQueryCallback() {
                override fun done(convs: List<AVIMConversation>?, e: AVIMException?) {
                    if (e == null) {
                        if (convs != null && !convs.isEmpty()) {
                            found(convs[0])
                        } else {
                            notFound()
                        }
                    } else {
                        failure(e)
                    }
                }
            })
        } else {
            clientError()
        }
    }

    //加入对话，在创建或查找到对话后调用
    fun joinConversation(conversation: AVIMConversation,
                         success: (AVIMConversation) -> Unit,
                         failure: (Exception) -> Unit,
                         clientError: () -> Unit) {
        if (checkClient()) {
            if (!conversation.members.contains(username)) {
                conversation.join(object : AVIMConversationCallback() {
                    override fun done(e: AVIMException?) {
                        if (e == null) {
                            currentConversation = conversation
                            success(conversation)
                        } else {
                            failure(e)
                        }
                    }
                })
            } else {
                currentConversation = conversation
                success(conversation)
            }
        } else {
            clientError()
        }
    }

    //退出对话,在创建或查找到对话后调用
    fun quitConversation(conversation: AVIMConversation?,
                         success: (AVIMConversation) -> Unit,
                         failure: (Exception) -> Unit,
                         clientError: () -> Unit) {
        if (checkClient() && conversation != null) {
            conversation.quit(object : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    if (e == null) {
                        success(conversation)
                        currentConversation = null
                    } else {
                        failure(e)
                    }
                }
            })
        } else {
            clientError()
        }
    }

    //退出对话，退出最近加入的对话
    fun quitConversation(success: (AVIMConversation) -> Unit,
                         failure: (Exception) -> Unit,
                         clientError: () -> Unit) {
        if (checkClient() && currentConversation != null) {
            currentConversation!!.quit(object : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    if (e == null) {
                        success(currentConversation!!)
                        currentConversation = null
                    } else {
                        failure(e)
                    }
                }
            })
        } else {
            clientError()
        }
    }

    //发送文本，发送到最近加入的对话中
    fun sendText(text: String,
                 success: () -> Unit,
                 failure: (Exception) -> Unit,
                 conversationError: () -> Unit) {
        if (checkClient() && currentConversation != null) {
            val message = AVIMTextMessage()
            message.text = text
            currentConversation!!.sendMessage(message, object : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    if (e == null) {
                        success()
                    } else {
                        failure(e)
                    }
                }
            })
        } else {
            conversationError()
        }
    }

    //发送文本，发送到对应的对话中
    fun sendText(conversation: AVIMConversation?,
                 text: String,
                 success: () -> Unit,
                 failure: (Exception) -> Unit,
                 conversationError: () -> Unit) {
        if (checkClient() && conversation != null) {
            val message = AVIMTextMessage()
            message.text = text
            conversation.sendMessage(message, object : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    if (e == null) {
                        success()
                    } else {
                        failure(e)
                    }
                }
            })
        } else {
            conversationError()
        }
    }

    //获取最近加入的对话的文本聊天记录
    fun getTextChatLog(success: (List<AVIMTextMessage>) -> Unit,
                       failure: (Exception) -> Unit,
                       conversationError: () -> Unit) {
        if (checkClient() && currentConversation != null) {
            currentConversation!!.queryMessages(object : AVIMMessagesQueryCallback() {
                override fun done(messages: List<AVIMMessage>, e: AVIMException?) {
                    if (e == null) {
                        val ms = messages.indices.reversed()
                                .map { messages[it] }
                                .filterIsInstance<AVIMTextMessage>()
                        success(ms)
                    } else {
                        failure(e)
                    }
                }
            })
        } else {
            conversationError()
        }
    }
}
