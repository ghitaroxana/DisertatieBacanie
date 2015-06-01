package com.example.roxanagh.test;

/**
 * Created by roxanagh on 3/31/2015.
 */
public enum UserRole {
    PRODUCER("Producător", 1),
    BUYER("Cumpărător", 2);


    private String stringValue;
    private int intValue;

    private UserRole(String toString, int value) {
        this.stringValue = toString;
        this.intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }


    private UserRole(String friendlyName) {
        this.stringValue = friendlyName;
    }

    public static UserRole getEnumForValue(int value) {
        switch (value) {
            case 1:
                return UserRole.PRODUCER;
            case 2:
                return UserRole.BUYER;
        }
        return UserRole.PRODUCER;
    }

    public int getIntValue() {
        return this.intValue;
    }
}