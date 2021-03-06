package cn.com.choicesoft.chinese.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.util.AlertDialogTitleUtil;
import cn.com.choicesoft.util.AppUpdate;
import cn.com.choicesoft.util.SharedPreferencesUtils;

/**
 * 中餐设置界面
 */
public class ChineseSoftSettingDialog extends DialogFragment {

	public ChineseSoftSettingDialog() {}

	static DialogClickListener mListener;

	public interface DialogClickListener {
		
		void doFinishClick();
		
		void doFtpItemClick();

		void doIpItemClick();
		
		void doUpdatedateItemClick();
		
		void doDeviceEncodeClick();
		
		//设置登陆页面背景
		void doSetBackground();
		//重置背景
		void reSetBackground();
		//设置账单查询界面左侧图片
		void doSetLeftbg();
		//重置账单查询界面左侧图片
		void reSetLeftbg();
		void updateApp();
		void doWebserviceIp();
		void doShopEncode();

		/**
		 * 选择台位分类方式 1 区域分类  2状态分类
		 * @param classifyText 
		 */
		void doTableClassify(TextView classifyText);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.chinese_softsetting, null);
		String title = getArguments().getString("title");
		if (title != null && title.length() > 0) {
			TextView t = (TextView) view.findViewById(R.id.chinese_dia_starttable_title);
			t.setText(title);
		}
		
		Button button = (Button) view.findViewById(R.id.chinese_dia_softsetting_finish);
		RelativeLayout updateData = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_updatedata);
		RelativeLayout ipSet = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_ipset);
		RelativeLayout ftpSet = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_ftpset);
		RelativeLayout deviceEncode = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_deviceencode);
		RelativeLayout BackGround = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_setbackground);
		Button resetbackground = (Button)view.findViewById(R.id.chinese_dialog_chongzhibackground_button);
		RelativeLayout leftbg = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_setleftbg);
		RelativeLayout upapp = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_checkappversion);
		RelativeLayout waitIp = (RelativeLayout) view.findViewById(R.id.dia_softsetting_wait_ipset);//设置等位总部IP
		RelativeLayout scode = (RelativeLayout) view.findViewById(R.id.dia_softsetting_shopencode);


		Button resetLeftBg = (Button)view.findViewById(R.id.chinese_dialog_chongzhileftbg_button);
		TextView banbenhao = (TextView) view.findViewById(R.id.chinese_dialog_textview_appversion);
//		RelativeLayout jnowAuthLayout = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_authuserandpwd);//美味不用等的授权
//		RelativeLayout jnowUploadLayout = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_uploaddata);//同步本地数据到美味不用等
        banbenhao.setText(new AppUpdate(getActivity()).getVersion());//设置显示版本号
        /*中快餐切换 2014年6月10日*/
        RelativeLayout chineseSnack=(RelativeLayout)view.findViewById(R.id.chinese_dia_softsetting_chinese_snack);
        final TextView chinese_snack_TextView=(TextView)view.findViewById(R.id.chinese_setting_chinese_snack_showTextView);
        if(SharedPreferencesUtils.getChineseSnack(getActivity())==0){
            chinese_snack_TextView.setText(R.string.snack_model);
        }else{
            chinese_snack_TextView.setText(R.string.chinese_model);
        }
        chineseSnack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] choice={getString(R.string.snack_model),getString(R.string.chinese_model)};
                ListAdapter diaAdapter=new ArrayAdapter<String>(getActivity(), R.layout.dia_ifsave_item_layout, R.id.dia_ifsave_item_tv, choice);
                AlertDialog alertDialog=new AlertDialog.Builder(getActivity(),R.style.edittext_dialog).setTitle(R.string.chinese_snack).setAdapter(diaAdapter,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case 0:
                                SharedPreferencesUtils.setChineseSnack(getActivity(),0);
                                chinese_snack_TextView.setText(R.string.snack_model);
                                break;
                            case 1:
                                SharedPreferencesUtils.setChineseSnack(getActivity(),1);
                                chinese_snack_TextView.setText(R.string.chinese_model);
                                break;
                            default:
                                break;
                        }
                        new AlertDialog.Builder(getActivity(),R.style.edittext_dialog)
                                .setTitle(R.string.hint)
                                .setMessage(R.string.hint_model_success)
                                .setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                }).show();
                    }
                }).show();
                AlertDialogTitleUtil.gravity(alertDialog,Gravity.CENTER);
            }
        });
		/*是否区分男女 ------------2014年4月28日*/
		ToggleButton disguiesSexToggle = (ToggleButton) view.findViewById(R.id.chinese_dia_softsetting_isDisguishToggleButton);
		disguiesSexToggle.setChecked(SharedPreferencesUtils.getDisguishGender(getActivity()));
		disguiesSexToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferencesUtils.setDisguishGender(getActivity(), isChecked);
			}
		});
		/*是否使用本地库 Mc-2014年3月12日*/
		RelativeLayout classify = (RelativeLayout) view.findViewById(R.id.chinese_dia_softsetting_classify);
		final TextView classifyText = (TextView) view.findViewById(R.id.chinese_taiweihuafenfanghsi_classifyText);
		String classify1 = "";
		if (SharedPreferencesUtils.getTableClass(getActivity()).equals("1")) {
			classify1 = getString(R.string.area);
		} else if(SharedPreferencesUtils.getTableClass(getActivity()).equals("2")){
			classify1 = getString(R.string.lc);
		}else if (SharedPreferencesUtils.getTableClass(getActivity()).equals("3")) {
			classify1 = getString(R.string.state);
		}
		classifyText.setText(classify1);
		classify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {// 1 区域分类  2状态分类
				if(mListener!=null){
					mListener.doTableClassify(classifyText);
				}
			}
		});
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (mListener != null) {
					mListener.doFinishClick();
				}

			}
		});
		
		ftpSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.doFtpItemClick();
				}
			}
		});
		
		ipSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.doIpItemClick();
				}
			}
		});
		
		updateData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.doUpdatedateItemClick();
				}
			}
		});
		
		deviceEncode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (mListener != null) {
					mListener.doDeviceEncodeClick();
				}
				
			}
		});

		//背景图片路径设置
		BackGround.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.doSetBackground();
				}
			}
		});
		//重置背景图片
		resetbackground.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.reSetBackground();
				}
			}
		});
		
		//账单查询界面左侧图片路径设置
		leftbg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.doSetLeftbg();
				}
			}
		});
		upapp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener!=null){
					mListener.updateApp();
				}
			}
		});
		//重置账单查询界面左侧图片
		resetLeftBg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.reSetLeftbg();
				}
			}
		});
		/*2015年04月24 总部等位IP设置*/
		waitIp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.doWebserviceIp();
				}
			}
		});
		scode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.doShopEncode();
				}
			}
		});
		dialog.setContentView(view);
		return dialog;
	}

	
	public static ChineseSoftSettingDialog newInstance(String title,DialogClickListener listener) {
		ChineseSoftSettingDialog frag = new ChineseSoftSettingDialog();
		Bundle b = new Bundle();
		b.putString("title", title);
		frag.setArguments(b);
		mListener = listener;
		
		return frag;
	}
	

}
