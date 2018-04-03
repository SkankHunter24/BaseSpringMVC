package com.model.vo;

public class TUser  implements java.io.Serializable{
    private String id;

    private String uName;

    private String uPassword;

    private String uClassId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuClassId() {
        return uClassId;
    }

    public void setuClassId(String uClassId) {
        this.uClassId = uClassId;
    }
}