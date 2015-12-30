package cn.com.choicesoft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.CommonFujia;

public class CommonFujiaAdapter extends BaseAdapter {
	
	private List<CommonFujia> comFujiaLists;
	private Context mContext;
	LayoutInflater inflater;
	
	

	public CommonFujiaAdapter(List<CommonFujia> comFujiaLists, Context pContext) {
		super();
		this.comFujiaLists = comFujiaLists;
		this.mContext = pContext;
		inflater= LayoutInflater.from(mContext);
	}
	
	public void setCommonFujiaSource(List<CommonFujia> pListComLists){
		this.comFujiaLists = pListComLists;
	}
	
	//得到选中的公共附加项的集合
	public List<CommonFujia> getSelectedItemLists(){
		List<CommonFujia> selectedLists = new ArrayList<CommonFujia>();
		for(int i = 0;i<comFujiaLists.size();i++){
			if(comFujiaLists.get(i).isSelected()){
				selectedLists.add(comFujiaLists.get(i));
			}
		}
		
		return selectedLists;
	}
	
	public void toggleChecked(int position){
		comFujiaLists.get(position).setSelected(!comFujiaLists.get(position).isSelected());
	}
	
	@Override
	public int getCount() {
		return comFujiaLists.size();
	}

	@Override
	public Object getItem(int position) {
		return comFujiaLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommonFujia comFujia = comFujiaLists.get(position);
		
		ComFujiaHolder holder;
		if(convertView == null){
			holder= new ComFujiaHolder();
			convertView = inflater.inflate(R.layout.comonfujia_listview_item, null);
			holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.fujia_listItem_checkbox);
			holder.mTextView = (TextView) convertView.findViewById(R.id.fujia_listItem_tvdes);
			convertView.setTag(holder);
		}else{
			holder = (ComFujiaHolder) convertView.getTag();
		}
		
		holder.mCheckBox.setChecked(comFujia.isSelected());
		holder.mTextView.setText(comFujia.getDES());
		
		return convertView;
	}
	
	public class ComFujiaHolder{
		
		public CheckBox mCheckBox;
		public TextView mTextView;
	}

}
