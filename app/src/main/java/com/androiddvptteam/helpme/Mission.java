package com.androiddvptteam.helpme;

import java.util.GregorianCalendar;

public class Mission
{
    //任务标题
    public String title="";
    //任务内容
    public String content="";

    //任务的属性，前端下拉列表获取到用户选择的值以后，把值分别传给这三个属性
    //性别
    private int gender=0;
    //标签
    private int attribute=0;
    //范围
    private int range=0;

    //任务时限（用户设置年、月、日、时、分）
    private GregorianCalendar endTime=new GregorianCalendar();

    //任务是否已经开始，false代表没有开始
    public boolean hasStarted=false;
    //任务是否已经完成，false代表没有完成
    public boolean hasFinished=false;
    //若任务正在进行中，则hasStarted=true，hasFinished=false
    //任务是否进入了草稿箱，false代表没有存入草稿
    public boolean draft=false;
    //任务是否被取消，false代表没有被取消
    public boolean hasCanceled=false;


    //设置任务的标题和内容
    public Mission(String title,String content)
    {
        this.title=title;
        this.content=content;
        //将标题和内容写进数据库
    }

    //设置任务的三个属性
    public void setMissionAttribute(int gender,int attribute,int range)
    {
        this.gender=gender;
        this.attribute=attribute;
        this.range=range;
        //将属性写进数据库
    }

    public int getGender()
    {
        return this.gender;
    }

    public int getAttribute()
    {
        return this.attribute;
    }

    public int getRange()
    {
        return this.range;
    }

    //读取任务时间
    public void setEndTime(int year,int month,int day,int hour,int minute)
    {
        endTime.set(year,month,day,hour,minute);
        //将属性写进数据库，由于mysql不支持GregorianCalendar，因此需要将年月日时分按int形式存储
    }

    //返回任务时间，由其他函数进行时间加减
    public GregorianCalendar getEndTime()
    {
        //从数据库中读出时间，然后调用set赋值给endTime，再返回
        return endTime;
    }
}
