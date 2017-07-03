package com.globalformulae.shiguang.maininterface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.model.ResponseBean;
import com.globalformulae.shiguang.model.User;
import com.globalformulae.shiguang.utils.MD5Util;
import com.globalformulae.shiguang.utils.OkHttpUtil;
import com.globalformulae.shiguang.utils.SPUtil;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * 已知的用户名，可以用来自动填充
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };


    @BindView(R.id.userId)
    AutoCompleteTextView userIdTV;
    @BindView(R.id.password)
    EditText passwordET;
    @BindView(R.id.regist_btn)
    Button registBTN;
    @BindView(R.id.login_btn)
    Button loginBTN;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.wxLogin)
    ImageView wxLoginIV;
    @BindView(R.id.wbLogin)
    ImageView wbLoginIV;
    @BindView(R.id.qqLogin)
    ImageView qqLoginIV;
    @BindView(R.id.logo_iv)
    ImageView logoIV;


    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // Set up the login form.
        populateAutoComplete();
        animation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);


        //在密码域输入完成之后如果点击键盘上的enter键的操作
        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        EventBus.getDefault().register(this);
        setActionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //登录按钮点击事件
    @OnClick(R.id.login_btn)
    void login() {
        attemptLogin();
    }

    @OnClick(R.id.wxLogin)
    void wxLogin() {

    }

    @OnClick(R.id.regist_btn)
    void regist() {
        Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forget_btn)
    void forget() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(final ResponseBean responseBean) {

        switch (responseBean.getCode()) {
            case ResponseBean.LOGIN_SUCC:   //登录成功
                Log.e("LogingA", "what");
                User user = responseBean.getUser();
                SharedPreferences.Editor editor = SPUtil.getSPD(LoginActivity.this, "user");
                editor.putBoolean("logged", true);
                editor.putLong("userid", user.getUserid());
                editor.putString("name", user.getName());
                editor.putString("phone", user.getPhone());
                editor.putString("icon", user.getIcon());
                editor.putInt("tomato_n", user.getTomatoN());
                editor.putInt("power_n", user.getPower());
                editor.putBoolean("isOnline",true);
                editor.apply();

                logoIV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, UserInfoActivity.class);
                        startActivity(intent);
                        finish();
                        showProgress(false);
                    }
                }, 3000);
                break;
            case ResponseBean.LOGIN_FAIL:

                logoIV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        if (responseBean.getMessage().equals("201")) {//密码错误
                            passwordET.setError("密码错误");
                            passwordET.requestFocus();
                        } else if (responseBean.getMessage().equals("202")) {//帐号不存在
                            userIdTV.setError("帐号不存在");
                            userIdTV.requestFocus();
                        } else {//服务器错误
                            StyleableToast st3 = new StyleableToast
                                    .Builder(LoginActivity.this, "服务器错误，请重试")
                                    .withMaxAlpha()
                                    .withBackgroundColor(getResources().getColor(R.color.colorAccent))
                                    .withTextColor(Color.WHITE)
                                    .withBoldText()
                                    .build();
                            st3.show();
                        }
                    }
                }, 3000);
                break;
        }
    }

    /**
     * 自动补齐
     */
    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * 尝试登录
     */
    private void attemptLogin() {
        userIdTV.setError(null);
        passwordET.setError(null);

        // 存储此时尝试登陆的帐号和密码
        String mPhone = userIdTV.getText().toString();
        String mPassword = passwordET.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 判断输入密码是否为空，是否有效
        if (!TextUtils.isEmpty(mPassword) && !isPasswordValid(mPassword)) {
            passwordET.setError(getString(R.string.error_invalid_password));
            focusView = passwordET;
            cancel = true;
        }

        // 检查输入帐号是否有误
        if (TextUtils.isEmpty(mPhone)) {
            userIdTV.setError(getString(R.string.error_field_required));
            focusView = userIdTV;
            cancel = true;
        } else if (!isMobileNO(mPhone)) {
            userIdTV.setError(getString(R.string.error_invalid_email));
            focusView = userIdTV;
            cancel = true;
        }

        if (cancel) {
            // 如果有错误，就把焦点给它
            focusView.requestFocus();
        } else {
            // 如果没啥问题就显示进度，然后登陆
            //showProgress(true);
            OkHttpUtil httpUtil = OkHttpUtil.getInstance();
            httpUtil.loginShiguang(mPhone, MD5Util.getMD5String(mPassword));
            showProgress(true);
            StyleableToast st2 = new StyleableToast
                    .Builder(LoginActivity.this, "登录中")
                    .withMaxAlpha()
                    .withBackgroundColor(getResources().getColor(R.color.colorAccent))
                    .withTextColor(Color.WHITE)
                    .withBoldText()
                    .withIcon(R.drawable.ic_autorenew_black_24dp, true)
                    .build();
            st2.show();
        }
    }

    /**
     * 验证手机格式
     */
    private boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * 检查密码格式
     *
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    /**
     * 显示进度，然后隐藏输入框
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
        //logoIV.setAnimation(animation);
        if (show) {
            logoIV.startAnimation(animation);
        } else {
            logoIV.clearAnimation();
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
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        userIdTV.setAdapter(adapter);
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
     * 设置toolbar
     */
    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_tb);
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

