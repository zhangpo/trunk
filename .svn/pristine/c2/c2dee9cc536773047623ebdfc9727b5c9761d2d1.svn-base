package cn.com.choicesoft.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.bean.Unit;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.util.ToastUtil;
import cn.com.choicesoft.util.ValueUtil;

import java.util.Map;

/**
 * 多单位显示
 * Created by M.c on 2014/9/3.
 */
public class MuchUntiView {
    private IResult<Unit> result;
    private Activity activity;
    private View mView;

    /**
     *
     * @param result
     * @param activity
     * @param view
     */
    public MuchUntiView(IResult<Unit> result, Activity activity, View view) {
        this.result = result;
        this.activity = activity;
        this.mView = view;
    }

    public void showView(final Food mFood){
        if(mFood.getUnitMap().size()>1) {
            View view = activity.getLayoutInflater().inflate(R.layout.unit_popup_window, null);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.popupWind);
            linearLayout.setPadding(2, 2, 2, 2);
            final PopupWindow popupWindow = new PopupWindow(view, (int) (MainActivity.screenWidth * 0.3), LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            LinearLayout dLin = new LinearLayout(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams linearlayoutlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dLin.setLayoutParams(linearlayoutlp);
            for (final Map.Entry<String, Unit> map : mFood.getUnitMap().entrySet()) {
                Button but = new Button(activity);
                but.setText(map.getValue().getUnitName());
                but.setTextSize(14);
                but.setWidth((int) (MainActivity.screenWidth * 0.1));
                but.setBackgroundResource(R.drawable.table_buton_green);
                but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        if (result != null) {
                            result.result(map.getValue());
                        }
                    }
                });
                dLin.addView(but, params);
                if (dLin.getChildCount() == 2) {
                    linearLayout.addView(dLin);
                    dLin = new LinearLayout(activity);
                }
            }
            if (dLin.getChildCount() > 0) {
                linearLayout.addView(dLin);
            }
            int[] d=new int[2];
            mView.getLocationInWindow(d);
            if(d[0]==0&&d[1]==0){//因为gridView第一个位置弹窗会出现错位，下面是处理方法
                ((GridView)mView.getParent()).getLocationInWindow(d);
                popupWindow.showAsDropDown(mView,(int)(d[0]+mView.getWidth()*0.2),(int)(d[1]+mView.getHeight()*0.2));
            }else {
                popupWindow.showAsDropDown(mView);
            }
        }else{
            result.result(mFood.getUnitMap().get("unit1"));
        }
    }
}
