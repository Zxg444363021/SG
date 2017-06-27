package com.globalformulae.shiguang.maininterface;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.maininterface.MainFragments.ScheduleFragment;
import com.globalformulae.shiguang.maininterface.MainFragments.TimerFragment;
import com.globalformulae.shiguang.model.MyDate;
import com.globalformulae.shiguang.utils.MenuManager;
import com.globalformulae.shiguang.utils.SPUtil;

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

public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener,ScheduleFragment.OnFragmentInteractionListener,TimerFragment.OnFragmentInteractionListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;
    private LinearLayout linearLayout;
    private MenuManager menuManager;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.tv_date)
    Button dateTV;
    @BindView(R.id.chronometer)
    TextView chronometer;
    @BindView(R.id.text_clock)
    TextClock textClock;

    private ScheduleFragment scheduleFragment;
    private TimerFragment timerFragment;
    private FragmentManager fragmentManager;
    private Calendar mCalendar;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String xinqi;
    private int mDayOfWeek;

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
        menuManager = MenuManager.getInstance();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, menuManager, drawerLayout, this);
        ButterKnife.bind(this);

        mCalendar=Calendar.getInstance();
        mYear=mCalendar.get(Calendar.YEAR);
        mMonth=mCalendar.get(Calendar.MONTH);
        mDay=mCalendar.get(Calendar.DAY_OF_MONTH);
        mDayOfWeek=mCalendar.get(Calendar.DAY_OF_WEEK);
        MyDate date=new MyDate(mYear,mMonth+1,mDay,mDayOfWeek);
        setDateTV(mYear,mMonth,mDay,getDayOfWeek(mDayOfWeek));

        scheduleFragment=new ScheduleFragment(date);
        timerFragment=new TimerFragment();
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        fTransaction.add(R.id.frameLayout,scheduleFragment);
        fTransaction.add(R.id.frameLayout,timerFragment);
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
                        textClock.setVisibility(View.GONE);
                        dateTV.setVisibility(View.VISIBLE);
                        fTransaction.show(scheduleFragment);
                        break;
                    case R.id.action_tomato:
                        chronometer.setVisibility(View.VISIBLE);
                        dateTV.setVisibility(View.GONE);
                        fTransaction.show(timerFragment);
                        break;
                    case R.id.action_custom:
                        // Toast.makeText(MainActivity.this,"7",Toast.LENGTH_SHORT).show();

                        break;
                }
                fTransaction.commit();
                return true;
            }
        });

    }
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(scheduleFragment != null)fragmentTransaction.hide(scheduleFragment);
        if(timerFragment != null)fragmentTransaction.hide(timerFragment);
        //if(fg3 != null)fragmentTransaction.hide(fg3);
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
    private void setDateTV(int year,int month,int day,String dayOfWeek){
        dateTV.setText((month+ 1) + " 月 " + day+ " 日 "
                + dayOfWeek);
    }
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

    }

    /**
     * 时间显示按钮监听器
     */
    @OnClick(R.id.text_clock)
    void choiceTime(){
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        Intent intent;
        switch (slideMenuItem.getName()) {
            case MenuManager.CLOSE:
                break;
            case MenuManager.USERICON:
                SharedPreferences sp= SPUtil.getSP(MainActivity.this,"user");
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
                break;
            case MenuManager.NBA:
                intent = new Intent(MainActivity.this, NBAActivity.class);
                startActivity(intent);
                break;
            case MenuManager.SCHOOL:
                intent=new Intent(MainActivity.this,SchoolLoginActivity.class);
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
        SlideMenuItem menuItem3 = new SlideMenuItem(MenuManager.FUTURETASK, R.drawable.icn_3);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(MenuManager.WEATHER, R.drawable.icn_4);
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
}
