package com.gene;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

//OkHttp的单例实现及配置
public class OkHttpManager {
    private static OkHttpClient instance;

    private OkHttpManager(){
    }

    public static OkHttpClient getInstance(){
        if (instance == null){
            instance = new OkHttpClient();
            instance.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
            instance.newBuilder().readTimeout(10, TimeUnit.SECONDS);
            instance.newBuilder().writeTimeout(10, TimeUnit.SECONDS);
        }
        return instance;
    }
}
