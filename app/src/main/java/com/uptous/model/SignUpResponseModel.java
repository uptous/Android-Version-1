package com.uptous.model;

/**
 * Created by Prakash.
 */

public class SignUpResponseModel {


    /**
     * contact :
     * contact2 :
     * dateTime : 1484276400000
     * endTime : 9:00PM
     * location : Eichler Swim and Tennis Club
     * name : 9th Grade Parent Network Social
     * notes : Hi Everyone,
     Our 2nd social for the year is next Thursday night, January 12 at the Eichler.  Not tonight!
     Please RSVP using the potluck sign-up attached.  Feel free to bring a little something for the potluck or just come by (see the last line).
     Thanks!
     -Lili, Annie, Barbara, Xin & Colleen
     * id : 19731
     * type : Potluck/Party
     * opportunityStatus : Active
     * communityId : 5420
     * createdByUserId : 38360
     * createDate : 1483650419000
     * sortOrder : 2
     * organizer1Id : 14976
     * organizer1PhotoUrl : https://dsnn35vlkp0h4.cloudfront.net/images/blank_image.gif
     * organizer1BackgroundColor : #330066
     * organizer1TextColor : #ffffff
     */

    private String contact;
    private String contact2;
    private long dateTime;
    private String endTime;
    private String location;
    private String name;
    private String notes;
    private int id;
    private String type;
    private String opportunityStatus;
    private int communityId;
    private int createdByUserId;
    private long createDate;
    private int sortOrder;
    private int organizer1Id;
    private String organizer1PhotoUrl;
    private String organizer1BackgroundColor;
    private String organizer1TextColor;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpportunityStatus() {
        return opportunityStatus;
    }

    public void setOpportunityStatus(String opportunityStatus) {
        this.opportunityStatus = opportunityStatus;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getOrganizer1Id() {
        return organizer1Id;
    }

    public void setOrganizer1Id(int organizer1Id) {
        this.organizer1Id = organizer1Id;
    }

    public String getOrganizer1PhotoUrl() {
        return organizer1PhotoUrl;
    }

    public void setOrganizer1PhotoUrl(String organizer1PhotoUrl) {
        this.organizer1PhotoUrl = organizer1PhotoUrl;
    }

    public String getOrganizer1BackgroundColor() {
        return organizer1BackgroundColor;
    }

    public void setOrganizer1BackgroundColor(String organizer1BackgroundColor) {
        this.organizer1BackgroundColor = organizer1BackgroundColor;
    }

    public String getOrganizer1TextColor() {
        return organizer1TextColor;
    }

    public void setOrganizer1TextColor(String organizer1TextColor) {
        this.organizer1TextColor = organizer1TextColor;
    }
}
