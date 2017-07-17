package com.globalformulae.shiguang.maininterface.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.model.AlternateRecord;
import com.globalformulae.shiguang.view.CircleImageView;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZXG on 2017/7/17.
 */

public class ComplexRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<AlternateRecord> mDataList;
    private LayoutInflater mLayoutInflater;

    public ComplexRecordAdapter(Context context, List<AlternateRecord> dataList){
        this.mContext=context;
        this.mDataList=dataList;
        this.mLayoutInflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1){
            View view=mLayoutInflater.inflate(R.layout.adapter_complex_record,parent,false);
            return new ComplexRecordViewHolder1(view);
        }else{
            View view=mLayoutInflater.inflate(R.layout.adapter_date_record,parent,false);
            return new ComplexRecordViewHolder2(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlternateRecord alternateRecord=mDataList.get(position);
        if(getItemViewType(position)==0){
            ((ComplexRecordViewHolder2)holder).dateTitleTV.setText(date2string(alternateRecord.getTime()));
        }else{
            ((ComplexRecordViewHolder1)holder).timeTV.setText(date2string2(alternateRecord.getTime()));
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataList.get(position).getType()==4){
            return 0;
        }else{
            return 1;
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
        if(theDay.after(today)){//是今天
            return "今天";
        }
        if(theDay.before(today)&&theDay.after(oneDay)){
            return "昨天";
        }
        else{
            String month=theDay.get(Calendar.MONTH)<9?"0"+(theDay.get(Calendar.MONTH)+1):String.valueOf(theDay.get(Calendar.MONTH)+1);
            String day=theDay.get(Calendar.DAY_OF_MONTH)<9?"0"+theDay.get(Calendar.DAY_OF_MONTH):String.valueOf(theDay.get(Calendar.DAY_OF_MONTH));
            return month+"-"+day;
        }
    }
    private String date2string2(Timestamp date){
        Calendar theDay=Calendar.getInstance();
        theDay.setTime(date);
        String hour=theDay.get(Calendar.HOUR_OF_DAY)<10?"0"+theDay.get(Calendar.HOUR_OF_DAY):String.valueOf(theDay.get(Calendar.HOUR_OF_DAY));
        String minute=theDay.get(Calendar.MINUTE)<10?"0"+theDay.get(Calendar.MINUTE):String.valueOf(theDay.get(Calendar.MINUTE));
        return hour+":"+minute;
    }

    class ComplexRecordViewHolder1 extends RecyclerView.ViewHolder{
        @BindView(R.id.complex_record_icon_iv)
        CircleImageView iconIV;
        @BindView(R.id.complex_record_user_tv)
        TextView userTV;
        @BindView(R.id.complex_record_action_tv)
        TextView actionTV;
        @BindView(R.id.complex_record_time_tv)
        TextView timeTV;

        public ComplexRecordViewHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    class ComplexRecordViewHolder2 extends RecyclerView.ViewHolder{
        @BindView(R.id.date_title_tv)
        TextView dateTitleTV;
        public ComplexRecordViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
