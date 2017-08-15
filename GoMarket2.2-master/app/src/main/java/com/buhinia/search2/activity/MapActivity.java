package com.buhinia.search2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.buhinia.search2.R;

public class MapActivity extends AppCompatActivity {

    //地图视图控件
    MapView mapView=null;

    //百度地图本身
    BaiduMap map=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取当前经纬度
        Double la=getIntent().getDoubleExtra("lo",0);
        Double lo=getIntent().getDoubleExtra("la",0);

        //初始化百度地图SDK
        SDKInitializer.initialize(getApplicationContext());

        //初始化当前Activity布局
        setContentView(R.layout.map_view);

        //获取地图视图
        mapView=(MapView) findViewById(R.id.bmapView);

        //获取百度地图
        map=mapView.getMap();

        //开启定位图层
        map.setMyLocationEnabled(true);

        //封装经纬度
        LatLng latLng=new LatLng(la,lo);

        //地图数据更新器设置当前位置
        MapStatusUpdate mapStatusUpdate= MapStatusUpdateFactory.newLatLng(latLng);
        //绑定位置
        map.animateMapStatus(mapStatusUpdate);

        //在当前位置设置可视圆点
        MyLocationData myLocationData=new MyLocationData.Builder().latitude(la).longitude(lo).build();
        map.setMyLocationData(myLocationData);

    }

    @Override
    protected void onDestroy() {

        mapView.onDestroy();
        map.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
}
