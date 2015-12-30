package cn.com.choicesoft.chinese.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.BaseActivity;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.adapter.CommonFujiaAdapter;
import cn.com.choicesoft.adapter.CommonFujiaAdapter.ComFujiaHolder;
import cn.com.choicesoft.adapter.SingleFujiaAdapter;
import cn.com.choicesoft.adapter.SingleFujiaAdapter.SingleFujiaHolder;
import cn.com.choicesoft.bean.*;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseResultAlt;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.chinese.util.OrderSaveUtil;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.*;
import cn.com.choicesoft.view.PresentAuthAndCountDialog.DialogPresentAuthAndCountClickListener;
import cn.com.choicesoft.view.PresentAuthDialog.DialogPresentAuthClickListener;
import cn.com.choicesoft.view.PresentCountDialog.DialogPresentCountClickListener;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 已点菜品页面
 * @author dell
 *
 */
public class ChineseYiDianDishActivity extends BaseActivity implements OnClickListener{
	
	
	private ExpandableListView expandListView;//页面正常显示数据绑定的控件是ExpandableListView
	private YidianDishAdapter adapter;//ExpandableListView对应的适配器
	
	
	private ListView listView;//搜索时数据显示的控件是ListView
	private YiDianDishSearchAdapter searchAdapter;//ListView对应的适配器
	
	private Button tablelogo;//台位
	private Button tempSave;//暂存,保存
	private Button allBillAddtions ;//特别备注
	private Button allBills;//全单
	private Button sendwait;//叫起
	private Button sendprompt ;//即起
	private Button back;//返回
	private Button deleteAll;//全部删除
	
	private Button btnSearchView;
	private EditText etSearchView;//搜索框
	
	private List<Food> guestDishes = GuestDishes.getGuDishInstance();
	
	private List<Food> adapterDataSourceLists = null;//expandablelistview的数据源
	
	private SingleMenu menu = SingleMenu.getMenuInstance();

	private LoadingDialog authLoadingDialog;//验证授权 
	private LoadingDialog sendLoadingDialog;//发送菜品
	
	private TextView dishCounts,totalMoney,fujiaMoney;
	
	private TextView tableNum,orderNum,peopleNum,userNum;//头部红色标题栏内容
	private List<PresentReason> presentreasonLists;//存放赠品集合
	private List<Food> matchLists = new ArrayList<Food>();//菜品信息检索完后的结果所匹配的集合
	private List<Addition> fujiamatchLists = new ArrayList<Addition>();//附加项信息检索完后的结果所匹配的集合
	private List<CommonFujia> specialfujiaMatchLists = new ArrayList<CommonFujia>();//全单附加项信息检索完后的结果所匹配的集合
	
	private LoadingDialog mLoadingDialog = null;
	
	private Dialog dialog = null;
	
	private LinearLayout digitalLayout = null;
	private ImageView menuinfo_imageView;
	private View layout;
	private LinearLayout topLinearLayout;
	public static  int screemwidth;
	
	
	
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screemwidth = getWindowManager().getDefaultDisplay().getWidth();
		if (ChioceActivity.ispad) {
			setContentView(R.layout.chinese_add_dishes_activity_pad);
		} else {
			setContentView(R.layout.chinese_add_dishes_activity);
		}
		initViews();
		initData();
	}

	/**
	 * 界面更新
	 */
	private void updateDisplay() {
		
		adapterDataSourceLists = guestDishes;
		
		if(adapter == null){
			adapter = new YidianDishAdapter(this, adapterDataSourceLists);
			expandListView.setAdapter(adapter);
		}else{
			adapter.setSingleInstance(adapterDataSourceLists);
			adapter.notifyDataSetChanged();
			
		}
		
		dishCounts.setText(adapter.getDishSelectedCount()+this.getString(R.string.part));//所点菜品的总数量
		
		double totalSalary = adapter.getTotalSalary();
		totalMoney.setText(ValueUtil.setScale(totalSalary, 2, 5));//所点菜品的总价钱
		
		double totalFujiaSalary = adapter.getFujiaSalary();
		fujiaMoney.setText(ValueUtil.setScale(totalFujiaSalary, 2, 5));//所点菜品附加项的总价钱
	}

	/**
	 * 控件初始化
	 */
	private void initViews() {
		
		mLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
		mLoadingDialog.setCancelable(false);
		
		digitalLayout = (LinearLayout) this.findViewById(R.id.toptitle_rg_number);
		digitalLayout.setVisibility(View.GONE);
		
		//菜品：3          总计：179.00      附加项：
		dishCounts = (TextView) this.findViewById(R.id.yidian_tv_dishcount);
		totalMoney = (TextView) this.findViewById(R.id.yidian_tv_totalSalary);
		fujiaMoney = (TextView) this.findViewById(R.id.yidian_tv_fujiaMoney);
				

		
		tablelogo = (Button) this.findViewById(R.id.yidian_btn_tbl);
		tablelogo.setOnClickListener(this);
		
		tempSave = (Button) this.findViewById(R.id.yidian_btn_tempsave);
		tempSave.setOnClickListener(this);
		
		allBillAddtions = (Button) this.findViewById(R.id.yidian_btn_allbilladditions);
		allBillAddtions.setOnClickListener(this);
		
		allBills = (Button) this.findViewById(R.id.yidian_btn_allbills);
		allBills.setOnClickListener(this);
		
		sendwait = (Button) this.findViewById(R.id.yidian_btn_allWaitOrPrompt);
		sendwait.setOnClickListener(this);
		
		sendprompt = (Button) this.findViewById(R.id.yidian_btn_sendprompt);
		sendprompt.setOnClickListener(this);


				


		deleteAll = (Button) this.findViewById(R.id.yidian_btn_deleteall);
		deleteAll.setOnClickListener(this);
		
		btnSearchView = (Button) this.findViewById(R.id.btn_searchView);
		btnSearchView.setOnClickListener(this);
		if (SingleMenu.getMenuInstance().getPermissionList()!=null) {
			for (Map<String, String> map : SingleMenu.getMenuInstance().getPermissionList()) {
				if (map.get("CODE").equals("31004")){
					sendprompt.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						sendprompt.setVisibility(View.GONE);
					}
				}
				if (map.get("CODE").equals("31002")){
					allBillAddtions.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						allBillAddtions.setVisibility(View.GONE);
					}
				}
				if (map.get("CODE").equals("31001")){
					tempSave.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						tempSave.setVisibility(View.GONE);
					}
				}
				if (map.get("CODE").equals("31003")){
					allBills.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						allBills.setVisibility(View.GONE);
					}
				}
				if (map.get("CODE").equals("31005")){
					sendwait.setTag(map.get("AUTHORITY"));
					if (map.get("ISSHOW").equals("0")) {
						sendwait.setVisibility(View.GONE);
					}
				}
			}
		}
		
		etSearchView =  (EditText) this.findViewById(R.id.et_searchView);
		etSearchView.addTextChangedListener(watcher);
		
		expandListView =  (ExpandableListView) this.findViewById(R.id.yidiandish_expandlist_menu);
		listView = (ListView) this.findViewById(R.id.yidiandish_list_menu);
		
		sendLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.food_sending));
		sendLoadingDialog.setCancelable(true);
		
		authLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.authorization_ing));
		authLoadingDialog.setCancelable(true);

        //顶部红色栏
        if (ChioceActivity.ispad) {
            tableNum = (TextView) this.findViewById(R.id.dishesact_tv_tblnum);
            orderNum = (TextView) this.findViewById(R.id.dishesact_tv_ordernum);
            peopleNum = (TextView) this.findViewById(R.id.dishesact_tv_people);
            userNum = (TextView) this.findViewById(R.id.dishesact_tv_usernum);
            back = (Button) this.findViewById(R.id.yidian_btn_back);
        }else {
            // TODO
            back = (Button) this.findViewById(R.id.eatable_btn_back);
            menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
            menuinfo_imageView.setClickable(true);
            menuinfo_imageView.setOnClickListener(this);
            layout = LayoutInflater.from(ChineseYiDianDishActivity.this).inflate(R.layout.chinese_menu_info, null);
            tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
            orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
            peopleNum = (TextView) layout.findViewById(R.id.dishesact_tv_people);
            userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
            topLinearLayout = (LinearLayout) findViewById(R.id.topLinearLyout);
        }
        back.setOnClickListener(this);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}

	private void initData() {
		
		tableNum.setText(menu.getTableNum());
		orderNum.setText(menu.getMenuOrder());
		peopleNum.setText(ValueUtil.isEmpty(menu.getManCounts())?"0":menu.getManCounts());
		userNum.setText(SharedPreferencesUtils.getOperator(this));
				
		updateDisplay();
		//TODO 中餐不需要
		//presentreasonLists = getDataManager(this).queryPresentReason();//查询数据库得出赠菜的原因
		
		
		//TODO 初始化会员信息 中餐不需要
		//VipMsg.iniVip(this, menu.getTableNum(),R.id.vipMsg_ImageView);
		
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
		case R.id.eatable_btn_back://返回
        case R.id.yidian_btn_back://返回
            Intent intent = new Intent(ChineseYiDianDishActivity.this, ChineseEatables.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("direction", "XS");
            startActivity(intent);
            break;

		case R.id.yidian_btn_deleteall://全部删除
			guestDishes.clear();
			updateDisplay();
			break;
			
		case R.id.yidian_btn_allWaitOrPrompt:  //叫起发送
			String call="";
			for (int i=0;i<adapterDataSourceLists.size();i++){
				if (i==0){
					if (adapterDataSourceLists.get(i).getCLASS().equals("Y")){
						call="N";
					}else {
						call = "Y";
					}
				}
				adapterDataSourceLists.get(i).setCLASS(call);
			}
			updateDisplay();//更新界面View
			break;
			
		case R.id.yidian_btn_sendprompt: //即起发送
			if(TsData.isReserve){
				alertReserve(this.getString(R.string.only_save_error));
				break;
			}
            guQing("N");
			break;
					
		case R.id.yidian_btn_allbills:    //全单
			startActivity(new Intent(ChineseYiDianDishActivity.this, ChineseYiXuanDishActivity2.class).putExtra("table",menu.getTableNum()));
			break;
			
		case R.id.yidian_btn_allbilladditions://特别备注
			
			final List<CommonFujia>  commonFujiaLists = getDataManager(this).queryCommonFujia();//存放公共附加项集合
			
			View commonLayout = LayoutInflater.from(ChineseYiDianDishActivity.this).inflate(R.layout.dia_common_cover, null);
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
					InputMethodUtils.toggleSoftKeyboard(ChineseYiDianDishActivity.this);

				}
			});
			
			//自定义附加项的控件EditText
			final EditText selfFujiaEdit = (EditText) commonLayout.findViewById(R.id.dia_quandanfujia_et_selffujia);
            selfFujiaEdit.addTextChangedListener(new TextWatcher() {
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
                        selfFujiaEdit.setText(str);
                        selfFujiaEdit.setSelection(str.length());
                    }
                }
            });
			
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
                    String comFujiaStr = selfFujiaEdit.getText().toString();
                    for(Food food:adapterDataSourceLists) {
                        StringBuilder buildPrice = new StringBuilder();//编码
                        StringBuilder buildDes = new StringBuilder();//名称
                        List<String> fujia=food.getChineseFujia();
                        if(fujia==null){
                            fujia=new ArrayList<String>();
                        }
                        if (ValueUtil.isNotEmpty(comFujiaStr)) {
                            buildPrice.append("0").append("!");
                            buildDes.append(comFujiaStr).append("!");
                            fujia.add(comFujiaStr+"|0|");
                            selfFujiaEdit.setText("");
                        }
                        List<CommonFujia> listComs = comFujiaAdapter.getSelectedItemLists();
                        if (listComs.size() != 0) {
                            for (CommonFujia comFujia : listComs) {
                                buildPrice.append(comFujia.getPRICE()).append("!");
                                buildDes.append(comFujia.getDES()).append("!");
                                String des=ValueUtil.isEmpty(comFujia.getDES())?"":comFujia.getDES();
                                String price=ValueUtil.isEmpty(comFujia.getPRICE())?"":comFujia.getPRICE();
                                fujia.add(des+"|"+price+"|");
                            }
                        }
                        if(buildPrice.length()>0){
                            buildPrice.deleteCharAt(buildPrice.length()-1);
                        }
                        if(buildDes.length()>0){
                            buildDes.deleteCharAt(buildDes.length()-1);
                        }
                        food.setFujiaprice(buildPrice.toString());
                        food.setFujianame(buildDes.toString());
                        food.setChineseFujia(fujia);
                    }
                    dialog.dismiss();
                    //隐藏软键盘
                    InputMethodUtils.hideSoftKeyboard(ChineseYiDianDishActivity.this,selfFujiaEdit);
                    updateDisplay();//更新页面
                }
            });
			
			Button buttonCancel = (Button) commonLayout.findViewById(R.id.dia_quandanfujia_btn_cancel);
			buttonCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
                    //隐藏软键盘
                    InputMethodUtils.hideSoftKeyboard(ChineseYiDianDishActivity.this,selfFujiaEdit);
				}
			});
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(ChineseYiDianDishActivity.this,R.style.edittext_dialog);
			builder.setView(commonLayout);
			
			dialog = builder.create();
			dialog.show();
			break;
			
			
		case R.id.yidian_btn_tempsave:  //保存
			if(TsData.isReserve){
				//TODO 发送
                break;
			}
            boolean insertSuccess = new OrderSaveUtil().insertHandle(ChineseYiDianDishActivity.this,menu.getTableNum(),menu.getMenuOrder(),guestDishes);
            if(insertSuccess){
                TsData.isSave=false;
                Toast.makeText(ChineseYiDianDishActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
            }else{
                TsData.isSave=true;
                Toast.makeText(ChineseYiDianDishActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
            }
			break;
			
		case R.id.yidian_btn_tbl:   //台位
			showIfSaveDialog().show();//弹出提示对话框，提示用户是否保存
			break;
		case R.id.orderinfo_ImageView:
			// TODO   
			View layout = null ;
			layout = LayoutInflater.from(ChineseYiDianDishActivity.this).inflate(R.layout.chinese_menu_info, null);
			tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
			orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
			peopleNum = (TextView) layout.findViewById(R.id.dishesact_tv_people);
			userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
			initData();
			AlertDialog tipdialog = new AlertDialog.Builder(ChineseYiDianDishActivity.this, R.style.Dialog_tip).setView(layout).create();
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
					for(int i = 0;i<guestDishes.size();i++){
						guestDishes.get(i).setSend("0");
					}
					boolean insertSuccess = new OrderSaveUtil().insertHandle(ChineseYiDianDishActivity.this,menu.getTableNum(),menu.getMenuOrder(),guestDishes);
					if(insertSuccess){
						Toast.makeText(ChineseYiDianDishActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(ChineseYiDianDishActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
					}
					guestDishes.clear();//清空singleton集合
					Intent intent = new Intent(ChineseYiDianDishActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
					break;
					
				case 1:
					guestDishes.clear();//清空singleton集合
					Intent it = new Intent(ChineseYiDianDishActivity.this, MainActivity.class);
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
	 * 判断插入数据库是否成功
	 * @param listFoods
	 * @return
	 */
	protected boolean insertDatabases(List<Food> listFoods) {
		
		return getDataManager(this).circleInsertAllCheck(listFoods);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(ChineseYiDianDishActivity.this, ChineseEatables.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 估清验证
	 * @param type
	 * @return
	 */
    public boolean guQing(final String type){
        //如果还没有点菜，就提示
        if(ValueUtil.isEmpty(adapterDataSourceLists)||adapterDataSourceLists.size() == 0){
            Toast.makeText(this, R.string.not_send_before_dian, Toast.LENGTH_SHORT).show();
            return false;
        }
        StringBuffer sb=new StringBuffer();
        freeGet(sb);
        if(ValueUtil.isEmpty(sb)){
            ToastUtil.toast(this,R.string.food_data_error);
            return false;
        }
        CList<Map<String,String>> list=new CList<Map<String, String>>();
        list.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        list.add("user",SharedPreferencesUtils.getUserCode(this));
        list.add("pass","");
        list.add("grantEmp",menu.getTableNum());
        list.add("grantPass","1");
        list.add("oSerial",sb.toString());
        list.add("rsn","");
        list.add("oStr","");
        new ChineseServer().connect(this,ChineseConstants.PFREEGET,list,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                sendLoadingDialog.dismiss();
                if(ValueUtil.isNotEmpty(result)){
                    Map<String,String> map=new ComXmlUtil().xml2Map(result);
                    if(ValueUtil.isNotEmpty(map)&&"1".equals(map.get("result"))){
                        send(type);
                    }else{
                        if(ValueUtil.isEmpty(map)){
                            ToastUtil.toast(ChineseYiDianDishActivity.this,R.string.net_error);
                            return;
                        }
                        new AlertDialogUtil().AlertDialog(ChineseYiDianDishActivity.this,getString(R.string.gu_qing_food),ChineseResultAlt.oStrAlt(map.get("oStr")));
                    }
                }
            }
            @Override
            public void onBeforeRequest() {
                sendLoadingDialog.show();
            }
        });
        return true;
    }
    /**
     * 菜品数据拼接
     * @param sb
     * @return
     */
    public String freeGet(StringBuffer sb){
        try{
            Integer dataCount=0;
            for (Food food : adapterDataSourceLists) {
                if (food.getPcode() != null && food.isIstc()&&food.getPcode().equals(food.getTpcode())) {
                    food.getPcount();
                    continue;
                }
                sb.append(food.getPcode()).append("^").append(food.getPcount()).append(";");
                dataCount++;
            }
            return dataCount.toString();
        }catch (Exception e){
            CSLog.e("菜品数据整理出错",e.toString());
        }
        return null;
    }

    /**
     * 菜品发送
     * @param typ
     */
	public void send(String typ){
        StringBuffer sb=new StringBuffer();
        String dataCount=dataTrim(sb);
        if(ValueUtil.isEmpty(sb)){
            ToastUtil.toast(this,R.string.food_data_error);
            return ;
        }
        Log.e("中餐菜品",dataCount.toString());
        Log.e("中餐菜品数据",sb.toString());
        CList<Map<String,String>> cList=new CList<Map<String, String>>();
        cList.add("pdaid",SharedPreferencesUtils.getDeviceId(this));//pad编码
        cList.add("user",SharedPreferencesUtils.getUserCode(this));//用户编码
        cList.add("pdaSerial","0");//上传菜品次数，这个值为一共上传了多少菜品，必须保存在机器上
        cList.add("acct",menu.getMenuOrder());//账单编号，有账单编号就填写，没有为空值
        cList.add("tblInit",menu.getTableNum());//台位缩写
        cList.add("waiter","");//服务员编号
        cList.add("pax","1");//人数 TODO
        cList.add("zcnt",dataCount);//上传菜品数量
        cList.add("typ",typ);//上传类型（Y,N,A Y叫起N即起A补单）
        cList.add("buffer",sb.toString());//上传值
        cList.add("oStr","");
        new ChineseServer().connect(this, ChineseConstants.PSENDTAB,cList,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                sendLoadingDialog.dismiss();
                if(ValueUtil.isNotEmpty(result)){
                    Map<String,String> map=new ComXmlUtil().xml2Map(result);
                    if(ValueUtil.isNotEmpty(map)&&"1".equals(map.get("result"))){
                        startActivity(new Intent(ChineseYiDianDishActivity.this, ChineseYiXuanDishActivity2.class).putExtra("table", menu.getTableNum()).putExtra("direction", "MainDirection"));
                        ToastUtil.toast(ChineseYiDianDishActivity.this,R.string.success);
                        //实体类food中的字段send代表是已发送菜品，还是暂存   ，为其赋值             已发送1          暂存0
                        for(int i = 0;i<guestDishes.size();i++){
                            guestDishes.get(i).setSend("1");
                        }
                        new OrderSaveUtil().delHandle(ChineseYiDianDishActivity.this,menu.getTableNum());
                        GuestDishes.getGuDishInstance().clear();
                    }else{
                        ToastUtil.toast(ChineseYiDianDishActivity.this, R.string.send_unsuccess);
                    }
                }
            }
            @Override
            public void onBeforeRequest() {
                sendLoadingDialog.show();
            }
        });
    }

    /**
     * 菜品数据拼接
     * @param sb
     * @return
     */
	public String dataTrim(StringBuffer sb){
        try{
            /*套餐编号(单品传-1)
            套餐数量(单品传0)|
                    菜品编 码|
                    菜品数量(第一单位)|
                    菜品 单位(第一单位)|
                    菜品单价|
                    菜 品数量(第二单位)|*/
            String tcount = null;
            Integer dataCount = 0;
            for (Food food : adapterDataSourceLists) {
                if (food.getPcode() != null && food.isIstc()&&food.getPcode().equals(food.getTpcode())) {
                    tcount = food.getPcount();
                    continue;
                }
                if (food.isIstc()) {//如果是套餐填入套餐编码，否则-1；
                    sb.append(food.getTpcode() + "|");//套餐编码
                    sb.append(tcount + "|");//套餐数量
                } else {
                    sb.append("-1|");//套餐编码
                    sb.append("0|");//套餐数量
                }
                sb.append(food.getPcode() + "|")
                        .append(food.getPlusd() != 0 ? "0.0" : food.getPcount()).append("|")
                        .append(food.getUnit() + "|");
                sb.append("0.0|").append(food.getPlusd()!=0 ? food.getPcount() : "0.0").append("|");
                if(food.getChineseFujia()!=null){
                    for(int i=0;i<5;i++){
                        if(i<food.getChineseFujia().size()) {
                            sb.append(food.getChineseFujia().get(i));
                        }else{
                            sb.append("||");
                        }
                    }
                }else{
                    sb.append("||||||||||");
                }
                sb.append("0|")
                        .append(food.getFoodIndex()+"|")
                        .append(food.getFoodMark()+"|")
                        .append(food.getPlusd()==0?"|":"只|")//第二单位
                        .append("N|")

                        .append(food.getPKID()+"|")//唯一标识
                        .append((food.getCLASS()==null?"N":food.getCLASS())+"|^");//即起叫起 Y叫起

                Log.i("不知道", food.getPlusd()==0?"|":food.getUnit2()+"|");
                Log.i("不", sb.toString());
                dataCount++;
            }
            return dataCount.toString();
        }catch (Exception e){
            CSLog.e("菜品数据整理出错",e.toString());
        }
        return null;
    }
	/**
	 * ExpandableListView的适配器
	 * @author dell
	 *
	 */
	public class YidianDishAdapter extends BaseExpandableListAdapter{
		
		private Context mContext;
		private List<Food> groupAdapterLists = new ArrayList<Food>();//父目录对应的集合
		private List<List<Food>> childAdapterLists = new ArrayList<List<Food>>();//子目录对应的集合
		LayoutInflater  layoutInflater;
		
		private List<Food> integratedLists;//总的集合
		

		public YidianDishAdapter(Context pContext,List<Food> pLists) {
			super();
			
			this.integratedLists = pLists;
			this.mContext = pContext;
			layoutInflater = LayoutInflater.from(mContext);
			
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
				if(mFood.isIstc() && mFood.getTpcode().equals(mFood.getPcode())){
					groupAdapterLists.add(mFood);
					int count=0;
					List<Food> childList = new ArrayList<Food>();
					for(Food food:singleAllLists){
						if(food.isIstc() && !food.getTpcode().equals(food.getPcode()) && food.getTpcode().equals(mFood.getPcode()) && food.getTpnum().equals(mFood.getTpnum())){
							childList.add(food);
                            count+=ValueUtil.isNaNofDouble(food.getPcount());
						}
					}
                    mFood.setDtlCount(count+"");
					childAdapterLists.add(childList);
					
				}else if(!mFood.isIstc()){
					groupAdapterLists.add(mFood);
					List<Food> childList = new ArrayList<Food>();
					childAdapterLists.add(childList);
				}
			}
		}
		
		//重新绑定数据源
		public void setSingleInstance(List<Food> changedLists){
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
			double totalFujiaSalary = 0.00;
			for(Food f:guestDishes){
				if(ValueUtil.isNotEmpty(f.getChineseFujia())){//表明该菜品有附加项,自定义附加项价格为0，不考虑
					for(String fj:f.getChineseFujia()){
						//如果是自定义附加项，价格为“”，就报错,必须判断一下
                        String[] fjx=fj.split("\\|");
						totalFujiaSalary += ValueUtil.isNaNofDouble((ValueUtil.isNotEmpty(fjx)&&fjx.length<=1)?"0":fj.split("\\|")[1]);
					}
				}
			}
			return totalFujiaSalary;
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

            TextView jijiao= (TextView) convertView.findViewById(R.id.yidian_text_jijiao);
            TextView nameChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesname);//菜品名称
			TextView countsChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishescounts);//数量
			TextView priceChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesprice);//价格
			TextView unitChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesunit);//单位
			ImageView additionChildImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_child_addition);//添加

			//第二行的套餐明细的附加项
			LinearLayout singleAddChildLayout = (LinearLayout) convertView.findViewById(R.id.yidian_expand_item_child_linear);
			TextView singleAddChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_show);
			
			//如果有附加项就显示，没有就隐藏第二行
			if(ValueUtil.isNotEmpty(food.getChineseFujia())){
				singleAddChildLayout.setVisibility(View.VISIBLE);
                String fujia="";
				for(String f:food.getChineseFujia()){
                    fujia+=f;
                }
				singleAddChildText.setText(fujia);
			}else{
				singleAddChildLayout.setVisibility(View.GONE);
			}

			nameChildText.setText(food.getPcname());
			countsChildText.setText(food.getPcount());//这里显示套餐明细每道菜品的数量
			priceChildText.setText(food.getPrice());
			unitChildText.setText(food.getUnit());

            if(ValueUtil.isEmpty(food.getPKID())){
                food.setPKID(ValueUtil.createPKID(ChineseYiDianDishActivity.this));
            }
            if(ValueUtil.isEmpty(food.getCLASS())||food.getCLASS().trim().equals("N")){
                food.setCLASS("N");
                jijiao.setText(R.string.ji);
                jijiao.setTextColor(Color.GREEN);
            }else {
                jijiao.setText(R.string.jiao);
                jijiao.setTextColor(Color.RED);
            }

            jijiao.setOnClickListener(new JiJiaoClick(food));
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
			
			expandListView.setGroupIndicator(ChineseYiDianDishActivity.this.getResources().getDrawable(R.drawable.expandablelistview_selector));

            TextView jijiao= (TextView) convertView.findViewById(R.id.yidian_text_jijiao);
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
				LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(screemwidth/7,screemwidth/14);
				lp2.setMargins(3, 3, 3, 5);
				countsText.setLayoutParams(lp2);
			}
			
			
			//套餐不能添加附加项，套餐明细能添加,将按钮设为不可点
			if(f.isIstc() && f.getPcode().equals(f.getTpcode())){
				additionImage.setEnabled(false);
                additionImage.setVisibility(View.INVISIBLE);
			}
			
			//如果有附加项就显示，没有就隐藏第二行
			LinearLayout singleAddShowLayout = (LinearLayout) convertView.findViewById(R.id.yidian_singlefujia_group_linear);
			TextView singleAddShowText = (TextView) convertView.findViewById(R.id.yidian_singlefujia_group_show);
            //如果有附加项就显示，没有就隐藏第二行
            if(ValueUtil.isNotEmpty(f.getChineseFujia())){
                singleAddShowLayout.setVisibility(View.VISIBLE);
                String fujia="";
                for(String food:f.getChineseFujia()){
                    fujia+=food.split("\\|")[0]+",";
                }
                singleAddShowText.setText(fujia);
            }else{
                singleAddShowLayout.setVisibility(View.GONE);
            }
			
			
			
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

            //=======================判断是否多单位======================
            if(ValueUtil.isNotEmpty(f.getUnitMap())){
                unitText.setOnClickListener(new DishUnitClick(f));
            }
            //============================================
			
			//如果涉及到赠菜，品名就复杂 
			if(ValueUtil.isNotEmpty(f.getPromonum())){  //代表此道菜有赠菜
				
				//分两种情况  套餐    普通单品
				if(f.isIstc()){
					StringBuilder sb = new StringBuilder();
					sb.append(f.getPcname())
					.append("-"+ChineseYiDianDishActivity.this.getString(R.string.give))
					.append(f.getPromonum());//因为套餐只有一份，套餐都是单份显示的，这里直接填1也可以
					
					nameText.setText(sb.toString());
				}else if(!f.isIstc()){
					StringBuilder sb = new StringBuilder();
					sb.append(f.getPcname())
					.append("-"+ChineseYiDianDishActivity.this.getString(R.string.give))
					.append(f.getPromonum());
					
					nameText.setText(sb.toString());
				}
			}else{//代表没有赠菜
				//String [] realPcname = f.getPcname().split("-");
				nameText.setText(f.getPcname());
			}
			
			
			countsText.setText(f.getPcount());//数量
			priceText.setText(f.getPrice());//价格
			unitText.setText(f.getUnit());//单位
			
			//给小计赋值, 只有套餐与普通单品才有小计，并赋值,套餐明细没有小计
			if(f.isIstc() && f.getPcode().equals(f.getTpcode()) || !f.isIstc()){
				double itemSubtotal = getItemSubtotal(f);
				subtotalText.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
			}


            if(ValueUtil.isEmpty(f.getPKID())){
                f.setPKID(ValueUtil.createPKID(ChineseYiDianDishActivity.this));
            }

            if(ValueUtil.isEmpty(f.getCLASS())||f.getCLASS().trim().equals("N")){
                f.setCLASS("N");
                jijiao.setText(R.string.ji);
                jijiao.setTextColor(Color.GREEN);
            }else {
                jijiao.setText(R.string.jiao);
                jijiao.setTextColor(Color.RED);
            }

			//为按钮设置监听
            jijiao.setOnClickListener(new JiJiaoClick(f));
			additionImage.setOnClickListener(new AdditionChildClick(f));
			plusImage.setOnClickListener(new PlusImageClick(f));
			minusImage.setOnClickListener(new MinusImageClick(f));
			presentImage.setOnClickListener(new PresentImageClick(f));
			deleteImage.setOnClickListener(new DeleteClick());
			deleteImage.setTag(groupPosition);//设置tag,不然就产生错位
			return convertView;
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
                new AlertDialog.Builder(ChineseYiDianDishActivity.this,R.style.edittext_dialog).setTitle(R.string.revamp_counts)
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
		//单品附加项的点击事件,非全单附加项
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


		/**
		 * 绑定赠菜按钮
		 */
		class PresentImageClick implements OnClickListener{
			private Food f;

			public PresentImageClick(Food f) {
				super();
				this.f = f;
			}


			@Override
			public void onClick(View v) {
                ToastUtil.toast(ChineseYiDianDishActivity.this,"中餐暂不支持赠送！");
                return;
			}
			
		}


		/**
		 * 绑定数量  - 按钮的点击类,这种加的情况只适用于普通单品，套餐不能加
		 */
		class MinusImageClick implements OnClickListener{
			
			private Food food;
			
			
			public MinusImageClick(Food food) {
				super();
				this.food = food;
			}


			@Override
			public void onClick(View v) {
				BigDecimal dishCounts = food.getPcount()==""?BigDecimal.valueOf(0):ValueUtil.isNaNofBigDecimal(food.getPcount());
				if(dishCounts.doubleValue() <= 1){
					groupAdapterLists.remove(food);
					Food tempFood = null;
					for(Food mFood:guestDishes){
						if(!mFood.isIstc() && mFood.getPcode().equals(food.getPcode())){
							tempFood = mFood;
						}
					}
					guestDishes.remove(tempFood);
				}else if(dishCounts.doubleValue()  > 1){
					dishCounts=dishCounts.subtract(BigDecimal.valueOf(1));
					food.setPcount(dishCounts.toString());
				}
				
				updateDisplay();//更新界面View
			}
			
		}

		/**
		 * 绑定数量  + 按钮的点击类,这种加的情况只适用于普通单品，套餐不能加
		 */
		class PlusImageClick implements OnClickListener{
			
			private Food food;

			public PlusImageClick(Food food) {
				super();
				this.food = food;
			}

			@Override
			public void onClick(View v) {
				BigDecimal dishCounts = ValueUtil.isNaNofBigDecimal(food.getPcount());
				dishCounts=dishCounts.add(BigDecimal.valueOf(1));
				food.setPcount(dishCounts.toString());
				
				updateDisplay();//更新界面View
			}
			
		}

		/**
		 * 绑定删除按钮的点击类
		 */
		class DeleteClick implements OnClickListener{
			

			@Override
			public void onClick(View v) {
				final int position = Integer.parseInt(String.valueOf(v.getTag()));
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ChineseYiDianDishActivity.this, R.style.edittext_dialog);
				builder.setMessage(R.string.if_delete_food)
				.setPositiveButton(R.string.delete_yc, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //将单例集合中所对应的数据删除
                        Food foodRemove = groupAdapterLists.get(position);
                        //从单例集合中删除该套餐及对应的明细
                        if (foodRemove.isIstc()) {
                            List<Food> tempLists = new ArrayList<Food>();
                            for (Food food : guestDishes) {
                                if (!(food.isIstc() && food.getTpcode().equals(foodRemove.getPcode()) && food.getTpnum().equals(foodRemove.getTpnum()))) {
                                    tempLists.add(food);
                                }
                            }
                            guestDishes.clear();
                            guestDishes.addAll(tempLists);
                            new OrderSaveUtil().delItemHandle(ChineseYiDianDishActivity.this, menu.getTableNum(), null, foodRemove.getTpcode(), null);
                        } else {     //从单例集合中删除该单品
                            List<Food> tempLists = new ArrayList<Food>();
                            for (Food mfood : guestDishes) {
                                if (!(!mfood.isIstc() && foodRemove.getPcode().equals(mfood.getPcode()) && mfood.getUnit().equals(foodRemove.getUnit()))) {
                                    tempLists.add(mfood);
                                }
                            }
                            guestDishes.clear();
                            guestDishes.addAll(tempLists);
                            new OrderSaveUtil().delItemHandle(ChineseYiDianDishActivity.this, menu.getTableNum(), foodRemove.getPcode(), null, foodRemove.getUnit());
                        }
                        updateDisplay();//更新界面
                    }
                })
				.setNegativeButton(R.string.cancle, null);
				
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
						Double countPresent = ValueUtil.isNaNofDouble(presentCount);
						Double countPcount = ValueUtil.isNaNofDouble(pFood.getPcount());
                        Double countPcountLeft = countPcount - countPresent;//点菜的总数量，减去赠送菜品的数量，得到剩下的数量
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
		int moneyAuth = Integer.parseInt(SharedPreferencesUtils.getGiftAuth(ChineseYiDianDishActivity.this));
		
		//先判断所点菜品的数量
		Double dishCount = ValueUtil.isNaNofDouble(pFood.getPcount());
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
				Double countGiftBefore = ValueUtil.isNaNofDouble(pFood.getPromonum());//得到先前已经赠送的数量
				Double countGiftNowPre = ValueUtil.isNaNofDouble(promCounts);
				Double countGiftEnd = countGiftBefore + countGiftNowPre;
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
				clists.add("deviceId", SharedPreferencesUtils.getDeviceId(ChineseYiDianDishActivity.this));
				clists.add("userCode", authorNum);
				clists.add("userPass", authorPwd);
				new Server().connect(ChineseYiDianDishActivity.this, Constants.FastMethodName.CHECKAUTH_METHODNAME, Constants.FastWebService.CHECKAUTH_WSDL, clists, new OnServerResponse() {


                    @Override
                    public void onResponse(String result) {
                        authLoadingDialog.dismiss();
                        if (ValueUtil.isNotEmpty(result)) {
                            String[] flag = result.split("@");
                            if (flag[0].equals("0")) {
                                //授权成功
                                Log.e("授权返回值", result);
                                if (ValueUtil.isNotEmpty(countgift) && ValueUtil.isNotEmpty(countCancel)) {
                                    Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
                                } else if (ValueUtil.isNotEmpty(countgift) && ValueUtil.isEmpty(countCancel)) {//表示赠菜
                                    String promonumgiftBefore = dianDishes.getPromonum();//得到先前已经赠送的数量

                                    if (ValueUtil.isNotEmpty(promonumgiftBefore) && ValueUtil.isNaNofDouble(promonumgiftBefore) + ValueUtil.isNaNofDouble(countgift) > ValueUtil.isNaNofDouble(dianDishes.getPcount()) || ValueUtil.isNaNofDouble(dianDishes.getPcount()) < ValueUtil.isNaNofDouble(countgift) || ValueUtil.isNaNofDouble(countgift) < 1) {
                                        Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    setFoodPromnumAndReason(dianDishes, guestDishes, reason, countgift);//这里将全集合guestDishes传了进去

                                    //如果当前处于搜索模式下
                                    if (listView.getVisibility() == View.VISIBLE && expandListView.getVisibility() == View.GONE) {
                                        etSearchView.setText("");
                                        listView.setVisibility(View.GONE);
                                        expandListView.setVisibility(View.VISIBLE);
                                    }

                                    updateDisplay();
                                } else if (ValueUtil.isEmpty(countgift) && ValueUtil.isNotEmpty(countCancel)) {//表示取消赠菜
                                    if (ValueUtil.isEmpty(dianDishes.getPromonum()) || ValueUtil.isNaNofDouble(dianDishes.getPromonum()) < ValueUtil.isNaNofDouble(countCancel) || ValueUtil.isNaNofDouble(countCancel) < 1) {
                                        Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    cancelFoodPromnum(dianDishes, guestDishes, countCancel);

                                    //如果当前处于搜索模式下
                                    if (listView.getVisibility() == View.VISIBLE && expandListView.getVisibility() == View.GONE) {
                                        etSearchView.setText("");
                                        listView.setVisibility(View.GONE);
                                        expandListView.setVisibility(View.VISIBLE);
                                    }
                                    updateDisplay();
                                } else {

                                }

                            } else {
                                Toast.makeText(ChineseYiDianDishActivity.this, R.string.not_jurisdiction, Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(ChineseYiDianDishActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
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
			Double presentBefore = ValueUtil.isNaNofDouble(pFood.getPromonum());//得到原有的赠送的数量
			Double cancelCountsInt = ValueUtil.isNaNofDouble(cancelCounts);//得到要取消赠送的数量
			Double countPromLeft = presentBefore - cancelCountsInt;
			
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
					Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
				}else if(ValueUtil.isNotEmpty(giftCounts) && ValueUtil.isEmpty(cancelGiftCounts)){//表示赠菜
					String promonumgiftBefore = dianDishes.getPromonum();//得到先前已经赠送的数量
					
					if(ValueUtil.isNotEmpty(promonumgiftBefore)&&ValueUtil.isNaNofDouble(promonumgiftBefore) + ValueUtil.isNaNofDouble(giftCounts) > ValueUtil.isNaNofDouble(dianDishes.getPcount()) || ValueUtil.isNaNofDouble(dianDishes.getPcount()) < ValueUtil.isNaNofDouble(giftCounts) || ValueUtil.isNaNofDouble(giftCounts) < 1){
						Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
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
					if(ValueUtil.isEmpty(dianDishes.getPromonum()) || ValueUtil.isNaNofDouble(dianDishes.getPromonum()) < ValueUtil.isNaNofDouble(cancelGiftCounts) || ValueUtil.isNaNofDouble(cancelGiftCounts) < 1){
						Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
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
				clists.add("deviceId", SharedPreferencesUtils.getDeviceId(ChineseYiDianDishActivity.this));
				clists.add("userCode", authorNum);
				clists.add("userPass", authorPwd);
				new Server().connect(ChineseYiDianDishActivity.this, Constants.FastMethodName.CHECKAUTH_METHODNAME, Constants.FastWebService.CHECKAUTH_WSDL, clists, new OnServerResponse() {

                    @Override
                    public void onResponse(String result) {
                        authLoadingDialog.dismiss();
                        if (ValueUtil.isNotEmpty(result)) {


                            String[] flag = result.split("@");
                            if (flag[0].equals("0")) {
                                //授权成功
                                Log.e("授权返回值", result);

                                setFoodPromnumAndReason(pFood, guestDishes, reason, "1");//因为只有数量等于1时，并且单道菜品价格大于49，才弹出此对话框，直接填 1

                                //如果当前处于搜索模式下
                                if (listView.getVisibility() == View.VISIBLE && expandListView.getVisibility() == View.GONE) {
                                    etSearchView.setText("");
                                    listView.setVisibility(View.GONE);
                                    expandListView.setVisibility(View.VISIBLE);
                                }
                                updateDisplay();//更新页面
                            } else {
                                Toast.makeText(ChineseYiDianDishActivity.this, R.string.not_jurisdiction, Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(ChineseYiDianDishActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
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
					searchAdapter = new YiDianDishSearchAdapter(ChineseYiDianDishActivity.this, matchLists);
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
		
		final List<Addition> additionLists;
		
				
		//现根据菜品编码查询出所有附加项得到集合，如果集合的长度是0，再查询 
        additionLists =  getDataManager(ChineseYiDianDishActivity.this).getAllFujiaListByPcode(pFood.getPcode());//查询出所有附加项 ;
		
		View sinleLayout = LayoutInflater.from(ChineseYiDianDishActivity.this).inflate(R.layout.dia_singlefujia, null);
		
		//搜索对话框
		final Button btnSearch = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_searchView);
		final cn.com.choicesoft.view.ClearEditText etSearch = (ClearEditText) sinleLayout.findViewById(R.id.dia_singlefujia_et_searchView);
		
		final SingleFujiaAdapter singleFujiaAdapter = new SingleFujiaAdapter(ChineseYiDianDishActivity.this, additionLists);

		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnSearch.setVisibility(View.GONE);
				etSearch.setVisibility(View.VISIBLE);
				etSearch.requestFocus();
				//弹出键盘
				InputMethodUtils.toggleSoftKeyboard(ChineseYiDianDishActivity.this);
				etSearch.addTextChangedListener(new SingleFujiaWatcher(etSearch, additionLists, singleFujiaAdapter));
			}
		});
		
		ListView singleFujiaLV = (ListView) sinleLayout.findViewById(R.id.dia_singlefujia_listView);
		
		//在此处判断，如果该菜品已经点了附加项，就附上
		if(ValueUtil.isNotEmpty(pFood.getChineseFujia())){
			for(String code:pFood.getChineseFujia()){
				for(Addition addition:additionLists){
					if(code.split("\\|")[0].equals(addition.getFoodFuJia_des())){//TODO 使用的DES
						addition.setSelected(true);
					}
				}
			}
		}
		
		singleFujiaLV.setAdapter(singleFujiaAdapter);
		singleFujiaLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleFujiaHolder holder = (SingleFujiaHolder) view.getTag();
                holder.mCheckBox.toggle();
                SingleFujiaAdapter adapter = (SingleFujiaAdapter) parent.getAdapter();
                adapter.toggleChecked(position);
            }
        });
		
		
		//自定义附加项的EditText
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
                if(buildPrice.length()>0){
                    buildPrice.deleteCharAt(buildPrice.length()-1);
                }
                if(buildDes.length()>0){
                    buildDes.deleteCharAt(buildDes.length()-1);
                }
                pFood.setChineseFujia(fujia);
                pFood.setFujianame(buildDes.toString());
                pFood.setFujiaprice(buildPrice.toString());

                selfFujiaAddEdit.setText("");
                dialog.dismiss();
                //隐藏软键盘
                InputMethodUtils.hideSoftKeyboard(ChineseYiDianDishActivity.this,selfFujiaAddEdit);

                //如果在搜索模式下，点击添加附加项按钮，就要执行如下操作
                if (listView.getVisibility() == View.VISIBLE && expandListView.getVisibility() == View.GONE) {
                    listView.setVisibility(View.GONE);
                    expandListView.setVisibility(View.VISIBLE);
                }
                updateDisplay();//更新页面

            }
        });
		
		Button diaBtnCancel = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_cancel);
		diaBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
                //隐藏软键盘
                InputMethodUtils.hideSoftKeyboard(ChineseYiDianDishActivity.this,selfFujiaAddEdit);
			}
		});
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ChineseYiDianDishActivity.this,R.style.edittext_dialog);
		builder.setView(sinleLayout);
		dialog = builder.create();
		dialog.show();
		
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
				holder.jijiao = (TextView) convertView.findViewById(R.id.yidian_text_jijiao);
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
			
			//表明这是套餐明细
			if(food.isIstc() && !food.getPcode().equals(food.getTpcode())){
				holder.plusImage.setVisibility(View.INVISIBLE);
				holder.minusImage.setVisibility(View.INVISIBLE);
				
				holder.presentImage.setVisibility(View.INVISIBLE);
				holder.deleteImage.setVisibility(View.INVISIBLE);
			}else{
                holder.dishCounts.setOnClickListener(new DishCountsClick(food));
            }
            //=======================判断是否多单位======================
            if(ValueUtil.isNotEmpty(food.getUnitMap())){
                holder.dishUnit.setOnClickListener(new DishUnitClick(food));
            }
            //============================================
			holder.dishName.setText(food.getPcname());
			holder.dishCounts.setText(food.getPcount());
			holder.dishPrice.setText(food.getPrice());
			holder.dishUnit.setText(food.getUnit());


            if(ValueUtil.isEmpty(food.getCLASS())||food.getCLASS().trim().equals("N")){
                food.setCLASS("N");
                holder.jijiao.setText(R.string.ji);
                holder.jijiao.setTextColor(Color.GREEN);
            }else {
                holder.jijiao.setText(R.string.jiao);
                holder.jijiao.setTextColor(Color.RED);
            }

            if(ValueUtil.isEmpty(food.getPKID())){
                food.setPKID(ValueUtil.createPKID(ChineseYiDianDishActivity.this));
            }

            holder.jijiao.setOnClickListener(new JiJiaoClick(food));
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
					Toast.makeText(ChineseYiDianDishActivity.this, R.string.meal_not_add_items, Toast.LENGTH_SHORT).show();
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
					AlertDialog.Builder builder = new AlertDialog.Builder(ChineseYiDianDishActivity.this);
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
						Double counts = ValueUtil.isNaNofDouble(mFood.getPcount());
						counts += 1;
						
						mFood.setCounts(counts+"");
						mFood.setPcount(counts+"");
						
						etSearchView.setText("");
						listView.setVisibility(View.GONE);
						expandListView.setVisibility(View.VISIBLE);
						updateDisplay();//更新界面View
					}
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
                new AlertDialog.Builder(ChineseYiDianDishActivity.this).setTitle(R.string.revamp_counts)
                        .setView(layout).setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(Food mFood:guestDishes){
                            if(food == mFood){
                                Double counts=ValueUtil.isNaNofDouble(text.getText().toString());
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
                }) .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
					}
				}).show();
            }

        }
		
		private class ViewYiDianSearchHolder{
			TextView jijiao;
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
            specialfujiaMatchLists.clear();
			String changingFujiaStr = fujiaEdit.getText().toString().trim();
			if(ValueUtil.isNotEmpty(changingFujiaStr)){
				for(CommonFujia commFujia:specialComLists){
					String currentStr = commFujia.getINIT();
					if(currentStr.indexOf(ValueUtil.exChangeToLower(changingFujiaStr)) != -1  || currentStr.indexOf(ValueUtil.exChangeToUpper(changingFujiaStr)) != -1){
						specialfujiaMatchLists.add(commFujia);
					}
					
				}
				
				if(commonFujiaAdapter != null){
					commonFujiaAdapter.setCommonFujiaSource(specialfujiaMatchLists);
					commonFujiaAdapter.notifyDataSetChanged();
				}
			}else{
				commonFujiaAdapter.setCommonFujiaSource(specialfujiaMatchLists);
				commonFujiaAdapter.notifyDataSetChanged();
			}
			
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
            },ChineseYiDianDishActivity.this,v).showView(food);
        }
    }
}
class JiJiaoClick implements OnClickListener{
    private Food food;
    public JiJiaoClick(Food food) {
        super();
        this.food=food;
    }

    @Override
    public void onClick(View v) {
        TextView textView= (TextView) v;
        if(ValueUtil.isEmpty(food.getCLASS())){
            food.setCLASS("N");
        }else{
            if(food.getCLASS().trim().equals("N")){
                food.setCLASS("Y");
                textView.setText(R.string.jiao);
                textView.setTextColor(Color.RED);
            }else {
                food.setCLASS("N");
                textView.setText(R.string.ji);
                textView.setTextColor(Color.GREEN);
            }
        }
    }
}
