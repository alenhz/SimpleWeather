package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alen on 2017/11/17.
 */

public class Weather {
    public String status;
    public Basic basic;
    public Now now;
    public UpDate upDate;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    @SerializedName("lifestyle")
    public List<LifeStyle> lifeStyleList;
}
