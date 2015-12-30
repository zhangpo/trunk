package cn.com.choicesoft.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 附加项
 * 
 */
public class Addition implements Parcelable{
	
	private String init;
	private String pcode;
    private String foodFuJia_Id;//编码
    private String foodFuJia_des;//名称
    private String foodFuJia_checked;//表示价格
    private boolean isSelected;
    private String pcount="0";
    private String temporaryFujia;
    private String maxCnt;
    private String minCnt;
    private String maxgCnt;
    private String rgrp;
    private String producttc_order;
    private String defualts;
    private String isaddprod;
    private String ismust;

    public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public Addition() {
		super();
	}

	public Addition(String foodFuJia_Id, String foodFuJia_des,
			String foodFuJia_checked, boolean isSelected) {
		super();
		this.foodFuJia_Id = foodFuJia_Id;
		this.foodFuJia_des = foodFuJia_des;
		this.foodFuJia_checked = foodFuJia_checked;
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getFoodFuJia_Id() {
		return foodFuJia_Id;
	}
	public void setFoodFuJia_Id(String foodFuJia_Id) {
		this.foodFuJia_Id = foodFuJia_Id;
	}
	public String getFoodFuJia_des() {
		return foodFuJia_des;
	}
	public void setFoodFuJia_des(String foodFuJia_des) {
		this.foodFuJia_des = foodFuJia_des;
	}
	public String getFoodFuJia_checked() {
		return foodFuJia_checked;
	}
	public void setFoodFuJia_checked(String foodFuJia_checked) {
		this.foodFuJia_checked = foodFuJia_checked;
	}

    public String getTemporaryFujia() {
        return temporaryFujia;
    }

    public void setTemporaryFujia(String temporaryFujia) {
        this.temporaryFujia = temporaryFujia;
    }

    public String getMaxCnt() {
        return maxCnt;
    }

    public void setMaxCnt(String maxCnt) {
        this.maxCnt = maxCnt;
    }

    public String getMinCnt() {
        return minCnt;
    }

    public void setMinCnt(String minCnt) {
        this.minCnt = minCnt;
    }

    public String getMaxgCnt() {
        return maxgCnt;
    }

    public void setMaxgCnt(String maxgCnt) {
        this.maxgCnt = maxgCnt;
    }

    public String getRgrp() {
        return rgrp;
    }

    public void setRgrp(String rgrp) {
        this.rgrp = rgrp;
    }

    public String getProducttc_order() {
        return producttc_order;
    }

    public void setProducttc_order(String producttc_order) {
        this.producttc_order = producttc_order;
    }

    public String getDefualts() {
        return defualts;
    }

    public void setDefualts(String defualts) {
        this.defualts = defualts;
    }

    public String getIsaddprod() {
        return isaddprod;
    }

    public void setIsaddprod(String isaddprod) {
        this.isaddprod = isaddprod;
    }

    public String getIsmust() {
        return ismust;
    }

    public void setIsmust(String ismust) {
        this.ismust = ismust;
    }

    public String getPcount() {
        return pcount;
    }

    public void setPcount(String pcount) {
        this.pcount = pcount;
    }

    @Override
	public String toString() {
		return "Addition [foodFuJia_Id=" + foodFuJia_Id + ", foodFuJia_des="
				+ foodFuJia_des + ", foodFuJia_checked=" + foodFuJia_checked
				+ ", isSelected=" + isSelected + "]";
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
	
	public static final Parcelable.Creator<Addition> CREATOR = new Creator<Addition>() {

		@Override
		public Addition createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			Addition addition = new Addition();
			return addition;
		}

		@Override
		public Addition[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Addition[size];
		}
		
	};

}
