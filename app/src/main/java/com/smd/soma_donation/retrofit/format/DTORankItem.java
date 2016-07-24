package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by YS on 2016-07-17.
 */
public class DTORankItem implements Serializable {
    @SerializedName("content_id")
    int contentId;
    @SerializedName("content_user_id")
    long contentUserId;
    @SerializedName("content_title")
    String contentTitle;
    @SerializedName("description")
    String description;
    @SerializedName("content_img")
    String contentImg;
    @SerializedName("like_count")
    int likeCount;
    @SerializedName("view_count")
    int viewCount;
    @SerializedName("post_date")
    String postDate;
    @SerializedName("is_like")
    short isLike;
    @SerializedName("is_finish")
    short isFinish;

    public DTORankItem(int id, int rank, String title, int likeCount, int viewCount){
        this.contentId = id;
        // TODO contentRank 로 변경
        this.contentId = rank;
        this.contentTitle = title;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public long getContentUserId() {
        return contentUserId;
    }

    public void setContentUserId(int contentUserId) {
        this.contentUserId = contentUserId;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentImg() {
        return contentImg;
    }

    public void setContentImg(String contentImg) {
        this.contentImg = contentImg;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public short isLike() {
        return isLike;
    }

    public void setIsLike(short isLike) {
        this.isLike = isLike;
    }

    public short isFinish() {
        return isFinish;
    }

    public void setFinish(short isFinish) {
        this.isFinish = isFinish;
    }
}
