package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alen on 2017/11/27.
 */

public class CaiyunData {
    @SerializedName("hourly")
    public Hourly hourly;

    @SerializedName("minutely")
    public Minutely minutely;

    @SerializedName("alert")
    public Alert alert;
}
