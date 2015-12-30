package cn.com.choicesoft.constants;

import android.content.Context;
import android.os.Environment;
import cn.com.choicesoft.util.SharedPreferencesUtils;

/**
 * ������
 *
 */
public class Constants {
    public static String RES_PATH_SDCARD_DIR= Environment.getExternalStorageDirectory()+"/ChoiceSoft/";// ��Դ�ļ���sdcard�е�·��
    public static String RES_PATH_MEMORY_DIR="/data" + Environment.getDataDirectory().getAbsolutePath() + "/ChoiceSoft/";//��Դ���ڴ��е�·��(��־�ļ��ڰ�װĿ¼�е�·��)="/sdcard/data/cn.com.choicesoft.phone/log";// ��־�ļ���sdcard�е�·��

    public static String getURL(Context context){
		return SharedPreferencesUtils.getServerAddress(context);
	}

	public static String getHTTP_URL(Context context){
		return SharedPreferencesUtils.getWaitUrl(context);
	}
	
	public static final String NAMESPACE = "http://service.web.choice.com";// �����������ռ�
	
	
	public static class FastWebService{
		
		public static final String specialRemark_WSDL = "ChoiceWebService/services/HHTSocket?/specialRemark";//�ر�ע (ȫ��������)
		
		public static final String soldOut_WSDL = "ChoiceWebService/services/HHTSocket?/soldOut";//����
		
		public static final String notTerminateOrder_WSDL = "ChoiceWebService/services/HHTSocket?/getOrdersBytabNum";//��ȡָ��̨λ��δ�����˵�
		
		public static final String prePrint_WSDL = "ChoiceWebService/services/HHTSocket?/priPrintOrder";//Ԥ��ӡ
		public static final String kitchenPrint_WSDL = "ChoiceWebService/services/HHTSocket?/kitchenPrint";//Ԥ��ӡ
		
		public static final String promptDish_WSDL = "ChoiceWebService/services/HHTSocket?/callPubitem";//�߲�
		
		public static final String getOrdersBytabNum_WSDL = "ChoiceWebService/services/HHTSocket?/getOrdersBytabNum";//��ȡָ��̨λ��δ�����˵�
		
		public static final String Senddish_WSDL = "ChoiceWebService/services/HHTSocket?/sendc";//���Ͳ�Ʒ
		
		public static final String CHANGETtablestate_WSDL = "ChoiceWebService/services/HHTSocket?/changTableState";//�ı�̨λ״̬
		
		public static final String INITDISHCLASS_WSDL = "ChoiceWebService/services/HHTSocket?/getFoodKind";//��Ʒ���
		
		public static final String INITDISH_WSDL = "ChoiceWebService/services/HHTSocket?/getProducts";//�����Ʒ
		
		public static final String INITTABLE_WSDL = "ChoiceWebService/services/HHTSocket?/listTables";//��ȡ̨λ������
		
		public static final String REGISTER_WSDL = "ChoiceWebService/services/HHTSocket?/registerDeviceId";//ע��
		
		public static final String LOGIN_WSDL = "ChoiceWebService/services/HHTSocket?/login";//��¼
		
		public static final String CHECKAUTH_WSDL = "ChoiceWebService/services/HHTSocket?/checkAuth";//��֤��Ȩ
		
		public static final String STARTTABLE_WSDL = "ChoiceWebService/services/HHTSocket?/startc";//��̨
		
		public static final String LOGOUT_WSDL = "ChoiceWebService/services/HHTSocket?/loginOut";//ע��

		public static final String QUERY_WHOLE_PRODUCTS_WSDL = "ChoiceWebService/services/HHTSocket?/queryWholeProducts";//ȫ����Ʒ��ѯ�ӿ� ��ȫ���и����

		public static final String Activate_WSDL = "http://61.174.28.122:9100/choicereg.asmx/choicereg";//��¼���漤��
	}
	
	public static class FastMethodName{
		
		public static final String activate_METHODNAME = "choicereg";//��½���漤�����
		
		public static final String specialRemark_METHODNAME = "specialRemark";//�ر�ע (ȫ��������)������
		
		public static final String soldOut_METHODNAME = "soldOut";//���巽����
		
		public static final String notTerminateOrder_METHODNAME = "getOrdersBytabNum";//��ȡָ��̨λ��δ�����˵�������
		
		public static final String prePrint_METHODNAME = "priPrintOrder";//Ԥ��ӡ������
		
		public static final String PromptDish_METHODNAME = "callPubitem";//�߲˷�����
		
		public static final String GetOrdersBytabNum_METHODNAME = "getOrdersBytabNum";//��ȡָ��̨λ��δ�����˵�������
		
		public static final String Senddish_METHODNAME = "sendc";//���Ͳ�Ʒ������
		
		public static final String ChangeTableState_METHODNAME = "changTableState";//�ı�̨λ״̬������
		
		public static final String ACHIEVEDISH_METHODNAME = "getProducts";//��ȡ��Ʒ������
		
		public static final String ACHIEVEDISHCLASS_METHODNAME = "getFoodKind";//��ȡ��Ʒ��𷽷���
		
		public static final String ACHIEVETABLE_METHODNAME = "listTables";//��ȡ̨λ������ķ�����

		public static final String REGISTERDEVICEID_METHODNAME = "registerDeviceId";//��ע�᷽����
		
		public static final String LOGIN_METHODNAME = "login";//��¼������
		public static final String GETPERMISSIONTOPAD_METHODNAME = "getPermissionToPad";//��¼������
		
		public static final String CHECKAUTH_METHODNAME = "checkAuth";//��֤��Ȩ������
		
		public static final String STARTTABLE_METHODNAME = "startc";//��̨������

		public static final String GETORDER_BY_AUTHCODE="getOrderByAuthCode";
		
		public static final String LOGOUT_METHODNAME = "loginOut";//�ǳ�������

		public static final String KITCHENPRINT="kitchenPrint";

		public static final String QUERY_WHOLE_PRODUCTS = "queryWholeProducts";//ȫ����Ʒ��ѯ�ӿ� ��ȫ���и����
	}


	/**
	 * ��λ�ӿ�
	 */
	public static class WaitHttp {
		/**
		 * ��ȡ�к���Ϣ
		 */
		public static final String QUERY_NO="waitSeat/queryNO.do";
		/**
		 * ȡ��
		 */
		public static final String TAKE_NO="waitSeat/printNO.do";
		/**
		 * �Ͳͣ�����
		 */
		public static final String CANCEL_SEAT="waitSeat/cancelSeat.do";
	}
	
	
}