package com.funnyseals.app.feature.doctorMessage;

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
    private List<User>           mAllMyFriend;

    public MessageItemAdapter (Context context, List<EMConversation> conversationList, List<User>
            allMyFriend) {
        this.mInflater = LayoutInflater.from(context);
        this.mConversationList = conversationList;
        this.mAllMyFriend = allMyFriend;
    }

    public MessageItemAdapter (Context context, List<EMConversation> conversationList) {
        this.mInflater = LayoutInflater.from(context);
        this.mConversationList = conversationList;
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

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_chat, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.mPortrait = convertView.findViewById(R.id.chat_item_portrait);
            viewHolder.mName = convertView.findViewById(R.id.chat_item_name);
            viewHolder.mTime = convertView.findViewById(R.id.chat_item_time);
            viewHolder.mContent = convertView.findViewById(R.id.chat_item_content);
            viewHolder.mMessageNum = convertView.findViewById(R.id.chat_item_message_num);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mPortrait.setImageResource(R.drawable.portrait);
        EMConversation conversation = getItem(position);
        viewHolder.mAccount = conversation.conversationId();
        String account = viewHolder.mAccount;
        for (User p : mAllMyFriend) {
            if (p.getAccount().equals(account)) {
                viewHolder.mName.setText(p.getName().isEmpty() ? account : p.getName());
                break;
            }
        }

        viewHolder.mContent.setText(((EMTextMessageBody) conversation.getLastMessage().getBody())
                .getMessage());

        int unread = conversation.getUnreadMsgCount();
        if(unread!=0){
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

