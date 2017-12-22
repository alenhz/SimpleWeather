package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alen on 2017/11/17.
 */

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