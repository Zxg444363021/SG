package com.globalformulae.shiguang.retrofit;

import com.globalformulae.shiguang.model.NBAEvents;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ZXG on 2017/7/17.
 */

public interface NbaService {
    @GET("onebox/basketball/nba")
    Observable<NBAEvents> getNBAEvent(@Query("key") String key);
}
