package com.smd.soma_donation.review;

/**
 * Created by YS on 2016-07-13.
 */
public class ReviewItem {
    private int id;
    private int reviewNumber;
    private String reviewTitle;
    private int reviewDonate;
    private String reviewDate;

    public ReviewItem(int id, int reviewNumber, String reviewTitle, int reviewDonate, String reviewDate) {
        this.id = id;
        this.reviewNumber = reviewNumber;
        this.reviewTitle = reviewTitle;
        this.reviewDonate = reviewDonate;
        this.reviewDate = reviewDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewNumber() {
        return reviewNumber;
    }

    public void setReviewNumber(int reviewNumber) {
        this.reviewNumber = reviewNumber;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public int getReviewDonate() {
        return reviewDonate;
    }

    public void setReviewDonate(int reviewDonate) {
        this.reviewDonate = reviewDonate;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
}
