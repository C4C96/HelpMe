package com.androiddvptteam.helpme;
public class PersonalInformation
{
    //只读的四个信息，只得在注册的时候填写，后期不允许修改。！不考虑改名变性转系等情况 ！
    private String userName="";//姓名
    private String schooleNumber="";//学号
    private String gender="";//性别
    private String departmentName="";//所在院系

    //允许后期用户自己修改
    public String introduction="";//个人简介

    public PersonalInformation(String name,String number,String gender,String department,String introduction)
    {
        this.userName=name;
        this.schooleNumber=number;
        this.gender=gender;
        this.departmentName=department;
        this.introduction=introduction;

        //向数据库写入用户信息
        //代码放这里
    }

    public String getUserName()
    {
        return this.userName;
    }

    public String getSchooleNumber()
    {
        return this.schooleNumber;
    }

    public String getGender()
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

    //用户修改自己的简介
    public void setIntroduction(String introduction)
    {
        this.introduction=introduction;
    }
}
