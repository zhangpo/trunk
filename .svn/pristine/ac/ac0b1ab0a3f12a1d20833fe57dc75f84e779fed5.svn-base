package cn.com.choicesoft.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.renderscript.Sampler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.Eatables;
import cn.com.choicesoft.adapter.ComFujiaGridAdapter;
import cn.com.choicesoft.adapter.FujiaGridAdapter;
import cn.com.choicesoft.bean.Addition;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.bean.FujiaType;
import cn.com.choicesoft.constants.IFujiaResult;
import cn.com.choicesoft.util.ListProcessor;
import cn.com.choicesoft.util.ToastUtil;
import cn.com.choicesoft.util.ValueUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 普通附加项界面
 * Created by M.c on 2014/8/29.
 */
public class ComFujiaView implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Activity activity;
    private ComFujiaGridAdapter subGridAdapter;
    private LinearLayout linearTCDetails;//套餐详情的内布局
    private Food food;
    private FujiaType type;//附加类别
    private Dialog dialog;
    private Button confirm,remove;//确定，删除
    private boolean clickDel=false;//删除标识
    private IFujiaResult mIfujiaResult;//回调接口
    private Map<String,Addition> additionMap;

    public ComFujiaView(Activity activity, Food food, FujiaType type,IFujiaResult result) {
        this.activity = activity;
        this.food = food;
        this.type = type;
        mIfujiaResult=result;
    }
    public ComFujiaView(Activity activity, Food food, FujiaType type) {
        this.activity = activity;
        this.food = food;
        this.type = type;
    }
    public ComFujiaView(Activity activity, Food food) {
        this.activity = activity;
        this.food = food;
        this.type=new FujiaType();
    }

    public void showView(List<Addition> additionList){
        if(additionList==null||additionList.size()<=0){
            ToastUtil.toast(activity,R.string.fujia_null);
            return;
        }
        LinearLayout layout=(LinearLayout)activity.getLayoutInflater().inflate(R.layout.must_fujia_layout,null);
        confirm=(Button)layout.findViewById(R.id.eatable_tcdetail_btn_certain);
        remove=(Button) layout.findViewById(R.id.eatable_tcdetail_btn_cancel);
        confirm.setOnClickListener(this);
        remove.setOnClickListener(this);
        linearTCDetails=(LinearLayout)layout.findViewById(R.id.eatable_linear_tcdetails);
        dialog = new Dialog(activity,R.style.edittext_dialog);
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(false);
        iniData(additionList);
        dialog.show();
    }
    public void iniData(List<Addition> additionList){
            if(additionList.size() > 0) {
                if(ValueUtil.isNotEmpty(food.getComfujiacode())){
                    additionMap=new HashMap<String, Addition>();
                    String [] codes=food.getComfujiacode().split("!");
                    String [] counts=food.getComfujiacount().split("!");
                    String [] prices=food.getComfujiaprice().split("!");
                    String [] names=food.getComfujianame().split("!");
                    if(ValueUtil.isNotEmpty(codes)&&codes.length==counts.length){
                        for(int i=0;i<codes.length;i++){
                            if(ValueUtil.isEmpty(additionMap.get(codes[i]))) {
                                Addition ad = new Addition();
                                ad.setFoodFuJia_checked(prices[i]);
                                ad.setFoodFuJia_des(names[i]);
                                ad.setPcount(counts[i]);
                                ad.setFoodFuJia_Id(codes[i]);
                                additionMap.put(codes[i], ad);
                            }
                            for(Addition addition:additionList){
                                if(addition.getFoodFuJia_Id().equals(codes[i])){
                                    addition.setPcount(counts[i]);
                                    addition.setSelected(true);
                                    additionMap.remove(codes[i]);
                                }
                            }
                        }
                    }
                }
                subGridAdapter = new ComFujiaGridAdapter(activity, additionList);

                LinearLayout tcTopGroupLayout = new LinearLayout(activity);
                tcTopGroupLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tcTopGroupLayout.setOrientation(LinearLayout.HORIZONTAL);
                // 附加项明细 ↓↓↓
                TcGridView tcGridView = new TcGridView(activity);
                if(ChioceActivity.ispad) {
                    tcGridView.setNumColumns(5);
                }else{
                    tcGridView.setNumColumns(3);
                }
                tcGridView.setDrawSelectorOnTop(true);
                tcGridView.setVerticalSpacing(5);
                tcGridView.setHorizontalSpacing(5);
                tcGridView.setBackgroundColor(Color.parseColor("#ADD8E6"));
                tcGridView.setSelector(R.color.transparent);
                tcGridView.setAdapter(subGridAdapter);
                tcGridView.setOnItemClickListener(this);
                // 附加项分组信息↓↓↓
                TextView tcGroupTv = new TextView(activity);
                tcGroupTv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tcGroupTv.setTextColor(Color.BLACK);
                tcGroupTv.setText(ValueUtil.isNotEmpty(type.getName())?type.getName()+"    ":"   ");//附加项分组名

                ImageView mImageView = new ImageView(activity);
                mImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                mImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));

                tcTopGroupLayout.addView(tcGroupTv);

                linearTCDetails.addView(tcTopGroupLayout);
                linearTCDetails.addView(tcGridView);
                linearTCDetails.addView(mImageView);
            }else{
                ToastUtil.toast(activity,R.string.fujia_type_null);
                if(dialog!=null)dialog.dismiss();
            }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ComFujiaGridAdapter adapter=(ComFujiaGridAdapter)parent.getAdapter();
        Addition posA= (Addition) adapter.getItem(position);//点击的附加项
        if(clickDel){//删除按钮事件
            posA.setPcount("0");
            posA.setSelected(false);
            adapter.notifyDataSetChanged();
            clickDel=false;
            return;
        }
        posA.setPcount((ValueUtil.isNaNofInteger(posA.getPcount()) + 1) + "");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.eatable_tcdetail_btn_certain:
                certain();
                break;
            case R.id.eatable_tcdetail_btn_cancel:
                clickDel=true;
                break;
        }
    }
    public void certain(){
        StringBuffer fName=new StringBuffer();
        StringBuffer fPrice=new StringBuffer();
        StringBuffer fcode=new StringBuffer();
        StringBuffer fcount=new StringBuffer();
        for(Addition addition:subGridAdapter.getSelectedAdditionLists()){
            fName.append(addition.getFoodFuJia_des()).append("!");
            fPrice.append(addition.getFoodFuJia_checked()).append("!");
            fcode.append(addition.getFoodFuJia_Id()).append("!");
            fcount.append(addition.getPcount()).append("!");
        }
        if(ValueUtil.isNotEmpty(additionMap)){
            for (Map.Entry<String, Addition> additionEntry:additionMap.entrySet()){
                fName.append(additionEntry.getValue().getFoodFuJia_des()).append("!");
                fPrice.append(additionEntry.getValue().getFoodFuJia_checked()).append("!");
                fcode.append(additionEntry.getValue().getFoodFuJia_Id()).append("!");
                fcount.append(additionEntry.getValue().getPcount()).append("!");
            }
        }
        food.setComfujiaprice(fPrice.toString());
        food.setComfujiacode(fcode.toString());
        food.setComfujianame(fName.toString());
        food.setComfujiacount(fcount.toString());
        if(dialog!=null){
            dialog.dismiss();
        }
        if(mIfujiaResult!=null){
            mIfujiaResult.result();
        }
    }
}
