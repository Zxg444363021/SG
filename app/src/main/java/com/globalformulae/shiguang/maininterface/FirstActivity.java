package com.globalformulae.shiguang.maininterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalformulae.shiguang.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstActivity extends AppCompatActivity {

    @BindView(R.id.welcome_iv)
    ImageView weclomeIV;
    @BindView(R.id.welcome_tv)
    TextView welcomeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome_anim);
        weclomeIV.setAnimation(animation);
        weclomeIV.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(FirstActivity.this, R.anim.text_anim);
                welcomeTV.startAnimation(animation);
                welcomeTV.setVisibility(View.VISIBLE);
            }
        },3000);
        weclomeIV.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
        super.onResume();
    }
}