package com.funnyseals.app.feature.patientNursingPlan;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;

//接受闹钟的id,作为闹钟设置循环、文件读取的标识。
public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        int id = intent.getIntExtra("ID", -1);

        System.err.println("reciever" + (id + ""));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent i = new Intent(context, AlarmReciever.class);
            i.putExtra("ID", id);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent sender = PendingIntent.getBroadcast(context, id, i, PendingIntent
                    .FLAG_CANCEL_CURRENT);
            am.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 24 * 60 * 60 *
                    1000, 0, sender);
            System.err.println("闹钟定制！" + (System.currentTimeMillis() + 24 * 60 * 60 * 1000 + ""));
        }


        SharedPreferences sp = context.getSharedPreferences("HAHA", Context.MODE_PRIVATE);
        final String content = sp.getString(id + "", null);

        NotificationManager barmanager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        Notification notice;
        Notification.Builder builder = new Notification.Builder(context).setTicker("789")
                .setSmallIcon(R.drawable.vector_drawable_alarm).setWhen(System.currentTimeMillis());
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Intent appIntent = null;
        appIntent = new Intent(context, PatientBottomActivity.class);
        appIntent.setAction(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notice = builder.setContentIntent(contentIntent).setContentTitle("您有护理计划待完成！")
                    .setContentText(content).build();
            notice.flags = Notification.FLAG_AUTO_CANCEL;
            int differid = (int) (Math.random() * 100);
            barmanager.notify(differid, notice);
        }
    }
}