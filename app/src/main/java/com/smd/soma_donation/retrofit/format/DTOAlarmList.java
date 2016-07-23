package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YS on 2016-07-17.
 */
public class DTOAlarmList {
    @SerializedName("status")
    boolean status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    List<DTOAlarmItem> data = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public DTOAlarmItem getData(int i)
    {
        return data.get(i);
    }

    public int getDatasize()
    {return  data.size();}

}
