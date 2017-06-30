package com.globalformulae.shiguang.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.globalformulae.shiguang.R;


/**
 * Created by Noline on 2016/6/27.
 */
public class CustomDialog extends Dialog {
    private Button leftBtn,rightBtn;
    private TextView tipTitle,tipContent;
    private View fengexian;
    private Context context;

    public CustomDialog(Context context) {
        super(context, R.style.theme_dialog);
        this.context = context;
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_custom,null,false);
        leftBtn = (Button) v.findViewById(R.id.leftBtn);
        rightBtn = (Button) v.findViewById(R.id.rightBtn);
        tipTitle = (TextView) v.findViewById(R.id.tipTitle);
        tipContent = (TextView) v.findViewById(R.id.tipContent);
        fengexian = (View) v.findViewById(R.id.fengexian);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });
        this.setContentView(v);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog_background);
    }

    public void setTipTitle(String tiptitle){
        tipTitle.setText(tiptitle);
    }
    public void hideTipTitle(){
        tipTitle.setVisibility(View.GONE);
    }
    public void hideFengexian(){
        fengexian.setVisibility(View.GONE);
    }
    public void setTipContent(String tipcontent){
        tipContent.setText(tipcontent);
    }
    public void setLeftBtn(String leftbtn){
        leftBtn.setText(leftbtn);
    }
    public void setRightBtn(String rightbtn){
        rightBtn.setText(rightbtn);
    }
    public void setLeftListener(View.OnClickListener leftListener){
        leftBtn.setOnClickListener(leftListener);
    }
    public void setRightListener(View.OnClickListener rightListener){
        rightBtn.setOnClickListener(rightListener);
    }

    public CustomDialog setCustomStyle(String tiptitle, String tipcontent, String leftbtn, String rightbtn, View.OnClickListener rightListener){
        this.setTipTitle(tiptitle);
        this.setTipContent(tipcontent);
        this.setLeftBtn(leftbtn);
        this.setRightBtn(rightbtn);
        this.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });
        this.setRightListener(rightListener);
        return this;
    }


    //作为提醒的弹出框样式
    public CustomDialog setCustomInformStyle(String tiptitle, String tipcontent, View.OnClickListener rightListener){
        this.setTipTitle(tiptitle);
        this.setTipContent(tipcontent);
        rightBtn.setText("确定");
        this.setRightListener(rightListener);
        leftBtn.setText("取消");
        this.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });
        return this;
    }
    //作为视频处wifi检测提醒的弹出框样式
    public CustomDialog setWifiInformStyle(String tiptitle, String tipcontent, View.OnClickListener leftListener){
        this.setTipTitle(tiptitle);
        this.setTipContent(tipcontent);
        rightBtn.setText("继续");
        this.setLeftListener(leftListener);
        leftBtn.setText("取消");
        this.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });
        return this;
    }

    //没有title的弹出框样式，左边为点击事件，右边为取消按钮
    public CustomDialog setNotitleStyle(String tipcontent, String leftbtn, String rightbtn, View.OnClickListener leftListener){
        this.hideTipTitle();
        this.hideFengexian();
        this.setTipContent(tipcontent);
        this.setLeftBtn(leftbtn);
        this.setRightBtn(rightbtn);
        this.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });
        this.setLeftListener(leftListener);
        return this;
    }

    /**
     * 将弹出框样式调整考试提醒样式
     */
    public void setTestHintStyle(int iWrong, int iRight, View.OnClickListener mListener) {
        tipTitle.setText("确认交卷");
        if(iWrong >= 10 || iRight < 90){
            tipContent.setText("您已答错" + iWrong + "题,考试得分" + iRight + "分，成绩不合格，是否继续答题？");
        }else{
            tipContent.setText("您考试得分为" + iRight + "分,答错" + iWrong + "题,是否确认交卷？");
        }

        leftBtn.setText("继续答题");
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });
        rightBtn.setOnClickListener(mListener);
        rightBtn.setText("交卷");
    }

    /**
     * 将弹出框样式调整为退出考试样式
     */
    public void setTestExitStyle(int iWrong, int iRight, View.OnClickListener lListener, View.OnClickListener rListener) {
        tipTitle.setText("考试提醒");
        if(iWrong >= 10 || iRight < 90){
            tipContent.setText("您已答错" + iWrong + "题,考试得分" + iRight + "分，成绩不合格，是否继续答题？");
        }else{
            tipContent.setText("您考试得分为" + iRight + "分,答错" + iWrong + "题,是否确认交卷？");
        }

        leftBtn.setText("交卷");
        leftBtn.setOnClickListener(lListener);
        rightBtn.setText("确认退出");
        rightBtn.setOnClickListener(rListener);
    }



    /**
     * 将弹出框设为计时须知样式
     */
    public void setTimerNeedStyle(View.OnClickListener mListener) {
        tipTitle.setText("计时必读");
        SpannableStringBuilder builder = new SpannableStringBuilder("本课时单位课时60分钟，为了保证学员有效学习，在开始计时和结束计时时分别要采集签到和签退照片。人脸比对通过后该时段有效，人脸比对未通过，该时段不计入有效学时。\n" +
                "\n" +
                "当学员点击“开始计时”，悬浮球切换到计时界面；再次点击即退出本次计时学习。\n" +
                "\n" +
                "当用户退出后，悬浮球时间清零，学时的历史记录可在“结业认证”界面查看。");
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        ForegroundColorSpan red2Span = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, 87, 91, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(red2Span, 145, 149, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipContent.setText(builder);
        rightBtn.setText("我知道了");
//        rightBtn.setTextColor(Color.rgb(52, 199, 179));
        rightBtn.setTextColor(Color.WHITE);
        rightBtn.setOnClickListener(mListener);
        leftBtn.setVisibility(View.GONE);
        //rightBtn.setBackgroundResource(R.drawable.btn_jump_to_test_result);
    }



    /**
     * 将弹出框设为停止计时样式
     */
    public void setStopTimerStyle(View.OnClickListener leftListener, View.OnClickListener rightListener){
        this.hideTipTitle();
        tipContent.setText("你确定要停止计时么？");
        leftBtn.setText("取消");
        rightBtn.setText("确认");
        leftBtn.setOnClickListener(leftListener);
        rightBtn.setOnClickListener(rightListener);
    }

    public void setSignEndAndStopTimerStyle(View.OnClickListener leftListener, View.OnClickListener rightListener){
        this.hideTipTitle();
        tipContent.setText("你已学习4小时，点击确认签退");
        leftBtn.setText("取消");
        rightBtn.setText("确认");
        leftBtn.setOnClickListener(leftListener);
        rightBtn.setOnClickListener(rightListener);
    }
}
