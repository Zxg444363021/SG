package com.globalformulae.shiguang.maininterface;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.globalformulae.shiguang.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DurmActivity extends AppCompatActivity {
    @BindView(R.id.durm13_iv)
    ImageView durm13IV;
    @BindView(R.id.durm12_iv)
    ImageView durm12IV;
    @BindView(R.id.durm11_iv)
    ImageView durm11IV;
    @BindView(R.id.durm10_iv)
    ImageView durm10IV;
    @BindView(R.id.durm9_iv)
    ImageView durm9IV;
    @BindView(R.id.durm8_iv)
    ImageView durm8IV;
    @BindView(R.id.durm7_iv)
    ImageView durm7IV;
    @BindView(R.id.durm6_iv)
    ImageView durm6IV;
    @BindView(R.id.durm5_iv)
    ImageView durm5IV;
    @BindView(R.id.durm4_iv)
    ImageView durm4IV;
    @BindView(R.id.durm3_iv)
    ImageView durm3IV;
    @BindView(R.id.durm2_iv)
    ImageView durm2IV;
    @BindView(R.id.durm1_iv)
    ImageView durm1IV;

    private SoundPool sp;
    private Map<Integer,Integer> map=new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_durm);
        ButterKnife.bind(this);
        initSoundPool();

    }
    public void initSoundPool(){
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(10);
        //AudioAttributes是一个封装音频各种属性的方法
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//设置音频流的合适的属性
        spb.setAudioAttributes(attrBuilder.build());//加载一个AudioAttributes
        sp = spb.build();

        map.put(12,sp.load(this,R.raw.bell,1));
        map.put(7,sp.load(this,R.raw.openhh,1));
        map.put(8,sp.load(this,R.raw.closehh,1));
        map.put(2,sp.load(this,R.raw.kick,1));
        map.put(3,sp.load(this,R.raw.snare,1));
        map.put(9,sp.load(this,R.raw.crash1,1));
        map.put(10,sp.load(this,R.raw.splash,1));
        map.put(11,sp.load(this,R.raw.crash2,1));
        map.put(4,sp.load(this,R.raw.tom1,1));
        map.put(5,sp.load(this,R.raw.tom2,1));
        map.put(6,sp.load(this,R.raw.tom3,1));
        map.put(13,sp.load(this,R.raw.floor,1));
        map.put(1,sp.load(this,R.raw.kick,1));
    }
    @OnClick(R.id.durm1_iv)
    void d1(){
        sp.play(map.get(1),1,1,0,0,1);
    }
    @OnClick(R.id.durm2_iv)
    void d2(){
        sp.play(map.get(2),1,1,0,0,1);
    }
    @OnClick(R.id.durm3_iv)
    void d3(){
        sp.play(map.get(3),1,1,0,0,1);
    }
    @OnClick(R.id.durm4_iv)
    void d4(){
        sp.play(map.get(4),1,1,0,0,1);
    }
    @OnClick(R.id.durm5_iv)
    void d5(){
        sp.play(map.get(5),1,1,0,0,1);
    }
    @OnClick(R.id.durm6_iv)
    void d6(){
        sp.play(map.get(6),1,1,0,0,1);
    }
    @OnClick(R.id.durm7_iv)
    void d7(){
        sp.play(map.get(7),1,1,0,0,1);
    }
    @OnClick(R.id.durm8_iv)
    void d8(){
        sp.play(map.get(8),1,1,0,0,1);
    }
    @OnClick(R.id.durm9_iv)
    void d9(){
        sp.play(map.get(9),1,1,0,0,1);
    }
    @OnClick(R.id.durm10_iv)
    void d10(){
        sp.play(map.get(10),1,1,0,0,1);
    }
    @OnClick(R.id.durm11_iv)
    void d11(){
        sp.play(map.get(11),1,1,0,0,1);
    }
    @OnClick(R.id.durm12_iv)
    void d12(){
        sp.play(map.get(12),1,1,0,0,1);
    }
    @OnClick(R.id.durm13_iv)
    void d13(){
        sp.play(map.get(13),1,1,0,0,1);
    }
}
