package cn.com.choicesoft.chinese.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.chinese.activity.ChineseGuQingActivity;
import cn.com.choicesoft.chinese.listener.GuQingClick;
import cn.com.choicesoft.util.ValueUtil;

/**
 * M。c
 * 2015-08-15
 * Jnwsczh@163.com
 */
public class GuQingListAdapter extends BaseAdapter {

    private ChineseGuQingActivity mActivity;
    private LayoutInflater layoutInflater;
    private List<Map<String, String>> guQingList;//估清的菜品
    private List<Food> childList;//父目录对应的集合


    public GuQingListAdapter(ChineseGuQingActivity pActivity,List<Food> childList, List<Map<String, String>> guQingList) {
        super();
        this.mActivity = pActivity;
        this.childList = childList;
        this.guQingList=guQingList;
        layoutInflater = LayoutInflater.from(mActivity);
        inithomeList();
    }

    private void inithomeList() {
        for (Food food : childList) {
            GuQingStateSetting(food);
        }
    }
    public void GuQingStateSetting(Food food){
        if(guQingList!=null) {
            for (int i = 0; i < guQingList.size(); i++) {
                if(ValueUtil.isNotEmpty(food.getPcode())&&food.getPcode().equals(guQingList.get(i).get("ITCODE"))){
                    food.setSoldOut(true);
                    food.setSoldCnt(guQingList.get(i).get("CNT"));
                }
            }
        }
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public int getCount() {
        return childList.size();
    }

    @Override
    public Object getItem(int position) {
        return childList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Food food = childList.get(position);
        convertView = getLayoutInflater().inflate(R.layout.guqing_expandlist_item_child, null);
        TextView nameText = (TextView) convertView.findViewById(R.id.gu_qing_expandlist_item_child_name_text);
        TextView cnttext = (TextView) convertView.findViewById(R.id.gu_qing_expandlist_item_child_cnt_text);
        TextView cnt = (TextView) convertView.findViewById(R.id.gu_qing_expandlist_item_child_cnt_text1);
        Button cancel= (Button) convertView.findViewById(R.id.gu_qing_expandlist_item_child_cancel_but);
        Button setting= (Button) convertView.findViewById(R.id.gu_qing_expandlist_item_child_setting_but);
        if(food.isSoldOut()){
            cancel.setVisibility(View.VISIBLE);
            cnttext.setVisibility(View.VISIBLE);
            cnt.setVisibility(View.VISIBLE);
            cnt.setText(food.getSoldCnt());
        }
        cancel.setOnClickListener(new GuQingClick(mActivity));
        setting.setOnClickListener(new GuQingClick(mActivity));
        nameText.setText(food.getPcname());
        convertView.setTag(food);
        setting.setTag(food);
        cancel.setTag(food);
        return convertView;
    }

    public void setFoodList(List<Food> foodList) {
        this.childList = foodList;
        inithomeList();
    }
}
