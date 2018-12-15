package com.funnyseals.app.feature.doctorMessage;

import com.hyphenate.chat.EMCallStateChangeListener;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CallEvent {
    private boolean                             isState;
    private boolean                             isTime;
    private EMCallStateChangeListener.CallState callState;
    private EMCallStateChangeListener.CallError callError;

    public EMCallStateChangeListener.CallState getCallState() {
        return callState;
    }

    public void setCallState(EMCallStateChangeListener.CallState callState) {
        this.callState = callState;
    }

    public EMCallStateChangeListener.CallError getCallError() {
        return callError;
    }

    public void setCallError(EMCallStateChangeListener.CallError callError) {
        this.callError = callError;
    }

    public boolean isState() {
        return isState;
    }

    public void setState(boolean state) {
        isState = state;
    }

    public boolean isTime() {
        return isTime;
    }

    public void setTime(boolean time) {
        isTime = time;
    }
}
