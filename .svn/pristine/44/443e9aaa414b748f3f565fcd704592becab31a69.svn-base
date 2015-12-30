package cn.com.choicesoft.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.adapter.TaiWeiAdapter;
import cn.com.choicesoft.bean.Storetable;
import cn.com.choicesoft.impl.MainGridViewClickImpl;
import cn.com.choicesoft.util.ConvertPinyin;
import cn.com.choicesoft.util.ValueUtil;

/**
 * ̨λFragment
 *
 */
public class MainFragment extends Fragment {
	
	private TaiWeiAdapter adapter;
	private ArrayList<Storetable> mytaiWeis = new ArrayList<Storetable>();
	private ArrayList<Storetable> allTables = null;
	private String regionId;
	private ArrayList<Storetable> matchLists = new ArrayList<Storetable>();
	private MainGridViewClickImpl clickImpl;
	private LinearLayout linearLayout = null;
	
	private EditText editSearch;
	private GridView mainGridView;
	

	public TaiWeiAdapter getAdapter() {
		return adapter;
	}


	public static MainFragment create(String regionId,ArrayList<Storetable> altbls) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putString("regionId", regionId);
		args.putParcelableArrayList("alltbls", altbls);
		fragment.setArguments(args);
		return fragment;
	}

	public MainFragment() {}

	public void setClickImpl(MainGridViewClickImpl click) {
		clickImpl = click;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.regionId = getArguments().getString("regionId");
		allTables = getArguments().getParcelableArrayList("alltbls");
		for(Storetable table:allTables){
			if(table.getArearid().equals(regionId)){
				mytaiWeis.add(table);
			}
		}
	}


	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {    

		linearLayout = (LinearLayout) inflater.inflate(R.layout.taiwei_fragment, null);
		mainGridView = (GridView) linearLayout.findViewById(R.id.mainGridView);
		editSearch = (EditText) linearLayout.findViewById(R.id.editSearch);
		editSearch.addTextChangedListener(watcher);
		
		if(mytaiWeis.size() == 0){
			Toast.makeText(getActivity(), R.string.is_area_not_table, Toast.LENGTH_SHORT).show();
		}
		adapter = new TaiWeiAdapter(getActivity(), mytaiWeis);
		mainGridView.setAdapter(adapter);
		mainGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				clickImpl.setOnTypeViewClick(parent,position, view,adapter.getListData().get(position));

			}
		});

		return linearLayout;

	}
	
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {
			String inputStr = editSearch.getText().toString().trim();
			matchLists.clear();
			if(!ValueUtil.isEmpty(inputStr)){
				for(Storetable table:allTables){
					String tblName = table.getTblname();
					if(tblName.indexOf(inputStr)!=-1 || ConvertPinyin.convertJianPin(tblName).indexOf(inputStr)!=-1 || ConvertPinyin.convertQuanPin(tblName).indexOf(inputStr)!=-1){
						
						matchLists.add(table);
						if(adapter != null){
							adapter.setData(matchLists);
							adapter.notifyDataSetChanged();
						}else{
							adapter = new TaiWeiAdapter(getActivity(), matchLists);
							mainGridView.setAdapter(adapter);
						}
					}
				}
			}else{
				adapter.setData(mytaiWeis);
				adapter.notifyDataSetChanged();
			}
		}
	};

}
