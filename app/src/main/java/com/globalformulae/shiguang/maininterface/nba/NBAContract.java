package com.globalformulae.shiguang.maininterface.nba;

import com.globalformulae.shiguang.BasePresenter;
import com.globalformulae.shiguang.BaseView;
import com.globalformulae.shiguang.bean.NBAEvents;

/**
 * Created by ZXG on 2017/7/24.
 * 为啥写一个Contract,就是为了区别不同的activity或者fragment有不同的presenter和view
 */

public interface NBAContract {
    interface Presenter extends BasePresenter{
        void refreshData();
    }
    interface View extends BaseView<Presenter>{
        void refreshList(NBAEvents nbaEvents);
        void showEmptyList();
        void showError();
    }
}
