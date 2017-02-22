package com.xmx.androidkotlinbase.common.im

import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage

/**
 * Created by The_onE on 2016/6/24.
 * 自定义文本消息管理器基类
 */
abstract class BaseTextMessageHandler : AVIMTypedMessageHandler<AVIMTextMessage>() {
    /**
     * 当接受到消息时的处理
     * @param[message] 接收到的消息
     * @param[conversation] 消息所在对话
     * @param[client] 消息所在客户端
     */
    override fun onMessage(message: AVIMTextMessage?,
                           conversation: AVIMConversation,
                           client: AVIMClient?) {
        super.onMessage(message, conversation, client)
        try {
            // 检查是否是当前用户接收到的消息
            val username = imClientManager.username
            if (client!!.clientId == username) {
                // 从消息中提取信息
                val text = message!!.text
                val from = message.from
                val time = message.timestamp
                // 将提取的信息交给接口处理
                onReceiveText(text, from, time, conversation, client)
            } else {
                // 若当前用户不匹配，则出现登录问题，关闭客户端
                client.close(null)
            }
        } catch (e: IllegalStateException) {
            client!!.close(null)
        }
    }

    /**
     * 接收到文本消息时的处理接口
     * @param[text] 接受到的文本
     * @param[from] 消息来源
     * @param[time] 消息时间
     * @param[conversation] 消息所在对话
     * @param[client] 消息所在客户端
     */
    abstract fun onReceiveText(text: String, from: String, time: Long,
                               conversation: AVIMConversation, client: AVIMClient)
}
