package cn.com.choicesoft.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.util.VipRecordUtil;

/**
 * 会员信息
 *@Author:M.c
 *@CreateDate:2014-3-31
 *@Email:JNWSCZH@163.COM
 */
public class VipMsg {
	private static List<VipRecord> vip=null;
	/**
	 * 会员信息显示
	 * @param activity
	 * @param tableNum
	 */
	public static void iniVip(final Activity activity,String tableNum,int vipImg){
		try {
			vip=null;
			vip=new VipRecordUtil().queryHandle(activity, tableNum);
		} catch (Exception e) {
			Log.e("SettleAccountsActivity-VipCardHandle-Error", e.getMessage());
		}
		ImageView img=(ImageView)activity.findViewById(vipImg);
		if(ValueUtil.isNotEmpty(vip)){
			img.setOnClickListener(new View.OnClickListener() {
				private PopupWindow pw=null;
				private int WIDTH=dip2px(activity,330f);
				private int HEIGHT=dip2px(activity,120f);
				@Override
				public void onClick(View paramView) {
					if(pw!=null&&pw.isShowing()){
						pw.dismiss();
					}else{
						View viewLayout=activity.getLayoutInflater().inflate(R.layout.vip_msg_layout,null);
						TextView phone=(TextView)viewLayout.findViewById(R.id.vip_msg_phone);
						TextView vipCode=(TextView)viewLayout.findViewById(R.id.vip_msg_vipCode);
						int textX=((Double)(WIDTH*0.45)).intValue();
						int phoneY=((Double)(HEIGHT*0.33)).intValue();
						int vipY=((Double)(HEIGHT*0.63)).intValue();
						phone.setX(textX);
						phone.setY(phoneY);
						phone.setText(vip.get(0).getPhone());
						vipCode.setText(vip.get(0).getCardNumber());
						vipCode.setX(textX);
						vipCode.setY(vipY);
						if(pw==null){
							pw=new PopupWindow(viewLayout,WIDTH,HEIGHT);
						}
						int width = activity.getWindowManager().getDefaultDisplay().getWidth();
						int popupwidth = pw.getWidth();
						LinearLayout layout=(LinearLayout)paramView.getParent();
						
						int pwX=layout.getLeft()+(paramView.getWidth()/2)-318;
						pw.showAsDropDown(paramView,-pwX,0);// TODO 如果切换到全屏时，1.6改成1即可.zjc
					}
				}
			});
			img.setVisibility(View.VISIBLE);
		}else{
			img.setVisibility(View.GONE);
		}
	}
	public static int dip2px(Activity activity, float dipValue){ 
        final float scale = activity.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 
	public static int px2dip(Context context, float pxValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(pxValue / scale + 0.5f); 
	}
}
