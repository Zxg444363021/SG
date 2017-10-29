package com.globalformulae.shiguang.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.Schedule;
import com.globalformulae.shiguang.greendao.DaoSession;
import com.globalformulae.shiguang.greendao.ScheduleDao;
import com.globalformulae.shiguang.maininterface.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *任务详情的对话框
 * 注意！其根布局需要设置一个minWidth
 * Created by ZXG on 2017/9/7.
 */

public class ScheduleDialogFragment extends DialogFragment {

    @BindView(R.id.ib_edit)
    ImageButton editIB;
    @BindView(R.id.ib_save)
    ImageButton saveIB;
    @BindView(R.id.et_schedule_name)
    EditText scheduleNameET;
    @BindView(R.id.et_schedule_des)
    EditText scheduleDesET;
    @BindView(R.id.switch_ifImp)
    Switch aSwitch;
    @BindView(R.id.tv_schedule_info_date)
    TextView scheduleDateTV;
    @BindView(R.id.tv_schedule_info_time)
    TextView scheduleTimeTV;

    private Schedule mSchedule;
    private String scheduleName;
    private String scheduleDes;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mDayOfWeek;
    private String mWeek;
    private int mHour;
    private int mMinute;
    private int mType;

    private int openType;

    public ScheduleDialogFragment(){}

    public ScheduleDialogFragment(Schedule mSchedule, int openType){
        this.mSchedule = mSchedule;
        this.openType=openType;
    }

    public ScheduleDialogFragment(int year, int mMonth, int mDay, int mDayOfWeek, String mWeek, int openType) {
        this.mYear = year;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mDayOfWeek = mDayOfWeek;
        this.mWeek = mWeek;
        this.openType=openType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_fragment_schedule_info,container);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    public void initView(){
        final Calendar calendar=Calendar.getInstance();
        mHour =calendar.get(Calendar.HOUR_OF_DAY);
        mMinute =calendar.get(Calendar.MINUTE);
        if (openType==0){//新建事件
            editIB.setVisibility(View.GONE);
            saveIB.setVisibility(View.VISIBLE);
            scheduleDateTV.setText(""+mYear+" 年 "+ mMonth +" 月 "+ mDay +" 日 "+ mWeek);
            if(mMinute <10){
                scheduleTimeTV.setText(""+ mHour +":0"+ mMinute);
            }else{
                scheduleTimeTV.setText(""+ mHour +":"+ mMinute);
            }
        }else{//查看事件
            editIB.setVisibility(View.VISIBLE);
            saveIB.setVisibility(View.GONE);
            scheduleNameET.setText(mSchedule.getName());
            scheduleDesET.setText(mSchedule.getDescription());
            aSwitch.setChecked(mSchedule.getType()==1?true:false);
            scheduleDateTV.setText(mSchedule.getData());
            scheduleTimeTV.setText(mSchedule.getTime());

            scheduleNameET.setFocusable(false);
            scheduleNameET.setFocusableInTouchMode(false);
            scheduleDesET.setFocusable(false);
            scheduleDesET.setFocusableInTouchMode(false);
            aSwitch.setEnabled(false);
            scheduleDateTV.setEnabled(false);
            scheduleTimeTV.setEnabled(false);
        }
        //日期选择器事件
        scheduleDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(),
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                mYear = year;
                                mMonth= month+1;
                                mDay= dayOfMonth;
                                calendar.set(Calendar.YEAR,year);
                                calendar.set(Calendar.MONTH,month);
                                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                mDayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
                                switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                                    case 1:
                                        mWeek= "星期天";
                                        break;
                                    case 2:
                                        mWeek= "星期一";
                                        break;
                                    case 3:
                                        mWeek= "星期二";
                                        break;
                                    case 4:
                                        mWeek= "星期三";
                                        break;
                                    case 5:
                                        mWeek= "星期四";
                                        break;
                                    case 6:
                                        mWeek= "星期五";
                                        break;
                                    case 7:
                                        mWeek= "星期六";
                                        break;
                                    default:
                                        break;
                                }
                                scheduleDateTV.setText(mYear + " 年 " + mMonth + " 月 " + mDay+ " 日 "
                                        + mWeek);
                            }
                        }, mYear
                        , mMonth-1
                        , mDay).show();
            }
        });



        //时间选择器事件
        scheduleTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),3, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mHour = i;
                        mMinute = i1;
                        if(mMinute<10){
                            scheduleTimeTV.setText(mHour+ ":0" + mMinute);
                        }else{
                            scheduleTimeTV.setText(mHour+ ":" + mMinute);
                        }

                    }
                }, mHour, mMinute, false).show();
            }
        });

        //是否重要
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mType=isChecked?1:0;
            }
        });
    }

    @OnClick(R.id.ib_edit)
    public void edit(){
        editIB.setVisibility(View.GONE);
        saveIB.setVisibility(View.VISIBLE);

        scheduleNameET.setFocusableInTouchMode(true);
        scheduleNameET.setFocusable(true);
        scheduleDesET.setFocusableInTouchMode(true);
        scheduleDesET.setFocusable(true);
        aSwitch.setEnabled(true);
        scheduleDateTV.setEnabled(true);
        scheduleTimeTV.setEnabled(true);


    }

    @OnClick(R.id.ib_save)
    public void save(){
        editIB.setVisibility(View.VISIBLE);
        saveIB.setVisibility(View.GONE);

        scheduleNameET.setFocusable(false);
        scheduleNameET.setFocusableInTouchMode(false);
        scheduleDesET.setFocusable(false);
        scheduleDesET.setFocusableInTouchMode(false);
        aSwitch.setEnabled(false);
        scheduleDateTV.setEnabled(false);
        scheduleTimeTV.setEnabled(false);

        if(openType==0){//如果是新建的，那么需要插入操作
            DaoSession daoSession = MyApplication.getDaoMaster().newSession();
            ScheduleDao scheduleDao=daoSession.getScheduleDao();
            String name=scheduleNameET.getText().toString();
            String des=scheduleDesET.getText().toString();
            if(name.isEmpty()){
                final Snackbar snackbar=Snackbar.make(saveIB,"任务名称不能为空",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }else{
                Schedule schedule=new Schedule();
                schedule.setId(null);
                schedule.setName(name);
                schedule.setDescription(des);
                schedule.setYear(mYear);
                schedule.setMonth(mMonth);
                schedule.setDay(mDay);
                schedule.setDayOfWeek(mDayOfWeek);
                schedule.setType(mType);
                schedule.setHour(mHour);
                schedule.setMinute(mMinute);
                if(mType==1){
                    schedule.setStatus(1);
                }else{
                    schedule.setStatus(0);
                }
                Long id=scheduleDao.insert(schedule);   //插入
                schedule.setId(id);
                schedule.setIfNew(true);
                EventBus.getDefault().post(schedule);   //回调
                ScheduleDialogFragment.this.dismiss();
            }

        }else{//如果是修改事件
            DaoSession daoSession = MyApplication.getDaoMaster().newSession();
            ScheduleDao scheduleDao=daoSession.getScheduleDao();
            String name=scheduleNameET.getText().toString();
            String des=scheduleDesET.getText().toString();
            if(name.isEmpty()){
                final Snackbar snackbar=Snackbar.make(saveIB,"任务名称不能为空",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }else{
                Schedule schedule1=new Schedule();
                schedule1.setId(mSchedule.getId());
                schedule1.setName(name);
                schedule1.setDescription(des);
                schedule1.setYear(mYear);
                schedule1.setMonth(mMonth);
                schedule1.setDay(mDay);
                schedule1.setDayOfWeek(mDayOfWeek);
                schedule1.setType(mType);
                schedule1.setHour(mHour);
                schedule1.setMinute(mMinute);
                if(mType==1){
                    schedule1.setStatus(1);
                }else{
                    schedule1.setStatus(0);
                }
                scheduleDao.update(schedule1);  //更新事件
                EventBus.getDefault().post(schedule1);
                ScheduleDialogFragment.this.dismiss();
            }
        }

    }
}
