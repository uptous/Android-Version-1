package com.uptous.model;

/**
 * Created by Prakash .
 */

public class InvitationResponseModel {
    /**
     * invitationId : 172070
     * communityId : 5845
     * communityName : New French Community
     */

    private int invitationId;
    private int communityId;
    private String communityName;

    public int getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(int invitationId) {
        this.invitationId = invitationId;
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
}
