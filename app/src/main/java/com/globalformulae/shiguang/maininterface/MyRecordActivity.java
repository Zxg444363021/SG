package com.globalformulae.shiguang.maininterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.maininterface.adapter.ComplexRecordAdapter;
import com.globalformulae.shiguang.model.AlternateRecord;
import com.globalformulae.shiguang.retrofit.RetrofitHelper;
import com.globalformulae.shiguang.retrofit.UserActionService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.globalformulae.shiguang.utils.SPUtil.getSP;

public class MyRecordActivity extends AppCompatActivity {
    @BindView(R.id.my_record_tb)
    Toolbar myRecordTB;
    @BindView(R.id.my_record_rv)
    RecyclerView myRecordRV;
    private Retrofit retrofit;
    private UserActionService userActionService;
    private List<AlternateRecord> mDataList;
    private ComplexRecordAdapter complexRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_record);
        ButterKnife.bind(this);
        setActioinBar();
        retrofit= RetrofitHelper.getInstance();
        initData();
    }
    private void initData(){
        userActionService=retrofit.create(UserActionService.class);
        userActionService.doGetRecord(String.valueOf(getSP(MyRecordActivity.this, "user").getLong("userid", 0)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AlternateRecord>>() {
            @Override
            public void accept(@NonNull List<AlternateRecord> alternateRecords) throws Exception {
                mDataList=alternateRecords;
                complexRecordAdapter=new ComplexRecordAdapter(MyRecordActivity.this,mDataList);
                myRecordRV.setAdapter(complexRecordAdapter);
                myRecordRV.setLayoutManager(new LinearLayoutManager(MyRecordActivity.this,LinearLayoutManager.VERTICAL,false));
                myRecordRV.smoothScrollToPosition(0);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });
    }

    private void setActioinBar(){
        setSupportActionBar(myRecordTB);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myRecordTB.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
