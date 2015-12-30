package cn.com.choicesoft.util;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.CouponMain;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

/**
 * 优惠验证类
 * @Author:M.c
 * @CreateDate:2014-1-20
 * @Email:JNWSCZH@163.COM
 */
public class VerPrivilege {
	private LoadingDialog dialog = null;
	public LoadingDialog getDialog(Activity activity) {
		if(dialog==null){
			dialog = new LoadingDialogStyle(activity, activity.getString(R.string.please_wait));
			dialog.setCancelable(true);
		}
		return dialog;
	}
	/**
	 * 判断优惠功能是否需要 权限验证
	 * @param activity
	 * @param v
	 * @param resMode
	 */
	public void verRight(final Activity activity,final View v,final IResult<View> resMode){
		boolean bool=true;
		if(ValueUtil.isNotEmpty(TsData.coupPay)&&ValueUtil.isEmpty(TsData.moneyPay)){
			String name="";
			if(v.getTag() instanceof CouponMain){
				CouponMain main=(CouponMain) v.getTag();
				name=main.getNam();
			}
			if(v.getTag() instanceof String[]){
				name=((String[])v.getTag())[2];
			}
			bool = ValueUtil.isEmpty(TsData.coupPay.get(name));
			if(bool){//判断是否需要授权
				LinearLayout linear=new LinearLayout(activity);
				linear.setOrientation(LinearLayout.VERTICAL);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				params.setMargins(12, 5, 12,12);
				final EditText user=new EditText(activity);
				user.setHint(R.string.input_user_name);
				user.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				final EditText pwd=new EditText(activity);
				pwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				pwd.setHint(R.string.input_user_password);
				linear.addView(user,params);
				linear.addView(pwd,params);
				new AlertDialog.Builder(activity,R.style.edittext_dialog).setTitle(R.string.youhui_shouquan).setView(linear).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CList<Map<String,String>> list=new CList<Map<String,String>>();
						list.add("deviceId", SharedPreferencesUtils.getDeviceId(activity));
						list.add("userCode", user.getText().toString());
						list.add("userPass", pwd.getText().toString());
						new Server().connect(activity, "checkAuth", "ChoiceWebService/services/HHTSocket?/checkAuth", list, new OnServerResponse() {
							@Override
							public void onResponse(String result) {
								getDialog(activity).dismiss();
								if(ValueUtil.isNotEmpty(result)){
									String[] res=result.split("@");
									if(!res[0].equals("0")){
										Toast.makeText(activity, res[1], Toast.LENGTH_LONG).show();
									}else{
										resMode.result(v);
									}
								}else{
									Toast.makeText(activity, R.string.shouquan_error, Toast.LENGTH_LONG).show();
								}
							}
							@Override
							public void onBeforeRequest() {
								getDialog(activity).show();
							}
						});
					}
				}).setNegativeButton(R.string.cancle,null).show();
			}else{
				resMode.result(v);
			}
		}else if(TsData.moneyPay!=null&&TsData.moneyPay.size()>0){
			new AlertDialog.Builder(activity).setTitle(R.string.hint).setMessage(R.string.pay_money_not_privilege1)
			.setPositiveButton(R.string.confirm, null).show();
		}else{
			resMode.result(v);
		}
	
	}
}
