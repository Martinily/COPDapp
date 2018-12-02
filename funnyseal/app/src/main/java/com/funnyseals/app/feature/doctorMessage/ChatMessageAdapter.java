package com.funnyseals.app.feature.doctorMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cpiz.android.bubbleview.BubbleTextView;
import com.funnyseals.app.R;
import com.funnyseals.app.custom_view.Portrait;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.model.User;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ChatMessageAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<EMMessage> messageList;

    private User          myfriend;
    private MyApplication application;

    public ChatMessageAdapter(Context context, User user, List<EMMessage> messageList) {
        this.inflater = LayoutInflater.from(context);
        this.myfriend = user;
        this.messageList = messageList;
        application = (MyApplication) context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public EMMessage getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        EMMessage message = messageList.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            if (application.getAccount().equals(message.getFrom())) {
                convertView = inflater.inflate(R.layout.chat_send, parent, false);

                viewHolder.portrait = convertView.findViewById(R.id.chat_send_portrait);
                viewHolder.text = convertView.findViewById(R.id.chat_send_text);
            } else {
                convertView = inflater.inflate(R.layout.chat_receive, parent, false);

                viewHolder.portrait = convertView.findViewById(R.id.chat_receive_portrait);
                viewHolder.text = convertView.findViewById(R.id.chat_receive_text);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (application.getAccount().equals(message.getFrom())) {
            viewHolder.portrait.setUserAccount(application.getAccount()).setUrl(null);
        } else {
            viewHolder.portrait.setUserAccount(myfriend.getAccount()).setUrl(null);
        }

        viewHolder.portrait.show();
        viewHolder.text.setText(((EMTextMessageBody) message.getBody()).getMessage());

        return convertView;
    }

    /**
     * 此处将发送的消息和接受的消息合并
     */
    private class ViewHolder {

        private Portrait portrait;

        private BubbleTextView text;
    }
}
