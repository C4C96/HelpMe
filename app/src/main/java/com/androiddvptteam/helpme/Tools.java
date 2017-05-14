package com.androiddvptteam.helpme;

import android.graphics.Bitmap;

/**
 * 工具类，放各种静态工具函数
 */
public class Tools
{
	/**
	 * 从后端获取用户头像
	 * @param id	用户ID
	 * @return 		头像的位图
	 * */
	public static Bitmap getUserAvatarFromWeb(String id)
	{
		netDelay(1000);
		return null;
	}

	/**
	 * 加密函数
	 * */
	public static String encrypt(String str)
	{
		return str;
	}

	/**
	 * 解密函数
	 * */
	public static String decrypt(String str)
	{
		return str;
	}

	/**
	 * 模拟网络延迟的函数，最终所有延迟的地方都会被真实的网络操作代替
	 * 所以涉及到这个函数（以后就是网络操作）的操作都必须异步执行
	 * @param	ms	模拟延迟的毫秒数
	 * */
	public static void netDelay(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
