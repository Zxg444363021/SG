package com.globalformulae.shiguang.model;

/**
 * Created by ZXG on 2017/4/29.
 */

public class User {
    private Long userid;
    private String name;
    private String icon;
    private String phone;
    private int tomato_n;
    private int power_n;

    public User(String name, String icon, int tomato_n, int power_n) {
        this.name = name;
        this.icon = icon;
        this.tomato_n = tomato_n;
        this.power_n = power_n;
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

    public int getTomato_n() {
        return tomato_n;
    }

    public void setTomato_n(int tomato_n) {
        this.tomato_n = tomato_n;
    }

    public int getPower_n() {
        return power_n;
    }

    public void setPower_n(int power_n) {
        this.power_n = power_n;
    }
}
