package cn.com.choicesoft.view;

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
import cn.com.choicesoft.util.*;

/**
 * ���-APP����
 */
public class SoftSettingDialog extends DialogFragment {

	public SoftSettingDialog() {}

	static DialogClickListener mListener;

	public interface DialogClickListener {
		
		void doFinishClick();
		
		void doFtpItemClick();

		void doIpItemClick(int typ);
		
		void doUpdatedateItemClick();
		
		void doDeviceRegisterClick();
		
		void doDeviceEncodeClick();
		
		//���õ�½ҳ�汳��
		void doSetBackground();
		//���ñ���
		void reSetBackground();
		//�����˵���ѯ�������ͼƬ
		void doSetLeftbg();
		//�����˵���ѯ�������ͼƬ
		void reSetLeftbg();
		/**
		 * ѡ��̨λ���෽ʽ 1 �������  2״̬����
		 * @param classifyText 
		 */
		void doTableClassify(TextView classifyText);
		
		void doPresentAuth();
		
		void doShopEncode();
		void doBlueTooth();
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		final View view = LayoutInflater.from(getActivity()).inflate(R.layout.softsetting, null);
		String title = getArguments().getString("title");
		if (title != null && title.length() > 0) {
			TextView t = (TextView) view.findViewById(R.id.dia_starttable_title);
			t.setText(title);
		}
		
		Button button = (Button) view.findViewById(R.id.dia_softsetting_finish);
		RelativeLayout updateData = (RelativeLayout) view.findViewById(R.id.dia_softsetting_updatedata);
		RelativeLayout ipSet = (RelativeLayout) view.findViewById(R.id.dia_softsetting_ipset);
		RelativeLayout ftpSet = (RelativeLayout) view.findViewById(R.id.dia_softsetting_ftpset);
		RelativeLayout deviceRegister = (RelativeLayout) view.findViewById(R.id.dia_softsetting_deviceRegister);
		RelativeLayout deviceEncode = (RelativeLayout) view.findViewById(R.id.dia_softsetting_deviceencode);
		RelativeLayout shopEncode = (RelativeLayout) view.findViewById(R.id.dia_softsetting_shopencode);
		RelativeLayout BackGround = (RelativeLayout) view.findViewById(R.id.dia_softsetting_setbackground);
		RelativeLayout logout = (RelativeLayout) view.findViewById(R.id.dia_softsetting_logout);
		Button resetbackground = (Button)view.findViewById(R.id.dialog_chongzhibackground_button);
		RelativeLayout leftbg = (RelativeLayout) view.findViewById(R.id.dia_softsetting_setleftbg);
		Button resetLeftBg = (Button)view.findViewById(R.id.dialog_chongzhileftbg_button);
		TextView banbenhao = (TextView) view.findViewById(R.id.dialog_textview_appversion);
		RelativeLayout presentAuth = (RelativeLayout) view.findViewById(R.id.dia_softsetting_presentAuth);//������Ȩ���
		RelativeLayout waitIp = (RelativeLayout) view.findViewById(R.id.dia_softsetting_wait_ipset);//���õ�λ�ܲ�IP
		RelativeLayout blueTooth = (RelativeLayout) view.findViewById(R.id.dia_softsetting_BlueTooth);//���õ�λ�ܲ�IP

        banbenhao.setText(new AppUpdate(getActivity()).getVersion());//������ʾ�汾��
        /*�п���л� 2014��6��10��*/
        RelativeLayout chineseSnack=(RelativeLayout)view.findViewById(R.id.dia_softsetting_chinese_snack);
        final TextView chinese_snack_TextView=(TextView)view.findViewById(R.id.setting_chinese_snack_showTextView);
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
                                SharedPreferencesUtils.setLanguage(getActivity(),0);
                                chinese_snack_TextView.setText(R.string.snack_model);
                                break;
                            case 1:
                                SharedPreferencesUtils.setChineseSnack(getActivity(),1);
                                SharedPreferencesUtils.setLanguage(getActivity(),0);
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
        /*���������� 2014��5��14��*/
        RelativeLayout language=(RelativeLayout)view.findViewById(R.id.dia_softsetting_language);
        final TextView textView=(TextView)language.findViewById(R.id.setting_language_showTextView);
        switch (SharedPreferencesUtils.getLanguage(getActivity())){
            case 0://��������
                textView.setText(R.string.chinese);
                resetbackground.setBackgroundResource(R.drawable.chongzhi_button_bg);
                resetLeftBg.setBackgroundResource(R.drawable.chongzhi_button_bg);
                break;
            case 1://��������
                textView.setText(R.string.chinese_tdl);
                resetbackground.setBackgroundResource(R.drawable.chongzhi_button_bg);
                resetLeftBg.setBackgroundResource(R.drawable.chongzhi_button_bg);
                break;
            case 2://Ӣ��
                textView.setText(R.string.english);
                resetbackground.setBackgroundResource(R.drawable.chongzhi_button_en_bg);
                resetLeftBg.setBackgroundResource(R.drawable.chongzhi_button_en_bg);
                break;
            default://Ĭ������
                textView.setText(R.string.chinese);
                resetbackground.setBackgroundResource(R.drawable.chongzhi_button_bg);
                resetLeftBg.setBackgroundResource(R.drawable.chongzhi_button_bg);
                break;
        }
        language.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout=(LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.language_setting_dialog, null);
                RadioButton cn=(RadioButton)layout.findViewById(R.id.language_cn);
                RadioButton hk=(RadioButton)layout.findViewById(R.id.language_hk);
                RadioButton en=(RadioButton)layout.findViewById(R.id.language_en);
                switch (SharedPreferencesUtils.getLanguage(getActivity())){
                    case 0://��������
                        cn.setChecked(true);
                        break;
                    case 1://��������
                        hk.setChecked(true);
                        break;
                    case 2://Ӣ��
                        en.setChecked(true);
                        break;
                    default://Ĭ������
                        cn.setChecked(true);
                        break;
                }
                final RadioGroup radioGroup=(RadioGroup)layout.findViewById(R.id.language_radioGroup);
                AlertDialog alertDialog=new AlertDialog.Builder(getActivity(),R.style.edittext_dialog).setTitle(R.string.language_setting).setView(layout)
                .setNegativeButton(R.string.confirm,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (radioGroup.getCheckedRadioButtonId()){
                            case R.id.language_cn://��������
                                SharedPreferencesUtils.setLanguage(getActivity(),0);
                                textView.setText(R.string.chinese);
                                break;
                            case R.id.language_hk://��������
                                SharedPreferencesUtils.setLanguage(getActivity(),1);
                                textView.setText(R.string.chinese_tdl);
                                break;
                            case R.id.language_en://Ӣ��
                                SharedPreferencesUtils.setLanguage(getActivity(),2);
                                textView.setText(R.string.english);
                                break;
                            default://Ĭ������
                                SharedPreferencesUtils.setLanguage(getActivity(), 0);
                                textView.setText(R.string.chinese);
                                break;
                        }
                        new AlertDialog.Builder(getActivity(),R.style.edittext_dialog)
                                .setTitle(R.string.hint)
                                .setMessage(R.string.hint_refresh)
                                .setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                }).setPositiveButton(R.string.dia_cancel,null).show();
                AlertDialogTitleUtil.gravity(alertDialog, Gravity.CENTER);
            }
        });
		/*�Ƿ�������Ů ------------2014��4��28��*/
		ToggleButton disguiesSexToggle = (ToggleButton) view.findViewById(R.id.dia_softsetting_isDisguishToggleButton);
		disguiesSexToggle.setChecked(SharedPreferencesUtils.getDisguishGender(getActivity()));
		disguiesSexToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferencesUtils.setDisguishGender(getActivity(), isChecked);
			}
		});
		/*�Ƿ�ʹ�ñ��ؿ� Mc-2014��3��12��*/
		RelativeLayout classify = (RelativeLayout) view.findViewById(R.id.dia_softsetting_classify);
		final TextView classifyText = (TextView) view.findViewById(R.id.taiweihuafenfanghsi_classifyText);
		classifyText.setText(SharedPreferencesUtils.getTableClass(getActivity()).equals("1")?getString(R.string.area):getString(R.string.state));
		classify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {// 1 �������  2״̬����
				if(mListener!=null){
					mListener.doTableClassify(classifyText);
				}
			}
		});
		/*�Ƿ�ʹ�ñ��ؿ� Mc-2014��3��11��*/
		ToggleButton isNet=(ToggleButton)view.findViewById(R.id.dia_softsetting_isNetToggleButton);
		isNet.setChecked(SharedPreferencesUtils.getIsNet(getActivity()));
		isNet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				SharedPreferencesUtils.setIsNet(getActivity(), isChecked);
			}
		});
        /*��Ա�Ƿ����Mc-2014��5��9��*/
        ToggleButton isVip=(ToggleButton)view.findViewById(R.id.dia_softsetting_isShowVipToggleButton);
        isVip.setChecked(SharedPreferencesUtils.getIsVip(getActivity()));
        isVip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesUtils.setIsVip(getActivity(), isChecked);
            }
        });
		/*�Ƿ�ʹ�ñ��ؿ� END*/
		//Ϊ�豸Ψһ��Ÿ�ֵ
		TextView physicalValue = (TextView) view.findViewById(R.id.dia_tv_physicalvalue);
		String uuId = SharedPreferencesUtils.getPhysicalId(getActivity());
		if(ValueUtil.isEmpty(uuId)){
			uuId = DeviceUuidFactory.getPhysicalId(getActivity());
			SharedPreferencesUtils.setPhysicalId(getActivity(), uuId);
		}
		physicalValue.setText(uuId);
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
					mListener.doIpItemClick(1);
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
		
		deviceRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (mListener != null) {
					mListener.doDeviceRegisterClick();
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
		
		shopEncode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.doShopEncode();
				}
			}
		});
		
		presentAuth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mListener != null){
					mListener.doPresentAuth();
				}
			}
		});
		
		//����ͼƬ·������
		BackGround.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.doSetBackground();
				}
			}
		});
		//���ñ���ͼƬ
		resetbackground.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.reSetBackground();
				}
			}
		});
		
		//�˵���ѯ�������ͼƬ·������
		leftbg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.doSetLeftbg();
				}
			}
		});
		//�����˵���ѯ�������ͼƬ
		resetLeftBg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.reSetLeftbg();
				}
			}
		});
		/*2015-04-09 ע������*/
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View logoutView=getActivity().getLayoutInflater().inflate(R.layout.alert_logout_layout,null);
				final EditText logout= (EditText) logoutView.findViewById(R.id.logout_time_edit);
				final EditText close= (EditText) logoutView.findViewById(R.id.close_time_edit);
				logout.setText(SharedPreferencesUtils.getLogoutTime(getActivity()).toString());
				close.setText(SharedPreferencesUtils.getCloseTime(getActivity()).toString());
				new AlertDialog.Builder(getActivity(),R.style.edittext_dialog)
						.setTitle(R.string.setting_logout_time)
						.setView(logoutView)
						.setNegativeButton(R.string.cancle, null)
						.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String ltime=logout.getText().toString();
								String ctime=close.getText().toString();
								SharedPreferencesUtils.setLogoutTime(getActivity(),ValueUtil.isEmpty(ltime)?0:Integer.valueOf(ltime));
								SharedPreferencesUtils.setCloseTime(getActivity(),ValueUtil.isEmpty(ctime)?0:Integer.valueOf(ctime));
							}
						}).show();
			}
		});
		/*2015��04��24 �ܲ���λIP����*/
		waitIp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.doIpItemClick(2);
				}
			}
		});
		blueTooth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.doBlueTooth();
				}
			}
		});
		dialog.setContentView(view);
		return dialog;
	}

	
	public static SoftSettingDialog newInstance(String title,DialogClickListener listener) {
		SoftSettingDialog frag = new SoftSettingDialog();
		Bundle b = new Bundle();
		b.putString("title", title);
		frag.setArguments(b);
		mListener = listener;
		
		return frag;
	}
	

}
