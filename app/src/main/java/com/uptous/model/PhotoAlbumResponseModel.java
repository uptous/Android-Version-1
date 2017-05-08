package com.uptous.model;

/**
 * Created by Prakash .
 */

public class PhotoAlbumResponseModel {
    /**
     * id : 26848
     * title : gdh
     * communityId : 3252
     * createTime : 1482938370000
     * thumb : https://d28cm1pjlvuuvi.cloudfront.net/d8b22aaeebfb823aa0a2dce0742e6234/defaultMobileImage
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
