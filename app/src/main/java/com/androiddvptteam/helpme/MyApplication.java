package com.androiddvptteam.helpme;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.androiddvptteam.helpme.MissionAttribute.MissionAttribute;

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

	@Override
	public void onCreate()
	{
		super.onCreate();
		init();
	}

	/**
	 * 初始化信息，可以先在这里加入测试需要数据
	 * */
	private void init()
	{
		loadLocalAvatar();

		personalInformation = new PersonalInformation("姜宁康", "23333", MissionAttribute.GENDER_MALE, "计软", "114514");
		//config = new Config()
		//myMissions = new ArrayList<>();
		//myMissions.add()
		//foundMissions = new ArrayList<>();
		//foundMissions.add()
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
	 * 加密函数
	 * */
	private void encrypt(String str)
	{

	}

	/**
	 * 解密函数
	 * */
	private void decrypt(String str)
	{

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
