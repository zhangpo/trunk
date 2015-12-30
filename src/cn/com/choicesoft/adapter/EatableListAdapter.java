package cn.com.choicesoft.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.bean.Grptyp;
import cn.com.choicesoft.util.ValueUtil;

/**
 * 左边菜品大类对应的控件的适配器
 */
public class EatableListAdapter extends BaseAdapter {
	
	Context context;
	LayoutInflater inflater;
	int last_item;
	private int selectedPosition = -1; 
	List<Grptyp> list;
	

	public EatableListAdapter(Context context, List<Grptyp> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	public void setListSource(List<Grptyp> plist){
		this.list = plist;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Grptyp getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Grptyp grptyp = list.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			if (ChioceActivity.ispad) {
				convertView = inflater.inflate(R.layout.eatable_listview_item_pad, null);
			} else {
				convertView = inflater.inflate(R.layout.eatable_listview_item, null);
			}
			holder.itemLinearLayout = (FrameLayout) convertView.findViewById(R.id.eatable_listview_item_layout);
			holder.dishClass = (TextView) convertView.findViewById(R.id.eatable_listview_item_dishclass);
			holder.dishCount = (TextView) convertView.findViewById(R.id.eatable_listview_item_count);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		 // 设置选中效果    
		if (selectedPosition == position) {
			if (ChioceActivity.ispad) {
				holder.dishClass.setTextColor(Color.BLUE);
				holder.itemLinearLayout.setBackgroundResource(R.drawable.feilei_down);
			} else {
				holder.dishClass.setTextColor(context.getResources().getColor(R.color.mistyrose));
			}
		} else {
			if (ChioceActivity.ispad) {
				holder.itemLinearLayout.setBackgroundResource(R.drawable.feilei_up);
				holder.dishClass.setTextColor(Color.WHITE);
			} else {
				holder.dishClass.setTextColor(Color.GRAY);
			}
		}
		
		holder.dishClass.setText(grptyp.getDes());

		holder.dishCount.setText(ValueUtil.isNotEmpty(grptyp.getQuantity()) && Float.parseFloat(grptyp.getQuantity()) >0?grptyp.getQuantity():"");
		return convertView;
	}
	
	public void setSelectedPosition(int position) {   
		   selectedPosition = position;   
	}  
	
	private static class ViewHolder{
		FrameLayout itemLinearLayout;
		TextView dishClass;
		TextView dishCount;
	}

}
