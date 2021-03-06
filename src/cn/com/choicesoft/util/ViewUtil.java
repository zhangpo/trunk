package cn.com.choicesoft.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.MoneyPay;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;

/**
 * 在R.id.bill_sum上动态添加信息
 * @Author:M.c
 * @CreateDate:2014-1-18
 * @Email:JNWSCZH@163.COM
 */
public class ViewUtil {

	/**
	 * 动态添加 信息到界面
	 * @param activity	
	 * @param name	
	 * @param money	
	 * @param isCoup	//是否为优惠券
	 */
	public static void setVew(Activity activity,String name,String money,boolean isCoup,IResult<String> result){
		DecimalFormat df=new DecimalFormat("0.00");
		TextView heJJE=(TextView)activity.findViewById(R.id.heJJE_Text2);
		TextView yingFJE=(TextView)activity.findViewById(R.id.yingFJE_Text2);
		TextView moL=(TextView)activity.findViewById(R.id.moLJE_Text2);
		BigDecimal heJ=new BigDecimal(ValueUtil.isNaNofDouble(yingFJE.getText().toString()));
		BigDecimal youH=new BigDecimal(ValueUtil.isNaNofDouble(money));//优惠/支付金额
		BigDecimal yingF=new BigDecimal(0.0);
		BigDecimal moLJE=new BigDecimal(ValueUtil.isNaNofDouble(moL.getText().toString()));
		//计算优惠后合计金额
//		BigDecimal heJText=youH.compareTo(heJ)>0?new BigDecimal(0.0):heJ.subtract(youH);
		yingF=heJ;//抹零前金额
//		int scale=ValueUtil.isNaNofInteger(SharedPreferencesUtils.getMoL(activity));//登录对时候获取抹零标示
		//判断是否需要抹零
//		yingF=heJText.setScale(scale, BigDecimal.ROUND_DOWN);
//		moLJE=heJText.subtract(yingF);//计算抹零金额
		
//		heJJE.setText(df.format(heJText));	//修改合计
//		yingFJE.setText(df.format(yingF));	//修改应收
//		moL.setText(df.format(moLJE));			//修改抹零
		if(isCoup){//是否为优惠券
			if(yingF.compareTo(BigDecimal.valueOf(0))<=0){
				new FinalPay().fPay(activity.getIntent().getExtras(), activity, 0.0);
			}
		}else{
			if(yingF.compareTo(youH)<=0){
				// 判断是否需要抹零
//				yingF=heJ.setScale(scale, BigDecimal.ROUND_DOWN);
				if(ValueUtil.isNotEmpty(TsData.moneyPay)){//判断支付的现金是否大于找零
					BigDecimal allMoney=new BigDecimal(0);
					for(Map.Entry<String,MoneyPay> map:TsData.moneyPay.entrySet()){//循环计算
						allMoney=allMoney.add(BigDecimal.valueOf(map.getValue().getPayMoney()));
					}
					if(allMoney.compareTo(youH.subtract(yingF))>0){//判断大小
						((TextView)activity.findViewById(R.id.zhaoLText2)).setText(df.format(youH.subtract(yingF)));
					}
				}
				if(result!=null){
					result.result(df.format(youH.subtract(yingF)));
				}
			}
		}
		//-----------------修改应收金额-上----------
		View view=new View(activity);//创建分割线
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,1));
		view.setBackgroundColor(Color.BLACK);
		///////////////////////上：分割线--下：布局////////////////
		LinearLayout sumLayout=(LinearLayout)activity.findViewById(R.id.bill_sum);//显示金额数据
		//////////////////////////获取 Layout//////////////////////////
		LinearLayout textLayout=new LinearLayout(activity);//创建LinearLayout
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
		params.weight=1;
		textLayout.setGravity(Gravity.LEFT|Gravity.RIGHT);
		//------------------------------------------------
		TextView text=new TextView(activity);
		text.setGravity(Gravity.LEFT);
		text.setTextColor(Color.BLACK);
		text.setText(name+"：");
		TextView text1=new TextView(activity);
		text1.setGravity(Gravity.RIGHT);
		text1.setTextColor(Color.BLACK);
		text1.setText(df.format(youH));
		textLayout.addView(text,params);
		textLayout.addView(text1,params);
		sumLayout.addView(textLayout, sumLayout.getChildCount()-3);
		sumLayout.addView(view, 5);
	}
}
