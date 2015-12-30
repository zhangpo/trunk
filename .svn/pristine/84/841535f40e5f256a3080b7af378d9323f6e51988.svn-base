package cn.com.choicesoft.chinese.events;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.chinese.activity.ChineseVipMsg;
import cn.com.choicesoft.chinese.bean.TicketData;
import cn.com.choicesoft.chinese.bean.VipData;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.fragment.ChineseBillFragment;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.chinese.util.ChineseVipUtil;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中餐会员选择
 * Created by M.c on 2014/9/9.
 */
public class ChineseVipChoice implements View.OnClickListener {
    private String TAG="ChineseVipChoice";
    private LinearLayout vipLayout;
    private Activity activity;
    private LoadingDialog dialog;
    public LoadingDialog getDialog() {
        if(dialog==null){
            dialog = new LoadingDialogStyle(activity, activity.getString((R.string.please_wait)));
            dialog.setCancelable(true);
        }
        return dialog;
    }
    public ChineseVipChoice(Activity activity,LinearLayout vipLayout) {
        this.vipLayout = vipLayout;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chinese_vip_confirm://确定事件
                vipConfirm();
                break;
            case R.id.chinese_vip_cancel://取消事件
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    }
                }).start();
                break;
            case R.id.chinese_vip_query://查询事件
                queryVip();
                break;
            default:
                break;
        }
    }

    /**
     * 会员号查询
     */
    public void queryVip(){
        EditText phone=(EditText)vipLayout.findViewById(R.id.chinese_vip_phone);
        if(!phone.getText().toString().matches("^[1][34578][0-9]{9}$")){
            ToastUtil.toast(activity,R.string.phone_error);
            return ;
        }
        CList list=new CList();
        list.add("queryMobTel",phone.getText().toString());
        new ChineseServer().connect(activity, ChineseConstants.QUERYCARDBYMOBTEL,list,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getDialog().dismiss();
                if(ValueUtil.isEmpty(result)||result.equals("-1")){
                    ToastUtil.toast(activity,R.string.query_null);
                    return;
                }else if(result.equals("-2")){
                    ToastUtil.toast(activity,R.string.query_error);
                    return;
                }
                String[] rs=result.split("@");
                Spinner spinner=(Spinner)vipLayout.findViewById(R.id.chinese_vip_cardNo_spinner);
                spinner.setAdapter(new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,rs));
                spinner.setVisibility(View.VISIBLE);
                vipLayout.findViewById(R.id.chinese_vip_query).setVisibility(View.GONE);
                vipLayout.findViewById(R.id.chinese_vip_confirm).setVisibility(View.VISIBLE);
            }
            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });
    }

    /**
     * 获取会员信息
     */
    public void vipConfirm(){
        Spinner spinner=(Spinner)vipLayout.findViewById(R.id.chinese_vip_cardNo_spinner);
        EditText vipNo=(EditText)vipLayout.findViewById(R.id.chinese_vip_number);
        String vipNum="";
        if(ValueUtil.isNotEmpty(vipNo.getText())){
            vipNum=vipNo.getText().toString();
        }else if (ValueUtil.isNotEmpty(spinner.getSelectedItem())) {
            vipNum = spinner.getSelectedItem().toString();
        }
        CList list=new CList();
        list.add("queryCardNo",vipNum);
        new ChineseServer().connect(activity, ChineseConstants.QUERYCARDBYCARDNO,list,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getDialog().dismiss();
                if(ValueUtil.isEmpty(result)||result.equals("-1")){
                    ToastUtil.toast(activity, R.string.query_null);
                    return;
                }else if(result.equals("-2")){
                    ToastUtil.toast(activity, R.string.query_error);
                    return;
                }
                Bundle bundle=new Bundle();
                bundle.putString("order",activity.getIntent().getStringExtra("order"));
                bundle.putString("table",activity.getIntent().getStringExtra("table"));
                bundle.putString("people", ChineseBillFragment.people);
                bundle.putString("user",SharedPreferencesUtils.getOperator(activity));
                bundle.putString("money",((TextView)activity.findViewById(R.id.yingFJE_Text2)).getText().toString());
                activity.startActivity(new Intent(activity, ChineseVipMsg.class).putExtra("VIP",new ChineseVipUtil().getVipDataByXml(result)).putExtra("Message",bundle));
            }
            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });
    }
}
