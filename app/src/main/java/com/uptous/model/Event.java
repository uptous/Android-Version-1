package com.uptous.model;

/**
 * Created by Prakash on 12/30/2016.
 */

public class Event {
    /**
     * title : Eyes ON
     * description : This is a test event with short description.
     * location :
     * startTime : 1483113600000
     * endTime : 1483117200000
     * allDay : false
     * id : 134076
     * repeatFrequency : Weekly
     * repeatTo : 1493362800000
     * communityId : 496
     */

    private String title;
    private String description;
    private String location;
    private long startTime;
    private long endTime;
    private boolean allDay;
    private int id;
    private String repeatFrequency;
    private long repeatTo;
    private int communityId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(String repeatFrequency) {
        this.repeatFrequency = repeatFrequency;
    }

    public long getRepeatTo() {
        return repeatTo;
    }

    public void setRepeatTo(long repeatTo) {
        this.repeatTo = repeatTo;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }
}
