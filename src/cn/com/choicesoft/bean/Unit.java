package cn.com.choicesoft.bean;

import java.io.Serializable;

/**
 * 单位实体类
 * Created by M.c on 2014/9/3.
 */
public class Unit implements Serializable{
    private String unitName;
    private String unitCode;
    private String unitPrice;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
}
