package com.funnyseals.app.feature.patientNursingPlan;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.funnyseals.app.R;

public class DialogActivity extends Activity {
    private TextView      tv;
    private Button        bt;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Intent i = getIntent();
        //System.err.println(i.getStringExtra("doctory"));
        SharedPreferences sp = DialogActivity.this.getSharedPreferences("HAHA", Context
                .MODE_PRIVATE);
        final String content = sp.getString(i.getStringExtra("doctory"), null);
        System.err.println(content);

        NotificationManager barmanager = (NotificationManager) DialogActivity.this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notice;
        Notification.Builder builder = new Notification.Builder(DialogActivity.this).setTicker
                ("789")
                .setSmallIcon(R.drawable.alarm).setWhen(System.currentTimeMillis());
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Intent appIntent = null;
        appIntent = new Intent(DialogActivity.this, PatientNursingPlanFragment.class);
        appIntent.setAction(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
        PendingIntent contentIntent = PendingIntent.getActivity(DialogActivity.this, 0,
                appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notice = builder.setContentIntent(contentIntent).setContentTitle("您有护理计划待完成！")
                    .setContentText(content).build();
            notice.flags = Notification.FLAG_AUTO_CANCEL;
            int differid = (int) System.currentTimeMillis();
            barmanager.notify(differid, notice);
        }

        tv = (TextView) findViewById(R.id.dialog_tv);
        bt = (Button) findViewById(R.id.dialog_bt);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    protected void onStart () {
        super.onStart();
        tv.setText("123");
        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick (View arg0) {
                finish();
            }
        });
    }
}
