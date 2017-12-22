package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alen on 2017/11/28.
 */

public class HourlyTemperature {
    @SerializedName("value")
    public String value;

    @SerializedName("datetime")
    public String datetime;
}
