package cn.com.choicesoft.activity;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.SingleMenu;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.fragment.StartTabVipFragment;
import cn.com.choicesoft.util.CList;
import cn.com.choicesoft.util.ExitApplication;
import cn.com.choicesoft.util.OnServerResponse;
import cn.com.choicesoft.util.Server;
import cn.com.choicesoft.util.SharedPreferencesUtils;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

/**
 *@Author:M.c
 *@Comments:会员卡查询
 *@CreateDate:2014-2-27
 */
public class VipCardQuery extends Activity implements View.OnClickListener{
	private FragmentManager manager=null;//Fragment管理
	private FragmentTransaction transaction=null;//Fragment事务
	private LoadingDialog loadingDialog;//提示框

	public LoadingDialog getLoadingDialog() {
		if(loadingDialog==null){
			loadingDialog = new LoadingDialogStyle(VipCardQuery.this, "请稍后...");
			loadingDialog.setCancelable(true);
		}
		return loadingDialog;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		manager=this.getFragmentManager();
		transaction=manager.beginTransaction();
		transaction.add(R.id.vip_cardQ_TopLayout, new StartTabVipFragment(),"vip_cardQ_TopLayout");//会员卡确认Fragment
		transaction.commit();
		setContentView(R.layout.activity_vip_card_query);
//		TextView czy=(TextView)findViewById(R.id.vip_activity_top_operator);
//		czy.setText(SharedPreferencesUtils.getOperator(this));
		Button confirm=(Button)findViewById(R.id.vip_card_confirm);//确认按钮
		Button cancle=(Button)findViewById(R.id.vip_card_cancle);//取消按钮
		confirm.setOnClickListener(this);
		cancle.setOnClickListener(this);
	}
	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {
			case R.id.vip_card_confirm://点菜
				if(VipCardQuery.this.getIntent().getStringExtra("state").equals("mark")){//判断从哪里跳转过来的
					new AlertDialog.Builder(VipCardQuery.this)
							.setTitle("提示").setMessage("是否预定台位并点菜！")
							.setPositiveButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface paramDialogInterface, int paramInt) {
									CList<Map<String,String>> data=new CList<Map<String,String>>();
									data.add("deviceId", SharedPreferencesUtils.getDeviceId(VipCardQuery.this));
									data.add("userCode", SharedPreferencesUtils.getUserCode(VipCardQuery.this));
									data.add("tableNum",VipCardQuery.this.getIntent().getStringExtra("tableNum"));
									data.add("manCounts",VipCardQuery.this.getIntent().getStringExtra("man"));
									data.add("womanCounts",VipCardQuery.this.getIntent().getStringExtra("woman"));
									new Server().connect(VipCardQuery.this, "reserveTableNum", "ChoiceWebService/services/HHTSocket?/reserveTableNum", data, new OnServerResponse() {
										@Override
										public void onResponse(String result) {
											getLoadingDialog().dismiss();
											final String[] res=ValueUtil.isNotEmpty(result)?result.split("@"):null;
											if(res!=null&&!res[0].equals("0")){
												showToast(res[1]);
												return;
											}else if(res!=null&&res[0].equals("0")){
												Intent intent = new Intent(VipCardQuery.this, Eatables.class);
												Bundle bundle=new Bundle();
												bundle.putString("orderId",res[1]);
												bundle.putString("manCs",VipCardQuery.this.getIntent().getStringExtra("man"));
												bundle.putString("womanCs",VipCardQuery.this.getIntent().getStringExtra("woman"));
												bundle.putString("tableNum",VipCardQuery.this.getIntent().getStringExtra("tableNum"));
												intent.putExtra("topBundle", bundle);
												VipCardQuery.this.startActivity(intent);
												VipCardQuery.this.finish();
											}else{
												showToast("网络错误！");
											}
										}
										@Override
										public void onBeforeRequest() {
											getLoadingDialog().show();
										}
									});
								}
							}).setNegativeButton("取消", null).show();
				}else if(VipCardQuery.this.getIntent().getStringExtra("state").equals("No")){
					CList<Map<String, String>> clist = new CList<Map<String,String>>();
					clist.add("deviceId", SharedPreferencesUtils.getDeviceId(VipCardQuery.this));
					clist.add("userCode", SharedPreferencesUtils.getUserCode(VipCardQuery.this));
					clist.add("tableNum", VipCardQuery.this.getIntent().getStringExtra("tableNum"));
					clist.add("manCounts", VipCardQuery.this.getIntent().getStringExtra("man"));
					clist.add("womanCounts", VipCardQuery.this.getIntent().getStringExtra("woman"));
					clist.add("ktKind", "1");
					clist.add("openTablemwyn", "1");

					new Server().connect(VipCardQuery.this, Constants.FastMethodName.STARTTABLE_METHODNAME, Constants.FastWebService.STARTTABLE_WSDL, clist, new OnServerResponse() {
						@Override
						public void onResponse(String startresult) {
							getLoadingDialog().dismiss();
							if(ValueUtil.isNotEmpty(startresult)){
								String[] str = startresult.split("@");
								if(str[0].equals("0")){
									SingleMenu.getMenuInstance().setManCounts(VipCardQuery.this.getIntent().getStringExtra("man"));
									SingleMenu.getMenuInstance().setWomanCounts(VipCardQuery.this.getIntent().getStringExtra("woman"));
									SingleMenu.getMenuInstance().setTableNum(VipCardQuery.this.getIntent().getStringExtra("tableNum"));
									SingleMenu.getMenuInstance().setUserCode(SharedPreferencesUtils.getUserCode(VipCardQuery.this));
									SingleMenu.getMenuInstance().setMenuOrder(str[1]);
									Intent intent = new Intent(VipCardQuery.this, Eatables.class);
									startActivity(intent);
									VipCardQuery.this.finish();
								}
							}else{
								Toast.makeText(VipCardQuery.this, "当前网络不稳定", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onBeforeRequest() {
							getLoadingDialog().show();
						}

					});
				}else if(VipCardQuery.this.getIntent().getStringExtra("state").equals("query")){
					this.finish();
				}
				break;
			case R.id.vip_card_cancle:
				this.finish();
				break;
			default:
				break;
		}
	}
	public void showToast(String Text){
		Toast.makeText(this, Text, Toast.LENGTH_LONG).show();
	}
}
