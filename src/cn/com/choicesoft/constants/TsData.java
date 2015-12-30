package cn.com.choicesoft.constants;

import java.util.List;
import java.util.Map;

import cn.com.choicesoft.bean.CouponStoreBean;
import cn.com.choicesoft.bean.MoneyPay;
import cn.com.choicesoft.util.ValueUtil;

/**
 * 暂存数据处理
 * @Author:M.c
 * @CreateDate:2014-1-18
 * @Email:JNWSCZH@163.COM
 */
public class TsData {
	/**
	 * 快餐现金记录
	 */
	public static Map<String,MoneyPay> moneyPay;
	/**
	 * 判断是否预定台位跳转过来的
	 */
	public static boolean isReserve=false;
	/**
	 * 优惠记录
	 */
	public static Map<String,CouponStoreBean> coupPay;
	/**
	 * 积分余额
	 */
	public static List<MoneyPay> member;
	/**
	 * 判断是否结束交易
	 */
	public static boolean isEnd=false;

    public static boolean isSave=false;
	
	/**
	 * 暂存 记录 初始化
	 */
	public static void initData(){
		moneyPay=null;
		coupPay=null;
		member=null;
		isEnd=false;
	}
	public static boolean isNotNull(){
		return ValueUtil.isNotEmpty(moneyPay) || ValueUtil.isNotEmpty(coupPay) || ValueUtil.isNotEmpty(member);
	}
}
