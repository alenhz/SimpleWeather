package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alen on 2017/11/27.
 */

public class CaiyunData {
    public String status;
    public String description;

    @SerializedName("aqi")
    public List<HourlyAQI> hourlyAQIList;

    @SerializedName("temperature")
    public List<HourlyTemperature> hourlyTemperatureList;

    @SerializedName("precipitation")
    public List<HourlyPrecipitation> hourlyPrecipitationList;
}
