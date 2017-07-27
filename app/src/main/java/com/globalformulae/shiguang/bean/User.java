package com.globalformulae.shiguang.bean;

/**
 * Created by ZXG on 2017/4/29.
 */

public class User {

    private long userid;
    private String phone;
    private String password;
    private String name;
    private String icon;
    private String wxid;
    private String wxname;
    private int power;
    private int tomatoN;
    private int power1Today;
    private int power2Today;
    private int power1Yesterday;
    private int power2Yesterday;
    private int power1Stolen;
    private int power2Stolen;
    private int power1CanSteal;
    private int power2CanSteal;

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getTomatoN() {
        return tomatoN;
    }

    public void setTomatoN(int tomatoN) {
        this.tomatoN = tomatoN;
    }

    public int getPower1Today() {
        return power1Today;
    }

    public void setPower1Today(int power1Today) {
        this.power1Today = power1Today;
    }

    public int getPower2Today() {
        return power2Today;
    }

    public void setPower2Today(int power2Today) {
        this.power2Today = power2Today;
    }

    public int getPower1Yesterday() {
        return power1Yesterday;
    }

    public void setPower1Yesterday(int power1Yesterday) {
        this.power1Yesterday = power1Yesterday;
    }

    public int getPower2Yesterday() {
        return power2Yesterday;
    }

    public void setPower2Yesterday(int power2Yesterday) {
        this.power2Yesterday = power2Yesterday;
    }

    public int getPower1Stolen() {
        return power1Stolen;
    }

    public void setPower1Stolen(int power1Stolen) {
        this.power1Stolen = power1Stolen;
    }

    public int getPower2Stolen() {
        return power2Stolen;
    }

    public void setPower2Stolen(int power2Stolen) {
        this.power2Stolen = power2Stolen;
    }

    public int getPower1CanSteal() {
        return power1CanSteal;
    }

    public void setPower1CanSteal(int power1CanSteal) {
        this.power1CanSteal = power1CanSteal;
    }

    public int getPower2CanSteal() {
        return power2CanSteal;
    }

    public void setPower2CanSteal(int power2CanSteal) {
        this.power2CanSteal = power2CanSteal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }
}
