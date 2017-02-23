package com.xmx.androidkotlinbase.module.im

import android.content.Context
import android.widget.Toast

import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.xmx.androidkotlinbase.common.im.BaseTextMessageHandler
import com.xmx.androidkotlinbase.utils.StringUtil

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by The_onE on 2016/6/24.
 * 测试文本消息处理器
 * @property[context] 当前上下文
 * @property[onReceive] 接受到消息的处理，参数为【文本内容,消息来源,消息时间,所在对话,所在客户端】
 */
class IMTextMessageHandler(private var context: Context,
                           private var onReceive: (text: String,
                                                   from: String,
                                                   time: Long,
                                                   AVIMConversation, AVIMClient) -> Unit)
    : BaseTextMessageHandler() {

    override fun onReceiveText(text: String, from: String, time: Long,
                               conversation: AVIMConversation, client: AVIMClient) {
        // 显示消息提示
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timeString = df.format(Date(time))
        val info = "$from : $text\n$timeString"
        StringUtil.showToast(context, info)
        // 自定义处理
        onReceive(text, from, time, conversation, client)
    }
}
