package cn.com.choicesoft.chinese.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
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
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.chinese.bean.ChinesePayMent;
import cn.com.choicesoft.chinese.bean.ChinesePayTyp;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.events.ChineseVipChoice;
import cn.com.choicesoft.chinese.fragment.ChineseBillFragment;
import cn.com.choicesoft.chinese.fragment.ChinesePrivilegeFragment;
import cn.com.choicesoft.chinese.util.ChineseResultAlt;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;
import cn.com.choicesoft.view.VipMsg;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * �������Activity-�в�
 * @Author:M.c
 * @CreateDate:2014-1-6
 * @Email:JNWSCZH@163.COM
 */
public class ChineseSettleAccountsActivity extends Activity implements OnClickListener{
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private LoadingDialog dialog;//��ʾ��
	private ListProcessor pro;//���ݿ⹫�ò�ѯ��� ������
	private VipRecord vip;//��Աʵ��
    private String tableNumber;
    private String orderNumber;
    public AlertDialog typDialog,mentDialog;
	private TextView userNum;
	private TextView orderNum;
	private ImageView menuinfo_imageView;
	private View layout;
	private LinearLayout topLinearLayout;
	private TextView peopleNum;
	private TextView tableNum;
	private Button back;
	private Integer mResultCode=null;
	private Intent mData;

	/**
	 * ��ȡ���ݿ�����
	 * @return
	 */
	public ListProcessor getPro() {
		if(pro==null){
			pro=new ListProcessor();
		}
		return pro;
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
		if (ChioceActivity.ispad) {
			this.setContentView(R.layout.chinese_activity_settle_accounts_pad);
		} else {
			this.setContentView(R.layout.chinese_activity_settle_accounts);
		}
        tableNumber=this.getIntent().getStringExtra("table");
        orderNumber= SingleMenu.getMenuInstance().getMenuOrder();
		//=====================button==============
        back=(Button)this.findViewById(R.id.SA_back);//���ذ�ť
		Button huiYuanKa=(Button)this.findViewById(R.id.SA_huiYuanKa);//��Ա��
		Button quXiaoPay=(Button)this.findViewById(R.id.SA_quXiaoPay);//ȡ��֧��
		Button quXiaoPrivilege=(Button)this.findViewById(R.id.SA_quXiaoPrivilege);//ȡ���Ż�
		Button xianJin=(Button)this.findViewById(R.id.SA_xianJin);//�ֽ����
		Button yinHangKa=(Button)this.findViewById(R.id.SA_yinHangKa);//���п�����
		Button yuDaYin=(Button)this.findViewById(R.id.SA_yuDaYin);//Ԥ��ӡ

		Button smallLift=(Button)this.findViewById(R.id.Small_Life);//Ԥ��ӡ
		Button pay=(Button)this.findViewById(R.id.SA_pay);//Ԥ��ӡ
		if (SingleMenu.getMenuInstance().getPermissionList()!=null) {
			for (Map<String, String> map : SingleMenu.getMenuInstance().getPermissionList()) {
				if (map.get("CODE").equals("51001")){
					quXiaoPay.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						quXiaoPay.setVisibility(View.GONE);
					}
				}
				if (map.get("CODE").equals("51002")){
					quXiaoPrivilege.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						quXiaoPrivilege.setVisibility(View.GONE);
					}
				}
				if (map.get("CODE").equals("51003")){
					pay.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						pay.setVisibility(View.GONE);
					}
				}

				if (map.get("CODE").equals("51006")){
					yuDaYin.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						yuDaYin.setVisibility(View.GONE);
					}
				}
				if (map.get("CODE").equals("51007")){
					smallLift.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						smallLift.setVisibility(View.GONE);
					}
				}
			}
		}
        //if(!SharedPreferencesUtils.getIsVip(this)){TODO �Ƿ��Ա
            //huiYuanKa.setVisibility(View.GONE);
        //}
		back.setOnClickListener(this);
		huiYuanKa.setOnClickListener(this);
		quXiaoPay.setOnClickListener(this);
		quXiaoPrivilege.setOnClickListener(this);
		xianJin.setOnClickListener(this);
		yinHangKa.setOnClickListener(this);
		yuDaYin.setOnClickListener(this);
        pay.setOnClickListener(this);
		smallLift.setOnClickListener(this);
		if (ChioceActivity.ispad) {
			userNum = (TextView)this.findViewById(R.id.operatorNames);
			orderNum = (TextView)this.findViewById(R.id.vip_orderText1);
			tableNum=(TextView)this.findViewById(R.id.vip_taiweiText1);
			peopleNum = (TextView)this.findViewById(R.id.vip_peopleNumber);
		} else {
			// TODO 
			back=(Button)this.findViewById(R.id.eatable_btn_back);//���ذ�ť
			back.setOnClickListener(this);
			menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
			menuinfo_imageView.setClickable(true);
			menuinfo_imageView.setOnClickListener(this);
			layout = LayoutInflater.from(ChineseSettleAccountsActivity.this).inflate(R.layout.chinese_menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			peopleNum = (TextView) layout.findViewById(R.id.dishesact_tv_people);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			topLinearLayout = (LinearLayout) findViewById(R.id.vip_top);
		}
		//=====================button==============
		peopleNum.setText(ChineseBillFragment.people);
		//��ֵ����Ա
		userNum.setText(SharedPreferencesUtils.getOperator(this));
		//���ƶ�����
		orderNum.setText(orderNumber);
		//��ֵ̨λ
		tableNum.setText(ValueUtil.isNotEmpty(tableNumber)?tableNumber:"");
		List<VipRecord> vip=new VipRecordUtil().queryHandle(this, tableNumber);//ͨ��̨λȥ���ؿ��ѯ��Ա��Ϣ
		TextView cardNumTitle=(TextView)findViewById(R.id.vipANumText);//��ԱText
		TextView cardNum=(TextView)findViewById(R.id.vipANumText1);//��Ա��Text
		if(ValueUtil.isNotEmpty(vip)){//�ж����л�Ա��Ϣ
			VipMsg.iniVip(this, tableNumber,R.id.settle_vipMsg_Img);//��ʼ�����Ͻǻ�Ա��Ϣ��ʶ
			cardNumTitle.setVisibility(TextView.VISIBLE);
			cardNum.setVisibility(TextView.VISIBLE);
			cardNum.setText(vip.get(0).getCardNumber());
		}
		


		
	}

//	/**
//	 * ��ʼ��Fragment
//	 */
//	@Override
//	protected void onResume() {
//        refresh();
//        super.onResume();
//	}
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
            quertPayments();
			break;
		case R.id.SA_xianJin://�ֽ�
			payAlert("5");
			break;
		case R.id.SA_yinHangKa://���п�
			payAlert("31");
			break;
		case R.id.SA_yuDaYin://Ԥ��ӡ
			printOrderByServer();
			break;
        case R.id.SA_pay://֧��
            payTyp();
            break;
		case R.id.orderinfo_ImageView://������Ϣ
			// TODO   
			View layout = null ;
			layout = LayoutInflater.from(ChineseSettleAccountsActivity.this).inflate(R.layout.chinese_menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			peopleNum = (TextView) layout.findViewById(R.id.dishesact_tv_people);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			//��ֵ����Ա
			userNum.setText(SharedPreferencesUtils.getOperator(this));
			//���ƶ�����
			orderNum.setText(orderNumber);
			//��ֵ̨λ
			tableNum.setText(ValueUtil.isNotEmpty(tableNumber)?tableNumber:"");
			if (ChineseBillFragment.people!=null ) {
				peopleNum.setText(ChineseBillFragment.people);
			}
			AlertDialog tipdialog = new AlertDialog.Builder(ChineseSettleAccountsActivity.this, R.style.Dialog_tip).setView(layout).create();
			tipdialog.setCancelable(true);
	        Window dialogWindow = tipdialog.getWindow();
	        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	        dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
	        lp.x = 0; // ��λ��X����
	        lp.y = topLinearLayout.getHeight() -15; // ��λ��Y����
	        dialogWindow.setAttributes(lp);
			tipdialog.show();
			break;
			case R.id.Small_Life:
				vipPayConfirm();
				break;
		}
	}
	/**
	 * ��Ա֧��ȡ��
	 */
	public void cancelVip(final int size){
        //TODO
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
	public void payMent(final String mark){
        List<ChinesePayMent> list;
        list = getPro().query("select payment as cod,des,typ,foreigntag,code from payment where typ=?", new String[]{mark}, this, ChinesePayMent.class);
		LayoutInflater layoutInflater = LayoutInflater.from(ChineseSettleAccountsActivity.this);
		//ͨ��infalte��ȡpopupWindow����
		View popupWindow = layoutInflater.inflate(R.layout.custom_popup_window, null); 
		//����������
        mentDialog = new AlertDialog.Builder(ChineseSettleAccountsActivity.this,R.style.edittext_dialog).setView(popupWindow).show();
		LinearLayout linearLayout=(LinearLayout)popupWindow.findViewById(R.id.popupWind);
		linearLayout.setPadding(15, 25, 15, 25);
		DisplayMetrics dmm = new DisplayMetrics();
        mentDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dmm);
		int dialogWidth  =  dmm.widthPixels;
		LinearLayout dLin=new LinearLayout(ChineseSettleAccountsActivity.this);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
		LinearLayout.LayoutParams linearlayoutlp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		dLin.setLayoutParams(linearlayoutlp);
		params.setMargins(3, 3, 3, 3);
		/**
		 * ���ݲ�ѯ������ѭ������but
		 */
		for (final ChinesePayMent payMent:list) {
			Button but=new Button(ChineseSettleAccountsActivity.this);
			but.setTextColor(Color.WHITE);
			but.setText(payMent.getDes());
			but.setTag(payMent);
			but.setTextSize(14);
			but.setBackgroundResource(R.drawable.blue_button_background);
			but.setOnClickListener(new OnClickListener() {//Ϊbut�����¼�
				@Override
				public void onClick(final View paramView) {
					if(ValueUtil.isEmpty(paramView.getTag())){//�ж�but��Tag�Ƿ�Ϊ��Tag�����Ż���Ϣ
						return;
					}
					final EditText text=new EditText(ChineseSettleAccountsActivity.this);
					LinearLayout layout = new LinearLayout(ChineseSettleAccountsActivity.this);
					layout.setOrientation(LinearLayout.VERTICAL);
					text.setHint(R.string.please_input_money);
					text.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
					params.setMargins(16, 12, 16, 12);
					text.setText(((TextView)ChineseSettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText());
                    text.setSelection(0,((TextView)ChineseSettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().length());
                    
					layout.addView(text,params);
					final AlertDialog dialog=new AlertDialog.Builder(ChineseSettleAccountsActivity.this,R.style.edittext_dialog)
					.setTitle(R.string.input_money)
					.setView(layout)
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String money=text.getText().toString();
                            if (!ValueUtil.isNaN(money)){
                                money="0";
                            }
                            moneyPay(money, ((ChinesePayMent) paramView.getTag()).getCod());
                        }
                    }).setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {//ȡ���¼�
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
                            }).show();
					text.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							if (KeyEvent.KEYCODE_ENTER == keyCode&& event.getAction() == KeyEvent.ACTION_DOWN) {
                                String money=text.getText().toString();
                                if (!ValueUtil.isNaN(money)){
                                    money="0";
                                }
                                moneyPay(money, ((ChinesePayMent) paramView.getTag()).getCod());
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
								imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							}
							return false;
						}
					});
				}
			});
			dLin.addView(but,params);
			if(dLin.getChildCount()==3){
				linearLayout.addView(dLin);
				dLin=new LinearLayout(ChineseSettleAccountsActivity.this);
			}
		}
		if(dLin.getChildCount()>0){
			linearLayout.addView(dLin);
		}
	}
    /**
	 * �ֽ�����п�֧��
	 */
	public void payTyp(){
        List<ChinesePayTyp> list;
        //�ӱ��ؿ��ȡ֧����ʽ
        list = getPro().query("select cod,des,code from paytyp", null, this, ChinesePayTyp.class);
		LayoutInflater layoutInflater = LayoutInflater.from(ChineseSettleAccountsActivity.this);
		//ͨ��infalte��ȡpopupWindow����
		View popupWindow = layoutInflater.inflate(R.layout.custom_popup_window, null);
		//����������
        typDialog = new AlertDialog.Builder(ChineseSettleAccountsActivity.this,R.style.edittext_dialog).setView(popupWindow).show();
		LinearLayout linearLayout=(LinearLayout)popupWindow.findViewById(R.id.popupWind);
		linearLayout.setPadding(15, 25, 15, 25);
		DisplayMetrics dmm = new DisplayMetrics();
        typDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dmm);
		int dialogWidth  =  dmm.widthPixels;
		LinearLayout dLin=new LinearLayout(ChineseSettleAccountsActivity.this);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
		LinearLayout.LayoutParams linearlayoutlp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		dLin.setLayoutParams(linearlayoutlp);
		params.setMargins(3, 3, 3, 3);
		/**
		 * ���ݲ�ѯ������ѭ������but
		 */
		for (final ChinesePayTyp payTyp:list) {
			Button but=new Button(ChineseSettleAccountsActivity.this);
			but.setTextColor(Color.WHITE);
			but.setText(payTyp.getDes());
			but.setTag(payTyp);
			but.setTextSize(14);
			but.setBackgroundResource(R.drawable.blue_button_background);
			but.setOnClickListener(new OnClickListener() {//Ϊbut�����¼�
				@Override
				public void onClick(final View paramView) {
					if(ValueUtil.isEmpty(paramView.getTag())){//�ж�but��Tag�Ƿ�Ϊ��Tag�����Ż���Ϣ
						return;
					}
                    payMent(((ChinesePayTyp)paramView.getTag()).getCod());
				}
			});
			dLin.addView(but,params);
			if(dLin.getChildCount()==3){
				linearLayout.addView(dLin);
				dLin=new LinearLayout(ChineseSettleAccountsActivity.this);
			}
		}
		if(dLin.getChildCount()>0){
			linearLayout.addView(dLin);
		}
	}
	/**
	 * ���п����ֽ�֧��
	 * @param mark
	 */
	public void payAlert(final String mark){
		if(TsData.moneyPay!=null){
			payMent(mark);
		}else{
			new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.hint).setMessage(R.string.pay_money_not_privilege)
			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface paramDialogInterface,int paramInt) {
                    payMent(mark);
				}
			}).setNegativeButton(R.string.cancle, null).show();
		}
	}
	
	/**
	 * ȡ���Ż�
	 */
	public void cancelActm(String cardPwd){
        String user = SharedPreferencesUtils.getUserCode(this).split("-")[0];
        String pwd = SharedPreferencesUtils.getUserCode(this).split("-")[1];
        CList<Map<String,String>> list=new CList<Map<String, String>>();
        list.addMap("json","{'order'='"+orderNumber+"',user='"+user+"','password'='"+pwd+"','cardPassword'='"+cardPwd+"'}");
        new ChineseServer().connect(this, ChineseConstants.CANCEL_ACTM, list, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getDialog().dismiss();
                if (ValueUtil.isNotEmpty(result)) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject res = jsonObject.getJSONObject("result");
                        String state=res.getString("state");
                        if (state.equals("1")) {
                            ToastUtil.toast(ChineseSettleAccountsActivity.this,R.string.execute_success);
                            refresh();
                        } else {
                            ToastUtil.toast(ChineseSettleAccountsActivity.this, jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
     * ��ȡ�Żݷ�ʽ
     */
    public void quertPayments(){
        CList<Map<String,String>> list=new CList<Map<String, String>>();
        list.addMap("pdaid",SharedPreferencesUtils.getDeviceId(this));
        list.addMap("user",SharedPreferencesUtils.getUserCode(this));
        list.addMap("folioNo",orderNumber);
        new ChineseServer().connect(this, ChineseConstants.QUERY_PAYMENTS, list, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getDialog().dismiss();
                if (ValueUtil.isNotEmpty(result)) {
                    Map<String, String> map = new ComXmlUtil().xml2Map(result);
                    if (ValueUtil.isEmpty(map)) {
                        ToastUtil.toast(ChineseSettleAccountsActivity.this, R.string.query_actm_error);
                        return;
                    }
                    if ("1".equals(map.get("result"))) {
                        String oStr=map.get("oStr");
                        oStr=oStr.substring(oStr.indexOf("ok:")+3,oStr.indexOf(">"));
                        String actms[]=oStr.split("\\^");
                        boolean isPwd=false;
                        for(int i=0;actms!=null&&i<actms.length&&actms.length>0;i++){
                            if(actms[i].split("@")[2].equals("6")){
                                isPwd=true;
                            }
                        }
                        if (isPwd) {
                            pwdVer();
                        }else{
                            cancelActm("");
                        }
                    } else {
                        ToastUtil.toast(ChineseSettleAccountsActivity.this, map.get("oStr"));
                    }
                } else {
                    ToastUtil.toast(ChineseSettleAccountsActivity.this, R.string.query_actm_null);
                }
            }
            @Override
            public void onBeforeRequest() {

            }
        });
    }
    public void pwdVer(){
        LinearLayout layout=(LinearLayout)this.getLayoutInflater().inflate(R.layout.verify_pwd, null);
        TextView textView= (TextView) layout.findViewById(R.id.verify_pwd_Text);
        textView.setText(R.string.vip_pwd);
        Button confirm=(Button)layout.findViewById(R.id.verify_pwd_confirm);
        Button cancel=(Button)layout.findViewById(R.id.verify_pwd_cancel);
        final EditText vip_pwd= (EditText) layout.findViewById(R.id.verify_pwd_Edit);
        final AlertDialog alertDialog=new AlertDialog.Builder(this,R.style.edittext_dialog)
                .setTitle(R.string.pwd_ver)
                .setView(layout).show();
        confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelActm(vip_pwd.getText().toString());
				alertDialog.dismiss();
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
    }
	/**
	 * ȡ��֧��
	 */
	public void cancelPay(){
        CList list=new CList();
        list.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        list.add("user",SharedPreferencesUtils.getUserCode(this));
        list.add("serial", orderNumber);
        new ChineseServer().connect(this, ChineseConstants.CANCELPAYMENT, list, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				if (ValueUtil.isNotEmpty(result)) {
					Map<String, String> map = new ComXmlUtil().xml2Map(result);
					if (ValueUtil.isEmpty(map)) {
						ToastUtil.toast(ChineseSettleAccountsActivity.this, R.string.data_error);
						return;
					}
					if ("1".equals(map.get("result"))) {
						ToastUtil.toast(ChineseSettleAccountsActivity.this, R.string.cancelPayment_success);
						refresh();
					} else {
						ToastUtil.toast(ChineseSettleAccountsActivity.this, R.string.data_error);
					}
				} else {
					ToastUtil.toast(ChineseSettleAccountsActivity.this, R.string.data_null);
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
        LinearLayout findVip=(LinearLayout)this.getLayoutInflater().inflate(R.layout.chinese_vip_choice_layout,null);
        final EditText phone=(EditText)findVip.findViewById(R.id.chinese_vip_phone);
        final EditText vipNo=(EditText)findVip.findViewById(R.id.chinese_vip_number);
        final Button confirm=(Button)findVip.findViewById(R.id.chinese_vip_confirm);
        final Spinner spinner=(Spinner)findVip.findViewById(R.id.chinese_vip_cardNo_spinner);
        Button cancel=(Button)findVip.findViewById(R.id.chinese_vip_cancel);
        final Button query=(Button)findVip.findViewById(R.id.chinese_vip_query);
        confirm.setOnClickListener(new ChineseVipChoice(this,findVip));
        cancel.setOnClickListener(new ChineseVipChoice(this,findVip));
        query.setOnClickListener(new ChineseVipChoice(this, findVip));
        phone.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 1) {
					vipNo.setText("");
					query.setVisibility(View.VISIBLE);
					confirm.setVisibility(View.GONE);
					spinner.setVisibility(View.GONE);
				}
			}
		});
        vipNo.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 1) {
					phone.setText("");
					query.setVisibility(View.GONE);
					spinner.setVisibility(View.GONE);
					confirm.setVisibility(View.VISIBLE);
				}
			}
		});
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(findVip).show().getWindow().setLayout(MainActivity.screenWidth / 2, LayoutParams.WRAP_CONTENT);
	}
	/**
	 * ��Ա������ 
	 * ע����ʱ����
	 */
	public void vipCardHandle(){
        //TODO
    }
    /**
     * �ֽ�֧��
     */
    public void moneyPay(final String money, final String ment){
        String yingF=((TextView)ChineseSettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().toString().toString();
        CList<Map<String,String>> list=new CList<Map<String, String>>();
        list.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        list.add("user",SharedPreferencesUtils.getUserCode(this));
        list.add("serial",orderNumber);
        list.add("cnt","1");
        list.add("amt",money);
        list.add("payment",ment);
        list.add("foliounite","0");
        if(ValueUtil.isNaNofDouble(money)>=ValueUtil.isNaNofDouble(yingF)) {
            list.add("flag", "Y");
            list.add("nbzero",((TextView)ChineseSettleAccountsActivity.this.findViewById(R.id.moLJE_Text2)).getText().toString().toString());
        }else{
            list.add("flag", "N");
            list.add("nbzero",0);
        }
        new ChineseServer().connect(this, ChineseConstants.USERPAYMENT,list,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getDialog().dismiss();
                if(ValueUtil.isNotEmpty(result)){
                    Map<String,String> map=new ComXmlUtil().xml2Map(result);
                    if(ValueUtil.isNotEmpty(map)&&"1".equals(map.get("result"))){
                        ToastUtil.toast(ChineseSettleAccountsActivity.this,R.string.pay_success);
                        if(typDialog!=null&&typDialog.isShowing()){
                            typDialog.dismiss();
                        }
                        if(mentDialog!=null&&mentDialog.isShowing()){
                            mentDialog.dismiss();
                        }
                        String yingF=((TextView)ChineseSettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().toString();
                        BigDecimal zhaoL=ValueUtil.isNaNofBigDecimal(money).subtract(ValueUtil.isNaNofBigDecimal(yingF));
                        if(zhaoL.doubleValue()>0){
                            new AlertDialog.Builder(ChineseSettleAccountsActivity.this).setTitle(R.string.hint)
                                    .setMessage(ChineseSettleAccountsActivity.this.getString(R.string.zhaoL)+zhaoL.toString())
                                    .setNegativeButton(R.string.yes,new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            printOrderByServer();//���ӿڴ�ӡ���˵� TODO
                                            Intent intent = new Intent(ChineseSettleAccountsActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    }).show();
                        }else if(zhaoL.doubleValue()==0){
//                            printOrderByServer();//���ӿڴ�ӡ���˵� TODO
                            Intent intent = new Intent(ChineseSettleAccountsActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            refresh();
                        }
                    }else{
                        if(ValueUtil.isEmpty(map)){
                            ToastUtil.toast(ChineseSettleAccountsActivity.this,R.string.net_error);
                            return;
                        }
                        ToastUtil.toast(ChineseSettleAccountsActivity.this, ChineseResultAlt.oStrAlt2(map.get("oStr")));
                    }
                }
            }
            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });
    }
    public void refresh(){
        manager=getFragmentManager();
        transaction=manager.beginTransaction();
        ChineseBillFragment billF=new ChineseBillFragment();//�˵���ϢFragment
        ChinesePrivilegeFragment billP=new ChinesePrivilegeFragment();//�˵���ϢFragment
        billP.setBundle(this.getIntent().getExtras());
        if(ValueUtil.isEmpty(manager.findFragmentByTag("AccLeftLayout"))){
            transaction.add(R.id.setAccLeftLayout, billF,"AccLeftLayout");
        }else{
            transaction.replace(R.id.setAccLeftLayout, billF,"AccLeftLayout");
        }
		if (SingleMenu.getMenuInstance().getPermissionList()!=null) {
			for (Map<String, String> map : SingleMenu.getMenuInstance().getPermissionList()) {
				if (map.get("CODE").equals("11020")) {
					if (map.get("ISSHOW").equals("0")) {
						View yinHangKa=this.findViewById(R.id.setAccRightLayout);
						yinHangKa.setVisibility(View.GONE);
					}
				}
			}
		}
		if (ValueUtil.isEmpty(manager.findFragmentByTag("AccRightLayout"))) {
			transaction.add(R.id.setAccRightLayout, billP, "AccRightLayout");
		} else {
			transaction.replace(R.id.setAccRightLayout, billP, "AccRightLayout");
		}
        transaction.commit();
    }
    /**
     * ���ӿڴ�ӡ��ѯ������˵�
     * @author Young
     * @created 2014-11-11
     * @comment ��1����ѯ�� 2�����˵���
     */
    private void printOrderByServer(){
        CList<Map<String, String>> params = new CList<Map<String,String>>();
        params.add("pdaid", SharedPreferencesUtils.getDeviceId(this));//
        params.add("user", SharedPreferencesUtils.getUserCode(this));//
        params.add("serial", orderNumber);//�˵���
        params.add("typ", "2");
        new ChineseServer().connect(this, ChineseConstants.PRINT_ORDER, params, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				if (ValueUtil.isNotEmpty(result)) {
				}
			}

			@Override
			public void onBeforeRequest() {
			}
		});
    }

	//--------------------΢����
	public void vipPayConfirm(){
		CSLog.i("��Ա��","ִ��");
		final EditText text = new EditText(ChineseSettleAccountsActivity.this);
		LinearLayout layout = new LinearLayout(ChineseSettleAccountsActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		text.setHint(R.string.please_input_money);//��������
		text.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(16, 12, 16, 12);
		text.setText(((TextView) ChineseSettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().toString());
		text.setSelection(0, ((TextView) ChineseSettleAccountsActivity.this.findViewById(R.id.yingFJE_Text2)).getText().length());

		layout.addView(text, params);
		final AlertDialog dialog = new AlertDialog.Builder(ChineseSettleAccountsActivity.this,R.style.edittext_dialog)
				.setTitle(R.string.input_money)
				.setView(layout)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String money = text.getText().toString();
						if (!ValueUtil.isNaN(money)){
							money="0";
						}
						payHandle(Double.valueOf(money));
					}
				})
				.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {//ȡ���¼�

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
				}).show();
		text.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
					String money = text.getText().toString();
					if (!ValueUtil.isNaN(money)) {
						money = "0";
					}
					payHandle(Double.valueOf(money));
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
	}


	private void payHandle(double money){
		try {
			Intent intent = new Intent();
			intent.setClassName("cn.com.choicesoft.tkpay", "cn.com.choicesoft.tkpay.activity.LoginActivity");
			Bundle bundle = new Bundle();
			bundle.putInt("typ", 1);//���ﴫ1�Ϳ���
			bundle.putString("operator",SharedPreferencesUtils.getOperator(this));
			bundle.putDouble("money", money);//��Ҫ֧���Ľ��
			bundle.putString("batch", DateFormat.getStringByDate(new Date(), "yyMMdd"));//�˵���ʶ
			bundle.putString("biz", SharedPreferencesUtils.getDeviceId(ChineseSettleAccountsActivity.this)
					+ DateFormat.getStringByDate(new Date(), "yyMMddmmssSS"));//Ψһ��ʶ
			intent.putExtra("bundle", bundle);
			startActivityForResult(intent, 1);
		}catch (ActivityNotFoundException e){
			new AlertDialog.Builder(this)
					.setTitle(R.string.hint)
					.setMessage(R.string.lack_vip_app)
					.setNegativeButton(R.string.confirm, null).show();
		}
	}

	/**
	 * 1 ֧���ɹ� 0 ֧���ɹ�����û��֧��ȫ���з��ز��� -1 ����������� -2 ��ȡ����ʧ�� -3 ������Ա��Ϣ���� -4 ���ýӿڴ��� -5 ����ʧ�� -99 ȡ��֧��
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mResultCode=resultCode;
		mData=data;
		CSLog.i("��Ա��", "�ص����"+mResultCode+mData);
		super.onActivityResult(requestCode, resultCode, data);
	}


	/**
	 * ��ʼ��Fragment
	 */
	@Override
	protected void onResume() {
		if(mResultCode==null) {
			refresh();
		}else{
			String msg=this.getString(R.string.payment_error);
			switch (mResultCode){
				case 1:
					msg=this.getString(R.string.pay_success);
					rHandle(mData);
					break;
				case -1:
					msg=this.getString(R.string.input_value_error);
					break;
				case -2:
					msg=this.getString(R.string.net_error);
					break;
				case -3:
					msg=this.getString(R.string.result_anxml_error);
					break;
				case -4:
					//�ӿ��Լ��ᵯ��������Ϣ����Ҫ����
					break;
				case -99:
					msg=this.getString(R.string.SA_quXiaoPay);
					break;
				default:
					break;
			}
			if(mResultCode!=-4) {
				ToastUtil.toast(this, msg);
			}
		}
		super.onResume();
	}
	/**
	 * ����������Ϣ
	 */
	public void rHandle(Intent intent){
		CSLog.i("��Ա��", "�ص�"+intent);
		if(intent==null){
			return;
		}
		Bundle bundle=intent.getBundleExtra("bundle");
		String cash=bundle.getString("cash");
		String point=bundle.getString("point");
		String money=bundle.getString("money");
		String coupon=bundle.getString("coupon_money");
		CSLog.i("΢����֧��", "����֧��:" + point + "-----��ֵ֧��:" + money + "-----ȯ֧��:" + coupon);
		CList<Map<String,String>> maps=new CList<Map<String, String>>();
//        if(Double.valueOf(ValueUtil.isNaN(cash)?cash:"0")>0){//�ֽ�֧��
//			maps.add("1",cash);
//        }

		if(Double.valueOf(ValueUtil.isNaN(point)?point:"0")>0){//����֧��
			maps.add("19",point);
		}
		if(Double.valueOf(ValueUtil.isNaN(money)?money:"0")>0){//�����֧��
			BigDecimal bdMoney=BigDecimal.valueOf(Double.valueOf(money)).divide(BigDecimal.valueOf(1.08), 2, BigDecimal.ROUND_DOWN);
			maps.add("6",bdMoney.toString());//����
			maps.add("14", BigDecimal.valueOf(Double.valueOf(money)).subtract(bdMoney).toString());//����
		}
		if(Double.valueOf(ValueUtil.isNaN(coupon)?coupon:"0")>0){//ȯ֧��
			maps.add("3",coupon);
		}
		if(maps.size()>0) {
			for (Map.Entry<String, String> entry : maps.get(0).entrySet()) {
				maps.remove(0);
				moneyPay(entry.getValue(), entry.getKey());
			}
		}
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