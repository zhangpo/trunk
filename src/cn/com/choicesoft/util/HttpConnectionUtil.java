package cn.com.choicesoft.util;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.com.choicesoft.util.SimpleThread.OnThreadRun;

/**
 * 对接美味不用等
 * @author 
 *
 */
public class HttpConnectionUtil {
	private static final String TAG = "HttpConnectionUtil";// 标记
	private static final String CHARSET = HTTP.UTF_8;// 定义编码

	public enum HttpMethod {// 请求方法的一个枚举
		GET, POST
	}


	/**
	 * 带参数的连接方法
	 * @param url 网址
	 * @param params  POST或GET要传递的参数
	 * @param method 方法,POST或GET
	 * @param callback 回调方法
	 *            
	 */
	private String jsonStr = null;//
	public  void connect(final String url, final Map<String, String> params,final HttpMethod method, final OnServerResponse serverResponse) {
		if(serverResponse != null){
			serverResponse.onBeforeRequest();
		}
		SimpleThread simpleThread = new SimpleThread();
		simpleThread.setOnThreadRun(new OnThreadRun() {
			
			@Override
			public void onRun(Handler h) {
				
				try {
					HttpClient client      = new DefaultHttpClient();  
					HttpUriRequest request = getRequest(url, params, method);// 设置请求
					HttpResponse response  = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						Log.d(TAG, "-- request success --");

						// 获取返回内容也可以如下:
						HttpEntity resEntity = response.getEntity();
						jsonStr = (resEntity == null) ?null : EntityUtils.toString(resEntity,CHARSET);
					} else {
						Log.d(TAG, "-- request failed --");
					}
				}  catch (IOException e) {
					Log.d(TAG, e.toString());
				} finally {			
					Message msg = h.obtainMessage();
					h.sendMessage(msg);
				}
			}
			
			@Override
			public void onHandleMessage(Message msg) {
				serverResponse.onResponse(jsonStr);
			}
		});
		simpleThread.run();

	}
	

	
	/**
	 * POST跟GET传递参数不同,POST是隐式传递,GET是显式传递
	 * @param url 网址
	 * @param params  参数
	 * @param method  方法
	 * @return
	 */
	private HttpUriRequest getRequest(String url, Map<String, String> params,HttpMethod method) {
		if (method.equals(HttpMethod.POST)) {
			List<NameValuePair> listParams = new ArrayList<NameValuePair>();
			if (params != null) {
				Set<Entry<String, String>> set = params.entrySet();
				for(Entry<String, String> entrySet:set){
					listParams.add(new BasicNameValuePair(entrySet.getKey(), entrySet.getValue()));
				}

				for(Entry<String, String> entrySet:set){
					Log.i("工具类", entrySet.getKey()+"-----"+entrySet.getValue());
				}

			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listParams, CHARSET);
				HttpPost request = new HttpPost(url);
				request.setEntity(entity);
				return request;
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		} else {
			StringBuilder sb=new StringBuilder();
			sb.append(url);
			//拼写url参数

			if(params!=null && !params.isEmpty()){
				sb.append("?");
				for(Entry<String, String> entry: params.entrySet()){
					sb.append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue()))
			        .append('&');
				}
				sb.deleteCharAt(sb.length()-1);
			}
			url = sb.toString();
			Log.d("get请求方式的网址", url);
			HttpGet request = new HttpGet(url);
			return request;
		}
	}

}