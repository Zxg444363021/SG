package com.globalformulae.shiguang.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.globalformulae.shiguang.R;

import java.util.ArrayList;

/**
 * Created by ZXG on 2017/6/28.
 */

public class TimePickerDialogF extends DialogFragment {
    public interface OnDialogCancelListener{
        void onCancel();
    }
    public interface OnCallDialog{
        Dialog getDialog(Context context);
    }
    /**     * 监听弹出窗是否被取消     */
    private OnDialogCancelListener mCancelListener;
    /**     * 回调获得需要显示的dialog     */
    private OnCallDialog mOnCallDialog;

    public static TimePickerDialogF newInstance(OnCallDialog call, boolean cancelable) {
        return newInstance(call, cancelable, null);
    }
    public static TimePickerDialogF newInstance(OnCallDialog call, boolean cancelable, OnDialogCancelListener cancelListener) {
        TimePickerDialogF instance = new TimePickerDialogF();
        instance.setCancelable(cancelable);
        instance.mCancelListener = cancelListener;
        instance.mOnCallDialog = call;
        return instance;
    }

    private TextView titleTV;
    private EasyPickerView hourPicker;
    private EasyPickerView minutePicker;
    private Button leftBtn;
    private Button rightBtn;

    private ArrayList<String> hourList;
    private ArrayList<String> minuteList;

    private Integer chosenHour; //最终选择的小时
    private Integer chosenMinute;   //最终选择的分钟

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_time_picker,container);
        titleTV= (TextView) view.findViewById(R.id.tipTitle);
        hourPicker= (EasyPickerView) view.findViewById(R.id.epv_h);
        minutePicker= (EasyPickerView) view.findViewById(R.id.epv_m);
        leftBtn= (Button) view.findViewById(R.id.leftBtn);
        rightBtn= (Button) view.findViewById(R.id.rightBtn);
        initParmter();
        hourPicker.setDataList(hourList);
        minutePicker.setDataList(minuteList);
        hourPicker.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {
                chosenHour=Integer.parseInt(hourList.get(curIndex));
            }
        });
        minutePicker.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {
                chosenMinute=Integer.parseInt(minuteList.get(curIndex));
            }
        });
        return view;
    }

    /**
     * 为滚轮添加参数
     */
    public void initParmter(){
        hourList=new ArrayList<>();
        minuteList=new ArrayList<>();
        for(int i=0;i<=5;i++){
            hourList.add(String.valueOf(i));
        }
        for(int i=0;i<=60;i++){
            minuteList.add(String.valueOf(i));
        }
    }
}
