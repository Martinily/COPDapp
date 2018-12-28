package com.funnyseals.app.feature.patientNursingPlan;

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

import com.funnyseals.app.R;

import java.util.Calendar;

import static android.app.PendingIntent.getBroadcast;

public class InstrumentRetimeActivity extends AppCompatActivity {
    private static final String                  KEY_ALARM_LIST = "instrument";
    private              Button                  mBbtnAddAlarm;
    private              Button                  mQuitInstrumentTime;
    private              ListView                mLvAlarmList;
    private              ArrayAdapter<AlarmData> mAdapter;
    private              AlarmManager            mAlarmManager;//系统的闹钟服务
    private              String                  mNames         = "kun";  //药品/器械/运动名称

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retime);
        ActionBar actionBar = getSupportActionBar();   //隐藏自带actionBar
        if (actionBar != null) {
            actionBar.hide();
        }

        //获取当前设置闹钟的器械名称
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String aa = bundle.getString("instrumenttitle");
        mNames = aa;
        mAlarmManager = (AlarmManager) InstrumentRetimeActivity.this.getSystemService(Context
                .ALARM_SERVICE);

        //实例化按钮和列表
        mBbtnAddAlarm = findViewById(R.id.addalarm);
        mLvAlarmList = findViewById(R.id.alarmlist_view);
        mQuitInstrumentTime = findViewById(R.id.quitalarm);
        mQuitInstrumentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                finish();
            }
        });

        //实例化适配器
        mAdapter = new ArrayAdapter<InstrumentRetimeActivity.AlarmData>(InstrumentRetimeActivity
                .this, R.layout.support_simple_spinner_dropdown_item);

        //为列表添加适配器
        mLvAlarmList.setAdapter(mAdapter);

        //保存闹钟列表
        ReadSavedAlarmList();

        //添加闹钟 的监听事件
        mBbtnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick (View view) {
                AddAlarm();
            }
        });

        //闹钟列表的监听事件
        mLvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick (AdapterView<?> adapterView, View view, final int
                    position, long id) {
                //单击调出对话框
                new AlertDialog.Builder(InstrumentRetimeActivity.this).setTitle("操作选项").setItems
                        (new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0:
                                DeleteAlarm(position);
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

    //删除闹钟
    private void DeleteAlarm (int position) {
        AlarmData ad = mAdapter.getItem(position);
        mAdapter.remove(ad);
        SaveAlarmList();//重新保存
        mAlarmManager.cancel(getBroadcast(InstrumentRetimeActivity.this, ad.getId(), new Intent
                (InstrumentRetimeActivity.this, AlarmReciever.class), 0));
    }

    //添加闹钟
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void AddAlarm () {
        //获取当前时间
        Calendar c = Calendar.getInstance();
        //时间选择框
        new TimePickerDialog(InstrumentRetimeActivity.this, new TimePickerDialog
                .OnTimeSetListener() {
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

                AlarmData ad = new AlarmData(calendar.getTimeInMillis(), mNames);
                mAdapter.add(ad);
/*
第一个参数  RTC操作系统时间为启动时间点，WAKEUP系统休眠时同样执行
第二个参数  什么时候启动
第三个参数  启动之后什么时间再次启动
第四个参数  Pendingintent挂起的Intent,不立即执行
 */
                Intent intent = new Intent(InstrumentRetimeActivity.this,
                        AlarmReciever.class);
                intent.putExtra("ID", ad.getId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mAlarmManager.setWindow(AlarmManager.RTC_WAKEUP, ad.getTime(), 0,
                            PendingIntent.getBroadcast(InstrumentRetimeActivity.this,
                            ad.getId(),  // 请求码
                            intent,
                            0));

                } else {
                    PendingIntent pendingIntent = getBroadcast(InstrumentRetimeActivity.this,
                            ad.getId(),
                            intent, 0);
                    mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            ad.getTime(),//当前时间当做请求码
                            24 * 60 * 60 * 1000,
                            //执行广播
                            pendingIntent);
                }

                //对应每个闹钟生成一个特定的提醒标语
                SharedPreferences.Editor editor = InstrumentRetimeActivity.this
                        .getSharedPreferences("HAHA", Context.MODE_PRIVATE).edit();
                editor.putString(ad.getId() + "", ad.getTimeLable());
                editor.commit();
                // SharedPreferences sp=InstrumentRetimeActivity.this.getSharedPreferences
                // ("HAHA", Context.MODE_PRIVATE);
                // String content=sp.getString(ad.getId()+"",null);

                //每一个闹钟对应一个请求码，根据请求码移除特定的闹钟,那么把时间当做请求码
                SaveAlarmList();
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    //保存闹钟数据
    private void SaveAlarmList () {
        SharedPreferences.Editor editor = InstrumentRetimeActivity.this.getSharedPreferences
                ("HAHA", Context.MODE_PRIVATE).edit();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            //格式：“名称：时间”
            sb.append(mAdapter.getItem(i).getTimeName()).append(":").append(mAdapter.getItem(i)
                    .getTime()).append(",");
        }
        if (sb.length() > 1) {
            String content = sb.toString().substring(0, sb.length() - 1);//去掉最后一个逗号
            editor.putString(KEY_ALARM_LIST, content);
        } else {
            editor.putString(KEY_ALARM_LIST, null);
        }
        editor.commit();
    }

    //读取已存数据
    private void ReadSavedAlarmList () {
        SharedPreferences sp = InstrumentRetimeActivity.this.getSharedPreferences("HAHA", Context
                .MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST, null);
        if (content != null) {
            String[] timeStrings = content.split(",");
            for (String string : timeStrings) {
                String[] timeString = string.split(":");
                //分割成  时间，名称
                mAdapter.add(new AlarmData(Long.parseLong(timeString[1]), timeString[0]));
            }
        }
    }

    //AlarmData类，其中设置方法获取闹钟的请求码
    private static class AlarmData {
        //获取时间的标签
        private String   timeLable = "";
        private long     time      = 0;
        private String   name      = "";
        private Calendar date;

        //闹钟所要响起的时间
        public AlarmData (long time, String name) {
            this.time = time;
            this.name = name;

            date = Calendar.getInstance();
            date.setTimeInMillis(time);

            //时间格式的标准化：xx-xx。
            String mins, hours;
            if (date.get(Calendar.HOUR_OF_DAY) < 10)
                hours = "0" + String.valueOf(date.get(Calendar.HOUR_OF_DAY));
            else
                hours = String.valueOf(date.get(Calendar.HOUR_OF_DAY));
            if (date.get(Calendar.MINUTE) < 10)
                mins = "0" + String.valueOf(date.get(Calendar.MINUTE));
            else
                mins = String.valueOf(date.get(Calendar.MINUTE));
            /*timeLable=String.format("%d月%d日 %d:%d",
                    date.get(Calendar.MONTH)+1,//getMonth的返回值是从0开始的
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));
             */
            timeLable = name + ":" + hours + ":" + mins;
        }

        public long getTime () {
            return time;
        }

        public String getTimeName () {return name;}

        public String getTimeLable () {
            return timeLable;
        }

        @Override
        public String toString () {
            return getTimeLable();
        }

        public int getId () {
            return (int) ((getTime() / 1000 / 60) + 2000);//精确到分钟，getTime为毫秒
        }
    }
}
