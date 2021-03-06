package cn.com.choicesoft.chinese.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.BaseActivity;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.Eatables;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.activity.WelcomeActivity;
import cn.com.choicesoft.adapter.EatableGridAdapter;
import cn.com.choicesoft.adapter.EatableListAdapter;
import cn.com.choicesoft.adapter.EatableSubGridViewAdapter;
import cn.com.choicesoft.adapter.SingleFujiaAdapter;
import cn.com.choicesoft.bean.*;
import cn.com.choicesoft.chinese.bean.JNowFood;
import cn.com.choicesoft.chinese.bean.JNowOrder;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseResultAlt;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.chinese.util.JsonUtil;
import cn.com.choicesoft.chinese.util.OrderSaveUtil;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.singletaiwei.YiDianDishActivity;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.*;
import cn.com.choicesoft.chinese.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import cn.com.choicesoft.util.HttpConnectionUtil.HttpMethod;
/**
 * 中餐点餐界面
 */
public class ChineseEatables extends BaseActivity implements OnClickListener{
		
	private ListView listView;//左边菜品大类对应的控件
	private EatableListAdapter eatListAdapter;//左边菜品大类对应的控件的适配器
	private LoadingDialog dialog1;//提示框
	private GridView gridView;//右边具体菜品对应的控件
	private EatableGridAdapter eatGridAdapter;//右边具体菜品对应的控件的适配器
	
	private LoadingDialog mLoadingDialog;
	
	private TextView tableNum,orderNum,peopleNum,userNum;//顶部红色标题栏
	
	private TextView radioOne, radioTwo, radioThree, radioFour, radioFive,radioSix, radioSeven, radioEight, radioNight,radioZero,radioX,radioValueShow ;
	
	private ClearEditText etSearchView;//搜索框
	private Button btnSearchView;//搜索按钮
	private TextView btnSearchCancel;//搜索按钮

	private Button remark,yiDian,back;//底部三个按钮,备注 ，已点菜，返回
	
	private LinearLayout linearTCDetails;//套餐详情的内布局
	private LinearLayout linearTcLayout;//套餐详情的整个布局
	private Button tcBtnCertain;//套餐详情的确认按钮
	private Button tcBtnCancel;//套餐详情的取消按钮

	private List<Food> dishLists = null;// 存放具体菜品
//	private List<CurrentUnit> unitCurLists = null;//存放第二单位的集合
	private List<Grptyp> grpLists;//存放菜品大类集合
	
	private List<Food> dishClassLists = null;//每个菜品大类对应着一些具体菜品的集合
	private List<Food> tempdishClassLists = new ArrayList<Food>();//每个菜品大类对应着一些具体菜品的集合            临时的
	private List<Food> matchLists = new ArrayList<Food>();//信息检索完后的结果所匹配的集合
	private List<Map<String,String>> soldOutLists = new ArrayList<Map<String, String>>();//存放沽清菜品编码集合
	private List<Food> tempNotAlternativeLists = null;//点套餐时，存放不可换购项的临时集合
    private List<Addition> fujiamatchLists = new ArrayList<Addition>();//附加项信息检索完后的结果所匹配的集合
	
	
	private SingleMenu menu = SingleMenu.getMenuInstance();
	private List<Food> guestLists = GuestDishes.getGuDishInstance();
	

	int tpnumflag = 0;//声明成全局变量，为了使用tpnum
	
	private Food tempFujiaFood = null;//针对于附加项的
	private Food curTpnumTCfood =null;//套餐需要tcmoneymode 和 tpnum
	
	private int digitalFlag = -1;//头部数字标题栏所用的标识位
	
	private Dialog dialog = null;//附加项所用的dialog
	
	private List<EatableSubGridViewAdapter> subGridViewAdapterLists = null;
	
	private String grp = null;
	private ImageView menuinfo_imageView;
	private View layout;
	private LinearLayout topLinearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        TsData.isSave=false;
//		 设置为全屏模式
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (ChioceActivity.ispad) {
			setContentView(R.layout.chinese_eatables_layout);
		} else {
			setContentView(R.layout.eatables_layout);
		}
		initView();
		initData();
		initListener();
		
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		
		radioOne.setOnClickListener(onClickListener);
		radioTwo.setOnClickListener(onClickListener); 
		radioThree.setOnClickListener(onClickListener);
		radioFour.setOnClickListener(onClickListener);
		radioFive.setOnClickListener(onClickListener);
		radioSix.setOnClickListener(onClickListener);
		radioSeven.setOnClickListener(onClickListener);
		radioEight.setOnClickListener(onClickListener);
		radioNight.setOnClickListener(onClickListener);
		radioZero.setOnClickListener(onClickListener);
		radioX.setOnClickListener(onClickListener);
		remark.setOnClickListener(this);
		yiDian.setOnClickListener(this);
		back.setOnClickListener(this);
		
		tcBtnCertain.setOnClickListener(this);
		tcBtnCancel.setOnClickListener(this);
		
		btnSearchView.setOnClickListener(this);
		
		etSearchView.addTextChangedListener(watcher);
        btnSearchCancel.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				
				//用来判断某个套餐是否选择完毕
				if(linearTcLayout.getVisibility() == View.VISIBLE){
					Toast.makeText(ChineseEatables.this, R.string.tc_not_dismiss, Toast.LENGTH_SHORT).show();
					return;
				}
				
				eatListAdapter.setSelectedPosition(position);
				eatListAdapter.notifyDataSetInvalidated();
				grp = grpLists.get(position).getGrp();
				
				updateDishClass(grp, dishLists);
				
				if(eatGridAdapter != null){
					eatGridAdapter.setEatableGridSource(dishClassLists);
					eatGridAdapter.notifyDataSetChanged();
				}
				
				tempFujiaFood = null;//rather special当点击左边菜单栏的时候，重新切换到了另一个类别，赋值为空
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			private static final int DOUBLE_CLICK_TIME = 350;//双击间隔时间350毫秒
			private boolean waitDouble = true;

			//单击事件
			private Handler handler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					Food foodItem = dishClassLists.get(msg.arg1);
					TsData.isSave=true;//是否保存
					if(!foodItem.isIstc()){
						tempFujiaFood=foodItem;
					}

					//如果是沽清菜品，就得处理
					if (foodItem.isSoldOut()) {
						return;
					}

					foodOrder(foodItem, (View)msg.obj);
				}

			};
			//双击事件
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, final int position,long id) {
				if(waitDouble){
					waitDouble = false;        //与执行双击事件
					new Thread(){
						public void run() {
							try {
								Thread.sleep(DOUBLE_CLICK_TIME);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}    //等待双击时间，否则执行单击事件
							if(!waitDouble){
								//如果过了等待事件还是预执行双击状态，则视为单击
								waitDouble = true;
								Message msg = handler.obtainMessage();
								msg.obj = view;
								msg.arg1 = position;
								handler.sendMessage(msg);
							}
						}
					}.start();
				}else{
					waitDouble = true;
					Food foodItem = dishClassLists.get(position);
					delectFood(foodItem);
				}
			}
		});
		
	}
	public LoadingDialog getDialog() {
		if(dialog1==null){
			dialog1 = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
			dialog1.setCancelable(true);
		}
		return dialog1;
	}
	/**
	 * 点击某个套餐后所进行的操作
	 * @param pFood
	 */
	protected void tcCallBack(Food pFood) {
	
		Food creatFood = new Food();
		boolean isTpnumExists = false;
		
		for(Food f:guestLists){
			if(f.isIstc() && f.getTpcode().equals(f.getPcode()) && pFood.getPcode().equals(f.getPcode())){
				tpnumflag = Integer.parseInt(f.getTpnum());
				tpnumflag +=1;
				creatFood.setTpnum(tpnumflag+"");
				isTpnumExists = true;
			}
		}
		
		if(!isTpnumExists){
			creatFood.setTpnum("0");
		}
		
		creatFood.setTabNum(menu.getTableNum());
		creatFood.setOrderId(menu.getMenuOrder());
		creatFood.setMan(menu.getManCounts());
		creatFood.setWoman(menu.getWomanCounts());
		creatFood.setPcode(pFood.getPcode());
		creatFood.setPcname(pFood.getPcname());
		creatFood.setTpcode(pFood.getPcode());//此处比较特殊
		creatFood.setTpname(pFood.getPcname());//此处比较特殊
		creatFood.setPcount("1");//此处很特殊
		creatFood.setCounts(pFood.getCounts());//此处很特殊
		creatFood.setPrice(pFood.getPrice());
		creatFood.setUnit(pFood.getUnit());
		creatFood.setWeightflg(pFood.getWeightflg());//第二单位标识
		creatFood.setIstc(pFood.isIstc());//此处比较特殊
		creatFood.setTcMoneyMode(pFood.getTcMoneyMode());
		//设置赠送todo
//		guestLists.add(creatFood);
		
		curTpnumTCfood = creatFood;//这里不是为哪一项菜品添加附加项做准备 ，而是套餐有三种计价方式，计算时，用到tpnum和tcmoneymode,将tpnum,tcmoneymode传进去       rather special
		
				
		linearTcLayout.setVisibility(View.VISIBLE);
		
		List<List<Food>> allTcDetailLists= getDataManager(this).queryTcItemLists(creatFood);//查询出该套餐下的所有明细集合 
		showTcdetails(allTcDetailLists);
			
	}


	/**
	 * 查询出数据，初始化套餐详情的view,新版本
	 * @param allTcDetails
	 */
	private void showTcdetails(List<List<Food>> allTcDetails) {
		
		subGridViewAdapterLists = new ArrayList<EatableSubGridViewAdapter>();
		
		tempNotAlternativeLists = new ArrayList<Food>();//将不可换购项放入到该集合中
		
		//匹配套餐明细菜品的第二单位,如果有第二单位就设为2，默认的都设为1
//		for(List<Food> singleItemLists:allTcDetails){
//			for(Food food:singleItemLists){
//				food.setWeightflg("1");
//				for(CurrentUnit curUnit:unitCurLists){
//					if(food.getPcode().equals(curUnit.getItcode())){
//						food.setWeightflg("2");
//					}
//				}
//			}
//		}
		
		int tcGroupNumber = 0;//套餐编号所用
		
		for(final List<Food> singleLists:allTcDetails){
			
			if(singleLists.size() > 0){        //可换购项

				//在此处匹配套餐明细菜品中的沽清菜品    只匹配可换购项
//				for(Food food:singleLists){
//					for(String outStr:soldOutLists){
//						if(outStr.equals(food.getPcode())){
//							food.setSoldOut(true);
//						}else{
//							food.setSoldOut(false);
//						}
//					}
//				}

				tcGroupNumber+=1;
				
				TextView tcGroupTv = new TextView(ChineseEatables.this);
				tcGroupTv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tcGroupTv.setTextColor(Color.BLACK);
				tcGroupTv.setText(tcGroupNumber+"    ");
				cn.com.choicesoft.view.TcGridView tcGridView = new cn.com.choicesoft.view.TcGridView(this);
				tcGridView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				// TODO 
				if (ChioceActivity.ispad) {
					tcGridView.setNumColumns(5);
				}else{
					tcGridView.setNumColumns(3);
				}
				tcGridView.setDrawSelectorOnTop(true);
				tcGridView.setVerticalSpacing(5);
				tcGridView.setHorizontalSpacing(5);
				tcGridView.setBackgroundColor(Color.parseColor("#ADD8E6"));
				tcGridView.setSelector(R.color.transparent);
				EatableSubGridViewAdapter subGridAdapter = new EatableSubGridViewAdapter(this, singleLists,String.valueOf(tpnumflag));
				tcGridView.setAdapter(subGridAdapter);
				subGridViewAdapterLists.add(subGridAdapter);
				tcGridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						int  radioValue = Integer.parseInt(radioValueShow.getText().toString());//得到数字标题栏最右边的那个值
						radioValueShow.setText("1");//将上面的数字标题栏还原
						digitalFlag = -1;
						
						EatableSubGridViewAdapter adapter = (EatableSubGridViewAdapter) parent.getAdapter();
						
						Food foodDefault = adapter.getDefaultsFood();
						//TODO 快餐错误 应为下
						Food foodCurrent = (Food)adapter.getItem(position);
						
						//如果套餐明细是沽清菜品，就得处理
						if(!foodCurrent.isSoldOut()){
							tempFujiaFood = foodCurrent;//为套餐明细菜品添加附加项做准备
						}else{
							return;
						}						

						
						foodCurrent.setTabNum(menu.getTableNum());
						foodCurrent.setOrderId(menu.getMenuOrder());
						foodCurrent.setMan(menu.getManCounts());
						foodCurrent.setWoman(menu.getWomanCounts());
//						foodCurrent.setTpnum(tpnumflag+"");//这里相当的特殊,为套餐明细添加tpnum
						
						if(radioValue != 0){
							if(foodCurrent.isSelected()){
								
								int countCurrent = Integer.parseInt(foodCurrent.getPcount());
								int countNew = countCurrent + radioValue;
								if(ValueUtil.isNotEmpty(foodCurrent.getMaxCnt()) && countNew > Integer.parseInt(foodCurrent.getMaxCnt()) ){
									Toast.makeText(ChineseEatables.this, "该份菜品最多可选择 "+foodCurrent.getMaxCnt(), Toast.LENGTH_SHORT).show();
								}else if(adapter.getCountsAboutPcount() + radioValue > Integer.parseInt(foodDefault.getMaxCnt())){
									Toast.makeText(ChineseEatables.this, "已超过该组数量的上限 "+foodDefault.getMaxCnt(), Toast.LENGTH_SHORT).show();
								}else{
									
									foodCurrent.setPcount(countNew+"");
									adapter.notifyDataSetChanged();
									

								}
							}else{
								if(ValueUtil.isNotEmpty(foodCurrent.getMaxCnt()) && radioValue > Integer.parseInt(foodCurrent.getMaxCnt()) ){
									Toast.makeText(ChineseEatables.this, "该份菜品最多可选择 "+foodCurrent.getMaxCnt(), Toast.LENGTH_SHORT).show();
									return;
								}else if(adapter.getCountsAboutPcount() + radioValue > Integer.parseInt(foodDefault.getMaxCnt())){
									Toast.makeText(ChineseEatables.this, "已超过该组数量的上限 "+foodDefault.getMaxCnt(), Toast.LENGTH_SHORT).show();
									return;
								}
								foodCurrent.setSelected(true);
								foodCurrent.setPcount(radioValue+"");  
								adapter.notifyDataSetChanged();
								
							}
						}else if(radioValue == 0){
							if(!foodCurrent.isSelected()){
								//点击0时，对绿色的菜品是没有反应的
							}else{
								if(ValueUtil.isNotEmpty(foodCurrent.getMinCnt()) && Integer.parseInt(foodCurrent.getMinCnt()) > 0){
									foodCurrent.setSelected(true);
									foodCurrent.setPcount(foodCurrent.getMinCnt());
									adapter.notifyDataSetChanged();
									
								}else{
									foodCurrent.setSelected(false);
									foodCurrent.setPcount("");
									adapter.notifyDataSetChanged();
									
								}
							}
						}
					}
				});
				
				ImageView mImageView = new ImageView(ChineseEatables.this);
				mImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
				mImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
				
				linearTCDetails.addView(tcGroupTv);
				linearTCDetails.addView(tcGridView);
				linearTCDetails.addView(mImageView);
				
				
			}else if(singleLists.size() == 1 && singleLists.get(0).getMinCnt().equals(singleLists.get(0).getMaxCnt())){   //不可换购项
				Food food = singleLists.get(0);
				food.setTabNum(menu.getTableNum());
				food.setOrderId(menu.getMenuOrder());
				food.setMan(menu.getManCounts());
				food.setWoman(menu.getWomanCounts());
				food.setTpnum(tpnumflag+"");//rather special
//				guestLists.add(food);
				tempNotAlternativeLists.add(food);
				
			}
			
		}
		
		//如果该套餐明细只有不可换购项，没有可换购项，做判断，让套餐详情布局消失
		int countTcDetails = 0;
		for(EatableSubGridViewAdapter adapter:subGridViewAdapterLists){
			countTcDetails += adapter.getCount();
		}
		if(countTcDetails == 0){
			
			linearTcLayout.setVisibility(View.GONE);//让套餐详情布局消失
			
			//如果各项不可换购项有相同的菜品就合并
			List<Food> tempLists = new ArrayList<Food>();//存放去重后的food
			for(Food f:tempNotAlternativeLists){
				if(!tempLists.contains(f)){
					tempLists.add(f);
				}
			}
			
			//将相同的菜品合并数量
			int i=1;
			for(Food mFood:tempLists){
				int counts = 0;
				for(Food food:tempNotAlternativeLists) {
					if (mFood.getPcode().equals(food.getPcode())) {
						counts += Integer.parseInt(food.getPcount());
					}
				}
				mFood.setPKID(String.format("%d", ValueUtil.createPKID(this) + i));
				mFood.setPcount(counts + "");
				i++;
			}
			guestLists.add(curTpnumTCfood);
			guestLists.addAll(tempLists);
		}
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Bundle bundle = getTblStateBundle();
//		if(ValueUtil.isEmpty(bundle)){
			tableNum.setText(menu.getTableNum());
			orderNum.setText(menu.getMenuOrder());
			peopleNum.setText(ValueUtil.isEmpty(menu.getManCounts())?"0":menu.getManCounts());
			userNum.setText(SharedPreferencesUtils.getOperator(this));
//		}else{
//			tableNum.setText(bundle.getString("tableNum"));
//			orderNum.setText(bundle.getString("orderId"));
//            peopleNum.setText(ValueUtil.isNotEmpty(bundle.getString("manCs"))?bundle.getString("manCs"):"0");
//			userNum.setText(SharedPreferencesUtils.getOperator(this));
//
//			menu.setTableNum(bundle.getString("tableNum"));
//			menu.setMenuOrder(bundle.getString("orderId"));
//			menu.setManCounts(bundle.getString("manCs"));
//			menu.setWomanCounts(bundle.getString("womanCs"));
//			menu.setUserCode(SharedPreferencesUtils.getUserCode(this));
//		}
		
		// 初始化会员信息 中餐不用
		//VipMsg.iniVip(this, menu.getTableNum(), R.id.vipMsg_ImageView);
		
		grpLists = getDataManager(this).getAllDishClassList();//获得菜品类别
		dishLists =  getDataManager(this).getAllFoodList();//获取所有菜品

		if (guestLists.size()==0){
			List<Food> cacheLists = new OrderSaveUtil().queryHandle(this,menu.getTableNum(), menu.getMenuOrder());//获取缓存的菜品
			guestLists.clear();
			guestLists.addAll(cacheLists);
		}

		//如果已点菜品界面的数量发生改变，在点菜界面就应该更新
		for (Food food :dishLists){
			//菜品数量
			for (Food guestFood :guestLists){
				if (food.getPcode().equals(guestFood.getPcode())){
					food.setSelected(true);
					Float count1 =Float.parseFloat(food.getCounts() == null ? "0" : food.getCounts());
					food.setCounts(count1+Float.parseFloat(guestFood.getPcount())+"");
				}
			}
		}

		//更新左边菜品大类的数量
		for(Grptyp grptyp:grpLists){
			int countGrp = 0;
			for(Food f:dishLists){
				if(grptyp.getGrp().equals(f.getSortClass())){
					countGrp += Float.parseFloat(ValueUtil.isEmpty(f.getCounts())?"0":f.getCounts());
				}
			}
			grptyp.setQuantity(countGrp+"");
		}

		eatListAdapter = new EatableListAdapter(this, grpLists);
		listView.setAdapter(eatListAdapter);
		
		//GridView首次显示的时候，初始化默认的值
		setGridDefault();
		
		//实例化那些弹出的对话框
		showIfSaveDialog();
				
	}

	/**
	 * GridView首次显示的时候，初始化默认的值
	 */
	private void setGridDefault() {
				
		grp = grpLists.get(0).getGrp();
		
		updateDishClass(grp, dishLists);
		
		//更新界面上的左边的ListView
		eatListAdapter.setSelectedPosition(0);
		eatListAdapter.notifyDataSetInvalidated();
//        eatGridAdapter = new EatableGridAdapter(ChineseEatables.this, dishClassLists);
//        gridView.setAdapter(eatGridAdapter);
		accessSoldOutFace();

	}
	/**
	 * 对接美味不用等
	 * 调用接口，根据账单号查询出该账单号对应的所有菜品
	 * @param orderId 账单号
	 */
	private void getJNowStrByServer(String orderId){
		CList<Map<String, String>> params = new CList<Map<String,String>>();
		params.add("orderId",orderId);
		new ChineseServer().connect(this, ChineseConstants.DELICIOUS_NOT_ETC_BY_ORDERID,params,new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				if (ValueUtil.isNotEmpty(result)) {
					mLoadingDialog.dismiss();
					if (ValueUtil.isNotEmpty(result)) {
						Log.i("从美味不用等获取的菜品", result);
						try {
							JNowOrder jnowOrder = JsonUtil.getObject(result, JNowOrder.class);
							if (jnowOrder.getErrno() == 0) {
								List<JNowFood> jnowLists = jnowOrder.getOrder();
								for (JNowFood jnowFood : jnowLists) {
									for (Food food : dishLists) {
										if (jnowFood.getId().equals(food.getPcode())) {
											food.setPcount(jnowFood.getNum() + "");
											food.setCounts(food.getPcount());
											food.setSelected(true);
										}
									}
								}

								//给左边的ListView的数据源转换数值，一开始就初始化
								for (Grptyp grptyp : grpLists) {
									int grpCounts = 0;
									for (Food fd : dishLists) {
										if (fd.isSelected() && grptyp.getGrp().equals(fd.getSortClass())) {
											grpCounts += Integer.parseInt(fd.getPcount());
										}
									}
									grptyp.setQuantity(grpCounts + "");
								}
								setGridDefault();
								//将从美味不用等拉取的菜品存储到单例集合中
								for (Food fd : dishLists) {
									if (fd.isSelected()) {
										guestLists.add(fd);
									}
								}

							} else {
								ToastUtil.toast(ChineseEatables.this, R.string.jnow_params_error);//参数错误，请稍后再试
							}
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						ToastUtil.toast(ChineseEatables.this, R.string.net_error);
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
	 * 调用沽清菜品接口
	 */
	private void accessSoldOutFace(){
		new ChineseServer().connect(this, ChineseConstants.ESTIMATESFOODLIST,null,new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				if (ValueUtil.isNotEmpty(result)) {
					try {
						Map<String,Object> map=JsonUtil.getObject(result, Map.class);
						if(ValueUtil.isNotEmpty(map)) {
							Integer state = ((Map<String, Integer>) map.get("result")).get("state");
							if (ValueUtil.isNotEmpty(state) && state == 1) {
								List<Map<String, String>> msg = ((Map<String, List<Map<String, String>>>) map.get("result")).get("msg");
								for (int i = 0; i < dishLists.size(); i++) {
									Food food = dishLists.get(i);
									for (Map<String, String> imap : msg) {
										if (food.getPcode().equals(imap.get("ITCODE"))) {
											food.setSoldCnt(imap.get("CNT"));
											if (imap.get("SOLD") != null) {

												food.setSoldChangeCnt(imap.get("SOLD"));
											}
											dishLists.remove(food);
											dishLists.add(0, food);
											break;
										}
									}
								}

							}
						}
						if(ValueUtil.isNotEmpty(menu.getOrderIdJNow()) && menu.isJNowFirst()){
							//对接美味不用等,调用接口，根据账单号查询出该账单号对应的所有菜品
							SingleMenu.getMenuInstance().setJNowFirst(false);
							getJNowStrByServer(menu.getOrderIdJNow());
						}else{
							eatGridAdapter = new EatableGridAdapter(ChineseEatables.this, dishClassLists);
							gridView.setAdapter(eatGridAdapter);
						}

					} catch (IOException e) {
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
	 * 根据菜品类别，查询出该类别下的具体菜品集合
	 * //更新dishclass集合
	 * @param grp
	 * @param pDishLists
	 */
	private void updateDishClass(String grp, List<Food> pDishLists) {
		tempdishClassLists.clear();
		for(Food mFood:pDishLists){
			if(mFood.getSortClass().equals(grp)){
				tempdishClassLists.add(mFood);
			}
		}
		
		dishClassLists = tempdishClassLists;//将得到的集合赋值给gridview的数据源集合
	}

	private Bundle getTblStateBundle() {
		return this.getIntent().getBundleExtra("topBundle");
	}

	/**
	 * 初始化控件
	 */
	private void initView() {		
		
		mLoadingDialog = new LoadingDialogStyle(this, "数据加载中，请稍后...");
		mLoadingDialog.setCancelable(false);
		
		//顶部红色栏
		if (ChioceActivity.ispad) {
			tableNum = (TextView) this.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) this.findViewById(R.id.dishesact_tv_ordernum);
			peopleNum = (TextView) this.findViewById(R.id.dishesact_tv_people);
			userNum = (TextView) this.findViewById(R.id.dishesact_tv_usernum);
		}else {
			// TODO 
			menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
			menuinfo_imageView.setClickable(true);
			menuinfo_imageView.setOnClickListener(this);
			layout = LayoutInflater.from(ChineseEatables.this).inflate(R.layout.chinese_menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			peopleNum = (TextView) layout.findViewById(R.id.dishesact_tv_people);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			topLinearLayout = (LinearLayout) findViewById(R.id.topLinearLyout);
		}


		radioOne = (TextView) findViewById(R.id.toptitle_rb_one);
		radioTwo = (TextView) findViewById(R.id.toptitle_rb_two);
		radioThree = (TextView) findViewById(R.id.toptitle_rb_three);
		radioFour = (TextView) findViewById(R.id.toptitle_rb_four);
		radioFive = (TextView) findViewById(R.id.toptitle_rb_five);
		radioSix = (TextView) findViewById(R.id.toptitle_rb_six);
		radioSeven = (TextView) findViewById(R.id.toptitle_rb_seven);
		radioEight = (TextView) findViewById(R.id.toptitle_rb_eight);
		radioNight = (TextView) findViewById(R.id.toptitle_rb_night);
		radioZero = (TextView) findViewById(R.id.toptitle_rb_zero);
		radioX = (TextView) findViewById(R.id.toptitle_rb_X);
		radioValueShow = (TextView) findViewById(R.id.toptitle_rb_newvalue);
		etSearchView =  (ClearEditText) this.findViewById(R.id.et_searchView);
		btnSearchView = (Button) this.findViewById(R.id.btn_searchView);
        btnSearchCancel = (TextView) this.findViewById(R.id.btn_searchCancel);

		listView =   (ListView) this.findViewById(R.id.eatables_listView);
		gridView =  (GridView) this.findViewById(R.id.eatables_gridView);
		if (!ChioceActivity.ispad) {
			gridView.setNumColumns(3);
		}
		
		linearTcLayout = (LinearLayout) this.findViewById(R.id.eatable_tc_layout);
		linearTCDetails = (LinearLayout) this.findViewById(R.id.eatable_linear_tcdetails);
		tcBtnCertain = (Button) this.findViewById(R.id.eatable_tcdetail_btn_certain);
		tcBtnCancel = (Button) this.findViewById(R.id.eatable_tcdetail_btn_cancel);
//		tcGroupNumShow = (TextView) this.findViewById(R.id.eatable_tc_groupnumber);
		
		remark = (Button) this.findViewById(R.id.eatable_btn_remark);

		yiDian = (Button) this.findViewById(R.id.eatable_btn_finishdish);
		back = (Button) this.findViewById(R.id.eatable_btn_back);
		if (SingleMenu.getMenuInstance().getPermissionList()!=null) {
			for (Map<String, String> map : SingleMenu.getMenuInstance().getPermissionList()) {
				if (map.get("CODE").equals("21001") && map.get("ISSHOW").equals("0")) {
					remark.setVisibility(View.GONE);
					break;
				}
			}
		}
	}

	/**
	 * 点击事件
	 */
	private View.OnClickListener onClickListener = new OnClickListener() {
		
		private void dataClicked(String dataStr){
			String valueStr = radioValueShow.getText().toString();
			
			if(valueStr.length() == 2){
				radioValueShow.setText("1");
				return;
			}
			
			if(valueStr.equals("1")){
				//再判断是默认的1，还是点的数字1
				if(digitalFlag == -1){
					radioValueShow.setText(dataStr);
				}else if(digitalFlag == 0){
					radioValueShow.setText("1" + dataStr);
					digitalFlag = -1;
				}
			}else{
				if(valueStr.equals("0")){
					radioValueShow.setText(dataStr);
				}else{
					radioValueShow.setText(valueStr +dataStr);
				}
			}
			
		}
		
		@Override
		public void onClick(View v) {
			
			//当digitalFlag = 0时，表示已经点了数字1；当digitalFlag = -1时，表示没有点击数字1
			switch (v.getId()) {
			case R.id.toptitle_rb_one:
				
				if(radioValueShow.getText().toString().length() == 2){
					radioValueShow.setText("1");
					return;
				}
				
				String valueStr = radioValueShow.getText().toString();
				if(valueStr.equals("1")){
					//再判断是默认的1，还是点的数字1
					if(digitalFlag == -1){
						radioValueShow.setText("1");
						digitalFlag = 0;
					}else if(digitalFlag == 0){
						radioValueShow.setText("11");
						digitalFlag = -1;
					}
				}else{
					if(valueStr.equals("0")){
						radioValueShow.setText("1");
					}else{
						radioValueShow.setText(valueStr +"1");
					}
				}
				
				break;

			case R.id.toptitle_rb_two:
				dataClicked("2");
				break;
			case R.id.toptitle_rb_three:
				dataClicked("3");
				
				break;
			case R.id.toptitle_rb_four:
				
				dataClicked("4");
				break;
			case R.id.toptitle_rb_five:
				dataClicked("5");
				break;
			case R.id.toptitle_rb_six:
				dataClicked("6");
				break;
			case R.id.toptitle_rb_seven:
				dataClicked("7");
				break;
			case R.id.toptitle_rb_eight:
				dataClicked("8");
				break;
			case R.id.toptitle_rb_night:
				dataClicked("9");
				break;
				
			case R.id.toptitle_rb_zero:
				if(radioValueShow.getText().toString().length() == 2){
					radioValueShow.setText("1");
					return;
				}
				
				String valueStr0 = radioValueShow.getText().toString();
				if(valueStr0.equals("1")){
					if(digitalFlag == -1){
						radioValueShow.setText("0");
					}else if(digitalFlag == 0){
						radioValueShow.setText("10");
						digitalFlag = -1;
					}
				}else{
					if(valueStr0.equals("0")){
						radioValueShow.setText("0");
					}else{
						radioValueShow.setText(valueStr0 +"0");
					}
					
				}
				break;
				
			case R.id.toptitle_rb_X:
				radioValueShow.setText("1");
				digitalFlag = -1;
				break;
			
			case R.id.toptitle_rb_newvalue:
				break;

			default:
				break;
			}
			
		}
	};
	
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_searchView:
			btnSearchView.setVisibility(View.GONE);
			etSearchView.setVisibility(View.VISIBLE);
			etSearchView.requestFocus();
			//弹出键盘
			InputMethodUtils.toggleSoftKeyboard(this);
			break;
        case R.id.btn_searchCancel:
            btnSearchView.setVisibility(View.VISIBLE);
            etSearchView.setVisibility(View.GONE);
            etSearchView.clearFocus();
            etSearchView.setText("");
            break;
		case R.id.eatable_btn_remark:
			showSingleFujiaDia(tempFujiaFood);
			break;
		case R.id.eatable_btn_finishdish:
			if(linearTcLayout.getVisibility() == View.VISIBLE){
				Toast.makeText(this, R.string.tc_not_dismiss_other, Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(ChineseEatables.this,ChineseYiDianDishActivity.class);
			startActivity(intent);
			break;
			
		case R.id.eatable_btn_back:
			//如果客人已点了菜品，点击返回时，弹出提示框，提示是否保存 
			if(guestLists.size() == 0||!TsData.isSave){
				this.finish();
			}else{
				//套餐还未选择完毕
				if(linearTcLayout.getVisibility() == View.VISIBLE){
					Toast.makeText(this, R.string.eatable_tc_tcnofinish, Toast.LENGTH_SHORT).show();
					return;
				}
				showIfSaveDialog().show();
			}
			break;
		case R.id.eatable_tcdetail_btn_certain:
			
			//一个套餐明细也不选，是不允许的
			int tcDetailsCount = 0;
			for(EatableSubGridViewAdapter subAadapter:subGridViewAdapterLists){
				tcDetailsCount += subAadapter.getCountsAboutPcount();
			}
			
			if(tcDetailsCount == 0){
				Toast.makeText(this, R.string.eatable_tc_servelnotxuan, Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			//如果有一组套餐明细所选的总数量不满足该组所对应的mincnt
			for(EatableSubGridViewAdapter adapter:subGridViewAdapterLists){
				if(!adapter.isSatisfyMincntDefault()){
					Toast.makeText(this, R.string.eatable_tc_servelnotxuan, Toast.LENGTH_SHORT).show();
					return;
				}
			}
			
//			//将套餐添加进集合中
//			guestLists.add(curTpnumTCfood);
//			guestLists.addAll(tempNotAlternativeLists);
//			for(EatableSubGridViewAdapter subAdapter:subGridViewAdapterLists){
//				List<Food> selectedLists = subAdapter.getSelectedFoodLists();
//				guestLists.addAll(selectedLists);
//			}
			
			//如果所点的这个套餐有相同的菜品明细，就合并
			List<Food> currentTcLists = new ArrayList<Food>();
			
			currentTcLists.addAll(tempNotAlternativeLists);//添加不可换购项
			for(EatableSubGridViewAdapter subAdapter:subGridViewAdapterLists){
				List<Food> selectedLists = subAdapter.getSelectedFoodLists();
				currentTcLists.addAll(selectedLists);//添加可换购项
			}
			
			//去除重复元素后的集合
			List<Food> removeDuplicateLists = new ArrayList<Food>();
            int i=1;
            String uuid= ValueUtil.createUUID();
			for(Food mFood:currentTcLists){
				if(!removeDuplicateLists.contains(mFood)){
                    mFood.setFoodIndex(i++);
					mFood.setPKID(String.format("%d", Integer.parseInt(ValueUtil.createPKID(this)) + i));
                    mFood.setFoodMark(tpnumflag+1);
					removeDuplicateLists.add(mFood);
				}
			}

			
			for(Food food:removeDuplicateLists){
				int countFood = 0;
				for(Food f:currentTcLists){
					if(food.getPcode().equals(f.getPcode())){
						countFood += Integer.parseInt(f.getPcount());
					}
				}
				food.setPcount(countFood+"");
			}
			
			guestLists.add(curTpnumTCfood);
			guestLists.addAll(removeDuplicateLists);
			
			
			getTcPriceByMode(curTpnumTCfood, guestLists,dishLists);//rather special套餐点完了，就计算该套餐的计价方式
			linearTCDetails.removeAllViews();
			linearTcLayout.setVisibility(View.GONE);
			
			//更新左边菜品大类的数量
			for(Grptyp grpTyp:grpLists){
				if(grpTyp.getGrp().equals(grp)){
					grpTyp.setQuantity(eatGridAdapter.getItemCounts()+"");
					eatListAdapter.notifyDataSetChanged();
					break;
				}
			}
			
			tpnumflag = 0;//将套餐编号设为0
			break;
			
		case R.id.eatable_tcdetail_btn_cancel:
			linearTCDetails.removeAllViews();
			linearTcLayout.setVisibility(View.GONE);
			
			tempNotAlternativeLists = null;
			
			for(Food food:dishClassLists){
				if(food.getPcode().equals(curTpnumTCfood.getPcode())){
					//将数量减1
					int tcCurrentCounts = Integer.parseInt(food.getCounts());
					tcCurrentCounts -= 1;
					if(tcCurrentCounts == 0){
						food.setSelected(false);
						food.setCounts("");
					}
					food.setCounts(tcCurrentCounts+"");
//					guestLists.remove(food);
					break;
				}
			}
			
			eatGridAdapter.notifyDataSetChanged();
			tpnumflag = 0;//将套餐编号设为0
			
			break;
			
		case R.id.orderinfo_ImageView:
			// TODO   
						View layout = null ;
						layout = LayoutInflater.from(ChineseEatables.this).inflate(R.layout.chinese_menu_info, null);
						tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
						orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
						peopleNum = (TextView) layout.findViewById(R.id.dishesact_tv_people);
						userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
						initData();
						AlertDialog tipdialog = new AlertDialog.Builder(ChineseEatables.this, R.style.Dialog_tip).setView(layout).create();
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
	 * 弹出对话框，提示用户是否保存已点菜品
	 * @return
	 */
	private Dialog showIfSaveDialog() {
		String [] itemSave = {this.getString(R.string.yes),this.getString(R.string.no),this.getString(R.string.cancle)};
		AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.edittext_dialog);
		builder.setTitle(R.string.whether_save_food);
		ListAdapter diaAdapter=new ArrayAdapter<String>(this, R.layout.dia_ifsave_item_layout, R.id.dia_ifsave_item_tv, itemSave);
		builder.setAdapter(diaAdapter,  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					//实体类food中的字段send代表是已发送菜品，还是暂存   ，为其赋值             已发送1          暂存0
					for(int i = 0;i<guestLists.size();i++){
						guestLists.get(i).setSend("0");
					}
					boolean insertSuccess= new OrderSaveUtil().insertHandle(ChineseEatables.this,menu.getTableNum(),menu.getMenuOrder(),guestLists);
					if(insertSuccess){
						Toast.makeText(ChineseEatables.this, R.string.save_success, Toast.LENGTH_SHORT).show();
					}else{
						Log.e("所点菜保存到数据库表order_dishes", "save fail");
					}
					guestLists.clear();
					Intent intent = new Intent(ChineseEatables.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					break;
					
				case 1:
					guestLists.clear();
					ChineseEatables.this.finish();
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
	 * 当点击返回键的时候，如果已点了菜，就弹框提示是否保存到本地数据库
	 * @param guestLists
	 */
	private boolean saveToAllCheck(List<Food> guestLists) {
		
		return getDataManager(this).circleInsertAllCheck(guestLists);
	}

	/**
	 * 信息检索用的
	 */
	private TextWatcher watcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				matchLists.clear();
				String changingStr = etSearchView.getText().toString().trim();
				if(ValueUtil.isNotEmpty(changingStr)){
					//设置左边的菜品大类不可点击
					listView.setEnabled(false);
					
					for(Food dishes:dishLists){
						String currentStr = dishes.getPcname();
						String numEncode = dishes.getPcode();
						if(currentStr.indexOf(changingStr)!=-1 || ConvertPinyin.convertJianPin(currentStr).indexOf(changingStr) !=-1 ||ConvertPinyin.convertQuanPin(currentStr).indexOf(changingStr)!=-1 || numEncode.indexOf(changingStr)!=-1){
							matchLists.add(dishes);
						}
					}
					if(eatGridAdapter!=null){
						dishClassLists = matchLists;//将得到的集合赋值给gridview的数据源集合
						for(Food f:dishClassLists){
							Log.d("-----------", f.getPcname());
						}
						eatGridAdapter.setEatableGridSource(dishClassLists);
						eatGridAdapter.notifyDataSetChanged();
					}
				}else{
					dishClassLists = tempdishClassLists;//将得到的集合赋值给gridview的数据源集合
					eatGridAdapter.setEatableGridSource(dishClassLists);
					eatGridAdapter.notifyDataSetChanged();
					
					//设置左边的菜品大类可点击
					listView.setEnabled(true);
					
					//隐藏软键盘
					InputMethodUtils.toggleSoftKeyboard(ChineseEatables.this);
				}
			}
		};
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			
			if(linearTcLayout.getVisibility() == View.VISIBLE){
				Toast.makeText(this, "套餐还未选择完毕！", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
				if(guestLists.size() == 0){  //如果客人没点一道菜，单例集合长度为0，就不弹框
					this.finish();
				}else{
					showIfSaveDialog().show();
				}
			}
			return super.onKeyDown(keyCode, event);
		}


		/**
		 * 显示单品附加项的对话框
		 * @param pFood
		 */
		private void showSingleFujiaDia(final Food pFood){
			
			if(pFood == null){
				Toast.makeText(ChineseEatables.this, "你还没有选择菜品", Toast.LENGTH_SHORT).show();
				return;
			}else if(pFood.getPcode().equals(pFood.getTpcode())){//套餐是不能添加附加项的
				Toast.makeText(ChineseEatables.this, "套餐不能添加附加项", Toast.LENGTH_SHORT).show();
				return;
			}
			
			//PAD2 已改
			final List<Addition> additionLists =  getDataManager(ChineseEatables.this).getAllFujiaListByPcode(pFood.getPcode());//查询出所有附加项 ;
			
			View sinleLayout = LayoutInflater.from(ChineseEatables.this).inflate(R.layout.dia_singlefujia, null);

            /**
             * 搜索按钮
             */
			final Button btnSearch = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_searchView);
			final cn.com.choicesoft.view.ClearEditText etSearch = (ClearEditText) sinleLayout.findViewById(R.id.dia_singlefujia_et_searchView);

			ListView singleFujiaLV = (ListView) sinleLayout.findViewById(R.id.dia_singlefujia_listView);
			final SingleFujiaAdapter singleFujiaAdapter = new SingleFujiaAdapter(ChineseEatables.this, additionLists);
			singleFujiaLV.setAdapter(singleFujiaAdapter);
            //附加项搜索事件
            btnSearch.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    btnSearch.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();

                    //弹出键盘
                    InputMethodUtils.toggleSoftKeyboard(ChineseEatables.this);
                    etSearch.addTextChangedListener(new FujiaWatcher(etSearch, additionLists, singleFujiaAdapter));
                }
            });
            //附加项选择事件
			singleFujiaLV.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					SingleFujiaAdapter.SingleFujiaHolder holder = (SingleFujiaAdapter.SingleFujiaHolder) view.getTag();
					holder.mCheckBox.toggle();
					SingleFujiaAdapter adapter = (SingleFujiaAdapter) parent.getAdapter();
					adapter.toggleChecked(position);
				}
			});

			
			final EditText selfFujiaAddEdit = (EditText) sinleLayout.findViewById(R.id.dia_singlefujia_et_selffujia);
            selfFujiaAddEdit.addTextChangedListener(new TextWatcher() {
                String strBefore;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    strBefore=s.toString();
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equals(strBefore)){
                        String str=s.toString().replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
                        selfFujiaAddEdit.setText(str);
                        selfFujiaAddEdit.setSelection(str.length());
                    }
                }
            });
            /**
             * 确定按钮
             */
			Button diaBtnCertain = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_certain);
			diaBtnCertain.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//将客人所选中的附加项添加到对象中
                    if(pFood==null){
                        return;
                    }
                    StringBuilder buildPrice = new StringBuilder();//编码
                    StringBuilder buildDes = new StringBuilder();//名称
                    List<String> fujia=new ArrayList<String>();
					
					String selfFujiaStr = selfFujiaAddEdit.getText().toString();//得到自定义的附加项的值
					if(ValueUtil.isNotEmpty(selfFujiaStr)){
                        buildPrice.append("0").append("!");
                        buildDes.append(selfFujiaStr).append("!");
						//将自定义附加项的值添加进去
                        fujia.add(selfFujiaStr+"|0|");
					}
					
					for(Addition addition:additionLists){
						if(addition.isSelected()){
                            buildPrice.append(addition.getFoodFuJia_checked()).append("!");
                            buildDes.append(addition.getFoodFuJia_des()).append("!");
							//buildFujiaCode.append(ValueUtil.isEmpty(addition.getFoodFuJia_Id())?"":addition.getFoodFuJia_Id()).append("!");
                            /*buildFujia.append(ValueUtil.isEmpty(addition.getFoodFuJia_des())?"":addition.getFoodFuJia_des()).append("|")
                            .append(ValueUtil.isEmpty(addition.getFoodFuJia_checked())?"":addition.getFoodFuJia_checked()).append("|");*/
                            String des=ValueUtil.isEmpty(addition.getFoodFuJia_des())?"":addition.getFoodFuJia_des();
                            String price=ValueUtil.isEmpty(addition.getFoodFuJia_checked())?"":addition.getFoodFuJia_checked();
                            fujia.add(des+"|"+price+"|");
						}
					}
					pFood.setChineseFujia(fujia);
                    pFood.setFujianame(buildDes.toString());
                    pFood.setFujiaprice(buildPrice.toString());
					tempFujiaFood = null;//将临时的那个为附加项准备的food赋值为空
					
					selfFujiaAddEdit.setText("");
					dialog.dismiss();
                    //隐藏软键盘
                    InputMethodUtils.hideSoftKeyboard(ChineseEatables.this,selfFujiaAddEdit);
				}
			});
			
			Button diaBtnCancel = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_cancel);
			diaBtnCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					//隐藏软键盘
					InputMethodUtils.hideSoftKeyboard(ChineseEatables.this,selfFujiaAddEdit);
				}
			});
			
			AlertDialog.Builder builder = new AlertDialog.Builder(ChineseEatables.this,R.style.edittext_dialog);
			builder.setView(sinleLayout);

			dialog = builder.create();
			dialog.show();
			
		}

		
		
		
		/**
		 * 套餐的三种计价方式
		 * @param pLists  singleton集合
		 * @param wholeFoodLists    所有菜品大集合stable
		 */
		public void getTcPriceByMode(Food tcFood,List<Food> pLists,List<Food> wholeFoodLists){
			int tcMode = Integer.valueOf(tcFood.getTcMoneyMode());
			if(tcMode == 1){
				double tcDetailPrices = 0.00;
				for(Food food:pLists){
					if(food.isIstc() && !food.getTpcode().equals(food.getPcode()) && food.getTpcode().equals(tcFood.getPcode()) && food.getTpnum().equals(tcFood.getTpnum())){
						double priceItem = Double.parseDouble(food.getPrice()) * Double.parseDouble(food.getPcount());
						tcDetailPrices += Double.parseDouble(ValueUtil.setScale(priceItem, 2, 5));
					}
				}//得到套餐明细菜品的价格相加后的和
                double rate=0;
                if(tcDetailPrices!=0) {
                    rate = ValueUtil.isNaNofDouble(tcFood.getPrice()) / tcDetailPrices;//得到比率
                }
				 for(Food f:pLists){
					 if(f.isIstc() && !f.getTpcode().equals(f.getPcode()) && f.getTpcode().equals(tcFood.getPcode()) && tcFood.getTpnum().equals(f.getTpnum())){
						 
						 double newPrice = ValueUtil.isNaNofDouble(f.getPrice()) * ValueUtil.isNaNofDouble(f.getPcount()) * rate;
						 f.setPrice(ValueUtil.setScale(newPrice, 2, 5));
					 }
				 }

				 //新算出的价格再相加，如果不等于套餐价格，就重新计算出第一项的价格
				 List<Food> tcOnlys = new ArrayList<Food>();//这个新集合只存放针对于tcFood的明细菜品
				 
				 
				 for(Food mFood:pLists){
					
					 if(mFood.isIstc() && !mFood.getPcode().equals(mFood.getTpcode()) && tcFood.getPcode().equals(mFood.getTpcode()) && mFood.getTpnum().equals(tcFood.getTpnum())){
						 tcOnlys.add(mFood);
					 }
				 }
				  
				 double newallPrice = 0.00;//得到新的总价
				 for(Food fd:tcOnlys){
					 newallPrice += Double.valueOf(fd.getPrice()); 
				 }

				 if(newallPrice != Double.valueOf(tcFood.getPrice())){
					 if(tcOnlys.size() != 0){   //必须加判断，如果所有的MINCNT都等于0，就报数组越界
						 double allPriceButFirst = newallPrice - Double.parseDouble(tcOnlys.get(0).getPrice());
						 double a1 = Double.valueOf(tcFood.getPrice()) - allPriceButFirst;
						 
						 tcOnlys.get(0).setPrice(ValueUtil.setScale(a1, 2, 5));
					 }
				 }
				 				 
				 tcFood.setPrice(ValueUtil.setScale(Double.valueOf(tcFood.getPrice()), 2, 5));
			}else if(tcMode == 2){
				double tcDetailsPrice = 0.00;
				for(Food mFood:pLists){
					if(mFood.isIstc() && !mFood.getPcode().equals(mFood.getTpcode()) && mFood.getTpcode().equals(tcFood.getPcode()) && mFood.getTpnum().equals(tcFood.getTpnum())){
						for(Food f:wholeFoodLists){
							if(mFood.getPcode().equals(f.getPcode())){
								//rather special
								Double itemPrices = Double.parseDouble(f.getPrice()) * Double.parseDouble(mFood.getPcount());
								mFood.setPrice(ValueUtil.setScale(itemPrices, 2, 5));
								tcDetailsPrice += Double.valueOf(mFood.getPrice());
							}
						}
					}
				}//得到套餐明细菜品的价格相加后的和
				
				tcFood.setPrice(ValueUtil.setScale(tcDetailsPrice, 2, 5));
			}else if(tcMode == 3){ 
				double tcDetailsPrice = 0.00;
				for(Food food:pLists){
					if(food.isIstc() && !food.getTpcode().equals(food.getPcode()) && food.getTpcode().equals(tcFood.getPcode()) && food.getTpnum().equals(tcFood.getTpnum())){
						food.setPrice(ValueUtil.setScale(Double.valueOf(food.getPrice()), 2, 5));
						double itemPrices = Double.parseDouble(food.getPrice()) * Double.parseDouble(food.getPcount());
						tcDetailsPrice += Double.valueOf(itemPrices);
					}
				}//得到套餐明细菜品的价格相加后的和
				
				double tcInitialPrice = Double.valueOf(tcFood.getPrice());//套餐原有价格
				
				if(tcDetailsPrice >= tcInitialPrice){
					tcFood.setPrice(ValueUtil.setScale(tcDetailsPrice, 2, 5));
				}else{
					double rate = tcInitialPrice/tcDetailsPrice;//得到比率
					
					for(Food f:pLists){
						if(f.isIstc() && !f.getTpcode().equals(f.getPcode()) && f.getTpcode().equals(tcFood.getPcode()) && f.getTpnum().equals(tcFood.getTpnum())){
							double newPrice = Double.parseDouble(f.getPrice()) * Double.parseDouble(f.getPcount())* rate;
							f.setPrice(ValueUtil.setScale(newPrice, 2, 5));
						}
					}
					
					//如果新算出的价格相加，仍然不等于套餐原有的价格，就.......
					 List<Food> tcOnlys = new ArrayList<Food>();//这个新集合只存放针对于tcFood的明细菜品
					 for(Food mFood:pLists){
						 if(mFood.isIstc() && !mFood.getPcode().equals(mFood.getTpcode())&& tcFood.getPcode().equals(mFood.getTpcode()) && mFood.getTpnum().equals(tcFood.getTpnum())){
							 tcOnlys.add(mFood);
						 }
					 }
					 
					 double newConformPrice = 0.00;//得到新的总价
					 for(Food fd:tcOnlys){
						 newConformPrice += Double.valueOf(fd.getPrice()); 
					 }
					 
					if(newConformPrice != tcInitialPrice){
						if(tcOnlys.size() != 0){//必须加判断，如果所有的MINCNT都等于0，就报数组越界
							
							double allPriceButFirst = newConformPrice - Double.parseDouble(tcOnlys.get(0).getPrice());
							double a1 = Double.valueOf(tcFood.getPrice()) - allPriceButFirst;
							
							tcOnlys.get(0).setPrice(ValueUtil.setScale(a1, 2, 5));
						}   
					}
					
					tcFood.setPrice(ValueUtil.setScale(tcInitialPrice, 2, 5));
				}
				
			}
		}
    /**
     * 附加项搜索用的
     */
    private class FujiaWatcher implements TextWatcher{

        private EditText fujiaEdit;
        private List<Addition> additionLists;
        private SingleFujiaAdapter singleFujiaAdapter;


        public FujiaWatcher(EditText fujiaEdit,List<Addition> additionLists,SingleFujiaAdapter singleFujiaAdapter) {
            super();
            this.fujiaEdit = fujiaEdit;
            this.additionLists = additionLists;
            this.singleFujiaAdapter = singleFujiaAdapter;
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {
            fujiamatchLists.clear();
            String changingFujiaStr = fujiaEdit.getText().toString().trim();
            if(ValueUtil.isNotEmpty(changingFujiaStr)){
                for(Addition addition:additionLists){
                    String currentStr = addition.getInit();//根据数据库表中的init字段
                    String currentNameStr = addition.getFoodFuJia_des();//根据数据库表中的名称字段
                    if(currentStr.indexOf(ValueUtil.exChangeToLower(changingFujiaStr)) != -1 || currentStr.indexOf(ValueUtil.exChangeToUpper(changingFujiaStr)) != -1 || currentNameStr.indexOf(changingFujiaStr)!=-1){
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
//					setItemFood();
            }
        }

    }
    //=====================================点菜================================
   //点菜
    public void foodOrder(final Food foodItem,View view){
        int radioValue = Integer.parseInt(radioValueShow.getText().toString());//得到数字标题栏最右边的那个值
        if(!foodItem.isIstc()) {
			tempFujiaFood = foodItem;//为哪一项菜品添加附加项做准备,针对于普通单品
		}
		//当为0的时候直接删除
		if (radioValue==0){
			delectFood(foodItem);
		}else {
			foodSold(foodItem, view);
		}
    }
	public void delectFood(Food foodItem){
		radioValueShow.setText("1");//将上面的数字标题栏还原
		digitalFlag = -1;
		foodItem.setSelected(false);//按钮选择
		foodItem.setCounts("");
		foodItem.setPcount("");

		List<Food> tempUnitLists = new ArrayList<Food>();
		for(Food food:guestLists){
			if(food.getPcode().equals(foodItem.getPcode())){
				tempUnitLists.add(food);
			}
		}
		linearTCDetails.removeAllViews();
		linearTcLayout.setVisibility(View.GONE);
		guestLists.removeAll(tempUnitLists);
		eatGridAdapter.notifyDataSetChanged();
		//更新左边菜品大类的数量
		for(Grptyp grpTyp:grpLists){
			if(grpTyp.getGrp().equals(grp)){
				grpTyp.setQuantity(eatGridAdapter.getItemCounts()+"");
				break;
			}
		}
		eatListAdapter.notifyDataSetChanged();
	}
	//菜品估清
	public void foodSold(final Food foodItem,View view){
		if (foodItem.getSoldCnt()!=null){
			Double radioValue = Double.parseDouble(radioValueShow.getText().toString())+Double.parseDouble(foodItem.getCounts()==null?"0.0":foodItem.getCounts());
			if (radioValue>Double.parseDouble(foodItem.getSoldCnt())) {
				Toast.makeText(ChineseEatables.this, R.string.food_solding, Toast.LENGTH_SHORT).show();
				return;
			}
		}
		foodIstc(foodItem,view);
	}
	//是否套餐
	public void foodIstc(final Food foodItem,View view){
		if(foodItem.isIstc()) {
			radioValueShow.setText("1");//将上面的数字标题栏还原
			digitalFlag = -1;

			if (linearTcLayout.getVisibility() == View.VISIBLE) {
				Toast.makeText(ChineseEatables.this, R.string.tc_not_dismiss, Toast.LENGTH_SHORT).show();
				return;
			}
			if (!foodItem.isSelected()) {
				foodItem.setSelected(true);
				foodItem.setCounts("1");
				foodItem.setPKID(ValueUtil.createPKID(this));
				foodItem.setCLASS("N");
			} else {
				int counts = Integer.parseInt(foodItem.getCounts());
				counts += 1;
				foodItem.setCounts(counts + "");
			}
			eatGridAdapter.notifyDataSetChanged();
			tcCallBack(foodItem);//对套餐明细进行操作
		}else {
			foodItem.setPKID( ValueUtil.createPKID(this));
			foodPlusd(foodItem, view);
		}
	}
	//第二单位
	public void foodPlusd(final Food foodItem,View view){
		if(ValueUtil.isNotEmpty(foodItem.getPlusd()) && foodItem.getPlusd()!=0)
		{
			foodItem.setWeightflg("2");
			foodUnits(foodItem,view);
		}else {
			foodUnits(foodItem,view);
		}
	}
	//多单位
	public void foodUnits(final Food foodItem,View view){
		Log.i("food",foodItem.getUnitMap().toString());

		if (foodItem.getUnitMap() != null && foodItem.getUnitMap().size() > 1) {
			if (null!=SingleMenu.getMenuInstance().getPriceKay())
			{
				Unit unit=foodItem.getUnitMap().get("unit"+(SingleMenu.getMenuInstance().getPriceKay()));
				foodItem.setUnit(unit.getUnitName());
				foodItem.setPrice(unit.getUnitPrice());
				foodDis(foodItem);
			}else {
				IResult<Unit> ir = new IResult<Unit>() {
					@Override
					public void result(Unit unit) {
						foodItem.setUnit(unit.getUnitName());
						foodItem.setUnitCode(unit.getUnitCode());
						foodItem.setPrice(unit.getUnitPrice());
						foodDis(foodItem);
					}
				};
				new MuchUntiView(ir, ChineseEatables.this, view).showView(foodItem);
			}
		} else {
			foodDis(foodItem);
		}

	}
    //保存菜品
    public void foodDis(Food foodItem){
		int radioValue = Integer.parseInt(radioValueShow.getText().toString());
		Boolean foodTag = false;
		if (foodItem.isSelected()){
			for (Food f : guestLists){
				if (foodItem.getPcode().equals(f.getPcode())){
					if (foodItem.getFujianame()==null||foodItem.getFujianame().equals(f.getFujianame())){
						if (foodItem.getUnitCode()==null||foodItem.getUnitCode().equals(f.getUnitCode())){
							foodItem.setCounts(radioValue + ValueUtil.isNaNofInteger(foodItem.getCounts()) + "");
							f.setPcount(radioValue + ValueUtil.isNaNofInteger(f.getPcount()) + "");
							foodTag=true;
							break;
						}
					}

				}
			}
		}
		if (!foodTag){
			foodItem.setSelected(true);
			foodItem.setCounts(radioValue+ValueUtil.isNaNofInteger(foodItem.getCounts())+"");
			foodItem.setPcount(radioValue+"");

			foodItem.setTabNum(menu.getTableNum());
			foodItem.setOrderId(menu.getMenuOrder());
			foodItem.setMan(menu.getManCounts());
			foodItem.setWoman(menu.getWomanCounts());
			foodItem.setTpnum("0");

			tempFujiaFood = new ValueCopy<Food>().objectCopy(foodItem);
			foodItem.setFujiacode(null);
			foodItem.setFujiacount(null);
			foodItem.setFujianame(null);
			foodItem.setFujiaprice(null);

			tempFujiaFood.setCounts(radioValue+"");
			guestLists.add(tempFujiaFood);
		}
		radioValueShow.setText("1");
		digitalFlag = -1;
		eatGridAdapter.notifyDataSetChanged();

		for(Grptyp grpTyp:grpLists){
			if(grpTyp.getGrp().equals(grp)){
				grpTyp.setQuantity(eatGridAdapter.getItemCounts()+"");
				eatListAdapter.notifyDataSetChanged();
				break;
			}
		}


	}

    //=====================================================================
}
