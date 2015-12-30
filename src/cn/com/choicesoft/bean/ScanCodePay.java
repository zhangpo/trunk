package cn.com.choicesoft.bean;

/**
 * É¨Ãè¶þÎ¬ÂëÖ§¸¶
 * M¡£c
 * 2015-06-04
 * Jnwsczh@163.com
 */
public class ScanCodePay {
    private String operate;
    private String auth_code;
    private String total_fee;
    private String orderid;
    private String finished;
    private String type;

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
