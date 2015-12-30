package cn.com.choicesoft.impl;


import java.text.SimpleDateFormat;
import java.util.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.*;
import cn.com.choicesoft.chinese.bean.ChineseZS;
import cn.com.choicesoft.chinese.bean.JGrptyp;
import cn.com.choicesoft.chinese.bean.JNowFood;
import cn.com.choicesoft.chinese.constants.ChineseSql;
import cn.com.choicesoft.util.*;

/**
 * ��Ʒ���ݹ���ʵ����
 */
public class DishDataManagerImpl implements DishDataManager{
	
	private static final String TAG = "DishDataManagerImpl";
	public Context mContext;
	private static DishDataManager mDishDataManager = null;
	private ListProcessor processor;
    private DBManager manager=null;
	FoodList mFoodList = new FoodList();
	
	private String dateString = null;
	
	@SuppressLint("SimpleDateFormat")
	private DishDataManagerImpl(Context pContext){
		mContext = pContext;
		processor = new ListProcessor();

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dateString = sdf.format(date);
        manager = new DBManager(mContext);
	}
	public SQLiteDatabase getDB(){
        if(manager!=null) {
            return manager.openDatabase();
        }else{
            return  null;
        }
    }
	public static DishDataManager getDishDataManager(Context pContext){
		if(null == mDishDataManager){
			mDishDataManager = new DishDataManagerImpl(pContext);
		}
		return mDishDataManager;
	}
	
	
	/**
	 * //ȫ��ҳ�棬��AllCheck������в�Ʒ,������̨λ�ţ��˵��� 
	 * ����̨λ�ţ��˵��ţ�����ʱ�� �� send=1 ����ѯ
	 */
	@Override
	public List<Food> getAllFoodListByTablenum(String tableNum,String orderID) {
		mFoodList.clear();
		updateFromDBForAllBill(tableNum,orderID);
		return mFoodList.getAllFoodListByTablenum();
	}

	private void updateFromDBForAllBill(String tableNum,String orderID) {
		Cursor cursor = null;
        SQLiteDatabase db=getDB();
		try {
            if(db!=null) {
                cursor = db.query("AllCheck", null, "tableNum = ? and Time= ? and orderId = ? and Send = 1", new String[]{tableNum, dateString, orderID}, null, null, null);
            }
			while(cursor.moveToNext()){
				Food food = buildFood(cursor);
				mFoodList.add(food);
			}
		} catch (Exception e) {
			Log.d(TAG, "initData fail");
		}finally{
			if(cursor!=null){
				cursor.close();
			}
            if(db!=null){
                db.close();
            }
		}
	}

	private Food buildFood(Cursor cursor) {
		Food f = new Food();
		f.setTabNum(cursor.getString(cursor.getColumnIndex("tableNum")));
		f.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
		f.setTime(cursor.getString(cursor.getColumnIndex("Time")));
		f.setPKID(cursor.getString(cursor.getColumnIndex("PKID")));
		f.setPcode(cursor.getString(cursor.getColumnIndex("Pcode")));
		f.setPcname(cursor.getString(cursor.getColumnIndex("PCname")));
		f.setTpcode(cursor.getString(cursor.getColumnIndex("Tpcode")));
		f.setTpname(cursor.getString(cursor.getColumnIndex("TPNAME")));
		f.setTpnum(cursor.getString(cursor.getColumnIndex("TPNUM")));
		f.setPcount(cursor.getString(cursor.getColumnIndex("pcount")));
		f.setPromonum(cursor.getString(cursor.getColumnIndex("promonum")));
		f.setFujiacode(cursor.getString(cursor.getColumnIndex("fujiacode")));
		f.setFujianame(cursor.getString(cursor.getColumnIndex("fujianame")));
		f.setPrice(cursor.getString(cursor.getColumnIndex("price")));
		f.setFujiaprice(cursor.getString(cursor.getColumnIndex("fujiaprice")));
		f.setWeight(cursor.getString(cursor.getColumnIndex("Weight")));
		f.setWeightflg(cursor.getString(cursor.getColumnIndex("Weightflg")));
		f.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
		f.setIstc(cursor.getString(cursor.getColumnIndex("ISTC")).equals("1")?true:false);//�˴��Ƚ����� 1 true            0 false
		f.setOver(cursor.getString(cursor.getColumnIndex("Over")));
		f.setUrge(cursor.getString(cursor.getColumnIndex("Urge")));
		f.setMan(cursor.getString(cursor.getColumnIndex("man")));
		f.setWoman(cursor.getString(cursor.getColumnIndex("woman")));
		f.setSend(cursor.getString(cursor.getColumnIndex("Send")));
		f.setCLASS(cursor.getString(cursor.getColumnIndex("CLASS")));
		f.setCnt(cursor.getString(cursor.getColumnIndex("CNT")));
		f.setId(cursor.getInt(cursor.getColumnIndex("ID")));
		return f;
	}

	@Override
	public void updateFoodOverItem(Food food) {
		ContentValues values = buildValuesForMarkDish(food);
        SQLiteDatabase db=getDB();
        try {
            if (db != null) {
                db.update("AllCheck", values, "tableNum=? and Pcode=?", new String[]{food.getTabNum(), food.getPcode()});
            }
        }catch (Exception e){
            CSLog.e("DishDataManagerImpl", e.getMessage());
        }finally {
            if(db!=null){
                db.close();
            }
        }

	}

	private ContentValues buildValuesForMarkDish(Food food) {
		ContentValues values = new ContentValues();
		values.put("Over", food.getOver());
		return values;
	}
	
	/**
	 * �ڵ��ҳ�棬��ѯ����Ʒ����
	 */
	@Override
	public List<Grptyp> getAllDishClassList() {
		List<Grptyp> grpLists = processor.query("select grp,des from class order by grp", null, mContext, Grptyp.class);//��ò�Ʒ���
		if (SharedPreferencesUtils.getChineseSnack(mContext)!=0) {
			Grptyp grptyp=new Grptyp();
	        grptyp.setDes(mContext.getString(R.string.set_meal));
	        grptyp.setGrp("00");
	        grpLists.add(0,grptyp);
		}
		return grpLists;
	}
	
	
	/**
	 * �õ����и�����ǵ�Ʒ������,���ݲ�Ʒ�����ѯ���ò�Ʒ�����Ӧ�����и�����
	 */
	@Override
	public List<Addition> getAllFujiaListByPcode(String pcode) {
        List<Addition> grpLists;
        if(SharedPreferencesUtils.getChineseSnack(mContext)==0) {
            grpLists = processor.query("select INIT,PCODE,FOODFUJIA_ID,FOODFUJIA_DES,Fprice as FOODFUJIA_CHECKED,'false' as ISSELECTED from FoodFuJia where PCODE = ? order by foodfujia_id", new String[]{pcode}, mContext, Addition.class);//������в�Ʒ������
        }else{
            grpLists=processor.query(ChineseSql.ATTACH_SQL,new String[]{pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode,pcode},mContext,Addition.class);
            if(ValueUtil.isEmpty(grpLists)) {
                grpLists = processor.query("select INIT,\"\" as PCODE,ITCODE AS FOODFUJIA_ID,DES AS FOODFUJIA_DES,PRICE1 AS FOODFUJIA_CHECKED,'false' as ISSELECTED from Attach", null, mContext, Addition.class);
            }
        }
		return grpLists;
	}
	
	
	/**
	 * �õ����и�����ǵ�Ʒ������,������û�в�Ʒ��������
	 */
	@Override
	public List<Addition> getAllFujiaListNoPcode() {
        List<Addition> singleAddLists;
        if(SharedPreferencesUtils.getChineseSnack(mContext)==0) {
            singleAddLists = processor.query("select INIT,PCODE,FOODFUJIA_ID,FOODFUJIA_DES,Fprice as FOODFUJIA_CHECKED,'false' as ISSELECTED from FoodFuJia where PCODE like '%pcode%' or pcode is null", null, mContext, Addition.class);
            //singleAddLists = processor.query("select INIT,PCODE,FOODFUJIA_ID,FOODFUJIA_DES,Fprice as FOODFUJIA_CHECKED,'false' as ISSELECTED from FoodFuJia", null, mContext, Addition.class);
        }else{
            singleAddLists=processor.query("select INIT,\"\" as PCODE,ITCODE AS FOODFUJIA_ID,DES AS FOODFUJIA_DES,PRICE1 AS FOODFUJIA_CHECKED,'false' as ISSELECTED from Attach",null,mContext,Addition.class);
        }
		return singleAddLists;
	}
	
	
	/**
	 * �ڵ��ҳ�棬��food��������в�Ʒ
	 */
	@Override
	public List<Food> getAllFoodList() {
		mFoodList.clear();
		initDataFromDB();
		return mFoodList.getAllFoodListFromtabFood();
	}

    /**
     * ��ȡ��Ʒ
     */
	private void initDataFromDB() {
		Cursor cursor = null;
        SQLiteDatabase db=getDB();
        try {
            if (db != null) {
                if (SharedPreferencesUtils.getChineseSnack(mContext) == 0) {//0Ϊ��� 1Ϊ�в�
                    cursor = db.rawQuery("select PRICE,CLASS,0 as ISSELECTED,ITCODE,DES,\n" +
                                        "UNIT1 as UNIT,ISTC,TCMONEYMODE,FUJIAMODE,UNITCUR,\n" +
                                        "UNIT2,UNIT3,UNIT4,UNIT5,UNIT6,\n" +
                                        "PRICE5,PRICE6,PRICE7,PRICE8,PRICE9,\n" +
                                        "(select name from measdoc m where m.code=f.unit1) as UNITNAME,\n" +
                                        "(select name from measdoc m where m.code=f.unit2) as UNITNAME2,\n" +
                                        "(select name from measdoc m where m.code=f.unit3) as UNITNAME3,\n" +
                                        "(select name from measdoc m where m.code=f.unit4) as UNITNAME4,\n" +
                                        "(select name from measdoc m where m.code=f.unit5) as UNITNAME5,\n" +
                                        "(select name from measdoc m where m.code=f.unit6) as UNITNAME6, ISTEMP,STATE from food f\n" +
                                        " where class not null and class !=''", null);
                    while (cursor.moveToNext()) {
                        Food food = buildFoodFromtableFood(cursor);
                        mFoodList.add(food);
                    }
                } else {//�в�
                    cursor = db.rawQuery("select PRICE,CLASS,0 as ISSELECTED,ITCODE,DES,UNIT,0 as ISTC,1 as TCMONEYMODE,\n" +
                            "UNIT2,UNIT3,UNIT4,UNIT5,\n" +
                        "PRICE2,PRICE3,PRICE4,PRICE5,PLUSD,RE1,RE2,RE3,RE4,RE5,RE6,RE7,RE8,RE9,RE10 from food where class not null and class !=''\n" +
                            "union\n" +
                            "select PRICE,'00' as CLASS,0 as ISSELECTED,packid as ITCODE,DES,'��' as UNIT,1 as ISTC,1 as TCMONEYMODE,\n" +
                            "\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",0 as PLUSD,\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\" from PACKAGE\n" +
                            "where class not null and class !='' order by itcode ", null);
                    while (cursor.moveToNext()) {
                        Food food = chineseBuildFood(cursor);
                        mFoodList.add(food);
                    }
                }
            }
		} catch (Exception e) {
			Log.e(TAG, "��ѯfood����ȡ���еĲ�Ʒfail"+e.getMessage());
		}finally{
			if(cursor!=null){
				cursor.close();
			}
            if(db!=null){
                db.close();
            }
		}
	}

    private Food chineseBuildFood(Cursor cursor){
        Food food=new Food();
        food.setPrice(cursor.getString(0));//PRICE
        food.setSortClass(cursor.getString(1));//CLASS
        food.setSelected(cursor.getInt(2) == 0 ? false : true);//ISSELECTED
        food.setPcode(cursor.getString(3));//ITCODE
        food.setPcname(cursor.getString(4));//DES
        food.setUnit(cursor.getString(5));//UNIT
        food.setIstc("1".equals(cursor.getString(6)) ? true : false);//ISTC �˴��Ƚ����� 1 true            0 false
        food.setTcMoneyMode(cursor.getString(7));//TCMONEYMODE
        String unit2=cursor.getString(8);//UNIT2
        String unit3=cursor.getString(9);//UNIT3
        String unit4=cursor.getString(10);//UNIT4
        String unit5=cursor.getString(11);//UNIT5
        String price2=cursor.getString(12);//PRICE2
        String price3=cursor.getString(13);//PRICE3
        String price4=cursor.getString(14);//PRICE4
        String price5=cursor.getString(15);//PRICE5
        food.setPlusd(cursor.getInt(16));//PLUSD
        Map<String,Unit> map=new HashMap<String,Unit>();

        if(isValue(food.getUnit(), food.getPrice())){
            map.put("unit1",setValue(food.getUnit(),food.getUnit(),food.getPrice()));
        }
        if (SingleMenu.getMenuInstance().getPriceTyp().equals("Y")) {
            if (isValue(unit2, price2) && !unit2.equals(food.getUnit())) {
                map.put("unit2", setValue(unit2, unit2, price2));
            }
            if (isValue(unit3, price3) && !unit3.equals(food.getUnit()) && !unit2.equals(unit3)) {
                map.put("unit3", setValue(unit3, unit3, price3));
            }
            if (isValue(unit4, price4) && !unit4.equals(food.getUnit()) && !unit2.equals(unit4) && !unit3.equals(unit4)) {
                map.put("unit4", setValue(unit4, unit4, price4));
            }
            if (isValue(unit5, price5) && !unit5.equals(food.getUnit()) && !unit2.equals(unit5) && !unit3.equals(unit5) && !unit4.equals(unit5)) {
                map.put("unit5", setValue(unit5, unit5, price5));
            }
        }else {
            if (isValue(unit2, price2)) {
                map.put("unit2", setValue(unit2, unit2, price2));
            }
            if (isValue(unit3, price3)) {
                map.put("unit3", setValue(unit3, unit3, price3));
            }
            if (isValue(unit4, price4)) {
                map.put("unit4", setValue(unit4, unit4, price4));
            }
            if (isValue(unit5, price5)) {
                map.put("unit5", setValue(unit5, unit5, price5));
            }
        }
        food.setUnitMap(map);
        return food;
    }
    /**
     * ������ݽ���
     * @param cursor
     * @return
     */
    private Food buildFoodFromtableFood(Cursor cursor) {
        Food food = new Food();
        food.setPrice(cursor.getString(0));//PRICE
        food.setSortClass(cursor.getString(1));//CLASS
        food.setSelected(cursor.getInt(2) == 0 ? false : true);//ISSELECTED
        food.setPcode(cursor.getString(3));//ITCODE
        food.setPcname(cursor.getString(4));//DES
        food.setUnitCode(cursor.getString(5));//UNIT
        food.setIstc("1".equals(cursor.getString(6)) ? true : false);//ISTC�˴��Ƚ����� 1 true            0 false
        food.setTcMoneyMode(cursor.getString(7));//TCMONEYMODE
        food.setFujiaModel(cursor.getInt(8));//FUJIAMODE
        food.setWeightflg(cursor.getString(9));//UNITCUR
        food.setIsTemp(ValueUtil.isNaNofInteger(cursor.getString(26)));//��ʱ��
        food.setState(ValueUtil.isNaNofInteger(cursor.getString(27)));
        String unit2=cursor.getString(10);//
        String unit3=cursor.getString(11);//
        String unit4=cursor.getString(12);//
        String unit5=cursor.getString(13);//
        String unit6=cursor.getString(14);//
        String price2=cursor.getString(15);//
        String price3=cursor.getString(16);//
        String price4=cursor.getString(17);//
        String price5=cursor.getString(18);//
        String price6=cursor.getString(19);//
        food.setUnit(cursor.getString(20));//
        String unitName2=cursor.getString(21);//
        String unitName3=cursor.getString(22);//
        String unitName4=cursor.getString(23);//
        String unitName5=cursor.getString(24);//
        String unitName6=cursor.getString(25);//
        Map<String,Unit> map=new HashMap<String,Unit>();
        if(isValue(food.getUnit(),food.getPrice())){
            map.put("unit1",setValue(food.getUnit(),food.getUnitCode(),food.getPrice()));
        }
        if(isValue(unit2,price2)){
            map.put("unit2",setValue(unitName2,unit2,price2));
        }
        if(isValue(unit3,price3)){
            map.put("unit3",setValue(unitName3,unit3,price3));
        }
        if(isValue(unit4,price4)){
            map.put("unit4",setValue(unitName4,unit4,price4));
        }
        if(isValue(unit5,price5)){
            map.put("unit5",setValue(unitName5,unit5,price5));
        }
        if(isValue(unit6,price6)){
            map.put("unit6",setValue(unitName6,unit6,price6));
        }
        food.setUnitMap(map);
		return food;
	}
	public boolean isValue(String u,String p){
        if(ValueUtil.isNotEmpty(u)&&!u.contains("UNTI")&&!u.contains("null")&&!u.contains("UNIT")){
            if(ValueUtil.isNotEmpty(p)&&!p.contains("PRICE")&&!u.contains("null")){
                return true;
            }
        }
        return false;
    }
	public Unit setValue(String name,String code,String price){
        Unit unit=new Unit();
        unit.setUnitCode(code);
        unit.setUnitPrice(price);
        unit.setUnitName(name);
        return unit;
    }
	/**
	 * ��products_sub���producttc_order ��һ��
	 */
	@Override
	public List<String> queryTcOrder(Food f) {
		
		Cursor cursor = null;
		List<String> tcOrderlist = new ArrayList<String>();
        SQLiteDatabase db=getDB();
        try {
            if (db != null) {
                cursor = db.rawQuery("select producttc_order from products_sub where pcode = ? group by producttc_order", new String[]{f.getPcode()});
                while (cursor.moveToNext()) {
                    String tcOrder = cursor.getString(cursor.getColumnIndex("PRODUCTTC_ORDER"));
                    tcOrderlist.add(tcOrder);
                }
            }
		} catch (Exception e) {
			Log.d(TAG, "��ѯ�ײ���ϸ������ȡ���ײ����ж��ٸ���Ʒ");
		}finally{
			if(cursor!=null){
				cursor.close();
			}
            if(db!=null){
                db.close();
            }
		}
		
		return tcOrderlist;
	}
	
	/**
	 * ��products_sub������ײͶ�Ӧ��������ϸ   �ڶ���
	 */
	@Override
	public List<List<Food>> queryTcItemLists(Food pFood) {
		List<List<Food>> allLists = new ArrayList<List<Food>>();
        if(SharedPreferencesUtils.getChineseSnack(mContext)==0) {
            List<String> tcOrderlist = queryTcOrder(pFood);//��products_sub���producttc_order ��һ��
            for (String str : tcOrderlist) {
                Cursor c = null;
                List<Food> tempLists = null;
                SQLiteDatabase db=getDB();
                try {
                    if (db != null) {
                        tempLists = new ArrayList<Food>();
//                        c = db.rawQuery("select pcode,pcode1,pname,unit,cnt,price1,producttc_order,defualtS,NADJUSTPRICE,case when mincnt>0 then mincnt else 0 end as MINCNT,case when Maxcnt>0 then maxcnt else 1 end as MAXCNT from products_sub where pcode = ? and producttc_order = ?", new String[]{pFood.getPcode(), str});
                        String sql="SELECT " +
                                "s.pcode," +//�ײͱ���
                                " s.pcode1," +//��Ʒ����
                                " s.pname," +//��Ʒ����
                                " s.unit," +//��Ʒ��λ
                                " s.cnt," +//��Ʒ����
                                " s.price1," +//��Ʒ����
                                " s.producttc_order," +//�����ʶ
                                " s.defualtS," +//˳��
                                " s.NADJUSTPRICE," +//�ײͼӼ�
                                " f.ISTC," +
                                " f.TCMONEYMODE," +
                                " f.FUJIAMODE," +
                                " f.UNITCUR," +
                                " CASE" +
                                " WHEN mincnt > 0 THEN" +
                                "  s.mincnt" +
                                " ELSE" +
                                "  0" +
                                " END AS MINCNT," +
                                " CASE" +
                                " WHEN Maxcnt > 0 THEN" +
                                "  s.maxcnt" +
                                " ELSE" +
                                "  1" +
                                " END AS MAXCNT,a.PRICE" +
                                " FROM products_sub s,food a " +
                                " left join food f on s.pcode1=f.ITCODE " +
                                " where s.pcode = ? and s.producttc_order = ? and f.class not null and f.class !='' and a.itcode=s.pcode1";
                        CSLog.i("��ȡ�ײ���ϸ��ϢSQL",sql);
                        CSLog.i("��ȡ�ײ���ϸ��Ϣ����",pFood.getPcode()+"----"+str);
                        c=db.rawQuery(sql, new String[]{pFood.getPcode(), str});
                        foodMastFJ(tempLists, pFood, c, null);
                    }
                } catch (Exception e) {
                    Log.e("��ѯ����ײ���ϸ����", e.getMessage());
                } finally {
                    if (c != null) {
                        c.close();
                    }
                    if(db!=null){
                        db.close();
                    }
                }
                allLists.add(tempLists);
            }
        }else{
            Cursor c=null;
            SQLiteDatabase db=getDB();
            try {
                if (db != null) {
                    List<Food> tempLists = new ArrayList<Food>();
                    c = db.rawQuery("select packid as PCODE,f.itcode as PCODE1,f.des as PNAME,\n" +
                            "f.UNIT,f.PRICE as PRICE1,defualtS,0 as NADJUSTPRICE,grp as producttc_order,\n" +
                            "case when grp='N' or defualts<=0 then cnt else 0 end MINCNT,\n" +
                            "cnt as MAXCNT\n" +
                            " from (\n" +
                            "select 1 as defualtS,subitem as item,l.packid,g.cnt,g.item as grp from ITEMPKG g\n" +
                            "left join packdtl l\n" +
                            "on l.item=g.item\n" +
                            "where l.TAG='Y' and l.packid=? and g.item!=g.subitem\n" +
                            "union all\n" +
                            "select 1 as defualtS,item,packid,cnt,\n" +
                            "case when TAG='Y' then\n" +
                            "    item\n" +
                            "else 'N' end as grp from PACKDTL where packid=?\n" +
                            "union all\n" +
                            "select 0 as defualtS,item,packid,cnt,item as grp from PACKDTL Where TAG='Y' and packid=?) g\n" +
                            "left join food f\n" +
                            "on g.item=f.item\n" +
                            "order by producttc_order", new String[]{pFood.getPcode(), pFood.getPcode(), pFood.getPcode()});
                    foodCPD(tempLists, pFood, c, allLists);
                }
            } catch (Exception e) {
                Log.e("��ѯ�в��ײ���ϸ����", e.getMessage());
            } finally {
                if (c != null) {
                    c.close();
                }
                if(db!=null){
                    db.close();
                }
            }
        }
		return allLists;
	}

    /**
     * ���������ײ���ϸ��ʾ��ѡ������
     * @param tempLists
     * @param pFood
     * @param c
     * @param allLists
     */
    private void foodMastFJ(List<Food> tempLists,Food pFood,Cursor c,List<List<Food>> allLists){
        while (c.moveToNext()) {
            Food food = new Food();
            food.setTpcode(c.getString(0));
            food.setPcode(c.getString(1));
            food.setPcname(c.getString(2));
            food.setTpname(pFood.getPcname());//�˴�������
//			food.setPcount("1");
            food.setUnit(c.getString(3));
//			food.setCnt(c.getString(c.getColumnIndex("CNT")));
            food.setPrice(c.getString(5));
            food.setDefalutS(ValueUtil.isNotEmpty(c.getString(7)) ? c.getString(7) : "");
            food.setMaxCnt(ValueUtil.isNotEmpty(c.getString(14)) ? c.getString(14) : "");
            food.setMinCnt(ValueUtil.isNotEmpty(c.getString(13)) ? c.getString(13) : "");
            food.setPcount(ValueUtil.isNotEmpty(food.getMinCnt()) ? food.getMinCnt() : "");
            food.setAdJustPrice(ValueUtil.isNotEmpty(c.getDouble(8)) ? c.getDouble(8) : 0);
            food.setWeightflg(c.getString(12));
            food.setSelected(false);
            food.setIstc(true);
            food.setFujiaModel(c.getInt(11));
            food.setTcMoneyMode(c.getString(10));
            if (c.getString(15)!=null)
            {
                food.setEachprice(c.getString(15));
            }

            tempLists.add(food);
        }
    }

    /**
     * �вͽ����ײ����ݣ��ޱ�ѡ�����������
     * @param tempLists
     * @param pFood
     * @param c
     * @param allLists
     */
    private void foodCPD(List<Food> tempLists,Food pFood,Cursor c,List<List<Food>> allLists){
        String mark=null;
        while (c.moveToNext()) {
            Food food = new Food();
            food.setTpcode(c.getString(c.getColumnIndex("PCODE")));
            food.setPcode(c.getString(c.getColumnIndex("PCODE1")));
            food.setPcname(c.getString(c.getColumnIndex("PNAME")));
            food.setTpname(pFood.getPcname());//�˴�������
//			food.setPcount("1");
            food.setUnit(c.getString(c.getColumnIndex("UNIT")));
//			food.setCnt(c.getString(c.getColumnIndex("CNT")));
            food.setPrice(c.getString(c.getColumnIndex("PRICE1")));
            food.setDefalutS(ValueUtil.isNotEmpty(c.getString(c.getColumnIndex("defualtS"))) ? c.getString(c.getColumnIndex("defualtS")) : "");//����
            food.setMaxCnt(ValueUtil.isNotEmpty(c.getString(c.getColumnIndex("MAXCNT"))) ? c.getString(c.getColumnIndex("MAXCNT")) : "");//����
            food.setMinCnt(ValueUtil.isNotEmpty(c.getString(c.getColumnIndex("MINCNT"))) ? c.getString(c.getColumnIndex("MINCNT")) : "");//����
            food.setPcount(ValueUtil.isNotEmpty(food.getMinCnt()) ? food.getMinCnt() : "");
            food.setAdJustPrice(ValueUtil.isNotEmpty(c.getDouble(c.getColumnIndex("NADJUSTPRICE"))) ? c.getDouble(c.getColumnIndex("NADJUSTPRICE")) : 0);
            food.setWeightflg("1");//�˴��Ƚ�����    ��ѯʱ��û�д��ֶΣ�������Ϊ�丳ֵ,Ĭ���������ƷΪ��һ��λ
            food.setIstc(true);//�˴��Ƚ�����    ��ѯʱ��û�д��ֶΣ�������Ϊ�丳ֵ
            food.setSelected(false);//�˴��Ƚ�����     ��ѯʱ��û�д��ֶΣ�������Ϊ�丳ֵ
            if(allLists!=null){//Ϊ�ձ�ʶΪ�в�
                String po=c.getString(c.getColumnIndex("producttc_order"));
                if(mark==null){//�ж��Ƿ�Ϊ��
                    tempLists.add(food);
                    mark=po;
                }else if(!mark.equals(po)){//�������֤ͬ��Ӧ�÷�����
                    mark=po;
                    allLists.add(tempLists);
                    tempLists=new ArrayList<Food>();
                    tempLists.add(food);
                }else{
                    tempLists.add(food);
                }
            }else{
                tempLists.add(food);
            }
        }
        if(allLists!=null){
            allLists.add(tempLists);
        }
    }
	/**
	 * ��food����ڶ���λ����  SELECT itcode,des,unitcur FROM food WHERE UNITCUR = 2
	 */
	@Override
	public List<CurrentUnit> queryCurUnitLists() {
		Cursor c= null;
		List<CurrentUnit> curUnitLists = new ArrayList<CurrentUnit>();
        SQLiteDatabase db=getDB();
        try {
            if (db != null) {
                c = db.rawQuery("SELECT itcode,des,unitcur FROM food WHERE UNITCUR = 2", null);
                while (c.moveToNext()) {
                    CurrentUnit curUnit = new CurrentUnit();
                    curUnit.setItcode(c.getString(c.getColumnIndex("ITCODE")));
                    curUnit.setDes(c.getString(c.getColumnIndex("DES")));
                    curUnit.setUnitcur(c.getString(c.getColumnIndex("UNITCUR")));
                    curUnitLists.add(curUnit);
                }
            }
		} catch (Exception e) {
			CSLog.e(TAG, "��ѯ�ڶ���λ"+e.getMessage());
		}finally{
			if(c!=null){
				c.close();
			}
            if(db!=null){
                db.close();
            }
		}
		
		return curUnitLists;
	}
	
	
	/**
	 * ���Ͳ�Ʒ�ɹ���,���浽���ݿ�
	 */
	@Override
	public long insertAllCheckSendFinish(SQLiteDatabase db,Food f) {
		
		ContentValues values = new ContentValues();
		values.put("tableNum", f.getTabNum());
		values.put("orderId", f.getOrderId());
		values.put("Time", dateString);//���뵱��ʱ��
		values.put("PKID", ValueUtil.isEmpty(f.getPKID()) ? "" : f.getPKID());
		values.put("Pcode", f.getPcode());
		values.put("PCname", f.getPcname());
		values.put("Tpcode", ValueUtil.isEmpty(f.getTpcode())?"":f.getTpcode());
		values.put("TPNAME", ValueUtil.isEmpty(f.getTpname())?"":f.getTpname());
		values.put("TPNUM", ValueUtil.isEmpty(f.getTpnum())?"":f.getTpnum());
		values.put("pcount", f.getPcount());
		values.put("promonum", ValueUtil.isEmpty(f.getPromonum())?"":f.getPromonum());
		values.put("fujiacode", ValueUtil.isEmpty(f.getFujiacode())?"":f.getFujiacode());
		values.put("fujianame", ValueUtil.isEmpty(f.getFujianame())?"":f.getFujianame());
		values.put("price", f.getPrice());
		values.put("fujiaprice", ValueUtil.isEmpty(f.getFujiaprice())?"":f.getFujiaprice());
		values.put("Weight", ValueUtil.isNotEmpty(f.getWeight())? f.getWeight():"");
		values.put("Weightflg", ValueUtil.isNotEmpty(f.getWeightflg())?f.getWeightflg():"");
		values.put("unit", f.getUnit());
		values.put("ISTC", f.isIstc()?"1":"0");//�˴������⣬��boolean����
		values.put("Over", "0");
		values.put("Urge", "0");
		values.put("man", ValueUtil.isEmpty(f.getMan())?"0":f.getMan());
		values.put("woman", ValueUtil.isEmpty(f.getWoman())?"0":f.getWoman());
		values.put("Send", f.getSend());//1�����ѷ���        0�����ݴ�
		values.put("CLASS", f.getCLASS());//�������� 1           ����2
		values.put("CNT", ValueUtil.isNotEmpty(f.getCnt())?f.getCnt():"");
        long id=-1;
        try {
            if (db != null) {
                id=db.insert("AllCheck", null, values);
            }
        }catch (Exception e){
            CSLog.e(TAG,e.getMessage());
        }
        return id;
    }

	@Override
	public void updateFoodUrgeItem(Food food) {
		ContentValues values = buildUrgeValues(food);
        SQLiteDatabase db=getDB();
        try {
            int i = -1;
            if (db != null) {
                i = db.update("AllCheck", values, "ID=?", new String[]{food.getId() + ""});
            }
        }catch (Exception e){
            CSLog.e(TAG,e.getMessage());
        }finally {
            if(db!=null){
                db.close();
            }
        }
    }

	private ContentValues buildUrgeValues(Food food) {
		ContentValues values = new ContentValues();
		int urgeCounts = Integer.valueOf(food.getUrge());
		urgeCounts += 1;
		values.put("Urge", urgeCounts);
		return values;
	}
	
	/**
	 * ����˺�û�з��ͣ��ݴ浽���ݿ⣬����̨λ�ţ��˵��ţ�send=0����ѯ
	 */
	@Override
	public List<Food> queryAllCheckIfCache(String tableNum,String orderID) {
		mFoodList.clear();
		updateFromDBForCache(tableNum,orderID);
		return mFoodList.getAllFoodListByTablenum();
	}

	private void updateFromDBForCache(String tableNum, String orderID) {
		Cursor cursor = null;
        SQLiteDatabase db=getDB();
        try {
            if (db != null) {
                cursor = db.query("AllCheck", null, "tableNum = ? and Time= ? and orderId = ? and Send = 0", new String[]{tableNum, dateString, orderID}, null, null, null);
                while (cursor.moveToNext()) {
                    Food food = buildFood(cursor);
                    mFoodList.add(food);
                }
            }
		} catch (Exception e) {
			CSLog.d(TAG, "initData fail");
		}finally{
			if(cursor!=null){
				cursor.close();
			}
            if(db!=null){
                db.close();
            }
		}
	}
	
	/**
	 * ��̨��ʱ������û�ɫ̨λ����Ӧ��AllCheck���������ݣ���ɾ��
	 */
	@Override
	public void deleteDishListsByTblnum(String tableNum) {
        SQLiteDatabase db=getDB();
		int i = db.delete("AllCheck", "tableNum=?", new String[]{ tableNum });
		Log.i("��̨��ʱ������û�ɫ̨λ����Ӧ��AllCheck���������ݣ���ɾ��", i + "");
        db.close();
	}
	
	/**
	 * ��̨��ʱ�򣬸���̨λ�Ž�ԭ�е�̨λ���������
	 */
	@Override
	public void clearAllCheckByTabNum(String tableNum) {
        SQLiteDatabase db=getDB();
		int i = db.delete("AllCheck", "tableNum=?", new String[]{tableNum});
        db.close();
	}
	
	/**
	 * //��ѯ������ԭ��
	 */
	@Override
	public List<PresentReason> queryPresentReason() {
		List<PresentReason> presentReasons = processor.query("select VCode,VName,VInit from presentreason", null, mContext, PresentReason.class);//��ò�Ʒ���
		return presentReasons;
	}
	
	@Override
	public void updateDishesByOver(String table, String over,String pcode,String pkid) {
        SQLiteDatabase db=getDB();
		Log.e("SQL-----", "UPDATE AllCheck SET over="+over+" WHERE tableNum='"+table+"' and pcode='"+pcode+"' and pkid='"+pkid+"'");
		db.execSQL("UPDATE AllCheck SET over="+over+" WHERE tableNum='"+table+"' and pcode='"+pcode+"' and pkid='"+pkid+"'");
        db.close();
	}
	@Override
	public void updateDishesByOver(String table,int state) {
        SQLiteDatabase db=getDB();
		if(state==0){
			db.execSQL("UPDATE AllCheck Set over=pcount where tableNum="+table);
		}else{
			db.execSQL("UPDATE AllCheck Set over=0 where tableNum="+table);
		}
        db.close();
	}
	
	/**
	 * ��ѯ������������
	 */
	@Override
	public List<CommonFujia> queryCommonFujia() {
        List<CommonFujia> comFujiaLists;
        if(SharedPreferencesUtils.getChineseSnack(mContext)==0) {
            comFujiaLists = processor.query("select Id,DES,Init from specialremark", null, mContext, CommonFujia.class);
        }else {
            comFujiaLists = processor.query("select ITCODE AS Id,Init,DES,price1 as PRICE from Attach", null, mContext, CommonFujia.class);
        }
		return comFujiaLists;
	}
	
	
	/**
	 * ���ѵ��ҳ�棬����Ѿ��ݴ���ˣ����ݴ��ɾ��ԭ���� ,ɾ��send=0
	 */
	@Override
	public void deleteDataIfSaved(String tblNumber, String orderId) {
        SQLiteDatabase db=getDB();
		db.delete("AllCheck", "tableNum=? and orderId=? and send= 0", new String[]{tblNumber,orderId});
        db.close();
	}
	
	/**
	 * �����������е����ݱ��浽���ݿ⣬ѭ������,����ɹ�������true
	 */
	@Override
	public boolean circleInsertAllCheck(List<Food> lists) {
        SQLiteDatabase db=getDB();
		boolean isInsertSuccess = true;

		db.beginTransaction();
		try {
			for(Food f:lists){
				insertAllCheckSendFinish(db,f);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.d("&&&&&&&&&&&", e.toString());
			isInsertSuccess = false;
			// TODO ���������쳣
		}finally{
			if(db!=null){
				if(db.inTransaction()){
					db.endTransaction();
				}
				db.close();
			}
			
		}
		return isInsertSuccess;
	}
	
	
	//��̨�ɹ����޸��˵���
	@Override
	public void updateOrderIdAfterBingtaiSuccess(String thisTblNumber,String tagTblNumber,String newOrderId) {
//		update AllCheck set PKID='jobs' where orderId = 'H000440' or orderId = 'H000441'
        SQLiteDatabase db=getDB();
		ContentValues values = new ContentValues();
		values.put("orderId", newOrderId);
		db.update("AllCheck", values, "tableNum=? or tableNum=?", new String[]{tagTblNumber, thisTblNumber});
        db.close();
	}
	
	////��ѯfood����ȡ�ñ��е������ײ�,�ײ��Ƽ�����õ�
	@Override
	public List<Food> queryFoodgetAllIsTc() {
		Cursor cursor = null;
		List<Food> lists = new ArrayList<Food>();
        SQLiteDatabase db=getDB();
        try {
            if (db != null) {
                cursor = db.rawQuery("select ITCODE,GRPTYP,DES from food where ISTC = ?", new String[]{"1"});
                while (cursor.moveToNext()) {
                    Food tcFood = new Food();
                    tcFood.setPcode(cursor.getString(cursor.getColumnIndex("ITCODE")));
                    tcFood.setSortClass(cursor.getString(cursor.getColumnIndex("GRPTYP")));
                    tcFood.setPcname(cursor.getString(cursor.getColumnIndex("DES")));
                    lists.add(tcFood);
                }
            }
		} catch (Exception e) {
			CSLog.d(TAG,e.getMessage());
		}finally{
			if(cursor!=null){
				cursor.close();
			}
            if(db!=null){
                db.close();
            }
		}
		
		return lists;
	}

	@Override
	public List<List<List<Food>>> queryProducts_SubgetAllTc(List<Food> pList) {
		List<List<List<Food>>> superLists = new ArrayList<List<List<Food>>>();//��������һ�㼯��
        SQLiteDatabase db=getDB();
		for(Food food:pList){
			List<List<Food>> tcLists = new ArrayList<List<Food>>();//�ڶ��㼯��
			List<String> productorderLists = queryTcOrder(food);
			for(String orderStr:productorderLists){
				List<Food> thirdLists = new ArrayList<Food>();//�����㼯��
                if (db != null) {
                    Cursor c = db.rawQuery("select PCODE,PCODE1,PNAME,UNIT,PRICE1,defualtS,PRODUCTTC_ORDER,MINCNT,MAXCNT ,0 as RECOMMENDCNT from products_sub where PCODE = ? and PRODUCTTC_ORDER = ? order by PRODUCTTC_ORDER ", new String[]{food.getPcode(), orderStr});
                    while (c.moveToNext()) {
                        Food mFood = new Food();
                        mFood.setTpcode(c.getString(c.getColumnIndex("PCODE")));
                        mFood.setPcode(c.getString(c.getColumnIndex("PCODE1")));
                        mFood.setPcname(c.getString(c.getColumnIndex("PNAME")));
                        mFood.setDefalutS(c.getString(c.getColumnIndex("defualtS")));
                        mFood.setMinCnt(c.getString(c.getColumnIndex("MINCNT")));
                        mFood.setMaxCnt(c.getString(c.getColumnIndex("MAXCNT")));
                        mFood.setSelected(false);
                        mFood.setRecommendCnt(c.getString(c.getColumnIndex("RECOMMENDCNT")));

                        thirdLists.add(mFood);
                    }
                    tcLists.add(thirdLists);
                }
			}
			superLists.add(tcLists);
		}
        if(db!=null){
            db.close();
        }
		return superLists;
	}


    @Override
    public List<ChineseZS> queryChineseZS() {
        List<ChineseZS> chineseZSLists = new ArrayList<ChineseZS>();
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        ChineseZS chineseZS = null;
        try {
            cursor = db.rawQuery("select * from CODEDESC where CODE = ?", new String[]{"ZS"});
            while(cursor.moveToNext()){
                chineseZS = new ChineseZS();
                chineseZS.setCode(cursor.getString(cursor.getColumnIndex("CODE")));
                chineseZS.setDes(cursor.getString(cursor.getColumnIndex("DES")));
                chineseZS.setSno((cursor.getInt(cursor.getColumnIndex("SNO"))));
                chineseZSLists.add(chineseZS);
            }
        } catch (Exception e) {
            Log.d("", e.toString());
        }
        return chineseZSLists;
    }
    /**
     * ��ѯ�����еĲ�Ʒ��food���� ���Խ���ζ���õ�ר��
     */
    @Override
    public List<JNowFood> queryAllFoodForJNow() {
        List<JNowFood> jnowLists = new ArrayList<JNowFood>();
        SQLiteDatabase db = getDB();
        JNowFood jnowFood = null;
        Cursor cursor = null;//and PRICE != 79
        try {
            cursor = db.rawQuery("select PRICE,ITCODE,DES,UNIT, ITEM,CLASS ,0 as STATE from food where class not null and class !='' and PRICE != 0   order by ITEM", null);//and ITEM < 121
            while(cursor.moveToNext()){
                jnowFood = new JNowFood();
                jnowFood.setPrice(cursor.getInt(cursor.getColumnIndex("PRICE")));//��Ʒ�۸�
                jnowFood.setId(cursor.getString(cursor.getColumnIndex("ITCODE")));//��Ʒ����
                jnowFood.setName(cursor.getString(cursor.getColumnIndex("DES")));//��Ʒ����
                jnowFood.setUnit(cursor.getString(cursor.getColumnIndex("UNIT")));//��Ʒ��λ
                jnowFood.setSortClass(cursor.getInt(cursor.getColumnIndex("CLASS")));//��Ʒ���
                jnowFood.setState(cursor.getInt(cursor.getColumnIndex("STATE")));//��Ʒ״̬
                jnowLists.add(jnowFood);
            }
        } catch (Exception e) {
            Log.d("�Խ���ζ���õ�", e.toString());
        }finally{
            if(cursor!= null){
                cursor.close();
            }
        }
        return jnowLists;
    }

    /**
     * ��ѯ�����еĲ�Ʒ����class���У��Խ���ζ���õ�ר��
     */
    @Override
    public List<JGrptyp> queryAllGrptypForJNow() {
        List<JGrptyp> jGrpLists = new ArrayList<JGrptyp>();
        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        JGrptyp jGrptyp = null;
        try {
            cursor = db.rawQuery("select GRP,DES from class", null);
            while (cursor.moveToNext()) {
                jGrptyp = new JGrptyp();
                jGrptyp.setGrpTyp(cursor.getInt(cursor.getColumnIndex("GRP")));
                jGrptyp.setGrpDes(cursor.getString(cursor.getColumnIndex("DES")));
                jGrpLists.add(jGrptyp);
            }
        } catch (Exception e) {
            Log.d("�Խ���ζ���õ�", e.toString());
        }
        return jGrpLists;
    }


}