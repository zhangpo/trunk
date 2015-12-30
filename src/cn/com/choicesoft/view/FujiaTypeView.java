package cn.com.choicesoft.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Addition;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.bean.FujiaType;
import cn.com.choicesoft.constants.IFujiaResult;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.util.ListProcessor;
import cn.com.choicesoft.util.ValueUtil;

import java.util.List;

/**
 * 附加项类别显示
 * Created by M.c on 2014/9/1.
 */
public class FujiaTypeView {
    private Activity activity;
    private AlertDialog typDialog;
    private IResult<FujiaType> mIResult;

    public FujiaTypeView(Activity activity, IResult<FujiaType> mIResult) {
        this.activity = activity;
        this.mIResult = mIResult;
    }

    public void showView(final Food pFood){
        if(pFood == null){
            Toast.makeText(activity, R.string.you_not_choice_food, Toast.LENGTH_SHORT).show();
            return;
        }else if(pFood.getPcode().equals(pFood.getTpcode())){//套餐是不能添加附加项的
            Toast.makeText(activity, R.string.meal_not_add_items, Toast.LENGTH_SHORT).show();
            return;
        }
        List<FujiaType> list;
        //从本地库获取支付方式
        list = new ListProcessor().query("select name,code,pk_redefine_type,sortno from redefine_type", null, activity, FujiaType.class);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        //通过infalte获取popupWindow界面
        View popupWindow = layoutInflater.inflate(R.layout.custom_popup_window, null);
        //创建弹出框
        typDialog = new AlertDialog.Builder(activity,R.style.edittext_dialog).setView(popupWindow).show();
        LinearLayout linearLayout=(LinearLayout)popupWindow.findViewById(R.id.popupWind);
        linearLayout.setPadding(15, 25, 15, 25);
        DisplayMetrics dmm = new DisplayMetrics();
        typDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dmm);
        int dialogWidth  =  dmm.widthPixels;
        LinearLayout dLin=new LinearLayout(activity);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
        LinearLayout.LayoutParams linearlayoutlp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dLin.setLayoutParams(linearlayoutlp);
        params.setMargins(3, 3, 3, 3);
        /**
         * 根据查询出来的循环遍历but
         */
        for (final FujiaType type:list) {
            Button but=new Button(activity);
            but.setTextColor(Color.WHITE);
            but.setText(type.getName());
            but.setTag(type);
            but.setTextSize(14);
            but.setBackgroundResource(R.drawable.blue_button_background);
            but.setOnClickListener(new View.OnClickListener() {//为but添加事件
                @Override
                public void onClick(final View paramView) {
                    if(ValueUtil.isEmpty(paramView.getTag())){//判断but的Tag是否为空Tag存有优惠信息
                        return;
                    }
                    if(mIResult!=null){
                        mIResult.result(type);
                    }
                }
            });
            dLin.addView(but,params);
            if(dLin.getChildCount()==3){
                linearLayout.addView(dLin);
                dLin=new LinearLayout(activity);
            }
        }
        if(dLin.getChildCount()>0){
            linearLayout.addView(dLin);
        }
    }
}
