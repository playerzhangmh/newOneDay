package com.april.oneday.bean;

import java.io.Serializable;

/**
 * Created by coins on 2016/4/20.
 */
public class RoutinesDetailInfo implements Serializable {

    /*
    * "create table details (detail_id integer primary key autoincrement,p_id integer promary key," +
                "constraint p_id_FK foreign key(p_id) references process(process_id)," +
                "iconRid integer,icon_name varchar(50),detail_start time,detail_end time,detail_comment varchar(100)," +
                "detail_ringbefore varchar(30),detail_ringpath varchar(30),detail_vabrate integer);";
    * */
    int detail_id;
    int p_id;
    int s_id;
    int iconRid;
    String icon_name;
    String detail_start;
    String detail_end;
    String detail_comment;
    String detail_ringbefore;
    String detail_ringpath;
    int detail_vabrate;

    public RoutinesDetailInfo() {
    }

    public int getDetail_id() {
        return detail_id;
    }

    public RoutinesDetailInfo(int detail_id, int p_id,int s_id, int iconRid, String icon_name, String detail_start, String detail_end, String detail_comment,
                              String detail_ringbefore, String detail_ringpath, int detail_vabrate) {
        this.detail_id = detail_id;
        this.p_id = p_id;
        this.s_id=s_id;
        this.iconRid = iconRid;
        this.icon_name = icon_name;
        this.detail_start = detail_start;
        this.detail_end = detail_end;
        this.detail_comment = detail_comment;
        this.detail_ringbefore = detail_ringbefore;
        this.detail_ringpath = detail_ringpath;
        this.detail_vabrate = detail_vabrate;
    }

    public void setDetail_id(int detail_id) {
        this.detail_id = detail_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getIconRid() {
        return iconRid;
    }

    public void setIconRid(int iconRid) {
        this.iconRid = iconRid;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }

    public String getDetail_start() {
        return detail_start;
    }

    public void setDetail_start(String detail_start) {
        this.detail_start = detail_start;
    }

    public String getDetail_end() {
        return detail_end;
    }

    public void setDetail_end(String detail_end) {
        this.detail_end = detail_end;
    }

    public String getDetail_comment() {
        return detail_comment;
    }

    public void setDetail_comment(String detail_comment) {
        this.detail_comment = detail_comment;
    }

    public String getDetail_ringbefore() {
        return detail_ringbefore;
    }

    public void setDetail_ringbefore(String detail_ringbefore) {
        this.detail_ringbefore = detail_ringbefore;
    }

    public String getDetail_ringpath() {
        return detail_ringpath;
    }

    public void setDetail_ringpath(String detail_ringpath) {
        this.detail_ringpath = detail_ringpath;
    }

    public int getDetail_vabrate() {
        return detail_vabrate;
    }

    public void setDetail_vabrate(int detail_vabrate) {
        this.detail_vabrate = detail_vabrate;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    @Override
    public String toString() {
        return "RoutinesDetailInfo{" +
                "detail_id=" + detail_id +
                ", p_id=" + p_id +
                ", s_id=" + s_id +
                ", iconRid=" + iconRid +
                ", icon_name='" + icon_name + '\'' +
                ", detail_start='" + detail_start + '\'' +
                ", detail_end='" + detail_end + '\'' +
                ", detail_comment='" + detail_comment + '\'' +
                ", detail_ringbefore='" + detail_ringbefore + '\'' +
                ", detail_ringpath='" + detail_ringpath + '\'' +
                ", detail_vabrate=" + detail_vabrate +
                '}';
    }
}
