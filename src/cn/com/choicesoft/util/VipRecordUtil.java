package cn.com.choicesoft.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.com.choicesoft.bean.VipRecord;

/**
 * Vip 信息记录
 * @Author:M.c
 * @CreateDate:2014-2-18
 * @Email:JNWSCZH@163.COM
 */
public class VipRecordUtil {
	private DBManager db;
	public DBManager getDb(Context context) {
		if(db==null){
			db = new DBManager(context);
		}
		return db;
	}
	/**
	 * 首先判断表是否纯在-如果存在清除同台相同是数据-然后数据插入
	 * @param context
	 * @param vip
	 */
	public void insertHandle(Context context,VipRecord vip){
		SQLiteDatabase database = null;
		try {
			database = getDb(context).openDatabase();
			if(database==null){
				ToastUtil.dbToast(context);
				return;
			}
			queryTable(context,database);
			delHandle(context, vip.getTableNum());
			ContentValues cvl=new ContentValues();
			cvl.put("Id", createUUID());
			cvl.put("manCounts", vip.getManCounts());
			cvl.put("womanCounts", vip.getWomanCounts());
			cvl.put("orderId", vip.getOrderId());
			cvl.put("Phone", vip.getPhone());
			cvl.put("TableNum", vip.getTableNum());
			cvl.put("CardNumber", vip.getCardNumber());
			cvl.put("CardType", vip.getCardType());
			java.text.DecimalFormat df=new java.text.DecimalFormat("#.00");
			cvl.put("StoredCardsBalance", df.format(vip.getStoredCardsBalance()));

			cvl.put("IntegralOverall", df.format(vip.getIntegralOverall()));
			cvl.put("CouponsOverall", df.format(vip.getCouponsOverall()));
			cvl.put("CouponsAvail", vip.getCouponsAvail());
			cvl.put("ticketInfoList", vip.getTicketInfoList());
			cvl.put("Payable", vip.getPayable());
			cvl.put("DateVal", DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
			database.insert("vip_record",null,cvl);
		} catch (Exception e) {
			Log.e("VipRecordUtil-insertHandle", e.getMessage());
		}finally{
			if(database!=null){
				database.close();
			}
		}
		
	}
	/**
	 * 根据台位删除数据
	 * @param context
	 * @param tableNum
	 */
	public void delHandle(Context context,String tableNum){
		SQLiteDatabase database = getDb(context).openDatabase();
		if(database==null){
			ToastUtil.dbToast(context);
			return;
		}
		try {
			queryTable(context,database);
			//删除条件  
			String whereClause = "TableNum=?";
			//删除条件参数  
			String[] whereArgs = {tableNum};  
			//执行删除  
			database.delete("vip_record",whereClause,whereArgs);  
		} catch (Exception e) {
			Log.e("VipRecordUtil-delHandle",e.getMessage());
		}finally{
			if(database!=null){
				database.close();
			}
		}
	}
	/**
	 * 根据台位查询数据
	 * @param context
	 * @param tableNum
	 * @return
	 */
	public List<VipRecord> queryHandle(Context context,String tableNum){
		SQLiteDatabase dataBase=getDb(context).openDatabase();
		try {
			queryTable(context,dataBase);
		} catch (Exception e) {
			Log.e("VipRecordUtil-queryHandle", e.getMessage());
		}finally{
			if(dataBase!=null){
				dataBase.close();
			}
		}
		return new ListProcessor().query("select * from vip_record where TableNum=? and DateVal=?", new String[]{tableNum,DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")}, context, VipRecord.class);
	}
	/**
	 * 查询vip_record表是否存在
	 * @param context
	 * @param database
	 */
	public void queryTable(Context context,SQLiteDatabase database){
		Cursor cursor=null;
		try {
			cursor=database.rawQuery("SELECT count(*) as count FROM sqlite_master WHERE type='table' AND name='vip_record'",null);
			while (cursor.moveToNext()) {
				if(cursor.getInt(0)<=0){
					database.execSQL("CREATE TABLE vip_record(Id varchar(32)," +
							"orderId varchar(20)," +
							"Phone varchar(11)," +
							"manCounts Integer," +
							"womanCounts Integer," +
							"Payable double,"+
							"ticketInfoList varchar," +
							"TableNum varchar(10),"+
							"CardNumber varchar(20)," +//会员卡号
							"CardType varchar(20)," +//会员卡类型
							"StoredCardsBalance varchar(20)," +//储值余额
							"IntegralOverall varchar(20)," +//积分余额
							"CouponsOverall varchar(20)," +//券余额 券
							"DateVal varchar(10),"+
							"CouponsAvail varchar(10),PRIMARY KEY (id));");//可用余额
				}
			}
		} catch (Exception e) {
			Log.e("VipRecordUtil-queryTable", e.getMessage());
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
	}
	/**
	 * 获取32位UUID
	 * @return
	 */
	public static String createUUID(){
		return String.valueOf(UUID.randomUUID()).replaceAll("-", "");
	}
}
