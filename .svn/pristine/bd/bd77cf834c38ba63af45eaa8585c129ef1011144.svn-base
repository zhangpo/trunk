package cn.com.choicesoft.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Food;

/**
 * Ì×²Í¿É»»¹ºÏîµÄÊÊÅäÆ÷
 *
 */
public class SubDishesGridViewAdapter extends BaseAdapter {

	private List<Food> myDishes;
	private Context myContext;

	public SubDishesGridViewAdapter(Context context,List<Food> dishes){
		myContext = context;
		myDishes = dishes;
	} 
	
	public List<Food> getListData(){
		return myDishes;
	}
	
	public void setListDataSource(List<Food> dishes) {
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
		SubDishesGridViewHolder holder = null;
		if(convertView==null){
			holder = new SubDishesGridViewHolder();
			convertView = LayoutInflater.from(myContext).inflate(R.layout.dishes_gridview_item, null);
			holder.textItemName = (TextView) convertView.findViewById(R.id.gridview_item_name);
			convertView.setTag(holder);
		}else{
			holder = (SubDishesGridViewHolder) convertView.getTag();
		}
		
		holder.textItemName.setText(dishes.getPcname());
		if(dishes.isSelected()){
			convertView.setBackgroundResource(R.drawable.taiwei_item_bg3_red);
			
		}else{
			convertView.setBackgroundResource(R.drawable.taiwei_item_bg1_green);
		}
		
		return convertView;
	}

}

class SubDishesGridViewHolder{
	
	TextView textItemName;
}
