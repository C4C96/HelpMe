package com.androiddvptteam.helpme;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class GetLocation
{
    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    private double latitude = 0.0;
    private double longitude = 0.0;

    public GetLocation(Context context)
    {
        mLocationClient = new LocationClient(context);
        mBDLocationListener = new MyBDLocationListener();
        mLocationClient.registerLocationListener(mBDLocationListener);
        getLocation();
    }

    public void getLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();
        BDLocation bd = mLocationClient.getLastKnownLocation();
        while (bd == null)
            bd = mLocationClient.getLastKnownLocation();
        setLatitude(bd.getLatitude());
        setLongitude(bd.getLongitude());
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public class MyBDLocationListener implements BDLocationListener
    {
        private double latitude = 0.0;
        private double longitude = 0.0;

        public MyBDLocationListener()
        {
        }

        public void onConnectHotSpotMessage(String s, int i)
        {
        }

        public void onReceiveLocation(BDLocation location)
        {
            // 非空判断
            if (location != null)
            {
                // 根据BDLocation 对象获得经纬度以及详细地址信息
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if (mLocationClient.isStarted())
                    mLocationClient.stop();
            }
        }
    }
}
