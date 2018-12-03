package com.funnyseals.app.feature.patientNursingPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;

import java.util.ArrayList;
import java.util.List;

public class MedicineRetimeActivity extends AppCompatActivity {

    private Button       mQuit_button;
    private Button       mDone_button;
    private ListView     mListView;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retime);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        initList(bundle);
        this.mListView = findViewById(R.id.listViewtime);

        final MyAdapter adapter = new MyAdapter(MedicineRetimeActivity.this, mList);
        mListView.setAdapter(adapter);
        //ListView item的点击事件
        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView txt = view.findViewById(R.id.item_tv);
            Toast.makeText(MedicineRetimeActivity.this, txt.getText().toString(), Toast.LENGTH_SHORT).show();
        });
        mQuit_button = findViewById(R.id.quitit);
        mDone_button = findViewById(R.id.done);
        mQuit_button.setOnClickListener(v -> {
            finish();  //关闭此activity
        });
        mDone_button.setOnClickListener(v -> {
            finish();  //关闭此activity
        });
    }

    private void initList(Bundle bundle) {  //添加所有时间

        int i = Integer.parseInt(bundle.getString("medicinenametime"));
        if (i / 100000 == 1)
            mList.add("6:00");
        if (i % 100000 / 10000 == 1)
            mList.add("7:30");
        if (i % 100000 % 10000 / 1000 == 1)
            mList.add("11:00");
        if (i % 100000 % 10000 % 1000 / 100 == 1)
            mList.add("12:30");
        if (i % 100000 % 10000 % 1000 % 100 / 10 == 1)
            mList.add("17:00");
        if (i % 10 == 1)
            mList.add("18:30");
        //for (int i = 0; i < 20; i++) {
        //    mList.add(i + "");
        //}
    }
}
