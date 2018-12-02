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
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MessageItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    private List<EMConversation> conversationList;

    private List<User> myfriends;

    public MessageItemAdapter(Context context, List<EMConversation> conversationList, List<User> myfriends) {
        this.inflater = LayoutInflater.from(context);
        this.conversationList = conversationList;
        this.myfriends = myfriends;
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public Conversation getItem(int position) {
        EMMessage lastMessage = conversationList.get(position).getLastMessage();
        return new Conversation(myfriends.get(position), lastMessage, conversationList.get(position).getUnreadMsgCount());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_chat, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.portrait = convertView.findViewById(R.id.chat_item_portrait);
            viewHolder.name = convertView.findViewById(R.id.chat_item_name);
            viewHolder.time = convertView.findViewById(R.id.chat_item_time);
            viewHolder.content = convertView.findViewById(R.id.chat_item_content);
            viewHolder.messageNum = convertView.findViewById(R.id.chat_item_message_num);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.init(getItem(position));

        return convertView;
    }

    private class ViewHolder {

        private Portrait portrait;

        private TextView name;

        private TextView time;

        private TextView content;

        private BGABadgeTextView messageNum;

        private void init(Conversation item) {
            EMMessage message = item.getMessage();
            User user = item.getUser();
            int unread = item.getUnReadNum();

            portrait.setUserAccount(user.getAccount()).setUrl(user.getIconUrl()).show();
            name.setText(user.getNickName());
            time.setText(com.funnyseals.app.util.TimeUtil.getFormatTime(message.getMsgTime()));
            content.setText(((EMTextMessageBody) message.getBody()).getMessage());

            if (unread != 0) {
                messageNum.showCirclePointBadge();
                messageNum.showTextBadge(unread + "");
            }
        }
    }
}

