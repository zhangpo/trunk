package cn.com.choicesoft.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import cn.com.choicesoft.bean.Food;


public class FoodList {
	List<Food> mFoodLists = new LinkedList<Food>();
	
	public void add(Food foodItem){
		mFoodLists.add(foodItem);
	}
	
	public void clear(){
		mFoodLists.clear();
	}
	
	public List<Food> getAllFoodListByTablenum(){       //��AllCheck���в�ѯ�����еĲ�Ʒ
		return mFoodLists;
	}
	
	public List<Food> getAllFoodListFromtabFood(){   //��foodL���в�ѯ�����еĲ�Ʒ
		List<Food> lists = new ArrayList<Food>();
		for(Food food:mFoodLists){
			lists.add(food);
		}
		return lists;
	}

}
