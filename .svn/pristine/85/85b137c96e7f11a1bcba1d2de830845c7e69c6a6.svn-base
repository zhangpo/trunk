package cn.com.choicesoft.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库公用查询结果 处理类
 * @Author:M.c
 * @CreateDate:2014-1-20
 * @Email:JNWSCZH@163.COM
 */
public class ListProcessor {
	private DBManager db;
	public DBManager getDb(Context context) {
		if(db==null){
			db = new DBManager(context);
		}
		return db;
	}
	/**
	 * 根据传入实体返回对应的实体数组
	 * @param sql SQL语句
	 * @param selectionArgs SQL条件
	 * @param context Activity
	 * @param cal 实体类 【实体内属性只支持：String,Integer,Float,Double,Boolean,Short,Long,Character(char)】
	 * @return List<T>
	 */
	public <T> List<T> query(String sql,String[] selectionArgs,Context context,Class<T> cal){
		SQLiteDatabase database = getDb(context).openDatabase();
		if(database==null){
			ToastUtil.dbToast(context);
			return null;
		}
		Cursor c = database.rawQuery(sql, selectionArgs);
		Method[] methods = cal.getMethods();
		List<T> list=new ArrayList<T>();
		while (c.moveToNext()) {
			Object obj=null;
			try {
				obj=cal.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			for (Method method : methods) {
				String methodName=method.getName();
				if(methodName.matches("^set[A-Z]\\w+$")){
					String name=methodName.substring(3, methodName.length()).toUpperCase();
					for (int i=0;i<c.getColumnCount();i++) {
						if(name.equalsIgnoreCase(c.getColumnName(i))){
							try {
								typeDivision(method,c.getString(i),cal,obj);
								break;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			list.add((T)obj);
		}
		if(c!=null){
			c.close();
		}
		if(database!=null){
			database.close();
		}
		return list;
	}
	/**
	 * 自定义 数据库查询类
	 * @param sql
	 * @param selectionArgs
	 * @param context
	 * @param res
	 * @return
	 */
	public <T> T query(String sql,String[] selectionArgs,Context context,Result<T> res){
		SQLiteDatabase database = getDb(context).openDatabase();
		if(database==null){
			ToastUtil.dbToast(context);
			return null;
		}
		Cursor c = database.rawQuery(sql, selectionArgs);
		try {
			if(res!=null){
				return res.handle(c);
			}
		} catch (Exception e) {
			Log.e("cn.com.choicesoft.util.ListProcessor-自定义query", e.getMessage());
		}finally{
			if(c!=null){
				c.close();
			}
			if(database!=null){
				database.close();
			}
		}
		return null;
	}
	/**
	 * 不同类型转换
	 * @param method
	 * @param value
	 * @throws Exception
	 */
	public void typeDivision(Method method, Object value, Class<?> cls,Object obj)	throws Exception {
		Object typeName = method.getParameterTypes()[0];
		Method setMethod = null;
		value=value==null?"":value;
		if (typeName == String.class) {
			setMethod = cls.getMethod(method.getName(), String.class);
			setMethod.invoke(obj, ValueUtil.isEmpty(value.toString())?"":value.toString());
		}else if (typeName == Double.class||typeName.toString().equals("double")){
			setMethod = cls.getMethod(method.getName(), typeName == Double.class?Double.class:double.class);
			setMethod.invoke(obj, new Double(ValueUtil.isEmpty(value.toString())?"0.0":value.toString()));
		}else if (typeName == Integer.class||typeName.toString().equals("int")) {
			setMethod = cls.getMethod(method.getName(), typeName == Integer.class?Integer.class:int.class);
			setMethod.invoke(obj, Integer.valueOf(ValueUtil.isEmpty(value.toString())?"0":value.toString()));
		}else if (typeName == Long.class||typeName.toString().equals("long")) {
			setMethod = cls.getMethod(method.getName(), typeName == Long.class?Long.class:long.class);
			setMethod.invoke(obj, Long.valueOf(ValueUtil.isEmpty(value.toString())?"0":value.toString()));
		} else if (typeName == Character.class||typeName.toString().equals("char")) {
			setMethod = cls.getMethod(method.getName(),typeName == Character.class?Character.class:char.class);
			setMethod.invoke(obj, ValueUtil.isEmpty(value.toString())?"":value.toString().charAt(0));
		} else if (typeName == Boolean.class||typeName.toString().equals("boolean")) {
			setMethod = cls.getMethod(method.getName(), typeName == Boolean.class?Boolean.class:boolean.class);
            if(ValueUtil.isNaNofBoolean(value.toString())==null) {
                setMethod.invoke(obj, new Boolean(ValueUtil.isEmpty(value.toString()) ? "false" : value.toString()));
            }else{
                setMethod.invoke(obj, new Boolean(ValueUtil.isNaNofBoolean(value.toString())));
            }
		} else if (typeName == Float.class||typeName.toString().equals("float")) {
			setMethod = cls.getMethod(method.getName(), typeName == Float.class?Float.class:float.class);
			setMethod.invoke(obj, Float.valueOf(ValueUtil.isEmpty(value.toString())?"0":value.toString()));
		}  else if (typeName == Short.class||typeName.toString().equals("short")) {
			setMethod = cls.getMethod(method.getName(), typeName == Short.class?Short.class:short.class);
			setMethod.invoke(obj, Short.valueOf(ValueUtil.isEmpty(value.toString())?"":value.toString()));
		}
	}
	/**
	 *@Author:M.c
	 *@Comments:自定义 结果处理
	 *@CreateDate:2014-2-21
	 * @param <T>
	 */
	public interface Result<T>{
		/**
		 * 结果处理类
		 * @return
		 */
		T handle(Cursor c);
	}
}
