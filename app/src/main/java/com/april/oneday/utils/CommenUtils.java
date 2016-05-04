package com.april.oneday.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wangtongyu on 2016/4/19.
 */
public class CommenUtils {

    public static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static String getPathByName(String fileName) {
        String path = "";
        //fileType img表示图片 audio表示音频 video表示视频
        String fileType = fileName.substring(0, 3);
        String dir = PATH_SD + "/" + "oneday" + "/"+fileType+"/"  + fileName;

        return dir;
    }

    /**
     * 将头像文件 保存到data/data/com/april/oneday/files
     */
    public static void saveHeadIcon(final Bitmap bitmap, final Context context) {


        File f = new File(context.getFilesDir(), "headIcon.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            System.out.println("头像已经复制完毕");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    /**
//     * 将头像文件 从data/data/com/april/oneday/files读取出来
//     * 返回bitmap
//     */
//    public static Bitmap decodeHeadIcon(final Context context) {
//
//        Bitmap bitmap;
//        File f = new File(context.getFilesDir(), "headIcon.jpg");
//        if (!f.exists()) {
//            return null;
//        }
//
//        BitmapUtils bitmapUtils = new BitmapUtils(context);
//
//
//        return bitmap;
//
//    }

}
