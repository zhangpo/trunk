package cn.com.choicesoft.activity.action;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.bean.Storetable;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.singletaiwei.YiXuanDishActivity2;
import cn.com.choicesoft.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同台位多账单显示
 * Created by mc on 14-12-22.
 */
public class CombineTableListView {
    private Activity activity;
    private Storetable storetable;

    public CombineTableListView(Activity activity, Storetable storetable) {
        this.activity = activity;
        this.storetable = storetable;
    }

    /**
     * 根据台位号获取账单
     * @param iResult
     */
    public void isCombineTable(final IResult iResult){
        CList<Map<String, String>> clists = new CList<Map<String,String>>();
        clists.add("deviceId", SharedPreferencesUtils.getDeviceId(activity));
        clists.add("userCode", SharedPreferencesUtils.getUserCode(activity));
        clists.add("tableNum", storetable.getTablenum());
        new Server().connect(activity, Constants.FastMethodName.GetOrdersBytabNum_METHODNAME, Constants.FastWebService.getOrdersBytabNum_WSDL, clists, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                if(ValueUtil.isNotEmpty(result)){
                    try {
                        String rt[]=result.split("@");
                        if(rt!=null&&rt.length>=2){
                            if(!"0".equals(rt[0])){
                                ToastUtil.toast(activity,rt[1]);
                                return;
                            }
                        }
                        final List<Map<String,Object>> list=AnalyticalXmlUtil.orderMsg(result);
                        if(list!=null){
                            if(list.size()>1){
                                String[] items=new String[list.size()];
                                for(int i=0;i<list.size();i++){
                                    items[i]=list.get(i).get("tableName").toString();
                                }
                                ListAdapter listAdapter=new ArrayAdapter(activity, R.layout.dia_ifsave_item_layout,R.id.dia_ifsave_item_tv,items);
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Map<String,Object> map=list.get(which);
                                        if(ValueUtil.isNotEmpty(map)){
                                            if(map.get("tableState").toString().trim().equals("1")){
                                                ToastUtil.toast(activity,R.string.table_occupy);
                                            }else if(map.get("fengtaiyn").toString().trim().equals("1")){
                                                ToastUtil.toast(activity,R.string.feng_tai);
                                            }else{
                                                ((MainActivity)activity).checkOrderBytabnum(list.get(which),YiXuanDishActivity2.class);
                                                ((MainActivity)activity).getGuestLists().clear();//清空singleton集合
                                            }
                                        }
                                    }
                                }).create().show();
                            }else if(list.size()==1){
                                if(list.get(0)!=null) {
                                    list.get(0).put("tableName",storetable.getTablenum());
                                }
                                iResult.result(list.get(0));
                            }
                        }
                    } catch (Exception e) {
                        Log.e("CombineTableListView","根据台位号获取账单出错！");
                        iResult.result(null);
                    }
                }
            }
            @Override
            public void onBeforeRequest() {

            }
        });
    }
}
