package cn.com.choicesoft.singletaiwei;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.BaseActivity;
import cn.com.choicesoft.util.ExitApplication;

/**
 * 台位显示
 * 
 */
public class TaiWeiActivity extends BaseActivity implements OnClickListener {

	private TextView title;
	
	private Button one, two, three, four, five, six, seven, eight, nine, zero;
	private Button btnGuoDi, btnLiangCai, btnRouLei, btnXiaoLiao, btnTaocan,
			btnJiuShui;
	private ListView listView;
	private GridView gridView;
	private TextView nodata;
	private EditText editSearch;
	private Button btnSearch;

	private Button btnBack, btnYiDian;

	// 以何种样式显示，ListView或者是GridView
	// true--gridview，false--listview
	private boolean isListOrGrid = false;

	private FrameLayout container;
	private View showView = null;
	private String number;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.taiwei_activity);
		number = getIntent().getStringExtra("info");
		findView();
		setOnClick();
	}

	private void findView() {
		title = (TextView) findViewById(R.id.title);
		title.setText(number);
		one = (Button) findViewById(R.id.one);
		two = (Button) findViewById(R.id.two);
		three = (Button) findViewById(R.id.three);
		four = (Button) findViewById(R.id.four);
		five = (Button) findViewById(R.id.five);
		six = (Button) findViewById(R.id.six);
		seven = (Button) findViewById(R.id.seven);
		eight = (Button) findViewById(R.id.eight);
		nine = (Button) findViewById(R.id.nine);
		zero = (Button) findViewById(R.id.zero);

		btnGuoDi = (Button) findViewById(R.id.guodi);
		btnLiangCai = (Button) findViewById(R.id.liangcai);
		btnRouLei = (Button) findViewById(R.id.roulei);
		btnXiaoLiao = (Button) findViewById(R.id.xiaoliao);
		btnTaocan = (Button) findViewById(R.id.taocan);
		btnJiuShui = (Button) findViewById(R.id.jiushui);

		editSearch = (EditText) findViewById(R.id.editSearch);
		btnSearch = (Button) findViewById(R.id.btnSearch);

		listView = (ListView) findViewById(R.id.listview);
		gridView = (GridView) findViewById(R.id.gridview);
		nodata = (TextView) findViewById(R.id.nodata);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnYiDian = (Button) findViewById(R.id.btnYiDian);
	}

	private void setOnClick() {
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);
		six.setOnClickListener(this);
		seven.setOnClickListener(this);
		eight.setOnClickListener(this);
		nine.setOnClickListener(this);
		zero.setOnClickListener(this);

		btnGuoDi.setOnClickListener(this);
		btnLiangCai.setOnClickListener(this);
		btnRouLei.setOnClickListener(this);
		btnXiaoLiao.setOnClickListener(this);
		btnTaocan.setOnClickListener(this);
		btnJiuShui.setOnClickListener(this);

		btnSearch.setOnClickListener(this);

		btnBack.setOnClickListener(this);
		btnYiDian.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == one.getId()) {
			number = "1";
		} else if (v.getId() == two.getId()) {
			number = "2";
		} else if (v.getId() == three.getId()) {
			number = "3";
		} else if (v.getId() == four.getId()) {
			number = "4";
		} else if (v.getId() == five.getId()) {
			number = "5";
		} else if (v.getId() == six.getId()) {
			number = "6";
		} else if (v.getId() == seven.getId()) {
			number = "7";
		} else if (v.getId() == eight.getId()) {
			number = "8";
		} else if (v.getId() == nine.getId()) {
			number = "9";
		} else if (v.getId() == zero.getId()) {
			number = "0";
		} else if (v.getId() == btnGuoDi.getId()) {

		} else if (v.getId() == btnLiangCai.getId()) {

		} else if (v.getId() == btnRouLei.getId()) {

		} else if (v.getId() == btnXiaoLiao.getId()) {

		} else if (v.getId() == btnTaocan.getId()) {

		} else if (v.getId() == btnJiuShui.getId()) {

		} else if (v.getId() == btnSearch.getId()) {
			String mySearchText = editSearch.getText().toString().trim();
			if (mySearchText.length() == 0) {
				Toast.makeText(TaiWeiActivity.this, R.string.please_input_search_content,Toast.LENGTH_SHORT).show();
				return;
			} else {
				// TODO 根据输入内容，去数据库中查询,是否查询到了数据，isHaveData
				boolean isHaveData = false;
				if(isHaveData){
					gridView.setVisibility(View.GONE);
					listView.setVisibility(View.GONE);
					nodata.setVisibility(View.VISIBLE);
				}else{
					// true--gridview，false--listview
					if (isListOrGrid) {
						gridView.setVisibility(View.VISIBLE);
						listView.setVisibility(View.GONE);
						nodata.setVisibility(View.GONE);
					} else {
						listView.setVisibility(View.VISIBLE);
						gridView.setVisibility(View.GONE);
						nodata.setVisibility(View.GONE);
					}
				}
				
			}
		} else if (v.getId() == btnBack.getId()) {

		} else if (v.getId() == btnYiDian.getId()) {

		}

	}

}
