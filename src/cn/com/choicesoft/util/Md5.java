package cn.com.choicesoft.util;

import java.security.MessageDigest;

public class Md5 {

	public static String byteArrayToHexString(byte[] bytes) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < bytes.length; n++) {
			stmp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	public static String Digest(String plain) {
		try {
			String b;
			MessageDigest md = MessageDigest.getInstance("md5");
			b = byteArrayToHexString(md.digest(plain.getBytes()));
			return b;
		} catch (Exception e) {
			return null;
		}
	}
}
