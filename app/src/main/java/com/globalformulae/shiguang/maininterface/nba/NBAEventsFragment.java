package com.globalformulae.shiguang.maininterface.nba;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.NBAEvents;
import com.globalformulae.shiguang.maininterface.adapter.EventListAdapter;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * mvp模式
 */
public class NBAEventsFragment extends Fragment implements NBAContract.View{

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.rv_eventlists)
    RecyclerView mRecyclerView;
    @BindView(R.id.nba_event_srl)
    SwipeRefreshLayout nbaEventSRL;
    private NBAEvents mResult;
    private EventListAdapter mEventListAdapter;
    private List<NBAEvents.EventsResult.DayResult.Compatition> list;
    private NBAContract.Presenter nbaPresenter;

    public NBAEventsFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("lifecycle", "onActivityCreated: ");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("lifecycle", "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_nbaevents,container,false);
        ButterKnife.bind(this,view);
        initView(view);
        initDatas();
        Log.e("lifecycle", "onCreateView: ");
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Log.e("lifecycle", "onAttach: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 刷新列表（包含第一次
     * @param nbaEvents
     */
    @Override
    public void refreshList(NBAEvents nbaEvents) {
        mResult=nbaEvents;
        addData();//把获取到的数据添加到recyclerView中去
        if(nbaEventSRL.isRefreshing()){
            nbaEventSRL.setRefreshing(false);
        }
    }

    @Override
    public void showEmptyList() {

    }

    @Override
    public void showError() {
        StyleableToast st2 = new StyleableToast
                .Builder(getContext(), "获取数据失败，请稍后重试")
                .withMaxAlpha()
                .withBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                .withTextColor(Color.WHITE)
                .withBoldText()
                .withIcon(R.drawable.ic_autorenew_black_24dp,true)
                .build();
        st2.show();
    }

    @Override
    public void setPresenter(NBAContract.Presenter presenter) {
        nbaPresenter=presenter;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    //初始化页面
    public void initView(View view){
        if(isNetworkConnected(getContext())){
            StyleableToast st = new StyleableToast
                    .Builder(getContext(), "刷新中")
                    .withMaxAlpha()
                    .withBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .withTextColor(Color.WHITE)
                    .withBoldText()
                    .withIcon(R.drawable.ic_autorenew_black_24dp,true)
                    .build();
            st.show();
        }else{
            StyleableToast st2 = new StyleableToast
                    .Builder(getContext(), "没有网络可用哟，请检查网络配置")
                    .withMaxAlpha()
                    .withBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .withTextColor(Color.WHITE)
                    .withBoldText()
                    .withIcon(R.drawable.ic_autorenew_black_24dp,true)
                    .build();
            st2.show();
        }

        //选择一种LayoutManager 这里是线性垂直的List
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        //设置layoutManager
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //设置recycleView的item分割线,并设置为垂直
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        nbaEventSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDatas();
            }
        });
    }

    /**
     * 获取数据
     * retrofit+rxjava请求方式
     */
    public void initDatas(){
        nbaPresenter.refreshData();
    }

    /**
     * 把获取到的数据添加到recyclerView中去
     */
    private void addData(){
        list=mResult.getResult().getList().get(0).getTr();
        int p1=list.size();
        list.addAll(mResult.getResult().getList().get(1).getTr());
        if(mResult.getResult().getList().size()==3)
            list.addAll(mResult.getResult().getList().get(2).getTr());

        if(mEventListAdapter==null){
            //构建adapter,传入list
            mEventListAdapter=new EventListAdapter(getContext(),list);
            mEventListAdapter.setmItemClickListener(new EventListAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });
            //为recyclerView设置adapter
            mRecyclerView.setAdapter(mEventListAdapter);
        }else{
            mEventListAdapter.notifyItemRangeChanged(0,list.size()-1);
        }
        mRecyclerView.scrollToPosition(p1);
    }

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public void setList(List<NBAEvents.EventsResult.DayResult.Compatition> list) {
        this.list = list;
    }
}
