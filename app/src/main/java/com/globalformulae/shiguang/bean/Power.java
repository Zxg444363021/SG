package com.globalformulae.shiguang.bean;

import java.sql.Date;

/**
 * Created by ZXG on 2017/9/8.
 */

public class Power {
    private Long id;
    private Long userid;
    private int power;
    private int powerStolen;
    private Date date;
    private int canSteal;
    private String uuid;
    private Integer powerType;
    private Integer hasGain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPowerStolen() {
        return powerStolen;
    }

    public void setPowerStolen(int powerStolen) {
        this.powerStolen = powerStolen;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCanSteal() {
        return canSteal;
    }

    public void setCanSteal(int canSteal) {
        this.canSteal = canSteal;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getPowerType() {
        return powerType;
    }

    public void setPowerType(Integer powerType) {
        this.powerType = powerType;
    }

    public Integer getHasGain() {
        return hasGain;
    }

    public void setHasGain(Integer hasGain) {
        this.hasGain = hasGain;
    }

    @Override
    public String toString() {
        return ""+getUserid()+" "+getPower()+" "+getPowerType();
    }
}
