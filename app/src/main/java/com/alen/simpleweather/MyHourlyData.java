package com.alen.simpleweather;

import com.alen.simpleweather.view.IBaseWeatherData;

/**
 * Created by Alen on 2017/11/27.
 */

public class MyHourlyData implements IBaseWeatherData {
    public int[] degree;
    public String aqi;
    public int aqiBG;
    public String time;
    public String precipitation;
    @Override
    public int[] getDegree() {
        return degree;
    }

    public MyHourlyData(int[] degree, String aqi, int aqiBG, String time, String precipitation) {
        this.degree = degree;
        this.aqi = aqi;
        this.aqiBG = aqiBG;
        this.time = time;
        this.precipitation = precipitation;
    }
}
