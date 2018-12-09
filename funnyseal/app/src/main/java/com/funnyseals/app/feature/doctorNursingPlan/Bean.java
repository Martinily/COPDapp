package com.funnyseals.app.feature.doctorNursingPlan;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/30
 *     desc   :列表内容实体
 *     version: 1.0
 * </pre>
 */
public class Bean {
    private int mId;

    private String mName;

    private String mContent;

    private String mAttention;

    private String mTime;

    public Bean(String name) {
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getContent() {
        return mContent;
    }

    public void setcontent(String content)  {this.mContent=content;}

    public String getAttention() {
        return mAttention;
    }

    public void setattention(String attention) { this.mAttention=attention;}

    public String gettime(){return mTime;}

    public void settime(String time) {this.mTime=time;}
}
