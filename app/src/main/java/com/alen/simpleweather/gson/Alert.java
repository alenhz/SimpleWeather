package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alen on 2017/12/22.
 */

public class Alert {
    @SerializedName("code")
    public int code;

    @SerializedName("description")
    public String description;

    @SerializedName("pubdate")
    public String pubdate;

    @SerializedName("location")
    public String location;
}
