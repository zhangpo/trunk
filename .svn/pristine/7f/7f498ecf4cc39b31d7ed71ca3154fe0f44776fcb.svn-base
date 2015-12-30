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

public class PresentCountDialog extends DialogFragment {
	

	public PresentCountDialog() {}

	static DialogPresentCountClickListener mListener;

	public interface DialogPresentCountClickListener {
		void doPositiveClick(String giftCounts, String cancelGiftCounts, String reason);

		void doNegativeClick();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dia_present, null);
		String title = getArguments().getString("title");
		if (!ValueUtil.isEmpty(title)) {
			TextView t = (TextView) layout.findViewById(R.id.dia_tv_presenttitle);
			t.setText(title);
		}
				
		
		final EditText presentCount = (EditText) layout.findViewById(R.id.yidian_et_presentcount);
		final EditText cancelPresent = (EditText) layout.findViewById(R.id.yidian_et_cancelPresent);
		
		//实例化滚轮，为滚轮填充数据
		ArrayList<PresentReason> presentLists = getArguments().getParcelableArrayList("reasonlists");
		//先暂时这样做
		final String [] itemReasons = new String[presentLists.size()];
		for(int i = 0;i<presentLists.size();i++){
			itemReasons[i] = presentLists.get(i).getVName();
		}
		
		final WheelView wheelView = (WheelView) layout.findViewById(R.id.dia_present_wheelview);
		wheelView.setVisibleItems(5);
		wheelView.setAdapter(new ArrayWheelAdapter<String>(itemReasons));
		wheelView.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
			}
		});
		
		

		View certain = layout.findViewById(R.id.dia_btn_presentcertainbtn);
		View cancel = layout.findViewById(R.id.dia_btn_presentcancelbtn);

		certain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (mListener != null) {
					String giftCounts = presentCount.getText().toString();
					String cancelCounts = cancelPresent.getText().toString();
					//此处很特殊
					String presentCode = wheelView.getCurrentItem()+"";
//					String presentCode = itemReasons[wheelView.getCurrentItem()];
					mListener.doPositiveClick(giftCounts, cancelCounts, presentCode);
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

	public static PresentCountDialog newInstance(String title,ArrayList<PresentReason> reasons,DialogPresentCountClickListener listener) {
		PresentCountDialog frag = new PresentCountDialog();
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putParcelableArrayList("reasonlists", reasons);
		frag.setArguments(b);
		mListener = listener;

		return frag;
	}

}
