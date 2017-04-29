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
	public Config config;								//当前设置
	public List<Mission> myMissions;					//我的任务（全部种类）
	public List<Mission> foundMissions;					//发现的任务

	@Override
	public void onCreate()
	{
		super.onCreate();
		init();
	}

	private void init()
	{
		//读取本地头像图片
		Bitmap avatar = null;
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
		personalInformation = new PersonalInformation("姜宁康", "23333", MissionAttribute.GENDER_MALE, "计软", "114514", avatar);
		//config = new Config()
		myMissions = new ArrayList<>();
		for(int i=0;i<3;i++)
		{
			Mission m1=new Mission("give me some water","...");
			m1.setMissionAttribute(1,2,1);
			myMissions.add(m1);
			Mission m2=new Mission("give me some shit","...");
			m2.setMissionAttribute(2,3,1);
			myMissions.add(m2);
			Mission m3=new Mission("give me some food","...");
			m3.setMissionAttribute(3,1,1);
			myMissions.add(m3);
			Mission m4=new Mission("give me some paper","...");
			m4.setMissionAttribute(1,2,3);
			myMissions.add(m4);
			Mission m5=new Mission("give me some money","...");
			m5.setMissionAttribute(2,2,2);
			myMissions.add(m5);
		}
		//foundMissions = new ArrayList<>();
		//foundMissions.add()
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

	public String getUserName()
	{
		return personalInformation.getUserName();
	}

	public String getUserIntroduction()
	{
		return personalInformation.getIntroduction();
	}

	public String getUserDepartmentName()
	{
		return personalInformation.getDepartmentName();
	}

	public int getUserGender()
	{
		return personalInformation.getGender();
	}

	public String getUserSchoolNumber(){return personalInformation.getSchoolNumber();}

	public Bitmap getUserAvatar(){return personalInformation.getAvatar();}

	public void setUserIntroduction(String introduction){personalInformation.setIntroduction(introduction);}

	public void setUserAvatar(Bitmap avatar)
	{
		personalInformation.setAvatar(avatar);
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
			Log.i(TAG, "已经保存");
		}
		catch (Exception e)
		{
		   e.printStackTrace();
		}
	}
}
