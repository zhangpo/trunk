package cn.com.choicesoft.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.util.ValueUtil;

import java.util.List;
import java.util.Map;

/**
 * M¡£c
 * 2015-06-04
 * Jnwsczh@163.com
 */
public class OrderPayAdapter extends BaseAdapter {


    private List<Map<String, String>> list;
    private Context myContext;

    public OrderPayAdapter(Context context, List<Map<String, String>> list) {
        this.myContext = context;
        this.list = list;
    }

    public List<Map<String, String>> getListData() {
        return list;
    }

    public void setListDataSource(List<Map<String, String>> list) {
        this.list = list;
    }
    @Override
    public int getCount() {
        return ValueUtil.isEmpty(this.list) ? 0 : this.list.size();
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
        Map<String, String> map = list.get(position);
        convertView = LayoutInflater.from(myContext).inflate(R.layout.order_pay_list, null);
        TextView number = (TextView) convertView.findViewById(R.id.order_pay_number);
        TextView table = (TextView) convertView.findViewById(R.id.order_pay_tablenum);
        number.setText(map.get("ORDERID"));
        table.setText(map.get("TABLENUM"));
        return convertView;
    }
}
