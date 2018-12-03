package com.funnyseals.app.feature.doctorMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.model.ConversationTemp;

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
public class MessageItemAdapterTemp extends BaseAdapter {
    private LayoutInflater mInflater;

    private List<ConversationTemp> mData;

    public MessageItemAdapterTemp(Context context, List<ConversationTemp> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ConversationTemp getItem(int position) {
        return mData.get(position);
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

        private ImageView mPortrait;

        private TextView mName;

        private TextView mTime;

        private TextView mContent;

        private BGABadgeTextView mMessageNum;

        private void init(ConversationTemp item) {
            int unread = item.getUnreadNum();

            mPortrait.setBackgroundResource(item.getPortrait());
            mName.setText(item.getName());
            mTime.setText(item.getTime());
            mContent.setText(item.getContent());

            if (unread != 0) {
                mMessageNum.showCirclePointBadge();
                mMessageNum.showTextBadge(unread + "");
            }
        }
    }
}
