package cn.com.choicesoft.util;

import org.ksoap2.serialization.SoapObject;

/**
 * 访问服务器的接口
 *
 */
public interface OnServerResponse {

	void onResponse(String result);
	void onBeforeRequest();

}
