package cn.com.choicesoft.chinese.bean;

import java.io.Serializable;

/**
 * ����ȯ��Ϣ
 * Created by M.c on 2014/9/10.
 */
public class TicketData implements Serializable{
    private String ticketId;//ȯΨһ����1
    private String ticketClassId;//ȯ���ͱ���1
    private String ticketName;//ȯ����1
    private String mianE;//���1
    private String expDate;//��Ч��1

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketClassId() {
        return ticketClassId;
    }

    public void setTicketClassId(String ticketClassId) {
        this.ticketClassId = ticketClassId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getMianE() {
        return mianE;
    }

    public void setMianE(String mianE) {
        this.mianE = mianE;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
}
