package com.androiddvptteam.helpme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Icon;
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

import java.util.ArrayList;
import java.util.List;

import static com.androiddvptteam.helpme.R.layout.abc_list_menu_item_icon;
import static com.androiddvptteam.helpme.R.layout.map_fragment;
import static com.androiddvptteam.helpme.R.layout.support_simple_spinner_dropdown_item;
import static com.baidu.mapapi.BMapManager.init;
import java.io.Serializable;

public class MapFragment extends BaseFragment {
	public LocationClient mLocationClient;//定位客户端
    public MyLocationListener mMyLocationListener;  //定位监听器
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;//是否是第一次定位
    private volatile boolean isFristLocation = true;//初始化定位相关代码
    private TextureMapView mapview;
	private BaiduMap baiduMap;
	private boolean isFirstLocate = true;
	private TextView positionText;
    private BitmapDescriptor mbitmap = BitmapDescriptorFactory.fromResource(R.drawable.coordinate);//图标
    @Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_fragment, container, false);
        mLocationClient = new LocationClient(getContext().getApplicationContext());//获取全进程有效的context
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getContext().getApplicationContext());
        mapview = (TextureMapView) view.findViewById(R.id.bmapView);
        baiduMap = mapview.getMap();//获得地图实例
        baiduMap.setMyLocationEnabled(true);
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
    private void requestLocation(){//获取当前定位
        initMyLocation();
        mLocationClient.start();
    }
    private void stopRequestLocation(){
        mLocationClient.stop();
    }
    private void initMyLocation(){
        mLocationClient = new LocationClient(getContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);// 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("gcj02");//返回定位结果是百度经纬度
        option.setScanSpan(5000);//多长时间进行一次请求
        option.setOpenGps(true);//打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);//位置
        mLocationClient.setLocOption(option);//使用设置
    }
    private void navigateTo(BDLocation location){
        if(isFirstLocate){//判断是否第一次定位，若是就执行以下代码
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }
    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onConnectHotSpotMessage(String s,int i){
        }
        @Override
        public void onReceiveLocation(BDLocation location){
            if(location.getLocType()==BDLocation.TypeNetWorkLocation||location.getLocType()==BDLocation.TypeGpsLocation){
                navigateTo(location);
            }else return;
        }
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true){
            requestLocation();
        }
        else if(isVisibleToUser==false){
            stopRequestLocation();
        }
    }
	@Override
    public void onResume(){
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
    public void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
        mapview.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
    public void addInfosOverlay(final List<Info> infos) {//加载覆盖物
        baiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;//地图覆盖物
        for (Info info : infos) {//循环的将模拟数据标记
            latLng = new LatLng(info.getLatitude(), info.getLongitude());//获取经纬度
            overlayOptions = new MarkerOptions().position(latLng).icon(mbitmap).zIndex(5);//设置maker位置，图标，层级
            marker = (Marker) (baiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", (Serializable) info);
            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(update);
    }
    public void initmapEvent(){//地图长按事件监听回调函数
        //baiduMap.setOnMapLongClickListener(mlongclicklistener);
        BaiduMap.OnMarkerClickListener mMarkerlis=new BaiduMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                Info Info=(Info) marker.getExtraInfo().get("info");
                InfoWindow mInfoWindow;//生成一个TextView用户在地图中显示InfoWindow
                TextView location = new TextView(getActivity().getApplicationContext());
                location.setBackgroundResource(R.drawable.coordinate);
                location.setPadding(30, 20, 30, 50);
                location.setText(Info.getName());//将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                Point p = baiduMap.getProjection().toScreenLocation(ll);
                p.y -= 50;
                LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
                //为弹出的InfoWindow添加点击事件
                mInfoWindow = new InfoWindow(location, llInfo, -47);
                //显示InfoWindow
                baiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        };
        baiduMap.setOnMarkerClickListener(mMarkerlis);
    }
//    private void search() {//步行线路规划，或许还用不上
//        RoutePlanSearch routePlanSearch = RoutePlanSearch.newInstance();
//        WalkingRoutePlanOption walkingOption = new WalkingRoutePlanOption();
//        PlanNode from = PlanNode.withLocation(hmPos);//创建起点
//        PlanNode to = PlanNode.withLocation(new LatLng(40.065796,116.349868));// 创建终点
//        walkingOption.from(from);
//        walkingOption.to(to);
//        routePlanSearch.walkingSearch(walkingOption);
//        routePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
//            @Override
//            public void onGetWalkingRouteResult(WalkingRouteResult result) {
//                if (result == null
//                        || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error) {
//                    Toast.makeText(getApplicationContext(), "未搜索到结果", 0).show();
//                    return;
//                }
//                WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
//                baiduMap.setOnMarkerClickListener(overlay);
//                overlay.setData(result.getRouteLines().get(0));
//                overlay.addToMap();
//                overlay.zoomToSpan();
//            }
//            @Override
//            public void onGetTransitRouteResult(TransitRouteResult arg0) {
//
//            }
//            @Override
//            public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
//
//            }
//        });
//    }
}

