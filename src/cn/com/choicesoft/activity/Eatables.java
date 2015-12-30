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
 * ��˽���
 */
public class Eatables extends BaseActivity implements OnClickListener{

	private ListView listView;//��߲�Ʒ�����Ӧ�Ŀؼ�
	private EatableListAdapter eatListAdapter;//��߲�Ʒ�����Ӧ�Ŀؼ���������

	private GridView gridView;//�ұ߾����Ʒ��Ӧ�Ŀؼ�
	private EatableGridAdapter eatGridAdapter;//�ұ߾����Ʒ��Ӧ�Ŀؼ���������
	
	private LoadingDialog mLoadingDialog;
	private View layout;
	
	private TextView tableNumTv;//̨λ����
	private TextView orderNumTv;//�˵���
	private TextView manNumTv;//������
	private TextView womanNumTv;//Ů����
	private TextView userNumTv;//̨λ����
	private ImageView menuinfo_imageView;
	
	private TextView radioOne, radioTwo, radioThree, radioFour, radioFive,radioSix, radioSeven, radioEight, radioNight,radioZero,radioX,radioValueShow ;

	private ClearEditText etSearchView;//������
	private Button btnSearchView;//������ť
	private LinearLayout topLinearLayout;

	private Button remarkBtn;//��ע
	private Button yiDianBtn;//�ѵ��
	private Button backBtn;//����
    private Button fujiaChanPin;//���Ӳ�Ʒ

	private LinearLayout linearTCDetails;//�ײ�������ڲ���
	private LinearLayout linearTcLayout;//�ײ��������������
	private Button tcBtnCertain;//�ײ������ȷ�ϰ�ť
	private Button tcBtnCancel;//�ײ������ȡ����ť
	
	private Bundle bundle;

	private List<Food> dishLists = null;// ��ž����Ʒ
	private List<CurrentUnit> unitCurLists = null;//��ŵڶ���λ�ļ���
	private List<Grptyp> grpLists;//��Ų�Ʒ���༯��

	private List<Food> dishClassLists = null;//ÿ����Ʒ�����Ӧ��һЩ�����Ʒ�ļ���
	private List<Food> tempdishClassLists = new ArrayList<Food>();//ÿ����Ʒ�����Ӧ��һЩ�����Ʒ�ļ���            ��ʱ��
	private List<Food> matchLists = new ArrayList<Food>();//��Ϣ�������Ľ����ƥ��ļ���
	private List<String> soldOutLists = new ArrayList<String>();//��Ź����Ʒ���뼯��
	private List<Food> tempNotAlternativeLists = null;//���ײ�ʱ����Ų��ɻ��������ʱ����
	
	
	private SingleMenu menu = SingleMenu.getMenuInstance();
	private List<Food> guestLists = GuestDishes.getGuDishInstance();


	int tpnumflag = 0;//������ȫ�ֱ�����Ϊ��ʹ��tpnum

	private Food tempFujiaFood = null;//����ڸ������
	private Food curTpnumTCfood =null;//�ײ���Ҫtcmoneymode �� tpnum

	private int digitalFlag = -1;//ͷ�����ֱ��������õı�ʶλ

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
		//����ѵ��Ʒ��������������ı䣬�ڵ�˽����Ӧ�ø���
		if(YiDianDishActivity.hasChanged){
			for(Grptyp grptyp:grpLists){
				grptyp.setQuantity("0");//��ԭ����������ֵΪ0
			}
			
			for(Food fd:dishLists){
				fd.setSelected(false);//����Ϊδѡ״̬
				fd.setCounts("0");
			}
			List<Food> onlyTcLists = new ArrayList<Food>();//ֻ����ײͲ����ײ͵�TPNUM = 0��������ײ���ϸ
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
			onlyTcLists = null;//�����˾͸�ֵΪ�գ��ײ͵ĸ����������

			//����ͨ�ĵ�Ʒ��ֵ,��δѡ��״̬��Ϊѡ��״̬���ٸ�������
			for(Food mFood:guestLists){
				for(Food food:dishLists){
					if(!mFood.isIstc()&& mFood.getPcode().equals(food.getPcode())){
						food.setSelected(true);
						food.setCounts(mFood.getPcount());
					}
				}
			}

			//������߲�Ʒ���������
			for(Grptyp grptyp:grpLists){
				int countGrp = 0;
				String grpCode = grptyp.getGrp();//�õ��ô���ı��
				for(Food f:dishLists){
					if(grpCode.equals(f.getSortClass())){
						countGrp += Double.parseDouble(ValueUtil.isEmpty(f.getCounts()) ? "0" : f.getCounts());
					}
				}
				grptyp.setQuantity(countGrp+"");
			}

			//������߲�Ʒ���ListView
			eatListAdapter.setSelectedPosition(currentPosition);
			eatListAdapter.setListSource(grpLists);
			eatListAdapter.notifyDataSetChanged();

			//�����ұ߲�ƷGridView
			String currentGrp = grpLists.get(currentPosition).getGrp();
			updateDishClass(currentGrp, dishLists);

			if(eatGridAdapter == null){
				eatGridAdapter=new EatableGridAdapter(this,dishClassLists);
			}
			eatGridAdapter.setEatableGridSource(dishClassLists);
			eatGridAdapter.notifyDataSetChanged();
			
			YiDianDishActivity.hasChanged = false;
		}//�ѵ��ҳ���������޸ģ��������
	}


	/**
	 * ���ù����Ʒ�ӿ�
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
						
						//ƥ��dishLists�����еĹ����Ʒ
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
							Log.d("����", "ʧ��");
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
	 * ��ÿ���ռ������¼�
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

				//�����ж�ĳ���ײ��Ƿ�ѡ�����
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
				
				tempFujiaFood = null;//rather special�������߲˵�����ʱ�������л�������һ����𣬸�ֵΪ��
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,long id) {
                final int radioValue = Integer.parseInt(radioValueShow.getText().toString());//�õ����ֱ��������ұߵ��Ǹ�ֵ
				Food foodItem = dishClassLists.get(position);
				//����ǹ����Ʒ���͵ô���
                if (foodItem.isSoldOut()) {
                    return;
                }
				foodOrder(foodItem,view);
            }
		});
		
	}


	/**
	 * �����ڶ���λ�ĶԻ���
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
		
		curTpnumTCfood = creatFood;//���ﲻ��Ϊ��һ���Ʒ���Ӹ�������׼�� �������ײ������ּƼ۷�ʽ������ʱ���õ�tpnum��tcmoneymode,��tpnum,tcmoneymode����ȥ       rather special
		
				
		linearTcLayout.setVisibility(View.VISIBLE);
		
		List<List<Food>> allTcDetailLists= getDataManager(this).queryTcItemLists(creatFood);//��ѯ�����ײ��µ�������ϸ���� 
		showTcdetails(allTcDetailLists);
			
	}

	/**
	 * ִ���ײͼӼ�,ֻ�е�һ�ּƼ۷�ʽ��ִ���ײͼӼ�
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
	 * ��ѯ�����ݣ���ʼ���ײ������view,�°汾
	 * @param allTcDetails
	 */
	private void showTcdetails(List<List<Food>> allTcDetails) {
		
		subGridViewAdapterLists = new ArrayList<EatableSubGridViewAdapter>();
		
		tempNotAlternativeLists = new ArrayList<Food>();

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
			
			if(singleLists.size() > 1){        //�ɻ�����

				//�ڴ˴�ƥ���ײ���ϸ��Ʒ�еĹ����Ʒ    ֻƥ��ɻ�����
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
				tcGroupMinCntTv.setText(ValueUtil.isNotEmpty(defaultSFood.getMinCnt())?defaultSFood.getMinCnt()+"����":"0" +"����");
				
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

						final int radioValue = Integer.parseInt(radioValueShow.getText().toString());//�õ����ֱ��������ұߵ��Ǹ�ֵ

						radioValueShow.setText("1");//����������ֱ�������ԭ
						digitalFlag = -1;

						final EatableSubGridViewAdapter adapter = (EatableSubGridViewAdapter) parent.getAdapter();

						final Food foodDefault = adapter.getDefaultsFood();

						final Food foodCurrent = singleLists.get(position + 1);

						//����ײ���ϸ�ǹ����Ʒ���͵ô���
						if (!foodCurrent.isSoldOut()) {
							tempFujiaFood = foodCurrent;//Ϊ�ײ���ϸ��Ʒ���Ӹ�������׼��
						} else {
							return;
						}
						foodCurrent.setTabNum(menu.getTableNum());
						foodCurrent.setOrderId(menu.getMenuOrder());
						foodCurrent.setMan(menu.getManCounts());
						foodCurrent.setWoman(menu.getWomanCounts());
//						foodCurrent.setTpnum(tpnumflag+"");//�����൱������,Ϊ�ײ���ϸ����tpnum
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
				
				
			}else if(singleLists.size() == 1 && singleLists.get(0).getMinCnt().equals(singleLists.get(0).getMaxCnt())){//���ɻ�����
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
	 * ��ѡ�������жϼӲ�
	 * @param foodCurrent
	 * @param radioValue
	 * @param view
	 * @param foodDefault
	 * @param tcCountsChoosedTv
	 * @param adapter
	 */
	public void addfood(final Food foodCurrent, final int radioValue, final View view, final Food foodDefault, final TextView tcCountsChoosedTv, final EatableSubGridViewAdapter adapter){
		if (foodCurrent.getFujiaModel().equals(1) && radioValue > 0) {//�ж��Ƿ��б�ѡ������
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
	 * �Ӳ˲���
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
					//ִ���ײͼӼ�,ֻ�е�һ�ּƼ۷�ʽ��ִ���ײͼӼ�
					executeAddJustPrice(curTpnumTCfood, foodCurrent, radioValue);
					//���ö��������Ѿ�ѡ�������
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

				//ִ���ײͼӼ�,ֻ�е�һ�ּƼ۷�ʽ��ִ���ײͼӼ�
				executeAddJustPrice(curTpnumTCfood, foodCurrent, radioValue);

				//���ö��������Ѿ�ѡ�������
				tcCountsChoosedTv.setText(adapter.getCountsAboutPcount() + "");
			}
		}else if(radioValue == 0){
			if(foodCurrent.isSelected()){
				//��������õ�foodCurrent�Ѿ�ѡ�������,����Ҫ�õ����ֵ
				int countFormer = Integer.parseInt(foodCurrent.getPcount());
				if(ValueUtil.isNotEmpty(foodCurrent.getMinCnt()) && Integer.parseInt(foodCurrent.getMinCnt()) > 0){
					foodCurrent.setSelected(true);
					foodCurrent.setPcount(foodCurrent.getMinCnt());
					adapter.notifyDataSetChanged();

					//���ö��������Ѿ�ѡ�������
					tcCountsChoosedTv.setText(adapter.getCountsAboutPcount()+"");

					//��radioValue= 0ʱ����ʾȡ����ѡ���ײ���ϸ,ͬʱҲ�ü���
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

					//���ö��������Ѿ�ѡ�������
					tcCountsChoosedTv.setText(adapter.getCountsAboutPcount()+"");

					//��radioValue= 0ʱ����ʾȡ����ѡ���ײ���ϸ,ͬʱҲ�ü���
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
	 * ��ʼ������
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

		//��ʼ����Ա��Ϣ
		VipMsg.iniVip(this, menu.getTableNum(),R.id.vipMsg_ImageView);	
		
		grpLists = getDataManager(this).getAllDishClassList();//��ò�Ʒ���
		dishLists =  getDataManager(this).getAllFoodList();//��ȡ���в�Ʒ
		
//		unitCurLists = getDataManager(this).queryCurUnitLists();//��ȡ���еڶ���λ��Ʒ
//		//ƥ��dishLists�����е��еڶ���λ�Ĳ�Ʒ
//		for(Food mFood:dishLists){
//			mFood.setWeightflg("1");
//			for(CurrentUnit cUnit:unitCurLists){
//				if(cUnit.getItcode().equals(mFood.getPcode())){
//					mFood.setWeightflg("2");//2�����еڶ���λ           1������һ��λ
//				}
//			}
//		}
		if (guestLists.size()==0){
			List<Food> cacheLists = new ValueSetFile<Food>(menu.getTableNum()
					+DateFormat.getStringByDate(new Date(),"yyyy-MM-dd")
					+menu.getMenuOrder()+".mc").read();//getDataManager(this).queryAllCheckIfCache(menu.getTableNum(), menu.getMenuOrder());
//			if(cacheLists.size() != 0){
				guestLists.clear();
				//����ѯ������food���ӵ�����������
				guestLists.addAll(cacheLists);
		}
		//����ѵ��Ʒ��������������ı䣬�ڵ�˽����Ӧ�ø���
		for (Food food :dishLists){
			//��Ʒ����
			for (Food guestFood :guestLists){
				if (food.getPcode().equals(guestFood.getPcode())&&(guestFood.getTpcode()==null||guestFood.getPcode().equals(guestFood.getTpcode()))){
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

		//����ڵ��ҳ�汣���ѵ�˵��������ݿ⣬�������ѵ��ҳ�����ݴ水ťҲ���ѵ�Ĳ�Ʒ���浽�������ݿ�
//		if(this.getIntent().getStringExtra("direction")!=null && this.getIntent().getStringExtra("direction").equals("MainDirection")){   //�ж��Ǵ��ĸ�activity��ת������.��mainactivity
//			List<Food> cacheLists = new ValueSetFile<Food>(menu.getTableNum()
//					+DateFormat.getStringByDate(new Date(),"yyyy-MM-dd")
//					+menu.getMenuOrder()+".mc").read();//getDataManager(this).queryAllCheckIfCache(menu.getTableNum(), menu.getMenuOrder());
//			if(cacheLists.size() != 0){
//				guestLists.clear();
//				//����ѯ������food���ӵ�����������
//				guestLists.addAll(cacheLists);
//
//
//				//���ײ͸�ֵ,��δѡ��״̬��Ϊѡ��״̬���ٸ�������,ͨ���ۼӵõ�����
//				List<Food> onlyTcLists = new ArrayList<Food>();//ֻ����ײͲ����ײ͵�tpnum = 0��������ײ���ϸ
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
//				//����ͨ�ĵ�Ʒ��ֵ,��δѡ��״̬��Ϊѡ��״̬���ٸ�������
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
//				//������߲�Ʒ���������
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
		
		//GridView�״���ʾ��ʱ�򣬳�ʼ��Ĭ�ϵ�ֵ
		setGridDefault();
		
		//ʵ������Щ�����ĶԻ���
		showIfSaveDialog();
				
	}

	/**
	 * GridView�״���ʾ��ʱ�򣬳�ʼ��Ĭ�ϵ�ֵ
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

		//���½����ϵ���ߵ�ListView
		eatListAdapter.setSelectedPosition(0);
		eatListAdapter.notifyDataSetChanged();

		accessSoldOutFace();//���ù���ӿ�
		
	}
	
	
	
	/**  
	 *  ���ݲ�Ʒ��𣬲�ѯ��������µľ����Ʒ����
	 * ����dishclass����
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

	/**
	 * ��ȡ��ת��������Bundle
	 * @return
	 */
	private Bundle getTblStateBundle() {
		return this.getIntent().getBundleExtra("topBundle");
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {		
		
		mLoadingDialog = new LoadingDialogStyle(this,this.getString(R.string.update_data));
		mLoadingDialog.setCancelable(false);

		//������ɫ��
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
				//��������ѵ��˲�Ʒ���������ʱ��������ʾ����ʾ�Ƿ񱣴�
                if(guestLists.size() == 0){
                    this.finish();
                }else{
					//�ײͻ�δѡ�����
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
				foodTotalPrice();
                tpnumflag = 0;//���ײͱ����Ϊ0
                break;

            case R.id.eatable_tcdetail_btn_cancel:
                linearTCDetails.removeAllViews();
                linearTcLayout.setVisibility(View.GONE);

                tempNotAlternativeLists = null;

                for(Food food:dishClassLists){
                    if(food.getPcode().equals(curTpnumTCfood.getPcode())){
						//��������1
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
                tpnumflag = 0;//���ײͱ����Ϊ0

                break;
            case R.id.orderinfo_ImageView:
                if(tipdialog==null) {
                    tipdialog = new AlertDialog.Builder(Eatables.this, R.style.Dialog_tip).setView(layout).create();
                    tipdialog.setCancelable(true);
                    Window dialogWindow = tipdialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
                    lp.x = 0; // ��λ��X����
                    lp.y = topLinearLayout.getHeight() -15; // ��λ��Y����
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
	 * ��������ؼ���ʱ������ѵ��˲ˣ��͵�����ʾ�Ƿ񱣴浽�������ݿ�
	 * @param guestLists
	 */
	private boolean saveToAllCheck(List<Food> guestLists) {
		
		return getDataManager(this).circleInsertAllCheck(guestLists);
	}


	/**
	 * ��Ʒ��Ϣ�����õ�
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
					
					InputMethodUtils.toggleSoftKeyboard(Eatables.this);

					//������ߵĲ�Ʒ����ɵ��
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
				if(guestLists.size() == 0){  //�������û��һ���ˣ��������ϳ���Ϊ0���Ͳ�����
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
		 * �ײ͵����ּƼ۷�ʽ
		 * @param pLists  singleton����
		 * @param wholeFoodLists    ���в�Ʒ�󼯺�
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
				
				 double rate = Double.parseDouble(tcFood.getPrice())/tcDetailPrices;//�õ�����
				 for(Food f:pLists){
					 if(f.isIstc() && !f.getTpcode().equals(f.getPcode()) && f.getTpcode().equals(tcFood.getPcode()) && tcFood.getTpnum().equals(f.getTpnum())){
						 
						 double newPrice = Double.parseDouble(f.getPrice()) * Double.parseDouble(f.getPcount()) * rate;
						 f.setPrice(ValueUtil.setScale(newPrice, 2, 5));//�����˲����ײ���ϸ�ļ۸���Ƕ�ݵ��ˣ������ǵ��ݵ�
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
						tcDetailsPrice+=Double.valueOf(mFood.getEachprice());
						mFood.setPrice(mFood.getEachprice());
					}
				}//�õ��ײ���ϸ��Ʒ�ļ۸���Ӻ�ĺ�
				
				tcFood.setPrice(ValueUtil.setScale(tcDetailsPrice, 2, 5));
			}else if(tcMode == 3){ 
				double tcDetailsPrice = 0.00;
				double tcaddPrice = 0.00;
				for(Food food:pLists){
					if(food.isIstc() && !food.getTpcode().equals(food.getPcode()) && food.getTpcode().equals(tcFood.getPcode()) && food.getTpnum().equals(tcFood.getTpnum())){
						double itemPrices = Double.parseDouble(food.getEachprice()) * Double.parseDouble(food.getPcount());
						tcaddPrice += itemPrices;
					}
				}//�õ��ײ���ϸ��Ʒ�ļ۸���Ӻ�ĺ�
				
				double tcInitialPrice = Double.valueOf(tcFood.getPrice());//�ײ�ԭ�м۸�
				
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
					double rate = tcInitialPrice/tcDetailsPrice;//�õ�����
					
					for(Food f:pLists){
						if(f.isIstc() && !f.getTpcode().equals(f.getPcode()) && f.getTpcode().equals(tcFood.getPcode()) && f.getTpnum().equals(tcFood.getTpnum())){
							double newPrice = Double.parseDouble(f.getPrice()) * Double.parseDouble(f.getPcount())* rate;
							f.setPrice(ValueUtil.setScale(newPrice, 2, 5));//�����˲����ײ���ϸ�ļ۸���Ƕ�ݵ��ˣ������ǵ��ݵ�
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
						 newConformPrice += Double.parseDouble(fd.getPrice()); 
					 }
					 
					 if(newConformPrice != tcInitialPrice){
						if(tcOnlys.size() != 0){//������жϣ�������е�MINCNT������0���ͱ�����Խ��
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
	 * �ж��Ƿ�ĳ���ײͻ������һ��ûѡ������ʾ
	 */
	private void judgeIsToLastLayer(List<Food> allTcLists){
		final List<Food> makeUpTcListsRes = new ArrayList<Food>();//�ײ�����õ�
		final List<Food> recommandTcListsRes = new ArrayList<Food>();//�ײ��Ƽ��õ�
		
		
		
		for(List<List<Food>> tcLists :entireTCOrderLists){
			int layerCounts = tcLists.size();//�ó�ĳ���ײ��ж��ٲ�
			int layerSatisfiedFlag = 0;
			for(List<Food> layerLists:tcLists){//�ó�ĳ���ײ�ÿһ��ļ���,�ж�ÿһ���Ƿ���ϸò��maxcnt��minCNT�������ж��������Ĳ��Ƿ�����������maxcnt��mincnt
				
				int groupMinCnts = 0;
				int groupMaxCnts = 0;
				
				boolean isGroupSatisfy = false;//�Ƿ���������mincnt��maxcnt
				boolean isSelfSatisfy = false;//�Ƿ������Լ���mincnt��maxcnt
				boolean isSelfMinCntSatisfy = true;//�Ƿ��Լ�����С����mincnt�õ�����
				
				for(Food mFood:layerLists){
					if(mFood.getDefalutS().equals("0")){
						groupMinCnts = ValueUtil.isNaNofInteger(mFood.getMinCnt());//�ó�ÿһ����С���޶�����
						groupMaxCnts = ValueUtil.isNaNofInteger(mFood.getMaxCnt());
					}
					break;
				}

				//�ж�ÿһ���Ƿ���ϸò��maxcnt��minCNT,ʵ�������ֻ�ж�minCNT����
				int minCntCounts = 0;
				for(Food food:layerLists){
					int recommandCnt = Integer.parseInt(food.getRecommendCnt());
					if(recommandCnt > 0 ){
						minCntCounts += recommandCnt;
					}
				}
				
				if(minCntCounts >= groupMinCnts){
//					Log.d("����ò��mincnt", "����ò��mincnt");
					isGroupSatisfy = true;
				}

				//�ж��������Ĳ��Ƿ�����������maxcnt��mincnt
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
				
				//�ж��������Ĳ��Ƿ����������maxcnt��mincnt
				for(Food f:layerLists){
					if(!f.getDefalutS().equals("0")){
						
						int recommandCnt = Integer.parseInt(f.getRecommendCnt());
						
						if(recommandCnt > 0 && recommandCnt >= Integer.parseInt(f.getMinCnt()) ){
//						Log.d("��������ײ���ϸ��mincnt��maxcnt��,����ֻ�ж�mincnt����", "��������ײ���ϸ��mincnt��maxcnt��,����ֻ�ж�mincnt����");

							isSelfSatisfy = true;
						}
					}
					
				}

				//�жϸò��Ƿ������ײ͵�Ҫ��,�ò�����groupcnt,ÿ����Ʒ����mincnt
				if(isGroupSatisfy && isSelfSatisfy && isSelfMinCntSatisfy){
					layerSatisfiedFlag += 1;
				}
			}//��ʾ����ײ��Ѿ�ѭ������




			//����õ�1����˵���������һ��û�е� ,�ͱ�ʾ���Ƽ��ײ�
			if(layerCounts - layerSatisfiedFlag == 1){
				String tpcode = tcLists.get(0).get(0).getTpcode();
				for(Food food:allTcLists){
					if(food.getPcode().equals(tpcode)){
//						Toast.makeText(this, "�Ƽ�"+food.getPcname(), Toast.LENGTH_SHORT).show();
						recommandTcListsRes.add(food);

					}
				}
			}

			//��ʾ������ײ�
			if(layerCounts == layerSatisfiedFlag){
				String tpcode = tcLists.get(0).get(0).getTpcode();
				for(Food f:allTcLists){
					if(f.getPcode().equals(tpcode)){
//						Toast.makeText(this, "���"+f.getPcname(), Toast.LENGTH_SHORT).show();
						makeUpTcListsRes.add(f);
					}
				}
			}
			
			

			
		}//��Ӧ��������forѭ��


		//����С��������
		if(makeUpTcListsRes.size() > 0 || recommandTcListsRes.size() > 0){
			if(!MyWindowManager.isWindowShowing()){
				MyWindowManager.createSmallWindow(this,new SmallWindowClickListener() {
					
					@Override
					public void floatWindowClick() {
						
						popupWindow.removeViews();//�����ʱ�������view
						
						if(makeUpTcListsRes.size() > 0){
							TextView tcTVMakeUpTitle = new TextView(Eatables.this);
							tcTVMakeUpTitle.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
							tcTVMakeUpTitle.setTextSize(20);
							tcTVMakeUpTitle.setGravity(Gravity.CENTER_HORIZONTAL);
							tcTVMakeUpTitle.setText("�ײ�����б�");
							
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
									//���ײͼ���ϸ���뵽singletonlist��
									Food food = makeUpTcListsRes.get(position);
									List<Food> lists = addIntoSingletonLists(food);
									
									
									//���ײ���ϸ�󼯺��е����ݽ���ɾ������
									updateEntireListTcData(food, entireTCOrderLists, lists);
									
									//������߲�Ʒ��𼰲�Ʒ��������������
									updateViewDishData(lists, dishLists);

									
									//ˢ�½���
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

								
							});
						}
						
						if(recommandTcListsRes.size() > 0){
							TextView tcTVRecomandTitle = new TextView(Eatables.this);
							tcTVRecomandTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							tcTVRecomandTitle.setTextSize(20);
							tcTVRecomandTitle.setGravity(Gravity.CENTER_HORIZONTAL);
							tcTVRecomandTitle.setText("�ײ��Ƽ��б�");
							
							popupWindow.addToView(tcTVRecomandTitle);
							
							for(Food food:recommandTcListsRes){
								
								TextView tcTVRecomandItemTitle = new TextView(Eatables.this);
								tcTVRecomandItemTitle.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
								tcTVRecomandItemTitle.setTextSize(18);
								tcTVRecomandItemTitle.setGravity(Gravity.LEFT);
								tcTVRecomandItemTitle.setPadding(5, 5, 5, 10);
								tcTVRecomandItemTitle.setText(food.getPcname());
								popupWindow.addToView(tcTVRecomandItemTitle);

								//������ʾ�ײ͵����һ����ϸ,�õ��ײ����һ���δѡ��Ĳ�Ʒ
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
	 * ��������Ʒ�������
	 * @param pLists
	 * @param entireFoods
	 */
	private void updateViewDishData(List<Food> pLists,List<Food> entireFoods){
		for(Food mFood:pLists){
			for(Food f:entireFoods){
				if(!f.isIstc() && mFood.getPcode().equals(f.getPcode())){
					int cntNormal = Integer.parseInt(f.getPcount());//��ͨ��Ʒԭ�е�����
					int cntSpecial = Integer.parseInt(mFood.getPcount());//����ײ͵�����
					int endCnt = cntNormal - cntSpecial;
																	
					f.setPcount(endCnt+"");
				}
			}
		}
	}
	
	/**
	 * ��entireTCOrderLists�е���ϸ��Ʒɾȥ
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
						//��Ĳ�Ը����ɶ��ֶ��ĵ��ҵ���������
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
	 * ����ϵ��ײ����ӽ�����������
	 * @param pFood
	 */
	private List<Food> addIntoSingletonLists(Food pFood) {
		
		//���ÿһ����ѡ��Ĳ�Ʒ
		List<Food> tcItemLists = new ArrayList<Food>();
		
		for(List<List<Food>> tcLists :entireTCOrderLists){
			if(!tcLists.get(0).get(0).getTpcode().equals(pFood.getPcode())){
				continue;
			}
			
			for(List<Food> layerLists:tcLists){
				
				int countLayerFlag = 0;//ÿһ��������������Ʊ�ʾ
				
				int maxCntLayer = 0;
				for(Food f:layerLists){
					if(f.getDefalutS().equals("0")){
						maxCntLayer = Integer.parseInt(f.getMaxCnt());
					}else{
						//ÿһ���˵�mincnt���>1���͵�����ȷ������˱�ѡ��
						if(Integer.parseInt(f.getMinCnt()) > 0){
							f.setPcount(f.getMinCnt());
							tcItemLists.add(f);
							countLayerFlag += Integer.parseInt(f.getPcount());
						}
					}
					
				}
				
				//�ٽ���ͨ�Ĳ�Ʒ���ӽ�ȥ
				for(Food mFood:layerLists){
					if(!mFood.getDefalutS().equals("0") && Integer.parseInt(mFood.getMinCnt()) <= 0){
						if(ValueUtil.isNotEmpty(mFood.getRecommendCnt()) && Integer.parseInt(mFood.getRecommendCnt()) > 0){
							mFood.setPcount("1");//�˴������⣬
							countLayerFlag += Integer.parseInt(mFood.getPcount());
							
							tcItemLists.add(mFood);
							
							//�����������жϣ�����ﵽ����������������break
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
	 * �Ƽ��ײ���ʾ���һ��
	 * @param food
	 */
	protected void showTcLastLayer(final Food food,SelectPicPopupWindow popupWindow) { 
		final List<Food> lastLayerLists = new ArrayList<Food>();//�ü��ϴ洢����û�е����һ��
		for(List<List<Food>> tcLists :entireTCOrderLists){
			if(!tcLists.get(0).get(0).getTpcode().equals(food.getPcode())){
				continue;
			}
			
			//��ȡ���һ��,�����һ��û��recommendCnt,�ͱ�ʾ����һ��,��֪������û�гɹ�
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
				
				Food foodItem = lastLayerLists.get(position);	//�����������뵽����������
				
				//�����ײ͵���ϸ��Ʒ���뵽����������
				List<Food> lists = addIntoSingletonLists(food);
				
				//���ײ���ϸ�󼯺��е����ݽ���ɾ������
				updateEntireListTcData(food, entireTCOrderLists, lists);
				
				//������߲�Ʒ��𼰲�Ʒ��������������
				updateViewDishData(lists, dishLists);

				
				//ˢ�½���
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
		});
		
		popupWindow.addToView(tcGridView);
		
	}
	
	//================================��ѯ�ײͱ�ѡ������Ŀ

	/**
	 * ��ȡ��ѡ������
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

    //================================��˲���=============================

	/**
	 * �㵥Ʒ����
	 * @param foodItem
	 * @param view
	 */
    public void foodOrder(final Food foodItem,View view){
        int radioValue = Integer.parseInt(radioValueShow.getText().toString());//�õ����ֱ��������ұߵ��Ǹ�ֵ
        if(!foodItem.isIstc()){
            tempFujiaFood = foodItem;//Ϊ��һ���Ʒ���Ӹ�������׼��,�������ͨ��Ʒ
        }
		if (radioValue==0){
			radioValueShow.setText("1");//����������ֱ�������ԭ
			digitalFlag = -1;
			foodItem.setSelected(false);//��ťѡ��
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

			//������߲�Ʒ���������
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
	//�ж��Ƿ��ײ�
	public void foodIstc(Food foodItem,View view){
		if (foodItem.isIstc()==true){
			radioValueShow.setText("1");//����������ֱ�������ԭ
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
			tcCallBack(foodItem);//���ײ���ϸ���в���
		}else {
			foodUnits(foodItem,view);
		}
	}
	//�൥λ
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
	//�Ƿ���ʱ��
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
	//�Ƿ������޸ļ۸�
	public  void foodState(final Food foodItem, final View view){
		if (foodItem.getState()==1){
			Dialog dialog = null;
			View layout = LayoutInflater.from(Eatables.this).inflate(R.layout.unitcur_layout, null);
			final EditText weightEdit = (EditText) layout.findViewById(R.id.dia_et_weightflag);

			AlertDialog.Builder builder = new AlertDialog.Builder(Eatables.this,R.style.edittext_dialog);
			builder.setTitle("������۸�").setView(layout)
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
	//�Ƿ�ڶ���λ
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
	//���븽����򸽼Ӳ�Ʒ
	public void foodFujiaModeOrIsAddPro(Food foodItem,View view){
		if (foodItem.getFujiaModel().equals(1)){
			new MustFuJiaView(foodItem, Eatables.this,view).showView();
		}else {
			foodDis(foodItem);
		}
	}
	//���
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
	//��ʾ��Ʒ�۸�
	private void foodTotalPrice(){
		Double foodTotalPrice=0.00;
		for (Food food:guestLists){
			if (food.isIstc()==false||(food.isIstc()==true&&food.getTpcode()!=food.getPcode()))
				foodTotalPrice+=(Double.parseDouble(food.getPrice())*Double.parseDouble(food.getPcount()));
		}
		DecimalFormat df = new DecimalFormat("0.00");
		if (foodTotalPrice>0) {
			yiDianBtn.setText("�ѵ��(" + df.format(foodTotalPrice) + ")");
		}else {
			yiDianBtn.setText(R.string.eatable_yidiancai);
		}
	}
}