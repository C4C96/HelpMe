package com.androiddvptteam.helpme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MyTaskActivity extends BaseActivity
{
	//进入该Activity时TAB的状态
	public static final int ALL_TAB = 0;
	public static final int RELEASED_TAB = 1;
	public static final int ACCEPTED_TAB = 2;
	public static final int DOING_TAB = 3;

	private MyTaskListFragment allList, releasedList, acceptedList, doingList;
	private TabLayout tabLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_task);

		//设置返回按钮
		Toolbar toolbar = (Toolbar) findViewById(R.id.options_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initTabLayout();

		//获取传入参数
		Intent intent = getIntent();
		int tabType = intent.getIntExtra("tabType", ALL_TAB);
		tabLayout.getTabAt(tabType).select();

		//初次刷新
		final MyApplication myApplication = (MyApplication)getApplication();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				myApplication.refreshMyMissions();
			}
		}).start();
	}


	/**
	 * 初始化TabLayout
	 * */
	private void initTabLayout()
	{
		ViewPager viewPager = (ViewPager) findViewById(R.id.myTask_viewPager);
		List<Fragment> list_fragment = new ArrayList<>();
		List<String> list_title = new ArrayList<>();
		MyTaskTabAdapter adapter;

		MyTaskListFragment.myApplication = (MyApplication) getApplication();
		MyTaskListFragment.myTaskActivity = this;

		allList = new MyTaskListFragment();
		allList.setTabType(ALL_TAB);
		releasedList = new MyTaskListFragment();
		releasedList.setTabType(RELEASED_TAB);
		acceptedList = new MyTaskListFragment();
		acceptedList.setTabType(ACCEPTED_TAB);
		doingList = new MyTaskListFragment();
		doingList.setTabType(DOING_TAB);

		list_fragment.add(allList);
		list_fragment.add(releasedList);
		list_fragment.add(acceptedList);
		list_fragment.add(doingList);

		list_title.add("全部");
		list_title.add("发布过的");
		list_title.add("接受过的");
		list_title.add("正在进行的");

		tabLayout = (TabLayout) findViewById(R.id.myTask_tabLayout);
		tabLayout.setTabMode(TabLayout.MODE_FIXED);
		for(String title : list_title)
			tabLayout.addTab(tabLayout.newTab().setText(title));

		adapter = new MyTaskTabAdapter(getSupportFragmentManager(), list_fragment, list_title);
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
	}

	public static void actionStart(Context context, int tabType)
	{
		Intent intent = new Intent(context, MyTaskActivity.class);
		intent.putExtra("tabType", tabType);
		context.startActivity(intent);
	}
}
