package com.globalformulae.shiguang.maininterface.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.model.AlternateRecord;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * timer页面的5条记录
 * Created by ZXG on 2017/5/28.
 */

public class SimpleRecordAdapter extends RecyclerView.Adapter<SimpleRecordViewHolder> {
    private Context mContext;
    private List<AlternateRecord> mDatas;
    private LayoutInflater mInflater;

    public SimpleRecordAdapter(Context context, List<AlternateRecord> datas) {
        mContext=context;
        mDatas=datas;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public SimpleRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.adapter_simple_record,parent,false);
        SimpleRecordViewHolder viewHolder=new SimpleRecordViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleRecordViewHolder holder, int position) {
        //holder.simpleRecordUserTV.setText(mDatas.get(position).getUser1name());
        if(mDatas.get(position).getType()==1||mDatas.get(position).getType()==2){
           holder.simpleRecordActionTV.setText("收取"+mDatas.get(position).getPower()+"g");
        }else{
            holder.simpleRecordActionTV.setText("来浇过水，+10g能量");
        }
        holder.simpleRecordTimeTV.setText(date2string(mDatas.get(position).getTime()));
    }

    /**
     * 最多只显示5条记录
     * @return
     */
    @Override
    public int getItemCount() {
        if (mDatas.size()>5){
            return 5;
        }else{
            return mDatas.size();
        }

    }

    /**
     * 将日期转换为“昨天”...
     * @param date
     * @return
     */
    private  String date2string(Timestamp date){
        Calendar today=Calendar.getInstance();
        System.out.println("zzz1"+today.getTime().toString());
        today.set(Calendar.HOUR_OF_DAY,0);
        today.set(Calendar.MINUTE,0);
        Calendar theDay=Calendar.getInstance();
        theDay.setTime(date);
        System.out.println("zzz2"+theDay.getTime().toString());
        Calendar oneDay=Calendar.getInstance();
        oneDay.set(Calendar.HOUR_OF_DAY,0);
        oneDay.set(Calendar.MINUTE,0);
        oneDay.add(Calendar.DAY_OF_MONTH, -2);
        System.out.println("zzz3"+oneDay.getTime().toString());
        if(theDay.after(today)){
            java.util.Date  date2=theDay.getTime();
            String month=date2.getHours()<10?"0"+date2.getHours():String.valueOf(date2.getHours());
            String day=date2.getMinutes()<10?"0"+date2.getMinutes():String.valueOf(date2.getMinutes());
            return month+":"+day;
        }
        if(theDay.before(today)&&theDay.after(oneDay)){
            return "昨天";
        }
        oneDay.add(Calendar.DAY_OF_MONTH, -1);
        today.add(Calendar.DAY_OF_MONTH, -1);
        if(theDay.before(today)&&theDay.after(oneDay)){
            return "1天前";
        }
        oneDay.add(Calendar.DAY_OF_MONTH, -1);
        today.add(Calendar.DAY_OF_MONTH, -1);
        if(theDay.before(today)&&theDay.after(oneDay)){
            return "2天前";
        }
        oneDay.add(Calendar.DAY_OF_MONTH, -1);
        today.add(Calendar.DAY_OF_MONTH, -1);
        if(theDay.before(today)&&theDay.after(oneDay)){
            return "3天前";
        }
        oneDay.add(Calendar.DAY_OF_MONTH, -1);
        today.add(Calendar.DAY_OF_MONTH, -1);
        if(theDay.before(today)&&theDay.after(oneDay)){
            return "4天前";
        }
        oneDay.add(Calendar.DAY_OF_MONTH, -1);
        today.add(Calendar.DAY_OF_MONTH, -1);
        if(theDay.before(today)&&theDay.after(oneDay)){
            return "5天前";
        }else{
            java.util.Date  date2=theDay.getTime();
            String month=date2.getMonth()<10?"0"+date2.getMonth()+1:String.valueOf(date2.getMonth());
            String day=date2.getDay()<10?"0"+date2.getDay():String.valueOf(date2.getDay());
            return month+"-"+day;
        }
    }

}
class SimpleRecordViewHolder extends RecyclerView.ViewHolder{
    TextView simpleRecordUserTV;
    TextView simpleRecordActionTV;
    TextView simpleRecordTimeTV;


    public SimpleRecordViewHolder(View itemView) {
        super(itemView);
        simpleRecordUserTV= (TextView) itemView.findViewById(R.id.simple_record_user_tv);
        simpleRecordActionTV= (TextView) itemView.findViewById(R.id.simple_record_action_tv);
        simpleRecordTimeTV= (TextView) itemView.findViewById(R.id.simple_record_time_tv);
    }
}