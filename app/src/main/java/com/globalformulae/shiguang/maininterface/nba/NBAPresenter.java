package com.globalformulae.shiguang.maininterface.nba;

import android.support.annotation.NonNull;

import com.globalformulae.shiguang.bean.NBAEvents;
import com.globalformulae.shiguang.model.NBAModel;

/**
 * Created by ZXG on 2017/7/24.
 */

public class NBAPresenter implements NBAContract.Presenter{
    //view
    @NonNull
    private NBAContract.View mView;
    //model
    @NonNull
    private NBAModel mModel;

    //需要setPresenter(),否则view无法向presenter交互
    public NBAPresenter(@NonNull NBAContract.View mView, @NonNull NBAModel mModel) {
        this.mView = mView;
        this.mModel = mModel;
        this.mView.setPresenter(this);
    }

    @Override
    public void refreshData() {
        //model的回调接口
        mModel.doGetNBAEvents(new NBAModel.RefreshDataCallBack() {
            @Override
            public void onDataGet(NBAEvents nbaEvents) {
                mView.refreshList(nbaEvents);
            }

            @Override
            public void onDataGetError() {
                mView.showError();
            }

            @Override
            public void onDataGetComplete() {

            }
        });
    }


    @Override
    public void start() {

    }
}
