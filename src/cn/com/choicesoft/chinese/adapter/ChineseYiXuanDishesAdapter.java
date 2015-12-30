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
 * ��Ʒ������
 *
 */
public class ChineseYiXuanDishesAdapter extends BaseAdapter {
    private Context context;
    private List<Food> yiDianDishes = null;//����Դ
    private OnBtnClickCallback l;


    public ChineseYiXuanDishesAdapter(Context context, List<Food> yiDianDishes) {
        super();
        this.context = context;
        this.yiDianDishes = yiDianDishes;
        setAllItemCheckedAndNotify(false);//������ʾ�������е�checkboxδѡ��
    }

    public void setOnBtnClickCallback(OnBtnClickCallback callback){
        this.l = callback;
    }

    public void setAllItemChecked(boolean isChecked){
        for(int i = 0; i<yiDianDishes.size(); i++){
            yiDianDishes.get(i).setSelected(isChecked);
        }
    }


    //�õ�ѡ�и�ѡ�������
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
     * ��ȫ��ҳ�棬�����޸������еڶ���λ������֮��,�ſ��Խ���
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
     * �˲˻����޸Ĳ�Ʒ������ʱ��ͬһʱ�̣�ֻ�ܲ���һ����Ʒ
     * �ú������ǻ�ȡ�ǵ�ѡ�еĲ�Ʒ
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

    public int getDishSelectedCount(){//�õ������Ʒ������
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

    //�õ������Ʒ���ܼ�
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
     * �õ����и�������ܼ�
     * @return
     */
    public double getFujiaSalary(){
        double totalFujiaSalary = 0.00;
        if(yiDianDishes != null){
            for(Food f:yiDianDishes){
                if(ValueUtil.isNotEmpty(f.getFujiaprice())){//�����ò�Ʒ�и�����,�Զ��帽����۸�Ϊ0��������
                    String [] fujiaStr = f.getFujiaprice().split("!");
                    for(int i = 0;i<fujiaStr.length;i++){
                        //������Զ��帽����۸�Ϊ�������ͱ���,�����ж�һ��
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

            dianViewHolder.presentImgV = (ImageView) convertView.findViewById(R.id.yixun_item_zeng_imgv);//���Ͳ�Ʒ��ť

            //��������ʾ
            dianViewHolder.linearFujia = (LinearLayout) convertView.findViewById(R.id.yixuan_item_fujia_linear);
            dianViewHolder.fujiaText = (TextView) convertView.findViewById(R.id.yixuan_item_fujia_show);

            convertView.setTag(dianViewHolder);
        } else {
            dianViewHolder = (ChineseYiXuanViewHolder) convertView.getTag();
        }

        dianViewHolder.checkbox.setChecked(dianDishes.isSelected());
        if(dianDishes.getRushorcall()!=null && dianDishes.getRushorcall().equals("1")){//����
            dianViewHolder.checkbox.setTextColor(Color.BLUE);
            dianViewHolder.checkbox.setText(R.string.ji);
        }else if(dianDishes.getRushorcall()!=null &&dianDishes.getRushorcall().equals("2")){//����
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
        dianViewHolder.hcCount.setText(hcCount.matches("-?\\d+$")?hcCount:big.intValue()+"");//��������
        //�Ը�����Ĵ���
        if(ValueUtil.isNotEmpty(dianDishes.getFujianame())){
            dianViewHolder.linearFujia.setVisibility(View.VISIBLE);
            dianViewHolder.fujiaText.setText(dianDishes.getFujianame().replace("!", " "));//�����滻�ɿո�
        }else{
            dianViewHolder.linearFujia.setVisibility(View.GONE);
        }
//		dianViewHolder.dishesCount.setText(dianDishes.getPcount());//��Ʒ����
        if(dianDishes.getPlusd()==1&&Double.parseDouble(dianDishes.getPcount())==0){
            dianViewHolder.dishesCount.setText(ValueUtil.isEmpty(dianDishes.getUnit2())?dianDishes.getPcount():dianDishes.getUnit2Cnt());
        }else {
            dianViewHolder.dishesCount.setText(dianDishes.getPcount());
        }

        //���û���
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
        //��û���õ�Eachprice �����Ϊ�� priceΪ�ϼ� Eachprice Ϊ����
        dianViewHolder.price.setText(dianDishes.getEachprice()==null?dianDishes.getPrice():dianDishes.getEachprice());

        //�˴��ж���ʾ��һ��λ���ǵڶ���λ
        if(ValueUtil.isEmpty(dianDishes.getUnit2())){
            dianViewHolder.unit.setText(dianDishes.getUnit());
        }else if(ValueUtil.isNotEmpty(dianDishes.getUnit2()) && dianDishes.getModifyFlag().equals("N")){
            dianViewHolder.unit.setText(dianDishes.getUnit2());
        }else if(ValueUtil.isNotEmpty(dianDishes.getUnit2()) && dianDishes.getModifyFlag().equals("Y")){
            dianViewHolder.unit.setText(dianDishes.getUnit());
        }


        //��С�Ƹ�ֵ
        double itemSubtotal = dianDishes.getSubtotal();
		/*//�����ײ��µ���ϸ��Ʒ��С���ֶ�����
		if(dianDishes.isIstc() && !dianDishes.getPcode().equals(dianDishes.getTpcode())){
			dianViewHolder.subtotal.setText("         ");
			dianViewHolder.name.setText("--"+dianDishes.getPcname());
		}else{
			dianViewHolder.subtotal.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
			dianViewHolder.name.setText((ValueUtil.isEmpty(dianDishes.getTpname())?"":dianDishes.getTpname()+"--")+dianDishes.getPcname());
		}*/

        //����û���ײ�
        if(!dianDishes.isIstc()){
            if(dianDishes.getPlusd()==1 && Double.parseDouble(dianDishes.getUnit2Cnt())==0){//�ڶ���λ���޸�����֮ǰ
                dianViewHolder.subtotal.setText("0.00");
                dianViewHolder.name.setText("--"+dianDishes.getPcname());
            }else if(dianDishes.getPlusd()==1){//�ڶ���λ���޸�����֮��
                dianViewHolder.subtotal.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
                dianViewHolder.name.setText("--"+dianDishes.getPcname());
            }else{//��һ��λ
                dianViewHolder.subtotal.setText(ValueUtil.setScale(itemSubtotal, 2, 5));
                dianViewHolder.name.setText("--"+dianDishes.getPcname());
            }
        }

        //���߲˸�ֵ
        dianViewHolder.prompt.setText(context.getString(R.string.cui) + dianDishes.getUrge() +context.getString(R.string.ci));

        dianViewHolder.presentImgV.setOnClickListener(new PresentClick(dianDishes));//Ϊ���Ͳ�Ʒ��ť���õ���¼�

		/*2014��3��17�� Mc ���Ϊ��Ʒ ��Ʒ����������ֺ�����*/
//		if(ValueUtil.isNaNofDouble(dianDishes.getPromonum()) > 0){
//			String name = dianViewHolder.name.getText().toString();
//			dianViewHolder.name.setText(name+"-"+context.getString(R.string.give)+dianDishes.getPromonum());
//		}
        if(ValueUtil.isEmpty(dianDishes.getPromonum()) || dianDishes.getPromonum().equals("0.0")){
            dianViewHolder.name.setText(dianDishes.getPcname());
        }else{
            dianViewHolder.name.setText(new StringBuilder().append(dianDishes.getPcname()).append("-��").append(dianDishes.getPromonum()));
        }

        return convertView;
    }


    public static class ChineseYiXuanViewHolder {
        public CheckBox checkbox;
        TextView hcCount;//��������
        TextView dishesCount;//��Ʒ������
        ImageView markImage;
        TextView name;
        TextView count;
        TextView price;
        TextView unit;
        TextView subtotal;// С��
        TextView prompt;//�߲�
        ImageView presentImgV;//����
        LinearLayout linearFujia;//����������岼��
        TextView fujiaText;//�������ֵ
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
