package com.funnyseals.app.util;

import android.widget.Button;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TimeDownUtil extends android.os.CountDownTimer {
    private static Button BTN_CODE_SEND;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and
     *                          {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeDownUtil (long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        BTN_CODE_SEND = button;
    }

    @Override
    public void onTick (long millisUntilFinished) {
        BTN_CODE_SEND.setClickable(false);
        BTN_CODE_SEND.setText(millisUntilFinished / 1000 + "s重新发送");
    }

    @Override
    public void onFinish () {
        BTN_CODE_SEND.setClickable(true);
        BTN_CODE_SEND.setText("发送验证码");
    }
}
