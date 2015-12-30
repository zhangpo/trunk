package cn.com.choicesoft.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.util.SharedPreferencesUtils;
import cn.com.choicesoft.util.ValueUtil;

/**
 * 总部地址设置
 */
public class HdIpSettingDialog extends DialogFragment {

	public HdIpSettingDialog() {}
	static DialogHdIpClickListener mListener;

	public interface DialogHdIpClickListener {
		void doPositiveClick(String ipaddress);

		void doNegativeClick();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Dialog dialog = new Dialog(getActivity(), R.style.edittext_dialog);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.hdipset, null);
		String title = getArguments().getString("title");
		if (!ValueUtil.isEmpty(title)) {
			TextView t = (TextView) view.findViewById(R.id.dia_tv_ipsettitle);
			t.setText(title);
		}
		
		TextView ipShow = (TextView) view.findViewById(R.id.dia_ipsetting_ipshow);
        ipShow.setText(SharedPreferencesUtils.getWaitUrl(getActivity()));
		

		final EditText ipAddress = (EditText) view.findViewById(R.id.dia_et_ipsetip);

		View certain = view.findViewById(R.id.dia_btn_ipsetcertainbtn);
		View cancel = view.findViewById(R.id.dia_btn_ipsetcancelbtn);

		certain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (mListener != null) {
					String ipadd = ipAddress.getText().toString();
					mListener.doPositiveClick(ipadd);
				}

			}

		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (mListener != null) {
					mListener.doNegativeClick();
				}
			}

		});
		dialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

				if (keyCode==KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
				}
				
				return false;
			}
		});
		dialog.setContentView(view);
		return dialog;
	}

	public static HdIpSettingDialog newInstance(String title,DialogHdIpClickListener listener) {
		HdIpSettingDialog frag = new HdIpSettingDialog();
		Bundle b = new Bundle();
		b.putString("title", title);
		frag.setArguments(b);
		mListener = listener;
		return frag;
	}

}
