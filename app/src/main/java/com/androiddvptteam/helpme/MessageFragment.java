package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.androiddvptteam.helpme.Connection.GetMessageConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static android.R.drawable.ic_menu_info_details;
import static com.androiddvptteam.helpme.ListFragment.mainActivity;

public class MessageFragment extends Fragment
{
	private static View view ;
	public List<Message> message=new LinkedList<>();
	private MessageAdapter adapter;

	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.message_fragment, container, false);
		getMessage();
		adapter = new MessageAdapter(getContext(), R.layout.message_item, message);
		ListView listView = (ListView) view.findViewById(R.id.message_listView);
		listView.setAdapter(adapter);
		return view;
	}

	public void getMessage()
	{
		final MyApplication myApplication = (MyApplication) getActivity().getApplication();
		final PersonalInformation me=myApplication.getPersonalInformation();
		int threadNumber = 1;
		final CountDownLatch countDownLatch = new CountDownLatch(threadNumber);
		try
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					final GetMessageConnection connection;
					try
					{
						connection = new GetMessageConnection(new URL("http://192.168.0.3:8080/AndroidServlet/GetMessageServlet"));
//							connection=new GetMessageConnection(new URL("http://123.206.125.166:8080/AndroidServlet/GetMessageServlet"));
						connection.setMyNum(me.getSchoolNumber());
						connection.connect();
						message = connection.getMessage();
						countDownLatch.countDown();
						//adapter.Message.addAll(message);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			).start();
			countDownLatch.await();
		}
		catch (Exception e){}
	}
}
