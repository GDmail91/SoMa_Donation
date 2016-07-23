package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YS on 2016-07-22.
 */
public class DTOReviewItem {
    @SerializedName("review_id")
    int review_id;
    @SerializedName("review_support_id")
    int review_support_id;
    @SerializedName("review_title")
    String review_title;
    @SerializedName("banner_img")
    String banner_img;
    @SerializedName("review_content")
    String review_content;
    @SerializedName("donation")
    long donation;
    @SerializedName("post_date")
    String post_date;

    public DTOReviewItem(int review_id, int review_support_id,String review_title, String banner_img, String review_content, long donation, String post_date) {
        this.review_id = review_id;
        this.review_support_id = review_support_id;
        this.review_title = review_title;
        this.banner_img = banner_img;
        this.review_content = review_content;
        this.donation = donation;
        this.post_date = post_date;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public int getReview_support_id() {
        return review_support_id;
    }

    public void setReview_support_id(int review_support_id) {
        this.review_support_id = review_support_id;
    }

    public String getReview_title() {
        return review_title;
    }

    public void setReview_title(String review_title) {
        this.review_title = review_title;
    }

    public String getBanner_img() {
        return banner_img;
    }

    public void setBanner_img(String banner_img) {
        this.banner_img = banner_img;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public long getDonation() {
        return donation;
    }

    public void setDonation(long donation) {
        this.donation = donation;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
}
