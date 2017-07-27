package com.globalformulae.shiguang.bean;

/**
 * Created by ZXG on 2017/4/20.
 */

public class MyDate {
    int Year;
    int Month;
    int Day;
    int DayOfWeek;
    String Xinqi;

    public MyDate(int year, int month, int day, int dayOfWeek) {
        Year = year;
        Month = month;
        Day = day;
        DayOfWeek = dayOfWeek;
        Xinqi=getDayOfWeek(dayOfWeek);
    }

    public int getYear() {
        return Year;
    }

    public int getMonth() {
        return Month;
    }

    public int getDay() {
        return Day;
    }

    public int getDayOfWeek() {
        return DayOfWeek;
    }

    public String getXinqi() {
        return Xinqi;
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
