package cn.com.choicesoft.bean.wait;

import java.util.List;

/**
 * 等位类别
 * M。c
 * 2015-04-25
 * Jnwsczh@163.com
 */
public class WaitType {
    private String vname;
    private String vinit;
    private String vcode;
    private List<Wait> waits;

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public List<Wait> getWaits() {
        return waits;
    }

    public void setWaits(List<Wait> waits) {
        this.waits = waits;
    }

    public String getVinit() {
        return vinit;
    }

    public void setVinit(String vinit) {
        this.vinit = vinit;
    }
}
