package com.globalformulae.shiguang.maininterface.MainFragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.maininterface.adapter.Weather7DayAdapter;
import com.globalformulae.shiguang.model.WeatherBean24h;
import com.globalformulae.shiguang.model.WeatherBean7D;
import com.globalformulae.shiguang.utils.OkHttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getWeather(null);
        getWeather7d(null);

    }


    public void getWeather7d(String areaid){
        String host = "http://saweather.market.alicloudapi.com";
        String path = "/area-to-weather";
        String method = "GET";
        String appcode = "37b0d40affd14dfd8de3fd1925f862b8";
        Request request=new Request.Builder()
                .url(host+path+"?areaid=101200101"+"&&needMoreDay=1")
                .addHeader("Authorization", "APPCODE " + appcode)
                .get()
                .build();
        OkHttpUtil.getInstance().getWeather7d(request);
    }
    public void getWeather(String areaid){
        String host = "http://saweather.market.alicloudapi.com";
        String path = "/hour24";
        String method = "GET";
        String appcode = "37b0d40affd14dfd8de3fd1925f862b8";
        Request request=new Request.Builder()
                .url(host+path+"?areaid=101200101")
                .addHeader("Authorization", "APPCODE " + appcode)
                .get()
                .build();
        OkHttpUtil.getInstance().getWeather(request);
    }


    @Subscribe(threadMode=ThreadMode.MAIN)
    public void shown(WeatherBean24h weatherBean24h){
        Log.e("shoudao  l", "shown: ");
        WeatherBean24h.ShowAPIBody.HourBody[] hourList=weatherBean24h.getShowapi_res_body().getHourList();
        temperatureTV.setText(hourList[0].getTemperature()+"℃");
        weatherTV.setText(hourList[0].getWeather());
        windPowerTV.setText(hourList[0].getWind_power());
        weatherToPicture(hourList[0].getWeather());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shown1(WeatherBean7D weatherBean7D){
        WeatherBean7D.ShowAPIResBody showAPIResBody=weatherBean7D.getShowapi_res_body();
        List<WeatherBean7D.ShowAPIResBody.DayWeather> dataList=new ArrayList<>();
        dataList.add(showAPIResBody.getF1());
        dataList.add(showAPIResBody.getF2());
        dataList.add(showAPIResBody.getF3());
        dataList.add(showAPIResBody.getF4());
        dataList.add(showAPIResBody.getF5());
        dataList.add(showAPIResBody.getF6());
        dataList.add(showAPIResBody.getF7());
        Weather7DayAdapter weather7DayAdapter=new Weather7DayAdapter(dataList,this);
        weather7dRV.setAdapter(weather7DayAdapter);
        weather7dRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    public void weatherToPicture(String weather){
        switch (weather){
            case "多云":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w1));
                break;
            case "晴天":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w0));
                break;
            case "阴":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w2));
                break;
            case "阵雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w4));
                break;
            case "雷阵雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w4));
                break;
            case "雨夹雪":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w6));
                break;
            case "小雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w7));
                break;
            case "中雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w301));
                break;
            case "大雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w9));
                break;
            case "小雪":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w14));
                break;
            case "中雪":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w15));
                break;
            case "大雪":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w16));
                break;
            case "雾":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w18));
                break;
            case "霾":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w53));
                break;
            case "雨":
                weatherIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.w301));
                break;

        }
    }
}
