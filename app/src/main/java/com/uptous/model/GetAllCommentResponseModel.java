package com.uptous.model;

/**
 * Created by Prakash .
 */

public class GetAllCommentResponseModel {
    /**
     * commentId : 3252
     * referenceType : Photos
     * referenceId : 26848
     * communityId : 352
     * ownerId : 1334
     * ownerName : Testp1 testp1
     * ownerPhotoUrl : https://d12h6ivzrhy8zy.cloudfront.net/77f84799-e3e1-4b83-967c-06d39f32b644dXNlcklkPTEzMzQ=
     * ownerBackgroundColor : #ffdab3
     * ownerTextColor : #000000
     * createDate : 1483386690000
     * modifiedDate : 1483386690000
     * body : Great keyboard
     */

    private int commentId;
    private String referenceType;
    private int referenceId;
    private int communityId;
    private int ownerId;
    private String ownerName;
    private String ownerPhotoUrl;
    private String ownerBackgroundColor;
    private String ownerTextColor;
    private long createDate;
    private long modifiedDate;
    private String body;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
