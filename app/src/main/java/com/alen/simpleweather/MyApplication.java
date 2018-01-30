package com.alen.simpleweather;

import android.app.Application;

/**
 * Created by Alen on 2018/1/30.
 */

public class MyApplication extends Application{

    private static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static MyApplication getInstance(){
        return app;
    }
}
