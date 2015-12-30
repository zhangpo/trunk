package cn.com.choicesoft.activity;



import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import cn.com.choicesoft.R;
import cn.com.choicesoft.chinese.activity.ChineseWelcomeActivity;
import cn.com.choicesoft.impl.DishDataManager;
import cn.com.choicesoft.util.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * �������
 */
public class BaseActivity extends FragmentActivity {
	private Date screenOnDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		new LanguageSetting().setting(this);
	}

	/**
	 * �ڴ���Ӧ�ó�ʱ��δʹ���жϴ���
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if(getScreenOnDate()!=null){
			Date newDate=new Date();
			double csmTime=newDate.getTime()-screenOnDate.getTime();
			double csMinute=csmTime/(1000*60);
			if (SharedPreferencesUtils.getChineseSnack(BaseActivity.this) == 0) {//����ǿ��
				logout();
//				int lt = SharedPreferencesUtils.getLogoutTime(this);
//				int ct = SharedPreferencesUtils.getCloseTime(this);
//				if (csMinute >= ct && ct != 0) {//�ж��Զ��ر�
//					hintOut(this.getString(R.string.close_hint), 0);
//				} else if (csMinute >= lt && lt != 0) {//�ж��Զ�ע��
//					hintOut(this.getString(R.string.out_hint), 1);
//				}
			}
		}
	}

	/**
	 * �ǳ���ʾ 0 �˳� 1 ע��
	 * @param text
	 * @param typ
	 */
	public void hintOut(String text, final int typ){
		new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.hint)
				.setMessage(text).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (typ == 0) {
					ExitApplication.getInstance().exit();
				} else {
					logout();
				}
			}
		}).show();
	}

	/**
	 * ע��
	 */
	public void logout(){
		if(SharedPreferencesUtils.getChineseSnack(this)==0) {
			CList<Map<String, String>> data = new CList<Map<String, String>>();
			data.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
			data.add("userCode", SharedPreferencesUtils.getUserCode(this));
			new Server().connect(this, "loginOut", "ChoiceWebService/services/HHTSocket?/loginOut", data, new OnServerResponse() {
				@Override
				public void onResponse(String result) {
					if (ValueUtil.isNotEmpty(result)) {
						String[] res = result.split("@");
						if (ValueUtil.isNotEmpty(res) && res[0].equals("0")) {
							Intent intent = new Intent(BaseActivity.this, WelcomeActivity.class);
							BaseActivity.this.startActivity(intent);
							BaseActivity.this.finish();
						}
					}
				}
				@Override
				public void onBeforeRequest() {
				}
			});
		}else{
			startActivity(new Intent(this, ChineseWelcomeActivity.class));
			finish();
		}
	}
	@Override
	protected void onStop() {
		screenOnDate=null;
		if(!isAppOnForeground()||!isScreenOn()){
			screenOnDate=new Date();
		}
		super.onStop();
	}

	/**
	 * ��ȡDishDataManager
	 * @param context
	 * @return
	 */
	public DishDataManager getDataManager(Context context){
		return ((ChoiceApplication)this.getApplication()).getDishDataManager(context);
	}

	/**
	 * �ж�App�Ƿ�����
	 * @return
	 */
	public boolean isScreenOn(){
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}
	/**
	 * �����Ƿ���ǰ̨����
	 * @return ��̨���� false
	 */
	public boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	public Date getScreenOnDate() {
		return screenOnDate;
	}
}
