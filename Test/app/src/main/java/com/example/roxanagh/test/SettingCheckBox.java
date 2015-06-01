package com.example.roxanagh.test;

import java.io.Serializable;

/**
 * Created by Roxana on 5/17/2015.
 */
public class SettingCheckBox implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_DESCRIPTION = "N/A";

    private final String description;
    private ProductCategory productCategory;

    private boolean checked;


    public SettingCheckBox(String description, ProductCategory productCategory) {
        this.description = description;
        this.productCategory = productCategory;
    }

    public String getDescription() {
        return description == null ? DEFAULT_DESCRIPTION : description;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public void setChecked(final boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return checked;
    }

    public SettingCheckBox(final String description) {
        this.description = description;
    }

}
