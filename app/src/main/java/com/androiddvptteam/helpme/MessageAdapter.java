package com.androiddvptteam.helpme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message>
{
    private int resourceId;
    public List<Message> M = new LinkedList<>();
    public Activity A;

    public MessageAdapter(Context context, int textViewResourceId, List<Message> objects, Activity a)
    {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        A = a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Message message = /*Message.get(position);*/getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.messageImage = (ImageView) view.findViewById(R.id.message_image);
            viewHolder.messageName = (TextView) view.findViewById(R.id.message_name);
            viewHolder.messageTime = (TextView) view.findViewById(R.id.message_time);
            viewHolder.messageText = (TextView) view.findViewById(R.id.message_text);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        System.out.println("执行到这里" + message.getImageId());
        viewHolder.messageImage.setImageResource(message.getImageId());
        viewHolder.messageName.setText(message.getMessageTitle());
        viewHolder.messageTime.setText(message.getMessageTime());
        viewHolder.messageText.setText(message.getMessageContent());

        final List<Integer> po = new ArrayList<>();
        po.add(position);
        ImageButton img = (ImageButton) view.findViewById(R.id.imageButton2);
        img.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        final MyApplication myApplication = (MyApplication) A.getApplication();
                        remove(message);
                    }
                }
        );

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
