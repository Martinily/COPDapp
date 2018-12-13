package com.funnyseals.app.feature.doctorMessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

public class DoctorChatActivity extends AppCompatActivity {

    private ListView           mLv_message;
    private EditText           mEt_input;
    private Button             mBtn_send;
    private MyApplication      myApplication;
    private ChatMessageAdapter CurrentChatadapter;
    private String             mMyfriend;
    private List<EMMessage>    mMessageList;
    private EMConversation     mConversation;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_chat);
        myApplication = (MyApplication) getApplication();
        init();
    }

    @Override
    protected void onResume () {
        super.onResume();

        loadAllMessage();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    protected void onStop () {
        super.onStop();
        // 退出前将所有消息导入数据库
        EMClient.getInstance().chatManager().importMessages(mMessageList);
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    private void init () {
        mMyfriend = (String) getIntent().getSerializableExtra("myfriend");

        initUIComponents();

        addListener();

        loadAllMessage();
    }

    private void initUIComponents () {
        initToolbar();

        mLv_message = findViewById(R.id.lv_doctor_chat_message_container);

        mEt_input = findViewById(R.id.et_doctor_chat_input);

        mBtn_send = findViewById(R.id.btn_doctor_chat_send);

        sendEnabled(false);
    }

    private void addListener () {
        mEt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged (Editable s) {
                if ("".equals(mEt_input.getText().toString())) {
                    sendEnabled(false);
                } else {
                    sendEnabled(true);
                }
            }
        });

        mBtn_send.setOnClickListener(v -> onSend());

        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private void initToolbar () {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.tb_doctor_chat_toolbar);
        toolbar.setTitle(mMyfriend);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * 加载所有聊天
     */
    private void loadAllMessage () {
        mConversation = EMClient.getInstance().chatManager().getConversation(mMyfriend,
                EMConversation.EMConversationType.Chat, true);

        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();

        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
        // 打开聊天界面获取最后一条消息内容并显示
        if (mConversation.getAllMessages().size() > 0) {
            EMMessage message = mConversation.getLastMessage();
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            // 将消息内容和时间显示出来
            //mContentText.setText(body.getMessage() + " - " + conversation.getLastMessage()
            // .getMsgTime());
        }

        if (mConversation != null) {
            //获取此会话的所有消息
            mMessageList = mConversation.getAllMessages();
        } else {
            mMessageList = new ArrayList<>();
        }

        initMessageList();
    }

    private void initMessageList () {
        CurrentChatadapter = new ChatMessageAdapter(this, mMyfriend, mMessageList);

        mLv_message.setAdapter(CurrentChatadapter);

        mLv_message.smoothScrollToPositionFromTop(mMessageList.size(), 0);
    }

    private void onSend () {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户
        EMMessage message = EMMessage.createTxtSendMessage(mEt_input.getText().toString(),
                mMyfriend);
        message.setAttribute("nickName", "医生");
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        onAddMessage(message);
    }

    private void onAddMessage (EMMessage message) {
        mMessageList.add(message);
        mEt_input.setText("");
        mLv_message.smoothScrollToPositionFromTop(mMessageList.size(), 0);
        CurrentChatadapter.notifyDataSetChanged();
    }

    private void sendEnabled (boolean enabled) {
        mBtn_send.setEnabled(enabled);

        if (enabled) {
            mBtn_send.setBackgroundResource(R.drawable.bordered_button);
        } else {
            mBtn_send.setBackgroundResource(R.drawable.bordered_button_disable);
        }
    }

    private EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived (List<EMMessage> messages) {
            // 循环遍历当前收到的消息
            for (EMMessage message : messages) {
                if (message.getFrom().equals(mMyfriend)) {
                    // 设置消息为已读
                    mConversation.markMessageAsRead(message.getMsgId());

                    onAddMessage(message);
                }
            }
        }

        @Override
        public void onCmdMessageReceived (List<EMMessage> messages) {
            //收到透传消息
            System.out.println("DoctorChatActivity.onCmdMessageReceived");
        }

        @Override
        public void onMessageRead (List<EMMessage> messages) {
            //收到已读回执
            System.out.println("DoctorChatActivity.onMessageRead");
        }

        @Override
        public void onMessageDelivered (List<EMMessage> message) {
            //收到已送达回执
            System.out.println("DoctorChatActivity.onMessageDelivered");
        }

        @Override
        public void onMessageRecalled (List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged (EMMessage message, Object change) {
            //消息状态变动
            System.out.println("DoctorChatActivity.onMessageChanged");
        }
    };
}