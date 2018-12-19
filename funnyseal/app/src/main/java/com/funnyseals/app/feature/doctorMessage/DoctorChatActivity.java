package com.funnyseals.app.feature.doctorMessage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.doctorNursingPlan.DoctorNursingPlanFragment;
import com.funnyseals.app.model.User;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

public class DoctorChatActivity extends AppCompatActivity implements EMMessageListener{

    private ListView    mLv_message;
    private EditText    mEt_input;
    private Button      mBtn_send;
    private ImageButton mVioce;
    private ImageButton mNursingPlan;
    private ImageButton mVideo;

    private User   mMyPatient;
    private String mPatientAccount;

    private ChatMessageAdapter CurrentChatadapter;
    private List<EMMessage>    mMessageList;

    private EMConversation    mConversation;
    private EMMessageListener msgListener;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_chat);
        Intent intent = this.getIntent();
        mMyPatient = (User) intent.getSerializableExtra("myPatient");
        msgListener=this;
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
        mPatientAccount = (String) getIntent().getSerializableExtra("myfriend");

        initView();
        addListener();
        loadAllMessage();
    }

    private void initView () {
        mLv_message = findViewById(R.id.lv_doctor_chat_message_container);
        mEt_input = findViewById(R.id.et_doctor_chat_input);
        mBtn_send = findViewById(R.id.btn_doctor_chat_send);
        mNursingPlan = findViewById(R.id.ibtn_doctor_nursingplan);
        mVioce = findViewById(R.id.ibtn_doctor_vioce);
        mVideo = findViewById(R.id.ibtn_doctor_video);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.tb_doctor_chat_toolbar);

        if (!mMyPatient.getName().isEmpty()) {
            toolbar.setTitle(mMyPatient.getName());
        } else {
            toolbar.setTitle(mPatientAccount);
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
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

        mVioce.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorChatActivity.this, VoiceCallActivity.class);
            CallManager.getInstance().setChatId(mPatientAccount);
            CallManager.getInstance().setInComingCall(false);
            CallManager.getInstance().setCallType(CallManager.CallType.VOICE);
            startActivity(intent);
        });

        mVideo.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorChatActivity.this, DoctorVideoCallActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("myPatient", mMyPatient);
            intent.putExtras(bundle);
            CallManager.getInstance().setChatId(mPatientAccount);
            CallManager.getInstance().setInComingCall(false);
            CallManager.getInstance().setCallType(CallManager.CallType.VIDEO);
            startActivity(intent);
        });

        mNursingPlan.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorChatActivity.this, DoctorNursingPlanFragment.class);
            intent.putExtra("myFriend", mPatientAccount);
            startActivity(intent);
        });

        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    /**
     * 加载所有聊天
     */
    private void loadAllMessage () {
        mConversation = EMClient.getInstance().chatManager().getConversation(mPatientAccount,
                EMConversation.EMConversationType.Chat, true);
        mConversation.markAllMessagesAsRead();

        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
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
        CurrentChatadapter = new ChatMessageAdapter(this, mPatientAccount, mMessageList);
        mLv_message.setAdapter(CurrentChatadapter);
        mLv_message.smoothScrollToPositionFromTop(mMessageList.size(), 0);
    }

    private void onSend () {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户
        EMMessage message = EMMessage.createTxtSendMessage(mEt_input.getText().toString(),
                mPatientAccount);
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

    @Override
    public void onMessageReceived (List<EMMessage> messages) {
        for (EMMessage message : messages) {
            if (message.getFrom().equals(mPatientAccount)) {
                mConversation.markMessageAsRead(message.getMsgId());
                runOnUiThread(() -> onAddMessage(message));
            }
        }
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
}