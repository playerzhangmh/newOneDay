package com.april.oneday;

import android.test.AndroidTestCase;

import com.april.oneday.bean.MediaInfo;
import com.april.oneday.dao.MediaDao;
import com.april.oneday.db.MediaOpenHelper;

import java.util.List;


/**
 * Created by wangtongyu on 2016/4/19.
 */
public class MediaDataBaseTest extends AndroidTestCase {
    //测试创建数据库
    public void testCreateDataBase(){
        MediaOpenHelper mediaOpenHelper = new MediaOpenHelper(getContext());
    }
    //测试添加数据
    public void testAddMedia() {

        MediaDao mediaDao = new MediaDao(getContext());
        int type = -1;


        //添加100条测试数据
        for (int i = 0; i < 100; i++) {

            type = (int) ((Math.random() * 100 + i) % 4);

            if (type == 0) {
                MediaInfo mediaInfo0 = new MediaInfo(0, "纯文本测试数据"+i, "2016.04.20", "", "", "", "", "");
                mediaDao.addMediaInfo(mediaInfo0);
            } else if (type == 1) {
                MediaInfo mediaInfo1 = new MediaInfo(1, "一张图片测试数据"+i, "2016.04.20", "pic_test", "", "", "", "");
                mediaDao.addMediaInfo(mediaInfo1);
            } else if (type == 2) {
                MediaInfo mediaInfo2 = new MediaInfo(2, "两张图片测试数据"+i, "2016.04.20", "pic_test", "pic_test", "", "", "");
                mediaDao.addMediaInfo(mediaInfo2);
            } else if (type == 3) {
                MediaInfo mediaInfo3 = new MediaInfo(3, "三张图片测试数据"+i, "2016.04.20", "pic_test", "pic_test", "pic_test", "", "");
                mediaDao.addMediaInfo(mediaInfo3);
            }

            type = -1;
        }
    }

    /**
     * 测试查询部分数据
     */
    public void testQueryPartMediaInfo(){
        MediaDao mediaDao = new MediaDao(getContext());
        List<MediaInfo> list = mediaDao.queryPartMediaInfo(20, 0);
        System.out.println(list.toString());
    }

    /**
     *测试删除数据
     */
    public void testDeleteMediaInfo(){
        MediaDao mediaDao = new MediaDao(getContext());
        MediaInfo mediaInfo1 = new MediaInfo(1,"今天是个好日子","2016.05.20","pic_test","","","","");
        mediaDao.deleteMediaInfo(mediaInfo1);
    }
}
