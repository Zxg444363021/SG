package com.globalformulae.shiguang.retrofit;

import com.globalformulae.shiguang.bean.WeatherBean24h;
import com.globalformulae.shiguang.bean.WeatherBean7D;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by ZXG on 2017/7/18.
 */

public interface WeatherService {
    @Headers("Authorization: APPCODE 37b0d40affd14dfd8de3fd1925f862b8")
    @GET("hour24")
    Observable<WeatherBean24h> doGetWeatherToday(@Query("areaid") String areaid);


    @Headers("Authorization: APPCODE 37b0d40affd14dfd8de3fd1925f862b8")
    @GET("area-to-weather")
    Observable<WeatherBean7D> doGetWeather7Days(@Query("areaid") String areaid,@Query("needMoreDay")String needMoreDay);

}
