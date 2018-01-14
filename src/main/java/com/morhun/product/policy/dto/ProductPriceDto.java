package com.morhun.product.policy.dto;

/**
 * Created by yarki on 12.11.2017.
 */
public class ProductPriceDto {
    private double price;
    private int productId;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
