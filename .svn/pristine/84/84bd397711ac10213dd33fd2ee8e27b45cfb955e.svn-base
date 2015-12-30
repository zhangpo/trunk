package cn.com.choicesoft.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentUnit implements Parcelable{
//	SELECT itcode,des,unitcur FROM food WHERE UNITCUR = 2
	
	private String itcode;//菜品编码
	private String des;//菜品名称
	private String unitcur;//第二单位标志=2
	public String getItcode() {
		return itcode;
	}
	public void setItcode(String itcode) {
		this.itcode = itcode;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getUnitcur() {
		return unitcur;
	}
	public void setUnitcur(String unitcur) {
		this.unitcur = unitcur;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}
	
	public static final Parcelable.Creator<CurrentUnit> CREATOR = new Creator<CurrentUnit>() {

		@Override
		public CurrentUnit createFromParcel(Parcel source) {
			CurrentUnit curUnit = new CurrentUnit();
			return curUnit;
		}

		@Override
		public CurrentUnit[] newArray(int size) {
			return new CurrentUnit[size];
		}
		
	};
	
	

}
