package cn.com.choicesoft.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Addition;

/**
 * 自定义附加项适配器
 * 
 */
public class AttachAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Addition> additions;
	public Map<Integer, Boolean> selectedMap;

	/**
	 * 构造器
	 */
	public AttachAdapter(Context context, ArrayList<Addition> addition) {
		this.additions = addition;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 保存每条记录是否被选中的状态
		selectedMap = new HashMap<Integer, Boolean>();
		for (int i = 0; i < additions.size(); i++) {
			selectedMap.put(i, false);
		}
		
	}


	public void setData(ArrayList<Addition> additions){
		this.additions = additions;
	}
	
	@Override
	public int getCount() {
		return additions == null ? 0 : additions.size();
	}

	@Override
	public Object getItem(int position) {
		return additions == null ? 0 : additions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.addition_popup_list_item,
					null);
			holder = new ViewHolder();

			// 实例化的控件
			holder.checkBox_attach = (CheckBox) convertView
					.findViewById(R.id.check_attachs);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		additions.get(position).setCheck(false);
		// 取得本地附加项信息
//		holder.checkBox_attach.setText(this.additions.get(position).getName());
//		if (selectedMap.get(position)) {
//			holder.checkBox_attach.setChecked(true);
//		} else {
//			holder.checkBox_attach.setChecked(false);
//		}
		holder.checkBox_attach
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
//						selectedMap.put(position, arg1);
//						additions.get(position).setCheck(arg1);
					}
				});
		return convertView;

	}

	class ViewHolder {
		CheckBox checkBox_attach;
	}

}
