package cn.com.choicesoft.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *@Author:M.c
 *@Comments:Ãÿ ‚–Ë«ÛList
 *@CreateDate:2014-1-10
 *@Email:JNWSCZH@163.COM
 */
public class CList<E> extends ArrayList<E>{
	private static final long serialVersionUID = 1L;

	public boolean addMap(Object key,Object value){
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put(key, value);
        CSLog.i("", "name£∫" + key + " value£∫" + value);
		return super.add((E)map);
	}
	public boolean add(Object key,Object value){
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put(key, value);
        CSLog.i("", "name£∫" + key + " value£∫" + value);
		return super.add((E)map);
	}

	public boolean addNameValue(String name,String value){
		NameValuePair nameValuePair=new BasicNameValuePair(name,value);
		CSLog.i("", "name£∫" + name + " value£∫" + value);
		return super.add((E)nameValuePair);
	}

}
