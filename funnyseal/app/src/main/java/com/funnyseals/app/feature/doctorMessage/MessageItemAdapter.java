package com.funnyseals.app.feature.doctorMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.custom_view.Portrait;
import com.funnyseals.app.model.Conversation;
import com.funnyseals.app.model.User;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

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
    private LayoutInflater mInflater;

    private List<EMConversation> mConversationList;

    private List<User> mMyfriends;

    public MessageItemAdapter(Context context, List<EMConversation> conversationList, List<User> myfriends) {
        this.mInflater = LayoutInflater.from(context);
        this.mConversationList = conversationList;
        this.mMyfriends = myfriends;
    }

    @Override
    public int getCount() {
        return mConversationList.size();
    }

    @Override
    public Conversation getItem(int position) {
        EMMessage lastMessage = mConversationList.get(position).getLastMessage();
        return new Conversation(mMyfriends.get(position), lastMessage, mConversationList.get(position).getUnreadMsgCount());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        viewHolder.init(getItem(position));

        return convertView;
    }

    private class ViewHolder {

        private Portrait mPortrait;

        private TextView mName;

        private TextView mTime;

        private TextView mContent;

        private BGABadgeTextView mMessageNum;

        private void init(Conversation item) {
            EMMessage message = item.getMessage();
            User user = item.getUser();
            int unread = item.getUnReadNum();

            mPortrait.setUserAccount(user.getAccount()).setUrl(user.getIconUrl()).show();
            mName.setText(user.getNickName());
            mTime.setText(com.funnyseals.app.util.TimeUtil.getFormatTime(message.getMsgTime()));
            mContent.setText(((EMTextMessageBody) message.getBody()).getMessage());

            if (unread != 0) {
                mMessageNum.showCirclePointBadge();
                mMessageNum.showTextBadge(unread + "");
            }
        }
    }
}

