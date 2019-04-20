package com.example.kersc.ezpay.Classes;

import android.graphics.drawable.Drawable;

public class item {
    private String name;
    private String category;
    private String price;
    private String description;
    private String barcode;
    private String seller_paypalID;
    private String location;
    private Drawable image;
    private String title;

    public item( String description, String barcode, String seller_paypalID,String price, String title,Drawable image) {
        this.price = price;
        this.description = description;
        this.barcode = barcode;
        this.seller_paypalID = seller_paypalID;
        this.image = image;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public item(Drawable image, String category, String price, String description, String barcode, String seller_paypalID, String location) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.barcode = barcode;
        this.seller_paypalID = seller_paypalID;
        this.location = location;
        this.image  = image;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public item(String name, String category, String price, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public item(String name, String category, String price, String description, String barcode) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.barcode = barcode;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSeller_paypalID() {
        return seller_paypalID;
    }

    public void setSeller_paypalID(String seller_paypalID) {
        this.seller_paypalID = seller_paypalID;
    }


}
