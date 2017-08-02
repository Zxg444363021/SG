package com.globalformulae.shiguang.retrofit;

import com.globalformulae.shiguang.bean.AlternateRecord;
import com.globalformulae.shiguang.bean.OnesRecord;
import com.globalformulae.shiguang.bean.ResponseBean;
import com.globalformulae.shiguang.bean.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Observable<AlternateRecord> doStealPower(@Field("user1id") String user1id, @Field("user2id") String user2id, @Field("powertype") String powertype);

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


    @FormUrlEncoded
    @POST("doGainPower")
    Observable<String> doGainPower(@Field("userid")String userid,@Field("powertype")String powertype);


    @FormUrlEncoded
    @POST
    Observable<String> doAddPower(@Field("userid")String userid,@Field("tomatoTime")String tomatoTime,@Field("powertype")String powertype);
}
