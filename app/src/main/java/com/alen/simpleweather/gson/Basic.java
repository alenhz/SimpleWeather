package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alen on 2017/11/17.
 */

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