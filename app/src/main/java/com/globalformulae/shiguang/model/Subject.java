package com.globalformulae.shiguang.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by ZXG on 2017/3/12.
 */
@Entity(
        generateConstructors=true,
        generateGettersSetters = true)
public class Subject {
    @Id
    private Long id;
    @Property
    String name;
    @Property
    String teacher;
    @Property
    String detail;
    @Property
    String day;
    @Property
    String type;
    @Property
    String credit;
    @Property
    String area;
    @Property
    String room;
    @Property
    String start;
    @Property
    String end;
@Generated(hash = 1180378244)
public Subject(Long id, String name, String teacher, String detail, String day,
        String type, String credit, String area, String room, String start,
        String end) {
    this.id = id;
    this.name = name;
    this.teacher = teacher;
    this.detail = detail;
    this.day = day;
    this.type = type;
    this.credit = credit;
    this.area = area;
    this.room = room;
    this.start = start;
    this.end = end;
}
@Generated(hash = 1617906264)
public Subject() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getName() {
    return this.name==null?"":name;
}
public void setName(String name) {
    this.name = name;
}
public String getTeacher() {
    return this.teacher==null?"":teacher;
}
public void setTeacher(String teacher) {
    this.teacher = teacher;
}
public String getDetail() {
    return this.detail==null?"":detail;
}
public void setDetail(String detail) {
    this.detail = detail;
}
public String getDay() {
    return this.day==null?"":day;
}
public void setDay(String day) {
    this.day = day;
}
public String getType() {
    return this.type==null?"":type;
}
public void setType(String type) {
    this.type = type;
}
public String getCredit() {
    return this.credit==null?"":credit;
}
public void setCredit(String credit) {
    this.credit = credit;
}
public String getArea() {
    return this.area==null?"":room;
}
public void setArea(String area) {
    this.area = area;
}
public String getRoom() {
    return this.room==null?"":room;
}
public void setRoom(String room) {
    this.room = room;
}
public String getStart() {
    return this.start==null?"":start;
}
public void setStart(String start) {
    this.start = start;
}
public String getEnd() {
    return this.end==null?"":end;
}
public void setEnd(String end) {
    this.end = end;
}

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", detail='" + detail + '\'' +
                ", day='" + day + '\'' +
                ", type='" + type + '\'' +
                ", credit='" + credit + '\'' +
                ", area='" + area + '\'' +
                ", room='" + room + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
