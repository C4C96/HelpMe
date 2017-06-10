package com.androiddvptteam.helpme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message>
{
    private int resourceId;
    //public List<Message> Message=new LinkedList<>();

    public MessageAdapter(Context context, int textViewResourceId, List<Message> objects)
    {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Message message = /*Message.get(position);*/getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.messageImage = (ImageView) view.findViewById (R.id.message_image);
            viewHolder.messageName = (TextView) view.findViewById (R.id.message_name);
            viewHolder.messageTime = (TextView) view.findViewById (R.id.message_time);
            viewHolder.messageText = (TextView) view.findViewById (R.id.message_text);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        }
        else
        {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        System.out.println("执行到这里"+message.getImageId());
        viewHolder.messageImage.setImageResource(message.getImageId());
        viewHolder.messageName.setText(message.getMessageTitle());
        viewHolder.messageTime.setText(message.getMessageTime());
        viewHolder.messageText.setText(message.getMessageContent());

        return view;
    }

    class ViewHolder
    {
        ImageView messageImage;
        TextView messageTime;
        TextView messageName;
        TextView messageText;
    }
}
