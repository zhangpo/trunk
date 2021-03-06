package cn.com.choicesoft.activity.action;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.adapter.TaiWeiAdapter;
import cn.com.choicesoft.bean.Storetable;
import cn.com.choicesoft.util.SharedPreferencesUtils;

/**
 * 搭台（拼台）【新增功能】
 * Created by Mc on 14-12-22.
 */
public class CombineTable implements AdapterView.OnItemLongClickListener {
    private Activity activity;

    public CombineTable(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
        final Storetable storeTable = ((TaiWeiAdapter) parent.getAdapter()).getListData().get(position);
        int CHIN_SNACK=SharedPreferencesUtils.getChineseSnack(this.activity);
        if(CHIN_SNACK==0&&("2".equals(storeTable.getUsestate()) || "3".equals(storeTable.getUsestate()))) {
                Dialog mDialog;
                //判断是否区分男女，会弹出不同的对话框
                boolean isDisguishGender = SharedPreferencesUtils.getDisguishGender(activity);
            mDialog = ((MainActivity) (activity)).createStartcDia(storeTable,"6",false);
//                if (isDisguishGender) {
//                    mDialog = ((MainActivity) (activity)).createStartcDia(storeTable, "6");
//                } else {
//                    mDialog = ((MainActivity) (activity)).createStartcDiaNotDisguish(storeTable, "6");
//                }
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                ((MainActivity) (activity)).getGuestLists().clear();
                return true;
        }else if (CHIN_SNACK==1&&("1".equals(storeTable.getUsestate()) || "4".equals(storeTable.getUsestate()))){
            Dialog mDialog = ((MainActivity) (activity)).createChineseStartcDia(storeTable, "1");
            mDialog.show();
            ((MainActivity) (activity)).getGuestLists().clear();
            return true;
        }
        return false;
    }
}
