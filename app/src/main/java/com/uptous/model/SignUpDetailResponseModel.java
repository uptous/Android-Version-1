package com.uptous.model;

import java.util.List;

/**
 * Created by Prakash.
 */

public class SignUpDetailResponseModel {


    /**
     * contact : Testp1 testp1
     * contact2 : testp2 testp2
     * dateTime : 1487149200000
     * endTime : 1:00AM
     * location :
     * name : International Night
     * notes : khfsd hk sadfkjh kjhdsakljh
     sadkfh sdajh k
     * id : 19382
     * type : Shifts
     * opportunityStatus : Active
     * communityId : 9
     * createdByUserId : 2
     * organizer1Id : 2
     * organizer1PhotoUrl : https://d12h6ivzrhy8zy.cloudfront.net/48b640ef-3284-4de1-806a-62f200b8958ddXNlcklkPTI=
     * organizer1BackgroundColor : #ffdab3
     * organizer1TextColor : #000000
     * organizer2Id : 3
     * organizer2PhotoUrl : https://dsnn35vlkp0h4.cloudfront.net/images/blank_image.gif
     * organizer2BackgroundColor : #e5fff2
     * organizer2TextColor : #000000
     * createDate : 1478189888000
     * sortOrder : 2
     * items : [{"id":194966,"dateTime":1487170800000,"endTime":"8:00AM","name":"Set Up","numVolunteers":5,"VolunteerCount":1,"VolunteerStatus":"Volunteered","volunteers":[{"firstName":"Testp1 testp1","phone":"Yyyyyy","attendees":1,"hours":0}]},{"id":194967,"dateTime":1487174400000,"endTime":"10:00AM","name":"Serve Food","numVolunteers":10,"VolunteerCount":1,"VolunteerStatus":"Open","volunteers":[{"firstName":"Anna Smith","phone":"With a message","attendees":1}]},{"id":194968,"dateTime":1487181600000,"endTime":"11:00AM","name":"Cleanup","VolunteerCount":2,"VolunteerStatus":"Volunteered","volunteers":[{"firstName":"Anna Smith","attendees":1},{"firstName":"Testp1 testp1","attendees":1}]}]
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
    private int organizer1Id;
    private String organizer1PhotoUrl;
    private String organizer1BackgroundColor;
    private String organizer1TextColor;
    private int organizer2Id;
    private String organizer2PhotoUrl;
    private String organizer2BackgroundColor;
    private String organizer2TextColor;
    private long createDate;
    private int sortOrder;
    private List<ItemsBean> items;

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

    public int getOrganizer2Id() {
        return organizer2Id;
    }

    public void setOrganizer2Id(int organizer2Id) {
        this.organizer2Id = organizer2Id;
    }

    public String getOrganizer2PhotoUrl() {
        return organizer2PhotoUrl;
    }

    public void setOrganizer2PhotoUrl(String organizer2PhotoUrl) {
        this.organizer2PhotoUrl = organizer2PhotoUrl;
    }

    public String getOrganizer2BackgroundColor() {
        return organizer2BackgroundColor;
    }

    public void setOrganizer2BackgroundColor(String organizer2BackgroundColor) {
        this.organizer2BackgroundColor = organizer2BackgroundColor;
    }

    public String getOrganizer2TextColor() {
        return organizer2TextColor;
    }

    public void setOrganizer2TextColor(String organizer2TextColor) {
        this.organizer2TextColor = organizer2TextColor;
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

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * id : 194966
         * dateTime : 1487170800000
         * endTime : 8:00AM
         * name : Set Up
         * numVolunteers : 5
         * VolunteerCount : 1
         * VolunteerStatus : Volunteered
         * volunteers : [{"firstName":"Testp1 testp1","phone":"Yyyyyy","attendees":1,"hours":0}]
         */

        private int id;
        private long dateTime;
        private String endTime;
        private String name;
        private int numVolunteers;
        private int VolunteerCount;
        private String VolunteerStatus;
        private List<VolunteersBean> volunteers;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumVolunteers() {
            return numVolunteers;
        }

        public void setNumVolunteers(int numVolunteers) {
            this.numVolunteers = numVolunteers;
        }

        public int getVolunteerCount() {
            return VolunteerCount;
        }

        public void setVolunteerCount(int VolunteerCount) {
            this.VolunteerCount = VolunteerCount;
        }

        public String getVolunteerStatus() {
            return VolunteerStatus;
        }

        public void setVolunteerStatus(String VolunteerStatus) {
            this.VolunteerStatus = VolunteerStatus;
        }

        public List<VolunteersBean> getVolunteers() {
            return volunteers;
        }

        public void setVolunteers(List<VolunteersBean> volunteers) {
            this.volunteers = volunteers;
        }

        public static class VolunteersBean {
            /**
             * firstName : Testp1 testp1
             * phone : Yyyyyy
             * attendees : 1
             * hours : 0.0
             */

            private String firstName;
            private String phone;
            private int attendees;
            private double hours;

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            private String comment;

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getAttendees() {
                return attendees;
            }

            public void setAttendees(int attendees) {
                this.attendees = attendees;
            }

            public double getHours() {
                return hours;
            }

            public void setHours(double hours) {
                this.hours = hours;
            }
        }
    }
}
