package com.globalformulae.shiguang.maininterface;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.greendao.DaoSession;
import com.globalformulae.shiguang.greendao.ScheduleDao;
import com.globalformulae.shiguang.maininterface.MainFragments.ScheduleFragment;
import com.globalformulae.shiguang.model.Schedule;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleInfoActivity extends AppCompatActivity {
    @BindView(R.id.tb_schedule_info)
    Toolbar toolbar;
    @BindView(R.id.et_schedule_name)
    EditText scheduleName;
    @BindView(R.id.et_schedule_des)
    EditText scheduleDes;
    @BindView(R.id.switch_ifImp)
    Switch aSwitch;
    @BindView(R.id.tv_schedule_info_date)
    TextView scheduleDate;
    @BindView(R.id.tv_schedule_info_time)
    TextView scheduleTime;
    @BindView(R.id.fab_commit)
    FloatingActionButton floatingActionButton;

    private ScheduleFragment fragment;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mDayOfWeek;
    private int mHour;
    private int mMinute;
    private String xinqi;   //字符串星期
    private boolean mType;
    private int openType;
    private Schedule mSchedule;
    public interface MycallBack{
        void reGetData();
    }
    private MycallBack mycallBack;

    public void setMycallBack(MycallBack mycallBack) {
        this.mycallBack = mycallBack;
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_info);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("info");
        if(bundle.getString("openType").equals("0")){//如果是新建事件
            openType=0;
            mYear=bundle.getInt("year");
            mMonth=bundle.getInt("month");
            mDay=bundle.getInt("day");
            xinqi=bundle.getString("xinqi");
            mDayOfWeek=bundle.getInt("dayOfWeek");
            initView(openType,null);
        }else{//如果是修改事件
            openType=1;
            mSchedule=new Schedule();
            mSchedule.setId(bundle.getLong("id"));
            mSchedule.setStatus(bundle.getInt("status"));
            mSchedule.setName(bundle.getString("name"));
            mSchedule.setDescription(bundle.getString("desc"));
            mSchedule.setYear(bundle.getInt("year"));
            mSchedule.setMonth(bundle.getInt("month"));
            mSchedule.setDay(bundle.getInt("day"));
            mSchedule.setDayOfWeek(bundle.getInt("dayOfWeek"));
            mSchedule.setHour(bundle.getInt("hour"));
            mSchedule.setMinute(bundle.getInt("minute"));
            mSchedule.setType(bundle.getBoolean("type"));

            mYear=mSchedule.getYear();
            mMonth=mSchedule.getMonth();
            mDay=mSchedule.getDay();
            mDayOfWeek=mSchedule.getDayOfWeek();
            xinqi=getDayOfWeek(mDayOfWeek);
            mHour=mSchedule.getHour();
            mMinute=mSchedule.getMinute();
            Log.e("hui3",mYear+" "+mMonth+" "+mDay+" "+xinqi);

            initView(openType,mSchedule);
        }


    }

    private String getDayOfWeek(int dayOfWeek){
        String result;
        switch (dayOfWeek) {
            case 1:
                result= "星期天";
                break;
            case 2:
                result= "星期一";
                break;
            case 3:
                result= "星期二";
                break;
            case 4:
                result= "星期三";
                break;
            case 5:
                result= "星期四";
                break;
            case 6:
                result= "星期五";
                break;
            case 7:
                result= "星期六";
                break;
            default:
                result="";
                break;
        }
        return result;
    }

    public void initView(int type,Schedule schedule){
        toolbar.setTitle("任务详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Calendar c=Calendar.getInstance();
        mHour=c.get(Calendar.HOUR_OF_DAY);
        mMinute=c.get(Calendar.MINUTE);
        if(type==1){    //如果是修改事件
            scheduleName.setText(schedule.getName());
            scheduleDes.setText(schedule.getDescription());
            aSwitch.setChecked(schedule.getType());
            scheduleDate.setText(schedule.getData());
            scheduleTime.setText(schedule.getTime());
        }else{      //如果是新建事件
            scheduleDate.setText(""+mYear+" 年 "+mMonth+" 月 "+mDay+" 日 "+xinqi);
            if(mMinute<10){
                scheduleTime.setText(""+mHour+":0"+mMinute);
            }else{
                scheduleTime.setText(""+mHour+":"+mMinute);
            }
        }

        //日期选择器事件
        scheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ScheduleInfoActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                mYear = year;
                                mMonth= month+1;
                                mDay= dayOfMonth;
                                c.set(Calendar.YEAR,year);
                                c.set(Calendar.MONTH,month);
                                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                mDayOfWeek=c.get(Calendar.DAY_OF_WEEK);
                                switch (c.get(Calendar.DAY_OF_WEEK)) {
                                    case 1:
                                        xinqi= "星期天";
                                        break;
                                    case 2:
                                        xinqi= "星期一";
                                        break;
                                    case 3:
                                        xinqi= "星期二";
                                        break;
                                    case 4:
                                        xinqi= "星期三";
                                        break;
                                    case 5:
                                        xinqi= "星期四";
                                        break;
                                    case 6:
                                        xinqi= "星期五";
                                        break;
                                    case 7:
                                        xinqi= "星期六";
                                        break;
                                    default:
                                        break;
                                }
                                scheduleDate.setText(mYear + " 年 " + mMonth + " 月 " + mDay+ " 日 "
                                        + xinqi);
                            }
                        }, mYear
                        , mMonth-1
                        , mDay).show();
            }
        });



        //时间选择器事件
        scheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ScheduleInfoActivity.this,3, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mHour = i;
                        mMinute = i1;
                        if(mMinute<10){
                            scheduleTime.setText(mHour+ ":0" + mMinute);
                        }else{
                            scheduleTime.setText(mHour+ ":" + mMinute);
                        }

                    }
                }, mHour, mMinute, false).show();
            }
        });

        //是否重要
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mType=isChecked;
            }
        });
        //确认按钮
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(openType==0){//如果是新建的，那么需要插入操作
                    DaoSession daoSession = MyApplication.getDaoMaster().newSession();
                    ScheduleDao scheduleDao=daoSession.getScheduleDao();
                    String name=scheduleName.getText().toString();
                    String des=scheduleDes.getText().toString();
                    if(name.isEmpty()){
                        final Snackbar snackbar=Snackbar.make(floatingActionButton,"任务名称不能为空",Snackbar.LENGTH_SHORT);
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
                        if(mType){
                            schedule.setStatus(1);
                        }else{
                            schedule.setStatus(0);
                        }
                        Long id=scheduleDao.insert(schedule);   //插入
                        schedule.setId(id);
                        schedule.setIfNew(true);
                        EventBus.getDefault().post(schedule);   //回调
                        ScheduleInfoActivity.this.finish();
                    }

                }else{//如果是修改事件
                    DaoSession daoSession = MyApplication.getDaoMaster().newSession();
                    ScheduleDao scheduleDao=daoSession.getScheduleDao();
                    String name=scheduleName.getText().toString();
                    String des=scheduleDes.getText().toString();
                    if(name.isEmpty()){
                        final Snackbar snackbar=Snackbar.make(floatingActionButton,"任务名称不能为空",Snackbar.LENGTH_SHORT);
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
                        if(mType){
                            schedule1.setStatus(1);
                        }else{
                            schedule1.setStatus(0);
                        }
                        scheduleDao.update(schedule1);  //更新事件
                        EventBus.getDefault().post(schedule1);
                        ScheduleInfoActivity.this.finish();

                    }
                }

            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
