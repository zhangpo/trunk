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
 * 点菜页面，右侧滑动菜单listview对应的适配器
 * @author dell
 *
 */
public class RightMenuAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Food> list;
	private LayoutInflater inflater;
	
	public RightMenuAdapter(Context pContext, List<Food> list) {
		super();
		this.mContext = pContext;
		this.list = list;
		this.inflater = LayoutInflater.from(mContext);
	}
	
	public void setRightMenuSource(List<Food> list){
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Food food = list.get(position);
		
		RightMenuViewholder holder = null;
		if(convertView == null){
			holder = new RightMenuViewholder();
			convertView = inflater.inflate(R.layout.eatable_rightmenu_listview_item, null);
			holder.dishName = (TextView) convertView.findViewById(R.id.eatable_rightmenu_item_foodname);
			holder.dishCounts = (TextView) convertView.findViewById(R.id.eatable_rightmenu_item_foodcounts);
			convertView.setTag(holder);
		}else{
			holder = (RightMenuViewholder) convertView.getTag();
		}
		
		holder.dishName.setText(food.getPcname());
		if(!food.isIstc()){
			holder.dishCounts.setText(food.getPcount());
		}else if(food.isIstc() && food.getPcode().equals(food.getTpcode())){
			holder.dishCounts.setText(food.getPcount());//套餐是单份显示的，直接填1也行
		}else if(food.isIstc() && !food.getTpcode().equals(food.getPcode())){
			holder.dishCounts.setText(food.getCnt());
		}
		return convertView;
	}
	
	class RightMenuViewholder{
		
		TextView dishName;
		TextView dishCounts;
	}

}
