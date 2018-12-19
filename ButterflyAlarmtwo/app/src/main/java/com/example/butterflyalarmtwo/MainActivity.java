package com.example.butterflyalarmtwo;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.app.PendingIntent.getBroadcast;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_ALARM_LIST = "alarmList2";
    private Button btnAddAlarm;
    private ListView lvAlarmList;
    private ArrayAdapter<AlarmData> adapter;
    private AlarmManager alarmManager;//系统的闹钟服务
    private static final String names = "kun";  //药品/器械/运动名称

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();   //隐藏自带actionBar
        if (actionBar != null) {
            actionBar.hide();
        }

        alarmManager = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);

        //实例化按钮和列表
        btnAddAlarm = findViewById(R.id.addalarm);
        lvAlarmList = findViewById(R.id.list_view);

        //实例化适配器
        adapter = new ArrayAdapter<MainActivity.AlarmData>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item);

        //为列表添加适配器
        lvAlarmList.setAdapter(adapter);

        //保存闹钟列表
        readSavedAlarmList();

        //添加闹钟 的监听事件
        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                addAlarm();
            }
        });

        //闹钟列表的监听事件
        lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick (AdapterView<?> adapterView, View view, final int position, long id) {
                //单击调出对话框
                new AlertDialog.Builder(MainActivity.this).setTitle("操作选项").setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0:
                                deleteAlarm(position);
                                break;
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("取消", null).show();
                return true;
            }
        });
    }


    private void addAlarm () {
        //获取当前时间
        Calendar c = Calendar.getInstance();
        //时间选择框
        new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet (TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                //写出当前时间
                Calendar currentTime = Calendar.getInstance();

                //设置时间小于当前时间，往后推一天
                if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                    calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                }
                AlarmData ad = new AlarmData(calendar.getTimeInMillis());
                adapter.add(ad);
                //System.err.println(adapter.getCount());
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                {
                    System.err.println("aaaaa");
                    Intent intent=new Intent(MainActivity.this,
                            Alarmreciever.class);
                    intent.putExtra("time", ad.getTime());
                    intent.putExtra("id", ad.getId());
                    System.err.println(ad.getTime()+"");
                    alarmManager.setWindow(AlarmManager.RTC_WAKEUP, ad.getTime(), 0, PendingIntent.getBroadcast(MainActivity.this,
                            ad.getId(),  // 请求码
                           intent,
                            0));
                }
                else{
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            ad.getTime(),//时间
                            5 * 60 * 1000,
                            //执行广播pendingIntent
                            PendingIntent.getBroadcast(MainActivity.this,
                                    ad.getId(),  // 请求码
                                    new Intent(MainActivity.this,
                                            Alarmreciever.class),
                                    0));
                }

                //对应每个闹钟生成一个特定的提醒标语
                //每一个闹钟对应一个请求码，根据请求码移除特定的闹钟,那么把时间当做请求码
                saveAlarmList();
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    //保存闹钟数据
    private void saveAlarmList () {
        //System.out.println("save");
        SharedPreferences.Editor editor = MainActivity.this.getSharedPreferences(
                "HAHAtoo",
                Context.MODE_PRIVATE).edit();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
            sb.append(adapter.getItem(i).getTime()).append(",");
        }
        if (sb.length() > 1) {
            String content = sb.toString().substring(0, sb.length() - 1);//去掉最后一个逗号
            editor.putString(KEY_ALARM_LIST, content);
            System.out.println(content);  // 调试使用
        } else {
            editor.putString(KEY_ALARM_LIST, null);
        }
        editor.commit();
    }

    //读取已存数据
    private void readSavedAlarmList () {
        SharedPreferences sp = MainActivity.this.getSharedPreferences("HAHAtoo", Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST, null);
        if (content != null) {
            String[] timeStrings = content.split(",");
            for (String string : timeStrings) {
                adapter.add(new AlarmData(Long.parseLong(string)));
            }
        }
    }

    //删除闹钟
    private void deleteAlarm (int position) {
        AlarmData ad = adapter.getItem(position);
        adapter.remove(ad);
        saveAlarmList();//重新保存
        alarmManager.cancel(getBroadcast(MainActivity.this, ad.getId(), new Intent(MainActivity.this, Alarmreciever.class), 0));
    }

    //AlarmData类，其中设置方法获取闹钟的请求码
    private static class AlarmData {

        private String timeLable = "";
        private long time = 0;
        private Calendar date;

        //闹钟所要响起的时间
        public AlarmData (long time) {
            this.time = time;

            date = Calendar.getInstance();
            date.setTimeInMillis(time);

            timeLable = String.format("%d月%d日 %d:%d",
                    date.get(Calendar.MONTH) + 1,//getMonth的返回值是从0开始的
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));
        }

        public long getTime () {
            return time;
        }

        public String getTimeLable () {
            return timeLable;
        }

        public int getId () {
            int time=(int) (getTime() / 1000 / 60);
            System.err.println(time+"");
            return time;//精确到分钟，getTime为毫秒
        }
        //获取时间的标签
        @Override
        public String toString () {
            return getTimeLable();
        }
    }
}