package com.androiddvptteam.helpme;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.androiddvptteam.helpme.Connection.LoginConnection;
import com.androiddvptteam.helpme.Connection.MyMissionConnection;
import com.androiddvptteam.helpme.Connection.ReceiveConnection;
import com.androiddvptteam.helpme.Connection.ReleaseConnection;
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
import java.net.URL;
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

	private SharedPreferences preferences;				//默认的SharedPreferences

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
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Config.init(preferences);
		personalInformation = null;
		loadLocalAvatar();
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
		//调试数据
		myMissions = new ArrayList<>();
		PersonalInformation tmp = new PersonalInformation("Name", "233", 0, "ABC", "");
		myMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		myMissions.add(new Mission("Title1", "Content1", 3, 2, 2, personalInformation, Calendar.getInstance()));
		myMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		myMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		myMissions.add(new Mission("Title1", "Content1", 3, 2, 2, personalInformation, Calendar.getInstance()));
		myMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		myMissions.add(new Mission("Title1", "Content1", 3, 2, 2, personalInformation, Calendar.getInstance()));
		myMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		if (true)
		{
			return true;
		}

		MyMissionConnection connection;
		boolean result=true;
		try
		{
			connection=new MyMissionConnection(new URL("http://123.206.125.166:8080/AndroidServlet/MyMissionServlet"));
			connection.setPublisher(personalInformation);
			connection.connect();

			myMissions=connection.getList();

			if(connection.connectionResult)
				result=true;
			else//链接服务器失败
			{
				result = false;
			}
			if(!connection.listResult)
				result=false;//此时没有找到这个人发布的信息
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 可以接收的任务
	 * 刷新foundMissions，可以先写一些测试信息
	 * 执行之后对foundMissions重新赋值，使其内容为最新的找到可以接的任务
	 * 若失败则保持foundMissions不变，但不能是null，若是null则给一个空集合
	 * @return 是否成功
	 * */
	public boolean refreshFoundMissions()
	{
		//调试数据
		foundMissions = new ArrayList<>();
		PersonalInformation tmp = new PersonalInformation("Name", "233", 0, "ABC", "");
		foundMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		foundMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		foundMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		foundMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		foundMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		foundMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		foundMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		foundMissions.add(new Mission("Title1", "Content1", 3, 2, 2, tmp, Calendar.getInstance()));
		if (true)
		{
			return true;
		}
		MyMissionConnection connection;
		boolean result=true;
		try
		{
			connection=new MyMissionConnection(new URL("http://123.206.125.166:8080/AndroidServlet/MyMissionServlet"));
			connection.setPublisher(personalInformation);
			connection.connect();

			myMissions=connection.getList();

			if(connection.connectionResult)
				result=true;
			else//链接服务器失败
			{
				result = false;
			}
			if(!connection.listResult)
				result=false;//此时没有任何可以接收的任务
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
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
		LoginConnection connection;
		boolean resultForConnection=true;//连接结果
		boolean resultForLogin=true;//登陆结果

		try
		{
			android.util.Log.d("ID",id);
			connection=new LoginConnection(new URL("http://123.206.125.166:8080/AndroidServlet/LoginServlet"));
			//connection=new LoginConnection(new URL("http://192.168.0.3:8080/AndroidServlet/LoginServlet"));
			connection.setAttributes(id,password);
			connection.connect();
			//判断登陆结果
			if(connection.getResult().equals("success"))//连接成功
			{
				personalInformation = connection.getPersonalInformation();
				preferences.edit().putString("UserName", personalInformation.getUserName()).
						putString("UserSchoolNumber", personalInformation.getSchoolNumber()).
						putInt("UserGender", personalInformation.getGender()).
						putString("UserDepartment", personalInformation.getDepartmentName()).
						putString("UserIntroduction", personalInformation.getIntroduction()).
						apply();
			}
			else
				resultForLogin=false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return resultForLogin;
	}

	/**
	 * 登出，该删的都删了
	 * */
	public void logout()
	{
		personalInformation = null;
		avatar = null;
		myMissions = new ArrayList<>();
		foundMissions =  new ArrayList<>();
		deleteLocalAvatar();
		startActivity(new Intent(this, LoginActivity.class));
	}



	//get方法

	public PersonalInformation getPersonalInformation() { return personalInformation; }

	public Bitmap getAvatar(){return avatar;}


	/**
	 * 设置头像
	 * @param 	avatar		新头像
	 * @return 				上传是否成功
	 * */
	public boolean setAvatar(Bitmap avatar)
	{
		if (avatar == null) return false;
		netDelay(1000);
		//if (上传失败)
		//	return false;

		//显示的更新
		this.avatar = avatar;
		//更新本地缓存
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
		return true;
	}

	/**
	 * 设置个人简介
	 * @param 	introduction		新简介
	 * @return 						上传是否成功
	 * */
	public boolean setIntroduction(String introduction)
	{
		if (introduction == null) return false;
		netDelay(1000);
		//if (上传失败)
		//	return false;

		personalInformation.setIntroduction(introduction);
		return true;
	}
}
