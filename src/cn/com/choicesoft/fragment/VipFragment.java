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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.util.CSLog;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.util.VipRecordUtil;

/**
 * 会员卡确认Fragment
 * @Author:M.c
 * @CreateDate:2014-1-11
 * @Email:JNWSCZH@163.COM
 */
public class VipFragment extends Fragment {
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.vip_fragment, null);
		return view;
	}

	@Override
	public void onResume() {
		List<VipRecord> vipList=null;
		//try防止查询数据报错
		try {
			vipList=new VipRecordUtil().queryHandle(getActivity(), getActivity().getIntent().getStringExtra("tableNum"));
		} catch (Exception e) {
			Log.e("VipFragment-获取会员信息", e.getMessage());
		}
//		Log.i("会员卡信息",vipList);
		if(vipList==null||vipList.size()<=0){
			Toast.makeText(getActivity(), R.string.vip_msg_error, Toast.LENGTH_LONG).show();
			getActivity().finish();
			super.onResume();
			return;
		}
		TextView yingF=(TextView)view.findViewById(R.id.vip_yingFText1);
		EditText VIPNumEdut= (EditText)view.findViewById(R.id.vip_VIPNumEdut);//会员卡号
		EditText chuXuKEdit= (EditText)view.findViewById(R.id.vip_chuXuKEdit);//储值余额
		EditText quanYEEdit= (EditText)view.findViewById(R.id.vip_quanYEEdit);//券余额 券 
		EditText quanKeYYEEdit= (EditText)view.findViewById(R.id.vip_quanKeYYEEdit);//可用余额
		EditText jiFenYEEdit= (EditText)view.findViewById(R.id.vip_jiFenYEEdit);//积分余额
		ListView couponListView= (ListView)view.findViewById(R.id.Vip_couponListView);//积分余额
		//判断数据库有没有应收金额，如果有取数据库，如果没有取传过来的值
		String payable=vipList.get(0).getPayable()>0?vipList.get(0).getPayable().toString():getActivity().getIntent().getStringExtra("Payable");
		yingF.setText(payable);
		yingF.setTag(payable);
		if(ValueUtil.isNaNDoubleDiv(payable)<=0){
			view.findViewById(R.id.vip_layout2).setVisibility(LinearLayout.INVISIBLE);
		}

		VIPNumEdut.setText(vipList.get(0).getCardNumber());
		chuXuKEdit.setText(String.valueOf(vipList.get(0).getStoredCardsBalance()));
		quanYEEdit.setText(String.valueOf(vipList.get(0).getCouponsOverall()));
		quanKeYYEEdit.setText(String.valueOf(vipList.get(0).getCouponsAvail()));
		jiFenYEEdit.setText(String.valueOf(vipList.get(0).getIntegralOverall()));
		String ticketInfoList=vipList.get(0).getTicketInfoList();
		CSLog.i("券", ticketInfoList);
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
					map.put("ticketAmount", ticketInfo[3]);//优惠券数量
					list.add(map);
				}
			}
			SimpleAdapter adapter=new SimpleAdapter(getActivity(), list, R.layout.vip_coupon_list_item, new String[]{"ticketId","ticketName","ticketMoney","ticketAmount"}, new int[]{R.id.vip_ticketId,R.id.vip_ticketName,R.id.vip_ticketMoney,R.id.vip_ticketAmount});
			couponListView.setAdapter(adapter);
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
