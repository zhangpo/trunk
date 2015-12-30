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
 * ��R.id.bill_sum�϶�̬�����Ϣ
 * @Author:M.c
 * @CreateDate:2014-1-18
 * @Email:JNWSCZH@163.COM
 */
public class ChineseViewUtil {

	/**
	 * ��̬��� ��Ϣ������
	 * @param activity	
	 * @param name	
	 * @param money	
	 */
	public static void setVew(Activity activity,String name,String money){
		DecimalFormat df=new DecimalFormat("0.00");

		BigDecimal youH=new BigDecimal(ValueUtil.isNaNofDouble(money));//�Ż�/֧�����
		//-----------------�޸�Ӧ�ս��-��----------
		View view=new View(activity);//�����ָ���
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,1));
		view.setBackgroundColor(Color.BLACK);
		///////////////////////�ϣ��ָ���--�£�����////////////////
		LinearLayout sumLayout=(LinearLayout)activity.findViewById(R.id.bill_sum);//��ʾ�������
		//////////////////////////��ȡ Layout//////////////////////////
		LinearLayout textLayout=new LinearLayout(activity);//����LinearLayout
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
		params.weight=1;
		textLayout.setGravity(Gravity.LEFT|Gravity.RIGHT);
		//------------------------------------------------
		TextView text=new TextView(activity);
		text.setGravity(Gravity.LEFT);
		text.setTextColor(Color.BLACK);
		text.setText(name+"��");
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
