package com.globalformulae.shiguang.maininterface.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
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
import com.globalformulae.shiguang.bean.OnesRecord;
import com.globalformulae.shiguang.bean.SoilTimeBean;
import com.globalformulae.shiguang.bean.User;
import com.globalformulae.shiguang.maininterface.FriendInfoActivity;
import com.globalformulae.shiguang.maininterface.adapter.SimpleRecordAdapter;
import com.globalformulae.shiguang.maininterface.adapter.TimerRankAdapter;
import com.globalformulae.shiguang.retrofit.RetrofitHelper;
import com.globalformulae.shiguang.retrofit.UserActionService;
import com.globalformulae.shiguang.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.globalformulae.shiguang.utils.SPUtil.getSP;


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
    @BindView(R.id.more_record_btn)
    Button moreRecordBTN;
    @BindView(R.id.more_friend_btn)
    Button moreFriendBTN;


    private List<OnesRecord> myRecordList=new ArrayList<>();
    private List<User> userRankList =new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private SimpleRecordAdapter simpleRecordAdapter;//上面的5条记录
    private TimerRankAdapter timerRankAdapter;//下面的10条记录
    private Retrofit retrofit;
    private UserActionService userActionService;


    public TimerFragment() {
        // Required empty public constructor
    }

    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit= RetrofitHelper.getInstance();
        userActionService=retrofit.create(UserActionService.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_timer, container, false);
        ButterKnife.bind(this,view);
        timerRecordRV.setLayoutManager(new LinearLayoutManager(this.getContext()));
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.sun_anim);
        sunIV.setAnimation(animation);
        timerRankRV.setLayoutManager(new LinearLayoutManager(this.getContext()));
        timerRankRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                Paint paint=new Paint();
                paint.setColor(getResources().getColor(R.color.divider));
                int childCount=state.getItemCount();
                for (int i = 0; i < childCount-1; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    final int top = child.getBottom() + params.bottomMargin +
                            Math.round(ViewCompat.getTranslationY(child));
                    c.drawLine(dip2px(getActivity(),46),top,parent.getWidth(),top,paint);
                }
            }
        });


        return view;
    }
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    public void initView(){
        if(!getSP(getActivity(),"user").getBoolean("isOnline",false)){
            errorPage.setVisibility(View.VISIBLE);
            normalPage.setVisibility(View.GONE);
            return;
        }
        errorPage.setVisibility(View.GONE);
        normalPage.setVisibility(View.VISIBLE);
        SharedPreferences sp= getSP(getActivity(),"user");
        timerPowerTV.setText(String.valueOf(sp.getInt("power_n",0))+"g");
        timerTomatoNTV.setText(String.valueOf(sp.getInt("tomato_n",0)));
        Glide.with(getActivity()).load(sp.getString("icon",null)).placeholder(R.mipmap.unlogged_icon).into(timerIconIV);
        getTimerRR();
    }

    /**
     * 获取排行榜和我的记录
     */
    private void getTimerRR(){
        //获取排名
        userActionService.doGetRank(String.valueOf(getSP(getActivity(), "user").getLong("userid", 0)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<User>>() {
                    @Override
                    public void accept(@NonNull List<User> users) throws Exception {
                        userRankList =users;
                        timerRankAdapter=new TimerRankAdapter(getContext(), userRankList);
                        timerRankAdapter.setOnItemClickListener(TimerFragment.this);
                        timerRankRV.setAdapter(timerRankAdapter);
                        timerRankRV.invalidate();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }).subscribe();

        //获取我的记录
        userActionService.doGetRecord(String.valueOf(getSP(getActivity(), "user").getLong("userid", 0)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<OnesRecord>>() {
                    @Override
                    public void accept(@NonNull List<OnesRecord> alternateRecords) throws Exception {
                        myRecordList=alternateRecords;
                        simpleRecordAdapter=new SimpleRecordAdapter(getActivity(),myRecordList);
                        timerRecordRV.setAdapter(simpleRecordAdapter);
                        if(myRecordList.size()==0){
                            moreRecordBTN.setText("暂无动态");
                        }else {
                            if(myRecordList.size()<=5&&myRecordList.size()!=0){
                                moreRecordBTN.setVisibility(View.GONE);
                            }
                            if(userRankList.size()<=10&& userRankList.size()>=0){
                                moreFriendBTN.setVisibility(View.GONE);
                            }else{
                                moreRecordBTN.setText("查看更多动态");
                            }
                        }
                        //timerRecordRV.invalidate();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        EventBus.getDefault().register(this);
    }

    /**
     * 排行榜点击事件，跳转至朋友页面
     * @param friend
     */
    @Override
    public void onItemClick(User friend) {

        if(friend.getUserid()== getSP(getActivity(),"user").getLong("userid",0))
            return;

        Intent intent=new Intent(getActivity(), FriendInfoActivity.class);
        intent.putExtra("friendId",friend.getUserid());
        intent.putExtra("icon",friend.getIcon());
        intent.putExtra("name",friend.getName());
        intent.putExtra("power",friend.getPower());
        intent.putExtra("tomato_n",friend.getTomatoN());
        intent.putExtra("power1Yesterday",friend.getPower1Yesterday());
        intent.putExtra("power2Yesterday",friend.getPower2Yesterday());
        intent.putExtra("power1CanSteal",friend.getPower1CanSteal());
        intent.putExtra("power2CanSteal",friend.getPower2CanSteal());
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



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeSoil(SoilTimeBean soilTime){
        Log.e("changeSoil", "啊哈？");
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

}
