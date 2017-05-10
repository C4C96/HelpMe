package com.androiddvptteam.helpme;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import com.androiddvptteam.helpme.MissionAttribute.MissionAttribute;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 存储全局信息的类
 * */
public class MyApplication extends Application
{
	private PersonalInformation personalInformation;	//当前的用户信息
	private Bitmap avatar;								//当前用户头像
	public Config config;								//当前设置
	public List<Mission> myMissions;					//我的任务（全部种类）
	public List<Mission> foundMissions;					//发现的任务

	//百度地图需要
	private MapView mapview;
	private BaiduMap baiduMap;

	@Override
	public void onCreate()
	{
		super.onCreate();

		//百度地图需要
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		//自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
		//包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
		SDKInitializer.setCoordType(CoordType.BD09LL);

		init();
	}

	/**
	 * 初始化信息
	 * */
	private void init()
	{
		personalInformation = null;
		loadLocalAvatar();
		//config = new Config()
	}

	/**
	 * 读取本地的用户头像信息
	 * */
	private void loadLocalAvatar()
	{
		avatar = null;
		try
		{
			File f = new File(getFilesDir(), "avatar.jpg");
			if (f.exists())
			{
				FileInputStream fis = new FileInputStream(f);
				avatar = BitmapFactory.decodeStream(fis);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 删除本地的用户头像信息
	 * */
	private void deleteLocalAvatar()
	{
		File f = new File(getFilesDir(), "avatar.jpg");
		if (f.exists())
		{
			f.delete();
		}
	}

	/**
	 * 刷新mMissions，可以先写一些测试信息
	 * */
	public void refreshMyMissions()
	{
		//模拟延迟
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		//myMissions = new ArrayList<>();
		//myMissions.add()
	}

	/**
	 * 刷新foundMissions，可以先写一些测试信息
	 * */
	public void refreshFoundMissions()
	{
		//模拟延迟
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		//foundMissions = new ArrayList<>();
		//foundMissions.add()
	}

	/**
	 * 登录，若登录成功，则会对personalInformation赋值
	 * @param id			用户ID
	 * @param password  	密码
	 * @return 是否登录成功
	 * */
	public boolean login(String id, String password)
	{
		if (id == null || id.equals("") || password == null || password.equals("")) return false;
		//模拟延迟
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		personalInformation = new PersonalInformation("姜宁康", "23333", MissionAttribute.GENDER_MALE, "计软", "114514");
		return true;
	}

	/**
	 * 登出，该删的都删了
	 * */
	public void logout()
	{
		personalInformation = null;
		avatar = null;
		config = null;
		myMissions = null;
		foundMissions = null;
		deleteLocalAvatar();
	}

	//get方法

	public PersonalInformation getPersonalInformation() { return personalInformation; }

	public Bitmap getAvatar(){return avatar;}


	//set方法

	public void setAvatar(Bitmap avatar)
	{
		this.avatar = avatar;
		//存储图片
		File f = new File(getFilesDir(), "avatar.jpg");
		if (f.exists())
			f.delete();
		try
		{
			FileOutputStream out = new FileOutputStream(f);
			avatar.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.i(TAG, "New avatar has been saved.");
		}
		catch (Exception e)
		{
		   e.printStackTrace();
		}
	}
}
