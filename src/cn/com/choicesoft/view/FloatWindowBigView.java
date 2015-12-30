package cn.com.choicesoft.view;

import cn.com.choicesoft.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class FloatWindowBigView extends LinearLayout {

	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;

	public FloatWindowBigView(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.tc_float_window, this);
		View view = findViewById(R.id.big_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
//		ListView makeUpListView = (ListView) view.findViewById(R.id.tc_float_window_makeup);
//		ListView recommandListView = (ListView) view.findViewById(R.id.tc_float_window_recommend);
	}
}
