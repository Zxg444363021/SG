package com.globalformulae.shiguang.maininterface;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.maininterface.adapter.EventListAdapter;
import com.globalformulae.shiguang.model.NBAEvents;
import com.globalformulae.shiguang.retrofit.NbaService;
import com.globalformulae.shiguang.utils.IPConfig;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class NBAEventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.rv_eventlists)
    RecyclerView mRecyclerView;
    @BindView(R.id.nba_event_srl)
    SwipeRefreshLayout nbaEventSRL;
    private NBAEvents mResult;
    private EventListAdapter mEventListAdapter;
    private List<NBAEvents.EventsResult.DayResult.Compatition> list;

    private String mTitle;



    public NBAEventsFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static NBAEventsFragment newInstance(String param1, String param2) {
        NBAEventsFragment fragment = new NBAEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_nbaevents,container,false);
        ButterKnife.bind(this,view);
        initView(view);
        initDatas();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                mResult=nbaEvents;
                addData();//把获取到的数据添加到recyclerView中去
            }

            @Override
            public void onError(@NonNull Throwable e) {
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
            public void onComplete() {
                if(nbaEventSRL.isRefreshing()){
                    nbaEventSRL.setRefreshing(false);
                }
            }
        });
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
