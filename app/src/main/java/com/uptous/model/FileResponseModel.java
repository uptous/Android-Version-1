package com.uptous.model;

/**
 * Created by Prakash .
 */

public class FileResponseModel {
    /**
     * id : 30776
     * communityId : 351
     * communityName : Class B
     * title : Social Problem Solvers 2.pdf
     * path : https://d2kwux309fn5li.cloudfront.net/099dadaa3b664ebb930e2935541ee90b/SocialProblemSolvers2.pdf
     * createDate : 1479172241000
     */

    private int id;
    private int communityId;
    private String communityName;
    private String title;
    private String path;
    private long createDate;
    private  String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
