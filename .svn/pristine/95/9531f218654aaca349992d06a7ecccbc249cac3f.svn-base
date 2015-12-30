package cn.com.choicesoft.chinese.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.chinese.activity.ChineseGuQingActivity;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.chinese.util.JsonUtil;
import cn.com.choicesoft.util.CList;
import cn.com.choicesoft.util.CSLog;
import cn.com.choicesoft.util.OnServerResponse;
import cn.com.choicesoft.util.SharedPreferencesUtils;
import cn.com.choicesoft.util.ToastUtil;
import cn.com.choicesoft.util.ValueUtil;

public class GuQingClick implements View.OnClickListener{
    private ChineseGuQingActivity mActivity;

    public GuQingClick(ChineseGuQingActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
        public void onClick(final View v) {
            switch (v.getId()){
                case R.id.gu_qing_expandlist_item_child_cancel_but:
                    executeGQ(v,null,null,"2");
                    break;
                case R.id.gu_qing_expandlist_item_child_setting_but:
                    LinearLayout linearLayout=new LinearLayout(mActivity);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final EditText cntEdit=new EditText(mActivity);
                    final EditText soldEdit=new EditText(mActivity);
                    cntEdit.setHint(R.string.gu_qing_count);
                    soldEdit.setHint(R.string.gu_qing_sold);
                    linearLayout.addView(cntEdit);
                    linearLayout.addView(soldEdit);
                    new AlertDialog.Builder(mActivity, R.style.edittext_dialog)
                            .setTitle(R.string.gu_qing_setting).setView(linearLayout)
                            .setNegativeButton(R.string.cancle,null)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    executeGQ(v, cntEdit.getText().toString(),soldEdit.getText().toString(), "1");
                                }
                            }).show();
                    break;
            }
        }

        /**
         * 估清操作
         * @param view
         * @param setting 1 执行估清 2取消估清
         * @param cnt 估清数量
         * @param sold 估清提醒数量
         */
        public void executeGQ(View view,String cnt,String sold,String setting){
            Food food= (Food) view.getTag();
            String user[]= SharedPreferencesUtils.getUserCode(mActivity).split("-");
            Map<String,String> map=new HashMap<String, String>();
            map.put("user",user[0]);
            map.put("pass",user.length>0?user[1]:"");
            map.put("setting",setting);
            map.put("cnt", ValueUtil.isEmpty(cnt)?"":cnt);
            map.put("sold", ValueUtil.isEmpty(sold)?"0":sold);
            map.put("itcode",food.getPcode());
            CList list=new CList();
            try {
                list.add("json", JsonUtil.getJson(map));//user pass setting cnt itcode
            } catch (IOException e) {
                CSLog.e("GuQingExpListAdapter", e.getMessage());
            }
            new ChineseServer().connect(mActivity, ChineseConstants.SETESTIMATES_FOOD_LIST, list, new OnServerResponse() {
                @Override
                public void onResponse(String result) {
                    int msgId= R.string.gu_qing_error;
                    try {
                        Map<String,Object> map= JsonUtil.getObject(result, Map.class);
                        if(ValueUtil.isNotEmpty(map)){
                            Integer state=((Map<String,Integer>)map.get("result")).get("state");
                            if(ValueUtil.isNotEmpty(state)&&state==1) {
                                msgId= R.string.gu_qing_success;
                                mActivity.getFoodData();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ToastUtil.toast(mActivity, msgId);
                }
                @Override
                public void onBeforeRequest() {

                }
            });
        }

    }