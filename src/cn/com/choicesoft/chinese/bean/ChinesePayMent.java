package cn.com.choicesoft.chinese.bean;

/**
 * 支付方式
 * Created by M.c on 2014/7/18.
 */
public class ChinesePayMent {
    private String cod;
    private String des;
    private String typ;
    private String foreigntag;//N实收，Y非实收
    private String code;
    private String money;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getForeigntag() {
        return foreigntag;
    }

    public void setForeigntag(String foreigntag) {
        this.foreigntag = foreigntag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
