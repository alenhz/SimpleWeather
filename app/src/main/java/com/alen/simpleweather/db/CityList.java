package com.alen.simpleweather.db;

/**
 * Created by Alen on 2017/12/7.
 */


public class CityList{
    public String cn;
    public String province;
    public String city;
    public String lonlat;
    public String judge;

    public CityList(String cn, String province, String city, String lonlat, String judge) {
        this.cn = cn;
        this.province = province;
        this.city = city;
        this.lonlat = lonlat;
        this.judge = judge;
    }
}
