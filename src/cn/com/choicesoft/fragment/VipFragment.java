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
 * ��Ա��ȷ��Fragment
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
		//try��ֹ��ѯ���ݱ���
		try {
			vipList=new VipRecordUtil().queryHandle(getActivity(), getActivity().getIntent().getStringExtra("tableNum"));
		} catch (Exception e) {
			Log.e("VipFragment-��ȡ��Ա��Ϣ", e.getMessage());
		}
//		Log.i("��Ա����Ϣ",vipList);
		if(vipList==null||vipList.size()<=0){
			Toast.makeText(getActivity(), R.string.vip_msg_error, Toast.LENGTH_LONG).show();
			getActivity().finish();
			super.onResume();
			return;
		}
		TextView yingF=(TextView)view.findViewById(R.id.vip_yingFText1);
		EditText VIPNumEdut= (EditText)view.findViewById(R.id.vip_VIPNumEdut);//��Ա����
		EditText chuXuKEdit= (EditText)view.findViewById(R.id.vip_chuXuKEdit);//��ֵ���
		EditText quanYEEdit= (EditText)view.findViewById(R.id.vip_quanYEEdit);//ȯ��� ȯ 
		EditText quanKeYYEEdit= (EditText)view.findViewById(R.id.vip_quanKeYYEEdit);//�������
		EditText jiFenYEEdit= (EditText)view.findViewById(R.id.vip_jiFenYEEdit);//�������
		ListView couponListView= (ListView)view.findViewById(R.id.Vip_couponListView);//�������
		//�ж����ݿ���û��Ӧ�ս������ȡ���ݿ⣬���û��ȡ��������ֵ
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
		CSLog.i("ȯ", ticketInfoList);
		if(ValueUtil.isNotEmpty(ticketInfoList)){//����ȯ��Ϣ����ʾ��ListView��
			List<Map<String,String>> list=new ArrayList<Map<String,String>>();
			String[] ticketList=ticketInfoList.split(";");
			for (int i = 0; i < ticketList.length; i++) {
				String[] ticketInfo=ticketList[i].split(",");
				if(ValueUtil.isNotEmpty(ticketInfo)&&ticketInfo.length==4){
					Map<String,String> map=new HashMap<String,String>();
					map.put("ticketId", ticketInfo[0]);//�Ż�ȯID
					map.put("ticketMoney", ValueUtil.isNaNDoubleDiv(ticketInfo[1])+"");//�Ż�ȯ���
					map.put("ticketName", ticketInfo[2]);//�Ż�ȯ����
					map.put("ticketAmount", ticketInfo[3]);//�Ż�ȯ����
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
