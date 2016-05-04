package com.april.oneday.bean;

import java.io.Serializable;

/**
 * Created by coins on 2016/4/20.
 */
public class RoutinesProcessInfo implements Serializable{
    /*
    * "create table process (process_id integer,s_id integer primary key," +
                "constraint s_id_FK foreign key(s_id) references schedule(schedule_id)," +
                "process_active integer,process_start datetime,process_end datetime,process_time varchar(30));";
    * */
    int process_id;
    int s_id;
    boolean process_active;
    String process_start;
    String process_end;
    String process_time;

    public RoutinesProcessInfo() {
    }

    public RoutinesProcessInfo(int process_id, int s_id, boolean process_active, String process_start, String process_end, String process_time) {
        this.process_id = process_id;
        this.s_id = s_id;
        this.process_active = process_active;
        this.process_start = process_start;
        this.process_end = process_end;
        this.process_time = process_time;
    }

    public int getProcess_id() {
        return process_id;
    }

    public void setProcess_id(int process_id) {
        this.process_id = process_id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public boolean getProcess_active() {
        return process_active;
    }

    public void setProcess_active(boolean process_active) {
        this.process_active = process_active;
    }

    public String getProcess_start() {
        return process_start;
    }

    public void setProcess_start(String process_start) {
        this.process_start = process_start;
    }

    public String getProcess_end() {
        return process_end;
    }

    public void setProcess_end(String process_end) {
        this.process_end = process_end;
    }

    public String getProcess_time() {
        return process_time;
    }

    public void setProcess_time(String process_time) {
        this.process_time = process_time;
    }

    @Override
    public String  toString() {
        return "RoutinesProcessInfo{" +
                "process_id=" + process_id +
                ", s_id=" + s_id +
                ", process_active=" + process_active +
                ", process_start='" + process_start + '\'' +
                ", process_end='" + process_end + '\'' +
                ", process_time='" + process_time + '\'' +
                '}';
    }
}
