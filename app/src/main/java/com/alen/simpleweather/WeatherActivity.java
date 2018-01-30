package com.alen.simpleweather;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alen.simpleweather.gson.MyCity;
import com.alen.simpleweather.util.Utility;
import com.alen.simpleweather.view.ViewPagerIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.List;


public class WeatherActivity extends FragmentActivity{

    private LinearLayout indicator_layout;
    private Button add_city, setting_button;
    private ViewPager viewPager;
    private List<MyCity> list;
    private List<Fragment> fragments;
    private int position, i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化控件
        init();
        listener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String listTxt = Utility.getPrefe("list", "list");
        if (listTxt != null){
            list = new Gson().fromJson(listTxt, new TypeToken<List<MyCity>>(){}.getType());
        }
        fragmentPager();
    }

    private void init(){
        Utility.statusBar(getWindow());

        indicator_layout = (LinearLayout) findViewById(R.id.indicator_layout);
        add_city = (Button) findViewById(R.id.add_city);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setting_button = (Button) findViewById(R.id.setting_button);
    }
    private void listener(){
        add_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, MyCityListActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
    private void fragmentPager(){
        indicator_layout.removeAllViews();
        if (list == null){
            position = 1;
        }else {
            position = list.size();
        }
        fragments = new ArrayList<Fragment>();
        for (int i = 0 ; i<position ; i++){
            WeatherFragment weatherFragment = WeatherFragment.newInstance(i);
            fragments.add(weatherFragment);
        }
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(position);
        viewPager.setCurrentItem(i);
        viewPager.addOnPageChangeListener(new ViewPagerIndicator(this, viewPager, indicator_layout, position));
        i = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == 1){
                i = data.getIntExtra("pager", 0);
            }
        }
    }
}