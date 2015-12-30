package cn.com.choicesoft.util;


import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 修改AlertDialog 标题
 * @Author:M.c
 * @CreateDate:2014-4-22
 * @Email:JNWSCZH@163.COM
 */
public class AlertDialogTitleUtil {
    /**
     * 设置对话框是否消失
     * @author Young
     * @created 2014-11-6
     * @comment
     * @param dialog
     * @param isDismissed
     */
    public static void setDialogIfDismissed(DialogInterface dialog, boolean isDismissed) {
        try {
            java.lang.reflect.Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, isDismissed);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
	/**
	 * 更改标题显示位置
	 * @param dialog
	 * @param gravity 居中、靠左、右
	 */
	public static void gravity(AlertDialog dialog,int gravity){
		try {
			Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
			mAlert.setAccessible(true);
			Object alertController = mAlert.get(dialog);
			Field mTitleView = alertController.getClass().getDeclaredField("mTitleView");  
			mTitleView.setAccessible(true);  
			TextView title = (TextView) mTitleView.get(alertController);  
			if (title!=null) {
				title.setGravity(gravity);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (NoSuchFieldException e) {
			e.printStackTrace();
		}    
	}

	/**
	 * 更改标题颜色
	 * @param dialog
	 * @param color
	 */
	public static void textColor(AlertDialog dialog,int color){
		try {
			Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
			mAlert.setAccessible(true);
			Object alertController = mAlert.get(dialog);
			Field mTitleView = alertController.getClass().getDeclaredField("mTitleView");  
			mTitleView.setAccessible(true);  
			TextView title = (TextView) mTitleView.get(alertController);  
			title.setTextColor(color);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (NoSuchFieldException e) {
			e.printStackTrace();
		}    
	}
}
