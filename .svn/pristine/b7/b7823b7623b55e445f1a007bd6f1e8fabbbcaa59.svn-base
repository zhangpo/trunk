package cn.com.choicesoft.adapter;

import java.util.ArrayList;
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
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.util.ValueUtil;

/**
 *
 */
public class EatableSubGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<Food> mLists;
	private LayoutInflater inflater;

	private List<Food> exceptFirstLists = new ArrayList<Food>();

	public Food getDefaultsFood(){
        for(Food food:mLists){
            if(ValueUtil.isNaNofInteger(food.getDefalutS())==0){
                return food;
            }
        }
		return mLists.get(0);
	}

	/**
	 * 获取该组所选中的对象
	 * @return
	 */
	public List<Food> getSelectedFoodLists(){
		List<Food> selectedLists = new ArrayList<Food>();
		for(Food mFood:exceptFirstLists){
			if(mFood.isSelected()){
				selectedLists.add(mFood);
			}
		}

		return selectedLists;
	}

	/**
	 * 得到该组可换购项已经选择的数量
	 * @return
	 */
	public int getCountsAboutPcount(){
		int countsFood = 0;
		if(mLists != null){
			for(Food food :exceptFirstLists){
				if(ValueUtil.isNotEmpty(food.getPcount())){
					countsFood += Integer.parseInt(food.getPcount());
				}
			}
		}
		return countsFood;
	}

	/**
	 * 判断该组套餐明细的数量是否满足MinCnt
	 * @return
	 */
	public boolean isSatisfyMincntDefault(){
		return getCountsAboutPcount() >= Integer.parseInt(getDefaultsFood().getMinCnt());
	}

	public EatableSubGridViewAdapter(Context pContext, List<Food> pLists,String tpnum) {
		super();
		this.mContext = pContext;
		this.mLists = pLists;
		inflater = LayoutInflater.from(mContext);

		for(Food f:pLists){
			f.setTpnum(tpnum);//一初始化，就附上tpnum
			if(!f.getDefalutS().equals("0")){
				exceptFirstLists.add(f);
			}
		}
	}

	@Override
	public int getCount() {
		return exceptFirstLists.size();
	}

	@Override
	public Object getItem(int position) {
		return exceptFirstLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Food food = exceptFirstLists.get(position);
		TcGridHolder holder = null;
		if(convertView == null){
			holder = new TcGridHolder();
			if (ChioceActivity.ispad) {
				convertView = inflater.inflate(R.layout.dishes_subgridview_item_pad, null);
			} else {
				convertView = inflater.inflate(R.layout.dishes_subgridview_item, null);
			}
			holder.textView = (TextView) convertView.findViewById(R.id.subgridview_item_name);
			holder.countText = (TextView) convertView.findViewById(R.id.subgridview_item_count);
			holder.minCntText = (TextView) convertView.findViewById(R.id.subgridview_item_mincnt);
			holder.maxCntText = (TextView) convertView.findViewById(R.id.subgridview_item_maxcnt);


			convertView.setTag(holder);
		}else{
			holder = (TcGridHolder) convertView.getTag();
		}

		if(ValueUtil.isNotEmpty(food.getPcount()) && Integer.parseInt(food.getPcount()) > 0){
			food.setSelected(true);
		}

		if(food.isSoldOut()){//代表沽清菜品
			convertView.setBackgroundResource(R.drawable.gq);//灰色
		}else{
			if(food.isSelected()){
				convertView.setBackgroundResource(R.drawable.table_buton_red);
				holder.countText.setVisibility(View.VISIBLE);
				holder.countText.setText(food.getPcount());
			}else{
				convertView.setBackgroundResource(R.drawable.table_buton_green);
				holder.countText.setVisibility(View.GONE);
			}
		}


		holder.textView.setText(food.getPcname());
		holder.minCntText.setText(food.getMinCnt());
		holder.maxCntText.setText(food.getMaxCnt());
		//手机下的菜品名称字体颜色为白色
		if (!ChioceActivity.ispad) {
			int imagesize = 0;
			AbsListView.LayoutParams layoutParams = null;
			imagesize = (int) (MainActivity.screenWidth * 0.205);
			//菜品右下角数量大小
			//菜品adapter背景大小
			layoutParams = new AbsListView.LayoutParams(
					imagesize,imagesize*4/5);
			convertView.setLayoutParams(layoutParams);
		}

		return convertView;
	}

	class TcGridHolder{

		TextView textView;
		TextView countText;
		TextView minCntText;
		TextView maxCntText;
	}

}
