package com.funnyseals.app.feature.doctorMessage;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.vmloft.develop.library.tools.utils.VMLog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoiceCallActivity extends CallActivity {

    // 使用 ButterKnife 注解的方式获取控件
    @BindView(R.id.layout_root)
    View                 rootView;
    @BindView(R.id.text_call_state)
    TextView             callStateView;
    @BindView(R.id.text_call_time)
    TextView             callTimeView;
    @BindView(R.id.img_call_avatar)
    ImageView            avatarView;
    @BindView(R.id.text_call_username)
    TextView             usernameView;
    @BindView(R.id.btn_exit_full_screen)
    ImageButton          exitFullScreenBtn;
    @BindView(R.id.btn_mic_switch)
    ImageButton          micSwitch;
    @BindView(R.id.btn_speaker_switch)
    ImageButton          speakerSwitch;
    @BindView(R.id.fab_reject_call)
    FloatingActionButton rejectCallFab;
    @BindView(R.id.fab_end_call)
    FloatingActionButton endCallFab;
    @BindView(R.id.fab_answer_call)
    FloatingActionButton answerCallFab;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 重载父类方法,实现一些当前通话的操作，
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected void initView () {
        super.initView();
        if (CallManager.getInstance().isInComingCall()) {
            endCallFab.setVisibility(View.GONE);
            answerCallFab.setVisibility(View.VISIBLE);
            rejectCallFab.setVisibility(View.VISIBLE);
            callStateView.setText("对方申请与你进行通话");
        } else {
            endCallFab.setVisibility(View.VISIBLE);
            answerCallFab.setVisibility(View.GONE);
            rejectCallFab.setVisibility(View.GONE);
            callStateView.setText("正在呼叫对方");
        }

        usernameView.setText(CallManager.getInstance().getChatId());
        micSwitch.setActivated(!CallManager.getInstance().isOpenMic());
        speakerSwitch.setActivated(CallManager.getInstance().isOpenSpeaker());

        // 判断当前通话时刚开始，还是从后台恢复已经存在的通话
        if (CallManager.getInstance().getCallState() == CallManager.CallState.ACCEPTED) {
            endCallFab.setVisibility(View.VISIBLE);
            answerCallFab.setVisibility(View.GONE);
            rejectCallFab.setVisibility(View.GONE);
            callStateView.setText("通话已接通");
            refreshCallTime();
        }
    }

    /**
     * 界面控件点击监听器
     */
    @OnClick({
            R.id.btn_exit_full_screen, R.id.btn_mic_switch, R.id.btn_speaker_switch,
            R.id.fab_reject_call, R.id.fab_end_call, R.id.fab_answer_call
    })
    void onClick (View v) {
        switch (v.getId()) {
            case R.id.btn_exit_full_screen:
                // 最小化通话界面
                exitFullScreen();
                break;
            case R.id.btn_mic_switch:
                // 麦克风开关
                onMicrophone();
                break;
            case R.id.btn_speaker_switch:
                // 扬声器开关
                onSpeaker();
                break;
            case R.id.fab_end_call:
                // 结束通话
                endCall();
                break;
            case R.id.fab_reject_call:
                // 拒绝接听通话
                rejectCall();
                break;
            case R.id.fab_answer_call:
                // 接听通话
                answerCall();
                break;
        }
    }

    /**
     * 接听通话
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected void answerCall () {
        super.answerCall();

        endCallFab.setVisibility(View.VISIBLE);
        rejectCallFab.setVisibility(View.GONE);
        answerCallFab.setVisibility(View.GONE);
    }

    /**
     * 退出全屏通话界面
     */
    private void exitFullScreen () {
        CallManager.getInstance().addFloatWindow();
        onFinish();
    }

    /**
     * 麦克风开关，主要调用环信语音数据传输方法
     */
    private void onMicrophone () {
        try {
            // 根据麦克风开关是否被激活来进行判断麦克风状态，然后进行下一步操作
            if (micSwitch.isActivated()) {
                // 设置按钮状态
                micSwitch.setActivated(false);
                // 暂停语音数据的传输
                EMClient.getInstance().callManager().resumeVoiceTransfer();
                CallManager.getInstance().setOpenMic(true);
            } else {
                // 设置按钮状态
                micSwitch.setActivated(true);
                // 恢复语音数据的传输
                EMClient.getInstance().callManager().pauseVoiceTransfer();
                CallManager.getInstance().setOpenMic(false);
            }
        } catch (HyphenateException e) {
            VMLog.e("exception code: %d, %s", e.getErrorCode(), e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 扬声器开关
     */
    private void onSpeaker () {
        // 根据按钮状态决定打开还是关闭扬声器
        if (speakerSwitch.isActivated()) {
            // 设置按钮状态
            speakerSwitch.setActivated(false);
            CallManager.getInstance().closeSpeaker();
            CallManager.getInstance().setOpenSpeaker(false);
        } else {
            // 设置按钮状态
            speakerSwitch.setActivated(true);
            CallManager.getInstance().openSpeaker();
            CallManager.getInstance().setOpenSpeaker(true);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus (CallEvent event) {
        if (event.isState()) {
            refreshCallView(event);
        }
        if (event.isTime()) {
            // 不论什么情况都检查下当前时间
            refreshCallTime();
        }
    }

    /**
     * 刷新通话界面
     */
    private void refreshCallView (CallEvent event) {
        EMCallStateChangeListener.CallError callError = event.getCallError();
        EMCallStateChangeListener.CallState callState = event.getCallState();
        switch (callState) {
            case CONNECTING: // 正在呼叫对方，TODO 没见回调过
                VMLog.i("正在呼叫对方" + callError);
                break;
            case CONNECTED: // 正在等待对方接受呼叫申请（对方申请与你进行通话）
                VMLog.i("正在连接" + callError);
                runOnUiThread(() -> {
                    if (CallManager.getInstance().isInComingCall()) {
                        callStateView.setText("对方申请与你进行通话");
                    } else {
                        callStateView.setText("正在等待对方接受呼叫申请");
                    }
                });
                break;
            case ACCEPTED: // 通话已接通
                VMLog.i("通话已接通");
                runOnUiThread(() -> callStateView.setText("通话已接通"));
                break;
            case DISCONNECTED: // 通话已中断
                VMLog.i("通话已结束" + callError);
                onFinish();
                break;
            case NETWORK_DISCONNECTED:
                showToast("对方网络断开");
                VMLog.i("对方网络断开");
                break;
            case NETWORK_NORMAL:
                VMLog.i("网络正常");
                break;
            case NETWORK_UNSTABLE:
                if (callError == EMCallStateChangeListener.CallError.ERROR_NO_DATA) {
                    VMLog.i("没有通话数据" + callError);
                } else {
                    VMLog.i("网络不稳定" + callError);
                }
                break;
            case VIDEO_PAUSE:
                showToast("对方已暂停视频传输");
                VMLog.i("对方已暂停视频传输");
                break;
            case VIDEO_RESUME:
                showToast("对方已恢复视频传输");
                VMLog.i("对方已恢复视频传输");
                break;
            case VOICE_PAUSE:
                showToast("对方已暂停语音传输");
                VMLog.i("对方已暂停语音传输");
                break;
            case VOICE_RESUME:
                showToast("对方已恢复语音传输");
                VMLog.i("对方已恢复语音传输");
                break;
            default:
                break;
        }
    }

    /**
     * 刷新通话时间显示
     */
    private void refreshCallTime () {
        int t = CallManager.getInstance().getCallTime();
        int h = t / 60 / 60;
        int m = t / 60 % 60;
        int s = t % 60 % 60;
        String time;
        if (h > 9) {
            time = "" + h;
        } else {
            time = "0" + h;
        }
        if (m > 9) {
            time += ":" + m;
        } else {
            time += ":0" + m;
        }
        if (s > 9) {
            time += ":" + s;
        } else {
            time += ":0" + s;
        }
        if (!callTimeView.isShown()) {
            callTimeView.setVisibility(View.VISIBLE);
        }
        callTimeView.setText(time);
    }

    /**
     * 屏幕方向改变回调方法
     */
    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onUserLeaveHint () {
        //super.onUserLeaveHint();
        exitFullScreen();
    }

    /**
     * 通话界面拦截 Back 按键，不能返回
     */
    @Override
    public void onBackPressed () {
        //super.onBackPressed();
        exitFullScreen();
    }

    public void showToast (final String msg) {
        runOnUiThread(() -> Toast.makeText(VoiceCallActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
}
