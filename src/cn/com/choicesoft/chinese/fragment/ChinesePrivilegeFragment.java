package cn.com.choicesoft.chinese.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.bean.CouponMain;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.chinese.bean.Actm;
import cn.com.choicesoft.chinese.bean.ActmTyp;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.fragment.BillFragment;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠Fragment
 *
 * @Author:M.c
 * @CreateDate:2014-1-6
 * @Email:JNWSCZH@163.COM
 */
public class ChinesePrivilegeFragment extends Fragment implements OnClickListener {
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private LoadingDialog dialog;
    private Bundle bundle;
    private ListProcessor pro;

    private int screenWidth;

    public ListProcessor getPro() {
        if (pro == null) {
            pro = new ListProcessor();
        }
        return pro;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public LoadingDialog getDialog() {
        if (dialog == null) {
            dialog = new LoadingDialogStyle(getActivity(), getString(R.string.please_wait));
            dialog.setCancelable(true);
        }
        return dialog;
    }

    public Server getServer() {
        Server server = new Server();
        return server;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化PrivilegeFragment-动态加载优惠分类按钮
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<ActmTyp> list = null;
        try {
            list = getPro().query("select distinct B.PK_ACTTYPMIN,B.VMEMO,B.VCODE,B.VNAME,B.ENABLESTATE,B.PK_ACTTYP,B.VINIT,B.TS from ACTM A,ACTTYP B where A.pk_acttypmin=B.pk_acttypmin", null, getActivity(), ActmTyp.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final View view = inflater.inflate(R.layout.privilege_fragment, null);
        DisplayMetrics dmm = new DisplayMetrics();
        // 获取窗口属性
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dmm);
        // 窗口宽度
        screenWidth = (int) (dmm.widthPixels * 0.55);
        //获取LinearLayout
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.youHuiLB);
        linearLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
        LinearLayout row = new LinearLayout(getActivity());
        row.setGravity(Gravity.LEFT | Gravity.CENTER);
        row.setOrientation(LinearLayout.HORIZONTAL);
        //设置按钮样式
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (screenWidth * 0.4), (int) (screenWidth * 0.15));
        params.setMargins(0, 1, 1, 0);
        int count = 0;
        int size = ValueUtil.isNotEmpty(list) ? list.size() : 0;
        if (size <= 0) {
            return view;
        }
        //循环优惠分类
        for (ActmTyp typ : list) {
            count++;
            Button but = new Button(getActivity());
            but.setText(typ.getVname());
            but.setTextSize(10);
            but.setTextColor(Color.GRAY);
            //为按钮附事件
            but.setOnClickListener(ChinesePrivilegeFragment.this);
            but.setTag(typ);
            //判断but是不是超出屏幕宽度，如果超出者把按钮加到下一行
            if (((row.getChildCount() + 1) * (int) (screenWidth * 0.4)) > screenWidth - 10) {
                row.getChildAt(row.getChildCount() - 1).setBackgroundResource(R.drawable.but_right_bg);//but背景
                row.getChildAt(row.getChildCount() - 1).setLayoutParams(params);//but样式
                linearLayout.addView(row);
                row = new LinearLayout(getActivity());//对多出屏幕的but加到下一行
                row.setGravity(Gravity.LEFT | Gravity.CENTER);
                row.setOrientation(LinearLayout.HORIZONTAL);
                but.setBackgroundResource(R.drawable.but_left_bg);
                row.addView(but, params);
            } else {
                if (row.getChildCount() == 0) {//第一个but
                    but.setBackgroundResource(R.drawable.but_left_bg);
                    row.addView(but, params);
                } else {//普通but
                    but.setBackgroundResource(R.drawable.but_center_bg);
                    params.setMargins(1, 1, 0, 0);
                    row.addView(but, params);
                }
            }
            if (size <= count) {//对多出but加载到最后一行
                row.setGravity(Gravity.LEFT | Gravity.CENTER);
                if (row.getChildCount() == 1) {//修改单一but样式
                    row.getChildAt(row.getChildCount() - 1).setBackgroundResource(R.drawable.but_alone_bg2);
                } else {//多个but样式
                    row.getChildAt(row.getChildCount() - 1).setBackgroundResource(R.drawable.but_right_bg);
                    row.getChildAt(row.getChildCount() - 1).setLayoutParams(params);
                }
                linearLayout.addView(row);
            }
        }
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 优惠分类but事件
     */
    @Override
    public void onClick(View v) {
        List<Actm> list = null;
        try {
            list = getPro().query("select vname, pk_actm, vcode,\n" +
                    "enablestate, bremit, nscorerate, pk_acttyp,showinpad, bistime,\n" +
                    "nderatenum, badjust, paidmoney, poundage, operategroup,\n" +
                    "bltype, xztj, jmrdo, pk_acttypmin,ismember,vvoucherdisc,vatwill," +
                    "voucherback,isvalidate from Actm where pk_acttypmin=? and showinpad='Y'", new String[]{((ActmTyp) v.getTag()).getPk_acttypmin()}, getActivity(), Actm.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.youHuiMX);
        linearLayout.removeAllViews();
        LinearLayout hLinearLayout = new LinearLayout(getActivity());
        hLinearLayout.setGravity(Gravity.CENTER);
        if (ValueUtil.isEmpty(list) || list.size() <= 0) {
            return;
        }
        //根据手机和PAD属性不通
        double widthbl = ChioceActivity.ispad?0.44:0.52;
        double heightbl = ChioceActivity.ispad?0.14:0.15;
        int textsize = ChioceActivity.ispad?15:10;
        int count = ChioceActivity.ispad?2:1;
        for (Actm actm : list) {//循环优惠券生成按钮
            if (actm.getShowinpad().trim().equals("Y")) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(screenWidth*widthbl), (int)(screenWidth*heightbl));
                params.setMargins(5, 8, 8, 5);
                Button but = new Button(getActivity());
                but.setTag(actm);
                but.setMinWidth(100);
                but.setText(actm.getVname());
                but.setTextSize(textsize);
                but.setBackgroundResource(R.drawable.yellow_button_background);
                but.setTextColor(Color.WHITE);
                but.setOnClickListener(new YouHuiMXClick());
                hLinearLayout.addView(but, params);
                if (hLinearLayout.getChildCount() == count) {//每行显示but数
                    linearLayout.addView(hLinearLayout);
                    hLinearLayout = new LinearLayout(getActivity());
                    hLinearLayout.setGravity(Gravity.CENTER);
                }
            }
        }
        if (hLinearLayout.getChildCount() > 0) {
            linearLayout.addView(hLinearLayout);
        }
    }

    class YouHuiMXClick implements OnClickListener {
        @Override
        public void onClick(final View v) {
            final Actm actm= (Actm) v.getTag();
            if(actm.getIsmember().trim().equals("Y")){//是否会员
                vipCheck(actm);
            }else if(actm.getVname().trim().equals("抹零")){//是否抹零
                String money= ((TextView)getActivity().findViewById(R.id.moLJE_Text2)).getText().toString();
                actm.setPaidmoney(money);
            }else if(actm.getVname().trim().equals("服务费")||actm.getVname().trim().equals("包间费")||actm.getVatwill().trim().equals("Y")){//是否服务费
                inputAlert(actm, new IResult<Actm>() {
                    @Override
                    public void result(Actm actm) {
                        isAuth(actm);
                    }
                });
            }else if(actm.getIsvalidate().trim().equals("Y")){//是否需要团购验证
            }else {
                isAuth(actm);
            }
        }

        public void vipCheck(final Actm actm){
            View view=getActivity().getLayoutInflater().inflate(R.layout.chinese_alert_vip,null);
            final RadioButton phone= (RadioButton) view.findViewById(R.id.alert_vip_phone_but);
            RadioButton card= (RadioButton) view.findViewById(R.id.alert_vip_cardid_but);
            final LinearLayout phone_layout= (LinearLayout) view.findViewById(R.id.alert_vip_phone_layout);
            final LinearLayout card_layout= (LinearLayout) view.findViewById(R.id.alert_vip_card_layout);
            Button cancel= (Button) view.findViewById(R.id.alert_vip_cancel_but);
            final Button confirm= (Button) view.findViewById(R.id.alert_vip_confirm_but);
            final Button query= (Button) view.findViewById(R.id.alert_vip_query_but);
            final EditText phone_edit= (EditText) view.findViewById(R.id.alert_vip_phone_edit);//手机号输入框
            final EditText card_edit= (EditText) view.findViewById(R.id.alert_vip_card_edit);//会员卡输入框
            final Spinner card_spinner= (Spinner) view.findViewById(R.id.alert_vip_card_spinner);
            phone.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    phone_layout.setVisibility(View.VISIBLE);
                    card_layout.setVisibility(View.GONE);
                    if (card_spinner.getCount() <= 0) {
                        query.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.GONE);
                    } else {
                        query.setVisibility(View.GONE);
                        confirm.setVisibility(View.VISIBLE);
                    }
                }
            });
            card.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    phone_layout.setVisibility(View.GONE);
                    card_layout.setVisibility(View.VISIBLE);
                    query.setVisibility(View.GONE);
                    confirm.setVisibility(View.VISIBLE);
                }
            });
            phone_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    query.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.GONE);
                }
            });
            final AlertDialog dialog=new AlertDialog.Builder(getActivity(),R.style.edittext_dialog)
                    .setTitle(R.string.search_vip)
                    .setView(view).show();
            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            query.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNum=phone_edit.getText().toString();
                    if(phoneNum==null||phoneNum.trim().equals("")){
                        ToastUtil.toast(getActivity(),R.string.phone_not_null);
                        return;
                    }
                    CList<Map<String,String>> list=new CList<Map<String, String>>();
                    list.addMap("queryMobTel",phoneNum);
                    new ChineseServer().connect(getActivity(), ChineseConstants.QUERY_CARD_BY_MOBTEL, list, new OnServerResponse() {
                        @Override
                        public void onResponse(String result) {
                            getDialog().dismiss();
                            if(ValueUtil.isEmpty(result)){
                                ToastUtil.toast(getActivity(),R.string.net_error);
                            }else if (result.equals("-1")){
                                ToastUtil.toast(getActivity(),R.string.query_error);
                            }else{
                                String cards[]=result.split("@");
                                card_spinner.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,cards));
                                confirm.setVisibility(View.VISIBLE);
                                query.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onBeforeRequest() {
                            getDialog().show();
                        }
                    });
                }
            });
            confirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cardNum;
                    if(phone_layout.getVisibility()==View.VISIBLE){
                        cardNum= (String) card_spinner.getSelectedItem();
                    }else{
                        cardNum=card_edit.getText().toString();
                    }
                    if(ValueUtil.isEmpty(cardNum)){
                        ToastUtil.toast(getActivity(),R.string.vip_num_not_null);
                    }else{
                        queryCardMsg(cardNum,actm,dialog);
                    }
                }
            });
        }
        public void queryCardMsg(String cardNum, final Actm actm, final AlertDialog dialog){
            CList<Map<String,String>> list=new CList<Map<String, String>>();
            list.addMap("queryCardNo",cardNum);
            new ChineseServer().connect(getActivity(), ChineseConstants.QUERY_CARD_BY_CARDNO, list, new OnServerResponse() {
                @Override
                public void onResponse(String result) {
                    getDialog().dismiss();
                    Log.e("获取的卡信息-QUERY_CARD_BY_CARDNO",result);
                    if(ValueUtil.isEmpty(result)){
                        ToastUtil.toast(getActivity(),R.string.vip_num_not_null);
                    }else{
                        final String card[]=result.split("@");
                        boolean have=false;
                        if(card.length>=13) {
                            String tickets[]=card[13].split("#");
                            for (String ticket:tickets){
                                String ticket_no=ticket.split(",")[0];
                                if(ValueUtil.isNotEmpty(ticket_no)&&ticket_no.equals(actm.getVcode())){
                                    have=true;
                                }
                            }
                            if(have){
                                LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.verify_pwd, null);
                                TextView textView = (TextView) layout.findViewById(R.id.verify_pwd_Text);
                                textView.setText(R.string.vip_pwd);
                                Button confirm = (Button) layout.findViewById(R.id.verify_pwd_confirm);
                                Button cancel = (Button) layout.findViewById(R.id.verify_pwd_cancel);
                                final EditText vip_pwd = (EditText) layout.findViewById(R.id.verify_pwd_Edit);
                                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.edittext_dialog)
                                        .setTitle(R.string.pwd_ver)
                                        .setView(layout).show();
                                confirm.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        actm.setCardTyp(card[6]);
                                        actm.setCardId(card[0]);
                                        actm.setCardNo(card[1]);
                                        actm.setCardPassword(vip_pwd.getText().toString());
                                        isAuth(actm);
                                        dialog.dismiss();
                                        alertDialog.dismiss();
                                    }
                                });
                                cancel.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }else {
                                ToastUtil.toast(getActivity(),R.string.actm_error);
                            }
                        }else{
                            ToastUtil.toast(getActivity(),R.string.actm_error);
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
         * 金额输入
         */
        public void inputAlert(final Actm actm, final IResult<Actm> result){
            View view=getActivity().getLayoutInflater().inflate(R.layout.chinese_alert_discount,null);
            final EditText money= (EditText) view.findViewById(R.id.alert_money);
            final EditText cause= (EditText) view.findViewById(R.id.alert_cause);
            if(actm.getVname().equals("服务费")||actm.getVname().equals("包间费")){
                cause.setVisibility(View.GONE);
            }
            new AlertDialog.Builder(getActivity(),R.style.edittext_dialog)
                    .setTitle(actm.getVname())
                    .setView(view)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            actm.setPaidmoney(money.getText().toString());
                            actm.setCause(cause.getText().toString());
                            result.result(actm);
                        }
                    }).setNegativeButton(R.string.cancle, null).show();
        }

        /**
         * 执行优惠
         *
         * @param actm * user 		用户名
         *          password 	密码
         *          padid		设备编码
         *          order		账单号
         *          actmCode		活动编码
         *          cardId		会员卡编码
         *          cardNo   	会员卡号
         *          cardTyp  	会员卡类别
         *          cardPassword 会员卡密码
         *          ticketCode   券编码
         *          ticketId		券Id
         *          ticketPrice	券金额
         *          ticketCnt    券数量
         *          phoneamt		手动优免金额
         *          Amt			账单金额
         *          phandcnt     手动优免数量
         *          phanddes		手动优免原因
         */
        public void isAuth(Actm actm) {
            String user = SharedPreferencesUtils.getUserCode(getActivity()).split("-")[0];
            String pwd = SharedPreferencesUtils.getUserCode(getActivity()).split("-")[1];
            String amt= ((TextView) getActivity().findViewById(R.id.yingFJE_Text2)).getText().toString();
            CList<Map<String, String>> list = new CList<Map<String, String>>();
            Json json=new Json();
            json.setUser(user);
            json.setPassword(pwd);
            json.setPadid(SharedPreferencesUtils.getDeviceId(getActivity()));
            json.setOrder(getBundle().getString("order"));
            json.setActmCode(actm.getVcode());
            json.setAmt(amt);
            json.setPhoneamt(actm.getPaidmoney());
            json.setPhanddes(actm.getCause());
            json.setCardId(actm.getCardId());
            json.setCardNo(actm.getCardNo());
            json.setCardTyp(actm.getCardTyp());
            json.setCardPassword(actm.getCardPassword());
            String foodStr="";
            if (SingleMenu.getMenuInstance().getAcctFood()!=null){

                for (String str:SingleMenu.getMenuInstance().getAcctFood()){
                    foodStr+=str;
                }
            }
            json.setFood(foodStr);

            list.addMap("json", new Gson().toJson(json));
            new ChineseServer().connect(getActivity(), ChineseConstants.USER_ACTM, list, new OnServerResponse() {
                @Override
                public void onResponse(String result) {
                    getDialog().dismiss();
                    if (ValueUtil.isNotEmpty(result)) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject res = jsonObject.getJSONObject("result");
                            String state = res.getString("state");
                            if (state.equals("1")) {
                                SingleMenu.getMenuInstance().setAcctFood(null);
                                ToastUtil.toast(getActivity(), R.string.execute_success);
                                manager = getFragmentManager();
                                transaction = manager.beginTransaction();
                                ChineseBillFragment bill = new ChineseBillFragment();
                                transaction.replace(R.id.setAccLeftLayout, bill, "AccLeftLayout");
                                transaction.commit();

                            } else {
                                ToastUtil.toast(getActivity(), jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
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

    }
    class Json{
        private String user;
        private String password;
        private String padid;
        private String order;
        private String actmCode;
        private String cardId="0";
        private String cardNo;
        private String cardTyp="0";
        private String cardPassword;
        private String ticketCode;
        private String ticketId;
        private String ticketPrice;
        private String ticketCount="0";
        private String phoneamt="0";
        private String Amt;
        private String phandcnt="1";
        private String phanddes;
        private String food;

        public String getFood() {
            return food;
        }

        public void setFood(String food) {
            this.food = food;
        }
        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPadid() {
            return padid;
        }

        public void setPadid(String padid) {
            this.padid = padid;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getActmCode() {
            return actmCode;
        }

        public void setActmCode(String actmCode) {
            this.actmCode = actmCode;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCardTyp() {
            return cardTyp;
        }

        public void setCardTyp(String cardTyp) {
            this.cardTyp = cardTyp;
        }

        public String getCardPassword() {
            return cardPassword;
        }

        public void setCardPassword(String cardPassword) {
            this.cardPassword = cardPassword;
        }

        public String getTicketCode() {
            return ticketCode;
        }

        public void setTicketCode(String ticketCode) {
            this.ticketCode = ticketCode;
        }

        public String getTicketId() {
            return ticketId;
        }

        public void setTicketId(String ticketId) {
            this.ticketId = ticketId;
        }

        public String getTicketPrice() {
            return ticketPrice;
        }

        public void setTicketPrice(String ticketPrice) {
            this.ticketPrice = ticketPrice;
        }

        public String getTicketCount() {
            return ticketCount;
        }

        public void setTicketCount(String ticketCount) {
            this.ticketCount = ticketCount;
        }

        public String getPhoneamt() {
            return phoneamt;
        }

        public void setPhoneamt(String phoneamt) {
            this.phoneamt = phoneamt;
        }

        public String getAmt() {
            return Amt;
        }

        public void setAmt(String amt) {
            Amt = amt;
        }

        public String getPhandcnt() {
            return phandcnt;
        }

        public void setPhandcnt(String phandcnt) {
            this.phandcnt = phandcnt;
        }

        public String getPhanddes() {
            return phanddes;
        }

        public void setPhanddes(String phanddes) {
            this.phanddes = phanddes;
        }
    }
}
