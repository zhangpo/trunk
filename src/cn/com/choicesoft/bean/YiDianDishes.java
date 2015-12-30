package cn.com.choicesoft.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 已点菜品
 * 
 */
public class YiDianDishes implements Parcelable {

	private String id;
	private String name;// 菜名
	private String price;// 价格
	private String unit;// 单位
	// private String type;//属于那种类型的
	private String count;// 点菜的数量
//	private String subtotal;// 小计
	private String additions;// 附加项
	private String init;// 缩写
	private String grptyp;// 所属大类
	private String isChecked;// 是否选择了此菜
	

	public YiDianDishes() {
		super();
	}

	public YiDianDishes(String id, String name, String price, String unit,
			String count, String additions, String init, String grptyp,
			String isChecked) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.unit = unit;
		this.count = count;
		this.additions = additions;
		this.init = init;
		this.grptyp = grptyp;
		this.isChecked = isChecked;
	}

	public static final Parcelable.Creator<YiDianDishes> CREATOR = new Creator<YiDianDishes>() {

		@Override
		public YiDianDishes createFromParcel(Parcel source) {
			YiDianDishes dishes = new YiDianDishes();
			dishes.setId(source.readString());
			dishes.setName(source.readString());
			dishes.setPrice(source.readString());
			dishes.setUnit(source.readString());
			// dishes.setType(source.readString());
			dishes.setCount(source.readString());
//			dishes.setSubtotal(source.readString());
			dishes.setAdditions(source.readString());
			dishes.setInit(source.readString());
			dishes.setGrptyp(source.readString());
			dishes.setIsChecked(source.readString());
			return dishes;
		}

		@Override
		public YiDianDishes[] newArray(int size) {
			return new YiDianDishes[size];
		}

	};

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

//	public String getSubtotal() {
//		return subtotal;
//	}
//
//	public void setSubtotal(String subtotal) {
//		this.subtotal = subtotal;
//	}

	public String getAdditions() {
		return additions;
	}

	public void setAdditions(String object) {
		this.additions = object;
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
		// dest.writeString(type);
		dest.writeString(count);
//		dest.writeString(subtotal);
		dest.writeString(additions);
		dest.writeString(init);
		dest.writeString(grptyp);
		dest.writeString(isChecked);
	}

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// public String getType() {
	// return type;
	// }
	//
	//
	// public void setType(String type) {
	// this.type = type;
	// }

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getGrptyp() {
		return grptyp;
	}

	public void setGrptyp(String grptyp) {
		this.grptyp = grptyp;
	}

	@Override
	public String toString() {
		return id + "===" + name + "=== 价格：" + price + "===" + unit + "===" + count
				+ "==="+additions + "===" + init + "==="
				+ grptyp + "===" + isChecked;
	}

}
