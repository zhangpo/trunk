package cn.com.choicesoft.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * �ж��Ƿ���PAD
 */
public class IsPad {

    /**
     * �жϵ�ǰ�豸���ֻ�����ƽ��
     * @param context
     * @return ƽ�巵�� True���ֻ����� False
     */
    public static boolean isPad(Context context) {
        return false; //TODO ƽ������
//        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
	
}
