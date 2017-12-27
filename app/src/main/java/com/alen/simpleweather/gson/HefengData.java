package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alen on 2017/11/17.
 */

public class HefengData {
    public String status;
    public Basic basic;
    public Now now;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    @SerializedName("lifestyle")
    public List<LifeStyle> lifeStyleList;


    public class Basic {
        @SerializedName("location")
        public String location;

        @SerializedName("parent_city")
        public String parent_city;

        @SerializedName("admin_area")
        public String admin_area;

        @SerializedName("lon")
        public String lon;

        @SerializedName("lat")
        public String lat;
    }

    public class Now {
        @SerializedName("tmp")
        public String tmp;

        @SerializedName("fl")
        public String fl;

        @SerializedName("cond_txt")
        public String cond_txt;

        @SerializedName("wind_dir")
        public String wind_dir;

        @SerializedName("wind_sc")
        public String wind_sc;

        @SerializedName("hum")
        public String hum;

        @SerializedName("cond_code")
        public int cond_code;
    }

    public class Forecast {
        public String date;

        @SerializedName("sr")
        public String sr;

        @SerializedName("ss")
        public String ss;

        @SerializedName("tmp_max")
        public int tmp_max;

        @SerializedName("tmp_min")
        public int tmp_min;

        @SerializedName("cond_txt_d")
        public String cond_txt_d;

        @SerializedName("cond_txt_n")
        public String cond_txt_n;

        @SerializedName("wind_dir")
        public String wind_dir;

        @SerializedName("wind_sc")
        public String wind_sc;

        @SerializedName("hum")
        public String hum;

        @SerializedName("cond_code_d")
        public int cond_code_d;

        @SerializedName("cond_code_n")
        public int cond_code_n;
    }

    public class LifeStyle {
        @SerializedName("brf")
        public String brf;

        @SerializedName("txt")
        public String txt;
    }
}
