package com.globalformulae.shiguang.retrofit;

import com.globalformulae.shiguang.bean.OnesRecord;
import com.globalformulae.shiguang.bean.ResponseBean;
import com.globalformulae.shiguang.bean.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ZXG on 2017/7/17.
 */

public interface UserActionService {

    /**
     * 获取交互记录
     * @param userid
     * @return
     */
    @FormUrlEncoded
    @POST("doGetRecord")
    Observable<List<OnesRecord>> doGetRecord(@Field("userid") String userid);

    /**
     * 能量交互
     * @param user1id
     * @param user2id
     * @param powertype
     * @return
     */
    @FormUrlEncoded
    @POST("doStealPower")
    Observable<ResponseBean> doStealPower(@Field("user1id") String user1id, @Field("user2id") String user2id, @Field("powertype") String powertype);

    /**
     * 获取排名
     * @param userid
     * @return
     */
    @FormUrlEncoded
    @POST("doGetRank")
    Observable<List<User>> doGetRank(@Field("userid")String userid);

    /**
     * 登录
     * @param phone
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Observable<ResponseBean> doLogin(@Field("phone")String phone, @Field("password")String password);

    /**
     * 发送注册验证码
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("sendIdentifyCode")
    Observable<ResponseBean> doSendIdentifyCode(@Field("phone")String phone);

    /**
     * 注册
     * @param phone
     * @param password
     * @param identifyCode
     * @param name
     * @return
     */
    @FormUrlEncoded
    @POST("regist")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    Observable<ResponseBean> doRegister(@Field("phone")String phone,@Field("password")String password,@Field("identifyCodeC")String identifyCode,@Field("name")String name);

    /**
     * 收取自己的能量
     * @param userid
     * @param powertype
     * @return
     */
    @FormUrlEncoded
    @POST("doGainPower")
    Observable<String> doGainPower(@Field("userid")String userid,@Field("powertype")String powertype);

    /**
     * 种番茄上传能量
     * @param userid
     * @param tomatoTime
     * @param powertype
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<String> doAddPower(@Field("userid")String userid,@Field("tomatoTime")String tomatoTime,@Field("powertype")String powertype);

    /**
     * 查看我的某一位好友的几种能量是否能被偷
     * @param user1id 我的id
     * @param user2id 好友的id
     * @return
     */
    @FormUrlEncoded
    @POST("doGetCanSteal")
    Observable<ResponseBean> doGetCanBeSteal(@Field("user1id")String user1id,@Field("user2id")String user2id);


}
