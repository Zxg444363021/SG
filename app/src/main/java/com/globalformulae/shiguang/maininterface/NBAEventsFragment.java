package com.globalformulae.shiguang.maininterface;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.maininterface.adapter.EventListAdapter;
import com.globalformulae.shiguang.model.NBAEvents;
import com.globalformulae.shiguang.utils.OkHttpUtil;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NBAEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NBAEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NBAEventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private OkHttpUtil mOkHttpUtil;
    private NBAEvents mResult;
    //    private EventAdapter eventAdapter;
    private EventListAdapter mEventListAdapter;
    private List<NBAEvents.EventsResult.DayResult.Compatition> list;

    private String mTitle;
    private View view;



    public NBAEventsFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NBAEventsFragment.
     */
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
        view=inflater.inflate(R.layout.fragment_nbaevents,container,false);
        initView();
        initDatas();
        // Inflate the layout for this fragment
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //初始化数据
    public void initView(){
//初始化
        mRecyclerView= (RecyclerView)view.findViewById(R.id.rv_eventlists);
        StyleableToast st = new StyleableToast
                .Builder(getContext(), "刷新中")
                .withMaxAlpha()
                .withBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                .withTextColor(Color.WHITE)
                .withBoldText()
                .withIcon(R.drawable.ic_autorenew_black_24dp,true)
                .build();
        StyleableToast st2 = new StyleableToast
                .Builder(getContext(), "没有网络可用哟，请检查网络配置")
                .withMaxAlpha()
                .withBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                .withTextColor(Color.WHITE)
                .withBoldText()
                .withIcon(R.drawable.ic_autorenew_black_24dp,true)
                .build();

        if(isNetworkConnected(getContext())){
            st.show();
        }else{
            st2.show();
        }

        //选择一种LayoutManager 这里是线性垂直的List
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        //设置layoutManager
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //设置recycleView的item分割线,并设置为垂直
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

    }
    public void initDatas(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOkHttpUtil= OkHttpUtil.getInstance();
                mResult=mOkHttpUtil.NbaEvents();
                if(mResult!=null) {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", "success");
                    msg.setData(data);
                    handler.sendMessage(msg);
                }else{
                    Log.e("nba","event设置");
                }
            }
        }).start();
    }
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            if(val.equals("success")){
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
                    mEventListAdapter.setmDatas(list);
                    mRecyclerView.invalidate();
                }
                mRecyclerView.scrollToPosition(p1);

            }else{
                Log.e("nba","不太对");
            }

        }
    };


    public void setList(List<NBAEvents.EventsResult.DayResult.Compatition> list) {
        this.list = list;
    }
}
