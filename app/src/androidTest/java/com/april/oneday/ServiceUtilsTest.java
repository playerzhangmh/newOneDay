package com.april.oneday;

import android.test.AndroidTestCase;
import android.util.Log;

import com.april.oneday.utils.ServiceUtils;

/**
 * Created by coins on 2016/5/4.
 */
public class ServiceUtilsTest extends AndroidTestCase {

    private static final String TAG = "ServiceUtilsTest";

    public void testServiceIsRunning(){
        boolean proessRunning = ServiceUtils.isProessRunning(getContext(), "com.april.oneday");
        Log.v(TAG,proessRunning+"proessRunning");
    }
}
