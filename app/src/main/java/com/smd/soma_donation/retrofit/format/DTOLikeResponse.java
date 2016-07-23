package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YS on 2016-07-17.
 */
public class DTOLikeResponse {
    @SerializedName("status")
    boolean status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    DTOLikeData data;

    public boolean isStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean getData()
    {
        return data.isLike();
    }

    class DTOLikeData{
        @SerializedName("is_like")
        boolean isLike;

        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean status) {
            this.isLike = status;
        }
    }
}


