package com.globalformulae.shiguang.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;


/**
 * Created by ZXG on 2017/5/12.
 * 单例模式加工厂模式
 */

public class SPUtil {
    private static SharedPreferences userSp;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;
    public static SharedPreferences getSP(ContextWrapper contextWrapper,String name){
        if(name.equals("user")){
            if(userSp==null){
                userSp=contextWrapper.getSharedPreferences(name,Context.MODE_PRIVATE);
            }
            return userSp;
        }else{
            sp=contextWrapper.getSharedPreferences(name,Context.MODE_PRIVATE);
            return sp;
        }
    }
    public static SharedPreferences.Editor getSPD(ContextWrapper contextWrapper, String name){
        if(name.equals("user")){
            if(userSp==null){
                getSP(contextWrapper,name);
            }
            editor=userSp.edit();
            return editor;
        }
        else{
            if(sp==null){
                getSP(contextWrapper,name);
            }
            editor=sp.edit();
            return editor;
        }

    }
}
