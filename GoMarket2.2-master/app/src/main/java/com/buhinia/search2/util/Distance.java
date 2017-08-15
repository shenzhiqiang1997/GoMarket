package com.buhinia.search2.util;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.math.BigDecimal;

/**
 * 用于计算用户与超市距离的工具类
 */

public class Distance {

    public static double getDistance(BDLocation location,double marketLatitude,double marketLongitude){

        //封装用户经纬度
        LatLng userLatLng=new LatLng(location.getLatitude(),location.getLongitude());

        //封装超市经纬度
        LatLng marketLatLng=new LatLng(marketLongitude,marketLatitude);

        //计算用户与超市距离
        BigDecimal bd=new BigDecimal(DistanceUtil.getDistance(userLatLng,marketLatLng)/1000);

        return  bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
