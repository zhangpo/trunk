package cn.com.choicesoft.util;

import android.util.Log;

/**
 * ��ӡ
 *
 */

public class LogUtil {

	static boolean b = true;
	public static void d(String tag, String msg){
		if(b){
			Log.d(tag, msg);
		}
	}
}
