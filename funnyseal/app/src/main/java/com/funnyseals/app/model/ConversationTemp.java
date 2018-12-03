package com.funnyseals.app.model;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ConversationTemp {

    private int mPortrait;
    private String mName;
    private String mAccount;
    private String mTime;
    private String mContent;
    private int mUnreadNum;

    public ConversationTemp(int portrait, String account, String name, String time, String content, int unreadNum) {
        this.mAccount = account;
        this.mPortrait = portrait;
        this.mName = name;
        this.mTime = time;
        this.mContent = content;
        this.mUnreadNum = unreadNum;
    }

    public int getPortrait() {
        return mPortrait;
    }

    public void setPortrait(int portrait) {
        this.mPortrait = portrait;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        this.mAccount = account;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public int getUnreadNum() {
        return mUnreadNum;
    }

    public void setUnreadNum(int unreadNum) {
        this.mUnreadNum = unreadNum;
    }
}