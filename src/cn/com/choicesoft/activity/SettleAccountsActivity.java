package cn.com.choicesoft.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.MoneyPay;
import cn.com.choicesoft.bean.ScanCodePay;
import cn.com.choicesoft.bean.SettlementOperate;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.fragment.BillFragment;
import cn.com.choicesoft.fragment.PrivilegeFragment;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.ClearEditText;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;
import cn.com.choicesoft.view.VipMsg;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * 结算界面Activity
 *@Author:M.c
 *@CreateDate:2014-1-6
 *@Email:JNWSCZH@163.COM
 */
public class SettleAccountsActivity extends BaseActivity implements OnClickListener{
	private FragmentManager manager;
	private FragmentTransaction transaction;
    private PopupWindow popupWindow=null;//弹出窗
	/**
	 * 提示框
	 */
	private LoadingDialog dialog;
	/**
	 * 用于接收台位信息
	 */
	private Bundle bundle;
	/**
	 * 数据库公用查询结果 处理类
	 */
	private ListProcessor pro;
	/**
	 * 会员实体
	 */
	private VipRecord vip;
	/**
	 * 用户编码,台位编码,账单号
	 */
	private TextView userNum, tableNum,orderNum;
	private ImageView menuinfo_imageView;
	private View layout;
	private LinearLayout topLinearLayout;
	private Button back;
	/**
	 * 男人数,女人数
	 */
	private TextView manNum,womanNum;


	public ListProcessor getPro() {
		if(pro==null){
			pro=new ListProcessor();
		}
		return pro;
	}
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	public VipRecord getVip() {
		return vip;
	}
	public void setVip(VipRecord vip) {
		this.vip = vip;
	}
	public LoadingDialog getDialog() {
		if(dialog==null){
			dialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
			dialog.setCancelable(true);
		}
		return dialog;
	}

	public Server getServer() {
		return new Server();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		if(ValueUtil.isEmpty(this.getIntent().getStringExtra("mark"))||!this.getIntent().getStringExtra("mark").equals("VipFind")){
			TsData.initData();//初始化暂存数据
		}
		setBundle(this.getIntent().getExtras());//获取Intent传过来的数据
		if (ChioceActivity.ispad) {
			this.setContentView(R.layout.activity_settle_accounts_pad);
		} else {
			this.setContentView(R.layout.activity_settle_accounts);
		}
		//=====================button==============
		back=(Button)this.findViewById(R.id.SA_back);//返回按钮
		Button huiYuanKa=(Button)this.findViewById(R.id.SA_huiYuanKa);//会员卡
		Button quXiaoPay=(Button)this.findViewById(R.id.SA_quXiaoPay);//取消支付
		Button quXiaoPrivilege=(Button)this.findViewById(R.id.SA_quXiaoPrivilege);//取消优惠
		Button xianJin=(Button)this.findViewById(R.id.SA_pay);//支付按钮
		Button yinHangKa=(Button)this.findViewById(R.id.SA_yinHangKa);//银行卡结算
		Button yuDaYin=(Button)this.findViewById(R.id.SA_yuDaYin);//预打印
		Button weixin=(Button)this.findViewById(R.id.SA_winxin_up);//微信上传

		for (Map<String,String>map: SingleMenu.getMenuInstance().getPermissionList()){
			if (map.get("CODE").equals("5001")){
				xianJin.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					xianJin.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("5002")){
				quXiaoPay.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					quXiaoPay.setVisibility(View.GONE);
				}
			}
//			if (map.get("CODE").equals("5003")){
//				addDish.setTag(map.get("AUTHORITY"));
//				if (map.get("ISSHOW").equals("0")) {
//					addDish.setVisibility(View.GONE);
//				}
//			}
			if (map.get("CODE").equals("5004")){
				quXiaoPrivilege.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					quXiaoPrivilege.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("5005")){
				yinHangKa.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					yinHangKa.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("5006")){
				yuDaYin.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					yuDaYin.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("5007")){
				weixin.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					weixin.setVisibility(View.GONE);
				}
			}
		}
        if(!SharedPreferencesUtils.getIsVip(this)){
            huiYuanKa.setVisibility(View.GONE);
        }
		back.setOnClickListener(this);
		huiYuanKa.setOnClickListener(this);
		quXiaoPay.setOnClickListener(this);
		quXiaoPrivilege.setOnClickListener(this);
		xianJin.setOnClickListener(this);
		yinHangKa.setOnClickListener(this);
		yuDaYin.setOnClickListener(this);
        weixin.setOnClickListener(this);
		//=====================button==============

		if (ChioceActivity.ispad) {
			userNum = (TextView)this.findViewById(R.id.operatorNames);
			orderNum = (TextView)this.findViewById(R.id.vip_orderText1);
			tableNum=(TextView)this.findViewById(R.id.vip_taiweiText1);
		} else {
			// TODO 
			back=(Button)this.findViewById(R.id.eatable_btn_back);//返回按钮
			back.setOnClickListener(this);
			menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
			menuinfo_imageView.setClickable(true);
			menuinfo_imageView.setOnClickListener(this);
			layout = LayoutInflater.from(SettleAccountsActivity.this).inflate(R.layout.menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			manNum = (TextView) layout.findViewById(R.id.dishesact_tv_mannum);
			womanNum = (TextView) layout.findViewById(R.id.dishesact_tv_womannum);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			topLinearLayout = (LinearLayout) findViewById(R.id.vip_top);
		}
		//赋值操作员
		userNum.setText(SharedPreferencesUtils.getOperator(this));
		//复制订单号
		orderNum.setText(getBundle().getString("orderId"));
		if (SingleMenu.getMenuInstance().getOrderNum()==null||SingleMenu.getMenuInstance().getOrderNum().length()==0){
			SingleMenu.getMenuInstance().setOrderNum(getBundle().getString("orderId"));
		}
		//赋值台位
		String table=getBundle().getString("tableNum");//给台位TextView赋值
		tableNum.setText(ValueUtil.isNotEmpty(table)?table:"");
		List<VipRecord> vip=new VipRecordUtil().queryHandle(this, table);//通过台位去本地库查询会员信息
		TextView cardNumTitle=(TextView)findViewById(R.id.vipANumText);//会员Text
		TextView cardNum=(TextView)findViewById(R.id.vipANumText1);//会员号Text
		if(ValueUtil.isNotEmpty(vip)){//判断是有会员信息
			VipMsg.iniVip(this, table,R.id.settle_vipMsg_Img);//初始化右上角会员信息标识
			cardNumTitle.setVisibility(TextView.VISIBLE);
			cardNum.setVisibility(TextView.VISIBLE);
			cardNum.setText(vip.get(0).getCardNumber());
			SingleMenu.getMenuInstance().setCardNum(vip.get(0).getCardNumber());

			SingleMenu.getMenuInstance().setCardPhone(vip.get(0).getPhone());
			String str=SingleMenu.getMenuInstance().getCardNum();
			String str1=SingleMenu.getMenuInstance().getCardPhone();
		}
	}
	/**
	 * 初始化Fragment
	 */
	@Override
	protected void onResume() {
		manager=getFragmentManager();
		transaction=manager.beginTransaction();
        BillFragment billF=new BillFragment();//账单信息Fragment
        PrivilegeFragment privilegeF=new PrivilegeFragment();//优惠券Fragment
        billF.setBundle(this.getIntent().getExtras());
        privilegeF.setBundle(this.getIntent().getExtras());
		if(manager.findFragmentByTag("AccLeftLayout")==null&&manager.findFragmentByTag("AccRightLayout")==null){
			transaction.add(R.id.setAccLeftLayout, billF,"AccLeftLayout");
			transaction.add(R.id.setAccRightLayout, privilegeF,"AccRightLayout");
		}else{
            transaction.replace(R.id.setAccLeftLayout, billF,"AccLeftLayout");
            transaction.replace(R.id.setAccRightLayout, privilegeF,"AccRightLayout");
        }
		transaction.commit();
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.SA_back://返回
			cancelHandle();
			break;
		case R.id.eatable_btn_back://返回
			cancelHandle();
			break;
		case R.id.SA_huiYuanKa://会员卡操作
			findVipCard();
			break;
		case R.id.SA_quXiaoPay://取消支付
			if(ValueUtil.isNotEmpty(TsData.member)){//判断是否存在会员操作
				cancelVip(TsData.member.size()-1);
				break;
			}
			cancelPay();
			break;
		case R.id.SA_quXiaoPrivilege://取消优惠
			cancel("cancleUserCounp","cancleUserCounp",1);
			break;
		case R.id.SA_pay://现金（支付）
            vipPrice((Button) v);
			break;
		case R.id.pay_xianjin://现金支付
            payAlert(new String[]{"5"});
            break;
        case R.id.pay_yinhangka://银行卡
			payment(new String[]{"31"});
            break;
        case R.id.SA_winxin_up://微信上传
            weiXinUp();
            break;
        case R.id.pay_network://网络
            payment(new String[]{"50","48"});
            break;
		case R.id.SA_yuDaYin://预打印
			cancel("priPrintOrder","priPrintOrder",2);
			break;
		case R.id.orderinfo_ImageView://订单信息
			View layout = null ;
			layout = LayoutInflater.from(SettleAccountsActivity.this).inflate(R.layout.menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			//赋值操作员
			userNum.setText(SharedPreferencesUtils.getOperator(this));
			//复制订单号
			orderNum.setText(getBundle().getString("orderId"));
			//赋值台位
			String table=getBundle().getString("tableNum");//给台位TextView赋值
			tableNum.setText(ValueUtil.isNotEmpty(table)?table:"");
			manNum = (TextView) layout.findViewById(R.id.dishesact_tv_mannum);
			womanNum = (TextView) layout.findViewById(R.id.dishesact_tv_womannum);
			if (BillFragment.man!=null && BillFragment.woman!=null) {
				manNum.setText(BillFragment.man);
				womanNum.setText(BillFragment.woman);
			}
			AlertDialog tipdialog = new AlertDialog.Builder(SettleAccountsActivity.this, R.style.Dialog_tip).setView(layout).create();
			tipdialog.setCancelable(true);
	        Window dialogWindow = tipdialog.getWindow();
	        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	        dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
	        lp.x = 0; // 新位置X坐标
	        lp.y = topLinearLayout.getHeight() -15;// 新位置Y坐标
	        dialogWindow.setAttributes(lp);
			tipdialog.show();
			break;
		}
	}

    /**
     * 微信上传
     */
	public void weiXinUp(){
        String money=((TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().toString();
        if(ValueUtil.isNaNofDouble(money)<=0){
            ToastUtil.toast(this,R.string.pay_money_zero);
            return;
        }
        CList list=new CList();
        list.add("deviceId",SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));
        list.add("userCode",SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
        list.add("json","{\"orderid\":\""+getBundle().getString("orderId")+"\",\"paymoney\":\""+money+"\" }");
        new Server().connect(this, "pushWeChatCheckOut", "ChoiceWebService/services/HHTSocket?/pushWeChatCheckOut", list, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getDialog().dismiss();
                if(ValueUtil.isNotEmpty(result)){
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        String res= jsonObject.getString("return");
                        if (res.equals("0")){
                            ToastUtil.toast(SettleAccountsActivity.this, R.string.up_success);
                            Intent intent=new Intent(SettleAccountsActivity.this,MainActivity.class);
                            SettleAccountsActivity.this.startActivity(intent);
                            SettleAccountsActivity.this.finish();
                        }else{
                            ToastUtil.toast(SettleAccountsActivity.this,jsonObject.getString("error"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    ToastUtil.toast(SettleAccountsActivity.this,R.string.up_error);
                }
            }
            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });
    }
	/**
	 * 会员支付取消
	 */
	public void cancelVip(final int size){
		//TODO 会员卡号通过 数据库查询,可能会有异议
		try {
			setVip(new VipRecordUtil().queryHandle(this, getBundle().getString("tableNum")).get(0));
		} catch (Exception e) {
			Log.e("SettleAccountsActivity-查询数据错误", e.getMessage());
		}
		CList<Map<String,String>> list=new CList<Map<String,String>>();
		list.add("deviceId", SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));
		list.add("userCode", SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
		list.add("cardNumber", getVip().getCardNumber().trim());
		list.add("trace", TsData.member.get(size).getPayTrace().trim());
		list.add("printtye", "2");
		list.add("cardPWD", "");//取消不需要密码
		list.add("orderId", ValueUtil.isNotEmpty(getVip())?getVip().getOrderId().trim():"");
		getServer().connect(this, "card_Undo", "ChoiceWebService/services/HHTSocket?/card_Undo", list, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				String[] str = ValueUtil.isEmpty(result) ? null : result.split("@");
				if (str != null && !str[0].equals("0")) {
					showToast(str[1]);
				} else if ((str != null && str[0].equals("0"))) {
					if (size == 0) {
						TsData.member = null;//清楚积分暂存记录
						cancelPay();
					} else {//如果取消的非最后一个递归继续取消
						int count = size;
						cancelVip(count--);
					}
				} else {
					showToast(R.string.net_error);
				}
			}

			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
	}
	
	/**
	 * 返回方法
	 */
	public void cancelHandle(){
		if(ValueUtil.isNotEmpty(TsData.coupPay)||ValueUtil.isNotEmpty(TsData.moneyPay)){
			new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.hint).setMessage(R.string.bill_Paid_not_cancle).setPositiveButton(R.string.confirm, null).show();
		}else{
			this.finish();
		}
	}
	/**
	 * 现金或银行卡支付
	 * @param mark
	 */
	public void payment(String[] mark){
        String phr="?";
        for(int i=1;mark!=null&i<mark.length;i++){
            phr+=",?";
        }
		//从本地库获取支付方式
		List<SettlementOperate> list=getPro().query("select operate,operatename,operatevalue,operategroupid from settlementoperate where operategroupid in("+phr+")", mark,this, SettlementOperate.class);
		LayoutInflater layoutInflater = LayoutInflater.from(SettleAccountsActivity.this);
		//通过infalte获取popupWindow界面
		View popupWindow = layoutInflater.inflate(R.layout.custom_popup_window, null); 
		//创建弹出框
		AlertDialog dialog = new AlertDialog.Builder(SettleAccountsActivity.this,R.style.edittext_dialog).setView(popupWindow).show();
		LinearLayout linearLayout=(LinearLayout)popupWindow.findViewById(R.id.popupWind);
		linearLayout.setPadding(15, 25, 15, 25);
		DisplayMetrics dmm = new DisplayMetrics();
		dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dmm);
		int dialogWidth  =  dmm.widthPixels;
		LinearLayout dLin=new LinearLayout(SettleAccountsActivity.this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
        LinearLayout.LayoutParams linearlayoutlp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dLin.setLayoutParams(linearlayoutlp);
		params.setMargins(3, 3, 3, 3);
		/**
		 * 根据查询出来的循环遍历but
		 */
		for (SettlementOperate operate:list) {
			Button but=new Button(SettleAccountsActivity.this);
			but.setTextColor(Color.WHITE);
			but.setText(operate.getOperateName());
			but.setTag(operate);
			but.setTextSize(14);
			but.setBackgroundResource(R.drawable.blue_button_background);
			but.setOnClickListener(new OnClickListener() {//为but添加事件
                @Override
                public void onClick(View paramView) {
                    if (ValueUtil.isEmpty(paramView.getTag())) {//判断but的Tag是否为空Tag存有优惠信息
                        return;
                    }
                    SettlementOperate set = (SettlementOperate) paramView.getTag();
                    if ("50".equals(set.getOperateGroupid())||"48".equals(set.getOperateGroupid())) {
                        execute_network(set);
                    } else {
                        exc_Payment((SettlementOperate)paramView.getTag());
                    }
                }
            });
			dLin.addView(but, params);
			if(dLin.getChildCount()==3){
				linearLayout.addView(dLin);
				dLin=new LinearLayout(SettleAccountsActivity.this);
			}
		}
		if(dLin.getChildCount()>0){
			linearLayout.addView(dLin);
		}
	}

    /**
     * 微信支付宝支付
     */
    public void execute_network(SettlementOperate set){
        if (ValueUtil.isEmpty(set)) {//判断but的Tag是否为空Tag存有优惠信息
            return;
        }
        String money=((TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().toString();
        if(ValueUtil.isNaNofDouble(money)<=0){
            ToastUtil.toast(this,R.string.pay_money_zero);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(SettleAccountsActivity.this, ScanActivity.class);
        intent.putExtra("money",money);
        intent.putExtra("SettlementOperate",set);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, ValueUtil.isNaNofInteger(set.getOperateGroupid()));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 48://微信
            case 50://支付宝
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    SettlementOperate set= (SettlementOperate) bundle.getSerializable("SettlementOperate");
                    scanCodePay(bundle.getString("result"),bundle.getString("money"),requestCode==48?"2":"1",set);
                }
                break;
        }
    }

    /**
     * 调用扫码支付接口
     * @param code
     * @param money
     * @param type
     * @param slo
     */
    public void scanCodePay(String code, final String money,String type, final SettlementOperate slo){
        CList list=new CList();
        list.add("deviceId",SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));
        list.add("userCode",SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
        list.add("json",scanJson(code,money,type,slo));
        new Server().connect(this, "scanCode", "ChoiceWebService/services/HHTSocket?/scanCode", list, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				if (ValueUtil.isNotEmpty(result)) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						String res = jsonObject.getString("return");
						if (res.equals("0")) {
							ToastUtil.toast(SettleAccountsActivity.this, R.string.scan_code_pay_success);
							ViewUtil.setVew(SettleAccountsActivity.this, slo.getOperateName(), money, false, new IResult<String>() {
								@Override
								public void result(String t) {
									new FinalPay().fPay(getBundle(), SettleAccountsActivity.this, ValueUtil.isNaNofDouble(t));
								}
							});
						} else {
							ToastUtil.toast(SettleAccountsActivity.this, jsonObject.getString("error"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.toast(SettleAccountsActivity.this, R.string.scan_code_pay_error);
				}
			}

			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
    }

    /**
     * 拼接扫码支付JSON
     * @param code
     * @param money
     * @param type
     * @param slo
     * @return
     */
    public String scanJson(String code,String money,String type,SettlementOperate slo){
        ScanCodePay codePay=new ScanCodePay();
        codePay.setOperate(slo.getOperate());//编码
        codePay.setAuth_code(code);//扫描二维码或条码号
        codePay.setTotal_fee(money);//应付金额
        codePay.setOrderid(getBundle().getString("orderId"));
        codePay.setFinished("1");//是否支付完毕 0 未完 1 完毕
        codePay.setType(type);//1 支付宝 2 微信支付
        String json= new Gson().toJson(codePay);
        Log.e("扫描支付JSON", json);
        return json;
    }
	/**
	 * 普通支付类型支付
	 * @param settlementOperate
	 */
	public void new_exc_Payment(final SettlementOperate settlementOperate){

		if (settlementOperate==null){
			return;
		}
//		if(ValueUtil.isEmpty(paramView.getTag())){//判断but的Tag是否为空Tag存有优惠信息
//            return;
//        }
		final SettlementOperate set=settlementOperate;
		View view = LayoutInflater.from(this).inflate(R.layout.dia_cash_pay, null);
		final EditText text=(EditText)view.findViewById(R.id.dia_cash_pay_editText);
		Button button100=(Button)view.findViewById(R.id.button_100);
		Button button50=(Button)view.findViewById(R.id.button_50);
		Button button20=(Button)view.findViewById(R.id.button_20);
		Button button10=(Button)view.findViewById(R.id.button_10);
		final AlertDialog.Builder builder= new AlertDialog.Builder(SettleAccountsActivity.this,R.style.edittext_dialog);
		builder.setView(view);
		final AlertDialog dialog = builder.show();
		button100.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				text.setText("100");
//				fPay(set, "100");
//				dialog.dismiss();
			}
		});
		button50.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				text.setText("50");
//				fPay(set, "50");
//				dialog.dismiss();

			}
		});
		button20.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				text.setText("20");
			}
		});
		button10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				text.setText("10");
			}
		});

		builder.setTitle(R.string.input_money);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final String yingF=((TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().toString();
				if (text.getText().length()==0){
					Toast.makeText(SettleAccountsActivity.this, "录入金额不能为空", Toast.LENGTH_LONG).show();
				}else if (Double.parseDouble(text.getText().toString())>Double.parseDouble(yingF)&&set.getOperateGroupid().equals("31")){
					Toast.makeText(SettleAccountsActivity.this, "金额过大，请重新录入", Toast.LENGTH_LONG).show();
				}else {

					fPay(set, text.getText().toString());
					dialog.dismiss();
				}
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {//取消事件
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Field field = null;
				//通过反射获取dialog中的私有属性mShowing
				try {
					field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);//设置该属性可以访问
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
				try {
					field.set(dialog, true);
					dialog.dismiss();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});

		/**
		 *  设置小数位数控制
		 */
		setPricePoint(text);
		text.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    moneyPay(dialog,paramView);
					fPay(set, text.getText().toString());
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
	}
    /**
     * 普通支付类型支付
     * @param settlementOperate
     */
    public void exc_Payment(final SettlementOperate settlementOperate){

		if (settlementOperate==null){
			return;
		}
//		if(ValueUtil.isEmpty(paramView.getTag())){//判断but的Tag是否为空Tag存有优惠信息
//            return;
//        }
		final SettlementOperate set=settlementOperate;
        final EditText text=new EditText(SettleAccountsActivity.this);
        LinearLayout layout = new LinearLayout(SettleAccountsActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        text.setHint(R.string.please_input_money);
        text.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        params.setMargins(16, 12, 16, 12);
        if(ValueUtil.isNaNofDouble(set.getOperateValue())>1){
            text.setText(set.getOperateValue());
        }else{
            text.setText(((TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText());
        }
        layout.addView(text,params);
		AlertDialog.Builder builder = new AlertDialog.Builder(SettleAccountsActivity.this, R.style.edittext_dialog);
		builder.setTitle(R.string.input_money);
		builder.setView(layout);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final String yingF=((TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().toString();
				if (text.getText().length()==0){
					Toast.makeText(SettleAccountsActivity.this, "录入金额不能为空", Toast.LENGTH_LONG).show();
				}else if (Double.parseDouble(text.getText().toString())>Double.parseDouble(yingF)&&set.getOperateGroupid().equals("31")){
					Toast.makeText(SettleAccountsActivity.this, "金额过大，请重新录入", Toast.LENGTH_LONG).show();
				}else {

					fPay(set, text.getText().toString());
				}
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {//取消事件
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Field field = null;
				//通过反射获取dialog中的私有属性mShowing
				try {
					field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);//设置该属性可以访问
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
				try {
					field.set(dialog, true);
					dialog.dismiss();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
		final AlertDialog dialog = builder.show();
		/**
		 *  设置小数位数控制
		 */
		setPricePoint(text);
		text.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    moneyPay(dialog,paramView);
					fPay(set, text.getText().toString());
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
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
//	public void moneyPay(DialogInterface dialog,View paramView){
//		String money=((EditText)((AlertDialog)dialog).getCurrentFocus()).getText().toString();
//		SettlementOperate set=(SettlementOperate)paramView.getTag();
//		Field field = null;
//		//通过反射获取dialog中的私有属性mShowing
//        try {
//			field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//			field.setAccessible(true);//设置该属性可以访问
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		}
//		if(ValueUtil.isEmpty(money)){
//			showToast(R.string.money_not_null);
//			try {
//				field.set(dialog, false);//防止弹出框关闭
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//			return;
//		}
//		if(ValueUtil.isEmpty(TsData.moneyPay)){
//			TsData.moneyPay=new HashMap<String,MoneyPay>();
//		}
//		MoneyPay pay=TsData.moneyPay.get(set.getOperateName());
//		if(ValueUtil.isEmpty(pay)){
//			pay=new MoneyPay();
//		}
//		pay.setPayId(set.getOperate());
//		pay.setPayMoney(pay.getPayMoney()+ValueUtil.isNaNofDouble(money));
//		pay.setPayNum(pay.getPayNum()+1);
//		pay.setPayName(set.getOperateName());
//		TsData.moneyPay.put(set.getOperateName(), pay);
//		ViewUtil.setVew(SettleAccountsActivity.this,set.getOperateName(),money,false,new IResult<String>() {
//			@Override
//			public void result(String t) {
//				new FinalPay().fPay(getBundle(), SettleAccountsActivity.this, ValueUtil.isNaNofDouble(t));
//			}
//		});
//		try {//执行成功后关闭画面
//			field.set(dialog, true);
//			dialog.dismiss();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 *
	 * @param payMentCode
	 * @param money
	 */
	public void fPay(final SettlementOperate payMentCode, final String money){
		//TODO 通过数据库获取 vip 信息
		final String yingF=((TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().toString();
		CList<Map<String,String>> clist=new CList<Map<String,String>>();
		clist.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
		clist.add("userCode", SharedPreferencesUtils.getUserCode(this));
		clist.add("tableNum", SingleMenu.getMenuInstance().getTableNum());
		clist.add("orderId", SingleMenu.getMenuInstance().getOrderNum());
		clist.add("paymentId", payMentCode.getOperate()+"!");
		clist.add("paymentCnt", 1+"!");
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
		clist.add("mpaymentMoney", df.format(Double.parseDouble(money))+"!");
		clist.add("payFinish", 0+"!");
		clist.add("integralOverall", 0);
		clist.add("cardNumber",ValueUtil.isNotEmpty(SingleMenu.getMenuInstance().getCardNum())?SingleMenu.getMenuInstance().getCardNum():"");

		new Server().connect(this, "userPayment", "ChoiceWebService/services/HHTSocket?/userPayment", clist, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				//	账单号，支付方式编码，数量， 支付金额
				final String[] str=ValueUtil.isEmpty(result)?null:result.split("@");
				if(str!=null&&str[0].equals("0")){//判断是否支付成功
					ToastUtil.toast(SettleAccountsActivity.this, R.string.execute_success);
					if (Double.parseDouble(yingF)-Double.parseDouble(money)<=0){
						String mes=getString(R.string.pay_success);
						ViewUtil.setVew(SettleAccountsActivity.this,payMentCode.getOperateName(),money,false,new IResult<String>() {
							@Override
							public void result(String t) {
								new FinalPay().fPay(getBundle(), SettleAccountsActivity.this, ValueUtil.isNaNofDouble(t));
							}
						});
//						if(Double.parseDouble(yingF)-Double.parseDouble(money)<0){//判断是否需要找零
//							mes=getString(R.string.pay_success_zhaoL)+(-Double.parseDouble(yingF)+Double.parseDouble(money))+getString(R.string.money_unit);
//						}
//						TsData.isEnd=true;
//						new VipRecordUtil().delHandle(SettleAccountsActivity.this, SingleMenu.getMenuInstance().getTableNum());//删除库总该台位数据
//						new AlertDialog.Builder(SettleAccountsActivity.this)//支付完成提示
//								.setTitle(R.string.pay_success)
//								.setMessage(mes)
//								.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//										if(ValueUtil.isEmpty(TsData.coupPay)&&ValueUtil.isEmpty(TsData.member)&&TsData.moneyPay!=null){//判断是否需要开发票
//											invoice(SettleAccountsActivity.this,SingleMenu.getMenuInstance().getOrderNum(),(-Double.parseDouble(yingF)+Double.parseDouble(money)));//执行开发票
//										}else{
//											backTable(SettleAccountsActivity.this);//结束界面
//										}
//									}
//								}).show();
					}else
					{
						manager=getFragmentManager();
						transaction=manager.beginTransaction();
						BillFragment bill=new BillFragment();
						bill.setBundle(getBundle());
						transaction.replace(R.id.setAccLeftLayout, bill);
						transaction.commit();
					}
				}else if(str!=null&&!str[0].equals("0")){
					Toast.makeText(SettleAccountsActivity.this, str[1], Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(SettleAccountsActivity.this,R.string.net_error, Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
	}
	/**
	 * 是否打印发票
	 */
	public void invoice(final Activity activity,final String orderId,final Double zhaoL){
		new AlertDialog.Builder(activity).setTitle(R.string.hint)
				.setMessage(R.string.whether_the_invoice).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				String invoiceFace(String deviceId, String orderId, String invoiceMoney)
//				输入参数：
//				deviceId   设备标号
//				orderId    单号
//				invoiceMoney  开发票金额
				BigDecimal big=new BigDecimal(0.0);
				for(Map.Entry<String,MoneyPay> map:TsData.moneyPay.entrySet()){//循环计算现金金额
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
						getDialog().dismiss();
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
						getDialog().show();
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
	 * 返回方法
	 * @param activity
	 */
	public void backTable(Activity activity){
		Intent intent=new Intent(activity,MainActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}
	/**
	 * 支付验证
	 * @param mark
	 */
	public void payAlert(final String[] mark){
		for (Map<String,String>map:SingleMenu.getMenuInstance().getPermissionList())
		{
			if (map.get("CODE").equals("5008")){
				if (map.get("ISSHOW").equals("1")){
					if(TsData.moneyPay!=null){
						payment(mark);
					}else{
						new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.hint).setMessage(R.string.pay_money_not_privilege)
								.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface paramDialogInterface,int paramInt) {
										payment(mark);
									}
								}).setNegativeButton(R.string.cancle, null).show();
					}
				}else {
					List<SettlementOperate> list=getPro().query("select operate,operatename,operatevalue,operategroupid from settlementoperate where operatename ='现金'", null,this, SettlementOperate.class);
					final SettlementOperate settlementOperate=list.get(0);
					if(TsData.moneyPay!=null){
						new_exc_Payment(settlementOperate);
//						exc_Payment(settlementOperate);
					}else{
						new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.hint).setMessage(R.string.pay_money_not_privilege)
								.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface paramDialogInterface,int paramInt) {
										new_exc_Payment(settlementOperate);
									}
								}).setNegativeButton(R.string.cancle, null).show();
					}
				}
			}
		}

	}
	
	/**
	 * 公用代码-出入参数只有【deviceId】【userCode】【tableNum】【orderId】成功
	 * @param model
	 * @param url
	 */
	public void cancel(String model,String url,final int mark){
		CList<Map<String,String>> params=new CList<Map<String,String>>();
		params.add("deviceId",SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));// cancleUserCounp（String deviceId, String userCode ,String tableNum ,String orderId )
		params.add("userCode",SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
		params.add("tableNum",getBundle().getString("tableNum"));
		params.add("orderId",getBundle().getString("orderId"));
		getServer().connect(SettleAccountsActivity.this, model, "ChoiceWebService/services/HHTSocket?/"+url, params, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				if(ValueUtil.isNotEmpty(result)){
					String [] res = result.split("@");
					if(res.length>=1){
						if(!res[0].equals("0")){
							showToast(res[1]);
							return;
						}else if(res[0].equals("0")){
							if(mark==1){
								TsData.coupPay=null;//清楚优惠暂存数据
								//刷新BillFragment 更新账单信息
								manager=getFragmentManager();
								transaction=manager.beginTransaction();
								BillFragment bill=new BillFragment();
								bill.setBundle(getBundle());
								transaction.replace(R.id.setAccLeftLayout, bill);
								transaction.commit();
							}
							showToast(R.string.success);
						}
					}
				}else{
					showToast(R.string.net_error);
				}
			}
			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
	}
	/**
	 * 取消支付
	 */
	public void cancelPay(){
		CList<Map<String,String>> params=new CList<Map<String,String>>();
		params.add("deviceId",SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));// cancleUserCounp（String deviceId, String userCode ,String tableNum ,String orderId )
		params.add("userCode",SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
		params.add("tableNum",getBundle().getString("tableNum"));
		params.add("orderId",getBundle().getString("orderId"));
		getServer().connect(SettleAccountsActivity.this, "cancleUserPayment", "ChoiceWebService/services/HHTSocket?/cancleUserPayment", params, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				if(ValueUtil.isNotEmpty(result)){
					String [] res = result.split("@");
					if(res.length>=1){
						if(!res[0].equals("0")){
							showToast(res[1]);
						}else{
							TsData.moneyPay=null;//清楚暂存 现金数据
							if(ValueUtil.isEmpty(TsData.coupPay)){//如果优惠为空则结束，如果不为空继续执行并刷新账单
								manager=getFragmentManager();
								transaction=manager.beginTransaction();
								BillFragment bill=new BillFragment();
								bill.setBundle(getBundle());
								transaction.replace(R.id.setAccLeftLayout, bill);
								transaction.commit();
								showToast(R.string.success);
							}else{
								cancel("cancleUserCounp","cancleUserCounp",1);
							}
						}
					}
				}else{
					showToast(R.string.net_error);
				}
			}
			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
	}
	/**
	 * 使用会员卡
	 */
	public void findVipCard(){
//		List<VipRecord> vip=null;
//		try {
//			vip=new VipRecordUtil().queryHandle(this, getBundle().getString("tableNum"));
//		} catch (Exception e) {
//			Log.e("SettleAccountsActivity-VipCardHandle-Error", e.getMessage());
//		}
//		if(ValueUtil.isNotEmpty(vip)){
//			new AlertDialog.Builder(this).setTitle("提示").setMessage("是否更改会员号？\n手机号："+vip.get(0).getPhone()+"\n会员号："+vip.get(0).getCardNumber()).setPositiveButton("是", new DialogInterface.OnClickListener(){
//						@Override
//						public void onClick(DialogInterface paramDialogInterface,int paramInt) {
							TextView tview=(TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2);
							Intent intent=new Intent(SettleAccountsActivity.this,VipFindActivity.class);
							intent.putExtra("BillBundle", getBundle());
							intent.putExtra("Payable",(String)tview.getText());
							intent.putExtra("mark","settle");
							intent.putExtra("tableNum",getBundle().getString("tableNum"));
							startActivity(intent);
							SettleAccountsActivity.this.finish();
//						}
//					}).setNegativeButton("否", new DialogInterface.OnClickListener(){
//				@Override
//				public void onClick(DialogInterface paramDialogInterface,int paramInt) {
//					Intent intent = new Intent(SettleAccountsActivity.this,PayMentActivity.class);//打开会员支付页面
//					TextView tview=(TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2);
//					intent.putExtra("BillBundle", getBundle());
//					intent.putExtra("Payable", (String) tview.getText());
//					intent.putExtra("tableNum", getBundle().getString("tableNum"));
//					startActivity(intent);
//				}
//			}).show();
//		}else{
//			TextView tview=(TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2);
//			Intent intent=new Intent(SettleAccountsActivity.this,VipFindActivity.class);
//			intent.putExtra("BillBundle", getBundle());
//			intent.putExtra("Payable",(String)tview.getText());
//			intent.putExtra("mark","settle");
//			intent.putExtra("tableNum",getBundle().getString("tableNum"));
//			startActivity(intent);
//			SettleAccountsActivity.this.finish();
//		}
	}
	/**
	 * 会员卡操作
	 * 注：暂时废弃
	 */
	public void vipCardHandle(){
		List<VipRecord> vip=null;
		try {
			vip=new VipRecordUtil().queryHandle(this, getBundle().getString("tableNum"));
		} catch (Exception e) {
			Log.e("SettleAccountsActivity-VipCardHandle-Error", e.getMessage());
		}
		LayoutInflater layoutInflater = LayoutInflater.from(this); 
        final View popupWindow = layoutInflater.inflate(R.layout.custom_vip_query_window, null); 
        final AlertDialog alert=new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.search_vip).setView(popupWindow).show();
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {//AlertDialog 关闭事件
			@Override
			public void onDismiss(DialogInterface paramDialogInterface) {//窗口关闭  刷新订单
				manager=getFragmentManager();
				transaction=manager.beginTransaction();
				BillFragment bill=new BillFragment();
				bill.setBundle(bundle);
				transaction.replace(R.id.setAccLeftLayout, bill);
				transaction.commit();
				//----------------------------------------------
				List<VipRecord> vip=new VipRecordUtil().queryHandle(SettleAccountsActivity.this, SettleAccountsActivity.this.getIntent().getExtras().getString("tableNum"));
				TextView cardNumTitle=(TextView)findViewById(R.id.vipANumText);
				TextView cardNum=(TextView)findViewById(R.id.vipANumText1);
				if(ValueUtil.isNotEmpty(vip)){
					cardNumTitle.setVisibility(TextView.VISIBLE);
					cardNum.setVisibility(TextView.VISIBLE);
					cardNum.setText(vip.get(0).getCardNumber());
				}
			}
		});
        final Spinner spinner=(Spinner)popupWindow.findViewById(R.id.PopWin_VipSpinner);//获取下拉空间控件
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(SettleAccountsActivity.this,android.R.layout.simple_spinner_dropdown_item,new ArrayList<String>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		spinner.setAdapter(adapter);
        final EditText editText=(EditText)popupWindow.findViewById(R.id.PopWin_phoneNumEdit);//获取手机号输入框控件
        editText.setText(ValueUtil.isNotEmpty(vip)?vip.get(0).getCardNumber():"");
        final Button cancelBut=(Button)popupWindow.findViewById(R.id.PopWin_VipCancelBut);
        final Button queryBut=(Button)popupWindow.findViewById(R.id.PopWin_VipQueryBut);
        /**
         * 查询按钮单机事件
         */
        queryBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num=editText.getText().toString();
				if(num.matches("^[1][3458][0-9]{9}$")&&ValueUtil.isEmpty(spinner.getSelectedItem())){//判断是否符合手机号码
					CList<Map<String,String>> cl=new CList<Map<String,String>>();
					cl.add("deviceId", SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));
					cl.add("userCode", SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
					cl.add("phoneNumber", num);
					//根据手机号码查询会员信息
					getServer().connect(SettleAccountsActivity.this, "card_GetTrack2", "ChoiceWebService/services/HHTSocket?/card_GetTrack2", cl, new OnServerResponse() {
						@Override
						public void onResponse(String result) {
							getDialog().dismiss();
							String[] str=result==null?null:result.split("@");
							if(ValueUtil.isNotEmpty(str)&&str[0].equals("0")){
								List<String> list=Arrays.asList(str[2].split(";"));
								adapter.clear();
								adapter.addAll(list);
								queryBut.setText(R.string.confirm);
							}else if(ValueUtil.isNotEmpty(str)&&!str[0].equals("0")){
								showToast(str[1]);
							}else{
								showToast(R.string.net_error);
							}
						}
						@Override
						public void onBeforeRequest() {
							getDialog().show();
						}
					});
				}else if(ValueUtil.isNotEmpty(num)&&num.length()==16||ValueUtil.isNotEmpty(spinner.getSelectedItem())){
					boolean mark=true;
					final TextView tview=(TextView)SettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2);
					// card_QueryBalance（String deviceId,String userCode ,String cardNumber,String orderId
					CList<Map<String,String>> cl=new CList<Map<String,String>>();
					cl.add("deviceId", SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));
					cl.add("userCode", SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
					if(ValueUtil.isNotEmpty(num)&&num.length()==16){//如果输入的是会员卡号
						cl.add("cardNumber", num);
					}else if(ValueUtil.isNotEmpty(spinner.getSelectedItem())){//如果输入的不是会员卡号则判断下拉框内是否有选中数据
						cl.add("cardNumber", spinner.getSelectedItem());
					}else{//如果以上都不满足则错误
						showToast(R.string.num_error);
						mark=false;
					}
					cl.add("orderId", getBundle().getString("orderId"));
					if(mark){//如果会员卡被复制则进行查询会员信息
						getServer().connect(SettleAccountsActivity.this, "card_QueryBalance", "ChoiceWebService/services/HHTSocket?/card_QueryBalance", cl, new OnServerResponse() {
							@Override
							public void onResponse(String result) {
								getDialog().dismiss();
								String[] str=result==null?null:result.split("@");
								if(ValueUtil.isNotEmpty(str)&&str[0].equals("0")){
									 //0@ CardNumber  @ CardType@ StoredCardsBalance@ IntegralOverall@ CouponsOverall@ CouponsAvail@ TicketInfoList 
									//会员卡号        会员卡类型          储值余额           积分余额          券余额 券       可用余额             券信息列表
									VipRecord vip=new VipRecord();//VipRecordUtil
									vip.setCardNumber(str[1]);			//会员卡号
									vip.setCardType(str[2]);  			//会员卡类型
									vip.setStoredCardsBalance(ValueUtil.isNaNofDouble(str[3]));	//储值余额
									vip.setIntegralOverall(ValueUtil.isNaNofDouble(str[4]));   	//积分余额
									vip.setCouponsOverall(ValueUtil.isNaNofDouble(str[5]));		//券余额 券
									vip.setCouponsAvail(ValueUtil.isNaNofDouble(str[6]));		//可用余额
									vip.setOrderId(getBundle().getString("orderId"));
									vip.setPhone(editText.getText().toString().matches("^[1][3458][0-9]{9}$")?editText.getText().toString():"");
									vip.setTableNum(getBundle().getString("tableNum"));
									vip.setManCounts(ValueUtil.isNaNofInteger(getBundle().getString("manCounts")));
									vip.setWomanCounts(ValueUtil.isNaNofInteger(getBundle().getString("womanCounts")));
									vip.setPayable(ValueUtil.isNaNofDouble(tview.getText().toString()));
									vip.setTicketInfoList("");
									if(str.length>7){//判断速度是否大于7，大于7证明含有【券信息列表】
										vip.setTicketInfoList(str[7]);
									}
									new VipRecordUtil().insertHandle(SettleAccountsActivity.this, vip);//把会员数据添加到表里
									//---------------------------------------------------
									/*Bundle bundle=new Bundle();
									bundle.putString("CardNumber", str[1]);			//会员卡号
									bundle.putString("CardType", str[2]);  			//会员卡类型
									bundle.putString("StoredCardsBalance", str[3]);	//储值余额
									bundle.putString("IntegralOverall", str[4]);   	//积分余额
									bundle.putString("CouponsOverall", str[5]);		//券余额 券
									bundle.putString("CouponsAvail", str[6]);		//可用余额
									bundle.putBundle("BillBundle", getBundle());			//传过来的账单信息参数->YiXuanDishActivity2
									bundle.putString("TicketInfoList", "");			//券信息列表
									if(str.length>7){//判断速度是否大于7，大于7证明含有【券信息列表】
										bundle.putString("TicketInfoList", str[7]);
									}*/
									Intent intent = new Intent(SettleAccountsActivity.this,PayMentActivity.class);//打开会员支付页面
									intent.putExtra("BillBundle", getBundle());
									intent.putExtra("Payable",(String)tview.getText());
									intent.putExtra("table",getBundle().getString("tableNum"));
									startActivity(intent);
								}else if(ValueUtil.isNotEmpty(str)&&!str[0].equals("0")){
									showToast(str[1]);
								}else{
									showToast(R.string.net_error);
								}
							}
							@Override
							public void onBeforeRequest() {
								getDialog().show();
							}
						});
					}
				}else{
					showToast(R.string.num_error);
				}
			}
		});
        /**
         * 确认按钮事件
         */
        cancelBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				alert.dismiss();
			}
		});
        /**
         * 对输入的信息进行判断 清理
         */
        editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence paramCharSequence, int paramInt1,
					int paramInt2, int paramInt3) {
			}
			@Override
			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}
			@Override
			public void afterTextChanged(Editable paramEditable) {
				if(ValueUtil.isNotEmpty(spinner.getSelectedItem())&&editText.getText().length()>=11){
					queryBut.setText(R.string.PopWin_phoneNumBut);
					adapter.clear();
				}
			}
		});
	}
    //---------------------------------------------------------------------
    //选择支付弹窗
    private void vipPrice(Button v) {
        View view=this.getLayoutInflater().inflate(R.layout.select_payment,null);
        if(popupWindow==null){
            if(ChioceActivity.ispad){
                popupWindow = new PopupWindow(view, v.getWidth() * 1, ViewGroup.LayoutParams.WRAP_CONTENT);
            }else {
                popupWindow = new PopupWindow(view, v.getWidth() * 1, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }else {
            popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, v.getHeight());
            Button xianj = (Button) view.findViewById(R.id.pay_xianjin);
            Button yinhangk = (Button) view.findViewById(R.id.pay_yinhangka);
            Button network = (Button) view.findViewById(R.id.pay_network);
            xianj.setOnClickListener(this);
            yinhangk.setOnClickListener(this);
            network.setOnClickListener(this);
        }
    }
    //---------------------------------------------------------------------
	/**
	 * 简化Toast显示
	 * @param text 显示内容
	 */
	public void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
    /**
     * 简化Toast显示
     * @param id 显示内容
     */
    public void showToast(int id){
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
    }
	/**
	 * 屏蔽机械返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            cancelHandle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
