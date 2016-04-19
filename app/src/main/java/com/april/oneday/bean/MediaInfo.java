package com.april.oneday.bean;

/**
 * Created by wangtongyu on 2016/4/19.
 */
public class MediaInfo {
    private int type;
    private String desc="";
    private String date="";
    private String pic1="";
    private String pic2="";
    private String pic3="";
    private String audio="";
    private String video="";


    /**
     * 构造方法
     */
    public MediaInfo(int type, String desc, String date, String pic1, String pic2, String pic3, String audio, String video) {
        this.type = type;
        this.desc = desc;
        this.date = date;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.audio = audio;
        this.video = video;
    }


    /**
     * Getter and Setter
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }


    @Override
    public String toString() {
        return "MediaInfo{" +
                "type=" + type +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                ", pic1='" + pic1 + '\'' +
                ", pic2='" + pic2 + '\'' +
                ", pic3='" + pic3 + '\'' +
                ", audio='" + audio + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}
