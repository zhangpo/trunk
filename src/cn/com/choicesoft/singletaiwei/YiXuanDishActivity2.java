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
 * �ѵ�˵�
 * 
 */
public class YiXuanDishActivity2 extends BaseActivity implements OnClickListener,SlideListener{
	
	private TextView dishCounts,moneyCounts,orderNumber;//�����Ʒ  3.0��  �ܼ� 51.80  �˵��ţ�H000364
	
	private TextView tableNum,orderNum,manNum,womanNum,userNum;//ͷ����ɫ����������
		
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
	
	private LinearLayout topDigitalLayout;//���ֱ�������һ��������
	
	private CheckBox selectAllCb;//ȫѡ��ť
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
		userNum.setText(SharedPreferencesUtils.getOperator(this));//���ò���Ա
		if(bundle != null){
			tableNumber = bundle.getString("tableNum");//̨λ��
			menuOrder = bundle.getString("orderId");//�˵���
			womanCounts = bundle.getString("womanCs");//Ů������
			manCounts = bundle.getString("manCs");//��������
			
			//Ϊͷ����ɫ��������ֵ
			tableNum.setText(tableNumber);
			orderNum.setText(menuOrder);
			manNum.setText(ValueUtil.isEmpty(manCounts)?"0" :manCounts);
			womanNum.setText(ValueUtil.isEmpty(womanCounts)?"0" :womanCounts);
			//��ʼ����Ա��Ϣ
			VipMsg.iniVip(this, bundle.getString("tableNum"),R.id.vipMsg_ImageView);	
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateDisplay();
	}

	/**
	 * ˢ�½���
	 */
	private void updateDisplay() {
		if(SharedPreferencesUtils.getIsNet(this)){
			foodArray = getDataManager(this).getAllFoodListByTablenum(tableNumber,menuOrder);//����tableNumber,orderID,send = 1 ��AllCheck���в�ѯ
			if(ValueUtil.isEmpty(foodArray)){//������ݿ�û��������Ĭ��ʹ�ýӿ�
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
				//�õ��ܵļ�Ǯ
				double totalSalary = adapter.getTotalSalary();
				moneyCounts.setText(ValueUtil.setScale(totalSalary, 2, 5));
				
				//�õ��ܸ�����ļ�Ǯ
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
//					final Spinner cause = (Spinner) view.findViewById(R.id.cancle_products_cause);//�˲�ԭ��
//					final EditText count = (EditText) view.findViewById(R.id.cancle_products_count);//�˲�����
//					final EditText usercode = (EditText) view.findViewById(R.id.cancle_products_usercode);//�û�����
//					final EditText userpwd = (EditText) view.findViewById(R.id.cancle_products_userpwd);//�û�����
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
//											//ͨ�������ȡdialog�е�˽������mShowing
//											try {
//												field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//												field.setAccessible(false);//���ø����Կ��Է���
//											} catch (NoSuchFieldException e) {
//												e.printStackTrace();
//											}
//											String c = count.getText().toString();
//											if (ValueUtil.isNotEmpty(c)) {
//												if (ValueUtil.isNaNofInteger(c) > 0) {
//													if (field != null) {
//														field.setAccessible(true);//���ø����Կ��Է���
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
		
		//ȫѡ��ť
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
	//�˲�
	private void tuicai(final Food food){
		final LinearLayout layout=(LinearLayout)this.getLayoutInflater().inflate(R.layout.verify_pwd, null);
		Button confirm=(Button)layout.findViewById(R.id.verify_pwd_confirm);
		Button cancel=(Button)layout.findViewById(R.id.verify_pwd_cancel);
		final cn.com.choicesoft.view.ClearEditText pwdEdit=(ClearEditText)layout.findViewById(R.id.verify_pwd_Edit);
		TextView textView=(TextView)layout.findViewById(R.id.verify_pwd_Text);
		textView.setText("�������˲�����");
		pwdEdit.setText(food.getPcount());
		pwdEdit.setInputType(InputType.TYPE_CLASS_PHONE);
		final AlertDialog alert=new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle("�˲�").setView(layout).show();
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
	 * �����Ƿ񱣴�Ի���
	 * @return
	 */
	private Dialog showIfSaveDialog(final Food food) {

		String [] itemSave = {this.getString(R.string.yes),this.getString(R.string.no)};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("�Ƿ��˲ˣ�");
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
		//������ɫ��
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
	 * ������ص�����
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
	 * �˲�
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
	 * �˲�jsonת��
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
		Log.e("�˲�JSON", json);
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
	 * ƴ�����軮�Ĳ���Ϣ
	 * @author M.c
	 * @param sb
	 */
	public void setFood(List<Food> foods,StringBuilder sb){
		List<Food> foodList=foodSaiXuan(foods);
		for(Food food:foodList){
			sb.append(food.getPcode()).append("@")//��Ʒ����
			.append(food.getTpcode()).append("@")//�ײͱ���
			.append(food.getTpnum()).append("@")//�ײ���� ����0 ��ʼ�� ÿ���ײͶ�����ʼ��
			.append(ValueUtil.isEmpty(food.getFujiacode()) ? "" : food.getFujiacode()).append("@")//���������  �����������  !  ����
			.append(ValueUtil.isNotEmpty(food.getWeightflg()) ? food.getWeightflg() : "1").append("@")//�ڶ���λ��־  1 ��һ��λ  2 �ڶ���λ
			.append(food.isIstc() ? "1" : "0").append("@")//�Ƿ��ײ�  0  ����  1 ��
			.append(food.getOverCount() == null ? food.getPcount() : food.getOverCount()).append("@")//���˵�����
			.append(food.getPKID()).append("@")
			.append(ValueUtil.isEmpty(food.getSublistid()) ? "" : food.getSublistid()).append("@")
			.append(ValueUtil.isEmpty(food.getUnitCode()) ? "" : food.getUnitCode()).append("@")
			.append(food.getIsTemp()).append(";");//��ƷΨһ����
		}
		Log.e("-------���˲���-------", sb.toString());
	}
	
	
	/**
	 * �ײͻ���ɸѡ
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
	 * ���û��˽ӿ�
	 * @author M.c
	 * @param productList
	 * @param methodName
	 * @param state 0���� 1������
	 */
	public void callElide(String productList,final List<Food> food,String methodName,final int state,final List<Food> reFood){
		CList<Map<String,String>> list=new CList<Map<String,String>>();
		list.add("deviceId", SharedPreferencesUtils.getDeviceId(this));	//�豸��� 
		list.add("userCode", SharedPreferencesUtils.getUserCode(this));	//�û�����
		list.add("orderId", menuOrder);	//�˵���
		list.add("tableNum", tableNumber);	//̨λ��
		list.add("productList", productList);//��Ʒ�б�
		new Server().connect(this, methodName, "ChoiceWebService/services/HHTSocket?/"+methodName, list, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				mLoadingDialog.dismiss();
				if(ValueUtil.isNotEmpty(result)&&result.split("@").length>0){
					String[] res=result.split("@");
					if(res[0].equals("0")){
						try {
							if(food==null){//���Ϊ��֤��Ϊȫ������
								getDataManager(YiXuanDishActivity2.this).updateDishesByOver(tableNumber,state);
								updateDisplay();
							}else if(reFood==null||reFood.size()<=0){//�������������Ϊ�����������
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
	 * �޸ı��ؿ����ݸ��½���
	 * @author M.c
	 * @param state
	 */
	public void editDishes(List<Food> foods,String tableNum,int state){
		List<Food> foodList=foodSaiXuan(foods);
		for(Food food:foodList){
			if(ValueUtil.isNotEmpty(food.getTpcode())&&food.getTpcode().equals(food.getPcode())){//�ж��Ƿ�Ϊ�ײ���
				for(int i=0;i<adapter.getCount();i++){
					Food fFood=adapter.getItem(i);
					if(ValueUtil.isNotEmpty(fFood.getTpcode())&&food.getTpcode().equals(fFood.getTpcode())){
						if(state==0){//����
							getDataManager(this).updateDishesByOver(tableNum,fFood.getOverCount()==null?fFood.getPcount():fFood.getOverCount(), fFood.getPcode(),fFood.getPKID());
						}else if(state==1){//������
							getDataManager(this).updateDishesByOver(tableNum,"0", fFood.getPcode(),fFood.getPKID());
						}
					}
				}
			}else{
				if(state==0){//����
					getDataManager(this).updateDishesByOver(tableNum,food.getOverCount()==null?food.getPcount():food.getOverCount(), food.getPcode(),food.getPKID());
				}else if(state==1){//������
					getDataManager(this).updateDishesByOver(tableNum,"0", food.getPcode(),food.getPKID());
				}
			}
		}
		updateDisplay();
	}
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yixuan_btn_table://̨λ
                Intent intent = new Intent(YiXuanDishActivity2.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.yixuan_btn_promptDish://�߲�
                if(TsData.isReserve){
                    alertReserve();
                    break;
                }
                if(adapter==null||adapter.getSelectedCount() == 0){
                    Toast.makeText(this, R.string.please_checked_to_handle, Toast.LENGTH_SHORT).show();
                    return;
                }
                //���ô߲˽ӿ�
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
                        sb.append(mFood.getPKID()).append("@")//��ƷΨһ����
                                .append(mFood.getPcode()).append("@")//��Ʒ����
                                .append(mFood.getPcname()).append("@")// ��Ʒ����
                                .append(mFood.getTpcode()).append("@")//�ײͱ���
                                .append(mFood.getTpname()).append("@")//�ײ�����
                                .append(mFood.getTpnum()).append("@")//�ײ���� ����0 ��ʼ�� ÿ���ײͶ�����ʼ��
                                .append(mFood.getPcount()).append("@")//����    �������1 �ײ���ϸ��������ʵ���������   �˲�-1
                                .append("0").append("@")//�������� 0  ��  1  �� �ڶ���λ��������
                                .append(ValueUtil.isEmpty(mFood.getFujiacode()) ? "" : mFood.getFujiacode()).append("@")//���������  �����������  !  ����
                                .append(ValueUtil.isEmpty(mFood.getFujianame()) ? "" : mFood.getFujianame()).append("@")//����������   �����������  !  ����
                                .append(mFood.getPrice()).append("@")//�۸�
                                .append(ValueUtil.isEmpty(mFood.getFujiaprice()) ? "" : mFood.getFujiaprice()).append("@")//������۸�
                                .append("0").append("@")//�ڶ���λ����    û��Ϊ0
                                .append("1").append("@")//�ڶ���λ��־  1 ��һ��λ  2 �ڶ���λ
                                .append(mFood.getUnitCode()).append("@")//��λ
                                .append(mFood.isIstc() ? "1" : "0").append("@")
								.append(ValueUtil.isEmpty(mFood.getSublistid())?"":mFood.getSublistid()).append("@")
								.append(mFood.getIsTemp()).append(";");//�Ƿ��ײ�  0  ����  1 ��
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

                        //���ô߲˽ӿڳɹ���,�������ݿ��е�urge�ֶ�
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
		case R.id.yixuan_btn_clearDish://����
			if(TsData.isReserve){
				alertReserve();
				break;
			}
			callElide();
			break;
		case R.id.yixuan_btn_addDish://�Ӳ� 
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
		case R.id.yixuan_btn_prePrint: //Ԥ��ӡ
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
		case R.id.yixuan_btn_preBalance://Ԥ����
            balance();
			break;
			
		case R.id.yixuan_btn_back://����
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
		case R.id.eatable_btn_back://����// TODO 
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
	        lp.x = 0; // ��λ��X����
	        lp.y = topLinearLayout.getHeight() -15; // ��λ��Y����
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
	 * ʹ�õ��ýӿڻ�ȡ��Ʒ
	 */
	public void refresh(){
//		��String deviceId,String userCode,String tableNum,String orderId�� 
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
                        Log.e("���صĲ�Ʒ", result);
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

                        //�õ��ܵļ�Ǯ
                        double totalSalary = adapter.getTotalSalary();
                        moneyCounts.setText(ValueUtil.setScale(totalSalary, 2, 5));

                        //�õ��ܸ�����ļ�Ǯ
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
	 * ���˽���
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
	 * ������
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
                bundle.putString("tableNum", tableNumber);     //̨λ���
                bundle.putString("manCounts", manCounts);     // ����
                bundle.putString("womanCounts", womanCounts);    // ����
                bundle.putString("orderId", menuOrder);    //�˵���
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        }else{
            ToastUtil.toast(this, R.string.not_permission);
        }

    }

	/**
	 * ���������õ�
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