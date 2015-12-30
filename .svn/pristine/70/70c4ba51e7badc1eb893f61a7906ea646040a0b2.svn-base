package cn.com.choicesoft.chinese.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.BaseActivity;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.bean.Grptyp;
import cn.com.choicesoft.chinese.adapter.GuQingExpListAdapter;
import cn.com.choicesoft.chinese.adapter.GuQingListAdapter;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.listener.GuQingWatcher;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.chinese.util.JsonUtil;
import cn.com.choicesoft.util.InputMethodUtils;
import cn.com.choicesoft.util.OnServerResponse;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.view.ClearEditText;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

/**
 * M。c
 * 2015-08-14
 * Jnwsczh@163.com
 */
public class ChineseGuQingActivity extends BaseActivity implements View.OnClickListener{
    private Button back;
    private ExpandableListView expandableListView;
    private ListView listView;
    private GuQingExpListAdapter guQingExpListAdapter;
    private GuQingListAdapter guQingListAdapter;
    private LoadingDialog loadingDialog;
    private List<Grptyp> grpLists;
    private List<Food> dishLists;
    private Button searchBtn;//搜索提示按钮
    private ClearEditText searchEdt;//搜索输入框
    private TextView searchCancel;//搜索取消按钮

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gu_qing);
        initView();
    }

    public void initView(){
        loadingDialog = new LoadingDialogStyle(this,this.getString(R.string.please_wait));
        loadingDialog.setCancelable(true);
        back= (Button) this.findViewById(R.id.gu_qing_btn_back);
        searchBtn= (Button) this.findViewById(R.id.btn_searchView);
        searchEdt =  (ClearEditText) this.findViewById(R.id.et_searchView);
        searchCancel = (TextView) this.findViewById(R.id.btn_searchCancel);
        expandableListView = (ExpandableListView) this.findViewById(R.id.gu_qing_exp_list);
        listView = (ListView) this.findViewById(R.id.gu_qing_list);
        searchBtn.setOnClickListener(this);
        back.setOnClickListener(this);
        searchCancel.setOnClickListener(this);
        getFoodData();
    }


    public void getFoodData(){
        new ChineseServer().connect(this, ChineseConstants.ESTIMATES_FOOD_LIST, null, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                loadingDialog.dismiss();
                List<Map<String,String>> msg = null;
                try {
                    Map<String,Object> map= JsonUtil.getObject(result, Map.class);
                    if(ValueUtil.isNotEmpty(map)){
                        Integer state=((Map<String,Integer>)map.get("result")).get("state");
                        if(ValueUtil.isNotEmpty(state)&&state==1) {
                            msg = ((Map<String, List<Map<String, String>>>) map.get("result")).get("msg");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                grpLists = getDataManager(ChineseGuQingActivity.this).getAllDishClassList();//获得菜品类别
                Grptyp grptyp=new Grptyp();//默认一个估清类别
                grptyp.setDes(ChineseGuQingActivity.this.getString(R.string.gu_qing_typ));
                grptyp.setGrp("GQTYP");

                grpLists.add(grptyp);
                dishLists =  getDataManager(ChineseGuQingActivity.this).getAllFoodList();//获取所有菜品
                guQingExpListAdapter=new GuQingExpListAdapter(ChineseGuQingActivity.this, expandableListView,grpLists,dishLists,msg);
                guQingListAdapter=new GuQingListAdapter(ChineseGuQingActivity.this,dishLists,msg);
                expandableListView.setAdapter(guQingExpListAdapter);
                searchEdt.addTextChangedListener(new GuQingWatcher(dishLists, guQingListAdapter));
            }
            @Override
            public void onBeforeRequest() {
                loadingDialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gu_qing_btn_back:
                this.finish();
                break;
            case R.id.btn_searchView:
                searchBtn.setVisibility(View.GONE);
                searchEdt.requestFocus();
                expandableListView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(guQingListAdapter);
                //弹出键盘
                InputMethodUtils.toggleSoftKeyboard(this);
                break;
            case R.id.btn_searchCancel://搜索取消按钮
                searchEdt.setText("");
                searchBtn.setVisibility(View.VISIBLE);
                searchEdt.clearFocus();
                expandableListView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                break;
        }
    }
}