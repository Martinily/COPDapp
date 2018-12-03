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
    private User      mUser;
    //最后一条消息
    private EMMessage mLastMessage;
    //未读数量
    private int       mUnReadNum;

    public Conversation(User user, EMMessage message, int unReadNum) {
        this.mUser = user;
        this.mLastMessage = message;
        this.mUnReadNum = unReadNum;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public EMMessage getMessage() {
        return mLastMessage;
    }

    public void setMessage(EMMessage message) {
        this.mLastMessage = message;
    }

    public int getUnReadNum() {
        return mUnReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.mUnReadNum = unReadNum;
    }
}
