package cn.com.choicesoft.singletaiwei;

import android.os.Bundle;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.BaseActivity;
import cn.com.choicesoft.util.ExitApplication;

/**
 * Ω·À„“≥√Ê
 *
 */
public class SettlementActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.settlement_activity);
	}
}
