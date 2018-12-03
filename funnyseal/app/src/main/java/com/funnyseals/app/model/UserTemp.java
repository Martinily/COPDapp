package com.funnyseals.app.model;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UserTemp {

    private int mPortrait;
    private String mName;
    private String mAccount;
    private User mUser;

    public UserTemp(int portrait, String name, String account, User user) {
        this.mPortrait = portrait;
        this.mName = name;
        this.mAccount = account;
        this.mUser = user;
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

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        this.mAccount = account;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
}
