package com.globalformulae.shiguang.maininterface.MainFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.MyDate;
import com.globalformulae.shiguang.bean.Schedule;
import com.globalformulae.shiguang.greendao.DaoSession;
import com.globalformulae.shiguang.greendao.ScheduleDao;
import com.globalformulae.shiguang.maininterface.MyApplication;
import com.globalformulae.shiguang.maininterface.adapter.ScheduleAdapter;
import com.globalformulae.shiguang.view.ScheduleDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("ValidFragment")
public class ScheduleFragment extends Fragment implements ScheduleAdapter.onScheduleItemClickListener{


    @BindView(R.id.rv_schedlue)
    RecyclerView mSchedlueRecyclerView;
    @BindView(R.id.fab_add_event)
    FloatingActionButton floatingActionButton;

    private ScheduleAdapter scheduleAdapter;
    private ScheduleDao scheduleDao;

    private List<Schedule> mDatas=new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    //显示事件的日期
    private int mYear;
    private int mMonth;
    private int mDay;

    private int mDayOfWeek;
    private String xinqi;

    private MyDate myDate;


    public ScheduleFragment() {
        // Required empty public constructor
    }
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaoSession daoSession = MyApplication.getDaoMaster().newSession();
        scheduleDao=daoSession.getScheduleDao();
        EventBus.getDefault().register(this);
    }

    /**
     * 获取今天的数据
     * rxjava方式，在io线程中进行数据库操作
     */
    private void initDate(){
        Calendar mCalendar=Calendar.getInstance();
        mYear=mCalendar.get(Calendar.YEAR);
        mMonth=mCalendar.get(Calendar.MONTH)+1;
        mDay=mCalendar.get(Calendar.DAY_OF_MONTH);
        mDayOfWeek=mCalendar.get(Calendar.DAY_OF_WEEK);
        xinqi=getDayOfWeek(mDayOfWeek);

        Observable.create(new ObservableOnSubscribe<List<Schedule>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Schedule>> e) throws Exception {
                QueryBuilder<Schedule> qb = scheduleDao.queryBuilder();
                qb.where(qb.and(ScheduleDao.Properties.Year.eq(mYear),ScheduleDao.Properties.Month.eq(mMonth),
                        ScheduleDao.Properties.Day.eq(mDay)));
                qb.orderDesc(ScheduleDao.Properties.Status);
                qb.build();
                e.onNext(qb.list());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<List<Schedule>>() {
            @Override
            public void accept(@NonNull List<Schedule> schedules) throws Exception {
                mDatas=schedules;
                scheduleAdapter=new ScheduleAdapter(ScheduleFragment.this.getActivity(),mDatas,mYear,mMonth,mDay);
                scheduleAdapter.setOnScheduleItemClickListener(ScheduleFragment.this);
                mSchedlueRecyclerView.setAdapter(scheduleAdapter);
            }
        }).subscribe();

    }


    /**
     * 修改事件之后回调
     * @param schedule
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onMessageEvent(Schedule schedule){
        QueryBuilder<Schedule> qb = scheduleDao.queryBuilder();
        qb.where(qb.and(ScheduleDao.Properties.Year.eq(mYear),ScheduleDao.Properties.Month.eq(mMonth),
                ScheduleDao.Properties.Day.eq(mDay)));
        qb.orderDesc(ScheduleDao.Properties.Status);
        qb.build();
        mDatas=qb.list();
        //如果是新建事件
        if(schedule.isIfNew()){
            scheduleAdapter.add(schedule);
        }
        //否则刚才是在修改事件，就执行修改方法。
        else{
            scheduleAdapter.update(schedule);
        }
        mSchedlueRecyclerView.scrollToPosition(0);
    }

    /**
     * 修改日期,刷新显示
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeDate(MyDate date){
        myDate=date;
        mYear=date.getYear();
        mMonth=date.getMonth();
        mDay=date.getDay();
        mDayOfWeek=date.getDayOfWeek();
        xinqi=date.getXinqi();

        Observable.create(new ObservableOnSubscribe<List<Schedule>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Schedule>> e) throws Exception {
                QueryBuilder<Schedule> qb = scheduleDao.queryBuilder();
                qb.where(qb.and(ScheduleDao.Properties.Year.eq(mYear),ScheduleDao.Properties.Month.eq(mMonth),
                        ScheduleDao.Properties.Day.eq(mDay)));
                qb.orderDesc(ScheduleDao.Properties.Status);
                qb.build();
                e.onNext(qb.list());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Schedule>>() {
            @Override
            public void accept(@NonNull List<Schedule> schedules) throws Exception {
                mDatas=schedules;
                scheduleAdapter.setDatas(mDatas);
                scheduleAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this,view);
        initDate();
        //添加分割线
        mSchedlueRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                int width=parent.getWidth();
                float d=20;
                float l1=100;
                float l2=width-100-20;
                final int childCount = parent.getChildCount();
                if(mDatas.size()==0)
                    return;
                Paint paint=new Paint(Color.argb(256,79,55,48));
                c.drawRect(l1,0,l1+d,40,paint);
                c.drawRect(l2,0,l2+d,40,paint);
                for (int i = 0; i < childCount-1; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    final int top = child.getBottom() + params.bottomMargin +
                            Math.round(ViewCompat.getTranslationY(child));
                    final int bottom = top + 40;
                    c.drawRect(l1,top,l1+d,bottom,paint);
                    c.drawRect(l2,top,l2+d,bottom,paint);
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 40;
                outRect.right = 20;
                outRect.left = 20;
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        mSchedlueRecyclerView.setLayoutManager(linearLayoutManager);
        floatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getActionMasked();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        v.setTranslationZ(0);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setTranslationZ(120);
                        break;
                }
                return false;
            }
        });
        return view;
    }

    /**
     * 添加新事件
     */
    @OnClick(R.id.fab_add_event)
    void addEventClick(){

        new ScheduleDialogFragment(mYear,mMonth,mDay,mDayOfWeek,xinqi,0).show(getFragmentManager(), "SchedlueInfoDialog");
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
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
     * 查看事件详情、修改事件
     * @param view
     * @param position
     * @param schedule1
     */
    @Override
    public void onItemClick(View view, int position,Schedule schedule1) {
        Schedule schedule=schedule1;
        new ScheduleDialogFragment(schedule,1).show(getFragmentManager(), "SchedlueInfoDialog");
    }

    @Override
    public void onItemLongClick(View view, int position) {
    }

    @Override
    public void onStatusChecked(View view, int position) {
        sort(mDatas,0,mDatas.size());
        Toast.makeText(this.getContext(),"回调了",Toast.LENGTH_SHORT).show();
    }


    /**
     * 对已有重要性顺序的数组进行时间排序
     * @param list
     * @param low
     * @param high
     */
    public  void sort(List<Schedule> list,int low,int high){
        if(list==null||list.size()==0||low<0||high>=list.size())
            return;
        if(low<high){
            int l=low;
            int h=high;
            Schedule temp=list.get(l);
            while(l<h){
                while(l<h&&list.get(h).getStatus()<=temp.getStatus())
                    h--;
                if(l<h)
                    list.set(l++,list.get(h));
                while(l<h&&list.get(l).getStatus()>=temp.getStatus())
                    l++;
                if(l<h)
                    list.set(h--,list.get(l));

            }
            list.set(l,temp);
            sort(list,low,l-1);
            sort(list,l+1,high);
        }
    }
    @Override
    public void onTypeChecked(View view, int position) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String str);
    }


    private String getDayOfWeek(int dayOfWeek){
        String result;
        switch (dayOfWeek) {
            case 1:
                result= "星期天";
                break;
            case 2:
                result= "星期一";
                break;
            case 3:
                result= "星期二";
                break;
            case 4:
                result= "星期三";
                break;
            case 5:
                result= "星期四";
                break;
            case 6:
                result= "星期五";
                break;
            case 7:
                result= "星期六";
                break;
            default:
                result="";
                break;
        }
        return result;
    }
}
