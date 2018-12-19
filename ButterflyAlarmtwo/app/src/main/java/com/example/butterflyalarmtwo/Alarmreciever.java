package com.example.butterflyalarmtwo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.provider.ContactsContract.Intents.Insert.ACTION;

public class Alarmreciever extends BroadcastReceiver {
    @Override
    public void onReceive (Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // 闹钟管理
        int  id = intent.getIntExtra("id",-1);
        Intent i = new Intent(context,Alarmreciever.class);
        i.putExtra("id", id);

        System.err.println("reciever"+(id+""));
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, i, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000*10, 0, sender);
            System.err.println("闹钟定制！"+(System.currentTimeMillis()+1000*10+""));
        }

        System.err.println("闹钟响了！");
        //int id = intent.getIntExtra("ID",-1);

      //  Intent ii=new Intent(context,DialogActivity.class);
        //ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.putExtra("timere", id);
       // context.startActivity(ii);
        NotificationManager barmanager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notice;
        Notification.Builder builder = new Notification.Builder(context).setTicker("789")
                .setSmallIcon(R.drawable.alarm).setWhen(System.currentTimeMillis());
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Intent appIntent=null;
        appIntent = new Intent(context,MainActivity.class);
        appIntent.setAction(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
        PendingIntent contentIntent =PendingIntent.getActivity(context, 0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notice = builder.setContentIntent(contentIntent).setContentTitle("您有护理计划待完成！").setContentText("222").build();
            notice.flags=Notification.FLAG_AUTO_CANCEL;
            barmanager.notify(10,notice);
        }
    }
}
