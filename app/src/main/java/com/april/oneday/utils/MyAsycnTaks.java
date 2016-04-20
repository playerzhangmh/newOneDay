package com.april.oneday.utils;

import android.os.Handler;

public abstract class MyAsycnTaks {
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			postTask();
		};
	};
	/**
	 * 在子线程之前执行的方法
	 */
	public abstract void preTask();
	/**
	 * 在子线程之中执行的方法
	 */
	public abstract void doinBack();
	/**
	 * 在子线程之后执行的方法
	 */
	public abstract void postTask();
	/**
	 * 执行
	 */
	public void execute(){
		preTask();
		new Thread(){
			public void run() {
				doinBack();
				handler.sendEmptyMessage(0);
			};
		}.start();
	}
}
