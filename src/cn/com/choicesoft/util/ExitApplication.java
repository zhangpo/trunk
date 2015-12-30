package cn.com.choicesoft.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**
 * ���׹ر�Ӧ��
 * @Author:M.c
 * @CreateDate:2014-2-21
 * @Email:JNWSCZH@163.COM
 */
public class ExitApplication extends Application {
	/*
	 *���ϱȽ����еķ����Ƕ���ջ��дһ��ExitApplication�࣬
	 *���õ���ģʽ����Activity����ÿ����Activity��onCreate()
	 *�����е���ExitApplication.getInstance().addActivity(this)����,
	 *���˳�ʱ����ExitApplication.getInstance().exit()������
	 *�Ϳ�����ȫ�˳�Ӧ�ó����� 
	 */
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ExitApplication instance;

	private ExitApplication() {
	}

	// ����ģʽ�л�ȡΨһ��ExitApplicationʵ��
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}