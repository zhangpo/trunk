package cn.com.choicesoft.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.util.ToastUtil;
import cn.com.choicesoft.util.ValueUtil;

/**
 * 临时菜
 */
public class TempFoodView {
    private Activity mActivity;
    private Food mFood;
    private Result mResult;

    public TempFoodView(Activity activity, Food mFood,Result result) {
        this.mActivity = activity;
        this.mFood = mFood;
        this.mResult=result;
    }

    /**
     * 显示临时菜设置
     */
    public void show(){
        View view= mActivity.getLayoutInflater().inflate(R.layout.temp_food_view,null);
        final EditText name= (EditText) view.findViewById(R.id.temp_food_name);
        final EditText price= (EditText) view.findViewById(R.id.temp_food_price);
        price.addTextChangedListener(new TextWatcher()//保留两位小数
        {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        new AlertDialog.Builder(mActivity, R.style.edittext_dialog).setTitle(R.string.temp_food_set).setView(view)
                .setNegativeButton(R.string.cancle, null)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String n = name.getText().toString();
                        String p = price.getText().toString();
                        String msg = null;
                        if (ValueUtil.isEmpty(n)) {
                            msg = mActivity.getString(R.string.name_null);
                        }
                        if (msg == null && ValueUtil.isEmpty(p)) {
                            msg = mActivity.getString(R.string.price_null);
                        }
                        if (msg != null) {
                            ToastUtil.toast(mActivity, msg);
                            return;
                        }
                        mFood.setTempName(n);
                        mFood.setPrice(p);
                        if (mResult != null) {
                            mResult.handle(mFood);
                        }
                    }
                }).show();
    }

    /**
     * 回调函数
     */
    public interface Result{
         void handle(Food food);
    }

}
