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

    private String account;

    private String url;

    public Portrait(Context context) {
        super(context);

        init();
    }

    public Portrait(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public Portrait(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        setOnClickListener(v -> {
            String userAccount = ((MyApplication) getContext().getApplicationContext()).getAccount();

            //if (!account.equals(userAccount)) {
            Intent intent = new Intent(getContext(), DoctorChatActivity.class);

            intent.putExtra("account", account);

            getContext().startActivity(intent);
            //}
        });
    }

    public Portrait setUserAccount(String account) {
        this.account = account;
        return this;
    }

    public Portrait setUrl(String url) {
        this.url = url;
        return this;
    }

    public void show() {
        Glide.with(getContext()).load(/*Uri.parse(url)*/R.mipmap.portrait0).into(this);
    }
}
