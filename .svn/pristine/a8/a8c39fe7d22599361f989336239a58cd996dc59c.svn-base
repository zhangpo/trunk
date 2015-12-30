package cn.com.choicesoft.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 台位类
 *
 */

public class TaiWei implements Parcelable {

	private String id;// 台位id
	private String des;// 台位名称
	private String area;//台位对应的区域
	private String printerNumber;// 台位连接的打印机编号
	private String maxPeople;// 台位最多人数
	private String currentPeople;// 当前人数
	private String minConsumption;// 最低消费
	private String status;// 桌台状态，根据不同状态，显示不同的颜色

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(String maxPeople) {
		this.maxPeople = maxPeople;
	}

	public String getPrinterNumber() {
		return printerNumber;
	}

	public void setPrinterNumber(String printerNumber) {
		this.printerNumber = printerNumber;
	}

	public String getCurrentPeople() {
		return currentPeople;
	}

	public void setCurrentPeople(String currentPeople) {
		this.currentPeople = currentPeople;
	}

	public String getMinConsumption() {
		return minConsumption;
	}

	public void setMinConsumption(String minConsumption) {
		this.minConsumption = minConsumption;
	}
	
	
	public TaiWei() {
		super();
	}

	public TaiWei(String id, String des, String area, String printerNumber,
			String maxPeople, String currentPeople, String minConsumption,
			String status) {
		super();
		this.id = id;
		this.des = des;
		this.area = area;
		this.printerNumber = printerNumber;
		this.maxPeople = maxPeople;
		this.currentPeople = currentPeople;
		this.minConsumption = minConsumption;
		this.status = status;
	}





	public static final Parcelable.Creator<TaiWei> CREATOR = new Creator<TaiWei>() {

		@Override
		public TaiWei createFromParcel(Parcel source) {
			TaiWei dishes = new TaiWei();
			dishes.setId(source.readString());
			dishes.setDes(source.readString());
			dishes.setArea(source.readString());
			dishes.setStatus(source.readString());
			dishes.setMinConsumption(source.readString());
			dishes.setCurrentPeople(source.readString());
			dishes.setMaxPeople(source.readString());
			dishes.setPrinterNumber(source.readString());
			return dishes;
		}

		@Override
		public TaiWei[] newArray(int size) {
			return new TaiWei[size];
		}

	};

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(des);
		dest.writeString(area);
		dest.writeString(printerNumber);
		dest.writeString(maxPeople);
		dest.writeString(currentPeople);
		dest.writeString(minConsumption);
		dest.writeString(status);

	}

	@Override
	public String toString() {
		return "id:"+id + "-" +"des:"+ des + "-" +"printerNumber:"+ printerNumber+ "-" + "maxPeople:"+maxPeople + "-"
				+ "currentPeople:"+currentPeople + "-" +"minConsumption:"+ minConsumption + "-"
				+ "status:"+status;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

}
