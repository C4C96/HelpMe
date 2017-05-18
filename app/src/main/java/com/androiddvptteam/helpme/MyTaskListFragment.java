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
	public static MyApplication myApplication;//生成MyTaskListFragment对象时不一定能getActivity()，所以通过这种方式获得MyApplication实例
	private List<Mission> missionList;//列表内容

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
		MissionAdapter adapter = new MissionAdapter(missionList, this.getActivity());
		recyclerView.setAdapter(adapter);
		return view;
	}

	/**
	 * 刷新列表，基于已刷新的myApplication.myMissions
	 * */
	public void refreshMissionList()
	{
		Log.d(TAG, "refreshMissionList");

		PersonalInformation personalInformation = myApplication.getPersonalInformation();
		List<Mission> myMissions = myApplication.myMissions;
		if (myMissions == null || personalInformation == null)
		{
			missionList = new ArrayList<>();
			return;
		}
		switch (tabType)
		{
			case ALL_TAB:
				missionList = new ArrayList<>(myMissions);
				break;
			case ACCEPTED_TAB:
				missionList = new ArrayList<>();
				for(Mission mission : myMissions)
					if (mission.getRecipient().getSchoolNumber().equals(personalInformation.getSchoolNumber())
						&& (mission.getState() == Mission.STATE_FINISHED || mission.getState() == Mission.STATE_CANCELED))
						missionList.add(mission);
			break;
			case RELEASED_TAB:
				missionList = new ArrayList<>();
				for(Mission mission : myMissions)
					if (mission.getPublisher().getSchoolNumber().equals(personalInformation.getSchoolNumber()))
						missionList.add(mission);
				break;
			case DOING_TAB:
				missionList = new ArrayList<>();
				for(Mission mission : myMissions)
					if (mission.getRecipient().getSchoolNumber().equals(personalInformation.getSchoolNumber())
						&& mission.getState() == Mission.STATE_DOING)
						missionList.add(mission);
				break;
		}
	}
}
