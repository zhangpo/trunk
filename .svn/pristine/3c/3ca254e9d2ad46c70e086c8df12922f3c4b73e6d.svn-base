package cn.com.choicesoft.chinese.util;

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
import cn.com.choicesoft.util.FinalPay;
import cn.com.choicesoft.util.SharedPreferencesUtils;
import cn.com.choicesoft.util.ValueUtil;

/**
 * 在R.id.bill_sum上动态添加信息
 * @Author:M.c
 * @CreateDate:2014-1-18
 * @Email:JNWSCZH@163.COM
 */
public class ChineseViewUtil {

	/**
	 * 动态添加 信息到界面
	 * @param activity	
	 * @param name	
	 * @param money	
	 */
	public static void setVew(Activity activity,String name,String money){
		DecimalFormat df=new DecimalFormat("0.00");

		BigDecimal youH=new BigDecimal(ValueUtil.isNaNofDouble(money));//优惠/支付金额
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
		sumLayout.addView(textLayout, 8);
		sumLayout.addView(view, 9);
	}
}
