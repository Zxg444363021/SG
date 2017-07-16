package com.globalformulae.shiguang.maininterface.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.maininterface.FriendInfoActivity;
import com.globalformulae.shiguang.maininterface.adapter.SimpleRecordAdapter;
import com.globalformulae.shiguang.maininterface.adapter.TimerRankAdapter;
import com.globalformulae.shiguang.model.AlternateRecord;
import com.globalformulae.shiguang.model.SoilTimeBean;
import com.globalformulae.shiguang.model.User;
import com.globalformulae.shiguang.utils.OkHttpUtil;
import com.globalformulae.shiguang.utils.SPUtil;
import com.globalformulae.shiguang.view.CircleImageView;
import com.globalformulae.shiguang.view.RecycleViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TimerFragment extends Fragment implements TimerRankAdapter.onItemClickListener{

    @BindView(R.id.iv_sun)
    ImageView sunIV;
    @BindView(R.id.iv_soil)
    ImageView soilIV;
    @BindView(R.id.watering)
    ImageView wateringIV;
    @BindView(R.id.water_btn)
    Button waterBTN;
    @BindView(R.id.timer_record_rv)
    RecyclerView timerRecordRV;
    @BindView(R.id.timer_rank_rv)
    RecyclerView timerRankRV;
    @BindView(R.id.timer_icon_iv)
    CircleImageView timerIconIV;
    @BindView(R.id.timer_power_tv)
    TextView timerPowerTV;
    @BindView(R.id.timer_tomato_n_tv)
    TextView timerTomatoNTV;
    @BindView(R.id.normal_page)
    LinearLayout normalPage;
    @BindView(R.id.error_page)
    LinearLayout errorPage;
    @BindView(R.id.more_record_button)
    Button moreRecordBTN;


    private List<AlternateRecord> datalist1=new ArrayList<>();
    private List<User> datalist2=new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private SimpleRecordAdapter simpleRecordAdapter;//上面的5条记录
    private TimerRankAdapter timerRankAdapter;//下面的10条记录
    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_timer, container, false);
        ButterKnife.bind(this,view);
        simpleRecordAdapter=new SimpleRecordAdapter(getContext(),datalist1);
        timerRecordRV.setLayoutManager(new LinearLayoutManager(this.getContext()));
        timerRecordRV.setAdapter(simpleRecordAdapter);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.sun_anim);
        sunIV.setAnimation(animation);
        timerRankRV.setLayoutManager(new LinearLayoutManager(this.getContext()));
        timerRankRV.addItemDecoration(new RecycleViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, R.drawable.divider_01));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getTimerRR();
        initView();
    }

    public void initView(){
        if(!SPUtil.getSP(getActivity(),"user").getBoolean("isOnline",false)){
            errorPage.setVisibility(View.VISIBLE);
            normalPage.setVisibility(View.GONE);
            return;
        }
        errorPage.setVisibility(View.GONE);
        normalPage.setVisibility(View.VISIBLE);
        SharedPreferences sp= SPUtil.getSP(getActivity(),"user");
        timerPowerTV.setText(String.valueOf(sp.getInt("power_n",0))+"g");
        timerTomatoNTV.setText(String.valueOf(sp.getInt("tomato_n",0)));
        Glide.with(getActivity()).load(sp.getString("icon",null)).placeholder(R.mipmap.unlogged_icon).into(timerIconIV);
        if(datalist1.size()==0){
            moreRecordBTN.setText("暂无动态");
        }
    }

    /**
     *
     */
    private void getTimerRR(){
//        datalist1.add(new AlternateRecord(1,15,"牛馨曼",new Timestamp(2017,06,04)));
//        datalist1.add(new AlternateRecord(1,9,"孙佳",new Timestamp(2017,06,03)));
//        datalist1.add(new AlternateRecord(1,28,"宁雪",new Timestamp(2017,06,02)));
//        datalist1.add(new AlternateRecord(1,15,"刘妤涵",new Timestamp(2017,06,01)));
//        datalist1.add(new AlternateRecord(1,2,"牛馨曼",new Timestamp(2017,05,26)));

        OkHttpUtil.getInstance().getRank();
//        datalist2.add(new User("牛馨曼",null,5,18410));
//        datalist2.add(new User("孙佳",null,3,18910));
//        datalist2.add(new User("宁雪",null,6,15244));
//        datalist2.add(new User("刘妤涵",null,1,22595));
//        datalist2.add(new User("郑晓光",null,19,33875));
//        datalist2.add(new User("张家瑞",null,0,653));
    }

    @OnClick(R.id.water_btn)
    void waterClick(){
        waterBTN.setClickable(false);
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.watering);
        animation1.setFillAfter(false);
        wateringIV.startAnimation(animation1);

        wateringIV.setBackgroundResource(R.drawable.watering_anim);
        wateringIV.setVisibility(View.VISIBLE);
        final AnimationDrawable animationDrawable= (AnimationDrawable) wateringIV.getBackground();
        animationDrawable.start();
        wateringIV.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationDrawable.stop();
                wateringIV.setVisibility(View.GONE);
                waterBTN.setClickable(true);
            }
        },4500);

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String str) {
        if (mListener != null) {
            mListener.onFragmentInteraction(str);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onItemClick(User friend) {

        if(friend.getUserid()==SPUtil.getSP(getActivity(),"user").getLong("userid",0))
            return;

        Intent intent=new Intent(getActivity(), FriendInfoActivity.class);
        intent.putExtra("friendId",friend.getUserid());
        intent.putExtra("icon",friend.getIcon());
        intent.putExtra("name",friend.getName());
        intent.putExtra("power",friend.getPower());
        intent.putExtra("tomato_n",friend.getTomatoN());
        startActivity(intent);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String str);
    }
    abstract class MyAnimationDrawable extends AnimationDrawable {
        Handler finishHandler;      // 判断结束的Handler
        public MyAnimationDrawable() {
        }
        @Override
        public void start() {
            super.start();
            /**
             * 首先用父类的start()
             * 然后启动线程，来调用onAnimationEnd()
             */
            finishHandler = new Handler();
            finishHandler.postDelayed(
                    new Runnable() {
                        public void run() {
                            onAnimationEnd();
                        }
                    }, 4500);
        }
        /**
         * 这个方法获得动画的持续时间（之后调用onAnimationEnd()）
         */
        public int getTotalDuration() {
            int durationTime = 0;
            for (int i = 0; i < this.getNumberOfFrames(); i++) {
                durationTime += this.getDuration(i);
            }
            return durationTime;
        }
        /**
         * 结束时调用的方法，一定要实现
         */
        abstract void onAnimationEnd();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeSoil(SoilTimeBean soilTime){
        int time=soilTime.getTime()%1500;
        switch (time){
            case 0:
                AnimationDrawable animationDrawable=(AnimationDrawable)soilIV.getBackground();
                animationDrawable.setOneShot(true); //设置只播放一遍
                animationDrawable.start();
                break;
            case 300:
                soilIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.grow5));
                break;
            case 600:
                soilIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.grow6));
                break;
            case 900:
                soilIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.grow7));
                break;
            case 1200:
                soilIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.grow8));
                break;
            case 1400:
                soilIV.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.grow9));

                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setRank(List<User> userList){

        if(userList.size()!=0&&userList.get(0).getClass().getName().equals("com.globalformulae.shiguang.model.User")){
            Log.e("name", userList.get(0).getClass().getName());
            datalist2=userList;
            timerRankAdapter=new TimerRankAdapter(getContext(),datalist2);
            timerRankAdapter.setOnItemClickListener(this);
            timerRankRV.setAdapter(timerRankAdapter);
            timerRankRV.invalidate();
        }

    }
}
