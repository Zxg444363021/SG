package com.globalformulae.shiguang.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by ZXG on 2017/8/19.
 */

public class MyStepListener implements SensorEventListener{
    private Context mContext;

    public MyStepListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //这里获取到的数据是自上一次开机以来的总步数。
        for(float f:event.values){
            Log.e("**********step2", String.valueOf(f));
        }

//        SharedPreferences sharedPreferences=mContext.getSharedPreferences("user",Context.MODE_PRIVATE);
//        Boolean isDirty=sharedPreferences.getBoolean("isDirty",true);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        if(!isDirty){
//            float startStep=sharedPreferences.getFloat("startStep",0);
//            if(event.values[0]<startStep)
//                editor.putFloat("todayStep",event.values[0]);
//            else
//                editor.putFloat("todayStep",event.values[0]-startStep);
//            editor.apply();
//        }else{
//            editor.putFloat("startStep",event.values[0]);
//            editor.putFloat("todayStep",0.0f);
//            editor.putBoolean("isDirty",false);
//            editor.apply();
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
