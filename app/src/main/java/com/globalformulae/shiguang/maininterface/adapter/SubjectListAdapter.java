package com.globalformulae.shiguang.maininterface.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.model.Subject;

import java.util.List;

/**
 * Created by ZXG on 2017/3/12.
 */

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectViewHolder>{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Subject> subjects;

    public SubjectListAdapter(Context mContext, List<Subject> subjects) {
        mInflater=LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.subjects = subjects;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.adapter_subject,parent,false);//获取item的layout，设置到父组件里，
        SubjectViewHolder viewHolder=new SubjectViewHolder(view); //构造ViewHolder 把Item试图传进去
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {
        Subject subject=subjects.get(position);
        holder.name.setText(subject.getName());
        holder.type.setText(subject.getType());
        holder.place.setText(subject.getArea()+" "+subject.getRoom());
        String detail=subject.getDetail();
        String cycle=detail.substring(0,detail.lastIndexOf("周")+1);
        holder.cycle.setText(cycle);
        holder.time.setText("星期"+subject.getDay()+" "+subject.getStart()+"-"+subject.getEnd()+"节");
        holder.teacher.setText("授课老师："+subject.getTeacher());

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public void setData(List<Subject> subjects){
        this.subjects=subjects;
    }
}

class SubjectViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView type;
    TextView credit;
    TextView place;
    TextView cycle;
    TextView time;
    TextView teacher;

    public SubjectViewHolder(View itemView) {
        super(itemView);
        name= (TextView) itemView.findViewById(R.id.subject_name);
        type= (TextView) itemView.findViewById(R.id.subject_type);
        credit= (TextView) itemView.findViewById(R.id.subject_credit);
        place= (TextView) itemView.findViewById(R.id.subject_place);
        cycle= (TextView) itemView.findViewById(R.id.subject_cycle);
        time= (TextView) itemView.findViewById(R.id.subject_time);
        teacher= (TextView) itemView.findViewById(R.id.subject_teacher);
    }
}