package cn.com.choicesoft.chinese.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.BaseActivity;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.chinese.adapter.ChineseYiXuanDishesAdapter;
import cn.com.choicesoft.chinese.adapter.ChineseYiXuanDishesAdapter.ChineseYiXuanViewHolder;
import cn.com.choicesoft.chinese.adapter.ChineseYiXuanDishesAdapter.OnBtnClickCallback;
import cn.com.choicesoft.chinese.bean.ChineseZS;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseResultAlt;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.util.SlideListView.SlideDirection;
import cn.com.choicesoft.util.SlideListView.SlideListener;
import cn.com.choicesoft.view.ClearEditText;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.math.BigDecimal;
import java.util.*;


/**
 * �ѵ�˵�
 *
 */
public class ChineseYiXuanDishActivity2 extends BaseActivity implements OnClickListener,SlideListener,AdapterView.OnItemLongClickListener,OnBtnClickCallback {

    private TextView dishCounts;
    private TextView moneyCounts;//�����Ʒ  3.0��                  �ܼ� 51.80                                      �˵��ţ�H000364

    private PopupWindow popupWindow=null;//ִ�л�Ա��
    private TextView tableNum;//̨λ��
    private TextView orderNum;//�˵���
    private TextView peopleNum;//�Ͳ�����
    private TextView userNum;//ͷ����ɫ����������

    private LoadingDialog mLoadingDialog;

    private Button table;//̨λ
    private Button promptDish;//�߲�
    private Button clearDish;//����
    private Button addDish;//�Ӳ�
    private Button prePrint;//
    private Button preBalance;//
    private Button foodback;//�˲�
    private Button foodToOtherTable;

    private Button btnSearch;
    private TextView btnCancel;
    private ClearEditText etSearch;

    private SlideListView listView;
    private ChineseYiXuanDishesAdapter adapter;

    private String tableNumber;
    private String menuOrder;
    private String peopleNumber;

    private List<Food> foodArray;

    private LinearLayout topDigitalLayout;//���ֱ�������һ��������

    private CheckBox selectAllCb;//ȫѡ��ť
    private List<Food> matchLists = new ArrayList<Food>();

    private ImageView menuinfo_imageView;

    private View layout;

    private LinearLayout topLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ChioceActivity.ispad) {
            setContentView(R.layout.chinese_yixuandish_pad);
        } else {
            setContentView(R.layout.chinese_yixuandish);
        }
        initViews();
        initListener();
        initData();
    }

    private void initData() {
        mLoadingDialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
        mLoadingDialog.setCancelable(true);
        tableNumber = this.getIntent().getStringExtra("table");
        tableNum.setText(tableNumber);
        peopleNum.setText(peopleNumber);
        orderNum.setText(menuOrder);
        userNum.setText(SharedPreferencesUtils.getOperator(this));//���ò���Ա

        refresh();//���ýӿڻ�ȡ����
        listView.setOnItemLongClickListener(this);//���ó����¼�
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initListener() {
        table.setOnClickListener(this);
        promptDish.setOnClickListener(this);
        clearDish.setOnClickListener(this);
        addDish.setOnClickListener(this);
        prePrint.setOnClickListener(this);
        preBalance.setOnClickListener(this);
        foodback.setOnClickListener(this);
        foodToOtherTable.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChineseYiXuanViewHolder holder = (ChineseYiXuanViewHolder) view.getTag();
                holder.checkbox.toggle();
                ChineseYiXuanDishesAdapter adapter = (ChineseYiXuanDishesAdapter) parent.getAdapter();
                adapter.toggleChecked(position);
            }
        });

        listView.setSlideListener(this);

        //ȫѡ��ť
        selectAllCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if(isChecked){
                    adapter.setAllItemCheckedAndNotify(true);// �����ȡ��ƷΪ�� ���ѵ��Ʒ�����ȫѡ ��ָ��
                }else{
                    adapter.setAllItemCheckedAndNotify(false);
                }
            }
        });
    }

    private void initViews() {
        //������ɫ��
        if (ChioceActivity.ispad) {
            tableNum = (TextView) this.findViewById(R.id.dishesact_tv_tblnum);
            orderNum = (TextView) this.findViewById(R.id.dishesact_tv_ordernum);
            peopleNum = (TextView) this.findViewById(R.id.dishesact_tv_people);
            userNum = (TextView) this.findViewById(R.id.dishesact_tv_usernum);
        }else {
            menuinfo_imageView = (ImageView)findViewById(R.id.orderinfo_ImageView);
            menuinfo_imageView.setClickable(true);
            menuinfo_imageView.setOnClickListener(this);
            layout = LayoutInflater.from(ChineseYiXuanDishActivity2.this).inflate(R.layout.chinese_menu_info, null);
            tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
            orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
            peopleNum = (TextView) layout.findViewById(R.id.dishesact_tv_people);
            userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
            topLinearLayout = (LinearLayout) findViewById(R.id.topLinearLyout);
        }

//		topDigitalLayout = (LinearLayout) this.findViewById(R.id.toptitle_rg_number);
//		topDigitalLayout.setVisibility(View.GONE);

        selectAllCb = (CheckBox) this.findViewById(R.id.yixuan_cb_quanxuan);

        dishCounts = (TextView) this.findViewById(R.id.yixuan_tv_dishcounts);
        moneyCounts = (TextView) this.findViewById(R.id.yixuan_tv_moneycounts);

        table = (Button) this.findViewById(R.id.yixuan_btn_table);
        promptDish = (Button) this.findViewById(R.id.yixuan_btn_promptDish);
        clearDish = (Button) this.findViewById(R.id.yixuan_btn_clearDish);
        addDish = (Button) this.findViewById(R.id.yixuan_btn_addDish);
        prePrint = (Button) this.findViewById(R.id.yixuan_btn_prePrint);
        preBalance = (Button) this.findViewById(R.id.yixuan_btn_preBalance);
        foodback = (Button) this.findViewById(R.id.yixuan_btn_foodback);
        foodToOtherTable = (Button) this.findViewById(R.id.yixuan_btn_zhuandan);
        listView = (SlideListView) this.findViewById(R.id.yixuan_listview_alldishes);

        btnSearch = (Button) this.findViewById(R.id.btn_searchView);
        btnCancel = (TextView) this.findViewById(R.id.btn_searchCancel);
        etSearch = (ClearEditText) this.findViewById(R.id.et_searchView);
        etSearch.addTextChangedListener(watcher);
        Button huiYuanJia=(Button)this.findViewById(R.id.SA_huiYuanJia);//��Ա��
        huiYuanJia.setOnClickListener(this);
        if (SingleMenu.getMenuInstance().getPermissionList()!=null) {
            for (Map<String, String> map : SingleMenu.getMenuInstance().getPermissionList()) {
                if (map.get("CODE").equals("41001")){
                    promptDish.setTag(map.get("AUTHORITY"));
                    if (map.get("ISSHOW").equals("0")) {
                        promptDish.setVisibility(View.GONE);
                    }
                }
                if (map.get("CODE").equals("41002")){
                    clearDish.setTag(map.get("AUTHORITY"));
                    if (map.get("ISSHOW").equals("0")) {
                        clearDish.setVisibility(View.GONE);
                    }
                }
                if (map.get("CODE").equals("41003")){
                    foodToOtherTable.setTag(map.get("AUTHORITY"));
                    if (map.get("ISSHOW").equals("0")) {
                        foodToOtherTable.setVisibility(View.GONE);
                    }
                }
                if (map.get("CODE").equals("41004")){
                    addDish.setTag(map.get("AUTHORITY"));
                    if (map.get("ISSHOW").equals("0")) {
                        addDish.setVisibility(View.GONE);
                    }
                }
                if (map.get("CODE").equals("41005")){
                    foodback.setTag(map.get("AUTHORITY"));
                    if (map.get("ISSHOW").equals("0")) {
                        foodback.setVisibility(View.GONE);
                    }
                }
                if (map.get("CODE").equals("41006")){
                    preBalance.setTag(map.get("AUTHORITY"));
                    if (map.get("ISSHOW").equals("0")) {
                        preBalance.setVisibility(View.GONE);
                    }
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

        Food food = adapter.getItem(position);
        if(null == food && null == food.getPcode()){
            Toast.makeText(this, R.string.hua_cai_error, Toast.LENGTH_LONG).show();
            return;
        }
        switch(dir){
            case RIGHT:
                huaDishes(food.getPcode(),"Y",null);
                break;
            case LEFT:
                huaDishes(food.getPcode(),"N",null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yixuan_btn_table://̨λ
                Intent intent = new Intent(ChineseYiXuanDishActivity2.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.yixuan_btn_promptDish://�߲�
                if(adapter==null||adapter.getSelectedCount() == 0){
                    Toast.makeText(this, R.string.please_checked_to_handle, Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer sb = new StringBuffer();
                int count = 0;
                for(Food mFood:foodArray){
                    if(mFood.isSelected()&&!ValueUtil.isNaNofDouble(mFood.getPcount()).equals(ValueUtil.isNaNofDouble(mFood.getOver()))){
                        sb.append(mFood.getPcode()+",");
                    }else{
                        count++;
                    }
                }
                if(sb.length()>1){
                    sb.deleteCharAt(sb.length()-1);
                }else if(count>0){
                    ToastUtil.toast(this,R.string.huaCai_not_cuiCai);
                    return;
                }
                CList<Map<String,String>> cList = new CList<Map<String, String>>();
                cList.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
                cList.add("user",SharedPreferencesUtils.getUserCode(this));
                cList.add("pass","");
                cList.add("acct",menuOrder);//�˵���
                cList.add("oSerial",sb.toString());//��Ʒ����
                cList.add("oStr","");
                new ChineseServer().connect(this,ChineseConstants.PGOGO,cList,new OnServerResponse() {

                    @Override
                    public void onResponse(String result) {
                        mLoadingDialog.dismiss();
                        if(ValueUtil.isEmpty(result)){
                            return ;
                        }
                        Map<String,String> map = new ComXmlUtil().xml2Map(result);
                        if(ValueUtil.isEmpty(map)){
                            return;
                        }
                        if("0".equals(map.get("result"))){
                            ToastUtil.toast(ChineseYiXuanDishActivity2.this, ChineseResultAlt.oStrAlt(map.get("oStr")));
                            return;
                        }
                        refresh();
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
                Intent it = new Intent(ChineseYiXuanDishActivity2.this, ChineseEatables.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.putExtra("direction", "XS");
                SingleMenu singleMenu = SingleMenu.getMenuInstance();
                singleMenu.setTableNum(tableNumber);
                singleMenu.setMenuOrder(menuOrder);
                singleMenu.setManCounts(peopleNumber);
                startActivity(it);
                break;
            case R.id.yixuan_btn_prePrint: //Ԥ��ӡ
                printOrderByServer("1");//���ýӿڴ�ӡ��ѯ��
                break;
            case R.id.yixuan_btn_preBalance://Ԥ����
                if(TsData.isReserve){
                    alertReserve();
                    break;
                }
                if(!adapter.isModifyAllCnt()){
                    Toast.makeText(this, R.string.chinese_yixuan_ts_modify, Toast.LENGTH_SHORT).show();//������δ�޸������ĵڶ���λ��Ʒ,�������޸���������ִ�н���
                    return;
                }
                Intent intent1 = new Intent(ChineseYiXuanDishActivity2.this,ChineseSettleAccountsActivity.class);
                startActivity(intent1.putExtra("table",tableNumber).putExtra("order",menuOrder));
                break;

            case R.id.btn_searchView:
                btnSearch.setVisibility(View.GONE);
                etSearch.requestFocus();
                InputMethodUtils.toggleSoftKeyboard(this);
                break;
            case R.id.btn_searchCancel:
                etSearch.setText("");
                btnSearch.setVisibility(View.VISIBLE);
                etSearch.clearFocus();
                listView.setEnabled(true);
                break;
            case R.id.yixuan_btn_foodback:
                foodBackConf();
                break;
            case R.id.orderinfo_ImageView:
                // TODO
                View layout;
                layout = LayoutInflater.from(ChineseYiXuanDishActivity2.this).inflate(R.layout.chinese_menu_info, null);
                tableNum = (TextView) layout.findViewById(R.id.dishesact_tv_tblnum);
                orderNum = (TextView) layout.findViewById(R.id.dishesact_tv_ordernum);
                peopleNum = (TextView) layout.findViewById(R.id.dishesact_tv_people);
                userNum = (TextView) layout.findViewById(R.id.dishesact_tv_usernum);
                initData();
                AlertDialog tipdialog = new AlertDialog.Builder(ChineseYiXuanDishActivity2.this, R.style.Dialog_tip).setView(layout).create();
                tipdialog.setCancelable(true);
                Window dialogWindow = tipdialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
                lp.x = 0; // ��λ��X����
                lp.y = topLinearLayout.getHeight() -15; // ��λ��Y����
                dialogWindow.setAttributes(lp);
                tipdialog.show();
                break;
            case R.id.SA_huiYuanJia:
                vipPrice((Button) view);
                break;
            case R.id.cancel_huiyuanjia:
                popupWindow.dismiss();
                updatePrice("1");
                break;
            case R.id.execute_huiyuanjia:
                popupWindow.dismiss();
                updatePrice("4");
                break;
            case R.id.yixuan_btn_zhuandan:
                FoodToOtherOrderClick();
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
         /*pQuery*/
        CList<Map<String,String>> cList = new CList<Map<String, String>>();
        SingleMenu singleMenu = SingleMenu.getMenuInstance();
        cList.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        cList.add("user",SharedPreferencesUtils.getUserCode(this));
        cList.add("pass","");
        cList.add("tblInit",singleMenu.getTableNum());
        cList.add("irecno",singleMenu.getMenuOrder());
        cList.add("buffer","");
        cList.add("orecno", "");
        new ChineseServer().connect(this, ChineseConstants.PQUERY, cList, new OnServerResponse() {

            @Override
            public void onResponse(String result) {
                mLoadingDialog.dismiss();
                if (ValueUtil.isNotEmpty(result)) {
                    Log.i("ȫ��ҳ��ӿڷ��ص�����", result);
                    Map<String, String> map = new ComXmlUtil().xml2Map(result);
                    if ("1".equals(map.get("result"))) {
                        HashMap<String, Object> order = (HashMap) ChineseResultAlt.foodAlt(map.get("oStr"));
                        if (ValueUtil.isNotEmpty(foodArray)) {
                            foodArray.clear();
                        }
                        if (ValueUtil.isEmpty(order)) {
                            ToastUtil.toast(ChineseYiXuanDishActivity2.this, R.string.data_error);
                            return;
                        }
                        //===================================================================
                        menuOrder = order.get("order").toString();
                        peopleNumber = order.get("people").toString();
                        orderNum.setText(menuOrder);
                        peopleNum.setText(peopleNumber);
                        //===================================================================
                        foodArray = (List<Food>) order.get("food");
                        //������Ҫ��,�Ѿ��˵��Ĳ�Ʒ��������ʾ��,�͵ôӼ�����ɾȥ�˵��Ĳ�Ʒ
                        for (Iterator<Food> it = foodArray.iterator(); it.hasNext(); ) {
                            Food food = it.next();
                            if (food.getPcount().equals("0.00") && Double.parseDouble(food.getIsNormalCnt()) < 0) {
                                it.remove();
                            }
                        }
                        if (adapter == null) {
                            adapter = new ChineseYiXuanDishesAdapter(ChineseYiXuanDishActivity2.this, foodArray);
                            listView.setAdapter(adapter);
                            adapter.setOnBtnClickCallback(ChineseYiXuanDishActivity2.this);
                        } else {
                            adapter.setData(foodArray);
                            adapter.notifyDataSetChanged();
                        }
                        dishCounts.setText(adapter.getDishSelectedCount() + " " + ChineseYiXuanDishActivity2.this.getString(R.string.part));

                        //�õ��ܵļ�Ǯ
                        double totalSalary = adapter.getTotalSalary();
                        moneyCounts.setText((String)order.get("money"));
//                        moneyCounts.setText(BigDecimal.valueOf(ValueUtil.isNaNofDouble(order.get("money").toString())).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                        if (selectAllCb != null) {
                            selectAllCb.setChecked(false);
                        }
                    } else {
                        ToastUtil.toast(ChineseYiXuanDishActivity2.this, R.string.data_error);
                    }
                } else {
                    ToastUtil.toast(ChineseYiXuanDishActivity2.this, R.string.data_null);
                }
            }

            @Override
            public void onBeforeRequest() {
                mLoadingDialog.show();
            }
        });
    }

    public void alertReserve(){
        Toast.makeText(this,R.string.reserve_table_error,Toast.LENGTH_LONG).show();//Ԥ��̨λ��֧�ִ˲���
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            String mark = this.getIntent().getStringExtra("direction");
            Intent intent2 = null;
            if(mark!=null && mark.equals("MainDirection")){
                intent2 = new Intent(ChineseYiXuanDishActivity2.this, MainActivity.class);
            }else{
                intent2 = new Intent(ChineseYiXuanDishActivity2.this, ChineseEatables.class);
            }
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent2.putExtra("direction", "yixuan");
            startActivity(intent2);
        }
        return super.onKeyDown(keyCode, event);
    }


    //���������õ�
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

    /**
     * ����
     * @param food
     * @param flag
     */
    public void huaDishes(String food, final String flag, final List<Food> list){
        CList<Map<String,String>> cList=new CList<Map<String, String>>();
        cList.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        cList.add("user",SharedPreferencesUtils.getUserCode(this));
        cList.add("folioNo",menuOrder);
        cList.add("serials",food);//�˵���
        cList.add("flag",flag);//��Ʒ����
        new ChineseServer().connect(this,ChineseConstants.DRAWITEMS,cList,new OnServerResponse() {

            @Override
            public void onResponse(String result) {
                mLoadingDialog.dismiss();
                if(ValueUtil.isEmpty(result)){
                    return ;
                }
                Log.i("���˽ӿڷ��صĽ��", result);
                Map<String,String> map=new ComXmlUtil().xml2Map(result);
                if(ValueUtil.isEmpty(map)){
                    return;
                }
                Log.i("�������map", map.toString());
                if("0".equals(map.get("result"))){
                    String oStr=map.get("oStr");
                    Toast.makeText(ChineseYiXuanDishActivity2.this, ChineseResultAlt.oStrAlt(oStr), Toast.LENGTH_LONG).show();
                    return;
                }
                if(list!=null && list.size()>0 && "Y".equals(flag)){
                    StringBuffer sb=new StringBuffer();
                    setFood(list,sb);
                    huaDishes(sb.toString(),"N",null);
                }else{
                    refresh();
                    Toast.makeText(ChineseYiXuanDishActivity2.this, R.string.hua_cai_success, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onBeforeRequest() {
                mLoadingDialog.show();
            }
        });
    }

    /**
     * ������˰�ť���л���ѡ��
     */
    public void callElide(){
        if(adapter==null||adapter.getSelectedCount() == 0){
            Toast.makeText(this, R.string.please_checked_to_handle, Toast.LENGTH_SHORT).show();//����ѡ�и�ѡ���ٲ���
            return;
        }

        if(adapter.getSelectedCount() == foodArray.size()){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.hint)
                    .setMessage(R.string.is_all_hua_cai)//ȫ������/�����ˣ�
                    .setPositiveButton(R.string.yixuan_huacai, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            huaDishes(null, "Y", null);
                        }
                    })
                    .setNeutralButton(R.string.fan_hua_cai, new DialogInterface.OnClickListener() {//������
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            huaDishes(null, "N",null);
                        }
                    }).setNegativeButton(R.string.cancle, null).show();
        }else{
            final List<Food> list = new ArrayList<Food>();
            final List<Food> reList = new ArrayList<Food>();
            for(final Food food:foodArray){
                BigDecimal count = ValueUtil.isNaNofBigDecimal(food.getPcount()).subtract(ValueUtil.isNaNofBigDecimal(food.getOver()));
                String countString = count.toString().matches("-?\\d+$")?count.toString():count.intValue()+"";
                if(food!=null && food.isSelected()){
                    if(count.doubleValue()>0){
                        food.setOverCount(countString);
                        list.add(food);
                    }else if(count.doubleValue() == 0){
                        food.setOverCount(food.getPcount());
                        reList.add(food);
                    }else{
                        food.setOverCount(count.abs().toString());
                        reList.add(food);
                    }
                }
            }
            StringBuffer sb;
            if(list.size()>0){
                sb=new StringBuffer();
                setFood(list,sb);
                huaDishes(sb.toString(), "Y",reList);
            }else if(reList.size()>0){
                sb=new StringBuffer();
                setFood(reList,sb);
                huaDishes(sb.toString(),"N",null);
            }
        }
    }

    /**
     * ƴ�Ӳ�Ʒ
     * @param list
     * @param sb
     */
    public void setFood(List<Food> list,StringBuffer sb){
        for(Food food:list){
            sb.append(food.getPcode()).append("^");
        }
        sb.deleteCharAt(sb.length() - 1);
    }

    public void foodBackConf(){
        if(adapter==null||adapter.getSelectedCount() == 0){
            Toast.makeText(this, R.string.please_checked_to_handle, Toast.LENGTH_SHORT).show();
            return;
        }
        if(adapter.getSelectedCount() > 1){
            Toast.makeText(this, R.string.please_checked_one_handle, Toast.LENGTH_SHORT).show();
            return;
        }
        for(final Food food:foodArray){
            if(food.isSelected()){
                List<Map<String,String>> list = new ListProcessor().query("select code,des,sno from CODEDESC WHERE CODE ='XT'",null,this,new ListProcessor.Result<List<Map<String, String>>>() {

                    @Override
                    public List<Map<String, String>> handle(Cursor c) {
                        List<Map<String,String>> list=new ArrayList<Map<String, String>>();
                        while (c.moveToNext()){
                            Map<String,String> map=new HashMap<String, String>();
                            map.put(c.getColumnName(0),c.getString(0));
                            map.put(c.getColumnName(1),c.getString(1));
                            map.put(c.getColumnName(2),c.getString(2));
                            list.add(map);
                        }
                        return list;
                    }
                });

                LinearLayout linear=(LinearLayout)this.getLayoutInflater().inflate(R.layout.food_back,null);
                final EditText count=(EditText)linear.findViewById(R.id.back_count);
                final Spinner cause=(Spinner)linear.findViewById(R.id.back_cause);
                final EditText user=(EditText)linear.findViewById(R.id.root_code);
                final EditText pass=(EditText)linear.findViewById(R.id.back_pwd);
                LinearLayout userView=(LinearLayout)linear.findViewById(R.id.root_code_linearLayout);
                LinearLayout pwdView=(LinearLayout)linear.findViewById(R.id.root_pwd_linearLayout);
                if (SingleMenu.getMenuInstance().getPermissionList()!=null) {
                    for (Map<String, String> map : SingleMenu.getMenuInstance().getPermissionList()) {
                        if (map.get("CODE").equals("41007")){
                            if (map.get("ISSHOW").equals("0")) {
                                userView.setVisibility(View.GONE);
                                pwdView.setVisibility(View.GONE);
                                String[] sourceStrArray = SharedPreferencesUtils.getUserCode(this).split("-");
                                user.setText(sourceStrArray[0]);
                                pass.setText(sourceStrArray[1]);
                            }
                        }
                    }
                }
                SimpleAdapter simpleAdapter=new SimpleAdapter(this,list,R.layout.spinner_item,new String[]{"DES","SNO"},new int[]{R.id.spinner_name,R.id.spinner_code});
                cause.setAdapter(simpleAdapter);
                count.setText(food.getPcount());
                new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.foodback).setView(linear)
                        .setNegativeButton(R.string.cancle, null)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String cnt = count.getText().toString();
                                if (ValueUtil.isNaNofDouble(cnt) > 0) {
                                    Map<String,String> map=(Map<String,String>)cause.getSelectedItem();
                                    String [] splitStrs = SharedPreferencesUtils.getUserCode(ChineseYiXuanDishActivity2.this).split("-");
                                    foodBack(user.getText().toString(),pass.getText().toString(),food.getPcode(), cnt, map.get("SNO"));//�����˲˽ӿ�
                                } else {
                                    ToastUtil.toast(ChineseYiXuanDishActivity2.this, R.string.food_back_count_error);
                                }
                            }
                        }).show();
                break;
            }
        }
    }

    /**
     * �˲�
     */
    public void foodBack(String emp,String pass,String code,String count,String sno){
        CList<Map<String, String>> list = new CList<Map<String,String>>();
        list.add("pdaid",SharedPreferencesUtils.getDeviceId(this));
        list.add("user",SharedPreferencesUtils.getUserCode(this));
        list.add("pass","");
        list.add("grantEmp",emp);
        list.add("grantPass",pass);
        list.add("oSerial",code);
        list.add("rsn",sno);
        list.add("cnt",count);
        list.add("oStr","");
        new ChineseServer().connect(this, ChineseConstants.PCHUCK, list, new OnServerResponse() {

            @Override
            public void onResponse(String result) {
                mLoadingDialog.dismiss();
                if (ValueUtil.isEmpty(result)) {
                    Toast.makeText(ChineseYiXuanDishActivity2.this, R.string.food_back_error, Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("�˲˽ӿڷ��ص�����", result);
                Map<String, String> map = new ComXmlUtil().xml2Map(result);
                if (ValueUtil.isEmpty(map)) {
                    Toast.makeText(ChineseYiXuanDishActivity2.this, R.string.food_back_error, Toast.LENGTH_LONG).show();
                    return;
                }
                String oStr = map.get("oStr");
                if ("1".equals(map.get("result"))) {
                    refresh();
                    Toast.makeText(ChineseYiXuanDishActivity2.this, ChineseResultAlt.oStrAlt(oStr), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChineseYiXuanDishActivity2.this, ChineseResultAlt.oStrAlt(oStr), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBeforeRequest() {
                mLoadingDialog.show();
            }
        });
    }

    /**
     * ת����ť�¼�
     * @param
     */
    public void FoodToOtherOrderClick() {
        if(adapter==null||adapter.getSelectedCount() == 0){
            Toast.makeText(this, R.string.please_checked_to_handle, Toast.LENGTH_SHORT).show();//����ѡ�и�ѡ���ٲ���
            return;
        }
        final StringBuffer sb = new StringBuffer();
        int count = 0;
        for(Food mFood:foodArray){
            if(mFood!=null&&mFood.isSelected()){
                sb.append(mFood.getPcode()+",");
            }
        }
        if(sb.length()>1){
            sb.deleteCharAt(sb.length()-1);
        }
        View diaLayout = this.getLayoutInflater().inflate(R.layout.chinese_yixuan_modify_dishcount, null);
        final TextView title=(TextView)diaLayout.findViewById(R.id.textView5);
        final EditText countEdtText = (EditText) diaLayout.findViewById(R.id.dia_yixuan_modifycount_edtText);//Ҫ�޸ĺ������
        title.setText("������̨λ��");
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.edittext_dialog);
        builder.setTitle("��Ʒת��");
        builder.setView(diaLayout);
        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                String modifyCountStr = countEdtText.getText().toString();
                if(ValueUtil.isEmpty(modifyCountStr)){
                    AlertDialogTitleUtil.setDialogIfDismissed(dialog, false);
                    Toast.makeText(ChineseYiXuanDishActivity2.this, R.string.chinese_yixuan_inputTable, Toast.LENGTH_SHORT).show();
                }else{
                    pFoodToOtherOredr(sb.toString(), modifyCountStr);
                    AlertDialogTitleUtil.setDialogIfDismissed(dialog, true);
                }
            }
        });
        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                AlertDialogTitleUtil.setDialogIfDismissed(dialog, true);
            }
        });
        builder.create().show();
    }
    private void pFoodToOtherOredr(final String pFood,final String tableInit){

        CList<Map<String, String>> params = new CList<Map<String,String>>();
        params.add("pdaid", SharedPreferencesUtils.getDeviceId(this));//pda���
        params.add("user", SharedPreferencesUtils.getUserCode(this));//�û���ţ�user-pass��
        params.add("pserial", pFood);//��Ʒ����ˮ��
        params.add("tblInit", tableInit);//�޸ĵ�����

        new ChineseServer().connect(this, ChineseConstants.FOODTOOTHERORDR, params, new OnServerResponse() {

            @Override
            public void onResponse(String result) {
                mLoadingDialog.dismiss();
                if(ValueUtil.isNotEmpty(result)){
                    Log.i("�޸������ӿڷ���ֵ", result);
                    Map<String,String> map = new ComXmlUtil().xml2Map(result);
                    if(map.get("result").equals("1")){
                        refresh();
                        Toast.makeText(getApplicationContext(), "ת���ɹ�", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "ת��ʧ��", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), R.string.net_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBeforeRequest() {
                mLoadingDialog.show();
            }
        });
    }


    /**
     * @author Young
     * @created 2014-10-24
     * @comment ��ȫ��ҳ�棬���ӿ��޸Ĳ�Ʒ������
     * @param pFood ��Ҫ�޸ĵ��ǵ���Ʒ����
     * @param foodCnt �޸ĺ�Ĳ�Ʒ����
     */
    private void modifyFCountByServer(final Food pFood,final String foodCnt){

        CList<Map<String, String>> params = new CList<Map<String,String>>();
        params.add("pdaid", SharedPreferencesUtils.getDeviceId(this));//pda���
        params.add("user", SharedPreferencesUtils.getUserCode(this));//�û���ţ�user-pass��
        params.add("serial", pFood.getPcode());//��Ʒ����ˮ��
        params.add("cnt", foodCnt);//�޸ĵ�����

        new ChineseServer().connect(this, ChineseConstants.MODIFY_FOODCNT, params, new OnServerResponse() {

            @Override
            public void onResponse(String result) {
                mLoadingDialog.dismiss();
                if(ValueUtil.isNotEmpty(result)){
                    Log.i("�޸������ӿڷ���ֵ", result);
                    Map<String,String> map = new ComXmlUtil().xml2Map(result);
                    if(map.get("result").equals("1")){
                        refresh();
                        Toast.makeText(getApplicationContext(), "�޸Ĳ�Ʒ�����ɹ�", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "�����������Ժ�����", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), R.string.net_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBeforeRequest() {
                mLoadingDialog.show();
            }
        });

    }



    /**
     * ��ʾ�޸Ĳ�Ʒ�����ĶԻ���
     * @author Young
     * @created 2014-10-24
     * @comment
     * @param foodCur
     */
    private void showDialogwithModifyCount(final Food foodCur){
        View diaLayout = this.getLayoutInflater().inflate(R.layout.chinese_yixuan_modify_dishcount, null);
        final EditText countEdtText = (EditText) diaLayout.findViewById(R.id.dia_yixuan_modifycount_edtText);//Ҫ�޸ĺ������
        countEdtText.setText(foodCur.getPcount());

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.edittext_dialog);
        builder.setTitle("�����޸�");
        builder.setView(diaLayout);
        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                String modifyCountStr = countEdtText.getText().toString();
                if (ValueUtil.isEmpty(modifyCountStr)) {
                    AlertDialogTitleUtil.setDialogIfDismissed(dialog, false);
                    Toast.makeText(ChineseYiXuanDishActivity2.this, R.string.chinese_yixuan_inputcode, Toast.LENGTH_SHORT).show();
                } else {
                    modifyFCountByServer(foodCur, modifyCountStr);
                    AlertDialogTitleUtil.setDialogIfDismissed(dialog, true);
                }
            }
        });
        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                AlertDialogTitleUtil.setDialogIfDismissed(dialog, true);
            }
        });
        builder.create().show();
    }


//	public String donateItem(String pdaid,String user,String serial,String cnt,String emp,String grant,String rsn){

    private void processZSByServer(Food foodCur,String presentCnt,String presentCode){
        CList<Map<String, String>> params = new CList<Map<String,String>>();
        params.add("pdaid", SharedPreferencesUtils.getDeviceId(this));//
        params.add("user", SharedPreferencesUtils.getUserCode(this));//
        params.add("serial", foodCur.getPcode());//�˵���ϸ��ˮ��,Ҳ���ǲ�Ʒ����
        params.add("cnt", presentCnt);//��������
        params.add("emp", SharedPreferencesUtils.getUserCode(this));//����Ա����
        params.add("grant", "1");//������Ȩ��
        params.add("rsn", presentCode);//����ԭ�򣨴�������ԭ���Ӧ�ı��룩
        new ChineseServer().connect(this, ChineseConstants.DONATE_FOODITEM, params, new OnServerResponse() {

            @Override
            public void onResponse(String result) {
                mLoadingDialog.dismiss();
                if(ValueUtil.isNotEmpty(result)){
                    Log.i("���ͽӿڷ��ص�����", result);
                    Map<String,String> map = new ComXmlUtil().xml2Map(result);
                    String oStr=map.get("oStr");
                    if("1".equals(map.get("result"))){
                        Toast.makeText(ChineseYiXuanDishActivity2.this,ChineseResultAlt.oStrAlt(oStr), Toast.LENGTH_SHORT).show();
                        refresh();//���µ��ӿ�ˢ�½���
                    }else{
                        Toast.makeText(ChineseYiXuanDishActivity2.this,ChineseResultAlt.oStrAlt(oStr), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChineseYiXuanDishActivity2.this, R.string.net_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBeforeRequest() {
                mLoadingDialog.show();
            }
        });

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position,long id) {
        int countSelected = adapter.getSelectedCount();
        if(countSelected == 0){
            Toast.makeText(this, R.string.chinese_yixuan_choosed, Toast.LENGTH_SHORT).show();//������ѡ�в�Ʒ
        }else if(countSelected > 1){
            Toast.makeText(this, R.string.chinese_yixuan_choosed_one, Toast.LENGTH_SHORT).show();//ͬһʱ�̣�ֻ���޸�һ����Ʒ��������������ѡ
        }else{
            Food selectedItem = adapter.getSelectedItem();
            if(selectedItem.getPlusd()!=0){
                showDialogwithModifyCount(selectedItem);
            }else{
                Toast.makeText(this, R.string.chinese_xibei_notmodifycount, Toast.LENGTH_SHORT).show();//�ò�Ʒ�����ڵڶ���λ��Ʒ,�����޸�����
            }
        }
        return true;
    }

    /**
     * ���ӿڴ�ӡ��ѯ������˵�
     * @author Young
     * @created 2014-11-11
     * @comment
     * @param printType ��ӡ���ͣ�1����ѯ�� 2�����˵���
     */
    private void printOrderByServer(String printType){
        /**
         * @Description:
         * @Title:pPrintquery
         * @Author:dwh
         * @Date:2014-11-5 ����3:17:21
         * @param pdaid padid
         * @param user �û�
         * @param serial �˵���
         * @param typ ��ӡ���ͣ�1����ѯ�� 2�����˵���
         * @return
         */
        //public String pPrintquery(String pdaid,String user,String serial,String typ){*/
        CList<Map<String, String>> params = new CList<Map<String,String>>();
        params.add("pdaid", SharedPreferencesUtils.getDeviceId(this));//
        params.add("user", SharedPreferencesUtils.getUserCode(this));//
        params.add("serial", menuOrder);//�˵���
        params.add("typ", printType);
        new ChineseServer().connect(this, ChineseConstants.PRINT_ORDER, params, new OnServerResponse() {

            @Override
            public void onResponse(String result) {
                mLoadingDialog.dismiss();
                if(ValueUtil.isNotEmpty(result)){
                    Log.i("��ӡ��ѯ������˵��ӿڷ��صĽ��", result);
                    Map<String,String> map = new ComXmlUtil().xml2Map(result);
                    if(ValueUtil.isEmpty(map)){
//	                    Toast.makeText(ChineseYiXuanDishActivity2.this,R.string.food_back_error, Toast.LENGTH_LONG).show();
                        return;
                    }
                    String oStr=map.get("oStr");
                    if("1".equals(map.get("result"))){
                        Toast.makeText(ChineseYiXuanDishActivity2.this,ChineseResultAlt.oStrAlt(oStr), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChineseYiXuanDishActivity2.this,ChineseResultAlt.oStrAlt(oStr), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChineseYiXuanDishActivity2.this, R.string.net_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBeforeRequest() {
                mLoadingDialog.show();
            }
        });
    }

    private void vipPrice(Button v) {
        View view=this.getLayoutInflater().inflate(R.layout.update_price,null);
        if(popupWindow==null){
            if(ChioceActivity.ispad){
                popupWindow = new PopupWindow(view, (int) (v.getWidth() * 1.2), ViewGroup.LayoutParams.WRAP_CONTENT);
            }else {
                popupWindow = new PopupWindow(view, v.getWidth() * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }else {
            popupWindow.showAtLocation(v, Gravity.BOTTOM, v.getWidth()*2, v.getHeight());
            Button cancel = (Button) view.findViewById(R.id.cancel_huiyuanjia);
            Button execute = (Button) view.findViewById(R.id.execute_huiyuanjia);
            cancel.setOnClickListener(this);
            execute.setOnClickListener(this);
        }
    }

    /**
     * ��Ա���޸�
     * @param typ
     */
    public void updatePrice(final String typ){
        View view=this.getLayoutInflater().inflate(R.layout.verify_jurisdiction,null);
        final EditText name= (EditText) view.findViewById(R.id.verify_jur_user);
        final EditText pass=(EditText)view.findViewById(R.id.verify_jur_pass);
        new AlertDialog.Builder(this).setTitle(R.string.vip_jurisdiction).setView(view).setNegativeButton(R.string.cancle, null)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CList list = new CList();
                        list.add("pdaid", SharedPreferencesUtils.getDeviceId(ChineseYiXuanDishActivity2.this));
                        list.add("user", SharedPreferencesUtils.getUserCode(ChineseYiXuanDishActivity2.this));
                        list.add("grantEmp", name.getText().toString());
                        list.add("grantPass", pass.getText().toString());
                        list.add("pserial", menuOrder);
                        list.add("typ", typ);
                        new ChineseServer().connect(ChineseYiXuanDishActivity2.this, ChineseConstants.UPDATE_PRICE, list, new OnServerResponse() {
                            @Override
                            public void onResponse(String result) {
                                mLoadingDialog.dismiss();
                                if (ValueUtil.isNotEmpty(result)) {
                                    Map<String, String> map = new ComXmlUtil().xml2Map(result);
                                    if (ValueUtil.isEmpty(map)) {
                                        ToastUtil.toast(ChineseYiXuanDishActivity2.this, R.string.data_error);
                                        return;
                                    }
                                    if ("1".equals(map.get("result"))) {
                                        if ("1".equals(typ)) {
                                            ToastUtil.toast(ChineseYiXuanDishActivity2.this, R.string.cancelPayment_success);
                                        }else{
                                            ToastUtil.toast(ChineseYiXuanDishActivity2.this, R.string.execute_success);
                                        }
                                        refresh();
                                    } else {
                                        ToastUtil.toast(ChineseYiXuanDishActivity2.this, map.get("oStr"));
                                    }
                                } else {
                                    ToastUtil.toast(ChineseYiXuanDishActivity2.this, R.string.data_null);
                                }
                            }

                            @Override
                            public void onBeforeRequest() {
                                mLoadingDialog.show();
                            }
                        });
                    }
                }).show();
    }


    @Override
    public void btnClick(final Food food) {
        if(ValueUtil.isNotEmpty(food.getUnit2()) && food.getModifyFlag().equals("N")){
            Toast.makeText(this, R.string.chinese_yixuan_ts_present, Toast.LENGTH_SHORT).show();//�ò�Ʒ���ڵڶ���λ��Ʒ,�������޸���������ִ������
            return;
        }
        final List<ChineseZS> presentLists = getDataManager(this).queryChineseZS();//��ѯ������ԭ��
        List<Map<String, String>> simpleLists = new ArrayList<Map<String,String>>();
        for(ChineseZS czs:presentLists){
            Map<String, String> map = new HashMap<String, String>();
            map.put("DES", czs.getDes());
            map.put("SNO", czs.getSno()+"");
            simpleLists.add(map);
        }

        View layout = LayoutInflater.from(this).inflate(R.layout.chineseyixuan_present_layout, null);
        final EditText cntEdt = (EditText) layout.findViewById(R.id.dia_chineseyixuan_presentcount);
        cntEdt.setText(food.getPcount());//���������������
        final Spinner spinner = (Spinner) layout.findViewById(R.id.dia_chineseyixuan_spinner);
        SimpleAdapter adapter = new SimpleAdapter(this,simpleLists,R.layout.spinner_item,new String[]{"DES","SNO"},new int[]{R.id.spinner_name,R.id.spinner_code});
        spinner.setAdapter(adapter);
        AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.edittext_dialog)
                .setTitle("����")
                .setView(layout)
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (spinner.getSelectedItemPosition() >=0) {
                            String presentCnts = cntEdt.getText().toString().trim();//��ȡ���͵�����
                            String presentCode = presentLists.get(spinner.getSelectedItemPosition()).getSno() + "";//��ȡ�����Ͳ�Ʒ��code
                            if (ValueUtil.isEmpty(presentCnts)) {
                                AlertDialogTitleUtil.setDialogIfDismissed(dialog, false);
                                Toast.makeText(ChineseYiXuanDishActivity2.this, R.string.chinese_yixuan_inputzscnt, Toast.LENGTH_SHORT).show();//������Ҫ���͵�����
                            } else if (Double.parseDouble(presentCnts) > Double.parseDouble(food.getPcount())) {
                                AlertDialogTitleUtil.setDialogIfDismissed(dialog, false);
                                Toast.makeText(ChineseYiXuanDishActivity2.this, R.string.chinese_yixuan_beyondzscnt, Toast.LENGTH_SHORT).show();//�����������͵�����������ԭ�в�Ʒ������
                            } else {
                                processZSByServer(food, presentCnts, presentCode);//�������Ͳ�Ʒ�Ľӿ�
                                AlertDialogTitleUtil.setDialogIfDismissed(dialog, true);
                            }
                        }else{
                            ToastUtil.toast(ChineseYiXuanDishActivity2.this,R.string.chinese_yixuan_zs_null);
                        }
                    }
                })
                .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        AlertDialogTitleUtil.setDialogIfDismissed(dialog, true);
                    }
                }).create();
        alertDialog.show();
    }

}