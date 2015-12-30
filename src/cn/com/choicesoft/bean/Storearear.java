package cn.com.choicesoft.bean;

public class Storearear {
	
	private String arearid;
	private String tblname;
	
	public String getArearid() {
		return arearid;
	}
	public void setArearid(String arearid) {
		this.arearid = arearid;
	}
	public String getTblname() {
		return tblname;
	}
	public void setTblname(String tblname) {
		this.tblname = tblname;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return arearid+"**"+tblname;
	}

}
