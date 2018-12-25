package com.funnyseals.app.feature.doctorMessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.funnyseals.app.feature.patientMessage.PatientVideoCallActivity;
import com.hyphenate.chat.EMClient;
import com.vmloft.develop.library.tools.utils.VMLog;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CallReceiver extends BroadcastReceiver {
    private static String USER_TYPE;

    public CallReceiver () {
    }

    public static void setUserType(String type){
        USER_TYPE=type;
    }

    @Override
    public void onReceive (Context context, Intent intent) {
        // 判断环信是否登录成功
        if (!EMClient.getInstance().isLoggedInBefore()) {
            return;
        }

        // 呼叫方的usernmae
        String callFrom = intent.getStringExtra("from");
        // 呼叫类型，有语音和视频两种
        String callType = intent.getStringExtra("type");
        // 呼叫接收方，
        String callTo = intent.getStringExtra("to");
        // 获取通话时的扩展字段
        String callExt = EMClient.getInstance().callManager().getCurrentCallSession().getExt();
        VMLog.d("call extension data %s", callExt);
        Intent callIntent = new Intent();
        // 根据通话类型跳转到语音通话或视频通话界面
        if (callType.equals("video")) {
            // 设置当前通话类型为视频通话
            CallManager.getInstance().setCallType(CallManager.CallType.VIDEO);
            if(USER_TYPE.equals("d")){
                callIntent.setClass(context, DoctorVideoCallActivity.class);
            }else{
                callIntent.setClass(context, PatientVideoCallActivity.class);
            }

        } else if (callType.equals("voice")) {
            // 设置当前通话类型为语音通话
            CallManager.getInstance().setCallType(CallManager.CallType.VOICE);
            callIntent.setClass(context, VoiceCallActivity.class);
        }
        // 初始化通化管理类的一些属性
        CallManager.getInstance().setChatId(callFrom);
        CallManager.getInstance().setInComingCall(true);

        // 设置 activity 启动方式
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(callIntent);
    }
}
