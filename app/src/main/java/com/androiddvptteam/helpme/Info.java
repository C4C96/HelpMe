package com.androiddvptteam.helpme;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Created by 尤豪谦 on 2017/5/14.从服务器返回的数据部分，我们需要转换为实体集合
 */

public class Info
{
    private static final long serialVersionUID = -758459502806858414L;//精度
    private double latitude;
    private double longitude;
    private int imgId;//图标
    private String name;//用户名
    private String distance;//距离
    public static List<Info> infos = new ArrayList<Info>();
    static
    {
        infos.add(new Info(34.242652, 108.971171,R.drawable.coordinate , "jnk",
                "距离209米"));
        infos.add(new Info(34.242952, 108.972171,R.drawable.coordinate , "hjf",
                "距离897米"));
        infos.add(new Info(34.242852, 108.973171,R.drawable.coordinate , "cly",
                "距离249米"));
        infos.add(new Info(34.242152, 108.971971, R.drawable.coordinate, "cnm",
                "距离679米"));
    }//模拟数据库信息

    public Info() {}

    public Info(double latitude, double longitude, int imgId, String name, String distance)
    {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgId = imgId;
        this.name = name;
        this.distance = distance;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public String getName()
    {
        return name;
    }

    public int getImgId()
    {
        return imgId;
    }

    public void setImgId(int imgId)
    {
        this.imgId = imgId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDistance()
    {
        return distance;
    }

    public void setDistance(String distance)
    {
        this.distance = distance;
    }

}

