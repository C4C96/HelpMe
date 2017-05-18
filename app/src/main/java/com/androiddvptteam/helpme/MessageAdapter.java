package com.androiddvptteam.helpme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private int resourceId;

    public MessageAdapter(Context context, int textViewResourceId, List<Message> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.messageImage = (ImageView) view.findViewById (R.id.message_image);
            viewHolder.messageName = (TextView) view.findViewById (R.id.message_name);
            viewHolder.messageText = (TextView) view.findViewById (R.id.message_text);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.messageImage.setImageResource(message.getImageId());
        viewHolder.messageName.setText(message.getName());
        viewHolder.messageText.setText(message.getText());
        return view;
    }

    class ViewHolder {

        ImageView messageImage;

        TextView messageName;

        TextView messageText;

    }

}
