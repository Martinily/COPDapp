package com.funnyseals.app.model;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MessageInfo {

    private boolean mResult;
    private Object  mObject;
    private String  mReason;

    public MessageInfo() {

    }

    public boolean isResult() {
        return mResult;
    }

    public void setResult(boolean result) {
        this.mResult = result;
    }

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object object) {
        this.mObject = object;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        this.mReason = reason;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "result=" + mResult +
                ", object=" + mObject +
                ", reason='" + mReason + '\'' +
                '}';
    }
}
