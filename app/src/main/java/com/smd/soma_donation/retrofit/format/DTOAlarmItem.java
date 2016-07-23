package com.smd.soma_donation.retrofit.format;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by YS on 2016-07-17.
 */
public class DTOAlarmItem implements Serializable {
    @SerializedName("alarm_id")
    int alarmId;
    @SerializedName("alarm_user_id")
    int alarmUserId;
    @SerializedName("alarm_category")
    String alarmCategory;
    @SerializedName("alarm_content_id")
    int alarmContentId;
    @SerializedName("content_title")
    String contentTitle;
    @SerializedName("description")
    String description;
    @SerializedName("content_img")
    String contentImg;
    @SerializedName("alarm_date")
    String alarmDate;
    @SerializedName("is_check")
    short isCheck;

    public DTOAlarmItem(int alarmId, int alarmUserId, String alarmCategory, int alarmContentId, String contentTitle, String description, String contentImg, String alarmDate, short isCheck) {
        this.alarmId = alarmId;
        this.alarmUserId = alarmUserId;
        this.alarmCategory = alarmCategory;
        this.alarmContentId = alarmContentId;
        this.contentTitle = contentTitle;
        this.description = description;
        this.contentImg = contentImg;
        this.alarmDate = alarmDate;
        this.isCheck = isCheck;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public int getAlarmUserId() {
        return alarmUserId;
    }

    public void setAlarmUserId(int alarmUserId) {
        this.alarmUserId = alarmUserId;
    }

    public String getAlarmCategory() {
        return alarmCategory;
    }

    public void setAlarmCategory(String alarmCategory) {
        this.alarmCategory = alarmCategory;
    }

    public int getAlarmContentId() {
        return alarmContentId;
    }

    public void setAlarmContentId(int alarmContentId) {
        this.alarmContentId = alarmContentId;
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

    public String getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
    }

    public short isCheck() {
        return isCheck;
    }

    public void setCheck(short isCheck) {
        this.isCheck = isCheck;
    }
}
