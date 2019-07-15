package com.example.yf.testgitapplication;

import android.app.Application;

import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;

/**
 * Created by 杨帆 on 2018/8/22.
 */

public class MyApplication extends Application {
    private static MyApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;
        InitializationConfig config = InitializationConfig.newBuilder(this).connectionTimeout(30 * 1000).readTimeout(30 * 1000).retry(10).build();
        NoHttp.initialize(config);
    }

    public static  MyApplication  getInstance() {
        return mApp;
    }
}
