package cn.com.choicesoft.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	
	/**
	 * 判断手机是否连着网
	 * @param context
	 * @return
	 */
	public static boolean isConnected(final Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isAvailable();
	}
	
	
	/**
	 * 判断是否用着WIFI连着网 
	 * @param context
	 * @return
	 */
	public static boolean checkIsWifiActive(Context context) {
		final Context mContext = context.getApplicationContext();
		final ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if ("WIFI".equals(info[i].getTypeName())
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
