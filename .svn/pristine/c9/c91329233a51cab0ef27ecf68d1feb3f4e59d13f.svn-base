package cn.com.choicesoft.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.util.VipRecordUtil;

/**
 * 会员开台点菜
 * @CreateDate:2014-4-20
 * @Email:JNWSCZH@163.COM
 * @author:M.c
 */
public class StartTabVipFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.start_tai_fragment, null);
		List<VipRecord> vipList=null;
		//try防止查询数据报错
		try {
			vipList=new VipRecordUtil().queryHandle(getActivity(), getActivity().getIntent().getStringExtra("tableNum"));
		} catch (Exception e) {
			Log.e("VipFragment-获取会员信息", e.getMessage());
		}
		if(vipList==null||vipList.size()<=0){
			Toast.makeText(getActivity(),R.string.vip_msg_error, Toast.LENGTH_LONG).show();
			getActivity().finish();
		}
		EditText VIPNumEdut= (EditText)view.findViewById(R.id.start_vip_VIPNumEdut);//会员卡号
		EditText chuXuKEdit= (EditText)view.findViewById(R.id.start_vip_chuXuKEdit);//储值余额
		EditText phoneEdit= (EditText)view.findViewById(R.id.start_vip_PhoneNumEdit);//券余额 券 
		EditText jiFenYEEdit= (EditText)view.findViewById(R.id.start_vip_jiFenYEEdit);//积分余额
		ListView couponListView= (ListView)view.findViewById(R.id.start_Vip_couponListView);//积分余额
		VIPNumEdut.setText(vipList.get(0).getCardNumber());
		chuXuKEdit.setText(String.valueOf(vipList.get(0).getStoredCardsBalance()));
		phoneEdit.setText(String.valueOf(vipList.get(0).getPhone()));
		jiFenYEEdit.setText(String.valueOf(vipList.get(0).getIntegralOverall()));
		String ticketInfoList=vipList.get(0).getTicketInfoList();
		if(ValueUtil.isNotEmpty(ticketInfoList)){//遍历券信息，显示在ListView里
			List<Map<String,String>> list=new ArrayList<Map<String,String>>();
			String[] ticketList=ticketInfoList.split(";");
			for (int i = 0; i < ticketList.length; i++) {
				String[] ticketInfo=ticketList[i].split(",");
				if(ValueUtil.isNotEmpty(ticketInfo)&&ticketInfo.length==4){
					Map<String,String> map=new HashMap<String,String>();
					map.put("ticketId", ticketInfo[0]);//优惠券ID
					map.put("ticketMoney", ValueUtil.isNaNDoubleDiv(ticketInfo[1])+"");//优惠券金额
					map.put("ticketName", ticketInfo[2]);//优惠券名称
					map.put("ticketAmount", ticketInfo[3]+getString(R.string.zhang));//优惠券数量
					list.add(map);
				}
			}
			SimpleAdapter adapter=new SimpleAdapter(getActivity(), list, R.layout.vip_coupon_list_item, new String[]{"ticketId","ticketName","ticketMoney","ticketAmount"}, new int[]{R.id.vip_ticketId,R.id.vip_ticketName,R.id.vip_ticketMoney,R.id.vip_ticketAmount});
			couponListView.setAdapter(adapter);
		}
		super.onResume();
		return view;
	}
	@Override
	public void onPause() {
		super.onPause();
	}
}
