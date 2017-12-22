package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alen on 2017/11/17.
 */

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