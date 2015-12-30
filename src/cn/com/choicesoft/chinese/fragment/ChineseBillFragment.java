package cn.com.choicesoft.chinese.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.adapter.BillAdapter;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseResultAlt;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.chinese.util.ChineseViewUtil;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账单Fragment
 * @Author:M.c
 * @CreateDate:2014-1-6
 * @Email:JNWSCZH@163.COM
 */
public class ChineseBillFragment extends Fragment {
    private LoadingDialog dialog;
    private String tableNumber;
    private String orderNumber;
    private BillAdapter adapter;
    private List<Map<String,String>>foodList;
    public static String  people;

    public LoadingDialog getDialog() {
        if(dialog==null){
            dialog = new LoadingDialogStyle(getActivity(), this.getString(R.string.please_wait));
            dialog.setCancelable(true);
        }
        return dialog;
    }
    public Server getServer() {
        Server server=new Server();
        return server;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tableNumber=getActivity().getIntent().getStringExtra("table");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.chinese_bill_fragment, null);
        final ListView listView=(ListView)view.findViewById(R.id.BillListView);
		/*pQuery*/
        CList<Map<String,String>> cList=new CList<Map<String, String>>();
        cList.add("pdaid",SharedPreferencesUtils.getDeviceId(getActivity()));
        cList.add("user",SharedPreferencesUtils.getUserCode(getActivity()));
        cList.add("pass","");
        cList.add("tblInit",tableNumber);
        cList.add("irecno", SingleMenu.getMenuInstance().getMenuOrder());
        cList.add("buffer","");
        cList.add("orecno", "");
        new ChineseServer().connect(getActivity(), ChineseConstants.PQUERY, cList, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getDialog().dismiss();
                if (ValueUtil.isNotEmpty(result)) {
                    Map<String, String> map = new ComXmlUtil().xml2Map(result);
                    if (ValueUtil.isEmpty(map)) {
                        ToastUtil.toast(getActivity(), R.string.data_error);
                        return;
                    }
                    if ("1".equals(map.get("result"))) {
                        HashMap<String, Object> order = (HashMap) ChineseResultAlt.mapAlt(map.get("oStr"));
                        if (ValueUtil.isEmpty(order)) {
                            ToastUtil.toast(getActivity(), R.string.data_error);
                            return;
                        }
                        //===================================================================
                        orderNumber = order.get("order").toString();
                        people = order.get("people").toString();
                        foodList = (List<Map<String, String>>) order.get("food");
                        queryPayments(orderNumber);
                        //===================================================================
                        adapter = new BillAdapter(getActivity(), foodList);
                        showText(view, order);
                        listView.setAdapter(adapter);
                    } else {
                        ToastUtil.toast(getActivity(), R.string.data_error);
                    }
                } else {
                    ToastUtil.toast(getActivity(), R.string.data_null);
                }
            }

            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List<String> acctFood=SingleMenu.getMenuInstance().getAcctFood()==null?new ArrayList<String>():SingleMenu.getMenuInstance().getAcctFood();
                Map<String,String>foodMap=foodList.get(position);
                boolean select=false;
                for (String foodStr:acctFood){
                    if (foodMap.get("foodid").equals(foodStr)){
                        acctFood.remove(foodStr);
                        select=true;
                        foodMap.put("SELECT","0");
                        view.setBackgroundColor(Color.LTGRAY);
//                        view.setSelected(false);
                        break;
                    }
                }
                if (select==false){
                    acctFood.add(foodMap.get("foodid"));
                    foodMap.put("SELECT","1");
                    view.setBackgroundColor(Color.WHITE);
                }
                SingleMenu.getMenuInstance().setAcctFood(acctFood);
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
    }


    public void queryPayments(String order){
        CList list=new CList();
        list.add("pdaid",SharedPreferencesUtils.getDeviceId(getActivity()));
        list.add("user",SharedPreferencesUtils.getUserCode(getActivity()));
        list.add("folioNo",order);
        new ChineseServer().connect(getActivity(),ChineseConstants.QUERYPAYMENTS,list,new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getDialog().dismiss();
                if (ValueUtil.isNotEmpty(result)) {
                    Map<String, String> map = new ComXmlUtil().xml2Map(result);
                    if (ValueUtil.isEmpty(map)) {
                        ToastUtil.toast(getActivity(), R.string.payment_error);
                        return;
                    }
                    if ("1".equals(map.get("result"))) {
                        String oStr=ChineseResultAlt.oStrAlt(map.get("oStr"));
                        for(String str:oStr.split("\\^")){
                            if(str.indexOf("@")>0){
                                String []pay=str.split("@");
                                ChineseViewUtil.setVew(getActivity(),pay[0],pay[1]);
                            }
                        }
                    }
                } else {
                    ToastUtil.toast(getActivity(), R.string.payment_null);
                }
            }
            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });

    }

    /**
     * 计算显示：计金额。。
     * @param view
     * @param result 账单信息合计
     */
    @SuppressWarnings("unchecked")
    public void showText(View view,Map<String,Object> result){
        BigDecimal heJ;		//合计金额
        BigDecimal yingF;
        BigDecimal zheK=BigDecimal.valueOf(ValueUtil.isNaNofDouble(result.get("折扣金额").toString()));
        BigDecimal fuWu=BigDecimal.valueOf(ValueUtil.isNaNofDouble(result.get("服务费").toString()));
        BigDecimal baoJian=BigDecimal.valueOf(ValueUtil.isNaNofDouble(result.get("包间金额").toString()));
        BigDecimal moL=BigDecimal.valueOf(ValueUtil.isNaNofDouble(result.get("抹零金额").toString()));
        BigDecimal mianX=BigDecimal.valueOf(ValueUtil.isNaNofDouble(result.get("免项").toString()));

        TextView hejText=(TextView)view.findViewById(R.id.heJJE_Text2);
        TextView molText=(TextView)view.findViewById(R.id.moLJE_Text2);
        TextView yingfText=(TextView)view.findViewById(R.id.yingFJE_Text2);
        TextView zheKText=(TextView)view.findViewById(R.id.zheKJE_Text2);
        TextView fuWufText=(TextView)view.findViewById(R.id.fuWuJE_Text2);
        TextView baoJfText=(TextView)view.findViewById(R.id.baoJianJE_Text2);
        TextView mianXfText=(TextView)view.findViewById(R.id.mianXJE_Text2);//免项
        zheKText.setText(zheK.toString());
        fuWufText.setText(fuWu.toString());
        baoJfText.setText(baoJian.toString());
        mianXfText.setText(mianX.toString());
        heJ=BigDecimal.valueOf(ValueUtil.isNaNofDouble(ValueUtil.isEmpty(result.get("money")) ? "0" : result.get("money").toString()));//.add(moL);
        yingF=heJ.subtract(BigDecimal.valueOf(ValueUtil.isNaNofDouble(result.get("结算金额").toString()))).add(zheK).add(fuWu).add(mianX).add(baoJian);
        String isMol=SharedPreferencesUtils.getMoL(getActivity());
        Double molNum=0.00;
        if(isMol!=null&&isMol.length()>0){
            Double zeroNum=1-Double.parseDouble(isMol);
            double   f   =  Math.floor(yingF.doubleValue()+zeroNum);//应付金额
            double   f2   =  yingF.doubleValue()-f;
            yingF=new  BigDecimal(f);
            BigDecimal   b   =   new   BigDecimal(f2);
//            zAmt=f;
            molNum  = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        molText.setText(molNum+"");
        hejText.setText(heJ.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        yingfText.setText(yingF.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
    }
}
