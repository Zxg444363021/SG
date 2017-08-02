package com.globalformulae.shiguang.maininterface.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.WeatherBean24h;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZXG on 2017/7/18.
 */

public class Weather24HourAdapter extends RecyclerView.Adapter<Weather24HourAdapter.Weather24HourViewHolder> {

    private Context mContext;
    private List<WeatherBean24h.ShowAPIBody.HourBody> mDataList;
    private LayoutInflater mLayoutInflater;

    public Weather24HourAdapter(Context mContext, List<WeatherBean24h.ShowAPIBody.HourBody> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public Weather24HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.adapter_24_hours_weather,parent,false);
        return new Weather24HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Weather24HourViewHolder holder, int position) {
        String time=mDataList.get(position).getTime().substring(8,10)
                +":"+mDataList.get(position).getTime().substring(10,12);
        holder.hourTV.setText(time);
        setIcon(holder,mDataList.get(position).getWeather_code());
        holder.hourTemperatureTV.setText(mDataList.get(position).getTemperature()+"℃");
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setIcon(Weather24HourViewHolder holder,int dayT){
        switch (dayT){
            case 0:
                Glide.with(mContext).load(R.mipmap.w0).into(holder.iconOfWeatherIV);
                break;
            case 1:
                Glide.with(mContext).load(R.mipmap.w1).into(holder.iconOfWeatherIV);
                break;
            case 2:
                Glide.with(mContext).load(R.mipmap.w2).into(holder.iconOfWeatherIV);
                break;
            case 3:
                Glide.with(mContext).load(R.mipmap.w4).into(holder.iconOfWeatherIV);
                break;
            case 4:
                Glide.with(mContext).load(R.mipmap.w4).into(holder.iconOfWeatherIV);
                break;
            case 5:

                break;
            case 6:
                Glide.with(mContext).load(R.mipmap.w6).into(holder.iconOfWeatherIV);
                break;
            case 7:
                Glide.with(mContext).load(R.mipmap.w7).into(holder.iconOfWeatherIV);
                break;
            case 8:
                Glide.with(mContext).load(R.mipmap.w301).into(holder.iconOfWeatherIV);
                break;
            case 9:
                Glide.with(mContext).load(R.mipmap.w9).into(holder.iconOfWeatherIV);
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
                Glide.with(mContext).load(R.mipmap.w14).into(holder.iconOfWeatherIV);
                break;
            case 15:
                Glide.with(mContext).load(R.mipmap.w15).into(holder.iconOfWeatherIV);
                break;
            case 16:
                Glide.with(mContext).load(R.mipmap.w16).into(holder.iconOfWeatherIV);
                break;
            case 17:

                break;
            case 18:
                Glide.with(mContext).load(R.mipmap.w18).into(holder.iconOfWeatherIV);
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
                Glide.with(mContext).load(R.mipmap.w53).into(holder.iconOfWeatherIV);
                break;
            case 99:

                break;
            case 301:
                Glide.with(mContext).load(R.mipmap.w301).into(holder.iconOfWeatherIV);
                break;
            case 302:

                break;
            default:
                break;
        }
    }

    class  Weather24HourViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.hour_tv)
        TextView hourTV;
        @BindView(R.id.hour_icon_iv)
        ImageView iconOfWeatherIV;
        @BindView(R.id.hour_temperature_tv)
        TextView hourTemperatureTV;
        public Weather24HourViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
