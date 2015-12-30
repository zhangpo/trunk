package cn.com.choicesoft.chinese.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.BaseActivity;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.activity.WelcomeActivity;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseResultAlt;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.chinese.util.JsonUtil;
import cn.com.choicesoft.chinese.util.OrderSaveUtil;
import cn.com.choicesoft.chinese.view.ChineseSoftSettingDialog;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.util.CList;
import cn.com.choicesoft.util.CSExceptionHandler;
import cn.com.choicesoft.util.CSLog;
import cn.com.choicesoft.util.ComXmlUtil;
import cn.com.choicesoft.util.DBManager;
import cn.com.choicesoft.util.DateFormat;
import cn.com.choicesoft.util.DeviceUuidFactory;
import cn.com.choicesoft.util.EditTextUtil;
import cn.com.choicesoft.util.ExitApplication;
import cn.com.choicesoft.util.FTPDownLoad;
import cn.com.choicesoft.util.LanguageSetting;
import cn.com.choicesoft.util.OnServerResponse;
import cn.com.choicesoft.util.Server;
import cn.com.choicesoft.util.SharedPreferencesUtils;
import cn.com.choicesoft.util.ToastUtil;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.view.ClearEditText;
import cn.com.choicesoft.view.HdIpSettingDialog;
import cn.com.choicesoft.view.IpSettingDialog;
import cn.com.choicesoft.view.IpSettingDialog.DialogIpClickListener;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;
import cn.com.choicesoft.chinese.bean.*;
import cn.com.choicesoft.util.HttpConnectionUtil;
import cn.com.choicesoft.util.HttpConnectionUtil.HttpMethod;

/**
 * 中餐登录界面
 */
public class ChineseWelcomeActivity extends BaseActivity implements OnClickListener{

	private static int RESULT_LOAD_IMAGE = 1;
	private static int RESULT_LEFT_IMAGE = 100;

	private Button setting, login,one,tow,three,four,five,six,seven,eight,nine,zero,succeed,reset,delete;
	private EditText userName, userPassword;
	private LoadingDialog mDialog = null;
	private LoadingDialog loginDialog = null;
	private ChineseSoftSettingDialog softset = null;
	private RelativeLayout welcomelayout;
	private LinearLayout linearLayout;//登陆输入界面
	private TextView androidIDView;//该台设备的机器码
	private AlertDialog mAlertDialog = null;
	private HttpConnectionUtil hcUtil = null;//对接美味不用等
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        CSExceptionHandler.instance().init(getApplicationContext());//开启应用错误记录
		ExitApplication.getInstance().addActivity(this);
        new LanguageSetting().setting(this);
        if (ChioceActivity.ispad) {
            setContentView(R.layout.activity_welcome_pad);
        } else {
            setContentView(R.layout.activity_welcome);
        }
		initView();
		initListener();
		initLayout();
		initData();
	}


	@Override
	protected void onResume() {
		setBackground();
		super.onResume();
	}

	/**
	 * 界面初始化
	 */
	public void initLayout(){
		DisplayMetrics dmm = new DisplayMetrics();
		// 获取窗口属性
		getWindowManager().getDefaultDisplay().getMetrics(dmm);
		Double downbl = ChioceActivity.ispad?0.08:0.22;
		linearLayout.setY(((Double) (dmm.heightPixels * downbl)).floatValue());
	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		androidIDView.setText(DeviceUuidFactory.getPhysicalId(this));

		mDialog = new LoadingDialogStyle(this, "");
		mDialog.setCancelable(true);

		loginDialog = new LoadingDialogStyle(this, this.getString(R.string.in_the_login));
		loginDialog.setCancelable(true);

		//注册用的
		boolean isRegistered = SharedPreferencesUtils.getIsRegister(this);
		if(isRegistered){
			setting.setVisibility(View.VISIBLE);
		}else{
//			//调用接口去注册
//			CList<Map<String, String>> clists = new CList<Map<String,String>>();
//			clists.add("uuid", androidIDView.getText().toString());
//			new Server().connect(this, Constants.FastMethodName.activate_METHODNAME, Constants.FastWebService.Activate_WSDL, clists, new OnServerResponse() {
//
//				@Override
//				public void onResponse(String result) {
//					mDialog.dismiss();
//					Toast.makeText(WelcomeActivity.this, result, Toast.LENGTH_SHORT).show();
//				}
//
//				@Override
//				public void onBeforeRequest() {
//					mDialog.show();
//				}
//			});
		}


		softset = ChineseSoftSettingDialog.newInstance(ChineseWelcomeActivity.this.getString(R.string.app_setting), new ChineseSoftSettingDialog.DialogClickListener() {

			@Override
			public void doUpdatedateItemClick() {
				updateForFTP();
			}
			@Override
			public void doTableClassify(final TextView classifyText) {
				LinearLayout layout=(LinearLayout)ChineseWelcomeActivity.this.getLayoutInflater().inflate(R.layout.table_classify_set, null);
				RadioGroup radio=(RadioGroup)layout.findViewById(R.id.table_Radio);
				radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt) {
						if(paramInt==R.id.table_classify_area){
							SharedPreferencesUtils.setTableClass(ChineseWelcomeActivity.this, "1");
							classifyText.setText(R.string.area);
						}else if(paramInt==R.id.table_classify_lc){
							SharedPreferencesUtils.setTableClass(ChineseWelcomeActivity.this, "2");
							classifyText.setText(R.string.lc);
						}else if (paramInt==R.id.table_classify_state) {
							SharedPreferencesUtils.setTableClass(ChineseWelcomeActivity.this, "3");
							classifyText.setText(R.string.state);
						}
						backKey();
					}
				});
				RadioButton area=(RadioButton)layout.findViewById(R.id.table_classify_area);
				RadioButton state=(RadioButton)layout.findViewById(R.id.table_classify_state);
				RadioButton lc=(RadioButton)layout.findViewById(R.id.table_classify_lc);
				if(SharedPreferencesUtils.getTableClass(ChineseWelcomeActivity.this).equals("1")){
					area.setChecked(true);
				}else if (SharedPreferencesUtils.getTableClass(ChineseWelcomeActivity.this).equals("2")) {
					lc.setChecked(true);
				}else if (SharedPreferencesUtils.getTableClass(ChineseWelcomeActivity.this).equals("3")) {
					state.setChecked(true);
				}
				new Builder(ChineseWelcomeActivity.this,R.style.edittext_dialog).setTitle(R.string.settinggroup).setView(layout).show();
			}
			@Override
			public void doIpItemClick() {
				IpSettingDialog ipset = IpSettingDialog.newInstance(ChineseWelcomeActivity.this.getString(R.string.ip_setting), new DialogIpClickListener() {

					@Override
					public void doPositiveClick(String ipaddress, String port) {
						String ip="^\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
								"((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)" +
								"\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]" +
								"\\d|25[0-5])\\b$";
						String pt="^([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]" +
								"\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";
						if(!ValueUtil.isEmpty(ipaddress) && !ValueUtil.isEmpty(port)){
							if(!ipaddress.matches(ip)){
								ToastUtil.toast(ChineseWelcomeActivity.this, R.string.ip_error);
								return;
							}else if(!port.matches(pt)){
								ToastUtil.toast(ChineseWelcomeActivity.this,R.string.port_error);
								return;
							}
							StringBuilder sb = new StringBuilder();
							SharedPreferencesUtils.setServerAddress(ChineseWelcomeActivity.this, sb.append(ipaddress).append(":").append(port).toString());
							Toast.makeText(ChineseWelcomeActivity.this, R.string.setting_success, Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(ChineseWelcomeActivity.this,R.string.input_not_null, Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void doNegativeClick() {

					}
				});
				ipset.setCancelable(false);
				ipset.show(getSupportFragmentManager(), "dialog");
			}

			@Override
			public void doFtpItemClick() {
				final AlertDialog dialog;
				View layout = LayoutInflater.from(ChineseWelcomeActivity.this).inflate(R.layout.dia_ftpsetting, null);
				final EditText etUserName = (EditText) layout.findViewById(R.id.dia_et_ftpsettingusername);
				final EditText etPwd = (EditText) layout.findViewById(R.id.dia_et_ftpsettingpwd);
				final EditText etIp = (EditText) layout.findViewById(R.id.dia_et_ftpsettingip);

				StringBuilder sb = new StringBuilder();
				sb.append(ValueUtil.isNotEmpty(SharedPreferencesUtils.getFtpUsername(ChineseWelcomeActivity.this))?SharedPreferencesUtils.getFtpUsername(ChineseWelcomeActivity.this):"")
				.append(":")
				.append(ValueUtil.isNotEmpty(SharedPreferencesUtils.getFtpPwd(ChineseWelcomeActivity.this))?SharedPreferencesUtils.getFtpUsername(ChineseWelcomeActivity.this):"")
				.append("@")
				.append(ValueUtil.isNotEmpty(SharedPreferencesUtils.getFtpIp(ChineseWelcomeActivity.this))?SharedPreferencesUtils.getFtpIp(ChineseWelcomeActivity.this):"");

				final Button  ensurebuton = (Button)layout.findViewById(R.id.dia_btn_ftpsetcertainbtn);
				final Button  canclebuton = (Button)layout.findViewById(R.id.dia_btn_ftpsetcancelbtn);
				dialog = new Builder(ChineseWelcomeActivity.this, R.style.edittext_dialog).setTitle(R.string.please_setting_ftp_adds).setMessage(ChineseWelcomeActivity.this.getString(R.string.current_ftp_adds) + sb.toString()).setView(layout).create();
				ensurebuton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String ftpuserName = etUserName.getText().toString();
						String ftppassword = etPwd.getText().toString();
						String ftpipValue = etIp.getText().toString();
						String perl="^\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
								"((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)" +
								"\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]" +
								"\\d|25[0-5])\\b:([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]" +
								"\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";
						if(ValueUtil.isNotEmpty(ftpipValue)&&ftpipValue.matches(perl)){
							SharedPreferencesUtils.setFtpIp(ChineseWelcomeActivity.this, ftpipValue);
							SharedPreferencesUtils.setFtpUsername(ChineseWelcomeActivity.this, ftpuserName);
							SharedPreferencesUtils.setFtpPwd(ChineseWelcomeActivity.this, ftppassword);
							Toast.makeText(ChineseWelcomeActivity.this, R.string.setting_success, Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}else{
							Toast.makeText(ChineseWelcomeActivity.this,R.string.ftp_example,Toast.LENGTH_SHORT).show();
						}
					}

				});
				canclebuton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.show();


			}

			@Override
			public void doFinishClick() {
			}

			@Override
			public void doDeviceEncodeClick() {
				Builder builder = new Builder(ChineseWelcomeActivity.this,R.style.edittext_dialog);
				View view = LayoutInflater.from(ChineseWelcomeActivity.this).inflate(R.layout.device_number_layout, null);
				final EditText editText = (EditText) view.findViewById(R.id.devicenumber_et_layout);
				Button ensurebutton = (Button)view.findViewById(R.id.dia_btn_devicesetcertainbtn);
				Button canclebutton = (Button)view.findViewById(R.id.dia_btn_devicesetcancelbtn);
				//得到设备编号
				String encodeStr = SharedPreferencesUtils.getDeviceId(ChineseWelcomeActivity.this);
				builder.setTitle(R.string.encodesetting).setMessage(ChineseWelcomeActivity.this.getString(R.string.current_encode) + encodeStr);
				builder.setView(view);
				final AlertDialog dialog =builder.create();

				ensurebutton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String padid = editText.getText().toString();
						if(ValueUtil.isEmpty(padid)){
							Toast.makeText(ChineseWelcomeActivity.this,R.string.input_not_null, Toast.LENGTH_SHORT).show();
							return;
						}
						String equipment  = androidIDView.getText().toString();
						Map<String,String> map=new HashMap<String, String>();
			            map.put("equipment",equipment );
			            map.put("padid",padid);
			            CList list=new CList();
			            try {
							list.add("json", JsonUtil.getJson(map));
						} catch (IOException e) {
							e.printStackTrace();
						}
			            new ChineseServer().connect(ChineseWelcomeActivity.this, ChineseConstants.EQUIP_MENT_CODING, list, new OnServerResponse() {
			                @Override
			                public void onResponse(String result) {
			                    int msgId= R.string.encodesetting_error;
			                    System.out.println("result===="+result);
			                    try {
			                        Map<String,Object> map= JsonUtil.getObject(result, Map.class);
			                        System.out.println("map================"+map);
			                        if(ValueUtil.isNotEmpty(map)){
			                            Integer state=((Map<String,Integer>)map.get("result")).get("state");
			                            System.out.println("state============"+state);
			                            String msg=((Map<String,String>)map.get("result")).get("msg");
			                            if(ValueUtil.isNotEmpty(state)&&state==1) {
			                            	SharedPreferencesUtils.setDeviceId(ChineseWelcomeActivity.this, msg+"-1000");//TODO 中餐暂定配置编号为：1000
			    							Toast.makeText(ChineseWelcomeActivity.this, R.string.encodesetting_success, Toast.LENGTH_SHORT).show();
			    							dialog.dismiss();
			                            }else{
//			                            	ToastUtil.toast(ChineseWelcomeActivity.this, msgId);
			                				Toast.makeText(ChineseWelcomeActivity.this, msg, Toast.LENGTH_LONG).show();
			                            }
			                        }
			                    } catch (IOException e) {
			                        e.printStackTrace();
			                    }
			                    
			                }
			                @Override
			                public void onBeforeRequest() {

			                }
			            });

					}
				});
				canclebutton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();

			}
			@Override
			public void doSetBackground() {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
			@Override
			public void reSetBackground() {
				reSetBackgound();
			}
			@Override
			public void doSetLeftbg() {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LEFT_IMAGE);
			}
			@Override
			public void reSetLeftbg() {
				SharedPreferencesUtils.setLeftBgPath(ChineseWelcomeActivity.this, "NotFind");
			}

			@Override
			public void updateApp(){
				new ChineseServer().connect(ChineseWelcomeActivity.this, ChineseConstants.ANDROID_APP_URL, null, new OnServerResponse() {
					@Override
					public void onResponse(String result) {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						Uri url = null;
						if (result != null) {
							url = Uri.parse(result);
						} else {
							url = Uri.parse("https://pad1.choicesoft.com.cn/ChoiceIpad/AndroidApp/AndroidApp.html");
						}
						intent.setData(url);
						startActivity(intent);
					}

					@Override
					public void onBeforeRequest() {

					}
				});
			}
			@Override
			public void doWebserviceIp(){
				HdIpSettingDialog ipset = HdIpSettingDialog.newInstance(ChineseWelcomeActivity.this.getString(R.string.ip_setting), new HdIpSettingDialog.DialogHdIpClickListener() {

					@Override
					public void doPositiveClick(String ipaddress) {
						SharedPreferencesUtils.setWaitUrl(ChineseWelcomeActivity.this, ipaddress);
						Toast.makeText(ChineseWelcomeActivity.this, R.string.setting_success, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void doNegativeClick() {
					}
				});
				ipset.setCancelable(false);
				ipset.show(getSupportFragmentManager(), "dialog");
			}
			@Override
			public void doShopEncode() {
				View view = LayoutInflater.from(ChineseWelcomeActivity.this).inflate(R.layout.dia_shopencode_encode, null);
				final EditText shopencode = (EditText) view.findViewById(R.id.dia_shopcode_user_input);
				Button certainBtn = (Button) view.findViewById(R.id.dia_btn_shopencodecertainbtn);
				Button cancelBtn = (Button) view.findViewById(R.id.dia_btn_shopencodecancelbtn);
				AlertDialog.Builder builder = new Builder(ChineseWelcomeActivity.this,R.style.edittext_dialog);
				builder.setTitle(R.string.dia_shopencode_title).setMessage(ChineseWelcomeActivity.this.getString(R.string.current_num)+SharedPreferencesUtils.getShopEncode(ChineseWelcomeActivity.this)).setView(view);
				final AlertDialog dialog =builder.create();
				certainBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String shopEncodeStr = shopencode.getText().toString();
						if(ValueUtil.isNotEmpty(shopEncodeStr)){
							SharedPreferencesUtils.setShopEncode(ChineseWelcomeActivity.this, shopEncodeStr);
							Toast.makeText(ChineseWelcomeActivity.this, R.string.store_num_setting_success, Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}else{
							Toast.makeText(ChineseWelcomeActivity.this, R.string.input_not_null, Toast.LENGTH_SHORT).show();
						}
					}
				});

				cancelBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.show();
			}



		});
		softset.setCancelable(false);

	}


	/**
	 * 控件初始化
	 */
	private void initView() {
		userName = (EditText) findViewById(R.id.userName);
		userPassword = (EditText) findViewById(R.id.userPassword);
		if (ChioceActivity.ispad) {
			EditTextUtil.hideKeyboard(userName);
			EditTextUtil.hideKeyboard(userPassword);//隐藏软键盘
		}
		setting = (Button) findViewById(R.id.setting);
		login = (Button) findViewById(R.id.welcome_login);
		one = (Button) findViewById(R.id.welcome_one);
		tow = (Button) findViewById(R.id.welcome_tow);
		three = (Button) findViewById(R.id.welcome_three);
		four = (Button) findViewById(R.id.welcome_four);
		five = (Button) findViewById(R.id.welcome_five);
		six = (Button) findViewById(R.id.welcome_six);
		seven = (Button) findViewById(R.id.welcome_seven);
		eight = (Button) findViewById(R.id.welcome_eight);
		nine = (Button) findViewById(R.id.welcome_nine);
		zero = (Button) findViewById(R.id.welcome_zero);
		succeed = (Button) findViewById(R.id.welcome_succeed);
		delete = (Button) findViewById(R.id.welcome_delete);
		reset = (Button) findViewById(R.id.welcome_reset);
		welcomelayout = (RelativeLayout)findViewById(R.id.welcome_activity_layout);
		linearLayout=(LinearLayout)findViewById(R.id.welcome_Layout);
		androidIDView = (TextView) this.findViewById(R.id.welcome_tv_androidId);
	}

	/**
	 * 监听初始化
	 */
	private void initListener() {
		setting.setOnClickListener(this);
		login.setOnClickListener(this);
		one.setOnClickListener(this);
		tow.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);
		six.setOnClickListener(this);
		seven.setOnClickListener(this);
		eight.setOnClickListener(this);
		nine.setOnClickListener(this);
		zero.setOnClickListener(this);
		succeed.setOnClickListener(this);
		delete.setOnClickListener(this);
		reset.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.welcome_login:
			loginClick();
			break;
		case R.id.setting:
			LinearLayout layout=(LinearLayout)this.getLayoutInflater().inflate(R.layout.verify_pwd, null);
			Button confirm=(Button)layout.findViewById(R.id.verify_pwd_confirm);
			Button cancel=(Button)layout.findViewById(R.id.verify_pwd_cancel);
			final ClearEditText pwdEdit=(ClearEditText)layout.findViewById(R.id.verify_pwd_Edit);
			final AlertDialog alert=new Builder(this,R.style.edittext_dialog).setTitle(R.string.pwd_ver).setView(layout).show();
			confirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View paramView) {
					String pwd=pwdEdit.getText().toString();
					String forPwd=DateFormat.getStringByDate(DateFormat.getDateBefore(new Date(), "day", 1, 3),"ddMMyy");
					if(forPwd.equals(pwd)){
						alert.dismiss();
                        softset.show(getSupportFragmentManager(), "ChineseSoftSettingDialog");
					}else{
						Toast.makeText(ChineseWelcomeActivity.this, R.string.pwd_error, Toast.LENGTH_LONG).show();
					}
				}
			});
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View paramView) {
					backKey();
				}
			});
			break;
		case R.id.welcome_one:
			numClick("1");
			break;
		case R.id.welcome_tow:
			numClick("2");
			break;
		case R.id.welcome_three:
			numClick("3");
			break;
		case R.id.welcome_four:
			numClick("4");
			break;
		case R.id.welcome_five:
			numClick("5");
			break;
		case R.id.welcome_six:
			numClick("6");
			break;
		case R.id.welcome_seven:
			numClick("7");
			break;
		case R.id.welcome_eight:
			numClick("8");
			break;
		case R.id.welcome_nine:
			numClick("9");
			break;
		case R.id.welcome_zero:
			numClick("0");
			break;
		case R.id.welcome_delete:
			if(userName.isFocused()){
				int index=userName.getSelectionStart();
				if(index>0){
					userName.getText().delete(index-1, index);
				}
			}else if(userPassword.isFocused()){
				int index=userPassword.getSelectionStart();
				if(index>0){
					userPassword.getText().delete(index-1, index);
				}
			}
			break;
		case R.id.welcome_reset:
			userName.getText().clear();
			userPassword.getText().clear();
			userName.requestFocus();
			break;
		case R.id.welcome_succeed:
			if(userName.isFocused()){
				userPassword.requestFocus();
			}else if(userPassword.isFocused()){
				loginClick();
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 登陆事件
	 */
	public void loginClick(){
		final String username = userName.getText().toString();
//		final String handvId = androidIDView.getText().toString();
//		final String padid = androidIDView.getText().toString();
		if (ValueUtil.isEmpty(username)) {
			Toast.makeText(ChineseWelcomeActivity.this, R.string.please_user_name,Toast.LENGTH_SHORT).show();
			return;
		}
		final String password = userPassword.getText().toString();
		if (ValueUtil.isEmpty(password)) {
			Toast.makeText(ChineseWelcomeActivity.this, R.string.input_user_password,Toast.LENGTH_SHORT).show();
			return;
		}
		if(ValueUtil.isEmpty(SharedPreferencesUtils.getServerAddress(this))){
			Toast.makeText(ChineseWelcomeActivity.this, R.string.ip_not_null,Toast.LENGTH_SHORT).show();
			return;
		}
		
		CList<Map<String, String>> clist = new CList<Map<String,String>>();
		clist.add("user", username);
		clist.add("pass", password);
		//从本地取出设备的编号和机器码
		clist.add("padid", SharedPreferencesUtils.getDeviceId(this));
		clist.add("handvId ", SharedPreferencesUtils.getPhysicalId(this));
		clist.add("oStr", "");
		new ChineseServer().connect(ChineseWelcomeActivity.this,ChineseConstants.PLOGIN, clist, new OnServerResponse() {
            @Override
			public void onResponse(String result) {
				loginDialog.dismiss();
				if(!ValueUtil.isEmpty(result)){
                    Map<String,String> map=new ComXmlUtil().xml2Map(result);
                    if(ValueUtil.isEmpty(map)) {
                        ToastUtil.toast(ChineseWelcomeActivity.this, R.string.login_error);
                        return;
                    }
					if("1".equals(map.get("result"))){
                        SharedPreferencesUtils.setUserCode(ChineseWelcomeActivity.this,username+"-"+password);
						String[] oStr=ChineseResultAlt.oStrAlt(map.get("oStr")).split("\\^");
						String mol=null;
						if("Y".equals(oStr[1])){//判断是否需要抹零
							mol=oStr[2];
						}
						SharedPreferencesUtils.setPadInformation(ChineseWelcomeActivity.this, mol, null, null, oStr[0],null);
						if (oStr.length>3){
							SingleMenu.getMenuInstance().setPriceTyp(oStr[3]);
						}
						getChinesePermission();
					}else{
                        Toast.makeText(ChineseWelcomeActivity.this, map.get("oStr"), Toast.LENGTH_SHORT).show();
                    }
				}else{
					Toast.makeText(ChineseWelcomeActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onBeforeRequest() {
				loginDialog.show();
			}
		});
	}
	public void getChinesePermission(){
		new ChineseServer().connect(ChineseWelcomeActivity.this,ChineseConstants.GET_CHINESE_PERMISSION, null, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				loginDialog.dismiss();
				try {
					JSONArray jsonArray = new JSONArray(result);
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("CODE", jsonObject.getString("CODE"));
						map.put("NAME", jsonObject.getString("NAME"));
						map.put("ISSHOW", jsonObject.getString("ISSHOW"));
						list.add(map);
					}
					SingleMenu.getMenuInstance().setPermissionList(list);
					final Intent intent = new Intent(ChineseWelcomeActivity.this, MainActivity.class);
					startActivity(intent);
					ChineseWelcomeActivity.this.finish();
				} catch (JSONException e) {
					final Intent intent = new Intent(ChineseWelcomeActivity.this, MainActivity.class);
					startActivity(intent);
					ChineseWelcomeActivity.this.finish();
//					ToastUtil.toast(ChineseWelcomeActivity.this, R.string.gain_wait_error);
				}
			}

			@Override
			public void onBeforeRequest() {
				loginDialog.show();
			}
		});
	}
	/**
	 * 输入事件
	 * @param num
	 */
	public void numClick(String num){
		if(userName.isFocused()){
			userName.getText().insert(userName.getSelectionStart(), num);
		}else if(userPassword.isFocused()){
			userPassword.getText().insert(userPassword.getSelectionStart(), num);
		}
	}
	
	/**设置登陆界面背景图片
	 * 
	 */
	private void setBackground() {
		String path = SharedPreferencesUtils.getbackgroundPath(this);
    	
    	if (path!=null &&  !path.equals("NotFind")  ) {
    		Bitmap bitmap = BitmapFactory.decodeFile(path);
    		if (bitmap==null || bitmap.getByteCount()==0) {
    			welcomelayout.setBackgroundResource(R.drawable.bg_login_big);
			}else {
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
	    		welcomelayout.setBackgroundDrawable(bitmapDrawable);
			}
		}else if (path!=null&&path.equals("NotFind")) {
			welcomelayout.setBackgroundResource(R.drawable.bg_login_big);
		}
		
	}
	/**
	 * 背景重置
	 */
	private void reSetBackgound() {
		SharedPreferencesUtils.setbackgroundPath(ChineseWelcomeActivity.this,"NotFind");
		welcomelayout.setBackgroundResource(R.drawable.bg_login_big);
	}
	
	/**
	 * 获取图片路径
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {//欢迎页面背景
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            ToastUtil.toast(getApplicationContext(), picturePath);
            cursor.close();
            if (picturePath!=null) {
            	SharedPreferencesUtils.setbackgroundPath(ChineseWelcomeActivity.this,picturePath);
			}
        } else if (requestCode == RESULT_LEFT_IMAGE && resultCode == RESULT_OK && null != data) {//账单查询左侧
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            ToastUtil.toast(getApplicationContext(), picturePath);
            cursor.close();
            if (picturePath!=null) {
            	SharedPreferencesUtils.setLeftBgPath(ChineseWelcomeActivity.this,picturePath);
			}
        	
		}
    }
    /**
     * FTP数据更新
     */
    public void updateForFTP(){
//    	 String updateDataVersion(String deviceId,String userCode,String dataVersion);
    	final LoadingDialog upDialog=new LoadingDialogStyle(ChineseWelcomeActivity.this, this.getString(R.string.update_data));
    	upDialog.setCancelable(true);
    	if(SharedPreferencesUtils.getFtpIp(ChineseWelcomeActivity.this)!=null){
    		if(!(SharedPreferencesUtils.getFtpIp(ChineseWelcomeActivity.this).split(":").length>1)){
    			Toast.makeText(ChineseWelcomeActivity.this, R.string.ftp_adds_error, Toast.LENGTH_LONG).show();
    		}
    		FTPDownLoad load=null;
			try {
				load=new FTPDownLoad();
				load.downLoad(ChineseWelcomeActivity.this, SharedPreferencesUtils.getFtpIp(ChineseWelcomeActivity.this), SharedPreferencesUtils.getFtpUsername(ChineseWelcomeActivity.this), SharedPreferencesUtils.getFtpPwd(ChineseWelcomeActivity.this),new FTPDownLoad.ICallBack(){
					@Override
					public void runBack() {
                        upDialog.dismiss();
						new ChineseServer().connect(ChineseWelcomeActivity.this, ChineseConstants.UPDATA_CODEDESC, null, new OnServerResponse() {
							@Override
							public void onResponse(String result) {
								System.out.println("result=====" + result);
								if (ValueUtil.isNotEmpty(result)) {
									try {
										Map<String, Object> map = JsonUtil.getObject(result, Map.class);
										if (ValueUtil.isNotEmpty(map)) {
											Integer state = ((Map<String, Integer>) map.get("result")).get("state");
											if (ValueUtil.isNotEmpty(state) && state == 1) {
												List<Map<String, String>> msg = ((Map<String, List<Map<String, String>>>) map.get("result")).get("msg");
												new OrderSaveUtil().insertCodesdesc(ChineseWelcomeActivity.this, msg);
											}
										}
										Toast.makeText(ChineseWelcomeActivity.this, R.string.update_success, Toast.LENGTH_LONG).show();
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else {
									ToastUtil.toast(ChineseWelcomeActivity.this, R.string.net_error);
								}

							}

							@Override
							public void onBeforeRequest() {

							}

						});
                        Toast.makeText(ChineseWelcomeActivity.this, R.string.update_success, Toast.LENGTH_LONG).show();
					}
					@Override
					public void loseBack(String msg) {
						upDialog.dismiss();
						Looper.prepare();
						Toast.makeText(ChineseWelcomeActivity.this,msg, Toast.LENGTH_LONG).show();
						Looper.loop();
					}
				});
			} catch (Exception e) {
				Toast.makeText(ChineseWelcomeActivity.this, R.string.update_unsuccess, Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(ChineseWelcomeActivity.this, R.string.ftp_adds_not_null, Toast.LENGTH_LONG).show();
		}
    }
    /**
     * 两次不连续单击退出应用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitApp();
        }
        return false;
    }
    private long exitTime = 0;

    /**
     * 退出当前应用程序
     */
    public void ExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this,R.string.exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            this.finish();
            System.exit(0);
        }

    }
	/**
	 * 代码调用Back键
	 */
	@SuppressLint("CommitTransaction")
	public void backKey(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				ChineseWelcomeActivity.this.getFragmentManager().beginTransaction();
				Instrumentation inst = new Instrumentation(); 
				inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);  
			}
		}).start();
	}
	/**
	 * 对接美味不用等  授权对话框
	 */
	private void showDialogForJnowAuth(){
		View diaAuthLayout = LayoutInflater.from(this).inflate(R.layout.dia_jnow_authuserandpwd, null);
		final EditText userEdt = (EditText) diaAuthLayout.findViewById(R.id.dia_jnow_authuser_et);
		final EditText pwdEdt = (EditText) diaAuthLayout.findViewById(R.id.dia_jnow_authpwd_et);

		AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.edittext_dialog);
		builder.setTitle(R.string.jnow_auth);//美味不用等的授权
		builder.setView(diaAuthLayout);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String userNameVal = userEdt.getText().toString().trim();
				String pwdVal = pwdEdt.getText().toString().trim();
				if (ValueUtil.isEmpty(userNameVal)) {
					ChineseResultAlt.setDialogIfDismiss(mAlertDialog, false);
					ToastUtil.toast(ChineseWelcomeActivity.this, R.string.dia_jnow_inputauthuser);
				} else if (ValueUtil.isEmpty(pwdVal)) {
					ChineseResultAlt.setDialogIfDismiss(mAlertDialog, false);
					ToastUtil.toast(ChineseWelcomeActivity.this, R.string.dia_jnow_inputauthpwd);
				} else {
					ChineseResultAlt.setDialogIfDismiss(mAlertDialog, true);
					SharedPreferencesUtils.setJNOWUserName(ChineseWelcomeActivity.this, userNameVal);
					SharedPreferencesUtils.setJNOWPwd(ChineseWelcomeActivity.this, pwdVal);

					//对接美味不用等  上传菜品接口时，弹出的对话框
					showDialogForJnowUpload();
				}


			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ChineseResultAlt.setDialogIfDismiss(mAlertDialog, true);
			}
		});

		mAlertDialog = builder.create();
		mAlertDialog.show();
	}


	/**
	 * 对接美味不用等  上传菜品接口时，弹出的对话框
	 */
	private void showDialogForJnowUpload(){

		AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.edittext_dialog);
		builder.setTitle(R.string.dia_jnow_upload_title);//美味不用等的授权
		builder.setMessage(R.string.dia_jnow_upload_message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				uploadDataByServer();//调用接口将本地数据同步到美味不用等的服务器
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});
		builder.show();
	}

	public static class DefaultView{}//用于对接美味不用等,只拼接含有DefaultView注释的字段

	/**
	 * 调用接口将本地数据同步到美味不用等的服务器
	 */
	private void uploadDataByServer(){
		String jnowUserName = SharedPreferencesUtils.getJNOWUserName(this);//美味不用等的授权账号
		String jnowPwd = SharedPreferencesUtils.getJNOWPwd(this);//美味不用等的授权密码

		//先判断用户是否用FTP更新了BookSystem.sqlite数据库,如果没有，就提示用户先去更新BookSystem.sqlite数据库
		SQLiteDatabase database = new DBManager(ChineseWelcomeActivity.this).openDatabase();
		if(database == null){
			ToastUtil.toast(this, R.string.jnow_instruct_updatesqlitefirst);//请先更新数据库
			return;
		}

		if(ValueUtil.isNotEmpty(jnowUserName) && ValueUtil.isNotEmpty(jnowPwd)){
			List<JGrptyp> jGryLists = getDataManager(this).queryAllGrptypForJNow();
			List<JNowFood> jFoodLists = getDataManager(this).queryAllFoodForJNow();

			//如果数据库中有重名的菜品，即使菜品编码，单位,价格不同，美味不用等那边就会报错,就得去重
			List<JNowFood> singlejFoodLists = new ArrayList<JNowFood>();
			for(JNowFood jNowFood:jFoodLists){
				if(!singlejFoodLists.contains(jNowFood)){
					singlejFoodLists.add(jNowFood);
				}
			}

			//将菜品类别赋值进菜品对象中
			List<String> dirLists = null;
			for(JGrptyp grptyp:jGryLists){
				for(JNowFood jfood:singlejFoodLists){
					if(grptyp.getGrpTyp() == jfood.getSortClass()){
						dirLists = new ArrayList<String>();
						dirLists.add(grptyp.getGrpDes());
						jfood.setDirs(dirLists);
					}
				}
			}

			//将对象拼接成JSON
			JNowData jNowData = new JNowData();
			jNowData.setGoods(singlejFoodLists);
			try {
				String jsonStr = JsonUtil.getJson(jNowData, DefaultView.class);
				Log.d("拼接的json", jsonStr);
				Map<String, String> params = new LinkedHashMap<String, String>();
				params.put("user", jnowUserName);
//			params.put("user", "XbTestAdmin");//"SMARTSCENE"+"xb123456" MwtydHkAdmin,
//			String md5Val = ChineseResultAlt.encryMD5("SMARTSCENE"+"xb123456");
				String md5Val = ChineseResultAlt.encryMD5("SMARTSCENE"+ jnowPwd);
				Log.d("md5加密", md5Val);
				params.put("pwd", md5Val);
				params.put("data", jsonStr);
				new HttpConnectionUtil().connect("http://9now.cn/api/shop/good/", params, HttpMethod.POST, new OnServerResponse() {

					@Override
					public void onResponse(String result) {
						mDialog.dismiss();
						if (ValueUtil.isNotEmpty(result)) {
							Log.d("上传菜品接口返回的结果", result);
							try {
								JNowError jnowError = JsonUtil.getObject(result, JNowError.class);
								if (jnowError.getErrno() == 0) {
									ToastUtil.toast(ChineseWelcomeActivity.this, jnowError.getErrmsg());
								} else {
									ToastUtil.toast(ChineseWelcomeActivity.this, jnowError.getErrmsg());//参数错误，请稍后再试
								}
							} catch (JsonParseException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							ToastUtil.toast(ChineseWelcomeActivity.this, R.string.net_error);
						}
					}

					@Override
					public void onBeforeRequest() {
						mDialog.show();
					}
				});
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}else{
			ToastUtil.toast(this, R.string.jnow_input_authuserandpwd);//请先填写美味不用等的授权账号和密码
		}
	}
}
