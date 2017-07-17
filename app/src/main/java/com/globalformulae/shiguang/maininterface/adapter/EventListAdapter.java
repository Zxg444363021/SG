package com.globalformulae.shiguang.maininterface.adapter;

/**
 * Created by ZXG on 2017/3/1.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.model.NBAEvents;

import java.util.List;

/**使用了recycleView
 *
 */

public class EventListAdapter extends  RecyclerView.Adapter<EventListAdapter.CompatitionViewHolder>{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<NBAEvents.EventsResult.DayResult.Compatition> mDatas;
    private ItemClickListener mItemClickListener;
    public interface ItemClickListener{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }


    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * 构造函数
     * @param context 要传入的Activity
     * @param datas   list的数据
     */
    public EventListAdapter(Context context,List<NBAEvents.EventsResult.DayResult.Compatition> datas){
        this.mContext=context;
        this.mDatas=datas;
        mInflater=LayoutInflater.from(mContext);
    }


    /**
     * 创建viewHolder
     * @param parent 所要显示的父组件
     * @param viewType
     * @return
     */
    @Override
    public CompatitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.adapter_compatition,parent,false);//获取item的layout，设置到父组件里，
        CompatitionViewHolder viewHolder=new CompatitionViewHolder(view); //构造ViewHolder 把Item试图传进去
        return viewHolder;
    }

    /**viewHolder,和上面是分开的
     * 设置
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final CompatitionViewHolder holder, final int position) {
        NBAEvents.EventsResult.DayResult.Compatition data=mDatas.get(position);
        holder.compatition_time.setText(data.getTime());
        Glide.with(mContext).load(data.getPlayer1logo()).into(holder.player1logo);
        Glide.with(mContext).load(data.getPlayer2logo()).into(holder.player2logo);
        holder.player1.setText(data.getPlayer1());
        holder.player2.setText(data.getPlayer2());
        holder.score.setText(data.getScore());
        switch(data.getStatus()){
            case "0":
                holder.status.setText("未开始");
                break;
            case "1":
                holder.status.setText("进行中");
                break;
            case "2":
                holder.status.setText("已结束");
                break;
        }

        if(mItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int layoutPosition=holder.getLayoutPosition();
                    mItemClickListener.onItemClick(holder.itemView,layoutPosition);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int layoutPosition=holder.getLayoutPosition();
                    mItemClickListener.onItemLongClick(holder.itemView,layoutPosition);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }




    public void setmDatas(List<NBAEvents.EventsResult.DayResult.Compatition> mDatas) {
        this.mDatas = mDatas;
    }
    /**
     * ViewHolder，与直接使用listview不同，这里会初始化控件
     */
    class CompatitionViewHolder extends RecyclerView.ViewHolder {
        TextView compatition_time;
        ImageView player1logo;
        ImageView player2logo;
        TextView player1;
        TextView player2;
        TextView score;
        TextView status;

        /**
         * @param itemView 每一个item，其Layout已经定义
         */
        public CompatitionViewHolder(View itemView) {
            super(itemView);
            compatition_time = (TextView) itemView.findViewById(R.id.compatition_time);
            player1logo = (ImageView) itemView.findViewById(R.id.player1logo);
            player2logo = (ImageView) itemView.findViewById(R.id.player2logo);
            player1 = (TextView) itemView.findViewById(R.id.player1);
            player2 = (TextView) itemView.findViewById(R.id.player2);
            score = (TextView) itemView.findViewById(R.id.score);
            status = (TextView) itemView.findViewById(R.id.status);
        }
    }
}

