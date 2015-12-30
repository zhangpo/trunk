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
 * �������Activity
 *@Author:M.c
 *@CreateDate:2014-1-6
 *@Email:JNWSCZH@163.COM
 */
public class SettleAccountsActivity extends BaseActivity implements OnClickListener{
	private FragmentManager manager;
	private FragmentTransaction transaction;
    private PopupWindow popupWindow=null;//������
	/**
	 * ��ʾ��
	 */
	private LoadingDialog dialog;
	/**
	 * ���ڽ���̨λ��Ϣ
	 */
	private Bundle bundle;
	/**
	 * ���ݿ⹫�ò�ѯ��� ������
	 */
	private ListProcessor pro;
	/**
	 * ��Աʵ��
	 */
	private VipRecord vip;
	/**
	 * �û�����,̨λ����,�˵���
	 */
	private TextView userNum, tableNum,orderNum;
	private ImageView menuinfo_imageView;
	private View layout;
	private LinearLayout topLinearLayout;
	private Button back;
	/**
	 * ������,Ů����
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
			TsData.initData();//��ʼ���ݴ�����
		}
		setBundle(this.getIntent().getExtras());//��ȡIntent������������
		if (ChioceActivity.ispad) {
			this.setContentView(R.layout.activity_settle_accounts_pad);
		} else {
			this.setContentView(R.layout.activity_settle_accounts);
		}
		//=====================button==============
		back=(Button)this.findViewById(R.id.SA_back);//���ذ�ť
		Button huiYuanKa=(Button)this.findViewById(R.id.SA_huiYuanKa);//��Ա��
		Button quXiaoPay=(Button)this.findViewById(R.id.SA_quXiaoPay);//ȡ��֧��
		Button quXiaoPrivilege=(Button)this.findViewById(R.id.SA_quXiaoPrivilege);//ȡ���Ż�
		Button xianJin=(Button)this.findViewById(R.id.SA_pay);//֧����ť
		Button yinHangKa=(Button)this.findViewById(R.id.SA_yinHangKa);//���п�����
		Button yuDaYin=(Button)this.findViewById(R.id.SA_yuDaYin);//Ԥ��ӡ
		Button weixin=(Button)this.findViewById(R.id.SA_winxin_up);//΢���ϴ�

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
			back=(Button)this.findViewById(R.id.eatable_btn_back);//���ذ�ť
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
		//��ֵ����Ա
		userNum.setText(SharedPreferencesUtils.getOperator(this));
		//���ƶ�����
		orderNum.setText(getBundle().getString("orderId"));
		if (SingleMenu.getMenuInstance().getOrderNum()==null||SingleMenu.getMenuInstance().getOrderNum().length()==0){
			SingleMenu.getMenuInstance().setOrderNum(getBundle().getString("orderId"));
		}
		//��ֵ̨λ
		String table=getBundle().getString("tableNum");//��̨λTextView��ֵ
		tableNum.setText(ValueUtil.isNotEmpty(table)?table:"");
		List<VipRecord> vip=new VipRecordUtil().queryHandle(this, table);//ͨ��̨λȥ���ؿ��ѯ��Ա��Ϣ
		TextView cardNumTitle=(TextView)findViewById(R.id.vipANumText);//��ԱText
		TextView cardNum=(TextView)findViewById(R.id.vipANumText1);//��Ա��Text
		if(ValueUtil.isNotEmpty(vip)){//�ж����л�Ա��Ϣ
			VipMsg.iniVip(this, table,R.id.settle_vipMsg_Img);//��ʼ�����Ͻǻ�Ա��Ϣ��ʶ
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
	 * ��ʼ��Fragment
	 */
	@Override
	protected void onResume() {
		manager=getFragmentManager();
		transaction=manager.beginTransaction();
        BillFragment billF=new BillFragment();//�˵���ϢFragment
        PrivilegeFragment privilegeF=new PrivilegeFragment();//�Ż�ȯFragment
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
		case R.id.SA_back://����
			cancelHandle();
			break;
		case R.id.eatable_btn_back://����
			cancelHandle();
			break;
		case R.id.SA_huiYuanKa://��Ա������
			findVipCard();
			break;
		case R.id.SA_quXiaoPay://ȡ��֧��
			if(ValueUtil.isNotEmpty(TsData.member)){//�ж��Ƿ���ڻ�Ա����
				cancelVip(TsData.member.size()-1);
				break;
			}
			cancelPay();
			break;
		case R.id.SA_quXiaoPrivilege://ȡ���Ż�
			cancel("cancleUserCounp","cancleUserCounp",1);
			break;
		case R.id.SA_pay://�ֽ�֧����
            vipPrice((Button) v);
			break;
		case R.id.pay_xianjin://�ֽ�֧��
            payAlert(new String[]{"5"});
            break;
        case R.id.pay_yinhangka://���п�
			payment(new String[]{"31"});
            break;
        case R.id.SA_winxin_up://΢���ϴ�
            weiXinUp();
            break;
        case R.id.pay_network://����
            payment(new String[]{"50","48"});
            break;
		case R.id.SA_yuDaYin://Ԥ��ӡ
			cancel("priPrintOrder","priPrintOrder",2);
			break;
		case R.id.orderinfo_ImageView://������Ϣ
			View layout = null ;
			layout = LayoutInflater.from(SettleAccountsActivity.this).inflate(R.layout.menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			//��ֵ����Ա
			userNum.setText(SharedPreferencesUtils.getOperator(this));
			//���ƶ�����
			orderNum.setText(getBundle().getString("orderId"));
			//��ֵ̨λ
			String table=getBundle().getString("tableNum");//��̨λTextView��ֵ
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
	        lp.x = 0; // ��λ��X����
	        lp.y = topLinearLayout.getHeight() -15;// ��λ��Y����
	        dialogWindow.setAttributes(lp);
			tipdialog.show();
			break;
		}
	}

    /**
     * ΢���ϴ�
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
	 * ��Ա֧��ȡ��
	 */
	public void cancelVip(final int size){
		//TODO ��Ա����ͨ�� ���ݿ��ѯ,���ܻ�������
		try {
			setVip(new VipRecordUtil().queryHandle(this, getBundle().getString("tableNum")).get(0));
		} catch (Exception e) {
			Log.e("SettleAccountsActivity-��ѯ���ݴ���", e.getMessage());
		}
		CList<Map<String,String>> list=new CList<Map<String,String>>();
		list.add("deviceId", SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));
		list.add("userCode", SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
		list.add("cardNumber", getVip().getCardNumber().trim());
		list.add("trace", TsData.member.get(size).getPayTrace().trim());
		list.add("printtye", "2");
		list.add("cardPWD", "");//ȡ������Ҫ����
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
						TsData.member = null;//��������ݴ��¼
						cancelPay();
					} else {//���ȡ���ķ����һ���ݹ����ȡ��
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
	 * ���ط���
	 */
	public void cancelHandle(){
		if(ValueUtil.isNotEmpty(TsData.coupPay)||ValueUtil.isNotEmpty(TsData.moneyPay)){
			new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.hint).setMessage(R.string.bill_Paid_not_cancle).setPositiveButton(R.string.confirm, null).show();
		}else{
			this.finish();
		}
	}
	/**
	 * �ֽ�����п�֧��
	 * @param mark
	 */
	public void payment(String[] mark){
        String phr="?";
        for(int i=1;mark!=null&i<mark.length;i++){
            phr+=",?";
        }
		//�ӱ��ؿ��ȡ֧����ʽ
		List<SettlementOperate> list=getPro().query("select operate,operatename,operatevalue,operategroupid from settlementoperate where operategroupid in("+phr+")", mark,this, SettlementOperate.class);
		LayoutInflater layoutInflater = LayoutInflater.from(SettleAccountsActivity.this);
		//ͨ��infalte��ȡpopupWindow����
		View popupWindow = layoutInflater.inflate(R.layout.custom_popup_window, null); 
		//����������
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
		 * ���ݲ�ѯ������ѭ������but
		 */
		for (SettlementOperate operate:list) {
			Button but=new Button(SettleAccountsActivity.this);
			but.setTextColor(Color.WHITE);
			but.setText(operate.getOperateName());
			but.setTag(operate);
			but.setTextSize(14);
			but.setBackgroundResource(R.drawable.blue_button_background);
			but.setOnClickListener(new OnClickListener() {//Ϊbut����¼�
                @Override
                public void onClick(View paramView) {
                    if (ValueUtil.isEmpty(paramView.getTag())) {//�ж�but��Tag�Ƿ�Ϊ��Tag�����Ż���Ϣ
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
     * ΢��֧����֧��
     */
    public void execute_network(SettlementOperate set){
        if (ValueUtil.isEmpty(set)) {//�ж�but��Tag�Ƿ�Ϊ��Tag�����Ż���Ϣ
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
            case 48://΢��
            case 50://֧����
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    SettlementOperate set= (SettlementOperate) bundle.getSerializable("SettlementOperate");
                    scanCodePay(bundle.getString("result"),bundle.getString("money"),requestCode==48?"2":"1",set);
                }
                break;
        }
    }

    /**
     * ����ɨ��֧���ӿ�
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
     * ƴ��ɨ��֧��JSON
     * @param code
     * @param money
     * @param type
     * @param slo
     * @return
     */
    public String scanJson(String code,String money,String type,SettlementOperate slo){
        ScanCodePay codePay=new ScanCodePay();
        codePay.setOperate(slo.getOperate());//����
        codePay.setAuth_code(code);//ɨ���ά��������
        codePay.setTotal_fee(money);//Ӧ�����
        codePay.setOrderid(getBundle().getString("orderId"));
        codePay.setFinished("1");//�Ƿ�֧����� 0 δ�� 1 ���
        codePay.setType(type);//1 ֧���� 2 ΢��֧��
        String json= new Gson().toJson(codePay);
        Log.e("ɨ��֧��JSON", json);
        return json;
    }
	/**
	 * ��֧ͨ������֧��
	 * @param settlementOperate
	 */
	public void new_exc_Payment(final SettlementOperate settlementOperate){

		if (settlementOperate==null){
			return;
		}
//		if(ValueUtil.isEmpty(paramView.getTag())){//�ж�but��Tag�Ƿ�Ϊ��Tag�����Ż���Ϣ
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
					Toast.makeText(SettleAccountsActivity.this, "¼�����Ϊ��", Toast.LENGTH_LONG).show();
				}else if (Double.parseDouble(text.getText().toString())>Double.parseDouble(yingF)&&set.getOperateGroupid().equals("31")){
					Toast.makeText(SettleAccountsActivity.this, "������������¼��", Toast.LENGTH_LONG).show();
				}else {

					fPay(set, text.getText().toString());
					dialog.dismiss();
				}
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {//ȡ���¼�
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Field field = null;
				//ͨ�������ȡdialog�е�˽������mShowing
				try {
					field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);//���ø����Կ��Է���
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
		 *  ����С��λ������
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
     * ��֧ͨ������֧��
     * @param settlementOperate
     */
    public void exc_Payment(final SettlementOperate settlementOperate){

		if (settlementOperate==null){
			return;
		}
//		if(ValueUtil.isEmpty(paramView.getTag())){//�ж�but��Tag�Ƿ�Ϊ��Tag�����Ż���Ϣ
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
					Toast.makeText(SettleAccountsActivity.this, "¼�����Ϊ��", Toast.LENGTH_LONG).show();
				}else if (Double.parseDouble(text.getText().toString())>Double.parseDouble(yingF)&&set.getOperateGroupid().equals("31")){
					Toast.makeText(SettleAccountsActivity.this, "������������¼��", Toast.LENGTH_LONG).show();
				}else {

					fPay(set, text.getText().toString());
				}
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {//ȡ���¼�
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Field field = null;
				//ͨ�������ȡdialog�е�˽������mShowing
				try {
					field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);//���ø����Կ��Է���
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
		 *  ����С��λ������
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
//	public void moneyPay(DialogInterface dialog,View paramView){
//		String money=((EditText)((AlertDialog)dialog).getCurrentFocus()).getText().toString();
//		SettlementOperate set=(SettlementOperate)paramView.getTag();
//		Field field = null;
//		//ͨ�������ȡdialog�е�˽������mShowing
//        try {
//			field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//			field.setAccessible(true);//���ø����Կ��Է���
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		}
//		if(ValueUtil.isEmpty(money)){
//			showToast(R.string.money_not_null);
//			try {
//				field.set(dialog, false);//��ֹ������ر�
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
//		try {//ִ�гɹ���رջ���
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
		//TODO ͨ�����ݿ��ȡ vip ��Ϣ
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
				//	�˵��ţ�֧����ʽ���룬������ ֧�����
				final String[] str=ValueUtil.isEmpty(result)?null:result.split("@");
				if(str!=null&&str[0].equals("0")){//�ж��Ƿ�֧���ɹ�
					ToastUtil.toast(SettleAccountsActivity.this, R.string.execute_success);
					if (Double.parseDouble(yingF)-Double.parseDouble(money)<=0){
						String mes=getString(R.string.pay_success);
						ViewUtil.setVew(SettleAccountsActivity.this,payMentCode.getOperateName(),money,false,new IResult<String>() {
							@Override
							public void result(String t) {
								new FinalPay().fPay(getBundle(), SettleAccountsActivity.this, ValueUtil.isNaNofDouble(t));
							}
						});
//						if(Double.parseDouble(yingF)-Double.parseDouble(money)<0){//�ж��Ƿ���Ҫ����
//							mes=getString(R.string.pay_success_zhaoL)+(-Double.parseDouble(yingF)+Double.parseDouble(money))+getString(R.string.money_unit);
//						}
//						TsData.isEnd=true;
//						new VipRecordUtil().delHandle(SettleAccountsActivity.this, SingleMenu.getMenuInstance().getTableNum());//ɾ�����ܸ�̨λ����
//						new AlertDialog.Builder(SettleAccountsActivity.this)//֧�������ʾ
//								.setTitle(R.string.pay_success)
//								.setMessage(mes)
//								.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//										if(ValueUtil.isEmpty(TsData.coupPay)&&ValueUtil.isEmpty(TsData.member)&&TsData.moneyPay!=null){//�ж��Ƿ���Ҫ����Ʊ
//											invoice(SettleAccountsActivity.this,SingleMenu.getMenuInstance().getOrderNum(),(-Double.parseDouble(yingF)+Double.parseDouble(money)));//ִ�п���Ʊ
//										}else{
//											backTable(SettleAccountsActivity.this);//��������
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
	 * ���ط���
	 * @param activity
	 */
	public void backTable(Activity activity){
		Intent intent=new Intent(activity,MainActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}
	/**
	 * ֧����֤
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
					List<SettlementOperate> list=getPro().query("select operate,operatename,operatevalue,operategroupid from settlementoperate where operatename ='�ֽ�'", null,this, SettlementOperate.class);
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
	 * ���ô���-�������ֻ�С�deviceId����userCode����tableNum����orderId���ɹ�
	 * @param model
	 * @param url
	 */
	public void cancel(String model,String url,final int mark){
		CList<Map<String,String>> params=new CList<Map<String,String>>();
		params.add("deviceId",SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));// cancleUserCounp��String deviceId, String userCode ,String tableNum ,String orderId )
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
								TsData.coupPay=null;//����Ż��ݴ�����
								//ˢ��BillFragment �����˵���Ϣ
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
	 * ȡ��֧��
	 */
	public void cancelPay(){
		CList<Map<String,String>> params=new CList<Map<String,String>>();
		params.add("deviceId",SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));// cancleUserCounp��String deviceId, String userCode ,String tableNum ,String orderId )
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
							TsData.moneyPay=null;//����ݴ� �ֽ�����
							if(ValueUtil.isEmpty(TsData.coupPay)){//����Ż�Ϊ��������������Ϊ�ռ���ִ�в�ˢ���˵�
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
	 * ʹ�û�Ա��
	 */
	public void findVipCard(){
//		List<VipRecord> vip=null;
//		try {
//			vip=new VipRecordUtil().queryHandle(this, getBundle().getString("tableNum"));
//		} catch (Exception e) {
//			Log.e("SettleAccountsActivity-VipCardHandle-Error", e.getMessage());
//		}
//		if(ValueUtil.isNotEmpty(vip)){
//			new AlertDialog.Builder(this).setTitle("��ʾ").setMessage("�Ƿ���Ļ�Ա�ţ�\n�ֻ��ţ�"+vip.get(0).getPhone()+"\n��Ա�ţ�"+vip.get(0).getCardNumber()).setPositiveButton("��", new DialogInterface.OnClickListener(){
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
//					}).setNegativeButton("��", new DialogInterface.OnClickListener(){
//				@Override
//				public void onClick(DialogInterface paramDialogInterface,int paramInt) {
//					Intent intent = new Intent(SettleAccountsActivity.this,PayMentActivity.class);//�򿪻�Ա֧��ҳ��
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
	 * ��Ա������
	 * ע����ʱ����
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
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {//AlertDialog �ر��¼�
			@Override
			public void onDismiss(DialogInterface paramDialogInterface) {//���ڹر�  ˢ�¶���
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
        final Spinner spinner=(Spinner)popupWindow.findViewById(R.id.PopWin_VipSpinner);//��ȡ�����ռ�ؼ�
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(SettleAccountsActivity.this,android.R.layout.simple_spinner_dropdown_item,new ArrayList<String>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		spinner.setAdapter(adapter);
        final EditText editText=(EditText)popupWindow.findViewById(R.id.PopWin_phoneNumEdit);//��ȡ�ֻ��������ؼ�
        editText.setText(ValueUtil.isNotEmpty(vip)?vip.get(0).getCardNumber():"");
        final Button cancelBut=(Button)popupWindow.findViewById(R.id.PopWin_VipCancelBut);
        final Button queryBut=(Button)popupWindow.findViewById(R.id.PopWin_VipQueryBut);
        /**
         * ��ѯ��ť�����¼�
         */
        queryBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num=editText.getText().toString();
				if(num.matches("^[1][3458][0-9]{9}$")&&ValueUtil.isEmpty(spinner.getSelectedItem())){//�ж��Ƿ�����ֻ�����
					CList<Map<String,String>> cl=new CList<Map<String,String>>();
					cl.add("deviceId", SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));
					cl.add("userCode", SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
					cl.add("phoneNumber", num);
					//�����ֻ������ѯ��Ա��Ϣ
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
					// card_QueryBalance��String deviceId,String userCode ,String cardNumber,String orderId
					CList<Map<String,String>> cl=new CList<Map<String,String>>();
					cl.add("deviceId", SharedPreferencesUtils.getDeviceId(SettleAccountsActivity.this));
					cl.add("userCode", SharedPreferencesUtils.getUserCode(SettleAccountsActivity.this));
					if(ValueUtil.isNotEmpty(num)&&num.length()==16){//���������ǻ�Ա����
						cl.add("cardNumber", num);
					}else if(ValueUtil.isNotEmpty(spinner.getSelectedItem())){//�������Ĳ��ǻ�Ա�������ж����������Ƿ���ѡ������
						cl.add("cardNumber", spinner.getSelectedItem());
					}else{//������϶������������
						showToast(R.string.num_error);
						mark=false;
					}
					cl.add("orderId", getBundle().getString("orderId"));
					if(mark){//�����Ա������������в�ѯ��Ա��Ϣ
						getServer().connect(SettleAccountsActivity.this, "card_QueryBalance", "ChoiceWebService/services/HHTSocket?/card_QueryBalance", cl, new OnServerResponse() {
							@Override
							public void onResponse(String result) {
								getDialog().dismiss();
								String[] str=result==null?null:result.split("@");
								if(ValueUtil.isNotEmpty(str)&&str[0].equals("0")){
									 //0@ CardNumber  @ CardType@ StoredCardsBalance@ IntegralOverall@ CouponsOverall@ CouponsAvail@ TicketInfoList 
									//��Ա����        ��Ա������          ��ֵ���           �������          ȯ��� ȯ       �������             ȯ��Ϣ�б�
									VipRecord vip=new VipRecord();//VipRecordUtil
									vip.setCardNumber(str[1]);			//��Ա����
									vip.setCardType(str[2]);  			//��Ա������
									vip.setStoredCardsBalance(ValueUtil.isNaNofDouble(str[3]));	//��ֵ���
									vip.setIntegralOverall(ValueUtil.isNaNofDouble(str[4]));   	//�������
									vip.setCouponsOverall(ValueUtil.isNaNofDouble(str[5]));		//ȯ��� ȯ
									vip.setCouponsAvail(ValueUtil.isNaNofDouble(str[6]));		//�������
									vip.setOrderId(getBundle().getString("orderId"));
									vip.setPhone(editText.getText().toString().matches("^[1][3458][0-9]{9}$")?editText.getText().toString():"");
									vip.setTableNum(getBundle().getString("tableNum"));
									vip.setManCounts(ValueUtil.isNaNofInteger(getBundle().getString("manCounts")));
									vip.setWomanCounts(ValueUtil.isNaNofInteger(getBundle().getString("womanCounts")));
									vip.setPayable(ValueUtil.isNaNofDouble(tview.getText().toString()));
									vip.setTicketInfoList("");
									if(str.length>7){//�ж��ٶ��Ƿ����7������7֤�����С�ȯ��Ϣ�б�
										vip.setTicketInfoList(str[7]);
									}
									new VipRecordUtil().insertHandle(SettleAccountsActivity.this, vip);//�ѻ�Ա������ӵ�����
									//---------------------------------------------------
									/*Bundle bundle=new Bundle();
									bundle.putString("CardNumber", str[1]);			//��Ա����
									bundle.putString("CardType", str[2]);  			//��Ա������
									bundle.putString("StoredCardsBalance", str[3]);	//��ֵ���
									bundle.putString("IntegralOverall", str[4]);   	//�������
									bundle.putString("CouponsOverall", str[5]);		//ȯ��� ȯ
									bundle.putString("CouponsAvail", str[6]);		//�������
									bundle.putBundle("BillBundle", getBundle());			//���������˵���Ϣ����->YiXuanDishActivity2
									bundle.putString("TicketInfoList", "");			//ȯ��Ϣ�б�
									if(str.length>7){//�ж��ٶ��Ƿ����7������7֤�����С�ȯ��Ϣ�б�
										bundle.putString("TicketInfoList", str[7]);
									}*/
									Intent intent = new Intent(SettleAccountsActivity.this,PayMentActivity.class);//�򿪻�Ա֧��ҳ��
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
         * ȷ�ϰ�ť�¼�
         */
        cancelBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				alert.dismiss();
			}
		});
        /**
         * ���������Ϣ�����ж� ����
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
    //ѡ��֧������
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
	 * ��Toast��ʾ
	 * @param text ��ʾ����
	 */
	public void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
    /**
     * ��Toast��ʾ
     * @param id ��ʾ����
     */
    public void showToast(int id){
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
    }
	/**
	 * ���λ�е���ؼ�
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
