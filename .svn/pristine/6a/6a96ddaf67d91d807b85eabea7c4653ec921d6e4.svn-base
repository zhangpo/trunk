package cn.com.choicesoft.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.util.SimpleThread.OnThreadRun;

/**
 * 服务器交互类
 * 
 */
public class Server {
	String result = "";
	
	public void connect( final Context context, final String methodName, final String wsdl,
			 final List<Map<String, String>> params, final OnServerResponse onServerResponse){
		if(onServerResponse != null){
			onServerResponse.onBeforeRequest();
		}
		SimpleThread simpleThread = new SimpleThread();
		simpleThread.setOnThreadRun(new OnThreadRun() {
			
			@Override
			public void onRun(Handler h) {
				SoapObject soapObject = new SoapObject(Constants.NAMESPACE,methodName);
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
				HttpTransportSE transport = new HttpTransportSE(getUrl(Constants.getURL(context), wsdl));
				StringBuilder builder = new StringBuilder();
				builder.append(Constants.NAMESPACE).append("/").append(methodName);
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
    /**
     * 自定义地址调用
     * @param context
     * @param methodName
     * @param wsdl
     * @param params
     * @param nameSpace 最后需要带有符号：“/”
     * @param onServerResponse
     */
    public void appUpdate( final Context context, final String methodName,final String wsdl,
                           final String nameSpace,final List<Map<String, String>> params,final OnServerResponse onServerResponse){
        if(onServerResponse != null){
            onServerResponse.onBeforeRequest();
        }
        SimpleThread simpleThread = new SimpleThread();
        simpleThread.setOnThreadRun(new OnThreadRun() {
            @Override
            public void onRun(Handler h) {
                SoapObject soapObject = new SoapObject(nameSpace,methodName);
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
                HttpTransportSE transport = new HttpTransportSE(wsdl);
                StringBuilder builder = new StringBuilder();
                builder.append(nameSpace).append(methodName);
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

	/**
	 * HTTP调用
	 * @param paramList
	 * @param onServerResponse
	 */
	public void httpPost(final Context context,final List<NameValuePair> paramList, final String methodName,final OnServerResponse onServerResponse) {

		if(onServerResponse != null){
			onServerResponse.onBeforeRequest();
		}
		new SimpleThread().setOnThreadRun(new OnThreadRun() {
											  @Override
											  public void onRun(Handler h) {
												  HttpClient httpClient = new DefaultHttpClient();
												  HttpPost httpPost = new HttpPost(getUrl(Constants.getHTTP_URL(context),methodName));
												  try {
													  //设置参数，仿html表单提交
													  httpPost.setEntity(new UrlEncodedFormEntity(paramList, "utf-8"));
													  //发送HttpPost请求，并返回HttpResponse对象
													  HttpResponse httpResponse = httpClient.execute(httpPost);
													  // 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
													  if (httpResponse.getStatusLine().getStatusCode() == 200) {
														  //获取返回结果
														  result = EntityUtils.toString(httpResponse.getEntity());
													  }
												  } catch (IOException e) {
													  CSLog.e("cn.com.choice.Server.httpPost", e.getMessage());
												  } finally {
													  Message msg = new Message();
													  msg.what = 0;
													  h.sendMessage(msg);
												  }
											  }

											  @Override
											  public void onHandleMessage(Message msg) {
												  if (onServerResponse != null) {
													  onServerResponse.onResponse(result);
												  }
											  }
										  }
		).run();
	}

	private static String getUrl(String URL, String webServiceAdd) {
		if (!URL.contains("http://")) {
			URL = "http://" + URL;
		}
		if (!"/".equals(URL.lastIndexOf(URL.length() - 1))) {
			URL = URL + "/";
		}
		return URL + webServiceAdd;
	}

	

}
