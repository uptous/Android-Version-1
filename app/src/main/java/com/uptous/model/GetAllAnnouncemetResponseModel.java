package com.uptous.model;

/**
 * Created by Prakash on 2/16/2017.
 */

public class GetAllAnnouncemetResponseModel {
    /**
     * body : Testing
     * createTime : 1486962620000
     * createdByUserId : 3
     * createdByUserName : testp2 testp2
     * ownerPhotoUrl : https://d12h6ivzrhy8zy.cloudfront.net/322527dd-c9bf-48f5-83ef-bfccfa8fd0c0dXNlcklkPTM=
     * ownerBackgroundColor : #e5fff2
     * ownerTextColor : #000000
     */

    private String body;
    private long createTime;
    private int createdByUserId;
    private String createdByUserName;
    private String ownerPhotoUrl;
    private String ownerBackgroundColor;
    private String ownerTextColor;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public String getOwnerPhotoUrl() {
        return ownerPhotoUrl;
    }

    public void setOwnerPhotoUrl(String ownerPhotoUrl) {
        this.ownerPhotoUrl = ownerPhotoUrl;
    }

    public String getOwnerBackgroundColor() {
        return ownerBackgroundColor;
    }

    public void setOwnerBackgroundColor(String ownerBackgroundColor) {
        this.ownerBackgroundColor = ownerBackgroundColor;
    }

    public String getOwnerTextColor() {
        return ownerTextColor;
    }

    public void setOwnerTextColor(String ownerTextColor) {
        this.ownerTextColor = ownerTextColor;
    }
}
