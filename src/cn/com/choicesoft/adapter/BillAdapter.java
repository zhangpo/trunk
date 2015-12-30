package cn.com.choicesoft.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.util.ValueUtil;

/**
 * 支付界面 账单信息adapter
 *@Author:M.c
 *@CreateDate:2014-4-10
 *@Email:JNWSCZH@163.COM
 */
public class BillAdapter extends BaseAdapter {


	private List<Map<String,String>> list;
	private Context myContext;

	public BillAdapter(Context context,List<Map<String,String>> list){
		this.myContext = context;
		this.list = list;
	} 
	
	public List<Map<String,String>> getListData(){
		return list;
	}
	
	public void setListDataSource(List<Map<String,String>> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return ValueUtil.isEmpty(this.list)? 0 : this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return ValueUtil.isEmpty(list) ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String,String> map = list.get(position);
		if (ChioceActivity.ispad) {
			convertView = LayoutInflater.from(myContext).inflate(R.layout.bill_list_table_pad, null);
		} else {
			convertView = LayoutInflater.from(myContext).inflate(R.layout.bill_list_table, null);
		}
		TextView name=(TextView)convertView.findViewById(R.id.greensName);
		TextView number=(TextView)convertView.findViewById(R.id.greensNum);
		TextView price=(TextView)convertView.findViewById(R.id.greensPrice);
		LinearLayout fujiaLayout=(LinearLayout)convertView.findViewById(R.id.Bill_fujiaLayout);
		TextView fuJiaX=(TextView)convertView.findViewById(R.id.Bill_fuJiaXText1);
		number.setText(map.get("pcount"));
		price.setText(map.get("price"));
        String fujia=map.get("fujianame");
		String temp=map.get("istemp");
		String fName=map.get("pcname");
		if (map.get("SELECT")!=null&&map.get("SELECT").equals("1")){
			convertView.setBackgroundColor(Color.LTGRAY);
		}else {
			convertView.setBackgroundColor(Color.WHITE);
		}


		if(temp!=null&&temp.trim().equals("1")){
			fName=fName+"--"+map.get("tempname");
		}
		name.setText(fName);
        if(ValueUtil.isNotEmpty(map.get("fujiaprice"))){
            fujia+="-"+map.get("fujiaprice");
        }
		fuJiaX.setText(myContext.getString(R.string.fujia_money));
		if(ValueUtil.isEmpty(map.get("fujiaprice"))){
			fujiaLayout.setVisibility(LinearLayout.GONE);
		}
		return convertView;
	}

}
