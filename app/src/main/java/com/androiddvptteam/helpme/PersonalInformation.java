package com.androiddvptteam.helpme;

import android.graphics.Bitmap;

public class PersonalInformation
{
    //只读的四个信息，只得在注册的时候填写，后期不允许修改。！不考虑改名变性转系等情况 ！
    private String userName;//姓名
    private String schoolNumber;//学号
    private int gender;//性别
    private String departmentName;//所在院系
    private Bitmap avatar;//头像,null表示默认头像

    //允许后期用户自己修改
    public String introduction;//个人简介

    public PersonalInformation(String name, String number, int gender, String department, String introduction, Bitmap avatar)
    {
        this.userName = name;
        this.schoolNumber = number;
        this.gender = gender;
        this.departmentName = department;
        this.introduction = introduction;
        this.avatar = avatar;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public String getSchoolNumber()
    {
        return this.schoolNumber;
    }

    public int getGender()
    {
        return this.gender;
    }

    public String getDepartmentName()
    {
        return this.departmentName;
    }

    public String getIntroduction()
    {
        return this.introduction;
    }

    public Bitmap getAvatar(){return avatar;}

    //用户修改自己的简介
    public void setIntroduction(String introduction)
    {
        this.introduction=introduction;
    }

    public void setAvatar(Bitmap avatar){this.avatar = avatar;}
}
