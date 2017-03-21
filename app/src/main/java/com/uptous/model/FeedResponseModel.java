package com.uptous.model;

import java.util.List;

/**
 * Created by Prakash.
 */

public class FeedResponseModel {
    /**
     * feedId : 1801882
     * communityId : 351
     * ownerId : 2
     * ownerName : Testp1 testp1
     * ownerEmail : Testp1@uptous.com
     * ownerPhotoUrl : https://d12h6ivzrhy8zy.cloudfront.net/abdc7125-b54b-493e-9caf-ab7ed570aa99dXNlcklkPTI=
     * ownerBackgroundColor : #ffdab3
     * ownerTextColor : #000000
     * createDate : 1483575735000
     * modifiedDate : 1483575829000
     * communityName : Class B
     * communityLogoUrl : https://d3jayiz76fhr9f.cloudfront.net/422287ae-cf9b-438c-8838-79993fe56e07Y29tbXVuaXR5SWQ9MzUx
     * communityBackgroundColor : #c609ec
     * communityTextColor : #ffffff
     * newsType : Opportunity
     * newsItemId : 19723
     * newsItemIndex : 0
     * newsItemName : New SHIFTS
     * newsItemDescription : Yes, Yes
     * newsItemUrl : https://www.uptous.com/uptous.htm?_flowId=directLink-flow&communityId=351&action=opp&extraId=19723
     * newsItemPhoto :
     * comments : []
     */

    private int feedId;
    private int communityId;
    private int ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerPhotoUrl;
    private String ownerBackgroundColor;
    private String ownerTextColor;
    private long createDate;
    private long modifiedDate;
    private String communityName;
    private String communityLogoUrl;
    private String communityBackgroundColor;
    private String communityTextColor;
    private String newsType;
    private int newsItemId;
    private int newsItemIndex;
    private String newsItemName;
    private String newsItemDescription;
    private String newsItemUrl;
    private String newsItemPhoto;
    private List<?> comments;

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
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

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
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

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityLogoUrl() {
        return communityLogoUrl;
    }

    public void setCommunityLogoUrl(String communityLogoUrl) {
        this.communityLogoUrl = communityLogoUrl;
    }

    public String getCommunityBackgroundColor() {
        return communityBackgroundColor;
    }

    public void setCommunityBackgroundColor(String communityBackgroundColor) {
        this.communityBackgroundColor = communityBackgroundColor;
    }

    public String getCommunityTextColor() {
        return communityTextColor;
    }

    public void setCommunityTextColor(String communityTextColor) {
        this.communityTextColor = communityTextColor;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public int getNewsItemId() {
        return newsItemId;
    }

    public void setNewsItemId(int newsItemId) {
        this.newsItemId = newsItemId;
    }

    public int getNewsItemIndex() {
        return newsItemIndex;
    }

    public void setNewsItemIndex(int newsItemIndex) {
        this.newsItemIndex = newsItemIndex;
    }

    public String getNewsItemName() {
        return newsItemName;
    }

    public void setNewsItemName(String newsItemName) {
        this.newsItemName = newsItemName;
    }

    public String getNewsItemDescription() {
        return newsItemDescription;
    }

    public void setNewsItemDescription(String newsItemDescription) {
        this.newsItemDescription = newsItemDescription;
    }

    public String getNewsItemUrl() {
        return newsItemUrl;
    }

    public void setNewsItemUrl(String newsItemUrl) {
        this.newsItemUrl = newsItemUrl;
    }

    public String getNewsItemPhoto() {
        return newsItemPhoto;
    }

    public void setNewsItemPhoto(String newsItemPhoto) {
        this.newsItemPhoto = newsItemPhoto;
    }

    public List<?> getComments() {
        return comments;
    }

    public void setComments(List<?> comments) {
        this.comments = comments;
    }
}
