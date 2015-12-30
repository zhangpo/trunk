package cn.com.choicesoft.singletaiwei;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.BaseActivity;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.Eatables;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.adapter.CommonFujiaAdapter;
import cn.com.choicesoft.adapter.CommonFujiaAdapter.ComFujiaHolder;
import cn.com.choicesoft.adapter.SingleFujiaAdapter;
import cn.com.choicesoft.bean.*;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.constants.IFujiaResult;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.*;
import cn.com.choicesoft.view.PresentAuthAndCountDialog.DialogPresentAuthAndCountClickListener;
import cn.com.choicesoft.view.PresentAuthDialog.DialogPresentAuthClickListener;
import cn.com.choicesoft.view.PresentCountDialog.DialogPresentCountClickListener;

/**
 * �ѵ��Ʒҳ��
 * @author dell
 *
 */
public class YiDianDishActivity extends BaseActivity implements OnClickListener{
	
	
	private ExpandableListView expandListView;//ҳ��������ʾ���ݰ󶨵Ŀؼ���ExpandableListView
	private YidianDishAdapter adapter;//ExpandableListView��Ӧ��������
	
	
	private ListView listView;//����ʱ������ʾ�Ŀؼ���ListView
	private YiDianDishSearchAdapter searchAdapter;//ListView��Ӧ��������
	
	private Button tablelogo;//̨λ
	private Button tempSave;//�ݴ�,����
	private Button allBillAddtions ;//�ر�ע
	private Button allBills;//ȫ��
	private Button sendwait;//����
	private Button sendprompt;//����
	private Button back;//����
	private Button deleteAll;//ȫ��ɾ��
	
	private Button btnSearchView;
	private EditText etSearchView;//������
	
	private List<Food> guestDishes = GuestDishes.getGuDishInstance();
	
	private List<Food> adapterDataSourceLists = null;//ExpandableListView������Դ
	
	private SingleMenu menu = SingleMenu.getMenuInstance();
	public static boolean hasChanged = false;
	
	private LoadingDialog authLoadingDialog;//��֤��Ȩ 
	private LoadingDialog sendLoadingDialog;//���Ͳ�Ʒ
	
	private TextView dishCounts,totalMoney,fujiaMoney;
	
	private TextView tableNum,orderNum,manNum,womanNum,userNum;//ͷ����ɫ����������
	private List<PresentReason> presentreasonLists;//�����Ʒ����
	private List<Food> matchLists = new ArrayList<Food>();//��Ʒ��Ϣ�������Ľ����ƥ��ļ���
	private List<Addition> fujiamatchLists = new ArrayList<Addition>();//��������Ϣ�������Ľ����ƥ��ļ���
	private List<CommonFujia> specialfujiaMatchLists = new ArrayList<CommonFujia>();//ȫ����������Ϣ�������Ľ����ƥ��ļ���
	
	
	private TextView specialRemark;
	
	private LoadingDialog mLoadingDialog = null;
	
	private Dialog dialog = null;
	
	private LinearLayout digitalLayout = null;
	private ImageView menuinfo_imageView;
	private View layout;
	private LinearLayout topLinearLayout;
	
	public int screemwidth ;
    public AlertDialog typDialog;//���������
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screemwidth = getWindowManager().getDefaultDisplay().getWidth();
		if (ChioceActivity.ispad) {
			setContentView(R.layout.add_dishes_activity_pad);
		} else {
			setContentView(R.layout.add_dishes_activity);
		}
		initViews();
		initData();
	}
	/**
	 *����ˢ��
	 */
	private void updateDisplay() {
		
		adapterDataSourceLists = guestDishes;
		
		if(adapter == null){
			adapter = new YidianDishAdapter(this, adapterDataSourceLists);
			expandListView.setAdapter(adapter);
		}else{
			adapter.setDataSource(adapterDataSourceLists);
			adapter.notifyDataSetChanged();
			
		}
		
		dishCounts.setText(adapter.getDishSelectedCount()+ this.getString(R.string.part));//�����Ʒ��������
		
		double totalSalary = adapter.getTotalSalary();
		totalMoney.setText(ValueUtil.setScale(totalSalary, 2, 5));//�����Ʒ���ܼ�Ǯ
		
		double totalFujiaSalary = adapter.getFujiaSalary();
		fujiaMoney.setText(ValueUtil.setScale(totalFujiaSalary, 2, 5));//�����Ʒ��������ܼ�Ǯ
	}

	private void initViews() {
		
		mLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
		mLoadingDialog.setCancelable(false);
		
		digitalLayout = (LinearLayout) this.findViewById(R.id.toptitle_rg_number);
		digitalLayout.setVisibility(View.GONE);
		
		//��Ʒ��3          �ܼƣ�179.00      �����
		dishCounts = (TextView) this.findViewById(R.id.yidian_tv_dishcount);
		totalMoney = (TextView) this.findViewById(R.id.yidian_tv_totalSalary);
		fujiaMoney = (TextView) this.findViewById(R.id.yidian_tv_fujiaMoney);
				
		
		//������ɫ��
		if (ChioceActivity.ispad) {
			tableNum = (TextView) this.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) this.findViewById(R.id.dishesact_tv_ordernum);
			manNum = (TextView) this.findViewById(R.id.dishesact_tv_mannum);
			womanNum = (TextView) this.findViewById(R.id.dishesact_tv_womannum);
			userNum = (TextView) this.findViewById(R.id.dishesact_tv_usernum);
			back = (Button) this.findViewById(R.id.yidian_btn_back);
		}else {
			// TODO 
			menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
			menuinfo_imageView.setClickable(true);
			menuinfo_imageView.setOnClickListener(this);
			layout = LayoutInflater.from(YiDianDishActivity.this).inflate(R.layout.menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			manNum = (TextView) layout.findViewById(R.id.dishesact_tv_mannum);
			womanNum = (TextView) layout.findViewById(R.id.dishesact_tv_womannum);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			topLinearLayout = (LinearLayout) findViewById(R.id.topLinearLyout);
			back = (Button) this.findViewById(R.id.eatable_btn_back);
		}
		
		
		tablelogo = (Button) this.findViewById(R.id.yidian_btn_tbl);
		tablelogo.setOnClickListener(this);
		
		tempSave = (Button) this.findViewById(R.id.yidian_btn_tempsave);
		tempSave.setOnClickListener(this);
		
		allBillAddtions = (Button) this.findViewById(R.id.yidian_btn_allbilladditions);
		allBillAddtions.setOnClickListener(this);
		
		allBills = (Button) this.findViewById(R.id.yidian_btn_allbills);
		allBills.setOnClickListener(this);
		
		sendwait = (Button) this.findViewById(R.id.yidian_btn_sendwait);
		sendwait.setVisibility(View.GONE);
		sendwait.setOnClickListener(this);
		
		sendprompt = (Button) this.findViewById(R.id.yidian_btn_sendprompt);
		sendprompt.setText("�ϴ�");
		sendprompt.setOnClickListener(this);
		
				
		back.setOnClickListener(this);
		
		deleteAll = (Button) this.findViewById(R.id.yidian_btn_deleteall);
		deleteAll.setOnClickListener(this);
		
		btnSearchView = (Button) this.findViewById(R.id.btn_searchView);
		btnSearchView.setOnClickListener(this);
		
		etSearchView =  (EditText) this.findViewById(R.id.et_searchView);
		etSearchView.addTextChangedListener(watcher);
		if (!ChioceActivity.ispad) {
			etSearchView.setVisibility(View.GONE);
		}
		expandListView =  (ExpandableListView) this.findViewById(R.id.yidiandish_expandlist_menu);
		listView = (ListView) this.findViewById(R.id.yidiandish_list_menu);
		
		sendLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.food_sending));
		sendLoadingDialog.setCancelable(true);
		
		authLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.authorization_ing));
		authLoadingDialog.setCancelable(true);
		
		specialRemark = (TextView) this.findViewById(R.id.yidian_tv_specialremark);
		for (Map<String,String>map:SingleMenu.getMenuInstance().getPermissionList()){
			if (map.get("CODE").equals("3001")){
				sendprompt.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					sendprompt.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("3002")){
				sendwait.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					sendwait.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("3003")){
				allBillAddtions.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					allBillAddtions.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("3004")){
				tempSave.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					tempSave.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("3005")){
				allBills.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					allBills.setVisibility(View.GONE);
				}
			}
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}

	private void initData() {
		
		tableNum.setText(menu.getTableNum());
		orderNum.setText(menu.getMenuOrder());
		manNum.setText(menu.getManCounts()==null&&menu.getManCounts().equals("")?"0":menu.getManCounts());
		womanNum.setText(menu.getWomanCounts()==null&&menu.getWomanCounts().equals("")?"0":menu.getWomanCounts());
		userNum.setText(SharedPreferencesUtils.getOperator(this));
				
		updateDisplay();
		
		presentreasonLists = getDataManager(this).queryPresentReason();//��ѯ���ݿ�ó����˵�ԭ��
		
		
		//��ʼ����Ա��Ϣ
		VipMsg.iniVip(this, menu.getTableNum(), R.id.vipMsg_ImageView);
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_searchView:
			btnSearchView.setVisibility(View.GONE);
			etSearchView.setVisibility(View.VISIBLE);
			etSearchView.requestFocus();
			//��������
			break;
		case R.id.yidian_btn_back://����
			this.finish();
			break;
		case R.id.eatable_btn_back://����
			if(hasChanged){
				Intent intent = new Intent(YiDianDishActivity.this, Eatables.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}else{
				this.finish();
			}
			break;
		case R.id.yidian_btn_deleteall:
			new ValueSetFile(menu.getTableNum()
					+DateFormat.getStringByDate(new Date(),"yyyy-MM-dd")
					+menu.getMenuOrder()+".mc").del();
			guestDishes.clear();//��յ�������
			YiDianDishActivity.hasChanged = true;
			updateDisplay();
			break;
			
		case R.id.yidian_btn_sendwait:  //������
			if(TsData.isReserve){
				alertReserve(this.getString(R.string.only_save_error));
				break;
			}
			Send("2");
			break;
			
		case R.id.yidian_btn_sendprompt: //������
			if(TsData.isReserve){
				alertReserve(this.getString(R.string.only_save_error));
				break;
			}
			Send("1");
			break;
					
		case R.id.yidian_btn_allbills:    //ȫ��
			
			Intent intent = new Intent(YiDianDishActivity.this, YiXuanDishActivity2.class);
			Bundle bundle = new Bundle();
			bundle.putString("orderId", menu.getMenuOrder());//�˵���
			bundle.putString("manCs", menu.getManCounts());//��������
			bundle.putString("womanCs", menu.getWomanCounts());//Ů������
			bundle.putString("tableNum", menu.getTableNum());//̨λ��
			
			intent.putExtra("topBundle", bundle);
			startActivity(intent);
			break;
			
		case R.id.yidian_btn_allbilladditions://�ر�ע
			
			final List<CommonFujia>  commonFujiaLists = getDataManager(this).queryCommonFujia();//��Ź����������
			
			View commonLayout = LayoutInflater.from(YiDianDishActivity.this).inflate(R.layout.dia_common_cover, null);
			final Button btnSearch = (Button) commonLayout.findViewById(R.id.dia_quandanfujia_btn_searchView);
			final cn.com.choicesoft.view.ClearEditText editSearch = (ClearEditText) commonLayout.findViewById(R.id.dia_quandanfujia_et_searchView);
			final CommonFujiaAdapter comFujiaAdapter = new CommonFujiaAdapter(commonFujiaLists, this);
			btnSearch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					btnSearch.setVisibility(View.GONE);
					editSearch.setVisibility(View.VISIBLE);
					editSearch.requestFocus();
					
					editSearch.addTextChangedListener(new SpecialFujiaWatcher(editSearch, commonFujiaLists, comFujiaAdapter));
					//��������
					InputMethodUtils.toggleSoftKeyboard(YiDianDishActivity.this);

				}
			});
			
			//�Զ��帽����Ŀؼ�EditText
			final EditText selfFujiaEdit = (EditText) commonLayout.findViewById(R.id.dia_quandanfujia_et_selffujia);
			
			
			ListView comFujiaListView = (ListView) commonLayout.findViewById(R.id.dia_quandanfujia_listView);
			
			comFujiaListView.setAdapter(comFujiaAdapter);
			
			comFujiaListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					ComFujiaHolder holder = (ComFujiaHolder) view.getTag();
					holder.mCheckBox.toggle();
					CommonFujiaAdapter adapter = (CommonFujiaAdapter) parent.getAdapter();
					adapter.toggleChecked(position);
				}
			});
			
			Button buttonCertain = (Button) commonLayout.findViewById(R.id.dia_quandanfujia_btn_certain);
			buttonCertain.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					StringBuilder buildCode = new StringBuilder();//����
					final StringBuilder buildDes = new StringBuilder();//����
					
					String comFujiaStr = selfFujiaEdit.getText().toString();
					if(ValueUtil.isNotEmpty(comFujiaStr)){
						
						buildCode.append("").append("!");
						buildDes.append(comFujiaStr).append("!");
						
						selfFujiaEdit.setText("");
					}
					
//					/*deviceId   �豸���
//					userCode  ��½���
//					orderId    �˵���
//					remarkId   �ر�ע���   ����ã�����    �Զ��帽����Ϊ�գ����Ƕ��Ҳ���ã�����
//					remark    �ر�ע        ����ã�����
//					flag       1 ���  0 ȡ��*/
					List<CommonFujia> listComs = comFujiaAdapter.getSelectedItemLists();
					if(listComs.size() != 0){
						for(CommonFujia comFujia:listComs){
							buildCode.append(comFujia.getId()).append("!");
							buildDes.append(comFujia.getDES()).append("!");
						}
						
						//ɾ�����ģ�
						if(buildDes.toString().length()>0){
							buildDes.deleteCharAt(buildDes.toString().length() - 1);
						}
						if(buildCode.toString().length()>0){
							buildCode.deleteCharAt(buildCode.toString().length() - 1);
						}
					}	
					dialog.dismiss();
					InputMethodUtils.toggleSoftKeyboard(YiDianDishActivity.this);
					//��ʼ���ýӿ�
					CList<Map<String, String>> params = new CList<Map<String,String>>();
					params.add("deviceId", SharedPreferencesUtils.getDeviceId(YiDianDishActivity.this));
					params.add("userCode", SharedPreferencesUtils.getUserCode(YiDianDishActivity.this));
					params.add("orderId", menu.getMenuOrder());
					params.add("remarkIdList", buildCode.toString());
					params.add("remarkList", buildDes.toString());
					params.add("flag", "1");
					new Server().connect(YiDianDishActivity.this, Constants.FastMethodName.specialRemark_METHODNAME, Constants.FastWebService.specialRemark_WSDL, params, new OnServerResponse() {
						
						@Override
						public void onResponse(String result) {
							mLoadingDialog.dismiss();
							if(ValueUtil.isNotEmpty(result)){
								String [] flag = result.split("@");
								if(flag[0].equals("0")){
									specialRemark.setText(buildDes.toString().replace("!", "  "));
								}
								
							}else{
								Toast.makeText(YiDianDishActivity.this, R.string.fj_shibai, Toast.LENGTH_SHORT).show();
							}
							
						}
						
						@Override
						public void onBeforeRequest() {
							mLoadingDialog.show();
						}
					});
						
					
					
				}
			});
			
			Button buttonCancel = (Button) commonLayout.findViewById(R.id.dia_quandanfujia_btn_cancel);
			buttonCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					dialog.dismiss();
					InputMethodUtils.toggleSoftKeyboard(YiDianDishActivity.this);
				}
			});
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(YiDianDishActivity.this,R.style.edittext_dialog);
			builder.setView(commonLayout);
			
			dialog = builder.create();
			dialog.show();
			break;
			
			
		case R.id.yidian_btn_tempsave:  //����
			if(TsData.isReserve){
				Send("");
                break;
			}
			//ʵ����food�е��ֶ�send�������ѷ��Ͳ�Ʒ�������ݴ�   ��Ϊ�丳ֵ             �ѷ���1          �ݴ�0
			for(int i = 0;i<guestDishes.size();i++){
				guestDishes.get(i).setSend("0");
			}
			
			//ÿ��ִ���ݴ�ʱ����Ҫɾ����̨λ���˵�������Ӧ�����ݿ��б��е�����
//			getDataManager(this).deleteDataIfSaved(menu.getTableNum(), menu.getMenuOrder());
			new ValueSetFile<Food>(menu.getTableNum()
					+ DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")
					+ menu.getMenuOrder() + ".mc").write(guestDishes);
//			if(insertDatabases(guestDishes))
				Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.yidian_btn_tbl:   //̨λ
//			if (guestDishes.size()>0)
				showIfSaveDialog().show();//������ʾ�Ի�����ʾ�û��Ƿ񱣴�
//			else {
//				intent = new Intent(YiDianDishActivity.this, MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				startActivity(intent);
//			}

			break;
		case R.id.orderinfo_ImageView:
			// TODO   
			View layout = null ;
			layout = LayoutInflater.from(YiDianDishActivity.this).inflate(R.layout.menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			manNum = (TextView) layout.findViewById(R.id.dishesact_tv_mannum);
			womanNum = (TextView) layout.findViewById(R.id.dishesact_tv_womannum);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			initData();
			AlertDialog tipdialog = new AlertDialog.Builder(YiDianDishActivity.this, R.style.Dialog_tip).setView(layout).create();
			tipdialog.setCancelable(true);
	        Window dialogWindow = tipdialog.getWindow();
	        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	        dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
	        lp.x = 0; // ��λ��X����
	        lp.y = topLinearLayout.getHeight() -15; // ��λ��Y����
	        dialogWindow.setAttributes(lp);
			tipdialog.show();
			break;
		default:
			break;
		}
	}
	
	/**
	 * �����Ƿ񱣴�Ի��� 
	 * @return
	 */
	private Dialog showIfSaveDialog() {
		String [] itemSave = {this.getString(R.string.yes),this.getString(R.string.no),this.getString(R.string.cancle)};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.whether_save_food);
		ListAdapter diaAdapter=new ArrayAdapter<String>(this, R.layout.dia_ifsave_item_layout, R.id.dia_ifsave_item_tv, itemSave);
		builder.setAdapter(diaAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						//����������Ĳ�Ʒ���浽���ݿ��AllCheck��

						//ʵ����food�е��ֶ�send�������ѷ��Ͳ�Ʒ�������ݴ�   ��Ϊ�丳ֵ             �ѷ���1          �ݴ�0
						for (int i = 0; i < guestDishes.size(); i++) {
							guestDishes.get(i).setSend("0");
						}

						//�ڲ���֮ǰ����ԭ����̨λ�š��˵�������Ӧ�����ݿ��Allcheck�е���Ϣɾ��

//						getDataManager(YiDianDishActivity.this).deleteDataIfSaved(menu.getTableNum(), menu.getMenuOrder());
//
//						boolean insertSuccess = insertDatabases(guestDishes);
//						if (insertSuccess) {
						ValueSetFile valueSetFile=new ValueSetFile<Food>(menu.getTableNum()
								+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")
								+menu.getMenuOrder()+".mc");
						valueSetFile.write(guestDishes);
						Toast.makeText(YiDianDishActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
						guestDishes.clear();//���singleton����
						Intent intent = new Intent(YiDianDishActivity.this, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);
						break;

					case 1:
						guestDishes.clear();//���singleton����
						Intent it = new Intent(YiDianDishActivity.this, MainActivity.class);
						it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(it);
						break;

					case 2:

					default:
						break;
				}
			}


		});
		
		return builder.create();
	}

	/**
	 * ���Ͳ�Ʒ
	 * @param immediateOrWaitFlag ���ͷ�ʽ
	 */
	private void Send(String immediateOrWaitFlag) {
		
		//��������дtpnum
		//�����û�е�ˣ�����ʾ 
		if(guestDishes.size() == 0){
			Toast.makeText(this, R.string.not_send_before_dian, Toast.LENGTH_SHORT).show();
			return;
		}
		
		Calendar c = Calendar.getInstance();
		String curTimeInMillis = String.valueOf(c.getTimeInMillis());//��ȡ��ǰʱ��ĺ�����
		
		//��Pkid��ֵ
		String menuId = menu.getMenuOrder();
		final String tableNumber = menu.getTableNum();
		List<Food> tcLists = new ArrayList<Food>();
		for(Food f:guestDishes){
			if(f.isIstc() && f.getTpcode() != null && f.getPcode().equals(f.getTpcode())){
				tcLists.add(f);
			}else if(!f.isIstc()){
				f.setPKID(getPKID(curTimeInMillis,menuId, tableNumber, f));
			}
		}
					
		for(Food fd:tcLists){
			
			for(Food f:guestDishes){
				if(f.isIstc() && f.getTpcode().equals(fd.getPcode()) && f.getTpnum().equals(fd.getTpnum())){
					f.setPKID(getTcPKID(curTimeInMillis,menuId, tableNumber, f));
				}
			}
			
		}
		
		//ʵ����food�е��ֶ�class,�����Ǽ����ǽ���Ϊ�丳ֵ
		for(Food mFood:guestDishes){
			mFood.setCLASS(immediateOrWaitFlag);
		}
		
		Server server = new Server();
		CList<Map<String, String>> params = new CList<Map<String,String>>();
		params.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
		params.add("userCode", SharedPreferencesUtils.getUserCode(this));
		params.add("tableNum", menu.getTableNum());
		params.add("orderId", menu.getMenuOrder());
		
		StringBuilder sb = new StringBuilder();

		for(Food food:guestDishes){
			sb.append(food.getPKID()).append("@")//��ƷΨһ����
			.append(food.getPcode()).append("@")//��Ʒ����
			.append(food.getPcname()).append("@")// ��Ʒ����
			.append(ValueUtil.isEmpty(food.getTpcode())?"":food.getTpcode()).append("@")//�ײͱ���
			.append(ValueUtil.isEmpty(food.getTpname())?"":food.getTpname()).append("@")//�ײ�����
			.append(food.getTpnum()).append("@")//�ײ���� ����0 ��ʼ�� ÿ���ײͶ�����ʼ��
			.append(food.getPcount()).append("@");//����    �������1 �ײ���ϸ��������ʵ���������   �˲�-1
			
			//�������� 0  ��  1  �� �ڶ���λ��������
			if(ValueUtil.isNotEmpty(food.getWeightflg()) && food.getWeightflg().equals("2")){  //��ʾ�ڶ���λ
				sb.append(food.getWeight()).append("@");
			}else{ //��ʾ��һ��λ
				sb.append(ValueUtil.isEmpty(food.getPromonum())?"0":food.getPromonum()).append("@");
			}
			
			sb.append(ValueUtil.isEmptyOrNull(food.getFujiacode()) ? "" : food.getFujiacode());
            sb.append(ValueUtil.isEmptyOrNull(food.getComfujiacode()) ? "" : food.getComfujiacode()).append("@");//���������  �����������  !  ����
			sb.append(ValueUtil.isEmptyOrNull(food.getFujianame()) ? "" : food.getFujianame());
            sb.append(ValueUtil.isEmptyOrNull(food.getComfujianame()) ? "" : food.getComfujianame()).append("@");//����������   �����������  !  ����
			sb.append(food.getPrice()).append("@");//�۸�
			sb.append(ValueUtil.isEmptyOrNull(food.getFujiaprice()) ? "" : food.getFujiaprice());
            sb.append(ValueUtil.isEmptyOrNull(food.getComfujiaprice()) ? "" : food.getComfujiaprice()).append("@");//������۸�
			sb.append(ValueUtil.isEmpty(food.getWeight()) ? "0" : food.getWeight()).append("@");//�ڶ���λ����    û��Ϊ0
			sb.append(food.getWeightflg().equals("1") ? "1" : "2").append("@");//�ڶ���λ��־  1 ��һ��λ  2 �ڶ���λ
			sb.append(food.getUnit()).append("@");//��λ
			sb.append(food.isIstc() ? "1" : "0").append("@");//�Ƿ��ײ�  0  ����  1 ��      ������
			sb.append(ValueUtil.isEmptyOrNull(food.getPresentCode()) ? "" : food.getPresentCode()).append("@");//����ԭ��
			sb.append(ValueUtil.isEmptyOrNull(food.getFujiacount()) ? "" : food.getFujiacount());
			sb.append(ValueUtil.isEmptyOrNull(food.getComfujiacount()) ? "" : food.getComfujiacount()).append("@");
			sb.append(ValueUtil.isEmpty(food.getUnitCode()) ? "" : food.getUnitCode()).append("@");//�൥λ
			sb.append(food.getIsTemp()).append("@");//��ʱ��
			sb.append(ValueUtil.isEmpty(food.getTempName()) ? "" : food.getTempName());
			sb.append(";");
			
		}
		sb.deleteCharAt(sb.length()-1);
		Log.e("��Ʒ�б�", sb.toString());
		params.add("productList", sb.toString());
		
		params.add("immediateOrWait", immediateOrWaitFlag);//���� 1                                   ����2
		
		server.connect(YiDianDishActivity.this, Constants.FastMethodName.Senddish_METHODNAME, Constants.FastWebService.Senddish_WSDL, params, new OnServerResponse() {
			
			@Override
			
			public void onResponse(String result) {
				sendLoadingDialog.dismiss();
				if(ValueUtil.isNotEmpty(result)){
					Log.e("���Ͳ�Ʒ��", result);
					String [] str = result.split("@");
					String flag = str[0];
					if(flag.equals("0")){
						Toast.makeText(YiDianDishActivity.this, str[1], Toast.LENGTH_SHORT).show();
						new ValueSetFile(menu.getTableNum()
								+DateFormat.getStringByDate(new Date(),"yyyy-MM-dd")
								+menu.getMenuOrder()+".mc").del();
						guestDishes.clear();//��յ�������
						if(TsData.isReserve){
                            return;
                        }
						Intent intent = new Intent(YiDianDishActivity.this, YiXuanDishActivity2.class);
						Bundle bundle = new Bundle();
						bundle.putString("orderId", menu.getMenuOrder());//�˵���
						bundle.putString("manCs", menu.getManCounts());//��������
						bundle.putString("womanCs", menu.getWomanCounts());//Ů������
						bundle.putString("tableNum", menu.getTableNum());//̨λ��
						intent.putExtra("topBundle", bundle);
						startActivity(intent);
					}else if(!flag.equals("0")){
						Toast.makeText(YiDianDishActivity.this, str[1], Toast.LENGTH_SHORT).show();//û�и��˵�
						return;
					}
				}else{
					Toast.makeText(YiDianDishActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
				}
				
			}
			
			@Override
			public void onBeforeRequest() {
				sendLoadingDialog.show();
			}
		});
		
	}
	
	/**
	 * �жϲ������ݿ��Ƿ�ɹ�
	 * @param listFoods
	 * @return
	 */
	protected boolean insertDatabases(List<Food> listFoods) {
		
		return getDataManager(this).circleInsertAllCheck(listFoods);
	}

	/**
	 * ��ͨ��Ʒ�õ�
	 * @param timeInMillis
	 * @param menuId
	 * @param tabNum
	 * @param food
	 * @return
	 */
	private String getPKID(String timeInMillis,String menuId,String tabNum,Food food){//��ͨ��Ʒ�õ�
		StringBuilder sb = new StringBuilder();
		sb.append("PKID").append(timeInMillis).append(menuId).append(tabNum).append(food.getPcode()).append(food.getTpnum());
		return sb.toString();
	}

	/**
	 * �ײ��õ�
	 * @param timeInMillis
	 * @param menuId
	 * @param tabNum
	 * @param food
	 * @return
	 */
	private String getTcPKID(String timeInMillis,String menuId,String tabNum,Food food){//�ײ��õ�
		StringBuilder sb = new StringBuilder();
		sb.append("PKID").append(timeInMillis).append(menuId).append(tabNum).append(food.getTpcode()).append(food.getTpnum());
		return sb.toString();
	}

	
	
	/**
	 * ExpandableListView��������
	 * @author dell
	 *
	 */
	public class YidianDishAdapter extends BaseExpandableListAdapter{
		
		private List<Food> groupAdapterLists = new ArrayList<Food>();//��Ŀ¼��Ӧ�ļ���
		private List<List<Food>> childAdapterLists = new ArrayList<List<Food>>();//��Ŀ¼��Ӧ�ļ���
		LayoutInflater  layoutInflater;
		
		private List<Food> integratedLists;//�ܵļ���
		

		public YidianDishAdapter(Context pContext,List<Food> pLists) {
			super();
			
			this.integratedLists = pLists;
			layoutInflater = LayoutInflater.from(pContext);
			
			inithomeList(integratedLists);
		}
		
		/**
		 * �ӵ���������ɸѡ���ײ�����ͨ��Ʒһ�飬����ڸ�����
		 * �ײ���ϸһ��,��Ž��Ӽ���
		 * @param singleAllLists
		 */
		private void inithomeList(List<Food> singleAllLists){
			
			groupAdapterLists.clear();
			childAdapterLists.clear();
			
			for(Food mFood:singleAllLists){
				List<Food> childLists = new ArrayList<Food>();
				if(mFood.isIstc() && mFood.getTpcode().equals(mFood.getPcode())){
					groupAdapterLists.add(mFood);

					for(Food food:singleAllLists){
						if(food.isIstc() && !food.getTpcode().equals(food.getPcode()) && food.getTpcode().equals(mFood.getPcode()) && food.getTpnum().equals(mFood.getTpnum())){
							childLists.add(food);
						}
					}
					childAdapterLists.add(childLists);

				}else if(!mFood.isIstc()){
					groupAdapterLists.add(mFood);
					childAdapterLists.add(childLists);
				}
			}
		}

		/**
		 * ���°�����Դ
		 * @param changedLists
		 */
		public void setDataSource(List<Food> changedLists){
			this.integratedLists = changedLists;
			
			//����ɸѡ����
			inithomeList(integratedLists);
		}
		
		
		/**
		 *  //�õ������Ʒ������
		 * @return
		 */
        public int getDishSelectedCount(){
            int counts = 0;
            for(Food food:groupAdapterLists){
                if(food.isIstc() && food.getPcode().equals(food.getTpcode())){
                    counts += 1;
                }else if(!food.isIstc()){
                    counts += ValueUtil.isNaNofDouble(food.getPcount());
                }
            }
            return counts;
        }
		
		/**
		 * �õ������Ʒ���ܼ�                     
		 * @return 
		 * ��ע��������С����ӵõ���
		 */
		public double getTotalSalary(){             
			double totalSalary = 0.00;
			for(Food mFood:groupAdapterLists){
				totalSalary += getItemSubtotal(mFood);
			}
			return totalSalary;
		}
		
		
		/**
		 *�õ������Ʒ��������ܼ� 
		 * @return double
		 */
		public double getFujiaSalary(){               
			BigDecimal totalFujiaSalary=new BigDecimal(0);
			for(Food f:guestDishes){
				if(ValueUtil.isNotEmpty(f.getFujiaprice())){//�����ò�Ʒ�и�����,�Զ��帽����۸�Ϊ0��������
					String [] fujiaStr = f.getFujiaprice().split("!");
                    String [] fujiaCnt=f.getFujiacount().split("!");
					for(int i = 0;i<fujiaStr.length;i++){
						//������Զ��帽����۸�Ϊ�������ͱ���,�����ж�һ��
						totalFujiaSalary=totalFujiaSalary.add(ValueUtil.isNaNofBigDecimal(fujiaStr[i]).multiply(ValueUtil.isNaNofBigDecimal(fujiaCnt[i])));
					}
				}
                if(ValueUtil.isNotEmpty(f.getComfujianame())){
                    String [] fujiaPri=f.getComfujiaprice().split("!");
                    String [] fujiaCnt=f.getComfujiacount().split("!");
                    for(int i = 0;i<fujiaPri.length;i++){
                        //������Զ��帽����۸�Ϊ�������ͱ���,�����ж�һ��
                        totalFujiaSalary=totalFujiaSalary.add(ValueUtil.isNaNofBigDecimal(fujiaPri[i]).multiply(ValueUtil.isNaNofBigDecimal(fujiaCnt[i])));
                    }
                }
			}
			return totalFujiaSalary.doubleValue();
		}
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childAdapterLists.get(groupPosition).get(childPosition);
		}
		
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}
		
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,ViewGroup parent) {
			
			Food food = childAdapterLists.get(groupPosition).get(childPosition);
			if (ChioceActivity.ispad) {
				convertView = layoutInflater.inflate(R.layout.yidian_expandlist_item_child_pad, null);
			} else {
				convertView = layoutInflater.inflate(R.layout.yidian_expandlist_item_child_phone, null);
			}
			TextView nameChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesname);//��Ʒ����
			TextView countsChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishescounts);//����
			TextView priceChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesprice);//�۸�
			TextView unitChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesunit);//��λ
			ImageView additionChildImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_child_addition);//������
			if (!ChioceActivity.ispad) {
				int buttonwidth  = screemwidth/11;
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonwidth, buttonwidth);
				lp.setMargins(4, 3, 4, 3);
				additionChildImage.setLayoutParams(lp);
				LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(screemwidth/8,screemwidth/15);
				lp2.setMargins(3, 3, 3, 5);
				countsChildText.setLayoutParams(lp2);
			}
			//�ڶ��е��ײ���ϸ�ĸ�����
			LinearLayout singleAddChildLayout = (LinearLayout) convertView.findViewById(R.id.yidian_expand_item_child_linear);
			TextView singleAddChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_show);
			
			//����и��������ʾ��û�о����صڶ���
			if(ValueUtil.isNotEmpty(food.getFujianame())||ValueUtil.isNotEmpty(food.getComfujianame())){
                String fujia=ValueUtil.isEmpty(food.getFujianame())?"":food.getFujianame().replace("!", ",");
                fujia+=ValueUtil.isEmpty(food.getComfujianame())?"":food.getComfujianame().replace("!", ",");
				singleAddChildLayout.setVisibility(View.VISIBLE);
				singleAddChildText.setText(fujia);
			}else{
				singleAddChildLayout.setVisibility(View.GONE);
			}

			nameChildText.setText(food.getPcname());
			countsChildText.setText(food.getPcount());//������ʾ�ײ���ϸÿ����Ʒ������
			priceChildText.setText(food.getPrice());
			unitChildText.setText(food.getUnit());
			
			additionChildImage.setOnClickListener(new AdditionChildClick(food));
						
			return convertView;
		}
		
		
		
		@Override
		public int getChildrenCount(int groupPosition) {
			
			return childAdapterLists.get(groupPosition).size();
		}
		@Override
		public Object getGroup(int groupPosition) {
			
			return groupAdapterLists.get(groupPosition);
		}
		@Override
		public int getGroupCount() {
			
			return groupAdapterLists.size();
		}
		
		
		@Override
		public long getGroupId(int groupPosition) {
			
			return groupPosition;
		}
		
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView,ViewGroup parent) {
			
			Food f = groupAdapterLists.get(groupPosition);
			
			if (ChioceActivity.ispad) {
				convertView = layoutInflater.inflate(R.layout.yidian_expandlist_item_group_pad, null);
			} else {
				convertView = layoutInflater.inflate(R.layout.yidian_expandlist_item_group, null);
			}
			
			expandListView.setGroupIndicator(YiDianDishActivity.this.getResources().getDrawable(R.drawable.expandablelistview_selector));
			
			TextView nameText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishesname);//��Ʒ����
			
			ImageView plusImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_dishesplus);//+ ��ť
			TextView countsText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishescounts);//��Ʒ����
			ImageView minusImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_dishesminus);//- ��ť
			
			TextView priceText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishesprice);//�۸�
			
			TextView unitText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishesunit);//��λ
			
			TextView subtotalText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishessubtotal);//С��
			
			ImageView presentImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_present);//���Ͱ�ť
			ImageView additionImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_addition);//�����ť
			ImageView deleteImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_delete);//ɾ����ť
			
			// TODO �ض����ֻ��水ť��С�߾�
			if (!ChioceActivity.ispad) {
				int buttonwidth  = screemwidth/11;
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonwidth, buttonwidth);
				lp.setMargins(4, 3, 4, 3);
				plusImage.setLayoutParams(lp);
				minusImage.setLayoutParams(lp);
				presentImage.setLayoutParams(lp);
				additionImage.setLayoutParams(lp);
				deleteImage.setLayoutParams(lp);
				LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(screemwidth/8,screemwidth/15);
				lp2.setMargins(3, 3, 3, 5);
				countsText.setLayoutParams(lp2);
			}
			
			//�ײͲ�����Ӹ�����ײ���ϸ�����,����ť��Ϊ���ɵ�
			if(f.isIstc() && f.getPcode().equals(f.getTpcode())){
				additionImage.setVisibility(View.INVISIBLE);
			}
			
			//����и��������ʾ��û�о����صڶ���
			LinearLayout singleAddShowLayout = (LinearLayout) convertView.findViewById(R.id.yidian_singlefujia_group_linear);
			TextView singleAddShowText = (TextView) convertView.findViewById(R.id.yidian_singlefujia_group_show);
			
			if(ValueUtil.isNotEmpty(f.getFujianame())||ValueUtil.isNotEmpty(f.getComfujianame())){
                String fujia=ValueUtil.isEmpty(f.getFujianame())?"":f.getFujianame().replace("!", ",");
                fujia+=ValueUtil.isEmpty(f.getComfujianame())?"":f.getComfujianame().replace("!", ",");
				singleAddShowLayout.setVisibility(View.VISIBLE);
				singleAddShowText.setText(fujia);
			}else{
				singleAddShowLayout.setVisibility(View.GONE);
			}


            //=======================�ж��Ƿ�൥λ======================
            if(ValueUtil.isNotEmpty(f.getUnitMap())){
                unitText.setOnClickListener(new DishUnitClick(f));
            }
            //============================================
			//������ײͣ����� + ��ť������ �� ��ť��������ʾ
			if(f.isIstc()){
				plusImage.setVisibility(View.INVISIBLE);
				minusImage.setVisibility(View.INVISIBLE);	
			}else{
                countsText.setOnClickListener(new DishCountsClick(f));
            }
			
			//����ǵڶ���λ��Ʒ������ + ��ť������ �� ��ť��������ʾ
			if(!f.isIstc() && ValueUtil.isNotEmpty(f.getWeightflg()) && f.getWeightflg().equals("2")){
				plusImage.setVisibility(View.INVISIBLE);
				minusImage.setVisibility(View.INVISIBLE);	
			}
			
			
			//����漰�����ˣ�Ʒ���͸��� 
			if(ValueUtil.isNotEmpty(f.getPromonum())){  //����˵���������
				
				//���������  �ײ�    ��ͨ��Ʒ
				if(f.isIstc()){
					StringBuilder sb = new StringBuilder();
					sb.append(f.getPcname())
					.append("-"+YiDianDishActivity.this.getString(R.string.give))
					.append(f.getPromonum());//��Ϊ�ײ�ֻ��һ�ݣ��ײͶ��ǵ�����ʾ�ģ�����ֱ����1Ҳ����
					
					nameText.setText(sb.toString());
				}else if(!f.isIstc()){
					StringBuilder sb = new StringBuilder();
					sb.append(f.getPcname())
					.append("-"+YiDianDishActivity.this.getString(R.string.give))
					.append(f.getPromonum());
					
					nameText.setText(sb.toString());
				}
			}else{//����û������
				String [] realPcname = f.getPcname().split("-");
				nameText.setText(realPcname[0]);
			}
			
			
			countsText.setText(f.getPcount());//����
			priceText.setText(f.getPrice());//�۸�
			unitText.setText(f.getUnit());//��λ
			
			//��С�Ƹ�ֵ, ֻ���ײ�����ͨ��Ʒ����С�ƣ�����ֵ,�ײ���ϸû��С��
			if(f.isIstc() && f.getPcode().equals(f.getTpcode()) || !f.isIstc()){
				double itemSubtotal = getItemSubtotal(f);
				subtotalText.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
			}
			
			//Ϊ��ť���ü���
			additionImage.setOnClickListener(new AdditionChildClick(f));
			plusImage.setOnClickListener(new PlusImageClick(f));
			minusImage.setOnClickListener(new MinusImageClick(f));
			
			deleteImage.setTag(groupPosition);//����tag,��Ȼ�Ͳ�����λ
			deleteImage.setOnClickListener(new DeleteClick(f));
			
			presentImage.setOnClickListener(new PresentImageClick(f));
			return convertView;
		}


		/**
		 * ��Ʒ������ĵ���¼�,��ȫ��������
		 */
		class AdditionChildClick implements View.OnClickListener{
			
			private Food mFood;
					
			public AdditionChildClick(Food pFood) {
				super();
				this.mFood = pFood;
			}


			@Override
			public void onClick(View v) {
				showSingleFujiaDia(mFood);
			}
					
		}
		
		
		//�����˰�ť
		class PresentImageClick implements OnClickListener{
			private Food f;

			public PresentImageClick(Food f) {
				super();
				this.f = f;
			}


			@Override
			public void onClick(View v) {
				if(f.isIstc() && ValueUtil.isNotEmpty(f.getPromonum())){  //˵�����ײ�������������״̬�����ʱ���͵���ȡ���Ի���
					AlertDialog.Builder builder = new AlertDialog.Builder(YiDianDishActivity.this,R.style.edittext_dialog);
					builder.setTitle(R.string.hint);
					builder.setMessage(R.string.whether_cancel_the_gift);
					builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							cancelFoodPromnum(f, guestDishes, "");//��Ϊ�˴���ȡ���ײͣ��ײ�ֻ��һ�ݣ�����������ֱ����""
							updateDisplay();//���½���
						}
					});
					builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					builder.create().show();
				}else if(f.isIstc() && ValueUtil.isEmpty(f.getPromonum())){  //˵�����ײͻ�û������
					showDiaAuthOrCount(f);
				}else if(!f.isIstc()){   //�Է��ײͽ��д���
					showDiaAuthOrCount(f);
				}
			}
			
		}


		/**
		 * ������  - ��ť�ĵ����,���ּӼ������ֻ��������ͨ��Ʒ���ײͲ��ܼӼ�
		 */
		class MinusImageClick implements OnClickListener{
			
			private Food food;
			
			public MinusImageClick(Food food) {
				super();
				this.food = food;
			}


			@Override
			public void onClick(View v) {
				Double dishCounts = Double.parseDouble(food.getPcount());
				if(dishCounts == 1){
					for(Iterator<Food> it = guestDishes.iterator();it.hasNext();){
						Food mFood = it.next();
						if(!mFood.isIstc() && mFood.getPcode().equals(food.getPcode())){
							it.remove();
						}
					}
					
				}else if(dishCounts > 1){
					dishCounts -= 1;
					food.setPcount(dishCounts+"");
				}
				
				updateDisplay();//���½���View
				YiDianDishActivity.hasChanged = true;
			}
			
		}
		
		//������  + ��ť�ĵ����,���ּӵ����ֻ��������ͨ��Ʒ���ײͲ��ܼ�
		class PlusImageClick implements OnClickListener{
			
			private Food food;

			public PlusImageClick(Food food) {
				super();
				this.food = food;
			}

			@Override
			public void onClick(View v) {
				Double dishCounts = Double.valueOf(food.getPcount());
				dishCounts += 1;
				food.setPcount(dishCounts+"");
				
				updateDisplay();//���½���View
				YiDianDishActivity.hasChanged = true;
			}
			
		}


		/**
		 * ��ɾ����ť�ĵ����
		 */
		class DeleteClick implements OnClickListener{
			
			private Food foodRemove;
			
			public DeleteClick(Food mFood) {
				super();
				this.foodRemove = mFood;
			}

			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(YiDianDishActivity.this, R.style.edittext_dialog);
				builder.setMessage(R.string.if_delete_food)
				.setPositiveButton(R.string.delete_yc, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						List<Food> saveList= new ValueSetFile(menu.getTableNum()
								+DateFormat.getStringByDate(new Date(),"yyyy-MM-dd")
								+menu.getMenuOrder()+".mc").read();
						for (Iterator<Food> it = guestDishes.iterator();it.hasNext();)
						{
							Food food = it.next();
							if((food.isIstc() && food.getTpcode().equals(foodRemove.getPcode()) && food.getTpnum().equals(foodRemove.getTpnum()))||!food.isIstc() && foodRemove.getPcode().equals(food.getPcode())&&foodRemove.getUnit().equals(food.getUnit())){
								it.remove();
							}
						}
						for(Iterator<Food> it = saveList.iterator();it.hasNext();){
							Food food = it.next();
							if((food.isIstc() && food.getTpcode().equals(foodRemove.getPcode()) && food.getTpnum().equals(foodRemove.getTpnum()))||!food.isIstc() && foodRemove.getPcode().equals(food.getPcode())&&foodRemove.getUnit().equals(food.getUnit())){
								it.remove();
							}
						}

						new ValueSetFile(menu.getTableNum()
								+DateFormat.getStringByDate(new Date(),"yyyy-MM-dd")
								+menu.getMenuOrder()+".mc").write(saveList);
						updateDisplay();//���½���
										
						YiDianDishActivity.hasChanged = true;
						
						
					}
				})
				.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				
				builder.show();
				
				
			}
			
		}
		
		
		@Override
		public boolean hasStableIds() {
			
			return false;
		}
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			
			return false;
		}
		
		
		/**
		 * //�ڴ˴�����ÿ����Ʒ��С��
		 * @param pFood
		 * @return ���ص���double���ͣ��ڸ�С��setTextʱ����ValueUtil.setScale(val, newScale, roundingMode)
		 */
		public double getItemSubtotal(Food pFood){
			//�ڴ˴�����С��,����漰�����ˣ��ͺܸ���
				if(!pFood.isIstc()){ //���ײ�,��ͨ�ĵ�Ʒ
					double itemSubtotal = 0.00;
					
					String presentCount = pFood.getPromonum();//�õ����������Ʒ����������
					if(ValueUtil.isNotEmpty(presentCount)){ //˵��������
						int countPresent = Integer.parseInt(presentCount);
						int countPcount = Integer.parseInt(pFood.getPcount());
						int countPcountLeft = countPcount - countPresent;//��˵�����������ȥ���Ͳ�Ʒ���������õ�ʣ�µ�����
						double counts = Double.valueOf(countPcountLeft +"");
						double price = Double.valueOf(pFood.getPrice());
						itemSubtotal = counts * price;
					}else{   //˵��û������
						double counts = Double.valueOf(pFood.getPcount());
						double price = Double.valueOf(pFood.getPrice());
						itemSubtotal = counts * price;
					}
					
					return itemSubtotal;
				}else{   //���ײ�,��Ϊ�ײͶ��ǵ�����ʾ��
					double itemTcSubtotal = 0.00;
					String presentCount = pFood.getPromonum();
					if(ValueUtil.isEmpty(presentCount)){//����û������
						return Double.valueOf(pFood.getPrice());
					}else{//����������,������ײ��ѱ�����,ֱ�ӷ���0.00����
						return itemTcSubtotal;
					}
					
				}
				
		}
		
	}
	
	
	
	
	/**
	 * ��������Ĳ�ͬ�������ʾ��ͬ�ĵ����Ի���
	 * @param pFood
	 */
	protected void showDiaAuthOrCount(Food pFood) {
		
		//���������������Ͳ�Ʒ�Ľ��
		int moneyAuth = Integer.parseInt(SharedPreferencesUtils.getGiftAuth(YiDianDishActivity.this));
		
		//���ж������Ʒ������
		int dishCount = Integer.parseInt(pFood.getPcount());
		double moneyPresent = 0.00;
		
		//���Ͳ�Ʒ�۸����49����Ҫ��Ȩ,�Ƚϸ��ӣ��÷��������
		if(pFood.isIstc()){  //�ײ͵�����ֻ��һ��
			moneyPresent = Double.valueOf(pFood.getPrice());
		}else{ //��ͨ��Ʒ
			moneyPresent = Double.valueOf(pFood.getPrice());
		}
		
		if(dishCount > 1 && moneyPresent > moneyAuth){   //������Ȩ����������
			createPresentAuthAndCountDia(pFood);
		}else if(dishCount == 1 && moneyPresent > moneyAuth){//������Ȩ���� 
			createPresentAuthDia(pFood);
		}else if(moneyPresent < moneyAuth){   //������������
			createPresentCountDia(pFood);
		}

	}
	
	
	/**
	 * //������˰�ť���Ϊfood���е�Promonum��PresentCode��ֵ
	 * @param pFood  �ǵ���Ʒ
	 * @param plist 
	 * @param presentReason
	 * @param promCounts
	 */
	private void setFoodPromnumAndReason(Food pFood,List<Food> plist,String presentReason,String promCounts){
		if(pFood.isIstc()){
			for(Food food:plist){//Ϊ�ײͺ��ײ���ϸͬʱ��������������ԭ��
				if(food.isIstc() && food.getTpcode().equals(pFood.getPcode()) && pFood.getTpnum().equals(food.getTpnum())){
					food.setPromonum("1");//��Ϊ�ײ�ֻ��һ�ݣ�ֱ���� 1 Ҳ����,��pFood.getPcount()Ҳ��
					food.setPresentCode(presentReason);
				}
			}
		}else{
			//�����ǰ�Ѿ����͵�������Ϊ�գ������
			if(ValueUtil.isNotEmpty(pFood.getPromonum())){
				int countGiftBefore = Integer.parseInt(pFood.getPromonum());//�õ���ǰ�Ѿ����͵�����
				int countGiftNowPre = Integer.parseInt(promCounts);
				int countGiftEnd = countGiftBefore + countGiftNowPre;
				pFood.setPromonum(countGiftEnd+"");
				pFood.setPresentCode(presentReason);
			}else{
				pFood.setPromonum(promCounts);
				pFood.setPresentCode(presentReason);
			}
		}
	}
	
	/**
	 * //����������Ȩ�������ĶԻ���
	 * @param dianDishes
	 */
	private void createPresentAuthAndCountDia(final Food dianDishes) {
		PresentAuthAndCountDialog authAndcountDia = PresentAuthAndCountDialog.newInstance(this.getString(R.string.give_food_authorization), (ArrayList<PresentReason>)presentreasonLists, new DialogPresentAuthAndCountClickListener() {
			
			@Override
			public void doPositiveClick(String authorNum, String authorPwd,final String reason, final String countgift, final String countCancel) {
				//���ж���Ȩ,���ж�����������Ƿ���ȷ
				CList<Map<String, String>> clists = new CList<Map<String,String>>();
				clists.add("deviceId", SharedPreferencesUtils.getDeviceId(YiDianDishActivity.this));
				clists.add("userCode", authorNum);
				clists.add("userPass", authorPwd);
				new Server().connect(YiDianDishActivity.this, Constants.FastMethodName.CHECKAUTH_METHODNAME, Constants.FastWebService.CHECKAUTH_WSDL, clists, new OnServerResponse() {
					@Override
					public void onResponse(String result) {
						authLoadingDialog.dismiss();
						if(ValueUtil.isNotEmpty(result)){
							String [] flag = result.split("@");
							if(flag[0].equals("0")){
								//��Ȩ�ɹ�
								Log.e("��Ȩ����ֵ", result);
								if(ValueUtil.isNotEmpty(countgift) && ValueUtil.isNotEmpty(countCancel)){
									Toast.makeText(YiDianDishActivity.this,R.string.input_count_error,Toast.LENGTH_SHORT).show();
								}else if(ValueUtil.isNotEmpty(countgift) && ValueUtil.isEmpty(countCancel)){//��ʾ����
									String promonumgiftBefore = dianDishes.getPromonum();//�õ���ǰ�Ѿ����͵�����
									
									if(ValueUtil.isNotEmpty(promonumgiftBefore) && Integer.parseInt(promonumgiftBefore) + Integer.parseInt(countgift) > Integer.parseInt(dianDishes.getPcount()) || Integer.parseInt(dianDishes.getPcount()) < Integer.parseInt(countgift) || Integer.parseInt(countgift) < 1){
										Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
										return;
									}
									setFoodPromnumAndReason(dianDishes, guestDishes, reason, countgift);//���ｫȫ����guestDishes���˽�ȥ
									
									//�����ǰ��������ģʽ��
									if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
										etSearchView.setText("");
										listView.setVisibility(View.GONE);
										expandListView.setVisibility(View.VISIBLE);
									}
									
									updateDisplay();
								}else if(ValueUtil.isEmpty(countgift) && ValueUtil.isNotEmpty(countCancel)){//��ʾȡ������
									if(ValueUtil.isEmpty(dianDishes.getPromonum()) || Integer.parseInt(dianDishes.getPromonum()) < Integer.parseInt(countCancel)|| Integer.parseInt(countCancel) < 1){
										Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
										return;
									}
									cancelFoodPromnum(dianDishes, guestDishes, countCancel);
									
									//�����ǰ��������ģʽ��
									if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
										etSearchView.setText("");
										listView.setVisibility(View.GONE);
										expandListView.setVisibility(View.VISIBLE);
									}
									updateDisplay();
								}else{
									
								}
								
							}else{
								Toast.makeText(YiDianDishActivity.this, R.string.not_jurisdiction, Toast.LENGTH_SHORT).show();
							}
							
							
						}else{
							Toast.makeText(YiDianDishActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
						}
					}
					
					@Override
					public void onBeforeRequest() {
						authLoadingDialog.show();
					}
				});
									
			}
			
			@Override
			public void doNegativeClick() {
				
			}
		});
		
		authAndcountDia.show(getSupportFragmentManager(), "authAndCountDia");
	}
	
	
	
	/**
	 * //ȡ������
	 * @param pFood ��Ҫ�������ǵ��˵Ķ���
	 * @param plist ����
	 * @param cancelCounts   ȡ�����˵�����
	 */
	private void cancelFoodPromnum(Food pFood,List<Food> plist,String cancelCounts){
		if(pFood.isIstc()){//������ײ�
			for(Food food:plist){
				if(food.isIstc() && food.getTpcode().equals(pFood.getPcode()) && pFood.getTpnum().equals(food.getTpnum())){
					food.setPromonum("");//�˴��Ƚ�����,��Ϊ�ײ���һ�ݣ�ȡ��,������ֱ�Ӹ�ֵΪ""
				}
			}
		}else{//����ڷ��ײ�
			int presentBefore = Integer.parseInt(pFood.getPromonum());//�õ�ԭ�е����͵����� 
			int cancelCountsInt = Integer.parseInt(cancelCounts);//�õ�Ҫȡ�����͵�����
			int countPromLeft = presentBefore - cancelCountsInt;
			
			if(countPromLeft == 0){//�������ȫ��ȡ��
				pFood.setPromonum("");
			}else{
				pFood.setPromonum(countPromLeft+"");
			}

		}
	}
	
	/**
	 * //����ֻ��ʾ�����ĶԻ���
	 * @param dianDishes
	 */
	protected void createPresentCountDia(final Food dianDishes) {
		
		PresentCountDialog countDia = PresentCountDialog.newInstance(this.getString(R.string.give_food_authorization), (ArrayList<PresentReason>)presentreasonLists, new DialogPresentCountClickListener() {
			
			@Override
			public void doPositiveClick(String giftCounts, String cancelGiftCounts,String reason) {
				if(ValueUtil.isNotEmpty(giftCounts) && ValueUtil.isNotEmpty(cancelGiftCounts)){
					Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
				}else if(ValueUtil.isNotEmpty(giftCounts) && ValueUtil.isEmpty(cancelGiftCounts)){//��ʾ����
					String promonumgiftBefore = dianDishes.getPromonum();//�õ���ǰ�Ѿ����͵�����
					
					if(ValueUtil.isNotEmpty(promonumgiftBefore)&&Integer.parseInt(promonumgiftBefore) + Integer.parseInt(giftCounts) > Integer.parseInt(dianDishes.getPcount()) || Integer.parseInt(dianDishes.getPcount()) < Integer.parseInt(giftCounts) || Integer.parseInt(giftCounts) < 1){
						Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
						return;
					}
					
					setFoodPromnumAndReason(dianDishes, guestDishes, reason,giftCounts);
					
					//�����ǰ��������ģʽ��
					if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
						etSearchView.setText("");
						listView.setVisibility(View.GONE);
						expandListView.setVisibility(View.VISIBLE);
					}
					updateDisplay();//����ҳ��
				}else if(ValueUtil.isEmpty(giftCounts) && ValueUtil.isNotEmpty(cancelGiftCounts) ){//��ʾȡ������
					if(ValueUtil.isEmpty(dianDishes.getPromonum()) || Integer.parseInt(dianDishes.getPromonum()) < Integer.parseInt(cancelGiftCounts) || Integer.parseInt(cancelGiftCounts) < 1){
						Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
						return;
					}
					cancelFoodPromnum(dianDishes, guestDishes, cancelGiftCounts);
					
					//�����ǰ��������ģʽ��
					if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
						etSearchView.setText("");
						listView.setVisibility(View.GONE);
						expandListView.setVisibility(View.VISIBLE);
					}
					updateDisplay();//����ҳ��
				}else{
					
				}
			}
			
			@Override
			public void doNegativeClick() {
				
			}
		});
		
		countDia.show(getSupportFragmentManager(), "PresentCountDia");
	}
	
	
	/**
	 * //������Ȩ�����˶Ի���
	 * @param pFood
	 */
	protected void createPresentAuthDia(final Food pFood) {
		PresentAuthDialog dialogAuthPresent = PresentAuthDialog.newInstance(this.getString(R.string.give_food), (ArrayList<PresentReason>)presentreasonLists, new DialogPresentAuthClickListener() {
			
			@Override
			public void doPositiveClick(String authorNum, String authorPwd,final String reason) {
				CList<Map<String, String>> clists = new CList<Map<String,String>>();
				clists.add("deviceId", SharedPreferencesUtils.getDeviceId(YiDianDishActivity.this));
				clists.add("userCode", authorNum);
				clists.add("userPass", authorPwd);
				new Server().connect(YiDianDishActivity.this, Constants.FastMethodName.CHECKAUTH_METHODNAME, Constants.FastWebService.CHECKAUTH_WSDL, clists, new OnServerResponse() {
					
					@Override
					public void onResponse(String result) {
						authLoadingDialog.dismiss();
						if(ValueUtil.isNotEmpty(result)){
							
							
							String [] flag = result.split("@");
							if(flag[0].equals("0")){
								//��Ȩ�ɹ�
								Log.e("��Ȩ����ֵ", result);
								
								setFoodPromnumAndReason(pFood, guestDishes, reason,"1");//��Ϊֻ����������1ʱ�����ҵ�����Ʒ�۸����49���ŵ����˶Ի���ֱ���� 1
								
								//�����ǰ��������ģʽ��
								if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
									etSearchView.setText("");
									listView.setVisibility(View.GONE);
									expandListView.setVisibility(View.VISIBLE);
								}
								updateDisplay();//����ҳ��
							}else{
								Toast.makeText(YiDianDishActivity.this, R.string.not_jurisdiction, Toast.LENGTH_SHORT).show();
							}
							
							
						}else{
							Toast.makeText(YiDianDishActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
						}
					}
					
					@Override
					public void onBeforeRequest() {
						authLoadingDialog.show();
					}
				});
				
			}
			
			@Override
			public void doNegativeClick() {
				
			}
		});
		
		dialogAuthPresent.show(getSupportFragmentManager(), "presentAuthDia");
	}
	
	/**
	 * ���������
	 */
	TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {	
			matchLists.clear();
			String changingStr = etSearchView.getText().toString().trim();
			if(ValueUtil.isNotEmpty(changingStr)){
				
				//��ǰ����������ģʽ����expandListView���أ�listView��ʾ
				expandListView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				
				for(Food food:guestDishes){
					String currentStr = food.getPcname();
					String numEncode = food.getPcode();
					if(currentStr.indexOf(changingStr)!=-1 || ConvertPinyin.convertJianPin(currentStr).indexOf(changingStr) !=-1 ||ConvertPinyin.convertQuanPin(currentStr).indexOf(changingStr)!=-1 || numEncode.indexOf(changingStr)!=-1){
						matchLists.add(food);
					}
				}
				
				if(searchAdapter != null){
					searchAdapter.setSearchSource(matchLists);
					searchAdapter.notifyDataSetChanged();
				}else{
					searchAdapter = new YiDianDishSearchAdapter(YiDianDishActivity.this, matchLists);
					listView.setAdapter(searchAdapter);
				}
			}else{//��������Ϊ��ʱ���ͻָ�������ģʽ
				listView.setVisibility(View.GONE);
				expandListView.setVisibility(View.VISIBLE);
			}
		}
	};


	/**
	 * ��ʾ������ĶԻ���
	 * @param pFood
	 */
	private void showSingleFujiaDia(final Food pFood){
        new FujiaTypeView(this,new IResult<FujiaType>() {
            @Override
            public void result(FujiaType type) {
                String fj = "select INIT,PCODE,FCODE as FOODFUJIA_ID,FNAME AS FOODFUJIA_DES,\n" +
                        "       Fprice as FOODFUJIA_CHECKED,'false' as ISSELECTED,\n" +
                        "              MAXCNT,MINCNT,PRODUCTTC_ORDER,DEFUALTS,ISADDPROD,\n" +
                        "                     ISMUST from FoodFuJia where pcode=? and rgrp=? order by DEFUALTS";
                List<Addition> list=new ListProcessor().query(fj, new String[]{pFood.getPcode(),type.getPk_redefine_type()}, YiDianDishActivity.this, Addition.class);
                if(ValueUtil.isEmpty(list)){
                    fj = "select INIT,PCODE,FCODE as FOODFUJIA_ID,FNAME AS FOODFUJIA_DES,\n" +
                            "       Fprice as FOODFUJIA_CHECKED,'false' as ISSELECTED,\n" +
                            "              MAXCNT,MINCNT,PRODUCTTC_ORDER,DEFUALTS,ISADDPROD,\n" +
                            "                     ISMUST from FoodFuJia where (pcode is null or pcode like '%PCODE%' or PCODE ='') and rgrp=? order by DEFUALTS";
                    list=new ListProcessor().query(fj, new String[]{type.getPk_redefine_type()}, YiDianDishActivity.this, Addition.class);
                }
                IFujiaResult r=new IFujiaResult(){
                    @Override
                    public void result() {
                        adapter.notifyDataSetChanged();
                    }
                };
                new ComFujiaView(YiDianDishActivity.this,pFood,type,r).showView(list);
            }
        }).showView(pFood);
    }

	
	/**
	 * ����listview��Ӧ��������
	 * @author dell
	 *
	 */
	public class YiDianDishSearchAdapter extends BaseAdapter {
		
		private Context mContext;
		private List<Food> lists;
		private LayoutInflater inflater;
		

		public YiDianDishSearchAdapter(Context pContext, List<Food> lists) {
			super();
			this.mContext = pContext;
			this.lists = lists;
			inflater = LayoutInflater.from(mContext);
		}
		
		
		public void setSearchSource(List<Food> pLists){
			this.lists = pLists;
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Food food = lists.get(position);
			
			ViewYiDianSearchHolder holder =null;
			
			if(convertView == null){
				holder = new ViewYiDianSearchHolder();
				convertView = inflater.inflate(R.layout.dia_yidian_search_listitem, null);
				holder.dishName = (TextView) convertView.findViewById(R.id.yidian_search_listitem_dishesname);
				holder.plusImage = (ImageView) convertView.findViewById(R.id.yidian_search_listitem_dishesplus);
				holder.dishCounts = (TextView) convertView.findViewById(R.id.yidian_search_listitem_dishescounts);
				holder.minusImage = (ImageView) convertView.findViewById(R.id.yidian_search_listitem_dishesminus);
				holder.dishPrice = (TextView) convertView.findViewById(R.id.yidian_search_listitem_dishesprice);
				holder.dishUnit = (TextView) convertView.findViewById(R.id.yidian_search_listitem_dishesunit);
				
				holder.presentImage = (ImageView) convertView.findViewById(R.id.yidian_search_listitem_present);
				holder.fujiaImage = (ImageView) convertView.findViewById(R.id.yidian_search_listitem_addition);
				holder.deleteImage = (ImageView) convertView.findViewById(R.id.yidian_search_listitem_delete);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewYiDianSearchHolder) convertView.getTag();
			}
			
			//�������Ǵ��ײ�.
			if(food.isIstc() && food.getPcode().equals(food.getTpcode())){
				holder.plusImage.setVisibility(View.INVISIBLE);
				holder.minusImage.setVisibility(View.INVISIBLE);
			}else{
                holder.dishCounts.setOnClickListener(new DishCountsClick(food));
            }
			//=======================�ж��Ƿ�൥λ======================
            if(ValueUtil.isNotEmpty(food.getUnitMap())){
                holder.dishUnit.setOnClickListener(new DishUnitClick(food));
            }
            //============================================
			//���������ײ���ϸ
			if(food.isIstc() && !food.getPcode().equals(food.getTpcode())){
				holder.plusImage.setVisibility(View.INVISIBLE);
				holder.minusImage.setVisibility(View.INVISIBLE);
				
				holder.presentImage.setVisibility(View.INVISIBLE);
				holder.deleteImage.setVisibility(View.INVISIBLE);
			}else{
                holder.dishCounts.setOnClickListener(new DishCountsClick(food));
            }
			String dishName=food.getPcname();
			if(food.getIsTemp()==1){
				dishName=dishName+"--"+food.getTempName();
			}
			holder.dishName.setText(dishName);
			holder.dishCounts.setText(food.getPcount());
			holder.dishPrice.setText(food.getPrice());
			holder.dishUnit.setText(food.getUnit());
			
			
			holder.plusImage.setOnClickListener(new PlusImageClick(food));
			holder.minusImage.setOnClickListener(new MinusImageClick(food));
			holder.presentImage.setOnClickListener(new PresentImageClick(food));
			holder.fujiaImage.setOnClickListener(new FujiaImageClick(food));
			holder.deleteImage.setOnClickListener(new DeleteImageClick(food));
			
			return convertView;
		}
		
		/**
		 * ���������ģʽ�µ�ɾ����ť
		 * @author dell
		 *
		 */
		class DeleteImageClick implements OnClickListener{
			private Food food;

			public DeleteImageClick(Food food) {
				super();
				this.food = food;
			}

			@Override
			public void onClick(View v) {
				if(food.isIstc()){
					List<Food> deleteLists = new ArrayList<Food>();
					for(Food f:guestDishes){
						if(f.isIstc() && f.getTpcode().equals(food.getPcode()) && f.getTpnum().equals(food.getTpnum())){
							deleteLists.add(f);
						}
					}
					
					guestDishes.removeAll(deleteLists);
				}else if(!food.isIstc()){
					guestDishes.remove(food);
				}
				
				etSearchView.setText("");
				listView.setVisibility(View.GONE);
				expandListView.setVisibility(View.VISIBLE);
				updateDisplay();
				YiDianDishActivity.hasChanged = true;
			}
			
			
		}
		/**
		 * ���������ģʽ�µ���Ӹ����ť
		 * @author dell
		 *
		 */
		class FujiaImageClick implements OnClickListener{
			private Food food;

			public FujiaImageClick(Food food) {
				super();
				this.food = food;
			}


			@Override
			public void onClick(View v) {
				if(food.isIstc() && food.getTpcode().equals(food.getPcode())){
					Toast.makeText(YiDianDishActivity.this, R.string.meal_not_add_items, Toast.LENGTH_SHORT).show();
					return;
				}
				showSingleFujiaDia(food);
			}
			
		}
		
		/**
		 * ���������ģʽ�µ����Ͱ�ť
		 * @author dell
		 *
		 */
		class PresentImageClick implements OnClickListener{
			
			private Food f;
			
			public PresentImageClick(Food food) {
				super();
				this.f = food;
			}


			@Override
			public void onClick(View v) {
				if(f.isIstc() && ValueUtil.isNotEmpty(f.getPromonum())){  //˵�����ײ�������������״̬�����ʱ���͵���ȡ���Ի���
					AlertDialog.Builder builder = new AlertDialog.Builder(YiDianDishActivity.this);
					builder.setTitle(R.string.hint);
					builder.setMessage(R.string.whether_cancel_the_gift);
					builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							cancelFoodPromnum(f, guestDishes, "");//��Ϊ�˴���ȡ���ײͣ��ײ�ֻ��һ�ݣ�����������ֱ����""
							
							
							//�����ǰ��������ģʽ��
							if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
								etSearchView.setText("");
								listView.setVisibility(View.GONE);
								expandListView.setVisibility(View.VISIBLE);
							}
							
							updateDisplay();//���½���
						}
					});
					builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					builder.create().show();
				}else if(f.isIstc() && ValueUtil.isEmpty(f.getPromonum())){  //˵�����ײͻ�û������
					showDiaAuthOrCount(f);
				}else if(!f.isIstc()){   //�Է��ײͽ��д���
					showDiaAuthOrCount(f);
				}
			}
			
		}

		/**
		 * ���������ģʽ�µ����� - ��ť
		 * @author dell
		 *
		 */
		class MinusImageClick implements OnClickListener{
			
			private Food food;
			
			public MinusImageClick(Food food) {
				super();
				this.food = food;
			}


			@Override
			public void onClick(View v) {
				int dishCounts = Integer.valueOf(food.getPcount());
				int subCounts = Integer.valueOf(food.getCounts());
				if(dishCounts == 1){
					guestDishes.remove(food);
				}else if(dishCounts > 1){
					dishCounts-=1;
					food.setPcount(dishCounts+"");
					
					subCounts-=1;
					food.setCounts(subCounts +"");
					
				}
				
				etSearchView.setText("");
				listView.setVisibility(View.GONE);
				expandListView.setVisibility(View.VISIBLE);
				updateDisplay();//���½���View
				YiDianDishActivity.hasChanged = true;
			}
			
		}
		/**
		 * ���������ģʽ�µ�����  + ��ť
		 * @author dell
		 *
		 */
		class PlusImageClick implements OnClickListener{
			
			private Food food;

			public PlusImageClick(Food food) {
				super();
				this.food = food;
			}


			@Override
			public void onClick(View v) {
				for(Food mFood:guestDishes){
					if(food == mFood){
						int counts = Integer.parseInt(mFood.getPcount());
						counts += 1;
						
						mFood.setCounts(counts+"");
						mFood.setPcount(counts+"");
						
						etSearchView.setText("");
						listView.setVisibility(View.GONE);
						expandListView.setVisibility(View.VISIBLE);
						updateDisplay();//���½���View
						YiDianDishActivity.hasChanged = true;
					}
				}
			}
			
		}
		
		
		private class ViewYiDianSearchHolder{
			TextView dishName;
			ImageView plusImage;
			TextView dishCounts;
			ImageView minusImage;
			TextView dishPrice;
			TextView dishUnit;
			
			ImageView presentImage;
			ImageView fujiaImage;
			ImageView deleteImage;
			
		}

	}
	
	public void alertReserve(String text){
		Toast.makeText(this, text==null?this.getString(R.string.reserve_table_error):text, Toast.LENGTH_LONG).show();
	}
	
	
	/**
	 * ��ȫ��������ļ����� 
	 * @author dell
	 *
	 */
	private class SingleFujiaWatcher implements TextWatcher{
		
		private EditText fujiaEdit;
		private List<Addition> additionLists;
		private SingleFujiaAdapter singleFujiaAdapter;
		

		public SingleFujiaWatcher(EditText fujiaEdit,List<Addition> additionLists,SingleFujiaAdapter singleFujiaAdapter) {
			super();
			this.fujiaEdit = fujiaEdit;
			this.additionLists = additionLists;
			this.singleFujiaAdapter = singleFujiaAdapter;
		}

		@Override
		public void afterTextChanged(Editable s) {	}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {	}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			fujiamatchLists.clear();
			String changingStr = fujiaEdit.getText().toString().trim();
			if(ValueUtil.isNotEmpty(changingStr)){
				for(Addition addition:additionLists){
					String currentStr = addition.getInit();
					if(currentStr.indexOf(ValueUtil.exChangeToLower(changingStr)) != -1  || currentStr.indexOf(ValueUtil.exChangeToUpper(changingStr)) != -1){
						fujiamatchLists.add(addition);
					}
				}
				
				if(singleFujiaAdapter != null){
					singleFujiaAdapter.setFujiaLists(fujiamatchLists);
					singleFujiaAdapter.notifyDataSetChanged();
				}
				
			}else{
				singleFujiaAdapter.setFujiaLists(additionLists);
				singleFujiaAdapter.notifyDataSetChanged();
			}
			
		}
		
	}
	
	
	/**
	 * ȫ�������������õ�
	 */
	private class SpecialFujiaWatcher implements TextWatcher{
		
		private EditText fujiaEdit;
		private List<CommonFujia> specialComLists;
		private CommonFujiaAdapter commonFujiaAdapter;
		
		

		public SpecialFujiaWatcher(EditText fujiaEdit,List<CommonFujia> specialComLists,CommonFujiaAdapter commonFujiaAdapter) {
			super();
			this.fujiaEdit = fujiaEdit;
			this.specialComLists = specialComLists;
			this.commonFujiaAdapter = commonFujiaAdapter;
		}

		@Override
		public void afterTextChanged(Editable s) {}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

		@SuppressLint("DefaultLocale")
		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			specialComLists.clear();
			String changingFujiaStr = fujiaEdit.getText().toString().trim();
			
			if(ValueUtil.isNotEmpty(changingFujiaStr)){
				for(CommonFujia commFujia:specialComLists){
					String currentStr = commFujia.getINIT();
					if(currentStr.indexOf(ValueUtil.exChangeToLower(changingFujiaStr)) != -1  || currentStr.indexOf(ValueUtil.exChangeToUpper(changingFujiaStr)) != -1){
						specialfujiaMatchLists.add(commFujia);
					}
				}
				
				if(commonFujiaAdapter != null){
					commonFujiaAdapter.setCommonFujiaSource(specialComLists);
					commonFujiaAdapter.notifyDataSetChanged();
				}
			}else{
				commonFujiaAdapter.setCommonFujiaSource(specialComLists);
				commonFujiaAdapter.notifyDataSetChanged();
			}
			
		}
	}
    /**
     * �����޸�
     */
    class DishCountsClick implements OnClickListener{

        private Food food;

        public DishCountsClick(Food food) {
            super();
            this.food = food;
        }
        @Override
        public void onClick(View v) {
            LinearLayout layout=(LinearLayout)getLayoutInflater().inflate(R.layout.revamp_count_layout,null);
            final EditText text=(EditText)layout.findViewById(R.id.count_edittext);
            text.setText(food.getPcount());
            new AlertDialog.Builder(YiDianDishActivity.this,R.style.edittext_dialog).setTitle(R.string.revamp_counts)
                    .setView(layout).setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for(Food mFood:guestDishes){
                        if(food == mFood){
                            String countString=new DecimalFormat("#.00").format(ValueUtil.isNaNofBigDecimal(text.getText().toString()));
                            Double counts=ValueUtil.isNaNofDouble(countString);
                            if(counts<=0){
                                counts=1.0;
                            }
                            mFood.setCounts(counts+"");
                            mFood.setPcount(counts+"");

                            etSearchView.setText("");
                            listView.setVisibility(View.GONE);
                            expandListView.setVisibility(View.VISIBLE);
                            updateDisplay();//���½���View
                        }
                    }
                }
            }) .setNegativeButton(R.string.cancle,null).show();
        }

    }

    class DishUnitClick implements OnClickListener{
        private Food food;

        DishUnitClick(Food food) {
            this.food = food;
        }

        @Override
        public void onClick(View v) {
            new MuchUntiView(new IResult<Unit>() {
                @Override
                public void result(Unit unit) {
                    food.setUnit(unit.getUnitName());
                    food.setUnitCode(unit.getUnitCode());
                    food.setPrice(unit.getUnitPrice());
                    updateDisplay();//���½���View
                }
            },YiDianDishActivity.this,v).showView(food);
        }
    }
}
