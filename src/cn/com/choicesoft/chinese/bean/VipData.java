package cn.com.choicesoft.chinese.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 会员卡信息
 * Created by M.c on 2014/9/10.
 */
public class VipData implements Serializable {
    private String cardId;//卡ID
    private String cardNumber;//卡号
    private String cardName;//卡姓名
    private String phone;//手机号
    private String expDate;//有效期
    private String cardState;//卡状态
    private String cardClass;//卡类别
    private String cardYuE;//卡余额
    private String jiFenYuE;//卡积分余额
    private String benJin;//本金
    private String give;//赠送
    private String shouXuFei;//手续费
    private String tuiKaJinE;//退卡金额
    private List<TicketData> list;//电子券列表

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCardState() {
        return cardState;
    }

    public void setCardState(String cardState) {
        this.cardState = cardState;
    }

    public String getCardClass() {
        return cardClass;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    public String getCardYuE() {
        return cardYuE;
    }

    public void setCardYuE(String cardYuE) {
        this.cardYuE = cardYuE;
    }

    public String getJiFenYuE() {
        return jiFenYuE;
    }

    public void setJiFenYuE(String jiFenYuE) {
        this.jiFenYuE = jiFenYuE;
    }

    public String getBenJin() {
        return benJin;
    }

    public void setBenJin(String benJin) {
        this.benJin = benJin;
    }

    public String getGive() {
        return give;
    }

    public void setGive(String give) {
        this.give = give;
    }

    public String getShouXuFei() {
        return shouXuFei;
    }

    public void setShouXuFei(String shouXuFei) {
        this.shouXuFei = shouXuFei;
    }

    public String getTuiKaJinE() {
        return tuiKaJinE;
    }

    public void setTuiKaJinE(String tuiKaJinE) {
        this.tuiKaJinE = tuiKaJinE;
    }

    public List<TicketData> getList() {
        return list;
    }

    public void setList(List<TicketData> list) {
        this.list = list;
    }
}
