package com.alen.simpleweather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alen.simpleweather.adapter.ForecastAdapter;
import com.alen.simpleweather.adapter.HourlyAdapter;
import com.alen.simpleweather.gson.CaiyunData;
import com.alen.simpleweather.gson.Forecast;
import com.alen.simpleweather.gson.HourlyAQI;
import com.alen.simpleweather.gson.HourlyPrecipitation;
import com.alen.simpleweather.gson.HourlyTemperature;
import com.alen.simpleweather.gson.LifeStyle;
import com.alen.simpleweather.gson.MyCity;
import com.alen.simpleweather.gson.Weather;
import com.alen.simpleweather.util.HttpUtil;
import com.alen.simpleweather.util.LBS;
import com.alen.simpleweather.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Alen on 2017/12/15.
 */

@SuppressLint("ValidFragment")
public class WeatherFragment extends Fragment {
    private LBS lbs;
    private View view;
    private Window window;
    private Context context;
    private Activity activity;

    private TextView title_text_city_name, title_text_temperature, title_text_weather_info, title_text_weather_pm25,
            title_text_wind_power, title_text_humidity, title_text_somatosensory_temperature, title_text_wind_text,
            title_text_CL01, title_text_CL02, title_text_CL03, now_title_info_text;

    private TextView travel, travel_info, clothes, clothes_info,
            sunscreen, sunscreen_info, sport, sport_info, car, car_info,
            description_text, activity_weather_text_CL01;
    private RecyclerView forecastRecyclerView, hourlyRecyclerView;
    private LinearLayout title_null_layout, info_layout, menu_layout;
    private RelativeLayout title_layout;
    DisplayMetrics dm;
    private boolean today;
    private List forecastAllTemperature, code;
    private int highestDegree, lowestDegree, position;
    private String week, imageId_d, imageId_n, street, lonlat, fileName;
    private NestedScrollView scrollView;
    private RefreshLayout refreshLayout;
    private View bg;
    private int mOffset = 0, mScrollY = 0;
    private Toolbar toolbar;
    private ImageView now_weather_code_image;
    private List<MyCity> list;

    public WeatherFragment(LinearLayout menu_layout, int position){
        this.menu_layout = menu_layout;
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weather, container, false);
        context = getActivity();
        activity = getActivity();
        window = getActivity().getWindow();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getList();
        init();
        //监听
        listener();
        //处理缓存
        cache();

    }
    private void getList(){
        String listTxt = Utility.getPrefe(context, "list", "list");
        if (listTxt != null){
            list = new Gson().fromJson(listTxt, new TypeToken<List<MyCity>>(){}.getType());
            if (position != 0){
                lonlat = list.get(position).lonlat;
                street = list.get(position).cn;
            }
        }else {
            list = new ArrayList<MyCity>();
        }
    }
    private void init(){
        if (position == 0){
            fileName = "lbs";
            lbs();
        }else {
            fileName = lonlat;
        }
        //初始化view

        title_text_city_name = (TextView) view.findViewById(R.id.title_text_city_name);
        title_text_temperature = (TextView) view.findViewById(R.id.title_text_temperature);
        title_text_weather_info = (TextView) view.findViewById(R.id.title_text_weather_info);
        title_text_weather_pm25 = (TextView) view.findViewById(R.id.title_text_weather_pm25);
        title_text_wind_power = (TextView) view.findViewById(R.id.title_text_wind_power);
        title_text_humidity = (TextView) view.findViewById(R.id.title_text_humidity);
        title_text_somatosensory_temperature = (TextView) view.findViewById(R.id.title_text_somatosensory_temperature);
        title_text_wind_text = (TextView) view.findViewById(R.id.title_text_wind_text);
        title_text_CL01 = (TextView) view.findViewById(R.id.title_text_CL01);
        title_text_CL02 = (TextView) view.findViewById(R.id.title_text_CL02);
        title_text_CL03 = (TextView) view.findViewById(R.id.title_text_CL03);
        now_title_info_text = (TextView) view.findViewById(R.id.now_title_info_text);

        travel = (TextView) view.findViewById(R.id.travel);
        travel_info = (TextView) view.findViewById(R.id.travel_info);
        clothes = (TextView) view.findViewById(R.id.clothes);
        clothes_info = (TextView) view.findViewById(R.id.clothes_info);
        sunscreen = (TextView) view.findViewById(R.id.sunscreen);
        sunscreen_info = (TextView) view.findViewById(R.id.sunscreen_info);
        sport = (TextView) view.findViewById(R.id.sport);
        sport_info = (TextView) view.findViewById(R.id.sport_info);
        car = (TextView) view.findViewById(R.id.car);
        car_info = (TextView) view.findViewById(R.id.car_info);
        description_text = (TextView) view.findViewById(R.id.description_text);
        activity_weather_text_CL01 = (TextView) view.findViewById(R.id.activity_weather_text_CL01);

        forecastAllTemperature = new ArrayList();
        forecastRecyclerView = (RecyclerView) view.findViewById(R.id.forecast_recyclerview);
        hourlyRecyclerView = (RecyclerView) view.findViewById(R.id.hourly_recyclerview);
        title_layout = (RelativeLayout) view.findViewById(R.id.title_layout);
        title_null_layout = (LinearLayout) view.findViewById(R.id.title_null_layout);
        info_layout = (LinearLayout) view.findViewById(R.id.info_layout);

        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        bg = view.findViewById(R.id.bg);
        scrollView = (NestedScrollView) view.findViewById(R.id.bindablescrollView);

        now_weather_code_image = (ImageView) view.findViewById(R.id.now_weather_code_image);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        code = new ArrayList();
        dm = new DisplayMetrics();

        //设置字体
        Utility.setTypeFace(context, new TextView[]{
                title_text_city_name, title_text_temperature, title_text_weather_info, title_text_weather_pm25,
                title_text_wind_power, title_text_humidity, title_text_somatosensory_temperature, title_text_wind_text,
                title_text_CL01, title_text_CL02, title_text_CL03, travel, travel_info, clothes, clothes_info,
                sunscreen, sunscreen_info, sport, sport_info, car, car_info, description_text, activity_weather_text_CL01,
                now_title_info_text
        });
    }
    private void cache(){
        String[] weatherData = {Utility.getPrefe(context, fileName, "hefeng"),
                Utility.getPrefe(context, fileName, "caiyun"), Utility.getPrefe(context, fileName, "street")};
        if (weatherData[2] != null) {
            street = weatherData[2];
        }
        if (weatherData[0] != null){
            //有缓存时先解析天气,再联网更新
            for (int i = 0 ; i < 2 ; i ++){
                if (weatherData[i] != null) {
                    handleWeather(weatherData[i], i+1, 0);
                }
            }
            update();
        }else {
            //无缓存时去服务器查询天气
            noInternet();
            update();
        }
    }
    private void noInternet(){
        if (Utility.getPrefe(context, fileName, "caiyun") == null){
            title_layout.setVisibility(View.GONE);
            info_layout.setVisibility(View.GONE);
            menu_layout.setVisibility(View.GONE);
            title_null_layout.setVisibility(View.VISIBLE);
        }else {
            title_layout.setVisibility(View.VISIBLE);
            info_layout.setVisibility(View.VISIBLE);
            menu_layout.setVisibility(View.VISIBLE);
            title_null_layout.setVisibility(View.GONE);
        }
    }
    private void lbs(){
        lbs = new LBS();
        lbs.init(context);
        lbs.setCallBack(new LBS.LocationCallBack() {
            @Override
            public void callBack(String lbslonlat, String lbsSting, String city, String province) {
                street = lbsSting;
                lonlat = lbslonlat;
                show();
                refreshLayout.finishRefresh(true);
            }
        });
    }
    private void save(String province, String city, String tmp, int code){
        if (list.size() != 0){
            list.set(position, new MyCity(street, province, city, lonlat, tmp, code));
        }else {
            list.add(new MyCity(street, province, city, lonlat, tmp, code));
        }
        String json = new Gson().toJson(list);
        Utility.setPrefe(context, "list", "list", json);
    }
    private void saveTime(int type, int in){
        if (in == 1){
            Utility.setPrefe(context, fileName, "upDataTime"+type, System.currentTimeMillis()+"");
        }
    }
    private void update(){
        if (position==0){
            lbs.start();
        }else {
            show();
        }
    }
    private void listener(){
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                bg.setTranslationY(mOffset - mScrollY);
            }
            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                bg.setTranslationY(mOffset - mScrollY);
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
        private int lastScrollY = 0;
        private int h = DensityUtil.dp2px(320);
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            if (lastScrollY < h) {
                scrollY = Math.min(h, scrollY);
                mScrollY = scrollY > h ? h : scrollY;
                toolbar.setAlpha((mScrollY / h));
                bg.setTranslationY(mOffset - mScrollY);
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }else {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            lastScrollY = scrollY;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                update();
            }
        });
    }
    public void show(){
        for (int i = 1 ; i < 3 ; i++){
            final int type = i;
            if (Utility.getTime(context, fileName, "upDataTime"+type) > 300){
                HttpUtil.sendOkHttpRequest(Utility.getURL(lonlat, i), new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.finishRefresh(false);
                                Utility.showToast(activity, "请检查网络连接并重试");
                            }
                        });
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        handleWeather(responseText, type, 1);
                    }
                });
            }
        }
    }
    public void handleWeather(final String responseText, final int type, final int in){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case 1:
                        final Weather weather = Utility.handleWeatherResponse(responseText);
                        if (weather != null && "ok".equals(weather.status)){
                            Utility.setPrefe(context, fileName, "hefeng", responseText);
                            saveTime(type, in);
                            save(weather.basic.admin_area, weather.basic.parent_city, weather.now.tmp, weather.now.cond_code);
                            showTitleInfo(weather);
                            showForcastInfo(weather);
                            showLifeInfo(weather);
                        }else {
                            refreshLayout.finishRefresh(false);
                            Utility.showToast(activity, "获取天气数据失败");
                        }
                        break;
                    case 2:
                        final CaiyunData caiyunData = Utility.handleCaiyunWeatherResponse(responseText);
                        if (caiyunData != null && "ok".equals(caiyunData.status)){
                            Utility.setPrefe(context, fileName, "caiyun", responseText);
                            saveTime(type, in);
                            showHourlyInfo(caiyunData);
                            noInternet();
                        }else {
                            refreshLayout.finishRefresh(false);
                            Utility.showToast(activity, "获取天气数据失败");
                        }
                        break;
                    default:
                }
            }
        });
    }
    private void showTitleInfo(Weather weather){
        if (street != null){
            Utility.setPrefe(context, fileName, "street", street);
            title_text_city_name.setText(street);
            now_title_info_text.setText(street+" "+weather.now.tmp+"℃");
        }else {
            now_title_info_text.setText(weather.basic.location+" "+weather.now.tmp+"℃");
            title_text_city_name.setText(weather.basic.location);
        }
        title_text_temperature.setText(weather.now.tmp);
        title_text_weather_info.setText(weather.now.cond_txt);
        title_text_wind_text.setText(weather.now.wind_dir);
        title_text_wind_power.setText(weather.now.wind_sc);
        title_text_humidity.setText(weather.now.hum);
        title_text_somatosensory_temperature.setText(weather.now.fl);
        int nowTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        try {
            if (weather.now.cond_code <= 103 && (nowTime<6 || nowTime>=18)) {
                imageId_d = "icon/" + weather.now.cond_code + "_n.png";
            } else {
                imageId_d = "icon/" + weather.now.cond_code + ".png";
            }
            Bitmap bitmap = BitmapFactory.decodeStream(activity.getAssets().open(imageId_d));
            now_weather_code_image.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showForcastInfo(Weather weather){
        today = true;
        forecastAllTemperature.clear();
        List<MyForecastData> datas = new ArrayList<>();
        for (Forecast forecast : weather.forecastList) {
            code.clear();
            code.add(forecast.cond_code_d);
            code.add(forecast.cond_code_n);
            if (forecast.cond_code_n <= 103) {
                imageId_n = "icon/" + forecast.cond_code_n + "_n.png";
            } else {
                imageId_n = "icon/" + forecast.cond_code_n + ".png";
            }
            imageId_d = "icon/" + forecast.cond_code_d + ".png";

            forecastAllTemperature.add(forecast.tmp_max);
            forecastAllTemperature.add(forecast.tmp_min);
            if (!today) {
                week = Utility.getWeekOfDate(forecast.date);
            } else {
                week = "今天";
                today = false;
            }
            try {
                Bitmap bitmapD = BitmapFactory.decodeStream(activity.getAssets().open(imageId_d));
                Bitmap bitmapN = BitmapFactory.decodeStream(activity.getAssets().open(imageId_n));
                int[] tmp = {forecast.tmp_max, forecast.tmp_min};
                datas.add(new MyForecastData(tmp, forecast.date,
                        forecast.cond_txt_d, forecast.cond_txt_n, bitmapD,
                        bitmapN, forecast.wind_dir, forecast.wind_sc, week));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        highestDegree = (int) Collections.max(forecastAllTemperature);
        lowestDegree = (int) Collections.min(forecastAllTemperature);
        int mScreenWidth = dm.widthPixels / 6;
        LinearLayoutManager forecastManager = new LinearLayoutManager(activity);
        forecastManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        forecastRecyclerView.setLayoutManager(forecastManager);
        forecastRecyclerView.setNestedScrollingEnabled(false);
        forecastRecyclerView.setAdapter(new ForecastAdapter(activity, datas, highestDegree, lowestDegree, mScreenWidth));
    }
    private void showLifeInfo(Weather weather){
        for (LifeStyle lifeStyle : weather.lifeStyleList){
            switch (lifeStyle.type){
                case "comf":
                    travel.setText(lifeStyle.brf);
                    travel_info.setText(lifeStyle.txt);
                    break;
                case "drsg":
                    clothes.setText(lifeStyle.brf);
                    clothes_info.setText(lifeStyle.txt);
                    break;
                case "uv":
                    sunscreen.setText(lifeStyle.brf);
                    sunscreen_info.setText(lifeStyle.txt);
                    break;
                case "sport":
                    sport.setText(lifeStyle.brf);
                    sport_info.setText(lifeStyle.txt);
                    break;
                case "cw":
                    car.setText(lifeStyle.brf);
                    car_info.setText(lifeStyle.txt);
                    break;
                default:
            }
        }
    }
    private void showHourlyInfo(CaiyunData caiyunData){
        description_text.setText(caiyunData.description);
        List<MyHourlyData> datas = new ArrayList<>();
        HourlyAQI hourlyAQI;
        HourlyPrecipitation hourlyPrecipitation;
        HourlyTemperature hourlyTemperature;
        forecastAllTemperature.clear();
        hourlyAQI = caiyunData.hourlyAQIList.get(0);
        title_text_weather_pm25.setText("空气质量 "+Integer.parseInt(hourlyAQI.value));
        for(int i = 0; i < caiyunData.hourlyAQIList.size(); i++){
            hourlyAQI = caiyunData.hourlyAQIList.get(i);
            hourlyTemperature = caiyunData.hourlyTemperatureList.get(i);
            hourlyPrecipitation = caiyunData.hourlyPrecipitationList.get(i);
            int tmp = new BigDecimal(hourlyTemperature.value).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            int aqi = Integer.parseInt(hourlyAQI.value);
            String aqiInfo;
            int aqiBG;
            if (aqi <= 50){
                aqiInfo = aqi + " 优秀";
                aqiBG = 1;
            }else if (aqi <= 100){
                aqiInfo = aqi + " 良好";
                aqiBG = 2;
            }else if (aqi <= 150){
                aqiInfo = aqi + " 轻度";
                aqiBG = 3;
            }else if (aqi <= 200){
                aqiInfo = aqi + " 中度";
                aqiBG = 4;
            }else if (aqi <= 300){
                aqiInfo = aqi + " 重度";
                aqiBG = 5;
            }else {
                aqiInfo = aqi + " 严重";
                aqiBG = 6;
            }
            String time = hourlyTemperature.datetime.substring(11);
            String precipitation = hourlyPrecipitation.value;
            if (Double.parseDouble(precipitation) < 0.05){
                precipitation = "晴";
            }else if (Double.parseDouble(precipitation) <= 0.9){
                precipitation = "小雨";
            }else if (Double.parseDouble(precipitation) <= 2.87){
                precipitation = "中雨";
            }else {
                precipitation = "大雨";
            }
            forecastAllTemperature.add(tmp);
            datas.add(new MyHourlyData(new int[]{tmp}, aqiInfo, aqiBG, time, precipitation));
        }
        highestDegree = (int) Collections.max(forecastAllTemperature);
        lowestDegree = (int) Collections.min(forecastAllTemperature);
        int mScreenWidth = dm.widthPixels / 6;
        LinearLayoutManager hourlyManager = new LinearLayoutManager(activity);
        hourlyManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourlyRecyclerView.setLayoutManager(hourlyManager);
        hourlyRecyclerView.setNestedScrollingEnabled(false);
        hourlyRecyclerView.setAdapter(new HourlyAdapter(activity, datas, highestDegree, lowestDegree, mScreenWidth));
    }
}
