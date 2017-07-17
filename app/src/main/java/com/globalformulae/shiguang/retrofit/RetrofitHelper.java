package com.globalformulae.shiguang.retrofit;

import com.globalformulae.shiguang.utils.IPConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ZXG on 2017/7/17.
 */

public class RetrofitHelper {
    private static Retrofit userRetrofit=new Retrofit.Builder()
            .baseUrl(IPConfig.getUrl("serverIp"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static Retrofit getInstance(){
        return userRetrofit;
    }

}
