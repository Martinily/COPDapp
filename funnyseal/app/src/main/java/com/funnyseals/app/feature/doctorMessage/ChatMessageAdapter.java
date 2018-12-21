package com.funnyseals.app.feature.doctorMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cpiz.android.bubbleview.BubbleTextView;
import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   : 聊天界面的消息adapter
 *     version: 1.0
 * </pre>
 */
public class ChatMessageAdapter extends BaseAdapter {

    private String mMyfriend;

    private LayoutInflater  mInflater;
    private List<EMMessage> mMessageList;
    private MyApplication   mApplication;

    public ChatMessageAdapter (Context context, String friend, List<EMMessage> messageList) {
        this.mInflater = LayoutInflater.from(context);
        this.mMyfriend = friend;
        this.mMessageList = messageList;
        mApplication = (MyApplication) context.getApplicationContext();
    }

    @Override
    public int getCount () {
        return mMessageList.size();
    }

    @Override
    public EMMessage getItem (int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        EMMessage message = mMessageList.get(position);

        if (mApplication.getAccount().equals(message.getFrom())) {
            convertView = mInflater.inflate(R.layout.chat_send, parent, false);
            viewHolder.portrait = convertView.findViewById(R.id.chat_send_portrait);
            viewHolder.text = convertView.findViewById(R.id.chat_send_text);
        } else {
            convertView = mInflater.inflate(R.layout.chat_receive, parent, false);
            viewHolder.portrait = convertView.findViewById(R.id.chat_receive_portrait);
            viewHolder.text = convertView.findViewById(R.id.chat_receive_text);
        }

        convertView.setTag(viewHolder);

        if (mApplication.getAccount().equals(message.getFrom())) {
            viewHolder.portrait.setImageResource(R.drawable.vector_drawable_user);
        } else {
            viewHolder.portrait.setImageResource(R.drawable.vector_drawable_user);
        }

        viewHolder.text.setText(((EMTextMessageBody) message.getBody()).getMessage());

        return convertView;
    }

    private class ViewHolder {
        private ImageView      portrait;
        private BubbleTextView text;
    }

}
