package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.androiddvptteam.helpme.MyTaskActivity.*;

public class MyTaskListFragment extends BaseFragment
{
	private List<Mission> missionList = new ArrayList<>();

	//google不让搞Fragment的构造函数，我能怎么办，我也很无奈啊
	private int tabType = -1;
	public void setTabType(int tabType)
	{
		if (this.tabType == -1)
			this.tabType = tabType;
		else
			Log.e(TAG, "Multiple setting tab type.");
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		refreshMissionList();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.my_task_list_fragment, container, false);
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.myTask_recyclerView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
		recyclerView.setLayoutManager(layoutManager);
		MissionAdapter adapter = new MissionAdapter(missionList, null, this.getActivity());
		recyclerView.setAdapter(adapter);
		return view;
	}

	private void refreshMissionList()
	{
		MyApplication myApplication = (MyApplication) getActivity().getApplication();
		switch (tabType)
		{
			case ALL_TAB:
				missionList = new ArrayList<>(myApplication.myMissions);
				break;
			case ACCEPTED_TAB:
				missionList.clear();
				missionList.add(new Mission("A Accepted Mission", "..."));
				break;
			case RELEASED_TAB:
				missionList.clear();
				missionList.add(new Mission("A Released Mission", "..."));
				break;
			case DOING_TAB:
				missionList.clear();
				missionList.add(new Mission("A Doing Mission", "..."));
				break;
		}
	}
}
