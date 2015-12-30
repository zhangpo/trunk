package cn.com.choicesoft.table;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.Eatables;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.activity.VipFindActivity;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;


/**
 * 等位所需-按钮时间
 * @Author:M.c
 * @CreateDate:2014-2-15
 * @Email:JNWSCZH@163.COM
 */
public class ReserveTableEvent implements OnClickListener{
	private Activity activity;
	private LoadingDialog loadingDialog;
	private View view=null;
	private View sub_view=null;
	private View confirm_view=null;
	private AlertDialog alertDialog;
	private Map<String,String> map=null;
	public View getConfirm_view() {
		if(confirm_view==null){
			confirm_view=activity.getLayoutInflater().inflate(R.layout.confirm_table, null);
		}
		return confirm_view;
	}
	
	public Map<String, String> getMap() {
		if(map==null){
			map=(Map<String,String>)((MainActivity)activity).getLeftPw().getContentView().findViewById(R.id.yuding_orderList).getTag();
		}
		return map;
	}
	public void setMap(Map<String,String> map) {
		this.map=map;
	}

	public View getView() {
		if(view==null){
			view=activity.getLayoutInflater().inflate(R.layout.reserve_table, null);
		}
		return view;
	}
	public View getSub_view() {
		if(sub_view==null){
			sub_view=activity.getLayoutInflater().inflate(R.layout.sub_reserve_table, null);
		}
		return sub_view;
	}
	
	public LoadingDialog getLoadingDialog() {
		if(loadingDialog==null){
			loadingDialog = new LoadingDialogStyle(activity, activity.getString(R.string.please_wait));
			loadingDialog.setCancelable(true);
		}
		return loadingDialog;
	}
	public ReserveTableEvent(Activity activity) {
		this.activity=activity;
		this.view=null;
		this.sub_view=null;
	}
	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.yuding_add:
			this.view=null;
			Button cancle=(Button)getView().findViewById(R.id.add_res_table_cancle);//添加等位取消按钮
			Button confirm=(Button)getView().findViewById(R.id.add_res_table_confirm);//添加等位确认按钮
			EditText woman=(EditText)getView().findViewById(R.id.rtable_Edit_woman);
			woman.setOnKeyListener(new OnKeyListener() {  
	            @Override  
	            public boolean onKey(View v, int keyCode, KeyEvent event) {  
	                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {  
	                	addEvent();
	                    return true;  
	                }  
	                return false;  
	            }  
	        });  
			cancle.setOnClickListener(this);//取消事件
			confirm.setOnClickListener(this);//确认事件
			alertDialog=showDialog(getView(), activity.getString(R.string.yuding_table));
			break;
		case R.id.yuding_sub:
			this.sub_view=null;
			Button sub_concle=(Button)getSub_view().findViewById(R.id.sub_res_table_cancle);
			Button sub_fonfirm=(Button)getSub_view().findViewById(R.id.sub_res_table_ok);
			sub_concle.setOnClickListener(this);//取消事件
			sub_fonfirm.setOnClickListener(this);//确认事件
			alertDialog=showDialog(getSub_view(), activity.getString(R.string.cancel_reserve));
			break;
		case R.id.add_res_table_confirm:
			addEvent();
			break;
		case R.id.sub_res_table_ok:
			subEvent(null);
			break;
		case R.id.add_res_table_cancle:
		case R.id.confirm_table_cancle:
		case R.id.sub_res_table_cancle:
			backKey();
			break;
		case R.id.res_Order_cancle:
			((MainActivity)activity).getLeftPw().dismiss();
			break;
		case R.id.res_Order_confirm://转正事件
            orderConfirm();
			break;
		case R.id.confirm_table_confirm://转正确认
			confirmTable();
			break;
        case R.id.res_cancel_table:
            subEvent(getMap());
            break;
        case R.id.res_jiacai:
            jiaCai();
            break;
		default:
			break;
		}
	}
	/**
	 * 预定转正弹窗！
	 */
	public void orderChoice(Map<String,String> map){
		setMap(map);
        String [] items = {activity.getString(R.string.yuding_Order_off),activity.getString(R.string.yuding_cancel_table),activity.getString(R.string.vip_card_disButton),activity.getString(R.string.cancle)};
        ListAdapter diaAdapter=new ArrayAdapter<String>(activity, R.layout.dia_ifsave_item_layout, R.id.dia_ifsave_item_tv, items);
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle(R.string.order_not_food).
                setAdapter(diaAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int position) {
                switch (position) {
                    case 0:  //转正式台
                        orderConfirm();
                        break;
                    case 1:  //取消等位
                        subEvent(getMap());
                        break;
                    case 2:  //点菜
                        jiaCai();
                        break;
                    case 3:  //取消
                        break;
                    default:
                        break;
                }
            }
        }).show();
        AlertDialogTitleUtil.gravity(alertDialog,Gravity.CENTER);

	}

	/**
	 * 加菜
	 */
    public void jiaCai(){
        Intent intent=new Intent(activity, Eatables.class);
        TsData.isReserve=true;//初始化预定标识
        Bundle bundle=new Bundle();
        bundle.putString("orderId", getMap().get("orderId"));//账单号
        bundle.putString("manCs", getMap().get("manNum"));//男人数量
        bundle.putString("womanCs", getMap().get("womanNum"));//女人数量
        bundle.putString("tableNum", getMap().get("phone"));//台位号
        bundle.putString("member", "");//会员信息
        intent.putExtra("topBundle", bundle);
        intent.putExtra("direction", "MainDirection");
        activity.startActivity(intent);
    }

	/**
	 * 预订账单转正
	 */
    public void orderConfirm(){
        this.confirm_view=null;
        EditText phoneText=(EditText)getConfirm_view().findViewById(R.id.confirm_table_phone);
        phoneText.setText(getMap().get("phone"));
        EditText numberText=(EditText)getConfirm_view().findViewById(R.id.confirm_table_number);
        numberText.setText(getMap().get("tabNum"));
        EditText numText=(EditText)getConfirm_view().findViewById(R.id.confirm_table_num);
        numText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    confirmTable();
                    return true;
                }
                return false;
            }
        });
        Button confirm_concle=(Button)getConfirm_view().findViewById(R.id.confirm_table_cancle);
        Button confirm_ok=(Button)getConfirm_view().findViewById(R.id.confirm_table_confirm);
        confirm_concle.setOnClickListener(this);
        confirm_ok.setOnClickListener(this);
        alertDialog=showDialog(getConfirm_view(), activity.getString(R.string.reserve_to_table));
    }
	/**
	 * 添加预定事件
	 */
	public void addEvent(){
		EditText phone=(EditText)getView().findViewById(R.id.rtable_Edit_phone);
		EditText man=(EditText)getView().findViewById(R.id.rtable_Edit_man);
		EditText woman=(EditText)getView().findViewById(R.id.rtable_Edit_woman);
		CheckBox isVip=(CheckBox)getView().findViewById(R.id.rtable_isVip);
		final String phoneText=phone.getText().toString();
		final String manText=man.getText().toString();
		final String womanText=woman.getText().toString();
		if(ValueUtil.isNotEmpty(phoneText)){
			if(phoneText.matches("^[1][34578][0-9]{9}$")){
				if(ValueUtil.isEmpty(manText)&&ValueUtil.isEmpty(womanText)){
					showToast(R.string.people_not_null);
				}else{
					if(isVip.isChecked()){
						Intent intent = new Intent(activity,VipFindActivity.class);//打开会员支付页面
						TsData.isReserve=true;//初始化预定标识
						intent.putExtra("mark", "mark");
						intent.putExtra("tableNum", phoneText);
						intent.putExtra("Payable", "0");
						intent.putExtra("man", manText);
						intent.putExtra("woman", womanText);
						activity.startActivity(intent);
						alertDialog.dismiss();
					}else{
						CList<Map<String,String>> data=new CList<Map<String,String>>();
						data.add("deviceId", SharedPreferencesUtils.getDeviceId(activity));
						data.add("userCode", SharedPreferencesUtils.getUserCode(activity));
						data.add("tableNum",phoneText);
						data.add("manCounts",manText);
						data.add("womanCounts",womanText);
//						0@单号@等位单号
						new Server().connect(activity, "reserveTableNum", "ChoiceWebService/services/HHTSocket?/reserveTableNum", data, new OnServerResponse() {
							@Override
							public void onResponse(String result) {
								getLoadingDialog().dismiss();
								final String[] res=ValueUtil.isNotEmpty(result)?result.split("@"):null;
								if(res!=null&&!res[0].equals("0")){
									showToast(res[1]);
									return;
								}else if(res!=null&&res[0].equals("0")){
									((MainActivity)activity).showPw();
									alertDialog.dismiss();
									LinearLayout layout=(LinearLayout)activity.getLayoutInflater().inflate(R.layout.reserve_dialog, null);
									((TextView)layout.findViewById(R.id.reserve_ID)).setText(res[2]);
									AlertDialog alert=new AlertDialog.Builder(activity,R.style.edittext_dialog).setTitle(R.string.wait_ready_order)
									.setView(layout)
									.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											TsData.isReserve=true;//初始化预定标识
											Intent intent = new Intent(activity, Eatables.class);
											Bundle bundle=new Bundle();
											bundle.putString("orderId",res[1]);
											bundle.putString("manCs",manText);
											bundle.putString("womanCs",womanText);
											bundle.putString("tableNum",phoneText);
											intent.putExtra("topBundle", bundle);
											activity.startActivity(intent);
										}
									}).setPositiveButton(R.string.cancle, null).show();
									AlertDialogTitleUtil.gravity(alert, Gravity.CENTER);
								}else{
									ToastUtil.toast(activity,R.string.net_error);
								}
							}
							@Override
							public void onBeforeRequest() {
								getLoadingDialog().show();
							}
						});
					}
				}
			}else{
				showToast(R.string.phone_error);
			}
		}else{
			showToast(R.string.phone_not_null);
		}
	}
	/**
	 * 删除预定事件
	 */
	public void subEvent(Map<String,String> map){
        boolean isOK=true;
        String phone="";
        String number="";
        if(map==null){
		    phone=((EditText)getSub_view().findViewById(R.id.sub_res_table_phone)).getText().toString();
		    number=((EditText)getSub_view().findViewById(R.id.sub_res_table_Number)).getText().toString();
        }else{
            phone=map.get("phone");
            number=map.get("tabNum");
        }
        if(ValueUtil.isNotEmpty(phone)){
			if(phone.matches("^[1][3458][0-9]{9}$")){
				isOK=true;
			}
		}
        if(ValueUtil.isNotEmpty(number)){
            isOK=true;
        }
        if(isOK){
            CList<Map<String,String>> data=new CList<Map<String,String>>();
            data.add("deviceId", SharedPreferencesUtils.getDeviceId(activity));
            data.add("userCode", SharedPreferencesUtils.getUserCode(activity));
            data.add("tableNum", phone);
            data.add("misOrderId", number);
            new Server().connect(activity, "cancelReserveTableNum", "ChoiceWebService/services/HHTSocket?/cancelReserveTableNum", data, new OnServerResponse() {
                @Override
                public void onResponse(String result) {
                    getLoadingDialog().dismiss();
                    if(ValueUtil.isNotEmpty(result)){
                        String[] res=result.split("@");
                        if(res.length>0){
                            if(!res[0].equals("0")){
                                showToast(res[1]);
                                return;
                            }
                            backKey();
                            showToast(R.string.success);
                            ((MainActivity)activity).showPw();
                        }
                    }else{
                        showToast(R.string.net_error);
                    }
                }

                @Override
                public void onBeforeRequest() {
                    getLoadingDialog().show();
                }
            });
        }else{
            ToastUtil.toast(activity,R.string.cancel_error);
        }

	}
	/**
	 * 台位转正实现
	 */
	public void confirmTable(){
		String phone=((EditText)getConfirm_view().findViewById(R.id.confirm_table_phone)).getText().toString();
		String num=((EditText)getConfirm_view().findViewById(R.id.confirm_table_num)).getText().toString();
		if(phone.matches("^[1][3458][0-9]{9}$")){
			if(ValueUtil.isEmpty(num)){
				showToast(R.string.tagTai_not_null);
				return;
			}
		}else{
			showToast(R.string.phone_error);
			return;
		}
		CList<Map<String,String>> data=new CList<Map<String,String>>();
		data.add("deviceId", SharedPreferencesUtils.getDeviceId(activity));
		data.add("userCode", SharedPreferencesUtils.getUserCode(activity));
		data.add("tablenumSource",phone);
		data.add("tablenumDest",num);
		data.add("orderId",getMap().get("orderId"));
		new Server().connect(activity, "changeTableNum", "ChoiceWebService/services/HHTSocket?/changeTableNum", data, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getLoadingDialog().dismiss();
				if(ValueUtil.isNotEmpty(result)){
					String[] res=result.split("@");
					if(res.length>0&&!res[0].equals("0")){
						showToast(res[1]);
					}else{
						showToast(res[1]);
						setMap(null);
						((MainActivity)activity).showPw();
						((MainActivity)activity).refreshDisplay();
						((MainActivity)activity).getLeftPw().dismiss();
						alertDialog.dismiss();
					}
				}
			}
			@Override
			public void onBeforeRequest() {
				getLoadingDialog().show();
			}
		});
	}
	public void showToast(String Text){
		Toast.makeText(activity, Text, Toast.LENGTH_LONG).show();
	}
	public void showToast(int id){
		Toast.makeText(activity, id, Toast.LENGTH_LONG).show();
	}
	/**
	 * 显示弹窗 方法
	 * @param view
	 * @param title
	 */
	public AlertDialog showDialog(View view,String title){
		AlertDialog dialog=new AlertDialog.Builder(activity,R.style.edittext_dialog).setTitle(title).setView(view).show();
		AlertDialogTitleUtil.gravity(dialog,Gravity.CENTER);
		return dialog;
	}
	/**
	 * 返回事件
	 */
	public void backKey(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Instrumentation inst = new Instrumentation();
				inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
			}
		}).start();
	}
}
