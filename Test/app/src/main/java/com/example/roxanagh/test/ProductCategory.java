package com.example.roxanagh.test;

/**
 * Created by roxanagh on 4/22/2015.
 */
public enum ProductCategory {
    VEGETABLES("Legume", 0), FRUITS("Fructe", 1), EGGS("Ouă", 2), MEAT("Carne", 3), BREAD("Panificație", 4), MILK("Lactate", 5), OTHERS("Altele", 6);

    private String stringValue;

    private ProductCategory(String toString, int value) {
        this.stringValue = toString;
        this.intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }


    private ProductCategory(String friendlyName) {
        this.stringValue = friendlyName;
    }


    private int intValue;

    ProductCategory(int value) {
        this.intValue = value;
    }

    public static ProductCategory getEnumForValue(int value) {
        switch (value) {
            case 0:
                return ProductCategory.VEGETABLES;
            case 1:
                return ProductCategory.FRUITS;
            case 2:
                return ProductCategory.EGGS;
            case 3:
                return ProductCategory.MEAT;
            case 4:
                return ProductCategory.BREAD;
            case 5:
                return ProductCategory.MILK;
            case 6:
                return ProductCategory.OTHERS;
        }
        return ProductCategory.OTHERS;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}
