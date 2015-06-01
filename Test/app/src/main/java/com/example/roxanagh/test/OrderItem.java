package com.example.roxanagh.test;

/**
 * Created by Roxana on 5/2/2015.
 */
public class OrderItem {
    int quantity = 0;
    ProductItem product;

    public OrderItem(int quantity, ProductItem product) {
        this.quantity = quantity;
        this.product = product;
    }

    public OrderItem() {
        quantity = 0;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductItem getProduct() {
        return product;
    }

    public void setProduct(ProductItem product) {
        this.product = product;
    }
}
