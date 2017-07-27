package com.globalformulae.shiguang.maininterface;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.maininterface.adapter.Weather24HourAdapter;
import com.globalformulae.shiguang.maininterface.adapter.Weather7DayAdapter;
import com.globalformulae.shiguang.bean.WeatherBean24h;
import com.globalformulae.shiguang.bean.WeatherBean7D;
import com.globalformulae.shiguang.retrofit.WeatherService;
import com.globalformulae.shiguang.utils.IPConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {
    @BindView(R.id.city_btn)
    Button cityBTN;
    @BindView(R.id.weather_iv)
    ImageView weatherIV;
    @BindView(R.id.temperature_tv)
    TextView temperatureTV;
    @BindView(R.id.wind_power_tv)
    TextView windPowerTV;
    @BindView(R.id.weather_tv)
    TextView weatherTV;
    @BindView(R.id.weather_7d_rv)
    RecyclerView weather7dRV;
    @BindView(R.id.weather_24h_rv)
    RecyclerView weather24HRV;
    @BindView(R.id.aqi_tv)
    TextView aqiTV;
    @BindView(R.id.wet_percent_TV)
    TextView wetPercentTV;
    private Retrofit retrofit;
    private WeatherService weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        retrofit = new Retrofit.Builder()
                .baseUrl(IPConfig.getUrl("weatherIp"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        weatherService = retrofit.create(WeatherService.class);
        getWeather24H(null);
        getWeather7d(null);

    }

    public void getWeather7d(String areaid) {
        weatherService.doGetWeather7Days("101200101","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<WeatherBean7D>() {
                    @Override
                    public void accept(@NonNull WeatherBean7D weatherBean7D) throws Exception {
                        WeatherBean7D.ShowAPIResBody showAPIResBody = weatherBean7D.getShowapi_res_body();
                        List<WeatherBean7D.ShowAPIResBody.DayWeather> dataList = new ArrayList<>();
                        dataList.add(showAPIResBody.getF1());
                        dataList.add(showAPIResBody.getF2());
                        dataList.add(showAPIResBody.getF3());
                        dataList.add(showAPIResBody.getF4());
                        dataList.add(showAPIResBody.getF5());
                        dataList.add(showAPIResBody.getF6());
                        dataList.add(showAPIResBody.getF7());
                        Weather7DayAdapter weather7DayAdapter = new Weather7DayAdapter(dataList,WeatherActivity.this);
                        weather7dRV.setAdapter(weather7DayAdapter);
                        weather7dRV.setLayoutManager(new LinearLayoutManager(WeatherActivity.this, LinearLayoutManager.HORIZONTAL, false));

                        WeatherBean7D.ShowAPIResBody.Now now=showAPIResBody.getNow();
                        temperatureTV.setText(now.getTemperature()+"℃");
                        weatherToPicture(now.getWeather());
                        weatherTV.setText(now.getWeather());
                        aqiTV.setText("   "+now.getAqi()+" "+now.getAqiDetail().getQuality());
                        wetPercentTV.setText("   "+now.getSd());

                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })
                .subscribe();
    }

    public void getWeather24H(String areaid) {
        weatherService.doGetWeatherToday("101200101")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<WeatherBean24h>() {
                    @Override
                    public void accept(@NonNull WeatherBean24h weatherBean24h) throws Exception {
                        List<WeatherBean24h.ShowAPIBody.HourBody> hourList = weatherBean24h.getShowapi_res_body().getHourList();

                        windPowerTV.setText("    "+hourList.get(0).getWind_power());

                        Weather24HourAdapter weather24HourAdapter=new Weather24HourAdapter(WeatherActivity.this,hourList);
                        weather24HRV.setAdapter(weather24HourAdapter);
                        weather24HRV.setLayoutManager(new LinearLayoutManager(WeatherActivity.this,LinearLayoutManager.HORIZONTAL,false));


                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })
                .subscribe();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    public void weatherToPicture(String weather) {
        switch (weather) {
            case "多云":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w1));
                break;
            case "晴天":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w0));
                break;
            case "阴":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w2));
                break;
            case "阵雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w4));
                break;
            case "雷阵雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w4));
                break;
            case "雨夹雪":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w6));
                break;
            case "小雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w7));
                break;
            case "中雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w301));
                break;
            case "大雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w9));
                break;
            case "小雪":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w14));
                break;
            case "中雪":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w15));
                break;
            case "大雪":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w16));
                break;
            case "雾":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w18));
                break;
            case "霾":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w53));
                break;
            case "雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.w301));
                break;

        }
    }
}
