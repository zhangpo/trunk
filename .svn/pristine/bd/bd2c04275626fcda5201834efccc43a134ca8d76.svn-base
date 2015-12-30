package cn.com.choicesoft.chinese.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.util.OnServerResponse;
import cn.com.choicesoft.util.SimpleThread;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.com.choicesoft.util.SimpleThread.OnThreadRun;

/**
 * 服务器交互类
 * 
 */
public class ChineseServer {
    String result;
	
	public void connect( final Context context, final String methodName,final List<Map<String, String>> params, final OnServerResponse onServerResponse){
		if(onServerResponse != null){
			onServerResponse.onBeforeRequest();
		}
		SimpleThread simpleThread = new SimpleThread();
		simpleThread.setOnThreadRun(new OnThreadRun() {
			
			@Override
			public void onRun(Handler h) {
				SoapObject soapObject = new SoapObject(ChineseConstants.NAMESPACE,methodName);
				if(null != params){
					for(Map<String, String> map:params){
						Set<Entry<String, String>> set = map.entrySet();
						for(Map.Entry<String, String> entry:set){
							soapObject.addProperty(entry.getKey(), entry.getValue());
						}
					}
				}
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.bodyOut = soapObject;
				envelope.dotNet = true;//设置是否调用的是dotNet开发的WebService
				envelope.setOutputSoapObject(soapObject);
				HttpTransportSE transport = new HttpTransportSE(getUrl(context, ChineseConstants.HOST+methodName));
                transport.debug=true;
				StringBuilder builder = new StringBuilder();
				builder.append(ChineseConstants.NAMESPACE).append("/").append(methodName);
				try {
                    transport.call(builder.toString(), envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    result = (object == null||object.getProperty(0)==null) ?null:object.getProperty(0).toString();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}finally{
					Message msg = new Message();
					msg.what = 0;
					h.sendMessage(msg);
				}
				
			}
			
			@Override
			public void onHandleMessage(Message msg) {
				if(onServerResponse != null){
					onServerResponse.onResponse(result);
				}
			}
		});
		simpleThread.run();
		
	}



	private static String getUrl(Context context, String webServiceAdd) {
		String URL = ChineseConstants.getURL(context);
		if (!URL.contains("http://")) {
			URL = "http://" + URL;
		}
		if (!"/".equals(URL.lastIndexOf(URL.length() - 1))) {
			URL = URL + "/";
		}
		System.out.print(URL+webServiceAdd);
		return URL + webServiceAdd;
	}

	

}
