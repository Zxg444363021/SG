package com.globalformulae.shiguang.maininterface;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.bean.ResponseBean;
import com.globalformulae.shiguang.bean.User;
import com.globalformulae.shiguang.utils.MD5Util;
import com.globalformulae.shiguang.utils.OkHttpUtil;
import com.globalformulae.shiguang.utils.TimeCountUtil;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends AppCompatActivity{
    private static final String TAG ="RegistActivity" ;
    @BindView(R.id.regist_phone)
    EditText phoneET;
    @BindView(R.id.regist_password)
    EditText passwordET;
    @BindView(R.id.regist_password_again)
    EditText passwordAgainET;
    @BindView(R.id.regist_btn)
    Button registBTN;
    @BindView(R.id.regist_identify_code)
    EditText identifyCodeET;
    @BindView(R.id.regist_name)
    EditText nameET;
    @BindView(R.id.regist_get_identify_code_btn)
    Button identifyCodeBTN;
    private UserRegistTask userRegistTask=null;

    private String phoneFirst;

    public class UserRegistTask extends AsyncTask<Void, Void, Boolean> {
        private String phone;
        private String identifyCode;
        private String password;
        private String name;

        public UserRegistTask(String phone, String password, String identifyCode,String name) {
            this.phone = phone;
            this.password = password;
            this.identifyCode=identifyCode;
            this.name=name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpUtil httpUtil=OkHttpUtil.getInstance();
            httpUtil.registShiguang(phone, MD5Util.getMD5String(password),identifyCode,name);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onMessage(ResponseBean responseBean){
        Log.e(TAG, "onMessage: ");
        StyleableToast st;
        switch (responseBean.getCode()){
            //获取验证码失败
            case ResponseBean.GET_IDENTIFY_CODE_FAIL:
                phoneET.setError("验证码发送失败，请检查手机号和网络后重新发送");
                break;
            //获取验证码成功
            case ResponseBean.GET_IDENTIFY_CODE_SUCC:
                st= new StyleableToast
                        .Builder(RegistActivity.this, "验证码发送成功，请注意查收")
                        .withMaxAlpha()
                        .withBackgroundColor(getResources().getColor(R.color.colorAccent))
                        .withTextColor(Color.WHITE)
                        .withBoldText()
                        .build();
                st.show();
                st=null;
                break;
            case ResponseBean.REGIST_SUCC:    //注册成功
                SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                User user=responseBean.getUser();
                editor.putLong("userid",user.getUserid());
                editor.putString("phone",user.getPhone());
                editor.putString("name",user.getName());
                editor.commit();
                sharedPreferences=null;
                editor=null;
                st= new StyleableToast
                        .Builder(RegistActivity.this, "注册成功")
                        .withMaxAlpha()
                        .withBackgroundColor(getResources().getColor(R.color.colorAccent))
                        .withTextColor(Color.WHITE)
                        .withBoldText()
                        .build();
                st.show();
                st=null;
                this.finish();
                break;
            case ResponseBean.REGIST_FAIL:
                if(responseBean.getMessage().equals("201")){
                    st=new StyleableToast
                        .Builder(RegistActivity.this, "验证码错误")
                            .withMaxAlpha()
                            .withBackgroundColor(getResources().getColor(R.color.colorAccent))
                            .withTextColor(Color.WHITE)
                            .withBoldText()
                            .build();
                    st.show();
                    st=null;
                }else if(responseBean.getMessage().equals("202")){
                    st=new StyleableToast
                            .Builder(RegistActivity.this, "手机号码错误")
                            .withMaxAlpha()
                            .withBackgroundColor(getResources().getColor(R.color.colorAccent))
                            .withTextColor(Color.WHITE)
                            .withBoldText()
                            .build();
                    st.show();
                    st=null;
                }else {
                    st=new StyleableToast
                            .Builder(RegistActivity.this, "服务器错误，请稍后重试")
                            .withMaxAlpha()
                            .withBackgroundColor(getResources().getColor(R.color.colorAccent))
                            .withTextColor(Color.WHITE)
                            .withBoldText()
                            .build();
                    st.show();
                    st=null;
                }
                break;
            default:
                break;
        }
    }


    @OnClick(R.id.regist_btn)
    void regsit(){
        String phone=phoneET.getText().toString().trim();
        String password=passwordET.getText().toString().trim();
        String passwordAgain=passwordAgainET.getText().toString().trim();
        String identifyCode=identifyCodeET.getText().toString().trim();
        String name=nameET.getText().toString().trim();
        phoneET.setError(null);
        identifyCodeET.setError(null);
        passwordET.setError(null);
        passwordAgainET.setError(null);
        if(TextUtils.isEmpty(phone)){
            phoneET.setError("请输入手机号码");
        }else if(!phone.equals(phoneFirst)){
            phoneET.setError("手机号码有误");
        }
        else if(TextUtils.isEmpty(password)){
            passwordET.setError("请输入密码");
        }else if(TextUtils.isEmpty(identifyCode)){
            identifyCodeET.setError("请输入验证码");
        }else if(TextUtils.isEmpty(passwordAgain)){
            passwordAgainET.setError("请再输一次密码");
        }else if(!passwordAgain.equals(password)){
            passwordAgainET.setError("两次输入的密码不一致");
        }else if(TextUtils.isEmpty(name)){
            nameET.setError("请输入用户名");
        }
        else{
            userRegistTask=new UserRegistTask(phone,password,identifyCode,name);
            userRegistTask.execute((Void) null);
        }


    }

    @OnClick(R.id.regist_get_identify_code_btn)
    void getIdentifyCode(){
        phoneFirst=phoneET.getText().toString().trim();
        phoneET.setError(null);
        if(TextUtils.isEmpty(phoneFirst)){
            phoneET.setError("请输入手机号码");
        }else if(!isMobileNO(phoneFirst)){
            phoneET.setError("手机号码格式不正确");
        }
        else{
            OkHttpUtil httpUtil=OkHttpUtil.getInstance();
            httpUtil.getIdentifyCode(phoneFirst);
            //  把按钮变成不可点击，并且显示倒计时（正在获取）
            TimeCountUtil timeCountUtil = new TimeCountUtil(RegistActivity.this, 60000, 1000,identifyCodeBTN);
            timeCountUtil.start();
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



    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.regist_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(888);
                finish();
            }
        });
    }
}
