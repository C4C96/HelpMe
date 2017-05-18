package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.ic_menu_info_details;

public class MessageFragment extends Fragment implements View.OnClickListener
{

	private List<Message> messageList = new ArrayList<Message>();
	private static View view ;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		view = inflater.inflate(R.layout.message_fragment, container, false);

		Button button = (Button) view.findViewById(R.id.button);
		initMessage(); // 初始化消息数据
		button.setOnClickListener(this);
		MessageAdapter adapter = new MessageAdapter(getContext(), R.layout.message_item, messageList);
		ListView listView = (ListView) view.findViewById(R.id.message_listView);
		listView.setAdapter(adapter);
		return view;
	}

	private void initMessage() {
		Message systemMesssge = new Message(ic_menu_info_details, "系统消息", "您收到了一则新消息。");
		messageList.add(systemMesssge);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button:
				Message newMessage = new Message(ic_menu_info_details, "系统消息", "您收到了一则新消息。");
				messageList.add(newMessage);
				MessageAdapter adapter = new MessageAdapter(getContext(), R.layout.message_item, messageList);
				ListView listView = (ListView) view.findViewById(R.id.message_listView);
				listView.setAdapter(adapter);
				break;
			default:
				break;
		}
	}


}
