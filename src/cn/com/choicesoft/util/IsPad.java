package cn.com.choicesoft.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * 判断是否是PAD
 */
public class IsPad {

    /**
     * 判断当前设备是手机还是平板
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return false; //TODO 平板设置
//        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
	
}
