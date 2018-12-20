package com.funnyseals.app.feature.doctorMessage;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;

import com.vmloft.develop.library.tools.VMActivity;

import org.greenrobot.eventbus.EventBus;

public class CallActivity extends VMActivity {

    // 呼叫方名字
    protected String chatId;

    // 震动器
    private Vibrator vibrator;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置通话界面属性，保持屏幕常亮，关闭输入法，以及解锁
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    /**
     * 初始化界面方法，做一些界面的初始化操作
     */
    protected void initView () {
        activity = this;

        // 初始化振动器
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

        if (CallManager.getInstance().getCallState() == CallManager.CallState.DISCONNECTED) {
            // 收到呼叫或者呼叫对方时初始化通话状态监听
            CallManager.getInstance().setCallState(CallManager.CallState.CONNECTING);
            CallManager.getInstance().registerCallStateListener();
            CallManager.getInstance().attemptPlayCallSound();

            // 如果不是对方打来的，就主动呼叫
            if (!CallManager.getInstance().isInComingCall()) {
                CallManager.getInstance().makeCall();
            }
        }
    }


    /**
     * 挂断通话
     */
    protected void endCall () {
        CallManager.getInstance().endCall();
        onFinish();
    }

    /**
     * 拒绝通话
     */
    protected void rejectCall () {
        CallManager.getInstance().rejectCall();
        onFinish();
    }

    /**
     * 接听通话
     */
    protected void answerCall () {
        CallManager.getInstance().answerCall();
    }

    /**
     * 调用系统振动，触发按钮的震动反馈
     */
    protected void vibrate () {
        vibrator.vibrate(88);
    }

    /**
     * 销毁界面时做一些自己的操作
     */
    @Override
    public void onFinish () {
        super.onFinish();
    }

    @Override
    protected void onStart () {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop () {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume () {
        // 判断当前通话状态，如果已经挂断，则关闭通话界面
        if (CallManager.getInstance().getCallState() == CallManager.CallState.DISCONNECTED) {
            onFinish();
            return;
        } else {
            CallManager.getInstance().removeFloatWindow();
        }
        super.onResume();
    }
}