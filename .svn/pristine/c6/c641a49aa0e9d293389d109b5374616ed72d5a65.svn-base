package cn.com.choicesoft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.util.ExitApplication;
import cn.com.choicesoft.util.SharedPreferencesUtils;

/**
 * App…Ë÷√
 */
public class ServerConfig extends BaseActivity implements OnClickListener{
	
	private Button cancel,certain;
	private TextView back;
	private EditText ipEdit;
	private EditText portEdit;
	
	private String ipText;
	private String portText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.serverconfig);
		
		initView();
		initListener();
		
	}

	private void initListener() {
		back.setOnClickListener(this);
		cancel.setOnClickListener(this);
		certain.setOnClickListener(this);
	}

	private void initView() {
		back = (TextView) this.findViewById(R.id.title_tv_finish);
		cancel = (Button) this.findViewById(R.id.serverconfig_btn_cancel);
		certain = (Button) this.findViewById(R.id.serverconfig_btn_certain);
		
		ipEdit = (EditText) this.findViewById(R.id.serverconfig_et_ip);
		portEdit = (EditText) this.findViewById(R.id.serverconfig_et_port);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_tv_finish:
			this.finish();
			break;

		case R.id.serverconfig_btn_cancel:
			this.finish();
			break;
			
		case R.id.serverconfig_btn_certain:
			if(validate()){
				final Intent intent = new Intent(ServerConfig.this, WelcomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				Toast.makeText(this, R.string.setting_success, Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	private boolean validate(){
		ipText = ipEdit.getText().toString().trim();
		portText = portEdit.getText().toString().trim();
		if(ipText.isEmpty()){
			Toast.makeText(this, R.string.ip_not_null, Toast.LENGTH_SHORT).show();
			return false;
		}else if(portText.isEmpty()){
			Toast.makeText(this, R.string.port_not_null, Toast.LENGTH_SHORT).show();
			return false;
		}else{
			StringBuilder builder = new StringBuilder();
			builder.append(ipText).append(":").append(portText);
			SharedPreferencesUtils.setServerAddress(this, builder.toString());
			return true;
		}
	}
}
