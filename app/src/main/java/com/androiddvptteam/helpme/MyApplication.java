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
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.androiddvptteam.helpme.Tools.netDelay;

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
		myMissions = new ArrayList<>();
		foundMissions = new ArrayList<>();
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
	 * 刷新myMissions，可以先写一些测试信息
	 * 执行之后对myMissions重新复制，使其内容为最新的与我有关的任务（我发布的，接收的，正在做的）
	 * 若失败则保持myMissions不变，但不能是null，若是null则给一个空集合
	 * @return 是否成功
	 * */
	public boolean refreshMyMissions()
	{
		netDelay(1000);
		//myMissions = new ArrayList<>();
		//myMissions.add()
		return true;
	}

	/**
	 * 刷新foundMissions，可以先写一些测试信息
	 * 执行之后对foundMissions重新赋值，使其内容为最新的找到可以接的任务
	 * 若失败则保持foundMissions不变，但不能是null，若是null则给一个空集合
	 * @return 是否成功
	 * */
	public boolean refreshFoundMissions()
	{
		netDelay(1000);
		//PersonalInformation p=new PersonalInformation("Jiang nin kang","1111111111",1,"Ruan jian gong chen","wo shi kawaii jiang nin kang.");
		//Calendar c=Calendar.getInstance();
		//Mission m1=new Mission("Fuck me","Come to fuck fuck me",p,c);
		//m1.setMissionAttribute(1,2,1);
		//foundMissions=new ArrayList<>();
		//foundMissions.add(m1);
		return true;
	}

	/**
	 * 登录，若登录成功，则会对personalInformation赋值
	 * @param id			用户ID
	 * @param password  	密码
	 * @return 是否登录成功
	 * */
	public boolean login(String id, String password)
	{
		if (id == null || id.equals("") || password == null || password.equals(""))
			return false;
		netDelay(1000);
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
