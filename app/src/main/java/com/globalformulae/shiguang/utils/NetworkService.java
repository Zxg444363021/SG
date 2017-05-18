package com.globalformulae.shiguang.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by 彭阳坤 on 2016/3/14.
 * Description:网络服务公共类
 */
public class NetworkService {

    /**
     * 描述:判断网络状态是可用
     * @author 彭阳坤
     * @time 2016/3/14 16:23
     * @param activity
     * @return
     */
    public static boolean checkNetworkState(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connManager.getAllNetworkInfo();
        for (int i = 0; i < networkInfo.length; i++) {
            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }
}
