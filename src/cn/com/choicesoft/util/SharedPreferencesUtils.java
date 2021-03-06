package cn.com.choicesoft.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的一个工具类
 * 
 */
public class SharedPreferencesUtils {
	/**
	 * 保存在手机里面的文件名
	 */
	private static final String FILE_NAME = "share_date";
	private static final String USER_NAME = "user_code";
	private static final String OR_NET = "or_net";
    private static final String OR_VIP = "or_vip";
    private static final String LANGUAGE = "language";
    private static final String SERVER_ADDRESS = "server_address";//webservice所用的IP地址
	private static final String IS_TEMPORARYSAVE = "is_temporary_save";
	private static final String PHYSICAL_ID = "physicalId";//该部手持机的唯一标示
	private static final String DEVICE_ID = "deviceId";//设备号
	private static final String FTP_IP = "ftpIp";//FTP所用的IP地址
	private static final String FTP_USERNAME = "ftpUsername";//FTP所用的用户名
	private static final String FTP_PWD = "ftpPwd";//FTP所用的密码
	private static final String POS_VER = "posVer";
	private static final String HHT_VER = "hhtVer";
	private static final String OPERATOR = "operator";
	private static final String ROLECODE = "rolecode";

	private static final String TABLE_CLASSIFY="table_classify";
	private static final String MOL = "moLing";
	private static final String PADINFOR = "PadInformation";
	private static final String GIFT_AUTH = "giftAuth";//赠送授权的金额
	private static final String SHOP_ENCODE = "shopEncode";//店铺编号
	private static final String IS_DISGUISH_GENDER = "is_disguishgender";//是否区分男女
	private static final String LEFTIMAGE_PATH = "leftimage_path";//账单查询页面左侧图片
	private static final String IS_REGISTERED = "isRegister";//判断设备是否已经注册用的
	private static final String CHINESE_TO_SNACK = "ChineseToSnack";//判断设备是否已经注册用的
	private static final String LOGOUT_TIME = "Logout_Time";//判断设备是否已经注册用的
	private static final String CLOSE_TIME = "Close_Time";//判断设备是否已经注册用的
	private static final String WAIT_URL = "wait_url";//等位信息
	private static final String JNOW_AUTH_USERNAME = "9now_username";//对接美味不用等，某一个西贝门店的授权账号
	private static final String JNOW_AUTH_PWD = "9now_pwd";//对接美味不用等，某一个西贝门店的授权密码
	private static final String BULETOOLTH_ADDRESS="";

	/**
	 *@Author:Ysc
	 *@Comments:存储某一个西贝门店的授权账号
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
	 *@Comments:得到某一个西贝门店的授权账号
	 *@CreateDate:2014-9-11
	 */
	public static String getJNOWUserName(Context pContext){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setSharePfs.getString(JNOW_AUTH_USERNAME, null);
	}
	/**
	 *@Author:Ysc
	 *@Comments:存储某一个西贝门店的授权账号
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
	 *@Comments:得到某一个西贝门店的授权账号
	 *@CreateDate:2014-9-11
	 */
	public static String getBuleToolthaddress(Context pContext){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setSharePfs.getString(BULETOOLTH_ADDRESS, null);
	}

	/**
	 *@Author:Ysc
	 *@Comments:存储某一个西贝门店的授权密码
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
	 *@Comments:得到某一个西贝门店的授权密码
	 *@CreateDate:2014-9-11
	 */
	public static String getJNOWPwd(Context pContext){
		SharedPreferences setSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setSharePfs.getString(JNOW_AUTH_PWD, null);
	}



	/**
	 *@Author:Ysc
	 *@Comments:判断该pad是否已经注册过
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
	 *@Comments:判断该pad是否已经注册过
	 *@CreateDate:2014-5-12
	 */
	public static boolean getIsRegister(Context pContext){
		SharedPreferences getSharePfs = pContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return getSharePfs.getBoolean(IS_REGISTERED, false);
	}

    /**
     * 设置中快餐切换
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
     * 获取中快餐切换
     * 0为快餐 1为中餐
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
     * 设置应用显示语言
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
     * 获取设置的语言
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
	
	//登陆页面背景图片路径
		private static final String BACKGROUND_PATH = "welcome_activity_background_path";
		
		/**
		 * 保存登陆页面背景
		 * @param context 
		 * @param path 图片路径
		 */
		public static void setbackgroundPath(Context context,String path){
			SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = setpath.edit();
			editor.putString(BACKGROUND_PATH, path);
			editor.commit();
		}
		
		/**
		 * 获取登陆页面背景
		 * @param context 
		 */
		public static String getbackgroundPath(Context context){
			SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
			return setpath.getString(BACKGROUND_PATH, "NotFind");
		}
	/**
	 * 保存Ftp的iP地址
	 * @param context
	 */
	public static void setFtpIp(Context context,String ftpIp){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(FTP_IP, ftpIp);
		editor.commit();
	}
	
	/**
	 * 得到Ftp的iP地址
	 * @param context
	 * @return
	 */
	public static String getFtpIp(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(FTP_IP, null);
	}
	
	
	/**
	 * 保存Ftp的用户名
	 * @param context
	 */
	public static void setFtpUsername(Context context,String ftpUsername){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(FTP_USERNAME, ftpUsername);
		editor.commit();
	}
	
	/**
	 * 得到Ftp的用户名
	 * @param context
	 * @return
	 */
	public static String getFtpUsername(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(FTP_USERNAME, "anonymous");
	}
	
	/**
	 * 保存Ftp的密码
	 * @param context
	 */
	public static void setFtpPwd(Context context,String passWord){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(FTP_PWD, passWord);
		editor.commit();
	}
	
	/**
	 * 得到Ftp的密码
	 * @param context
	 * @return
	 */
	public static String getFtpPwd(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(FTP_PWD, "");
	}
	
	
	/**
	 * 保存deviceId
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
	 * 得到deviceId
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(DEVICE_ID, "");
	}
	
	
	/**
	 * 保存当前使用设备的唯一标识
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
	 * 得到当前使用设备的唯一标识
	 * @param context
	 */
	public static String getPhysicalId(Context context){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(PHYSICAL_ID, "");
	}

	
	/**
	 * 保存数据
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
	 * 设置服务器端地址
	 */
	public static void setServerAddress(Context context, String address){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(SERVER_ADDRESS, address);
		editor.commit();
	}
	
	/**
	 * 得到服务器端地址
	 */
	public static String getServerAddress(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(SERVER_ADDRESS, "");

	}
	
	/**
	 * 保存数据userCode
	 */
	public static void setUserCode(Context context, String username) {

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(USER_NAME, username);
		editor.commit();
	}
	/**
	 * 台位根据什么分类
	 */
	public static void setTableClass(Context context, String clas) {
		
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(TABLE_CLASSIFY, clas);
		editor.commit();
	}
	/**
	 * 台位根据什么分类
	 */
	public static String getTableClass(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(TABLE_CLASSIFY, "1");
	}
	/**
	 * 是否使用本地库
	 */
	public static void setIsNet(Context context, boolean orNet) {
		
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(OR_NET, orNet);
		editor.commit();
	}
	/**
	 * 获取是否使用本地库设置
	 */
	public static boolean getIsNet(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
//		return sp.getBoolean(OR_NET, true);
		return false;

	}

    /**
     * 会员是否可用
     */
    public static void setIsVip(Context context, boolean orVip) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(OR_VIP, orVip);
        editor.commit();
    }
    /**
     * 获取会员是否可用
     */
    public static boolean getIsVip(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getBoolean(OR_VIP, false);

    }
	
	/**
	 * 得到UserCode
	 * 
	 */
	public static String getUserCode(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return sp.getString(USER_NAME, "1");

	}
//	营业日@权限编号由IPAD方 面判断是否有权限@抹零金 额保留小数位@pos 版本号 @hht 版本号@操作员名称 
	/**
	 * 保存PAD信息
	 * @param context
	 * @param moL 抹零金额 保留小数位
	 * @param posVer POS版本
	 * @param HHTVer hht版本
	 * @param operator 操作员
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
	 * 获取权限
	 * @param context
	 * @return
	 */
	public static String getRoleCode(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(ROLECODE,"" );
	}
	/**
	 * 获取抹零信息
	 * @param context
	 * @return
	 */
	public static String getMoL(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(MOL,"");
	}
	/**
	 * 获取POS版本
	 * @param context
	 * @return
	 */
	public static String getPosVer(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(POS_VER, "");
	}
	/**
	 * 获取HHT版本
	 */
	public static String getHHTVer(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(HHT_VER, "");
	}
	/**
	 * 获取操作员名称
	 * @param context
	 * @return
	 */
	public static String getOperator(Context context){
		SharedPreferences sp = context.getSharedPreferences(PADINFOR,Context.MODE_PRIVATE);
		return sp.getString(OPERATOR, "");
	}
	
	/**
	 * 保存账单查询左侧图片路径
	 * @param context 
	 * @param path 图片路径
	 */
	public static void setLeftBgPath(Context context,String path){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setpath.edit();
		editor.putString(LEFTIMAGE_PATH, path);
		editor.commit();
	}
	
	/**
	 * 获取账单查询左侧图片路径
	 * @param context 
	 */
	public static String getLeftBgPath(Context context){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getString(LEFTIMAGE_PATH, "NotFind");
	}

	/**
	 * 设置自动注销所需时间
	 * @param context
	 * @param path 图片路径
	 */
	public static void setLogoutTime(Context context,int path){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setpath.edit();
		editor.putInt(LOGOUT_TIME, path);
		editor.commit();
	}

	/**
	 * 获取自动注销所需时间
	 * @param context
	 */
	public static Integer getLogoutTime(Context context){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getInt(LOGOUT_TIME, 0);
	}


	/**
	 * 保存自动退菜所需的时间
	 * @param context
	 * @param path 图片路径
	 */
	public static void setCloseTime(Context context,int path){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setpath.edit();
		editor.putInt(CLOSE_TIME, path);
		editor.commit();
	}

	/**
	 * 获取自动退菜所需的时间
	 * @param context
	 */
	public static Integer getCloseTime(Context context){
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getInt(CLOSE_TIME, 0);
	}

	/**
	 * 等位地址存储
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
	 * 获取等位地址信息
	 * @param context
	 * @return
	 */
	public static String getWaitUrl(Context context) {
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getString(WAIT_URL, "");
	}

	/**
	 * 等位地址存储
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
	 * 获取等位地址信息
	 * @param context
	 * @return
	 */
	public static String getWaitTV(Context context) {
		SharedPreferences setpath = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return setpath.getString("WAIT_TV", "");
	}
}
