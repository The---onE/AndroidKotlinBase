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
 * Created by The_onE on 15/8/13.
 * IM客户端管理器，单例对象
 */
object imClientManager {
    // IM客户端
    private var client: AVIMClient? = null
    // 获取用户名，IM客户端以用户名为唯一标识
    var username: String? = null
        private set
    // 客户端是否打开
    private var openFlag = false
    // 当前进行的对话
    private var currentConversation: AVIMConversation? = null

    /**
     * 检查客户端是否打开
     * @return 客户端是否打开
     */
    private fun checkClient(): Boolean {
        return openFlag && client != null && !username.isNullOrEmpty()
    }

    /**
     * 打开客户端，所有操作必须在打开客户端之后进行
     * @param[username] 用户名，IM客户端以用户名为唯一标识
     * @param[callback] 打开客户端的回调
     */
    fun openClient(username: String,
                   callback: AVIMClientCallback) {
        // 设置用户名
        this.username = username
        // 打开客户端
        client = AVIMClient.getInstance(username)
        client!!.open(callback)
        openFlag = true
    }

    /**
     * 关闭客户端
     * @param[callback] 关闭客户端回调
     */
    fun closeClient(callback: AVIMClientCallback) {
        // 如果已打开客户端
        if (checkClient()) {
            // 关闭客户端
            client!!.close(callback)
            openFlag = false
            client = null
            username = ""
        }
    }

    /**
     * 创建对话，若同名对话已存在则获取该对话
     * @param[name] 对话名，一个对话的唯一标识
     * @param[success] 创建成功，获取对话
     * @param[exist] 对话已存在，获取对话
     * @param[failure] 创建失败
     * @param[clientError] 客户端未打开，创建失败
     */
    fun createConversation(name: String,
                           success: (AVIMConversation) -> Unit,
                           exist: (AVIMConversation) -> Unit,
                           failure: (Exception) -> Unit,
                           clientError: () -> Unit) {
        // 检查客户端是否打开
        if (checkClient()) {
            // 客户端已打开
            val query = client!!.query
            query.whereEqualTo("name", name)
            // 根据对话名查找对话
            query.findInBackground(object : AVIMConversationQueryCallback() {
                override fun done(conversations: List<AVIMConversation>?, e: AVIMException?) {
                    if (e == null) {
                        if (conversations != null && !conversations.isEmpty()) {
                            // 若对话已存在则不创建，获取该对话
                            val conversation = conversations[0]
                            exist(conversation)
                        } else {
                            // 对话不存在，创建对话
                            client!!.createConversation(ArrayList<String>(), name, null,
                                    object : AVIMConversationCreatedCallback() {
                                        override fun done(imConversation: AVIMConversation, e: AVIMException?) {
                                            if (e == null) {
                                                // 创建成功，获取对话
                                                success(imConversation)
                                            } else {
                                                // 创建失败
                                                failure(e)
                                            }
                                        }
                                    })
                        }
                    } else {
                        // 查找对话失败
                        failure(e)
                    }
                }
            })
        } else {
            // 客户端尚未打开
            clientError()
        }
    }

    /**
     * 查找对话
     * @param[name] 对话名，一个对话的唯一标识
     * @param[found] 查找成功，获取对话
     * @param[notFound] 对话不存在
     * @param[failure] 查找失败，网络错误
     * @param[clientError] 客户端未打开，查找失败
     */
    fun findConversation(name: String,
                         found: (AVIMConversation) -> Unit,
                         notFound: () -> Unit,
                         failure: (Exception) -> Unit,
                         clientError: () -> Unit) {
        // 检查客户端是否打开
        if (checkClient()) {
            // 客户端已打开
            val query = client!!.query
            query.whereEqualTo("name", name)
            // 根据对话名查找对话
            query.findInBackground(object : AVIMConversationQueryCallback() {
                override fun done(conversations: List<AVIMConversation>?, e: AVIMException?) {
                    if (e == null) {
                        if (conversations != null && !conversations.isEmpty()) {
                            // 对话存在，获取对话
                            found(conversations[0])
                        } else {
                            // 对话不存在
                            notFound()
                        }
                    } else {
                        // 查找失败
                        failure(e)
                    }
                }
            })
        } else {
            // 客户端尚未打开
            clientError()
        }
    }

    /**
     * 加入对话，在创建或查找到对话后调用
     * 加入成功后该对话成为当前对话
     * @param[conversation] 查找或创建的对话
     * @param[success] 加入成功
     * @param[failure] 加入失败
     * @param[clientError] 客户端未打开，加入失败
     */
    fun joinConversation(conversation: AVIMConversation,
                         success: (AVIMConversation) -> Unit,
                         failure: (Exception) -> Unit,
                         clientError: () -> Unit) {
        // 检查客户端是否打开
        if (checkClient()) {
            // 客户端已打开
            // 检查对话中是否包含当前用户
            if (!conversation.members.contains(username)) {
                // 若不包含则加入该对话
                conversation.join(object : AVIMConversationCallback() {
                    override fun done(e: AVIMException?) {
                        if (e == null) {
                            // 加入成功，设置为当前对话
                            currentConversation = conversation
                            success(conversation)
                        } else {
                            // 加入失败
                            failure(e)
                        }
                    }
                })
            } else {
                // 已包含当前用户，设置为当前对话
                currentConversation = conversation
                success(conversation)
            }
        } else {
            // 客户端尚未打开
            clientError()
        }
    }

    /**
     * 退出对话,在创建或查找到对话后调用
     * @param[conversation] 查找或创建的对话
     * @param[success] 退出成功
     * @param[failure] 退出失败
     * @param[clientError] 客户端未打开，退出失败
     */
    fun quitConversation(conversation: AVIMConversation,
                         success: (AVIMConversation) -> Unit,
                         failure: (Exception) -> Unit,
                         clientError: () -> Unit) {
        // 检查客户端是否打开
        if (checkClient()) {
            // 客户端已打开
            // 退出对话
            conversation.quit(object : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    if (e == null) {
                        // 退出成功
                        success(conversation)
                        currentConversation = null
                    } else {
                        // 退出失败
                        failure(e)
                    }
                }
            })
        } else {
            // 客户端尚未打开
        }
    }

    /**
     * 退出最近加入的对话
     * @param[success] 退出成功
     * @param[failure] 退出失败
     * @param[clientError] 客户端未打开，退出失败
     */
    fun quitConversation(success: (AVIMConversation) -> Unit,
                         failure: (Exception) -> Unit,
                         clientError: () -> Unit) {
        // 检查是否已加入对话
        if (checkClient() && currentConversation != null) {
            // 退出当前对话
            quitConversation(currentConversation!!, success, failure, clientError)
        } else {
            // 尚未加入对话
            clientError()
        }
    }

    /**
     * 发送文本消息，发送到对应的对话中
     * @param[conversation] 要发送到的对话
     * @param[text] 要发送的文本
     * @param[success] 发送成功
     * @param[failure] 发送失败
     * @param[conversationError] 未加入对话，发送失败
     */
    fun sendText(conversation: AVIMConversation,
                 text: String,
                 success: () -> Unit,
                 failure: (Exception) -> Unit,
                 conversationError: () -> Unit) {
        // 检查是否已加入对话
        if (checkClient()) {
            // 已加入对话
            // 生成文本消息实体
            val message = AVIMTextMessage()
            message.text = text
            // 发送消息
            conversation.sendMessage(message, object : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    if (e == null) {
                        // 发送成功
                        success()
                    } else {
                        // 发送失败
                        failure(e)
                    }
                }
            })
        } else {
            // 尚未加入对话
            conversationError()
        }
    }

    /**
     * 发送文本消息，发送到最近加入的对话中
     * @param[text] 要发送的文本
     * @param[success] 发送成功
     * @param[failure] 发送失败
     * @param[conversationError] 未加入对话，发送失败
     */
    fun sendText(text: String,
                 success: () -> Unit,
                 failure: (Exception) -> Unit,
                 conversationError: () -> Unit) {
        // 检查是否已加入对话
        if (checkClient() && currentConversation != null) {
            // 已加入对话
            // 发送文本到最近加入的对话
            sendText(currentConversation!!, text, success, failure, conversationError)
        } else {
            // 尚未加入对话
            conversationError()
        }
    }

    /**
     * 获取最近加入的对话的文本聊天记录
     * @param[success] 获取成功，获取文本消息列表
     * @param[failure] 获取失败
     * @param[conversationError] 未加入对话，获取失败
     */
    fun getTextChatLog(success: (List<AVIMTextMessage>) -> Unit,
                       failure: (Exception) -> Unit,
                       conversationError: () -> Unit) {
        // 检查是否已加入对话
        if (checkClient() && currentConversation != null) {
            // 已加入对话
            // 查询最近加入对话消息记录
            currentConversation!!.queryMessages(object : AVIMMessagesQueryCallback() {
                override fun done(messages: List<AVIMMessage>, e: AVIMException?) {
                    if (e == null) {
                        // 获取成功
                        // 将消息记录中的文本消息提取并生成列表
                        val ms = messages.indices.reversed()
                                .map { messages[it] }
                                .filterIsInstance<AVIMTextMessage>()
                        success(ms)
                    } else {
                        // 获取失败
                        failure(e)
                    }
                }
            })
        } else {
            // 尚未加入对话
            conversationError()
        }
    }
}
