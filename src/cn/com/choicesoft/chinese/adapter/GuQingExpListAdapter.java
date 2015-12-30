package cn.com.choicesoft.chinese.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.bean.Grptyp;
import cn.com.choicesoft.chinese.activity.ChineseGuQingActivity;
import cn.com.choicesoft.chinese.listener.GuQingClick;
import cn.com.choicesoft.util.ValueUtil;

/**
 * M��c
 * 2015-08-15
 * Jnwsczh@163.com
 */
public class GuQingExpListAdapter extends BaseExpandableListAdapter {

    private ChineseGuQingActivity mActivity;
    private LayoutInflater layoutInflater;
    private ExpandableListView expandListView;
    private List<Grptyp> groupList;//��Ŀ¼��Ӧ�ļ���
    private List<Map<String, String>> guQingList;//����Ĳ�Ʒ
    private List<Food> childList;//��Ŀ¼��Ӧ�ļ���
    private List<Grptyp> groupAdapterLists = new ArrayList<Grptyp>();//��Ŀ¼��Ӧ�ļ���
    private List<List<Food>> childAdapterLists = new ArrayList<List<Food>>();//��Ŀ¼��Ӧ�ļ���

    private List<Food> integratedLists;//�ܵļ���

    public GuQingExpListAdapter(ChineseGuQingActivity pActivity, ExpandableListView expandListView, List<Grptyp> groupList, List<Food> childList,List<Map<String, String>> guQingList) {
        super();
        this.mActivity = pActivity;
        this.expandListView = expandListView;
        this.groupList = groupList;
        this.childList = childList;
        this.guQingList=guQingList;
        layoutInflater = LayoutInflater.from(mActivity);
        inithomeList();
    }

    private void inithomeList() {
        groupAdapterLists.clear();
        childAdapterLists.clear();
        for (Grptyp grptyp : groupList) {
            groupAdapterLists.add(grptyp);
            List<Food> childs = new ArrayList<Food>();
            for (Food food : childList) {
                if (ValueUtil.isNotEmpty(grptyp.getGrp())&&(grptyp.getGrp().equals(food.getSortClass())||grptyp.getGrp().equals("000"))){
                    GuQingStateSetting(food);
                    childs.add(food);
                }else if(grptyp.getGrp().equals("GQTYP")){
                    if(GuQingStateSetting(food)){
                        childs.add(food);
                    }
                }
            }
            childAdapterLists.add(childs);
        }
    }
    public boolean GuQingStateSetting(Food food){
        if(guQingList!=null) {
            for (int i = 0; i < guQingList.size(); i++) {
                if(ValueUtil.isNotEmpty(food.getPcode())&&food.getPcode().equals(guQingList.get(i).get("ITCODE"))){
                    food.setSoldOut(true);
                    food.setSoldCnt(guQingList.get(i).get("CNT"));
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return childAdapterLists.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Food food = childAdapterLists.get(groupPosition).get(childPosition);
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


    @Override
    public int getChildrenCount(int groupPosition) {

        return childAdapterLists.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupAdapterLists.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return groupAdapterLists.size();
    }


    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Grptyp grptyp = groupAdapterLists.get(groupPosition);
        convertView = getLayoutInflater().inflate(R.layout.guqing_expandlist_item_group, null);
        expandListView.setGroupIndicator(null);
        TextView nameText = (TextView) convertView.findViewById(R.id.gu_qing_expandlist_item_group_name_text);
        nameText.setText(grptyp.getDes());
        convertView.setTag(grptyp);
        return convertView;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return false;
    }
}
