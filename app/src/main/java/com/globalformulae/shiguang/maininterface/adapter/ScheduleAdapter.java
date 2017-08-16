package com.globalformulae.shiguang.maininterface.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.Schedule;
import com.globalformulae.shiguang.greendao.DaoSession;
import com.globalformulae.shiguang.greendao.ScheduleDao;
import com.globalformulae.shiguang.maininterface.MyApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZXG on 2017/4/10.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.SchedlueViewHolder>{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Schedule> mDatas;
    private onScheduleItemClickListener onScheduleItemClickListener;

    DaoSession daoSession;
    ScheduleDao scheduleDao;
    final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            sort(mDatas,0,mDatas.size()-1);
        }
    };
    private int mYear;
    private int mMonth;
    private int mDay;

    public ScheduleAdapter(Context context, List<Schedule> datas,int year,int month,int day){
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mDatas=datas;
        daoSession = MyApplication.getDaoMaster().newSession();
        scheduleDao=daoSession.getScheduleDao();
        mYear=year;
        mMonth=month;
        mDay=day;
    }

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    @Override
    public SchedlueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.adapter_schedule,parent,false);
        return new SchedlueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SchedlueViewHolder holder, final int position) {
        final Schedule schedule=mDatas.get(position);
        holder.itemName.setText(schedule.getName());
        if(schedule.getMinute()<10){
            holder.itemTime.setText(schedule.getHour()+":0"+schedule.getMinute());
        }else {
            holder.itemTime.setText(schedule.getHour()+":"+schedule.getMinute());
        }

        if(schedule.getType()){
            holder.itemType.setChecked(true);
        }
        if(schedule.getStatus()==-1){
            holder.itemStatus.setChecked(true);
        }
        holder.itemType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(schedule.getType()==isChecked){
                }
                else{
                    if(schedule.getStatus()==0&&isChecked){
                        schedule.setStatus(1);
                    }else if(schedule.getStatus()==1&&!isChecked){
                        schedule.setStatus(0);
                    }
                    schedule.setType(isChecked);


                    scheduleDao.update(schedule);

                    QueryBuilder<Schedule> qb = scheduleDao.queryBuilder();
                    qb.where(qb.and(ScheduleDao.Properties.Year.eq(mYear),ScheduleDao.Properties.Month.eq(mMonth),
                            ScheduleDao.Properties.Day.eq(mDay)));
                    qb.orderDesc(ScheduleDao.Properties.Status);
                    qb.build();
                    mDatas=qb.list();
                    final int toPosition=getPosition(schedule);
                    final int layoutposition=holder.getLayoutPosition();
                    Handler handler=new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemMoved(layoutposition,toPosition);
                        }
                    });


                }
            }
        });
        holder.itemStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        schedule.setStatus(-1);
                    }else{
                        if(schedule.getType()){
                            schedule.setStatus(1);
                        }else{
                            schedule.setStatus(0);
                        }
                    }
                scheduleDao.update(schedule);

                QueryBuilder<Schedule> qb = scheduleDao.queryBuilder();
                qb.where(qb.and(ScheduleDao.Properties.Year.eq(mYear),ScheduleDao.Properties.Month.eq(mMonth),
                        ScheduleDao.Properties.Day.eq(mDay)));
                qb.orderDesc(ScheduleDao.Properties.Status);
                qb.build();
                mDatas=qb.list();
                final int layoutposition=holder.getLayoutPosition();
                final int toPosition=getPosition(schedule);
                Handler handler=new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemMoved(layoutposition,toPosition);
                    }
                });

            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int layoutposition=holder.getLayoutPosition();
                onScheduleItemClickListener.onItemClick(holder.item,layoutposition,mDatas.get(layoutposition));
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int layoutposition=holder.getLayoutPosition();
                notifyItemRemoved(layoutposition);
                scheduleDao.delete(mDatas.get(layoutposition));
                mDatas.remove(layoutposition);

                onScheduleItemClickListener.onItemLongClick(holder.item,layoutposition);
                return false;
            }
        });
    }
    public int getPosition(Schedule schedule){
        for(int i=0;i<mDatas.size();i++){
            if(mDatas.get(i).getId()==schedule.getId())
                return i;
        }
        return 0;
    }
    public  void sort(List<Schedule> list,int low,int high){
        if(list==null||list.size()==0||low<0||high>=list.size())
            return;
        if(low<high){
            int l=low;
            int h=high;
            Schedule temp=list.get(l);
            while(l<h){
                while(l<h&&list.get(h).getStatus()<=temp.getStatus())
                    h--;
                if(l<h)
                    list.set(l++,list.get(h));
                while(l<h&&list.get(l).getStatus()>=temp.getStatus())
                    l++;
                if(l<h)
                    list.set(h--,list.get(l));

            }
            list.set(l,temp);
            sort(list,low,l-1);
            sort(list,l+1,high);
        }
    }


    public  void add(Schedule schedule){
        //刷新数据
        QueryBuilder<Schedule> qb = scheduleDao.queryBuilder();
        qb.where(qb.and(ScheduleDao.Properties.Year.eq(schedule.getYear()),ScheduleDao.Properties.Month.eq(schedule.getMonth()),
                ScheduleDao.Properties.Day.eq(schedule.getDay())));
        qb.orderDesc(ScheduleDao.Properties.Status);
        qb.build();
        mDatas=qb.list();
        int toPosition=getPosition(schedule);     //获取应该添加到的位置
        notifyItemInserted(toPosition);

    }
    public void update(Schedule schedule){
        int fromPosition=getPosition(schedule);//获取原来的位置
        QueryBuilder<Schedule> qb = scheduleDao.queryBuilder();
        qb.where(qb.and(ScheduleDao.Properties.Year.eq(schedule.getYear()),ScheduleDao.Properties.Month.eq(schedule.getMonth()),
                ScheduleDao.Properties.Day.eq(schedule.getDay())));
        qb.orderDesc(ScheduleDao.Properties.Status);

        qb.build();
        mDatas=qb.list();
        int toPosition=getPosition(schedule);   //获取需要去的位置
        notifyItemMoved(fromPosition,toPosition);
        notifyItemChanged(toPosition);
    }

    public void setDatas(List<Schedule> list){
        mDatas=list;
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 点击事件回调接口
     */
    public interface onScheduleItemClickListener{
        void onItemClick(View view,int position,Schedule schedule);
        void onItemLongClick(View view,int position);
        void onStatusChecked(View view,int position);
        void onTypeChecked(View view,int position);
    }
    public void setOnScheduleItemClickListener(onScheduleItemClickListener mOnItemClickListener) {
        this.onScheduleItemClickListener = mOnItemClickListener;
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(observer);
    }
    class SchedlueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.event_item)
        LinearLayout item;
        @BindView(R.id.event_item_status)
        CheckBox itemStatus;
        @BindView(R.id.event_item_type)
        CheckBox itemType;
        @BindView(R.id.event_item_name)
        TextView itemName;
        @BindView(R.id.event_item_time)
        TextView itemTime;


        public SchedlueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}





