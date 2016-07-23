package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YS on 2016-07-22.
 */
public class DTOReview {
    @SerializedName("status")
    boolean status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    ArrayList<DTOReviewItem> data = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public DTOReviewItem getData(int i)
    {
        return data.get(i);
    }

    public int getDatasize()
    {return  data.size();}
}
