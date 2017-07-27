package com.globalformulae.shiguang.maininterface;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.globalformulae.shiguang.greendao.DaoMaster;

/**
 * Created by  on 2017/4/11.
 */

public class MyApplication extends Application {
    private static DaoMaster daoMaster;
    private static boolean isTiming;
    public static int remainTime;
    public static boolean isOnLine=false;
    public MyApplication() {
    }
    static MyApplication myInstance=new MyApplication();
    public static MyApplication getInstance(){
        return myInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        setUpDatabase();
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        //注册推送服务，每次调用register方法都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//            @Override
//            public void onSuccess(String deviceToken) {
//                Log.e("deviceToken", deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                Log.e("deviceToken", s+"   "+s1);
//            }
//        });
    }

    private void setUpDatabase(){
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "myData.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        daoMaster = new DaoMaster(db);
    }
    public static DaoMaster getDaoMaster(){
        return daoMaster;
    }

    public static boolean isTiming() {
        return isTiming;
    }

    public static void setIsTiming(boolean isTiming) {
        MyApplication.isTiming = isTiming;
    }


}
