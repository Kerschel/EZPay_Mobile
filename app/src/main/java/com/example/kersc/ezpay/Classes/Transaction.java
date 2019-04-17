package com.example.kersc.ezpay.Classes;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Transaction {
    String customerID;
    Date timestamp;
    String category;
    String price;
    int transactionID; // randomly generated
    String barcode;
    String merchantId;


    public Transaction(String customerID, String category, String price,String barcode, String merchantID) {
        this.customerID = customerID;
        this.timestamp = Calendar.getInstance().getTime();
        this.category = category;
        this.price = price;
        this.transactionID = ThreadLocalRandom.current().nextInt(1, 10000000);
        this.barcode = barcode;
        this.merchantId = merchantID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
