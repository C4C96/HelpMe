package com.androiddvptteam.helpme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.androiddvptteam.helpme.R.layout.map_fragment;
import static com.androiddvptteam.helpme.R.layout.support_simple_spinner_dropdown_item;
import static com.baidu.mapapi.BMapManager.init;

public class MapFragment extends BaseFragment {
	public LocationClient mLocationClient;
	private TextureMapView mapview;
	private BaiduMap baiduMap;
	private boolean isFirstLocate = true;
	private TextView positionText;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// super.onCreateView(inflater,container,savedInstanceState);
		View view = inflater.inflate(R.layout.map_fragment, container, false);

		MapApplication mapApplication = new MapApplication();
		mapApplication.onCreate();

        mLocationClient = new LocationClient(getContext().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getContext().getApplicationContext());
        mapview = (TextureMapView) view.findViewById(R.id.bmapView);
        baiduMap = mapview.getMap();
        baiduMap.setMyLocationEnabled(true);
        getActivity().setContentView(R.layout.map_fragment);
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
    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }
    private void stopRequestLocation(){
        mLocationClient.stop();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
    private void navigateTo(BDLocation location){
        if(isFirstLocate){
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
}
