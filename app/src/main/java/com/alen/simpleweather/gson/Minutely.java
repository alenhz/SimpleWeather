package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alen on 2017/12/22.
 */

public class Minutely {
    @SerializedName("precipitation_2h")
    public int[] precipitation_2h;

    @SerializedName("description")
    public String description;
}
