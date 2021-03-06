package cn.com.choicesoft.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 会员查询界面
 */
public class VipFindActivity extends BaseActivity implements OnClickListener,EditText.OnEditorActionListener{
	private TextView tableNum,ts,ts1,orderNum,manNum,womanNum,userNum;//操作员，提示Text
	private EditText phone,vipCard,chuzhiYEEdit,jifenYEEdit;//手机号，会员号，储值余额，积分余额
	private Button confirm,back;//确认，返回
	private Spinner cardSpinner;//会员显示下拉框
	private Bundle bundle;//接受上级传来的ACTIVTIY数据
	private LoadingDialog dialog;
	private ArrayAdapter<String> adapter;//下拉框 信息adapter

//	private ImageView backIntopLayout;//手机版顶部标题的返回按钮

	public LoadingDialog getDialog() {
		if(dialog==null){
			dialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
			dialog.setCancelable(true);
		}
		return dialog;
	}
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(ChioceActivity.ispad){
			setContentView(R.layout.activity_vip_find_pad);
		}else{
			setContentView(R.layout.activity_vip_find);
			Button backIntopLayout = (Button) this.findViewById(R.id.eatable_btn_back);
			backIntopLayout.setOnClickListener(this);

		}
		iniView();
		iniData();
	}
	public void iniView(){
//		czyText=(TextView)findViewById(R.id.find_vip_czyText1);			//操作员
		ts=(TextView)findViewById(R.id.find_vip_ts);					//操作提示
		ts1=(TextView)findViewById(R.id.find_vip_ts1);					//操作提示符
		phone=(EditText)findViewById(R.id.find_vip_phoneEdit);			//手机号

		vipCard=(EditText)findViewById(R.id.find_vip_cardEdit);			//会员卡号
		if (SingleMenu.getMenuInstance().getCardNum()!=null&&SingleMenu.getMenuInstance().getCardNum().length()>0){
			phone.setText(SingleMenu.getMenuInstance().getCardPhone());
		}
		cardSpinner=(Spinner)findViewById(R.id.find_vip_spinner);		//会员下拉框
		chuzhiYEEdit=(EditText)findViewById(R.id.find_vip_chuzhiYEEdit);//储值余额
		jifenYEEdit=(EditText)findViewById(R.id.find_vip_jifenYEEdit);	//积分余额
		confirm=(Button)findViewById(R.id.find_vip_confirm);			//确认按钮
//		back=(Button)findViewById(R.id.find_vip_back);					//返回按钮
		ImageView menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
		menuinfo_imageView.setClickable(true);
		menuinfo_imageView.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	public void iniData(){
		setBundle(this.getIntent().getBundleExtra("BillBundle"));

		confirm.setOnClickListener(this);//确认事件
//		back.setOnClickListener(this);//返回事件
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new ArrayList<String>());
		cardSpinner.setAdapter(adapter);
		phone.addTextChangedListener(new TextWatcher() {
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
				if(phone.getText().length()==11){
//					vipCard.setText(null);
					adapter.clear();
					cardSpinner.setVisibility(View.INVISIBLE);
					ts.setText(R.string.query_vip_hint_one);
					confirm.setText(R.string.confirm);
					ts1.setVisibility(View.INVISIBLE);
				}
			}
		});
//		vipCard.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void onTextChanged(CharSequence paramCharSequence, int paramInt1,
//									  int paramInt2, int paramInt3) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence paramCharSequence,
//										  int paramInt1, int paramInt2, int paramInt3) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable paramEditable) {
//				if (vipCard.getText().length() == 16) {
//					phone.setText(null);
//					adapter.clear();
//					cardSpinner.setVisibility(View.INVISIBLE);
//					ts.setText(R.string.query_vip_hint_one);
//					ts1.setVisibility(View.INVISIBLE);
//					confirm.setText(R.string.confirm);
//				}
//			}
//		});
		phone.setOnEditorActionListener(this);
//		vipCard.setOnEditorActionListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.find_vip_confirm://确认
				confEvent();
				break;
			case R.id.find_vip_back://返回
			case R.id.eatable_btn_back:
				backSett();
				break;
			case R.id.orderinfo_ImageView:
				// TODO
				View layout = null ;
				layout = LayoutInflater.from(VipFindActivity.this).inflate(R.layout.menu_info, null);
				tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
				orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
				manNum = (TextView) layout.findViewById(R.id.dishesact_tv_mannum);
				womanNum = (TextView) layout.findViewById(R.id.dishesact_tv_womannum);
				userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
				tableNum.setText(SingleMenu.getMenuInstance().getTableNum());
				orderNum.setText(SingleMenu.getMenuInstance().getOrderNum());
				manNum.setText(SingleMenu.getMenuInstance().getManCounts());
				womanNum.setText(SingleMenu.getMenuInstance().getWomanCounts());
				userNum.setText(SharedPreferencesUtils.getOperator(this));//操作员设置
				AlertDialog tipdialog = new AlertDialog.Builder(VipFindActivity.this, R.style.Dialog_tip).setView(layout).create();
				tipdialog.setCancelable(true);
				Window dialogWindow = tipdialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
				lp.x = 0; // 新位置X坐标
				View view=(View)this.findViewById(R.id.vip_top);
				lp.y = view.getHeight() -15; // 新位置Y坐标
				dialogWindow.setAttributes(lp);
				tipdialog.show();
				break;
			default:
				break;
		}
	}
	/**
	 * 查询会员
	 */
	public void confEvent(){
		String num=phone.getText().toString();
		String cardNum="";
		if(num.matches("^[1][34578][0-9]{9}$")&&ValueUtil.isEmpty(cardSpinner.getSelectedItem())){
			CList<Map<String,String>> cl=new CList<Map<String,String>>();
			cl.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
			cl.add("userCode", SharedPreferencesUtils.getUserCode(this));
			cl.add("phoneNumber", num);
			SingleMenu.getMenuInstance().setCardPhone(num);
			//根据手机号码查询会员信息
			new Server().connect(this, "card_GetTrack2", "ChoiceWebService/services/HHTSocket?/card_GetTrack2", cl, new OnServerResponse() {
				@Override
				public void onResponse(String result) {
					getDialog().dismiss();
					String[] str=result==null?null:result.split("@");
					if(ValueUtil.isNotEmpty(str)&&str[0].equals("0")){
						List<String> list=Arrays.asList(str[2].split(";"));
						adapter.clear();
						adapter.addAll(list);
						ts.setText(R.string.query_vip_hint_two);
						ts1.setVisibility(View.VISIBLE);
						cardSpinner.setVisibility(View.VISIBLE);
						confirm.setText(R.string.PopWin_phoneNumBut);
					}else if(ValueUtil.isNotEmpty(str)&&!str[0].equals("0")){
						showToast(R.string.net_error);
					}else{
						showToast(R.string.net_error);
					}
				}
				@Override
				public void onBeforeRequest() {
					getDialog().show();
				}
			});
		}else if(ValueUtil.isNotEmpty(cardNum)&&cardNum.length()==16||ValueUtil.isNotEmpty(cardSpinner.getSelectedItem())){
			boolean mark=true;
			String markTZ=VipFindActivity.this.getIntent().getStringExtra("mark");
			// card_QueryBalance（String deviceId,String userCode ,String cardNumber,String orderId
			final CList<Map<String,String>> cl=new CList<Map<String,String>>();
			cl.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
			cl.add("userCode", SharedPreferencesUtils.getUserCode(this));
			if(ValueUtil.isNotEmpty(cardNum)&&cardNum.length()==16){//如果输入的是会员卡号
				cl.add("cardNumber", cardNum);
				SingleMenu.getMenuInstance().setCardNum(cardNum);
			}else if(ValueUtil.isNotEmpty(cardSpinner.getSelectedItem())){//如果输入的不是会员卡号则判断下拉框内是否有选中数据
				cl.add("cardNumber", cardSpinner.getSelectedItem());
				SingleMenu.getMenuInstance().setCardNum(cardSpinner.getSelectedItem().toString());
			}else{//如果以上都不满足则错误
				showToast(R.string.num_error);
				mark=false;
			}
			if(markTZ.equals("settle")){
				cl.add("orderId", getBundle().getString("orderId"));
//				String payable=VipFindActivity.this.getIntent().getStringExtra("Payable");
			}else{
				cl.add("orderId", "");
			}
			if(mark){//如果会员卡被复制则进行查询会员信息
				new Server().connect(this, "card_QueryBalance", "ChoiceWebService/services/HHTSocket?/card_QueryBalance", cl, new OnServerResponse() {
					@Override
					public void onResponse(String result) {
						getDialog().dismiss();
						String[] str=result==null?null:result.split("@");
						if(ValueUtil.isNotEmpty(str)&&str[0].equals("0")){
							String mark=VipFindActivity.this.getIntent().getStringExtra("mark");
							if(mark!=null&&mark.equals("settle")){

								String payable=VipFindActivity.this.getIntent().getStringExtra("Payable");
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
								vip.setPhone(phone.getText().toString());
								vip.setTableNum(VipFindActivity.this.getIntent().getStringExtra("tableNum"));
								vip.setManCounts(ValueUtil.isNaNofInteger(getBundle().getString("manCounts")));
								vip.setWomanCounts(ValueUtil.isNaNofInteger(getBundle().getString("womanCounts")));
								vip.setPayable(ValueUtil.isNaNofDouble(payable));
								vip.setTicketInfoList("");
								if(str.length>7){//判断速度是否大于7，大于7证明含有【券信息列表】
									vip.setTicketInfoList(str[7]);
								}
								new VipRecordUtil().insertHandle(VipFindActivity.this, vip);//把会员数据添加到表里
								//---------------------------------------------------
								Intent intent = new Intent(VipFindActivity.this,PayMentActivity.class);//打开会员支付页面
								intent.putExtra("BillBundle", getBundle());
								intent.putExtra("Payable",payable);
								intent.putExtra("tableNum",getBundle().getString("tableNum"));
								startActivity(intent);
								VipFindActivity.this.finish();
							}else if(mark!=null&&mark.equals("main")){
								queryVip(result);
							}else if(mark!=null&&mark.equals("startTable")||mark.equals("mark")){
								String tableNum=VipFindActivity.this.getIntent().getStringExtra("tableNum");
								String man=VipFindActivity.this.getIntent().getStringExtra("man");
								String woman=VipFindActivity.this.getIntent().getStringExtra("woman");
								VipRecord vip=new VipRecord();//VipRecordUtil
								vip.setCardNumber(str[1]);			//会员卡号
								vip.setCardType(str[2]);  			//会员卡类型
								vip.setStoredCardsBalance(ValueUtil.isNaNofDouble(str[3]));	//储值余额
								vip.setIntegralOverall(ValueUtil.isNaNofDouble(str[4]));   	//积分余额
								vip.setCouponsOverall(ValueUtil.isNaNofDouble(str[5]));		//券余额 券
								vip.setCouponsAvail(ValueUtil.isNaNofDouble(str[6]));		//可用余额
								vip.setOrderId("");
								vip.setPhone(phone.getText().toString());
								vip.setTableNum(tableNum);
								vip.setManCounts(ValueUtil.isNaNofInteger(man));
								vip.setWomanCounts(ValueUtil.isNaNofInteger(woman));
								vip.setTicketInfoList("");
								if(str.length>7){//判断速度是否大于7，大于7证明含有【券信息列表】
									vip.setTicketInfoList(str[7]);
								}
								new VipRecordUtil().insertHandle(VipFindActivity.this, vip);
								Intent intent = new Intent(VipFindActivity.this,VipCardQuery.class);//打开会员支付页面
								intent.putExtra("state", mark.equals("mark")?"mark":"No");
								intent.putExtra("tableNum", tableNum);
								intent.putExtra("card", str[1]);
								intent.putExtra("Payable", "0");
								intent.putExtra("man", man);
								intent.putExtra("woman", woman);
								VipFindActivity.this.startActivity(intent);
								VipFindActivity.this.finish();
							}else{
								showToast(R.string.skip_error);
							}
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
			}else{
				showToast(R.string.net_error);
			}
		}else{
			showToast(R.string.net_error);
		}
	}


	/**
	 * 返回方法
	 */
	public void backSett(){
		String markTZ=VipFindActivity.this.getIntent().getStringExtra("mark");
		if(markTZ!=null&&markTZ.equals("settle")){
			 Intent intent=new Intent(this,SettleAccountsActivity.class);
			 intent.putExtras(getBundle());
			 intent.putExtra("mark", "VipFind");
			 startActivity(intent);
			 this.finish();
		}else{
			this.finish();
		}
	}
	/**
	 * 返回会员查询页面
	 * @param result
	 */
	public void queryVip(String result){
		Intent intent=new Intent(VipFindActivity.this,QueryVipCardActivity.class);
		intent.putExtra("result", result);
		startActivity(intent);
		this.finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			 backSett();
			 return true;
         }
		return false;
	}
	/**
	 * 简化Toast
	 * @param text
	 */
	public void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
    /**
	 * 简化Toast
	 * @param id
	 */
	public void showToast(int id){
		Toast.makeText(this, id, Toast.LENGTH_LONG).show();
	}
	@Override
	public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_ENDCALL == keyCode) {
        	confEvent();
            return true;
        }
        return false;
    }
}
