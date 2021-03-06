package com.globalformulae.shiguang.maininterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.utils.NetServiceUser;
import com.globalformulae.shiguang.utils.OkHttpUtil;
import com.globalformulae.shiguang.utils.SPUtil;

import java.util.Comparator;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends AppCompatActivity implements NetServiceUser{



    @BindView(R.id.my_icon_iv)  //头像
    ImageView myIconIV;
    @BindView(R.id.my_name_tv)  //昵称
    TextView myNameTV;
    @BindView(R.id.my_phone_tv) //手机号
    TextView myPhoneTV;
    @BindView(R.id.my_tomato_n_tv)  //番茄数
    TextView myTomatoNTV;
    @BindView(R.id.my_power_n_tv)   //总能量
    TextView myPowerNTV;
    @BindView(R.id.user_n_fl)   //昵称行
    View userNFL;
    @BindView(R.id.user_p_fl)   //手机号行
    View userPFL;
    @BindView(R.id.user_t_fl)   //番茄行
    View userTFL;
    @BindView(R.id.user_exit)   //退出
    View userEFL;
    @BindView(R.id.my_step_tv)
    TextView myStepTV;
    @BindView(R.id.modify_schedule)
    FrameLayout modifyScheduleFL;

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        sp=SPUtil.getSP(this,"user");
        setActionBar();
        initView();

    }
    private void getMyInfo(){
        TreeMap<Integer,Integer> treeMap=new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 0;
            }
        });

    }

    private void initView(){
        Glide.with(this).load(sp.getString("icon", "")).placeholder(R.mipmap.unlogged_icon).into(myIconIV);
        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis()))) 用来保持头像始终重新加载
        myNameTV.setText(sp.getString("name",""));
        myPhoneTV.setText(sp.getString("phone",""));
        myTomatoNTV.setText(String.valueOf(sp.getInt("tomato_n",0)));
        myPowerNTV.setText(String.valueOf(sp.getInt("power_n",0))+"g");
        myStepTV.setText(Float.toString(sp.getFloat("todayStep",0))+"步");
    }

    @OnClick(R.id.my_icon_iv)
    void click(){
        Intent intent=new Intent(UserInfoActivity.this,IconActivity.class);
        startActivityForResult(intent,666);
    }

    /**
     * 昵称
     */
    @OnClick(R.id.user_n_fl)
    void click1(){

    }

    /**
     * 手机号
     */
    @OnClick(R.id.user_p_fl)
    void click2(){

    }

    /**
     * 番茄数量
     */
    @OnClick(R.id.user_t_fl)
    void click3(){
    }



    /**
     * 退出
     */
    @OnClick(R.id.user_exit)
    void click4(){
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.apply();
        ((MyApplication)getApplication()).isOnLine=false;
        finish();
    }

    @OnClick(R.id.modify_schedule)
    void click5(){

    }



    @Override
    public void connectedcallBack(int code, int type, String result, String message) {

    }

    /**
     * 选完头像之后回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==666&&resultCode==888){
            Log.e("onActivityResult", "onActivityResult: ");
            String defaultIconUrl=OkHttpUtil.BASEURL+"files/"+sp.getString("phone","")+".jpg";
            sp.edit().putString("icon",defaultIconUrl).apply();
            initView();
        }

    }
    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_info_tb);
        toolbar.setTitle("个人信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
