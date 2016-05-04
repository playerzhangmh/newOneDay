package com.april.oneday.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的工具类封装
 */
public class PreUtils {

	public static final String PRE_NAME = "config";

	public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {

		//判断之前有没有显示过新手引导
		SharedPreferences sp =
				ctx.getSharedPreferences(PRE_NAME, ctx.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences sp =
				ctx.getSharedPreferences(PRE_NAME, ctx.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
}
