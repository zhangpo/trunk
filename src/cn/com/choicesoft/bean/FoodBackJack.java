package cn.com.choicesoft.bean;

import java.util.List;

/**
 * M��c
 * 2015-05-07
 * Jnwsczh@163.com
 */
public class FoodBackJack {
    private String orderid;
    private String accreditcode;
    private String backreason;
    private String isAccurate;
    private List<FoodBack> dishList;

    public String getIsAccurate() {
        return isAccurate;
    }

    public void setIsAccurate(String isAccurate) {
        this.isAccurate = isAccurate;
    }
    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAccreditcode() {
        return accreditcode;
    }

    public void setAccreditcode(String accreditcode) {
        this.accreditcode = accreditcode;
    }

    public String getBackreason() {
        return backreason;
    }

    public void setBackreason(String backreason) {
        this.backreason = backreason;
    }

    public List<FoodBack> getDishList() {
        return dishList;
    }

    public void setDishList(List<FoodBack> dishList) {
        this.dishList = dishList;
    }
}
