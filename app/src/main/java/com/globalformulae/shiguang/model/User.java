package com.globalformulae.shiguang.model;

/**
 * Created by ZXG on 2017/4/29.
 */

public class User {
    private Long userid;
    private String name;
    private String icon;
    private String phone;
    private int tomatoN;
    private int power;

    public User(String name, String icon, int tomato_n, int power_n) {
        this.name = name;
        this.icon = icon;
        this.tomatoN = tomato_n;
        this.power = power_n;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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

    public int getTomatoN() {
        return tomatoN;
    }

    public void setTomatoN(int tomatoN) {
        this.tomatoN = tomatoN;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power_n) {
        this.power = power_n;
    }
}
