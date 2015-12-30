package cn.com.choicesoft.fragment;

import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.bean.CouponKind;
import cn.com.choicesoft.bean.CouponMain;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 优惠Fragment
 * @Author:M.c
 * @CreateDate:2014-1-6
 * @Email:JNWSCZH@163.COM
 */
public class PrivilegeFragment extends Fragment implements OnClickListener{
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private LoadingDialog dialog;
	private Bundle bundle;
	private ListProcessor pro;
	
	private int screenWidth;
	
	public ListProcessor getPro() {
		if(pro==null){
			pro=new ListProcessor();
		}
		return pro;
	}
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	
	public LoadingDialog getDialog() {
		if(dialog==null){
			dialog = new LoadingDialogStyle(getActivity(), getString(R.string.please_wait));
			dialog.setCancelable(true);
		}
		return dialog;
	}
	public Server getServer() {
		Server server=new Server();
		return server;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/**
	 * 初始化PrivilegeFragment-动态加载优惠分类按钮
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		List<CouponKind> list=null;
		try {
			list=getPro().query("select kindid,nam,typ from coupon_kind", null, getActivity(), CouponKind.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final View view=inflater.inflate(R.layout.privilege_fragment, null);
		DisplayMetrics dmm = new DisplayMetrics();
		// 获取窗口属性
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dmm);
		// 窗口宽度
		screenWidth = (int) (dmm.widthPixels*0.6);
		//获取LinearLayout
		LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.youHuiLB);
		linearLayout.setGravity(Gravity.LEFT|Gravity.CENTER);
		LinearLayout row=new LinearLayout(getActivity());
		row.setGravity(Gravity.LEFT|Gravity.CENTER);
		row.setOrientation(LinearLayout.HORIZONTAL);
		//设置按钮样式
		int rowwidth = (int) (ChioceActivity.ispad?130:(screenWidth/2.4));
		int rowtextsize = ChioceActivity.ispad?15:11;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(screenWidth*0.31), (int)(screenWidth*0.12));
		params.setMargins(0, 1, 1, 0);
		int count=0;
		int size=ValueUtil.isNotEmpty(list)?list.size():0;
		if(size<=0){
			return view;
		}
		//循环优惠分类
		for (CouponKind kind:list) {
			count++;
			Button but=new Button(getActivity());
			but.setText(kind.getNam());
			but.setTextSize(rowtextsize);
			but.setTextColor(Color.GRAY);
			//为按钮附事件
			but.setOnClickListener(PrivilegeFragment.this);
			but.setTag(kind);
			//判断but是不是超出屏幕宽度，如果超出者把按钮加到下一行
			if(((row.getChildCount()+1)*rowwidth)>screenWidth-10){
				row.getChildAt(row.getChildCount()-1).setBackgroundResource(R.drawable.but_right_bg);//but背景
				row.getChildAt(row.getChildCount()-1).setLayoutParams(params);//but样式
				linearLayout.addView(row);
				row=new LinearLayout(getActivity());//对多出屏幕的but加到下一行
				row.setGravity(Gravity.LEFT|Gravity.CENTER);
				row.setOrientation(LinearLayout.HORIZONTAL);
				but.setBackgroundResource(R.drawable.but_left_bg);
				row.addView(but,params);
			}else{
				if(row.getChildCount()==0){//第一个but
					but.setBackgroundResource(R.drawable.but_left_bg);
					row.addView(but,params);
				}else{//普通but
					but.setBackgroundResource(R.drawable.but_center_bg);
					params.setMargins(1, 1, 0, 0);
					row.addView(but,params);
				}
			}
			if(size<=count){//对多出but加载到最后一行
				row.setGravity(Gravity.LEFT|Gravity.CENTER);
				if(row.getChildCount()==1){//修改单一but样式
					row.getChildAt(row.getChildCount()-1).setBackgroundResource(R.drawable.but_alone_bg2);
				}else{//多个but样式
					row.getChildAt(row.getChildCount()-1).setBackgroundResource(R.drawable.but_right_bg);
					row.getChildAt(row.getChildCount()-1).setLayoutParams(params);
				}
				linearLayout.addView(row);
			}
		}
		return view;
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
	}
	/**
	 * 优惠分类but事件
	 */
	@Override
	public void onClick(View v) {
		List<CouponMain> list=null;
		try{
			list=getPro().query("SELECT id,code,nam,kindid,isshow FROM coupon_main where kindid=?", new String[]{((CouponKind)v.getTag()).getKindId()}, getActivity(), CouponMain.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		final LinearLayout linearLayout=(LinearLayout)getActivity().findViewById(R.id.youHuiMX);
		linearLayout.removeAllViews();
		LinearLayout hLinearLayout=new LinearLayout(getActivity());
		hLinearLayout.setGravity(Gravity.CENTER);
		if(ValueUtil.isEmpty(list)||list.size()<=0){
			return;
		}
		//根据手机和PAD属性不通
		double widthbl = ChioceActivity.ispad?0.44:0.52;
		double heightbl = ChioceActivity.ispad?0.14:0.15;
		int textsize = ChioceActivity.ispad?15:11;
		int count = ChioceActivity.ispad?2:1;

		for (CouponMain main:list) {//循环优惠券生成按钮
			if(main.getIsshow().equals("1")){
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(screenWidth*widthbl), (int)(screenWidth*heightbl));
				params.setMargins( 5, 8, 8, 5);
				Button but=new Button(getActivity());
				but.setTag(main);
				but.setMinWidth(100);
				but.setText(main.getNam());
				but.setTextSize(textsize);
				but.setBackgroundResource(R.drawable.yellow_button_background);
				but.setTextColor(Color.WHITE);
				but.setOnClickListener(new YouHuiMXClick());
				hLinearLayout.addView(but,params);
				if(hLinearLayout.getChildCount()==count){//每行显示but数
					linearLayout.addView(hLinearLayout);
					hLinearLayout=new LinearLayout(getActivity());
					hLinearLayout.setGravity(Gravity.CENTER);
				}
			}
		}
		if(hLinearLayout.getChildCount()>0){
			linearLayout.addView(hLinearLayout);
		}
	}
	class YouHuiMXClick implements OnClickListener{
		@Override
		public void onClick(final View v) {
			new VerPrivilege().verRight(getActivity(), v, new IResult<View>() {
				@Override
				public void result(View v) {
					isAuth(v);
				}
			});
			}
		/**
		 * 执行优惠
		 * @param v
		 */
		public void isAuth(View v){
			CList<Map<String,String>> list=new CList<Map<String,String>>();
			list.add("deviceId", SharedPreferencesUtils.getDeviceId(getActivity()));	//设备编号
			list.add("userCode", SharedPreferencesUtils.getUserCode(getActivity()));	//登录编号
			list.add("tableNum", getBundle().getString("tableNum"));	//台位编号
			list.add("orderId", getBundle().getString("orderId"));	//订单编号
			list.add("counpId", ((CouponMain)v.getTag()).getCode());	//优惠券编码
			list.add("counpCnt", "1");	//优惠券数量
			list.add("counpMoney", "0");	//优惠券金额 
			list.add("json", "{\"jmtyp\":\"\",\"ryzktyp\":\"\"}");	//优惠券金额
			getServer().connect(getActivity(), "userCounp", "ChoiceWebService/services/HHTSocket?/userCounp", list, new OnServerResponse() {
				@Override
				public void onResponse(String result) {
					getDialog().dismiss();
					if(ValueUtil.isNotEmpty(result)){
						String[] str=result.split("@");
						if(str[0].equals("0")){
//							账单号  优惠券编码 优惠劵名称 数量 优惠金额
							manager=getFragmentManager();
							transaction=manager.beginTransaction();
							BillFragment bill=new BillFragment();
							bill.setBundle(bundle);
							transaction.replace(R.id.setAccLeftLayout, bill);
							transaction.commit();
							if(str.length>5){
								ViewUtil.setVew(getActivity(),str[3],str[5],true,implR);
							}else{
								ViewUtil.setVew(getActivity(),str[3],"0",true,implR);
							}
						}else{
							Toast.makeText(getActivity(), str[1], Toast.LENGTH_LONG).show();
						}
					}else{
						Toast.makeText(getActivity(),R.string.net_error, Toast.LENGTH_LONG).show();
					}
				}
				IResult<String> implR= new IResult<String>() {//最后支付
					@Override
					public void result(String t) {
						new FinalPay().fPay(getBundle(), getActivity(), ValueUtil.isNaNofDouble(t));
					}
				};
				@Override
				public void onBeforeRequest() {
					getDialog().show();
				}
			});
			
		}
		
	}
}
