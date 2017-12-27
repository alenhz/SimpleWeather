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

    public class Minutely {
        @SerializedName("description")
        public String description;
    }

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
}
