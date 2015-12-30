package cn.com.choicesoft.chinese.bean;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

/**
 * �Խ���ζ���õ�
 * @author dell
 *
 */
public class JNowFood {
	
	
	private String id;//��Ʒ����
	private String name;//��Ʒ����
	private double price;//��Ʒ�۸�
	private List<String> dirs;//��Ʒ���
	private int state;//��Ʒ״̬
	private String unit;//��Ʒ��λ
	private int sortClass;//��Ʒ���     int����
	private int num;//��Ʒ����
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public List<String> getDirs() {
		return dirs;
	}
	public void setDirs(List<String> dirs) {
		this.dirs = dirs;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@JsonView
	public int getSortClass() {
		return sortClass;
	}
	
	public void setSortClass(int sortClass) {
		this.sortClass = sortClass;
	}
	@JsonView
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dirs == null) ? 0 : dirs.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + num;
		result = (int) (prime * result + price);
		result = prime * result + sortClass;
		result = prime * result + state;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof JNowFood)){
			throw new ClassCastException("Invalid class type");
		}
		
		JNowFood other = (JNowFood) obj;
		return this.name .equals(other.getName());
		
	}
	
	
	
	
}