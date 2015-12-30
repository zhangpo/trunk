package cn.com.choicesoft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Addition;

import java.util.ArrayList;
import java.util.List;

/**
 * ∏Ωº”œÓ  ≈‰∆˜
 */
public class SingleFujiaAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Addition> fujiaLists;
	private LayoutInflater inflater;

    private List<Addition> exceptFirstLists = new ArrayList<Addition>();

	public SingleFujiaAdapter(Context pContext, List<Addition> fujiaLists) {
		super();
		this.mContext = pContext;
		this.fujiaLists = fujiaLists;
		inflater = LayoutInflater.from(mContext);
        for(Addition addition:fujiaLists){
            if(!"0".equals(addition.getDefualts())){
                exceptFirstLists.add(addition);
            }
        }
	}
	
	
	
	
	public void setFujiaLists(List<Addition> pfujiaLists){
		this.fujiaLists = pfujiaLists;
	}
	
	public void toggleChecked(int position){
		fujiaLists.get(position).setSelected(!fujiaLists.get(position).isSelected());
	}

	@Override
	public int getCount() {
		return fujiaLists.size();
	}

	@Override
	public Addition getItem(int position) {
		return fujiaLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Addition addition = fujiaLists.get(position);
		
		SingleFujiaHolder holder = null;
		if(convertView == null){
			holder = new SingleFujiaHolder();
			convertView = inflater.inflate(R.layout.comonfujia_listview_item, null);
			holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.fujia_listItem_checkbox);
			holder.mTextView = (TextView) convertView.findViewById(R.id.fujia_listItem_tvdes);
			convertView.setTag(holder);
		}else{
			holder = (SingleFujiaHolder) convertView.getTag();
		}
		
		holder.mCheckBox.setChecked(addition.isSelected());
		holder.mTextView.setText(addition.getFoodFuJia_des());
		
		return convertView;
	}
	
	public class SingleFujiaHolder{
		
		public CheckBox mCheckBox;
		TextView mTextView;
		
	}

}
