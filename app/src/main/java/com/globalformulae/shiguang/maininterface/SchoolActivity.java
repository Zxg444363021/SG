package com.globalformulae.shiguang.maininterface;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.greendao.DaoMaster;
import com.globalformulae.shiguang.greendao.DaoSession;
import com.globalformulae.shiguang.greendao.SubjectDao;
import com.globalformulae.shiguang.maininterface.adapter.FragmentAdapter;
import com.globalformulae.shiguang.model.Subject;
import com.globalformulae.shiguang.utils.HtmlPageAnalyseUtil;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.List;

public class SchoolActivity extends AppCompatActivity implements SubjectFragment.OnFragmentInteractionListener,ScoreFragment.OnFragmentInteractionListener{

    private TabLayout tabs;
    private List<Fragment> mFragments=new ArrayList<>();
    private List<String> mTitles=new ArrayList<String>(){};
    private ViewPager mViewPager;
    private SubjectFragment mSubjectFragment;
    private ScoreFragment mScoreFragment;
    private FragmentAdapter mSchoolFragmentAdapter;
    private List<Subject> mSubjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);

        //获取传输来的网页源代码和其他信息
        Intent intent = getIntent();
        if(intent.getIntExtra("status",-1)==0){
            final String html = intent.getStringExtra("Html");
            String week = intent.getStringExtra("Week");
            String term = intent.getStringExtra("Term");
            mSubjects=getSubjectList(html);
            saveData(mSubjects);
        }else{
            getData();
        }
        initView();
    }


    public void initView(){
        tabs= (TabLayout) findViewById(R.id.tab_school);
        mViewPager= (ViewPager) findViewById(R.id.vp_school);
        tabs.addTab(tabs.newTab().setText("课程"));
        tabs.addTab(tabs.newTab().setText("成绩"));
        mTitles.add("课程");
        mTitles.add("成绩");
        mSubjectFragment=new SubjectFragment();
        Bundle bundle=new Bundle();
        //bundle.putSerializable("",mSubjects);
        mScoreFragment=new ScoreFragment();
        mFragments.add(mSubjectFragment);
        mFragments.add(mScoreFragment);
        mSchoolFragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),mFragments,mTitles);
        mViewPager.setAdapter(mSchoolFragmentAdapter);
        tabs.setupWithViewPager(mViewPager);
        setActionBar();
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_school);
        toolbar.setTitle("教务信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    private String[] what = {"lessonName\\s*=\\s*\"(\\S*)\";//课程名", "teacherName\\s*=\\s*\"(\\S*)\";//任课老师",
            "detail\\s*=\\s*\"(\\S*)\";//课程的详细信息", "day\\s*=\\s*\"(\\d*)\";//"/*周几*/, "planType\\s*=\\s*\"(\\S*)\";"/*种类*/,
            "credit\\s*=\\s*\"(\\S*)\";//课程学分", "var\\sareaName\\s=\\s\"(\\S*)\";"/*大地点*/, "var\\sclassRoom\\s=\\s\"(\\S*)\";//教室",
            "var\\s*beginTime\\s*=\\s*\"(\\S*)\";//上课时间"/*从第几节课开始*/, "var\\s*endTime\\s*=\\s*\"(\\S*)\";//上课时间"/*到第几节课结束*/};
    //对课表页面源代码进行分析，并初始化mDataset
    private List<Subject> getSubjectList(String html){
        List<Subject> subjects=new ArrayList<>();
        List<String> list;

        for(int i=0;i<10;i++){
            list=new HtmlPageAnalyseUtil(what[i],html).getGroup();
            if (i == 0) {
                //先添加空元素
                for(int k=0;k<list.size();k++){
                    Subject subject=new Subject();
                    subjects.add(subject);
                }
            }
            //再后续设置属性
            for(int j=0;j<list.size();j++){
                switch (i){
                    case 0:
                        subjects.get(j).setName(list.get(j));
                        break;
                    case 1:
                        subjects.get(j).setTeacher(list.get(j));
                        break;
                    case 2:
                        subjects.get(j).setDetail(list.get(j));
                        break;
                    case 3:
                        subjects.get(j).setDay(list.get(j));
                        break;
                    case 4:
                        subjects.get(j).setType(list.get(j));
                        break;
                    case 5:
                        subjects.get(j).setCredit(list.get(j));
                        break;
                    case 6:
                        subjects.get(j).setArea(list.get(j));
                        break;
                    case 7:
                        subjects.get(j).setRoom(list.get(j));
                        break;
                    case 8:
                        subjects.get(j).setStart(list.get(j));
                        break;
                    case 9:
                        subjects.get(j).setEnd(list.get(j));
                        break;
                }
            }
        }
        return subjects;
    }

    private void saveData(List<Subject> subjects) {
        StyleableToast st = new StyleableToast
                .Builder(SchoolActivity.this, "正在获取信息，请稍后")
                .withMaxAlpha()
                .withBackgroundColor(ContextCompat.getColor(SchoolActivity.this, R.color.colorAccent))
                .withTextColor(Color.WHITE)
                .withBoldText()
                .withIcon(R.drawable.ic_autorenew_black_24dp,true)
                .withDuration(2000)
                .build();
        st.show();
        //第一步获取数据库helper
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "myData.db", null);
        //通过helper可以得到一个数据库，传递给master
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb()); //
        DaoSession daoSession = daoMaster.newSession();   //创建会话
        SubjectDao dao=daoSession.getSubjectDao();    //所对应的实体操作类
        dao.deleteAll();
        for(Subject s:subjects){
            dao.insert(s);
        }

    }
    private void getData(){

    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
