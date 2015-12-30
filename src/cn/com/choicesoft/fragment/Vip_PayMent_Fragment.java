package cn.com.choicesoft.fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.DisplayMetrics;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.CouponStoreBean;
import cn.com.choicesoft.bean.VipRecord;
import cn.com.choicesoft.constants.IResult;
import cn.com.choicesoft.constants.TsData;
import cn.com.choicesoft.util.CList;
import cn.com.choicesoft.util.FinalPay;
import cn.com.choicesoft.util.OnServerResponse;
import cn.com.choicesoft.util.Server;
import cn.com.choicesoft.util.SharedPreferencesUtils;
import cn.com.choicesoft.util.ValueUtil;
import cn.com.choicesoft.util.VerPrivilege;
import cn.com.choicesoft.util.VipRecordUtil;
import cn.com.choicesoft.view.LoadingDialog;
import android.widget.RelativeLayout;
import cn.com.choicesoft.view.LoadingDialogStyle;

/**
 * ��Ա�����֧��ҳ��
 * @Author:M.c
 * @CreateDate:2014-1-13
 * @Email:JNWSCZH@163.COM
 */
public class Vip_PayMent_Fragment extends Fragment implements View.OnClickListener{
	private Bundle bundle;
	private LoadingDialog dialog = null;
	private VipRecord vip;//��Աʵ��

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
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	public VipRecord getVip() {
		return vip;
	}
	public void setVip(VipRecord vip) {
		this.vip = vip;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.vip_payment_fragment, null);
		setVip(new VipRecordUtil().queryHandle(getActivity(),getBundle().getString("tableNum")).get(0));//��YiXuanDishActivity2����

		TextView yingF=(TextView)view.findViewById(R.id.vip_payMent_yingFText1);//Ӧ�����
		final EditText chuZhiKYYEEdit=(EditText)view.findViewById(R.id.vip_payMent_chuZhiKYYEEdit);//��ֵ�������
		final EditText jiFenKYYEEdit=(EditText)view.findViewById(R.id.vip_payment_jiFenKYYEEdit);//���ֿ������
//		getActivity().findViewById(R.id.vip_taiweiText).setTag(getVip().getCardNumber());//��Ա����
		String payable=getVip().getPayable()>0?getVip().getPayable().toString():getBundle().getString("Payable");
		yingF.setText(payable);//Ӧ�����
		yingF.setTag(payable);//Ӧ�����
		EditText yeEXFEdit=(EditText)view.findViewById(R.id.vip_payment_yeEXFEdit);
		EditText jiFenXFEdit=(EditText)view.findViewById(R.id.vip_payment_jiFenXFEdit);
		chuZhiKYYEEdit.setText(getVip().getStoredCardsBalance().toString());//��ֵ������� Text
		chuZhiKYYEEdit.setTag(getVip().getStoredCardsBalance());//��ֵ�������  Tag
		jiFenKYYEEdit.setText(getVip().getIntegralOverall().toString());//���ֿ������ TEXT
		jiFenKYYEEdit.setTag(getVip().getIntegralOverall()); //���ֿ������ Tag
		//��̬������ֵ�������
		setPricePoint(chuZhiKYYEEdit);
		setPricePoint(jiFenKYYEEdit);
		setPricePoint(yeEXFEdit);
		setPricePoint(jiFenXFEdit);
		yeEXFEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			/**
			 * ��ֵ��������洢ֵ���Ѹı�
			 */
			@Override
			public void afterTextChanged(Editable s) {
				String ssje=((TextView)getActivity().findViewById(R.id.vip_payMent_yingFText1)).getText().toString();
				EditText edit=(EditText)getActivity().findViewById(R.id.vip_payMent_chuZhiKYYEEdit);
				String czky=edit.getText().toString();
				double yingSJE=0;//Ӧ�ս��
				double chuZhiKY=0;//��ֵ����
				double chuZXF=0;//��ֵ����
				double chuZKYTag=0;//��ֵ����Tag
				if(ValueUtil.isNaN(ssje)){
					yingSJE=Double.valueOf(ssje);
				}
				if(ValueUtil.isNaN(czky)){
					chuZhiKY=Double.valueOf(czky);
				}
				if(ValueUtil.isNaN(s.toString())){
					chuZXF=Double.valueOf(s.toString());
				}
				if(ValueUtil.isNaN(chuZhiKYYEEdit.getTag().toString())){
					chuZKYTag=Double.valueOf(chuZhiKYYEEdit.getTag().toString());
				}
				if(chuZhiKY<chuZXF||yingSJE<chuZXF){//�жϿ����Ƿ��������
					s.delete(s.length()>0?s.length()-1:0,s.length());
					Toast.makeText(getActivity(),R.string.pay_money_error3, Toast.LENGTH_LONG).show();
				}else{
					edit.setText(chuZKYTag-chuZXF+"");
				}
			}
		});
		//��̬�������ֿ������
		jiFenXFEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			/**
			 * ���ֿ��������������Ѹı�
			 */
			@Override
			public void afterTextChanged(Editable s) {
				String ssje=((TextView)getActivity().findViewById(R.id.vip_payMent_yingFText1)).getText().toString();
				EditText edit=(EditText)getActivity().findViewById(R.id.vip_payment_jiFenKYYEEdit);
				String jfky=edit.getText().toString();
				double yingSJE=0;//Ӧ�ս��
				double jiFenKY=0;//��ֵ����
				double jiFXF=0;
				double jiFKYTag=0;
				if(ValueUtil.isNaN(ssje)){
					yingSJE=Double.valueOf(ssje);
				}
				if(ValueUtil.isNaN(jfky)){
					jiFenKY=Double.valueOf(jfky);
				}
				if(ValueUtil.isNaN(s.toString())){
					jiFXF=Double.valueOf(s.toString());
				}
				if(ValueUtil.isNaN(jiFenKYYEEdit.getTag().toString())){
					jiFKYTag=Double.valueOf(jiFenKYYEEdit.getTag().toString());
				}
				if(jiFenKY<jiFXF||yingSJE<jiFXF){
					s.delete(s.length()>0?s.length()-1:0,s.length());
					Toast.makeText(getActivity(), R.string.pay_money_error4, Toast.LENGTH_LONG).show();
				}else{
					edit.setText(jiFKYTag-jiFXF+"");
				}
			}
		});
		String ticketInfoList=getVip().getTicketInfoList();//�Ż�ȯ����
		ScrollView scrollView=(ScrollView)view.findViewById(R.id.vip_payMent_scrollView);

		if(ValueUtil.isNotEmpty(ticketInfoList)){//����ȯ��Ϣ����ʾ��ListView��
			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width= dm.widthPixels;
			int hight= 150;
			RelativeLayout layout = (RelativeLayout)scrollView.findViewById(R.id.vip_payMent_couponButtonLy);
			String[] ticketList=ticketInfoList.split(";");
			//���ﴴ��16����ť��ÿ�з���4����ť

			Button Btn[] = new Button[ticketList.length];
			int j = -1;
			for  (int i=0; i<ticketList.length; i++) {
				String[] ticketInfo=ticketList[i].split(",");
				Btn[i]=new Button(getActivity());
				Btn[i].setText(ticketInfo[2]);
				Btn[i].setTag(ticketInfo);
				Btn[i].setBackgroundColor(Color.RED);
				Btn[i].setTextSize(13);
				Btn[i].setTextColor(Color.WHITE);
				Btn[i].setOnClickListener(this);
				RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams ((width-50)/4,hight);  //��
				//�ð�ť�Ŀ�Ⱥ͸߶�
				if (i%4 == 0) {
					j++;
				}
				btParams.leftMargin = 10+ ((width-50)/4+10)*(i%4);   //�����궨λ
				btParams.topMargin = (hight+5)*j;   //�����궨λ
				layout.addView(Btn[i],btParams);   //����ť����layout���
			}
		}else {
			scrollView.setVisibility(View.GONE);
		}
		return view;
	}
	/**
	 * �������2λС��
	 * @param editText
	 */
	public static void setPricePoint(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					editText.setText(s);
					editText.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText(s.subSequence(0, 1));
						editText.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

	}
	//�Ż�ȯ��ťʹ��
	@Override
	public void onClick(View v) {
		new VerPrivilege().verRight(getActivity(), v, new IResult<View>() {
			@Override
			public void result(View v) {
				isAuth(v);
			}
		});
	}
	/*String  userCounp��String deviceId,String userCode,String tableNum,String orderId��String counpId,String counpCnt,String counpMoney)   
	  deviceId�� �豸��� 
	  userCode����¼��� 
	  tableNum��̨λ��� 
	  orderId�� �Ż�ȯ����   
	  counpCnt���Ż�ȯ���� 
	  counpMoney���Ż�ȯ���  */
	/**
	 * ִ���Ż�ȯ
	 * @param paramView
	 */
	public void isAuth(View paramView){
		Bundle ble=getBundle().getBundle("BillBundle");
		CList<Map<String,String>> list=new CList<Map<String,String>>();
		list.add("deviceId", SharedPreferencesUtils.getDeviceId(getActivity()));	//�豸���
		list.add("userCode", SharedPreferencesUtils.getUserCode(getActivity()));	//��¼���
		list.add("tableNum", ble.getString("tableNum"));	//̨λ���
		list.add("orderId", ble.getString("orderId"));	//�˵����
		list.add("counpId", ((String[])paramView.getTag())[0]);	//�Ż�ȯ���� 
		list.add("counpCnt", "1");	//�Ż�ȯ����
		list.add("counpMoney", "0");	//�Ż�ȯ���
		getServer().connect(getActivity(), "userCounp", "ChoiceWebService/services/HHTSocket?/userCounp", list, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				if(ValueUtil.isNotEmpty(result)){
					getDialog().dismiss();
					String[] str=result.split("@");
					if(str[0].equals("0")){
//						�˵���  �Ż�ȯ���� �Ż݄����� ���� �Żݽ�� 
						if(ValueUtil.isNotEmpty(TsData.coupPay)){//�жϸò˵��Ƿ�����Ż���Ϣ
							CouponStoreBean bean=TsData.coupPay.get(str[3]);
							if(ValueUtil.isEmpty(bean)){
								bean=new CouponStoreBean();
								bean.setCouponAuth("ok");
								bean.setCouponId(str[1]);
								bean.setCouponNum(bean.getCouponNum()+1);
								bean.setCouponName(str[3]);
								if(str.length>5){
									bean.setCouponMoney(str[5]);
									modYinF(str[5]);
								}else{
									bean.setCouponMoney("0");
									modYinF("0");
								}
								TsData.coupPay.put(str[3], bean);
							}
						}else{//���Ϊ����ʵ�����ݴ����� ���б���
							TsData.coupPay=new HashMap<String,CouponStoreBean>();
							CouponStoreBean bean=new CouponStoreBean();
							bean.setCouponAuth("ok");
							bean.setCouponId(str[1]);
							bean.setCouponNum(1);
							bean.setCouponName(str[3]);
							if(str.length>5){
								bean.setCouponMoney(ValueUtil.isNaNDoubleDiv(str[5]).toString());
								modYinF(ValueUtil.isNaNDoubleDiv(str[5]).toString());
							}else{
								bean.setCouponMoney("0");
								modYinF("0");
							}
							TsData.coupPay.put(str[3], bean);
						}//����Ż���Ϣ��Tag ��
					}else{
						Toast.makeText(getActivity(), str[1], Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getActivity(), R.string.net_error, Toast.LENGTH_LONG).show();
				}
			}
			
			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
	}
	/**
	 * �޸Ļ�Ա֧��ҳ�� Ӧ�ս��
	 * @param val
	 */
	public void modYinF(String val){
		double youh=ValueUtil.isNaNofDouble(val);
		TextView textView=(TextView)getActivity().findViewById(R.id.vip_yingFText1);
		double yingf=ValueUtil.isNaNofDouble(textView.getText().toString());
		if(yingf-youh<=0){
			new FinalPay().fPay(getBundle(), getActivity(), 0.0);
		}else{
			textView.setText(yingf-youh+"");
		}
	}
	@Override
	public void onPause() {
		super.onPause();
	}
}
