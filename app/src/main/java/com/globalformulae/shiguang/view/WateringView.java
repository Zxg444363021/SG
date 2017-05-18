package com.globalformulae.shiguang.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by ZXG on 2017/5/2.
 */

public class WateringView extends android.support.v7.widget.AppCompatImageView {
    private AnimationDrawable anim;
    public WateringView(Context context){
        super(context);
    }
    public void setAnim(AnimationDrawable anim){
        this.anim=anim;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        try{
            Field field= AnimationDrawable.class.getDeclaredField("mCurFrame");
            field.setAccessible(true);
            int curFram=field.getInt(anim);
            if(curFram==anim.getNumberOfFrames()-1);{
                setVisibility(View.GONE);
            }
        }catch (Exception e){

        }
        super.onDraw(canvas);
    }
}
