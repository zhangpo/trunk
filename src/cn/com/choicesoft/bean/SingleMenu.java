package cn.com.choicesoft.bean;

import java.util.List;
import java.util.Map;

public class SingleMenu {
	
	public static SingleMenu singleMenu = null;
	
	private SingleMenu(){}
	
	public static SingleMenu getMenuInstance(){
		if(null == singleMenu){
			singleMenu = new SingleMenu();
		}
		return singleMenu;
		
	}
	
	private String menuOrder;
	private String manCounts;
	private String womanCounts;
	private String tableNum;
	private String userCode;
	private String orderNum;
	private String cardNum;
	private String cardPhone;
	private VipRecord vipRecord;
	private boolean isJNow;//是否使用美味不用等开台的标识
	private boolean isJNowFirst;//只执行一次
	private String orderIdJNow;//服务员从客人口头要的美味不用等生成的账单号
	private List<Map<String,String>> permissionList; //权限列表
	private String priceTyp;						//Y价格方案  N根据台位
	private String priceKay;						//价格
	private List<String>acctFood;
	public  void deleteData(){
		menuOrder=null;
		manCounts=null;
		womanCounts=null;
		tableNum=null;
		userCode=null;
		orderNum=null;
		cardNum=null;
		cardPhone=null;
	}
//	private String orderId;

//	public String getOrderId(){
//		return  orderId;
//	}
//	public  void  setOrderId(String orderId){
//		this.orderId=orderId;
//	}
	public List<String> getAcctFood(){
	return acctFood;
}
	public void setAcctFood(List<String> acctFood){
		this.acctFood=acctFood;
	}
	public String getPriceKay(){
	return priceKay;
}
	public void setPriceKay(String priceKay){
		this.priceKay=priceKay;
	}
	public String getPriceTyp(){
		return priceTyp;
	}
	public void setPriceTyp(String priceTyp){
		this.priceTyp=priceTyp;
	}
	public boolean isJNow() {
	return isJNow;
}

	public void setJNow(boolean isJNow) {
		this.isJNow = isJNow;
	}

	public String getOrderIdJNow() {
		return orderIdJNow;
	}

	public void setOrderIdJNow(String orderIdJNow) {
		this.orderIdJNow = orderIdJNow;
	}


	public boolean isJNowFirst() {
		return isJNowFirst;
	}

	public void setJNowFirst(boolean isJNowFirst) {
		this.isJNowFirst = isJNowFirst;
	}
	public List<Map<String,String>> getPermissionList(){
		return permissionList;
	}
	public void setPermissionList(List<Map<String,String>> permissionList){
		this.permissionList=permissionList;
	}
	public VipRecord getVipRecord(){
		return vipRecord;
	}
	public void setVipRecord(VipRecord VipRecord){
		vipRecord=VipRecord;
	}
	public String getCardNum(){return cardNum;}
	public void setCardNum(String CardNum){
		this.cardNum=CardNum;
	}
	public String getCardPhone(){
		return this.cardPhone;
	}
	public void setCardPhone(String CardPhone){
		this.cardPhone=CardPhone;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(String menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getManCounts() {
		return manCounts;
	}

	public void setManCounts(String manCounts) {
		this.manCounts = manCounts;
	}

	public String getWomanCounts() {
		return womanCounts;
	}

	public void setWomanCounts(String womanCounts) {
		this.womanCounts = womanCounts;
	}

	public String getTableNum() {
		return tableNum;
	}

	public void setTableNum(String tableNum) {
		this.tableNum = tableNum;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Override
	public String toString() {
		return "SingleMenu [menuOrder=" + menuOrder + ", manCounts="
				+ manCounts + ", womanCounts=" + womanCounts + ", tableNum="
				+ tableNum + ", userCode=" + userCode + "]";
	}
	
	

}
