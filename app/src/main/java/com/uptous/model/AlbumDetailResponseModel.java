package com.uptous.model;

/**
 * Created by Prakash on 1/12/2017.
 */

public class AlbumDetailResponseModel {
    /**
     * id : 580563
     * photo : https://d2vlu9rz6cbwhy.cloudfront.net/8995138b053fd709b7f6b10bab2b293b/defaultMobileImage
     * caption : Image
     * createTime : 1484200685000
     */

    private int id;
    private String photo;
    private String caption;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
