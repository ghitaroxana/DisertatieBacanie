package com.example.roxanagh.test;

/**
 * Created by Roxana on 5/2/2015.
 */
public enum Per {
    KG("KG", 1),
    BUC("BUC", 2),
    LITTER("LITRU", 3);


    private String stringValue;
    private int intValue;

    Per(String stringValue, int intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }


    private Per(String friendlyName) {
        this.stringValue = friendlyName;
    }

    public static Per getEnumForValue(int value) {
        switch (value) {
            case 0:
                return Per.KG;
            case 1:
                return Per.BUC;
            case 2:
                return Per.LITTER;

        }
        return Per.KG;
    }

    public int getIntValue() {
        return this.intValue;
    }
}
