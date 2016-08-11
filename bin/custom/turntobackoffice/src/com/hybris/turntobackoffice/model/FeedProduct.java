package com.hybris.turntobackoffice.model;

import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.lang.StringUtils;

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
    private String categorypathjson;

    public FeedProduct() {
    }

    public FeedProduct(ProductModel model, String homeURL) {
        this.setSku(StringUtils.defaultIfEmpty(model.getCode(),""));
        this.setCategory("");
        this.setCurrency("EUR");

        if (model.getPicture() != null) {
            this.setImageURL(homeURL + model.getPicture().getURL());
        }else{
            this.setImageURL("");
        }

        this.setTitle(StringUtils.defaultIfEmpty(model.getName(),""));
        this.setEan(StringUtils.defaultIfEmpty(model.getEan(),""));
        this.setBrand(StringUtils.defaultIfEmpty(model.getManufacturerName(),""));
        this.setMpn(StringUtils.defaultIfEmpty(model.getManufacturerAID(),""));
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

    public String getCategorypathjson() {
        return categorypathjson;
    }

    public void setCategorypathjson(String categorypathjson) {
        this.categorypathjson = categorypathjson;
    }
}

