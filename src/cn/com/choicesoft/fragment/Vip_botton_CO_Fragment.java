package cn.com.choicesoft.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.SettleAccountsActivity;

/**
 * ��Ա֧��ҳ�� ���� ��ȡ���� ��֧������ťFragment
 * @Author:M.c
 * @CreateDate:2014-1-13
 * @Email:JNWSCZH@163.COM
 */
public class Vip_botton_CO_Fragment extends Fragment implements View.OnClickListener{
	private Bundle bundle;
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.vip_botton_co_fragment, null);
		Button cancel=(Button)view.findViewById(R.id.vip_cancelBut);
		Button ok=(Button)view.findViewById(R.id.vip_payBut);
		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);
		return view;
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.vip_cancelBut://ȡ��but
			 Intent intent=new Intent(getActivity(),SettleAccountsActivity.class);
			 intent.putExtras(getBundle().getBundle("BillBundle"));
			 startActivity(intent);
			 getActivity().finish();
			 break;
		case R.id.vip_payBut://֧��but
			FragmentManager manager=getActivity().getFragmentManager();
			FragmentTransaction transaction=manager.beginTransaction();
			Vip_PayMent_Fragment ment_Fragment=new Vip_PayMent_Fragment();//��Ա�����֧��ҳ��
			Vip_botton_pay_fragment pay_but=new Vip_botton_pay_fragment();//֧��button����
			pay_but.setBundle(getBundle());
			ment_Fragment.setBundle(getBundle());
			transaction.replace(R.id.pay_Ment_TopLayout, ment_Fragment,"VipFragment");
			transaction.replace(R.id.pay_Ment_bottonLayout, pay_but,"vip_botton_CO_Fragment");
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		}
	}
}
