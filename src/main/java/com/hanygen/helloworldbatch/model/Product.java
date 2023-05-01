package com.hanygen.helloworldbatch.model;

import java.math.BigDecimal;

public class Product {
    private Integer productId;
    private String productName;
    private String ProductDesc;
    private BigDecimal pricce;
    private Integer unit;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        ProductDesc = productDesc;
    }

    public BigDecimal getPricce() {
        return pricce;
    }

    public void setPricce(BigDecimal pricce) {
        this.pricce = pricce;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", ProductDesc='" + ProductDesc + '\'' +
                ", pricce=" + pricce +
                ", unit=" + unit +
                '}';
    }
}
