package cn.com.choicesoft.chinese.bean;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * 对接美味不用等
 * @author dell
 *
 */
public class JGrptyp {
	
	
	private int grpTyp;
	private String grpDes;
	
	@JsonView
	public int getGrpTyp() {
		return grpTyp;
	}
	public void setGrpTyp(int grpTyp) {
		this.grpTyp = grpTyp;
	}
	public String getGrpDes() {
		return grpDes;
	}
	public void setGrpDes(String grpDes) {
		this.grpDes = grpDes;
	}
	
	

}
