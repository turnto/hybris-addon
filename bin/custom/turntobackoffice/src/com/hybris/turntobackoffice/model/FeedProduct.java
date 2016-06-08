package com.hybris.turntobackoffice.model;

import de.hybris.platform.core.model.product.ProductModel;

public class FeedProduct {

    private String sku;
    private String imageURL;
    private String title;
    private String price;
    private String currency;
    private String itemURL;
    private String category;
    private String brand;
    private String mpn;
    private String ean;

    public FeedProduct() {
    }

    public FeedProduct(ProductModel model, String homeURL) {
        this.setSku(model.getCode());
        this.setCategory(model.getSegment());
        this.setCurrency("EUR");
        if (model.getPicture() != null) {
            this.setImageURL(homeURL + model.getPicture().getURL());
        }
        this.setTitle(model.getName());
        this.setEan(model.getEan());
        this.setBrand(model.getManufacturerName());
        this.setMpn(model.getManufacturerAID());
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }
}

