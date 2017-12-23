package com.alen.simpleweather;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alen.simpleweather.adapter.HotCityAdapter;
import com.alen.simpleweather.adapter.SearchCityAdapter;
import com.alen.simpleweather.db.CityList;
import com.alen.simpleweather.gson.MyCity;
import com.alen.simpleweather.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchCityActivity extends AppCompatActivity {
    private Context context;
    private HotCityAdapter hotCityAdapter;
    private SearchCityAdapter searchCityAdapter;
    private String[] hotCityList = {
            "定位", "北京", "上海", "广州", "深圳", "珠海", "佛山", "南京", "苏州",
            "厦门", "长沙", "成都", "福州", "杭州", "武汉", "青岛", "西安", "太原",
            "沈阳", "重庆", "天津", "南宁", "郑州", "大连", "宁波"
    };
    private TextView add_city_CL01, add_city_CL02;
    private Button add_city_back_button;
    private RecyclerView add_city_hot_city, add_city_search_list;
    private SearchView add_city_search;
    private SQLiteDatabase database;
    private LinearLayout hot_city_layout;
    private List<CityList> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        //初始化
        init();

        setSearch();
        //热门城市初始化
        hotCity();

        //
        listener();
    }

    private void init(){
        context = SearchCityActivity.this;
        add_city_CL01 = (TextView) findViewById(R.id.add_city_CL01);
        add_city_CL02 = (TextView) findViewById(R.id.add_city_CL02);
        add_city_back_button = (Button) findViewById(R.id.add_city_back_button);
        add_city_hot_city = (RecyclerView) findViewById(R.id.add_city_hot_city);
        add_city_search_list = (RecyclerView) findViewById(R.id.add_city_search_list);
        add_city_search = (SearchView) findViewById(R.id.add_city_search);


        add_city_search_list.setLayoutManager(new LinearLayoutManager(this));

        database = Utility.getDB(context);

        hot_city_layout = (LinearLayout) findViewById(R.id.hot_city_layout);


        //设置透明状态栏
        Utility.statusBar(getWindow());
    }
    private void setSearch(){
        add_city_search.setIconified(false);
        add_city_search.onActionViewExpanded();

        //获取到TextView的控件
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) add_city_search.findViewById(R.id.search_src_text);
        //设置字体大小
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);//14sp
        //设置字体颜色
        textView.setTextColor(Color.parseColor("#000000"));
        //设置提示文字颜色
        textView.setHintTextColor(Color.parseColor("#c3c3c3"));
        textView.setGravity(Gravity.CENTER_VERTICAL);
//设置字体
        Utility.setTypeFace(this, new TextView[]{
                add_city_CL01, add_city_CL02, textView
        });
    }
    private void listener(){
        hotCityAdapter.setItemClickListener(new HotCityAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0){
                    Intent intent = new Intent(SearchCityActivity.this, WeatherActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    add_city_search.setQuery(hotCityList[position], true);
                }
            }
        });
        add_city_search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                add_city_CL01.setVisibility(View.GONE);
                add_city_CL02.setVisibility(View.VISIBLE);
                add_city_hot_city.setVisibility(View.VISIBLE);
                add_city_search_list.setVisibility(View.GONE);
                return false;
            }
        });

        add_city_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                query(s);
                return false;
            }
        });

        add_city_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void query(final String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (s.length()>0){
                    add_city_CL01.setVisibility(View.VISIBLE);
                    add_city_CL01.setText("正在搜索");
                    hot_city_layout.setVisibility(View.GONE);
                    if (searchCity(s)){
                        add_city_CL01.setVisibility(View.GONE);
                        add_city_search_list.setVisibility(View.VISIBLE);
                    }else {
                        add_city_CL01.setVisibility(View.VISIBLE);
                        add_city_CL01.setText("无匹配城市");
                    }
                }else {
                    add_city_CL01.setVisibility(View.GONE);
                    hot_city_layout.setVisibility(View.VISIBLE);
                    add_city_search_list.setVisibility(View.GONE);
                }
            }
        });

    }
    private void hotCity(){
        add_city_hot_city.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));
        hotCityAdapter = new HotCityAdapter(this, hotCityList);
        add_city_hot_city.setAdapter(hotCityAdapter);
    }
    private boolean searchCity(String text){
        datas.clear();
        if (database!=null){
            Cursor cursor = database.query(
                    "list", null, "pinyin like ? or cn like ? or city like ?",
                    new String[]{ "%" + text + "%", "%" + text + "%","%" + text + "%"}, null, null, "_id asc", "30");
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String cn = cursor.getString(cursor.getColumnIndex("cn"));
                    String province = cursor.getString(cursor.getColumnIndex("province"));
                    String city = cursor.getString(cursor.getColumnIndex("city"));
                    String lonlat = cursor.getString(cursor.getColumnIndex("lonlat"));
                    String judge = cursor.getString(cursor.getColumnIndex("judge"));
                    datas.add(new CityList(cn, province, city, lonlat, judge));
                    cursor.moveToNext();
                }
            }else {
                return false;
            }
        }else {
            return false;
        }
        searchCityAdapter = new SearchCityAdapter(this, datas);
        add_city_search_list.setAdapter(searchCityAdapter);
        add_city_search_list.refreshDrawableState();
        searchCityAdapter.setItemClickListener(new SearchCityAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CityList cityList = datas.get(position);
                if (cityList.judge.equals("n")){
                    Utility.saveList(context, -1, cityList.cn, cityList.province, cityList.city, cityList.lonlat);
                    ContentValues cv = new ContentValues();
                    cv.put("judge","y");
                    database.update("list", cv, "lonlat = ?", new String[]{cityList.lonlat});
                    database.close();
                    finish();
                }
            }
        });
        return true;
    }
    @Override
    public void onBackPressed() {
        if (add_city_search_list.getVisibility() == View.VISIBLE){
            add_city_search.setQuery(null, false);
        }else {
            database.close();
            finish();
        }
    }
}
