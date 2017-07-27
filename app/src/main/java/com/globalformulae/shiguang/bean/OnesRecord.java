package com.globalformulae.shiguang.bean;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/7/27.
 */

public class OnesRecord {

    private long user1id;
    private long user2id;
    private String name;
    private int type;
    private int power;
    private Timestamp time;

    public OnesRecord(){}
    public OnesRecord(int type,Timestamp date){
        this.type = type;
        this.time = date;
    }

    public long getUser1id() {
        return user1id;
    }

    public void setUser1id(long user1id) {
        this.user1id = user1id;
    }

    public long getUser2id() {
        return user2id;
    }

    public void setUser2id(long user2id) {
        this.user2id = user2id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
