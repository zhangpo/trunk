package cn.com.choicesoft.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.com.choicesoft.R;

/**
 * µ„≤À≈≈––
 *
 */
public class OrderFragment extends Fragment {
	private static final String TAG = "OrderFragment";
	private String tableNum;

	public static OrderFragment newInstance(String tableNum) {
		OrderFragment newFragment = new OrderFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tableNum", tableNum);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "TestFragment-----onCreate");
		Bundle args = getArguments();
		tableNum = args != null ? args.getString("tableNum") : "0";
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
        view = inflater.inflate(R.layout.order_fragment, container, false);
        ListView orderList = (ListView) view.findViewById(R.id.order_list);
        return view;
	}
	
	public class OrderAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			return null;
		}
		
	}
	
}
