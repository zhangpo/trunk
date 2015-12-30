package cn.com.choicesoft.chinese.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.chinese.bean.TicketData;
import cn.com.choicesoft.chinese.bean.VipData;
import cn.com.choicesoft.util.CSLog;
import cn.com.choicesoft.util.ToastUtil;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员信息显示
 * Created by M.c on 2014/9/10.
 */
public class ChineseVipMsg extends Activity implements View.OnClickListener {
    private TextView number, yu_e,jifen,yingfu,order,table,people,user;
    private Button confirm,cancel;
    private ListView listView;
    private Bundle bundle;
    private VipData vipData;
    private LoadingDialog dialog;
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
    public VipData getVipDate(){
        if(vipData==null){
            return new VipData();
        }
        return vipData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinese_vip_msg);
        bundle=this.getIntent().getBundleExtra("Message");
        iniView();
        iniData();
    }
    private void iniView(){
        order=(TextView)findViewById(R.id.vip_orderText1);
        table=(TextView)findViewById(R.id.vip_taiweiText1);
        people=(TextView)findViewById(R.id.vip_peopleNumber);
        user=(TextView)findViewById(R.id.operatorNames);
        //-------------------------------------------------------------
        number=(TextView)findViewById(R.id.chinese_vipmsg_number);
        yu_e =(TextView)findViewById(R.id.chinese_vipmsg_yue);
        jifen=(TextView)findViewById(R.id.chinese_vipmsg_jifen_yue);
        yingfu=(TextView)findViewById(R.id.chinese_vipmsg_yingFText1);
        confirm=(Button)findViewById(R.id.chinese_vipmsg_confirm);
        cancel=(Button)findViewById(R.id.chinese_vipmsg_cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        listView =(ListView)findViewById(R.id.chinese_vipmsg_couponListView);
    }
    private void iniData(){
        order.setText(getBundle().getString("order"));
        table.setText(getBundle().getString("table"));
        people.setText(getBundle().getString("people"));
        user.setText(getBundle().getString("user"));
        yingfu.setText(getBundle().getString("money"));
        vipData=(VipData)getIntent().getSerializableExtra("VIP");
        getVipData();
    }

    /**
     * 通过接口获取会员信息
     */
    private void getVipData(){
        try {
            number.setText(getVipDate().getCardNumber());
            jifen.setText(getVipDate().getJiFenYuE());
            yu_e.setText(getVipDate().getCardYuE());
            List<TicketData> dataList=getVipDate().getList();
            if(dataList!=null){
                List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
                for(TicketData ticketData:dataList){
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("ticketId",ticketData.getTicketId());
                    map.put("ticketName",ticketData.getTicketName());
                    map.put("ticketMoney",ticketData.getMianE());
                    mapList.add(map);
                }
                SimpleAdapter adapter=new SimpleAdapter(ChineseVipMsg.this, mapList, R.layout.chinese_vip_coupon_list_item,
                        new String[]{"ticketId","ticketName","ticketMoney"},
                        new int[]{R.id.vip_ticketId,R.id.vip_ticketName,R.id.vip_ticketMoney});
                listView.setAdapter(adapter);
            }
        }catch (NullPointerException e){
            CSLog.e("ChineseVipMsg",e.getMessage());
            ToastUtil.toast(ChineseVipMsg.this,R.string.data_error);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chinese_vipmsg_confirm:
                startActivity(new Intent(this, ChineseVipPayment.class).putExtra("Message", getBundle()).putExtra("VIP", getVipDate()));
                finish();
                break;
            case R.id.chinese_vipmsg_cancel:
                finish();
                break;
            default:
                break;
        }
    }
}