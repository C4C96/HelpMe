package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListFragment extends BaseFragment
{
	private Spinner spinnerGender;
	private Spinner spinnerAttribute;
	private Spinner spinnerRange;

	public String [] genderList=new String[5];
	public String [] attributeList=new String[5];
	public String [] rangeList=new String[5];

	private ArrayAdapter<String> arr_adapter;

	public int gender;
	public int attribute;
	public int range;

	public View view;

	private List<Mission> missionList=new ArrayList<>();
	private List<String> schoolnumList=new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
	private MissionAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.list_fragment, container, false);

		//加载筛选数据
		loadGenderData();
		loadAttributeData();
		loadRangeData();
		getSpinner();

		initMissions();

		//任务列表
		RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.list_recycler_view);
		LinearLayoutManager layoutManager=new LinearLayoutManager(this.getContext());
		recyclerView.setLayoutManager(layoutManager);
		adapter=new MissionAdapter(missionList,schoolnumList,this.getActivity());
		recyclerView.setAdapter(adapter);

		//下拉刷新
        swipeRefresh=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
		swipeRefresh.setOnRefreshListener(new
				SwipeRefreshLayout.OnRefreshListener()
				{
					@Override
					public void onRefresh() {
						refreshMissions();
						Log.d("ListSize:",String.valueOf(adapter.getItemCount()));
					}
				});



		return view;
	}

	private void loadGenderData()//加载性别数据
	{
		spinnerGender = (Spinner) view.findViewById(R.id.gender_Spinner);
		//数据
		genderList[0]="性别";
		genderList[1]=this.getString(R.string.genderMale);
		genderList[2]=this.getString(R.string.genderFemale);
		genderList[3]=this.getString(R.string.genderOther);
		genderList[4]=this.getString(R.string.genderIDontCare);

		//适配器
		arr_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,genderList);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinnerGender.setAdapter(arr_adapter);
		spinnerGender.setVisibility(View.VISIBLE);
	}

	private void loadAttributeData()//加载属性数据
	{
		spinnerAttribute = (Spinner) view.findViewById(R.id.attribute_Spinner);

		//数据
		attributeList[0]="类型";
		attributeList[1]=this.getString(R.string.attributeBring);
		attributeList[2]=this.getString(R.string.attributeTake);
		attributeList[3]=this.getString(R.string.attributeBuy);
		attributeList[4]=this.getString(R.string.attributeOther);

		//适配器
		arr_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,attributeList);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinnerAttribute.setAdapter(arr_adapter);
	}

	private void loadRangeData()//加载范围数据
	{
		spinnerRange = (Spinner) view.findViewById(R.id.range_Spinner);

		//数据
		rangeList[0]="范围";
		rangeList[1]=this.getString(R.string.range0);
		rangeList[2]=this.getString(R.string.range1);
		rangeList[3]=this.getString(R.string.range2);
		rangeList[4]=this.getString(R.string.range3);

		//适配器
		arr_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,rangeList);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinnerRange.setAdapter(arr_adapter);
	}



	public void getSpinner()//得到3个下拉框选择的值
	{
		spinnerGender.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						gender =  spinnerGender.getSelectedItemPosition();
					}
					public void onNothingSelected(AdapterView<?> parent)
					{
						// TODO Auto-generated method stub
					}
				}
		);

		spinnerAttribute.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						attribute =  spinnerAttribute.getSelectedItemPosition();
					}
					public void onNothingSelected(AdapterView<?> parent)
					{
						// TODO Auto-generated method stub
					}
				}
		);

		spinnerRange.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						range =  spinnerRange.getSelectedItemPosition();
					}
					public void onNothingSelected(AdapterView<?> parent)
					{
						// TODO Auto-generated method stub
					}
				}
		);
	}

	private void initMissions()//从数据库读取任务信息，放到missionList中
	{
		//删除missionList中的数据
		Iterator<Mission> it = missionList.iterator();
		while(it.hasNext())
		{
			Mission x = it.next();
				it.remove();
		}

	//	Mission m1=new Mission("give me some water","...");
	//	m1.setMissionAttribute(1,2,1);
	//	missionList.add(m1);

	//	Mission m2=new Mission("give me some shit","...");
	//	m2.setMissionAttribute(2,3,1);
	//	missionList.add(m2);

	//	Mission m3=new Mission("give me some food","...");
	//	m3.setMissionAttribute(3,1,1);
	//	missionList.add(m3);

	//	Mission m4=new Mission("give me some paper","...");
	//	m4.setMissionAttribute(1,2,3);
	//	missionList.add(m4);

	//	Mission m5=new Mission("give me some money","...");
	//	m5.setMissionAttribute(2,2,2);
	//	missionList.add(m5);
	}

	private void refreshMissions()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				getActivity().runOnUiThread(new
						Runnable() {
							@Override
							public void run() {
								initMissions();
								adapter.notifyDataSetChanged();
								swipeRefresh.setRefreshing(false);
							}
						});

			}
		}).start();
	}
}
