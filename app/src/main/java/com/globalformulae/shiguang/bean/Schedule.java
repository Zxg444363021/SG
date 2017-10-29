package com.globalformulae.shiguang.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by ZXG on 2017/4/10.
 */
@Entity(
        generateGettersSetters = true
)
public class Schedule {
    public static final int
            EVENT_WAITIMP=1,
            EVENT_WAIT=0,
            EVENT_FINISHED=-1;
    @Id(autoincrement = true)
    private Long id;

    @Property
    private String name;
    @Property
    private String description;
    @Property
    private int year;
    @Property
    private int month;
    @Property
    private int day;
    @Property
    private int hour;
    @Property
    private int minute;

    @Property
    private int dayOfWeek;
    @Property
    private int status=EVENT_WAIT;
    @Property
    private int type;

    

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }





    @Transient
    private boolean ifNew=false;    //这个属性不映射到数据库
    

    @Generated(hash = 729319394)
    public Schedule() {
    }

    @Generated(hash = 1587951530)
    public Schedule(Long id, String name, String description, int year, int month,
            int day, int hour, int minute, int dayOfWeek, int status, int type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.dayOfWeek = dayOfWeek;
        this.status = status;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static int getEventWaitimp() {
        return EVENT_WAITIMP;
    }

    public static int getEventWait() {
        return EVENT_WAIT;
    }

    public static int getEventFinished() {
        return EVENT_FINISHED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isIfNew() {
        return ifNew;
    }

    public void setIfNew(boolean ifNew) {
        this.ifNew = ifNew;
    }

    public String getData(){
        String xinqi;
        switch (dayOfWeek){
            case 1:
                xinqi= "星期天";
                break;
            case 2:
                xinqi= "星期一";
                break;
            case 3:
                xinqi= "星期二";
                break;
            case 4:
                xinqi= "星期三";
                break;
            case 5:
                xinqi= "星期四";
                break;
            case 6:
                xinqi= "星期五";
                break;
            case 7:
                xinqi= "星期六";
                break;
            default:
                xinqi= "";
                break;
        }
        return year+" 年 "+month+" 月 "+day+" 日 "+xinqi;
    }
    public String getTime(){
        if(minute<10){
            return hour+":0"+minute;
        }else{
            return hour+":"+minute;
        }
    }

    public boolean getIfNew() {
        return this.ifNew;
    }
}
