package com.example.roxanagh.test;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Roxana on 4/21/2015.
 */
public class ProductItem {
    Long id;
    String[] bytePhotos;
    String name;
    String description;
    Float price;
    Date dateFrom;
    Date dateTo;
    User seller;
    ProductCategory productCategory;
    Per per;
    Bitmap bmp;
    Bitmap[] bmpList;

    public ProductItem() {
    }


    public ProductItem(Long id,String[] bytePhotos, String name, String description, Float price, Date dateFrom, Date dateTo, User seller, ProductCategory productCategory, Per per) {
        this.id=id;
        this.bytePhotos = bytePhotos;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.seller = seller;
        this.productCategory = productCategory;
        this.per = per;
    }
    public ProductItem(Long id, String name, String description, Float price, Date dateFrom, Date dateTo, User seller, ProductCategory productCategory, Per per, Bitmap bitmap) {
        this.id=id;

        this.name = name;
        this.description = description;
        this.price = price;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
               this.seller = seller;
        this.productCategory = productCategory;
        this.per = per;
        this.bmp=bitmap;
    }
    public ProductItem( String name, String description, Float price, Date dateFrom, Date dateTo, User seller, ProductCategory productCategory, Per per, Bitmap[] bitmapList) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.seller = seller;
        this.productCategory = productCategory;
        this.per = per;
        this.bmpList=bitmapList;
    }
    public Bitmap getBmp() {
        return bmp;
    }

    public Bitmap[] getBmpList() {
        return bmpList;
    }

    public void setBmpList(Bitmap[] bmpList) {
        this.bmpList = bmpList;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public Per getPer() {
        return per;
    }

    public void setPer(Per per) {
        this.per = per;
    }

    public String[] getBytePhotos() {
        return bytePhotos;
    }

    public void setBytePhotos(String[] bytePhotos) {
        this.bytePhotos = bytePhotos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }


    public User getUser() {
        return seller;
    }

    public void setUser(User user) {
        this.seller = user;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductItem)) return false;

        ProductItem that = (ProductItem) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
