package com.hybris.turntobackoffice.model;

import de.hybris.platform.core.model.product.ProductModel;

public class HistoricalOrderFeed {

    private String orderId;
    private String orderDate;
    private String email;
    private String zip;
    private String firstname;
    private String lastname;

    private String sku;
    private String itemTitle;
    private String itemURL;
    private String itemLineId;
    private String price;

    public HistoricalOrderFeed() {
    }

    public HistoricalOrderFeed(ProductModel model, String homeURL) {
        this.setSku(model.getCode());
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public String getItemLineId() {
        return itemLineId;
    }

    public void setItemLineId(String itemLineId) {
        this.itemLineId = itemLineId;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
