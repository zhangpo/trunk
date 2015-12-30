package cn.com.choicesoft.fragment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.Eatables;
import cn.com.choicesoft.adapter.BillAdapter;
import cn.com.choicesoft.bean.CouponStoreBean;
import cn.com.choicesoft.bean.Food;
import cn.com.choicesoft.bean.MoneyPay;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.util.AnalyticalXmlUtil;
import cn.com.choicesoft.util.CList;
import cn.com.choicesoft.util.FinalPay;
import cn.com.choicesoft.util.OnServerResponse;
import cn.com.choicesoft.util.Server;
import cn.com.choicesoft.util.SharedPreferencesUtils;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.util.ViewUtil;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

/**
 * 账单Fragment
 * @Author:M.c
 * @CreateDate:2014-1-6
 * @Email:JNWSCZH@163.COM
 */
public class BillFragment extends Fragment {
	private Bundle bundle;
	private LoadingDialog dialog;
	
//	男女数量
	public static String  man,woman;
	public LoadingDialog getDialog() {
		if(dialog==null){
			dialog = new LoadingDialogStyle(getActivity(), this.getString(R.string.please_wait));
			dialog.setCancelable(true);
		}
		return dialog;
	}
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	public Server getServer() {
		Server server=new Server();
		return server;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/*<xs:element name="deviceId" type="xs:string"
		<xs:element name="userCode" type="xs:string"
		<xs:element name="tableNum" type="xs:string"
		<xs:element name="manCounts" type="xs:string"
		<xs:element name="womanCounts" type="xs:string"
		<xs:element name="orderId" type="xs:string"
		<xs:element name="chkCode" type="xs:string"
		<xs:element name="comOrDetach" type="xs:string"*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		final View view=inflater.inflate(R.layout.bill_fragment, null);
		final ListView listView=(ListView)view.findViewById(R.id.BillListView);
		CList<Map<String,String>> data=new CList<Map<String,String>>();
		data.add("deviceId", SharedPreferencesUtils.getDeviceId(getActivity()));	//设备编号
		data.add("userCode", SharedPreferencesUtils.getUserCode(getActivity()));	//登录编号
		data.add("tableNum", getBundle().getString("tableNum"));		//台位编号
		data.add("manCounts", ValueUtil.isNotEmpty(getBundle().getString("manCounts"))?getBundle().getString("manCounts"):"");		//男人数
		data.add("womanCounts", ValueUtil.isNotEmpty(getBundle().getString("womanCounts"))?getBundle().getString("womanCounts"):"");	//女人数
		data.add("orderId", getBundle().getString("orderId"));	//账单号
		data.add("chkCode", "");		//查询单授权人
		data.add("comOrDetach", "1");	//相同菜品合并发送还是分开发送   0 分开发送  1 合并发送
		getServer().connect(getActivity(), "queryProduct", "ChoiceWebService/services/HHTSocket?/queryProduct", data, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				if(ValueUtil.isNotEmpty(result)){
					String[] str=result.split("@");
					if(!str[0].equals("0")){
						Toast.makeText(getActivity(), R.string.data_error, Toast.LENGTH_LONG).show();
						getActivity().finish();
						return;
					}
				}else{
					Toast.makeText(getActivity(), R.string.net_error, Toast.LENGTH_LONG).show();
					getActivity().finish();
					return;
				}
				Map<String,Object> map=AnalyticalXmlUtil.analysisProductW(getActivity(),result);
				if(ValueUtil.isEmpty(map)){
					Toast.makeText(getActivity(),R.string.data_error, Toast.LENGTH_LONG).show();
					getActivity().finish();
					return;
				}
				@SuppressWarnings("unchecked")
				Map<String,String> manMap=(Map<String, String>) map.get("manMap");//获取支付状态与人数
				TextView womanNum = null,manNum = null;
				woman = ValueUtil.isNaNofInteger(manMap.get("womanCounts"))+"";
				man = ValueUtil.isNaNofInteger(manMap.get("manCounts"))+"";
				if (ChioceActivity.ispad) {
					womanNum= (TextView)getActivity().findViewById(R.id.vip_womanText1);
					manNum = (TextView)getActivity().findViewById(R.id.vip_manText1);
					womanNum.setText(woman);//女士人数赋值
					manNum.setText(man);//先生人数赋值
				} 
				
				if(ValueUtil.isNotEmpty(TsData.moneyPay)){//如果现金记录不为空则循环显示现金消费记录
					for (Map.Entry<String, MoneyPay> pay : TsData.moneyPay.entrySet()) {
						MoneyPay money=pay.getValue();
						for (int i = 0; i < money.getPayNum(); i++) {
							ViewUtil.setVew(getActivity(), money.getPayName(), String.valueOf(money.getPayMoney()), false,null);
						}
					}
				}

				showText(view,map);//显示堂食价格
				BillAdapter adapter=new BillAdapter(getActivity(), (List<Map<String,String>>)map.get("orderList"));
				listView.setAdapter(adapter);
				if (((List<Map<String,String>>) map.get("orderList")).size()>0&&map.get("orderMoney")!=null&&Double.parseDouble((String)map.get("orderMoney"))==0){
					new FinalPay().fPay(getBundle(), getActivity(), 0.0);
				}
			}
			
			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
		return view;
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	
	/**
	 * 计算显示：堂食金额、合计金额。。
	 * @param view
	 * @param result 账单信息合计
	 */
	@SuppressWarnings("unchecked")
	public void showText(View view,Map<String,Object> result){
		BigDecimal tangS=BigDecimal.ZERO;	//堂食金额
		BigDecimal heJ=BigDecimal.ZERO;		//合计金额
		BigDecimal moL=BigDecimal.ZERO;		//抹零金额
		BigDecimal yingF=BigDecimal.ZERO;	//应付金额
		List<Map<String,String>> orderList=(List<Map<String, String>>) result.get("orderList");
		for (Map<String, String> map : orderList) {//遍历单据 计算合计金额
			String price=map.get("price");
			String fujiaprice=map.get("fujiaprice");
			String[] fj={};
			if(ValueUtil.isNotEmpty(fujiaprice)) {
				fj = fujiaprice.split(",");
			}
			if(ValueUtil.isNotEmpty(price)){
				if(map.get("pcode").equals(map.get("tpcode"))||ValueUtil.isEmpty(map.get("tpcode"))){
					tangS=tangS.add(ValueUtil.isNaNofBigDecimal(price));
					heJ=heJ.add(ValueUtil.isNaNofBigDecimal(price));
				}
			}
			if(fj.length>0){
				for(int i=0;i<fj.length;i++){
					tangS=tangS.add(ValueUtil.isNaNofBigDecimal(fj[i]));
					heJ=heJ.add(ValueUtil.isNaNofBigDecimal(fj[i]));
				}
			}
		}
		//TODO 抹零，保留小数
		if (result.get("orderMoney")==null) {
			int scale = ValueUtil.isNaNofInteger(SharedPreferencesUtils.getMoL(getActivity()));
			//yingF=heJ;//抹零前金额
			//if(scale==0){//判断是否需要抹零
			//moL=heJ-yingF;//计算抹零金额
			//}
			yingF = heJ.setScale(scale, BigDecimal.ROUND_DOWN);
			moL=heJ.subtract(yingF);
		}else {
			yingF=yingF.add(ValueUtil.isNaNofBigDecimal(result.get("orderMoney").toString()));
			moL=heJ.subtract(yingF);
		}

		((TextView)view.findViewById(R.id.tangSJE_Text2)).setText(tangS.setScale(2).toString());//堂食金额
		((TextView)view.findViewById(R.id.heJJE_Text2)).setText(heJ.setScale(2).toString());//合计金额
		((TextView)view.findViewById(R.id.moLJE_Text2)).setText(moL.setScale(2).toString());//抹零金额
		((TextView)view.findViewById(R.id.yingFJE_Text2)).setText(yingF.setScale(2).toString());//应付金额
		//-------------------------------------------------------------------------------------------------------------上：金额计算——下：优惠信息显示
		List<Map<String,String>> couponList=(List<Map<String, String>>) result.get("couponList");//优惠信息集合
		if(ValueUtil.isNotEmpty(couponList)&&couponList.size()>0){
			TsData.coupPay=new HashMap<String,CouponStoreBean>();
			for (Map<String, String> map : couponList) {
				//---------------------------------------------向暂存类里添加数据TsData
				CouponStoreBean bean=new CouponStoreBean();
				bean.setCouponId("");
				bean.setCouponMoney(map.get("cMoney"));
				if (result.get("orderMoney")!=null) {
					moL=moL.subtract(ValueUtil.isNaNofBigDecimal(map.get("cMoney")));
					((TextView)view.findViewById(R.id.moLJE_Text2)).setText(moL.setScale(2).toString());//抹零金额
				}
				bean.setCouponName(map.get("cName"));
				TsData.coupPay.put(map.get("cName"), bean);
				//---------------------------------------------分割线
				ViewUtil.setVew(getActivity(), map.get("cName"), String.valueOf(map.get("cMoney")), true,null);
			}
		}

	}
}
