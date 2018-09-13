package com.uptous.model;

import java.util.List;

/**
 * Created by Prakash on 10/18/2016.
 */

public class FeaturedProductResponseModel {


    private List<WeekdayBean> Weekday;

    public List<WeekdayBean> getWeekday() {
        return Weekday;
    }

    public void setWeekday(List<WeekdayBean> Weekday) {
        this.Weekday = Weekday;
    }

    public static class WeekdayBean {
        /**
         * name : FEATURED PRODUCTS
         * productmodel : Fastrack Watch
         * productname : Smartwatch
         * price : US $80/Piece
         * quantity : QTY:1
         * image : http://img1.10bestmedia.com/static/img/placeholder-shopping.jpg
         * imageuser : http://img1.10bestmedia.com/static/img/placeholder-shopping.jpg
         */

        private String name;
        private String productmodel;
        private String productname;
        private String price;
        private String quantity;
        private String image;
        private String imageuser;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProductmodel() {
            return productmodel;
        }

        public void setProductmodel(String productmodel) {
            this.productmodel = productmodel;
        }

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImageuser() {
            return imageuser;
        }

        public void setImageuser(String imageuser) {
            this.imageuser = imageuser;
        }
    }
}
