package cn.com.choicesoft.chinese.util;

import android.widget.LinearLayout;
import cn.com.choicesoft.chinese.bean.TicketData;
import cn.com.choicesoft.chinese.bean.VipData;
import cn.com.choicesoft.util.ValueUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员工具类
 * Created by M.c on 2014/9/10.
 */
public class ChineseVipUtil {
    /**
     * 会员数据解析
     * @param result
     * @return
     */
    public VipData getVipDataByXml(String result){
        /*卡ID @卡号@卡姓名@手机号@有效期@卡状态@卡类别@卡余额@卡积分余额@本金@赠送@手续费@退卡金额@电子券列表
        电子券列表组成如下：
        电子券列表：券唯一编码1,券类型编码1,券名称1,面额1,有效期1#券唯一编码2,券类型编码2,券名称2,面额2,有效期2...*/
        String[] rs=ValueUtil.isEmpty(result)?null:result.split("@");
        if(ValueUtil.isNotEmpty(rs)) {
            VipData vipData = new VipData();
            vipData.setCardId(rs[0]);
            vipData.setCardNumber(rs[1]);
            vipData.setCardName(rs[2]);
            vipData.setPhone(rs[3]);
            vipData.setExpDate(rs[4]);
            vipData.setCardState(rs[5]);
            vipData.setCardClass(rs[6]);
            vipData.setCardYuE(rs[7]);
            vipData.setJiFenYuE(rs[8]);
            vipData.setBenJin(rs[9]);
            vipData.setGive(rs[10]);
            vipData.setShouXuFei(rs[11]);
            vipData.setTuiKaJinE(rs[12]);
            if(rs.length>13){
                String[] ts=rs[13].split("#");
                if(ts!=null) {
                    List<TicketData> list=new ArrayList<TicketData>();
                    for (String t:ts){
                        String [] d=t.split(",");
                        if(d!=null){
                            TicketData ticketData=new TicketData();
                            ticketData.setTicketId(d[0]);
                            ticketData.setTicketClassId(d[1]);
                            ticketData.setTicketName(d[2]);
                            ticketData.setMianE(d[3]);
                            ticketData.setExpDate(d[4]);
                            list.add(ticketData);
                        }
                    }
                    vipData.setList(list);
                }
            }
            return vipData;
        }
        return null;
    }
}
