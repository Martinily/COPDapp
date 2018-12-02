package com.funnyseals.app.model;

import com.hyphenate.chat.EMMessage;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Conversation {
    //聊天对象信息
    private User      user;
    //最后一条消息
    private EMMessage lastMessage;
    //未读数量
    private int       unReadNum;

    public Conversation(User user, EMMessage message, int unReadNum) {
        this.user = user;
        this.lastMessage = message;
        this.unReadNum = unReadNum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EMMessage getMessage() {
        return lastMessage;
    }

    public void setMessage(EMMessage message) {
        this.lastMessage = message;
    }

    public int getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }
}
