package cn.com.choicesoft.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 菜品
 */
public class Food implements Parcelable,Serializable{
	
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	private String tabNum;//台位号
	private String orderId;//账单号
	private String Time;//时间
	private String PKID;//每个菜品拼接的标识 
	private String pcode;
	private String pcname;
	private String tpcode;
	private String tpname;
	private String tpnum;
	private String pcount;
	private String promonum;
	private String fujiacode;
	private String fujianame;
    private String fujiacount;
    private String comfujiacode;
	private String comfujianame;
    private String comfujiacount;
	private String comfujiaprice;
	private String price;
	private String fujiaprice;
	private String weight;
	private String weightflg;
	private String unit;
    private String unitCode;
	private boolean istc;
	private String over;
	private String urge;
	private String man;
	private String woman;
	private String send;//是否已调用接口发送  1代表已发送                0代表没有发送，暂存在数据库
	private String CLASS;//即起    1                 叫起 2
	private String cnt;
	private String isquit;//退菜标志（如果该菜是退的菜 0，正常返回 1）
	private String quitcause;//退菜原因
	private String rushorcall;//1 即起  2 叫起  0 即不即起又不叫起 
	private String eachprice;//单价
	private String overCount;//记录划菜数量
	
	private boolean isSelected;
	private String sortClass;//该小菜品属于哪个类别
	private String counts;
	private double subtotal;//小计
	private String presentCode;//赠送的菜品所代表的编码
	private String tcMoneyMode;//套餐的计价方式
	private boolean isSoldOut;//是否已沽清
    private String soldCnt;   //估清数量
    private String soldChangeCnt;//警告数量
	
	private String maxCnt;//某道套餐明细可选的最多数量
	private String minCnt;//某道套餐明细可选的最少数量
	private String defalutS;
	private String recommendCnt;//套餐推荐用的
	private double adJustPrice;//套餐加价
    private String dtlCount;//明细数量
    private Integer foodIndex=0;//中餐套餐明细索引/排序
    private Integer foodMark=0;//中餐不同套餐标识
    private List<String> chineseFujia;
    private Map<String,Unit> unitMap;
    private Integer fujiaModel=0;//是否有必选附加项标识，1 代表有 0代表没有
    private String unit2;//第二单位，西贝专用
    private String modifyFlag;//在全单页面是否修改了第二单位的菜品数量 ,N未修改   Y修改了,西贝专用
    private String isNormalCnt;//在全单页面判断是正常点菜，还是退菜 0.0
    private String unit2Cnt;//第二单位数量
    private int isTemp=0;//临时菜
    private String tempName;//临时菜名
    private String sublistid;//套餐标识
    private int plusd;//3.3第二单位标识 0为非第二单位
    private int state; //是否允许修改价格


//    public double getSoldCnt{
//        return soldCnt;
//    }
//    public void setSoldCnt(double soldCnt){
//        this.soldCnt=soldCnt;
//    }
    public String getSoldChangeCnt(){
        return soldChangeCnt;
    }
    public void  setSoldChangeCnt(String SoldChangeCnt){
        this.soldChangeCnt=SoldChangeCnt;
    }
    public String getSoldCnt(){
        return soldCnt;
    }
    public void  setSoldCnt(String soldCnt){
        this.soldCnt=soldCnt;
    }
    public int getState(){
        return state;
    }
    public void  setState(int state){
        this.state=state;
    }
    public String getSublistid() {
        return sublistid;
    }

    public void setSublistid(String sublistid) {
        this.sublistid = sublistid;
    }

    public int getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(int isTemp) {
        this.isTemp = isTemp;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getTabNum() {
        return tabNum;
    }

    public void setTabNum(String tabNum) {
        this.tabNum = tabNum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPKID() {
        return PKID;
    }

    public void setPKID(String PKID) {
        this.PKID = PKID;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getPcname() {
        return pcname;
    }

    public void setPcname(String pcname) {
        this.pcname = pcname;
    }

    public String getTpcode() {
        return tpcode;
    }

    public void setTpcode(String tpcode) {
        this.tpcode = tpcode;
    }

    public String getTpname() {
        return tpname;
    }

    public void setTpname(String tpname) {
        this.tpname = tpname;
    }

    public String getTpnum() {
        return tpnum;
    }

    public void setTpnum(String tpnum) {
        this.tpnum = tpnum;
    }

    public String getPcount() {
        return pcount;
    }

    public void setPcount(String pcount) {
        this.pcount = pcount;
    }

    public String getPromonum() {
        return promonum;
    }

    public void setPromonum(String promonum) {
        this.promonum = promonum;
    }

    public String getFujiacode() {
        return fujiacode;
    }

    public void setFujiacode(String fujiacode) {
        this.fujiacode = fujiacode;
    }

    public String getFujianame() {
        return fujianame;
    }

    public void setFujianame(String fujianame) {
        this.fujianame = fujianame;
    }

    public String getFujiacount() {
        return fujiacount;
    }

    public void setFujiacount(String fujiacount) {
        this.fujiacount = fujiacount;
    }

    public String getComfujiacode() {
        return comfujiacode;
    }

    public void setComfujiacode(String comfujiacode) {
        this.comfujiacode = comfujiacode;
    }

    public String getComfujianame() {
        return comfujianame;
    }

    public void setComfujianame(String comfujianame) {
        this.comfujianame = comfujianame;
    }

    public String getComfujiacount() {
        return comfujiacount;
    }

    public void setComfujiacount(String comfujiacount) {
        this.comfujiacount = comfujiacount;
    }

    public String getComfujiaprice() {
        return comfujiaprice;
    }

    public void setComfujiaprice(String comfujiaprice) {
        this.comfujiaprice = comfujiaprice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFujiaprice() {
        return fujiaprice;
    }

    public void setFujiaprice(String fujiaprice) {
        this.fujiaprice = fujiaprice;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightflg() {
        return weightflg;
    }

    public void setWeightflg(String weightflg) {
        this.weightflg = weightflg;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public boolean isIstc() {
        return istc;
    }

    public void setIstc(boolean istc) {
        this.istc = istc;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getUrge() {
        return urge;
    }

    public void setUrge(String urge) {
        this.urge = urge;
    }

    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public String getWoman() {
        return woman;
    }

    public void setWoman(String woman) {
        this.woman = woman;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getCLASS() {
        return CLASS;
    }

    public void setCLASS(String CLASS) {
        this.CLASS = CLASS;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getIsquit() {
        return isquit;
    }

    public void setIsquit(String isquit) {
        this.isquit = isquit;
    }

    public String getQuitcause() {
        return quitcause;
    }

    public void setQuitcause(String quitcause) {
        this.quitcause = quitcause;
    }

    public String getRushorcall() {
        return rushorcall;
    }

    public void setRushorcall(String rushorcall) {
        this.rushorcall = rushorcall;
    }

    public String getEachprice() {
        return eachprice;
    }

    public void setEachprice(String eachprice) {
        this.eachprice = eachprice;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getSortClass() {
        return sortClass;
    }

    public void setSortClass(String sortClass) {
        this.sortClass = sortClass;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getPresentCode() {
        return presentCode;
    }

    public void setPresentCode(String presentCode) {
        this.presentCode = presentCode;
    }

    public String getTcMoneyMode() {
        return tcMoneyMode;
    }

    public void setTcMoneyMode(String tcMoneyMode) {
        this.tcMoneyMode = tcMoneyMode;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }

    public String getDefalutS() {
        return defalutS;
    }

    public void setDefalutS(String defalutS) {
        this.defalutS = defalutS;
    }

    public String getRecommendCnt() {
        return recommendCnt;
    }

    public void setRecommendCnt(String recommendCnt) {
        this.recommendCnt = recommendCnt;
    }

    public double getAdJustPrice() {
        return adJustPrice;
    }

    public void setAdJustPrice(double adJustPrice) {
        this.adJustPrice = adJustPrice;
    }

    public String getDtlCount() {
        return dtlCount;
    }

    public void setDtlCount(String dtlCount) {
        this.dtlCount = dtlCount;
    }

    public Integer getFoodIndex() {
        return foodIndex;
    }

    public void setFoodIndex(Integer foodIndex) {
        this.foodIndex = foodIndex;
    }

    public Integer getFoodMark() {
        return foodMark;
    }

    public void setFoodMark(Integer foodMark) {
        this.foodMark = foodMark;
    }

    public List<String> getChineseFujia() {
        return chineseFujia;
    }

    public void setChineseFujia(List<String> chineseFujia) {
        this.chineseFujia = chineseFujia;
    }

    public Map<String, Unit> getUnitMap() {
        return unitMap;
    }

    public void setUnitMap(Map<String, Unit> unitMap) {
        this.unitMap = unitMap;
    }

    public String getUnit2() {
        return unit2;
    }

    public void setUnit2(String unit2) {
        this.unit2 = unit2;
    }

    public String getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(String modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    public String getIsNormalCnt() {
        return isNormalCnt;
    }

    public void setIsNormalCnt(String isNormalCnt) {
        this.isNormalCnt = isNormalCnt;
    }

    public String getUnit2Cnt() {
        return unit2Cnt;
    }

    public void setUnit2Cnt(String unit2Cnt) {
        this.unit2Cnt = unit2Cnt;
    }

    @Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flag) {

	}
	public String getOverCount() {
		return overCount;
	}
	public void setOverCount(String overCount) {
		this.overCount = overCount;
	}

	public static final Parcelable.Creator<Food> CREATOR = new Creator<Food>() {

		@Override
		public Food createFromParcel(Parcel source) {
			Food food = new Food();
			return food;
		}

		@Override
		public Food[] newArray(int size) {
			return new Food[size];
		}

	};


	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Food)){
			return false;
		}
		Food mFood = (Food) o;
		return this.pcode!=null?this.pcode.equals(mFood.pcode):mFood.pcode==null;
	}

    public Integer getFujiaModel() {
        return fujiaModel;
    }

    public void setFujiaModel(Integer fujiaModel) {
        this.fujiaModel = fujiaModel;
    }

    public int getPlusd() {
        return plusd;
    }

    public void setPlusd(int plusd) {
        this.plusd = plusd;
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
	@Override
	public String toString() {
		return "Food [id=" + id + ", tabNum=" + tabNum + ", orderId=" + orderId
				+ ", Time=" + Time + ", PKID=" + PKID + ", pcode=" + pcode
				+ ", pcname=" + pcname + ", tpcode=" + tpcode + ", tpname="
				+ tpname + ", tpnum=" + tpnum + ", pcount=" + pcount
				+ ", promonum=" + promonum + ", fujiacode=" + fujiacode
				+ ", fujianame=" + fujianame + ", price=" + price
				+ ", fujiaprice=" + fujiaprice + ", weight=" + weight
				+ ", weightflg=" + weightflg + ", unit=" + unit + ", istc="
				+ istc + ", over=" + over + ", urge=" + urge + ", man=" + man
				+ ", woman=" + woman + ", send=" + send + ", CLASS=" + CLASS
				+ ", cnt=" + cnt + ", isquit=" + isquit + ", quitcause="
				+ quitcause + ", rushorcall=" + rushorcall + ", eachprice="
				+ eachprice + ", overCount=" + overCount + ", isSelected="
				+ isSelected + ", sortClass=" + sortClass + ", counts="
				+ counts + ", subtotal=" + subtotal + ", presentCode="
				+ presentCode + ", tcMoneyMode=" + tcMoneyMode + ", isSoldOut="
				+ isSoldOut + ", maxCnt=" + maxCnt + ", minCnt=" + minCnt
				+ ", defalutS=" + defalutS + "]";
	}
}
