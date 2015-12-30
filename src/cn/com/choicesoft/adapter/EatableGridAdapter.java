package cn.com.choicesoft.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.util.ValueUtil;

/**
 * �ұ߾����Ʒ��Ӧ�Ŀؼ���������
 */
public class EatableGridAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Food> list;
	private LayoutInflater inflater;
	
	public EatableGridAdapter(Context pContext, List<Food> list) {
		super();
		this.mContext = pContext;
		this.list = list;
		inflater = LayoutInflater.from(mContext);
	}
	
	
	/**
	 * �õ�����ѡ���������
	 * @return
	 */
	public int getItemCounts(){
		int itemCounts = 0;
		if(list != null){
			for(Food food:list){
				if(food.isSelected()){
					itemCounts += Float.parseFloat(food.getCounts());
//					itemCounts += Integer.parseInt(food.getCounts());
				}
			}
		}
		return itemCounts;
	}
	
	public void setEatableGridSource(List<Food> pList){
		this.list = pList;
	}
	

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Food getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		EatableViewHolder holder = null;

		if(convertView == null){
			holder = new EatableViewHolder();
			if (ChioceActivity.ispad) {
				convertView = inflater.inflate(R.layout.dishes_gridview_item_pad, null);
			} else {
				convertView = inflater.inflate(R.layout.dishes_gridview_item, null);
			}
			holder.textItemName = (TextView) convertView.findViewById(R.id.gridview_item_name);
			holder.textItemCounts = (TextView) convertView.findViewById(R.id.gridview_item_dishcounts);
			holder.textItemSoldoutCounts=(TextView) convertView.findViewById(R.id.gridview_item_soldoutcounts);
			holder.textItemPrice=(TextView)convertView.findViewById(R.id.gridview_item_price);
			convertView.setTag(holder);
		}else{
			holder = (EatableViewHolder) convertView.getTag();
		}

		holder.textItemSoldoutCounts.setText("");
		Food food = list.get(position);
		holder.textItemName.setText(food.getPcname());
		holder.textItemPrice.setText(food.getPrice());
		if (food.getSoldCnt()!=null){
			holder.textItemSoldoutCounts.setText(food.getSoldCnt());
			if (Double.parseDouble(food.getSoldCnt())<=Double.parseDouble(food.getSoldChangeCnt()==null?"0.0":food.getSoldChangeCnt())){
				convertView.setBackgroundResource(R.drawable.phone_taiwei_purple_normal);//��ɫ
			}
			if (Double.parseDouble(food.getSoldCnt())==0){
				food.setSoldOut(true);
			}
		}
		if(food.isSoldOut()){//���������Ʒ
			convertView.setBackgroundResource(R.drawable.gq);//��ɫ
		}else{
			if(food.isSelected()){
				convertView.setBackgroundResource(R.drawable.table_buton_red);
				holder.textItemCounts.setVisibility(View.VISIBLE);
//				holder.textItemSoldoutCounts.setVisibility(View.V);
				holder.textItemCounts.setText(food.getCounts());
			}else{
				convertView.setBackgroundResource(R.drawable.table_buton_green);
				holder.textItemCounts.setVisibility(View.GONE);
			}
		}
		
		//�ֻ��µĲ�Ʒ����������ɫΪ��ɫ
		if (!ChioceActivity.ispad) {
			holder.textItemName.setTextColor(Color.WHITE);
			holder.textItemName.setTextSize(12);
			int imagesize = 0;
			AbsListView.LayoutParams layoutParams = null;
			// ��̨���ȶ�����Ļ�ֱ��ʵ�0.244���߶��ǿ��ȵ�3/5
			imagesize = (int) (MainActivity.screenWidth * 0.205);
			//��Ʒ���½�������С
			FrameLayout.LayoutParams flp = new LayoutParams(imagesize/2,imagesize/3);
			flp.gravity = Gravity.BOTTOM|Gravity.RIGHT;
			holder.textItemCounts.setLayoutParams(flp);
			holder.textItemCounts.setTextSize(10);
			//��Ʒadapter������С
			layoutParams = new AbsListView.LayoutParams(
					imagesize,imagesize*4/5);
			convertView.setLayoutParams(layoutParams);
		}
		return convertView;
	}
	
	class EatableViewHolder{
		
		TextView textItemName;
		TextView textItemCounts;
		TextView textItemSoldoutCounts;
		TextView textItemPrice;
	}

}