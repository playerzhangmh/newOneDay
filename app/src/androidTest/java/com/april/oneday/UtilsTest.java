package com.april.oneday;

import android.test.AndroidTestCase;

import com.april.oneday.utils.CommenUtils;

/**
 * Created by wangtongyu on 2016/4/19.
 */
public class UtilsTest extends AndroidTestCase {

    public void testGetFilePathByFileName(){

        String img_test = CommenUtils.getPathByName("img_test");
        System.out.println("路径:"+img_test);
    }
}
