package com.androiddvptteam.helpme;

import android.content.SharedPreferences;

/**
 * 存储设定的类，都是静态方法
 * 使用前必须调用init来进行初始化（虽然MyApplication类里已经做了）
 */
public class Config
{
    private static SharedPreferences preferences;    //默认的SharedPreferences

    //用一个SharedPreferences实例来初始化
    public static void init(SharedPreferences p)
    {
        if (preferences == null)
            preferences = p;
    }

    //get方法

    //消息提醒
    public static boolean getNotificationConfig()
    {
        return preferences.getBoolean("NotificationConfig", true);
    }

    //声音
    public static boolean getSoundConfig()
    {
        return preferences.getBoolean("SoundConfig", true);
    }

    //震动
    public static boolean getVibrationConfig()
    {
        return preferences.getBoolean("VibrationConfig", true);
    }

    //set方法

    //消息提醒
    public static void setNotificationConfig(boolean notificationConfig)
    {
        preferences.edit().putBoolean("NotificationConfig", notificationConfig).apply();
    }

    //声音
    public static void setSoundConfig(boolean soundConfig)
    {
        preferences.edit().putBoolean("SoundConfig", soundConfig).apply();
    }

    //震动
    public static void setVibrationConfig(boolean vibrationConfig)
    {
        preferences.edit().putBoolean("VibrationConfig", vibrationConfig).apply();
    }
}
