package cn.com.choicesoft.bean;
/**
 * 现金支付实体
 *@Author:M.c
 *@CreateDate:2014-1-18
 *@Email:JNWSCZH@163.COM
 */
public class MoneyPay {
	private String payName;		 //名称
	private int payNum;		  	 //数量
	private String payId;		 //ID
	private double payMoney;	 //券金额
	private String payTrace;	 //支付流水号
	private String payCardNumber;//会员卡号
	private String payIntegral;	 //积分余额
	private String payAuth;		 //授权
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public int getPayNum() {
		return payNum;
	}
	public void setPayNum(int payNum) {
		this.payNum = payNum;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayAuth() {
		return payAuth;
	}
	public void setPayAuth(String payAuth) {
		this.payAuth = payAuth;
	}
	public String getPayTrace() {
		return payTrace;
	}
	public void setPayTrace(String payTrace) {
		this.payTrace = payTrace;
	}
	public String getPayCardNumber() {
		return payCardNumber;
	}
	public void setPayCardNumber(String payCardNumber) {
		this.payCardNumber = payCardNumber;
	}
	public String getPayIntegral() {
		return payIntegral;
	}
	public void setPayIntegral(String payIntegral) {
		this.payIntegral = payIntegral;
	}
	
}
