package com.backend.shop.DataTransferObject.Product;

import java.util.List;

/**
 * DTO for seller product list page.
 * Holds seller basic info and list of products.
 */
public class SellerProductsDTO {

    private Long sellerUserId;
    private String sellerUsername;
    private String sellerEmail;

    private List<ProductListItemDTO> products;

    // Getters and setters

    public Long getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(Long sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public List<ProductListItemDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductListItemDTO> products) {
        this.products = products;
    }
}
