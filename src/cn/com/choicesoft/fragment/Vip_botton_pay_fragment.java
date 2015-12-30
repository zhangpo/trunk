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
 * 支付button界面
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
		view.findViewById(R.id.vip_jiSuanYE).setOnClickListener(this);//计算余额
		view.findViewById(R.id.vip_jiSuanJF).setOnClickListener(this);//计算积分
//		view.findViewById(R.id.vip_jiSuanXJ).setOnClickListener(this);//计算现金
		view.findViewById(R.id.vip_cardXX).setOnClickListener(this);//卡信息
		view.findViewById(R.id.vip_ConfirmPayment).setOnClickListener(this);//确认支付
		return view;
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.vip_jiSuanYE://计算余额
			xfJs(1);
			break;
		case R.id.vip_jiSuanJF://计算积分
			xfJs(2);
			break;
//		case R.id.vip_jiSuanXJ://计算现金
//			xfJs(3);
//			break;
		case R.id.vip_cardXX://卡信息
			backKey();
			break;
		case R.id.vip_ConfirmPayment://确认支付
			ConfirmPayment();
			break;
		case R.id.pay_ver_cancelBut://输入密码取消
			backKey();
			break;
		}
	}
	
	/**
	 * 返回卡信息将取消所有优惠
	 */
	public void backCardInfo(){
		CList<Map<String,String>> params=new CList<Map<String,String>>();
		params.add("deviceId",SharedPreferencesUtils.getDeviceId(getActivity()));// cancleUserCounp（String deviceId, String userCode ,String tableNum ,String orderId )
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
	 * 确认支付方法
	 */
	public void ConfirmPayment(){
		TextView ysjeTView=(TextView)getActivity().findViewById(R.id.vip_payMent_yingFText1);//应付金额
		final EditText jfky=(EditText)getActivity().findViewById(R.id.vip_payment_jiFenKYYEEdit);//积分可用

//		final EditText xjxf=(EditText)getActivity().findViewById(R.id.vip_payment_xianJinXFEdit);//现金消费
		final EditText yexf=(EditText)getActivity().findViewById(R.id.vip_payment_yeEXFEdit);//余额消费

		final EditText jfxf=(EditText)getActivity().findViewById(R.id.vip_payment_jiFenXFEdit);//积分消费
		setPricePoint(jfxf);
		final double recYE=Double.valueOf(isNan(yexf.getText().toString()));
		final double recJF=Double.valueOf(isNan(jfxf.getText().toString()));
//		final double recXJ=Double.valueOf(isNan(xjxf.getText().toString()));
		final double ysJE=Double.valueOf(isNan(ysjeTView.getText().toString()));
		if(recYE+recJF>ysJE){//判断储值消费+积分消费是否大于应收金额，如果大于则抛错
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
				/*card_Sale(String deviceId, 设备编号
				 * 			String userCode, 登录编号
				 * 			String cardNumber,会员卡号
				 * 			String cardStoredAmount,储值消费金额
				 * 			String cardIntegralAmount,积分消费金额
				 * 			String cardCashAmount,现金消费金额
				 * 			String cardTicketInfoList,本次消费券列表
				 * 			String cardPWD,储值卡消费密码  6 位
				 * 			String orderId,账单号 
				 * 			String tableNum,台位号
				 *			QString integralOverall 当前积分余额)
				 */
				final TextView vipNum=(TextView)getActivity().findViewById(R.id.vip_taiweiText);
				TextView orderId=(TextView)getActivity().findViewById(R.id.vip_orderText1);
				TextView tableNum=(TextView)getActivity().findViewById(R.id.vip_taiweiText1);
				CList<Map<String,String>> list=new CList<Map<String,String>>();
				list.add("deviceId", SharedPreferencesUtils.getDeviceId(getActivity()));//设备编号
				list.add("userCode", SharedPreferencesUtils.getUserCode(getActivity()));//登录编号
				list.add("cardNumber", SingleMenu.getMenuInstance().getCardNum());//会员卡号
				list.add("cardStoredAmount", isNan(yexf.getText().toString()));//储值消费金额
				list.add("cardIntegralAmount", isNan(jfxf.getText().toString()));//积分消费金额
//				list.add("cardCashAmount", isNan(xjxf.getText().toString()));//现金消费金额
				list.add("cardTicketInfoList", "");//本次消费券列表
				list.add("cardPWD", pwd.getText().toString());//储值卡消费密码  6 位
				list.add("orderId",SingleMenu.getMenuInstance().getOrderNum());//账单号
				list.add("tableNum", SingleMenu.getMenuInstance().getTableNum());//台位号
				list.add("integralOverall", isNan(jfky.getText().toString()));//当前积分余额
				getServer().connect(getActivity(), "card_Sale", "ChoiceWebService/services/HHTSocket?/card_Sale", list, new OnServerResponse() {
					@Override
					public void onResponse(String result) {
						/*会员卡号@ 1
						终端流水号（消费撤销时使 用）@ 2
						实际储值消费金额@ 3
						优惠储值消费金额@ 4
						当前储值余额@ 5
						当前积分余额@---（需要在现 金消费时返回） @  6
						打印内容 7*/
						getDialog().dismiss();
						String[] str=ValueUtil.isEmpty(result)?null:result.split("@");
						if(str!=null&&str[0].equals("0")){
							//积分记录
							// 接口不返回 会员卡号。通过下面获取会员卡号
							//--------------消费记录-----------------------
							if(ValueUtil.isNaNofDouble(jfxf.getText().toString())>0){//判断是否使用积分消费
								if(ValueUtil.isEmpty(TsData.coupPay)){
									TsData.coupPay=new HashMap<String,CouponStoreBean>();
								}
								CouponStoreBean bean=TsData.coupPay.get("会员积分消费");
								if(ValueUtil.isEmpty(bean)){
									bean=new CouponStoreBean();
								}
								bean.setCouponName("会员积分消费");
								bean.setCouponMoney(jfxf.getText().toString());
								bean.setCouponNum(bean.getCouponNum()+1);
								TsData.coupPay.put("会员积分消费", bean);
								//积分消费
								if(ValueUtil.isEmpty(TsData.member)){
									TsData.member=new ArrayList<MoneyPay>();
								}
								MoneyPay pay=new MoneyPay();
								pay.setPayCardNumber(SingleMenu.getMenuInstance().getCardNum());
								pay.setPayIntegral(str[5].trim());
								pay.setPayTrace(str[1].trim());
								TsData.member.add(pay);

							}
							if(ValueUtil.isNaNofDouble(yexf.getText().toString())>0){//判断是否有储值消费
								if(ValueUtil.isEmpty(TsData.coupPay)){
									TsData.coupPay=new HashMap<String,CouponStoreBean>();
								}
								CouponStoreBean bean=TsData.coupPay.get("会员储值消费");
								if(ValueUtil.isEmpty(bean)){
									bean=new CouponStoreBean();
								}
								bean.setCouponName("会员储值消费");
								bean.setCouponMoney(yexf.getText().toString());
								bean.setCouponNum(1);
								TsData.coupPay.put("会员储值消费", bean);
							}
							Intent intent=new Intent(getActivity(),SettleAccountsActivity.class);
							intent.putExtras(getBundle().getBundle("BillBundle"));
							startActivity(intent);
							getActivity().finish();
							//TODO 暂时不需要 防止重复
//							if(ValueUtil.isNaNofDouble(xjxf.getText().toString())>0){//判断是否有现金消费
//								if(ValueUtil.isEmpty(TsData.moneyPay)){
//									TsData.moneyPay=new HashMap<String,MoneyPay>();
//								}
//								MoneyPay money=TsData.moneyPay.get("会员卡消费");
//								if(ValueUtil.isEmpty(money)){
//									money=new MoneyPay();
//								}
//								money.setPayCardNumber(str[1]);
//								money.setPayMoney(money.getPayMoney()+ValueUtil.isNaNofDouble(xjxf.getText().toString()));
//								money.setPayNum(money.getPayNum()+1);
//								money.setPayName("会员卡消费");
//								TsData.moneyPay.put("会员卡消费", money);
//							}
							//------------------------------------
							Toast.makeText(getActivity(), R.string.pay_success, Toast.LENGTH_LONG).show();
							//判断是否支付完成
//							if(recXJ+recYE+recJF>=ysJE){
//								Double zhaoL=recXJ-(ysJE-(recYE+recJF));
//								new FinalPay().fPay(getBundle().getBundle("BillBundle"), getActivity(), zhaoL);
//							}
							TsData.isEnd=false;
							alert.dismiss();
							//-----------------界面处理-------------------
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
	 * 输入框保留2位小数
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
	 * 更新界面显示
	 * @param result
	 * @param yinS
	 */
	public void showResult(String result,Double yinS){
		String[] str=result.split("@");
//		会员卡号@ 0
//		终端流水号（消费撤销时使 用）@ 1
//		实际储值消费金额@ 2
//		优惠储值消费金额@ 3
//		当前储值余额@ 4
//		当前积分余额@---（需要在现 金消费时返回） @  5
//		打印内容 6
//		((EditText)getActivity().findViewById(R.id.vip_payment_xianJinXFEdit)).setText("0.0");//现金消费
		((EditText)getActivity().findViewById(R.id.vip_payment_yeEXFEdit)).setText("0.0");//余额消费
		((EditText)getActivity().findViewById(R.id.vip_payment_jiFenXFEdit)).setText("0.0");//积分消费
		((EditText)getActivity().findViewById(R.id.vip_payment_jiFenKYYEEdit)).setText(str[5]);//积分可用
		((EditText)getActivity().findViewById(R.id.vip_payMent_chuZhiKYYEEdit)).setText(str[4]);//储值可用余额
		((TextView)getActivity().findViewById(R.id.vip_payMent_yingFText1)).setText(String.valueOf(yinS>=0?ValueUtil.setScale(yinS, 2, BigDecimal.ROUND_DOWN):0));//应付金额
		//TODO 保留的小数位
		getBundle().putString("Payable", String.valueOf(yinS>=0?ValueUtil.setScale(yinS,2, BigDecimal.ROUND_DOWN):0));
		VipRecordUtil util=new VipRecordUtil();
		VipRecord vip=util.queryHandle(getActivity(), getBundle().getString("tableNum")).get(0);
		vip.setIntegralOverall(ValueUtil.isNaNofDouble(str[5]));
		vip.setPayable(yinS>=0?Double.valueOf(ValueUtil.setScale(yinS, 2, BigDecimal.ROUND_DOWN)):0);
		vip.setStoredCardsBalance(ValueUtil.isNaNofDouble(str[4]));
		util.insertHandle(getActivity(), vip);
	}
	/**
	 * 判罚 val是否为数字如果是则返回val如果不是则返回0
	 * @param val
	 * @return
	 */
	public String isNan(String val){
		return ValueUtil.isNaN(val)?val:"0";
	}
	/**
	 * 代码调用Back键
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
	 * 消费计算方法
	 * @param key
	 */
	public void xfJs(int key){
		TextView ssje=(TextView)getActivity().findViewById(R.id.vip_payMent_yingFText1);//应付金额
		EditText czky=(EditText)getActivity().findViewById(R.id.vip_payMent_chuZhiKYYEEdit);//储值可用
		EditText jfky=(EditText)getActivity().findViewById(R.id.vip_payment_jiFenKYYEEdit);//积分可用
//		EditText xjxf=(EditText)getActivity().findViewById(R.id.vip_payment_xianJinXFEdit);//现金消费
		EditText yexf=(EditText)getActivity().findViewById(R.id.vip_payment_yeEXFEdit);//余额消费
		EditText jfxf=(EditText)getActivity().findViewById(R.id.vip_payment_jiFenXFEdit);//积分消费
		double yingSJE=0;//应收金额
		double chuZhiKY=0;//储值可用
		double jiFenKY=0;//积分可用
		double xianJinXF=0;//现金消费
		double yuEXF=0;//余额消费
		double jiFenXF=0;//积分消费
		if(ValueUtil.isNaN(ssje.getText().toString())){//判断应收金额
			yingSJE=Double.valueOf(ssje.getText().toString());
		}
		if(ValueUtil.isNaN(czky.getTag().toString())){//判断储值可用金额
			chuZhiKY=Double.valueOf(czky.getTag().toString());
		}
		if(ValueUtil.isNaN(jfky.getTag().toString())){//判断积分可用金额
			jiFenKY=Double.valueOf(jfky.getTag().toString());
		}
//		if(ValueUtil.isNaN(xjxf.getText().toString())){//判断现金消费
//			xianJinXF=Double.valueOf(xjxf.getText().toString());
//		}
		if(ValueUtil.isNaN(yexf.getText().toString())){//判断余额消费
			yuEXF=Double.valueOf(yexf.getText().toString());
		}
		if(ValueUtil.isNaN(jfxf.getText().toString())){//判断积分消费
			jiFenXF=Double.valueOf(jfxf.getText().toString());
		}
		if(yuEXF+jiFenXF>yingSJE){
			yexf.setText("0");//储值可用
			jfxf.setText("0");//积分可用
			Toast.makeText(getActivity(), R.string.pay_money_error2, Toast.LENGTH_LONG).show();
			return;
		}
		switch (key) {
		case 1: //计算余额
			double chuZYF=0;
			if(xianJinXF+jiFenXF<yingSJE){
				chuZYF=yingSJE-(xianJinXF+jiFenXF);
			}
			if(chuZhiKY>=chuZYF){
				yexf.setText(chuZYF+"");//储值消费
			}else{
				yexf.setText(chuZhiKY+"");//储值消费
			}
			break;
		case 2://计算积分
			double jiFYF=0;
			if(yuEXF+xianJinXF<yingSJE){
				jiFYF=yingSJE-(yuEXF+xianJinXF);
			}
			if(jiFenKY>=jiFYF){
				jfxf.setText(jiFYF+"");//积分消费
			}else{
				jfxf.setText(jiFenKY+"");//积分消费
			}
			break;
		case 3://计算现金
			double xianJYF=yingSJE-(yuEXF+jiFenXF);
//			xjxf.setText(xianJYF+"");
			break;
		}
		
	}
}
