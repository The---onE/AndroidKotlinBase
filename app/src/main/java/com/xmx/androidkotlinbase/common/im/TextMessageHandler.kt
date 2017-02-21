package com.xmx.androidkotlinbase.common.im

import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage

/**
 * Created by The_onE on 2016/6/24.
 */
abstract class TextMessageHandler : AVIMTypedMessageHandler<AVIMTextMessage>() {
    override fun onMessage(message: AVIMTextMessage?, conversation: AVIMConversation, client: AVIMClient?) {
        super.onMessage(message, conversation, client)
        try {
            val username = imClientManager.username
            if (client!!.clientId == username) {
                val text = message!!.text
                val from = message.from
                val time = message.timestamp
                onReceiveText(text, from, time, conversation, client)
            } else {
                client.close(null)
            }
        } catch (e: IllegalStateException) {
            client!!.close(null)
        }
    }

    abstract fun onReceiveText(text: String, from: String, time: Long,
                               conversation: AVIMConversation, client: AVIMClient)
}
