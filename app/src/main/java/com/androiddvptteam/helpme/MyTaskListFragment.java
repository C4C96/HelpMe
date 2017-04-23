package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static com.androiddvptteam.helpme.MyTaskActivity.*;

public class MyTaskListFragment extends BaseFragment
{
	//google不让搞Fragment的构造函数，我能怎么办，我也很无奈啊
	private int tabType = -1;
	public void setTabType(int tabType)
	{
		if (this.tabType == -1)
			this.tabType = tabType;
		else
			Log.e(TAG, "Multiple setting tab type.");
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.my_task_list_fragment, container, false);
		TextView debug = (TextView) view.findViewById(R.id.MY_TASK_LIST_DEBUG_TEXT);
		switch (tabType)
		{
			case ALL_TAB:
				debug.setText("all");
				break;
			case RELEASED_TAB:
				debug.setText("released");
				break;
			case ACCEPTED_TAB:
				debug.setText("accepted");
				break;
			case DOING_TAB:
				debug.setText("doing");
				break;
		}
		return view;
	}
}
