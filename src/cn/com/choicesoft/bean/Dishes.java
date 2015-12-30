package cn.com.choicesoft.bean;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 菜品
 * 
 */
public class Dishes implements Parcelable {

	private String id;//id号
	private String name;// 菜名
	private String price;// 价格
	private String unit;// 单位
	private String type;//属于那种类型的
	private String isChecked;//是否选择了此菜
	private String counts;//顾客点某一种菜品的数量
	private ArrayList<Addition> additions;//顾客点某一种菜品所对应的附加项

	public ArrayList<Addition> getAdditions() {
		return additions;
	}

	public void setAdditions(ArrayList<Addition> additions) {
		this.additions = additions;
	}

	public static Parcelable.Creator<Dishes> getCreator() {
		return CREATOR;
	}

	public String getCounts() {
		return counts;
	}

	public void setCounts(String counts) {
		this.counts = counts;
	}

	public static final Parcelable.Creator<Dishes> CREATOR = new Creator<Dishes>() {

		@Override
		public Dishes createFromParcel(Parcel source) {
			Dishes dishes = new Dishes();
			dishes.setId(source.readString());
			dishes.setName(source.readString());
			dishes.setPrice(source.readString());
			dishes.setUnit(source.readString());
			dishes.setType(source.readString());
			dishes.setIsChecked(source.readString());
			dishes.setCounts(source.readString());
			return dishes;
		}

		@Override
		public Dishes[] newArray(int size) {
			return new Dishes[size];
		}
		
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(price);
		dest.writeString(unit);
		dest.writeString(type);
		dest.writeString(isChecked);
		dest.writeString(counts);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
