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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alen.simpleweather.adapter.MyCityListAdapter;
import com.alen.simpleweather.gson.CaiyunData;
import com.alen.simpleweather.gson.MyCity;
import com.alen.simpleweather.gson.HefengData;
import com.alen.simpleweather.util.RequestWeather;
import com.alen.simpleweather.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

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
    private RequestWeather requestWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_city_list);
        init();
        listener();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updata();
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

        database = Utility.getDB();
        removeList = new ArrayList<>();

        requestWeather = new RequestWeather(MyCityListActivity.this, context);

        //设置字体
        Utility.setTypeFace(new TextView[]{
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
                Utility.setPrefe("list", "list", json);
            }
        });
        my_city_list_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchCityActivity.class);
                startActivity(intent);
            }
        });

        requestWeather.setCallBack(new RequestWeather.RequestCallBack() {

            @Override
            public void callBackHefeng(HefengData hefengData) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list = Utility.getList();
                        refresh(false);
                    }
                });
            }

            @Override
            public void callBackCaiyun(CaiyunData caiyunData) {

            }

            @Override
            public void callBackError() {

            }
        });
    }
    private void updata(){
        String listTxt = Utility.getPrefe("list", "list");
        if (listTxt != null){
            list = new Gson().fromJson(listTxt, new TypeToken<List<MyCity>>(){}.getType());
            refresh(false);
            for (int i = 0 ; i < list.size() ; i++){
                if (i != 0){
                    fileName = list.get(i).lonlat;
                }else {
                    fileName = "lbs";
                }
                requestWeather.request(i, fileName, list.get(i).lonlat, list.get(i).cn);
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
                removeList.add(list.get(position));
               list.remove(position);
               refresh(true);
            }
        });
    }





}
