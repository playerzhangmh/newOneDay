package com.april.oneday.bean;

/**
 * Created by coins on 2016/4/18.
 */
public class ReminderInfo {
    /*
    * 表格项有id ,reminder名称，日期，时间，重复模式，内容，铃声路径，震动模式（长度），震动次数，标记图标Rid，图标背景颜色（颜色，eg：red，white）
    * */
    int reminder_id;
    String reminder_name;
    String reminder_date;
    String reminder_time;
    int repeatType;//提醒周期，天 月  年   1,2,3,没有重复则为0
    String reminder_comment;
    String ring_path;
    int vabrate_type; //长震动，短震动 0,1
    int vabrate_times;//震动次数  2,4,6,8,10，自定义
    int iconRid;
    String iconBgColor;
    int active;//是否过期，0不过期 1过期，初值均为0


    public ReminderInfo() {
    }

    public ReminderInfo(int reminder_id, String reminder_name, String reminder_date, String reminder_time, int repeatType, String reminder_comment,
                        String ring_path, int vabrate_type, int vabrate_times, int iconRid, String iconBgColor, int active) {
        this.reminder_id=reminder_id;
        this.reminder_name = reminder_name;
        this.reminder_date = reminder_date;
        this.reminder_time = reminder_time;
        this.repeatType = repeatType;
        this.reminder_comment = reminder_comment;
        this.ring_path = ring_path;
        this.vabrate_type = vabrate_type;
        this.vabrate_times = vabrate_times;
        this.iconRid = iconRid;
        this.iconBgColor = iconBgColor;
        this.active = active;
    }

    public String getReminder_name() {
        return reminder_name;
    }

    public void setReminder_name(String reminder_name) {
        this.reminder_name = reminder_name;
    }

    public String getReminder_date() {
        return reminder_date;
    }

    public void setReminder_date(String reminder_date) {
        this.reminder_date = reminder_date;
    }

    public String getReminder_time() {
        return reminder_time;
    }

    public void setReminder_time(String reminder_time) {
        this.reminder_time = reminder_time;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    public String getReminder_comment() {
        return reminder_comment;
    }

    public void setReminder_comment(String reminder_comment) {
        this.reminder_comment = reminder_comment;
    }

    public String getRing_path() {
        return ring_path;
    }

    public void setRing_path(String ring_path) {
        this.ring_path = ring_path;
    }

    public int getVabrate_type() {
        return vabrate_type;
    }

    public void setVabrate_type(int vabrate_type) {
        this.vabrate_type = vabrate_type;
    }

    public int getVabrate_times() {
        return vabrate_times;
    }

    public void setVabrate_times(int vabrate_times) {
        this.vabrate_times = vabrate_times;
    }

    public int getIconRid() {
        return iconRid;
    }

    public void setIconRid(int iconRid) {
        this.iconRid = iconRid;
    }

    public String getIconBgColor() {
        return iconBgColor;
    }

    public void setIconBgColor(String iconBgColor) {
        this.iconBgColor = iconBgColor;
    }

    public int isActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getReminder_id() {
        return reminder_id;
    }

    public void setReminder_id(int reminder_id) {
        this.reminder_id = reminder_id;
    }

    @Override
    public String toString() {
        return "ReminderInfo{" +
                "reminder_id=" + reminder_id +
                ", reminder_name='" + reminder_name + '\'' +
                ", reminder_date='" + reminder_date + '\'' +
                ", reminder_time='" + reminder_time + '\'' +
                ", repeatType=" + repeatType +
                ", reminder_comment='" + reminder_comment + '\'' +
                ", ring_path='" + ring_path + '\'' +
                ", vabrate_type=" + vabrate_type +
                ", vabrate_times=" + vabrate_times +
                ", iconRid=" + iconRid +
                ", iconBgColor='" + iconBgColor + '\'' +
                ", active=" + active +
                '}';
    }
}
