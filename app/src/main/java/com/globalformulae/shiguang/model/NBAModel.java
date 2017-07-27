package com.globalformulae.shiguang.model;

import com.globalformulae.shiguang.bean.NBAEvents;
import com.globalformulae.shiguang.retrofit.NbaService;
import com.globalformulae.shiguang.utils.IPConfig;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ZXG on 2017/7/24.
 * 用来获取NBA数据的Model
 */

public class NBAModel {
    /**
     * 做请求，retrofit+rxjava模式
     * 通过回调接口来通知presenter
     * @param mRefreshDataCallBack
     */
    public void doGetNBAEvents(final RefreshDataCallBack mRefreshDataCallBack){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(IPConfig.getUrl("juheData"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        NbaService nbaService=retrofit.create(NbaService.class);
        nbaService.getNBAEvent(IPConfig.getUrl("nbaEventKey"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NBAEvents>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull NBAEvents nbaEvents) {
                        mRefreshDataCallBack.onDataGet(nbaEvents);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mRefreshDataCallBack.onDataGetError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    //回调接口
    public interface RefreshDataCallBack{
        void onDataGet(NBAEvents nbaEvents);
        void onDataGetError();
        void onDataGetComplete();
    }
}
