package com.funnyseals.app.feature.patientMessage;

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
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;
import com.funnyseals.app.feature.doctorMessage.MessageItemAdapter;
import com.funnyseals.app.model.User;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
public class PatientMessageListFragment extends Fragment {
    private View                 mView;
    private SwipeMenuListView    mChatList;
    private MessageItemAdapter   mAdapter;
    private String               mMyAccount;
    private List<EMConversation> mConversationList;
    private User                 mMyDoctocr;
    private EMMessageListener    mMsgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived (List<EMMessage> messages) {
            //loadConversations();
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
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_patient_message_list, container, false);

        MyApplication application = (MyApplication) getActivity().getApplication();
        mMyAccount = application.getAccount();
        mMyDoctocr = ((PatientBottomActivity) getActivity()).getMyDoctor();

        EMClient.getInstance().chatManager().addMessageListener(mMsgListener);

        initUIComponents();

        return mView;
    }


    @Override
    public void onResume () {
        super.onResume();
        loadConversations();
    }

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
        mAdapter = new MessageItemAdapter(getActivity(), mConversationList, mMyDoctocr);
        mChatList.setAdapter(mAdapter);
        mChatList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), PatientChatActivity.class);
            intent.putExtra("myAccount", mMyAccount);
            intent.putExtra("myDoctor", mMyDoctocr);
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
