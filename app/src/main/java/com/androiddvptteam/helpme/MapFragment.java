package com.androiddvptteam.helpme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import static com.androiddvptteam.helpme.R.layout.abc_list_menu_item_icon;
import static com.androiddvptteam.helpme.R.layout.map_fragment;
import static com.androiddvptteam.helpme.R.layout.support_simple_spinner_dropdown_item;
import static com.baidu.mapapi.BMapManager.init;
import java.io.Serializable;

public class MapFragment extends BaseFragment
{
    public LocationClient mLocationClient;//定位客户端
    public MyLocationListener mMyLocationListener;  //定位监听器
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;//是否是第一次定位
    private volatile boolean isFristLocation = true;//初始化定位相关代码
    private TextureMapView mapview;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private TextView positionText;
    private BitmapDescriptor mIconLocation;
    private MyLocationConfiguration.LocationMode locationMode;
    private BitmapDescriptor mbitmap = BitmapDescriptorFactory.fromResource(R.drawable.dingwei);//图标
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        mLocationClient = new LocationClient(getContext().getApplicationContext());//获取全进程有效的context
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getContext().getApplicationContext());
        mapview = (TextureMapView) view.findViewById(R.id.bmapView);
        baiduMap = mapview.getMap();//获得地图实例
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(19).build()));//设置初始缩放比例

        positionText = (TextView)getActivity().findViewById(R.id.position_text_view);
        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[]permission = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(),permission,1);
        }else{
            requestLocation();
        }
        return view;
    }

    private void requestLocation()
    {//获取当前定位
        initMyLocation();
        mLocationClient.start();
    }

    private void stopRequestLocation()
    {
        mLocationClient.stop();
    }

    private void initMyLocation()
    {
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

//    private void navigateTo(BDLocation location)
//    {
//        if(isFirstLocate)
//        {//判断是否第一次定位，若是就执行以下代码
//            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//            baiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomTo(16f);
//            baiduMap.animateMapStatus(update);
//            isFirstLocate = false;
//        }
//        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
//        locationBuilder.latitude(location.getLatitude());
//        locationBuilder.longitude(location.getLongitude());
//        MyLocationData locationData = locationBuilder.build();
//        baiduMap.setMyLocationData(locationData);
//    }

    public class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onConnectHotSpotMessage(String s,int i) {}
//        @Override
//        public void onReceiveLocation(BDLocation location)
//        {
//            if(location.getLocType()==BDLocation.TypeNetWorkLocation||location.getLocType()==BDLocation.TypeGpsLocation)
//            {
//                navigateTo(location);
//            }else return;
//        }
        private boolean isFirstIn=true;
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
            double mLatitude= bdLocation.getLatitude();
            double mLongitude=bdLocation.getLongitude();
            MyLocationData data= new MyLocationData.Builder()
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
                    =new MyLocationConfiguration(locationMode,true,mIconLocation);
            //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
            baiduMap.setMyLocationConfigeration(configuration);
            //判断是否为第一次定位,是的话需要定位到用户当前位置
            if(isFirstIn)
            {
                //地理坐标基本数据结构
                LatLng latLng=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
                MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latLng);
                //改变地图状态
                baiduMap.setMapStatus(msu);
                isFirstIn=false;
                Toast.makeText(getContext(), bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true)
        {
            requestLocation();
        }
        else if(isVisibleToUser==false)
        {
            stopRequestLocation();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapview.onResume();
        initMyLocation();
    }
    @Override
    public void onPause(){
        super.onPause();
        mapview.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mLocationClient.stop();
        mapview.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

//    public void addInfosOverlay(final List<Info> infos)
//    {//加载覆盖物
//        baiduMap.clear();
//        LatLng latLng = null;
//        OverlayOptions overlayOptions = null;
//        Marker marker = null;//地图覆盖物
//        for (Info info : infos)
//        {//循环的将模拟数据标记
//            latLng = new LatLng(info.getLatitude(), info.getLongitude());//获取经纬度
//            overlayOptions = new MarkerOptions().position(latLng).icon(mbitmap).zIndex(5);//设置maker位置，图标，层级
//            marker = (Marker) (baiduMap.addOverlay(overlayOptions));
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("info", (Serializable) info);
//            marker.setExtraInfo(bundle);
//        }
//        // 将地图移到到最后一个经纬度位置
//        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
//        baiduMap.setMapStatus(update);
//    }
//
//    public void initmapEvent()
//    {//地图长按事件监听回调函数
//        //baiduMap.setOnMapLongClickListener(mlongclicklistener);
//        BaiduMap.OnMarkerClickListener mMarkerlis=new BaiduMap.OnMarkerClickListener()
//        {
//            @Override
//            public boolean onMarkerClick(Marker marker)
//            {
//                Info Info=(Info) marker.getExtraInfo().get("info");
//                InfoWindow mInfoWindow;//生成一个TextView用户在地图中显示InfoWindow
//                TextView location = new TextView(getActivity().getApplicationContext());
//                location.setBackgroundResource(R.drawable.coordinate);
//                location.setPadding(30, 20, 30, 50);
//                location.setText(Info.getName());//将marker所在的经纬度的信息转化成屏幕上的坐标
//                final LatLng ll = marker.getPosition();
//                Point p = baiduMap.getProjection().toScreenLocation(ll);
//                p.y -= 50;
//                LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
//                //为弹出的InfoWindow添加点击事件
//                mInfoWindow = new InfoWindow(location, llInfo, -47);
//                //显示InfoWindow
//                baiduMap.showInfoWindow(mInfoWindow);
//                return true;
//            }
//        };
//        baiduMap.setOnMarkerClickListener(mMarkerlis);
//    }
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private InfoWindow mInfoWindow;
    private BitmapDescriptor bdA;
    private BitmapDescriptor bdB;
    private BitmapDescriptor bdC;
    private BitmapDescriptor bdD;
    private BitmapDescriptor bd;
    private BitmapDescriptor bdGround;
    public void initOverlay() {
        //(LatLng表示坐标位置 第一个参数为维度，第一个参数为经度)
        LatLng llA = new LatLng(31.2345790211, 121.4129109701);
        LatLng llB = new LatLng(31.2328212311, 121.4134269199);
        LatLng llC = new LatLng(31.2397232311, 121.4131125541);
        LatLng llD = new LatLng(31.2369651233, 121.4143201394);
        //LatLng llText = new LatLng(39.86923, 116.397428);

        //这里是将图标转化为对象
        bdA = BitmapDescriptorFactory
                .fromResource(R.drawable.dingwei);
        bdB = BitmapDescriptorFactory
                .fromResource(R.drawable.dingwei);
        bdC = BitmapDescriptorFactory
                .fromResource(R.drawable.dingwei);
        bdD = BitmapDescriptorFactory
                .fromResource(R.drawable.dingwei);
        bd = BitmapDescriptorFactory
                .fromResource(R.drawable.dingwei);
        bdGround = BitmapDescriptorFactory
                .fromResource(R.drawable.dingwei);

        //定义四种不同类型的覆盖物
        OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(9).draggable(true);//OverlayOptions 地图覆盖物选型

        mMarkerA = (Marker) (baiduMap.addOverlay(ooA));//addOverlay在当前图层添加覆盖物对象
        OverlayOptions ooB = new MarkerOptions().position(llB).icon(bdB)
                .zIndex(5);
        mMarkerB = (Marker) (baiduMap.addOverlay(ooB));
        OverlayOptions ooC = new MarkerOptions().position(llC).icon(bdC)
                .perspective(false).zIndex(7);
        mMarkerC = (Marker) (baiduMap.addOverlay(ooC));
        //将A,B,C三种坐标添加到list中
        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bdA);
        giflist.add(bdB);
        giflist.add(bdC);
        OverlayOptions ooD = new MarkerOptions().position(llD).icons(giflist)
                .zIndex(0).period(10);//每隔10毫秒变动下标记(自v3.3.0版本起，SDK提供了给Marker增加动画的能力)
        mMarkerD = (Marker) (baiduMap.addOverlay(ooD));
        //构建文字Option对象，用于在地图上添加文字
//          OverlayOptions textOption = new TextOptions()
//              .bgColor(0xAAFFFF00)
//              .fontSize(24)
//              .fontColor(0xFFFF00FF)
//              .text("百度地图SDK")
//              .rotate(-30)
//              .position(llText);
//          //在地图上添加该文字对象并显示
//          mMarkerE = (Marker) (mBaiduMap.addOverlay(textOption));

        // add ground overlay
//        LatLng southwest = new LatLng(31.2345790214,121.4129109706);
//        LatLng northeast = new LatLng(31.2345790221,121.4129109501);
//        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
//                .include(southwest).build();
//
//        OverlayOptions ooGround = new GroundOverlayOptions()
//                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
//        baiduMap.addOverlay(ooGround);
//
//        //生成变化地图状态
//        MapStatusUpdate u = MapStatusUpdateFactory
//                .newLatLng(bounds.getCenter());//newLatLng设置地图新中心点
//        //设置地图状态
//        baiduMap.setMapStatus(u);
    }
    private void initOverlayListener() {
        //设置坐标点击事件
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
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getContext().getApplicationContext());
                button.setBackgroundResource(R.drawable.dingwei);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA || marker == mMarkerD) {
                    button.setText("更改位置");
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            LatLng ll = marker.getPosition();
                            LatLng llNew = new LatLng(ll.latitude + 0.005,
                                    ll.longitude + 0.005);//改变坐标的维度和经度
                            marker.setPosition(llNew);//设置坐标的位置
                            baiduMap.hideInfoWindow();//隐藏消息窗
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                   baiduMap.showInfoWindow(mInfoWindow);//显示消息窗
                } else if (marker == mMarkerB) {
                    button.setText("更改图标");
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.setIcon(bd);//改变坐标的图标
                            baiduMap.hideInfoWindow();//隐藏消息窗
                        }
                    });
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, ll, -47);//设置消息窗
                    baiduMap.showInfoWindow(mInfoWindow);//显示消息窗
                } else if (marker == mMarkerC) {
                    button.setText("删除");
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.remove();//删除坐标
                            baiduMap.hideInfoWindow();//隐藏消息窗
                        }
                    });
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, ll, -47);//设置消息窗
                    baiduMap.showInfoWindow(mInfoWindow);//显示消息窗
                }
                return true;
            }
        });

        //地图点击事件

    }
}
