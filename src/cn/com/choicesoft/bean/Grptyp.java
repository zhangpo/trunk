package cn.com.choicesoft.bean;

/**
 * ��Ʒ����
 */
public class Grptyp {
	private String grp;//���
	private String des;//��Ӧ������ 
	private String quantity;//����
	
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getGrp() {
		return grp;
	}
	public void setGrp(String grp) {
		this.grp = grp;
	}
	@Override
	public String toString() {
		return "Grptyp [grp=" + grp + ", des=" + des + "]";
	}
	
	
}
