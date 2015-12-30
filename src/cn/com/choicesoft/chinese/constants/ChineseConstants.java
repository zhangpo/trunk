package cn.com.choicesoft.chinese.constants;

import android.content.Context;
import cn.com.choicesoft.util.SharedPreferencesUtils;

/**
 * 中餐公共参数
 * Created by M.c on 2014/6/10.
 */
public class ChineseConstants {
    public static String getURL(Context context){
        return SharedPreferencesUtils.getServerAddress(context);
    }
    public static final String NAMESPACE = "http://service.web.choice.com";// 新辣道命名空间
    /**
     * 调用webservice所用的地址
     */
    public static final String HOST = "ChoiceWebService/services/ChineseFoodIpadService/";
    /**
     * 登录
     */
    public static final String PLOGIN="pLogin";


    public static final String GET_CHINESE_PERMISSION="getChinesePermission";
    /**
     * 查询台位
     */
    public static final String PLISTTABLE="pListTable";
    /**
     * 开台
     */
    public static final String PSTART="pStart";
    /**
     * 取消开台
     */
    public static final String POVER="pOver";
    /**
     * 换台
     */
    public static final String PCHANGETABLE="pChangeTable";
    /**
     * 上传菜品
     */
    public static final String PSENDTAB="pSendtab";
    /**
     * 账单查询
     */
    public static final String PQUERY="pQuery";
    /**
     * 催菜
     */
    public static final String PGOGO="pGogo";
    /**
     * 沽清菜品验证
     */
    public static final String PFREEGET="pFreeGet";
    /**
     * 划菜
     */
    public static final String DRAWITEMS="drawItems";
    /**
     * 支付接口
     */
    public static final String USERPAYMENT="userPayment";
    /**
     * 台位搜索
     */
    public static final String QUERYTABLES="queryTables";
    /**
     * 退菜
     */
    public static final String PCHUCK="pChuck";
    /**
     * 修改人数
     */
    public static final String MODIFYPAX="modifyPax";
    /**
     * 获取账单号与人数
     */
    public static final String GETFOLIONO="getFolioNo";
    /**
     * 获取支付记录
     */
    public static final String QUERYPAYMENTS="queryPayments";
    /**
     * 取消支付
     */
    public static final String CANCELPAYMENT="cancelPayment";
    /**
     * 通过手机号查询会员卡信息
     */
    public static final String QUERYCARDBYMOBTEL="queryCardByMobTel";
    /**
     * 查询估清接口
     */
    public static final String ESTIMATESFOODLIST="EstimatesFoodList";
    /**
     * 通过会员卡号查询会员信息
     */
    public static final String QUERYCARDBYCARDNO="queryCardByCardNo";
    /**
     * 扣电子券
     */
    public static final String CARDOUTCOUPON="cardOutCoupon";

    /**
     * 打印查询单或结账单
     */
    public static final String PRINT_ORDER = "pPrintquery";
    /**
     * 发送菜品后，修改菜品数量，条，斤
     */
    public static final String MODIFY_FOODCNT = "modifyItemCnt";
    /**
     * 赠送菜品
     */
    public static final String DONATE_FOODITEM = "donateItem";
    /**
     * 执行会员价
     */
    public static final String UPDATE_PRICE="updatePrice";
    /**
     * 执行优惠券
     */
    public static final String USER_ACTM="userActm";
    /**
     * 根据手机号获取会员号
     */
    public static final String QUERY_CARD_BY_MOBTEL="queryCardByMobTel";
    /**
     * 根据会员号获取会员信息
     */
    public static final String QUERY_CARD_BY_CARDNO = "queryCardByCardNo";
    /**
     * 取消优惠
     */
    public static final String CANCEL_ACTM = "cancelActm";
    /**
     * 获取优惠列表
     */
    public static final String QUERY_PAYMENTS = "queryPayments";
    /**
     * 获取估清菜品
     */
    public static final String ESTIMATES_FOOD_LIST = "EstimatesFoodList";
    /**
     * 执行估清
     */
    public static final String SETESTIMATES_FOOD_LIST = "SetEstimatesFoodList";
    /**
     * 设备注册
     */
    public static final String EQUIP_MENT_CODING = "equipmentCoding";

    /**
     * 菜品转单
     */
    public static final String FOODTOOTHERORDR ="pFoodToOtherOrdr";
    /**
     * 获取美味不用等订单
     */
    public static final String DELICIOUS_NOT_ETC_BY_ORDERID="DeliciousNotEtcByOrderId";
    /**
     * 设备注册
     */
    public static final String ANDROID_APP_URL = "androidAppUrl";

    public static final String UPDATA_CODEDESC ="updataCodeDesc";
}
