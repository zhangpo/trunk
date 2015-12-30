package cn.com.choicesoft.chinese.listener;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;
import java.util.List;

import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.chinese.adapter.GuQingListAdapter;
import cn.com.choicesoft.util.ConvertPinyin;
import cn.com.choicesoft.util.ValueUtil;

public class GuQingWatcher implements TextWatcher {
        private List<Food> foodList;
        private List<Food> showFoodList=new ArrayList<Food>();
        private GuQingListAdapter guQingListAdapter;


        public GuQingWatcher(List<Food> foods, GuQingListAdapter guQingListAdapter) {
            super();
            this.foodList = foods;
            this.guQingListAdapter = guQingListAdapter;
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {
            showFoodList.clear();
            String changingStr = s.toString();
            if(ValueUtil.isNotEmpty(changingStr)){
                for(Food food:foodList){
                    String pcname = food.getPcname();
                    if(ConvertPinyin.convertJianPin(pcname).indexOf(changingStr) !=-1 || ConvertPinyin.convertQuanPin(pcname).indexOf(changingStr)!=-1 ){
                        showFoodList.add(food);
                    }
                }

                if(guQingListAdapter != null){
                    guQingListAdapter.setFoodList(showFoodList);
                    guQingListAdapter.notifyDataSetChanged();
                }

            }else{
                guQingListAdapter.setFoodList(foodList);
                guQingListAdapter.notifyDataSetChanged();
            }
        }

    }