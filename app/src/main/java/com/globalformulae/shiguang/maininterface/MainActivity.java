package com.globalformulae.shiguang.maininterface;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.MyDate;
import com.globalformulae.shiguang.bean.SoilTimeBean;
import com.globalformulae.shiguang.maininterface.MainFragments.HabitFragment;
import com.globalformulae.shiguang.maininterface.MainFragments.ScheduleFragment;
import com.globalformulae.shiguang.maininterface.MainFragments.TimerFragment;
import com.globalformulae.shiguang.maininterface.nba.NBAActivity;
import com.globalformulae.shiguang.utils.MenuManager;
import com.globalformulae.shiguang.utils.SPUtil;
import com.globalformulae.shiguang.view.CustomDialog;
import com.globalformulae.shiguang.view.TimePickerDialogF;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

import static com.globalformulae.shiguang.utils.SPUtil.getSP;

public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener,
        ScheduleFragment.OnFragmentInteractionListener,
        TimerFragment.OnFragmentInteractionListener,
        TimePickerDialogF.onTimeChosenListener {

    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;

    private MenuManager menuManager;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.tv_date)
    TextView dateTV;
    @BindView(R.id.chronometer)
    TextView chronometer;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.left_drawer)
    LinearLayout linearLayout;

    private ScheduleFragment scheduleFragment;
    private TimerFragment timerFragment;
    private HabitFragment habitFragment;
    private FragmentManager fragmentManager;
    private Calendar mCalendar;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String xinqi;
    private int mDayOfWeek;
    private int timingTength;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean tag=false;//是创建还是回来
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //transition动画的参数设置，必须在最开始运行
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode().setDuration(3000));
        getWindow().setExitTransition(new Fade().setDuration(3000));
        getWindow().setReturnTransition(new Slide().setDuration(3000));
        getWindow().setReenterTransition(new Slide().setDuration(3000));
        getWindow().setSharedElementEnterTransition(new ChangeTransform().setInterpolator(new BounceInterpolator()).setDuration(6000));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        tag=true;


    }




    /**
     * 初始化视图
     */
    private void initView(){
        menuManager = MenuManager.getInstance();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, menuManager, drawerLayout, this);
        mCalendar=Calendar.getInstance();
        mYear=mCalendar.get(Calendar.YEAR);
        mMonth=mCalendar.get(Calendar.MONTH);
        mDay=mCalendar.get(Calendar.DAY_OF_MONTH);
        mDayOfWeek=mCalendar.get(Calendar.DAY_OF_WEEK);
        setDateTV(mYear,mMonth,mDay,getDayOfWeek(mDayOfWeek));

        scheduleFragment=new ScheduleFragment();
        timerFragment=new TimerFragment();
        habitFragment=new HabitFragment();
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        fTransaction.add(R.id.frameLayout,scheduleFragment);
        fTransaction.add(R.id.frameLayout,timerFragment);
        fTransaction.add(R.id.frameLayout,habitFragment);
        hideAllFragment(fTransaction);
        fTransaction.show(scheduleFragment);
        fTransaction.commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fTransaction = fragmentManager.beginTransaction();
                hideAllFragment(fTransaction);
                switch (item.getItemId()){
                    case R.id.action_task:
                        chronometer.setVisibility(View.GONE);
                        dateTV.setVisibility(View.VISIBLE);
                        fTransaction.show(scheduleFragment);
                        break;
                    case R.id.action_tomato:
                        chronometer.setVisibility(View.VISIBLE);
                        dateTV.setVisibility(View.GONE);
                        fTransaction.show(timerFragment);
                        break;
                    case R.id.action_custom:
                        chronometer.setVisibility(View.GONE);
                        dateTV.setVisibility(View.GONE);
                        fTransaction.show(habitFragment);
                        break;
                }
                fTransaction.commit();
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
//获取步进计数器
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager!=null)
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        mSensor.getFifoMaxEventCount();
        if(tag){
            SharedPreferences sharedPreferences=SPUtil.getSP(MainActivity.this,"user");
            String date=sharedPreferences.getString("date","20170101");
            String month=(mMonth+1)<10?"0"+(mMonth+1):Integer.toString(mMonth);
            String day=mDay<10?"0"+mDay:Integer.toString(mDay);
            String today=Integer.toString(mYear)+month+day;
            if(!date.equals(today)){
                SharedPreferences.Editor editor=SPUtil.getSPD(MainActivity.this,"user");
                editor.putString("date",today);
                editor.putBoolean("isDirty",true);
                editor.apply();
            }

            tag=false;
        }

        //注册步进监听器
        mSensorManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注销步进监听器
        mSensorManager.unregisterListener(sensorEventListener);
    }

    /**
     * 隐藏所有的fragment
     * @param fragmentTransaction
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(scheduleFragment != null)fragmentTransaction.hide(scheduleFragment);
        if(timerFragment != null)fragmentTransaction.hide(timerFragment);
        if(habitFragment != null)fragmentTransaction.hide(habitFragment);
    }

    /**
     * 获取今天是星期几
     * @param dayOfWeek
     * @return
     */
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

    /**
     * 修改日期
     * @param year
     * @param month
     * @param day
     * @param dayOfWeek
     */
    private void setDateTV(int year,int month,int day,String dayOfWeek){
        dateTV.setText((month+ 1) + " 月 " + day+ " 日 "
                + dayOfWeek);
    }

    /**
     * 修改日期
     */
    @OnClick(R.id.tv_date)
    void changeDate(){
        final Calendar calendar=Calendar.getInstance();
        new DatePickerDialog(MainActivity.this,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dp, int year,
                                          int month, int dayOfMonth) {
                        mYear = year;
                        mMonth= month;
                        mDay= dayOfMonth;
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        mDayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
                        xinqi=getDayOfWeek(mDayOfWeek);
                        setDateTV(mYear,mMonth,mDay,xinqi);
                        MyDate myDate=new MyDate(mYear,mMonth+1,mDay,mDayOfWeek);
                        EventBus.getDefault().post(myDate);
                    }
                }, mYear
                , mMonth
                , mDay).show();

    }

    /**
     * 计时器点击事件监听器
     */
    @OnClick(R.id.chronometer)
    void stopTimer(){
        if(((MyApplication)MainActivity.this.getApplication()).isTiming()){
            final CustomDialog customDialog=new CustomDialog(this);
            customDialog.setCustomInformStyle("温馨提示", "想要停止计时吗？现在停止计时将前功尽弃哦！", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MyApplication)MainActivity.this.getApplication()).setIsTiming(false);
                    ((MyApplication)getApplication()).remainTime=0;
                    customDialog.dismiss();
                    chronometer.setText("00:00:00");
                }
            });
            customDialog.show();
        }else {
            TimePickerDialogF.newInstance(null,false).show(getFragmentManager(), "EditNameDialog");
        }


    }


    /**
     * 左侧菜单点击切换activity
     * @param slideMenuItem
     * @param screenShotable
     * @param position
     * @return
     */
    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        Intent intent;
        switch (slideMenuItem.getName()) {
            case MenuManager.CLOSE:
                break;
            case MenuManager.USERICON:
                SharedPreferences sp= getSP(MainActivity.this,"user");
                if(sp.getBoolean("logged",false)){
                    intent=new Intent(MainActivity.this,UserInfoActivity.class);
                }else{
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                break;
            case MenuManager.STATISTIC:
                break;
            case MenuManager.FUTURETASK:
                break;
            case MenuManager.WEATHER:
                intent=new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
                break;
            case MenuManager.NBA:
                intent = new Intent(MainActivity.this, NBAActivity.class);
                startActivity(intent);
                break;
            case MenuManager.SCHOOL:
                intent=new Intent(MainActivity.this,SchoolLoginActivity.class);
                startActivity(intent);
                break;
            case MenuManager.DRUM:
                intent=new Intent(MainActivity.this,DurmActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return screenShotable;
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();
    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * 创建菜单列表
     */
    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(MenuManager.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(MenuManager.USERICON, R.drawable.ic_person_outline_black_48dp);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(MenuManager.STATISTIC, R.drawable.icn_2);
        list.add(menuItem2);
//        SlideMenuItem menuItem3 = new SlideMenuItem(MenuManager.FUTURETASK, R.drawable.icn_3);
//        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(MenuManager.WEATHER, R.mipmap.icn_weather);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(MenuManager.NBA, R.mipmap.icn_nba);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(MenuManager.SCHOOL, R.drawable.ic_school_black_48dp);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(MenuManager.DRUM, R.mipmap.icn_drum);
        list.add(menuItem7);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(MainActivity.this).setTitle("您是否要退出？")
                    .setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //TODO 注销监听器
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
        }
    }

    @Override
    public void onFragmentInteraction(String str) {
        str.equals("timerFragment");
    }


    private CountDownTimer countDownTimer;

    /**
     * 时间选择好了开始计时
     * @param hour
     * @param minute
     */
    @Override
    public void onTimeChosen(int hour, int minute) {
        timingTength=(hour*60+minute)*60;
        ((MyApplication)getApplication()).remainTime=timingTength;
        if(!((MyApplication)MainActivity.this.getApplication()).isTiming()){
            ((MyApplication)MainActivity.this.getApplication()).setIsTiming(true);
        }
        if (countDownTimer!=null)
            countDownTimer.cancel();
        countDownTimer=new CountDownTimer((timingTength+1)*1000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                int time=Long.valueOf(millisUntilFinished).intValue()/1000;
                if(((MyApplication)getApplication()).isTiming()&&time>=0){
                    String hour = time / 3600 < 10 ? "0" + time / 3600 : "" + time / 3600;
                    int m=time%3600;
                    String min = m/ 60 < 10 ? "0" + m / 60 : "" + m / 60;
                    String sec = m % 60 < 10 ? "0" + m % 60 : "" + m % 60;
                    chronometer.setText(hour + ":" + min + ":" + sec);
                    if((timingTength-time)%1500==0){
                        EventBus.getDefault().post(new SoilTimeBean(0));
                    }else if((timingTength-time)%1500==60){
                        EventBus.getDefault().post(new SoilTimeBean(300));
                    }else if((timingTength-time)%1500==300){
                        EventBus.getDefault().post(new SoilTimeBean(600));
                    }else if((timingTength-time)%1500==600){
                        EventBus.getDefault().post(new SoilTimeBean(900));
                    }else if((timingTength-time)%1500==900){
                        EventBus.getDefault().post(new SoilTimeBean(1200));
                    }else if((timingTength-time)%1500==1400){
                        EventBus.getDefault().post(new SoilTimeBean(1400));
                    }else if(time==0||time==1){
                        chronometer.setText("00:00:00");
                        ((MyApplication)getApplication()).setIsTiming(false);
                        StyleableToast st2 = new StyleableToast
                                .Builder(MainActivity.this, "本次计时成功，上传ing")
                                .withMaxAlpha()
                                .withBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                                .withTextColor(Color.WHITE)
                                .withBoldText()
                                .withIcon(R.drawable.ic_autorenew_black_24dp,true)
                                .build();
                        st2.show();
                    }
                    else{}
                }else{

                }
            }

            @Override
            public void onFinish() {
                ((MyApplication)getApplication()).setIsTiming(false);
            }
        };
        countDownTimer.start();

    }


    SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //这里获取到的数据是自上一次开机以来的总步数。
            for(float f:event.values){
                Log.e("**********step", String.valueOf(f));
            }

            SharedPreferences sharedPreferences=SPUtil.getSP(MainActivity.this,"user");
            Boolean isDirty=sharedPreferences.getBoolean("isDirty",true);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            if(!isDirty){
               float startStep=sharedPreferences.getFloat("startStep",0);
                if(event.values[0]<startStep)
                    editor.putFloat("todayStep",event.values[0]);
                else
                    editor.putFloat("todayStep",event.values[0]-startStep);
                editor.apply();
            }else{
                editor.putFloat("startStep",event.values[0]);
                editor.putFloat("todayStep",0.0f);
                editor.putBoolean("isDirty",false);
                editor.apply();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
