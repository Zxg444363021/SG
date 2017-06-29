package com.globalformulae.shiguang.maininterface.MainFragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.model.WeatherBean24h;
import com.globalformulae.shiguang.utils.OkHttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getWeather(null);
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    public void weatherToPicture(String weather){
        switch (weather){
            case "多云":
                Glide.with(this).load(R.mipmap.cloudy).into(weatherIV);
                break;
        }
    }
}
