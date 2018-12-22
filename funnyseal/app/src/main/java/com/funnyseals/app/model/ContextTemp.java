package com.funnyseals.app.model;

import android.content.Context;

import java.io.Serializable;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ContextTemp implements Serializable {
    private Context mBottomActivity;

    public void setBottomActivity (Context BottomActivity) {
        this.mBottomActivity = BottomActivity;
    }

    public Context getBottomActivity(){
        return mBottomActivity;
    }
}
