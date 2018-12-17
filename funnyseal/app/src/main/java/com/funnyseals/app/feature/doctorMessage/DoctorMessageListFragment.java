package com.funnyseals.app.feature.doctorMessage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.funnyseals.app.model.User;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   : 医生端消息列表fragment
 *     version: 1.0
 * </pre>
 */
public class DoctorMessageListFragment extends Fragment {

    private View                 mView;
    private SwipeMenuListView    mChatList;
    private MessageItemAdapter   mAdapter;
    private List<EMConversation> mConversationList;
    private String               mMyAccount;
    private List<User>           mAllMyPatient;
    //消息监听，如果有新的消息就更新消息列表
    private EMMessageListener    mMsgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived (List<EMMessage> messages) {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCmdMessageReceived (List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead (List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered (List<EMMessage> messages) {

        }

        @Override
        public void onMessageRecalled (List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged (EMMessage message, Object change) {

        }
    };


    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_doctor_message_list, container, false);

        MyApplication application = (MyApplication) getActivity().getApplication();
        mMyAccount = application.getAccount();

        mAllMyPatient = ((DoctorBottomActivity) getActivity()).getAllMyPatient();

        EMClient.getInstance().chatManager().addMessageListener(mMsgListener);
        initUIComponents();

        return mView;
    }

    @Override
    public void onResume () {
        super.onResume();
        loadConversations();
    }

    /*
    * 创建自定义菜单，设置左划删除
    * */
    private void initUIComponents () {
        mChatList = mView.findViewById(R.id.chat_list);

        SwipeMenuCreator creator = menu -> {
            // 创建删除选项
            SwipeMenuItem deleteItem = new SwipeMenuItem(mView.getContext());
            // 设置属性
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
            deleteItem.setWidth(200);
            deleteItem.setTitle("删除");
            deleteItem.setTitleSize(18);
            deleteItem.setTitleColor(Color.WHITE);
            // 添加到菜单
            menu.addMenuItem(deleteItem);
        };

        mChatList.setMenuCreator(creator);
        // 设置左划
        mChatList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    /**
     * 加载所有回话
     */
    private void loadConversations () {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager()
                .getAllConversations();
        mConversationList = new ArrayList<>();
        mConversationList.addAll(conversations.values());
        mAdapter = new MessageItemAdapter(getActivity(), mConversationList, mAllMyPatient);

        mChatList.setAdapter(mAdapter);
        mChatList.setOnItemClickListener((parent, view, position, id) -> {
            EMConversation conversation = (EMConversation) mChatList.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), DoctorChatActivity.class);
            intent.putExtra("myfriend", conversation.conversationId());
            startActivity(intent);
        });

        //删除消息
        mChatList.setOnMenuItemClickListener((position, menu, index) -> {
            if (index == 0) {
                mConversationList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
            // false : close the menu; true : not close the menu
            return false;
        });
    }
}
