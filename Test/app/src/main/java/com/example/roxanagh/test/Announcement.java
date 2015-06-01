package com.example.roxanagh.test;

/**
 * Created by Roxana on 5/24/2015.
 */
public class Announcement {
    Long id;
    ProductItem product;
    User user;

    String userDescription;
    String phoneNumbe;
    String email;
    County county;
    Locality locality;

    public Announcement(ProductItem product, User user, Detail detail) {
        this.product = product;
        this.user = user;
    }

    public Announcement() {
    }

    public Announcement(ProductItem product, User user, String userDescription, String phoneNumbe, String email, County county, Locality locality) {
        this.product = product;
        this.user = user;
        this.userDescription = userDescription;
        this.phoneNumbe = phoneNumbe;
        this.email = email;
        this.county = county;
        this.locality = locality;
    }

    public Announcement(Long id, ProductItem product, User user, String userDescription, String phoneNumbe, String email, County county, Locality locality) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.userDescription = userDescription;
        this.phoneNumbe = phoneNumbe;
        this.email = email;
        this.county = county;
        this.locality = locality;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumbe() {
        return phoneNumbe;
    }

    public void setPhoneNumbe(String phoneNumbe) {
        this.phoneNumbe = phoneNumbe;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductItem getProduct() {
        return product;
    }

    public void setProduct(ProductItem product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
