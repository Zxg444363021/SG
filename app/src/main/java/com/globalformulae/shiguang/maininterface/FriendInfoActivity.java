package com.globalformulae.shiguang.maininterface;

import android.content.Intent;
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
import com.globalformulae.shiguang.maininterface.adapter.SimpleRecordAdapter;
import com.globalformulae.shiguang.model.AlternateRecord;
import com.globalformulae.shiguang.model.Power;
import com.globalformulae.shiguang.retrofit.RetrofitHelper;
import com.globalformulae.shiguang.retrofit.UserActionService;
import com.globalformulae.shiguang.utils.OkHttpUtil;
import com.globalformulae.shiguang.utils.SPUtil;
import com.globalformulae.shiguang.view.CircleImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
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

    public static final String DOSTEALTOMATO="1";
    public static final String DOSTEALCUSTOME="2";
    public static final String DOWATER="3";

    private Long friendId;
    private int friendPower;
    private SimpleRecordAdapter simpleRecordAdapter;
    private Retrofit retrofit;
    private UserActionService userActionService;
    private List<AlternateRecord> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        ButterKnife.bind(this);
        retrofit = RetrofitHelper.getInstance();
        userActionService = retrofit.create(UserActionService.class);
        setActionBar();
        Intent intent = getIntent();
        friendPower = intent.getIntExtra("power", 0);
        friendPowerTV.setText(String.valueOf(friendPower) + "g");
        friendId = intent.getLongExtra("friendId", 12);
        Glide.with(this).load(intent.getStringExtra("icon")).placeholder(R.mipmap.unlogged_icon).into(friendiIconIV);
        friendTomatoNumTV.setText(String.valueOf(intent.getIntExtra("tomato_n", 0)));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.sun_anim);
        sunIV.setAnimation(animation);
        int n = SPUtil.getSP(this, "user").getInt("pot_num", 5);
        friendPotNumTV.setText(String.valueOf(n));
        if (n == 0) {
            friendWaterBTN.setClickable(false);
        }
        getFriendInfo();
        getFriendRecord();
    }

    /**
     * 获取可偷取能量信息
     */
    public void getFriendInfo() {
        OkHttpUtil.getInstance().getFriendInfo(new FormBody.Builder()
                .add("user1id", String.valueOf(getSP(FriendInfoActivity.this, "user").getLong("userid", 0)))
                .add("user2id", String.valueOf(friendId))
                .build());
    }

    public void setActionBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initPower(Power power) {
        if (power.getCanPower1Steal() == 1) {
            gainTomatoBTN.setVisibility(View.VISIBLE);
        }
        if (power.getCanPower2Steal() == 1) {
            gainCustomBTN.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取朋友最近的被交互记录
     * retrofit+rxjava请求方式
     */
    public void getFriendRecord() {

        userActionService.doGetRecord(String.valueOf(friendId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AlternateRecord>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<AlternateRecord> alternateRecords) {
                        if (alternateRecords.size() != 0) {
                            dataList = alternateRecords;
                            Collections.reverse(dataList);
                            simpleRecordAdapter = new SimpleRecordAdapter(FriendInfoActivity.this, dataList);
                            friendRecordRV.setLayoutManager(new LinearLayoutManager(FriendInfoActivity.this, LinearLayoutManager.VERTICAL, false));
                            friendRecordRV.setAdapter(simpleRecordAdapter);
                        }else {
                            dataList=new ArrayList<AlternateRecord>();
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
     * @param type
     */
    private void doSometingToPower(String type){
        userActionService.doStealPower(String.valueOf(getSP(FriendInfoActivity.this, "user").getLong("userid", 0))
        ,String.valueOf(friendId),type)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<AlternateRecord>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull AlternateRecord alternateRecord) {
                dataList.add(0,alternateRecord);
                simpleRecordAdapter.notifyItemInserted(0);
                if (alternateRecord.getType() == 1) {//偷番茄能量
                    gainTomatoBTN.setVisibility(View.GONE);
                    wateringPlusTV.setVisibility(View.VISIBLE);
                    wateringPlusTV.setText("+" + alternateRecord.getPower());
                    Animation animation = AnimationUtils.loadAnimation(FriendInfoActivity.this, R.anim.gain_plus);
                    wateringPlusTV.setAnimation(animation);
                } else if (alternateRecord.getType() == 2) {//偷习惯能量
                    gainCustomBTN.setVisibility(View.GONE);
                    wateringPlusTV.setVisibility(View.VISIBLE);
                    wateringPlusTV.setText("+" + alternateRecord.getPower());
                    Animation animation = AnimationUtils.loadAnimation(FriendInfoActivity.this, R.anim.gain_plus);
                    wateringPlusTV.setAnimation(animation);
                } else if (alternateRecord.getType() == 3) {//浇水
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

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

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
}
