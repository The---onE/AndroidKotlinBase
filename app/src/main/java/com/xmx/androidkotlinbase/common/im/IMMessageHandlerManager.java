package com.xmx.androidkotlinbase.common.im;

import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_onE on 2016/6/27.
 * 消息处理器管理器，单例类
 * 无法转化为Kotlin对象，调用AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, handler);
 * 会导致IllegalAccessError，暂时使用Java类管理
 */
public class IMMessageHandlerManager {
    // 单例实例
    private static IMMessageHandlerManager imMessageHandlerManager;

    // 获取单例实例
    public synchronized static IMMessageHandlerManager getInstance() {
        if (null == imMessageHandlerManager) {
            imMessageHandlerManager = new IMMessageHandlerManager();
        }
        return imMessageHandlerManager;
    }

    // 所有文本消息处理器
    private List<BaseTextMessageHandler> textMessageHandlers = new ArrayList<>();

    /**
     * 添加文本消息处理器
     *
     * @param handler 要添加的文本消息处理器
     */
    public void addTextMessageHandler(BaseTextMessageHandler handler) {
        if (!textMessageHandlers.contains(handler)) {
            AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, handler);
            textMessageHandlers.add(handler);
        }
    }

    /**
     * 移除文本消息处理器
     *
     * @param handler 要移除的文本消息管理器
     */
    public void removeTextMessageHandler(BaseTextMessageHandler handler) {
        if (textMessageHandlers.contains(handler)) {
            AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, handler);
            textMessageHandlers.remove(handler);
        }
    }

    /**
     * 移除全部文本消息处理器
     */
    public void removeAllTextMessageHandlers() {
        for (BaseTextMessageHandler handler : textMessageHandlers) {
            AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, handler);
        }
        textMessageHandlers.clear();
    }
}
