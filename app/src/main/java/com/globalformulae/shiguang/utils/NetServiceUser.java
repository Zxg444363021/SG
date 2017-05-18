package com.globalformulae.shiguang.utils;

/**
 * Created by 彭阳坤 on 2016/3/17.
 * Description: 提供网络通信的回调接口
 */
public interface NetServiceUser {

    public static final int CONNECT_FAIL = 0;  //连接网络失败
    public static final int CONNECT_SUCC = 1;  //连接成功，并获得返回数据
    public static final int CONNECT_ERROR = 2; //连接失败，获得错误信息
    /**
     * 描述:通信完成后会回调此方法，并将结果传入result
     * @author 彭阳坤
     * @time 2016/3/18 10:37
     * @param code 链接标识，包括CONNECT_FAIL，CONNECT_SUCC，CONNECT_ERROR
     * @param type 用于标识同一个User调用不同的通信
     * @param result 链接成功返回服务器结果，否则返回null
     * @param message 返回的状态信息
     */
    void connectedcallBack(int code, int type, String result, String message);

}
