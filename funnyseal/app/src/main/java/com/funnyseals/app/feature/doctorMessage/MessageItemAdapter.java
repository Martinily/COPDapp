package com.funnyseals.app.feature.doctorMessage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.model.User;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   : 消息条目adapter
 *     version: 1.0
 * </pre>
 */
public class MessageItemAdapter extends BaseAdapter {

    private LayoutInflater       mInflater;
    private List<EMConversation> mConversationList;
    private List<User>           mAllMyPatient;
    private User                 mMyDoctor;
    private String               mUserType;

    public MessageItemAdapter (Context context, List<EMConversation> conversationList, List<User>
            allMyPatient) {
        this.mInflater = LayoutInflater.from(context);
        this.mConversationList = conversationList;
        this.mAllMyPatient = allMyPatient;
        mUserType = "doctor";
    }

    public MessageItemAdapter (Context context, List<EMConversation> conversationList, User
            myDoctor) {
        this.mInflater = LayoutInflater.from(context);
        this.mConversationList = conversationList;
        this.mMyDoctor = myDoctor;
        mUserType = "patient";
    }

    @Override
    public int getCount () {
        return mConversationList.size();
    }

    @Override
    public EMConversation getItem (int position) {
        return mConversationList.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        convertView = mInflater.inflate(R.layout.list_item_chat, parent, false);

        viewHolder = new ViewHolder();

        viewHolder.mPortrait = convertView.findViewById(R.id.chat_item_portrait);
        viewHolder.mName = convertView.findViewById(R.id.chat_item_name);
        viewHolder.mTime = convertView.findViewById(R.id.chat_item_time);
        viewHolder.mContent = convertView.findViewById(R.id.chat_item_content);
        viewHolder.mMessageNum = convertView.findViewById(R.id.chat_item_message_num);

        viewHolder.mPortrait.setImageResource(R.drawable.vector_drawable_portrait);
        EMConversation conversation = getItem(position);
        viewHolder.mAccount = conversation.conversationId();
        String account = viewHolder.mAccount;
        if (mUserType.equals("doctor")) {
            for (User p : mAllMyPatient) {
                if (p.getAccount().equals(account)) {
                    viewHolder.mName.setText(p.getName().isEmpty() ? account : p.getName());
                    break;
                }
            }
        } else if (mUserType.equals("patient")) {
            viewHolder.mName.setText(mMyDoctor.getName().isEmpty() ? account : mMyDoctor.getName());
        }


        viewHolder.mContent.setText(((EMTextMessageBody) conversation.getLastMessage().getBody())
                .getMessage());

        int unread = conversation.getUnreadMsgCount();
        if (unread != 0) {
            viewHolder.mMessageNum.showCirclePointBadge();
            viewHolder.mMessageNum.showTextBadge(unread + "");
            viewHolder.mMessageNum.setOnClickListener(v -> conversation.markAllMessagesAsRead());
        }

        viewHolder.mTime.setText(DateUtils.getTimestampString(new Date(conversation
                .getLastMessage().getMsgTime())));

        return convertView;
    }

    private class ViewHolder {
        private ImageView        mPortrait;
        private String           mAccount;
        private TextView         mName;
        private TextView         mTime;
        private TextView         mContent;
        private BGABadgeTextView mMessageNum;

        public String getAccount () {
            return mAccount;
        }
    }

}

