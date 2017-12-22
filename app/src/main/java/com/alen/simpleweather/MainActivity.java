package com.alen.simpleweather;


import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alen.simpleweather.gson.MyCity;
import com.alen.simpleweather.util.LBS;
import com.alen.simpleweather.util.Permission;
import com.alen.simpleweather.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utility.statusBar(getWindow());
        SharedPreferences weatherprefs = getSharedPreferences("weatherdata",MODE_PRIVATE);
        Permission permission = new Permission();
        if (permission.start(this)){
            go();
        }
        Button button = findViewById(R.id.main_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go();
            }
        });
    }
    private void go(){
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
        finish();
    }
}


