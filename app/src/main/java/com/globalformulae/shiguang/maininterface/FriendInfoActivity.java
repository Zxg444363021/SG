package com.globalformulae.shiguang.maininterface;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.AlternateRecord;
import com.globalformulae.shiguang.bean.OnesRecord;
import com.globalformulae.shiguang.maininterface.adapter.ComplexRecordAdapter;
import com.globalformulae.shiguang.retrofit.RetrofitHelper;
import com.globalformulae.shiguang.retrofit.UserActionService;
import com.globalformulae.shiguang.utils.SPUtil;
import com.globalformulae.shiguang.view.CircleImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.globalformulae.shiguang.utils.SPUtil.getSP;

public class FriendInfoActivity extends AppCompatActivity {
    @BindView(R.id.friend_icon_iv)
    CircleImageView friendiIconIV;
    @BindView(R.id.friend_tomato_n_tv)
    TextView friendTomatoNumTV;
    @BindView(R.id.friend_pot_n_tv)
    TextView friendPotNumTV;
    @BindView(R.id.friend_water_btn)
    Button friendWaterBTN;
    @BindView(R.id.gain_custom_btn)
    Button gainCustomBTN;
    @BindView(R.id.gain_tomato_btn)
    Button gainTomatoBTN;
    @BindView(R.id.friend_record_rv)
    RecyclerView friendRecordRV;
    @BindView(R.id.friend_info_tb)
    Toolbar toolbar;
    @BindView(R.id.friend_power_tv)
    TextView friendPowerTV;
    @BindView(R.id.watering_iv)
    ImageView wateringIV;
    @BindView(R.id.friend_sun_iv)
    ImageView sunIV;
    @BindView(R.id.watering_plus_tv)
    TextView wateringPlusTV;
    @BindView(R.id.new_record_tv)
    TextView newRecordTV;

    public static final String DOSTEALTOMATO = "1";
    public static final String DOSTEALCUSTOME = "2";
    public static final String DOWATER = "3";

    private Long friendId;
    private int friendPower;
    private ComplexRecordAdapter complexRecordAdapter;
    private UserActionService userActionService;
    private List<OnesRecord> friendRecordList;
    private Typeface mtypeface;
    private boolean hasLoad=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        ButterKnife.bind(this);
        Retrofit retrofit = RetrofitHelper.getInstance();
        userActionService = retrofit.create(UserActionService.class);
        mtypeface= Typeface.createFromAsset(getAssets(),"HYJiaShuJian.ttf");
        hasLoad=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(hasLoad){
            initView();
            getFriendRecord();
            hasLoad=false;
        }
    }

    /**
     * 界面控件初始化
     */
    private void initView() {
        Intent intent = getIntent();
        friendPower = intent.getIntExtra("power", 0);
        friendPowerTV.setText(String.valueOf(friendPower) + "g");
        friendId = intent.getLongExtra("friendId", 12);
        setActionBar(intent.getStringExtra("name"));
        Glide.with(this).load(intent.getStringExtra("icon")).placeholder(R.mipmap.unlogged_icon).into(friendiIconIV);
        friendTomatoNumTV.setText(String.valueOf(intent.getIntExtra("tomato_n", 0)));
        if (intent.getIntExtra("power1CanSteal",0) == 1) {
            gainTomatoBTN.setVisibility(View.VISIBLE);
        }
        if (intent.getIntExtra("power2CanSteal",0) == 1) {
            gainCustomBTN.setVisibility(View.VISIBLE);
        }


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.sun_anim);
        sunIV.setAnimation(animation);
        int n = SPUtil.getSP(this, "user").getInt("pot_num", 5);
        friendPotNumTV.setText(String.valueOf(n));
        if (n == 0) {
            friendWaterBTN.setClickable(false);
        }
        friendRecordRV.setLayoutManager(new LinearLayoutManager(FriendInfoActivity.this, LinearLayoutManager.VERTICAL, false));
        friendRecordRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);

                Paint paint = new Paint();
                paint.setColor(getResources().getColor(R.color.divider));
                paint.setStrokeWidth(3);

                for (int i = 0; i < state.getItemCount() - 1; i++) {

                    final View child = parent.getChildAt(i);
                    if (child != null) {



                        final int top1 = child.getTop();
                        final int bottom1 = top1 + dip2px(FriendInfoActivity.this, 10);

                        final int bottom2 = child.getBottom();
                        final int top2 = bottom2 - dip2px(FriendInfoActivity.this, 10);

                        c.drawLine(child.getLeft() + dip2px(FriendInfoActivity.this, 25),
                                top1,
                                child.getLeft() + dip2px(FriendInfoActivity.this, 25),
                                bottom1, paint);

                        c.drawLine(child.getLeft() + dip2px(FriendInfoActivity.this, 25),
                                top2,
                                child.getLeft() + dip2px(FriendInfoActivity.this, 25),
                                bottom2, paint);
                    }

                }
            }
        });
        newRecordTV.setTypeface(mtypeface);
    }



    public void setActionBar(String name) {
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 获取朋友最近的被交互记录
     * retrofit+rxjava请求方式
     */
    public void getFriendRecord() {
        userActionService.doGetRecord(String.valueOf(friendId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<OnesRecord>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<OnesRecord> onesRecord) {
                        if (onesRecord.size() != 0) {
                            friendRecordList = onesRecord;
                            processDataList();
                            complexRecordAdapter = new ComplexRecordAdapter(FriendInfoActivity.this, friendRecordList);
                            friendRecordRV.setAdapter(complexRecordAdapter);
                        } else {
                            friendRecordList = new ArrayList<OnesRecord>();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 对返回回来的记录进行加工
     * 主要是加上今天明天后天这样的信息
     */
    private void processDataList() {
        //所有信息的第一条的日期
        Calendar theFirstDay = Calendar.getInstance();
        theFirstDay.setTime(friendRecordList.get(0).getTime());
        //获取一个0727一样的数字
        String date = String.valueOf(theFirstDay.get(Calendar.MONTH) + 1) + String.valueOf(theFirstDay.get(Calendar.DAY_OF_MONTH));
        //现在一开始填上
        friendRecordList.add(0, new OnesRecord(4, friendRecordList.get(0).getTime()));

        //然后根据日期加上头部
        for (int i = 1; i < friendRecordList.size(); i++) {
            theFirstDay.setTime(friendRecordList.get(i).getTime());
            String date1 = String.valueOf(theFirstDay.get(Calendar.MONTH) + 1) + String.valueOf(theFirstDay.get(Calendar.DAY_OF_MONTH));
            if (!date1.equals(date)) {
                date = date1;
                friendRecordList.add(i, new OnesRecord(4, friendRecordList.get(i).getTime()));
                i++;
            }
        }
    }


    /**
     * 偷习惯能量
     */
    @OnClick(R.id.gain_custom_btn)
    void stealCustomPower() {
        doSometingToPower(DOSTEALCUSTOME);
    }

    /**
     * 偷番茄能量
     */
    @OnClick(R.id.gain_tomato_btn)
    void stealTomatPower() {
        doSometingToPower(DOSTEALTOMATO);
    }

    /**
     * 浇水
     */
    @OnClick(R.id.friend_water_btn)
    void waterPower() {
        doSometingToPower(DOWATER);
    }

    /**
     * 能量交互
     *retrofit+rxjava
     * @param type
     */
    private void doSometingToPower(String type) {
        userActionService.doStealPower(String.valueOf(getSP(FriendInfoActivity.this, "user").getLong("userid", 0))
                , String.valueOf(friendId), type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AlternateRecord>() {
                    @Override
                    public void accept(@NonNull AlternateRecord alternateRecord) throws Exception {
                        OnesRecord onesRecord=new OnesRecord();
                        onesRecord.setName(getSP(FriendInfoActivity.this,"user").getString("name","zzz"));
                        onesRecord.setPower(alternateRecord.getPower());
                        onesRecord.setType(alternateRecord.getType());
                        onesRecord.setTime(alternateRecord.getTime());
                        addItemToList(onesRecord);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });

    }

    /**
     * 交换完成在本地直接添加数据到list
     * @param onesRecord
     */
    private void addItemToList(OnesRecord onesRecord){
        Calendar today=Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY,0);
        today.set(Calendar.MINUTE,0);
        Calendar theDay=Calendar.getInstance();
        theDay.setTime(friendRecordList.get(0).getTime());
        if(theDay.after(today)){//是今天
            friendRecordList.add(1, onesRecord);
            complexRecordAdapter.notifyItemInserted(1);
        }else{
            friendRecordList.add(0,new OnesRecord(4,onesRecord.getTime()));
            friendRecordList.add(1, onesRecord);
            complexRecordAdapter.notifyItemRangeInserted(0,2);
            friendRecordRV.smoothScrollToPosition(0);
        }

        if (onesRecord.getType() == 1) {//偷番茄能量
            gainTomatoBTN.setVisibility(View.GONE);
            wateringPlusTV.setVisibility(View.VISIBLE);
            wateringPlusTV.setText("+" + onesRecord.getPower());
            Animation animation = AnimationUtils.loadAnimation(FriendInfoActivity.this, R.anim.gain_plus);
            wateringPlusTV.setAnimation(animation);
        } else if (onesRecord.getType() == 2) {//偷习惯能量
            gainCustomBTN.setVisibility(View.GONE);
            wateringPlusTV.setVisibility(View.VISIBLE);
            wateringPlusTV.setText("+" + onesRecord.getPower());
            Animation animation = AnimationUtils.loadAnimation(FriendInfoActivity.this, R.anim.gain_plus);
            wateringPlusTV.setAnimation(animation);
        } else if (onesRecord.getType() == 3) {//浇水
            int n = SPUtil.getSP(FriendInfoActivity.this, "user").getInt("pot_num", 5);
            n--;
            SPUtil.getSPD(FriendInfoActivity.this, "user").putInt("pot_num", n).commit();
            friendPotNumTV.setText(String.valueOf(n));
            friendPower = friendPower + 10;
            friendPowerTV.setText(String.valueOf(friendPower) + "g");
            SPUtil.getSPD(FriendInfoActivity.this, "user");
            wateringPlusTV.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(FriendInfoActivity.this, R.anim.gain_plus);
            wateringPlusTV.setAnimation(animation);
        }
    }


    @OnClick(R.id.friend_water_btn)
    void waterClick() {
        friendWaterBTN.setClickable(false);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.watering);
        animation1.setFillAfter(false);
        wateringIV.startAnimation(animation1);

        wateringIV.setBackgroundResource(R.drawable.watering_anim);
        wateringIV.setVisibility(View.VISIBLE);
        final AnimationDrawable animationDrawable = (AnimationDrawable) wateringIV.getBackground();
        animationDrawable.start();
        wateringIV.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationDrawable.stop();
                wateringIV.setVisibility(View.GONE);
                friendWaterBTN.setClickable(true);
            }
        }, 4500);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
