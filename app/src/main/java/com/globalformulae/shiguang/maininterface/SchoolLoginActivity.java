package com.globalformulae.shiguang.maininterface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.utils.HtmlPageAnalyseUtil;
import com.globalformulae.shiguang.utils.MD5Util;
import com.globalformulae.shiguang.utils.OkHttpUtil;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;


/**
 * 教务系统登陆
 */
public class SchoolLoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private static final String LOGINURL= "http://210.42.121.132/servlet/Login";
     /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * 登录异步任务
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    @BindView(R.id.student_id)
    AutoCompleteTextView mStudentIdView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.et_xdvfb)
    EditText mXdvfbView;
    @BindView(R.id.iv_xdvfb)
    ImageView imageView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.tb_school_login)
    Toolbar toolbar;
    @BindView(R.id.email_sign_in_button)
    Button button;
    //cookie用来保证验证码和登陆时一致
    private String cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login);
        ButterKnife.bind(this);
        populateAutoComplete();
        getGenImg();
        initView();
        setActionBar();
        EventBus.getDefault().register(this);
    }
    public void initView(){
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                getGenImg();
                return false;
            }
        });
    }
    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }





    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String message){
        StyleableToast st = new StyleableToast
                .Builder(SchoolLoginActivity.this, message)
                .withMaxAlpha()
                .withBackgroundColor(ContextCompat.getColor(SchoolLoginActivity.this, R.color.colorAccent))
                .withTextColor(Color.WHITE)
                .withBoldText()
                .withIcon(R.drawable.ic_autorenew_black_24dp,true)
                .build();
        st.show();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // 输入框的error
        mStudentIdView.setError(null);
        mPasswordView.setError(null);

        // 登陆需要的数据
        String studentId = mStudentIdView.getText().toString();
        String password = mPasswordView.getText().toString();
        String xdvfb=mXdvfbView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 密码为空检查
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("密码不能为空");
            focusView = mPasswordView;
            cancel = true;
        }

        // 学号为空检查
        if (TextUtils.isEmpty(studentId)) {
           mStudentIdView.setError("学号不能为空");
            focusView = mStudentIdView;
            cancel = true;
        } else if (!isStudentIdValid(studentId)) {
            mStudentIdView.setError("学号格式不正确");
            focusView = mStudentIdView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //TODO 进度取消
            //showProgress(true);
            mAuthTask = new UserLoginTask(studentId, password,xdvfb);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isStudentIdValid(String studentId) {
        if(studentId.matches("[0-9]{13}")){
            return true;
        }else{
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        if(password.matches("[0-9]*"))
            return true;
        else
            return false;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //缓存的学生账号
        List<String> studentIDs = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            studentIDs.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(studentIDs);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SchoolLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mStudentIdView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    class ResultObj{
        boolean reslut;
        int where;
        String htmlCode;
        String week;
        String term;

        public ResultObj(boolean reslut, int where) {
            this.reslut = reslut;
            this.where = where;

        }

        public String getHtmlCode() {
            return htmlCode;
        }

        public void setHtmlCode(String htmlCode) {
            this.htmlCode = htmlCode;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }
    }
    public class UserLoginTask extends AsyncTask<Void, Void, ResultObj> {

        private final String mStudentId;
        private final String mPassword;
        private final String mXdvfb;

        UserLoginTask(String studentId, String password,String xdvfb) {
            mStudentId = studentId;
            mPassword = MD5Util.getInstance().getMD5(password);
            mXdvfb=xdvfb;
        }

        @Override
        protected ResultObj doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            if(cookie!=null){
                try {
                    URL url = new URL(LOGINURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    conn.setRequestProperty("Host","210.42.121.132");
                    conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:51.0) Gecko/20100101 Firefox/51.0");
                    conn.setRequestProperty("Accept"," text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    conn.setRequestProperty("Cookie",cookie);
                    conn.setRequestProperty("Connection","keep-alive");
                    conn.connect();

                    String user = "id=" + URLEncoder.encode(mStudentId, "utf8") + "&pwd=" + URLEncoder.encode(mPassword, "utf8") + "&xdvfb=" + URLEncoder.encode(mXdvfb, "utf8");
                    OutputStream os = conn.getOutputStream();
                    os.write(user.getBytes("utf8"));
                    os.flush();
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = is.read(buffer)) != -1)
                    {
                        outStream.write(buffer,0,len);
                    }
                    is.close();
                    byte[] data =outStream.toByteArray();

                    String htmlCode = new String(data, "gbk");

                    //输出结果，校验是否操作成功
                    while (htmlCode != null) {
                        if (htmlCode.contains("修改密码")) {
                            //获取csrftoken
                            HtmlPageAnalyseUtil analyse = new HtmlPageAnalyseUtil("&csrftoken=(.*)','",htmlCode);
                            String csrf = analyse.getGroup(0);

                            HtmlPageAnalyseUtil analyse1 = new HtmlPageAnalyseUtil("<span\\s*id=\"showOrHide\">([^a-z]*)</span>"/*当前周*/,htmlCode);
                            String week = analyse1.getGroup(0);

                            HtmlPageAnalyseUtil analyse2 = new HtmlPageAnalyseUtil("\"term\">\\s* ([^a-z]*)\\s* <span",htmlCode);
                            String term = analyse2.getGroup(0);

                            //获取课表页面
                            htmlCode = getPage(csrf);
                            ResultObj resultObj=new ResultObj(true,-1);
                            resultObj.htmlCode=htmlCode;
                            resultObj.week=week;
                            resultObj.term=term;
                            return resultObj;
                        } else if (htmlCode.contains("用户名/密码错误")){
                            return new ResultObj(false,0);
                        } else if (htmlCode.contains("验证码错误")) {
                            return new ResultObj(false,1);
                        }
                    }
                    conn.disconnect();
                    //注意记得关闭流，不然连接不能结束会抛出异常
                    os.close();
                    is.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return new ResultObj(false,-1);
        }

        @Override
        protected void onPostExecute(final ResultObj resultObj) {
            mAuthTask = null;
            showProgress(false);
            if (resultObj.reslut) {
                StyleableToast st = new StyleableToast
                        .Builder(SchoolLoginActivity.this, "登陆成功")
                        .withMaxAlpha()
                        .withBackgroundColor(ContextCompat.getColor(SchoolLoginActivity.this, R.color.colorAccent))
                        .withTextColor(Color.WHITE)
                        .withBoldText()
                        .withIcon(R.drawable.ic_autorenew_black_24dp,true)
                        .build();
                st.show();
                Intent intent = new Intent(SchoolLoginActivity.this,SchoolActivity.class);
                intent.putExtra("status",0);
                intent.putExtra("Html", resultObj.htmlCode);
                intent.putExtra("Week", resultObj.week);
                intent.putExtra("Term", resultObj.term);
                startActivity(intent);
                finish();
            } else {
                switch (resultObj.where){
                    case 0:
                        EventBus.getDefault().post("用户名/密码错误");
                        mPasswordView.setText("");
                        mPasswordView.requestFocus();
                        break;
                    case 1:
                        EventBus.getDefault().post("验证码错误");
                        getGenImg();
                        mXdvfbView.setText("");
                        mXdvfbView.requestFocus();
                        break;
                }

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(ResultObj aBoolean) {
            super.onCancelled(aBoolean);
        }
    }


    private void setActionBar() {
        toolbar.setTitle("教务系统登录");
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

    public void getGenImg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response= OkHttpUtil.getInstance().getGenImg();
                if(response.code()==200){
                    cookie=response.header("Set-Cookie");
                    cookie=cookie.substring(0,cookie.length()-8);
                    InputStream is=response.body().byteStream();
                    Bitmap bitmap= BitmapFactory.decodeStream(is);
                    EventBus.getDefault().post(bitmap);
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){

        }.start();

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    //获取课表页面
    private String getPage(String csrf) {

        String ahtml = "";

        try {
            String page = "http://210.42.121.132/servlet/Svlt_QueryStuLsn?action=queryStuLsn&csrftoken=" + csrf;
            URL url = new URL(page);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Host", "210.42.121.132");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:51.0) Gecko/20100101 Firefox/51.0");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            conn.setRequestProperty("Accept-Encoding", "deflate");
            conn.setRequestProperty("Referer", "http://210.42.121.132/stu/stu_course_parent.jsp");
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.connect();

            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "gbk"));
            String str = null;

            while ((str = reader.readLine()) != null) {
                ahtml += str;
            }
            conn.disconnect();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ahtml;
    }


}

