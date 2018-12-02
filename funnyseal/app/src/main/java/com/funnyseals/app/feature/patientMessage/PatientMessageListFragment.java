package com.funnyseals.app.feature.patientMessage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.doctorMessage.DoctorChatActivity;
import com.funnyseals.app.feature.doctorMessage.MessageItemAdapter;
import com.funnyseals.app.feature.doctorMessage.MessageItemAdapterTemp;
import com.funnyseals.app.model.ConversationTemp;
import com.funnyseals.app.model.User;
import com.funnyseals.app.model.UserDao;
import com.funnyseals.app.util.TimeUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class PatientMessageListFragment extends Fragment {
    private View view;
    private        SwipeMenuListView chatList;
    private static Connection        conn;
    private MessageItemAdapter adapter;
    private String myAccount;
    private List<EMConversation> conversationList;
    private List<ConversationTemp> conversationTemps;
    private Map<String, User>      users;
    private Thread                 mThread;
    //用于执行数据库线程
    private Handler                mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                users = (Map<String, User>) msg.obj;
            }
            loadConversations();
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_message_list, container, false);

        MyApplication application = (MyApplication) getActivity().getApplication();
        myAccount = application.getAccount();
        if (mThread == null) {
            mThread = new Thread(runnable);
            mThread.start();
        }
        init();
        return view;
    }
    Runnable runnable = () -> {
        try {
            conn = UserDao.getConnection();
            if (conn != null) {
                PreparedStatement statement = conn.prepareStatement("select YS_ZH from hz where HZ_ZH=?");
                statement.setString(1, myAccount);
                ResultSet rs = statement.executeQuery();
                ResultSet rs2;
                statement = conn.prepareStatement("select * from ys where YS_ZH=?");
                users=new HashMap<>();
                while (rs.next()) {
                    statement.setString(1, rs.getString("YS_ZH"));
                    rs2 = statement.executeQuery();
                    if(rs2.next()){
                        users.put(rs.getString(1),
                                new User(rs2.getString("YS_ZH"), rs2.getString("YS_XM"),
                                        rs2.getString("YS_XB"), rs2.getInt("YS_NL")));
                    }
                }
                Message message = Message.obtain();
                message.what = 0;
                message.obj = users;
                mHandler.sendMessage(message);
                conn.close();
                statement.close();
                rs.close();
            }
        } catch (SQLException SQLe) {
            System.err.println(SQLe.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    };
    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        initUIComponents();
    }
    private void initUIComponents() {
        chatList = view.findViewById(R.id.chat_list);
        SwipeMenuCreator creator = menu -> {
            // 创建删除选项
            SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
            // 设置属性
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
            deleteItem.setWidth(200);
            deleteItem.setTitle("删除");
            deleteItem.setTitleSize(18);
            deleteItem.setTitleColor(Color.WHITE);
            // 添加到菜单
            menu.addMenuItem(deleteItem);
        };
        chatList.setMenuCreator(creator);
        // 设置左划
        chatList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }
    /**
     * 加载所有回话
     */
    private void loadConversations() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        System.err.println(conversations.isEmpty());
        conversationList = new ArrayList<>();
        conversationTemps = new ArrayList<>();
        //StringBuilder param = new StringBuilder();
        for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
            conversationList.add(entry.getValue());

            EMMessage message = entry.getValue().getLastMessage();
            String account = getChatTarget(message);
            //头像，账号，用户名，最后一条消息的时间，最后一条消息，未读取消息数量
            conversationTemps.add(new ConversationTemp(R.mipmap.portrait0, account, users.get((String) account).getNickName(),
                    TimeUtil.getFormatTime(entry.getValue().getLastMessage().getMsgTime()),
                    entry.getValue().getLastMessage().getBody().toString(),
                    entry.getValue().getUnreadMsgCount()));
        }
        chatList.setAdapter(new MessageItemAdapterTemp(getActivity(), conversationTemps));
        addListeners();
        //loadUserList(param.toString());
    }
    /*
    private void loadUserList(String account) {
        String url = "/user/phones/info";

        Ion.with(this)
                .load(com.funnyseals.app.util.HttpUtil.BASE_URL + url)
                .setBodyParameter("param", account)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if (result != null) {
                        if (result.get("result").getAsBoolean()) {
                            user = new Gson().fromJson(result.get("object"), new TypeToken<List<User>>() {
                            }.getType());

                            adapter = new MessageItemAdapter(getActivity(), conversationList, user);
                            chatList.setAdapter(adapter);

                            addListeners();
                        }
                    }
                });
    }*/
    private void addListeners() {
        //获取聊天对象
        chatList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), DoctorChatActivity.class);
            //传递参数,聊天者的账号
            intent.putExtra("myfriend", users.get(conversationTemps.get(position).getAccount()));
            startActivity(intent);
        });
        //删除消息
        chatList.setOnMenuItemClickListener((position, menu, index) -> {
            if (index == 0) {
                conversationList.remove(position);
                conversationTemps.remove(position);

                adapter.notifyDataSetChanged();
            }

            // false : close the menu; true : not close the menu
            return false;
        });
    }
    /**
     * 获取消息对象的账号
     */
    private String getChatTarget(EMMessage message) {
        if (myAccount.equals(message.getFrom())) {
            return message.getTo();
        }
        return message.getFrom();
    }
}
