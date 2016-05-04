package com.april.oneday.bean;

import java.io.Serializable;

/**
 * Created by coins on 2016/4/20.
 */
public class RoutinesInfo implements Serializable {
    int schedule_id;
    String schedule_title;
    int schedule_cycles;
    int currentDay;
    boolean active;

    public RoutinesInfo() {
    }

    public RoutinesInfo(int schedule_id, String schedule_title, int schedule_cycles, int currentDay, boolean active) {
        this.schedule_id = schedule_id;
        this.schedule_title = schedule_title;
        this.schedule_cycles = schedule_cycles;
        this.currentDay = currentDay;
        this.active = active;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getSchedule_title() {
        return schedule_title;
    }

    public void setSchedule_title(String schedule_title) {
        this.schedule_title = schedule_title;
    }

    public int getSchedule_cycles() {
        return schedule_cycles;
    }

    public void setSchedule_cycles(int schedule_cycles) {
        this.schedule_cycles = schedule_cycles;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RoutinesInfo{" +
                "schedule_id=" + schedule_id +
                ", schedule_title='" + schedule_title + '\'' +
                ", schedule_cycles=" + schedule_cycles +
                ", currentDay='" + currentDay + '\'' +
                ", active=" + active +
                '}';
    }
}
