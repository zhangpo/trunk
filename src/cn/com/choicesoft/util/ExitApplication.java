package cn.com.choicesoft.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**
 * 彻底关闭应用
 * @Author:M.c
 * @CreateDate:2014-2-21
 * @Email:JNWSCZH@163.COM
 */
public class ExitApplication extends Application {
	/*
	 *网上比较流行的方法是定义栈，写一个ExitApplication类，
	 *利用单例模式管理Activity，在每个在Activity的onCreate()
	 *方法中调用ExitApplication.getInstance().addActivity(this)方法,
	 *在退出时调用ExitApplication.getInstance().exit()方法，
	 *就可以完全退出应用程序了 
	 */
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ExitApplication instance;

	private ExitApplication() {
	}

	// 单例模式中获取唯一的ExitApplication实例
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}