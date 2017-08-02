package com.globalformulae.shiguang.maininterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.WeatherBean24h;
import com.globalformulae.shiguang.bean.WeatherBean7D;
import com.globalformulae.shiguang.maininterface.adapter.Weather24HourAdapter;
import com.globalformulae.shiguang.maininterface.adapter.Weather7DayAdapter;
import com.globalformulae.shiguang.retrofit.WeatherService;
import com.globalformulae.shiguang.utils.IPConfig;

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
    @BindView(R.id.bg_ll)
    LinearLayout bgLL;
    private Retrofit retrofit;
    private WeatherService weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

//        BitmapFactory.Options options=new BitmapFactory.Options();
//        options.inPreferredConfig=Bitmap.Config.RGB_565;//先设为RGB_565模式
//        options.inSampleSize=2; //二分之一抽样
//        //原先图片加载为bitmap为35M，经过上边这两步处理之后变为4M
//        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.weatherbg,options);
//        Log.e("bitmapSize", String.valueOf(bitmap.getByteCount()));
//        Drawable drawable=new BitmapDrawable(getResources(),bitmap);
//        bgLL.setBackground(drawable);
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

    public void weatherToPicture(String weather) {
        switch (weather) {
            case "多云":
                Glide.with(this).load(R.mipmap.w1).into(weatherIV);
                break;
            case "晴天":
                Glide.with(this).load(R.mipmap.w0).into(weatherIV);
                break;
            case "阴":
                Glide.with(this).load(R.mipmap.w2).into(weatherIV);
                break;
            case "阵雨":
                Glide.with(this).load(R.mipmap.w4).into(weatherIV);
                break;
            case "雷阵雨":
                Glide.with(this).load(R.mipmap.w4).into(weatherIV);
                break;
            case "雨夹雪":
                Glide.with(this).load(R.mipmap.w6).into(weatherIV);
                break;
            case "小雨":
                Glide.with(this).load(R.mipmap.w7).into(weatherIV);
                break;
            case "中雨":
                Glide.with(this).load(R.mipmap.w301).into(weatherIV);
                break;
            case "大雨":
                Glide.with(this).load(R.mipmap.w9).into(weatherIV);
                break;
            case "小雪":
                Glide.with(this).load(R.mipmap.w14).into(weatherIV);
                break;
            case "中雪":
                Glide.with(this).load(R.mipmap.w15).into(weatherIV);
                break;
            case "大雪":
                Glide.with(this).load(R.mipmap.w16).into(weatherIV);
                break;
            case "雾":
                Glide.with(this).load(R.mipmap.w18).into(weatherIV);
                break;
            case "霾":
                Glide.with(this).load(R.mipmap.w53).into(weatherIV);
                break;
            case "雨":
                Glide.with(this).load(R.mipmap.w301).into(weatherIV);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
