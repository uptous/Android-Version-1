package com.uptous.model;

/**
 * Created by Prakash on 1/10/2017.
 */

public class CommentRequestModel {

    public CommentRequestModel(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    private String contents;
}
