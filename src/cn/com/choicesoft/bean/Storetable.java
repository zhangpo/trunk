package cn.com.choicesoft.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * �ŵ�̨λʵ����
 */
public class Storetable implements Parcelable{
	
	private String tablenum;// ̨λid
	private String tblname;// ̨λ����
	private String arearid;//̨λ��Ӧ������id
	private String usestate;// ��̨״̬�����ݲ�ͬ״̬����ʾ��ͬ����ɫ
	private String person;//̨λ����
	private String floorId;//¥�����
	private String orderId;//�˵���
	private String priceKay;//�۸�


	public String getPriceKay(){
		return priceKay;
	}
	public void  setPriceKay(String priceKay){
		this.priceKay=priceKay;
	}
	public String getOrderId(){
		return orderId;
	}
	public void  setOrderId(String orderId){
		this.orderId=orderId;
	}

	public String getTablenum() {
		return tablenum;
	}
	public void setTablenum(String tablenum) {
		this.tablenum = tablenum;
	}
	
	public String getTblname() {
		return tblname;
	}
	public void setTblname(String tblname) {
		this.tblname = tblname;
	}
	public String getArearid() {
		return arearid;
	}
	public void setArearid(String arearid) {
		this.arearid = arearid;
	}
	public String getUsestate() {
		return usestate;
	}
	public void setUsestate(String usestate) {
		this.usestate = usestate;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getFloorId() {
		return floorId;
	}
	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(arearid);
		dest.writeString(arearid);
		dest.writeString(arearid);
		dest.writeString(arearid);
	}
	
	
	public static final Parcelable.Creator<Storetable> CREATOR = new Creator<Storetable>() {
		@Override
		public Storetable createFromParcel(Parcel source) {
			Storetable storeTable = new Storetable();
			storeTable.setArearid(source.readString());
			storeTable.setTablenum(source.readString());
			storeTable.setTblname(source.readString());
			storeTable.setUsestate(source.readString());
			storeTable.setPerson(source.readString());
			storeTable.setFloorId(source.readString());
			return storeTable;
		}

		@Override
		public Storetable[] newArray(int size) {
			return new Storetable[size];
		}
	};
}