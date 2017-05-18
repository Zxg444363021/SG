package com.globalformulae.shiguang.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.Button;

import com.globalformulae.shiguang.R;

public class TimeCountUtil extends CountDownTimer {
    private Activity mActivity;
    private Button btn;//按钮

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
    }


    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText(millisUntilFinished / 1000 + "秒后重试");//设置倒计时时间
        btn.setTextColor(mActivity.getResources().getColor(R.color.whiteTrans));

        //设置按钮为灰色，这时是不能点击的
        btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.sendmes_pressed_button_bg));
//        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
//        span.setSpan(new ForegroundColorSpan(Color.GREEN), 6, 8,
//                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
//        btn.setText(span);

    }


    @Override
    public void onFinish() {
        btn.setText("重新获取");
        btn.setClickable(true);//重新获得点击
        btn.setTextColor(mActivity.getResources().getColorStateList(R.color.selector_login_btn_textcolor));
        btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.sendmes_button_bg));//还原背景色
    }


}





