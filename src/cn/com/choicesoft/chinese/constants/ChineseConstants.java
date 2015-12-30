package cn.com.choicesoft.chinese.constants;

import android.content.Context;
import cn.com.choicesoft.util.SharedPreferencesUtils;

/**
 * �в͹�������
 * Created by M.c on 2014/6/10.
 */
public class ChineseConstants {
    public static String getURL(Context context){
        return SharedPreferencesUtils.getServerAddress(context);
    }
    public static final String NAMESPACE = "http://service.web.choice.com";// �����������ռ�
    /**
     * ����webservice���õĵ�ַ
     */
    public static final String HOST = "ChoiceWebService/services/ChineseFoodIpadService/";
    /**
     * ��¼
     */
    public static final String PLOGIN="pLogin";


    public static final String GET_CHINESE_PERMISSION="getChinesePermission";
    /**
     * ��ѯ̨λ
     */
    public static final String PLISTTABLE="pListTable";
    /**
     * ��̨
     */
    public static final String PSTART="pStart";
    /**
     * ȡ����̨
     */
    public static final String POVER="pOver";
    /**
     * ��̨
     */
    public static final String PCHANGETABLE="pChangeTable";
    /**
     * �ϴ���Ʒ
     */
    public static final String PSENDTAB="pSendtab";
    /**
     * �˵���ѯ
     */
    public static final String PQUERY="pQuery";
    /**
     * �߲�
     */
    public static final String PGOGO="pGogo";
    /**
     * �����Ʒ��֤
     */
    public static final String PFREEGET="pFreeGet";
    /**
     * ����
     */
    public static final String DRAWITEMS="drawItems";
    /**
     * ֧���ӿ�
     */
    public static final String USERPAYMENT="userPayment";
    /**
     * ̨λ����
     */
    public static final String QUERYTABLES="queryTables";
    /**
     * �˲�
     */
    public static final String PCHUCK="pChuck";
    /**
     * �޸�����
     */
    public static final String MODIFYPAX="modifyPax";
    /**
     * ��ȡ�˵���������
     */
    public static final String GETFOLIONO="getFolioNo";
    /**
     * ��ȡ֧����¼
     */
    public static final String QUERYPAYMENTS="queryPayments";
    /**
     * ȡ��֧��
     */
    public static final String CANCELPAYMENT="cancelPayment";
    /**
     * ͨ���ֻ��Ų�ѯ��Ա����Ϣ
     */
    public static final String QUERYCARDBYMOBTEL="queryCardByMobTel";
    /**
     * ��ѯ����ӿ�
     */
    public static final String ESTIMATESFOODLIST="EstimatesFoodList";
    /**
     * ͨ����Ա���Ų�ѯ��Ա��Ϣ
     */
    public static final String QUERYCARDBYCARDNO="queryCardByCardNo";
    /**
     * �۵���ȯ
     */
    public static final String CARDOUTCOUPON="cardOutCoupon";

    /**
     * ��ӡ��ѯ������˵�
     */
    public static final String PRINT_ORDER = "pPrintquery";
    /**
     * ���Ͳ�Ʒ���޸Ĳ�Ʒ������������
     */
    public static final String MODIFY_FOODCNT = "modifyItemCnt";
    /**
     * ���Ͳ�Ʒ
     */
    public static final String DONATE_FOODITEM = "donateItem";
    /**
     * ִ�л�Ա��
     */
    public static final String UPDATE_PRICE="updatePrice";
    /**
     * ִ���Ż�ȯ
     */
    public static final String USER_ACTM="userActm";
    /**
     * �����ֻ��Ż�ȡ��Ա��
     */
    public static final String QUERY_CARD_BY_MOBTEL="queryCardByMobTel";
    /**
     * ���ݻ�Ա�Ż�ȡ��Ա��Ϣ
     */
    public static final String QUERY_CARD_BY_CARDNO = "queryCardByCardNo";
    /**
     * ȡ���Ż�
     */
    public static final String CANCEL_ACTM = "cancelActm";
    /**
     * ��ȡ�Ż��б�
     */
    public static final String QUERY_PAYMENTS = "queryPayments";
    /**
     * ��ȡ�����Ʒ
     */
    public static final String ESTIMATES_FOOD_LIST = "EstimatesFoodList";
    /**
     * ִ�й���
     */
    public static final String SETESTIMATES_FOOD_LIST = "SetEstimatesFoodList";
    /**
     * �豸ע��
     */
    public static final String EQUIP_MENT_CODING = "equipmentCoding";

    /**
     * ��Ʒת��
     */
    public static final String FOODTOOTHERORDR ="pFoodToOtherOrdr";
    /**
     * ��ȡ��ζ���õȶ���
     */
    public static final String DELICIOUS_NOT_ETC_BY_ORDERID="DeliciousNotEtcByOrderId";
    /**
     * �豸ע��
     */
    public static final String ANDROID_APP_URL = "androidAppUrl";

    public static final String UPDATA_CODEDESC ="updataCodeDesc";
}
