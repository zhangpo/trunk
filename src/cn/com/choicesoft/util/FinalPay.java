package cn.com.choicesoft.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.bean.MoneyPay;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

/**
 * �����ύ��
 * @Author:M.c
 * @CreateDate:2014-1-21
 * @Email:JNWSCZH@163.COM
 */
public class FinalPay {
	private LoadingDialog dialog = null;
	public LoadingDialog getDialog(Activity activity) {
		if(dialog==null){
			dialog = new LoadingDialogStyle(activity, activity.getString(R.string.please_wait));
			dialog.setCancelable(true);
		}
		return dialog;
	}
	/**
	 * ����֧��
	 * @param ble ̨λ�������Ȳ���Bundle
	 * @param activity 
	 * @param zhaoL ������
	 */
	public void fPay(final Bundle ble,final Activity activity,final Double zhaoL){
		//TODO ͨ�����ݿ��ȡ vip ��Ϣ
		List<VipRecord> vipList=new VipRecordUtil().queryHandle(activity, ble.getString("tableNum"));
		VipRecord vip=ValueUtil.isNotEmpty(vipList)?vipList.get(0):null;
		CList<Map<String,String>> clist=new CList<Map<String,String>>();
		clist.add("deviceId", SharedPreferencesUtils.getDeviceId(activity));
		clist.add("userCode", SharedPreferencesUtils.getUserCode(activity));
		clist.add("tableNum", ble.getString("tableNum"));
		clist.add("orderId", ble.getString("orderId"));
		StringBuffer payMentId=new StringBuffer(), payMentCnt=new StringBuffer(),PayMentMoney=new StringBuffer(),payFinish=new StringBuffer();
		if(ValueUtil.isNotEmpty(TsData.moneyPay)){
			for (Map.Entry<String, MoneyPay> map : TsData.moneyPay.entrySet()) {
				MoneyPay money=map.getValue();
				if(ValueUtil.isEmpty(money.getPayId())){//�ж��ֽ��Ƿ�Ϊ��
					break;
				}
				payMentId.append(money.getPayId()+"!");
				payMentCnt.append(money.getPayNum()+"!");
				PayMentMoney.append(money.getPayMoney()+"!");
				payFinish.append("0!");
			}
		}
		Integer index=null;//�ж��Ƿ�ʹ�û�Ա֧��
		if(ValueUtil.isNotEmpty(TsData.member)){
			index=TsData.member.size()-1;
		}
		clist.add("paymentId", payMentId.length()>0?payMentId.toString():"0");
		clist.add("paymentCnt", payMentCnt.length()>0?payMentCnt.toString():"0");
		clist.add("mpaymentMoney", PayMentMoney.length()>0?PayMentMoney.toString():"0");
		clist.add("payFinish", "1");
		clist.add("integralOverall", index==null?"":TsData.member.get(index).getPayIntegral());
		clist.add("cardNumber",ValueUtil.isNotEmpty(vip)?vip.getCardNumber():"");
		new Server().connect(activity, "userPayment", "ChoiceWebService/services/HHTSocket?/userPayment", clist, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog(activity).dismiss();
			//	�˵��ţ�֧����ʽ���룬������ ֧����� 
				final String[] str=ValueUtil.isEmpty(result)?null:result.split("@");
				if(str!=null&&str[0].equals("0")){//�ж��Ƿ�֧���ɹ�
					String mes=activity.getString(R.string.pay_success);
					if(zhaoL>0){//�ж��Ƿ���Ҫ����
						mes=activity.getString(R.string.pay_success_zhaoL)+zhaoL+activity.getString(R.string.money_unit);
					}
					TsData.isEnd=true;
					new VipRecordUtil().delHandle(activity, ble.getString("tableNum"));//ɾ�����ܸ�̨λ����
					new AlertDialog.Builder(activity)//֧�������ʾ
					.setTitle(R.string.pay_success)
					.setMessage(mes)
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							if(ValueUtil.isEmpty(TsData.coupPay)&&ValueUtil.isEmpty(TsData.member)&&TsData.moneyPay!=null){//�ж��Ƿ���Ҫ����Ʊ
								invoice(activity,ble.getString("orderId"),zhaoL);//ִ�п���Ʊ
							}else{
								backTable(activity);//��������
							}
						}
					}).show();
				}else if(str!=null&&!str[0].equals("0")){
					Toast.makeText(activity, str[1], Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(activity,R.string.net_error, Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onBeforeRequest() {
				getDialog(activity).show();
			}
		});
	}
	/**
	 * �Ƿ��ӡ��Ʊ
	 */
	public void invoice(final Activity activity,final String orderId,final Double zhaoL){
		new AlertDialog.Builder(activity).setTitle(R.string.hint)
		.setMessage(R.string.whether_the_invoice).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				String invoiceFace(String deviceId, String orderId, String invoiceMoney)
//				���������
//				deviceId   �豸���
//				orderId    ����
//				invoiceMoney  ����Ʊ���
				BigDecimal big=new BigDecimal(0.0);
				for(Map.Entry<String,MoneyPay> map:TsData.moneyPay.entrySet()){//ѭ�������ֽ���
					big=big.add(BigDecimal.valueOf(map.getValue().getPayMoney()));
				}
				big=big.subtract(BigDecimal.valueOf(zhaoL));
				CList<Map<String,String>> data=new CList<Map<String,String>>();
				data.add("deviceId",SharedPreferencesUtils.getDeviceId(activity));
				data.add("orderId",orderId);
				data.add("invoiceMoney",big.toString());
				new Server().connect(activity, "invoiceFace", "ChoiceWebService/services/HHTSocket?/invoiceFace", data, new OnServerResponse() {
					@Override
					public void onResponse(String result) {
						getDialog(activity).dismiss();
						String[] res=ValueUtil.isEmpty(result)?null:result.split("@");
						if(res!=null&&res[0].equals("0")){
							Toast.makeText(activity, activity.getString(R.string.invoice)+res[1], Toast.LENGTH_LONG).show();
						}else if(res!=null&&!res[0].equals("0")){
							Toast.makeText(activity, activity.getString(R.string.invoice)+res[1], Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(activity, R.string.net_error, Toast.LENGTH_LONG).show();
						}
						backTable(activity);
					}
					
					@Override
					public void onBeforeRequest() {
						getDialog(activity).show();
					}
				});
			}
		}).setNegativeButton(R.string.cancle,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				backTable(activity);
			}
		}).show();
	}
	/**
	 * ���ط���
	 * @param activity
	 */
	public void backTable(Activity activity){
		Intent intent=new Intent(activity,MainActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}
}