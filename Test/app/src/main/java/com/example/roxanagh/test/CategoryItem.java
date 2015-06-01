package com.example.roxanagh.test;

/**
 * Created by Roxana on 4/19/2015.
 */
public class CategoryItem {
    String bytePhoto;
    String name;
    String type;
    UserRole userRole;

    public CategoryItem(){}

    public CategoryItem(String bytePhoto,String name,String type,UserRole userRole){
        this.bytePhoto=bytePhoto;
        this.name=name;
        this.type=type;
        this.userRole=userRole;
    }

    public String getBytePhoto() {
        return bytePhoto;
    }

    public void setBytePhoto(String bytePhoto) {
        this.bytePhoto = bytePhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
