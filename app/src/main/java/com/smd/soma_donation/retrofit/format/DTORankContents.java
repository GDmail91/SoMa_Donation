package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YS on 2016-07-17.
 */
public class DTORankContents {
    @SerializedName("status")
    boolean status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    List<DTORankItem> data = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public DTORankItem getData(int i)
    {
        return data.get(i);
    }

    public int getDatasize()
    {return  data.size();}

}
