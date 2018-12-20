package com.funnyseals.app.feature.health;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SimpleChartView extends View {
    public static List<Integer> data= new ArrayList<>();
    public static List<Integer> data2=new ArrayList<>();//数据

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    mHandler.sendMessage(mHandler.obtainMessage());
                    SimpleChartView.this.invalidate();//重绘
                }
            }, 1000);//一秒刷新一次
        };
    };

    public SimpleChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler.sendMessage(mHandler.obtainMessage());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画笔
        Paint paint=new Paint();
        paint.setColor(Color.parseColor("#7B68EE"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setTextSize(40);
        Paint paint1=new Paint();
        paint1.setColor(Color.parseColor("#000000"));
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(5);
        Paint paint2=new Paint();
        paint2.setColor(Color.parseColor("#00FF00"));
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(5);
        paint2.setTextSize(40);
        int Xstart=150;
        int Ystart=20;//起始坐标
        int Xlength=800;
        int Ylength=400;//XY轴长度
        //画Y轴
        canvas.drawLine(Xstart, Ystart, Xstart, Ystart+Ylength, paint);
        canvas.drawLine(Xstart+Xlength,Ystart,Xstart+Xlength,Ystart+Ylength,paint2);
        //画X轴
        canvas.drawLine(Xstart, Ystart+Ylength, Xstart+Xlength, Ystart+Ylength, paint1);
        //定义刻度
        String[] str1={"5000ml","4000ml","3000ml","2000ml","1000ml"};
        String[] str2={"100%","90%","80%","70%","60%","50%"};
        //画刻度
        for (int i = 0; i < 5; i++) {
            canvas.drawText(str1[i], Xstart-150, Ystart+i*100+15, paint);
            if (i == 0 | i == 4) continue;
            canvas.drawLine(Xstart, Ystart+i*100, Xstart-10, Ystart+i*100, paint);
        }
        for (int j = 0; j < 6; j++) {
            canvas.drawText(str2[j],Xstart+Xlength+15,Ystart+j*80+15,paint2);
            if (j == 0 | j == 5) continue;
            canvas.drawLine(Xstart+Xlength,Ystart+j*80,Xstart+Xlength+10,Ystart+j*80,paint2);
        }
        for (int i = 1; i <= 7; i++) {
            canvas.drawLine(Xstart+i*100, Ystart+Ylength, Xstart+i*100, Ystart+Ylength-10, paint1);
        }
        //画小箭头
        canvas.drawLine(Xstart, Ystart, Xstart-10, Ystart+20, paint);
        canvas.drawLine(Xstart, Ystart, Xstart+10, Ystart+20, paint);
        canvas.drawLine(Xstart+Xlength,Ystart,Xstart+Xlength-10,Ystart+20,paint2);
        canvas.drawLine(Xstart+Xlength,Ystart,Xstart+Xlength+10,Ystart+20,paint2);
        //写Y轴项目
        canvas.drawText("肺活量(VC)",Xstart-80,Ystart+Ylength+50,paint);
        canvas.drawText("FEV1/FVC",Xstart+Xlength-80,Ystart+Ylength+50,paint2);
        //画折线
        if (data.size()>1){
            if (data.size()>=7){
                for (int i = data.size()-7; i < data.size()-1; i++) {
                    if (data.get(i) == 0 | data.get(i+1) == 0) continue;
                    canvas.drawLine(Xstart+(i+1)*100, Ystart+Ylength-(data.get(i)-1000)/10, Xstart+(i+2)*100, Ystart+Ylength-(data.get(i+1)-1000)/10, paint);
                }
            }
            else {
                for (int i = 0; i < data.size()-1; i++) {
                    if (data.get(i) == 0 | data.get(i+1) == 0) continue;
                    canvas.drawLine(Xstart+(i+1)*100, Ystart+Ylength-(data.get(i)-1000)/10, Xstart+(i+2)*100, Ystart+Ylength-(data.get(i+1)-1000)/10, paint);
                }
            }
        }
        if (data2.size()>1){
            if (data2.size()>=7){
                for (int i = data2.size()-7; i < data2.size()-1; i++) {
                    if (data2.get(i) == 0 | data2.get(i+1) == 0) continue;
                    canvas.drawLine(Xstart+(i+1)*100, Ystart+Ylength-(data2.get(i)-50)*8, Xstart+(i+2)*100, Ystart+Ylength-(data2.get(i+1)-50)*8, paint2);
                }
            }
            else {
                for (int i = 0; i < data2.size()-1; i++) {
                    if (data2.get(i) == 0 | data2.get(i+1) == 0) continue;
                    canvas.drawLine(Xstart+(i+1)*100, Ystart+Ylength-(data2.get(i)-50)*8, Xstart+(i+2)*100, Ystart+Ylength-(data2.get(i+1)-50)*8, paint2);
                }
            }
        }
    }
}
