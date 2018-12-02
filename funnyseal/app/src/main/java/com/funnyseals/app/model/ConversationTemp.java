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

    private int portrait;

    private String name;

    private String account;

    private String time;

    private String content;

    private int unreadNum;

    public ConversationTemp(int portrait, String account, String name, String time, String content, int unreadNum) {
        this.account = account;
        this.portrait = portrait;
        this.name = name;
        this.time = time;
        this.content = content;
        this.unreadNum = unreadNum;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUnreadNum() {
        return unreadNum;
    }

    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }
}