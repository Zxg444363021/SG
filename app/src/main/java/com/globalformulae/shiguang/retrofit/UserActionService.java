package com.globalformulae.shiguang.retrofit;

import com.globalformulae.shiguang.model.AlternateRecord;
import com.globalformulae.shiguang.model.Power;

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
    Observable<List<AlternateRecord>> doGetRecord(@Field("userid") String userid);

    /**
     * 能量交互
     * @param user1id
     * @param user2id
     * @param powertype
     * @return
     */
    @FormUrlEncoded
    @POST("doStealPower")
    Observable<AlternateRecord> doStealPower(@Field("user1id") String user1id,@Field("user2id") String user2id,@Field("powertype") String powertype);

    /**
     * 获取我是否可以偷朋友能量
     * @param user1id
     * @param user2id
     * @return
     */
    @FormUrlEncoded
    @POST("doGetFriendInfo")
    Observable<Power> doGetFriendInfo(@Field("user1id") String user1id,@Field("user2id") String user2id);
}
