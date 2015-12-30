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
 * ��R.id.bill_sum�϶�̬������Ϣ
 * @Author:M.c
 * @CreateDate:2014-1-18
 * @Email:JNWSCZH@163.COM
 */
public class ViewUtil {

	/**
	 * ��̬���� ��Ϣ������
	 * @param activity	
	 * @param name	
	 * @param money	
	 * @param isCoup	//�Ƿ�Ϊ�Ż�ȯ
	 */
	public static void setVew(Activity activity,String name,String money,boolean isCoup,IResult<String> result){
		DecimalFormat df=new DecimalFormat("0.00");
		TextView heJJE=(TextView)activity.findViewById(R.id.heJJE_Text2);
		TextView yingFJE=(TextView)activity.findViewById(R.id.yingFJE_Text2);
		TextView moL=(TextView)activity.findViewById(R.id.moLJE_Text2);
		BigDecimal heJ=new BigDecimal(ValueUtil.isNaNofDouble(yingFJE.getText().toString()));
		BigDecimal youH=new BigDecimal(ValueUtil.isNaNofDouble(money));//�Ż�/֧�����
		BigDecimal yingF=new BigDecimal(0.0);
		BigDecimal moLJE=new BigDecimal(ValueUtil.isNaNofDouble(moL.getText().toString()));
		//�����Żݺ�ϼƽ��
//		BigDecimal heJText=youH.compareTo(heJ)>0?new BigDecimal(0.0):heJ.subtract(youH);
		yingF=heJ;//Ĩ��ǰ���
//		int scale=ValueUtil.isNaNofInteger(SharedPreferencesUtils.getMoL(activity));//��¼��ʱ���ȡĨ���ʾ
		//�ж��Ƿ���ҪĨ��
//		yingF=heJText.setScale(scale, BigDecimal.ROUND_DOWN);
//		moLJE=heJText.subtract(yingF);//����Ĩ����
		
//		heJJE.setText(df.format(heJText));	//�޸ĺϼ�
//		yingFJE.setText(df.format(yingF));	//�޸�Ӧ��
//		moL.setText(df.format(moLJE));			//�޸�Ĩ��
		if(isCoup){//�Ƿ�Ϊ�Ż�ȯ
			if(yingF.compareTo(BigDecimal.valueOf(0))<=0){
				new FinalPay().fPay(activity.getIntent().getExtras(), activity, 0.0);
			}
		}else{
			if(yingF.compareTo(youH)<=0){
				// �ж��Ƿ���ҪĨ��
//				yingF=heJ.setScale(scale, BigDecimal.ROUND_DOWN);
				if(ValueUtil.isNotEmpty(TsData.moneyPay)){//�ж�֧�����ֽ��Ƿ��������
					BigDecimal allMoney=new BigDecimal(0);
					for(Map.Entry<String,MoneyPay> map:TsData.moneyPay.entrySet()){//ѭ������
						allMoney=allMoney.add(BigDecimal.valueOf(map.getValue().getPayMoney()));
					}
					if(allMoney.compareTo(youH.subtract(yingF))>0){//�жϴ�С
						((TextView)activity.findViewById(R.id.zhaoLText2)).setText(df.format(youH.subtract(yingF)));
					}
				}
				if(result!=null){
					result.result(df.format(youH.subtract(yingF)));
				}
			}
		}
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
		sumLayout.addView(textLayout, sumLayout.getChildCount()-3);
		sumLayout.addView(view, 5);
	}
}