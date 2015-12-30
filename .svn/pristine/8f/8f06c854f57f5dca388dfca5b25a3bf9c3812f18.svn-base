package cn.com.choicesoft.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.bean.Storetable;
import cn.com.choicesoft.util.SharedPreferencesUtils;

/**
 * 台位适配器
 * 
 */
public class TaiWeiAdapter extends BaseAdapter {

	ArrayList<Storetable> tablelists;
	private Context myContext;

	public TaiWeiAdapter(Context context, ArrayList<Storetable> myzhuotais) {
		myContext = context;
		tablelists = myzhuotais;
	}

	public ArrayList<Storetable> getListData() {
		return tablelists;
	}

	public void setData(ArrayList<Storetable> myzhuotais) {
		tablelists = myzhuotais;
	}

	@Override
	public int getCount() {

		return tablelists == null ? 0 : tablelists.size();
	}

	@Override
	public Object getItem(int position) {
		return tablelists == null ? null : tablelists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Storetable storeTable = tablelists.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			if (ChioceActivity.ispad) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.gridview_item_pad, null);
			}else {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.gridview_item, null);
			}
			holder.textItem = (TextView) convertView
					.findViewById(R.id.gridview_item);
			// -----2014年3月11日 Mc 右上角 台位最大人数
			holder.textSize = (TextView) convertView
					.findViewById(R.id.gridview_item_right);
			// -----2014年3月11日 Mc 右上角 台位最大人数
			int imagesize = 0;
			AbsListView.LayoutParams layoutParams;
			if (ChioceActivity.ispad) {
				// 桌台宽度度是屏幕分辨率的0.192，高度是宽度的3/5
				imagesize = (int) (MainActivity.screenWidth * 0.192);
				layoutParams = new AbsListView.LayoutParams(
						imagesize, (imagesize * 3) / 5);
			} else {
				// 桌台宽度度是屏幕分辨率的0.244，高度是宽度的3/5
				imagesize = (int) (MainActivity.screenWidth * 0.1755);
				layoutParams = new AbsListView.LayoutParams(
						imagesize,imagesize);
			}

			convertView.setLayoutParams(layoutParams);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textItem.setText(storeTable.getTblname());
		// -----2014年3月11日 Mc 右上角 台位最大人数
		holder.textSize.setText(storeTable.getPerson());
		if (storeTable.getTblname().length()>=4) {
			holder.textItem.setTextSize(13);
		}
		if (ChioceActivity.ispad) {
			holder.textItem.setTextSize(19);
			holder.textSize.setTextSize(15);
		}
		// -----2014年3月11日 Mc 右上角 台位最大人数
		// 以下是我写的
		String tableStatus = storeTable.getUsestate();
		//判断是否是PAD,加载不同的台位背景
		if (ChioceActivity.ispad) {
			if (SharedPreferencesUtils.getChineseSnack(myContext) == 0) {
				if ("1".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_green);
				} else if ("2".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_yellow);
				} else if ("3".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_red);
				} else if ("4".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_purple);
				} else if ("6".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_blue);
				} else if ("10".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_gray);
				}
				// 1 ：空闲 绿色 2： 开台 黄色
				// 3 ：点菜 红色 4 ：结账 紫色 6 ：已封台 蓝色 7 ：换台 粉色 8 ：子台位 未使用 9 ：挂单 黑色
				// 10：菜齐 灰色"
			} else {
				if ("0".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_blue);
				} else if ("1".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_red);
				} else if ("2".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_green);
				} else if ("3".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_purple);
				} else if ("4".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.table_buton_yellow);
				}else if("5".equals(tableStatus)){//脏台
					convertView.setBackgroundResource(R.drawable.table_buton_gray);
				}
			}
		} else {
			if (SharedPreferencesUtils.getChineseSnack(myContext) == 0) {//0是快餐 1是中餐
				if ("1".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_green);
				} else if ("2".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_yellow);
				} else if ("3".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_red);
				} else if ("4".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_purple);
				} else if ("6".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_blue);
				} else if ("10".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_gray);
				}
			} else {
				if ("0".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_blue);
				} else if ("1".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_red);
				} else if ("2".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_green);
				} else if ("3".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_purple);
				} else if ("4".equals(tableStatus)) {
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_yellow);
				}else if("5".equals(tableStatus)){//脏台
					convertView.setBackgroundResource(R.drawable.phone_taiwei_buton_gray);
				}
			}
		}
		return convertView;
	}

}

class ViewHolder {
	TextView textItem;
	// -----2014年3月11日 Mc 右上角 台位最大人数
	TextView textSize;
	// -----2014年3月11日 Mc 右上角 台位最大人数
}
