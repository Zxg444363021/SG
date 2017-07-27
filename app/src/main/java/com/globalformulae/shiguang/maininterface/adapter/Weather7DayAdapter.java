package com.globalformulae.shiguang.maininterface.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.WeatherBean7D;

import java.util.List;

/**
 * Created by ZXG on 2017/6/30.
 */

public class Weather7DayAdapter extends RecyclerView.Adapter<Weather7DayAdapter.Weather7DayViewHolder>{
    private List<WeatherBean7D.ShowAPIResBody.DayWeather> mDataList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public Weather7DayAdapter(List<WeatherBean7D.ShowAPIResBody.DayWeather> mDataList, Context mContext) {
        this.mDataList = mDataList;
        this.mContext = mContext;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public Weather7DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Weather7DayViewHolder(mLayoutInflater.inflate(R.layout.adapter_7_days_weather,parent,false));
    }

    @Override
    public void onBindViewHolder(Weather7DayViewHolder holder, int position) {
        if(position==0){
            holder.dayOfWeekTV.setText("今天");
        }else{
            holder.dayOfWeekTV.setText(getDayOfWeek(mDataList.get(position).getWeekday()));
        }
        setIcon(holder,mDataList.get(position).getDay_weather_code());
        holder.dayTemperatureTV.setText(mDataList.get(position).getDay_air_temperature()+"℃");
        holder.nightTemperatureTV.setText(mDataList.get(position).getNight_air_temperature()+"℃");
        holder.weatherTV.setText(getWeather(mDataList.get(position).getDay_weather(),mDataList.get(position).getNight_weather()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 返回中文星期
     * @param index
     * @return
     */
    public String getDayOfWeek(int index){
        String result="";
        switch (index){
            case 1:
                result="周一";
                break;
            case 2:
                result="周二";
                break;
            case 3:
                result="周三";
                break;
            case 4:
                result="周四";
                break;
            case 5:
                result="周五";
                break;
            case 6:
                result="周六";
                break;
            case 7:
                result="周日";
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 连接早晚天气
     * @param day
     * @param night
     * @return
     */
    public String getWeather(String day,String night){
        if(day.equals(night))
            return day;
        else
            return day+"转"+night;
    }
    public void setIcon(Weather7DayViewHolder holder,int dayT){
        switch (dayT){
            case 0:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w0));
                break;
            case 1:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w1));
                break;
            case 2:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w2));
                break;
            case 3:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w4));
                break;
            case 4:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w4));
                break;
            case 5:

                break;
            case 6:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w6));
                break;
            case 7:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w7));
                break;
            case 8:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w301));
                break;
            case 9:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w9));
                break;
            case 10:

                break;
            case 11:

                break;
            case 12:

                break;
            case 13:

                break;
            case 14:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w14));
                break;
            case 15:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w15));
                break;
            case 16:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w16));
                break;
            case 17:

                break;
            case 18:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w18));
                break;
            case 19:

                break;
            case 20:

                break;
            case 21:

                break;
            case 22:

                break;
            case 23:

                break;
            case 24:

                break;
            case 25:

                break;
            case 26:

                break;
            case 27:

                break;
            case 28:

                break;
            case 29:

                break;
            case 30:

                break;
            case 31:

                break;
            case 53:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w53));
                break;
            case 99:

                break;
            case 301:
                holder.iconOfWeatherIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.w301));
                break;
            case 302:

                break;
            default:
                break;
        }
    }

    class Weather7DayViewHolder extends RecyclerView.ViewHolder{
        TextView dayOfWeekTV;
        ImageView iconOfWeatherIV;
        TextView dayTemperatureTV;
        TextView nightTemperatureTV;
        TextView weatherTV;
        public Weather7DayViewHolder(View itemView) {
            super(itemView);
            dayOfWeekTV= (TextView) itemView.findViewById(R.id.day_of_week_tv);
            iconOfWeatherIV= (ImageView) itemView.findViewById(R.id.icon_of_weather_iv);
            dayTemperatureTV= (TextView) itemView.findViewById(R.id.day_temperature_tv);
            nightTemperatureTV= (TextView) itemView.findViewById(R.id.night_temperature_tv);
            weatherTV= (TextView) itemView.findViewById(R.id.weather_tv);
        }
    }
}
