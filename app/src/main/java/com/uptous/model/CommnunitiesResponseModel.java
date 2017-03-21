package com.uptous.model;

/**
 * Created by Prakash.
 */

public class CommnunitiesResponseModel {
    /**
     * address : 878 ash
     * city : Santa Clara
     * state : CA
     * zipCode : 99999
     * country : United States of America
     * description : This is 4th grade at St. Lawrence
     * emailAdminOnly : false
     * name : 4th grade
     * id : 525
     */

    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String description;
    private boolean emailAdminOnly;
    private String name;
    private int id;

    public void setId(String id) {
        Id = id;
    }

    private String Id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEmailAdminOnly() {
        return emailAdminOnly;
    }

    public void setEmailAdminOnly(boolean emailAdminOnly) {
        this.emailAdminOnly = emailAdminOnly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
