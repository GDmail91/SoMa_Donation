package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by YS on 2016-07-17.
 */
public class DTOPostResponse {
    @SerializedName("status")
    boolean status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    JSONObject data;

    public boolean isStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject getData()
    {
        return data;
    }

    public int getDatasize()
    {return  data.length();}

}
