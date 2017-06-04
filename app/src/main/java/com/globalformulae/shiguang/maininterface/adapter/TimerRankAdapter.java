package com.globalformulae.shiguang.maininterface.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.model.User;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/6/4.
 */

public class TimerRankAdapter extends RecyclerView.Adapter<TimerRankAdapterViewHolder>{
    private Context mContext;
    private List<User> mDatas;
    private LayoutInflater layoutInflater;
    private Typeface mtypeface;//字体
    public TimerRankAdapter(Context mContext, List<User> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.layoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public TimerRankAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.adapter_timer_rank,parent,false);
        mtypeface=Typeface.createFromAsset(mContext.getAssets(),"BAUHS93.TTF");
        return new TimerRankAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimerRankAdapterViewHolder holder, int position) {
        User u=mDatas.get(position);
        if(position==0){
            Bitmap bitmap= BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.rank1);
            holder.rankIV.setImageBitmap(bitmap);
            holder.rankTV.setVisibility(View.GONE);
        }else if(position==1){
            Bitmap bitmap= BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.rank2);
            holder.rankIV.setImageBitmap(bitmap);
            holder.rankTV.setVisibility(View.GONE);
        }else if(position==2){
            Bitmap bitmap= BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.rank3);
            holder.rankIV.setImageBitmap(bitmap);
            holder.rankTV.setVisibility(View.GONE);
        }else{
            holder.rankIV.setVisibility(View.GONE);
            holder.rankTV.setTypeface(mtypeface);
            holder.rankTV.setText(String.valueOf(position+1));
        }
        Glide.with(mContext).load(u.getIcon()).placeholder(R.mipmap.unlogged_icon).into(holder.userIconIV);
        holder.userNameTV.setText(u.getName());
        if(u.getTomato_n()>0)
            holder.tomatoNTV.setText("已收获"+u.getTomato_n()+"个番茄");
        else
            holder.tomatoNTV.setText("");
        if(u.getPower_n()>1000){
            String kg=new DecimalFormat("#.0").format(u.getPower_n()/1000.0);
            holder.powerTV.setText(kg+"kg");
            kg=null;
        }else{
            holder.powerTV.setText(u.getPower_n()+"g");
        }

        u=null;
    }

    @Override
    public int getItemCount() {//最多只显示10个
        if(mDatas.size()>10){
            return 10;
        }else{
            return mDatas.size();
        }
    }

}


class TimerRankAdapterViewHolder extends RecyclerView.ViewHolder{
    ImageView rankIV;
    TextView rankTV;
    ImageView userIconIV;
    TextView userNameTV;
    TextView tomatoNTV;
    TextView powerTV;

    public TimerRankAdapterViewHolder(View itemView) {
        super(itemView);
        rankIV= (ImageView) itemView.findViewById(R.id.timer_rank_iv);
        rankTV= (TextView) itemView.findViewById(R.id.timer_rank_tv);
        userIconIV= (ImageView) itemView.findViewById(R.id.timer_rank_icon_iv);
        userNameTV= (TextView) itemView.findViewById(R.id.timer_rank_name_tv);
        tomatoNTV= (TextView) itemView.findViewById(R.id.timer_rank_tomato_n_tv);
        powerTV= (TextView) itemView.findViewById(R.id.timer_rank_power_tv);

    }
}
