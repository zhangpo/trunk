package cn.com.choicesoft.chinese.adapter;

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
 *
 */
public class ChineseYiXuanDishesAdapter extends BaseAdapter {
    private Context context;
    private List<Food> yiDianDishes = null;//数据源
    private OnBtnClickCallback l;


    public ChineseYiXuanDishesAdapter(Context context, List<Food> yiDianDishes) {
        super();
        this.context = context;
        this.yiDianDishes = yiDianDishes;
        setAllItemCheckedAndNotify(false);//初次显示，让所有的checkbox未选中
    }

    public void setOnBtnClickCallback(OnBtnClickCallback callback){
        this.l = callback;
    }

    public void setAllItemChecked(boolean isChecked){
        for(int i = 0; i<yiDianDishes.size(); i++){
            yiDianDishes.get(i).setSelected(isChecked);
        }
    }


    //得到选中复选框的数量
    public int getSelectedCount(){
        int count = 0;
        for(Food f : yiDianDishes){
            if(f.isSelected()){
                count++;
            }
        }
        return count;
    }

    /**
     * 在全单页面，必须修改了所有第二单位的数量之后,才可以结算
     * @author Young
     * @created 2014-11-21
     * @comment
     * @return
     */
    public boolean isModifyAllCnt(){//TODO
        boolean isModifyAll = true;
        for(Food f : yiDianDishes){
            if(f.getPlusd()!=0 && Double.parseDouble(f.getUnit2Cnt())==0){
                isModifyAll = false;
                break;
            }
        }
        return isModifyAll;
    }

    /**
     * 退菜或是修改菜品的数量时，同一时刻，只能操作一道菜品
     * 该函数就是获取那道选中的菜品
     *
     * @author Young
     * @created 2014-10-24
     * @comment
     * @return
     */
    public Food getSelectedItem(){
        Food selectedFood = null;
        for(Food f : yiDianDishes){
            if(f.isSelected()){
                selectedFood = f;
                break;
            }
        }
        return selectedFood;
    }

    public int getDishSelectedCount(){//得到共点菜品的数量
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

    //得到共点菜品的总价
    public double getTotalSalary(){
        double totalSalary = 0.00;
        if(yiDianDishes != null){
            for(Food f : yiDianDishes){
                if(!f.isIstc()){
                    if(ValueUtil.isNotEmpty(f.getUnit2()) && f.getModifyFlag().equals("N"))
                        continue;

//                    double itemSubtotal = getItemSubtotal(f);
                    totalSalary += f.getSubtotal();
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
        final ChineseYiXuanViewHolder dianViewHolder;
        if (convertView == null) {
            dianViewHolder = new ChineseYiXuanViewHolder();
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

            dianViewHolder.presentImgV = (ImageView) convertView.findViewById(R.id.yixun_item_zeng_imgv);//赠送菜品按钮

            //附加项显示
            dianViewHolder.linearFujia = (LinearLayout) convertView.findViewById(R.id.yixuan_item_fujia_linear);
            dianViewHolder.fujiaText = (TextView) convertView.findViewById(R.id.yixuan_item_fujia_show);

            convertView.setTag(dianViewHolder);
        } else {
            dianViewHolder = (ChineseYiXuanViewHolder) convertView.getTag();
        }

        dianViewHolder.checkbox.setChecked(dianDishes.isSelected());
        if(dianDishes.getRushorcall()!=null && dianDishes.getRushorcall().equals("1")){//即起
            dianViewHolder.checkbox.setTextColor(Color.BLUE);
            dianViewHolder.checkbox.setText(R.string.ji);
        }else if(dianDishes.getRushorcall()!=null &&dianDishes.getRushorcall().equals("2")){//叫起
            dianViewHolder.checkbox.setTextColor(Color.RED);
            dianViewHolder.checkbox.setText(R.string.jiao);
        }else if(dianDishes.getRushorcall()!=null&&dianDishes.getRushorcall().equals("3")){
            dianViewHolder.checkbox.setTextColor(Color.GREEN);
            dianViewHolder.checkbox.setText(R.string.qi);
        }else if(dianDishes.getRushorcall()!=null && dianDishes.getRushorcall().equals("")){
            dianViewHolder.checkbox.setTextColor(Color.YELLOW);
            dianViewHolder.checkbox.setText(R.string.yu);
        }

        BigDecimal big = BigDecimal.valueOf(ValueUtil.isNaNofDouble(dianDishes.getPcount()));
        big=big.subtract(BigDecimal.valueOf(ValueUtil.isNaNofDouble(dianDishes.getOver())));
        String hcCount = big.toString();
        dianViewHolder.hcCount.setText(hcCount.matches("-?\\d+$")?hcCount:big.intValue()+"");//划菜数量
        //对附加项的处理
        if(ValueUtil.isNotEmpty(dianDishes.getFujianame())){
            dianViewHolder.linearFujia.setVisibility(View.VISIBLE);
            dianViewHolder.fujiaText.setText(dianDishes.getFujianame().replace("!", " "));//将！替换成空格
        }else{
            dianViewHolder.linearFujia.setVisibility(View.GONE);
        }
//		dianViewHolder.dishesCount.setText(dianDishes.getPcount());//菜品数量
        if(dianDishes.getPlusd()==1&&Double.parseDouble(dianDishes.getPcount())==0){
            dianViewHolder.dishesCount.setText(ValueUtil.isEmpty(dianDishes.getUnit2())?dianDishes.getPcount():dianDishes.getUnit2Cnt());
        }else {
            dianViewHolder.dishesCount.setText(dianDishes.getPcount());
        }

        //设置划菜
        if(ValueUtil.isNotEmpty(dianDishes.getOver()) && ValueUtil.isNaNofDouble(dianDishes.getPcount()).equals(ValueUtil.isNaNofDouble(dianDishes.getOver()))){
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

//		dianViewHolder.count.setText(dianDishes.getPcount());
//		Log.i("#######", dianDishes.toString());
        dianViewHolder.count.setText(ValueUtil.isEmpty(dianDishes.getUnit2())?dianDishes.getPcount():dianDishes.getUnit2Cnt());
        //库没有用到Eachprice 如果不为空 price为合计 Eachprice 为单价
        dianViewHolder.price.setText(dianDishes.getEachprice()==null?dianDishes.getPrice():dianDishes.getEachprice());

        //此处判断显示第一单位还是第二单位
        if(ValueUtil.isEmpty(dianDishes.getUnit2())){
            dianViewHolder.unit.setText(dianDishes.getUnit());
        }else if(ValueUtil.isNotEmpty(dianDishes.getUnit2()) && dianDishes.getModifyFlag().equals("N")){
            dianViewHolder.unit.setText(dianDishes.getUnit2());
        }else if(ValueUtil.isNotEmpty(dianDishes.getUnit2()) && dianDishes.getModifyFlag().equals("Y")){
            dianViewHolder.unit.setText(dianDishes.getUnit());
        }


        //给小计赋值
        double itemSubtotal = dianDishes.getSubtotal();
		/*//对于套餐下的明细菜品，小计字段隐藏
		if(dianDishes.isIstc() && !dianDishes.getPcode().equals(dianDishes.getTpcode())){
			dianViewHolder.subtotal.setText("         ");
			dianViewHolder.name.setText("--"+dianDishes.getPcname());
		}else{
			dianViewHolder.subtotal.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
			dianViewHolder.name.setText((ValueUtil.isEmpty(dianDishes.getTpname())?"":dianDishes.getTpname()+"--")+dianDishes.getPcname());
		}*/

        //西贝没有套餐
        if(!dianDishes.isIstc()){
            if(dianDishes.getPlusd()==1 && Double.parseDouble(dianDishes.getUnit2Cnt())==0){//第二单位，修改数量之前
                dianViewHolder.subtotal.setText("0.00");
                dianViewHolder.name.setText("--"+dianDishes.getPcname());
            }else if(dianDishes.getPlusd()==1){//第二单位，修改数量之后
                dianViewHolder.subtotal.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
                dianViewHolder.name.setText("--"+dianDishes.getPcname());
            }else{//第一单位
                dianViewHolder.subtotal.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
                dianViewHolder.name.setText("--"+dianDishes.getPcname());
            }
        }

        //给催菜赋值
        dianViewHolder.prompt.setText(context.getString(R.string.cui) + dianDishes.getUrge() +context.getString(R.string.ci));

        dianViewHolder.presentImgV.setOnClickListener(new PresentClick(dianDishes));//为赠送菜品按钮设置点击事件

		/*2014年3月17日 Mc 如果为赠品 菜品名后面加赠字和数量*/
//		if(ValueUtil.isNaNofDouble(dianDishes.getPromonum()) > 0){
//			String name = dianViewHolder.name.getText().toString();
//			dianViewHolder.name.setText(name+"-"+context.getString(R.string.give)+dianDishes.getPromonum());
//		}
        if(ValueUtil.isEmpty(dianDishes.getPromonum()) || dianDishes.getPromonum().equals("0.0")){
            dianViewHolder.name.setText(dianDishes.getPcname());
        }else{
            dianViewHolder.name.setText(new StringBuilder().append(dianDishes.getPcname()).append("-赠").append(dianDishes.getPromonum()));
        }

        return convertView;
    }


    public static class ChineseYiXuanViewHolder {
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
        ImageView presentImgV;//赠菜
        LinearLayout linearFujia;//附加项的整体布局
        TextView fujiaText;//附加项的值
    }

    private class PresentClick implements View.OnClickListener{

        private Food foodCur = null;

        public PresentClick(Food foodCur) {
            super();
            this.foodCur = foodCur;
        }

        @Override
        public void onClick(View view) {
            l.btnClick(foodCur);
        }

    }



    public interface OnBtnClickCallback{
        void btnClick(Food food);
    }

}
