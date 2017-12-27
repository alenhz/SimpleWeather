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
import com.alen.simpleweather.adapter.LifeAdapter;
import com.alen.simpleweather.gson.CaiyunData;
import com.alen.simpleweather.gson.MyCity;
import com.alen.simpleweather.gson.HefengData;
import com.alen.simpleweather.util.LBS;
import com.alen.simpleweather.util.RequestWeather;
import com.alen.simpleweather.util.Utility;
import com.alen.simpleweather.view.MyForecastData;
import com.alen.simpleweather.view.MyHourlyData;
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

/**
 * Created by Alen on 2017/12/15.
 */


public class WeatherFragment extends Fragment {
    private RequestWeather requestWeather;
    private LBS lbs;
    private View view;
    private Window window;
    private Context context;
    private Activity activity;

    private TextView title_text_city_name, title_text_temperature, title_text_weather_info, title_text_weather_pm25,
            title_text_wind_power, title_text_humidity, title_text_somatosensory_temperature, title_text_wind_text,
            title_text_CL01, title_text_CL02, title_text_CL03, now_title_info_text, two_hours_text;

    private TextView description_text, activity_weather_text_CL01;
    private RecyclerView forecastRecyclerView, hourlyRecyclerView, lifeRecyclerView;
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

    public WeatherFragment(){}

    @SuppressLint("ValidFragment")
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
        if (position != 0){
            fileName = lonlat;
        }else {
            fileName = "lbs";
            lbs();
        }
        requestWeather = new RequestWeather(activity, context);

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
        two_hours_text = (TextView) view.findViewById(R.id.two_hours_text);

        description_text = (TextView) view.findViewById(R.id.description_text);
        activity_weather_text_CL01 = (TextView) view.findViewById(R.id.activity_weather_text_CL01);

        forecastAllTemperature = new ArrayList();
        forecastRecyclerView = (RecyclerView) view.findViewById(R.id.forecast_recyclerview);
        hourlyRecyclerView = (RecyclerView) view.findViewById(R.id.hourly_recyclerview);
        lifeRecyclerView = (RecyclerView) view.findViewById(R.id.life_recycler);

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
                title_text_CL01, title_text_CL02, title_text_CL03, description_text, activity_weather_text_CL01,
                now_title_info_text, two_hours_text
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
                    requestWeather.handleWeather(weatherData[i], position, i+1);
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
                requestWeather.request(position, fileName, lonlat, street);
                refreshLayout.finishRefresh(true);
            }
        });
    }
    private void update(){
        if (position==0){
            lbs.start();
        }else {
            requestWeather.request(position, fileName, lonlat);
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
        private int h = DensityUtil.dp2px(360);
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

        requestWeather.setCallBack(new RequestWeather.RequestCallBack() {

            @Override
            public void callBackHefeng(final HefengData hefengData) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showTitleInfo(hefengData);
                        showForcastInfo(hefengData);
                        showLifeInfo(hefengData);
                    }
                });
            }

            @Override
            public void callBackCaiyun(final CaiyunData caiyunData) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showHourlyInfo(caiyunData);
                        noInternet();
                    }
                });
            }

            @Override
            public void callBackError() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh(false);
                    }
                });
            }
        });
    }
    private void showTitleInfo(HefengData hefengData){
        if (street != null){
            Utility.setPrefe(context, fileName, "street", street);
            title_text_city_name.setText(street);
            now_title_info_text.setText(street+" "+ hefengData.now.tmp+"℃");
        }else {
            now_title_info_text.setText(hefengData.basic.location+" "+ hefengData.now.tmp+"℃");
            title_text_city_name.setText(hefengData.basic.location);
        }
        title_text_temperature.setText(hefengData.now.tmp);
        title_text_weather_info.setText(hefengData.now.cond_txt);
        title_text_wind_text.setText(hefengData.now.wind_dir);
        title_text_wind_power.setText(hefengData.now.wind_sc);
        title_text_humidity.setText(hefengData.now.hum);
        title_text_somatosensory_temperature.setText(hefengData.now.fl);
        int nowTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        try {
            if (hefengData.now.cond_code <= 103 && (nowTime<6 || nowTime>=18)) {
                imageId_d = "icon/" + hefengData.now.cond_code + "_n.png";
            } else {
                imageId_d = "icon/" + hefengData.now.cond_code + ".png";
            }
            Bitmap bitmap = BitmapFactory.decodeStream(activity.getAssets().open(imageId_d));
            now_weather_code_image.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showForcastInfo(HefengData hefengData){
        today = true;
        forecastAllTemperature.clear();
        List<MyForecastData> datas = new ArrayList<>();
        for (HefengData.Forecast forecast : hefengData.forecastList) {
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
        forecastRecyclerView.setAdapter(new ForecastAdapter(activity, datas, highestDegree, lowestDegree, mScreenWidth));
    }
    private void showLifeInfo(HefengData hefengData){
        List<HefengData.LifeStyle> list = new ArrayList();
        for (HefengData.LifeStyle lifeStyle : hefengData.lifeStyleList){
            list.add(lifeStyle);
        }
        lifeRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lifeRecyclerView.setAdapter(new LifeAdapter(context, list));
    }
    private void showHourlyInfo(CaiyunData caiyunData){
        description_text.setText(caiyunData.hourly.description);
        List<MyHourlyData> datas = new ArrayList<>();
        CaiyunData.Hourly.HourlyAQI hourlyAQI;
        CaiyunData.Hourly.HourlyPrecipitation hourlyPrecipitation;
        CaiyunData.Hourly.HourlyTemperature hourlyTemperature;
        forecastAllTemperature.clear();
        hourlyAQI = caiyunData.hourly.aqi.get(0);
        title_text_weather_pm25.setText("空气质量 "+Integer.parseInt(hourlyAQI.value));
        two_hours_text.setText(caiyunData.minutely.description);
        for(int i = 0; i < caiyunData.hourly.aqi.size(); i++){
            hourlyAQI = caiyunData.hourly.aqi.get(i);
            hourlyTemperature = caiyunData.hourly.temperature.get(i);
            hourlyPrecipitation = caiyunData.hourly.precipitation.get(i);
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
        hourlyRecyclerView.setAdapter(new HourlyAdapter(activity, datas, highestDegree, lowestDegree, mScreenWidth));
    }
}
