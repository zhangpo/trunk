package cn.com.choicesoft.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.fragment.VipFragment;
import cn.com.choicesoft.fragment.Vip_botton_CO_Fragment;
import cn.com.choicesoft.util.CList;
import cn.com.choicesoft.util.ExitApplication;
import cn.com.choicesoft.util.OnServerResponse;
import cn.com.choicesoft.util.Server;
import cn.com.choicesoft.util.SharedPreferencesUtils;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.util.VipRecordUtil;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;
import cn.com.choicesoft.view.VipMsg;

/**
 *@Author:M.c
 *@Comments:��Ա֧������
 *@CreateDate:2014-2-27
 */
public class PayMentActivity extends Activity {
	private FragmentManager manager=null;
	private FragmentTransaction transaction=null;
	private LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		Bundle bundle=this.getIntent().getExtras();
		Bundle theBun=bundle.getBundle("BillBundle");
		manager=this.getFragmentManager();
		transaction=manager.beginTransaction();
		Vip_botton_CO_Fragment but_co=new Vip_botton_CO_Fragment();//֧����ť fragment
		but_co.setBundle(bundle);
		transaction.add(R.id.pay_Ment_TopLayout, new VipFragment(),"VipFragment");//��Ա��ȷ��Fragment
		transaction.add(R.id.pay_Ment_bottonLayout, but_co,"vip_botton_CO_Fragment");
		transaction.commit();
		setContentView(R.layout.activity_pay_ment);
		Button btn=(Button)this.findViewById(R.id.eatable_btn_back);
		btn.setVisibility(View.GONE);
		ImageView image = (ImageView)this.findViewById(R.id.orderinfo_ImageView);
		image.setVisibility(View.GONE);

	}
	public void initData(){
		boolean mark=true;
		final CList<Map<String,String>> cl=new CList<Map<String,String>>();
		cl.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
		cl.add("userCode", SharedPreferencesUtils.getUserCode(this));
		cl.add("cardNumber",SingleMenu.getMenuInstance().getCardNum());
		cl.add("orderId",SingleMenu.getMenuInstance().getOrderNum());
		new Server().connect(this, "card_QueryBalance", "ChoiceWebService/services/HHTSocket?/card_QueryBalance", cl, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				String[] str=result==null?null:result.split("@");
				if(ValueUtil.isNotEmpty(str)&&str[0].equals("0")){
					String payable=PayMentActivity.this.getIntent().getStringExtra("Payable");
					//0@ CardNumber  @ CardType@ StoredCardsBalance@ IntegralOverall@ CouponsOverall@ CouponsAvail@ TicketInfoList
					//��Ա����        ��Ա������          ��ֵ���           �������          ȯ��� ȯ       �������             ȯ��Ϣ�б�
					VipRecord vip=new VipRecord();//VipRecordUtil
					vip.setCardNumber(str[1]);			//��Ա����
					vip.setCardType(str[2]);  			//��Ա������
					vip.setStoredCardsBalance(ValueUtil.isNaNofDouble(str[3]));	//��ֵ���
					vip.setIntegralOverall(ValueUtil.isNaNofDouble(str[4]));   	//�������
					vip.setCouponsOverall(ValueUtil.isNaNofDouble(str[5]));		//ȯ��� ȯ
					vip.setCouponsAvail(ValueUtil.isNaNofDouble(str[6]));		//�������
					vip.setOrderId(SingleMenu.getMenuInstance().getOrderNum());
					vip.setPhone(SingleMenu.getMenuInstance().getCardPhone());
					vip.setTableNum(SingleMenu.getMenuInstance().getTableNum());
					vip.setManCounts(ValueUtil.isNaNofInteger(SingleMenu.getMenuInstance().getManCounts()));
					vip.setWomanCounts(ValueUtil.isNaNofInteger(SingleMenu.getMenuInstance().getWomanCounts()));
					vip.setPayable(ValueUtil.isNaNofDouble(payable));
					vip.setTicketInfoList("");
					if(str.length>7){//�ж��ٶ��Ƿ����7������7֤�����С�ȯ��Ϣ�б�
						vip.setTicketInfoList(str[7]);
					}
				}else{
					showToast(R.string.skip_error);
				}
			}
			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
	}
	/**
	 * ��Toast
	 * @param id
	 */
	public void showToast(int id){
		Toast.makeText(this, id, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onResume() {

		super.onResume();
	}
	public LoadingDialog getDialog() {
		if(dialog==null){
			dialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
			dialog.setCancelable(true);
		}
		return dialog;
	}
	/**
	 * ���·����¼�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0&&TsData.isEnd) {
			Intent intent=new Intent(this,SettleAccountsActivity.class);
			intent.putExtras(this.getIntent().getExtras().getBundle("BillBundle"));
			startActivity(intent);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
