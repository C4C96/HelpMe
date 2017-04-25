package com.androiddvptteam.helpme;

import android.app.Application;

import com.androiddvptteam.helpme.MissionAttribute.MissionAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储全局信息的类
 * */
public class MyApplication extends Application
{
	public PersonalInformation personalInformation;		//当前的用户信息
	public Config config;								//当前设置
	public List<Mission> myMissions;					//我的任务（全部种类）
	public List<Mission> foundMissions;					//发现的任务

	@Override
	public void onCreate()
	{
		super.onCreate();
		//添加测试用数据
		personalInformation = new PersonalInformation("姜宁康", "23333", MissionAttribute.GENDER_MALE, "计软", "114514");
		//config = new Config()
		//myMissions = new ArrayList<>();
		//myMissions.add()
		//foundMissions = new ArrayList<>();
		//foundMissions.add()
	}
}
