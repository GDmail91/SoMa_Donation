package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by YS on 2016-07-18.
 */
public class DTOMonthlyItem implements Serializable{
    @SerializedName("m_support_content_id")
    int content_id;
    @SerializedName("m_support_title")
    String content_title;
    @SerializedName("description")
    String description;
    @SerializedName("m_support_img")
    String content_img;
    @SerializedName("donation")
    long donation;
    @SerializedName("end_date")
    String end_date;
    @SerializedName("post_date")
    String post_date;

    public DTOMonthlyItem(int content_id, String content_title, String description, String content_img, long donation, String end_date, String post_date) {
        this.content_id = content_id;
        this.content_title = content_title;
        this.description = description;
        this.content_img = content_img;
        this.donation = donation;
        this.end_date = end_date;
        this.post_date = post_date;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getContent_title() {
        return content_title;
    }

    public void setContent_title(String content_title) {
        this.content_title = content_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent_img() {
        return content_img;
    }

    public void setContent_img(String content_img) {
        this.content_img = content_img;
    }

    public long getDonation() {
        return donation;
    }

    public void setDonation(long donation) {
        this.donation = donation;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
}
