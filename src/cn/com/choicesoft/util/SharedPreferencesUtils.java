package cn.com.choicesoft.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences��һ��������
 * 
 */
public class SharedPreferencesUtils {
	/**
	 * �������ֻ�������ļ���
	 */
	private static final String FILE_NAME = "share_date";
	private static final String USER_NAME = "user_code";
	private static final String OR_NET = "or_net";
    private static final String OR_VIP = "or_vip";
    private static final String LANGUAGE = "language";
    private static final String SERVER_ADDRESS = "server_address";//webservice���õ�IP��ַ
	private static final String IS_TEMPORARYSAVE = "is_temporary_save";
	private static final String PHYSICAL_ID = "physicalId";//�ò��ֳֻ���Ψһ��ʾ
	private static final String DEVICE_ID = "deviceId";//�豸��
	private static final String FTP_IP = "ftpIp";//FTP���õ�IP��ַ
	private static final String FTP_USERNAME = "ftpUsername";//FTP���õ��û���
	private static final String FTP_PWD = "ftpPwd";//FTP���õ�����
	private static final String POS_VER = "posVer";
	private static final String HHT_VER = "hhtVer";
	private static final String OPERATOR = "operator";
	private static final String ROLECODE = "rolecode";

	private static final String TABLE_CLASSIFY="table_classify";
	private static final String MOL = "moLing";
	private static final String PADINFOR = "PadInformation";
	private static final String GIFT_AUTH = "giftAuth";//������Ȩ�Ľ��
	private static final String SHOP_ENCODE = "shopEncode";//���̱��
	private static final String IS_DISGUISH_GENDER = "is_disguishgender";//�Ƿ�������Ů
	private static final String LEFTIMAGE_PATH = "leftimage_path";//�˵���ѯҳ�����ͼƬ
	private static final String IS_REGISTERED = "isRegister";//�ж��豸�Ƿ��Ѿ�ע���õ�
	private static final String CHINESE_TO_SNACK = "ChineseToSnack";//�ж��豸�Ƿ��Ѿ�ע���õ�
	private static final String LOGOUT_TIME = "Logout_Time";//�ж��豸�Ƿ��Ѿ�ע���õ�
	private static final String CLOSE_TIME = "Close_Time";//�ж��豸�Ƿ��Ѿ�ע���õ�
	private static final String WAIT_URL = "wait_url";//��λ��Ϣ
	private static final String JNOW_AUTH_USERNAME = "9now_username";//�Խ���ζ���õȣ�ĳһ�������ŵ����Ȩ�˺�
	private static final String JNOW_AUTH_PWD = "9now_pwd";//�Խ���ζ���õȣ�ĳһ�������ŵ����Ȩ����
	private static final String BULETOOLTH_ADDRESS="";

	/**
	 *@Author:Ysc
	 *@Comments:�洢ĳһ�������ŵ����Ȩ�˺�
	 *@CreateDate:2014-9-11
	 */
	public static void setJNOWUserName(Context pContext,String jnowAuthUsername){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setSharePfs.edit();
		editor.putString(JNOW_AUTH_USERNAME, jnowAuthUsername);
		editor.commit();
	}

	/**
	 *@Author:Ysc
	 *@Comments:�õ�ĳһ�������ŵ����Ȩ�˺�
	 *@CreateDate:2014-9-11
	 */
	public static String getJNOWUserName(Context pContext){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setSharePfs.getString(JNOW_AUTH_USERNAME, null);
	}
	/**
	 *@Author:Ysc
	 *@Comments:�洢ĳһ�������ŵ����Ȩ�˺�
	 *@CreateDate:2014-9-11
	 */
	public static void setBuleToolthaddress(Context pContext,String buleToolthaddress){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setSharePfs.edit();
		editor.putString(BULETOOLTH_ADDRESS, buleToolthaddress);
		editor.commit();
	}

	/**
	 *@Author:Ysc
	 *@Comments:�õ�ĳһ�������ŵ����Ȩ�˺�
	 *@CreateDate:2014-9-11
	 */
	public static String getBuleToolthaddress(Context pContext){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setSharePfs.getString(BULETOOLTH_ADDRESS, null);
	}

	/**
	 *@Author:Ysc
	 *@Comments:�洢ĳһ�������ŵ����Ȩ����
	 *@CreateDate:2014-9-11
	 */
	public static void setJNOWPwd(Context pContext,String jnowAuthPwd){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setSharePfs.edit();
		editor.putString(JNOW_AUTH_PWD, jnowAuthPwd);
		editor.commit();
	}

	/**
	 *@Author:Ysc
	 *@Comments:�õ�ĳһ�������ŵ����Ȩ����
	 *@CreateDate:2014-9-11
	 */
	public static String getJNOWPwd(Context pContext){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setSharePfs.getString(JNOW_AUTH_PWD, null);
	}



	/**
	 *@Author:Ysc
	 *@Comments:�жϸ�pad�Ƿ��Ѿ�ע���
	 *@CreateDate:2014-5-12
	 */
	public static void setIsRegister(Context pContext,boolean isRegistered){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setSharePfs.edit();
		editor.putBoolean(IS_REGISTERED, isRegistered);
		editor.commit();
	}
	
	
	/**
	 *@Author:Ysc
	 *@Comments:�жϸ�pad�Ƿ��Ѿ�ע���
	 *@CreateDate:2014-5-12
	 */
	public static boolean getIsRegister(Context pContext){
		SharedPreferences getSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return getSharePfs.getBoolean(IS_REGISTERED, false);
	}

    /**
     * �����п���л�
     * @param pContext
     * @param choice
     */
	public static void setChineseSnack(Context pContext,int choice){
		SharedPreferences setShopEncode = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setShopEncode.edit();
		editor.putInt(CHINESE_TO_SNACK, choice);
		editor.commit();
	}
    /**
     * ��ȡ�п���л�
     * 0Ϊ��� 1Ϊ�в�
     * @param pContext
     */
	public static int getChineseSnack(Context pContext){
        SharedPreferences getSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return getSharePfs.getInt(CHINESE_TO_SNACK, 0);
	}

    public static void setDisguishGender(Context pContext,boolean isDisguish){
		SharedPreferences setShopEncode = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setShopEncode.edit();
		editor.putBoolean(IS_DISGUISH_GENDER, isDisguish);
		editor.commit();
	}
	
	public static boolean getDisguishGender(Context pContext){
		SharedPreferences getShopEncodePrefs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return getShopEncodePrefs.getBoolean(IS_DISGUISH_GENDER, true);
	}
	
	
	public static void setShopEncode(Context pContext,String shopEncode){
		SharedPreferences setShopEncode = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setShopEncode.edit();
		editor.putString(SHOP_ENCODE, shopEncode);
		editor.commit();
	}
	
	public static String getShopEncode(Context pContext){
		SharedPreferences getShopEncodePrefs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return getShopEncodePrefs.getString(SHOP_ENCODE, "");
	}

    /**
     * ����Ӧ����ʾ����
     * @param pContext
     * @param id
     */
    public static void setLanguage(Context pContext,int id){
		SharedPreferences setShopEncode = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setShopEncode.edit();
		editor.putInt(LANGUAGE, id);
		editor.commit();
	}

    /**
     * ��ȡ���õ�����
     * @param pContext
     * @return
     */
	public static int getLanguage(Context pContext){
		SharedPreferences getShopEncodePrefs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return getShopEncodePrefs.getInt(LANGUAGE, 0);
	}
	
	public static void setGiftAuth(Context pContext,String giftrange){
		SharedPreferences setPresentAuth = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE); 
		SharedPreferences.Editor editor = setPresentAuth.edit();
		editor.putString(GIFT_AUTH, giftrange);
		editor.commit();
	}
	
	public static String getGiftAuth(Context pContext){
		SharedPreferences getPresentAuth = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return getPresentAuth.getString(GIFT_AUTH, "49");
	}
	
	//��½ҳ�汳��ͼƬ·��
		private static final String BACKGROUND_PATH = "welcome_activity_background_path";
		
		/**
		 * �����½ҳ�汳��
		 * @param context 
		 * @param path ͼƬ·��
		 */
		public static void setbackgroundPath(Context context,String path){
			SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = setpath.edit();
			editor.putString(BACKGROUND_PATH, path);
			editor.commit();
		}
		
		/**
		 * ��ȡ��½ҳ�汳��
		 * @param context 
		 */
		public static String getbackgroundPath(Context context){
			SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
			return setpath.getString(BACKGROUND_PATH, "NotFind");
		}
	/**
	 * ����Ftp��iP��ַ
	 * @param context
	 */
	public static void setFtpIp(Context context,String ftpIp){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(FTP_IP, ftpIp);
		editor.commit();
	}
	
	/**
	 * �õ�Ftp��iP��ַ
	 * @param context
	 * @return
	 */
	public static String getFtpIp(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(FTP_IP, null);
	}
	
	
	/**
	 * ����Ftp���û���
	 * @param context
	 */
	public static void setFtpUsername(Context context,String ftpUsername){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(FTP_USERNAME, ftpUsername);
		editor.commit();
	}
	
	/**
	 * �õ�Ftp���û���
	 * @param context
	 * @return
	 */
	public static String getFtpUsername(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(FTP_USERNAME, "anonymous");
	}
	
	/**
	 * ����Ftp������
	 * @param context
	 */
	public static void setFtpPwd(Context context,String passWord){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(FTP_PWD, passWord);
		editor.commit();
	}
	
	/**
	 * �õ�Ftp������
	 * @param context
	 * @return
	 */
	public static String getFtpPwd(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(FTP_PWD, "");
	}
	
	
	/**
	 * ����deviceId
	 * @param context
	 * @param deviceId
	 */
	public static void setDeviceId(Context context,String deviceId){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(DEVICE_ID, deviceId);
		editor.commit();
	}
	
	/**
	 * �õ�deviceId
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(DEVICE_ID, "");
	}
	
	
	/**
	 * ���浱ǰʹ���豸��Ψһ��ʶ
	 * @param context
	 * @param physicalId
	 */
	public static void setPhysicalId(Context context,String physicalId){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(PHYSICAL_ID, physicalId);
		editor.commit();
	}
	
	/**
	 * �õ���ǰʹ���豸��Ψһ��ʶ
	 * @param context
	 */
	public static String getPhysicalId(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(PHYSICAL_ID, "");
	}

	
	/**
	 * ��������
	 */
	public static void setTemporarySave(Context context,boolean isSave) {

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(IS_TEMPORARYSAVE, isSave);
		editor.commit();
	}
	
	/**
	 * 
	 * 
	 */
	public static Boolean getTemporarySave(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getBoolean(IS_TEMPORARYSAVE, false);

	}
	
	
	
	/**
	 * ���÷������˵�ַ
	 */
	public static void setServerAddress(Context context, String address){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(SERVER_ADDRESS, address);
		editor.commit();
	}
	
	/**
	 * �õ��������˵�ַ
	 */
	public static String getServerAddress(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(SERVER_ADDRESS, "");

	}
	
	/**
	 * ��������userCode
	 */
	public static void setUserCode(Context context, String username) {

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(USER_NAME, username);
		editor.commit();
	}
	/**
	 * ̨λ����ʲô����
	 */
	public static void setTableClass(Context context, String clas) {
		
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(TABLE_CLASSIFY, clas);
		editor.commit();
	}
	/**
	 * ̨λ����ʲô����
	 */
	public static String getTableClass(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(TABLE_CLASSIFY, "1");
	}
	/**
	 * �Ƿ�ʹ�ñ��ؿ�
	 */
	public static void setIsNet(Context context, boolean orNet) {
		
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(OR_NET, orNet);
		editor.commit();
	}
	/**
	 * ��ȡ�Ƿ�ʹ�ñ��ؿ�����
	 */
	public static boolean getIsNet(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
//		return sp.getBoolean(OR_NET, true);
		return false;

	}

    /**
     * ��Ա�Ƿ����
     */
    public static void setIsVip(Context context, boolean orVip) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(OR_VIP, orVip);
        editor.commit();
    }
    /**
     * ��ȡ��Ա�Ƿ����
     */
    public static boolean getIsVip(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getBoolean(OR_VIP, false);

    }
	
	/**
	 * �õ�UserCode
	 * 
	 */
	public static String getUserCode(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(USER_NAME, "1");

	}
//	Ӫҵ��@Ȩ�ޱ����IPAD�� ���ж��Ƿ���Ȩ��@Ĩ��� ���С��λ@pos �汾�� @hht �汾��@����Ա���� 
	/**
	 * ����PAD��Ϣ
	 * @param context
	 * @param moL Ĩ���� ����С��λ
	 * @param posVer POS�汾
	 * @param HHTVer hht�汾
	 * @param operator ����Ա
	 */
	public static void setPadInformation(Context context,String moL,String posVer,String HHTVer,String operator,String rolecode){
		SharedPreferences sp=context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(POS_VER, posVer);
		editor.putString(HHT_VER, HHTVer);
		editor.putString(MOL, moL);
		editor.putString(OPERATOR, operator);
		editor.putString(ROLECODE, rolecode);
		editor.commit();
	}
	/**
	 * ��ȡȨ��
	 * @param context
	 * @return
	 */
	public static String getRoleCode(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(ROLECODE,"" );
	}
	/**
	 * ��ȡĨ����Ϣ
	 * @param context
	 * @return
	 */
	public static String getMoL(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(MOL,"");
	}
	/**
	 * ��ȡPOS�汾
	 * @param context
	 * @return
	 */
	public static String getPosVer(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(POS_VER, "");
	}
	/**
	 * ��ȡHHT�汾
	 */
	public static String getHHTVer(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(HHT_VER, "");
	}
	/**
	 * ��ȡ����Ա����
	 * @param context
	 * @return
	 */
	public static String getOperator(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(OPERATOR, "");
	}
	
	/**
	 * �����˵���ѯ���ͼƬ·��
	 * @param context 
	 * @param path ͼƬ·��
	 */
	public static void setLeftBgPath(Context context,String path){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setpath.edit();
		editor.putString(LEFTIMAGE_PATH, path);
		editor.commit();
	}
	
	/**
	 * ��ȡ�˵���ѯ���ͼƬ·��
	 * @param context 
	 */
	public static String getLeftBgPath(Context context){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getString(LEFTIMAGE_PATH, "NotFind");
	}

	/**
	 * �����Զ�ע������ʱ��
	 * @param context
	 * @param path ͼƬ·��
	 */
	public static void setLogoutTime(Context context,int path){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setpath.edit();
		editor.putInt(LOGOUT_TIME, path);
		editor.commit();
	}

	/**
	 * ��ȡ�Զ�ע������ʱ��
	 * @param context
	 */
	public static Integer getLogoutTime(Context context){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getInt(LOGOUT_TIME, 0);
	}


	/**
	 * �����Զ��˲������ʱ��
	 * @param context
	 * @param path ͼƬ·��
	 */
	public static void setCloseTime(Context context,int path){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setpath.edit();
		editor.putInt(CLOSE_TIME, path);
		editor.commit();
	}

	/**
	 * ��ȡ�Զ��˲������ʱ��
	 * @param context
	 */
	public static Integer getCloseTime(Context context){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getInt(CLOSE_TIME, 0);
	}

	/**
	 * ��λ��ַ�洢
	 * @param context
	 * @param url
	 */
	public static void setWaitUrl(Context context,String url){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setpath.edit();
		editor.putString(WAIT_URL, url);
		editor.commit();
	}

	/**
	 * ��ȡ��λ��ַ��Ϣ
	 * @param context
	 * @return
	 */
	public static String getWaitUrl(Context context) {
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getString(WAIT_URL, "");
	}

	/**
	 * ��λ��ַ�洢
	 * @param context
	 * @param ip
	 */
	public static void setWaitTV(Context context,String ip){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setpath.edit();
		editor.putString("WAIT_TV", ip);
		editor.commit();
	}

	/**
	 * ��ȡ��λ��ַ��Ϣ
	 * @param context
	 * @return
	 */
	public static String getWaitTV(Context context) {
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getString("WAIT_TV", "");
	}
}