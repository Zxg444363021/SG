package com.globalformulae.shiguang.maininterface;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Window;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.maininterface.adapter.FragmentAdapter;
import com.globalformulae.shiguang.model.NBAEvents;
import com.globalformulae.shiguang.utils.OkHttpUtil;

import java.util.ArrayList;
import java.util.List;


//使用了recycleView
public class NBAActivity extends AppCompatActivity implements NBAEventsFragment.OnFragmentInteractionListener,TermRankingFragment.OnFragmentInteractionListener {
    private static final String TAG = "NBAActivity";


    private RecyclerView mRecyclerView;
    private OkHttpUtil mOkHttpUtil;
    private NBAEvents mResult;

    private List<NBAEvents.EventsResult.DayResult.Compatition> list; //存储返回的json数据


    private TabLayout tabs;
    private List<Fragment> mFragments=new ArrayList<>();
    private List<String> mTitles=new ArrayList<String>(){};
    private ViewPager mViewPager;
    private NBAEventsFragment mNBAEventFragment;
    private TermRankingFragment mTermRankingFragment;
    private FragmentAdapter mNbaFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode().setDuration(3000));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nba);
        initView();
    }
    //初始化数据
    public void initView(){
        tabs= (TabLayout) findViewById(R.id.tab_nba);
        mViewPager= (ViewPager) findViewById(R.id.vp_nba);
        tabs.addTab(tabs.newTab().setText("赛事"));
        tabs.addTab(tabs.newTab().setText("排名"));
        mTitles.add("赛事");
        mTitles.add("排名");
        mNBAEventFragment=new NBAEventsFragment();
        mTermRankingFragment=new TermRankingFragment();
        mFragments.add(mNBAEventFragment);
        mFragments.add(mTermRankingFragment);
        mNbaFragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),mFragments,mTitles);
        mViewPager.setAdapter(mNbaFragmentAdapter);
        tabs.setupWithViewPager(mViewPager);
        //setActionBar();
    }


    /**
     * 判断是否有网络连接，可以复用
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


    @Override
    public void onFragmentInteraction(Uri uri) {
    }



    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_nba);
        toolbar.setTitle("NBA");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
