package com.alen.simpleweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alen on 2017/12/22.
 */

public class Hourly {
    @SerializedName("status")
    public String status;

    @SerializedName("description")
    public String description;

    @SerializedName("aqi")
    public List<HourlyAQI> aqi;

    @SerializedName("temperature")
    public List<HourlyTemperature> temperature;

    @SerializedName("precipitation")
    public List<HourlyPrecipitation> precipitation;

    public class HourlyAQI {
        @SerializedName("value")
        public String value;
    }

    public class HourlyTemperature {
        @SerializedName("value")
        public String value;

        @SerializedName("datetime")
        public String datetime;
    }

    public class HourlyPrecipitation {
        @SerializedName("value")
        public String value;
    }
}
