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
 * ��Ա��ѯ����
 */
public class VipFindActivity extends BaseActivity implements OnClickListener,EditText.OnEditorActionListener{
	private TextView tableNum,ts,ts1,orderNum,manNum,womanNum,userNum;//����Ա����ʾText
	private EditText phone,vipCard,chuzhiYEEdit,jifenYEEdit;//�ֻ��ţ���Ա�ţ���ֵ���������
	private Button confirm,back;//ȷ�ϣ�����
	private Spinner cardSpinner;//��Ա��ʾ������
	private Bundle bundle;//�����ϼ�������ACTIVTIY����
	private LoadingDialog dialog;
	private ArrayAdapter<String> adapter;//������ ��Ϣadapter

//	private ImageView backIntopLayout;//�ֻ��涥������ķ��ذ�ť

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
//		czyText=(TextView)findViewById(R.id.find_vip_czyText1);			//����Ա
		ts=(TextView)findViewById(R.id.find_vip_ts);					//������ʾ
		ts1=(TextView)findViewById(R.id.find_vip_ts1);					//������ʾ��
		phone=(EditText)findViewById(R.id.find_vip_phoneEdit);			//�ֻ���

		vipCard=(EditText)findViewById(R.id.find_vip_cardEdit);			//��Ա����
		if (SingleMenu.getMenuInstance().getCardNum()!=null&&SingleMenu.getMenuInstance().getCardNum().length()>0){
			phone.setText(SingleMenu.getMenuInstance().getCardPhone());
		}
		cardSpinner=(Spinner)findViewById(R.id.find_vip_spinner);		//��Ա������
		chuzhiYEEdit=(EditText)findViewById(R.id.find_vip_chuzhiYEEdit);//��ֵ���
		jifenYEEdit=(EditText)findViewById(R.id.find_vip_jifenYEEdit);	//�������
		confirm=(Button)findViewById(R.id.find_vip_confirm);			//ȷ�ϰ�ť
//		back=(Button)findViewById(R.id.find_vip_back);					//���ذ�ť
		ImageView menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
		menuinfo_imageView.setClickable(true);
		menuinfo_imageView.setOnClickListener(this);
	}

	/**
	 * ��ʼ������
	 */
	public void iniData(){
		setBundle(this.getIntent().getBundleExtra("BillBundle"));

		confirm.setOnClickListener(this);//ȷ���¼�
//		back.setOnClickListener(this);//�����¼�
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
			case R.id.find_vip_confirm://ȷ��
				confEvent();
				break;
			case R.id.find_vip_back://����
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
				userNum.setText(SharedPreferencesUtils.getOperator(this));//����Ա����
				AlertDialog tipdialog = new AlertDialog.Builder(VipFindActivity.this, R.style.Dialog_tip).setView(layout).create();
				tipdialog.setCancelable(true);
				Window dialogWindow = tipdialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
				lp.x = 0; // ��λ��X����
				View view=(View)this.findViewById(R.id.vip_top);
				lp.y = view.getHeight() -15; // ��λ��Y����
				dialogWindow.setAttributes(lp);
				tipdialog.show();
				break;
			default:
				break;
		}
	}
	/**
	 * ��ѯ��Ա
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
			//�����ֻ������ѯ��Ա��Ϣ
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
			// card_QueryBalance��String deviceId,String userCode ,String cardNumber,String orderId
			final CList<Map<String,String>> cl=new CList<Map<String,String>>();
			cl.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
			cl.add("userCode", SharedPreferencesUtils.getUserCode(this));
			if(ValueUtil.isNotEmpty(cardNum)&&cardNum.length()==16){//���������ǻ�Ա����
				cl.add("cardNumber", cardNum);
				SingleMenu.getMenuInstance().setCardNum(cardNum);
			}else if(ValueUtil.isNotEmpty(cardSpinner.getSelectedItem())){//�������Ĳ��ǻ�Ա�������ж����������Ƿ���ѡ������
				cl.add("cardNumber", cardSpinner.getSelectedItem());
				SingleMenu.getMenuInstance().setCardNum(cardSpinner.getSelectedItem().toString());
			}else{//������϶������������
				showToast(R.string.num_error);
				mark=false;
			}
			if(markTZ.equals("settle")){
				cl.add("orderId", getBundle().getString("orderId"));
//				String payable=VipFindActivity.this.getIntent().getStringExtra("Payable");
			}else{
				cl.add("orderId", "");
			}
			if(mark){//�����Ա������������в�ѯ��Ա��Ϣ
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
								//��Ա����        ��Ա������          ��ֵ���           �������          ȯ��� ȯ       �������             ȯ��Ϣ�б�
								VipRecord vip=new VipRecord();//VipRecordUtil
								vip.setCardNumber(str[1]);			//��Ա����
								vip.setCardType(str[2]);  			//��Ա������
								vip.setStoredCardsBalance(ValueUtil.isNaNofDouble(str[3]));	//��ֵ���
								vip.setIntegralOverall(ValueUtil.isNaNofDouble(str[4]));   	//�������
								vip.setCouponsOverall(ValueUtil.isNaNofDouble(str[5]));		//ȯ��� ȯ
								vip.setCouponsAvail(ValueUtil.isNaNofDouble(str[6]));		//�������
								vip.setOrderId(getBundle().getString("orderId"));
								vip.setPhone(phone.getText().toString());
								vip.setTableNum(VipFindActivity.this.getIntent().getStringExtra("tableNum"));
								vip.setManCounts(ValueUtil.isNaNofInteger(getBundle().getString("manCounts")));
								vip.setWomanCounts(ValueUtil.isNaNofInteger(getBundle().getString("womanCounts")));
								vip.setPayable(ValueUtil.isNaNofDouble(payable));
								vip.setTicketInfoList("");
								if(str.length>7){//�ж��ٶ��Ƿ����7������7֤�����С�ȯ��Ϣ�б���
									vip.setTicketInfoList(str[7]);
								}
								new VipRecordUtil().insertHandle(VipFindActivity.this, vip);//�ѻ�Ա�������ӵ�����
								//---------------------------------------------------
								Intent intent = new Intent(VipFindActivity.this,PayMentActivity.class);//�򿪻�Ա֧��ҳ��
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
								vip.setCardNumber(str[1]);			//��Ա����
								vip.setCardType(str[2]);  			//��Ա������
								vip.setStoredCardsBalance(ValueUtil.isNaNofDouble(str[3]));	//��ֵ���
								vip.setIntegralOverall(ValueUtil.isNaNofDouble(str[4]));   	//�������
								vip.setCouponsOverall(ValueUtil.isNaNofDouble(str[5]));		//ȯ��� ȯ
								vip.setCouponsAvail(ValueUtil.isNaNofDouble(str[6]));		//�������
								vip.setOrderId("");
								vip.setPhone(phone.getText().toString());
								vip.setTableNum(tableNum);
								vip.setManCounts(ValueUtil.isNaNofInteger(man));
								vip.setWomanCounts(ValueUtil.isNaNofInteger(woman));
								vip.setTicketInfoList("");
								if(str.length>7){//�ж��ٶ��Ƿ����7������7֤�����С�ȯ��Ϣ�б���
									vip.setTicketInfoList(str[7]);
								}
								new VipRecordUtil().insertHandle(VipFindActivity.this, vip);
								Intent intent = new Intent(VipFindActivity.this,VipCardQuery.class);//�򿪻�Ա֧��ҳ��
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
	 * ���ط���
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
	 * ���ػ�Ա��ѯҳ��
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
	 * ��Toast
	 * @param text
	 */
	public void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
    /**
	 * ��Toast
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