package cn.com.choicesoft.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.adapter.DishesGridViewAdapter;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.util.ConvertPinyin;
import cn.com.choicesoft.util.ValueUtil;

/**
 * 菜品Fragment
 *
 */

public class DishesFragment extends Fragment {
	public static final String ARG_PAGE = "page";
	public static final String DATA = "data";
	private  ArrayList<Food> homeDishes = new ArrayList<Food>();
	private ArrayList<Food> allDishes = null;
	
	private EditText editSearch;
	private ArrayList<Food> matchLists = new ArrayList<Food>();
	private GridView mainGridView;
	private DishesGridViewAdapter adapter;
	private String grp;
	
	private ScrollView mScrollView;
	private LinearLayout linearTCDetails;
	int len = 1;
	
//	int tpnumflag = 0;
	
	
	
	private DishItemClickListener mDishItemClickListener;
	
	
	public void setDishItemClickListener(DishItemClickListener pDishItemClickListener){
		mDishItemClickListener = pDishItemClickListener;
	}
	

	public static DishesFragment create(String grp,ArrayList<Food> allDishes) {
		DishesFragment fragment = new DishesFragment();
		 Bundle bundle = new Bundle();
		 bundle.putString("dishtype", grp);
		 bundle.putParcelableArrayList("alldishes", allDishes);
		 fragment.setArguments(bundle);
		 
		return fragment;
	}
	

	public DishesFragment() {}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.grp = getArguments().getString("dishtype");
		allDishes = getArguments().getParcelableArrayList("alldishes");
		for(Food food:allDishes){
			if(grp.equals(food.getSortClass())){
				homeDishes.add(food);
			}
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View layout = inflater.inflate(R.layout.food_fragment, null);
		editSearch = (EditText) layout.findViewById(R.id.foodfragment_editSearch);
		editSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				editSearch.setFocusable(true);
				editSearch.setFocusableInTouchMode(true);
				editSearch.setSelection(editSearch.length());// 设置光标在最后面
				editSearch.setTextColor(getResources().getColor(R.color.lightblue)); // 设置光标颜色
			}
		});
		editSearch.addTextChangedListener(watcher);
		mainGridView = (GridView) layout.findViewById(R.id.foodfragment_mainGridView);
		
		mScrollView = (ScrollView) layout.findViewById(R.id.foodfragment_scroll_tcdetails);
		linearTCDetails = (LinearLayout) layout.findViewById(R.id.foodfragment_linear_tcdetails);		
		
		adapter = new DishesGridViewAdapter(getActivity(), homeDishes);
		mainGridView.setAdapter(adapter);
		
		mainGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				mDishItemClickListener.dishTcLayoutDisplay(mScrollView,linearTCDetails);
				
				mDishItemClickListener.dishItemClickListener(parent, position, view, adapter.getListData().get(position));
				
			}
		});
		
		return layout;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {
			matchLists.clear();
			String changingStr = editSearch.getText().toString().trim();
			if(!ValueUtil.isEmpty(changingStr)){
				for(Food dishes:allDishes){
					String currentStr = dishes.getPcname();
					String numEncode = dishes.getPcode();
					if(currentStr.indexOf(changingStr)!=-1 || ConvertPinyin.convertJianPin(currentStr).indexOf(changingStr) !=-1 ||ConvertPinyin.convertQuanPin(currentStr).indexOf(changingStr)!=-1 || numEncode.indexOf(changingStr)!=-1){
						matchLists.add(dishes);
					}
				}
				if(adapter!=null){
					adapter.setListDataSource(matchLists);
					adapter.notifyDataSetChanged();
				}else{
					adapter = new DishesGridViewAdapter(getActivity(), matchLists);
					mainGridView.setAdapter(adapter);
				}
			}else{
				adapter.setListDataSource(homeDishes);
				adapter.notifyDataSetChanged();
			}
		}
	};
	
	
	public interface DishItemClickListener{
		
		void dishTcLayoutDisplay(ScrollView pmScrollView,LinearLayout mLinearLayout);
		
		void dishItemClickListener(AdapterView<?> parent,int position,View v,Object object);
	}
	
}


