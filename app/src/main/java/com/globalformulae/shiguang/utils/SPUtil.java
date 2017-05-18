package com.globalformulae.shiguang.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;


/**
 * Created by ZXG on 2017/5/12.
 */

public class SPUtil {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;
    public static SharedPreferences getSP(ContextWrapper contextWrapper,String name){
        sp=contextWrapper.getSharedPreferences(name,Context.MODE_PRIVATE);
        return sp;
    }
    public static SharedPreferences.Editor getSPD(ContextWrapper contextWrapper, String name){
        if(sp==null){
            getSP(contextWrapper,name);
        }
        editor=sp.edit();
        return editor;
    }
}
