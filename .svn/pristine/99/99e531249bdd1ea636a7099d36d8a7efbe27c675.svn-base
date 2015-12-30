package cn.com.choicesoft.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.util.Map;

/**
 * APP设置界面
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
	
	private TextView finish;
	private TextView middleTitle;
	private RelativeLayout serverConnect;
	private RelativeLayout register;
	private RelativeLayout deviceNumber;
	private TextView physicalNumber;
	
	private Server server;
	
	private LayoutInflater inflater;
	private LoadingDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.setting_activity);
		initView();
		
		initListener();
		initData();
	}

	private void initData() {
		middleTitle.setText(R.string.app_setting);
		server = new Server();
		
		mDialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
		
		inflater = LayoutInflater.from(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		String uniqueCode = SharedPreferencesUtils.getPhysicalId(this);
		if(!ValueUtil.isEmpty(uniqueCode)){
			physicalNumber.setText(uniqueCode);
		}

	}

	private void initListener() {
		finish.setOnClickListener(this);
		serverConnect.setOnClickListener(this);
		register.setOnClickListener(this);
		deviceNumber.setOnClickListener(this);
	}

	private void initView() {
		finish = (TextView) this.findViewById(R.id.title_tv_finish);
		middleTitle = (TextView) this.findViewById(R.id.title_tv_middletitle);
		serverConnect = (RelativeLayout) this.findViewById(R.id.setting_relative_ipconfig);
		register = (RelativeLayout) this.findViewById(R.id.setting_relative_register);
		deviceNumber = (RelativeLayout) this.findViewById(R.id.setting_relative_deviceNumber);
		physicalNumber = (TextView) this.findViewById(R.id.setting_tv_uniqueCode);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_tv_finish:
			this.finish();
			break;
			
		case R.id.setting_relative_deviceNumber:
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle(R.string.dia_encodesetting_bianhao_hint);
			View view = inflater.inflate(R.layout.device_number_layout, null);
			final EditText editText = (EditText) view.findViewById(R.id.devicenumber_et_layout);
			builder.setView(view);
			builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String deviceNumber = editText.getText().toString();
					if(ValueUtil.isEmpty(deviceNumber)){
						Toast.makeText(SettingActivity.this, R.string.input_not_null, Toast.LENGTH_SHORT).show();
						return;
					}else{
						//在此处将deviceId存放进SharedPreferences
						SharedPreferencesUtils.setDeviceId(SettingActivity.this, deviceNumber);
					}
				}
			});
			builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
			break;
		
		case R.id.setting_relative_ipconfig:
			Intent intent = new Intent(SettingActivity.this, ServerConfig.class);
			startActivity(intent);
			break;
			
		case R.id.setting_relative_register:
			final String physicalId = DeviceUuidFactory.getPhysicalId(this);
			Log.i("唯一标识", physicalId);
			CList<Map<String, String>> params1 = new CList<Map<String,String>>();
			params1.add("handvId", physicalId);
			server.connect(this, Constants.FastMethodName.REGISTERDEVICEID_METHODNAME, Constants.FastWebService.REGISTER_WSDL, params1, new OnServerResponse() {
				
				@Override
				public void onResponse(String result) {
					if(null != result){
						mDialog.dismiss();
						Log.e("*******************", result);
					}
					String getData[] = result.split("@");
					if(getData[0].equals("0")){
						Toast.makeText(SettingActivity.this, getData[1], Toast.LENGTH_SHORT).show();//注册成功，请激活后使用
						physicalNumber.setText(physicalId);
						SharedPreferencesUtils.setPhysicalId(SettingActivity.this, physicalId);
					}else if(!getData[0].equals("0")){
						physicalNumber.setText(physicalId);
						SharedPreferencesUtils.setPhysicalId(SettingActivity.this, physicalId);
						Toast.makeText(SettingActivity.this, getData[1], Toast.LENGTH_SHORT).show();//该设备已注册
					}
				}
				
				@Override
				public void onBeforeRequest() {
					mDialog.show();
				}
			});
			break;
		default:
			break;
		}

	}

}
