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
    private LayoutInflater inflater;

    private List<ConversationTemp> data;

    public MessageItemAdapterTemp(Context context, List<ConversationTemp> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ConversationTemp getItem(int position) {
        return data.get(position);
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

        private ImageView portrait;

        private TextView name;

        private TextView time;

        private TextView content;

        private BGABadgeTextView messageNum;

        private void init(ConversationTemp item) {
            int unread = item.getUnreadNum();

            portrait.setBackgroundResource(item.getPortrait());
            name.setText(item.getName());
            time.setText(item.getTime());
            content.setText(item.getContent());

            if (unread != 0) {
                messageNum.showCirclePointBadge();
                messageNum.showTextBadge(unread + "");
            }
        }
    }
}
