package cn.com.choicesoft.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.choicesoft.R;
import cn.com.choicesoft.adapter.EatableGridAdapter;
import cn.com.choicesoft.adapter.EatableListAdapter;
import cn.com.choicesoft.adapter.EatableSubGridViewAdapter;
import cn.com.choicesoft.bean.*;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.singletaiwei.YiDianDishActivity;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.*;
import cn.com.choicesoft.view.FloatWindowSmallView.SmallWindowClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;

/**
 * 点菜界面
 */
public class Eatables extends BaseActivity implements OnClickListener{

	private ListView listView;//左边菜品大类对应的控件
	private EatableListAdapter eatListAdapter;//左边菜品大类对应的控件的适配器

	private GridView gridView;//右边具体菜品对应的控件
	private EatableGridAdapter eatGridAdapter;//右边具体菜品对应的控件的适配器
	
	private LoadingDialog mLoadingDialog;
	private View layout;
	
	private TextView tableNumTv;//台位编码
	private TextView orderNumTv;//账单号
	private TextView manNumTv;//男人数
	private TextView womanNumTv;//女人数
	private TextView userNumTv;//台位编码
	private ImageView menuinfo_imageView;
	
	private TextView radioOne, radioTwo, radioThree, radioFour, radioFive,radioSix, radioSeven, radioEight, radioNight,radioZero,radioX,radioValueShow ;

	private ClearEditText etSearchView;//搜索框
	private Button btnSearchView;//搜索按钮
	private LinearLayout topLinearLayout;

	private Button remarkBtn;//备注
	private Button yiDianBtn;//已点菜
	private Button backBtn;//返回
    private Button fujiaChanPin;//附加产品

	private LinearLayout linearTCDetails;//套餐详情的内布局
	private LinearLayout linearTcLayout;//套餐详情的整个布局
	private Button tcBtnCertain;//套餐详情的确认按钮
	private Button tcBtnCancel;//套餐详情的取消按钮
	
	private Bundle bundle;

	private List<Food> dishLists = null;// 存放具体菜品
	private List<CurrentUnit> unitCurLists = null;//存放第二单位的集合
	private List<Grptyp> grpLists;//存放菜品大类集合

	private List<Food> dishClassLists = null;//每个菜品大类对应着一些具体菜品的集合
	private List<Food> tempdishClassLists = new ArrayList<Food>();//每个菜品大类对应着一些具体菜品的集合            临时的
	private List<Food> matchLists = new ArrayList<Food>();//信息检索完后的结果所匹配的集合
	private List<String> soldOutLists = new ArrayList<String>();//存放沽清菜品编码集合
	private List<Food> tempNotAlternativeLists = null;//点套餐时，存放不可换购项的临时集合
	
	
	private SingleMenu menu = SingleMenu.getMenuInstance();
	private List<Food> guestLists = GuestDishes.getGuDishInstance();


	int tpnumflag = 0;//声明成全局变量，为了使用tpnum

	private Food tempFujiaFood = null;//针对于附加项的
	private Food curTpnumTCfood =null;//套餐需要tcmoneymode 和 tpnum

	private int digitalFlag = -1;//头部数字标题栏所用的标识位

	private List<EatableSubGridViewAdapter> subGridViewAdapterLists = null;
	
	private String grp = null;
	private int currentPosition = -1;
    private AlertDialog tipdialog;

	private List<List<List<Food>>> entireTCOrderLists;
	private List<Food> isTcLists;
	private SelectPicPopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (ChioceActivity.ispad) {
			setContentView(R.layout.eatables_layout_pad);
		} else {
			setContentView(R.layout.eatables_layout);
		}
		initView();
		initData();
		initListener();
//		initTcRemand();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		//如果已点菜品界面的数量发生改变，在点菜界面就应该更新
		if(YiDianDishActivity.hasChanged){
			for(Grptyp grptyp:grpLists){
				grptyp.setQuantity("0");//将原来的数量赋值为0
			}
			
			for(Food fd:dishLists){
				fd.setSelected(false);//设置为未选状态
				fd.setCounts("0");
			}
			List<Food> onlyTcLists = new ArrayList<Food>();//只存放套餐并且套餐的TPNUM = 0，不存放套餐明细
			for(Food food:guestLists){
				if(food.isIstc() && food.getTpcode().equals(food.getPcode()) && food.getTpnum().equals("0")){
					onlyTcLists.add(food);
				}
			}
			for(Food f:onlyTcLists){
				int tcCounts = 0;
				for(Food tempFood:guestLists){
					if(tempFood.isIstc() && tempFood.getPcode().equals(tempFood.getTpcode()) && f.getPcode().equals(tempFood.getPcode())){
						tcCounts+=1;
					}
				}
				f.setCounts(tcCounts+"");
			}
			
			
			for(Food onlytcFood:onlyTcLists){
				for(Food pFood:dishLists){
					if(pFood.isIstc() && onlytcFood.getPcode().equals(pFood.getPcode())){
						pFood.setSelected(true);
						pFood.setCounts(onlytcFood.getCounts());
					}
				}
			}
			onlyTcLists = null;//用完了就赋值为空，套餐的更新数量完成

			//给普通的单品赋值,由未选中状态变为选中状态，再附上数量
			for(Food mFood:guestLists){
				for(Food food:dishLists){
					if(!mFood.isIstc()&& mFood.getPcode().equals(food.getPcode())){
						food.setSelected(true);
						food.setCounts(mFood.getPcount());
					}
				}
			}

			//更新左边菜品大类的数量
			for(Grptyp grptyp:grpLists){
				int countGrp = 0;
				String grpCode = grptyp.getGrp();//得到该大类的编号
				for(Food f:dishLists){
					if(grpCode.equals(f.getSortClass())){
						countGrp += Double.parseDouble(ValueUtil.isEmpty(f.getCounts()) ? "0" : f.getCounts());
					}
				}
				grptyp.setQuantity(countGrp+"");
			}

			//更新左边菜品类别ListView
			eatListAdapter.setSelectedPosition(currentPosition);
			eatListAdapter.setListSource(grpLists);
			eatListAdapter.notifyDataSetChanged();

			//更新右边菜品GridView
			String currentGrp = grpLists.get(currentPosition).getGrp();
			updateDishClass(currentGrp, dishLists);

			if(eatGridAdapter == null){
				eatGridAdapter=new EatableGridAdapter(this,dishClassLists);
			}
			eatGridAdapter.setEatableGridSource(dishClassLists);
			eatGridAdapter.notifyDataSetChanged();
			
			YiDianDishActivity.hasChanged = false;
		}//已点菜页面数量的修改，更新完成
	}


	/**
	 * 调用沽清菜品接口
	 */
	private void accessSoldOutFace(){
		CList<Map<String, String>> params = new CList<Map<String,String>>();
		params.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
		params.add("userCode", SharedPreferencesUtils.getUserCode(this));
		new Server().connect(this, Constants.FastMethodName.soldOut_METHODNAME, Constants.FastWebService.soldOut_WSDL, params, new OnServerResponse() {
			
			@Override
			public void onResponse(String result) {
				mLoadingDialog.dismiss();
				if(ValueUtil.isNotEmpty(result)){
					String [] flag = result.split("@");
					if(flag[0].equals("0")){
						
						for(int i = 1;i<flag.length;i++){
							soldOutLists.add(flag[i]);
						}
						
						//匹配dishLists集合中的沽清菜品
						for(Food food:dishLists){
							for(String outStr:soldOutLists){
								if(outStr.equals(food.getPcode())){
									food.setSoldOut(true);
								}else{
									food.setSoldOut(false);
								}
							}
						}
						
						eatGridAdapter = new EatableGridAdapter(Eatables.this, dishClassLists);
						gridView.setAdapter(eatGridAdapter);
							
					}else{
						if(flag[0].equals("1")){  
							
							eatGridAdapter = new EatableGridAdapter(Eatables.this, dishClassLists);
							gridView.setAdapter(eatGridAdapter);
							
						}else{
							Log.d("沽清", "失败");
						}
				       
					}
				}else{
					Toast.makeText(Eatables.this, R.string.net_error, Toast.LENGTH_SHORT).show();
				}
				
			}
			
			@Override
			public void onBeforeRequest() {
				mLoadingDialog.show();
			}
		});
	}

	/**
	 * 给每个空间添加事件
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
		if (SingleMenu.getMenuInstance().getPermissionList()!=null) {
			for (Map<String, String> map : SingleMenu.getMenuInstance().getPermissionList()) {
				if (map.get("CODE").equals("2002") && map.get("ISSHOW").equals("0")) {
					remarkBtn.setVisibility(View.GONE);
					break;
				}
			}
		}
		remarkBtn.setOnClickListener(this);
		yiDianBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		
		tcBtnCertain.setOnClickListener(this);
		tcBtnCancel.setOnClickListener(this);
		
		btnSearchView.setOnClickListener(this);
		
		etSearchView.addTextChangedListener(watcher);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

				//用来判断某个套餐是否选择完毕
				if(linearTcLayout.getVisibility() == View.VISIBLE){
					Toast.makeText(Eatables.this, R.string.tc_not_dismiss, Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				eatListAdapter.setSelectedPosition(position);
				eatListAdapter.notifyDataSetChanged();
				currentPosition = position;
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

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,long id) {
                final int radioValue = Integer.parseInt(radioValueShow.getText().toString());//得到数字标题栏最右边的那个值
				Food foodItem = dishClassLists.get(position);
				//如果是沽清菜品，就得处理
                if (foodItem.isSoldOut()) {
                    return;
                }
				foodOrder(foodItem,view);
            }
		});
		
	}


	/**
	 * 创建第二单位的对话框
	 */
	public void createDiaForUnitcur(final Food pFood){
		Dialog dialog = null;
		View layout = LayoutInflater.from(Eatables.this).inflate(R.layout.unitcur_layout, null);
		final EditText weightEdit = (EditText) layout.findViewById(R.id.dia_et_weightflag);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(Eatables.this,R.style.edittext_dialog);
		builder.setTitle(R.string.second_unit_weight).setView(layout)
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String weightValue = weightEdit.getText().toString();
				pFood.setWeight(weightValue);
			}
		}).setNegativeButton(R.string.cancle, null);
		
		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
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
		creatFood.setTpcode(pFood.getPcode());
		creatFood.setTpname(pFood.getPcname());
		creatFood.setPcount("1");
		creatFood.setCounts(pFood.getCounts());
		creatFood.setPrice(pFood.getPrice());
		creatFood.setUnit(pFood.getUnit());
		creatFood.setWeightflg(pFood.getWeightflg());
		creatFood.setIstc(pFood.isIstc());
		creatFood.setTcMoneyMode(pFood.getTcMoneyMode());

//		guestLists.add(creatFood);
		
		curTpnumTCfood = creatFood;//这里不是为哪一项菜品添加附加项做准备 ，而是套餐有三种计价方式，计算时，用到tpnum和tcmoneymode,将tpnum,tcmoneymode传进去       rather special
		
				
		linearTcLayout.setVisibility(View.VISIBLE);
		
		List<List<Food>> allTcDetailLists= getDataManager(this).queryTcItemLists(creatFood);//锟斤拷询锟斤拷锟斤拷锟阶诧拷锟铰碉拷锟斤拷锟斤拷锟斤拷细锟斤拷锟斤拷 
		showTcdetails(allTcDetailLists);
			
	}

	/**
	 * 执行套餐加价,只有第一种计价方式才执行套餐加价
	 * @param pCurTpnumTCfood
	 * @param food
	 * @param radioValue
	 */
	private void executeAddJustPrice(Food pCurTpnumTCfood,Food food,int radioValue){
		if(pCurTpnumTCfood.getTcMoneyMode().equals("1")){
			double adJustPrice = food.getAdJustPrice();
			if(adJustPrice > 0){
				double alladJustPrices = adJustPrice  * Double.parseDouble(radioValue+"");
				double newFoodTcPriceIndoble = Double.parseDouble(food.getPrice()) + Double.parseDouble(ValueUtil.setScale(alladJustPrices, 2, 5));
                food.setPrice(ValueUtil.setScale(newFoodTcPriceIndoble, 2, 5));
				pCurTpnumTCfood.setPrice(ValueUtil.setScale(Double.parseDouble(pCurTpnumTCfood.getPrice())+alladJustPrices, 2, 5));
			}
		}
		
	}

	/**
	 * 查询出数据，初始化套餐详情的view,新版本
	 * @param allTcDetails
	 */
	private void showTcdetails(List<List<Food>> allTcDetails) {
		
		subGridViewAdapterLists = new ArrayList<EatableSubGridViewAdapter>();
		
		tempNotAlternativeLists = new ArrayList<Food>();

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
			
			if(singleLists.size() > 1){        //可换购项

				//在此处匹配套餐明细菜品中的沽清菜品    只匹配可换购项
				for(Food food:singleLists){
					for(String outStr:soldOutLists){
						if(outStr.equals(food.getPcode())){
							food.setSoldOut(true);
						}else{
							food.setSoldOut(false);
						}
					}
				}
				
				tcGroupNumber+=1;
				
				LinearLayout tcTopGroupLayout = new LinearLayout(Eatables.this);
				tcTopGroupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				tcTopGroupLayout.setOrientation(LinearLayout.HORIZONTAL);
				
				
				TextView tcGroupTv = new TextView(Eatables.this);
				tcGroupTv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tcGroupTv.setTextColor(Color.BLACK);
				tcGroupTv.setText(tcGroupNumber+"    ");
				
				
				cn.com.choicesoft.view.TcGridView tcGridView = new cn.com.choicesoft.view.TcGridView(this);
				tcGridView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				if (ChioceActivity.ispad) {
					tcGridView.setNumColumns(5);
				} else {
					tcGridView.setNumColumns(3);
				}
				tcGridView.setDrawSelectorOnTop(true);
				tcGridView.setVerticalSpacing(5);
				tcGridView.setHorizontalSpacing(5);
				tcGridView.setBackgroundColor(Color.parseColor("#ADD8E6"));
				tcGridView.setSelector(R.color.transparent);
				
				EatableSubGridViewAdapter subGridAdapter = new EatableSubGridViewAdapter(this, singleLists,String.valueOf(tpnumflag));
				tcGridView.setAdapter(subGridAdapter);
				
				
				Food defaultSFood = subGridAdapter.getDefaultsFood();
				
				TextView tcGroupMinCntTv = new TextView(Eatables.this);
				tcGroupMinCntTv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tcGroupMinCntTv.setTextColor(Color.BLACK);
				tcGroupMinCntTv.setText(ValueUtil.isNotEmpty(defaultSFood.getMinCnt())?defaultSFood.getMinCnt()+"——":"0" +"——");
				
				TextView tcGroupMaxCntTv = new TextView(Eatables.this);
				tcGroupMaxCntTv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tcGroupMaxCntTv.setTextColor(Color.BLACK);
				tcGroupMaxCntTv.setText(ValueUtil.isNotEmpty(defaultSFood.getMaxCnt())?defaultSFood.getMaxCnt()+" :  " : "  : ");
				
				final TextView tcCountsChoosedTv = new TextView(Eatables.this);
				tcCountsChoosedTv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tcCountsChoosedTv.setTextColor(Color.BLACK);
				
				
				subGridViewAdapterLists.add(subGridAdapter);
				
				tcGridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {

						final int radioValue = Integer.parseInt(radioValueShow.getText().toString());//得到数字标题栏最右边的那个值

						radioValueShow.setText("1");//将上面的数字标题栏还原
						digitalFlag = -1;

						final EatableSubGridViewAdapter adapter = (EatableSubGridViewAdapter) parent.getAdapter();

						final Food foodDefault = adapter.getDefaultsFood();

						final Food foodCurrent = singleLists.get(position + 1);

						//如果套餐明细是沽清菜品，就得处理
						if (!foodCurrent.isSoldOut()) {
							tempFujiaFood = foodCurrent;//为套餐明细菜品添加附加项做准备
						} else {
							return;
						}
						foodCurrent.setTabNum(menu.getTableNum());
						foodCurrent.setOrderId(menu.getMenuOrder());
						foodCurrent.setMan(menu.getManCounts());
						foodCurrent.setWoman(menu.getWomanCounts());
//						foodCurrent.setTpnum(tpnumflag+"");//这里相当的特殊,为套餐明细添加tpnum
						if(foodCurrent.getIsTemp()==1 && radioValue > 0){
							new TempFoodView(Eatables.this, foodCurrent, new TempFoodView.Result() {
								@Override
								public void handle(Food food) {
									addfood(food,radioValue,view,foodDefault,tcCountsChoosedTv,adapter);
								}
							}).show();
						}else{
							addfood(foodCurrent,radioValue,view,foodDefault,tcCountsChoosedTv,adapter);
						}
					}
				});
				
				ImageView mImageView = new ImageView(Eatables.this);
				mImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
				mImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
				
				tcTopGroupLayout.addView(tcGroupTv);
				tcTopGroupLayout.addView(tcGroupMinCntTv);
				tcTopGroupLayout.addView(tcGroupMaxCntTv);
				tcTopGroupLayout.addView(tcCountsChoosedTv);
				
				linearTCDetails.addView(tcTopGroupLayout);
				linearTCDetails.addView(tcGridView);
				linearTCDetails.addView(mImageView);
				
				
			}else if(singleLists.size() == 1 && singleLists.get(0).getMinCnt().equals(singleLists.get(0).getMaxCnt())){//不可换购项
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
			for(Food mFood:tempLists){
				int counts = 0;
				for(Food food:tempNotAlternativeLists){
					if(mFood.getPcode().equals(food.getPcode())){
						counts += Integer.parseInt(food.getPcount());
					}
				}
				mFood.setPcount(counts+"");
			}
			
			guestLists.add(curTpnumTCfood);
			guestLists.addAll(tempLists);
			foodTotalPrice();
		}
		
		
	}

	/**
	 * 必选附加项判断加菜
	 * @param foodCurrent
	 * @param radioValue
	 * @param view
	 * @param foodDefault
	 * @param tcCountsChoosedTv
	 * @param adapter
	 */
	public void addfood(final Food foodCurrent, final int radioValue, final View view, final Food foodDefault, final TextView tcCountsChoosedTv, final EatableSubGridViewAdapter adapter){
		if (foodCurrent.getFujiaModel().equals(1) && radioValue > 0) {//判断是否有必选附加项
			new MustFuJiaView(foodCurrent, Eatables.this, view, new MustFuJiaView.MustCallBack() {
				@Override
				public void execute() {
					comboFood(radioValue,foodCurrent,foodDefault,tcCountsChoosedTv,adapter);
				}
			}).showView();
		} else {
			comboFood(radioValue, foodCurrent, foodDefault, tcCountsChoosedTv, adapter);
		}
	}

	/**
	 * 加菜操作
	 * @param radioValue
	 * @param foodCurrent
	 * @param foodDefault
	 * @param tcCountsChoosedTv
	 * @param adapter
	 */
	private void comboFood(int radioValue,Food foodCurrent,Food foodDefault,TextView tcCountsChoosedTv,EatableSubGridViewAdapter adapter){
		if(radioValue != 0){
			if(foodCurrent.isSelected()){

				int countCurrent = Integer.parseInt(foodCurrent.getPcount());
				int countNew = countCurrent + radioValue;
				if(ValueUtil.isNotEmpty(foodCurrent.getMaxCnt()) && countNew > Integer.parseInt(foodCurrent.getMaxCnt()) ){
					Toast.makeText(Eatables.this, Eatables.this.getString(R.string.food_maximum)+foodCurrent.getMaxCnt(), Toast.LENGTH_SHORT).show();
				}else if(adapter.getCountsAboutPcount() + radioValue > Integer.parseInt(foodDefault.getMaxCnt())){
					Toast.makeText(Eatables.this, Eatables.this.getString(R.string.food_beyond_maximum)+foodDefault.getMaxCnt(), Toast.LENGTH_SHORT).show();
				}else{

					foodCurrent.setPcount(countNew+"");
					adapter.notifyDataSetChanged();
					//执行套餐加价,只有第一种计价方式才执行套餐加价
					executeAddJustPrice(curTpnumTCfood, foodCurrent, radioValue);
					//设置顶部该组已经选择的数量
					tcCountsChoosedTv.setText(adapter.getCountsAboutPcount()+"");
					if(foodCurrent.getWeightflg().equals("2")){
						createDiaForUnitcur(foodCurrent);
					}

				}
			}else{
				if(ValueUtil.isNotEmpty(foodCurrent.getMaxCnt()) && radioValue > Integer.parseInt(foodCurrent.getMaxCnt()) ){
					Toast.makeText(Eatables.this, Eatables.this.getString(R.string.food_maximum)+foodCurrent.getMaxCnt(), Toast.LENGTH_SHORT).show();
					return;
				}else if(adapter.getCountsAboutPcount() + radioValue > Integer.parseInt(foodDefault.getMaxCnt())){
					Toast.makeText(Eatables.this, Eatables.this.getString(R.string.food_beyond_maximum)+foodDefault.getMaxCnt(), Toast.LENGTH_SHORT).show();
					return;
				}
				foodCurrent.setSelected(true);
				foodCurrent.setPcount(radioValue+"");
				adapter.notifyDataSetChanged();

				//执行套餐加价,只有第一种计价方式才执行套餐加价
				executeAddJustPrice(curTpnumTCfood, foodCurrent, radioValue);

				//设置顶部该组已经选择的数量
				tcCountsChoosedTv.setText(adapter.getCountsAboutPcount() + "");
			}
		}else if(radioValue == 0){
			if(foodCurrent.isSelected()){
				//先在这里得到foodCurrent已经选择的数量,下面要用到这个值
				int countFormer = Integer.parseInt(foodCurrent.getPcount());
				if(ValueUtil.isNotEmpty(foodCurrent.getMinCnt()) && Integer.parseInt(foodCurrent.getMinCnt()) > 0){
					foodCurrent.setSelected(true);
					foodCurrent.setPcount(foodCurrent.getMinCnt());
					adapter.notifyDataSetChanged();

					//设置顶部该组已经选择的数量
					tcCountsChoosedTv.setText(adapter.getCountsAboutPcount()+"");

					//当radioValue= 0时，表示取消已选的套餐明细,同时也得减价
					if(curTpnumTCfood.getTcMoneyMode().equals("1")){
						double adJustPrice = foodCurrent.getAdJustPrice();
						if(adJustPrice > 0){
							int minCntCount = Integer.parseInt(foodCurrent.getMinCnt());
							int countLeft = countFormer - minCntCount;
							double allAddJustPrice = adJustPrice * Double.parseDouble(countLeft+"");
							double newCurTpnumTcfoodPrice = Double.parseDouble(curTpnumTCfood.getPrice())- Double.parseDouble(ValueUtil.setScale(allAddJustPrice, 2, 5));
							curTpnumTCfood.setPrice(ValueUtil.setScale(newCurTpnumTcfoodPrice, 2, 5));
						}
					}

				}else{

					foodCurrent.setSelected(false);
					foodCurrent.setPcount("");
					adapter.notifyDataSetChanged();

					//设置顶部该组已经选择的数量
					tcCountsChoosedTv.setText(adapter.getCountsAboutPcount()+"");

					//当radioValue= 0时，表示取消已选的套餐明细,同时也得减价
					if(curTpnumTCfood.getTcMoneyMode().equals("1")){
						double adJustPrice = foodCurrent.getAdJustPrice();
						if(adJustPrice > 0){
							double allAddJustPrice = adJustPrice * Double.parseDouble(countFormer+"");
							double newCurTpnumTcfoodPrice = Double.parseDouble(curTpnumTCfood.getPrice()) - Double.parseDouble(ValueUtil.setScale(allAddJustPrice, 2, 5));
							curTpnumTCfood.setPrice(ValueUtil.setScale(newCurTpnumTcfoodPrice, 2, 5));
						}
					}
				}
			}
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		bundle = getTblStateBundle();
		if(ValueUtil.isEmpty(bundle)){
			tableNumTv.setText(menu.getTableNum());
			orderNumTv.setText(menu.getMenuOrder());
			manNumTv.setText(ValueUtil.isEmpty(menu.getManCounts())?"0":menu.getManCounts());
			womanNumTv.setText(ValueUtil.isEmpty(menu.getWomanCounts())?"0":menu.getWomanCounts());
			userNumTv.setText(SharedPreferencesUtils.getOperator(this));
		}else{
			tableNumTv.setText(bundle.getString("tableNum"));
			orderNumTv.setText(bundle.getString("orderId"));
			manNumTv.setText(ValueUtil.isNotEmpty(bundle.getString("manCs"))?bundle.getString("manCs"):"0");
			womanNumTv.setText(ValueUtil.isNotEmpty(bundle.getString("womanCs"))?bundle.getString("womanCs"):"0");
			userNumTv.setText(SharedPreferencesUtils.getOperator(this));
			menu.setTableNum(bundle.getString("tableNum"));
			menu.setMenuOrder(bundle.getString("orderId"));
			menu.setManCounts(bundle.getString("manCs"));
			menu.setWomanCounts(bundle.getString("womanCs"));
			menu.setUserCode(SharedPreferencesUtils.getUserCode(this));
		}

		//初始化会员信息
		VipMsg.iniVip(this, menu.getTableNum(),R.id.vipMsg_ImageView);	
		
		grpLists = getDataManager(this).getAllDishClassList();//获得菜品类别
		dishLists =  getDataManager(this).getAllFoodList();//获取所有菜品
		
//		unitCurLists = getDataManager(this).queryCurUnitLists();//获取所有第二单位菜品
//		//匹配dishLists集合中的有第二单位的菜品
//		for(Food mFood:dishLists){
//			mFood.setWeightflg("1");
//			for(CurrentUnit cUnit:unitCurLists){
//				if(cUnit.getItcode().equals(mFood.getPcode())){
//					mFood.setWeightflg("2");//2代表有第二单位           1代表第一单位
//				}
//			}
//		}
		if (guestLists.size()==0){
			List<Food> cacheLists = new ValueSetFile<Food>(menu.getTableNum()
					+DateFormat.getStringByDate(new Date(),"yyyy-MM-dd")
					+menu.getMenuOrder()+".mc").read();//getDataManager(this).queryAllCheckIfCache(menu.getTableNum(), menu.getMenuOrder());
//			if(cacheLists.size() != 0){
				guestLists.clear();
				//将查询出来的food添加到单例集合中
				guestLists.addAll(cacheLists);
		}
		//如果已点菜品界面的数量发生改变，在点菜界面就应该更新
		for (Food food :dishLists){
			//菜品数量
			for (Food guestFood :guestLists){
				if (food.getPcode().equals(guestFood.getPcode())&&(guestFood.getTpcode()==null||guestFood.getPcode().equals(guestFood.getTpcode()))){
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

		//如果在点菜页面保存已点菜到本地数据库，或者在已点菜页面点击暂存按钮也将已点的菜品保存到本地数据库
//		if(this.getIntent().getStringExtra("direction")!=null && this.getIntent().getStringExtra("direction").equals("MainDirection")){   //判断是从哪个activity跳转过来的.从mainactivity
//			List<Food> cacheLists = new ValueSetFile<Food>(menu.getTableNum()
//					+DateFormat.getStringByDate(new Date(),"yyyy-MM-dd")
//					+menu.getMenuOrder()+".mc").read();//getDataManager(this).queryAllCheckIfCache(menu.getTableNum(), menu.getMenuOrder());
//			if(cacheLists.size() != 0){
//				guestLists.clear();
//				//将查询出来的food添加到单例集合中
//				guestLists.addAll(cacheLists);
//
//
//				//给套餐赋值,由未选中状态变为选中状态，再附上数量,通过累加得到总数
//				List<Food> onlyTcLists = new ArrayList<Food>();//只存放套餐并且套餐的tpnum = 0，不存放套餐明细
//				for(Food food:guestLists){
//					if(food.isIstc() && food.getTpcode().equals(food.getPcode()) && food.getTpnum().equals("0")){
//						onlyTcLists.add(food);
//					}
//				}
//
//				for(Food f:onlyTcLists){
//					int tcCounts = 0;
//					for(Food tempFood:guestLists){
//						if(tempFood.isIstc() && tempFood.getPcode().equals(tempFood.getTpcode()) && f.getPcode().equals(tempFood.getPcode())){
//
//							tcCounts +=1;
//						}
//					}
//					f.setCounts(tcCounts+"");
//				}
//
//				for(Food onlytcFood:onlyTcLists){
//					for(Food pFood:dishLists){
//						if(onlytcFood.getPcode().equals(pFood.getPcode())){
//							pFood.setSelected(true);
//							pFood.setCounts(onlytcFood.getCounts());
//						}
//					}
//				}
//
//				//给普通的单品赋值,由未选中状态变为选中状态，再附上数量
//				for(Food mFood:guestLists){
//					for(Food food:dishLists){
//						if(!mFood.isIstc()&& mFood.getPcode().equals(food.getPcode())){
//							food.setSelected(true);
//							food.setCounts(mFood.getPcount());
//						}
//					}
//				}
//
//
//				//更新左边菜品大类的数量
//				for(Grptyp grptyp:grpLists){
//					int countGrp = 0;
//					for(Food f:dishLists){
//						if(grptyp.getGrp().equals(f.getSortClass())){
//							countGrp += Integer.parseInt(ValueUtil.isNotEmpty(f.getCounts())?f.getCounts():"0");
//						}
//					}
//					grptyp.setQuantity(countGrp+"");
//				}
//
//
//			}
//		}
		
		
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
		if(grpLists==null||grpLists.size()<=0){
			ToastUtil.toast(this,R.string.class_error);
			this.finish();
			return;
		}
		currentPosition = 0;	
		grp = grpLists.get(0).getGrp();

		updateDishClass(grp,dishLists);

		//更新界面上的左边的ListView
		eatListAdapter.setSelectedPosition(0);
		eatListAdapter.notifyDataSetChanged();

		accessSoldOutFace();//调用沽清接口
		
	}
	
	
	
	/**  
	 *  根据菜品类别，查询出该类别下的具体菜品集合
	 * 更新dishclass集合
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

	/**
	 * 获取跳转传过来的Bundle
	 * @return
	 */
	private Bundle getTblStateBundle() {
		return this.getIntent().getBundleExtra("topBundle");
	}

	/**
	 * 初始化控件
	 */
	private void initView() {		
		
		mLoadingDialog = new LoadingDialogStyle(this,this.getString(R.string.update_data));
		mLoadingDialog.setCancelable(false);

		//顶部红色栏
		if (ChioceActivity.ispad) {
			tableNumTv = (TextView) this.findViewById(R.id.dishesact_tv_tblnum);
			orderNumTv = (TextView) this.findViewById(R.id.dishesact_tv_ordernum);
			manNumTv = (TextView) this.findViewById(R.id.dishesact_tv_mannum);
			womanNumTv = (TextView) this.findViewById(R.id.dishesact_tv_womannum);
			userNumTv = (TextView) this.findViewById(R.id.dishesact_tv_usernum);
			fujiaChanPin=(Button)findViewById(R.id.eatable_btn_fujiaChanpin);
			fujiaChanPin.setOnClickListener(this);
		}else {
			// TODO 
			menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
			menuinfo_imageView.setClickable(true);
			menuinfo_imageView.setOnClickListener(this);
			layout = LayoutInflater.from(Eatables.this).inflate(R.layout.menu_info, null);
			tableNumTv = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNumTv = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			manNumTv = (TextView) layout.findViewById(R.id.dishesact_tv_mannum);
			womanNumTv = (TextView) layout.findViewById(R.id.dishesact_tv_womannum);
			userNumTv = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
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
		
		etSearchView = (ClearEditText) this.findViewById(R.id.et_searchView);
		btnSearchView = (Button) this.findViewById(R.id.btn_searchView);
		
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
		
		remarkBtn = (Button) this.findViewById(R.id.eatable_btn_remark);
		yiDianBtn = (Button) this.findViewById(R.id.eatable_btn_finishdish);
		backBtn = (Button) this.findViewById(R.id.eatable_btn_back);
		
	}
	
	
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
            case R.id.eatable_btn_remark:
                showCommonFujia();
                break;
            case R.id.eatable_btn_finishdish:
                if(linearTcLayout.getVisibility() == View.VISIBLE){
                    Toast.makeText(this, R.string.tc_not_dismiss_other, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(Eatables.this,YiDianDishActivity.class);
                startActivity(intent);
                break;
            case R.id.eatable_btn_back:
				//如果客人已点了菜品，点击返回时，弹出提示框，提示是否保存
                if(guestLists.size() == 0){
                    this.finish();
                }else{
					//套餐还未选择完毕
                    if(linearTcLayout.getVisibility() == View.VISIBLE){
                        Toast.makeText(this, R.string.eatable_tc_tcnofinish, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TsData.isReserve){
                        this.finish();
                        break;
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
                for(Food mFood:currentTcLists){
                    if(!removeDuplicateLists.contains(mFood)){
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
				foodTotalPrice();
                tpnumflag = 0;//将套餐编号设为0
                break;

            case R.id.eatable_tcdetail_btn_cancel:
                linearTCDetails.removeAllViews();
                linearTcLayout.setVisibility(View.GONE);

                tempNotAlternativeLists = null;

                for(Food food:dishClassLists){
                    if(food.getPcode().equals(curTpnumTCfood.getPcode())){
						//将数量减1
                        Float tcCurrentCounts = Float.parseFloat(food.getCounts());
                        tcCurrentCounts -= 1;
                        if(tcCurrentCounts == 0){
                            food.setSelected(false);
                            food.setCounts("");
                        }
                        food.setCounts(tcCurrentCounts+"");
                        break;
                    }
                }

                eatGridAdapter.notifyDataSetChanged();
                tpnumflag = 0;//将套餐编号设为0

                break;
            case R.id.orderinfo_ImageView:
                if(tipdialog==null) {
                    tipdialog = new AlertDialog.Builder(Eatables.this, R.style.Dialog_tip).setView(layout).create();
                    tipdialog.setCancelable(true);
                    Window dialogWindow = tipdialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
                    lp.x = 0; // 新位置X坐标
                    lp.y = topLinearLayout.getHeight() -15; // 新位置Y坐标
                    dialogWindow.setAttributes(lp);
                }
                if(tipdialog.isShowing()){
                    tipdialog.dismiss();
                }else {
                    tipdialog.show();
                }
                break;
            case R.id.eatable_btn_fujiaChanpin:
                List<Addition> list;
                if(tempFujiaFood==null){
                    Toast.makeText(Eatables.this, R.string.you_not_choice_food, Toast.LENGTH_SHORT).show();
                    return;
                }
                ListProcessor listProcessor=new ListProcessor();
                String sql="select INIT,PCODE,FCODE as FOODFUJIA_ID,FNAME AS FOODFUJIA_DES,\n" +
                        "Fprice as FOODFUJIA_CHECKED,'false' as ISSELECTED, \n" +
                        "MAXCNT,MINCNT,PRODUCTTC_ORDER,DEFUALTS,ISADDPROD,\n" +
                        "ISMUST from FoodFuJia where Isaddprod=1 and ismust=0 and pcode=? order by DEFUALTS";
                list=listProcessor.query(sql,new String[]{tempFujiaFood.getPcode()},this,Addition.class);
                if(list==null||list.size()<=0) {
                    String sql1 = "select INIT,PCODE,FCODE as FOODFUJIA_ID,FNAME AS FOODFUJIA_DES,\n" +
                            "Fprice as FOODFUJIA_CHECKED,'false' as ISSELECTED, \n" +
                            "MAXCNT,MINCNT,PRODUCTTC_ORDER,DEFUALTS,ISADDPROD,\n" +
                            "ISMUST from FoodFuJia where Isaddprod=1 and ismust=0 and (pcode is null or pcode like '%PCODE%' or PCODE ='') order by DEFUALTS";
                    list=listProcessor.query(sql1,null,this,Addition.class);
                }
                new ComFujiaView(this,tempFujiaFood).showView(list);
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
//					tableNum = ? and Time= ? and orderId = ?
					new ValueSetFile<Food>(guestLists.get(0).getTabNum()
							+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")
							+guestLists.get(0).getOrderId()+".mc").write(guestLists);
					guestLists.clear();
					Intent intent = new Intent(Eatables.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					break;
					
				case 1:
					guestLists.clear();
					Eatables.this.finish();
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
	 * 菜品信息检索用的
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
					
					InputMethodUtils.toggleSoftKeyboard(Eatables.this);

					//设置左边的菜品大类可点击
					listView.setEnabled(true);
				}
			}
		};
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			
			if(linearTcLayout.getVisibility() == View.VISIBLE){
				Toast.makeText(this, R.string.eatable_tc_tcnofinish, Toast.LENGTH_SHORT).show();
				return false;
			}
			
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
				if(guestLists.size() == 0){  //如果客人没点一道菜，单例集合长度为0，就不弹框
					this.finish();
				}else{
                    if(TsData.isReserve){
                        this.finish();
                        return false;
                    }
					showIfSaveDialog().show();
				}
			}
			return super.onKeyDown(keyCode, event);
		}
		/**
		 * 套餐的三种计价方式
		 * @param pLists  singleton集合
		 * @param wholeFoodLists    所有菜品大集合
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
				
				 double rate = Double.parseDouble(tcFood.getPrice())/tcDetailPrices;//得到比率
				 for(Food f:pLists){
					 if(f.isIstc() && !f.getTpcode().equals(f.getPcode()) && f.getTpcode().equals(tcFood.getPcode()) && tcFood.getTpnum().equals(f.getTpnum())){
						 
						 double newPrice = Double.parseDouble(f.getPrice()) * Double.parseDouble(f.getPcount()) * rate;
						 f.setPrice(ValueUtil.setScale(newPrice, 2, 5));//经过此步，套餐明细的价格就是多份的了，而不是单份的
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
						tcDetailsPrice+=Double.valueOf(mFood.getEachprice());
						mFood.setPrice(mFood.getEachprice());
					}
				}//得到套餐明细菜品的价格相加后的和
				
				tcFood.setPrice(ValueUtil.setScale(tcDetailsPrice, 2, 5));
			}else if(tcMode == 3){ 
				double tcDetailsPrice = 0.00;
				double tcaddPrice = 0.00;
				for(Food food:pLists){
					if(food.isIstc() && !food.getTpcode().equals(food.getPcode()) && food.getTpcode().equals(tcFood.getPcode()) && food.getTpnum().equals(tcFood.getTpnum())){
						double itemPrices = Double.parseDouble(food.getEachprice()) * Double.parseDouble(food.getPcount());
						tcaddPrice += itemPrices;
					}
				}//得到套餐明细菜品的价格相加后的和
				
				double tcInitialPrice = Double.valueOf(tcFood.getPrice());//套餐原有价格
				
				if(tcaddPrice >= tcInitialPrice){
					for(Food mFood:pLists){
						if(mFood.isIstc() && !mFood.getPcode().equals(mFood.getTpcode()) && mFood.getTpcode().equals(tcFood.getPcode()) && mFood.getTpnum().equals(tcFood.getTpnum())){
							mFood.setPrice(mFood.getEachprice());
						}
					}
					tcFood.setPrice(ValueUtil.setScale(tcaddPrice, 2, 5));
				}else{
					for(Food food:pLists){
						if(food.isIstc() && !food.getTpcode().equals(food.getPcode()) && food.getTpcode().equals(tcFood.getPcode()) && food.getTpnum().equals(tcFood.getTpnum())){
							double priceItem = Double.parseDouble(food.getPrice()) * Double.parseDouble(food.getPcount());
							tcDetailsPrice += Double.parseDouble(ValueUtil.setScale(priceItem, 2, 5));
						}
					}
					double rate = tcInitialPrice/tcDetailsPrice;//得到比率
					
					for(Food f:pLists){
						if(f.isIstc() && !f.getTpcode().equals(f.getPcode()) && f.getTpcode().equals(tcFood.getPcode()) && f.getTpnum().equals(tcFood.getTpnum())){
							double newPrice = Double.parseDouble(f.getPrice()) * Double.parseDouble(f.getPcount())* rate;
							f.setPrice(ValueUtil.setScale(newPrice, 2, 5));//经过此步，套餐明细的价格就是多份的了，而不是单份的
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
						 newConformPrice += Double.parseDouble(fd.getPrice()); 
					 }
					 
					 if(newConformPrice != tcInitialPrice){
						if(tcOnlys.size() != 0){//必须加判断，如果所有的MINCNT都等于0，就报数组越界
							double allPriceButFirst = newConformPrice - Double.parseDouble(tcOnlys.get(0).getPrice());
							double a1 = Double.parseDouble(tcFood.getPrice()) - allPriceButFirst;
							tcOnlys.get(0).setPrice(ValueUtil.setScale(a1, 2, 5));	
						}
					}
					
					tcFood.setPrice(ValueUtil.setScale(tcInitialPrice, 2, 5));
				}
				
			}
		}

	
	
	/**
	 * 判断是否某个套餐还有最后一层没选，就提示
	 */
	private void judgeIsToLastLayer(List<Food> allTcLists){
		final List<Food> makeUpTcListsRes = new ArrayList<Food>();//套餐组合用的
		final List<Food> recommandTcListsRes = new ArrayList<Food>();//套餐推荐用的
		
		
		
		for(List<List<Food>> tcLists :entireTCOrderLists){
			int layerCounts = tcLists.size();//得出某个套餐有多少层
			int layerSatisfiedFlag = 0;
			for(List<Food> layerLists:tcLists){//得出某个套餐每一层的集合,判断每一层是否符合该层的maxcnt与minCNT，还得判断有数量的菜是否满足自身的maxcnt与mincnt
				
				int groupMinCnts = 0;
				int groupMaxCnts = 0;
				
				boolean isGroupSatisfy = false;//是否满足该组的mincnt与maxcnt
				boolean isSelfSatisfy = false;//是否满足自己的mincnt与maxcnt
				boolean isSelfMinCntSatisfy = true;//是否自己的最小数量mincnt得到满足
				
				for(Food mFood:layerLists){
					if(mFood.getDefalutS().equals("0")){
						groupMinCnts = ValueUtil.isNaNofInteger(mFood.getMinCnt());//得出每一层最小的限定数量
						groupMaxCnts = ValueUtil.isNaNofInteger(mFood.getMaxCnt());
					}
					break;
				}

				//判断每一层是否符合该层的maxcnt与minCNT,实际情况是只判断minCNT就行
				int minCntCounts = 0;
				for(Food food:layerLists){
					int recommandCnt = Integer.parseInt(food.getRecommendCnt());
					if(recommandCnt > 0 ){
						minCntCounts += recommandCnt;
					}
				}
				
				if(minCntCounts >= groupMinCnts){
//					Log.d("满足该层的mincnt", "满足该层的mincnt");
					isGroupSatisfy = true;
				}

				//判断有数量的菜是否满足自身的maxcnt与mincnt
				for(Food food:layerLists){
					if(!food.getDefalutS().equals("0")){
						
						int minCnt = Integer.parseInt(food.getMinCnt());
						int recommandCnt = Integer.parseInt(food.getRecommendCnt());
						if(minCnt > 0 && recommandCnt < minCnt){
							isSelfMinCntSatisfy = false;
							break;
						}
					}
				}
				
				//锟叫讹拷锟斤拷锟斤拷锟斤拷锟侥诧拷锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟絤axcnt锟斤拷mincnt
				for(Food f:layerLists){
					if(!f.getDefalutS().equals("0")){
						
						int recommandCnt = Integer.parseInt(f.getRecommendCnt());
						
						if(recommandCnt > 0 && recommandCnt >= Integer.parseInt(f.getMinCnt()) ){
//						Log.d("满足这道套餐明细的mincnt与maxcnt啦,这里只判断mincnt就行", "满足这道套餐明细的mincnt与maxcnt啦,这里只判断mincnt就行");

							isSelfSatisfy = true;
						}
					}
					
				}

				//判断该层是否满足套餐的要求,该层满足groupcnt,每道菜品满足mincnt
				if(isGroupSatisfy && isSelfSatisfy && isSelfMinCntSatisfy){
					layerSatisfiedFlag += 1;
				}
			}//表示这个套餐已经循环完了




			//相减得到1，就说明还有最后一层没有点 ,就表示是推荐套餐
			if(layerCounts - layerSatisfiedFlag == 1){
				String tpcode = tcLists.get(0).get(0).getTpcode();
				for(Food food:allTcLists){
					if(food.getPcode().equals(tpcode)){
//						Toast.makeText(this, "推荐"+food.getPcname(), Toast.LENGTH_SHORT).show();
						recommandTcListsRes.add(food);

					}
				}
			}

			//表示是组合套餐
			if(layerCounts == layerSatisfiedFlag){
				String tpcode = tcLists.get(0).get(0).getTpcode();
				for(Food f:allTcLists){
					if(f.getPcode().equals(tpcode)){
//						Toast.makeText(this, "组合"+f.getPcname(), Toast.LENGTH_SHORT).show();
						makeUpTcListsRes.add(f);
					}
				}
			}
			
			

			
		}//对应着最外层的for循环


		//弹出小的悬浮窗
		if(makeUpTcListsRes.size() > 0 || recommandTcListsRes.size() > 0){
			if(!MyWindowManager.isWindowShowing()){
				MyWindowManager.createSmallWindow(this,new SmallWindowClickListener() {
					
					@Override
					public void floatWindowClick() {
						
						popupWindow.removeViews();//点击的时候先清空view
						
						if(makeUpTcListsRes.size() > 0){
							TextView tcTVMakeUpTitle = new TextView(Eatables.this);
							tcTVMakeUpTitle.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
							tcTVMakeUpTitle.setTextSize(20);
							tcTVMakeUpTitle.setGravity(Gravity.CENTER_HORIZONTAL);
							tcTVMakeUpTitle.setText("套餐组合列表");
							
							cn.com.choicesoft.view.TcGridView tcGridView = new cn.com.choicesoft.view.TcGridView(Eatables.this);
							tcGridView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							if(ChioceActivity.ispad) {
								tcGridView.setNumColumns(5);
							}else{
								tcGridView.setNumColumns(3);
							}
							tcGridView.setDrawSelectorOnTop(true);
							tcGridView.setVerticalSpacing(5);
							tcGridView.setHorizontalSpacing(5);
							tcGridView.setBackgroundColor(Color.parseColor("#ADD8E6"));
							tcGridView.setSelector(R.color.transparent);
							
							EatableSubGridViewAdapter subGridAdapter = new EatableSubGridViewAdapter(Eatables.this, makeUpTcListsRes,"");
							tcGridView.setAdapter(subGridAdapter);
							
							popupWindow.addToView(tcTVMakeUpTitle);
							popupWindow.addToView(tcGridView);
							
							tcGridView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,View arg1, int position, long arg3) {
									//将套餐及明细加入到singletonlist中
									Food food = makeUpTcListsRes.get(position);
									List<Food> lists = addIntoSingletonLists(food);
									
									
									//将套餐明细大集合中的数据进行删除更新
									updateEntireListTcData(food, entireTCOrderLists, lists);
									
									//更新左边菜品类别及菜品的数量，界面上
									updateViewDishData(lists, dishLists);

									
									//刷新界面
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

								
							});
						}
						
						if(recommandTcListsRes.size() > 0){
							TextView tcTVRecomandTitle = new TextView(Eatables.this);
							tcTVRecomandTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							tcTVRecomandTitle.setTextSize(20);
							tcTVRecomandTitle.setGravity(Gravity.CENTER_HORIZONTAL);
							tcTVRecomandTitle.setText("套餐推荐列表");
							
							popupWindow.addToView(tcTVRecomandTitle);
							
							for(Food food:recommandTcListsRes){
								
								TextView tcTVRecomandItemTitle = new TextView(Eatables.this);
								tcTVRecomandItemTitle.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
								tcTVRecomandItemTitle.setTextSize(18);
								tcTVRecomandItemTitle.setGravity(Gravity.LEFT);
								tcTVRecomandItemTitle.setPadding(5, 5, 5, 10);
								tcTVRecomandItemTitle.setText(food.getPcname());
								popupWindow.addToView(tcTVRecomandItemTitle);

								//下面显示套餐的最后一层明细,得到套餐最后一层的未选择的菜品
								showTcLastLayer(food,popupWindow);
								
							}
							
							
						}
						
						if(!popupWindow.isShowing()){
							popupWindow.showAtLocation(Eatables.this.findViewById(R.id.eatable_main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
						}
						
					}
				});
			}
		}
		
		
	}
	
	/**
	 * 更新左侧菜品类别数字
	 * @param pLists
	 * @param entireFoods
	 */
	private void updateViewDishData(List<Food> pLists,List<Food> entireFoods){
		for(Food mFood:pLists){
			for(Food f:entireFoods){
				if(!f.isIstc() && mFood.getPcode().equals(f.getPcode())){
					int cntNormal = Integer.parseInt(f.getPcount());//普通菜品原有的数量
					int cntSpecial = Integer.parseInt(mFood.getPcount());//组合套餐的数量
					int endCnt = cntNormal - cntSpecial;
																	
					f.setPcount(endCnt+"");
				}
			}
		}
	}
	
	/**
	 * 将entireTCOrderLists中的明细菜品删去
	 * @param food
	 * @param entireTcLists
	 * @param pTcLists
	 */
	private void updateEntireListTcData(Food food,List<List<List<Food>>> entireTcLists ,List<Food> pTcLists){
		
		
		for(List<List<Food>> tcLists :entireTcLists){
			if(!tcLists.get(0).get(0).getTpcode().equals(food.getPcode())){
				continue;
			}
			
			for(List<Food> layerLists:tcLists){
				for(Food f:layerLists){
					for(Food fd:pTcLists){
						//真的不愿做这可恶又恶心到家的新辣道啦
						if(f.getPcode().equals(fd.getPcode())){
							int recomCntInitial = Integer.parseInt(f.getRecommendCnt());
							int minusCnt = Integer.parseInt(fd.getPcount());
							int endRecommandCnt = recomCntInitial - minusCnt;
							f.setRecommendCnt(endRecommandCnt+"");
						}
					}
				}
			}
			
		}
	}
	
	/**
	 * 将组合的套餐添加进单例集合中
	 * @param pFood
	 */
	private List<Food> addIntoSingletonLists(Food pFood) {
		
		//存放每一层所选择的菜品
		List<Food> tcItemLists = new ArrayList<Food>();
		
		for(List<List<Food>> tcLists :entireTCOrderLists){
			if(!tcLists.get(0).get(0).getTpcode().equals(pFood.getPcode())){
				continue;
			}
			
			for(List<Food> layerLists:tcLists){
				
				int countLayerFlag = 0;//每一组最大数量的限制标示
				
				int maxCntLayer = 0;
				for(Food f:layerLists){
					if(f.getDefalutS().equals("0")){
						maxCntLayer = Integer.parseInt(f.getMaxCnt());
					}else{
						//每一道菜的mincnt如果>1，就得优先确保这道菜被选中
						if(Integer.parseInt(f.getMinCnt()) > 0){
							f.setPcount(f.getMinCnt());
							tcItemLists.add(f);
							countLayerFlag += Integer.parseInt(f.getPcount());
						}
					}
					
				}
				
				//再将普通的菜品添加进去
				for(Food mFood:layerLists){
					if(!mFood.getDefalutS().equals("0") && Integer.parseInt(mFood.getMinCnt()) <= 0){
						if(ValueUtil.isNotEmpty(mFood.getRecommendCnt()) && Integer.parseInt(mFood.getRecommendCnt()) > 0){
							mFood.setPcount("1");//此处很特殊，
							countLayerFlag += Integer.parseInt(mFood.getPcount());
							
							tcItemLists.add(mFood);
							
							//在这里作出判断，如果达到该组的最大数量，就break
							if(countLayerFlag >= maxCntLayer){
								break;
							}
						}
					}
				}
			}
		}
		
		return tcItemLists;
		
	}

	/**
	 * 推荐套餐显示最后一层
	 * @param food
	 */
	protected void showTcLastLayer(final Food food,SelectPicPopupWindow popupWindow) { 
		final List<Food> lastLayerLists = new ArrayList<Food>();//该集合存储最后的没有点的那一层
		for(List<List<Food>> tcLists :entireTCOrderLists){
			if(!tcLists.get(0).get(0).getTpcode().equals(food.getPcode())){
				continue;
			}
			
			//获取最后一层,如果那一层没有recommendCnt,就表示是那一层,你知道吗？你没有成功
			for(List<Food> layerLists:tcLists){
				boolean isNeedLastLayer = true;
				for(Food f:layerLists){
					if(ValueUtil.isNotEmpty(f.getRecommendCnt()) && !f.getRecommendCnt().equals("0")){
						isNeedLastLayer = false;
						break;
					}
				}
				
				if(isNeedLastLayer){
					lastLayerLists.addAll(layerLists);
				}
				
			}
			
		}	
		
		
		cn.com.choicesoft.view.TcGridView tcGridView = new cn.com.choicesoft.view.TcGridView(Eatables.this);
		tcGridView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		if(ChioceActivity.ispad) {
			tcGridView.setNumColumns(5);
		}else{
			tcGridView.setNumColumns(3);
		}
		tcGridView.setDrawSelectorOnTop(true);
		tcGridView.setVerticalSpacing(5);
		tcGridView.setHorizontalSpacing(5);
		tcGridView.setBackgroundColor(Color.parseColor("#ADD8E6"));
		tcGridView.setSelector(R.color.transparent);
		
		EatableSubGridViewAdapter subGridAdapter = new EatableSubGridViewAdapter(Eatables.this, lastLayerLists,"");
		tcGridView.setAdapter(subGridAdapter);
		
		tcGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				
				Food foodItem = lastLayerLists.get(position);	//将这个对象加入到单例集合中
				
				//将此套餐的明细菜品放入到单例集合中
				List<Food> lists = addIntoSingletonLists(food);
				
				//将套餐明细大集合中的数据进行删除更新
				updateEntireListTcData(food, entireTCOrderLists, lists);
				
				//更新左边菜品类别及菜品的数量，界面上
				updateViewDishData(lists, dishLists);

				
				//刷新界面
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
		});
		
		popupWindow.addToView(tcGridView);
		
	}
	
	//================================查询套餐必选附加项目

	/**
	 * 获取必选附加项
	 */
    public void showCommonFujia(){
        if(tempFujiaFood==null){
            Toast.makeText(Eatables.this, R.string.you_not_choice_food, Toast.LENGTH_SHORT).show();
            return;
        }
        new FujiaTypeView(this,new IResult<FujiaType>() {
            @Override
            public void result(FujiaType type) {
                String fj = "select INIT,PCODE,FCODE as FOODFUJIA_ID,FNAME AS FOODFUJIA_DES,\n" +
                        "       Fprice as FOODFUJIA_CHECKED,'false' as ISSELECTED,\n" +
                        "              MAXCNT,MINCNT,PRODUCTTC_ORDER,DEFUALTS,ISADDPROD,\n" +
                        "                     ISMUST from FoodFuJia where pcode=? and rgrp=? order by DEFUALTS";
                List<Addition> list=new ListProcessor().query(fj, new String[]{tempFujiaFood.getPcode(),type.getPk_redefine_type()}, Eatables.this, Addition.class);
                if(ValueUtil.isEmpty(list)){
                    fj = "select INIT,PCODE,FCODE as FOODFUJIA_ID,FNAME AS FOODFUJIA_DES,\n" +
                            "       Fprice as FOODFUJIA_CHECKED,'false' as ISSELECTED,\n" +
                            "              MAXCNT,MINCNT,PRODUCTTC_ORDER,DEFUALTS,ISADDPROD,\n" +
                            "                     ISMUST from FoodFuJia where (pcode is null or pcode like '%PCODE%' or PCODE ='') and rgrp=? order by DEFUALTS";
                    list=new ListProcessor().query(fj, new String[]{type.getPk_redefine_type()}, Eatables.this, Addition.class);
                }
                new ComFujiaView(Eatables.this,tempFujiaFood,type).showView(list);
            }
        }).showView(tempFujiaFood);
    }
    //=================================================================

    //================================点菜操作=============================

	/**
	 * 点单品操作
	 * @param foodItem
	 * @param view
	 */
    public void foodOrder(final Food foodItem,View view){
        int radioValue = Integer.parseInt(radioValueShow.getText().toString());//得到数字标题栏最右边的那个值
        if(!foodItem.isIstc()){
            tempFujiaFood = foodItem;//为哪一项菜品添加附加项做准备,针对于普通单品
        }
		if (radioValue==0){
			radioValueShow.setText("1");//将上面的数字标题栏还原
			digitalFlag = -1;
			foodItem.setSelected(false);//按钮选择
			foodItem.setCounts("");
			foodItem.setPcount("");

			List<Food> tempUnitLists = new ArrayList<Food>();
			for(Food food:guestLists){
				if(food.getPcode().equals(foodItem.getPcode())||(food.getTpcode()!=null&&food.getTpcode().equals(foodItem.getPcode()))){
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
			foodTotalPrice();
			eatListAdapter.notifyDataSetChanged();
		}else {
			foodIstc(foodItem,view);
		}
    }
	//判断是否套餐
	public void foodIstc(Food foodItem,View view){
		if (foodItem.isIstc()==true){
			radioValueShow.setText("1");//将上面的数字标题栏还原
			digitalFlag = -1;

			if(linearTcLayout.getVisibility() == View.VISIBLE){
				Toast.makeText(Eatables.this, R.string.tc_not_dismiss, Toast.LENGTH_SHORT).show();
				return;
			}

			if(!foodItem.isSelected()){
				foodItem.setSelected(true);
				foodItem.setCounts("1");
			}else{
				Float counts = Float.parseFloat(foodItem.getCounts());
				counts += 1;
				foodItem.setCounts(counts+"");
			}

			eatGridAdapter.notifyDataSetChanged();
			tcCallBack(foodItem);//对套餐明细进行操作
		}else {
			foodUnits(foodItem,view);
		}
	}
	//多单位
	public  void  foodUnits(final  Food foodItem, final View view){
		if(foodItem.getUnitMap()!=null&&foodItem.getUnitMap().size()>1) {
			IResult<Unit> ir = new IResult<Unit>() {
				@Override
				public void result(Unit unit) {
					foodItem.setUnit(unit.getUnitName());
					foodItem.setUnitCode(unit.getUnitCode());
					foodItem.setPrice(unit.getUnitPrice());
					foodIstemp(foodItem,view);
				}
			};
			new MuchUntiView(ir, Eatables.this, view).showView(foodItem);
		}else {
			foodIstemp(foodItem,view);
		}
	}
	//是否临时菜
	public  void  foodIstemp(final Food foodItem, final View view){
		if (foodItem.getIsTemp()==1){
			new TempFoodView(Eatables.this, foodItem, new TempFoodView.Result() {
				@Override
				public void handle(Food food) {
					foodState(foodItem,view);
				}
			}).show();
		}else {
			foodState(foodItem,view);
		}
	}
	//是否允许修改价格
	public  void foodState(final Food foodItem, final View view){
		if (foodItem.getState()==1){
			Dialog dialog = null;
			View layout = LayoutInflater.from(Eatables.this).inflate(R.layout.unitcur_layout, null);
			final EditText weightEdit = (EditText) layout.findViewById(R.id.dia_et_weightflag);

			AlertDialog.Builder builder = new AlertDialog.Builder(Eatables.this,R.style.edittext_dialog);
			builder.setTitle("请输入价格").setView(layout)
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String weightValue = weightEdit.getText().toString();
							foodItem.setPrice(weightValue);
							foodUnitCnt(foodItem,view);
						}
					}).setNegativeButton(R.string.cancle, null);

			dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		}else {
			foodUnitCnt(foodItem,view);
		}
	}
	//是否第二单位
	public  void foodUnitCnt(final Food foodItem, final View view){
		if (foodItem.getWeightflg().equals("2")){
			Dialog dialog = null;
			View layout = LayoutInflater.from(Eatables.this).inflate(R.layout.unitcur_layout, null);
			final EditText weightEdit = (EditText) layout.findViewById(R.id.dia_et_weightflag);

			AlertDialog.Builder builder = new AlertDialog.Builder(Eatables.this,R.style.edittext_dialog);
			builder.setTitle(R.string.second_unit_weight).setView(layout)
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String weightValue = weightEdit.getText().toString();
							foodItem.setWeight(weightValue);
//							pFood.setWeight(weightValue);
							foodFujiaModeOrIsAddPro(foodItem,view);
						}
					}).setNegativeButton(R.string.cancle, null);

			dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		}else {
			foodFujiaModeOrIsAddPro(foodItem,view);
		}
	}
	//必须附加项或附加产品
	public void foodFujiaModeOrIsAddPro(Food foodItem,View view){
		if (foodItem.getFujiaModel().equals(1)){
			new MustFuJiaView(foodItem, Eatables.this,view).showView();
		}else {
			foodDis(foodItem);
		}
	}
	//点菜
    public void foodDis(Food foodItem){
        int radioValue = Integer.parseInt(radioValueShow.getText().toString());
		for(Grptyp grpTyp:grpLists){
			if(grpTyp.getGrp().equals(foodItem.getSortClass())){
				grpTyp.setQuantity((Double.parseDouble(grpTyp.getQuantity())+radioValue)+"");
				eatListAdapter.notifyDataSetChanged();
				break;
			}
		}
		Boolean foodTag = false;
		if (foodItem.isSelected()){
			for (Food f : guestLists){
				if (foodItem.getPcode().equals(f.getPcode())){
					if (foodItem.getFujianame()==null||foodItem.getFujianame().equals(f.getFujianame())){
						if (foodItem.getUnitCode()==null||foodItem.getUnitCode().equals(f.getUnitCode())){
							foodItem.setCounts(radioValue + ValueUtil.isNaNofDouble(foodItem.getCounts()) + "");
							f.setPcount(radioValue + ValueUtil.isNaNofDouble(f.getPcount()) + "");
							foodTag=true;
							break;
						}
					}

				}
			}
		}
		if (!foodTag){
			foodItem.setSelected(true);
			foodItem.setCounts(radioValue+ValueUtil.isNaNofDouble(foodItem.getCounts())+"");
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

		foodTotalPrice();
        radioValueShow.setText("1");
        digitalFlag = -1;
        eatGridAdapter.notifyDataSetChanged();


    }
	//显示菜品价格
	private void foodTotalPrice(){
		Double foodTotalPrice=0.00;
		for (Food food:guestLists){
			if (food.isIstc()==false||(food.isIstc()==true&&food.getTpcode()!=food.getPcode()))
				foodTotalPrice+=(Double.parseDouble(food.getPrice())*Double.parseDouble(food.getPcount()));
		}
		DecimalFormat df = new DecimalFormat("0.00");
		if (foodTotalPrice>0) {
			yiDianBtn.setText("已点菜(" + df.format(foodTotalPrice) + ")");
		}else {
			yiDianBtn.setText(R.string.eatable_yidiancai);
		}
	}
}
