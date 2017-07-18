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
import com.globalformulae.shiguang.model.WeatherBean24h;

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
