package cn.com.choicesoft.util;

import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceUuidFactory {

	public static String getPhysicalId(Context context){
		
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		 
		final String tmDevice, tmSerial,androidId;
		 
		tmDevice = "" + tm.getDeviceId();
		 
		tmSerial = "" + tm.getSimSerialNumber();
		 
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		 
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		 
		String uniqueId = deviceUuid.toString();
		return uniqueId;
	}
}
