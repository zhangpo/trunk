package cn.com.choicesoft.adapter;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.util.ValueUtil;

/**
 * 菜品适配器
 */
public class YiXuanDishesAdapter extends BaseAdapter {
	private Context context;
	private List<Food> yiDianDishes = null;//数据源
	

	public YiXuanDishesAdapter(Context context, List<Food> yiDianDishes) {
		super();
		this.context = context;
		this.yiDianDishes = yiDianDishes;
		setAllItemCheckedAndNotify(false);//初次显示，让所有的checkbox未选中
	}
	
	public void setAllItemChecked(boolean isChecked){
		if(yiDianDishes!=null) {
			for (int i = 0; i < yiDianDishes.size(); i++) {
				yiDianDishes.get(i).setSelected(isChecked);
			}
		}
	}
	
	public int getSelectedCount(){             //得到选中复选框的数量
		int count = 0;
		for(Food f : yiDianDishes){
			if(f.isSelected()){
				count++;
			}
		}
		return count;
	}
	
	public int getDishSelectedCount(){           //得到共点菜品的数量
		int count = 0;
		if(yiDianDishes != null){
			for(Food f : yiDianDishes){
				if(f.isIstc() && f.getPcode().equals(f.getTpcode()) || !f.isIstc()){
					count += ValueUtil.isNaNofDouble(f.getPcount());
				}
			}
		}
		return count;
	}
	
	public double getTotalSalary(){             //得到共点菜品的总价
		double totalSalary = 0.00;
		if(yiDianDishes != null){
			for(Food f : yiDianDishes){
				if(f.isIstc() && f.getTpcode().equals(f.getPcode()) || !f.isIstc()){
					double itemSubtotal = getItemSubtotal(f);
					totalSalary += itemSubtotal;
				}
			}
		}
		return totalSalary ;
	}
	
	/**
	 * 得到所有附加项的总价
	 * @return
	 */
	public double getFujiaSalary(){               
		double totalFujiaSalary = 0.00;
		if(yiDianDishes != null){
			for(Food f:yiDianDishes){
				if(ValueUtil.isNotEmpty(f.getFujiaprice())){//表明该菜品有附加项,自定义附加项价格为0，不考虑
					String [] fujiaStr = f.getFujiaprice().split("!");
					for(int i = 0;i<fujiaStr.length;i++){
						//如果是自定义附加项，价格为“”，就报错,必须判断一下
						totalFujiaSalary += Double.parseDouble(ValueUtil.isEmpty(fujiaStr[i])?"0":fujiaStr[i]);
					}
				}
			}
		}
		return totalFujiaSalary;
	}
	
	public void setAllItemCheckedAndNotify(boolean isChecked){
		setAllItemChecked(isChecked);
		notifyDataSetChanged();
	}
	
	public void toggleChecked(int position){
		getItem(position).setSelected(!getItem(position).isSelected());
	}

	public void setData(List<Food> dianDishes) {
		this.yiDianDishes = dianDishes;
	}

	@Override
	public int getCount() {
		return this.yiDianDishes == null ? 0 : this.yiDianDishes.size();
	}

	@Override
	public Food getItem(int position) {
		return yiDianDishes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Food dianDishes = yiDianDishes.get(position);
		final YiXuanViewHolder dianViewHolder;
		if (convertView == null) {
			dianViewHolder = new YiXuanViewHolder();
			if (ChioceActivity.ispad) {
				convertView = LayoutInflater.from(context).inflate(R.layout.yixuan_list_item_pad, null);
			} else {
				convertView = LayoutInflater.from(context).inflate(R.layout.yixuan_list_item, null);
			}
			
			dianViewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.yixuan_item_checkimage);
			dianViewHolder.hcCount = (TextView) convertView.findViewById(R.id.yixuan_item_hcCount);
			dianViewHolder.dishesCount = (TextView) convertView.findViewById(R.id.yixuan_item_dishesCount);
			dianViewHolder.markImage = (ImageView) convertView.findViewById(R.id.yixuan_item_iv_dishclear);
			dianViewHolder.name = (TextView) convertView.findViewById(R.id.yixuan_item_dishesname);
			dianViewHolder.count = (TextView) convertView.findViewById(R.id.yixuan_item_dishescount);
			dianViewHolder.price = (TextView) convertView.findViewById(R.id.yixuan_item_dishesprice);
			dianViewHolder.unit = (TextView) convertView.findViewById(R.id.yixuan_item_dishesunit);
			dianViewHolder.subtotal = (TextView) convertView.findViewById(R.id.yixuan_item_dishessubtotal);
			dianViewHolder.prompt = (TextView) convertView.findViewById(R.id.yixuan_item_dishesprompt);
			
			//附加项显示
			dianViewHolder.linearFujia = (LinearLayout) convertView.findViewById(R.id.yixuan_item_fujia_linear);
			dianViewHolder.fujiaText = (TextView) convertView.findViewById(R.id.yixuan_item_fujia_show);
			
			convertView.setTag(dianViewHolder);
		} else {
			dianViewHolder = (YiXuanViewHolder) convertView.getTag();
		}
		
		
		dianViewHolder.checkbox.setChecked(dianDishes.isSelected());
		if(dianDishes.getRushorcall()!=null&&dianDishes.getRushorcall().equals("1")){//即起
			dianViewHolder.checkbox.setTextColor(Color.BLUE);
			dianViewHolder.checkbox.setText(R.string.ji);
		}else if(dianDishes.getRushorcall()!=null&&dianDishes.getRushorcall().equals("2")){//叫起
			dianViewHolder.checkbox.setTextColor(Color.RED);
			dianViewHolder.checkbox.setText(R.string.jiao);
		}else if(dianDishes.getRushorcall()!=null&&dianDishes.getRushorcall().equals("")){
			dianViewHolder.checkbox.setTextColor(Color.YELLOW);
			dianViewHolder.checkbox.setText(R.string.yu);
		}
		BigDecimal big=new BigDecimal(ValueUtil.isNaNofDouble(dianDishes.getPcount()));
		big=big.subtract(BigDecimal.valueOf(ValueUtil.isNaNofDouble(dianDishes.getOver())));
		String hcCount=big.toString();
		dianViewHolder.hcCount.setText(hcCount.matches("-?\\d+$")?hcCount:big.intValue()+"");//划菜数量
		//对附加项的处理
		if(ValueUtil.isNotEmpty(dianDishes.getFujianame())){
			dianViewHolder.linearFujia.setVisibility(View.VISIBLE);
			dianViewHolder.fujiaText.setText(dianDishes.getFujianame().replace("!", " "));//将！替换成空格
		}else{
			dianViewHolder.linearFujia.setVisibility(View.GONE);
		}
		dianViewHolder.dishesCount.setText(dianDishes.getPcount());//菜品数量
		
		//设置划菜  
		if(ValueUtil.isNaNofDouble(dianDishes.getPcount()).equals(ValueUtil.isNaNofDouble(dianDishes.getOver()))){
			if (ChioceActivity.ispad) {
				dianViewHolder.markImage.setBackgroundResource(R.drawable.mark_item_diksh_pressed);
			}else {
				dianViewHolder.markImage.setBackgroundResource(R.drawable.huaxian);
			}
			
		}else{
			if (ChioceActivity.ispad) {
				dianViewHolder.markImage.setBackgroundResource(R.drawable.mark_item_diksh_normal);
			}else {
				dianViewHolder.markImage.setBackgroundResource(R.drawable.dialog_button_normal);
			}
		}
		
		dianViewHolder.count.setText(dianDishes.getPcount());
		//库没有用到Eachprice 如果不为空 price为合计 Eachprice 为单价
		dianViewHolder.price.setText(dianDishes.getEachprice()==null?dianDishes.getPrice():dianDishes.getEachprice());
		dianViewHolder.unit.setText(dianDishes.getUnit());
		
		//给小计赋值
		double itemSubtotal = getItemSubtotal(dianDishes);
		String dishName=dianDishes.getPcname();
		if(dianDishes.getIsTemp()==1){
			dishName=dishName+"--"+dianDishes.getTempName();
		}
		//对于套餐下的明细菜品，小计字段隐藏
		if(dianDishes.isIstc() && !dianDishes.getPcode().equals(dianDishes.getTpcode())){ 
			dianViewHolder.subtotal.setText("         ");
			dianViewHolder.name.setText("--"+dishName);
		}else{
			dianViewHolder.subtotal.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
			dianViewHolder.name.setText(dishName);
		}
		
		//给催菜赋值
		dianViewHolder.prompt.setText(context.getString(R.string.cui) + dianDishes.getUrge() +context.getString(R.string.ci));
		
		/*2014年3月17日 Mc 如果为赠品 菜品名后面加赠字和数量*/
		if(ValueUtil.isNaNofDouble(dianDishes.getPromonum())>0){
			String name=dianViewHolder.name.getText().toString();
			dianViewHolder.name.setText(name+"-"+context.getString(R.string.give)+dianDishes.getPromonum());
		}
		
		return convertView;
	}
	
	
	//在此处处理每道菜品的小计,如果涉及到赠菜就很复杂
	public double getItemSubtotal(Food pFood){
		return ValueUtil.isNaNDouble(pFood.getPrice());
			
	}


	public class YiXuanViewHolder {
		public CheckBox checkbox;
		TextView hcCount;//划菜数量
		TextView dishesCount;//菜品总数量
		ImageView markImage;
		TextView name;
		TextView count;
		TextView price;
		TextView unit;
		TextView subtotal;// 小计
		TextView prompt;//催菜
		
		LinearLayout linearFujia;//附加项的整体布局
		TextView fujiaText;//附加项的值
	}
	
}
