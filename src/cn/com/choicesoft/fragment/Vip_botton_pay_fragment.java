package cn.com.choicesoft.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.SettleAccountsActivity;
import cn.com.choicesoft.bean.CouponStoreBean;
import cn.com.choicesoft.bean.MoneyPay;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.util.CList;
import cn.com.choicesoft.util.FinalPay;
import cn.com.choicesoft.util.OnServerResponse;
import cn.com.choicesoft.util.Server;
import cn.com.choicesoft.util.SharedPreferencesUtils;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.util.VipRecordUtil;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

/**
 * ֧��button����
 * @Author:M.c
 * @CreateDate:2014-1-13
 * @Email:JNWSCZH@163.COM
 */
public class Vip_botton_pay_fragment extends Fragment implements View.OnClickListener{
	private LoadingDialog dialog = null;
	private Bundle bundle;
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	public LoadingDialog getDialog() {
		if(dialog==null){
			dialog = new LoadingDialogStyle(getActivity(), this.getString(R.string.please_wait));
			dialog.setCancelable(true);
		}
		return dialog;
	}
	public Server getServer(){
		Server server=new Server();
		return server;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.vip_botton_pay_fragment, null);
		view.findViewById(R.id.vip_jiSuanYE).setOnClickListener(this);//�������
		view.findViewById(R.id.vip_jiSuanJF).setOnClickListener(this);//�������
//		view.findViewById(R.id.vip_jiSuanXJ).setOnClickListener(this);//�����ֽ�
		view.findViewById(R.id.vip_cardXX).setOnClickListener(this);//����Ϣ
		view.findViewById(R.id.vip_ConfirmPayment).setOnClickListener(this);//ȷ��֧��
		return view;
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.vip_jiSuanYE://�������
			xfJs(1);
			break;
		case R.id.vip_jiSuanJF://�������
			xfJs(2);
			break;
//		case R.id.vip_jiSuanXJ://�����ֽ�
//			xfJs(3);
//			break;
		case R.id.vip_cardXX://����Ϣ
			backKey();
			break;
		case R.id.vip_ConfirmPayment://ȷ��֧��
			ConfirmPayment();
			break;
		case R.id.pay_ver_cancelBut://��������ȡ��
			backKey();
			break;
		}
	}
	
	/**
	 * ���ؿ���Ϣ��ȡ�������Ż�
	 */
	public void backCardInfo(){
		CList<Map<String,String>> params=new CList<Map<String,String>>();
		params.add("deviceId",SharedPreferencesUtils.getDeviceId(getActivity()));// cancleUserCounp��String deviceId, String userCode ,String tableNum ,String orderId )
		params.add("userCode",SharedPreferencesUtils.getUserCode(getActivity()));
		params.add("tableNum",getBundle().getBundle("BillBundle").getString("tableNum"));
		params.add("orderId",getBundle().getBundle("BillBundle").getString("orderId"));
		getServer().connect(getActivity(), "cancleUserCounp", "ChoiceWebService/services/HHTSocket?/cancleUserCounp", params, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				if(ValueUtil.isNotEmpty(result)){
					String [] res = result.split("@");
					if(res.length>=1){
						if("0".equals(res[0])){
							TsData.coupPay=null;
							TsData.member=null;
							TsData.moneyPay=null;
							backKey();
							Toast.makeText(getActivity(),R.string.success, Toast.LENGTH_LONG).show();
						}else{
                            Toast.makeText(getActivity(), res[1], Toast.LENGTH_LONG).show();
                        }
					}
				}
			}
			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
	}
	/**
	 * ȷ��֧������
	 */
	public void ConfirmPayment(){
		TextView ysjeTView=(TextView)getActivity().findViewById(R.id.vip_payMent_yingFText1);//Ӧ�����
		final EditText jfky=(EditText)getActivity().findViewById(R.id.vip_payment_jiFenKYYEEdit);//���ֿ���

//		final EditText xjxf=(EditText)getActivity().findViewById(R.id.vip_payment_xianJinXFEdit);//�ֽ�����
		final EditText yexf=(EditText)getActivity().findViewById(R.id.vip_payment_yeEXFEdit);//�������

		final EditText jfxf=(EditText)getActivity().findViewById(R.id.vip_payment_jiFenXFEdit);//��������
		setPricePoint(jfxf);
		final double recYE=Double.valueOf(isNan(yexf.getText().toString()));
		final double recJF=Double.valueOf(isNan(jfxf.getText().toString()));
//		final double recXJ=Double.valueOf(isNan(xjxf.getText().toString()));
		final double ysJE=Double.valueOf(isNan(ysjeTView.getText().toString()));
		if(recYE+recJF>ysJE){//�жϴ�ֵ����+���������Ƿ����Ӧ�ս�����������״�
			new AlertDialog.Builder(getActivity()).setTitle(R.string.error).setMessage(R.string.pay_money_error).setPositiveButton(R.string.confirm, null).show();
			return;
		}
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View view = layoutInflater.inflate(R.layout.pay_ver_layout, null);
	    final AlertDialog alert= new AlertDialog.Builder(getActivity()).setTitle(R.string.pwd_input).setView(view).show();
	    Button cancelBut=(Button)view.findViewById(R.id.pay_ver_cancelBut);
	    cancelBut.setOnClickListener(this);
	    Button okBut=(Button)view.findViewById(R.id.pay_ver_okBut);
	    final EditText pwd=(EditText)view.findViewById(R.id.pay_ver_PasEdit);
	    okBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*card_Sale(String deviceId, �豸���
				 * 			String userCode, ��¼���
				 * 			String cardNumber,��Ա����
				 * 			String cardStoredAmount,��ֵ���ѽ��
				 * 			String cardIntegralAmount,�������ѽ��
				 * 			String cardCashAmount,�ֽ����ѽ��
				 * 			String cardTicketInfoList,��������ȯ�б�
				 * 			String cardPWD,��ֵ����������  6 λ
				 * 			String orderId,�˵��� 
				 * 			String tableNum,̨λ��
				 *			QString integralOverall ��ǰ�������)
				 */
				final TextView vipNum=(TextView)getActivity().findViewById(R.id.vip_taiweiText);
				TextView orderId=(TextView)getActivity().findViewById(R.id.vip_orderText1);
				TextView tableNum=(TextView)getActivity().findViewById(R.id.vip_taiweiText1);
				CList<Map<String,String>> list=new CList<Map<String,String>>();
				list.add("deviceId", SharedPreferencesUtils.getDeviceId(getActivity()));//�豸���
				list.add("userCode", SharedPreferencesUtils.getUserCode(getActivity()));//��¼���
				list.add("cardNumber", SingleMenu.getMenuInstance().getCardNum());//��Ա����
				list.add("cardStoredAmount", isNan(yexf.getText().toString()));//��ֵ���ѽ��
				list.add("cardIntegralAmount", isNan(jfxf.getText().toString()));//�������ѽ��
//				list.add("cardCashAmount", isNan(xjxf.getText().toString()));//�ֽ����ѽ��
				list.add("cardTicketInfoList", "");//��������ȯ�б�
				list.add("cardPWD", pwd.getText().toString());//��ֵ����������  6 λ
				list.add("orderId",SingleMenu.getMenuInstance().getOrderNum());//�˵���
				list.add("tableNum", SingleMenu.getMenuInstance().getTableNum());//̨λ��
				list.add("integralOverall", isNan(jfky.getText().toString()));//��ǰ�������
				getServer().connect(getActivity(), "card_Sale", "ChoiceWebService/services/HHTSocket?/card_Sale", list, new OnServerResponse() {
					@Override
					public void onResponse(String result) {
						/*��Ա����@ 1
						�ն���ˮ�ţ����ѳ���ʱʹ �ã�@ 2
						ʵ�ʴ�ֵ���ѽ��@ 3
						�Żݴ�ֵ���ѽ��@ 4
						��ǰ��ֵ���@ 5
						��ǰ�������@---����Ҫ���� ������ʱ���أ� @  6
						��ӡ���� 7*/
						getDialog().dismiss();
						String[] str=ValueUtil.isEmpty(result)?null:result.split("@");
						if(str!=null&&str[0].equals("0")){
							//���ּ�¼
							// �ӿڲ����� ��Ա���š�ͨ�������ȡ��Ա����
							//--------------���Ѽ�¼-----------------------
							if(ValueUtil.isNaNofDouble(jfxf.getText().toString())>0){//�ж��Ƿ�ʹ�û�������
								if(ValueUtil.isEmpty(TsData.coupPay)){
									TsData.coupPay=new HashMap<String,CouponStoreBean>();
								}
								CouponStoreBean bean=TsData.coupPay.get("��Ա��������");
								if(ValueUtil.isEmpty(bean)){
									bean=new CouponStoreBean();
								}
								bean.setCouponName("��Ա��������");
								bean.setCouponMoney(jfxf.getText().toString());
								bean.setCouponNum(bean.getCouponNum()+1);
								TsData.coupPay.put("��Ա��������", bean);
								//��������
								if(ValueUtil.isEmpty(TsData.member)){
									TsData.member=new ArrayList<MoneyPay>();
								}
								MoneyPay pay=new MoneyPay();
								pay.setPayCardNumber(SingleMenu.getMenuInstance().getCardNum());
								pay.setPayIntegral(str[5].trim());
								pay.setPayTrace(str[1].trim());
								TsData.member.add(pay);

							}
							if(ValueUtil.isNaNofDouble(yexf.getText().toString())>0){//�ж��Ƿ��д�ֵ����
								if(ValueUtil.isEmpty(TsData.coupPay)){
									TsData.coupPay=new HashMap<String,CouponStoreBean>();
								}
								CouponStoreBean bean=TsData.coupPay.get("��Ա��ֵ����");
								if(ValueUtil.isEmpty(bean)){
									bean=new CouponStoreBean();
								}
								bean.setCouponName("��Ա��ֵ����");
								bean.setCouponMoney(yexf.getText().toString());
								bean.setCouponNum(1);
								TsData.coupPay.put("��Ա��ֵ����", bean);
							}
							Intent intent=new Intent(getActivity(),SettleAccountsActivity.class);
							intent.putExtras(getBundle().getBundle("BillBundle"));
							startActivity(intent);
							getActivity().finish();
							//TODO ��ʱ����Ҫ ��ֹ�ظ�
//							if(ValueUtil.isNaNofDouble(xjxf.getText().toString())>0){//�ж��Ƿ����ֽ�����
//								if(ValueUtil.isEmpty(TsData.moneyPay)){
//									TsData.moneyPay=new HashMap<String,MoneyPay>();
//								}
//								MoneyPay money=TsData.moneyPay.get("��Ա������");
//								if(ValueUtil.isEmpty(money)){
//									money=new MoneyPay();
//								}
//								money.setPayCardNumber(str[1]);
//								money.setPayMoney(money.getPayMoney()+ValueUtil.isNaNofDouble(xjxf.getText().toString()));
//								money.setPayNum(money.getPayNum()+1);
//								money.setPayName("��Ա������");
//								TsData.moneyPay.put("��Ա������", money);
//							}
							//------------------------------------
							Toast.makeText(getActivity(), R.string.pay_success, Toast.LENGTH_LONG).show();
							//�ж��Ƿ�֧�����
//							if(recXJ+recYE+recJF>=ysJE){
//								Double zhaoL=recXJ-(ysJE-(recYE+recJF));
//								new FinalPay().fPay(getBundle().getBundle("BillBundle"), getActivity(), zhaoL);
//							}
							TsData.isEnd=false;
							alert.dismiss();
							//-----------------���洦��-------------------
//							showResult(result,ysJE-(recXJ+recYE+recJF));
						}else if(str!=null&&!str[0].equals("0")){
							Toast.makeText(getActivity(), str[1], Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(getActivity(), R.string.net_error, Toast.LENGTH_LONG).show();
						}
					}
					
					@Override
					public void onBeforeRequest() {
						getDialog().show();
					}
				});
			}
		});
	}
	/**
	 * �������2λС��
	 * @param editText
	 */
	public static void setPricePoint(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					editText.setText(s);
					editText.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText(s.subSequence(0, 1));
						editText.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

	}
	/**
	 * ���½�����ʾ
	 * @param result
	 * @param yinS
	 */
	public void showResult(String result,Double yinS){
		String[] str=result.split("@");
//		��Ա����@ 0
//		�ն���ˮ�ţ����ѳ���ʱʹ �ã�@ 1
//		ʵ�ʴ�ֵ���ѽ��@ 2
//		�Żݴ�ֵ���ѽ��@ 3
//		��ǰ��ֵ���@ 4
//		��ǰ�������@---����Ҫ���� ������ʱ���أ� @  5
//		��ӡ���� 6
//		((EditText)getActivity().findViewById(R.id.vip_payment_xianJinXFEdit)).setText("0.0");//�ֽ�����
		((EditText)getActivity().findViewById(R.id.vip_payment_yeEXFEdit)).setText("0.0");//�������
		((EditText)getActivity().findViewById(R.id.vip_payment_jiFenXFEdit)).setText("0.0");//��������
		((EditText)getActivity().findViewById(R.id.vip_payment_jiFenKYYEEdit)).setText(str[5]);//���ֿ���
		((EditText)getActivity().findViewById(R.id.vip_payMent_chuZhiKYYEEdit)).setText(str[4]);//��ֵ�������
		((TextView)getActivity().findViewById(R.id.vip_payMent_yingFText1)).setText(String.valueOf(yinS>=0?ValueUtil.setScale(yinS, 2, BigDecimal.ROUND_DOWN):0));//Ӧ�����
		//TODO ������С��λ
		getBundle().putString("Payable", String.valueOf(yinS>=0?ValueUtil.setScale(yinS,2, BigDecimal.ROUND_DOWN):0));
		VipRecordUtil util=new VipRecordUtil();
		VipRecord vip=util.queryHandle(getActivity(), getBundle().getString("tableNum")).get(0);
		vip.setIntegralOverall(ValueUtil.isNaNofDouble(str[5]));
		vip.setPayable(yinS>=0?Double.valueOf(ValueUtil.setScale(yinS, 2, BigDecimal.ROUND_DOWN)):0);
		vip.setStoredCardsBalance(ValueUtil.isNaNofDouble(str[4]));
		util.insertHandle(getActivity(), vip);
	}
	/**
	 * �з� val�Ƿ�Ϊ����������򷵻�val��������򷵻�0
	 * @param val
	 * @return
	 */
	public String isNan(String val){
		return ValueUtil.isNaN(val)?val:"0";
	}
	/**
	 * �������Back��
	 */
	public void backKey(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				getActivity().getFragmentManager().beginTransaction();
				Instrumentation inst = new Instrumentation(); 
				inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);  
			}
		}).start();
	}
	/**
	 * ���Ѽ��㷽��
	 * @param key
	 */
	public void xfJs(int key){
		TextView ssje=(TextView)getActivity().findViewById(R.id.vip_payMent_yingFText1);//Ӧ�����
		EditText czky=(EditText)getActivity().findViewById(R.id.vip_payMent_chuZhiKYYEEdit);//��ֵ����
		EditText jfky=(EditText)getActivity().findViewById(R.id.vip_payment_jiFenKYYEEdit);//���ֿ���
//		EditText xjxf=(EditText)getActivity().findViewById(R.id.vip_payment_xianJinXFEdit);//�ֽ�����
		EditText yexf=(EditText)getActivity().findViewById(R.id.vip_payment_yeEXFEdit);//�������
		EditText jfxf=(EditText)getActivity().findViewById(R.id.vip_payment_jiFenXFEdit);//��������
		double yingSJE=0;//Ӧ�ս��
		double chuZhiKY=0;//��ֵ����
		double jiFenKY=0;//���ֿ���
		double xianJinXF=0;//�ֽ�����
		double yuEXF=0;//�������
		double jiFenXF=0;//��������
		if(ValueUtil.isNaN(ssje.getText().toString())){//�ж�Ӧ�ս��
			yingSJE=Double.valueOf(ssje.getText().toString());
		}
		if(ValueUtil.isNaN(czky.getTag().toString())){//�жϴ�ֵ���ý��
			chuZhiKY=Double.valueOf(czky.getTag().toString());
		}
		if(ValueUtil.isNaN(jfky.getTag().toString())){//�жϻ��ֿ��ý��
			jiFenKY=Double.valueOf(jfky.getTag().toString());
		}
//		if(ValueUtil.isNaN(xjxf.getText().toString())){//�ж��ֽ�����
//			xianJinXF=Double.valueOf(xjxf.getText().toString());
//		}
		if(ValueUtil.isNaN(yexf.getText().toString())){//�ж��������
			yuEXF=Double.valueOf(yexf.getText().toString());
		}
		if(ValueUtil.isNaN(jfxf.getText().toString())){//�жϻ�������
			jiFenXF=Double.valueOf(jfxf.getText().toString());
		}
		if(yuEXF+jiFenXF>yingSJE){
			yexf.setText("0");//��ֵ����
			jfxf.setText("0");//���ֿ���
			Toast.makeText(getActivity(), R.string.pay_money_error2, Toast.LENGTH_LONG).show();
			return;
		}
		switch (key) {
		case 1: //�������
			double chuZYF=0;
			if(xianJinXF+jiFenXF<yingSJE){
				chuZYF=yingSJE-(xianJinXF+jiFenXF);
			}
			if(chuZhiKY>=chuZYF){
				yexf.setText(chuZYF+"");//��ֵ����
			}else{
				yexf.setText(chuZhiKY+"");//��ֵ����
			}
			break;
		case 2://�������
			double jiFYF=0;
			if(yuEXF+xianJinXF<yingSJE){
				jiFYF=yingSJE-(yuEXF+xianJinXF);
			}
			if(jiFenKY>=jiFYF){
				jfxf.setText(jiFYF+"");//��������
			}else{
				jfxf.setText(jiFenKY+"");//��������
			}
			break;
		case 3://�����ֽ�
			double xianJYF=yingSJE-(yuEXF+jiFenXF);
//			xjxf.setText(xianJYF+"");
			break;
		}
		
	}
}
