package cn.com.choicesoft.view;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.Addition;
import cn.com.choicesoft.util.ValueUtil;

public class FujiaDialog extends DialogFragment {

	public FujiaDialog() {}

	static DialogFujiaClickListener mListener;

	public interface DialogFujiaClickListener {
		void doPositiveClick(ArrayList<Addition> pArray);

		void doNegativeClick();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		View view = null;
		String title = getArguments().getString("title");
		if (!ValueUtil.isEmpty(title)) {
			TextView t = (TextView) view.findViewById(R.id.dia_tv_fujiatitle);
			t.setText(title);
		}
		
		final ArrayList<Addition> al = getArguments().getParcelableArrayList("additionLists");
		
		ListView listView = (ListView) view.findViewById(R.id.dia_fujia_listview);
		listView.setAdapter(new MyAdapter(getActivity(), al));
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				// TODO Auto-generated method stub
				ViewHolder holder = (ViewHolder) view.getTag();
				holder.checkBoxholder.toggle();
				MyAdapter adapter = (MyAdapter) parent.getAdapter();
				adapter.toggleChecked(position);
			}
		});
		

		View certain = view.findViewById(R.id.dia_fujia_certain);
		View cancel = view.findViewById(R.id.dia_fujia_cancel);

		certain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (mListener != null) {
					Log.e("^^^^^^^^^^^", al.size()+"");
					mListener.doPositiveClick(al);
				}

			}

		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (mListener != null) {
					mListener.doNegativeClick();
				}
			}

		});

		dialog.setContentView(view);
		return dialog;
	}

	public static FujiaDialog newInstance(String title,ArrayList<Addition> addLists,DialogFujiaClickListener listener) {
		FujiaDialog frag = new FujiaDialog();
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putParcelableArrayList("additionLists", addLists);
		frag.setArguments(b);
		mListener = listener;

		return frag;
	}
	
	class MyAdapter extends BaseAdapter{
		
		private Context mContext;
		private ArrayList<Addition> list;
		
		
		public MyAdapter(Context mContext, ArrayList<Addition> list) {
			super();
			this.mContext = mContext;
			this.list = list;
		}
		
		public void toggleChecked(int position) {
			getItem(position).setSelected(!getItem(position).isSelected());
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Addition getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder ;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.dia_fujia_listview_item, null);
				holder.checkBoxholder = (CheckBox) convertView.findViewById(R.id.dia_fujia_item_checkbox);
				holder.fujiaName = (TextView) convertView.findViewById(R.id.dia_fujia_item_fujianame);
				holder.fujiaPrice = (TextView) convertView.findViewById(R.id.dia_fujia_item_fujiaprice);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			Addition addition = list.get(position);
			holder.checkBoxholder.setChecked(addition.isSelected());
			holder.fujiaName.setText(addition.getFoodFuJia_des());
			holder.fujiaPrice.setText(addition.getFoodFuJia_checked());
			return convertView;
		}
		
	}
	
	class ViewHolder{
		CheckBox checkBoxholder;
		TextView fujiaName;
		TextView fujiaPrice;
	}

}
