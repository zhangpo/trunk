package cn.com.choicesoft.impl;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import cn.com.choicesoft.bean.Addition;
import cn.com.choicesoft.bean.CommonFujia;
import cn.com.choicesoft.bean.CurrentUnit;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.bean.Grptyp;
import cn.com.choicesoft.bean.PresentReason;
import cn.com.choicesoft.chinese.bean.ChineseZS;
import cn.com.choicesoft.chinese.bean.JGrptyp;
import cn.com.choicesoft.chinese.bean.JNowFood;

/**
 * 菜品数据管理接口
 */
public interface DishDataManager {
	/**
	 * 修改划菜
	 * @author M.c
	 */
	void updateDishesByOver(String table, String over, String pcode, String pkid);
	/**
	 * 修改全单划菜
	 * @author M.c
	 */
	void updateDishesByOver(String table, int state);
	
	List<Food> getAllFoodListByTablenum(String tableNum, String orderID);//全单页面，从AllCheck查出所有菜品,参数：台位号，账单号,send=1
	
	void updateFoodOverItem(Food food);//划菜  Update over
	
	void updateFoodUrgeItem(Food food);//催菜  Update urge
	
	List<Grptyp> getAllDishClassList();//在点菜页面，查询出菜品大类
	
	List<Addition> getAllFujiaListByPcode(String pcode);//得到所有附加项，是单品附加项,根据菜品编码查询出该菜品编码对应的所有附加项
	
	List<Addition> getAllFujiaListNoPcode();//得到所有附加项，是单品附加项,适用于没有菜品编码的情况
	
	List<Food> getAllFoodList();//从food查出所有菜品
	
	List<String> queryTcOrder(Food f);//从products_sub查出producttc_order 第一步
	
	List<List<Food>> queryTcItemLists(Food food);//从products_sub查出该套餐对应的所有明细   第二步
	
	List<CurrentUnit> queryCurUnitLists();//从food查出第二单位集合  SELECT itcode,des,unitcur FROM food WHERE UNITCUR = 2
	
	long insertAllCheckSendFinish(SQLiteDatabase db, Food pFood);//发送菜品成功后,保存到数据库
	
	List<Food> queryAllCheckIfCache(String tableNum, String orderID);//点完菜后，没有发送，暂存到数据库，根据台位号，账单号，send=0来查询
	
	void deleteDishListsByTblnum(String tableNum);//清台的时候，如果该黄色台位所对应的AllCheck表中有数据，就删除
	
	void clearAllCheckByTabNum(String tableNum);//开台的时候，根据台位号将原有的台位号数据清空
	
	List<PresentReason> queryPresentReason();//查询出赠菜原因
	
	List<CommonFujia> queryCommonFujia();//查询出公共附加项
	
	
	void deleteDataIfSaved(String tblNumber, String orderId);//在已点菜页面，如果已经暂存过了，再暂存就删除原来的 ,删除send=0
	
	boolean circleInsertAllCheck(List<Food> lists);//将单例集合中的数据保存到数据库，循环插入,插入成功，返回true
	
	void updateOrderIdAfterBingtaiSuccess(String thisTblNumber, String tagTblNumber, String newOrderId);//并台成功后修改账单号
	
	List<Food> queryFoodgetAllIsTc();//查询food表获取该表中的所有套餐
	
	List<List<List<Food>>> queryProducts_SubgetAllTc(List<Food> pList);

    List<ChineseZS> queryChineseZS();
	List<JNowFood> queryAllFoodForJNow();//查询出所有的菜品从food表中 ，对接美味不用等专用

	List<JGrptyp> queryAllGrptypForJNow();//查询出所有的菜品类别从class表中，对接美味不用等专用

}
