package cn.com.choicesoft.singletaiwei;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.*;
import cn.com.choicesoft.adapter.YiXuanDishesAdapter;
import cn.com.choicesoft.adapter.YiXuanDishesAdapter.YiXuanViewHolder;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.bean.FoodBack;
import cn.com.choicesoft.bean.FoodBackJack;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.util.SlideListView.SlideDirection;
import cn.com.choicesoft.util.SlideListView.SlideListener;
import cn.com.choicesoft.view.ClearEditText;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;
import cn.com.choicesoft.view.VipMsg;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已点菜单
 * 
 */
public class YiXuanDishActivity2 extends BaseActivity implements OnClickListener,SlideListener{
	
	private TextView dishCounts,moneyCounts,orderNumber;//共点菜品  3.0道  总计 51.80  账单号：H000364
	
	private TextView tableNum,orderNum,manNum,womanNum,userNum;//头部红色标题栏内容
		
	private LoadingDialog mLoadingDialog;
	
	private Button table;
	private Button promptDish;
	private Button clearDish;
	private Button addDish;
	private Button prePrint;
	private Button preBalance;
	private Button foodBack;
	private Button back;
	
	private Button btnSearch;
	private cn.com.choicesoft.view.ClearEditText etSearch;
	
	private SlideListView listView;
	private YiXuanDishesAdapter adapter;
	
	
	private String tableNumber;
	private String menuOrder;
	private String womanCounts;
	private String manCounts;
	
	private Boolean isStop=false;
	
	private List<Food> foodArray;
	
	private LinearLayout topDigitalLayout;//数字标题栏这一行需隐藏
	
	private CheckBox selectAllCb;//全选按钮
	private List<Food> matchLists = new ArrayList<Food>();

	private LinearLayout topLinearLayout;

	private ImageView menuinfo_imageView;

	private View layout;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (ChioceActivity.ispad) {
			setContentView(R.layout.yixuandish_pad);
		} else {
			setContentView(R.layout.yixuandish);
		}
		
		initViews();
		initListener();
		initData();
	}
	
	private Bundle getTblStateBundle(){
		return this.getIntent().getBundleExtra("topBundle");
	}

	private void initData() {
		mLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
		mLoadingDialog.setCancelable(true);
		
		Bundle bundle = getTblStateBundle();
		userNum.setText(SharedPreferencesUtils.getOperator(this));//设置操作员
		if(bundle != null){
			tableNumber = bundle.getString("tableNum");//台位号
			menuOrder = bundle.getString("orderId");//账单号
			womanCounts = bundle.getString("womanCs");//女人数量
			manCounts = bundle.getString("manCs");//男人数量
			
			//为头部红色标题栏赋值
			tableNum.setText(tableNumber);
			orderNum.setText(menuOrder);
			manNum.setText(ValueUtil.isEmpty(manCounts)?"0" :manCounts);
			womanNum.setText(ValueUtil.isEmpty(womanCounts)?"0" :womanCounts);
			//初始化会员信息
			VipMsg.iniVip(this, bundle.getString("tableNum"),R.id.vipMsg_ImageView);	
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateDisplay();
	}

	/**
	 * 刷新界面
	 */
	private void updateDisplay() {
		if(SharedPreferencesUtils.getIsNet(this)){
			foodArray = getDataManager(this).getAllFoodListByTablenum(tableNumber,menuOrder);//根据tableNumber,orderID,send = 1 从AllCheck表中查询
			if(ValueUtil.isEmpty(foodArray)){//如果数据库没有数据则默认使用接口
				refresh();
			}else{
				if(adapter == null){
					adapter = new YiXuanDishesAdapter(this,foodArray);
					listView.setAdapter(adapter);
				}else{
					adapter.setData(foodArray);
					adapter.notifyDataSetChanged();
				}
				
				dishCounts.setText(adapter.getDishSelectedCount()+this.getString(R.string.course));
				//得到总的价钱
				double totalSalary = adapter.getTotalSalary();
				moneyCounts.setText(ValueUtil.setScale(totalSalary, 2, 5));
				
				//得到总附加项的价钱
				double totalFujiaSalary = adapter.getFujiaSalary();
				orderNumber.setText(ValueUtil.setScale(totalFujiaSalary, 2, 5));
				isStop=false;
			}
		}else{
			refresh();
		}
	}

	private void initListener() {
		table.setOnClickListener(this);
		promptDish.setOnClickListener(this);
		clearDish.setOnClickListener(this);
		addDish.setOnClickListener(this);
		prePrint.setOnClickListener(this);
		preBalance.setOnClickListener(this);
//		foodBack.setOnClickListener(this);
		back.setOnClickListener(this);
		
		btnSearch.setOnClickListener(this);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				YiXuanViewHolder holder = (YiXuanViewHolder) view.getTag();
				holder.checkbox.toggle();
				YiXuanDishesAdapter adapter = (YiXuanDishesAdapter) parent.getAdapter();
				adapter.toggleChecked(position);
			}
		});
		listView.setSlideListener(this);
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View v, int position, long id) {

				final Food food = (Food) parent.getAdapter().getItem(position);
//				showIfSaveDialog(food).show();]
				tuicai(food);
				return false;
//				if (Integer.parseInt(food.getRushorcall())==0){
//					showIfSaveDialog(food).show();
//					return false;
//				}else {
//					View view = YiXuanDishActivity2.this.getLayoutInflater().inflate(R.layout.cancle_products_layout, null);
//					final Spinner cause = (Spinner) view.findViewById(R.id.cancle_products_cause);//退菜原因
//					final EditText count = (EditText) view.findViewById(R.id.cancle_products_count);//退菜数量
//					final EditText usercode = (EditText) view.findViewById(R.id.cancle_products_usercode);//用户编码
//					final EditText userpwd = (EditText) view.findViewById(R.id.cancle_products_userpwd);//用户名称
//					count.setText(food.getPcount());
//					List<Map<String, String>> list = new ListProcessor().query("select id,des,init from errorcustom", null, YiXuanDishActivity2.this, new ListProcessor.Result<List<Map<String, String>>>() {
//						@Override
//						public List<Map<String, String>> handle(Cursor c) {
//							List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//							while (c.moveToNext()) {
//								Map<String, String> map = new HashMap<String, String>();
//								map.put("id", c.getString(0));
//								map.put("des", c.getString(1));
//								map.put("init", c.getString(2));
//								list.add(map);
//							}
//							return list;
//						}
//					});
//					SpinnerAdapter spinnerAdapter = new SimpleAdapter(YiXuanDishActivity2.this, list, R.layout.dia_ifsave_item_layout,
//							new String[]{"id", "des"}, new int[]{R.id.dia_ifsave_item_hide, R.id.dia_ifsave_item_tv});
//					cause.setAdapter(spinnerAdapter);
//					new AlertDialog.Builder(YiXuanDishActivity2.this, R.style.edittext_dialog).setTitle(R.string.foodback).setView(view).setNegativeButton(R.string.cancle, null).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(DialogInterface dialog, int which) {
//											Field field = null;
//											//通过反射获取dialog中的私有属性mShowing
//											try {
//												field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//												field.setAccessible(false);//设置该属性可以访问
//											} catch (NoSuchFieldException e) {
//												e.printStackTrace();
//											}
//											String c = count.getText().toString();
//											if (ValueUtil.isNotEmpty(c)) {
//												if (ValueUtil.isNaNofInteger(c) > 0) {
//													if (field != null) {
//														field.setAccessible(true);//设置该属性可以访问
//													}
//													View v = cause.getSelectedView();
//													food.setPcount(c);
//													String id = ((TextView) (v.findViewById(R.id.dia_ifsave_item_hide))).getText().toString();
//													checkAuth(usercode.getText().toString(), userpwd.getText().toString(),
//															new ArrayList<Food>() {{
//																add(food);
//															}}, id);
//												} else {
//													ToastUtil.toast(YiXuanDishActivity2.this, R.string.food_back_count_error);
//												}
//											} else {
//												ToastUtil.toast(YiXuanDishActivity2.this, R.string.food_back_count_error1);
//											}
//										}
//									}
//
//							).show();
//
//					return false;
//				}
			}

		});
		
		//全选按钮
		selectAllCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					adapter.setAllItemCheckedAndNotify(true);//
				} else {
					adapter.setAllItemCheckedAndNotify(false);
				}
			}
		});
	}
	//退菜
	private void tuicai(final Food food){
		final LinearLayout layout=(LinearLayout)this.getLayoutInflater().inflate(R.layout.verify_pwd, null);
		Button confirm=(Button)layout.findViewById(R.id.verify_pwd_confirm);
		Button cancel=(Button)layout.findViewById(R.id.verify_pwd_cancel);
		final cn.com.choicesoft.view.ClearEditText pwdEdit=(ClearEditText)layout.findViewById(R.id.verify_pwd_Edit);
		TextView textView=(TextView)layout.findViewById(R.id.verify_pwd_Text);
		textView.setText("请输入退菜数量");
		pwdEdit.setText(food.getPcount());
		pwdEdit.setInputType(InputType.TYPE_CLASS_PHONE);
		final AlertDialog alert=new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle("退菜").setView(layout).show();
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				String pwd=pwdEdit.getText().toString();
				if (pwd.length()==0) {
					Toast.makeText(YiXuanDishActivity2.this, R.string.food_back_count_error, Toast.LENGTH_LONG).show();
				}else if (Double.parseDouble(pwd)>Double.parseDouble(food.getPcount())){
					Toast.makeText(YiXuanDishActivity2.this, R.string.food_back_error, Toast.LENGTH_LONG).show();
				}else {
					food.setPcount(pwd);
					cancleProducts(new ArrayList<Food>() {{
						add(food);
					}}, null, null);
					alert.dismiss();
				}

			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				alert.dismiss();
			}
		});
	}
	/**
	 * 弹出是否保存对话框
	 * @return
	 */
	private Dialog showIfSaveDialog(final Food food) {

		String [] itemSave = {this.getString(R.string.yes),this.getString(R.string.no)};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("是否退菜？");
		ListAdapter diaAdapter=new ArrayAdapter<String>(this, R.layout.dia_ifsave_item_layout, R.id.dia_ifsave_item_tv, itemSave);
		builder.setAdapter(diaAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:

						cancleProducts(new ArrayList<Food>() {{
							add(food);
						}}, null, null);
						break;

					case 1:
						break;

					case 2:

					default:
						break;
				}
			}


		});

		return builder.create();
	}
	private void initViews() {
		//顶部红色栏
		if (ChioceActivity.ispad) {
			tableNum = (TextView) this.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) this.findViewById(R.id.dishesact_tv_ordernum);
			manNum = (TextView) this.findViewById(R.id.dishesact_tv_mannum);
			womanNum = (TextView) this.findViewById(R.id.dishesact_tv_womannum);
			userNum = (TextView) this.findViewById(R.id.dishesact_tv_usernum);

			back = (Button) this.findViewById(R.id.yixuan_btn_back);
		}else {
			// TODO 
			menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
			menuinfo_imageView.setClickable(true);
			menuinfo_imageView.setOnClickListener(this);
			layout = LayoutInflater.from(YiXuanDishActivity2.this).inflate(R.layout.menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			manNum = (TextView) layout.findViewById(R.id.dishesact_tv_mannum);
			womanNum = (TextView) layout.findViewById(R.id.dishesact_tv_womannum);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			topLinearLayout = (LinearLayout) findViewById(R.id.topLinearLyout);
			back = (Button) this.findViewById(R.id.eatable_btn_back);
		}
		
		
		topDigitalLayout = (LinearLayout) this.findViewById(R.id.toptitle_rg_number);
		topDigitalLayout.setVisibility(View.GONE);
		
		selectAllCb = (CheckBox) this.findViewById(R.id.yixuan_cb_quanxuan);
		
		dishCounts = (TextView) this.findViewById(R.id.yixuan_tv_dishcounts);
		moneyCounts = (TextView) this.findViewById(R.id.yixuan_tv_moneycounts);
		orderNumber = (TextView) this.findViewById(R.id.yixuan_tv_ordernumber);
		
		table = (Button) this.findViewById(R.id.yixuan_btn_table);
		promptDish = (Button) this.findViewById(R.id.yixuan_btn_promptDish);
		clearDish = (Button) this.findViewById(R.id.yixuan_btn_clearDish);
		addDish = (Button) this.findViewById(R.id.yixuan_btn_addDish);
//		foodBack = (Button) this.findViewById(R.id.yixuan_btn_foodback);
		prePrint = (Button) this.findViewById(R.id.yixuan_btn_prePrint);
		preBalance = (Button) this.findViewById(R.id.yixuan_btn_preBalance);
		listView = (SlideListView) this.findViewById(R.id.yixuan_listview_alldishes);
		btnSearch = (Button) this.findViewById(R.id.btn_searchView);
		etSearch = (ClearEditText) this.findViewById(R.id.et_searchView);
		etSearch.addTextChangedListener(watcher);
		for (Map<String,String>map: SingleMenu.getMenuInstance().getPermissionList()){
			if (map.get("CODE").equals("4001")){
				promptDish.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					promptDish.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("4002")){
				clearDish.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					clearDish.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("4004")){
				addDish.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					addDish.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("4005")){
				preBalance.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					preBalance.setVisibility(View.GONE);
				}
			}
			if (map.get("CODE").equals("4006")){
				prePrint.setTag(map.get("AUTHORITY"));
				if (map.get("ISSHOW").equals("0")) {
					prePrint.setVisibility(View.GONE);
				}
			}
		}
	}
	/**
	 * 滑动后回调函数
	 * @author M.c
	 */
	@Override
	public void slideItem(SlideDirection dir, int position) {
		if(TsData.isReserve){
			alertReserve();
			return;
		}
		List<Food> list=new ArrayList<Food>();
		StringBuilder sb=null;
		try {
			sb=new StringBuilder();
			Food food=adapter.getItem(position);
			BigDecimal count=ValueUtil.isNaNofBigDecimal(food.getPcount()).subtract(ValueUtil.isNaNofBigDecimal(food.getOver()));
			String countString=count.toString().matches("-?\\d+$")?count.toString():count.intValue()+"";
			if(count.doubleValue()>0){
				food.setOverCount(countString);
			}else if(count.doubleValue()==0){
				food.setOverCount(food.getPcount());
			}else{
				food.setOverCount(count.abs().toString());
			}
			list.add(food);
			setFood(list,sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(ValueUtil.isEmpty(sb)){
			Toast.makeText(this, R.string.hua_cai_error, Toast.LENGTH_LONG).show();
			return;
		}
		switch(dir){
			case RIGHT:
				callElide(sb.toString(),list,"callElide",0,null);
				break;
			case LEFT:
				callElide(sb.toString(),list,"reCallElide",1,null);
				break;
			default:  
	            break;  
		}
	}

	/**
	 * 退菜
	 */
	public void cancleProducts(List<Food> foods,String usercode,String backreason){
		CList list=new CList();
		list.add("deviceId",SharedPreferencesUtils.getDeviceId(this));
		list.add("userCode",SharedPreferencesUtils.getUserCode(this));
		list.add("json",comCancleValue(foods,usercode,backreason));
		new Server().connect(this, "cancleProducts", "ChoiceWebService/services/HHTSocket?/cancleProducts", list, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				mLoadingDialog.dismiss();
				if(ValueUtil.isNotEmpty(result)){
					try {
						JSONObject jsonObject=new JSONObject(result);
						String res= jsonObject.getString("return");
						if (res.equals("0")){
							updateDisplay();
							ToastUtil.toast(YiXuanDishActivity2.this, R.string.food_back_success);
						}else{
							ToastUtil.toast(YiXuanDishActivity2.this,jsonObject.getString("error"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					ToastUtil.toast(YiXuanDishActivity2.this,R.string.food_back_error);
				}
			}
			@Override
			public void onBeforeRequest() {
				mLoadingDialog.show();
			}
		});
	}

	/**
	 * 退菜json转换
	 * @param foods
	 * @param backreason
	 * @return
	 */
	private String comCancleValue(List<Food> foods,String usercode,String backreason){
		FoodBackJack foodBackJack=new FoodBackJack();
		List<FoodBack> foodBacks=new ArrayList<FoodBack>();
		foodBackJack.setOrderid(menuOrder);
		foodBackJack.setAccreditcode(usercode);
		foodBackJack.setIsAccurate("1");
		foodBackJack.setBackreason(backreason);
		for(Food food:foods){
			FoodBack foodBack=new FoodBack();
			foodBack.setPcode(food.getPcode());
			foodBack.setCanclecount(food.getPcount());
			foodBack.setPkid(food.getPKID());
			foodBack.setWeightflg(food.getWeightflg());
			foodBack.setIstc(food.isIstc()?"1":"0");
			foodBack.setUnitcode(food.getUnitCode());
			foodBack.setJiorjiao(food.getRushorcall());
			foodBack.setIstemp(food.getIsTemp()+"");
			foodBack.setFujiacode(food.getFujiacode());
			foodBacks.add(foodBack);
		}
		foodBackJack.setDishList(foodBacks);
		String json= new Gson().toJson(foodBackJack);
		Log.e("退菜JSON", json);
		return json;
	}

	public void checkAuth(final String usercode,String pwd, final List<Food> foods, final String backreason){
		CList list=new CList();
		list.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
		list.add("userCode", usercode);
		list.add("userPass", pwd);
		new Server().connect(this, "checkAuth", "ChoiceWebService/services/HHTSocket?/checkAuth", list, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                mLoadingDialog.dismiss();
                if (ValueUtil.isNotEmpty(result)) {
                    String[] res = result.split("@");
                    if (res[0].equals("0")) {
                        cancleProducts(foods, usercode, backreason);
                    } else {
                        ToastUtil.toast(YiXuanDishActivity2.this, R.string.shouquan_error);
                    }
                } else {
                    ToastUtil.toast(YiXuanDishActivity2.this, R.string.shouquan_error);
                }
            }

            @Override
            public void onBeforeRequest() {
                mLoadingDialog.show();
            }
        });
	}
	/**
	 * 拼接所需划的菜信息
	 * @author M.c
	 * @param sb
	 */
	public void setFood(List<Food> foods,StringBuilder sb){
		List<Food> foodList=foodSaiXuan(foods);
		for(Food food:foodList){
			sb.append(food.getPcode()).append("@")//产品编码
			.append(food.getTpcode()).append("@")//套餐编码
			.append(food.getTpnum()).append("@")//套餐序号 （从0 开始， 每个套餐独立开始）
			.append(ValueUtil.isEmpty(food.getFujiacode()) ? "" : food.getFujiacode()).append("@")//附加项编码  多个附加项用  !  隔开
			.append(ValueUtil.isNotEmpty(food.getWeightflg()) ? food.getWeightflg() : "1").append("@")//第二单位标志  1 第一单位  2 第二单位
			.append(food.isIstc() ? "1" : "0").append("@")//是否套餐  0  不是  1 是
			.append(food.getOverCount() == null ? food.getPcount() : food.getOverCount()).append("@")//划菜的数量
			.append(food.getPKID()).append("@")
			.append(ValueUtil.isEmpty(food.getSublistid()) ? "" : food.getSublistid()).append("@")
			.append(ValueUtil.isEmpty(food.getUnitCode()) ? "" : food.getUnitCode()).append("@")
			.append(food.getIsTemp()).append(";");//产品唯一编码
		}
		Log.e("-------划菜参数-------", sb.toString());
	}
	
	
	/**
	 * 套餐划菜筛选
	 * @param foods
	 * @return
	 */
	public List<Food> foodSaiXuan(List<Food> foods){
		List<Food> foodList=new ArrayList<Food>();
		Map<String,Food> tpcode=null;
		for(Food food:foods){
			if(ValueUtil.isNotEmpty(food.getTpcode())){
				if(food.getPcode()!=null&&food.getPcode().equals(food.getTpcode())){
					if(tpcode==null){
						tpcode=new HashMap<String,Food>();
					}
					tpcode.put(food.getTpcode(),food);
					foodList.add(food);
				}
			}
		}
		if(tpcode!=null){
			for(Food food:foods){
				if(ValueUtil.isNotEmpty(food.getTpcode())){
					if(food.getPcode()!=null&&!food.getPcode().equals(food.getTpcode())){
						if(ValueUtil.isEmpty(tpcode.get(food.getTpcode()))){
							foodList.add(food);
						}
					}
				}else{
					foodList.add(food);
				}
			}
			return foodList;
		}else{
			return foods;
		}
	}
	/**
	 * 调用划菜接口
	 * @author M.c
	 * @param productList
	 * @param methodName
	 * @param state 0划菜 1反划菜
	 */
	public void callElide(String productList,final List<Food> food,String methodName,final int state,final List<Food> reFood){
		CList<Map<String,String>> list=new CList<Map<String,String>>();
		list.add("deviceId", SharedPreferencesUtils.getDeviceId(this));	//设备编号 
		list.add("userCode", SharedPreferencesUtils.getUserCode(this));	//用户编码
		list.add("orderId", menuOrder);	//账单号
		list.add("tableNum", tableNumber);	//台位号
		list.add("productList", productList);//菜品列表
		new Server().connect(this, methodName, "ChoiceWebService/services/HHTSocket?/"+methodName, list, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				mLoadingDialog.dismiss();
				if(ValueUtil.isNotEmpty(result)&&result.split("@").length>0){
					String[] res=result.split("@");
					if(res[0].equals("0")){
						try {
							if(food==null){//如果为空证明为全单划菜
								getDataManager(YiXuanDishActivity2.this).updateDishesByOver(tableNumber,state);
								updateDisplay();
							}else if(reFood==null||reFood.size()<=0){//如果返划菜数据为空则结束划菜
								editDishes(food,tableNumber,state);
//								Toast.makeText(YiXuanDishActivity2.this, R.string.hua_cai_success, Toast.LENGTH_LONG).show();
							}else if(food!=null&&reFood!=null&&state==0){
								reCallElide(reFood);
							}
							selectAllCb.setChecked(false);
							if (state==0) {
								Toast.makeText(YiXuanDishActivity2.this, R.string.hua_cai_success, Toast.LENGTH_LONG).show();
							} else if (state==1) {
								Toast.makeText(YiXuanDishActivity2.this, R.string.hua_cai_success_other, Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							Toast.makeText(YiXuanDishActivity2.this, R.string.hua_cai_error, Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
					}else{
						Toast.makeText(YiXuanDishActivity2.this, res[1], Toast.LENGTH_LONG).show();
					}
				}else{
					new AlertDialog.Builder(YiXuanDishActivity2.this).setTitle(R.string.hint).setMessage(R.string.net_error)
					.setPositiveButton(R.string.confirm, null).show();
				}
			}
			
			@Override
			public void onBeforeRequest() {
				mLoadingDialog.show();
				isStop=true;
			}
		});
	}
	/**
	 * 修改本地库数据更新界面
	 * @author M.c
	 * @param state
	 */
	public void editDishes(List<Food> foods,String tableNum,int state){
		List<Food> foodList=foodSaiXuan(foods);
		for(Food food:foodList){
			if(ValueUtil.isNotEmpty(food.getTpcode())&&food.getTpcode().equals(food.getPcode())){//判断是否为套餐项
				for(int i=0;i<adapter.getCount();i++){
					Food fFood=adapter.getItem(i);
					if(ValueUtil.isNotEmpty(fFood.getTpcode())&&food.getTpcode().equals(fFood.getTpcode())){
						if(state==0){//划菜
							getDataManager(this).updateDishesByOver(tableNum,fFood.getOverCount()==null?fFood.getPcount():fFood.getOverCount(), fFood.getPcode(),fFood.getPKID());
						}else if(state==1){//反划菜
							getDataManager(this).updateDishesByOver(tableNum,"0", fFood.getPcode(),fFood.getPKID());
						}
					}
				}
			}else{
				if(state==0){//划菜
					getDataManager(this).updateDishesByOver(tableNum,food.getOverCount()==null?food.getPcount():food.getOverCount(), food.getPcode(),food.getPKID());
				}else if(state==1){//反划菜
					getDataManager(this).updateDishesByOver(tableNum,"0", food.getPcode(),food.getPKID());
				}
			}
		}
		updateDisplay();
	}
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yixuan_btn_table://台位
                Intent intent = new Intent(YiXuanDishActivity2.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.yixuan_btn_promptDish://催菜
                if(TsData.isReserve){
                    alertReserve();
                    break;
                }
                if(adapter==null||adapter.getSelectedCount() == 0){
                    Toast.makeText(this, R.string.please_checked_to_handle, Toast.LENGTH_SHORT).show();
                    return;
                }
                //调用催菜接口
                CList<Map<String, String>> params = new CList<Map<String,String>>();
                params.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
                params.add("userCode", SharedPreferencesUtils.getUserCode(this));
                params.add("orderId", menuOrder);
                params.add("tableNum", tableNumber);
                List<Food> foods=null;
                List<Food> mFoods=new ArrayList<Food>();
                for(Food mFood:foodArray){
                    if(mFood.isSelected()&&!mFood.getPcount().trim().equals(mFood.getOver().trim())){
                        mFoods.add(mFood);
                    }
                }
                foods=foodSaiXuan(mFoods);
                if(foods==null||foods.size()<=0){
                    ToastUtil.toast(this, R.string.push_food_unsuccess);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for(Food mFood:foods){
                    if(mFood.isSelected()&&!mFood.getPcount().trim().equals(mFood.getOver().trim())){
                        sb.append(mFood.getPKID()).append("@")//产品唯一编码
                                .append(mFood.getPcode()).append("@")//产品编码
                                .append(mFood.getPcname()).append("@")// 产品名称
                                .append(mFood.getTpcode()).append("@")//套餐编码
                                .append(mFood.getTpname()).append("@")//套餐名称
                                .append(mFood.getTpnum()).append("@")//套餐序号 （从0 开始， 每个套餐独立开始）
                                .append(mFood.getPcount()).append("@")//数量    正常点菜1 套餐明细数量根据实际情况而定   退菜-1
                                .append("0").append("@")//赠送数量 0  或  1  或 第二单位赠送数量
                                .append(ValueUtil.isEmpty(mFood.getFujiacode()) ? "" : mFood.getFujiacode()).append("@")//附加项编码  多个附加项用  !  隔开
                                .append(ValueUtil.isEmpty(mFood.getFujianame()) ? "" : mFood.getFujianame()).append("@")//附加项名称   多个附加项用  !  隔开
                                .append(mFood.getPrice()).append("@")//价格
                                .append(ValueUtil.isEmpty(mFood.getFujiaprice()) ? "" : mFood.getFujiaprice()).append("@")//附加项价格
                                .append("0").append("@")//第二单位重量    没有为0
                                .append("1").append("@")//第二单位标志  1 第一单位  2 第二单位
                                .append(mFood.getUnitCode()).append("@")//单位
                                .append(mFood.isIstc() ? "1" : "0").append("@")
								.append(ValueUtil.isEmpty(mFood.getSublistid())?"":mFood.getSublistid()).append("@")
								.append(mFood.getIsTemp()).append(";");//是否套餐  0  不是  1 是
                    }
                }
                if(sb.length()>1){
                    sb.deleteCharAt(sb.length()-1);
                }
                params.add("productList", sb.toString());

                Server server = new Server();
                server.connect(this, Constants.FastMethodName.PromptDish_METHODNAME, Constants.FastWebService.promptDish_WSDL, params, new OnServerResponse() {
                    @Override
                    public void onResponse(String result) {
                        mLoadingDialog.dismiss();
                        if(ValueUtil.isNotEmpty(result)){
                            Log.i("result", result);
                            String [] str = result.split("@");
                            if(str[0].equals("0")){
                                Toast.makeText(YiXuanDishActivity2.this, str[1],Toast.LENGTH_SHORT).show();
                                selectAllCb.setChecked(false);
                            }else{
                                return;
                            }
                        }else{
                            Toast.makeText(YiXuanDishActivity2.this, R.string.net_error,Toast.LENGTH_SHORT).show();
                        }

                        //调用催菜接口成功后,更新数据库中的urge字段
                        for(Food mFood:foodArray){
                            if(mFood.isSelected()){
                                getDataManager(YiXuanDishActivity2.this).updateFoodUrgeItem(mFood);
                            }
                        }

                        updateDisplay();
                    }
                    @Override
                    public void onBeforeRequest() {
                        mLoadingDialog.show();
                    }
                });
			break;
		case R.id.yixuan_btn_clearDish://划菜
			if(TsData.isReserve){
				alertReserve();
				break;
			}
			callElide();
			break;
		case R.id.yixuan_btn_addDish://加菜 
			Intent it = new Intent(YiXuanDishActivity2.this, Eatables.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			it.putExtra("direction", "MainDirection");
			it.putExtra("topBundle", getTblStateBundle());
			startActivity(it);
			break;
//		case R.id.yixuan_btn_addDish1:
//			Server ser = new Server();
//			CList<Map<String, String>> param2 = new CList<Map<String,String>>();
//			param2.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
//			param2.add("userCode", SharedPreferencesUtils.getUserCode(this));
//			param2.add("tableNum", tableNumber);
//			param2.add("orderId", menuOrder);
//			param2.add("immediateOrWait", "1");
//			ser.connect(this, Constants.FastMethodName.KITCHENPRINT, Constants.FastWebService.kitchenPrint_WSDL, param2, new OnServerResponse() {
//				@Override
//				public void onResponse(String result) {
//					mLoadingDialog.dismiss();
//					if(ValueUtil.isNotEmpty(result)){
//						String response [] = result.split("@");
//						String flag = response[0];
//						if(flag.equals("0")){
//							Toast.makeText(YiXuanDishActivity2.this, R.string.print_success,Toast.LENGTH_SHORT).show();
//						}else{
//							Toast.makeText(YiXuanDishActivity2.this, response[1],Toast.LENGTH_SHORT).show();
//						}
//					}else{
//						Toast.makeText(YiXuanDishActivity2.this, R.string.net_error,Toast.LENGTH_SHORT).show();
//					}
//				}
//
//				@Override
//				public void onBeforeRequest() {
//					mLoadingDialog.show();
//				}
//			});
//			break;
		case R.id.yixuan_btn_prePrint: //预打印
			Server serv = new Server();
			CList<Map<String, String>> param1 = new CList<Map<String,String>>();
			param1.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
			param1.add("userCode", SharedPreferencesUtils.getUserCode(this));
			param1.add("tableNum", tableNumber);
			param1.add("orderId", menuOrder);
			serv.connect(this, Constants.FastMethodName.prePrint_METHODNAME, Constants.FastWebService.prePrint_WSDL, param1, new OnServerResponse() {
				@Override
				public void onResponse(String result) {
					mLoadingDialog.dismiss();
					if(ValueUtil.isNotEmpty(result)){
						String response [] = result.split("@");
						String flag = response[0];
						if(flag.equals("0")){
							Toast.makeText(YiXuanDishActivity2.this, R.string.print_success,Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(YiXuanDishActivity2.this, response[1],Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(YiXuanDishActivity2.this, R.string.net_error,Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onBeforeRequest() {
					mLoadingDialog.show();
				}
			});
			break;
		case R.id.yixuan_btn_preBalance://预结算
            balance();
			break;
			
		case R.id.yixuan_btn_back://返回
			String mark=this.getIntent().getStringExtra("direction");
			Intent intent2=null;
			if(mark!=null&&mark.equals("MainDirection")){
				intent2 = new Intent(YiXuanDishActivity2.this, MainActivity.class);
			}else{
				intent2 = new Intent(YiXuanDishActivity2.this, Eatables.class);
			}
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent2.putExtra("direction", "yixuan");
			startActivity(intent2);
			break;
		case R.id.eatable_btn_back://返回// TODO 
			String mark1=this.getIntent().getStringExtra("direction");
			Intent intent3=null;
			if(mark1!=null&&mark1.equals("MainDirection")){
				intent3 = new Intent(YiXuanDishActivity2.this, MainActivity.class);
			}else{
				intent3 = new Intent(YiXuanDishActivity2.this, Eatables.class);
			}
			intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent3.putExtra("direction", "yixuan");
			startActivity(intent3);
			break;
		case R.id.orderinfo_ImageView:
			// TODO   
			View layout = null ;
			layout = LayoutInflater.from(YiXuanDishActivity2.this).inflate(R.layout.menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			manNum = (TextView) layout.findViewById(R.id.dishesact_tv_mannum);
			womanNum = (TextView) layout.findViewById(R.id.dishesact_tv_womannum);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			initData();
			AlertDialog tipdialog = new AlertDialog.Builder(YiXuanDishActivity2.this, R.style.Dialog_tip).setView(layout).create();
			tipdialog.setCancelable(true);
	        Window dialogWindow = tipdialog.getWindow();
	        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	        dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
	        lp.x = 0; // 新位置X坐标
	        lp.y = topLinearLayout.getHeight() -15; // 新位置Y坐标
	        dialogWindow.setAttributes(lp);
			tipdialog.show();
			break;
		case R.id.btn_searchView:
			btnSearch.setVisibility(View.GONE);
			etSearch.setVisibility(View.VISIBLE);
			etSearch.requestFocus();
			InputMethodUtils.toggleSoftKeyboard(this);
			break;
		default:
			break;
		}
	}
	public void toast(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	/**
	 * 使用调用接口获取菜品
	 */
	public void refresh(){
//		（String deviceId,String userCode,String tableNum,String orderId） 
		CList<Map<String,String>> data=new CList<Map<String,String>>();
		data.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
		data.add("userCode", SharedPreferencesUtils.getUserCode(this));
		data.add("tableNum", tableNumber);
		data.add("orderId", menuOrder);
		new Server().connect(this, "queryWholeProducts", "ChoiceWebService/services/HHTSocket?/queryWholeProducts", data, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                isStop = false;
                String[] res = ValueUtil.isEmpty(result) ? null : result.split("@");
                if (ValueUtil.isNotEmpty(res)) {
                    if (res[0].equals("0")) {
                        if (ValueUtil.isNotEmpty(foodArray)) {
                            foodArray.clear();
                        }
                        Log.e("返回的菜品", result);
                        foodArray = AnalyticalXmlUtil.getFoodList(result);

                        //
                        if (adapter == null) {
                            adapter = new YiXuanDishesAdapter(YiXuanDishActivity2.this, foodArray);
                            listView.setAdapter(adapter);
                        } else {
                            adapter.setData(foodArray);
                            adapter.notifyDataSetChanged();
                        }
                        dishCounts.setText(adapter.getDishSelectedCount() + " " + YiXuanDishActivity2.this.getString(R.string.part));

                        //得到总的价钱
                        double totalSalary = adapter.getTotalSalary();
                        moneyCounts.setText(ValueUtil.setScale(totalSalary, 2, 5));

                        //得到总附加项的价钱
                        double totalFujiaSalary = adapter.getFujiaSalary();
                        orderNumber.setText(ValueUtil.setScale(totalFujiaSalary, 2, 5));
                    } else {
						if (foodArray!=null) {
							foodArray.clear();
							if (adapter == null) {
								adapter = new YiXuanDishesAdapter(YiXuanDishActivity2.this, foodArray);
								listView.setAdapter(adapter);
							} else {
								adapter.setData(foodArray);
								adapter.notifyDataSetChanged();
							}
						}
                        if (res.length < 2 && !res[1].trim().equals("NULL")) {
                            toast(res[1]);
                        }
                    }
                } else {
                    ToastUtil.toast(YiXuanDishActivity2.this, R.string.net_error);
                }
            }

            @Override
            public void onBeforeRequest() {
                isStop = true;
            }
        });
	}

	/**
	 * 划菜界面
	 */
	public void callElide(){
		if(adapter==null||adapter.getSelectedCount() == 0){
			Toast.makeText(this, R.string.please_checked_to_handle, Toast.LENGTH_SHORT).show();
			return;
		}
		if(adapter.getSelectedCount() == foodArray.size()){
			new AlertDialog.Builder(this).setTitle(R.string.hint).setMessage(R.string.is_all_hua_cai)
			.setPositiveButton(R.string.yixuan_huacai,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					callElide(null,null,"callElide",0,null);
				}
			}).setNeutralButton(R.string.fan_hua_cai, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					callElide(null,null,"reCallElide",1,null);
				}
			}).setNegativeButton(R.string.cancle, null).show();
		}else{
			final List<Food> list=new ArrayList<Food>();
			final List<Food> reList=new ArrayList<Food>();
			for(final Food food:foodArray){
				BigDecimal count=ValueUtil.isNaNofBigDecimal(food.getPcount()).subtract(ValueUtil.isNaNofBigDecimal(food.getOver()));
				String countString=count.toString().matches("-?\\d+$")?count.toString():count.intValue()+"";
				if(food!=null&&food.isSelected()){
					if(count.doubleValue()>0){
						food.setOverCount(countString);
						list.add(food);
					}else if(count.doubleValue()==0){
						food.setOverCount(food.getPcount());
						reList.add(food);
					}else{
						food.setOverCount(count.abs().toString());
						reList.add(food);
					}
				}
			}
			StringBuilder sb=null;
			if(list.size()>0){
				sb=new StringBuilder();
				setFood(list,sb);
				callElide(sb.toString(),list,"callElide",0,reList);
			}
		}
	}

	/**
	 * 返划菜
	 * @param reList
	 */
	public void reCallElide(List<Food> reList){
		if(reList.size()>0){
			StringBuilder sb=new StringBuilder();
			setFood(reList,sb);
			callElide(sb.toString(),reList,"reCallElide",1,null);
		}
	}
	
	public void alertReserve(){
		Toast.makeText(this,R.string.reserve_table_error,Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			String mark=this.getIntent().getStringExtra("direction");
			Intent intent2=null;
			if(mark!=null&&mark.equals("MainDirection")){
				intent2 = new Intent(YiXuanDishActivity2.this, MainActivity.class);
			}else{
				intent2 = new Intent(YiXuanDishActivity2.this, Eatables.class);
			}
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent2.putExtra("direction", "yixuan");
			startActivity(intent2);
		}
		return super.onKeyDown(keyCode, event);
	}


    public void balance(){
        String sql="SELECT b.code as rolecode from module a,role b,ROLEMODULE c " +
                " where a.PK_MODULE =c.PK_MODULE and b.PK_ROLE=c.PK_ROLE and a.CODE='9905005'";
        List<String> list=new ListProcessor().query(sql, new String[]{}, this, new ListProcessor.Result<List<String>>() {
            @Override
            public List<String> handle(Cursor c) {
				List<String> map=new ArrayList<String>();
                while (c.moveToNext()){
					map.add(c.getString(0));
                }
                return map;
            }
        });
		System.out.print(SharedPreferencesUtils.getRoleCode(this));
        if((list.size()>0&&list.contains(SharedPreferencesUtils.getRoleCode(this)))||list.size()==0){
            if(TsData.isReserve){
                alertReserve();
            }else {
                Intent intent1 = new Intent(YiXuanDishActivity2.this, SettleAccountsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tableNum", tableNumber);     //台位编号
                bundle.putString("manCounts", manCounts);     // 人数
                bundle.putString("womanCounts", womanCounts);    // 人数
                bundle.putString("orderId", menuOrder);    //账单号
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        }else{
            ToastUtil.toast(this, R.string.not_permission);
        }

    }

	/**
	 * 搜索框所用的
	 */
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			matchLists.clear();
			String changingStr = etSearch.getText().toString().trim();
			if(ValueUtil.isNotEmpty(changingStr)){
				if(foodArray != null){
					for(Food f:foodArray){
						String currentPcnameStr = f.getPcname();
						String currentPcodeStr = f.getPcode();
						if(currentPcodeStr.indexOf(changingStr)!= -1 || ConvertPinyin.convertJianPin(currentPcnameStr).indexOf(changingStr) != -1 || ConvertPinyin.convertQuanPin(currentPcnameStr).indexOf(changingStr)!= -1){
							matchLists.add(f);
						}
					}
					
					if(adapter != null){
						adapter.setData(matchLists);
						adapter.notifyDataSetChanged();
					}
				}
			}else{
				adapter.setData(foodArray);
				adapter.notifyDataSetChanged();
			}
		}
	};

}
