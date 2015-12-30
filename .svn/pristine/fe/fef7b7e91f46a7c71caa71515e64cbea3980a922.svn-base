package cn.com.choicesoft.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PresentReason implements Parcelable{
	
	private int VCode;
	private String VName;
	private String VInit;
	
	
	
	public int getVCode() {
		return VCode;
	}
	public void setVCode(int vCode) {
		VCode = vCode;
	}
	
	public String getVName() {
		return VName;
	}
	public void setVName(String vName) {
		VName = vName;
	}
	public String getVInit() {
		return VInit;
	}
	public void setVInit(String vInit) {
		VInit = vInit;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
	public static final Parcelable.Creator<PresentReason> CREATOR = new Creator<PresentReason>() {

		@Override
		public PresentReason createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			PresentReason reason = new PresentReason();
			return reason;
		}

		@Override
		public PresentReason[] newArray(int size) {
			// TODO Auto-generated method stub
			return new PresentReason[size];
		}
		
	};
	
	

}
