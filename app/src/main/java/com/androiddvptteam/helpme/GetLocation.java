package com.androiddvptteam.helpme;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13759 on 2017/6/10.
 */

public class GetLocation
{
    private double latitude=0.0;
    private double longitude =0.0;
    public LocationClient mLocationClient;//定位客户端
    public MyLocationListener mMyLocationListener;  //定位监听器

    public GetLocation(Context context)
    {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requestLocation(context);
    }

    private void requestLocation(Context context)
    {//获取当前定位
        initMyLocation(context);
        mLocationClient.start();
    }

    private void initMyLocation(Context context)
    {
        mLocationClient = new LocationClient(context);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);// 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//返回定位结果是百度经纬度
        option.setScanSpan(1);//多长时间进行一次请求
        option.setOpenGps(true);//打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);//位置
        mLocationClient.setLocOption(option);//使用设置
    }

   public class MyLocationListener implements BDLocationListener
   {
       @Override
       public void onConnectHotSpotMessage(String s, int i){}

       public void onReceiveLocation(BDLocation bdLocation)
       {
           setLatitude(bdLocation.getLatitude());
           setLongitude(bdLocation.getLatitude());
       }
   }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }
}
