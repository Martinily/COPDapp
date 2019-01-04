package com.funnyseals.app.feature.health;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HealthCenterFragment extends Fragment {

    public static List<String>  data1    = new ArrayList<>();
    public static List<String>  data2    = new ArrayList<>();
    public static List<String>  data3    = new ArrayList<>();
    public static List<String>  datatime = new ArrayList<>();
    private       String        msg;
    private       int           indexoffev1;
    private       int           indexoffvc;
    private       int           indexofvc;
    private       int           index;
    private       View          publicview;

    public HealthCenterFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        msg = "null";
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume () {
        //获取控件
        TextView textView1 = publicview.findViewById(R.id.data1);
        TextView textView2 = publicview.findViewById(R.id.data2);
        TextView textView2c = publicview.findViewById(R.id.data_caculate);
        TextView textView3 = publicview.findViewById(R.id.data3);
        MyApplication myApplication = (MyApplication) Objects.requireNonNull(getActivity()).getApplication();
        //获取数据
        final String m_id = myApplication.getAccount();
        Thread thread = new Thread(() -> {
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", "11");
                jsonObject.put("data_type", "getall");
                jsonObject.put("pID", m_id);
                String send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(send);

                Thread.sleep(500);
                socket = SocketUtil.getGetSocket();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String get = dataInputStream.readUTF();
                data1.clear();
                data2.clear();
                data3.clear();
                datatime.clear();
                SimpleChartView.data.clear();
                SimpleChartView.data2.clear();
                if (!get.equals("empty")) {
                    JSONArray jsonArray = new JSONArray(get);
                    indexoffev1 = jsonArray.length();
                    indexoffvc = jsonArray.length();
                    indexofvc = jsonArray.length();
                    index = jsonArray.length();
                    JSONObject jsonObject1;
                    for (int i = jsonArray.length() - 1; i >= 0; i--) {
                        jsonObject1 = jsonArray.getJSONObject(i);
                        data1.add(jsonObject1.getString("FEV1"));
                        data2.add(jsonObject1.getString("FVC"));
                        data3.add(jsonObject1.getString("VC"));
                        datatime.add(jsonObject1.getString("UpdateTime"));
                        if (!jsonObject1.getString("FEV1").equals("-1"))
                            indexoffev1 = jsonArray.length() - 1 - i;
                        if (!jsonObject1.getString("FVC").equals("-1"))
                            indexoffvc = jsonArray.length() - 1 - i;
                        if (!jsonObject1.getString("VC").equals("-1")) {
                            indexofvc = jsonArray.length() - 1 - i;
                            SimpleChartView.data.add(Integer.parseInt(jsonObject1.getString("VC")));
                        } else
                            SimpleChartView.data.add(0);
                        if (!jsonObject1.getString("FEV1").equals("-1") & !jsonObject1.getString
                                ("FVC").equals("-1")) {
                            int a = Integer.parseInt(jsonObject1.getString("FEV1"));
                            int b = Integer.parseInt(jsonObject1.getString("FVC"));
                            SimpleChartView.data2.add(a * 100 / b);
                        } else
                            SimpleChartView.data2.add(0);
                    }
                } else{
                    if (msg.equals("null")) {
                        showToast("暂无用户数据");
                        msg = "暂无用户数据";
                    }
                }
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        while (thread.isAlive()) {
        }
        if (indexoffev1 != index)
            textView1.setText(data1.get(indexoffev1));
        if (indexoffvc != index)
            textView2.setText(data2.get(indexoffvc));
        if (indexofvc != index)
            textView3.setText(data3.get(indexofvc));
        if (indexoffev1 != index & indexoffvc != index) {
            int percent = Integer.parseInt(data1.get(indexoffev1)) * 100 / Integer.parseInt
                    (data2.get(indexoffvc));
            if (percent < 70){
                textView2c.setTextColor(Color.parseColor("#FF0000"));
                    if (msg.equals("null")){
                    showToast("您的数据存在异常，请确认您的身体状况");
                    msg = "您的数据存在异常，请确认您的身体状况";
                }
            }
                String string = String.valueOf(percent) + "%";
                textView2c.setText(string);
        }
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden & !msg.equals("null")) showToast(msg);
    }

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_center, container, false);
        publicview = view;
        Button button1 = view.findViewById(R.id.gotocentre2);
        Button button2 = view.findViewById(R.id.gotocentre3);
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HealthCenterUpdateActivity.class);
            startActivity(intent);
        });
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HealthCenterHistoryActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void showToast (final String msg) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show());
    }

}
