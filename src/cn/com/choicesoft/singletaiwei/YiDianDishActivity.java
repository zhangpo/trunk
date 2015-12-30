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
 * 已点菜品页面
 * @author dell
 *
 */
public class YiDianDishActivity extends BaseActivity implements OnClickListener{
	
	
	private ExpandableListView expandListView;//页面正常显示数据绑定的控件是ExpandableListView
	private YidianDishAdapter adapter;//ExpandableListView对应的适配器
	
	
	private ListView listView;//搜索时数据显示的控件是ListView
	private YiDianDishSearchAdapter searchAdapter;//ListView对应的适配器
	
	private Button tablelogo;//台位
	private Button tempSave;//暂存,保存
	private Button allBillAddtions ;//特别备注
	private Button allBills;//全单
	private Button sendwait;//叫起
	private Button sendprompt;//即起
	private Button back;//返回
	private Button deleteAll;//全部删除
	
	private Button btnSearchView;
	private EditText etSearchView;//搜索框
	
	private List<Food> guestDishes = GuestDishes.getGuDishInstance();
	
	private List<Food> adapterDataSourceLists = null;//ExpandableListView的数据源
	
	private SingleMenu menu = SingleMenu.getMenuInstance();
	public static boolean hasChanged = false;
	
	private LoadingDialog authLoadingDialog;//验证授权 
	private LoadingDialog sendLoadingDialog;//发送菜品
	
	private TextView dishCounts,totalMoney,fujiaMoney;
	
	private TextView tableNum,orderNum,manNum,womanNum,userNum;//头部红色标题栏内容
	private List<PresentReason> presentreasonLists;//存放赠品集合
	private List<Food> matchLists = new ArrayList<Food>();//菜品信息检索完后的结果所匹配的集合
	private List<Addition> fujiamatchLists = new ArrayList<Addition>();//附加项信息检索完后的结果所匹配的集合
	private List<CommonFujia> specialfujiaMatchLists = new ArrayList<CommonFujia>();//全单附加项信息检索完后的结果所匹配的集合
	
	
	private TextView specialRemark;
	
	private LoadingDialog mLoadingDialog = null;
	
	private Dialog dialog = null;
	
	private LinearLayout digitalLayout = null;
	private ImageView menuinfo_imageView;
	private View layout;
	private LinearLayout topLinearLayout;
	
	public int screemwidth ;
    public AlertDialog typDialog;//附加项类别
			
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
	 *界面刷新
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
		
		dishCounts.setText(adapter.getDishSelectedCount()+ this.getString(R.string.part));//所点菜品的总数量
		
		double totalSalary = adapter.getTotalSalary();
		totalMoney.setText(ValueUtil.setScale(totalSalary, 2, 5));//所点菜品的总价钱
		
		double totalFujiaSalary = adapter.getFujiaSalary();
		fujiaMoney.setText(ValueUtil.setScale(totalFujiaSalary, 2, 5));//所点菜品附加项的总价钱
	}

	private void initViews() {
		
		mLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
		mLoadingDialog.setCancelable(false);
		
		digitalLayout = (LinearLayout) this.findViewById(R.id.toptitle_rg_number);
		digitalLayout.setVisibility(View.GONE);
		
		//菜品：3          总计：179.00      附加项：
		dishCounts = (TextView) this.findViewById(R.id.yidian_tv_dishcount);
		totalMoney = (TextView) this.findViewById(R.id.yidian_tv_totalSalary);
		fujiaMoney = (TextView) this.findViewById(R.id.yidian_tv_fujiaMoney);
				
		
		//顶部红色栏
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
		sendprompt.setText("上传");
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
		
		presentreasonLists = getDataManager(this).queryPresentReason();//查询数据库得出赠菜的原因
		
		
		//初始化会员信息
		VipMsg.iniVip(this, menu.getTableNum(), R.id.vipMsg_ImageView);
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_searchView:
			btnSearchView.setVisibility(View.GONE);
			etSearchView.setVisibility(View.VISIBLE);
			etSearchView.requestFocus();
			//弹出键盘
			break;
		case R.id.yidian_btn_back://返回
			this.finish();
			break;
		case R.id.eatable_btn_back://返回
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
			guestDishes.clear();//清空单例集合
			YiDianDishActivity.hasChanged = true;
			updateDisplay();
			break;
			
		case R.id.yidian_btn_sendwait:  //叫起发送
			if(TsData.isReserve){
				alertReserve(this.getString(R.string.only_save_error));
				break;
			}
			Send("2");
			break;
			
		case R.id.yidian_btn_sendprompt: //即起发送
			if(TsData.isReserve){
				alertReserve(this.getString(R.string.only_save_error));
				break;
			}
			Send("1");
			break;
					
		case R.id.yidian_btn_allbills:    //全单
			
			Intent intent = new Intent(YiDianDishActivity.this, YiXuanDishActivity2.class);
			Bundle bundle = new Bundle();
			bundle.putString("orderId", menu.getMenuOrder());//账单号
			bundle.putString("manCs", menu.getManCounts());//男人数量
			bundle.putString("womanCs", menu.getWomanCounts());//女人数量
			bundle.putString("tableNum", menu.getTableNum());//台位号
			
			intent.putExtra("topBundle", bundle);
			startActivity(intent);
			break;
			
		case R.id.yidian_btn_allbilladditions://特别备注
			
			final List<CommonFujia>  commonFujiaLists = getDataManager(this).queryCommonFujia();//存放公共附加项集合
			
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
					//弹出键盘
					InputMethodUtils.toggleSoftKeyboard(YiDianDishActivity.this);

				}
			});
			
			//自定义附加项的控件EditText
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
					
					StringBuilder buildCode = new StringBuilder();//编码
					final StringBuilder buildDes = new StringBuilder();//名称
					
					String comFujiaStr = selfFujiaEdit.getText().toString();
					if(ValueUtil.isNotEmpty(comFujiaStr)){
						
						buildCode.append("").append("!");
						buildDes.append(comFujiaStr).append("!");
						
						selfFujiaEdit.setText("");
					}
					
//					/*deviceId   设备编号
//					userCode  登陆编号
//					orderId    账单号
//					remarkId   特别备注编号   多个用！隔开    自定义附加项为空，但是多个也需用！隔开
//					remark    特别备注        多个用！隔开
//					flag       1 添加  0 取消*/
					List<CommonFujia> listComs = comFujiaAdapter.getSelectedItemLists();
					if(listComs.size() != 0){
						for(CommonFujia comFujia:listComs){
							buildCode.append(comFujia.getId()).append("!");
							buildDes.append(comFujia.getDES()).append("!");
						}
						
						//删除最后的！
						if(buildDes.toString().length()>0){
							buildDes.deleteCharAt(buildDes.toString().length() - 1);
						}
						if(buildCode.toString().length()>0){
							buildCode.deleteCharAt(buildCode.toString().length() - 1);
						}
					}	
					dialog.dismiss();
					InputMethodUtils.toggleSoftKeyboard(YiDianDishActivity.this);
					//开始调用接口
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
			
			
		case R.id.yidian_btn_tempsave:  //保存
			if(TsData.isReserve){
				Send("");
                break;
			}
			//实体类food中的字段send代表是已发送菜品，还是暂存   ，为其赋值             已发送1          暂存0
			for(int i = 0;i<guestDishes.size();i++){
				guestDishes.get(i).setSend("0");
			}
			
			//每次执行暂存时，先要删除该台位，账单号所对应的数据库中表中的数据
//			getDataManager(this).deleteDataIfSaved(menu.getTableNum(), menu.getMenuOrder());
			new ValueSetFile<Food>(menu.getTableNum()
					+ DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")
					+ menu.getMenuOrder() + ".mc").write(guestDishes);
//			if(insertDatabases(guestDishes))
				Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.yidian_btn_tbl:   //台位
//			if (guestDishes.size()>0)
				showIfSaveDialog().show();//弹出提示对话框，提示用户是否保存
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
	        lp.x = 0; // 新位置X坐标
	        lp.y = topLinearLayout.getHeight() -15; // 新位置Y坐标
	        dialogWindow.setAttributes(lp);
			tipdialog.show();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 弹出是否保存对话框 
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
						//将客人所点的菜品保存到数据库表AllCheck中

						//实体类food中的字段send代表是已发送菜品，还是暂存   ，为其赋值             已发送1          暂存0
						for (int i = 0; i < guestDishes.size(); i++) {
							guestDishes.get(i).setSend("0");
						}

						//在插入之前，将原来的台位号、账单号所对应的数据库表Allcheck中的信息删除

//						getDataManager(YiDianDishActivity.this).deleteDataIfSaved(menu.getTableNum(), menu.getMenuOrder());
//
//						boolean insertSuccess = insertDatabases(guestDishes);
//						if (insertSuccess) {
						ValueSetFile valueSetFile=new ValueSetFile<Food>(menu.getTableNum()
								+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")
								+menu.getMenuOrder()+".mc");
						valueSetFile.write(guestDishes);
						Toast.makeText(YiDianDishActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
						guestDishes.clear();//清空singleton集合
						Intent intent = new Intent(YiDianDishActivity.this, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);
						break;

					case 1:
						guestDishes.clear();//清空singleton集合
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
	 * 发送菜品
	 * @param immediateOrWaitFlag 发送方式
	 */
	private void Send(String immediateOrWaitFlag) {
		
		//就在这里写tpnum
		//如果还没有点菜，就提示 
		if(guestDishes.size() == 0){
			Toast.makeText(this, R.string.not_send_before_dian, Toast.LENGTH_SHORT).show();
			return;
		}
		
		Calendar c = Calendar.getInstance();
		String curTimeInMillis = String.valueOf(c.getTimeInMillis());//获取当前时间的毫秒数
		
		//给Pkid赋值
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
		
		//实体类food中的字段class,代表是即起还是叫起，为其赋值
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
			sb.append(food.getPKID()).append("@")//产品唯一编码
			.append(food.getPcode()).append("@")//产品编码
			.append(food.getPcname()).append("@")// 产品名称
			.append(ValueUtil.isEmpty(food.getTpcode())?"":food.getTpcode()).append("@")//套餐编码
			.append(ValueUtil.isEmpty(food.getTpname())?"":food.getTpname()).append("@")//套餐名称
			.append(food.getTpnum()).append("@")//套餐序号 （从0 开始， 每个套餐独立开始）
			.append(food.getPcount()).append("@");//数量    正常点菜1 套餐明细数量根据实际情况而定   退菜-1
			
			//赠送数量 0  或  1  或 第二单位赠送数量
			if(ValueUtil.isNotEmpty(food.getWeightflg()) && food.getWeightflg().equals("2")){  //表示第二单位
				sb.append(food.getWeight()).append("@");
			}else{ //表示第一单位
				sb.append(ValueUtil.isEmpty(food.getPromonum())?"0":food.getPromonum()).append("@");
			}
			
			sb.append(ValueUtil.isEmptyOrNull(food.getFujiacode()) ? "" : food.getFujiacode());
            sb.append(ValueUtil.isEmptyOrNull(food.getComfujiacode()) ? "" : food.getComfujiacode()).append("@");//附加项编码  多个附加项用  !  隔开
			sb.append(ValueUtil.isEmptyOrNull(food.getFujianame()) ? "" : food.getFujianame());
            sb.append(ValueUtil.isEmptyOrNull(food.getComfujianame()) ? "" : food.getComfujianame()).append("@");//附加项名称   多个附加项用  !  隔开
			sb.append(food.getPrice()).append("@");//价格
			sb.append(ValueUtil.isEmptyOrNull(food.getFujiaprice()) ? "" : food.getFujiaprice());
            sb.append(ValueUtil.isEmptyOrNull(food.getComfujiaprice()) ? "" : food.getComfujiaprice()).append("@");//附加项价格
			sb.append(ValueUtil.isEmpty(food.getWeight()) ? "0" : food.getWeight()).append("@");//第二单位重量    没有为0
			sb.append(food.getWeightflg().equals("1") ? "1" : "2").append("@");//第二单位标志  1 第一单位  2 第二单位
			sb.append(food.getUnit()).append("@");//单位
			sb.append(food.isIstc() ? "1" : "0").append("@");//是否套餐  0  不是  1 是      很特殊
			sb.append(ValueUtil.isEmptyOrNull(food.getPresentCode()) ? "" : food.getPresentCode()).append("@");//赠送原因
			sb.append(ValueUtil.isEmptyOrNull(food.getFujiacount()) ? "" : food.getFujiacount());
			sb.append(ValueUtil.isEmptyOrNull(food.getComfujiacount()) ? "" : food.getComfujiacount()).append("@");
			sb.append(ValueUtil.isEmpty(food.getUnitCode()) ? "" : food.getUnitCode()).append("@");//多单位
			sb.append(food.getIsTemp()).append("@");//临时菜
			sb.append(ValueUtil.isEmpty(food.getTempName()) ? "" : food.getTempName());
			sb.append(";");
			
		}
		sb.deleteCharAt(sb.length()-1);
		Log.e("菜品列表", sb.toString());
		params.add("productList", sb.toString());
		
		params.add("immediateOrWait", immediateOrWaitFlag);//即起 1                                   叫起2
		
		server.connect(YiDianDishActivity.this, Constants.FastMethodName.Senddish_METHODNAME, Constants.FastWebService.Senddish_WSDL, params, new OnServerResponse() {
			
			@Override
			
			public void onResponse(String result) {
				sendLoadingDialog.dismiss();
				if(ValueUtil.isNotEmpty(result)){
					Log.e("发送菜品后", result);
					String [] str = result.split("@");
					String flag = str[0];
					if(flag.equals("0")){
						Toast.makeText(YiDianDishActivity.this, str[1], Toast.LENGTH_SHORT).show();
						new ValueSetFile(menu.getTableNum()
								+DateFormat.getStringByDate(new Date(),"yyyy-MM-dd")
								+menu.getMenuOrder()+".mc").del();
						guestDishes.clear();//清空单例集合
						if(TsData.isReserve){
                            return;
                        }
						Intent intent = new Intent(YiDianDishActivity.this, YiXuanDishActivity2.class);
						Bundle bundle = new Bundle();
						bundle.putString("orderId", menu.getMenuOrder());//账单号
						bundle.putString("manCs", menu.getManCounts());//男人数量
						bundle.putString("womanCs", menu.getWomanCounts());//女人数量
						bundle.putString("tableNum", menu.getTableNum());//台位号
						intent.putExtra("topBundle", bundle);
						startActivity(intent);
					}else if(!flag.equals("0")){
						Toast.makeText(YiDianDishActivity.this, str[1], Toast.LENGTH_SHORT).show();//没有该账单
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
	 * 判断插入数据库是否成功
	 * @param listFoods
	 * @return
	 */
	protected boolean insertDatabases(List<Food> listFoods) {
		
		return getDataManager(this).circleInsertAllCheck(listFoods);
	}

	/**
	 * 普通单品用的
	 * @param timeInMillis
	 * @param menuId
	 * @param tabNum
	 * @param food
	 * @return
	 */
	private String getPKID(String timeInMillis,String menuId,String tabNum,Food food){//普通单品用的
		StringBuilder sb = new StringBuilder();
		sb.append("PKID").append(timeInMillis).append(menuId).append(tabNum).append(food.getPcode()).append(food.getTpnum());
		return sb.toString();
	}

	/**
	 * 套餐用的
	 * @param timeInMillis
	 * @param menuId
	 * @param tabNum
	 * @param food
	 * @return
	 */
	private String getTcPKID(String timeInMillis,String menuId,String tabNum,Food food){//套餐用的
		StringBuilder sb = new StringBuilder();
		sb.append("PKID").append(timeInMillis).append(menuId).append(tabNum).append(food.getTpcode()).append(food.getTpnum());
		return sb.toString();
	}

	
	
	/**
	 * ExpandableListView的适配器
	 * @author dell
	 *
	 */
	public class YidianDishAdapter extends BaseExpandableListAdapter{
		
		private List<Food> groupAdapterLists = new ArrayList<Food>();//父目录对应的集合
		private List<List<Food>> childAdapterLists = new ArrayList<List<Food>>();//子目录对应的集合
		LayoutInflater  layoutInflater;
		
		private List<Food> integratedLists;//总的集合
		

		public YidianDishAdapter(Context pContext,List<Food> pLists) {
			super();
			
			this.integratedLists = pLists;
			layoutInflater = LayoutInflater.from(pContext);
			
			inithomeList(integratedLists);
		}
		
		/**
		 * 从单例集合中筛选，套餐与普通单品一组，存放在父集合
		 * 套餐明细一组,存放进子集合
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
		 * 重新绑定数据源
		 * @param changedLists
		 */
		public void setDataSource(List<Food> changedLists){
			this.integratedLists = changedLists;
			
			//重新筛选数据
			inithomeList(integratedLists);
		}
		
		
		/**
		 *  //得到共点菜品的数量
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
		 * 得到共点菜品的总价                     
		 * @return 
		 * 备注：必须是小计相加得到的
		 */
		public double getTotalSalary(){             
			double totalSalary = 0.00;
			for(Food mFood:groupAdapterLists){
				totalSalary += getItemSubtotal(mFood);
			}
			return totalSalary;
		}
		
		
		/**
		 *得到共点菜品附加项的总价 
		 * @return double
		 */
		public double getFujiaSalary(){               
			BigDecimal totalFujiaSalary=new BigDecimal(0);
			for(Food f:guestDishes){
				if(ValueUtil.isNotEmpty(f.getFujiaprice())){//表明该菜品有附加项,自定义附加项价格为0，不考虑
					String [] fujiaStr = f.getFujiaprice().split("!");
                    String [] fujiaCnt=f.getFujiacount().split("!");
					for(int i = 0;i<fujiaStr.length;i++){
						//如果是自定义附加项，价格为“”，就报错,必须判断一下
						totalFujiaSalary=totalFujiaSalary.add(ValueUtil.isNaNofBigDecimal(fujiaStr[i]).multiply(ValueUtil.isNaNofBigDecimal(fujiaCnt[i])));
					}
				}
                if(ValueUtil.isNotEmpty(f.getComfujianame())){
                    String [] fujiaPri=f.getComfujiaprice().split("!");
                    String [] fujiaCnt=f.getComfujiacount().split("!");
                    for(int i = 0;i<fujiaPri.length;i++){
                        //如果是自定义附加项，价格为“”，就报错,必须判断一下
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
			TextView nameChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesname);//菜品名称
			TextView countsChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishescounts);//数量
			TextView priceChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesprice);//价格
			TextView unitChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesunit);//单位
			ImageView additionChildImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_child_addition);//附加项
			if (!ChioceActivity.ispad) {
				int buttonwidth  = screemwidth/11;
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonwidth, buttonwidth);
				lp.setMargins(4, 3, 4, 3);
				additionChildImage.setLayoutParams(lp);
				LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(screemwidth/8,screemwidth/15);
				lp2.setMargins(3, 3, 3, 5);
				countsChildText.setLayoutParams(lp2);
			}
			//第二行的套餐明细的附加项
			LinearLayout singleAddChildLayout = (LinearLayout) convertView.findViewById(R.id.yidian_expand_item_child_linear);
			TextView singleAddChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_show);
			
			//如果有附加项就显示，没有就隐藏第二行
			if(ValueUtil.isNotEmpty(food.getFujianame())||ValueUtil.isNotEmpty(food.getComfujianame())){
                String fujia=ValueUtil.isEmpty(food.getFujianame())?"":food.getFujianame().replace("!", ",");
                fujia+=ValueUtil.isEmpty(food.getComfujianame())?"":food.getComfujianame().replace("!", ",");
				singleAddChildLayout.setVisibility(View.VISIBLE);
				singleAddChildText.setText(fujia);
			}else{
				singleAddChildLayout.setVisibility(View.GONE);
			}

			nameChildText.setText(food.getPcname());
			countsChildText.setText(food.getPcount());//这里显示套餐明细每道菜品的数量
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
			
			TextView nameText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishesname);//菜品名称
			
			ImageView plusImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_dishesplus);//+ 按钮
			TextView countsText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishescounts);//菜品数量
			ImageView minusImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_dishesminus);//- 按钮
			
			TextView priceText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishesprice);//价格
			
			TextView unitText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishesunit);//单位
			
			TextView subtotalText = (TextView) convertView.findViewById(R.id.yidian_expand_item_group_dishessubtotal);//小计
			
			ImageView presentImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_present);//赠送按钮
			ImageView additionImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_addition);//附加项按钮
			ImageView deleteImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_group_delete);//删除按钮
			
			// TODO 重定义手机版按钮大小边距
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
			
			//套餐不能添加附加项，套餐明细能添加,将按钮设为不可点
			if(f.isIstc() && f.getPcode().equals(f.getTpcode())){
				additionImage.setVisibility(View.INVISIBLE);
			}
			
			//如果有附加项就显示，没有就隐藏第二行
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


            //=======================判断是否多单位======================
            if(ValueUtil.isNotEmpty(f.getUnitMap())){
                unitText.setOnClickListener(new DishUnitClick(f));
            }
            //============================================
			//如果是套餐，数量 + 按钮，数量 — 按钮，不能显示
			if(f.isIstc()){
				plusImage.setVisibility(View.INVISIBLE);
				minusImage.setVisibility(View.INVISIBLE);	
			}else{
                countsText.setOnClickListener(new DishCountsClick(f));
            }
			
			//如果是第二单位菜品，数量 + 按钮，数量 — 按钮，不能显示
			if(!f.isIstc() && ValueUtil.isNotEmpty(f.getWeightflg()) && f.getWeightflg().equals("2")){
				plusImage.setVisibility(View.INVISIBLE);
				minusImage.setVisibility(View.INVISIBLE);	
			}
			
			
			//如果涉及到赠菜，品名就复杂 
			if(ValueUtil.isNotEmpty(f.getPromonum())){  //代表此道菜有赠菜
				
				//分两种情况  套餐    普通单品
				if(f.isIstc()){
					StringBuilder sb = new StringBuilder();
					sb.append(f.getPcname())
					.append("-"+YiDianDishActivity.this.getString(R.string.give))
					.append(f.getPromonum());//因为套餐只有一份，套餐都是单份显示的，这里直接填1也可以
					
					nameText.setText(sb.toString());
				}else if(!f.isIstc()){
					StringBuilder sb = new StringBuilder();
					sb.append(f.getPcname())
					.append("-"+YiDianDishActivity.this.getString(R.string.give))
					.append(f.getPromonum());
					
					nameText.setText(sb.toString());
				}
			}else{//代表没有赠菜
				String [] realPcname = f.getPcname().split("-");
				nameText.setText(realPcname[0]);
			}
			
			
			countsText.setText(f.getPcount());//数量
			priceText.setText(f.getPrice());//价格
			unitText.setText(f.getUnit());//单位
			
			//给小计赋值, 只有套餐与普通单品才有小计，并赋值,套餐明细没有小计
			if(f.isIstc() && f.getPcode().equals(f.getTpcode()) || !f.isIstc()){
				double itemSubtotal = getItemSubtotal(f);
				subtotalText.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
			}
			
			//为按钮设置监听
			additionImage.setOnClickListener(new AdditionChildClick(f));
			plusImage.setOnClickListener(new PlusImageClick(f));
			minusImage.setOnClickListener(new MinusImageClick(f));
			
			deleteImage.setTag(groupPosition);//设置tag,不然就产生错位
			deleteImage.setOnClickListener(new DeleteClick(f));
			
			presentImage.setOnClickListener(new PresentImageClick(f));
			return convertView;
		}


		/**
		 * 单品附加项的点击事件,非全单附加项
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
		
		
		//绑定赠菜按钮
		class PresentImageClick implements OnClickListener{
			private Food f;

			public PresentImageClick(Food f) {
				super();
				this.f = f;
			}


			@Override
			public void onClick(View v) {
				if(f.isIstc() && ValueUtil.isNotEmpty(f.getPromonum())){  //说明该套餐正处于已赠送状态，点击时，就弹出取消对话框
					AlertDialog.Builder builder = new AlertDialog.Builder(YiDianDishActivity.this,R.style.edittext_dialog);
					builder.setTitle(R.string.hint);
					builder.setMessage(R.string.whether_cancel_the_gift);
					builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							cancelFoodPromnum(f, guestDishes, "");//因为此处是取消套餐，套餐只有一份，第三个参数直接填""
							updateDisplay();//更新界面
						}
					});
					builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					builder.create().show();
				}else if(f.isIstc() && ValueUtil.isEmpty(f.getPromonum())){  //说明该套餐还没有赠送
					showDiaAuthOrCount(f);
				}else if(!f.isIstc()){   //对非套餐进行处理
					showDiaAuthOrCount(f);
				}
			}
			
		}


		/**
		 * 绑定数量  - 按钮的点击类,这种加减的情况只适用于普通单品，套餐不能加减
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
				
				updateDisplay();//更新界面View
				YiDianDishActivity.hasChanged = true;
			}
			
		}
		
		//绑定数量  + 按钮的点击类,这种加的情况只适用于普通单品，套餐不能加
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
				
				updateDisplay();//更新界面View
				YiDianDishActivity.hasChanged = true;
			}
			
		}


		/**
		 * 绑定删除按钮的点击类
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
						updateDisplay();//更新界面
										
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
		 * //在此处处理每道菜品的小计
		 * @param pFood
		 * @return 返回的是double类型，在给小计setText时，再ValueUtil.setScale(val, newScale, roundingMode)
		 */
		public double getItemSubtotal(Food pFood){
			//在此处处理小计,如果涉及到赠菜，就很复杂
				if(!pFood.isIstc()){ //非套餐,普通的单品
					double itemSubtotal = 0.00;
					
					String presentCount = pFood.getPromonum();//得到关于这道菜品的赠送数量
					if(ValueUtil.isNotEmpty(presentCount)){ //说明有赠菜
						int countPresent = Integer.parseInt(presentCount);
						int countPcount = Integer.parseInt(pFood.getPcount());
						int countPcountLeft = countPcount - countPresent;//点菜的总数量，减去赠送菜品的数量，得到剩下的数量
						double counts = Double.valueOf(countPcountLeft +"");
						double price = Double.valueOf(pFood.getPrice());
						itemSubtotal = counts * price;
					}else{   //说明没有赠送
						double counts = Double.valueOf(pFood.getPcount());
						double price = Double.valueOf(pFood.getPrice());
						itemSubtotal = counts * price;
					}
					
					return itemSubtotal;
				}else{   //是套餐,因为套餐都是单份显示的
					double itemTcSubtotal = 0.00;
					String presentCount = pFood.getPromonum();
					if(ValueUtil.isEmpty(presentCount)){//代表没有赠菜
						return Double.valueOf(pFood.getPrice());
					}else{//代表有赠菜,代表该套餐已被赠送,直接返回0.00就行
						return itemTcSubtotal;
					}
					
				}
				
		}
		
	}
	
	
	
	
	/**
	 * 根据所点的不同情况，显示不同的弹出对话框
	 * @param pFood
	 */
	protected void showDiaAuthOrCount(Food pFood) {
		
		//在设置里面有赠送菜品的金额
		int moneyAuth = Integer.parseInt(SharedPreferencesUtils.getGiftAuth(YiDianDishActivity.this));
		
		//先判断所点菜品的数量
		int dishCount = Integer.parseInt(pFood.getPcount());
		double moneyPresent = 0.00;
		
		//赠送菜品价格高于49，就要授权,比较复杂，得分两种情况
		if(pFood.isIstc()){  //套餐的数量只有一份
			moneyPresent = Double.valueOf(pFood.getPrice());
		}else{ //普通单品
			moneyPresent = Double.valueOf(pFood.getPrice());
		}
		
		if(dishCount > 1 && moneyPresent > moneyAuth){   //弹出授权与数量窗口
			createPresentAuthAndCountDia(pFood);
		}else if(dishCount == 1 && moneyPresent > moneyAuth){//弹出授权窗口 
			createPresentAuthDia(pFood);
		}else if(moneyPresent < moneyAuth){   //弹出数量窗口
			createPresentCountDia(pFood);
		}

	}
	
	
	/**
	 * //点击赠菜按钮完后，为food表中的Promonum，PresentCode赋值
	 * @param pFood  那道菜品
	 * @param plist 
	 * @param presentReason
	 * @param promCounts
	 */
	private void setFoodPromnumAndReason(Food pFood,List<Food> plist,String presentReason,String promCounts){
		if(pFood.isIstc()){
			for(Food food:plist){//为套餐和套餐明细同时赋上赠送数量，原因
				if(food.isIstc() && food.getTpcode().equals(pFood.getPcode()) && pFood.getTpnum().equals(food.getTpnum())){
					food.setPromonum("1");//因为套餐只有一份，直接填 1 也可以,填pFood.getPcount()也行
					food.setPresentCode(presentReason);
				}
			}
		}else{
			//如果先前已经赠送的数量不为空，就相加
			if(ValueUtil.isNotEmpty(pFood.getPromonum())){
				int countGiftBefore = Integer.parseInt(pFood.getPromonum());//得到先前已经赠送的数量
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
	 * //创建含有授权和数量的对话框
	 * @param dianDishes
	 */
	private void createPresentAuthAndCountDia(final Food dianDishes) {
		PresentAuthAndCountDialog authAndcountDia = PresentAuthAndCountDialog.newInstance(this.getString(R.string.give_food_authorization), (ArrayList<PresentReason>)presentreasonLists, new DialogPresentAuthAndCountClickListener() {
			
			@Override
			public void doPositiveClick(String authorNum, String authorPwd,final String reason, final String countgift, final String countCancel) {
				//先判断授权,在判断输入的数量是否正确
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
								//授权成功
								Log.e("授权返回值", result);
								if(ValueUtil.isNotEmpty(countgift) && ValueUtil.isNotEmpty(countCancel)){
									Toast.makeText(YiDianDishActivity.this,R.string.input_count_error,Toast.LENGTH_SHORT).show();
								}else if(ValueUtil.isNotEmpty(countgift) && ValueUtil.isEmpty(countCancel)){//表示赠菜
									String promonumgiftBefore = dianDishes.getPromonum();//得到先前已经赠送的数量
									
									if(ValueUtil.isNotEmpty(promonumgiftBefore) && Integer.parseInt(promonumgiftBefore) + Integer.parseInt(countgift) > Integer.parseInt(dianDishes.getPcount()) || Integer.parseInt(dianDishes.getPcount()) < Integer.parseInt(countgift) || Integer.parseInt(countgift) < 1){
										Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
										return;
									}
									setFoodPromnumAndReason(dianDishes, guestDishes, reason, countgift);//这里将全集合guestDishes传了进去
									
									//如果当前处于搜索模式下
									if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
										etSearchView.setText("");
										listView.setVisibility(View.GONE);
										expandListView.setVisibility(View.VISIBLE);
									}
									
									updateDisplay();
								}else if(ValueUtil.isEmpty(countgift) && ValueUtil.isNotEmpty(countCancel)){//表示取消赠菜
									if(ValueUtil.isEmpty(dianDishes.getPromonum()) || Integer.parseInt(dianDishes.getPromonum()) < Integer.parseInt(countCancel)|| Integer.parseInt(countCancel) < 1){
										Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
										return;
									}
									cancelFoodPromnum(dianDishes, guestDishes, countCancel);
									
									//如果当前处于搜索模式下
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
	 * //取消赠送
	 * @param pFood 你要操作的那道菜的对象
	 * @param plist 集合
	 * @param cancelCounts   取消赠菜的数量
	 */
	private void cancelFoodPromnum(Food pFood,List<Food> plist,String cancelCounts){
		if(pFood.isIstc()){//针对于套餐
			for(Food food:plist){
				if(food.isIstc() && food.getTpcode().equals(pFood.getPcode()) && pFood.getTpnum().equals(food.getTpnum())){
					food.setPromonum("");//此处比较特殊,因为套餐是一份，取消,在这里直接赋值为""
				}
			}
		}else{//针对于非套餐
			int presentBefore = Integer.parseInt(pFood.getPromonum());//得到原有的赠送的数量 
			int cancelCountsInt = Integer.parseInt(cancelCounts);//得到要取消赠送的数量
			int countPromLeft = presentBefore - cancelCountsInt;
			
			if(countPromLeft == 0){//如果正好全部取消
				pFood.setPromonum("");
			}else{
				pFood.setPromonum(countPromLeft+"");
			}

		}
	}
	
	/**
	 * //创建只显示数量的对话框
	 * @param dianDishes
	 */
	protected void createPresentCountDia(final Food dianDishes) {
		
		PresentCountDialog countDia = PresentCountDialog.newInstance(this.getString(R.string.give_food_authorization), (ArrayList<PresentReason>)presentreasonLists, new DialogPresentCountClickListener() {
			
			@Override
			public void doPositiveClick(String giftCounts, String cancelGiftCounts,String reason) {
				if(ValueUtil.isNotEmpty(giftCounts) && ValueUtil.isNotEmpty(cancelGiftCounts)){
					Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
				}else if(ValueUtil.isNotEmpty(giftCounts) && ValueUtil.isEmpty(cancelGiftCounts)){//表示赠菜
					String promonumgiftBefore = dianDishes.getPromonum();//得到先前已经赠送的数量
					
					if(ValueUtil.isNotEmpty(promonumgiftBefore)&&Integer.parseInt(promonumgiftBefore) + Integer.parseInt(giftCounts) > Integer.parseInt(dianDishes.getPcount()) || Integer.parseInt(dianDishes.getPcount()) < Integer.parseInt(giftCounts) || Integer.parseInt(giftCounts) < 1){
						Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
						return;
					}
					
					setFoodPromnumAndReason(dianDishes, guestDishes, reason,giftCounts);
					
					//如果当前处于搜索模式下
					if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
						etSearchView.setText("");
						listView.setVisibility(View.GONE);
						expandListView.setVisibility(View.VISIBLE);
					}
					updateDisplay();//更新页面
				}else if(ValueUtil.isEmpty(giftCounts) && ValueUtil.isNotEmpty(cancelGiftCounts) ){//表示取消赠菜
					if(ValueUtil.isEmpty(dianDishes.getPromonum()) || Integer.parseInt(dianDishes.getPromonum()) < Integer.parseInt(cancelGiftCounts) || Integer.parseInt(cancelGiftCounts) < 1){
						Toast.makeText(YiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
						return;
					}
					cancelFoodPromnum(dianDishes, guestDishes, cancelGiftCounts);
					
					//如果当前处于搜索模式下
					if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
						etSearchView.setText("");
						listView.setVisibility(View.GONE);
						expandListView.setVisibility(View.VISIBLE);
					}
					updateDisplay();//更新页面
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
	 * //创建授权的赠菜对话框
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
								//授权成功
								Log.e("授权返回值", result);
								
								setFoodPromnumAndReason(pFood, guestDishes, reason,"1");//因为只有数量等于1时，并且单道菜品价格大于49，才弹出此对话框，直接填 1
								
								//如果当前处于搜索模式下
								if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
									etSearchView.setText("");
									listView.setVisibility(View.GONE);
									expandListView.setVisibility(View.VISIBLE);
								}
								updateDisplay();//更新页面
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
	 * 绑定搜索框的
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
				
				//当前正处于搜索模式，将expandListView隐藏，listView显示
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
			}else{//当搜索框为空时，就恢复到正常模式
				listView.setVisibility(View.GONE);
				expandListView.setVisibility(View.VISIBLE);
			}
		}
	};


	/**
	 * 显示附加项的对话框
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
	 * 搜索listview对应的适配器
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
			
			//表明这是纯套餐.
			if(food.isIstc() && food.getPcode().equals(food.getTpcode())){
				holder.plusImage.setVisibility(View.INVISIBLE);
				holder.minusImage.setVisibility(View.INVISIBLE);
			}else{
                holder.dishCounts.setOnClickListener(new DishCountsClick(food));
            }
			//=======================判断是否多单位======================
            if(ValueUtil.isNotEmpty(food.getUnitMap())){
                holder.dishUnit.setOnClickListener(new DishUnitClick(food));
            }
            //============================================
			//表明这是套餐明细
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
		 * 针对于搜索模式下的删除按钮
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
		 * 针对于搜索模式下的添加附加项按钮
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
		 * 针对于搜索模式下的赠送按钮
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
				if(f.isIstc() && ValueUtil.isNotEmpty(f.getPromonum())){  //说明该套餐正处于已赠送状态，点击时，就弹出取消对话框
					AlertDialog.Builder builder = new AlertDialog.Builder(YiDianDishActivity.this);
					builder.setTitle(R.string.hint);
					builder.setMessage(R.string.whether_cancel_the_gift);
					builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							cancelFoodPromnum(f, guestDishes, "");//因为此处是取消套餐，套餐只有一份，第三个参数直接填""
							
							
							//如果当前处于搜索模式下
							if(listView.getVisibility()== View.VISIBLE && expandListView.getVisibility()== View.GONE){
								etSearchView.setText("");
								listView.setVisibility(View.GONE);
								expandListView.setVisibility(View.VISIBLE);
							}
							
							updateDisplay();//更新界面
						}
					});
					builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					builder.create().show();
				}else if(f.isIstc() && ValueUtil.isEmpty(f.getPromonum())){  //说明该套餐还没有赠送
					showDiaAuthOrCount(f);
				}else if(!f.isIstc()){   //对非套餐进行处理
					showDiaAuthOrCount(f);
				}
			}
			
		}

		/**
		 * 针对于搜索模式下的数量 - 按钮
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
				updateDisplay();//更新界面View
				YiDianDishActivity.hasChanged = true;
			}
			
		}
		/**
		 * 针对于搜索模式下的数量  + 按钮
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
						updateDisplay();//更新界面View
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
	 * 非全单附加项的监听器 
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
	 * 全单附加项搜索用的
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
     * 数量修改
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
                            updateDisplay();//更新界面View
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
                    updateDisplay();//更新界面View
                }
            },YiDianDishActivity.this,v).showView(food);
        }
    }
}
