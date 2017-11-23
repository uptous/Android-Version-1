package com.uptous.model;

/**
 * Created by RaviPrakash on 12-10-2017.
 */

public class CommunityTitleModel {

    /**
     * id : 28279
     * title : rr
     * communityId : 1003
     * createTime : 1507802178000
     * thumb : https://d28cm1pjlvuuvi.cloudfront.net/d0392a67ed9ba98e4eb3c89a14126a4a/dffr
     */

    private int id;
    private String title;
    private int communityId;
    private long createTime;
    private String thumb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
