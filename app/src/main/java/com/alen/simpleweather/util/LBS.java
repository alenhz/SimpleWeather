package com.alen.simpleweather.util;

import android.content.Context;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;



/**
 * Created by Alen on 2017/12/2.
 */

public class LBS {

    public LocationClient mLocationClient;
    private LocationCallBack callBack;

    public void init(Context context){
        mLocationClient = new LocationClient(context.getApplicationContext());
        //.registerLocationListener()注册定位监听器
        mLocationClient.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(0);
        //需要获取当前位置的详细信息
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        //定位请求回调函数 通过mLocationClient.start()启动
        public void onReceiveLocation(BDLocation location) {
            if (callBack != null&&location!=null){
                callBack.callBack(location.getLongitude() + "," + location.getLatitude(), location.getStreet(), location.getCity(), location.getProvince());
            }
            //多次定位必须要调用stop方法
            mLocationClient.stop();
        }
    }
    public interface LocationCallBack {
        void callBack(String lonlat, String street, String city, String province);
    }

    public void start(){
        mLocationClient.start();
    }
    public void setCallBack( LocationCallBack callBack){
        this.callBack = callBack;
    }
}
