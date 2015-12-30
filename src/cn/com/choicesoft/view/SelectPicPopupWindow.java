package cn.com.choicesoft.view;


import cn.com.choicesoft.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class SelectPicPopupWindow extends PopupWindow {
	
	
	private Button btn_cancel;
	private LinearLayout mLinearLayout;
	private View mMenuView;
	
	public SelectPicPopupWindow(Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.selectpic_popupwindow, null);
		
		mLinearLayout = (LinearLayout) mMenuView.findViewById(R.id.select_popupwin_layout_content);
		
		btn_cancel = (Button) mMenuView.findViewById(R.id.select_popupwin_btn_cancel);
		//ȡ����ť
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//���ٵ�����
				removeViews();//���popupwindow�ϵ�view
				dismiss();
			}
		});
		//����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		//����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.FILL_PARENT);
		//����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		//����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.AnimBottom);
		//ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		//mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						removeViews();//���popupwindow�ϵ�view
						dismiss();
					}
				}				
				return true;
			}
		});

	}
	
	
	public void addToView(View view){
		mLinearLayout.addView(view);
	}
	
	public void removeViews(){
		mLinearLayout.removeAllViews();
	}
}
