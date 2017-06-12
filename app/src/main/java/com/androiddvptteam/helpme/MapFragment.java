package com.androiddvptteam.helpme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MapFragment extends BaseFragment {
    public LocationClient mLocationClient;//定位客户端
    public MyLocationListener mMyLocationListener;  //定位监听器
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;//是否是第一次定位
    private volatile boolean isFristLocation = true;//初始化定位相关代码
    private TextureMapView mapview;
    private BaiduMap baiduMap;
    private ImageButton refresh;
    private boolean isFirstLocate = true;
    private TextView positionText;
    private BitmapDescriptor mIconLocation;
    private MyLocationConfiguration.LocationMode locationMode;
    private BitmapDescriptor mbitmap = BitmapDescriptorFactory.fromResource(R.drawable.dingwei);//图标
    private Marker mMarker;

    private InfoWindow mInfoWindow;

    List<Mission> mission=new LinkedList<>();
    List<LatLng> points = new LinkedList<>();//将所有有的任务的经纬度加载
    List<OverlayOptions> oo = new LinkedList<>();//定义覆盖物

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        MyApplication myApplication=(MyApplication)getActivity().getApplication();
        mission=myApplication.foundMissions;
        System.out.println("这里是地图");
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        mLocationClient = new LocationClient(getContext().getApplicationContext());//获取全进程有效的context
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getContext().getApplicationContext());
        mapview = (TextureMapView) view.findViewById(R.id.bmapView);
        baiduMap = mapview.getMap();//获得地图实例
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(19).build()));//设置初始缩放比例
        refresh = (ImageButton)view.findViewById(R.id.refresh);
        positionText = (TextView) getActivity().findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permission = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permission, 1);
        }
        else
            {
            requestLocation();
        }
        //绑定匿名的监听器，并执行您所要在点击按钮后执行的逻辑代码
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                requestLocation();
//                Toast.makeText(getActivity().getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
         });

        if(null!=mission&&mission.size()!=0)
        {
            initOverlay(mission);
            initOverlayListener(mission);
        }
        return view;
    }

    private void requestLocation() {//获取当前定位
        initMyLocation();
        mLocationClient.start();
    }

    private void stopRequestLocation() {
        mLocationClient.stop();
    }

    private void initMyLocation() {
        mLocationClient = new LocationClient(getContext());
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
        public void onConnectHotSpotMessage(String s, int i) {
        }

        private boolean isFirstIn = true;

        //定位请求回调函数,这里面会得到定位信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //BDLocation 回调的百度坐标类，内部封装了如经纬度、半径等属性信息
            //MyLocationData 定位数据,定位数据建造器
            /*
            * 可以通过BDLocation配置如下参数
            * 1.accuracy 定位精度
            * 2.latitude 百度纬度坐标
            * 3.longitude 百度经度坐标
            * 4.satellitesNum GPS定位时卫星数目 getSatelliteNumber() gps定位结果时，获取gps锁定用的卫星数
            * 5.speed GPS定位时速度 getSpeed()获取速度，仅gps定位结果时有速度信息，单位公里/小时，默认值0.0f
            * 6.direction GPS定位时方向角度
            * */
            double mLatitude = bdLocation.getLatitude();
            double mLongitude = bdLocation.getLongitude();
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())//getRadius 获取定位精度,默认值0.0f
                    .latitude(mLatitude)//百度纬度坐标
                    .longitude(mLongitude)//百度经度坐标
                    .build();
            //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
            baiduMap.setMyLocationData(data);
            baiduMap.setMyLocationEnabled(true);
            //配置定位图层显示方式,三个参数的构造器
            /*
            * 1.定位图层显示模式
            * 2.是否允许显示方向信息
            * 3.用户自定义定位图标
            *
            * */
            MyLocationConfiguration configuration
                    = new MyLocationConfiguration(locationMode, true, mIconLocation);
            //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
            baiduMap.setMyLocationConfigeration(configuration);
            //判断是否为第一次定位,是的话需要定位到用户当前位置
            if (isFirstIn) {
                //地理坐标基本数据结构
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //改变地图状态
                baiduMap.setMapStatus(msu);
                isFirstIn = false;
                Toast.makeText(getContext(), bdLocation.getAddrStr(), LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
            requestLocation();
        } else if (isVisibleToUser == false) {
            stopRequestLocation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
        initMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapview.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        mission.clear();
        points.clear();
        oo.clear();
    }

    public void initOverlay( List<Mission> mission) {
        //(LatLng表示坐标位置 第一个参数为维度，第一个参数为经度)
        for (int i = 0; i <mission.size(); i++)
        {
            System.out.println("嘿嘿嘿"+mission.get(i).getLatitude());
            LatLng ll = new LatLng(mission.get(i).getLatitude(), mission.get(i).getLongitude());
            points.add(ll);
        }
            for (int i = 0; i <points.size(); i++)
            {
                OverlayOptions marker = new MarkerOptions().position(points.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.dingwei)).zIndex(9).draggable(false);
                oo.add(marker);
            }

        for (int i = 0; i <oo.size(); i++) {//在图层上添加覆盖物
            mMarker = (Marker) (baiduMap.addOverlay(oo.get(i)));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info",mission.get(i));
            mMarker.setExtraInfo(bundle);//将bundle值传入marker中，给baiduMap设置监听时可以得到它
        }
    }

    private void initOverlayListener( List<Mission> mission) {
//        //设置坐标点击事件
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                baiduMap.hideInfoWindow();
            }
        });
        if (oo != null) {
            int i = 0;
            for (i = 0; i < oo.size(); i++)
            {
                baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
                {
                    public boolean onMarkerClick(final Marker marker)
                    {
                        final Mission m=(Mission) marker.getExtraInfo().get("info");
                        Button button = new Button(getContext().getApplicationContext());
                        button.setBackgroundResource(R.drawable.kuang1);
                        InfoWindow.OnInfoWindowClickListener listener = null;
                        button.setText(m.getTitle());
                        listener = new InfoWindow.OnInfoWindowClickListener()
                        {
                            public void onInfoWindowClick()
                            {
                                MissionDetail.actionStart(getContext(),m);
                            }
                        };
                        LatLng ll = marker.getPosition();
                        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                        baiduMap.showInfoWindow(mInfoWindow);//显示消息窗
                        return true;
                    }
                });
            }
        }
    }
}
