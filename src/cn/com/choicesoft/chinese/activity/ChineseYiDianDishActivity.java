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
 * �ѵ��Ʒҳ��
 * @author dell
 *
 */
public class ChineseYiDianDishActivity extends BaseActivity implements OnClickListener{
	
	
	private ExpandableListView expandListView;//ҳ��������ʾ���ݰ󶨵Ŀؼ���ExpandableListView
	private YidianDishAdapter adapter;//ExpandableListView��Ӧ��������
	
	
	private ListView listView;//����ʱ������ʾ�Ŀؼ���ListView
	private YiDianDishSearchAdapter searchAdapter;//ListView��Ӧ��������
	
	private Button tablelogo;//̨λ
	private Button tempSave;//�ݴ�,����
	private Button allBillAddtions ;//�ر�ע
	private Button allBills;//ȫ��
	private Button sendwait;//����
	private Button sendprompt ;//����
	private Button back;//����
	private Button deleteAll;//ȫ��ɾ��
	
	private Button btnSearchView;
	private EditText etSearchView;//������
	
	private List<Food> guestDishes = GuestDishes.getGuDishInstance();
	
	private List<Food> adapterDataSourceLists = null;//expandablelistview������Դ
	
	private SingleMenu menu = SingleMenu.getMenuInstance();

	private LoadingDialog authLoadingDialog;//��֤��Ȩ 
	private LoadingDialog sendLoadingDialog;//���Ͳ�Ʒ
	
	private TextView dishCounts,totalMoney,fujiaMoney;
	
	private TextView tableNum,orderNum,peopleNum,userNum;//ͷ����ɫ����������
	private List<PresentReason> presentreasonLists;//�����Ʒ����
	private List<Food> matchLists = new ArrayList<Food>();//��Ʒ��Ϣ�������Ľ����ƥ��ļ���
	private List<Addition> fujiamatchLists = new ArrayList<Addition>();//��������Ϣ�������Ľ����ƥ��ļ���
	private List<CommonFujia> specialfujiaMatchLists = new ArrayList<CommonFujia>();//ȫ����������Ϣ�������Ľ����ƥ��ļ���
	
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
	 * �������
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
		
		dishCounts.setText(adapter.getDishSelectedCount()+this.getString(R.string.part));//�����Ʒ��������
		
		double totalSalary = adapter.getTotalSalary();
		totalMoney.setText(ValueUtil.setScale(totalSalary, 2, 5));//�����Ʒ���ܼ�Ǯ
		
		double totalFujiaSalary = adapter.getFujiaSalary();
		fujiaMoney.setText(ValueUtil.setScale(totalFujiaSalary, 2, 5));//�����Ʒ��������ܼ�Ǯ
	}

	/**
	 * �ؼ���ʼ��
	 */
	private void initViews() {
		
		mLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
		mLoadingDialog.setCancelable(false);
		
		digitalLayout = (LinearLayout) this.findViewById(R.id.toptitle_rg_number);
		digitalLayout.setVisibility(View.GONE);
		
		//��Ʒ��3          �ܼƣ�179.00      �����
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

        //������ɫ��
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
		//TODO �вͲ���Ҫ
		//presentreasonLists = getDataManager(this).queryPresentReason();//��ѯ���ݿ�ó����˵�ԭ��
		
		
		//TODO ��ʼ����Ա��Ϣ �вͲ���Ҫ
		//VipMsg.iniVip(this, menu.getTableNum(),R.id.vipMsg_ImageView);
		
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
		case R.id.eatable_btn_back://����
        case R.id.yidian_btn_back://����
            Intent intent = new Intent(ChineseYiDianDishActivity.this, ChineseEatables.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("direction", "XS");
            startActivity(intent);
            break;

		case R.id.yidian_btn_deleteall://ȫ��ɾ��
			guestDishes.clear();
			updateDisplay();
			break;
			
		case R.id.yidian_btn_allWaitOrPrompt:  //������
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
			updateDisplay();//���½���View
			break;
			
		case R.id.yidian_btn_sendprompt: //������
			if(TsData.isReserve){
				alertReserve(this.getString(R.string.only_save_error));
				break;
			}
            guQing("N");
			break;
					
		case R.id.yidian_btn_allbills:    //ȫ��
			startActivity(new Intent(ChineseYiDianDishActivity.this, ChineseYiXuanDishActivity2.class).putExtra("table",menu.getTableNum()));
			break;
			
		case R.id.yidian_btn_allbilladditions://�ر�ע
			
			final List<CommonFujia>  commonFujiaLists = getDataManager(this).queryCommonFujia();//��Ź����������
			
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
					//��������
					InputMethodUtils.toggleSoftKeyboard(ChineseYiDianDishActivity.this);

				}
			});
			
			//�Զ��帽����Ŀؼ�EditText
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
                        StringBuilder buildPrice = new StringBuilder();//����
                        StringBuilder buildDes = new StringBuilder();//����
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
                    //����������
                    InputMethodUtils.hideSoftKeyboard(ChineseYiDianDishActivity.this,selfFujiaEdit);
                    updateDisplay();//����ҳ��
                }
            });
			
			Button buttonCancel = (Button) commonLayout.findViewById(R.id.dia_quandanfujia_btn_cancel);
			buttonCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
                    //����������
                    InputMethodUtils.hideSoftKeyboard(ChineseYiDianDishActivity.this,selfFujiaEdit);
				}
			});
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(ChineseYiDianDishActivity.this,R.style.edittext_dialog);
			builder.setView(commonLayout);
			
			dialog = builder.create();
			dialog.show();
			break;
			
			
		case R.id.yidian_btn_tempsave:  //����
			if(TsData.isReserve){
				//TODO ����
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
			
		case R.id.yidian_btn_tbl:   //̨λ
			showIfSaveDialog().show();//������ʾ�Ի�����ʾ�û��Ƿ񱣴�
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
					for(int i = 0;i<guestDishes.size();i++){
						guestDishes.get(i).setSend("0");
					}
					boolean insertSuccess = new OrderSaveUtil().insertHandle(ChineseYiDianDishActivity.this,menu.getTableNum(),menu.getMenuOrder(),guestDishes);
					if(insertSuccess){
						Toast.makeText(ChineseYiDianDishActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(ChineseYiDianDishActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
					}
					guestDishes.clear();//���singleton����
					Intent intent = new Intent(ChineseYiDianDishActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
					break;
					
				case 1:
					guestDishes.clear();//���singleton����
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
	 * �жϲ������ݿ��Ƿ�ɹ�
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
	 * ������֤
	 * @param type
	 * @return
	 */
    public boolean guQing(final String type){
        //�����û�е�ˣ�����ʾ
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
     * ��Ʒ����ƴ��
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
            CSLog.e("��Ʒ������������",e.toString());
        }
        return null;
    }

    /**
     * ��Ʒ����
     * @param typ
     */
	public void send(String typ){
        StringBuffer sb=new StringBuffer();
        String dataCount=dataTrim(sb);
        if(ValueUtil.isEmpty(sb)){
            ToastUtil.toast(this,R.string.food_data_error);
            return ;
        }
        Log.e("�вͲ�Ʒ",dataCount.toString());
        Log.e("�вͲ�Ʒ����",sb.toString());
        CList<Map<String,String>> cList=new CList<Map<String, String>>();
        cList.add("pdaid",SharedPreferencesUtils.getDeviceId(this));//pad����
        cList.add("user",SharedPreferencesUtils.getUserCode(this));//�û�����
        cList.add("pdaSerial","0");//�ϴ���Ʒ���������ֵΪһ���ϴ��˶��ٲ�Ʒ�����뱣���ڻ�����
        cList.add("acct",menu.getMenuOrder());//�˵���ţ����˵���ž���д��û��Ϊ��ֵ
        cList.add("tblInit",menu.getTableNum());//̨λ��д
        cList.add("waiter","");//����Ա���
        cList.add("pax","1");//���� TODO
        cList.add("zcnt",dataCount);//�ϴ���Ʒ����
        cList.add("typ",typ);//�ϴ����ͣ�Y,N,A Y����N����A������
        cList.add("buffer",sb.toString());//�ϴ�ֵ
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
                        //ʵ����food�е��ֶ�send�������ѷ��Ͳ�Ʒ�������ݴ�   ��Ϊ�丳ֵ             �ѷ���1          �ݴ�0
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
     * ��Ʒ����ƴ��
     * @param sb
     * @return
     */
	public String dataTrim(StringBuffer sb){
        try{
            /*�ײͱ��(��Ʒ��-1)
            �ײ�����(��Ʒ��0)|
                    ��Ʒ�� ��|
                    ��Ʒ����(��һ��λ)|
                    ��Ʒ ��λ(��һ��λ)|
                    ��Ʒ����|
                    �� Ʒ����(�ڶ���λ)|*/
            String tcount = null;
            Integer dataCount = 0;
            for (Food food : adapterDataSourceLists) {
                if (food.getPcode() != null && food.isIstc()&&food.getPcode().equals(food.getTpcode())) {
                    tcount = food.getPcount();
                    continue;
                }
                if (food.isIstc()) {//������ײ������ײͱ��룬����-1��
                    sb.append(food.getTpcode() + "|");//�ײͱ���
                    sb.append(tcount + "|");//�ײ�����
                } else {
                    sb.append("-1|");//�ײͱ���
                    sb.append("0|");//�ײ�����
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
                        .append(food.getPlusd()==0?"|":"ֻ|")//�ڶ���λ
                        .append("N|")

                        .append(food.getPKID()+"|")//Ψһ��ʶ
                        .append((food.getCLASS()==null?"N":food.getCLASS())+"|^");//������� Y����

                Log.i("��֪��", food.getPlusd()==0?"|":food.getUnit2()+"|");
                Log.i("��", sb.toString());
                dataCount++;
            }
            return dataCount.toString();
        }catch (Exception e){
            CSLog.e("��Ʒ������������",e.toString());
        }
        return null;
    }
	/**
	 * ExpandableListView��������
	 * @author dell
	 *
	 */
	public class YidianDishAdapter extends BaseExpandableListAdapter{
		
		private Context mContext;
		private List<Food> groupAdapterLists = new ArrayList<Food>();//��Ŀ¼��Ӧ�ļ���
		private List<List<Food>> childAdapterLists = new ArrayList<List<Food>>();//��Ŀ¼��Ӧ�ļ���
		LayoutInflater  layoutInflater;
		
		private List<Food> integratedLists;//�ܵļ���
		

		public YidianDishAdapter(Context pContext,List<Food> pLists) {
			super();
			
			this.integratedLists = pLists;
			this.mContext = pContext;
			layoutInflater = LayoutInflater.from(mContext);
			
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
		
		//���°�����Դ
		public void setSingleInstance(List<Food> changedLists){
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
			double totalFujiaSalary = 0.00;
			for(Food f:guestDishes){
				if(ValueUtil.isNotEmpty(f.getChineseFujia())){//�����ò�Ʒ�и�����,�Զ��帽����۸�Ϊ0��������
					for(String fj:f.getChineseFujia()){
						//������Զ��帽����۸�Ϊ�������ͱ���,�����ж�һ��
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
            TextView nameChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesname);//��Ʒ����
			TextView countsChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishescounts);//����
			TextView priceChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesprice);//�۸�
			TextView unitChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_dishesunit);//��λ
			ImageView additionChildImage = (ImageView) convertView.findViewById(R.id.yidian_expand_item_child_addition);//����

			//�ڶ��е��ײ���ϸ�ĸ�����
			LinearLayout singleAddChildLayout = (LinearLayout) convertView.findViewById(R.id.yidian_expand_item_child_linear);
			TextView singleAddChildText = (TextView) convertView.findViewById(R.id.yidian_expand_item_child_show);
			
			//����и��������ʾ��û�о����صڶ���
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
			countsChildText.setText(food.getPcount());//������ʾ�ײ���ϸÿ����Ʒ������
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
				LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(screemwidth/7,screemwidth/14);
				lp2.setMargins(3, 3, 3, 5);
				countsText.setLayoutParams(lp2);
			}
			
			
			//�ײͲ������Ӹ�����ײ���ϸ������,����ť��Ϊ���ɵ�
			if(f.isIstc() && f.getPcode().equals(f.getTpcode())){
				additionImage.setEnabled(false);
                additionImage.setVisibility(View.INVISIBLE);
			}
			
			//����и��������ʾ��û�о����صڶ���
			LinearLayout singleAddShowLayout = (LinearLayout) convertView.findViewById(R.id.yidian_singlefujia_group_linear);
			TextView singleAddShowText = (TextView) convertView.findViewById(R.id.yidian_singlefujia_group_show);
            //����и��������ʾ��û�о����صڶ���
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

            //=======================�ж��Ƿ�൥λ======================
            if(ValueUtil.isNotEmpty(f.getUnitMap())){
                unitText.setOnClickListener(new DishUnitClick(f));
            }
            //============================================
			
			//����漰�����ˣ�Ʒ���͸��� 
			if(ValueUtil.isNotEmpty(f.getPromonum())){  //�����˵���������
				
				//���������  �ײ�    ��ͨ��Ʒ
				if(f.isIstc()){
					StringBuilder sb = new StringBuilder();
					sb.append(f.getPcname())
					.append("-"+ChineseYiDianDishActivity.this.getString(R.string.give))
					.append(f.getPromonum());//��Ϊ�ײ�ֻ��һ�ݣ��ײͶ��ǵ�����ʾ�ģ�����ֱ����1Ҳ����
					
					nameText.setText(sb.toString());
				}else if(!f.isIstc()){
					StringBuilder sb = new StringBuilder();
					sb.append(f.getPcname())
					.append("-"+ChineseYiDianDishActivity.this.getString(R.string.give))
					.append(f.getPromonum());
					
					nameText.setText(sb.toString());
				}
			}else{//����û������
				//String [] realPcname = f.getPcname().split("-");
				nameText.setText(f.getPcname());
			}
			
			
			countsText.setText(f.getPcount());//����
			priceText.setText(f.getPrice());//�۸�
			unitText.setText(f.getUnit());//��λ
			
			//��С�Ƹ�ֵ, ֻ���ײ�����ͨ��Ʒ����С�ƣ�����ֵ,�ײ���ϸû��С��
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

			//Ϊ��ť���ü���
            jijiao.setOnClickListener(new JiJiaoClick(f));
			additionImage.setOnClickListener(new AdditionChildClick(f));
			plusImage.setOnClickListener(new PlusImageClick(f));
			minusImage.setOnClickListener(new MinusImageClick(f));
			presentImage.setOnClickListener(new PresentImageClick(f));
			deleteImage.setOnClickListener(new DeleteClick());
			deleteImage.setTag(groupPosition);//����tag,��Ȼ�Ͳ�����λ
			return convertView;
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
                                updateDisplay();//���½���View
                            }
                        }
                    }
                }) .setNegativeButton(R.string.cancle,null).show();
            }

        }
		//��Ʒ������ĵ���¼�,��ȫ��������
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
		 * �����˰�ť
		 */
		class PresentImageClick implements OnClickListener{
			private Food f;

			public PresentImageClick(Food f) {
				super();
				this.f = f;
			}


			@Override
			public void onClick(View v) {
                ToastUtil.toast(ChineseYiDianDishActivity.this,"�в��ݲ�֧�����ͣ�");
                return;
			}
			
		}


		/**
		 * ������  - ��ť�ĵ����,���ּӵ����ֻ��������ͨ��Ʒ���ײͲ��ܼ�
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
				
				updateDisplay();//���½���View
			}
			
		}

		/**
		 * ������  + ��ť�ĵ����,���ּӵ����ֻ��������ͨ��Ʒ���ײͲ��ܼ�
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
				
				updateDisplay();//���½���View
			}
			
		}

		/**
		 * ��ɾ����ť�ĵ����
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

                        //����������������Ӧ������ɾ��
                        Food foodRemove = groupAdapterLists.get(position);
                        //�ӵ���������ɾ�����ײͼ���Ӧ����ϸ
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
                        } else {     //�ӵ���������ɾ���õ�Ʒ
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
                        updateDisplay();//���½���
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
						Double countPresent = ValueUtil.isNaNofDouble(presentCount);
						Double countPcount = ValueUtil.isNaNofDouble(pFood.getPcount());
                        Double countPcountLeft = countPcount - countPresent;//��˵�����������ȥ���Ͳ�Ʒ���������õ�ʣ�µ�����
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
					}else{//����������,�������ײ��ѱ�����,ֱ�ӷ���0.00����
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
		int moneyAuth = Integer.parseInt(SharedPreferencesUtils.getGiftAuth(ChineseYiDianDishActivity.this));
		
		//���ж������Ʒ������
		Double dishCount = ValueUtil.isNaNofDouble(pFood.getPcount());
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
				Double countGiftBefore = ValueUtil.isNaNofDouble(pFood.getPromonum());//�õ���ǰ�Ѿ����͵�����
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
	 * //����������Ȩ�������ĶԻ���
	 * @param dianDishes
	 */
	private void createPresentAuthAndCountDia(final Food dianDishes) {
		PresentAuthAndCountDialog authAndcountDia = PresentAuthAndCountDialog.newInstance(this.getString(R.string.give_food_authorization), (ArrayList<PresentReason>)presentreasonLists, new DialogPresentAuthAndCountClickListener() {
			
			@Override
			public void doPositiveClick(String authorNum, String authorPwd,final String reason, final String countgift, final String countCancel) {
				//���ж���Ȩ,���ж�����������Ƿ���ȷ
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
                                //��Ȩ�ɹ�
                                Log.e("��Ȩ����ֵ", result);
                                if (ValueUtil.isNotEmpty(countgift) && ValueUtil.isNotEmpty(countCancel)) {
                                    Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
                                } else if (ValueUtil.isNotEmpty(countgift) && ValueUtil.isEmpty(countCancel)) {//��ʾ����
                                    String promonumgiftBefore = dianDishes.getPromonum();//�õ���ǰ�Ѿ����͵�����

                                    if (ValueUtil.isNotEmpty(promonumgiftBefore) && ValueUtil.isNaNofDouble(promonumgiftBefore) + ValueUtil.isNaNofDouble(countgift) > ValueUtil.isNaNofDouble(dianDishes.getPcount()) || ValueUtil.isNaNofDouble(dianDishes.getPcount()) < ValueUtil.isNaNofDouble(countgift) || ValueUtil.isNaNofDouble(countgift) < 1) {
                                        Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    setFoodPromnumAndReason(dianDishes, guestDishes, reason, countgift);//���ｫȫ����guestDishes���˽�ȥ

                                    //�����ǰ��������ģʽ��
                                    if (listView.getVisibility() == View.VISIBLE && expandListView.getVisibility() == View.GONE) {
                                        etSearchView.setText("");
                                        listView.setVisibility(View.GONE);
                                        expandListView.setVisibility(View.VISIBLE);
                                    }

                                    updateDisplay();
                                } else if (ValueUtil.isEmpty(countgift) && ValueUtil.isNotEmpty(countCancel)) {//��ʾȡ������
                                    if (ValueUtil.isEmpty(dianDishes.getPromonum()) || ValueUtil.isNaNofDouble(dianDishes.getPromonum()) < ValueUtil.isNaNofDouble(countCancel) || ValueUtil.isNaNofDouble(countCancel) < 1) {
                                        Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    cancelFoodPromnum(dianDishes, guestDishes, countCancel);

                                    //�����ǰ��������ģʽ��
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
			Double presentBefore = ValueUtil.isNaNofDouble(pFood.getPromonum());//�õ�ԭ�е����͵�����
			Double cancelCountsInt = ValueUtil.isNaNofDouble(cancelCounts);//�õ�Ҫȡ�����͵�����
			Double countPromLeft = presentBefore - cancelCountsInt;
			
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
					Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
				}else if(ValueUtil.isNotEmpty(giftCounts) && ValueUtil.isEmpty(cancelGiftCounts)){//��ʾ����
					String promonumgiftBefore = dianDishes.getPromonum();//�õ���ǰ�Ѿ����͵�����
					
					if(ValueUtil.isNotEmpty(promonumgiftBefore)&&ValueUtil.isNaNofDouble(promonumgiftBefore) + ValueUtil.isNaNofDouble(giftCounts) > ValueUtil.isNaNofDouble(dianDishes.getPcount()) || ValueUtil.isNaNofDouble(dianDishes.getPcount()) < ValueUtil.isNaNofDouble(giftCounts) || ValueUtil.isNaNofDouble(giftCounts) < 1){
						Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
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
					if(ValueUtil.isEmpty(dianDishes.getPromonum()) || ValueUtil.isNaNofDouble(dianDishes.getPromonum()) < ValueUtil.isNaNofDouble(cancelGiftCounts) || ValueUtil.isNaNofDouble(cancelGiftCounts) < 1){
						Toast.makeText(ChineseYiDianDishActivity.this, R.string.input_count_error, Toast.LENGTH_SHORT).show();
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
                                //��Ȩ�ɹ�
                                Log.e("��Ȩ����ֵ", result);

                                setFoodPromnumAndReason(pFood, guestDishes, reason, "1");//��Ϊֻ����������1ʱ�����ҵ�����Ʒ�۸����49���ŵ����˶Ի���ֱ���� 1

                                //�����ǰ��������ģʽ��
                                if (listView.getVisibility() == View.VISIBLE && expandListView.getVisibility() == View.GONE) {
                                    etSearchView.setText("");
                                    listView.setVisibility(View.GONE);
                                    expandListView.setVisibility(View.VISIBLE);
                                }
                                updateDisplay();//����ҳ��
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
					searchAdapter = new YiDianDishSearchAdapter(ChineseYiDianDishActivity.this, matchLists);
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
		
		final List<Addition> additionLists;
		
				
		//�ָ��ݲ�Ʒ�����ѯ�����и�����õ����ϣ�������ϵĳ�����0���ٲ�ѯ 
        additionLists =  getDataManager(ChineseYiDianDishActivity.this).getAllFujiaListByPcode(pFood.getPcode());//��ѯ�����и����� ;
		
		View sinleLayout = LayoutInflater.from(ChineseYiDianDishActivity.this).inflate(R.layout.dia_singlefujia, null);
		
		//�����Ի���
		final Button btnSearch = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_searchView);
		final cn.com.choicesoft.view.ClearEditText etSearch = (ClearEditText) sinleLayout.findViewById(R.id.dia_singlefujia_et_searchView);
		
		final SingleFujiaAdapter singleFujiaAdapter = new SingleFujiaAdapter(ChineseYiDianDishActivity.this, additionLists);

		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnSearch.setVisibility(View.GONE);
				etSearch.setVisibility(View.VISIBLE);
				etSearch.requestFocus();
				//��������
				InputMethodUtils.toggleSoftKeyboard(ChineseYiDianDishActivity.this);
				etSearch.addTextChangedListener(new SingleFujiaWatcher(etSearch, additionLists, singleFujiaAdapter));
			}
		});
		
		ListView singleFujiaLV = (ListView) sinleLayout.findViewById(R.id.dia_singlefujia_listView);
		
		//�ڴ˴��жϣ�����ò�Ʒ�Ѿ����˸�����͸���
		if(ValueUtil.isNotEmpty(pFood.getChineseFujia())){
			for(String code:pFood.getChineseFujia()){
				for(Addition addition:additionLists){
					if(code.split("\\|")[0].equals(addition.getFoodFuJia_des())){//TODO ʹ�õ�DES
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
		
		
		//�Զ��帽�����EditText
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
                //����������
                InputMethodUtils.hideSoftKeyboard(ChineseYiDianDishActivity.this,selfFujiaAddEdit);

                //���������ģʽ�£�������Ӹ����ť����Ҫִ�����²���
                if (listView.getVisibility() == View.VISIBLE && expandListView.getVisibility() == View.GONE) {
                    listView.setVisibility(View.GONE);
                    expandListView.setVisibility(View.VISIBLE);
                }
                updateDisplay();//����ҳ��

            }
        });
		
		Button diaBtnCancel = (Button) sinleLayout.findViewById(R.id.dia_singlefujia_btn_cancel);
		diaBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
                //����������
                InputMethodUtils.hideSoftKeyboard(ChineseYiDianDishActivity.this,selfFujiaAddEdit);
			}
		});
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ChineseYiDianDishActivity.this,R.style.edittext_dialog);
		builder.setView(sinleLayout);
		dialog = builder.create();
		dialog.show();
		
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
			
			//�������Ǵ��ײ�.
			if(food.isIstc() && food.getPcode().equals(food.getTpcode())){
				holder.plusImage.setVisibility(View.INVISIBLE);
				holder.minusImage.setVisibility(View.INVISIBLE);
			}else{
                holder.dishCounts.setOnClickListener(new DishCountsClick(food));
            }
			
			//���������ײ���ϸ
			if(food.isIstc() && !food.getPcode().equals(food.getTpcode())){
				holder.plusImage.setVisibility(View.INVISIBLE);
				holder.minusImage.setVisibility(View.INVISIBLE);
				
				holder.presentImage.setVisibility(View.INVISIBLE);
				holder.deleteImage.setVisibility(View.INVISIBLE);
			}else{
                holder.dishCounts.setOnClickListener(new DishCountsClick(food));
            }
            //=======================�ж��Ƿ�൥λ======================
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
			}
			
			
		}
		/**
		 * ���������ģʽ�µ����Ӹ����ť
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
					AlertDialog.Builder builder = new AlertDialog.Builder(ChineseYiDianDishActivity.this);
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
						Double counts = ValueUtil.isNaNofDouble(mFood.getPcount());
						counts += 1;
						
						mFood.setCounts(counts+"");
						mFood.setPcount(counts+"");
						
						etSearchView.setText("");
						listView.setVisibility(View.GONE);
						expandListView.setVisibility(View.VISIBLE);
						updateDisplay();//���½���View
					}
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
                                updateDisplay();//���½���View
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
                    updateDisplay();//���½���View
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