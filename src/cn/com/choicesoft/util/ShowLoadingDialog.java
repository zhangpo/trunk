package cn.com.choicesoft.util;

import android.content.Context;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

public class ShowLoadingDialog {
	
	private static LoadingDialog mLoadingDialog = null;
	
	public static void showTypeLoadDia(Context pContext,String title){
		if(mLoadingDialog == null){
			mLoadingDialog = new LoadingDialogStyle(pContext, title);
			mLoadingDialog.setCancelable(false);
		}
	}

}
