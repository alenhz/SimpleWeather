package com.alen.simpleweather;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alen.simpleweather.adapter.MyCityListAdapter;
import com.alen.simpleweather.gson.MyCity;
import com.alen.simpleweather.gson.Weather;
import com.alen.simpleweather.util.HttpUtil;
import com.alen.simpleweather.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Response;

public class MyCityListActivity extends AppCompatActivity {
    private Context context;
    private TextView my_city_list_title_text;
    private Button my_city_list_back_button, my_city_list_edit_cancel_button, my_city_list_edit_ok_button, my_city_list_edit_button;
    private FloatingActionButton my_city_list_add_button;
    private SQLiteDatabase database;
    private List<MyCity> list, removeList;
    private RecyclerView my_city_list_recycler;
    private MyCityListAdapter myCityListAdapter;
    private String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_city_list);
        init();
        listener();

    }
    private void init(){
        context = MyCityListActivity.this;
        my_city_list_title_text = (TextView) findViewById(R.id.my_city_list_title_text);
        my_city_list_edit_cancel_button = (Button) findViewById(R.id.my_city_list_edit_cancel_button);
        my_city_list_edit_ok_button = (Button) findViewById(R.id.my_city_list_edit_ok_button);
        my_city_list_back_button = (Button) findViewById(R.id.my_city_list_back_button);
        my_city_list_edit_button = (Button) findViewById(R.id.my_city_list_edit_button);

        my_city_list_add_button = (FloatingActionButton) findViewById(R.id.my_city_list_add_button);

        my_city_list_recycler = (RecyclerView) findViewById(R.id.my_city_list_recycler);

        my_city_list_recycler.setLayoutManager(new LinearLayoutManager(this));

        database = Utility.getDB(this);
        removeList = new ArrayList<>();

        //设置字体
        Utility.setTypeFace(this, new TextView[]{
                my_city_list_title_text, my_city_list_edit_cancel_button, my_city_list_edit_ok_button
        });

        //设置透明状态栏
        Utility.statusBar(getWindow());
    }

    private void listener(){
        //返回键
        my_city_list_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.close();
                finish();
            }
        });
        //编辑键
        my_city_list_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_city_list_edit_button.setVisibility(View.GONE);
                my_city_list_back_button.setVisibility(View.GONE);
                my_city_list_edit_cancel_button.setVisibility(View.VISIBLE);
                my_city_list_edit_ok_button.setVisibility(View.VISIBLE);
                my_city_list_add_button.setVisibility(View.GONE);
                removeList.clear();
                refresh(true);
            }
        });
        //编辑取消键
        my_city_list_edit_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_city_list_edit_cancel_button.setVisibility(View.GONE);
                my_city_list_edit_ok_button.setVisibility(View.GONE);
                my_city_list_edit_button.setVisibility(View.VISIBLE);
                my_city_list_back_button.setVisibility(View.VISIBLE);
                my_city_list_add_button.setVisibility(View.VISIBLE);
                updata();
            }
        });
        //编辑确定键
        my_city_list_edit_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_city_list_edit_cancel_button.setVisibility(View.GONE);
                my_city_list_edit_ok_button.setVisibility(View.GONE);
                my_city_list_edit_button.setVisibility(View.VISIBLE);
                my_city_list_back_button.setVisibility(View.VISIBLE);
                my_city_list_add_button.setVisibility(View.VISIBLE);
                if (removeList != null && database!=null){
                    ContentValues cv = new ContentValues();
                    cv.put("judge","n");
                    for (MyCity city : removeList){
                        database.update("list", cv, "lonlat = ?", new String[]{city.lonlat});

                    }
                }
                refresh(false);
                String json = new Gson().toJson(list);
                Utility.setPrefe(context, "list", "list", json);
            }
        });
        my_city_list_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchCityActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updata(){
        String listTxt = Utility.getPrefe(context, "list", "list");
        if (listTxt != null){
            list = new Gson().fromJson(listTxt, new TypeToken<List<MyCity>>(){}.getType());
            for (int i = 0 ; i < list.size() ; i++){
                if (i != 0){
                    fileName = list.get(i).lonlat;
                }else {
                    fileName = "lbs";
                }
                face(i, Utility.getPrefe(context, fileName, "hefeng"), 1);
                if (Utility.getTime(context, fileName, "upDataTime1") > 300){
                    handleWeather(i);
                }
            }
        }
    }
    private void refresh(boolean type){
        myCityListAdapter = new MyCityListAdapter(context, list, type);
        my_city_list_recycler.setAdapter(myCityListAdapter);
        my_city_list_recycler.refreshDrawableState();
        //监听跳转
        myCityListAdapter.setItemClickListener(new MyCityListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean type) {
                if (type){
                    Intent intent = new Intent();
                    intent.putExtra("pager", position);
                    setResult(1, intent);
                    database.close();
                    finish();
                }
            }
        });
        //监听删除按钮
        myCityListAdapter.buttonSetOnclick(new MyCityListAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                Utility.showToast(context, position+"");
                removeList.add(list.get(position));
               list.remove(position);
               refresh(true);
            }
        });
    }

    private void handleWeather(final int i){
        HttpUtil.sendOkHttpRequest(Utility.getURL(list.get(i).lonlat, 1), new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utility.showToast(context, "请检查网络连接并重试");
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        face(i, responseText, 0);
                    }
                });
            }
        });
    }
    private void face(int i, String responseText, int a){
        Weather weather = Utility.handleWeatherResponse(responseText);
        if (weather != null && "ok".equals(weather.status)){
            MyCity city = new MyCity(list.get(i).cn, list.get(i).province, list.get(i).city, list.get(i).lonlat, weather.now.tmp, weather.now.cond_code);
            list.set(i, city);
            String json = new Gson().toJson(list);
            Utility.setPrefe(context, "list", "list", json);
            Utility.setPrefe(context, list.get(i).lonlat, "hefeng", responseText);
            if (i != 0){
                Utility.setPrefe(context, list.get(i).lonlat, "upDataTime1", System.currentTimeMillis()+"");
            }else {
                Utility.setPrefe(context, "lbs", "upDataTime1", System.currentTimeMillis()+"");
            }
            refresh(false);
        }
        else if (a != 1){
            Utility.showToast(context, "获取天气数据失败");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updata();
    }
}
