package cn.com.choicesoft.chinese.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.chinese.bean.TicketData;
import cn.com.choicesoft.chinese.bean.VipData;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 中餐支付界面
 * Created by M.c on 2014/9/11.
 */
public class ChineseVipPayment extends Activity implements View.OnClickListener {
    private Button jf, ye, xj,back,confirm;//计算积分、余额、现金，返回，支付
    private EditText kyyue,kyjifen,yexf,jfxf,xjxf;//可用余额、积分 消费余额、积分、现金
    private TextView yingfu,order,table,people,user;//应付金额 账单号 台位号 人数 操作员
    private VipData vipData;
    private Bundle bundle;    private LoadingDialog dialog;
    public LoadingDialog getDialog() {
        if(dialog==null){
            dialog = new LoadingDialogStyle(this,this.getString((R.string.please_wait)));
            dialog.setCancelable(true);
        }
        return dialog;
    }

    public Bundle getBundle() {
        if(bundle==null){
            return new Bundle();
        }
        return bundle;
    }

    public VipData getVipData() {
        if(vipData==null){
            return new VipData();
        }
        return vipData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinese_vip_payment);
        iniView();
        iniData();
    }
    private void iniView(){
        jf =(Button)findViewById(R.id.chinese_vip_pay_jisuanjifen);
        ye =(Button)findViewById(R.id.chinese_vip_pay_jisuanyue);
        xj =(Button)findViewById(R.id.chinese_vip_pay_jisuanxianjin);
        back=(Button)findViewById(R.id.chinese_vip_pay_back);
        confirm=(Button)findViewById(R.id.chinese_vip_pay_confirm);
        kyyue=(EditText)findViewById(R.id.chinese_vip_pay_chuzhikeyong);
        kyjifen=(EditText)findViewById(R.id.chinese_vip_pay_jifenkeyong);
        yexf=(EditText)findViewById(R.id.chinese_vip_pay_chuzhixiaofei);
        jfxf=(EditText)findViewById(R.id.chinese_vip_pay_jifenxiaofei);
        xjxf=(EditText)findViewById(R.id.chinese_vip_pay_xianjinxiaofei);
        yingfu=(TextView)findViewById(R.id.chinese_vip_pay_yingFText);
        order=(TextView)findViewById(R.id.vip_orderText1);
        table=(TextView)findViewById(R.id.vip_taiweiText1);
        people=(TextView)findViewById(R.id.vip_peopleNumber);
        user=(TextView)findViewById(R.id.operatorNames);
    }
    private void iniData(){
        vipData=(VipData)getIntent().getSerializableExtra("VIP");
        bundle=getIntent().getBundleExtra("Message");
        order.setText(getBundle().getString("order"));
        table.setText(getBundle().getString("table"));
        people.setText(getBundle().getString("people"));
        user.setText(getBundle().getString("user"));
        yingfu.setText(getBundle().getString("money"));
        yingfu.setTag(getBundle().getString("money"));
        kyyue.setText(getVipData().getCardYuE());
        kyjifen.setText(getVipData().getJiFenYuE());
        jf.setOnClickListener(this);
        ye.setOnClickListener(this);
        xj.setOnClickListener(this);
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        //===============================================================
        jfxf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                BigDecimal jfky=ValueUtil.isNaNofBigDecimal(kyjifen.getText().toString());
                BigDecimal jifxf=ValueUtil.isNaNofBigDecimal(jfxf.getText().toString());
                BigDecimal yf=ValueUtil.isNaNofBigDecimal(yingfu.getText().toString());
                if(jfky.compareTo(jifxf)<0||yf.compareTo(jifxf)<0){
                    s.delete(s.length()>0?s.length()-1:0,s.length());
                    Toast.makeText(ChineseVipPayment.this, R.string.pay_money_error4, Toast.LENGTH_LONG).show();
                    return;
                }
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
        yexf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                BigDecimal yeky=ValueUtil.isNaNofBigDecimal(kyyue.getText().toString());
                BigDecimal czxf=ValueUtil.isNaNofBigDecimal(yexf.getText().toString());
                BigDecimal yf=ValueUtil.isNaNofBigDecimal(yingfu.getText().toString());
                if(yeky.compareTo(czxf)<0||yf.compareTo(czxf)<0){//判断可用是否大于消费
                    s.delete(s.length()>0?s.length()-1:0,s.length());
                    Toast.makeText(ChineseVipPayment.this,R.string.pay_money_error3, Toast.LENGTH_LONG).show();
                    return;
                }
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
        xjxf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
        //==============================================================
        List<TicketData> ticketList=getVipData().getList();
        if(ValueUtil.isNotEmpty(ticketList)){//遍历券信息，显示在ListView里
            ScrollView scrollView=(ScrollView)this.findViewById(R.id.chinese_vip_pay_scrollView);
            LinearLayout linearLayout=(LinearLayout)scrollView.findViewById(R.id.chinese_vip_pay_couponButtonLy);
            linearLayout.setGravity(Gravity.LEFT|Gravity.CENTER);
            LinearLayout couponBut=new LinearLayout(this);
            couponBut.setGravity(Gravity.LEFT|Gravity.CENTER);
            for (TicketData ticketData:ticketList) {//遍历优惠券
                Button but=new Button(this);
                but.setText(ticketData.getTicketName());
                but.setTag(ticketData);
                but.setTextSize(10);
                but.setTextColor(Color.WHITE);
                but.setOnClickListener(new TicketClick());
                but.setBackgroundResource(R.drawable.but_centre_bg);
                couponBut.addView(but);
                if(couponBut.getChildCount()==4){
                    linearLayout.addView(couponBut);
                    couponBut=new LinearLayout(this);
                    couponBut.setGravity(Gravity.LEFT|Gravity.CENTER);
                }
            }
            if(couponBut.getChildCount()>0){
                linearLayout.addView(couponBut);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chinese_vip_pay_jisuanjifen:
                jiSuan(2);
                break;
            case R.id.chinese_vip_pay_jisuanxianjin:
                jiSuan(3);
                break;
            case R.id.chinese_vip_pay_jisuanyue:
                jiSuan(1);
                break;
            case R.id.chinese_vip_pay_back:
                finish();
                break;
            case R.id.chinese_vip_pay_confirm:

                break;
            default:
                break;
        }
    }

    /**
     * 金额计算
     * @param key
     */
    private void jiSuan(int key){
        BigDecimal yf=ValueUtil.isNaNofBigDecimal(this.yingfu.getText().toString());
        BigDecimal kyye=ValueUtil.isNaNofBigDecimal(this.kyyue.getText().toString());
        BigDecimal kyjf=ValueUtil.isNaNofBigDecimal(this.kyjifen.getText().toString());
        BigDecimal yexf=ValueUtil.isNaNofBigDecimal(this.yexf.getText().toString());
        BigDecimal jfxf=ValueUtil.isNaNofBigDecimal(this.jfxf.getText().toString());
        BigDecimal xjxf=ValueUtil.isNaNofBigDecimal(this.xjxf.getText().toString());
        if(jfxf.add(yexf).compareTo(yf)>0){
            this.yexf.setText("0");//储值可用
            this.jfxf.setText("0");//积分可用
            Toast.makeText(this, R.string.pay_money_error2, Toast.LENGTH_LONG).show();
            return;
        }
        switch(key){
            case 1://储值计算
                BigDecimal chuZYF=new BigDecimal(0);
                if(xjxf.add(jfxf).compareTo(yf)<0){
                    chuZYF=yf.subtract(xjxf.add(jfxf));
                }
                if(kyye.compareTo(chuZYF)>=0){
                    this.yexf.setText(chuZYF.toString());//储值消费
                }else{
                    this.yexf.setText(kyye.toString());//储值消费
                }
                break;
            case 2://积分计算
                BigDecimal jiFYF=new BigDecimal(0);
                if(yexf.add(xjxf).compareTo(yf)<0){
                    jiFYF=yf.subtract(yexf.add(xjxf));
                }
                if(kyjf.compareTo(jiFYF)>=0){
                    this.jfxf.setText(jiFYF.toString());//积分消费
                }else{
                    this.jfxf.setText(kyjf.toString());//积分消费
                }
                break;
            default://现金计算
                BigDecimal xianJYF=yf.subtract(yexf.add(jfxf));
                this.xjxf.setText(xianJYF.toString());
                break;
        }

    }
    /**
     * 优惠券 点击事件
     */
    class TicketClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            View view = ChineseVipPayment.this.getLayoutInflater().inflate(R.layout.pay_ver_layout, null);
            final AlertDialog alert= new AlertDialog.Builder(ChineseVipPayment.this).setTitle(R.string.pwd_input).setView(view).show();
            Button cancelBut=(Button)view.findViewById(R.id.pay_ver_cancelBut);
            cancelBut.setOnClickListener(this);
            Button okBut=(Button)view.findViewById(R.id.pay_ver_okBut);
            final EditText pwd=(EditText)view.findViewById(R.id.pay_ver_PasEdit);
            okBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* cardNo 卡号
                     firmId 门店编号
                     empNo 操作员编号
                     empName 操作员姓名
                     dateTime 消费时间
                     couponCode 券编码
                     password 密码
                     serial*/
                    CList list=new CList();
                    list.add("cardNo",vipData.getCardNumber());
                    list.add("firmId","1");
                    list.add("empNo",SharedPreferencesUtils.getUserCode(ChineseVipPayment.this));
                    list.add("empName", SharedPreferencesUtils.getOperator(ChineseVipPayment.this));
                    list.add("dateTime", DateFormat.getStringByDate(new Date(), "yyyy-MM-dd mm:ss;hh"));
                    list.add("couponCode",((TicketData)v.getTag()).getTicketId());
                    list.add("password",pwd.getText().toString());
                    list.add("serial","");
                    new ChineseServer().connect(ChineseVipPayment.this,ChineseConstants.CARDOUTCOUPON,list,new OnServerResponse() {
                        @Override
                        public void onResponse(String result) {
                            getDialog().dismiss();
                            Log.e("---------",result);
                        }
                        @Override
                        public void onBeforeRequest() {
                            getDialog().show();
                        }
                    });
                }
            });
         }
    }
}
