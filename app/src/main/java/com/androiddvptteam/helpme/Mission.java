package com.androiddvptteam.helpme;

import android.support.annotation.NonNull;

import com.androiddvptteam.helpme.MissionAttribute.MissionAttribute;

import java.sql.Time;
import java.util.Calendar;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Mission
{
    //任务的状态枚举
    public static int STATE_UNRECEIVED = 0; //未接收
    public static int STATE_DOING = 1;      //进行中
    public static int STATE_FINISHED = 2;   //已完成
    public static int STATE_CANCELED = 3;   //已取消

    private String ID;                      //任务的ID，具有唯一性

    private String title;                   //任务标题
    private String content;                 //任务内容

    private PersonalInformation publisher;  //发布者信息
    private PersonalInformation recipient;  //接收者信息，未接收则为null

    //任务的限制属性属性
    private int gender = MissionAttribute.GENDER_IDONTCARE;      //性别
    private int attribute = MissionAttribute.ATTRIBUTE_OTHER;    //标签
    private int range = MissionAttribute.RANGE0;                 //范围

    private Calendar createTime;                //任务创建时间
    private Calendar receiveTime;               //任务被接收时间，未被接收则为null
    private Calendar finishTime;                //任务完成的时间，未完成则为null
    private Calendar cancelTime;                //任务取消的时间，未取消则为null
    private int state = STATE_UNRECEIVED;   //任务的状态

    /**
     * 新建任务时用的构造函数
     * */
    public Mission(@NonNull String              title,
                   @NonNull String              content,
                   @NonNull PersonalInformation publisher,
                   @NonNull Calendar            createTime)
    {
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.createTime = createTime;
        this.ID = generateID();
    }

	/**
     * 生成一条任务（比如说服务器上拉下来的）的实例时用的构造函数
     * _(:з)∠)_长得恶心的参数列表，不过估计也就我需要用了，所以不写约束了。——CC
     * */
    public Mission(String              ID,
                   String              title,
                   String              content,
                   PersonalInformation publisher,
                   PersonalInformation recipient,
                   int                 gender,
                   int                 attribute,
                   int                 range,
                   Calendar            createTime,
                   Calendar            receiveTime,
                   Calendar            finishTime,
                   Calendar            cancelTime,
                   int                 state)
    {
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.recipient = recipient;
        this.gender = gender;
        this.attribute = attribute;
        this.range = range;
        this.createTime = createTime;
        this.receiveTime = receiveTime;
        this.finishTime = finishTime;
        this.cancelTime = cancelTime;
        this.state = state;
    }

    /**
     * 生成唯一的ID（正常情况下不会冲突）
     * 由发布者学号+发布时间(距1970年1月1日0时的毫秒数)组成
     *    (11位)+(MAX_LONG = 9223372036854775807，最多19位，若小于19位，无前导零)
     * （你同一毫秒发两个任务算我输，好吧）
     * */
    private String generateID()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(publisher.getSchoolNumber());
        sb.append(createTime.getTime().getTime());
        return sb.toString();
    }

    /**
     * 接收该任务
     * @param recipient     接收者的信息
     * @param receivedTime  接收的时间
     * @return              是否成功,若失败，则表明该任务已被接收或已完成或已取消
     * */
    public boolean receive(@NonNull PersonalInformation recipient,
                           @NonNull Calendar            receivedTime)
    {
        if (state == STATE_UNRECEIVED)
        {
            state = STATE_DOING;
            this.recipient = recipient;
            this.receiveTime = receivedTime;
            return true;
        }
        else
            return false;
    }

    /**
     * 完成该任务
     * @param finishTime    完成的时间
     * @return              是否成功,若失败，则表明该任务未被接受或已完成或已取消
     * */
    public boolean finish(@NonNull Calendar finishTime)
    {
        if (state == STATE_DOING)
        {
            state = STATE_FINISHED;
            this.finishTime = finishTime;
            return true;
        }
        else
            return false;
    }

    /**
     * 发布者取消任务
     * @param cancelTime    取消的时间
     * @return              是否成功，若失败则表明该任务已完成或已取消
     * */
    public boolean cancel(@NonNull Calendar cancelTime)
    {
        if (state == STATE_UNRECEIVED || state == STATE_DOING )
        {
            state = STATE_CANCELED;
            this.cancelTime = cancelTime;
            return true;
        }
        else
            return false;
    }

    /**
     * 接收者放弃接收任务
     * @return      是否成功，若失败则表明该任务未被接收或已完成或已取消
     * */
    public boolean abandon()
    {
        if (state == STATE_DOING)
        {
            state = STATE_UNRECEIVED;
            this.recipient = null;
            this.receiveTime = null;
            return true;
        }
        else
            return false;
    }


    //get方法

    public String getID() { return this.ID; }

    public String getTitle() { return this.title; }

    public String getContent() { return this.content; }

    public PersonalInformation getPublisher() { return this.publisher; }

    public PersonalInformation getRecipient() { return this.recipient; }

    public int getGender() { return this.gender; }

    public int getAttribute() { return this.attribute; }

    public int getRange() { return this.range; }

    public int getState() { return this.state; }

    public Calendar getCreateTime() { return this.createTime; }

    public Calendar getReceivedTime() { return this.receiveTime; }


    //set方法


    //设置任务的三个属性
    public void setMissionAttribute(int gender,int attribute,int range)
    {
        this.gender=gender;
        this.attribute=attribute;
        this.range=range;
    }
}
