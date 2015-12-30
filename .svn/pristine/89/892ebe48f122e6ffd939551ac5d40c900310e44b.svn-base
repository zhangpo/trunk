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
 * È«²¿²ËÆ·
 * 
 */
public class AllDishesFragment extends Fragment {
	private static final String TAG = "OrderFragment";
	private String taiweiNum;

	public static OrderFragment newInstance(String taiweiNum) {
		OrderFragment newFragment = new OrderFragment();
		Bundle bundle = new Bundle();
		bundle.putString("taiweiNum", taiweiNum);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "TestFragment-----onCreate");
		Bundle args = getArguments();
		taiweiNum = args != null ? args.getString("taiweiNum") : "0";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.center_fragment, container, false);
		ListView leftList = (ListView) view.findViewById(R.id.left_list);
		ListView rightList = (ListView) view.findViewById(R.id.right_list);
		leftList.setAdapter(new LeftAdapter());
		rightList.setAdapter(new RightAdapter());
		return view;
	}

	public class LeftAdapter extends BaseAdapter {

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
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public class RightAdapter extends BaseAdapter {

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
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
