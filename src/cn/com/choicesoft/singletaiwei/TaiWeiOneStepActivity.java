package cn.com.choicesoft.singletaiwei;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.BaseActivity;
import cn.com.choicesoft.util.ExitApplication;

/**
 * 台位显示
 * 
 */
public class TaiWeiOneStepActivity extends BaseActivity implements OnClickListener {

	private EditText kaitaiNumber;
	private Button oneStepConfirm, oneStepCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.kaitai);
		getDataByIntent();
		findView();
		setOnClick();
	}

	private void getDataByIntent(){
		Intent intent = getIntent();
		kaitaiNumber.setText(intent.getStringExtra("number"));
	}
	
	private void findView() {
		kaitaiNumber = (EditText) findViewById(R.id.kaitaiNumber);
		oneStepConfirm = (Button) findViewById(R.id.oneStepConfirm);
		oneStepCancel = (Button) findViewById(R.id.oneStepCancel);
	}

	private void setOnClick() {
		oneStepConfirm.setOnClickListener(this);
		oneStepCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(oneStepConfirm.getId()==v.getId()){
			//TODO  确定
		}else if(oneStepCancel.getId()==v.getId()){
			//TODO  取消
		}
	}

}
