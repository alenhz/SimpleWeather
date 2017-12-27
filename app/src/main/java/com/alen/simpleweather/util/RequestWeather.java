package com.alen.simpleweather.util;

import android.app.Activity;
import android.content.Context;

import com.alen.simpleweather.gson.CaiyunData;
import com.alen.simpleweather.gson.HefengData;


import java.io.IOException;

import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Alen on 2017/12/22.
 */

public class RequestWeather {
    private Activity activity;
    private Context context;
    RequestCallBack callBack;
    private HefengData hefengData;
    private CaiyunData caiyunData;
    public RequestWeather(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }
    public void request(final int position, final String fileName, final String lonlat){
        request(position, fileName, lonlat, null);
    }
    public void request(final int position, final String fileName, final String lonlat, final String street){
        for (int i = 1 ; i < 3 ; i++){
            final int type = i;
            if (Utility.getTime(context, fileName, "upDataTime"+type) > 600){
                Utility.sendOkHttpRequest(Utility.getURL(lonlat, i), new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.callBackError();
                                Utility.showToast(context, "请检查网络连接并重试");
                            }
                        });
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        switch (type){
                            case 1:
                                hefengData = Utility.handleHefengResponse(responseText);
                                if (hefengData != null && "ok".equals(hefengData.status)){
                                    Utility.setPrefe(context, fileName, "hefeng", responseText);
                                    if (fileName == "lbs"){
                                        Utility.saveList(context, position, street, hefengData.basic.admin_area, hefengData.basic.parent_city, lonlat, hefengData.now.tmp, hefengData.now.cond_code);
                                    }else {
                                        Utility.saveList(context, position, hefengData.basic.location, hefengData.basic.admin_area, hefengData.basic.parent_city, lonlat, hefengData.now.tmp, hefengData.now.cond_code);
                                    }
                                    Utility.saveTime(context, fileName, type);
                                    callBack.callBackHefeng(hefengData);
                                }else {
                                    callBack.callBackError();
                                    Utility.showToast(context, "获取天气数据失败");
                                }
                                break;
                            case 2:
                                caiyunData = Utility.handleCaiyunWeatherResponse(responseText);
                                if (caiyunData != null && "ok".equals(caiyunData.hourly.status)){
                                    Utility.setPrefe(context, fileName, "caiyun", responseText);
                                    Utility.saveTime(context, fileName, type);
                                    callBack.callBackCaiyun(caiyunData);
                                }else {
                                    callBack.callBackError();
                                    Utility.showToast(context, "获取天气数据失败");
                                }
                                break;
                            default:
                        }
                    }
                });
            }
        }
    }

    public void handleWeather(final String responseText, final int position, final int type){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case 1:
                        hefengData = Utility.handleHefengResponse(responseText);
                        if (hefengData != null && "ok".equals(hefengData.status)){
                            callBack.callBackHefeng(hefengData);
                        }else {
                            callBack.callBackError();
                            Utility.showToast(context, "获取天气数据失败");
                        }
                        break;
                    case 2:
                        caiyunData = Utility.handleCaiyunWeatherResponse(responseText);
                        if (caiyunData != null && "ok".equals(caiyunData.hourly.status)){
                            callBack.callBackCaiyun(caiyunData);
                        }else {
                            callBack.callBackError();
                            Utility.showToast(context, "获取天气数据失败");
                        }
                        break;
                    default:
                }
            }
        });
    }

    public interface RequestCallBack {
        void callBackHefeng(HefengData hefengData);
        void callBackCaiyun(CaiyunData caiyunData);
        void callBackError();
    }
    public void setCallBack(RequestCallBack callBack){
        this.callBack = callBack;
    }
}
