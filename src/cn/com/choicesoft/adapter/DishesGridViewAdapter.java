package cn.com.choicesoft.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Food;

/**
 * 主页面Fragment里面的GridView的Adapter
 */
public class DishesGridViewAdapter extends BaseAdapter {

	private ArrayList<Food> myDishes;
	private Context myContext;

	public DishesGridViewAdapter(Context context,ArrayList<Food> dishes){
		myContext = context;
		myDishes = dishes;
	} 
	
	public ArrayList<Food> getListData(){
		return myDishes;
	}
	
	public void setListDataSource(ArrayList<Food> dishes) {
		myDishes = dishes;
	}

	@Override
	public int getCount() {
		return myDishes == null ? 0 : myDishes.size();
	}

	@Override
	public Object getItem(int position) {
		return myDishes == null ? null : myDishes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Food dishes = myDishes.get(position);
		DishesGridViewHolder holder = null;
		if(convertView==null){
			holder = new DishesGridViewHolder();
			convertView = LayoutInflater.from(myContext).inflate(R.layout.dishes_gridview_item, null);
			holder.textItemName = (TextView) convertView.findViewById(R.id.gridview_item_name);
			holder.textItemCounts = (TextView) convertView.findViewById(R.id.gridview_item_dishcounts);
			convertView.setTag(holder);
		}else{
			holder = (DishesGridViewHolder) convertView.getTag();
		}
		
		holder.textItemName.setText(dishes.getPcname());
		if(dishes.isSelected()){
			convertView.setBackgroundResource(R.drawable.taiwei_item_bg3_red);
			holder.textItemCounts.setVisibility(View.VISIBLE);
			holder.textItemCounts.setText(dishes.getCounts());
			
		}else{
			convertView.setBackgroundResource(R.drawable.taiwei_item_bg1_green);
			holder.textItemCounts.setVisibility(View.GONE);
		}
		
		return convertView;
	}

}
class DishesGridViewHolder{
	
	TextView textItemName;
	TextView textItemCounts;
}
