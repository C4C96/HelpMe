package com.androiddvptteam.helpme;

import java.util.Calendar;

public class Message
{
    private String title;//消息标题
    private String content;//消息内容
    private int imageId;//图片
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;

    public Message()
    {
    }

    public Message(int imageId,
                   String title,
                   String content,
                   String year,
                   String month,
                   String day,
                   String hour,
                   String minute,
                   String second)
    {
        this.imageId = imageId;
        this.title = title;
        this.content = content;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;

    }

    public String getMessageTitle()
    {
        return title;
    }

    public String getMessageContent()
    {
        return content;
    }

    public String getMessageTime()
    {
        String time = year + "-" + month + "-" + day + "  " + hour + ":" + minute + ":" + second;
        return time;
    }

    public int getImageId()
    {
        return imageId;
    }

    public String getYear()
    {
        return year;
    }

    public String getMonth()
    {
        return month;
    }

    public String getDay()
    {
        return day;
    }

    public String getHour()
    {
        return hour;
    }

    public String getMinute()
    {
        return minute;
    }

    public String getSecond()
    {
        return second;
    }
}
