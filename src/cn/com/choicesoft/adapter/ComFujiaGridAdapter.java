package cn.com.choicesoft.adapter;

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
import cn.com.choicesoft.bean.Addition;
import cn.com.choicesoft.util.ValueUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 附加项弹出界面适配器
 */
public class ComFujiaGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Addition> mLists;
    private LayoutInflater inflater;

    private List<Addition> exceptFirstLists = new ArrayList<Addition>();

    public Addition getDefaultsAddition(){//TODO 快餐错误 待修改 应为↓
        for(Addition addition:mLists){
            if(ValueUtil.isNaNofInteger(addition.getDefualts())==0){
                return addition;
            }
        }
        return mLists.get(0);
    }

    /**
     * 获取该组所选中的对象
     * @return
     */
    public List<Addition> getSelectedAdditionLists(){
        List<Addition> selectedLists = new ArrayList<Addition>();
        for(Addition mAddition:exceptFirstLists){
            if(mAddition.isSelected()){
                selectedLists.add(mAddition);
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
            for(Addition mAddition :exceptFirstLists){
                if(ValueUtil.isNotEmpty(mAddition.getPcount())){
                    countsFood += Integer.parseInt(mAddition.getPcount());
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
        return getCountsAboutPcount() >= Integer.parseInt(getDefaultsAddition().getMinCnt());
    }

    public ComFujiaGridAdapter(Context pContext, List<Addition> pLists) {
        super();
        this.mContext = pContext;
        this.mLists = pLists;
        inflater = LayoutInflater.from(mContext);

        for(Addition addition:pLists){
            if(ValueUtil.isNaNDouble(addition.getDefualts())!=0){//||addition.getFoodFuJia_Id()!=addition.getProducttc_order()
                exceptFirstLists.add(addition);
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
        Addition addition = exceptFirstLists.get(position);
        TcGridHolder holder = null;
        if(convertView == null){
            holder = new TcGridHolder();
            if (ChioceActivity.ispad) {
                convertView = inflater.inflate(R.layout.comfujia_dishes_subgridview_item_pad, null);
            } else {
                convertView = inflater.inflate(R.layout.comfujia_dishes_subgridview_item, null);
            }
            holder.textView = (TextView) convertView.findViewById(R.id.subgridview_item_name);
            holder.countText = (TextView) convertView.findViewById(R.id.subgridview_item_count);


            convertView.setTag(holder);
        }else{
            holder = (TcGridHolder) convertView.getTag();
        }

        if(ValueUtil.isNotEmpty(addition.getPcount()) && Integer.parseInt(addition.getPcount()) > 0){
            addition.setSelected(true);
        }

        if(addition.isSelected()){
            convertView.setBackgroundResource(R.drawable.table_buton_red);
            holder.countText.setVisibility(View.VISIBLE);
            holder.countText.setText(addition.getPcount());
        }else{
            convertView.setBackgroundResource(R.drawable.table_buton_green);
            holder.countText.setVisibility(View.GONE);
        }



        holder.textView.setText(addition.getFoodFuJia_des());
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
    }

}
