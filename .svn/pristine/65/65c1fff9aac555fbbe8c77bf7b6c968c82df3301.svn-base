package cn.com.choicesoft.view;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.PresentReason;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.widget.ArrayWheelAdapter;
import cn.com.choicesoft.widget.OnWheelChangedListener;
import cn.com.choicesoft.widget.WheelView;

public class PresentAuthDialog extends DialogFragment {

	public PresentAuthDialog() {}

	static DialogPresentAuthClickListener mListener;

	public interface DialogPresentAuthClickListener {
		void doPositiveClick(String authorNum, String authorPwd, String reason);

		void doNegativeClick();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dia_present_auth, null);
		String title = getArguments().getString("title");
		if (!ValueUtil.isEmpty(title)) {
			TextView t = (TextView) layout.findViewById(R.id.dia_tv_presenttitle);
			t.setText(title);
		}

		final EditText authorNum = (EditText) layout.findViewById(R.id.yidian_et_authorizeId);
		final EditText authorPwd = (EditText) layout.findViewById(R.id.yidian_et_authorizepwd);
		
		//实例化滚轮，为滚轮填充数据
		ArrayList<PresentReason> presentLists = getArguments().getParcelableArrayList("reasonlists");
		//先暂时这样做
		final String [] itemReasons = new String[presentLists.size()];
		for(int i = 0;i<presentLists.size();i++){
			itemReasons[i] = presentLists.get(i).getVName();
		}
		
		final WheelView wheelView = (WheelView) layout.findViewById(R.id.dia_presentauth_wheelview);
		wheelView.setVisibleItems(5);
//		wheelView.setCyclic(true);
		wheelView.setAdapter(new ArrayWheelAdapter<String>(itemReasons));
		wheelView.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				
			}
		});
		
		

		View certain = layout.findViewById(R.id.dia_btn_presentcertainbtn);
		View cancel = layout.findViewById(R.id.dia_btn_presentcancelbtn);

		certain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (mListener != null) {
					String authNumber = authorNum.getText().toString();
					String authPassWord = authorPwd.getText().toString();
					//此处很特殊
					String presentCode = wheelView.getCurrentItem()+"";
//					String presentCode = itemReasons[wheelView.getCurrentItem()];
					mListener.doPositiveClick(authNumber, authPassWord, presentCode);
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

		dialog.setContentView(layout);
		return dialog;
	}

	public static PresentAuthDialog newInstance(String title,ArrayList<PresentReason> reasons,DialogPresentAuthClickListener listener) {
		PresentAuthDialog frag = new PresentAuthDialog();
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putParcelableArrayList("reasonlists", reasons);
		frag.setArguments(b);
		mListener = listener;

		return frag;
	}

}
