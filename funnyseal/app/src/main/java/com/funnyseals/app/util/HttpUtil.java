package com.funnyseals.app.util;

import android.content.Context;

import com.funnyseals.app.model.MessageInfo;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class HttpUtil {

    public static final String BASE_URL = "http://119.29.137.14:8080/group";

    private HttpUtil() {
        /*do nothing*/
    }

    public static MessageInfo post(Context context, String url, JsonObject params) {
        MessageInfo messageInfo = new MessageInfo();

        Ion.with(context)
                .load(BASE_URL + url)
                .setJsonObjectBody(params)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        System.out.println(result);
                        if (result != null) {
                            System.out.println("HttpUtil.onCompleted");
                            messageInfo.setResult(result.get("result").getAsBoolean());
                            messageInfo.setReason(result.get("reason").getAsString());
                            messageInfo.setObject(result.get("object"));
                        }
                    }
                });

        return messageInfo;
    }
}