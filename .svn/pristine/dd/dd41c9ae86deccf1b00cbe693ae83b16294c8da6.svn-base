package cn.com.choicesoft.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import cn.com.choicesoft.R;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.util.*;

/**
 * 会员查询界面
 */
public class QueryVipCardActivity extends BaseActivity implements OnClickListener {

    private TabHost tabs;//页签控件
    private TabWidget tabWidget;
    private int screemheight;
    private LoadingDialog dialog;
    private EditText phone, vipCard;

    private ListView vipInfo, coupon;
    private SimpleAdapter va;

    private ArrayAdapter<String> adapter;

    private List<Map<String, Object>> card;

    private int selectCard = 0;
    private Button reset;
    private Button back;
    
    private ImageView backIntopLayout;//手机版顶部标题栏左边的返回按钮

    private boolean contentnull = true;
    private TextView czyText, tipTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ChioceActivity.ispad) {
        	setContentView(R.layout.activity_query_vip_card_pad);
		} else {
			setContentView(R.layout.activity_query_vip_card);
			backIntopLayout = (ImageView) this.findViewById(R.id.query_vip_back_iv);
			backIntopLayout.setOnClickListener(this);
		}
        screemheight = getscreemheight();
        //获取会员信息
        String result = "";
        Map<String, Object> map = AnalyticalXmlUtil.getVipInfo(result);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        card = new ArrayList<Map<String, Object>>();
        list.add(map);
        //设置显示会员
//		TextView text=(TextView)this.findViewById(R.id.query_vip_cardText);
//		text.setText(map!=null?map.get("CardNumber").toString():"");
        //获取会员信息Layout 并赋
        va = new SimpleAdapter(this, list, R.layout.query_vip_info_item, new String[]

                {"CardNumber", "joinDate", "", "StoredCardsBalance", "IntegralOverall"}, new int[]

                {R.id.queryVipInfo_cardNum, R.id.queryVipInfo_jiHuoSj, R.id.queryVipInfo_youXiaoQZ, R.id.queryVipInfo_chuZhiYE, R.id.queryVipInfo_jiFenYE});
        vipInfo = (ListView) this.findViewById(R.id.query_vipInfo);
        vipInfo.setAdapter(va);
        vipInfo.setOnItemClickListener(new CardListSelectLisenter());
        //获取优惠券信息Layout 并赋
        @SuppressWarnings("unchecked")
        SimpleAdapter ca = new SimpleAdapter(this, map != null ? (List<Map<String, String>>) map.get

                ("TicketInfoList") : null, R.layout.query_vip_coupon_item, new String[]{"Qid", "Qmoney", "Qname", "Qnum"}, new int[]

                {R.id.query_c_id, R.id.query_c_money, R.id.query_c_name, R.id.query_c_num});
        coupon = (ListView) this.findViewById(R.id.query_E_coupon);
//		coupon.setAdapter(ca);
        //处理界面But事件
        back = (Button) this.findViewById(R.id.query_vip_backBut);
        reset = (Button) this.findViewById(R.id.query_vip_resetBut);
        back.setOnClickListener(this);
        reset.setOnClickListener(this);
        tipTextView = (TextView) findViewById(R.id.query_tip);
        

        //初始化tab
        tabs = (TabHost) this.findViewById(R.id.tabhost_queryVip);
        tabs.setup();
        tabs.addTab(tabs.newTabSpec("tab1").setIndicator(this.getString(R.string.vip_cards)).setContent(R.id.tab_query1));
        tabs.addTab(tabs.newTabSpec("tab2").setIndicator(this.getString(R.string.dian_zi_you_hui)).setContent(R.id.tab_query2));
        tabs.addTab(tabs.newTabSpec("tab3").setIndicator(this.getString(R.string.xiao_fei_qing_kuang)).setContent(R.id.tab_query3));
        tabs.addTab(tabs.newTabSpec("tab4").setIndicator(this.getString(R.string.pay_condition)).setContent(R.id.tab_query4));
        tabWidget = tabs.getTabWidget();
        tabWidget.setStripEnabled(false);
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View view = tabWidget.getChildAt(i);
            TextView tv = (TextView) tabWidget.getChildAt(i)
                    .findViewById(android.R.id.title);
            tv.setTextSize(18);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0); //取消文字底边对齐
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE); //设置文字居中对齐
            view.getLayoutParams().height = screemheight / 17;
            if (tabs.getCurrentTab() == i) {
                view.setBackgroundResource(R.drawable.black_button);
                tv.setTextColor(Color.rgb(255, 255, 255));
            } else {
                view.setBackgroundResource(R.drawable.white_button);
                tv.setTextColor(Color.rgb(90, 90, 90));
            }
        }
        tabs.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabWidget.getChildCount(); i++) {
                    tabWidget.setStripEnabled(false);
                    View view = tabWidget.getChildAt(i);
                    TextView tv = (TextView) tabWidget.getChildAt(i)
                            .findViewById(android.R.id.title);
                    tv.setTextSize(18);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)

                            tv.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0); //取消文字底边对齐
                    params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE); //设置文字居中对齐
                    view.getLayoutParams().height = screemheight / 17;
                    if (tabs.getCurrentTab() == i) {
                        view.setBackgroundResource(R.drawable.black_button);
                        tv.setTextColor(Color.rgb(255, 255, 255));
                    } else {
                        view.setBackgroundResource(R.drawable.white_button);
                        tv.setTextColor(Color.rgb(90, 90, 90));
                    }
                }

            }
        });
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        phone = (EditText) findViewById(R.id.find_vip_phoneEdit);
        vipCard = (EditText) findViewById(R.id.find_vip_cardEdit);
        czyText = (TextView) findViewById(R.id.find_vip_czyText1);//操作员
        czyText.setText(SharedPreferencesUtils.getOperator(this));//操作员
    }


    /**
     * 查询会员
     */
    public void confEvent() {
        String num = phone.getText().toString().trim();
        String cardNum = vipCard.getText().toString().trim();
        if (num.matches("^[1][34578][0-9]{9}$") && "".equals(cardNum)) {
            CList<Map<String, String>> cl = new CList<Map<String, String>>();
            cl.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
            cl.add("userCode", SharedPreferencesUtils.getUserCode(this));
            cl.add("phoneNumber", num);
            //根据手机号码查询会员信息
            new Server().connect(this, "card_GetTrack2", "ChoiceWebService/services/HHTSocket?/card_GetTrack2",

                    cl, new OnServerResponse() {
                @Override
                public void onResponse(String result) {
                    getDialog().dismiss();
                    String[] str = result == null ? null : result.split("@");
                    if (ValueUtil.isNotEmpty(str) && str[0].equals("0")) {
                        List<String> list = Arrays.asList(str[2].split(";"));
                        for (int i = 0; i

                                < list.size(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("CardNumber", list.get(i));
                            card.add(map);
                        }
                        SimpleAdapter adapter = new SimpleAdapter(QueryVipCardActivity.this, card,
                                R.layout.query_vip_info_item, new String[]{"CardNumber", "joinDate", "", "StoredCardsBalance", "IntegralOverall"}, new int[]
                                {R.id.queryVipInfo_cardNum, R.id.queryVipInfo_jiHuoSj, R.id.queryVipInfo_youXiaoQZ, R.id.queryVipInfo_chuZhiYE, R.id.queryVipInfo_jiFenYE});
                        vipInfo.setAdapter(adapter);
                        contentnull = false;
                        reset.setText(R.string.reset);
                        tipTextView.setVisibility(View.GONE);
                    } else if (ValueUtil.isNotEmpty(str) && !str[0].equals("0")) {
                        showToast(str[1]);
                    } else {
                        showToast(R.string.net_error);
                    }
                }

                @Override
                public void onBeforeRequest() {
                    getDialog().show();
                }
            });
        } else if (!cardNum.equals("")) {

            boolean mark = true;
            // card_QueryBalance（String deviceId,String userCode ,String cardNumber,String orderId
            CList<Map<String, String>> cl = new CList<Map<String, String>>();
            cl.add("deviceId", SharedPreferencesUtils.getDeviceId(QueryVipCardActivity.this));
            cl.add("userCode", SharedPreferencesUtils.getUserCode(QueryVipCardActivity.this));
            cl.add("cardNumber", cardNum);
            if (mark) {//如果会员卡被复制则进行查询会员信??
                new Server().connect(QueryVipCardActivity.this, "card_QueryBalance",

                        "ChoiceWebService/services/HHTSocket?/card_QueryBalance", cl, new OnServerResponse() {
                    @Override
                    public void onResponse(String result) {
                        getDialog().dismiss();
                        String[] str = result == null ? null : result.split("@");
                        if (ValueUtil.isNotEmpty(str) && str[0].equals("0")) {
                            LoadResult(result, selectCard);
                            contentnull = false;
                            reset.setText(R.string.reset);
                            tipTextView.setVisibility(View.GONE);
                        } else if (ValueUtil.isNotEmpty(str) && !str[0].equals("0")) {
                            showToast(str[1]);
                        } else {
                            showToast(R.string.net_error);
                        }
                    }

                    @Override
                    public void onBeforeRequest() {
                        getDialog().show();
                    }
                });
            } else {
                showToast(R.string.num_error);
            }


        } else {
            showToast(R.string.num_error);
        }
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
    }

    public LoadingDialog getDialog() {
        if (dialog == null) {
            dialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
            dialog.setCancelable(true);
        }
        return dialog;
    }

    class CardListSelectLisenter implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

            selectCard = arg2;
            String cardNum = card.get(arg2).get("CardNumber").toString();
            if (ValueUtil.isEmpty(cardNum)) {
                return;
            }
            boolean mark = true;
            // card_QueryBalance（String deviceId,String userCode ,String cardNumber,String orderId
            CList<Map<String, String>> cl = new CList<Map<String, String>>();
            cl.add("deviceId", SharedPreferencesUtils.getDeviceId(QueryVipCardActivity.this));
            cl.add("userCode", SharedPreferencesUtils.getUserCode(QueryVipCardActivity.this));
            if (ValueUtil.isNotEmpty(cardNum) && cardNum.length() == 16) {//如果输入的是会员卡号
                cl.add("cardNumber", cardNum);
            } else {//如果以上都不满足则错??
                showToast(R.string.num_error);
                mark = false;
            }
            if (mark) {//如果会员卡被复制则进行查询会员信??
                new Server().connect(QueryVipCardActivity.this, "card_QueryBalance",

                        "ChoiceWebService/services/HHTSocket?/card_QueryBalance", cl, new OnServerResponse() {
                    @Override
                    public void onResponse(String result) {
                        getDialog().dismiss();
                        String[] str = result == null ? null : result.split("@");
                        if (ValueUtil.isNotEmpty(str) && str[0].equals("0")) {
                            LoadResult(result, selectCard);
                            contentnull = false;
                            reset.setText(R.string.reset);
                            tipTextView.setVisibility(View.GONE);
                        } else if (ValueUtil.isNotEmpty(str) && !str[0].equals("0")) {
                            showToast(str[1]);
                        } else {
                            showToast(R.string.net_error);
                        }
                    }

                    @Override
                    public void onBeforeRequest() {
                        getDialog().show();
                    }
                });
            } else {
                showToast(R.string.net_error);
            }


        }
    }

    /**
     * 刷新会员信息
     * @param result
     * @param arg2
     */
    private void LoadResult(String result, int arg2) {

        Map<String, Object> map = AnalyticalXmlUtil.getVipInfo(result);
        if (card.size() != 0) {
            card.remove(arg2);
            card.add(arg2, map);
        } else {
            card.add(map);
        }
        SimpleAdapter vipAdapter = new SimpleAdapter(this, card, R.layout.query_vip_info_item, new String[]

                {"CardNumber", "joinDate", "", "StoredCardsBalance", "IntegralOverall"}, new int[]

                {R.id.queryVipInfo_cardNum, R.id.queryVipInfo_jiHuoSj, R.id.queryVipInfo_youXiaoQZ, R.id.queryVipInfo_chuZhiYE, R.id.queryVipInfo_jiFenYE});
        vipInfo.setAdapter(vipAdapter);
        SimpleAdapter ca = new SimpleAdapter(this, map != null ? (List<Map<String, String>>) map.get("TicketInfoList") : null, R.layout.query_vip_coupon_item, new String[]{"Qid", "Qmoney", "Qname", "Qnum"}, new int[]
                {R.id.query_c_id, R.id.query_c_money, R.id.query_c_name, R.id.query_c_num});
        coupon.setAdapter(ca);
    }

    @Override
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.query_vip_resetBut:
                if (contentnull) {
                    confEvent();
                } else {
                    card = new ArrayList<Map<String, Object>>();
                    SimpleAdapter adapter = new SimpleAdapter

                            (QueryVipCardActivity.this, card, R.layout.query_vip_info_item, null, null);
                    vipInfo.setAdapter(adapter);
                    coupon.setAdapter(adapter);
                    phone.setText("");
                    vipCard.setText("");
                    reset.setText(R.string.confirm);
                    contentnull = true;
                    tipTextView.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.query_vip_backBut:
                this.finish();
                break;
                
            case R.id.query_vip_back_iv:
	            this.finish();
	            break;
            default:
                break;
        }
    }

    private int getscreemheight() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();

        return height;
    }
}
