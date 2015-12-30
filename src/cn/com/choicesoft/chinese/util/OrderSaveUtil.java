package cn.com.choicesoft.chinese.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.util.*;

import java.util.List;
import java.util.Map;

/**
 * 已点菜品保存
 * @Author:M.c
 * @CreateDate:2014-2-18
 * @Email:JNWSCZH@163.COM
 */
public class OrderSaveUtil {
	private DBManager db;
	public DBManager getDb(Context context) {
		if(db==null){
			db = new DBManager(context);
		}
		return db;
	}
	/**
	 * 首先判断表是否存在-如果存在清除同台相同是数据-然后数据插入
	 * @param context
	 */
	public boolean insertHandle(Context context,String tableNum,String orderNum,List<Food> foodList){
		SQLiteDatabase database = null;
		try {
			database = getDb(context).openDatabase();
			if(database==null){
				ToastUtil.dbToast(context);
				return false;
			}
            if(foodList==null){
                return false;
            }
			queryTable(database);
            database.beginTransaction();//开启事务
			delHandle(database, tableNum);
            ContentValues cvl;
            for(Food food:foodList) {
                cvl = new ContentValues();
                cvl.put("orderNum", orderNum);
                cvl.put("tableNum", tableNum);
                cvl.put("tpcode", food.getTpcode());
                cvl.put("pcode", food.getPcode());
                cvl.put("pcname", food.getPcname());
                cvl.put("tpname", food.getTpname());
                cvl.put("unit", food.getUnit());
                cvl.put("price", food.getPrice());
                cvl.put("defalutS", food.getDefalutS());
                cvl.put("MaxCnt", food.getMaxCnt());
                cvl.put("MinCnt", food.getMinCnt());
                cvl.put("pcount", food.getPcount());
                cvl.put("Adjustprice", food.getAdJustPrice());
                cvl.put("Weighrflg", food.getWeightflg());
                cvl.put("istc", food.isIstc());
                cvl.put("Selected", food.isSelected());
                cvl.put("send", food.getSend());
                cvl.put("SoldOut", food.isSoldOut());
                cvl.put("tcMoneyMode", food.getTcMoneyMode());
                cvl.put("counts", food.getCounts());
                cvl.put("tpnum", food.getTpnum());
                cvl.put("fujianame", food.getFujianame());
                cvl.put("fujiaprice", food.getFujiaprice());
                database.insert("order_dishes", null, cvl);
            }
            database.setTransactionSuccessful();//提交事务
            return true;
		} catch (Exception e) {
			CSLog.e("OrderSaveUtil-insertHandle", e.getMessage());
		}finally{
			if(database!=null){
                if(database.inTransaction()){
                    database.endTransaction();
                }
				database.close();
			}
		}
        return false;
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
            delHandle(database, tableNum);
        }catch (Exception e){
            CSLog.e("OrderSaveUtil",e.getMessage());
        }finally {
            if(database!=null) {
                database.close();
            }
        }
    }
    /**
	 * 根据台位删除数据
	 * @param tableNum
	 */
	public void delHandle(SQLiteDatabase database,String tableNum){
		try {
			queryTable(database);
			//删除条件
			String whereClause = "tableNum=?";
			//删除条件参数
			String[] whereArgs = {tableNum};
			//执行删除
			database.delete("order_dishes",whereClause,whereArgs);
		} catch (Exception e) {
			CSLog.e("OrderSaveUtil-delHandle",e.getMessage());
		}
	}
    /**
     * 根据台位删除数据
     * @param context
     * @param tableNum
     */
    public void delItemHandle(Context context,String tableNum,String foodCode,String tcode,String unit){
        SQLiteDatabase database = getDb(context).openDatabase();
        if(database==null){
            ToastUtil.dbToast(context);
            return;
        }
        try {
            delItemHandle(database, tableNum,foodCode,tcode,unit);
        }catch (Exception e){
            CSLog.e("OrderSaveUtil-delItemHandle",e.getMessage());
        }finally {
            if(database!=null) {
                database.close();
            }
        }
    }
    /**
     * 删除单个菜品
     * @param database
     * @param tableNum
     * @param foodCode
     */
    public void delItemHandle(SQLiteDatabase database,String tableNum,String foodCode,String tcode,String unit){
        try{
            queryTable(database);
            String whereClause;
            String[] whereArgs;
            if(tcode==null) {
                whereClause = "tableNum=? and pcode=?";
                if(unit==null){
                    whereClause+=" and unit=?";
                    whereArgs=new String[]{tableNum,foodCode,unit};
                }else {
                    whereArgs=new String[]{tableNum,foodCode};
                }
            }else{
                whereClause = "tableNum=? and tpcode=?";
                whereArgs=new String[]{tableNum,tcode};
            }
            database.delete("order_dishes",whereClause,whereArgs);
        }catch (Exception e){
            CSLog.e("OrderSaveUtil-delItemHandle",e.getMessage());
        }
    }
	/**
	 * 根据台位查询数据
	 * @param context
	 * @param tableNum
	 * @return
	 */
	public List<Food> queryHandle(Context context,String tableNum,String orderNum){
		SQLiteDatabase dataBase=getDb(context).openDatabase();
		try {
			queryTable(dataBase);
		} catch (Exception e) {
			CSLog.e("OrderSaveUtil-queryHandle", e.getMessage());
		}finally{
			if(dataBase!=null){
				dataBase.close();
			}
		}
		return new ListProcessor().query("select * from order_dishes where tableNum=?", new String[]{tableNum}, context, Food.class);
	}
    /**
     * 首先判断表是否存在-如果存在清除同台相同是数据-然后数据插入
     * @param context
     */
    public boolean insertCodesdesc(Context context,List<Map<String,String>> discList){
        SQLiteDatabase database = null;
        try {

            database = getDb(context).openDatabase();
            if(database==null){
                ToastUtil.dbToast(context);
                return false;
            }
            if(discList==null){
                return false;
            }
            database.delete("CODEDESC",null,null);
            database.beginTransaction();//开启事务
            ContentValues cvl;
            for(Map<String,String> discMap :discList) {
                cvl = new ContentValues();
                cvl.put("CODE", discMap.get("CODE"));
                cvl.put("DES", discMap.get("DES"));
                cvl.put("SNO", discMap.get("SNO"));
                database.insert("CODEDESC", null, cvl);
            }
            database.setTransactionSuccessful();//提交事务
            return true;
        } catch (Exception e) {
            CSLog.e("OrderSaveUtil-insertHandle", e.getMessage());
        }finally{
            if(database!=null){
                if(database.inTransaction()){
                    database.endTransaction();
                }
                database.close();
            }
        }
        return false;
    }
	/**
	 * 查询order_dishes表是否存在
	 * @param database
	 */
	public void queryTable(SQLiteDatabase database){
		Cursor cursor=null;
		try {
			cursor=database.rawQuery("SELECT count(*) as count FROM sqlite_master WHERE type='table' AND name='order_dishes'",null);
			while (cursor.moveToNext()) {
				if(cursor.getInt(0)<=0){
					database.execSQL("CREATE TABLE order_dishes(\n" +
                            "orderNum varchar,\n" +
                            "tableNum varchar,\n" +
                            "tpcode varchar,\n" +
                            "pcode varchar,\n" +
                            "pcname varchar,\n" +
                            "tpname varchar,\n" +
                            "unit varchar,\n" +
                            "price varchar,\n" +
                            "defalutS varchar,\n" +
                            "MaxCnt varchar,\n" +
                            "MinCnt varchar,\n" +
                            "pcount varchar,\n" +
                            "Adjustprice varcher,\n" +
                            "Weighrflg varcher,\n" +
                            "istc varchar,\n" +
                            "Selected varchar," +
                            "send varchar," +
                            "SoldOut varchar," +
                            "tcMoneyMode varchar," +
                            "counts varchar," +
                            "tpnum varchar," +
                            "fujianame varchar," +
                            "fujiaprice varchar);");//可用余额
				}
			}
		} catch (Exception e) {
			CSLog.e("OrderSaveUtil-queryTable", e.getMessage());
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
	}

    public int queryDateRow(Context context,String tableNum){
        SQLiteDatabase dataBase=getDb(context).openDatabase();
        int count=0;
        try {
            queryTable(dataBase);
            count=dataBase.rawQuery("select * from order_dishes where tableNum=?", new String[]{tableNum}).getColumnCount();
        } catch (Exception e) {
            CSLog.e("OrderSaveUtil-queryHandle", e.getMessage());
        }finally{
            if(dataBase!=null){
                dataBase.close();
            }
        }
        return count;
    }
}
