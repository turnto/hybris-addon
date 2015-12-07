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

    public FeedProduct() {
    }

    public FeedProduct(ProductModel model, String homeURL) {
        this.setSku(model.getCode());
        this.setCategory(model.getEan());
        this.setCurrency("EUR");
        if (model.getPicture() != null) {
            this.setImageURL(homeURL + model.getPicture().getURL());
        }
        this.setTitle(model.getName());
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
}
