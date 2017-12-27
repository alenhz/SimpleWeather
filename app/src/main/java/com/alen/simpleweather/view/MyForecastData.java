package com.alen.simpleweather.view;

import android.graphics.Bitmap;

import com.alen.simpleweather.view.IBaseWeatherData;

/**
 * Created by Alen on 2017/11/23.
 */

public class MyForecastData implements IBaseWeatherData {
    public int[] degree;
    public String date;
    public String cond_txt_d;
    public String cond_txt_n;
    public Bitmap cond_code_d;
    public Bitmap cond_code_n;
    public String wind_dir;
    public String wind_sc;
    public String week;


    public MyForecastData(int[] degree, String date, String cond_txt_d,
                          String cond_txt_n, Bitmap cond_code_d, Bitmap cond_code_n,
                          String wind_dir, String wind_sc, String week) {
        this.degree = degree;
        this.date = date;
        this.cond_txt_d = cond_txt_d;
        this.cond_txt_n = cond_txt_n;
        this.cond_code_d = cond_code_d;
        this.cond_code_n = cond_code_n;
        this.wind_dir = wind_dir;
        this.wind_sc = wind_sc;
        this.week = week;
    }

    @Override
    public int[] getDegree() {
        return degree;
    }


}
