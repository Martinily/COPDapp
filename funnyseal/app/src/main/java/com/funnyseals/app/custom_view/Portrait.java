package com.funnyseals.app.custom_view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.doctorMessage.DoctorChatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Portrait extends CircleImageView {

    private String mAccount;

    private String mUrl;

    public Portrait(Context context) {
        super(context);
    }

    public Portrait(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Portrait(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public Portrait setUserAccount(String account) {
        this.mAccount = account;
        return this;
    }

    public Portrait setUrl(String url) {
        this.mUrl = url;
        return this;
    }

    public void show() {
        Glide.with(getContext()).load(/*Uri.parse(url)*/R.drawable.user).into(this);
    }
}
