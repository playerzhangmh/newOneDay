package com.april.oneday.utils;

import android.os.Environment;

/**
 * Created by wangtongyu on 2016/4/19.
 */
public class CommenUtils {

    public static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static String getPathByName(String fileName){
        String path = "";
        //fileType img表示图片 audio表示音频 video表示视频
        String fileType = fileName.substring(0, 3);
        String dir = PATH_SD+"/"+"oneday"+"/"+fileType+"/"+fileName+".jpg";

        return dir;
    }

}
