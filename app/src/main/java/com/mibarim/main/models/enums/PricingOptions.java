package com.mibarim.main.models.enums;

/**
 * Created by Hamed on 3/8/2016.
 */
public enum PricingOptions {
    NoMatter("NoMatter", 1),
    MinMax("MinMax", 2),
    Free("Free", 3);


    private String stringValue;
    private int intValue;

    private PricingOptions(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
