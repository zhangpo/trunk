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
 * �в͵�ͽ���
 */
public class ChineseEatables extends BaseActivity implements OnClickListener{
		
	private ListView listView;//��߲�Ʒ�����Ӧ�Ŀؼ�
	private EatableListAdapter eatListAdapter;//��߲�Ʒ�����Ӧ�Ŀؼ���������
	private LoadingDialog dialog1;//��ʾ��
	private GridView gridView;//�ұ߾����Ʒ��Ӧ�Ŀؼ�
	private EatableGridAdapter eatGridAdapter;//�ұ߾����Ʒ��Ӧ�Ŀؼ���������
	
	private LoadingDialog mLoadingDialog;
	
	private TextView tableNum,orderNum,peopleNum,userNum;//������ɫ������
	
	private TextView radioOne, radioTwo, radioThree, radioFour, radioFive,radioSix, radioSeven, radioEight, radioNight,radioZero,radioX,radioValueShow ;
	
	private ClearEditText etSearchView;//������
	private Button btnSearchView;//������ť
	private TextView btnSearchCancel;//������ť

	private Button remark,yiDian,back;//�ײ�������ť,��ע ���ѵ�ˣ�����
	
	private LinearLayout linearTCDetails;//�ײ�������ڲ���
	private LinearLayout linearTcLayout;//�ײ��������������
	private Button tcBtnCertain;//�ײ������ȷ�ϰ�ť
	private Button tcBtnCancel;//�ײ������ȡ����ť

	private List<Food> dishLists = null;// ��ž����Ʒ
//	private List<CurrentUnit> unitCurLists = null;//��ŵڶ���λ�ļ���
	private List<Grptyp> grpLists;//��Ų�Ʒ���༯��
	
	private List<Food> dishClassLists = null;//ÿ����Ʒ�����Ӧ��һЩ�����Ʒ�ļ���
	private List<Food> tempdishClassLists = new ArrayList<Food>();//ÿ����Ʒ�����Ӧ��һЩ�����Ʒ�ļ���            ��ʱ��
	private List<Food> matchLists = new ArrayList<Food>();//��Ϣ�������Ľ����ƥ��ļ���
	private List<Map<String,String>> soldOutLists = new ArrayList<Map<String, String>>();//��Ź����Ʒ���뼯��
	private List<Food> tempNotAlternativeLists = null;//���ײ�ʱ����Ų��ɻ��������ʱ����
    private List<Addition> fujiamatchLists = new ArrayList<Addition>();//��������Ϣ�������Ľ����ƥ��ļ���
	
	
	private SingleMenu menu = SingleMenu.getMenuInstance();
	private List<Food> guestLists = GuestDishes.getGuDishInstance();
	

	int tpnumflag = 0;//������ȫ�ֱ�����Ϊ��ʹ��tpnum
	
	private Food tempFujiaFood = null;//����ڸ������
	private Food curTpnumTCfood =null;//�ײ���Ҫtcmoneymode �� tpnum
	
	private int digitalFlag = -1;//ͷ�����ֱ��������õı�ʶλ
	
	private Dialog dialog = null;//���������õ�dialog
	
	private List<EatableSubGridViewAdapter> subGridViewAdapterLists = null;
	
	private String grp = null;
	private ImageView menuinfo_imageView;
	private View layout;
	private LinearLayout topLinearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        TsData.isSave=false;
//		 ����Ϊȫ��ģʽ
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
	 * ��ʼ������
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
				
				//�����ж�ĳ���ײ��Ƿ�ѡ�����
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
				
				tempFujiaFood = null;//rather special�������߲˵�����ʱ�������л�������һ����𣬸�ֵΪ��
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			private static final int DOUBLE_CLICK_TIME = 350;//˫�����ʱ��350����
			private boolean waitDouble = true;

			//�����¼�
			private Handler handler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					Food foodItem = dishClassLists.get(msg.arg1);
					TsData.isSave=true;//�Ƿ񱣴�
					if(!foodItem.isIstc()){
						tempFujiaFood=foodItem;
					}

					//����ǹ����Ʒ���͵ô���
					if (foodItem.isSoldOut()) {
						return;
					}

					foodOrder(foodItem, (View)msg.obj);
				}

			};
			//˫���¼�
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, final int position,long id) {
				if(waitDouble){
					waitDouble = false;        //��ִ��˫���¼�
					new Thread(){
						public void run() {
							try {
								Thread.sleep(DOUBLE_CLICK_TIME);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}    //�ȴ�˫��ʱ�䣬����ִ�е����¼�
							if(!waitDouble){
								//������˵ȴ��¼�����Ԥִ��˫��״̬������Ϊ����
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
	 * ���ĳ���ײͺ������еĲ���
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
		creatFood.setTpcode(pFood.getPcode());//�˴��Ƚ�����
		creatFood.setTpname(pFood.getPcname());//�˴��Ƚ�����
		creatFood.setPcount("1");//�˴�������
		creatFood.setCounts(pFood.getCounts());//�˴�������
		creatFood.setPrice(pFood.getPrice());
		creatFood.setUnit(pFood.getUnit());
		creatFood.setWeightflg(pFood.getWeightflg());//�ڶ���λ��ʶ
		creatFood.setIstc(pFood.isIstc());//�˴��Ƚ�����
		creatFood.setTcMoneyMode(pFood.getTcMoneyMode());
		//��������todo
//		guestLists.add(creatFood);
		
		curTpnumTCfood = creatFood;//���ﲻ��Ϊ��һ���Ʒ���Ӹ�������׼�� �������ײ������ּƼ۷�ʽ������ʱ���õ�tpnum��tcmoneymode,��tpnum,tcmoneymode����ȥ       rather special
		
				
		linearTcLayout.setVisibility(View.VISIBLE);
		
		List<List<Food>> allTcDetailLists= getDataManager(this).queryTcItemLists(creatFood);//��ѯ�����ײ��µ�������ϸ���� 
		showTcdetails(allTcDetailLists);
			
	}


	/**
	 * ��ѯ�����ݣ���ʼ���ײ������view,�°汾
	 * @param allTcDetails
	 */
	private void showTcdetails(List<List<Food>> allTcDetails) {
		
		subGridViewAdapterLists = new ArrayList<EatableSubGridViewAdapter>();
		
		tempNotAlternativeLists = new ArrayList<Food>();//�����ɻ�������뵽�ü�����
		
		//ƥ���ײ���ϸ��Ʒ�ĵڶ���λ,����еڶ���λ����Ϊ2��Ĭ�ϵĶ���Ϊ1
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
		
		int tcGroupNumber = 0;//�ײͱ������
		
		for(final List<Food> singleLists:allTcDetails){
			
			if(singleLists.size() > 0){        //�ɻ�����

				//�ڴ˴�ƥ���ײ���ϸ��Ʒ�еĹ����Ʒ    ֻƥ��ɻ�����
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
						int  radioValue = Integer.parseInt(radioValueShow.getText().toString());//�õ����ֱ��������ұߵ��Ǹ�ֵ
						radioValueShow.setText("1");//����������ֱ�������ԭ
						digitalFlag = -1;
						
						EatableSubGridViewAdapter adapter = (EatableSubGridViewAdapter) parent.getAdapter();
						
						Food foodDefault = adapter.getDefaultsFood();
						//TODO ��ʹ��� ӦΪ��
						Food foodCurrent = (Food)adapter.getItem(position);
						
						//����ײ���ϸ�ǹ����Ʒ���͵ô���
						if(!foodCurrent.isSoldOut()){
							tempFujiaFood = foodCurrent;//Ϊ�ײ���ϸ��Ʒ���Ӹ�������׼��
						}else{
							return;
						}						

						
						foodCurrent.setTabNum(menu.getTableNum());
						foodCurrent.setOrderId(menu.getMenuOrder());
						foodCurrent.setMan(menu.getManCounts());
						foodCurrent.setWoman(menu.getWomanCounts());
//						foodCurrent.setTpnum(tpnumflag+"");//�����൱������,Ϊ�ײ���ϸ����tpnum
						
						if(radioValue != 0){
							if(foodCurrent.isSelected()){
								
								int countCurrent = Integer.parseInt(foodCurrent.getPcount());
								int countNew = countCurrent + radioValue;
								if(ValueUtil.isNotEmpty(foodCurrent.getMaxCnt()) && countNew > Integer.parseInt(foodCurrent.getMaxCnt()) ){
									Toast.makeText(ChineseEatables.this, "�÷ݲ�Ʒ����ѡ�� "+foodCurrent.getMaxCnt(), Toast.LENGTH_SHORT).show();
								}else if(adapter.getCountsAboutPcount() + radioValue > Integer.parseInt(foodDefault.getMaxCnt())){
									Toast.makeText(ChineseEatables.this, "�ѳ����������������� "+foodDefault.getMaxCnt(), Toast.LENGTH_SHORT).show();
								}else{
									
									foodCurrent.setPcount(countNew+"");
									adapter.notifyDataSetChanged();
									

								}
							}else{
								if(ValueUtil.isNotEmpty(foodCurrent.getMaxCnt()) && radioValue > Integer.parseInt(foodCurrent.getMaxCnt()) ){
									Toast.makeText(ChineseEatables.this, "�÷ݲ�Ʒ����ѡ�� "+foodCurrent.getMaxCnt(), Toast.LENGTH_SHORT).show();
									return;
								}else if(adapter.getCountsAboutPcount() + radioValue > Integer.parseInt(foodDefault.getMaxCnt())){
									Toast.makeText(ChineseEatables.this, "�ѳ����������������� "+foodDefault.getMaxCnt(), Toast.LENGTH_SHORT).show();
									return;
								}
								foodCurrent.setSelected(true);
								foodCurrent.setPcount(radioValue+"");  
								adapter.notifyDataSetChanged();
								
							}
						}else if(radioValue == 0){
							if(!foodCurrent.isSelected()){
								//���0ʱ������ɫ�Ĳ�Ʒ��û�з�Ӧ��
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
				
				
			}else if(singleLists.size() == 1 && singleLists.get(0).getMinCnt().equals(singleLists.get(0).getMaxCnt())){   //���ɻ�����
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
		
		//������ײ���ϸֻ�в��ɻ����û�пɻ�������жϣ����ײ����鲼����ʧ
		int countTcDetails = 0;
		for(EatableSubGridViewAdapter adapter:subGridViewAdapterLists){
			countTcDetails += adapter.getCount();
		}
		if(countTcDetails == 0){
			
			linearTcLayout.setVisibility(View.GONE);//���ײ����鲼����ʧ
			
			//�������ɻ���������ͬ�Ĳ�Ʒ�ͺϲ�
			List<Food> tempLists = new ArrayList<Food>();//���ȥ�غ��food
			for(Food f:tempNotAlternativeLists){
				if(!tempLists.contains(f)){
					tempLists.add(f);
				}
			}
			
			//����ͬ�Ĳ�Ʒ�ϲ�����
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
	 * ��ʼ������
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
		
		// ��ʼ����Ա��Ϣ �вͲ���
		//VipMsg.iniVip(this, menu.getTableNum(), R.id.vipMsg_ImageView);
		
		grpLists = getDataManager(this).getAllDishClassList();//��ò�Ʒ���
		dishLists =  getDataManager(this).getAllFoodList();//��ȡ���в�Ʒ

		if (guestLists.size()==0){
			List<Food> cacheLists = new OrderSaveUtil().queryHandle(this,menu.getTableNum(), menu.getMenuOrder());//��ȡ����Ĳ�Ʒ
			guestLists.clear();
			guestLists.addAll(cacheLists);
		}

		//����ѵ��Ʒ��������������ı䣬�ڵ�˽����Ӧ�ø���
		for (Food food :dishLists){
			//��Ʒ����
			for (Food guestFood :guestLists){
				if (food.getPcode().equals(guestFood.getPcode())){
					food.setSelected(true);
					Float count1 =Float.parseFloat(food.getCounts() == null ? "0" : food.getCounts());
					food.setCounts(count1+Float.parseFloat(guestFood.getPcount())+"");
				}
			}
		}

		//������߲�Ʒ���������
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
		
		//GridView�״���ʾ��ʱ�򣬳�ʼ��Ĭ�ϵ�ֵ
		setGridDefault();
		
		//ʵ������Щ�����ĶԻ���
		showIfSaveDialog();
				
	}

	/**
	 * GridView�״���ʾ��ʱ�򣬳�ʼ��Ĭ�ϵ�ֵ
	 */
	private void setGridDefault() {
				
		grp = grpLists.get(0).getGrp();
		
		updateDishClass(grp, dishLists);
		
		//���½����ϵ���ߵ�ListView
		eatListAdapter.setSelectedPosition(0);
		eatListAdapter.notifyDataSetInvalidated();
//        eatGridAdapter = new EatableGridAdapter(ChineseEatables.this, dishClassLists);
//        gridView.setAdapter(eatGridAdapter);
		accessSoldOutFace();

	}
	/**
	 * �Խ���ζ���õ�
	 * ���ýӿڣ������˵��Ų�ѯ�����˵��Ŷ�Ӧ�����в�Ʒ
	 * @param orderId �˵���
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
						Log.i("����ζ���õȻ�ȡ�Ĳ�Ʒ", result);
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

								//����ߵ�ListView������Դת����ֵ��һ��ʼ�ͳ�ʼ��
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
								//������ζ���õ���ȡ�Ĳ�Ʒ�洢������������
								for (Food fd : dishLists) {
									if (fd.isSelected()) {
										guestLists.add(fd);
									}
								}

							} else {
								ToastUtil.toast(ChineseEatables.this, R.string.jnow_params_error);//�����������Ժ�����
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
	 * ���ù����Ʒ�ӿ�
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
							//�Խ���ζ���õ�,���ýӿڣ������˵��Ų�ѯ�����˵��Ŷ�Ӧ�����в�Ʒ
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
	 * ���ݲ�Ʒ��𣬲�ѯ��������µľ����Ʒ����
	 * //����dishclass����
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
		
		dishClassLists = tempdishClassLists;//���õ��ļ��ϸ�ֵ��gridview������Դ����
	}

	private Bundle getTblStateBundle() {
		return this.getIntent().getBundleExtra("topBundle");
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {		
		
		mLoadingDialog = new LoadingDialogStyle(this, "���ݼ����У����Ժ�...");
		mLoadingDialog.setCancelable(false);
		
		//������ɫ��
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
	 * ����¼�
	 */
	private View.OnClickListener onClickListener = new OnClickListener() {
		
		private void dataClicked(String dataStr){
			String valueStr = radioValueShow.getText().toString();
			
			if(valueStr.length() == 2){
				radioValueShow.setText("1");
				return;
			}
			
			if(valueStr.equals("1")){
				//���ж���Ĭ�ϵ�1�����ǵ������1
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
			
			//��digitalFlag = 0ʱ����ʾ�Ѿ���������1����digitalFlag = -1ʱ����ʾû�е������1
			switch (v.getId()) {
			case R.id.toptitle_rb_one:
				
				if(radioValueShow.getText().toString().length() == 2){
					radioValueShow.setText("1");
					return;
				}
				
				String valueStr = radioValueShow.getText().toString();
				if(valueStr.equals("1")){
					//���ж���Ĭ�ϵ�1�����ǵ������1
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
			//��������
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
			//��������ѵ��˲�Ʒ���������ʱ��������ʾ����ʾ�Ƿ񱣴� 
			if(guestLists.size() == 0||!TsData.isSave){
				this.finish();
			}else{
				//�ײͻ�δѡ�����
				if(linearTcLayout.getVisibility() == View.VISIBLE){
					Toast.makeText(this, R.string.eatable_tc_tcnofinish, Toast.LENGTH_SHORT).show();
					return;
				}
				showIfSaveDialog().show();
			}
			break;
		case R.id.eatable_tcdetail_btn_certain:
			
			//һ���ײ���ϸҲ��ѡ���ǲ�������
			int tcDetailsCount = 0;
			for(EatableSubGridViewAdapter subAadapter:subGridViewAdapterLists){
				tcDetailsCount += subAadapter.getCountsAboutPcount();
			}
			
			if(tcDetailsCount == 0){
				Toast.makeText(this, R.string.eatable_tc_servelnotxuan, Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			//�����һ���ײ���ϸ��ѡ���������������������Ӧ��mincnt
			for(EatableSubGridViewAdapter adapter:subGridViewAdapterLists){
				if(!adapter.isSatisfyMincntDefault()){
					Toast.makeText(this, R.string.eatable_tc_servelnotxuan, Toast.LENGTH_SHORT).show();
					return;
				}
			}
			
//			//���ײ����ӽ�������
//			guestLists.add(curTpnumTCfood);
//			guestLists.addAll(tempNotAlternativeLists);
//			for(EatableSubGridViewAdapter subAdapter:subGridViewAdapterLists){
//				List<Food> selectedLists = subAdapter.getSelectedFoodLists();
//				guestLists.addAll(selectedLists);
//			}
			
			//������������ײ�����ͬ�Ĳ�Ʒ��ϸ���ͺϲ�
			List<Food> currentTcLists = new ArrayList<Food>();
			
			currentTcLists.addAll(tempNotAlternativeLists);//���Ӳ��ɻ�����
			for(EatableSubGridViewAdapter subAdapter:subGridViewAdapterLists){
				List<Food> selectedLists = subAdapter.getSelectedFoodLists();
				currentTcLists.addAll(selectedLists);//���ӿɻ�����
			}
			
			//ȥ���ظ�Ԫ�غ�ļ���
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
			
			
			getTcPriceByMode(curTpnumTCfood, guestLists,dishLists);//rather special�ײ͵����ˣ��ͼ�����ײ͵ļƼ۷�ʽ
			linearTCDetails.removeAllViews();
			linearTcLayout.setVisibility(View.GONE);
			
			//������߲�Ʒ���������
			for(Grptyp grpTyp:grpLists){
				if(grpTyp.getGrp().equals(grp)){
					grpTyp.setQuantity(eatGridAdapter.getItemCounts()+"");
					eatListAdapter.notifyDataSetChanged();
					break;
				}
			}
			
			tpnumflag = 0;//���ײͱ����Ϊ0
			break;
			
		case R.id.eatable_tcdetail_btn_cancel:
			linearTCDetails.removeAllViews();
			linearTcLayout.setVisibility(View.GONE);
			
			tempNotAlternativeLists = null;
			
			for(Food food:dishClassLists){
				if(food.getPcode().equals(curTpnumTCfood.getPcode())){
					//��������1
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
			tpnumflag = 0;//���ײͱ����Ϊ0
			
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
	 * �����Ի�����ʾ�û��Ƿ񱣴��ѵ��Ʒ
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
					//ʵ����food�е��ֶ�send�������ѷ��Ͳ�Ʒ�������ݴ�   ��Ϊ�丳ֵ             �ѷ���1          �ݴ�0
					for(int i = 0;i<guestLists.size();i++){
						guestLists.get(i).setSend("0");
					}
					boolean insertSuccess= new OrderSaveUtil().insertHandle(ChineseEatables.this,menu.getTableNum(),menu.getMenuOrder(),guestLists);
					if(insertSuccess){
						Toast.makeText(ChineseEatables.this, R.string.save_success, Toast.LENGTH_SHORT).show();
					}else{
						Log.e("����˱��浽���ݿ��order_dishes", "save fail");
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
	 * ��������ؼ���ʱ������ѵ��˲ˣ��͵�����ʾ�Ƿ񱣴浽�������ݿ�
	 * @param guestLists
	 */
	private boolean saveToAllCheck(List<Food> guestLists) {
		
		return getDataManager(this).circleInsertAllCheck(guestLists);
	}

	/**
	 * ��Ϣ�����õ�
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
					//������ߵĲ�Ʒ���಻�ɵ��
					listView.setEnabled(false);
					
					for(Food dishes:dishLists){
						String currentStr = dishes.getPcname();
						String numEncode = dishes.getPcode();
						if(currentStr.indexOf(changingStr)!=-1 || ConvertPinyin.convertJianPin(currentStr).indexOf(changingStr) !=-1 ||ConvertPinyin.convertQuanPin(currentStr).indexOf(changingStr)!=-1 || numEncode.indexOf(changingStr)!=-1){
							matchLists.add(dishes);
						}
					}
					if(eatGridAdapter!=null){
						dishClassLists = matchLists;//���õ��ļ��ϸ�ֵ��gridview������Դ����
						for(Food f:dishClassLists){
							Log.d("-----------", f.getPcname());
						}
						eatGridAdapter.setEatableGridSource(dishClassLists);
						eatGridAdapter.notifyDataSetChanged();
					}
				}else{
					dishClassLists = tempdishClassLists;//���õ��ļ��ϸ�ֵ��gridview������Դ����
					eatGridAdapter.setEatableGridSource(dishClassLists);
					eatGridAdapter.notifyDataSetChanged();
					
					//������ߵĲ�Ʒ����ɵ��
					listView.setEnabled(true);
					
					//����������
					InputMethodUtils.toggleSoftKeyboard(ChineseEatables.this);
				}
			}
		};
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			
			if(linearTcLayout.getVisibility() == View.VISIBLE){
				Toast.makeText(this, "�ײͻ�δѡ����ϣ�", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
				if(guestLists.size() == 0){  //�������û��һ���ˣ��������ϳ���Ϊ0���Ͳ�����
					this.finish();
				}else{
					showIfSaveDialog().show();
				}
			}
			return super.onKeyDown(keyCode, event);
		}


		/**
		 * ��ʾ��Ʒ������ĶԻ���
		 * @param pFood
		 */
		private void showSingleFujiaDia(final Food pFood){
			
			if(pFood == null){
				Toast.makeText(ChineseEatables.this, "�㻹û��ѡ���Ʒ", Toast.LENGTH_SHORT).show();
				return;
			}else if(pFood.getPcode().equals(pFood.getTpcode())){//�ײ��ǲ������Ӹ������
				Toast.makeText(ChineseEatables.this, "�ײͲ������Ӹ�����", Toast.LENGTH_SHORT).show();
				return;
			}
			
			//PAD2 �Ѹ�
			final List<Addition> additionLists =  getDataManager(ChineseEatables.this).getAllFujiaListByPcode(pFood.getPcode());//��ѯ�����и����� ;
			
			View sinleLayout = LayoutInflater.from(ChineseEatables.this).inflate(R.layout.dia_singlefujia, null);

            /**
             * ������ť
             */
			final Button btnSearch = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_searchView);
			final cn.com.choicesoft.view.ClearEditText etSearch = (ClearEditText) sinleLayout.findViewById(R.id.dia_singlefujia_et_searchView);

			ListView singleFujiaLV = (ListView) sinleLayout.findViewById(R.id.dia_singlefujia_listView);
			final SingleFujiaAdapter singleFujiaAdapter = new SingleFujiaAdapter(ChineseEatables.this, additionLists);
			singleFujiaLV.setAdapter(singleFujiaAdapter);
            //�����������¼�
            btnSearch.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    btnSearch.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();

                    //��������
                    InputMethodUtils.toggleSoftKeyboard(ChineseEatables.this);
                    etSearch.addTextChangedListener(new FujiaWatcher(etSearch, additionLists, singleFujiaAdapter));
                }
            });
            //������ѡ���¼�
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
             * ȷ����ť
             */
			Button diaBtnCertain = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_certain);
			diaBtnCertain.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//��������ѡ�еĸ��������ӵ�������
                    if(pFood==null){
                        return;
                    }
                    StringBuilder buildPrice = new StringBuilder();//����
                    StringBuilder buildDes = new StringBuilder();//����
                    List<String> fujia=new ArrayList<String>();
					
					String selfFujiaStr = selfFujiaAddEdit.getText().toString();//�õ��Զ���ĸ������ֵ
					if(ValueUtil.isNotEmpty(selfFujiaStr)){
                        buildPrice.append("0").append("!");
                        buildDes.append(selfFujiaStr).append("!");
						//���Զ��帽�����ֵ���ӽ�ȥ
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
					tempFujiaFood = null;//����ʱ���Ǹ�Ϊ������׼����food��ֵΪ��
					
					selfFujiaAddEdit.setText("");
					dialog.dismiss();
                    //����������
                    InputMethodUtils.hideSoftKeyboard(ChineseEatables.this,selfFujiaAddEdit);
				}
			});
			
			Button diaBtnCancel = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_cancel);
			diaBtnCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					//����������
					InputMethodUtils.hideSoftKeyboard(ChineseEatables.this,selfFujiaAddEdit);
				}
			});
			
			AlertDialog.Builder builder = new AlertDialog.Builder(ChineseEatables.this,R.style.edittext_dialog);
			builder.setView(sinleLayout);

			dialog = builder.create();
			dialog.show();
			
		}

		
		
		
		/**
		 * �ײ͵����ּƼ۷�ʽ
		 * @param pLists  singleton����
		 * @param wholeFoodLists    ���в�Ʒ�󼯺�stable
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
				}//�õ��ײ���ϸ��Ʒ�ļ۸���Ӻ�ĺ�
                double rate=0;
                if(tcDetailPrices!=0) {
                    rate = ValueUtil.isNaNofDouble(tcFood.getPrice()) / tcDetailPrices;//�õ�����
                }
				 for(Food f:pLists){
					 if(f.isIstc() && !f.getTpcode().equals(f.getPcode()) && f.getTpcode().equals(tcFood.getPcode()) && tcFood.getTpnum().equals(f.getTpnum())){
						 
						 double newPrice = ValueUtil.isNaNofDouble(f.getPrice()) * ValueUtil.isNaNofDouble(f.getPcount()) * rate;
						 f.setPrice(ValueUtil.setScale(newPrice, 2, 5));
					 }
				 }

				 //������ļ۸�����ӣ�����������ײͼ۸񣬾����¼������һ��ļ۸�
				 List<Food> tcOnlys = new ArrayList<Food>();//����¼���ֻ��������tcFood����ϸ��Ʒ
				 
				 
				 for(Food mFood:pLists){
					
					 if(mFood.isIstc() && !mFood.getPcode().equals(mFood.getTpcode()) && tcFood.getPcode().equals(mFood.getTpcode()) && mFood.getTpnum().equals(tcFood.getTpnum())){
						 tcOnlys.add(mFood);
					 }
				 }
				  
				 double newallPrice = 0.00;//�õ��µ��ܼ�
				 for(Food fd:tcOnlys){
					 newallPrice += Double.valueOf(fd.getPrice()); 
				 }

				 if(newallPrice != Double.valueOf(tcFood.getPrice())){
					 if(tcOnlys.size() != 0){   //������жϣ�������е�MINCNT������0���ͱ�����Խ��
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
				}//�õ��ײ���ϸ��Ʒ�ļ۸���Ӻ�ĺ�
				
				tcFood.setPrice(ValueUtil.setScale(tcDetailsPrice, 2, 5));
			}else if(tcMode == 3){ 
				double tcDetailsPrice = 0.00;
				for(Food food:pLists){
					if(food.isIstc() && !food.getTpcode().equals(food.getPcode()) && food.getTpcode().equals(tcFood.getPcode()) && food.getTpnum().equals(tcFood.getTpnum())){
						food.setPrice(ValueUtil.setScale(Double.valueOf(food.getPrice()), 2, 5));
						double itemPrices = Double.parseDouble(food.getPrice()) * Double.parseDouble(food.getPcount());
						tcDetailsPrice += Double.valueOf(itemPrices);
					}
				}//�õ��ײ���ϸ��Ʒ�ļ۸���Ӻ�ĺ�
				
				double tcInitialPrice = Double.valueOf(tcFood.getPrice());//�ײ�ԭ�м۸�
				
				if(tcDetailsPrice >= tcInitialPrice){
					tcFood.setPrice(ValueUtil.setScale(tcDetailsPrice, 2, 5));
				}else{
					double rate = tcInitialPrice/tcDetailsPrice;//�õ�����
					
					for(Food f:pLists){
						if(f.isIstc() && !f.getTpcode().equals(f.getPcode()) && f.getTpcode().equals(tcFood.getPcode()) && f.getTpnum().equals(tcFood.getTpnum())){
							double newPrice = Double.parseDouble(f.getPrice()) * Double.parseDouble(f.getPcount())* rate;
							f.setPrice(ValueUtil.setScale(newPrice, 2, 5));
						}
					}
					
					//���������ļ۸���ӣ���Ȼ�������ײ�ԭ�еļ۸񣬾�.......
					 List<Food> tcOnlys = new ArrayList<Food>();//����¼���ֻ��������tcFood����ϸ��Ʒ
					 for(Food mFood:pLists){
						 if(mFood.isIstc() && !mFood.getPcode().equals(mFood.getTpcode())&& tcFood.getPcode().equals(mFood.getTpcode()) && mFood.getTpnum().equals(tcFood.getTpnum())){
							 tcOnlys.add(mFood);
						 }
					 }
					 
					 double newConformPrice = 0.00;//�õ��µ��ܼ�
					 for(Food fd:tcOnlys){
						 newConformPrice += Double.valueOf(fd.getPrice()); 
					 }
					 
					if(newConformPrice != tcInitialPrice){
						if(tcOnlys.size() != 0){//������жϣ�������е�MINCNT������0���ͱ�����Խ��
							
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
     * �����������õ�
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
                    String currentStr = addition.getInit();//�������ݿ���е�init�ֶ�
                    String currentNameStr = addition.getFoodFuJia_des();//�������ݿ���е������ֶ�
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
    //=====================================���================================
   //���
    public void foodOrder(final Food foodItem,View view){
        int radioValue = Integer.parseInt(radioValueShow.getText().toString());//�õ����ֱ��������ұߵ��Ǹ�ֵ
        if(!foodItem.isIstc()) {
			tempFujiaFood = foodItem;//Ϊ��һ���Ʒ���Ӹ�������׼��,�������ͨ��Ʒ
		}
		//��Ϊ0��ʱ��ֱ��ɾ��
		if (radioValue==0){
			delectFood(foodItem);
		}else {
			foodSold(foodItem, view);
		}
    }
	public void delectFood(Food foodItem){
		radioValueShow.setText("1");//����������ֱ�������ԭ
		digitalFlag = -1;
		foodItem.setSelected(false);//��ťѡ��
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
		//������߲�Ʒ���������
		for(Grptyp grpTyp:grpLists){
			if(grpTyp.getGrp().equals(grp)){
				grpTyp.setQuantity(eatGridAdapter.getItemCounts()+"");
				break;
			}
		}
		eatListAdapter.notifyDataSetChanged();
	}
	//��Ʒ����
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
	//�Ƿ��ײ�
	public void foodIstc(final Food foodItem,View view){
		if(foodItem.isIstc()) {
			radioValueShow.setText("1");//����������ֱ�������ԭ
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
			tcCallBack(foodItem);//���ײ���ϸ���в���
		}else {
			foodItem.setPKID( ValueUtil.createPKID(this));
			foodPlusd(foodItem, view);
		}
	}
	//�ڶ���λ
	public void foodPlusd(final Food foodItem,View view){
		if(ValueUtil.isNotEmpty(foodItem.getPlusd()) && foodItem.getPlusd()!=0)
		{
			foodItem.setWeightflg("2");
			foodUnits(foodItem,view);
		}else {
			foodUnits(foodItem,view);
		}
	}
	//�൥λ
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
    //�����Ʒ
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