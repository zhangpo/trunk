package cn.com.choicesoft.util;

import android.os.Handler;
import android.os.Message;

/**
 * 线程工具类，线程调用
 * 
 */
public class SimpleThread {

	private Handler handler;

	private OnThreadRun onThreadRun;
    /**
	 * 设置回调函数
	 * 
	 * @param onThreadRun
	 */
	public SimpleThread setOnThreadRun(OnThreadRun onThreadRun) {
		this.onThreadRun = onThreadRun;
		return this;
	}

	/**
	 * 线程
	 */
	public void run() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (onThreadRun != null)
					onThreadRun.onHandleMessage(msg);
			}
		};
		new Thread() {
			@Override
			public void run() {
				if (onThreadRun != null)
					onThreadRun.onRun(handler);
			}

		}.start();

	}

	/**
	 * 回调接口，有使用线程者实�?
	 * 
	 * 
	 */
	public interface OnThreadRun {
		/**
		 * 执行线程
		 * 
		 * @param h
		 */
		void onRun(Handler h);

		/**
		 * 线程执行完线程后执行的方�?
		 * 
		 * @param msg
		 */
		void onHandleMessage(Message msg);

	}
}
