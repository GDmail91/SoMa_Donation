package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YS on 2016-07-18.
 */
public class DTORankDetail {
    @SerializedName("status")
    boolean status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    DTORankItem data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DTORankItem getData() {
        return data;
    }

    public void setData(DTORankItem data) {
        this.data = data;
    }
}
