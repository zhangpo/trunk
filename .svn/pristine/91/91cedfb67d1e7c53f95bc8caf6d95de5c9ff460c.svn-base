package cn.com.choicesoft.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * 语言更换工具类
 * Created by M.c on 14-5-14.
 */
public class LanguageSetting {
    /**
     * 语言切换
     * @param activity
     */
    public void setting(Activity activity) {
        Resources resources = activity.getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        switch (SharedPreferencesUtils.getLanguage(activity)) {
            case 0:
                config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文
                break;
            case 1:
                config.locale = Locale.TRADITIONAL_CHINESE;//繁体中文
                break;
            case 2:
                config.locale = Locale.ENGLISH;            //英文
                break;
            default:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                break;
        }
        resources.updateConfiguration(config, dm);
    }
}
