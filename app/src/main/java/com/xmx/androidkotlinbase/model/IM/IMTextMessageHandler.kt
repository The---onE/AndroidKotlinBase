package com.xmx.androidkotlinbase.model.IM

import android.content.Context
import android.widget.Toast

import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.xmx.androidkotlinbase.common.im.TextMessageHandler

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by The_onE on 2016/6/24.
 */
class IMTextMessageHandler(private var context: Context,
                           private var callback: OnReceiveCallback) : TextMessageHandler() {

    override fun onReceiveText(text: String, from: String, time: Long,
                               conversation: AVIMConversation, client: AVIMClient) {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timeString = df.format(Date(time))
        val info = from + ":" + text + "\n" + timeString
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show()
        callback.receive(text, from, time, conversation, client)
    }
}
