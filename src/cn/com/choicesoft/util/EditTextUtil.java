package cn.com.choicesoft.util;

import java.lang.reflect.Method;

import android.widget.EditText;

/**
 * EditText π§æﬂ¿‡
 * @author M.c
 */
public class EditTextUtil {
	/**
	 * “˛≤ÿ»Ìº¸≈Ã
	 * @param et
	 */
	public static void hideKeyboard(EditText et){
		try {  
            Class<EditText> cls = EditText.class;  
            Method setSoftInputShownOnFocus;  
            setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus",boolean.class);  
            setSoftInputShownOnFocus.setAccessible(true);  
            setSoftInputShownOnFocus.invoke(et, false);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
	}
}
