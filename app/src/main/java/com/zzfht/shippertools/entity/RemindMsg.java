package com.zzfht.shippertools.entity;

/**
 * Created by think on 2015-12-18.
 */
public class RemindMsg {

    private int id;
    private String infoId;
    private String tittle;
    private String content;
    private String pushDate;
    private String phone;
    private String type;
    private String isRead;

    public RemindMsg() {
    }

    public RemindMsg(String tittle, String content, String pushDate, String phone, String type, String infoId) {
        this.tittle = tittle;
        this.content = content;
        this.pushDate = pushDate;
        this.phone = phone;
        this.type = type;
        this.infoId = infoId;
    }

    public RemindMsg(int id, String tittle, String content, String pushDate, String phone, String type, String isRead) {
        this.id = id;
        this.tittle = tittle;
        this.content = content;
        this.pushDate = pushDate;
        this.phone = phone;
        this.type = type;
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPushDate() {
        return pushDate;
    }

    public void setPushDate(String pushDate) {
        this.pushDate = pushDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
