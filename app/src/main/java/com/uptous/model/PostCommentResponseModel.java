package com.uptous.model;

/**
 * Created by Prakash .
 */

public class PostCommentResponseModel {

    /**
     * albumId : 28604
     */

    private String albumId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public int getAlbumId() {

        return Integer.parseInt(albumId);
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
