package com.alen.simpleweather.gson;

/**
 * Created by Alen on 2017/12/13.
 */

public class MyCity {
    public String cn;
    public String province;
    public String city;
    public String lonlat;
    public String tmp;
    public int tmp_code;


    public MyCity(String cn, String province, String city, String lonlat) {
        this.cn = cn;
        this.province = province;
        this.city = city;
        this.lonlat = lonlat;
    }

    public MyCity(String cn, String province, String city, String lonlat, String tmp, int tmp_code) {
        this.cn = cn;
        this.province = province;
        this.city = city;
        this.lonlat = lonlat;
        this.tmp = tmp;
        this.tmp_code = tmp_code;
    }
}
