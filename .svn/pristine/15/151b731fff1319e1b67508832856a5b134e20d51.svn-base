package cn.com.choicesoft.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputMethodUtils {
	
	/**
	 * 如果输入法在窗口上已经显示，则隐藏，反之则显示
	 * @param pContext
	 */
	public static void toggleSoftKeyboard(Context pContext){
		
		InputMethodManager imm = (InputMethodManager)pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	
	/**
	 * 强制弹出键盘  view为接受软键盘输入的视图，SHOW_FORCED表示强制显示
	 * @param pContext
	 * @param pEditText
	 */
	public static void showSoftKeyboard(Context pContext, EditText pEditText) {

		InputMethodManager inputManager = (InputMethodManager)pContext.getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.showSoftInput(pEditText, 0); 

	}
	
	
	
	/**
	 * 隐藏软键盘
	 * @param pContext
	 * @param pEditText
	 */
	public static void hideSoftKeyboard(Context pContext, EditText pEditText) {

		InputMethodManager imm = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(pEditText.getWindowToken(), 0); // 强制隐藏键盘

	}
}
