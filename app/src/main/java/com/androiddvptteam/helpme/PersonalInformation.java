package com.androiddvptteam.helpme;

public class PersonalInformation
{
    private String userName;        //姓名
    private String schoolNumber;    //学号
    private int gender;             //性别
    private String departmentName;  //所在院系
    private String introduction;    //个人简介

    public PersonalInformation(String name, String number, int gender, String department, String introduction)
    {
        this.userName = name;
        this.schoolNumber = number;
        this.gender = gender;
        this.departmentName = department;
        this.introduction = introduction;
    }


    //get方法

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


	//set方法

    public void setIntroduction(String introduction) { this.introduction=introduction; }

}
