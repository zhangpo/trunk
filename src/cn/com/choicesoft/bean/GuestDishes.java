package cn.com.choicesoft.bean;

import java.util.ArrayList;
import java.util.List;

public class GuestDishes {
	
	private static ArrayList<Food> al = null;
	
	private GuestDishes(){}
	
	
	public static List<Food> getGuDishInstance(){
		if(al == null){
			al = new ArrayList<Food>();
		}
		return al;
	}
	

}
