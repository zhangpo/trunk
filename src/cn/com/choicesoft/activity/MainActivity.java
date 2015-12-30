package cn.com.choicesoft.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.*;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.action.CombineTable;
import cn.com.choicesoft.activity.action.CombineTableListView;
import cn.com.choicesoft.activity.wait.WaitMain;
import cn.com.choicesoft.adapter.OrderPayAdapter;
import cn.com.choicesoft.adapter.TaiWeiAdapter;
import cn.com.choicesoft.bean.*;
import cn.com.choicesoft.chinese.activity.ChineseEatables;
import cn.com.choicesoft.chinese.activity.ChineseFind_orderByTaiNum;
import cn.com.choicesoft.chinese.activity.ChineseGuQingActivity;
import cn.com.choicesoft.chinese.activity.ChineseWelcomeActivity;
import cn.com.choicesoft.chinese.activity.ChineseYiXuanDishActivity2;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseResultAlt;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.chinese.util.JsonUtil;
import cn.com.choicesoft.chinese.util.OrderSaveUtil;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.impl.MainGridViewClickImpl;
import cn.com.choicesoft.singletaiwei.YiXuanDishActivity2;
import cn.com.choicesoft.table.ReserveTableEvent;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.ClearEditText;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * �˵�������
 */
public class MainActivity extends BaseActivity implements OnClickListener,MainGridViewClickImpl {
    /**
     * 0Ϊ��� 1Ϊ�в�
     */
	private int CHIN_SNACK=0;
	private GridView gridView;//
    private TextView tidanbianhao;
    private String   orderNum;
	private LinearLayout LayTitle;
	private LoadingDialog loadingDialog;
	private ArrayList<Storetable> tableLists = null;	
	private PopupWindow rightPw;//Ԥ��̨λ�ұ�PopupWindow
	private PopupWindow leftPw;//Ԥ��̨λ���popupWindow
	private cn.com.choicesoft.view.ClearEditText searchEdit;
	private Button searchBtn;
	private ReserveTableEvent reserveTableEvent=null;//Ԥ��̨λʱ�䴦����
    private List<Map<String,String>> orderMapList;//��Ҫ֧���˵�����
	

	// ��Ļ�Ŀ���
	public static int screenWidth;
	public static int screenHeight;
	
	//ˢ�°�ťλ��
    public Integer lastX,lastY, downX, downY;
	
	private View searchLayout;
	
	private Button refresh,bingtai, search, change, waitTable,checkOrder,logOut,peopleCount,payOrder,estempty,openTable;

//	private SingleMenu singleMenu = SingleMenu.getMenuInstance();
	private TaiWeiAdapter adapter;//̨λadapter
	
	private List<Food> guestLists = GuestDishes.getGuDishInstance();

	public List<Food> getGuestLists() {
		return guestLists;
	}

    public LoadingDialog getLoadingDialog() {
        if(loadingDialog==null) {
            loadingDialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
            loadingDialog.setCancelable(true);
        }
        return loadingDialog;
    }

    /**
	 * ���������ڵ�Layout
	 * @return
	 */
	public View getSearchLayout() {
		if(searchLayout==null){
			searchLayout=this.findViewById(R.id.main_table_soText);
		}
		return searchLayout;
	}
	public void setSearchLayout(View searchLayout) {
		this.searchLayout = searchLayout;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new LanguageSetting().setting(this);
		DisplayMetrics dmm = new DisplayMetrics();
		// ��ȡ��������
		getWindowManager().getDefaultDisplay().getMetrics(dmm);
		// ���ڿ���
		screenWidth = dmm.widthPixels;
		// ���ڸ߶�
		screenHeight = dmm.heightPixels;

		// ����Ϊ�ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����Ϊȫ��ģʽ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CHIN_SNACK=SharedPreferencesUtils.getChineseSnack(this);
        if(CHIN_SNACK==0){
        	if (ChioceActivity.ispad) {
				setContentView(R.layout.activity_main2_pad);
			} else {
				setContentView(R.layout.activity_main2);
			}
        }else{
        	if (ChioceActivity.ispad) {
				setContentView(R.layout.chinese_activity_main2_pad);
			} else {
				setContentView(R.layout.chinese_activity_main2);
			}
        }
        
		findView();
		initData();
	}

	/**
	 * ̨λԤ�� �õ����¼���
	 * @return
	 */
	public ReserveTableEvent getReserveTableEvent() {
		reserveTableEvent=new ReserveTableEvent(this);
		return reserveTableEvent;
	}
	/**
	 * Toast����
	 * @param Text
	 */
	public void showToast(String Text){
		Toast.makeText(this, Text, Toast.LENGTH_LONG).show();
	}
    /**
     * Toast����
     */
    public void showToast(int id){
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
    }
	/**
	 * Ԥ��̨λ �ұ���ʾ��Ԥ����Ϣ popupWindow
	 * @return
	 */
	public PopupWindow getRightPw() {
		if(rightPw==null){
			View vPopupWindow=this.getLayoutInflater().inflate(R.layout.yuding_view, null); 
			vPopupWindow.setFocusable(true); // �������Ҫ
			vPopupWindow.setFocusableInTouchMode(true);
			int screenWidth = (int) (MainActivity.screenWidth *0.5);
			int gridHeight=this.gridView.getHeight();
			int soHeight=getSearchLayout().getHeight();
			rightPw=new PopupWindow(vPopupWindow,screenWidth,gridHeight+soHeight);
			rightPw.setFocusable(true);
			rightPw.setOutsideTouchable(true);
			rightPw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); 
			rightPw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); 
			vPopupWindow.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(ValueUtil.isEmpty(event.getX())||event.getX()>0){
						return false;
					}
					if(rightPw!=null&&rightPw.isShowing()){
						if(leftPw==null||!leftPw.isShowing()){
							rightPw.dismiss();
						}
					}
					return false;
				}
			});
			vPopupWindow.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						rightPw.dismiss();
						TsData.isReserve=false;//��ʼ��Ԥ����ʶ
						if(getLeftPw().isShowing()){
							getLeftPw().dismiss();
							leftPw=null;
						}
						rightPw = null;
						return true;
					}
					return false;
				}
			});
		}
		return rightPw;
	}
	/**
	 * Ԥ��̨λ��߲�Ʒ��ʾ�� popupWindow
	 * @return
	 */
	@SuppressWarnings("static-access")
	public PopupWindow getLeftPw() {
		if(leftPw==null){
			int gridHeight=this.gridView.getHeight();
			int soHeight=getSearchLayout().getHeight();
			View vPopupWindow=this.getLayoutInflater().inflate(R.layout.reserve_table_order, null); 
			int screenWidth = (int) (this.screenWidth*0.5);
			leftPw=new PopupWindow(vPopupWindow,screenWidth,gridHeight+soHeight);
		}
		return leftPw;
	}

	/**
	 * ʵ�����ؼ�
	 */
	private void findView() {
		searchBtn = (Button) this.findViewById(R.id.btn_searchView);
		searchBtn.setOnClickListener(this);
		
		searchEdit=(ClearEditText) this.findViewById(R.id.et_searchView);
		searchEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence paramCharSequence, int paramInt1,int paramInt2, int paramInt3) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}
			
			@Override
			public void afterTextChanged(Editable paramEditable) {
				if (CHIN_SNACK==0) {
					updateDisplay(LayTitle!=null&&LayTitle.getTag()!=null?LayTitle.getTag().toString():"", paramEditable.toString());
				} else {
					tableSearch(LayTitle!=null&&LayTitle.getTag()!=null?LayTitle.getTag().toString():"", paramEditable.toString());
				}
			}
		});
		
		gridView = (GridView) this.findViewById(R.id.main_table_gridView);
		if (!ChioceActivity.ispad) {
			gridView.setPadding(15, 10, 15, 1);
			gridView.setVerticalSpacing(10);
		}
		LayTitle = (LinearLayout) this.findViewById(R.id.main_view_title);
		refresh = (Button) findViewById(R.id.mainact_btn_refresh);//ˢ��
		bingtai = (Button) findViewById(R.id.mainact_btn_bingtai);//��̨
		search = (Button) findViewById(R.id.mainact_btn_searchVip);//��Ա��ѯ
		change = (Button) findViewById(R.id.mainact_btn_change);//��̨
		waitTable = (Button) findViewById(R.id.mainact_btn_waittai);//��λ
		checkOrder = (Button) findViewById(R.id.mainact_btn_checkorder);//�˵���ѯ
        openTable =(Button)findViewById(R.id.mainact_btn_kaitai);
		logOut = (Button) findViewById(R.id.mainact_btn_logout);//ע��


        if(CHIN_SNACK==0){
            payOrder = (Button) findViewById(R.id.mainact_btn_order);//��Ҫ�˵���ť
            refresh.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return buttonpx(v, event);
                }
            });
            payOrder.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return buttonpx(v, event);
                }
            });
        }else{
            estempty=(Button)findViewById(R.id.mainact_btn_estempty);//����
            estempty.setOnClickListener(this);
            peopleCount=(Button)findViewById(R.id.mainact_btn_peopleCount);//�޸�����
            peopleCount.setOnClickListener(this);
            if (!ChioceActivity.ispad) {
                refresh.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return buttonpx(v, event);
                    }
                });
			}
        }
        if (SingleMenu.getMenuInstance().getPermissionList()!=null) {
            for (Map<String, String> map : SingleMenu.getMenuInstance().getPermissionList()) {
                if (map.get("CODE").equals("1001") && map.get("ISSHOW").equals("0")) {
                    openTable.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("1002") && map.get("ISSHOW").equals("0")) {
                    change.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("1003") && map.get("ISSHOW").equals("0")) {
                    bingtai.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("1004") && map.get("ISSHOW").equals("0")) {
                    waitTable.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("1005") && map.get("ISSHOW").equals("0")) {
                    search.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("1006") && map.get("ISSHOW").equals("0")) {
                    checkOrder.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("11001") && map.get("ISSHOW").equals("0")) {
                    change.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("11002") && map.get("ISSHOW").equals("0")) {
                    peopleCount.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("11003") && map.get("ISSHOW").equals("0")) {
                    search.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("11004") && map.get("ISSHOW").equals("0")) {
                    checkOrder.setVisibility(View.GONE);
                }
                if (map.get("CODE").equals("11005") && map.get("ISSHOW").equals("0")) {
                    estempty.setVisibility(View.GONE);
                }
            }
        }
        if(!SharedPreferencesUtils.getIsVip(this)){
            search.setVisibility(View.GONE);
        }
		if (openTable!=null){
			openTable.setOnClickListener(this);
		}

		bingtai.setOnClickListener(this);
		search.setOnClickListener(this);
		change.setOnClickListener(this);
		logOut.setOnClickListener(this);
		checkOrder.setOnClickListener(this);
		waitTable.setOnClickListener(this);
		gridView.setSelector(R.color.transparent);
		
	}

    /**
     * ��Ҫ֧���˵�������ť ��ʾ����
     * @param isShow
     */
    public void showPayOrderBut(boolean isShow){
        if(CHIN_SNACK==0) {
            if (isShow) {
                payOrder.setBackgroundResource(R.drawable.should_cheak_mini_red);
            } else {
                payOrder.setBackgroundResource(R.drawable.should_cheak_mini);
            }
        }
    }
	/**
	 * ��̬�������沼��
	 */
	private boolean buttonpx(View v, MotionEvent event) {
        int ea = event.getAction();
        switch (ea) {
            case MotionEvent.ACTION_DOWN:
                lastX= downX = (int) event.getRawX();//��ȡ�����¼�����λ�õ�ԭʼX����
                lastY= downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                int l = v.getLeft() + dx;
                int b = v.getBottom() + dy;
                int r = v.getRight() + dx;
                int t = v.getTop() + dy;
                //�����ж��ƶ��Ƿ񳬳���Ļ
                if (l < 0) {
                    l = 0;
                    r = l + v.getWidth();
                }

                if (t < 0) {
                    t = 0;
                    b = t + v.getHeight();
                }

                if (r > screenWidth) {
                    r = screenWidth;
                    l = r - v.getWidth();
                }

                if (b > screenHeight) {
                    b = screenHeight;
                    t = b - v.getHeight();
                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                v.layout(l, t, r, b);
                v.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs((int) event.getRawX()-downX)<20&&Math.abs((int) event.getRawY()-downY)<20)
                if(v.getId()==R.id.mainact_btn_refresh) {
                    if (LayTitle != null && LayTitle.getTag() != null) {
                        updateDisplay(LayTitle.getTag().toString(), null);
                    }
                }else if(v.getId()==R.id.mainact_btn_order){
                    queryOrder(true);
                }
                break;
        }
        return false;
    }

    /**
     * ��ʾ�˵�����
     */
    public void showOrderList(){
        final OrderPayAdapter adapter=new OrderPayAdapter(this,orderMapList);
        View title=this.getLayoutInflater().inflate(R.layout.order_pay_list_title,null);
        AlertDialog alertDialog=new AlertDialog.Builder(this,R.style.edittext_dialog).setCustomTitle(title).setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> map = (Map<String, String>) adapter.getItem(which);
                Intent intent1 = new Intent(MainActivity.this, SettleAccountsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tableNum", map.get("TABLENUM"));     //̨λ���
                bundle.putString("manCounts", map.get("PEOLENUMMAN"));     // ����
                bundle.putString("womanCounts", map.get("PEOLENUMWOMEN"));    // ����
                bundle.putString("orderId", map.get("ORDERID"));    //�˵���
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        }).show();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = (int) (screenWidth*0.5);
        alertDialog.getWindow().setAttributes(params);

    }

    @Override
	protected void onResume() {
		TsData.isReserve=false;//��ʼ����Ԥ����ʶ
		if(LayTitle!=null&&LayTitle.getTag()!=null){
			updateDisplay(LayTitle.getTag().toString(), null);
		}
		super.onResume();
	}
	private void initData() {
        if(CHIN_SNACK==0) {
            updateClassifyOfSnack();
        }else {
            updateClassifyOfChinese();
        }
	}

    /**
     * ̨λ����
     * @param classid
     * @param tableInit
     */
    public void tableSearch(String classid, final String tableInit){
        //queryTables
        CList<Map<String,String>> list=new CList<Map<String, String>>();
        list.add("user", SharedPreferencesUtils.getUserCode(this));
        list.add("floor", "");
        if (SharedPreferencesUtils.getTableClass(this).equals("1")) {
            list.add("area", classid == null ? "" : classid.trim());//����
            list.add("status", "");//״̬
        } else {
            list.add("area", "");//����
            list.add("status", classid == null ? "" : classid.trim());//״̬
        }
        list.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        list.add("condition",tableInit);
        new ChineseServer().connect(this, ChineseConstants.QUERYTABLES, list, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getLoadingDialog().dismiss();
                //ArrayList<Storetable>
                Map<String, String> map = new ComXmlUtil().xml2Map(result);
                if (map != null) {
                    if ("1".equals(map.get("result"))) {
                        String buffer = map.get("oStr");
                        buffer = buffer.substring(buffer.indexOf("<") + 1, buffer.lastIndexOf(">"));
                        String[] tables = buffer.split("\\|");
                        if (tables == null) {
                            tableLists.clear();
                            ToastUtil.toast(MainActivity.this, R.string.data_error);
                            return;
                        }
                        tableLists = new ArrayList<Storetable>();
                        for (String table : tables) {
                            String[] tableInfo = table.split("\\^");
                            if (tableInfo == null || tableInfo.length < 3) {
                                continue;
                            }
                            Storetable storetable = new Storetable();
                            storetable.setTablenum(tableInfo[1]);
                            storetable.setTblname(tableInfo[2]);
                            storetable.setPerson(tableInfo[3]);
                            storetable.setUsestate(tableInfo[4]);
                            if (tableInfo.length>5){
                                storetable.setOrderId(tableInfo[5]);
                            }
                            if (tableInfo.length>6){
                                storetable.setPriceKay(tableInfo[6]);
                            }
                            tableLists.add(storetable);
                        }
                    } else {
                        tableLists.clear();
                    }
                } else {
                    tableLists.clear();
                }
                showTable();
            }

            @Override
            public void onBeforeRequest() {
                getLoadingDialog().show();
            }
        });
    }

    /**
     * �в� ��ʼ�� ̨λ
     */
    public void updateClassifyOfChinese(){
    	List<Storearear> list ; 
    	if ("3".equals(SharedPreferencesUtils.getTableClass(this))) {
    		list= new ArrayList<Storearear>();
    		for (int i = 1; i < 4; i++) {
    			Storearear storearear = new Storearear();
    			storearear.setArearid(i+"");
    			if (i==1) {
    				storearear.setTblname("����");
				} else if (i==2) {
					storearear.setTblname("��̨δ��");
				} else {
					storearear.setTblname("��ʼ���");
				}
    			list.add(storearear);
			}
		} else {
			String code="AR";
	        if("2".equals(SharedPreferencesUtils.getTableClass(this))){
	            code="LC";
	        }
	        list=new ListProcessor().query("select * from CODEDESC where CODE=\""+code+"\"",null,this,new ListProcessor.Result<List<Storearear>>() {
	            @Override
	            public List<Storearear> handle(Cursor cursor) {
	                List<Storearear> list=new ArrayList<Storearear>();
	                while (cursor.moveToNext()) {
	                    Storearear storearear=new Storearear();
	                    storearear.setArearid(cursor.getString(cursor.getColumnIndex("DES")));
	                    storearear.setTblname(cursor.getString(cursor.getColumnIndex("DES")));
	                    list.add(storearear);
	                }
	                return list;
	            }
	        });
		}
        
        Storearear arear=new Storearear();
        arear.setArearid("");
        arear.setTblname(this.getString(R.string.state_all));
        if(list==null){
            list=new ArrayList<Storearear>();
        }
        list.add(0, arear);
        selArear(list);
    }
	/**
	 * ��� ��ʼ�� ̨λ
	 */
	public void updateClassifyOfSnack(){
		TsData.isReserve=false;//��ʼ����Ԥ����ʶ
		if(SharedPreferencesUtils.getTableClass(this).equals("1")){
			List<Storearear> arears=new ListProcessor().query("select * from storearear_mis", null, this, Storearear.class);
			if(arears==null){
				arears=new ArrayList<Storearear>();
			}
			Storearear arear=new Storearear();
			arear.setArearid("");
			arear.setTblname(this.getString(R.string.state_all));
			arears.add(0, arear);
			selArear(arears);
		}else{//״̬
//			1 ������ ��ɫ  2�� ��̨ ��ɫ 
//			3 ����� ��ɫ 4 ������ ��ɫ 6 ���ѷ�̨ ��ɫ 7 ����̨ ��ɫ 8 ����̨λ δʹ��   9 ���ҵ� ��ɫ 10������ ��ɫ"
			boolean first=true;
			String[] butId=new String[]{this.getString(R.string.state_all)+"_ ",this.getString(R.string.state_leisure)+"_1",this.getString(R.string.state_start)+"_2",this.getString(R.string.state_order)+"_3",this.getString(R.string.state_pay)+"_4",this.getString(R.string.sealed_table)+"_6",this.getString(R.string.state_served)+"_10"};
			for(String name:butId){
				String[] but=name.split("_");
				ToggleButton one=new ToggleButton(this);
				one.setTextOff(but[0]);
				one.setTextOn(but[0]);
				one.setText(but[0]);
				one.setTag(but[1].trim());
				one.setTextColor(Color.WHITE);
				one.setBackgroundResource(R.drawable.bg_main_top_toggle_btn);
				if(first){
					one.setChecked(true);
					one.setBackgroundColor(getResources().getColor(R.color.table_bg_blue));
					one.setBackgroundResource(R.drawable.bg_main_top_toggle_btn);
					LayTitle.setTag(but[1].trim());
					first=false;
				}
				one.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1));
				one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked){
							for(int i=0;i<LayTitle.getChildCount();i++){
								((ToggleButton)LayTitle.getChildAt(i)).setChecked(false);
							}
							buttonView.setChecked(true);
							String tag = buttonView.getTag().toString();
							updateDisplay(tag,null);//���������ʾ̨λ
							LayTitle.setTag(buttonView.getTag());//����ѡ״̬�������Layout��Tag��
							buttonView.setBackgroundResource(R.color.table_bg_blue);
						}else{
							buttonView.setBackgroundResource(R.drawable.bg_main_top_toggle_btn);
						}
					}
				});
				LayTitle.addView(one);
			}
		}
		/**
		 * ��ʼ��̨λ
		 */
//		updateDisplay(LayTitle==null?"":LayTitle.getTag().toString(),null);
	}

	/**
	 * ��ʼ��̨λ ���� ����
	 * @param arears
	 */
	public void selArear(List<Storearear> arears){
		boolean first=true;
		for(Storearear arear:arears){
			ToggleButton one=new ToggleButton(this);
			one.setTextOff(arear.getTblname());
			one.setTextOn(arear.getTblname());
			one.setText(arear.getTblname());
			one.setTag(arear.getArearid());
			one.setTextColor(Color.WHITE);
			one.setBackgroundResource(R.drawable.bg_main_top_toggle_btn);
			if(first){
				one.setChecked(true);
//				one.setBackgroundColor(getResources().getColor(R.color.table_bg_blue));
				one.setBackgroundResource(R.drawable.bg_main_top_toggle_btn);
				LayTitle.setTag(arear.getArearid());
				first=false;
			}
			one.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1));
			one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						for(int i=0;i<LayTitle.getChildCount();i++){
							((ToggleButton)LayTitle.getChildAt(i)).setChecked(false);
						}
						buttonView.setChecked(true);
						String tag = buttonView.getTag().toString();
						updateDisplay(tag,null);//���������ʾ̨λ
						LayTitle.setTag(buttonView.getTag());//����ѡ״̬�������Layout��Tag��
						buttonView.setBackgroundResource(R.color.table_bg_red);
					}else{
						buttonView.setBackgroundResource(R.drawable.bg_main_top_toggle_btn);
					}
				}
			});
			LayTitle.addView(one);
		}
	}
	
	
	/**
	 * �Զ���������
	 * ���State��1.����(Green) 2.δ���(yellow) 3.�ѵ��(RED) 4.�ѽ���(purple) 5.��̨
	 * 6.�ѷ�̨��Blue��7.��̨(��ɫ) 8.��̨λ(backg)  9.����(��ɫ)  10.���� (��ɫ)
	 */
	@Override
	public void setOnTypeViewClick(AdapterView<?> parent,int position, View v,Object object) {
		TsData.isReserve=false;//��ʼ��Ԥ����ʶ
        SingleMenu.getMenuInstance().deleteData();
		final Storetable storeTable = (Storetable) object;
        this.getAllTableByTable(storeTable);

	}
    public void tableClick(final Storetable storeTable){

        if("1".equals(storeTable.getUsestate())){  //ִ�п�̨
            Dialog mDialog;
            //�ж��Ƿ�������Ů���ᵯ����ͬ�ĶԻ���
//			boolean isDisguishGender = SharedPreferencesUtils.getDisguishGender(this);
            mDialog = createStartcDia(storeTable,"1",false);
            if(!mDialog.isShowing()){
                mDialog.show();
            }

            guestLists.clear();
        }else if("2".equals(storeTable.getUsestate())){
            new CombineTableListView(this,storeTable).isCombineTable(new IResult<Map<String,Object>>() {
                @Override
                public void result(Map<String,Object> o) {
                    AlertDialog.Builder builder = new Builder(MainActivity.this);
                    String [] items = {MainActivity.this.getString(R.string.clean_table),MainActivity.this.getString(R.string.vip_card_disButton),MainActivity.this.getString(R.string.cancle)};
                    ListAdapter diaAdapter=new ArrayAdapter<String>(MainActivity.this, R.layout.dia_ifsave_item_layout, R.id.dia_ifsave_item_tv, items);
                    builder.setAdapter(diaAdapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int position) {
                            final String tableNumber = storeTable.getTablenum();
                            switch (position) {
                                case 0:  //��̨
                                    Server cleartbl = new Server();
                                    CList<Map<String, String>> clists = new CList<Map<String,String>>();
                                    clists.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
                                    clists.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
                                    clists.add("tableNum", storeTable.getTablenum());
                                    clists.add("currentState", "2");
                                    clists.add("nextState ", "1");
                                    cleartbl.connect(MainActivity.this, Constants.FastMethodName.ChangeTableState_METHODNAME, Constants.FastWebService.CHANGETtablestate_WSDL, clists, new OnServerResponse() {

                                        @Override
                                        public void onResponse(String result) {
                                            getLoadingDialog().dismiss();
                                            if(!ValueUtil.isEmpty(result)){
                                                Log.e("��̨", result);
                                                String[] str = result.split("@");
                                                String flag = str[0];
                                                if(flag.equals("0")){
                                                    Toast.makeText(MainActivity.this, str[1], Toast.LENGTH_SHORT).show();//�޸�̨λ״̬�ɹ�

                                                    guestLists.clear();//���singleton����
                                                    if(LayTitle!=null&&LayTitle.getTag()!=null){
                                                        updateDisplay(LayTitle.getTag().toString(),null);
                                                    }
                                                    return;
                                                }
                                            }else{
                                                Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onBeforeRequest() {
                                            getLoadingDialog().show();
                                        }
                                    });

                                    //����û�ɫ̨λ����Ӧ��AllCheck���������ݣ���ɾ��
                                    getDataManager(MainActivity.this).deleteDishListsByTblnum(tableNumber);
                                    break;

                                case 1://���
                                    new CombineTableListView(MainActivity.this,storeTable).isCombineTable(new IResult<Map<String,Object>>() {
                                        @Override
                                        public void result(Map<String,Object> map) {
                                            if(map==null) {
                                                checkOrderBytabnum(map, Eatables.class);
                                            }else{
                                                checkOrderBytabnum(tableNumber, Eatables.class);
                                            }
                                            guestLists.clear();//���singleton����
                                        }
                                    });
                                    break;
                                case 2://ȡ��
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                }
            });
        }else if("3".equals(storeTable.getUsestate())||"10".equals(storeTable.getUsestate())){
            new CombineTableListView(this,storeTable).isCombineTable(new IResult<Map<String,Object>>() {
                @Override
                public void result(Map<String,Object> map) {
                    if(map==null) {
                        //ֱ�ӽ���ȫ��
                        String tableNumber = storeTable.getTablenum();
                        checkOrderBytabnum(tableNumber, YiXuanDishActivity2.class);
                    }else{
                        checkOrderBytabnum(map,YiXuanDishActivity2.class);
                    }
                    guestLists.clear();//���singleton����
                }
            });
        }else if("4".equals(storeTable.getUsestate())){//���ˣ���̨

            Server cleartb4 = new Server();
            CList<Map<String, String>> clists = new CList<Map<String,String>>();
            clists.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
            clists.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
            clists.add("tableNum", storeTable.getTablenum());
            clists.add("currentState", "4");
            clists.add("nextState ", "1");
            cleartb4.connect(MainActivity.this, Constants.FastMethodName.ChangeTableState_METHODNAME, Constants.FastWebService.CHANGETtablestate_WSDL, clists, new OnServerResponse() {

                @Override
                public void onResponse(String result) {
                    getLoadingDialog().dismiss();
                    if(!ValueUtil.isEmpty(result)){
                        Log.e("��̨", result);
                        String[] str = result.split("@");
                        String flag = str[0];
                        if(flag.equals("0")){
                            Toast.makeText(MainActivity.this, str[1], Toast.LENGTH_SHORT).show();//�޸�̨λ״̬�ɹ�
                            if(ValueUtil.isNotEmpty(guestLists)){
                                guestLists.clear();//���singleton����
                            }
                            if(LayTitle!=null&&LayTitle.getTag()!=null){
                                updateDisplay(LayTitle.getTag().toString(),null);
                            }
                            return;
                        }
                    }else{
                        Toast.makeText(MainActivity.this,R.string.net_error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onBeforeRequest() {
                    getLoadingDialog().show();
                }
            });
            guestLists.clear();//���singleton����
        }else if("6".equals(storeTable.getUsestate())){
            Toast.makeText(MainActivity.this, R.string.sealed_table, Toast.LENGTH_SHORT).show();
            guestLists.clear();//���singleton����
        }
    }
    public void getAllTableByTable(final Storetable storetable){
        CList list=new CList();
        list.add("deviceId",SharedPreferencesUtils.getDeviceId(MainActivity.this));
        list.add("userCode",SharedPreferencesUtils.getUserCode(MainActivity.this));
        list.add("strsql","select TABLENUM,TBLNAME,USESTATE from storetables_mis where (TBLPID = '" +storetable.getTablenum()+ "' or tablenum='"+storetable.getTablenum()+"')");
        list.add("parityBit",getParityBit());
        new Server().connect(this, "querySqlInterface", "ChoiceWebService/services/HHTSocket?/querySqlInterface", list, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                if(ValueUtil.isNotEmpty(result)){
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        String res= jsonObject.getString("return");
                        if (res.equals("0")){

                            String count=jsonObject.getString("count");
                            if(ValueUtil.isNaNofInteger(count)>1){
                                JSONArray array=jsonObject.getJSONArray("data");
                                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                                //ͨ��infalte��ȡpopupWindow����
                                View popupWindow = layoutInflater.inflate(R.layout.custom_popup_window, null);
                                //����������
                                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this,R.style.edittext_dialog).setView(popupWindow).show();
                                LinearLayout linearLayout=(LinearLayout)popupWindow.findViewById(R.id.popupWind);
                                linearLayout.setPadding(15, 25, 15, 25);
                                DisplayMetrics dmm = new DisplayMetrics();
                                dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dmm);
//                                int dialogWidth  =  dmm.widthPixels;
                                LinearLayout dLin=new LinearLayout(MainActivity.this);
                                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
                                LinearLayout.LayoutParams linearlayoutlp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                                dLin.setLayoutParams(linearlayoutlp);
                                params.setMargins(3, 3, 1, 3);
                                /**
                                 * ���ݲ�ѯ������ѭ������but
                                 */

                                for(int i=0;i<array.length();i++){
                                    JSONObject jo=array.getJSONObject(i);
//                                    Map<String,String> map=new HashMap<String, String>();
                                    final Storetable table=new Storetable();
                                    table.setTablenum(jo.getString("TABLENUM"));
                                    table.setTblname(jo.getString("TBLNAME"));
                                    table.setUsestate(jo.getString("USESTATE"));

                                    Button but=new Button(MainActivity.this);
                                    but.setTextColor(Color.WHITE);
                                    but.setText(table.getTblname());
                                    but.setTag(table);
                                    but.setTextSize(14);
                                    String tableStatus=table.getUsestate();
                                    if ("1".equals(tableStatus)) {
                                        but.setBackgroundResource(R.drawable.phone_taiwei_buton_green);
                                    } else if ("2".equals(tableStatus)) {
                                        but.setBackgroundResource(R.drawable.phone_taiwei_buton_yellow);
                                    } else if ("3".equals(tableStatus)) {
                                        but.setBackgroundResource(R.drawable.phone_taiwei_buton_red);
                                    } else if ("4".equals(tableStatus)) {
                                        but.setBackgroundResource(R.drawable.phone_taiwei_buton_purple);
                                    } else if ("6".equals(tableStatus)) {
                                        but.setBackgroundResource(R.drawable.phone_taiwei_buton_blue);
                                    } else if ("10".equals(tableStatus)) {
                                        but.setBackgroundResource(R.drawable.phone_taiwei_buton_gray);
                                    }
//                                    but.setBackgroundResource(R.drawable.blue_button_background);
                                    but.setOnClickListener(new OnClickListener() {//Ϊbut�����¼�
                                        @Override
                                        public void onClick(View paramView) {
                                            tableClick(table);
                                            dialog.dismiss();
                                        }
                                    });
                                    dLin.addView(but, params);
                                    if(dLin.getChildCount()==4){
                                        linearLayout.addView(dLin);
                                        dLin=new LinearLayout(MainActivity.this);
                                    }
                                }
                                if(dLin.getChildCount()>0){
                                    linearLayout.addView(dLin);
                                }
                            }else{
                                tableClick(storetable);
                            }
                        }else{
                            ToastUtil.toast(MainActivity.this,jsonObject.getString("error"));
                            showPayOrderBut(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onBeforeRequest() {
            }
        });
    }

	@Override
	public void setOnTypeViewClick(String classId) {}

	
	//----------------------------------------------------
	@Override
	public void onClick(View view) {
		switch (view.getId()) {		case R.id.btn_searchView:
			searchBtn.setVisibility(View.GONE);
			searchEdit.setVisibility(View.VISIBLE);
			searchEdit.requestFocus();
			//��������
			InputMethodManager inputManager = (InputMethodManager)searchEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.showSoftInput(searchEdit, 0); 
			break;
		case R.id.mainact_btn_bingtai://��̨
			final LinearLayout btai=(LinearLayout)this.getLayoutInflater().inflate(R.layout.tai_manage, null);
			final EditText thisTai=(EditText)btai.findViewById(R.id.tai_thisTaiEdit);
			final EditText tagTai=(EditText)btai.findViewById(R.id.tai_tagTaiEdit);
			final AlertDialog alert=new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.bingtai).setView(btai)
					.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					final String thisTai=((EditText)btai.findViewById(R.id.tai_thisTaiEdit)).getText().toString();
					final String tagTai=((EditText)btai.findViewById(R.id.tai_tagTaiEdit)).getText().toString();
					if(ValueUtil.isNotEmpty(thisTai)&&ValueUtil.isNotEmpty(tagTai)){
						combineTable(thisTai,tagTai,(AlertDialog)dialog);
					}else{
						Toast.makeText(MainActivity.this, R.string.table_is_not_null, Toast.LENGTH_LONG).show();
					}
				}
			}).setPositiveButton(R.string.cancle, null).show();
			AlertDialogTitleUtil.gravity(alert, Gravity.CENTER);
			tagTai.setOnKeyListener(new OnKeyListener() {  
	            @Override  
	            public boolean onKey(View v, int keyCode, KeyEvent event) {  
	                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {  
	                	combineTable(thisTai.getText().toString(),tagTai.getText().toString(),alert);
	                    return true;  
	                }  
	                return false;  
	            }  
	        }); 
			break;
			
		case R.id.mainact_btn_searchVip://��ѯ��Ա
			Intent intent=new Intent(this,QueryVipCardActivity.class);
			startActivity(intent);
			break;
		case R.id.mainact_btn_logout://ע��
            Logout();
			break;
		case R.id.mainact_btn_change://��̨
			final LinearLayout htai=(LinearLayout)this.getLayoutInflater().inflate(R.layout.tai_manage, null);
			final EditText changeThisTai=(EditText)htai.findViewById(R.id.tai_thisTaiEdit);
			final EditText changeTagTai=(EditText)htai.findViewById(R.id.tai_tagTaiEdit);
            if(CHIN_SNACK==1){
                changeTagTai.setInputType(InputType.TYPE_CLASS_TEXT);
                changeThisTai.setInputType(InputType.TYPE_CLASS_TEXT);
            }
			final AlertDialog alertH=new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.change_tai).setView(htai)
					.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
                    if(CHIN_SNACK==1){
                        chinsesChangeTable(changeThisTai.getText().toString(), changeTagTai.getText().toString(), (AlertDialog) dialog);
                    }else {
                        changeTable(changeThisTai.getText().toString(), changeTagTai.getText().toString(), (AlertDialog) dialog);
                    }
				}
			}).setPositiveButton(R.string.cancle, null).show();
			AlertDialogTitleUtil.gravity(alertH, Gravity.CENTER);
			changeTagTai.setOnKeyListener(new OnKeyListener() {  
	            @Override  
	            public boolean onKey(View v, int keyCode, KeyEvent event) {  
	                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if(CHIN_SNACK==1){
                            chinsesChangeTable(changeThisTai.getText().toString(),changeTagTai.getText().toString(),alertH);
                        }else {
                            changeTable(changeThisTai.getText().toString(), changeTagTai.getText().toString(), alertH);
                        }
	                    return true;  
	                }  
	                return false;  
	            }  
	        });  
			break;
			
		case R.id.mainact_btn_waittai://��λ��̨
            waittai();
			break;
		case R.id.mainact_btn_checkorder://TODO ��ѯ�˵�
			RelativeLayout linear=(RelativeLayout)this.getLayoutInflater().inflate(R.layout.dia_searchtai, null);
			final EditText edit=(EditText)linear.findViewById(R.id.dia_et_searchtai);
			edit.setMaxLines(1);
			edit.setHint(R.string.please_input_table);
            if(CHIN_SNACK==1){
                edit.setHint(R.string.please_input_table_abbr);
                edit.setInputType(InputType.TYPE_CLASS_TEXT);
            }
			edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});//���������ĳ���
			final AlertDialog alertQ=new AlertDialog.Builder(MainActivity.this,R.style.edittext_dialog).setTitle(R.string.query_table)
					.setView(linear).setPositiveButton(R.string.PopWin_phoneNumBut, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							findOrder(edit,(AlertDialog)paramDialogInterface);
						}
					}).setNegativeButton(R.string.cancle, null).show();
			edit.setOnKeyListener(new OnKeyListener() {  
				@Override  
				public boolean onKey(View v, int keyCode, KeyEvent event) {  
					if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {  
						findOrder(edit,alertQ);
						return true;  
					}  
					return false;  
				}  
			});  
			break;
        case R.id.mainact_btn_peopleCount://�޸�����
            LinearLayout layout=(LinearLayout)this.getLayoutInflater().inflate(R.layout.people_count_revamp,null);
            final EditText table=(EditText)layout.findViewById(R.id.people_count_table);
            final EditText peopleNum=(EditText)layout.findViewById(R.id.people_count_peopleNum);
            new Builder(this).setTitle(R.string.people_revamp).setView(layout).setPositiveButton(R.string.cancle, null)
                    .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CList list = new CList();
                            list.add("pdaid", SharedPreferencesUtils.getDeviceId(MainActivity.this));
                            list.add("user", SharedPreferencesUtils.getUserCode(MainActivity.this));
                            list.add("tblInit", table.getText().toString().toUpperCase());
                            new ChineseServer().connect(MainActivity.this, ChineseConstants.GETFOLIONO, list, new OnServerResponse() {
                                @Override
                                public void onResponse(String result) {
                                    getLoadingDialog().dismiss();
                                    Map<String, String> map = new ComXmlUtil().xml2Map(result);
                                    if (ValueUtil.isEmpty(map)) {
                                        ToastUtil.toast(MainActivity.this, R.string.get_date_error);
                                        return;
                                    }
                                    if ("1".equals(map.get("result"))) {
                                        String oStr = ChineseResultAlt.oStrAlt(map.get("oStr"));
                                        peopleRevamp(oStr.split("@")[0], peopleNum.getText().toString());
                                    } else {
                                    	 String oStr = ChineseResultAlt.oStrAlt(map.get("oStr"));
                                        ToastUtil.toast(MainActivity.this,oStr);
                                    }
                                }

                                @Override
                                public void onBeforeRequest() {
                                    getLoadingDialog().show();
                                }
                            });
                        }
                    }).show();
            break;
        case R.id.mainact_btn_estempty:
            startActivity(new Intent(this,ChineseGuQingActivity.class));
            break;
            case R.id.mainact_btn_kaitai:// ��̨
                Dialog mDialog;
                Storetable storeTable = new Storetable();
                mDialog = createStartcDia(storeTable, "1", true);
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                break;
		default:
			break;
		}
	}

    private void waittai() {
        String waitIp=SharedPreferencesUtils.getWaitUrl(this);
        if(ValueUtil.isEmpty(waitIp)||waitIp.trim().equals("0.0.0.0")||waitIp.trim().equals("127.0.0.1")) {
            //��͵�λ����
            Button addBut = (Button) getRightPw().getContentView().findViewById(R.id.yuding_add);//���Ӱ�ť
            Button subBut = (Button) getRightPw().getContentView().findViewById(R.id.yuding_sub);//ȡ����ť
            Button refresh = (Button) getRightPw().getContentView().findViewById(R.id.yuding_refresh);//ȡ����ť
            addBut.setOnClickListener(getReserveTableEvent());//��λ���Ӱ�ť�¼�
            subBut.setOnClickListener(getReserveTableEvent());//��λȡ����ť�¼�
            refresh.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPw();
                }
            });
            //----------------------------------------------------------
            showPw();
            int soY = ((Float) getSearchLayout().getY()).intValue();
            getRightPw().showAtLocation(gridView, Gravity.NO_GRAVITY, ((Double) (screenWidth * 0.5)).intValue(), soY);
        }else {
            startActivity(new Intent(MainActivity.this, WaitMain.class));
        }
    }

    /**
     * ע������
     */
    public void Logout(){
        if(CHIN_SNACK==0) {
            CList<Map<String, String>> data = new CList<Map<String, String>>();
            data.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
            data.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
            new Server().connect(this, "loginOut", "ChoiceWebService/services/HHTSocket?/loginOut", data, new OnServerResponse() {
				@Override
				public void onResponse(String result) {
                    getLoadingDialog().dismiss();
					if (ValueUtil.isNotEmpty(result)) {
						String[] res = result.split("@");
						if (ValueUtil.isNotEmpty(res) && res[0].equals("0")) {
							showToast(R.string.cancel_success);
							Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
							MainActivity.this.startActivity(intent);
							MainActivity.this.finish();
						} else {
							showToast(R.string.cancel_unsuccess);
						}
					} else {
						showToast(R.string.net_error);
					}
				}

				@Override
				public void onBeforeRequest() {
                    getLoadingDialog().show();
				}
			});
        }else{
            startActivity(new Intent(this, ChineseWelcomeActivity.class));
            finish();
        }
    }
	/**
	 * ��ѯ�˵�
	 * @param edit
	 */
	protected void findOrder(EditText edit,AlertDialog alert){
		if(ValueUtil.isEmpty(edit.getText().toString())){
			Toast.makeText(MainActivity.this, R.string.table_is_not_null, Toast.LENGTH_LONG).show();
			return;
		}
		alert.dismiss();
        if(CHIN_SNACK==0) {
            //�����ȡ���ݳɹ�����ת
			Intent intent = new Intent(MainActivity.this, Find_orderByTaiNum.class);
			intent.putExtra("table", edit.getText().toString());
			intent.putExtra("direction", "MainDirection");
            startActivity(intent);
        }else{
            queryOrder(edit.getText().toString());
        }
	}
	/**
	 * ��̨
	 * @param thisTai
	 * @param tagTai
	 */
	public void changeTable(final String thisTai, final String tagTai,final AlertDialog alert){
		if(ValueUtil.isNotEmpty(thisTai)&&ValueUtil.isNotEmpty(tagTai)){
			CList<Map<String,String>> data=new CList<Map<String,String>>();
			data.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
			data.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
			data.add("tablenumSource", thisTai);
			data.add("tablenumDest", tagTai);
			new Server().connect(MainActivity.this, "changeTable", "ChoiceWebService/services/HHTSocket?/changeTable", data, new OnServerResponse() {
				@Override
				public void onResponse(String result) {
                    getLoadingDialog().dismiss();
					alert.dismiss();
					String[] res = result == null ? null : result.split("@");
                    checkOrderBytabnum(thisTai,null);
                    List<Food> foodArray = new ValueSetFile<Food>(thisTai+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")+SingleMenu.getMenuInstance().getOrderNum() + ".mc").read();
                    new ValueSetFile<Food>(thisTai+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")+SingleMenu.getMenuInstance().getOrderNum() + ".mc").del();
                    new ValueSetFile<Food>(tagTai + DateFormat.getStringByDate(new Date(), "yyyy-MM-dd") +SingleMenu.getMenuInstance().getOrderNum()+ ".mc").write(foodArray);
					if (ValueUtil.isNotEmpty(res) && res.length > 1) {
						Toast.makeText(MainActivity.this, res[1], Toast.LENGTH_LONG).show();
						if (LayTitle != null && LayTitle.getTag() != null) {//��̨�ɹ�ˢ��̨λ
							updateDisplay(LayTitle.getTag().toString(), null);
						}
					} else {
						Toast.makeText(MainActivity.this, R.string.huantai_error, Toast.LENGTH_LONG).show();
					}
				}

				@Override
				public void onBeforeRequest() {
                    getLoadingDialog().show();
				}
			});
		}else{
			Toast.makeText(MainActivity.this, R.string.table_is_not_null, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * ��̨
	 */
	public void combineTable(final String thisTai,final String tagTai,final AlertDialog alert){
		if(ValueUtil.isNotEmpty(thisTai)&&ValueUtil.isNotEmpty(tagTai)){
			CList<Map<String, String>> data=new CList<Map<String,String>>();
			data.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
			data.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
			data.add("tableList", thisTai+"@"+tagTai);
			new Server().connect(MainActivity.this, "combineTable", "ChoiceWebService/services/HHTSocket?/combineTable", data, new OnServerResponse() {
				@Override
				public void onResponse(String result) {
                    getLoadingDialog().dismiss();
					alert.dismiss();
					String[] res=result==null?null:result.split("@");
					if(ValueUtil.isNotEmpty(res)){
						if(res[0].equals("0")){
							Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.bingtai_success)+":"+res[1],Toast.LENGTH_LONG).show();
							
							//��̨�ɹ��󣬽����ݿ�������̨λ��Ӧ�������е��ֶ��˵���orderId�޸�Ϊ�µ��˵���
							getDataManager(MainActivity.this).updateOrderIdAfterBingtaiSuccess(thisTai, tagTai, res[1]);
							if(LayTitle!=null&&LayTitle.getTag()!=null){
								updateDisplay(LayTitle.getTag().toString(),null);
							}
						}else{
							Toast.makeText(MainActivity.this, res[1],Toast.LENGTH_LONG).show();
						}
					}else{
						Toast.makeText(MainActivity.this,R.string.bingtai_unsuccess,Toast.LENGTH_LONG).show();
					}
				}
				@Override
				public void onBeforeRequest() {
                    getLoadingDialog().show();
				}
			});
		}else{
			Toast.makeText(MainActivity.this, R.string.table_is_not_null, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * ̨λˢ��
	 */
	public void refreshDisplay() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
//	/**
//	 * ������̨�Ĳ�������Ů�ĶԻ���
//	 * @param storeTable
//	 * @return
//	 */
//	public Dialog createStartcDiaNotDisguish(final Storetable storeTable, final String ktkind){
//		View view = LayoutInflater.from(this).inflate(R.layout.start_tai_notdisguishwm, null);
//		TextView startTableTitle=(TextView)view.findViewById(R.id.start_table_title);
//		if("6".equals(ktkind)){
//			startTableTitle.setText(R.string.start_seed_table);
//		}
//		final ClearEditText clearEdit = (ClearEditText) view.findViewById(R.id.start_peopleNumEdit_notdisguish);
//		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.start_isVip_notdisguish);
//		if(!SharedPreferencesUtils.getIsVip(this)){
//            checkBox.setVisibility(View.GONE);
//        }
//		Button ensureButton = (Button)view.findViewById(R.id.dia_btn_notdisguish_peoplesumcertainbtn);
//		Button cancleButton = (Button)view.findViewById(R.id.dia_btn_notdisguish_peoplesumcancelbtn);
//		AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this,R.style.edittext_dialog);
//		builder.setView(view);
//		final AlertDialog dialog = builder.create();
//		clearEdit.setOnKeyListener(new OnKeyListener() {//�����س��¼�
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (KeyEvent.KEYCODE_ENTER == keyCode&& event.getAction() == KeyEvent.ACTION_DOWN) {
//					startTableNotDisguish(clearEdit,checkBox,storeTable,dialog,ktkind);
//					return true;
//				}
//				return false;
//			}
//		});
//
//		//ȷ�Ͽ�̨�¼�
//		ensureButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startTableNotDisguish(clearEdit,checkBox,storeTable,dialog,ktkind);
//			}
//		});
//		cancleButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//				im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//		});
//		return dialog;
//	}
//
//
//	/**
//	 * ��̨�¼�,��������Ů
//	 * @param clearEdit
//	 * @param checkBox
//	 * @param storeTable
//	 * @param dialog
//	 */
//	protected void startTableNotDisguish(ClearEditText clearEdit,CheckBox checkBox, final Storetable storeTable, final AlertDialog dialog,String ktkind) {
//		if(ValueUtil.isEmpty(clearEdit.getText().toString().trim()) ){
//			Toast.makeText(MainActivity.this, R.string.people_not_null, Toast.LENGTH_SHORT).show();
//		}else{
//			TsData.isReserve=false;//��ʼ��Ԥ����ʶ
//			if(checkBox.isChecked()){
//				Intent intent=new Intent(MainActivity.this,VipFindActivity.class);
//				intent.putExtra("mark", "startTable");
//				intent.putExtra("man", clearEdit.getText().toString());
//				intent.putExtra("woman", "");
//				intent.putExtra("tableNum", storeTable.getTablenum());
//				startActivity(intent);
//			}else{
//				CList<Map<String, String>> clist = new CList<Map<String,String>>();
//				clist.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
//				clist.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
//				clist.add("tableNum", storeTable.getTablenum());
//				//��������Ů
//				clist.add("manCounts", clearEdit.getText().toString().trim());
//				clist.add("womanCounts", "");
//				clist.add("ktKind", ktkind);
//				clist.add("openTablemwyn", "0");
//
//				new Server().connect(MainActivity.this, Constants.FastMethodName.STARTTABLE_METHODNAME, Constants.FastWebService.STARTTABLE_WSDL, clist, new OnServerResponse() {
//
//					@Override
//					public void onBeforeRequest() {
//                        getLoadingDialog().show();
//					}
//
//					@Override
//					public void onResponse(String startresult) {
//						dialog.dismiss();
//                        getLoadingDialog().dismiss();
//
//						if(ValueUtil.isNotEmpty(startresult)){
//							String[] str = startresult.split("@");
//							if(str[0].equals("0")){
//								/**
//								 * ������¿�̨ ��ո�̨�»�Ա��¼��Ϣ
//								 */
//								new VipRecordUtil().delHandle(MainActivity.this, storeTable.getTablenum());
//								//��ע���������ǲ�������Ů�������ӿ�û�з�����������Ů����,��Ϊ""
//								singleMenu.setManCounts("");
//								singleMenu.setWomanCounts("");
//								singleMenu.setTableNum(storeTable.getTablenum());
//								singleMenu.setUserCode(SharedPreferencesUtils.getUserCode(MainActivity.this));
//								singleMenu.setMenuOrder(str[1]);
//								storeTable.setUsestate("2");
//								Intent intent = new Intent(MainActivity.this, Eatables.class);
//								intent.putExtra("direction", "MainDirection");
//								startActivity(intent);
//							}else{
//								Toast.makeText(MainActivity.this,str[1] , Toast.LENGTH_SHORT).show();
//							}
//						}else{
//							Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
//						}
//					}
//				});
//			}
//			dialog.dismiss();
//		}
//	}
    /*
    * ������̨��������Ů�ĶԻ���
    *
            * @param storeTable
    * @return
            */

    public Dialog createStartcDia(final Storetable storeTable,
                                  final String ktkind, final Boolean tableTag) {
        LinearLayout linear = (LinearLayout) MainActivity.this
                .getLayoutInflater().inflate(R.layout.activity_start_tai, null);
        TextView startTableTitle = (TextView) linear
                .findViewById(R.id.start_tabletitle);

        if ("6".equals(ktkind)) {
            startTableTitle.setText(R.string.start_seed_table);

        }
        TextView man_text = (TextView) linear.findViewById(R.id.man_text);// ������
        TextView woman_text = (TextView) linear.findViewById(R.id.woman_text);// Ůʿ��
        TextView taihao_text = (TextView) linear.findViewById(R.id.taihao);// ̨�ţ�
        man_text.setOnClickListener(this);
        woman_text.setOnClickListener(this);
        taihao_text.setOnClickListener(this);// ̨�ţ�
        SingleMenu.getMenuInstance().setOrderNum(null);
        final ClearEditText clearEdittai = (ClearEditText) linear
                .findViewById(R.id.start_taiEdit);// Ůʿ
        clearEdittai.setOnClickListener(this);
        final ClearEditText man = (ClearEditText) linear
                .findViewById(R.id.start_manEdit);// ����
        final ClearEditText woman = (ClearEditText) linear
                .findViewById(R.id.start_womanEdit);// Ů��
        final CheckBox box = (CheckBox) linear.findViewById(R.id.check_isVip);// �Ƿ��Ա

        if (!SharedPreferencesUtils.getIsVip(this)) {
            box.setVisibility(View.GONE);
        }
        if (tableTag == false) {
            // ��̨�� ���������
            taihao_text.setVisibility(View.GONE);
            clearEdittai.setVisibility(View.GONE);
        }
        boolean isDisguishGender = SharedPreferencesUtils
                .getDisguishGender(this);
        if (!isDisguishGender) {
            man_text.setText("����");
			man.setHint("����������");
            woman.setVisibility(View.GONE);
            woman_text.setVisibility(View.GONE);
            // mDialog = createStartcDia(storeTable, "1", false);
        }
        Button ensureButton = (Button) linear.findViewById(R.id.ok_button);
        Button cancleButton = (Button) linear.findViewById(R.id.no_button);
        Button tidanbutton = (Button) linear.findViewById(R.id.tidan_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this, R.style.edittext_dialog);
        builder.setView(linear);
        final AlertDialog dialog = builder.create();
        man.setOnKeyListener(new OnKeyListener() {// ���������س��¼�
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    woman.requestFocus();// �ѹ�긳��Ů�������
                    return true;
                }
                return false;
            }
        });
        woman.setOnKeyListener(new OnKeyListener() {// Ů�������س��¼�
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    startTable(man, woman, box, storeTable, dialog, ktkind);// ��̨
                    return true;
                }
                return false;
            }
        });
        // ȷ�Ͽ�̨�¼�
        ensureButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tableTag) {
                    storeTable.setTablenum(clearEdittai.getText().toString());
                }
                if (SingleMenu.getMenuInstance().getOrderNum() == null)
                    startTable(man, woman, box, storeTable, dialog, ktkind);
                else {
                    orderByAuthCode(man, woman, storeTable, dialog);
                }
            }
        });
        cancleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus()
                                .getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        tidanbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });
        return dialog;
    }
//	/**
//	 * ������̨��������Ů�ĶԻ���
//	 * @param storeTable
//	 * @return
//	 */
//	public Dialog createStartcDia(final Storetable storeTable, final String ktkind){
//		LinearLayout linear=(LinearLayout)MainActivity.this.getLayoutInflater().inflate(R.layout.start_tai_disguishwm, null);
//		TextView startTableTitle=(TextView)linear.findViewById(R.id.start_table_title);
//		if("6".equals(ktkind)){
//			startTableTitle.setText(R.string.start_seed_table);
//		}
//        singleMenu.setOrderNum(null);
//		final ClearEditText man=(ClearEditText)linear.findViewById(R.id.start_manNumEdit);//����
//		final ClearEditText woman=(ClearEditText)linear.findViewById(R.id.start_womanNumEdit);//Ů��
//		final CheckBox box=(CheckBox)linear.findViewById(R.id.start_isVip);//�Ƿ��Ա
//        if(!SharedPreferencesUtils.getIsVip(this)){
//            box.setVisibility(View.GONE);
//        }
//		Button ensureButton = (Button)linear.findViewById(R.id.dia_btn_peoplesumcertainbtn);
//		Button cancleButton = (Button)linear.findViewById(R.id.dia_btn_peoplesumcancelbtn);
//        Button tidanbutton  = (Button)linear.findViewById(R.id.tidan);
//
//		AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this,R.style.edittext_dialog);
//		builder.setView(linear);
//		final AlertDialog dialog = builder.create();
//		man.setOnKeyListener(new OnKeyListener() {//���������س��¼�
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (KeyEvent.KEYCODE_ENTER == keyCode&& event.getAction() == KeyEvent.ACTION_DOWN) {
//					woman.requestFocus();//�ѹ�긳��Ů�������
//					return true;
//				}
//				return false;
//			}
//		});
//		woman.setOnKeyListener(new OnKeyListener() {//Ů�������س��¼�
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (KeyEvent.KEYCODE_ENTER == keyCode&& event.getAction() == KeyEvent.ACTION_DOWN) {
//					startTable(man,woman,box,storeTable,dialog,ktkind);//��̨
//					return true;
//				}
//				return false;
//			}
//		});
//		//ȷ�Ͽ�̨�¼�
//		ensureButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//                if (singleMenu.getOrderNum() == null)
//				    startTable(man,woman,box,storeTable,dialog,ktkind);
//                else
//                {
//                    orderByAuthCode(man,woman,storeTable,dialog);
//                }
//			}
//		});
//		cancleButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//				im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//		});
//        tidanbutton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, ScanActivity.class);
//                startActivity(intent);
//            }
//        });
//
//		return dialog;
//	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            tidanbianhao.setText(bundle.getString("result"));
            orderNum=bundle.getString("result");
        }
    }

    /**
     * �ᵥ��̨
     * @param man
     * @param woman
     * @param storeTable
     * @param dialog
     */
	private void orderByAuthCode(final EditText man,final EditText woman,final Storetable storeTable,final AlertDialog dialog){
        if(ValueUtil.isEmpty(man.getText().toString().trim()) && ValueUtil.isEmpty(woman.getText().toString().trim())){
            Toast.makeText(MainActivity.this, R.string.people_not_null, Toast.LENGTH_SHORT).show();
            return;
        }else{
            TsData.isReserve=false;
            CList<Map<String, String>> clist = new CList<Map<String,String>>();
            clist.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
            clist.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
            Map<String,String> map=new HashMap<String, String>();

            map.put("tablenum",storeTable.getTablenum());
            map.put("manPeolenum", man.getText().toString().trim());
            map.put("womenPeolenum", woman.getText().toString().trim());
            map.put("auth_code",SingleMenu.getMenuInstance().getOrderNum());
            try {
                clist.add("json", JsonUtil.getJson(map));//user pass setting cnt itcode
            } catch (IOException e) {
                CSLog.e("orderByAuthCodeExpListAdapter", e.getMessage());
            }
            Server server1 = new Server();
            server1.connect(MainActivity.this, Constants.FastMethodName.GETORDER_BY_AUTHCODE, Constants.FastWebService.STARTTABLE_WSDL, clist, new OnServerResponse() {
                @Override
                public void onBeforeRequest() {
                    getLoadingDialog().show();
                }
                @Override
                public void onResponse(String startresult) {
                    dialog.dismiss();
                    getLoadingDialog().dismiss();
                    if (ValueUtil.isNotEmpty(startresult)) {
                        try {
//                            String startresult="{\"return\":\"0\",\"remsg\":{\"remark\":\"\",\"addr\":\"\",\"type\":\"18\",\"paymoney\":\"\",\"realmoney\":\"\",\"listDishProdAdd\":[],\"openid\":\"\",\"longitude\":\"\",\"vcode\":\"811999\",\"receivable\":\"\",\"sft\":\"\",\"dat\":\"2015-10-31\",\"stws\":\"2\",\"roompax\":\"\",\"cardzamt\":\"\",\"receiveraddr\":\"\",\"resv\":\"15103112365998\",\"orderTimes\":\"2015-10-31\",\"listNetOrderDtl\":[{\"boxprice\":\"\",\"unitcode\":\"101\",\"listDishTcItem\":[],\"prodReqAddFlag\":\"N\",\"ordersid\":\"8c4953021ee64e1e96898fd577dc45cf\",\"ispackage\":\"0\",\"listDishAddItem\":[],\"totalprice\":\"12.00\",\"remark\":\"\",\"boxnum\":\"\",\"pcode\":\"1101\",\"reqredefine\":\"\",\"id\":\"a1a90e8bea3349ef8390113d0796908f\",\"unit\":\"��\",\"foodnum\":\"1\",\"price\":\"12.00\",\"orderPackageDetailList\":[],\"tcseq\":0,\"foodsid\":\"FE83AB87E6964BA7AA4E\",\"listDishProdAdd\":[],\"grptyp\":\"1\",\"foodzcnt\":\"0\",\"foodsname\":\"������������\"},{\"boxprice\":\"\",\"unitcode\":\"101\",\"listDishTcItem\":[],\"prodReqAddFlag\":\"N\",\"ordersid\":\"8c4953021ee64e1e96898fd577dc45cf\",\"ispackage\":\"0\",\"listDishAddItem\":[],\"totalprice\":\"8.00\",\"remark\":\"\",\"boxnum\":\"\",\"pcode\":\"1105\",\"reqredefine\":\"\",\"id\":\"48bfeeb33b8a4469bb574cb01aab266c\",\"unit\":\"��\",\"foodnum\":\"1\",\"price\":\"8.00\",\"orderPackageDetailList\":[],\"tcseq\":0,\"foodsid\":\"C56075D0AFA94A928BE3\",\"listDishProdAdd\":[],\"grptyp\":\"1\",\"foodzcnt\":\"0\",\"foodsname\":\"��˰������\"},{\"boxprice\":\"\",\"unitcode\":\"101\",\"listDishTcItem\":[{\"nzcnt\":\"0\",\"unit\":\"��\",\"unitcode\":\"101\",\"tcprice\":\"-36.83\",\"tcremark\":null,\"pk_pubitem\":\"FE83AB87E6964BA7AA4E\",\"tcfoodnum\":\"1\",\"tcpname\":\"������������\",\"tcpcode\":\"1101\",\"tctotalprice\":\"-36.83\",\"pk_orderpackagedetail\":\"6092b9f4b8544ce39eedbc848b57fdf5\"},{\"nzcnt\":\"0\",\"unit\":\"��\",\"unitcode\":\"101\",\"tcprice\":\"17.05\",\"tcremark\":null,\"pk_pubitem\":\"97E5DDC9FBD04B72B5A3\",\"tcfoodnum\":\"1\",\"tcpname\":\"����\",\"tcpcode\":\"8104\",\"tctotalprice\":\"17.05\",\"pk_orderpackagedetail\":\"338253eed2794aa8842b87b1e88d66c7\"},{\"nzcnt\":\"0\",\"unit\":\"��\",\"unitcode\":\"101\",\"tcprice\":\"39.78\",\"tcremark\":null,\"pk_pubitem\":\"890E3E309CFD4A87A44D\",\"tcfoodnum\":\"1\",\"tcpname\":\"����\",\"tcpcode\":\"7101\",\"tctotalprice\":\"39.78\",\"pk_orderpackagedetail\":\"bdaa5e129fa54e45b6dbf80e7457c155\"}],\"prodReqAddFlag\":\"\",\"ordersid\":\"8c4953021ee64e1e96898fd577dc45cf\",\"ispackage\":\"1\",\"listDishAddItem\":[],\"totalprice\":\"20.00\",\"remark\":\"\",\"boxnum\":\"\",\"pcode\":\"201002\",\"reqredefine\":\"\",\"id\":\"1a6f630bf1fe472ba96a6b950405e38c\",\"unit\":\"��\",\"foodnum\":\"1\",\"price\":\"20.00\",\"orderPackageDetailList\":[],\"tcseq\":0,\"foodsid\":\"029873A6BC934D66A9E3\",\"listDishProdAdd\":[],\"grptyp\":\"\",\"foodzcnt\":\"0\",\"foodsname\":\"�����ײ�\"}],\"pk_group\":\"\",\"shipperphone\":\"\",\"latitude\":\"\",\"tele\":\"\",\"listDishAddItem\":[],\"state\":\"1\",\"contact\":\"\",\"bookDeskOrderID\":\"\",\"id\":\"8c4953021ee64e1e96898fd577dc45cf\",\"rannum\":\"15103112365998580869\",\"printstate\":\"\",\"sumprice\":\"40.0\",\"shippingfee\":\"\",\"pax\":\"0\",\"name\":\"\",\"money\":\"\",\"isfeast\":\"\",\"payment\":\"\",\"deliverytime\":\"\",\"pubitem\":\"\",\"vinvoicetitle\":\"\",\"cardamt\":\"\",\"firmid\":\"EA3E650A1A9E432891CB\",\"firmdes\":\"�����������\",\"serialid\":\"841ca563daf04a45ab3e7d836a876f3c\",\"tables\":\"3\",\"datmins\":\"\",\"roomtype\":\"\",\"vtransactionid\":\"\",\"ordFrom\":\"CUSTOM\",\"cashier\":\"\"},\"orderid\":\"B3\"}";
                            Map<String, Object> map = JsonUtil.getObject(startresult, Map.class);
                            if (ValueUtil.isNotEmpty(map)) {
                                Integer state = Integer.parseInt(map.get("return").toString());
                                if (ValueUtil.isNotEmpty(state) && state == 0) {
                                    List<Map<String, Object>> foodList = ((Map<String, List<Map<String, Object>>>) map.get("remsg")).get("listNetOrderDtl");
                                    List<Food> foodArray = new ArrayList<Food>();
                                    int i=0;
                                    for (Map<String, Object> foodMap : foodList) {
                                        Food food = new Food();
                                        food.setPKID(foodMap.get("foodsid").toString());
                                        food.setPcname(foodMap.get("foodsname").toString());
                                        food.setIstc(foodMap.get("ispackage").toString().equals("1")?true:false);
                                        food.setPrice(foodMap.get("price").toString());
                                        food.setPcount(foodMap.get("foodnum").toString());
                                        food.setPcode(foodMap.get("pcode").toString());
                                        food.setUnitCode(foodMap.get("unitcode").toString());
                                        food.setUnit(foodMap.get("unit").toString());
                                        food.setWeightflg("1");
                                        food.setTpnum(i+"");
                                        if (foodMap.get("remark").toString().length()>0){
                                            food.setFujiacode("!");
                                            food.setFujiacount("!");
                                            food.setFujiaprice("!");
                                            food.setFujianame(foodMap.get("remark").toString()+"!");
                                        }else {
                                            food.setFujiacode("");
                                            food.setFujianame("");
                                            food.setFujiacount("");
                                            food.setFujiaprice("");
                                        }
                                        List<Map<String,Object>> fujiaList= (List<Map<String,Object>>)foodMap.get("listDishAddItem");
                                        for (Map<String,Object> fujia:fujiaList){
                                            food.setFujiacode(food.getFujiacode() + fujia.get("fcode").toString() + "!");
                                            food.setFujianame(food.getFujianame() + fujia.get("redefineName").toString()+"!");
                                            food.setFujiacount(food.getFujiacount() + fujia.get("ncount").toString() + "!");
                                            food.setFujiaprice(food.getFujiaprice() + fujia.get("nprice").toString() + "!");
                                        }
                                        if (food.isIstc()) {
                                            food.setTpcode(foodMap.get("pcode").toString());
                                            foodArray.add(food);
                                            List<Map<String,Object>> packageList= (List<Map<String,Object>>)foodMap.get("listDishTcItem");
                                            for (Map<String,Object> packageMap:packageList){
                                                Food packFood=new Food();
                                                packFood.setPKID(packageMap.get("pk_orderpackagedetail").toString());
                                                packFood.setPcname(packageMap.get("tcpname").toString());
                                                packFood.setTpname(foodMap.get("foodsname").toString());
                                                packFood.setTpcode(foodMap.get("pcode").toString());
                                                packFood.setTpnum(i+"");
                                                if (packageMap.get("tcremark")!=null&&packageMap.get("tcremark").toString().length()>0){
                                                    packFood.setFujiacode("!");
                                                    packFood.setFujiacount("!");
                                                    packFood.setFujiaprice("!");
                                                    packFood.setFujianame(packageMap.get("tcremark").toString()+"!");
                                                }else {
                                                    packFood.setFujiacode("");
                                                    packFood.setFujianame("");
                                                    packFood.setFujiacount("");
                                                    packFood.setFujiaprice("");
                                                }
                                                if (packageMap.get("tclistDishAddItem")!=null&&packageMap.get("tclistDishAddItem").toString().equals("null")) {
                                                    List<Map<String, Object>> packfujiaList = (List<Map<String, Object>>)packageMap.get("tclistDishAddItem");
                                                    for (Map<String, Object> fujia : packfujiaList) {
                                                        packFood.setFujiacode(packFood.getFujiacode() + fujia.get("fcode").toString() + "!");
                                                        packFood.setFujianame(packFood.getFujianame() + fujia.get("redefineName").toString() + "!");
                                                        packFood.setFujiacount(packFood.getFujiacount() + fujia.get("ncount").toString() + "!");
                                                        packFood.setFujiaprice(packFood.getFujiaprice() + fujia.get("nprice").toString() + "!");
                                                    }
                                                }

                                                packFood.setIstc(true);
                                                packFood.setWeightflg("1");
                                                packFood.setPcode(packageMap.get("tcpcode").toString());
                                                packFood.setUnit(packageMap.get("unit").toString());
                                                packFood.setPrice(packageMap.get("tcprice").toString());
                                                packFood.setUnitCode(packageMap.get("unitcode").toString());
                                                packFood.setPcount(packageMap.get("tcfoodnum").toString());
                                                foodArray.add(packFood);
                                            }

                                        } else {
                                            foodArray.add(food);
                                        }
                                        i++;
                                    }
                                    new ValueSetFile<Food>(storeTable.getTablenum() + DateFormat.getStringByDate(new Date(), "yyyy-MM-dd") +map.get("orderid").toString() + ".mc").write(foodArray);
									foodArray.clear();
									new VipRecordUtil().delHandle(MainActivity.this, storeTable.getTablenum());
									SingleMenu.getMenuInstance().setManCounts(man.getText().toString().trim());
                                    SingleMenu.getMenuInstance().setWomanCounts(woman.getText().toString().trim());
									SingleMenu.getMenuInstance().setTableNum(storeTable.getTablenum());
									SingleMenu.getMenuInstance().setUserCode(SharedPreferencesUtils.getUserCode(MainActivity.this));
									SingleMenu.getMenuInstance().setMenuOrder(map.get("orderid").toString());
                                    SingleMenu.getMenuInstance().setOrderNum(map.get("orderid").toString());
									storeTable.setUsestate("2");
									Intent intent = new Intent(MainActivity.this, Eatables.class);
									intent.putExtra("direction", "MainDirection");
									startActivity(intent);
                                }else {
									Toast.makeText(MainActivity.this, map.get("error").toString(), Toast.LENGTH_SHORT).show();
								}


                            } else {
                                Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
                            }
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }
	/**
	 * ��̨�¼�,������Ů
	 * @param man
	 * @param woman
	 * @param box
	 * @param storeTable
	 * @param dialog
	 */
	private void startTable(final EditText man,final EditText woman,CheckBox box,final Storetable storeTable,final AlertDialog dialog,String ktkind){
		if(ValueUtil.isEmpty(man.getText().toString().trim()) && ValueUtil.isEmpty(woman.getText().toString().trim())){
			Toast.makeText(MainActivity.this, R.string.people_not_null, Toast.LENGTH_SHORT).show();
			return;
		}else{
			TsData.isReserve=false;
			if(box.isChecked()){
				Intent intent=new Intent(MainActivity.this,VipFindActivity.class);
				intent.putExtra("mark", "startTable");
				intent.putExtra("man", man.getText().toString());
				intent.putExtra("woman", woman.getText().toString());
				intent.putExtra("tableNum", storeTable.getTablenum());
				startActivity(intent);
			}else{
				CList<Map<String, String>> clist = new CList<Map<String,String>>();
				clist.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
				clist.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
				clist.add("tableNum", storeTable.getTablenum());
				clist.add("manCounts", man.getText().toString().trim());
				clist.add("womanCounts", woman.getText().toString().trim());
				clist.add("ktKind", ktkind);
				clist.add("openTablemwyn", "1");
				
				Server server1 = new Server();
				server1.connect(MainActivity.this, Constants.FastMethodName.STARTTABLE_METHODNAME, Constants.FastWebService.STARTTABLE_WSDL, clist, new OnServerResponse() {

					@Override
					public void onBeforeRequest() {
                        getLoadingDialog().show();
					}

					@Override
					public void onResponse(String startresult) {
						dialog.dismiss();
                        getLoadingDialog().dismiss();
						if(ValueUtil.isNotEmpty(startresult)){
							String[] str = startresult.split("@");
							if("0".equals(str[0])){

								/**
								 * ������¿�̨ ��ո�̨�»�Ա��¼��Ϣ
								 */
								new VipRecordUtil().delHandle(MainActivity.this, storeTable.getTablenum());
								SingleMenu.getMenuInstance().setManCounts(man.getText().toString().trim());
								SingleMenu.getMenuInstance().setWomanCounts(woman.getText().toString().trim());
								SingleMenu.getMenuInstance().setTableNum(storeTable.getTablenum());
								SingleMenu.getMenuInstance().setUserCode(SharedPreferencesUtils.getUserCode(MainActivity.this));
								SingleMenu.getMenuInstance().setOrderNum(str[1]);
                                SingleMenu.getMenuInstance().setMenuOrder(str[1]);
								storeTable.setUsestate("2");

								Intent intent = new Intent(MainActivity.this, Eatables.class);
								intent.putExtra("direction", "MainDirection");
								startActivity(intent);
							}else{
								Toast.makeText(MainActivity.this,str[1] , Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
			dialog.dismiss();
		}
	}
	/**
	 * ��ȡָ��̨λ��δ�����˵�
	 * @param tableNumber
	 */
	private void checkOrderUseClearTabl(String tableNumber){
		Server server = new Server();
		CList<Map<String, String>> params = new CList<Map<String,String>>();
		params.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
		params.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
		params.add("tableNum", tableNumber);
		server.connect(MainActivity.this, Constants.FastMethodName.notTerminateOrder_METHODNAME, Constants.FastWebService.notTerminateOrder_WSDL, params, new OnServerResponse() {

			@Override
			public void onBeforeRequest() {
                getLoadingDialog().show();
			}

			@Override
			public void onResponse(String result) {
                getLoadingDialog().dismiss();//0@H000152;4;0
				if(ValueUtil.isNotEmpty(result)){
					String [] str = result.split("@");
					String flag = str[0];
					if(flag.equals("0")){
					}else{
						Toast.makeText(MainActivity.this, str[1], Toast.LENGTH_SHORT).show();
						return;
					}
				}else{
					Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	/**
	 * ��ȡָ��̨λ��δ�����˵�
	 * @param tableNumber
	 * @param cal
	 */
	public void checkOrderBytabnum(final String tableNumber,final Class<?> cal){
		Server server = new Server();
		CList<Map<String, String>> params = new CList<Map<String,String>>();
		params.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
		params.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
		params.add("tableNum", tableNumber);
		server.connect(MainActivity.this, Constants.FastMethodName.notTerminateOrder_METHODNAME, Constants.FastWebService.notTerminateOrder_WSDL, params, new OnServerResponse() {

			@Override
			public void onResponse(String result) {
                getLoadingDialog().dismiss();//0@H000152;4;0@H000152;4;0#0#1212@1212
				if(ValueUtil.isNotEmpty(result)){
					String [] str = result.split("@");
					String flag = str[0];
					if(flag.equals("0")){
						Map<String,String> map=AnalyticalXmlUtil.getOrderMs(result);
						if(ValueUtil.isEmpty(map)){
							showToast(R.string.data_error);
							return;
						}
						Bundle bundle = new Bundle();
						bundle.putString("orderId", map.get("orderId"));//�˵���
						bundle.putString("manCs", map.get("manCs"));//��������
						bundle.putString("womanCs", map.get("womanCs"));//Ů������
						bundle.putString("tableNum", tableNumber);//̨λ��
						saveVip(map.get("member"),map.get("orderId"),tableNumber,map.get("manCs"),map.get("womanCs"));//�����Ա��Ϣ
                        SingleMenu.getMenuInstance().setManCounts(map.get("manCs"));
                        SingleMenu.getMenuInstance().setWomanCounts(map.get("womanCs"));
                        SingleMenu.getMenuInstance().setTableNum(tableNumber);
                        SingleMenu.getMenuInstance().setUserCode(SharedPreferencesUtils.getUserCode(MainActivity.this));
                        SingleMenu.getMenuInstance().setOrderNum(map.get("orderId"));
                        if (cal!=null){
                            Intent intent = new Intent(MainActivity.this, cal);
                            intent.putExtra("topBundle", bundle);
                            intent.putExtra("direction", "MainDirection");
                            startActivity(intent);
                        }
					}else{
						if(ValueUtil.isNotEmpty(str[1])&&str[1].toString().equals("NULL")){
							Toast.makeText(MainActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(MainActivity.this, str[1], Toast.LENGTH_SHORT).show();
						}
						return;
					}
				}else{
					Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onBeforeRequest() {
                getLoadingDialog().show();
			}
		});
	}
	/**
	 * ��ȡָ��̨λ��δ�����˵�
	 * @param cal
	 */
	public void checkOrderBytabnum(final Map<String,Object> map,final Class<?> cal){
		Server server = new Server();
		CList<Map<String, String>> params = new CList<Map<String,String>>();
		params.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
		params.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
		params.add("tableNum", map.get("tableName"));
		params.add("orderId", map.get("orderid"));
		server.connect(MainActivity.this, Constants.FastMethodName.QUERY_WHOLE_PRODUCTS, Constants.FastWebService.QUERY_WHOLE_PRODUCTS_WSDL, params, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
                getLoadingDialog().dismiss();//0@H000152;4;0@H000152;4;0#0#1212@1212
				if(ValueUtil.isNotEmpty(result)){
					Bundle bundle = new Bundle();
					String orderId=ValueUtil.isEmpty(map.get("orderid"))?"":map.get("orderid").toString();
					String tableName=ValueUtil.isEmpty(map.get("tableName"))?"":map.get("tableName").toString();
					String manCs=ValueUtil.isEmpty(map.get("manCounts"))?"":map.get("manCounts").toString();
					String womanCs=ValueUtil.isEmpty(map.get("womanCount"))?"":map.get("womanCount").toString();
					bundle.putString("orderId",orderId);//�˵���
					bundle.putString("manCs",manCs);//��������
					bundle.putString("womanCs",womanCs);//Ů������
					bundle.putString("tableNum",tableName);//̨λ��
                    SingleMenu.getMenuInstance().setManCounts(manCs);
                    SingleMenu.getMenuInstance().setWomanCounts(womanCs);
                    SingleMenu.getMenuInstance().setTableNum(tableName);
                    SingleMenu.getMenuInstance().setUserCode(SharedPreferencesUtils.getUserCode(MainActivity.this));
                    SingleMenu.getMenuInstance().setOrderNum(orderId);
					if(ValueUtil.isNotEmpty(map.get("telNumber"))) {
						saveVip(map.get("telNumber").toString(), orderId, tableName, manCs, womanCs);//�����Ա��Ϣ
					}
					Intent intent = new Intent(MainActivity.this, cal);
					intent.putExtra("topBundle", bundle);
					intent.putExtra("direction", "MainDirection");
					startActivity(intent);
				}else{
					Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onBeforeRequest() {
                getLoadingDialog().show();
			}
		});
	}

	/**
	 * ����vip��Ϣ
	 * @param result �����»�Ա��Ϣ
	 * @param orderId �˵���
	 * @param tableNum ̨λ��
	 * @param mancs Ů����
	 * @param woman ������
	 */
	public void saveVip(String result,String orderId,String tableNum,String mancs,String woman){

		if(ValueUtil.isNotEmpty(result)){
			String res[]=result.split("@");
			VipRecord vip=new VipRecord();
			vip.setPhone(res[0]);
			vip.setCardNumber(res[1]);
			vip.setCardType(res[2]);
			vip.setStoredCardsBalance(ValueUtil.isNaNofDouble(res[3]));
			vip.setIntegralOverall(ValueUtil.isNaNofDouble(res[4]));
			vip.setCouponsOverall(ValueUtil.isNaNofDouble(res[5]));
			vip.setCouponsAvail(ValueUtil.isNaNofDouble(res[6]));
            if (res.length>7){
                vip.setTicketInfoList(res[7]);
            }

			vip.setTableNum(tableNum);
			vip.setManCounts(ValueUtil.isNaNofInteger(mancs));
			vip.setWomanCounts(ValueUtil.isNaNofInteger(woman));
			vip.setOrderId(orderId);
			new VipRecordUtil().insertHandle(this, vip);
		}
	}

	/**
	 * ��ʾ��λ����
	 */
	public void showPw(){
		CList<Map<String,String>> data=new CList<Map<String,String>>();
		data.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
		data.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
		new Server().connect(this, "queryReserveTableNum", "ChoiceWebService/services/HHTSocket?/queryReserveTableNum", data, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
                getLoadingDialog().dismiss();
				String[] res=ValueUtil.isNotEmpty(result)?result.split("@"):null;
				if(res!=null){
					List<Map<String,String>> list=AnalyticalXmlUtil.getReserveTable(result);
					ListView listView=(ListView)getRightPw().getContentView().findViewById(R.id.yuding_itemList);
					if(ValueUtil.isEmpty(list)){
						if(listView.getAdapter()!=null){
							listView.setAdapter(null);
						}
						Toast.makeText(MainActivity.this, R.string.not_wait_msg, Toast.LENGTH_LONG).show();
						return;
					}
					//ΪitemLayout��ֵ
					SimpleAdapter adapter=new SimpleAdapter(MainActivity.this, list, R.layout.yuding_item_layout, new String[]{"tabNum","phone","orderId"}, new int[]{R.id.yding_number1,R.id.yding_phone1,R.id.yding_order});
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> paramAdapterView,View paramView, int paramInt, long paramLong) {
							if(getLeftPw().isShowing()){
								getLeftPw().dismiss();
							}
							final Map<String,String> itemMap=(Map<String,String>)paramAdapterView.getAdapter().getItem(paramInt);
                            CList<Map<String,String>> data=new CList<Map<String,String>>();
                            data.add("deviceId", SharedPreferencesUtils.getDeviceId(MainActivity.this));
                            data.add("userCode", SharedPreferencesUtils.getUserCode(MainActivity.this));
                            data.add("tableNum", itemMap.get("phone"));
                            data.add("manCounts", "");
                            data.add("womanCounts", "");
                            data.add("orderId", itemMap.get("orderId"));
                            data.add("chkCode", "");
                            data.add("comOrDetach", "0");
                            new Server().connect(MainActivity.this, "queryProduct", "ChoiceWebService/services/HHTSocket?/queryProduct", data, new OnServerResponse() {
                                @Override
                                public void onResponse(String result) {
                                    getLoadingDialog().dismiss();
                                    String res[]=ValueUtil.isNotEmpty(result)?result.split("@"):null;
                                    if(res==null||!res[0].equals("0")){
                                        new ReserveTableEvent(MainActivity.this).orderChoice(itemMap);
                                    }else{
                                        Map<String,Object> map=AnalyticalXmlUtil.analysisProductW(MainActivity.this,result);
                                        List<Map<String,String>> orderList=(List<Map<String,String>>)map.get("orderList");
                                        if(ValueUtil.isNotEmpty(orderList)){
                                            getLeftPw().showAtLocation(gridView, Gravity.NO_GRAVITY,0, ((Float)getSearchLayout().getY()).intValue());
                                            ListView listView=(ListView)getLeftPw().getContentView().findViewById(R.id.yuding_orderList);
                                            Button cancle=(Button)getLeftPw().getContentView().findViewById(R.id.res_Order_cancle);
                                            Button confirm=(Button)getLeftPw().getContentView().findViewById(R.id.res_Order_confirm);
                                            Button jiacai=(Button)getLeftPw().getContentView().findViewById(R.id.res_jiacai);//�Ӳ�
                                            Button cancleTable=(Button)getLeftPw().getContentView().findViewById(R.id.res_cancel_table);//ȡ��Ԥ��
                                            getReserveTableEvent().setMap(itemMap);
                                            cancle.setOnClickListener(getReserveTableEvent());//ȡ����ť
                                            confirm.setOnClickListener(getReserveTableEvent());//ת����ť
                                            jiacai.setOnClickListener(getReserveTableEvent());
                                            cancleTable.setOnClickListener(getReserveTableEvent());
                                            SimpleAdapter adapter=new SimpleAdapter(MainActivity.this,orderList,R.layout.bill_list_table,new String[]{"pcount","pcname","price","fujianame"},new int[]{R.id.greensNum,R.id.greensName,R.id.greensPrice,R.id.Bill_fuJiaXText1});
                                            listView.setAdapter(adapter);
                                            listView.setTag(itemMap);
                                        }
                                    }
                                }

                                @Override
                                public void onBeforeRequest() {
                                    getLoadingDialog().show();
                                }
                            });
						}
					});
				}
			}
			@Override
			public void onBeforeRequest() {
                getLoadingDialog().show();
			}
		});
	}
	/**
	 * ��ѯ�ݴ�����Ƿ���Ԥ��̨λ�ݴ�����
	 * @param tableNum
	 * @return
	 */
	public boolean queryReserveDis(String tableNum){
		return new ListProcessor().query("select count(*) from AllCheck where tableNum=? and Time=? and send=0", new String[]{tableNum,DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")},this,new ListProcessor.Result<Boolean>(){
			@Override
			public Boolean handle(Cursor c) {
				while (c.moveToNext()) {
					return c.getInt(0)>0;
				}
				return false;
			}
		});
		
	}
	/**
	 * ˢ��̨λ
	 * @param classid ������� ��Ϊ��[Ϊ�ս���ʾ����̨λ]
	 * @param tableNum ̨λ���� ��Ϊ��
	 */
	public void updateDisplay(String classid, final String tableNum){
        if(CHIN_SNACK==0) {
            CList<Map<String, String>> params = new CList<Map<String, String>>();
            params.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
            params.add("userCode", SharedPreferencesUtils.getUserCode(this));
            params.add("floor", "");
            if (SharedPreferencesUtils.getTableClass(this).equals("1")) {
                params.add("area", classid == null ? "" : classid.trim());//����
                params.add("state", "");//״̬
            } else {
                params.add("area", "");//����
                params.add("state", classid == null ? "" : classid.trim());//״̬
            }
            params.add("tableNum", tableNum != null ? tableNum : "");
            Server server = new Server();
            server.connect(this, Constants.FastMethodName.ACHIEVETABLE_METHODNAME, Constants.FastWebService.INITTABLE_WSDL, params, new OnServerResponse() {
                @Override
                public void onResponse(String result) {
                    getLoadingDialog().dismiss();
                    tableLists = AnalyticalXmlUtil.anaRegionAndTable(result);//�ڴ˴���ʼ��̨λ
                    showTable();
                }

                @Override
                public void onBeforeRequest() {
                    getLoadingDialog().show();
                }
            });
        }else{//�в� ����
            CList<Map<String,String>> list=new CList<Map<String, String>>();
            list.add("user",SharedPreferencesUtils.getUserCode(this));
            if(SharedPreferencesUtils.getTableClass(this).equals("1")) {
                list.add("floor", "");
                list.add("area", classid == null ? "" : classid.trim());
                list.add("status","");
            }else if(SharedPreferencesUtils.getTableClass(this).equals("2")){
            	list.add("floor", classid == null ? "" : classid.trim());
                list.add("area", "");
                list.add("status","");
            }else if(SharedPreferencesUtils.getTableClass(this).equals("3")){
            	list.add("floor", "");
                list.add("area", "");
                list.add("status",classid == null ? "" : classid.trim());
            }
            list.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
            list.add("irecno",SingleMenu.getMenuInstance().getMenuOrder());
            list.add("buffer","");
            list.add("orecno","");
            new ChineseServer().connect(this, ChineseConstants.PLISTTABLE, list, new OnServerResponse() {
                @Override
                public void onResponse(String result) {
                    getLoadingDialog().dismiss();
                    //ArrayList<Storetable>
                    Map<String, String> map = new ComXmlUtil().xml2Map(result);
                    if (map != null) {
                        if ("1".equals(map.get("result"))) {
                            String buffer = map.get("oStr");
                            buffer = buffer.substring(buffer.indexOf("<") + 1, buffer.lastIndexOf(">"));
                            String[] tables = buffer.split("\\|");
                            if (tables == null) {
                                tableLists.clear();
                                ToastUtil.toast(MainActivity.this, R.string.data_error);
                                return;
                            }
                            tableLists = new ArrayList<Storetable>();
                            for (String table : tables) {
                                String[] tableInfo = table.split("\\^");
                                if (tableInfo == null || tableInfo.length < 3) {
                                    continue;
                                }
                                Storetable storetable = new Storetable();
                                storetable.setTablenum(tableInfo[1]);
                                storetable.setTblname(tableInfo[2]);
                                storetable.setPerson(tableInfo[3]);
                                storetable.setUsestate(tableInfo[4]);
                                if (tableInfo.length > 5) {
                                    storetable.setOrderId(tableInfo[5]);
                                }
                                if (tableInfo.length > 6) {
                                    storetable.setPriceKay(tableInfo[6]);
                                }
                                tableLists.add(storetable);
                            }
                        } else {
                            if (tableLists != null) {
                                tableLists.clear();
                            }
                        }
                    } else {
                        if (tableLists != null) {
                            tableLists.clear();
                        }
                    }
                    showTable();
                }

                @Override
                public void onBeforeRequest() {
                    getLoadingDialog().show();
                }
            });
        }
	}
	/**
	 * ���β����������˳�Ӧ��
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ExitApp();
		}
		return false;
	}
	private long exitTime = 0;

	/**
	 * �˳���ǰӦ�ó���
	 */
	public void ExitApp() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(MainActivity.this, R.string.exit_app, Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			MainActivity.this.finish();
			System.exit(0);
		}

	}

	/**
	 * ˢ��̨λ
	 */
	public void showTable(){
		if(ValueUtil.isEmpty(tableLists)){
			String cas = SharedPreferencesUtils.getTableClass(this);
			String toasts = "";
			switch (Integer.valueOf(cas)) {
			case 1:
				toasts = this.getString(R.string.is_area_not_table);
				break;
			case 2:
				toasts = this.getString(R.string.is_lc_not_table);
				break;
			case 3:
				toasts = this.getString(R.string.is_state_not_table);
				break;
			default:
				break;
			}
			showToast(toasts);
			gridView.setAdapter(null);
		}else{
			adapter=new TaiWeiAdapter(this, tableLists);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (CHIN_SNACK == 0) {
						MainActivity.this.setOnTypeViewClick(parent, position, view, adapter.getListData().get(position));
					} else {
						MainActivity.this.setOnTypeViewClickChinese(parent, position, view, adapter.getListData().get(position));
					}
				}
			});

            gridView.setOnItemLongClickListener(new CombineTable(this));
		}
	}

	/**
	 * ̨λ����¼�-�в�
	 * @param parent
	 * @param position
	 * @param v
	 * @param object
	 */
    public void setOnTypeViewClickChinese(AdapterView<?> parent,int position, View v,Object object){
        final Storetable storeTable = (Storetable) object;
        SingleMenu.getMenuInstance().setManCounts(storeTable.getPerson().split("/")[0]);//������
        SingleMenu.getMenuInstance().setWomanCounts("");
        SingleMenu.getMenuInstance().setTableNum(storeTable.getTablenum());
        SingleMenu.getMenuInstance().setMenuOrder(storeTable.getOrderId() == null ? "" : storeTable.getOrderId());
        if (!SingleMenu.getMenuInstance().getPriceTyp().equals("Y")){
            SingleMenu.getMenuInstance().setPriceKay(storeTable.getPriceKay());
        }
        final TaiWeiAdapter adapter = (TaiWeiAdapter) parent.getAdapter();
        if("0".equals(storeTable.getUsestate())){//Ԥ��
            ToastUtil.toast(this, "̨λ��Ԥ������ת����ʽ���������");
        }else if("1".equals(storeTable.getUsestate())){//���
            Intent intent=new Intent(MainActivity.this,ChineseYiXuanDishActivity2.class);
            startActivity(intent.putExtra("table",storeTable.getTablenum()).putExtra("direction","MainDirection"));
            guestLists.clear();//���singleton����
        }else if("2".equals(storeTable.getUsestate())){//����
            Dialog mDialog = createChineseStartcDia(storeTable,"0");
            mDialog.show();
            guestLists.clear();
        }else if("3".equals(storeTable.getUsestate())){//��̨
            ToastUtil.toast(this,R.string.feng_tai);
        }else if("4".equals(storeTable.getUsestate())){//�вͿ�̨
            AlertDialog.Builder builder = new Builder(MainActivity.this);
            String [] items = {this.getString(R.string.clean_table),this.getString(R.string.vip_card_disButton),this.getString(R.string.cancle)};
            ListAdapter diaAdapter=new ArrayAdapter<String>(this, R.layout.dia_ifsave_item_layout, R.id.dia_ifsave_item_tv, items);
            builder.setAdapter(diaAdapter, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int position) {
                    String tableNumber = storeTable.getTablenum();
                    switch (position) {
                        case 0:  //��̨
							cleanTable(tableNumber);
                            break;
                        case 1://���
//                            singleMenu.setManCounts("0");//������
//                            singleMenu.setWomanCounts("");
                            SingleMenu.getMenuInstance().setTableNum(tableNumber);
                            SingleMenu.getMenuInstance().setUserCode(SharedPreferencesUtils.getUserCode(MainActivity.this));
//                            singleMenu.setMenuOrder("");
                            startActivity(new Intent(MainActivity.this,ChineseEatables.class).putExtra("direction","MainDirection"));
                            guestLists.clear();//���singleton����
                            break;
                        case 2://ȡ��
                            break;
                        default:
                            break;
                    }
                }
            });
            builder.create().show();
        }else if("4".equals(storeTable.getUsestate())){
			new AlertDialog.Builder(this).setTitle(R.string.hint).setMessage(R.string.clean_table_hint)
					.setNegativeButton(R.string.cancle,null)
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							cleanTable(storeTable.getTablenum());
						}
					}).show();
		}
    }

	/**
	 * ����̨λ
	 * @param tableNumber
	 */
	public void cleanTable(final String tableNumber){
		CList<Map<String, String>> clists = new CList<Map<String,String>>();
		clists.add("pdaid", SharedPreferencesUtils.getDeviceId(MainActivity.this));
		clists.add("user", SharedPreferencesUtils.getUserCode(MainActivity.this));
		clists.add("tblInit", tableNumber);
		clists.add("oStr", "");
		new ChineseServer().connect(MainActivity.this,ChineseConstants.POVER, clists, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
                getLoadingDialog().dismiss();
				guestLists.clear();//���singleton����
				new OrderSaveUtil().delHandle(MainActivity.this, tableNumber);
				if(LayTitle!=null&&LayTitle.getTag()!=null){
					updateDisplay(LayTitle.getTag().toString(),null);
				}
			}
			@Override
			public void onBeforeRequest() {
                getLoadingDialog().show();
			}
		});
	}
    /**
     * �����вͿ�̨������
     * @param storeTable
     * @return
     */
    public Dialog createChineseStartcDia(final Storetable storeTable, final String ktkind){
        Boolean ISSHOW=false;
        if (SingleMenu.getMenuInstance().getPermissionList()!=null) {
            for (Map<String, String> map : SingleMenu.getMenuInstance().getPermissionList()) {
                if (map.get("CODE").equals("21001") && map.get("ISSHOW").equals("1")) {
                    ISSHOW=true;
                }
            }
        }
        View view = LayoutInflater.from(this).inflate(R.layout.dia_createtable_chooseways_item_layout, null);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.dia_createtable_chooseways_rdGroup);
        final LinearLayout normalLayout = (LinearLayout) view.findViewById(R.id.dia_createtable_chooseways_ln_normal);//������̨�Ĳ���
        final ClearEditText normalClearEt = (ClearEditText) view.findViewById(R.id.dia_createtable_chooseways_clet_normal);//������̨���������
        final LinearLayout preOrderLayout = (LinearLayout) view.findViewById(R.id.dia_createtable_chooseways_ln_preorder);//ʹ����ζ���õȿ�̨�Ĳ���
        final ClearEditText preOrderPaxClearEt  = (ClearEditText) view.findViewById(R.id.dia_createtable_chooseways_clet_pax_preorder);
        final ClearEditText preOrderIdClearEt = (ClearEditText) view.findViewById(R.id.dia_createtable_chooseways_clet_orderid_preorder);
        final ClearEditText clearEdit = (ClearEditText) view.findViewById(R.id.start_peopleNumEdit_notdisguish);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int checkId) {
                if(checkId == R.id.dia_createtable_chooseways_rb_normal){//������̨
                    normalLayout.setVisibility(View.VISIBLE);
                    preOrderLayout.setVisibility(View.GONE);
                }else{//ʹ����ζ���õȵ�Ԥ������orderId��̨
                    preOrderLayout.setVisibility(View.VISIBLE);
                    normalLayout.setVisibility(View.GONE);
                }
            }
        });

        if (true != ISSHOW){
            radioGroup.setVisibility(View.GONE);
            preOrderLayout.setVisibility(View.GONE);

        }
        Button comfirmBtn = (Button) view.findViewById(R.id.dia_createtable_chooseways_btn_confirm);
        Button cancelBtn = (Button) view.findViewById(R.id.dia_createtable_chooseways_btn_cancel);
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this,R.style.edittext_dialog);
        builder.setView(view);
        builder.setTitle(getString(R.string.mainactivity_createtable));
        final AlertDialog dialog = builder.create();

        comfirmBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int radioBtnId = radioGroup.getCheckedRadioButtonId();
                String pax = normalClearEt.getText().toString();//����
                pax=(pax.equals("")?"0":pax);
                if(radioBtnId == R.id.dia_createtable_chooseways_rb_normal){//������̨
                    chineseStartTable(storeTable.getTablenum(), pax, dialog,"0",null);
                }else{//ʹ����ζ���õȿ�̨
                    String paxJNow = preOrderPaxClearEt.getText().toString();//�Խ���ζ���õȣ�����Ͳ�����
                    String orderIdJNow = preOrderIdClearEt.getText().toString();//�Խ���ζ���õȣ��ӿ�������Ҫ�Ķ�����

                    if(ValueUtil.isEmpty(orderIdJNow)){
                        ToastUtil.toast(MainActivity.this,R.string.orderid_not_null);//��ζ���õȶ����Ų���Ϊ��
                    }else if(ValueUtil.isEmpty(paxJNow)){//�Ͳ���������Ϊ��
                        ToastUtil.toast(MainActivity.this,R.string.people_not_null);
                    }else{
                            //���ӿڻ�ȡ������orderId
                        chineseStartTable(storeTable.getTablenum(), paxJNow, dialog,"1",orderIdJNow);

                    }
                }
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
//        clearEdit.setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    chineseStartTable(storeTable.getTablenum(), clearEdit.getText().toString(), dialog, "0",null);
//                    return true;
//                }
//                return false;
//            }
//        });
        return  dialog;
    }
    /**
     * �вͿ�̨
     * @param table
     * @param pax
     * @param dialog
     */
    public void chineseStartTable(final String table, final String pax,final AlertDialog dialog,final String type,final String jNowOrderId){
//        if(ValueUtil.isEmpty(pax)){
////            ToastUtil.toast(this,R.string.people_not_null);
////            return;
//            pax="0";
//        }
        CList<Map<String,String>> list=new CList<Map<String, String>>();
        list.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        list.add("user",SharedPreferencesUtils.getUserCode(this));
        list.add("acct","0");//��ֵ
        list.add("tblInit",table);
        list.add("pax",ValueUtil.isEmpty(pax)?"0":pax);//����
//        list.add("typ",type);//����
        list.add("waiter","0");//����Ա��� TODO ��Ϊ��
        list.add("typ",type);//����
        list.add("oStr","");//����Ա���
        new ChineseServer().connect(this,ChineseConstants.PSTART,list,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getLoadingDialog().dismiss();
                if(result==null){
                    ToastUtil.toast(MainActivity.this,R.string.net_error);
                    return;
                }
                Map<String,String> map=new ComXmlUtil().xml2Map(result);
                if(map!=null){
                    if("1".equals(map.get("result"))){
                        dialog.dismiss();
                        new OrderSaveUtil().delHandle(MainActivity.this, table);
                        String oStr=ChineseResultAlt.oStrAlt(map.get("oStr"));
                        //��ע���������ǲ�������Ů�������ӿ�û�з�����������Ů����,��Ϊ""
                        SingleMenu.getMenuInstance().setManCounts(ValueUtil.isEmpty(pax) ? "0" : pax);//������
                        SingleMenu.getMenuInstance().setWomanCounts("");
                        SingleMenu.getMenuInstance().setTableNum(table);
                        SingleMenu.getMenuInstance().setUserCode(SharedPreferencesUtils.getUserCode(MainActivity.this));
                        SingleMenu.getMenuInstance().setMenuOrder(oStr);
                        if (jNowOrderId==null){
                            SingleMenu.getMenuInstance().setJNow(false);
                        }else {
                            SingleMenu.getMenuInstance().setJNow(true);//�Ƿ�������ζ���õȿ���̨λ
                            SingleMenu.getMenuInstance().setJNowFirst(true);//�Ƿ��ǵ�һ��
                            SingleMenu.getMenuInstance().setOrderIdJNow(jNowOrderId);//���˿�������ζ���õȵĶ�����
                        }
                        Intent intent = new Intent(MainActivity.this, ChineseEatables.class);
                        intent.putExtra("direction", "MainDirection");
                        startActivity(intent);
                    }else{
                    	String oStr=ChineseResultAlt.oStrAlt(map.get("oStr"));
                        ToastUtil.toast(MainActivity.this,oStr);
                    }
                }else{
                    ToastUtil.toast(MainActivity.this,R.string.net_error);
                }
            }

            @Override
            public void onBeforeRequest() {
                getLoadingDialog().show();
            }
        });
    }

    /**
     * �в� ��̨
     * @param oldTable
     * @param newTable
     */
    public void chinsesChangeTable(String oldTable,String newTable,AlertDialog alert){
        CList<Map<String,String>> cList=new CList<Map<String, String>>();
        cList.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        cList.add("user",SharedPreferencesUtils.getUserCode(this));
        cList.add("oldTblInit",oldTable.toUpperCase());
        cList.add("newTblInit",newTable.toUpperCase());
        cList.add("oStr","");
        new ChineseServer().connect(this,ChineseConstants.PCHANGETABLE,cList,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                if(ValueUtil.isNotEmpty(result)){
                    Map<String,String> map=new ComXmlUtil().xml2Map(result);
                    if("0".equals(map.get("result"))){
                        ToastUtil.toast(MainActivity.this,ChineseResultAlt.oStrAlt(map.get("oStr")));
                    }else{
                        if(LayTitle!=null&&LayTitle.getTag()!=null){//��̨�ɹ�ˢ��̨λ
                            updateDisplay(LayTitle.getTag().toString(),null);
                        }
                    }
                }else{
                    ToastUtil.toast(MainActivity.this,R.string.huantai_error);
                }
            }
            @Override
            public void onBeforeRequest() {

            }
        });
    }

    /**
     * �вͲ�ѯ�˵�
     * @param table
     */
    public void queryOrder(String table){
        /*pQuery*/
        CList<Map<String,String>> cList=new CList<Map<String, String>>();
        cList.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        cList.add("user",SharedPreferencesUtils.getUserCode(this));
        cList.add("pass","");
        cList.add("tblInit",table==null?table:table.toUpperCase());
        cList.add("irecno",SingleMenu.getMenuInstance().getMenuOrder());
        cList.add("buffer","");
        cList.add("orecno","");
        new ChineseServer().connect(this,ChineseConstants.PQUERY,cList,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                if(ValueUtil.isNotEmpty(result)){
                    getLoadingDialog().dismiss();
                    Map<String,String> map=new ComXmlUtil().xml2Map(result);
                    if("1".equals(map.get("result"))){
                        HashMap<String,Object> order=(HashMap)ChineseResultAlt.mapAlt(map.get("oStr"));
                        if(ValueUtil.isEmpty(order)||ValueUtil.isEmpty(order.get("food"))){
                            ToastUtil.toast(getApplicationContext(),R.string.data_error);
                            return ;
                        }
                        Intent intent=new Intent(MainActivity.this, ChineseFind_orderByTaiNum.class);
                        intent.putExtra("Food",order);
                        startActivity(intent);
                    }else{
                        ToastUtil.toast(MainActivity.this,R.string.data_error);
                    }
                }else{
                    ToastUtil.toast(MainActivity.this, R.string.data_null);
                }
            }
            @Override
            public void onBeforeRequest() {
                getLoadingDialog().show();
            }
        });
    }
	/**
	 * �������Back��
	 */
	@SuppressLint("CommitTransaction")
	public void backKey(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				MainActivity.this.getFragmentManager().beginTransaction();
				Instrumentation inst = new Instrumentation(); 
				inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);  
			}
		}).start();
	}

    /**
     * �����޸�
     */
	public void peopleRevamp(String order,String pax){
        CList list=new CList();
        list.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        list.add("user",SharedPreferencesUtils.getUserCode(this));
        list.add("folioNo",order);
        list.add("pax",pax);
        new ChineseServer().connect(this,ChineseConstants.MODIFYPAX,list,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getLoadingDialog().dismiss();
                Map<String, String> map = new ComXmlUtil().xml2Map(result);
                if (ValueUtil.isEmpty(map)) {
                    ToastUtil.toast(MainActivity.this,R.string.update_people_error);
                    return;
                }
                if ("1".equals(map.get("result"))) {
                    ToastUtil.toast(MainActivity.this,R.string.update_people_success);
                    updateDisplay(LayTitle.getTag().toString(),null);
                }else {
                    ToastUtil.toast(MainActivity.this, R.string.update_people_unsuccess);
                }
            }
            @Override
            public void onBeforeRequest() {
                getLoadingDialog().show();
            }
        });
    }
    //---------------------------------------------------------------
//    public String getParityBit(){
//        String pb="HHT";
//        pb+=DateFormat.getStringByDate(DateFormat.getDateBefore(new Date(),"year",1,1),"yyyy");
//        pb+=DateFormat.getStringByDate(DateFormat.getDateBefore(new Date(), "month", -1, 1),"MM");
//        pb+=DateFormat.getStringByDate(DateFormat.getDateBefore(new Date(),"day",1,1),"dd");
//        Log.i("hht",pb);
//        return Md5.Digest(pb);
//    }
    /**
     * ��ȡparityBit
     * @return
     */
    public String getParityBit(){
        String pb="HHT";
        Calendar ca = Calendar.getInstance();// �õ�һ��Calendar��ʵ��
        ca.setTime(new Date());// �·��Ǵ�0��ʼ�ģ�����11��ʾ12��
        ca.add(Calendar.YEAR, 1); // ��ݼ�1
        ca.add(Calendar.MONTH, -1);// �·ݼ�1
        ca.add(Calendar.DATE, 1);// ���ڼ�1
        Date resultDate = ca.getTime(); // ���
        DateFormat sdf = new DateFormat();
		pb+= DateFormat.formatDateToString(resultDate, "yyyyMMdd");
        return Md5.Digest(pb);
    }

    /**
     * ��ȡ��Ҫ���˵��˵�
     * @param isShow �Ƿ���ʾ�˵���Ϣ
     */
    public void queryOrder(final boolean isShow){
        CList list=new CList();
        list.add("deviceId",SharedPreferencesUtils.getDeviceId(MainActivity.this));
        list.add("userCode",SharedPreferencesUtils.getUserCode(MainActivity.this));
        list.add("strsql","SELECT a.TABLENUM,a.ORDERID,b.TBLNAME,a.PEOLENUMMAN,a.PEOLENUMWOMEN FROM handevtableorder_relation a left JOIN storetables_mis b ON a.TABLENUM=b.TABLENUM WHERE TABLESTATE='0' and MOBILEBILLOK='1'");
        list.add("parityBit",getParityBit());
        new Server().connect(this, "querySqlInterface", "ChoiceWebService/services/HHTSocket?/querySqlInterface", list, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                if(ValueUtil.isNotEmpty(result)){
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        String res= jsonObject.getString("return");
                        if (res.equals("0")){
                            String count=jsonObject.getString("count");
                            if(ValueUtil.isNaNofInteger(count)>0){
                                JSONArray array=jsonObject.getJSONArray("data");
                                orderMapList=new ArrayList<Map<String, String>>();
                               for(int i=0;i<array.length();i++){
                                   JSONObject jo=array.getJSONObject(i);
                                   Map<String,String> map=new HashMap<String, String>();
                                   map.put("TABLENUM",jo.getString("TABLENUM"));
                                   map.put("ORDERID",jo.getString("ORDERID"));
                                   map.put("TBLNAME",jo.getString("TBLNAME"));
                                   map.put("PEOLENUMMAN",jo.getString("PEOLENUMMAN"));
                                   map.put("PEOLENUMWOMEN",jo.getString("PEOLENUMWOMEN"));
                                   orderMapList.add(map);
                                }
                                showPayOrderBut(true);
                            }else{
                                showPayOrderBut(false);
                            }
                            if(isShow){
                                showOrderList();
                            }
                        }else{
                            ToastUtil.toast(MainActivity.this,jsonObject.getString("error"));
                            showPayOrderBut(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onBeforeRequest() {
            }
        });
    }
    /**
     * ����㲥
     */
    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(Intent.ACTION_TIME_CHANGED.equals(action)||Intent.ACTION_TIME_TICK.equals(action)){
                if(ValueUtil.isNotEmpty(SharedPreferencesUtils.getWaitUrl(MainActivity.this))) {
                    if (CHIN_SNACK==0){
                        queryOrder(false);
                    }

                }
            }

        }
    };

    /**
     * ע��㲥
     */
    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);//ʱ��仯�㲥�����ӣ�
        myIntentFilter.addAction(Intent.ACTION_DATE_CHANGED);//���ڱ仯�㲥
        myIntentFilter.addAction(Intent.ACTION_TIME_TICK);//���ڱ仯�㲥
        registerReceiver(mBroadcastReceiver, myIntentFilter);//ע��㲥
    }
    @Override
    protected void onStart() {
        registerBoradcastReceiver();
        super.onStart();
    }
	/**
	 * ���Ӳ˵���ע������
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST + 1, 5, getResources().getString(R.string.logout)).setIcon(R.drawable.logout);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * �˵��¼�
	 * @param item
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			Logout();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    protected void onStop() {
        unregisterReceiver(mBroadcastReceiver);
        super.onStop();
    }
}