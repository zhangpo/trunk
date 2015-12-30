package cn.com.choicesoft.bean;
/**
 * 优惠券 暂存 实体
 * @Author:M.c
 * @CreateDate:2014-1-17
 * @Email:JNWSCZH@163.COM
 */
public class CouponStoreBean {
	private String couponId;	//优惠券ID
	private String couponName;	//优惠券名称
	private String couponMoney;	//优惠券金额
	private int couponNum;	//优惠券数量
	private String couponAuth;	//优惠券授权
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getCouponMoney() {
		return couponMoney;
	}
	public void setCouponMoney(String couponMoney) {
		this.couponMoney = couponMoney;
	}
	public int getCouponNum() {
		return couponNum;
	}
	public void setCouponNum(int couponNum) {
		this.couponNum = couponNum;
	}
	public String getCouponAuth() {
		return couponAuth;
	}
	public void setCouponAuth(String couponAuth) {
		this.couponAuth = couponAuth;
	}
	
}
