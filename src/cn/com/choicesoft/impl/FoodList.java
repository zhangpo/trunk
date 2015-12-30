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
	
	public List<Food> getAllFoodListByTablenum(){       //从AllCheck表中查询出所有的菜品
		return mFoodLists;
	}
	
	public List<Food> getAllFoodListFromtabFood(){   //从foodL表中查询出所有的菜品
		List<Food> lists = new ArrayList<Food>();
		for(Food food:mFoodLists){
			lists.add(food);
		}
		return lists;
	}

}
