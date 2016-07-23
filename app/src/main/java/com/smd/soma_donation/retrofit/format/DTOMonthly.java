package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YS on 2016-07-18.
 */
public class DTOMonthly {
    @SerializedName("status")
    boolean status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    DTOMonthlyItem data;

    public boolean isStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public DTOMonthlyItem getData()
    {
        return data;
    }
}
