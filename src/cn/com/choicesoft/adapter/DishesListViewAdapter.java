package cn.com.choicesoft.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.YiDianDishes;

/**
 * 主页面Fragment里面的ListView的Adapter
 */
public class DishesListViewAdapter extends BaseAdapter {

	ArrayList<YiDianDishes> myDishes;
	private Context myContext;

	public DishesListViewAdapter(Context context,ArrayList<YiDianDishes> dishes){
		myContext = context;
		myDishes = dishes;
	} 
	
	public void setData(ArrayList<YiDianDishes> dishes) {
		myDishes = dishes;
	}

	@Override
	public int getCount() {

		return myDishes == null ? 0 : myDishes.size();
	}

	@Override
	public Object getItem(int position) {
		Log.d("page", myDishes.size()+"getItem====zhuotais.size()");
		return myDishes == null ? null : myDishes.get(position);
	}

	@Override
	public long getItemId(int position) {
		Log.d("page", position+"getItemId====zhuotais.size()");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		YiDianDishes dishes = myDishes.get(position);
		DishesListViewHolder holder = null;
		if(convertView==null){
			holder = new DishesListViewHolder();
			convertView = LayoutInflater.from(
					myContext).inflate(R.layout.dishes_listview_item, null);
			holder.textItemName = (TextView) convertView
					.findViewById(R.id.listview_item_name);
			holder.textItemPrice = (TextView) convertView
					.findViewById(R.id.listview_item_price);
			holder.textItemUnit = (TextView) convertView
					.findViewById(R.id.listview_item_unit);
			convertView.setTag(holder);
		}else{
			holder = (DishesListViewHolder) convertView.getTag();
		}
		
		holder.textItemName.setText(dishes.getName());
		holder.textItemPrice.setText(dishes.getPrice());
		holder.textItemUnit.setText(dishes.getUnit());
		
		return convertView;
	}

}
class DishesListViewHolder{
	TextView textItemName;
	TextView textItemPrice;
	TextView textItemUnit;
}
