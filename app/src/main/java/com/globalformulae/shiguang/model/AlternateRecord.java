package com.globalformulae.shiguang.model;

import java.sql.Timestamp;

/**
 * Created by ZXG on 2017/5/28.
 */

public class AlternateRecord {
    private Long id;
    private Long user1id;
    private Long user2id;
    private int type;
    private int power;
    private String user1name;
    private String user2name;
    private Timestamp time;

    public AlternateRecord(int type, int power, String user1name, Timestamp date) {
        this.type = type;
        this.power = power;
        this.user1name = user1name;
        this.time = date;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getUser1name() {
        return user1name;
    }

    public void setUser1name(String user1name) {
        this.user1name = user1name;
    }

    public String getUser2name() {
        return user2name;
    }

    public void setUser2name(String user2name) {
        this.user2name = user2name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser1id() {
        return user1id;
    }

    public void setUser1id(Long user1id) {
        this.user1id = user1id;
    }

    public Long getUser2id() {
        return user2id;
    }

    public void setUser2id(Long user2id) {
        this.user2id = user2id;
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
}
