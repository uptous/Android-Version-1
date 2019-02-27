package com.uptous.model;

import java.util.List;

/**
 * Created by Prakash.
 */

public class ContactListResponseModel {
    /**
     * firstName : Sarah
     * lastName : Anderson
     * zipcode : 94301
     * email : sandersonutu@uptous.com
     * website :
     * phone :
     * photo : https://dsnn35vlkp0h4.cloudfront.net/images/blank_image.gif
     * memberBackgroundColor : #ffffcc
     * memberTextColor : #000000
     * children : [{"firstName":"Maya Anderson","id":38132}]
     * communities : [{"name":"Art & Crafts Daycare","id":319},{"name":"Ms. Gill 16/17","id":315},{"name":"Redwood Alumni","id":317},{"name":"Redwood Parents 16/17","id":316},{"name":"Scout Troop 168","id":320},{"name":"Smith Family & Friends","id":318}]
     * address : 1200 South Liberty Ave
     * city : Sunnyvale
     * state : CA
     * country : United States of America
     * mobile : 650-269-8022
     * spouce :
     */

    private String firstName;
    private String lastName;
    private String zipcode;
    private String email;
    private String website;
    private String phone;
    private String photo;
    private String memberBackgroundColor;
    private String memberTextColor;
    private String address;
    private String city;
    private String state;
    private String country;
    private String mobile;
    private String spouce;
    private List<ChildrenBean> children;
    private List<CommunitiesBean> communities;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMemberBackgroundColor() {
        return memberBackgroundColor;
    }

    public void setMemberBackgroundColor(String memberBackgroundColor) {
        this.memberBackgroundColor = memberBackgroundColor;
    }

    public String getMemberTextColor() {
        return memberTextColor;
    }

    public void setMemberTextColor(String memberTextColor) {
        this.memberTextColor = memberTextColor;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSpouce() {
        return spouce;
    }

    public void setSpouce(String spouce) {
        this.spouce = spouce;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public List<CommunitiesBean> getCommunities() {
        return communities;
    }

    public void setCommunities(List<CommunitiesBean> communities) {
        this.communities = communities;
    }

    public static class ChildrenBean {
        /**
         * firstName : Maya Anderson
         * id : 38132
         */

        private String firstName;
        private int id;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class CommunitiesBean {
        /**
         * name : Art & Crafts Daycare
         * id : 319
         */

        private String name;
        private int id;

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


//    /________________________________________________old Response________________________________/
//    /**
//     * firstName : Anne
//     * lastName : Smith
//     * address : 152 Main Street
//     * city : Palo Alto
//     * state : CA
//     * zipcode : 94301
//     * country : United States of America
//     * email : asmithutu@gmail.com
//     * website :
//     * phone : (650)-351-5821
//     * mobile : 650-555-0831
//     * photo : https://d12h6ivzrhy8zy.cloudfront.net/0481fda9-bea3-4c40-81ee-58822c41f60bdXNlcklkPTEzMzQ=
//     * memberBackgroundColor : #ffdab3
//     * memberTextColor : #000000
//     * children : [{"firstName":"Daniel Smith","id":226},{"firstName":"Melanie Smith","id":225}]
//     * communities : [{"name":"aa","id":495},{"name":"Art & Crafts Daycare","id":319},{"name":"AYSO Soccer Team","id":3121},{"name":"Baseball2","id":496},{"name":"Bolder PTA","id":3252},{"name":"Classroom B-8","id":486},{"name":"Jordan Training","id":9},{"name":"Mr. Hall 16/17","id":5314},{"name":"Ms. Carol - Kinder","id":2132},{"name":"Ms. Gill 16/17","id":315},{"name":"Palo Alto Middle School","id":1036},{"name":"Redwood Alumni","id":317},{"name":"Redwood Parents 16/17","id":316},{"name":"Scout Troop 168","id":320},{"name":"Smith Family & Friends","id":318},{"name":"Summer Soccer","id":518},{"name":"Superstarters","id":528},{"name":"Xinlei Test Community","id":5192},{"name":"YMCA midpen - Bball U7B9","id":324},{"name":"YouthA","id":356}]
//     */
//
//    private String firstName;
//    private String lastName;
//    private String address;
//    private String city;
//    private String state;
//    private String zipcode;
//    private String country;
//    private String email;
//    private String website;
//    private String phone;
//    private String mobile;
//    private String photo;
//    private String memberBackgroundColor;
//    private String memberTextColor;
//    private List<ChildrenBean> children;
//    private List<CommunitiesBean> communities;
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public String getZipcode() {
//        return zipcode;
//    }
//
//    public void setZipcode(String zipcode) {
//        this.zipcode = zipcode;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getWebsite() {
//        return website;
//    }
//
//    public void setWebsite(String website) {
//        this.website = website;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }
//
//    public String getMemberBackgroundColor() {
//        return memberBackgroundColor;
//    }
//
//    public void setMemberBackgroundColor(String memberBackgroundColor) {
//        this.memberBackgroundColor = memberBackgroundColor;
//    }
//
//    public String getMemberTextColor() {
//        return memberTextColor;
//    }
//
//    public void setMemberTextColor(String memberTextColor) {
//        this.memberTextColor = memberTextColor;
//    }
//
//    public List<ChildrenBean> getChildren() {
//        return children;
//    }
//
//    public void setChildren(List<ChildrenBean> children) {
//        this.children = children;
//    }
//
//    public List<CommunitiesBean> getCommunities() {
//        return communities;
//    }
//
//    public void setCommunities(List<CommunitiesBean> communities) {
//        this.communities = communities;
//    }
//
//    public static class ChildrenBean {
//        /**
//         * firstName : Daniel Smith
//         * id : 226
//         */
//
//        private String firstName;
//        private int id;
//
//        public String getFirstName() {
//            return firstName;
//        }
//
//        public void setFirstName(String firstName) {
//            this.firstName = firstName;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//    }
//
//    public static class CommunitiesBean {
//        /**
//         * name : aa
//         * id : 495
//         */
//
//        private String name;
//        private int id;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//    }
}
