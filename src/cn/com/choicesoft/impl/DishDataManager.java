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
 * ��Ʒ���ݹ����ӿ�
 */
public interface DishDataManager {
	/**
	 * �޸Ļ���
	 * @author M.c
	 */
	void updateDishesByOver(String table, String over, String pcode, String pkid);
	/**
	 * �޸�ȫ������
	 * @author M.c
	 */
	void updateDishesByOver(String table, int state);
	
	List<Food> getAllFoodListByTablenum(String tableNum, String orderID);//ȫ��ҳ�棬��AllCheck������в�Ʒ,������̨λ�ţ��˵���,send=1
	
	void updateFoodOverItem(Food food);//����  Update over
	
	void updateFoodUrgeItem(Food food);//�߲�  Update urge
	
	List<Grptyp> getAllDishClassList();//�ڵ��ҳ�棬��ѯ����Ʒ����
	
	List<Addition> getAllFujiaListByPcode(String pcode);//�õ����и�����ǵ�Ʒ������,���ݲ�Ʒ�����ѯ���ò�Ʒ�����Ӧ�����и�����
	
	List<Addition> getAllFujiaListNoPcode();//�õ����и�����ǵ�Ʒ������,������û�в�Ʒ��������
	
	List<Food> getAllFoodList();//��food������в�Ʒ
	
	List<String> queryTcOrder(Food f);//��products_sub���producttc_order ��һ��
	
	List<List<Food>> queryTcItemLists(Food food);//��products_sub������ײͶ�Ӧ��������ϸ   �ڶ���
	
	List<CurrentUnit> queryCurUnitLists();//��food����ڶ���λ����  SELECT itcode,des,unitcur FROM food WHERE UNITCUR = 2
	
	long insertAllCheckSendFinish(SQLiteDatabase db, Food pFood);//���Ͳ�Ʒ�ɹ���,���浽���ݿ�
	
	List<Food> queryAllCheckIfCache(String tableNum, String orderID);//����˺�û�з��ͣ��ݴ浽���ݿ⣬����̨λ�ţ��˵��ţ�send=0����ѯ
	
	void deleteDishListsByTblnum(String tableNum);//��̨��ʱ������û�ɫ̨λ����Ӧ��AllCheck���������ݣ���ɾ��
	
	void clearAllCheckByTabNum(String tableNum);//��̨��ʱ�򣬸���̨λ�Ž�ԭ�е�̨λ���������
	
	List<PresentReason> queryPresentReason();//��ѯ������ԭ��
	
	List<CommonFujia> queryCommonFujia();//��ѯ������������
	
	
	void deleteDataIfSaved(String tblNumber, String orderId);//���ѵ��ҳ�棬����Ѿ��ݴ���ˣ����ݴ��ɾ��ԭ���� ,ɾ��send=0
	
	boolean circleInsertAllCheck(List<Food> lists);//�����������е����ݱ��浽���ݿ⣬ѭ������,����ɹ�������true
	
	void updateOrderIdAfterBingtaiSuccess(String thisTblNumber, String tagTblNumber, String newOrderId);//��̨�ɹ����޸��˵���
	
	List<Food> queryFoodgetAllIsTc();//��ѯfood����ȡ�ñ��е������ײ�
	
	List<List<List<Food>>> queryProducts_SubgetAllTc(List<Food> pList);

    List<ChineseZS> queryChineseZS();
	List<JNowFood> queryAllFoodForJNow();//��ѯ�����еĲ�Ʒ��food���� ���Խ���ζ���õ�ר��

	List<JGrptyp> queryAllGrptypForJNow();//��ѯ�����еĲ�Ʒ����class���У��Խ���ζ���õ�ר��

}